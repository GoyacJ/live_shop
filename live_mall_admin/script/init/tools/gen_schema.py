#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
live_mall_admin —— 从实体类反向生成完整初始化脚本。

数据来源：
  1. 所有带 @Table 的 Java 实体（88 个类 / 79 张表），推导列名、类型、注释、可空性
  2. script/snowy.sql（2025-02-24 Navicat 导出，32 张表）—— 这些表的列定义以导出为准，
     因为那是真实建表结果，比推导更可信
  3. script/update/update-2025-04-09.sql —— 菜单/字典增量

产出：
  script/init/01_schema.sql        全量表结构（79 张表）
  script/init/02_base_data.sql     系统基础数据（菜单、字典、配置、角色、用户、机构）
  script/init/03_business_data.sql 业务示例数据（商品、直播、内容等）
  script/init/04_patch_2025-04-09.sql  4 月补丁数据（菜单/字典/演示行）
  script/init/REPORT.md            生成报告：推导项、冲突项、被跳过的字段

用法：python3 script/init/tools/gen_schema.py
"""

import os
import re
import sys
import collections

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.abspath(os.path.join(HERE, '..', '..', '..'))
OUT = os.path.dirname(HERE)

DUMP_PATH = os.path.join(ROOT, 'script', 'snowy.sql')
PATCH_PATH = os.path.join(ROOT, 'script', 'update', 'update-2025-04-09.sql')

# 运行时日志表，种子数据里剔除
SKIP_DATA_TABLES = {'dev_log'}

# 系统表 / 业务表划分，决定落哪个 data 文件
SYSTEM_PREFIX = ('sys_', 'mobile_', 'dev_', 'gen_', 'auth_', 'client_')

report = collections.OrderedDict()
warn = []


def warnf(msg):
    warn.append(msg)


# ---------------------------------------------------------------- Java 解析

JAVA_SCALARS = {
    'String': ('varchar(255)', 'str'),
    'Integer': ('int', 'int'), 'int': ('int', 'int'),
    'Long': ('bigint', 'int'), 'long': ('bigint', 'int'),
    'Short': ('smallint', 'int'), 'short': ('smallint', 'int'),
    'BigDecimal': ('decimal(12,2)', 'dec'),
    'BigInteger': ('bigint', 'int'),
    'Boolean': ('tinyint(1)', 'int'), 'boolean': ('tinyint(1)', 'int'),
    'Double': ('double', 'dec'), 'double': ('double', 'dec'),
    'Float': ('float', 'dec'), 'float': ('float', 'dec'),
    'Date': ('datetime', 'date'),
    'LocalDateTime': ('datetime', 'date'),
    'LocalDate': ('date', 'date'),
    'LocalTime': ('time', 'date'),
}

# 列名 -> 精确类型（实体推导时生效，导出里有的以导出为准）
COLUMN_TYPE_HINTS = {
    'EXT_JSON': 'longtext',
    'ATTACH': 'text',
    'CONTENT': 'text',
    'DETAIL': 'text',
    'DESCRIPTION': 'text',
    'INTRODUCE': 'text',
    'REMARK': 'varchar(500)',
    'AVATAR': 'varchar(1000)',
    'COVER': 'varchar(1000)',
    'URL': 'varchar(1000)',
    'PIC': 'varchar(1000)',
    'IMAGE': 'varchar(1000)',
    'IMG': 'varchar(1000)',
    'PATH': 'varchar(1000)',
    'SRC': 'varchar(1000)',
    'CODE': 'varchar(100)',
    'ACCOUNT': 'varchar(100)',
    'EMAIL': 'varchar(100)',
    'MOBILE': 'varchar(20)',
    'PHONE': 'varchar(20)',
    'IP': 'varchar(64)',
    'ORDER_NUMBER': 'varchar(64)',
    'PASSWORD': 'varchar(255)',
    'SALT': 'varchar(255)',
    'TOKEN': 'varchar(500)',
    'PID': 'varchar(20)',
    'CREATE_USER': 'varchar(20)',
    'UPDATE_USER': 'varchar(20)',
}

MONEY_SUFFIX = ('_FEE', '_PRICE', '_AMOUNT', '_MONEY', '_BALANCE', '_COMMISSION',
                '_INTEGRAL', '_SCORE', '_POINTS')

COL_RE = re.compile(r'^`([^`]+)`\s+(.*)$')


def split_column(line):
    """把导出的列定义拆成 name / type / notnull / default / comment。"""
    m = COL_RE.match(line)
    if not m:
        return None
    name, rest = m.group(1), m.group(2).strip().rstrip(',')

    comment = ''
    cm = re.search(r"\s+COMMENT\s+'(.*)'\s*$", rest)
    if cm:
        comment = cm.group(1)
        rest = rest[:cm.start()]
    else:
        cm = re.search(r"\s+COMMENT\s+'(.*)'\s*,", rest)
        if cm:
            comment = cm.group(1)
            rest = rest[:cm.start()] + rest[cm.end():]

    notnull = 'NOT NULL' in rest
    default = ''
    dm = re.search(r'\s+DEFAULT\s+(.+)$', rest)
    if dm:
        default = dm.group(1).strip()
        rest = rest[:dm.start()]

    ctype = rest
    ctype = re.sub(r'\s+CHARACTER SET\s+\w+', ' ', ctype)
    ctype = re.sub(r'\s+COLLATE\s+\w+', ' ', ctype)
    ctype = re.sub(r'\s+NOT NULL\b', ' ', ctype)
    ctype = re.sub(r'\s+\bNULL\b', ' ', ctype)
    ctype = re.sub(r'\s+AUTO_INCREMENT\b', ' ', ctype)
    ctype = re.sub(r'\s+ON UPDATE[\w\s()]+$', ' ', ctype)
    ctype = re.sub(r'\s+', ' ', ctype).strip()
    return {'name': name, 'type': ctype, 'notnull': notnull, 'default': default, 'comment': comment}


def clean_comment(text):
    return (text or '').replace('\\', '\\\\').replace("'", "''").strip()


def strip_comments(line):
    return line.strip()


def camel_to_upper_snake(name):
    s1 = re.sub(r'([A-Z]+)([A-Z][a-z])', r'\1_\2', name)
    s2 = re.sub(r'([a-z\d])([A-Z])', r'\1_\2', s1)
    return s2.upper()


def parse_java_fields(path):
    """返回 [(name, javaType, comment, isId, ignored, isLogicDelete)]"""
    with open(path, encoding='utf-8') as fh:
        src = fh.read()

    body_start = src.find('class ')
    body = src[body_start:] if body_start >= 0 else src

    fields = []
    annotations = []
    comment = ''
    in_doc = False
    doc_buf = []
    i = 0
    lines = body.split('\n')
    while i < len(lines):
        raw = lines[i]
        line = strip_comments(raw)
        i += 1
        if in_doc:
            if '*/' in line:
                doc_buf.append(line.split('*/')[0])
                comment = ' '.join(x.strip().lstrip('*').strip() for x in doc_buf).strip()
                in_doc = False
                doc_buf = []
            else:
                doc_buf.append(line)
            continue
        if not line:
            continue
        if line.startswith('/**'):
            rest = line[3:]
            if '*/' in rest:
                inline = rest.split('*/')[0]
                comment = ' '.join(x.strip().lstrip('*').strip() for x in inline.split('\n')).strip()
            else:
                in_doc = True
                doc_buf = []
            continue
        if line.startswith('/*') or line.startswith('//'):
            continue
        if line.startswith('@'):
            buf = line
            while buf.count('(') > buf.count(')') and i < len(lines):
                buf += ' ' + lines[i].strip()
                i += 1
            annotations.append(buf)
            continue
        m = re.match(r'^(?:private|protected|public)\s+([A-Za-z0-9_.<>,?\[\]\s]+?)\s+(\w+)\s*(?:=[^;]*)?;', line)
        if m and '(' not in m.group(1):
            jtype, jname = m.group(1).strip(), m.group(2)
            ann = ' '.join(annotations)
            if 'static' in jtype or ' final ' in (' ' + jtype + ' '):
                annotations, comment = [], ''
                continue
            fields.append({
                'name': jname,
                'type': jtype,
                'comment': comment,
                'isId': '@Id' in ann,
                'ignored': re.search(r'@Column\([^)]*ignore\s*=\s*true', ann) is not None or '@Transient' in ann,
                'logicDelete': re.search(r'isLogicDelete\s*=\s*true', ann) is not None,
            })
            annotations, comment = [], ''
            continue
        # 方法体 / 其他行，清空挂起的注解
        if not line.startswith('}'):
            annotations, comment = [], ''
    return fields


def read_common_entity():
    for base, _, files in os.walk(ROOT):
        if 'CommonEntity.java' in files:
            path = os.path.join(base, 'CommonEntity.java')
            return parse_java_fields(path)
    warnf('未找到 CommonEntity.java，通用字段按惯例补齐')
    return [
        {'name': 'deleteFlag', 'type': 'String', 'comment': '删除标志', 'isId': False, 'ignored': False, 'logicDelete': True},
        {'name': 'createTime', 'type': 'Date', 'comment': '创建时间', 'isId': False, 'ignored': False, 'logicDelete': False},
        {'name': 'createUser', 'type': 'String', 'comment': '创建人', 'isId': False, 'ignored': False, 'logicDelete': False},
        {'name': 'updateTime', 'type': 'Date', 'comment': '修改时间', 'isId': False, 'ignored': False, 'logicDelete': False},
        {'name': 'updateUser', 'type': 'String', 'comment': '修改人', 'isId': False, 'ignored': False, 'logicDelete': False},
    ]


def collect_entities():
    tables = collections.OrderedDict()   # table -> {'fields': [...], 'sources': [...], 'comment': str}
    class_fields = {}
    for base, dirs, files in os.walk(ROOT):
        dirs[:] = [d for d in dirs if d not in ('.git', 'target', 'node_modules')]
        for fn in files:
            if not fn.endswith('.java'):
                continue
            path = os.path.join(base, fn)
            try:
                with open(path, encoding='utf-8') as fh:
                    src = fh.read()
            except UnicodeDecodeError:
                continue
            if '@Table(' not in src:
                continue
            m = re.search(r'@Table\(\s*(?:value\s*=\s*)?"([^"]+)"', src)
            if not m:
                continue
            table = m.group(1).lower()
            cm = re.search(r'public\s+class\s+(\w+)(?:\s+extends\s+([\w<>.]+))?', src)
            cls = cm.group(1) if cm else fn[:-5]
            parent = cm.group(2).split('<')[0].split('.')[-1] if cm and cm.group(2) else None
            doc = ''
            dm = re.search(r'/\*\*(.*?)\*/\s*(?:@\w+[^\n]*\n\s*)*@Table', src, re.S)
            if dm:
                parts = []
                for x in dm.group(1).split('\n'):
                    x = x.strip().lstrip('*').strip()
                    if x.startswith('@'):
                        break
                    if x:
                        parts.append(x)
                doc = ' '.join(parts).strip()
            fields = parse_java_fields(path)
            class_fields[cls] = fields
            entry = tables.setdefault(table, {'fields': [], 'sources': [], 'comment': doc, 'parents': []})
            entry['sources'].append(cls)
            if parent:
                entry['parents'].append(parent)
            entry['fields'].extend(fields)
    return tables, class_fields


def resolve_type(field, table):
    jtype = field['type'].strip()
    col = camel_to_upper_snake(field['name'])
    if jtype in JAVA_SCALARS:
        sql_type, kind = JAVA_SCALARS[jtype]
        if jtype == 'String':
            sql_type = COLUMN_TYPE_HINTS.get(col, 'varchar(255)')
            if col.endswith('_ID') or col == 'ID':
                sql_type = 'varchar(20)'
        if kind == 'dec' and jtype == 'BigDecimal' and col.endswith(MONEY_SUFFIX):
            sql_type = 'decimal(12,2)'
        return sql_type, None
    if jtype.endswith('[]'):
        return 'longblob', 'byte[] 按 longblob 处理'
    if re.match(r'^(List|Set|Map|Collection|Iterable|Optional)\b', jtype):
        return None, '集合类型非数据库列'
    # 未识别的类：枚举或自定义类型，按字符串存
    if jtype.endswith('Enum'):
        return 'varchar(50)', '枚举按 varchar(50)'
    warnf('表 %s 字段 %s 类型 %s 未识别，按 varchar(50) 处理' % (table, field['name'], jtype))
    return 'varchar(50)', '未识别类型按 varchar(50)'


# ---------------------------------------------------------------- 导出解析

def parse_dump():
    with open(DUMP_PATH, encoding='utf-8') as fh:
        src = fh.read()
    tables = collections.OrderedDict()
    for m in re.finditer(r'CREATE TABLE `([^`]+)`\s*\((.*?)\n\) ENGINE', src, re.S):
        name = m.group(1).lower()
        cols = []
        pk = []
        for line in m.group(2).split('\n'):
            line = line.strip().rstrip(',')
            if not line:
                continue
            if line.startswith('`'):
                parsed = split_column(line)
                if not parsed:
                    warnf('无法解析导出列定义：%s.%s' % (name, line[:60]))
                    continue
                cols.append(parsed)
            elif line.startswith('PRIMARY KEY'):
                pk = re.findall(r'`([^`]+)`', line)
        tables[name] = {'columns': cols, 'pk': pk,
                        'comment': (re.search(r"COMMENT = '([^']*)'", m.group(0)) or [None, ''])[1]}
    return tables


def split_values(tuple_text):
    """把 (v1, v2, ...) 拆成顶层值列表，尊重引号与反斜杠转义。"""
    vals, cur, i, n = [], [], 0, len(tuple_text)
    depth = 0
    quote = None
    while i < n:
        c = tuple_text[i]
        if quote:
            cur.append(c)
            if c == '\\':
                i += 1
                if i < n:
                    cur.append(tuple_text[i])
            elif c == quote:
                quote = None
        elif c in ("'", '"'):
            quote = c
            cur.append(c)
        elif c == '(':
            depth += 1
            if depth == 1:
                cur = []
            else:
                cur.append(c)
        elif c == ')':
            depth -= 1
            if depth == 0:
                vals.append(''.join(cur).strip())
                cur = []
            else:
                cur.append(c)
        elif c == ',' and depth == 1:
            vals.append(''.join(cur).strip())
            cur = []
        else:
            cur.append(c)
        i += 1
    return vals


INSERT_RE = re.compile(
    r'^INSERT(?P<ignore>\s+IGNORE)?\s+INTO\s+'
    r'(?P<table>(?:`[^`]+`|\w+)(?:\.(?:`[^`]+`|\w+))?)\s*'
    r'(?P<cols>\([^)]*\))?\s*VALUES\s*(?P<vals>\(.*\))\s*;?\s*$', re.I)


def parse_inserts(path):
    """返回 [(table, 列名列表或 None, [值...]), ...]

    带列名的 INSERT 必须按列名对齐，否则值会错位（补丁文件就是这种）。
    """
    out = []
    with open(path, encoding='utf-8') as fh:
        for line in fh:
            line = line.rstrip('\n')
            m = INSERT_RE.match(line)
            if not m:
                if line.upper().startswith('INSERT'):
                    warnf('无法解析的 INSERT，已跳过：%s' % line[:80])
                continue
            raw_table = m.group('table')
            table = re.sub(r'`', '', raw_table).split('.')[-1].lower()
            cols = None
            if m.group('cols'):
                cols = [c.strip().strip('`').upper() for c in m.group('cols').strip('()').split(',')]
            out.append((table, cols, split_values(m.group('vals'))))
    return out


def parse_updates(path):
    """补丁文件里的 UPDATE 语句，去掉库名前缀。"""
    out = []
    with open(path, encoding='utf-8') as fh:
        for line in fh:
            line = line.strip()
            if re.match(r'^UPDATE\s+`?\w+`?\.', line, re.I):
                line = re.sub(r'^(UPDATE\s+)`?\w+`?\.', r'\1', line, flags=re.I)
                out.append(line)
    return out


# ---------------------------------------------------------------- 组装

def build():
    common = read_common_entity()
    entities, class_fields = collect_entities()
    dump = parse_dump()

    tables = collections.OrderedDict()   # table -> {'columns': [...], 'pk': [...], 'comment': str, 'from_dump': bool}

    # 1. 导出里的表先落位，列序保持不变，保证位置式 INSERT 兼容
    for tname, tdef in dump.items():
        cols = []
        for c in tdef['columns']:
            cols.append({'name': c['name'].upper(), 'type': c['type'], 'comment': c['comment'],
                         'notnull': c['notnull'], 'default': c['default'], 'origin': 'dump'})
        tables[tname] = {'columns': cols, 'pk': tdef['pk'], 'comment': tdef['comment'], 'from_dump': True}

    # 2. 实体表
    for tname, tdef in entities.items():
        inherited = []
        for p in tdef['parents']:
            if p in class_fields and p not in tdef['sources']:
                inherited.extend(class_fields[p])
        if any(p in ('CommonEntity',) for p in tdef['parents']):
            inherited.extend(common)
        fields = tdef['fields'] + inherited

        seen = set()
        merged = []
        for f in fields:
            if f['ignored'] or f['name'] in seen:
                continue
            if re.match(r'^(List|Set|Map|Collection)\b', f['type'].strip()):
                warnf('表 %s 跳过集合字段 %s' % (tname, f['name']))
                continue
            seen.add(f['name'])
            merged.append(f)

        target = tables.get(tname)
        if target is None:
            target = {'columns': [], 'pk': [], 'comment': tdef['comment'], 'from_dump': False}
            tables[tname] = target

        existing = {c['name'].upper(): c for c in target['columns']}
        for f in merged:
            col = camel_to_upper_snake(f['name'])
            sql_type, note = resolve_type(f, tname)
            if not sql_type:
                continue
            if col in existing:
                # 导出里已有：保留导出类型，不一致只记录
                cur = existing[col]
                if normalize(cur['type']) != normalize(sql_type):
                    warnf('表 %s 列 %s 导出为 %s，实体推导 %s（保留导出定义）' % (tname, col, cur['type'], sql_type))
                cur['entity_type'] = sql_type
                continue
            target['columns'].append({'name': col, 'type': sql_type, 'comment': f['comment'],
                                      'notnull': f['isId'], 'default': '',
                                      'origin': 'entity', 'is_id': f['isId'],
                                      'logic_delete': f['logicDelete']})
            if f['isId']:
                target['pk'] = [col]
        if not target['pk']:
            ids = [c['name'] for c in target['columns'] if c.get('is_id')]
            target['pk'] = ids[:1]
        if not target['pk']:
            warnf('表 %s 没有 @Id 字段，未生成主键' % tname)

    # 导出里存在但代码里没有的实体（保留原样）
    for tname in dump:
        if tname not in entities:
            warnf('表 %s 只在导出中存在，代码无对应实体，原样保留' % tname)

    return tables


def normalize(t):
    """把类型压成“族”，避免 int(11) vs int、varchar(255) vs varchar(100) 这类噪音进报告。"""
    t = re.sub(r'\s+', ' ', t.strip().lower())
    for fam in ('longtext', 'mediumtext', 'text', 'longblob', 'mediumblob', 'blob'):
        if fam in t:
            return fam
    if t.startswith('bigint'):
        return 'bigint'
    if t.startswith('smallint'):
        return 'smallint'
    if t.startswith('tinyint'):
        return 'boolean'
    if t.startswith('int') or t.startswith('integer'):
        return 'int'
    if t.startswith('decimal') or t.startswith('numeric'):
        return 'decimal'
    if t.startswith('double') or t.startswith('float'):
        return 'float'
    if 'char' in t:
        return 'varchar'
    if t.startswith('datetime') or t.startswith('timestamp'):
        return 'datetime'
    if t.startswith('date'):
        return 'date'
    if t.startswith('time'):
        return 'time'
    return t


# ---------------------------------------------------------------- 索引

ID_INDEX_SKIP = {'CREATE_USER', 'UPDATE_USER', 'UPDATE_TIME', 'CREATE_TIME'}
LOOKUP_COLUMNS = {'OPEN_ID', 'OPENID', 'UNION_ID', 'TOKEN', 'MOBILE', 'PHONE',
                  'ORDER_NUMBER', 'THIRD_ID', 'CODE', 'ACCOUNT'}
UNIQUE_COLUMNS = {('SYS_USER', 'ACCOUNT')}


def build_indexes(table, tdef):
    idx = []
    pk = tdef['pk']
    colnames = [c['name'] for c in tdef['columns']]
    for c in tdef['columns']:
        name = c['name']
        if name in pk:
            continue
        if (table.upper(), name) in {(t.upper(), c2) for t, c2 in UNIQUE_COLUMNS}:
            idx.append(('UNIQUE KEY `uk_%s_%s` (`%s`)' % (table, name.lower(), name)))
            continue
        if (name.endswith('_ID') and name not in ID_INDEX_SKIP) or name in LOOKUP_COLUMNS:
            idx.append('KEY `idx_%s_%s` (`%s`)' % (table, name.lower(), name))
    return idx


def emit_schema(tables):
    out = []
    out.append('-- =============================================================')
    out.append('-- live_mall_admin 数据库初始化 —— 01_schema.sql')
    out.append('-- 由 script/init/tools/gen_schema.py 从实体类 + script/snowy.sql 生成')
    out.append('-- 表数量：%d' % len(tables))
    out.append('-- 适用：MySQL 5.7 / 8.0，字符集 utf8mb4')
    out.append('-- 说明：导出中已有的表保留原始列定义（真实建表结果），')
    out.append('--       其余表按实体推导；推导明细见 REPORT.md')
    out.append('-- =============================================================')
    out.append('')
    out.append('SET NAMES utf8mb4;')
    out.append('SET FOREIGN_KEY_CHECKS = 0;')
    out.append('')

    for tname, tdef in tables.items():
        comment = clean_comment(tdef['comment'])
        cols = tdef['columns']
        pk = tdef['pk']
        idx = build_indexes(tname, tdef)
        out.append('-- ----------------------------')
        out.append('-- %s  %s' % (tname, comment))
        out.append('-- ----------------------------')
        out.append('DROP TABLE IF EXISTS `%s`;' % tname)
        lines = []
        auto_pk = None
        if len(pk) == 1:
            pkcol = next((c for c in cols if c['name'] == pk[0]), None)
            if pkcol is not None:
                fam = normalize(pkcol['type'])
                if fam in ('int', 'bigint', 'smallint'):
                    auto_pk = pkcol['name']

        for c in cols:
            piece = '  `%s` %s' % (c['name'], c['type'])
            if c['name'] == auto_pk:
                piece += ' AUTO_INCREMENT'
            if c['notnull'] or c['name'] in pk:
                piece += ' NOT NULL'
            else:
                piece += ' NULL'
            if c.get('default') and c['default'].upper() != 'NULL':
                piece += ' DEFAULT %s' % c['default']
            if c['comment']:
                piece += " COMMENT '%s'" % clean_comment(c['comment'])
            lines.append(piece)
        if pk:
            lines.append('  PRIMARY KEY (%s) USING BTREE' % ', '.join('`%s`' % p for p in pk))
        lines.extend('  ' + x for x in idx)
        out.append('CREATE TABLE `%s` (' % tname)
        out.append(',\n'.join(lines))
        tail = ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci"
        if comment:
            tail += " COMMENT = '%s'" % comment
        tail += ' ROW_FORMAT = DYNAMIC;'
        out.append(tail)
        out.append('')

    out.append('SET FOREIGN_KEY_CHECKS = 1;')
    return '\n'.join(out) + '\n'


def emit_data(tables, inserts, title, header_note):
    """按导出列序生成带列名的 INSERT，避免列增减后位置错位。"""
    out = [header_note, '', 'SET NAMES utf8mb4;', '']
    grouped = collections.OrderedDict()
    skipped_tables = []
    for t, cols, vals in inserts:
        if t in SKIP_DATA_TABLES:
            continue
        if t not in tables:
            skipped_tables.append(t)
            continue
        grouped.setdefault((t, tuple(cols) if cols else None), []).append(vals)

    for (t, cols), rows in grouped.items():
        schema_cols = [c['name'] for c in tables[t]['columns']]
        colnames = list(cols) if cols else schema_cols
        fixed_rows = []
        for vals in rows:
            if len(vals) > len(colnames):
                warnf('表 %s 的 INSERT 值(%d)多于列(%d)，已截断' % (t, len(vals), len(colnames)))
                vals = vals[:len(colnames)]
            elif len(vals) < len(colnames):
                warnf('表 %s 的 INSERT 值(%d)少于列(%d)，缺的补 NULL' % (t, len(vals), len(colnames)))
                vals = vals + ['NULL'] * (len(colnames) - len(vals))
            fixed_rows.append(vals)
        out.append('-- %d 行' % len(fixed_rows))
        for vals in fixed_rows:
            out.append('INSERT INTO `%s` (%s) VALUES (%s);' % (
                t, ', '.join('`%s`' % c for c in colnames), ', '.join(vals)))
        out.append('')
    if skipped_tables:
        warnf('%s 中这些表不在结构里，已跳过：%s' % (title, ', '.join(sorted(set(skipped_tables)))))
    return '\n'.join(out)


def main():
    tables = build()

    with open(os.path.join(OUT, '01_schema.sql'), 'w', encoding='utf-8') as fh:
        fh.write(emit_schema(tables))

    inserts = parse_inserts(DUMP_PATH)
    system_rows = [row for row in inserts if row[0].startswith(SYSTEM_PREFIX)]
    business_rows = [row for row in inserts if not row[0].startswith(SYSTEM_PREFIX)]

    with open(os.path.join(OUT, '02_base_data.sql'), 'w', encoding='utf-8') as fh:
        fh.write(emit_data(tables, system_rows, '系统基础数据',
                           '-- =============================================================\n'
                           '-- live_mall_admin 数据库初始化 —— 02_base_data.sql\n'
                           '-- 系统表基础数据：机构、角色、用户、菜单、按钮、字典、配置、模块\n'
                           '-- 来源：script/snowy.sql（2025-02-24 导出），已转成带列名的 INSERT\n'
                           '-- ============================================================='))
    with open(os.path.join(OUT, '03_business_data.sql'), 'w', encoding='utf-8') as fh:
        fh.write(emit_data(tables, business_rows, '业务示例数据',
                           '-- =============================================================\n'
                           '-- live_mall_admin 数据库初始化 —— 03_business_data.sql\n'
                           '-- 业务表示例数据：商品、轮播、公告、用户、积分等（早期联调数据）\n'
                           '-- 不想带演示数据可以跳过本文件\n'
                           '-- ============================================================='))

    patch_inserts = parse_inserts(PATCH_PATH)
    patch_updates = parse_updates(PATCH_PATH)
    patch_out = ['-- =============================================================',
                 '-- live_mall_admin 数据库初始化 —— 04_patch_2025-04-09.sql',
                 '-- 来源：script/update/update-2025-04-09.sql',
                 '-- 内容：积分商城菜单、订单状态字典、营销活动字段等增量',
                 '-- 注意：原文件的 ALTER TABLE 已并入 01_schema.sql，这里只保留数据',
                 '-- =============================================================',
                 '', 'SET NAMES utf8mb4;', '']
    for t, cols, vals in patch_inserts:
        if t not in tables:
            warnf('补丁表 %s 不在结构里，已跳过' % t)
            continue
        schema_cols = [c['name'] for c in tables[t]['columns']]
        colnames = list(cols) if cols else schema_cols
        unknown = [c for c in colnames if c not in schema_cols]
        if unknown:
            warnf('补丁表 %s 引用了结构里没有的列 %s，已跳过该行' % (t, unknown))
            continue
        if len(vals) != len(colnames):
            warnf('补丁表 %s 值数量 %d 与列数量 %d 不一致，已跳过该行' % (t, len(vals), len(colnames)))
            continue
        patch_out.append('INSERT IGNORE INTO `%s` (%s) VALUES (%s);' % (
            t, ', '.join('`%s`' % c for c in colnames), ', '.join(vals)))
    patch_out.append('')
    patch_out.extend(patch_updates)
    with open(os.path.join(OUT, '04_patch_2025-04-09.sql'), 'w', encoding='utf-8') as fh:
        fh.write('\n'.join(patch_out) + '\n')

    # 报告
    enum_warn, type_warn, other_warn = [], [], []
    for w in dict.fromkeys(warn):
        if '未识别' in w or '枚举按' in w:
            enum_warn.append(w)
        elif '导出为' in w:
            type_warn.append(w)
        else:
            other_warn.append(w)

    L = ['# 初始化脚本生成报告', '',
         '由 `script/init/tools/gen_schema.py` 生成，改了实体后重跑即可覆盖。', '',
         '## 统计', '',
         '| 项 | 数量 |', '| --- | --- |',
         '| 实体表总数 | %d |' % len(tables),
         '| 沿用 `script/snowy.sql` 原始列定义 | %d |' % sum(1 for t in tables.values() if t['from_dump']),
         '| 按实体推导建表 | %d |' % sum(1 for t in tables.values() if not t['from_dump']),
         '| 总列数 | %d |' % len(inferred) if False else '| 总列数 | %d |' % sum(len(d['columns']) for d in tables.values()),
         '',
         '## 表清单', '',
         '| 表 | 列数 | 主键 | 定义来源 |', '| --- | --- | --- | --- |']
    for t, d in tables.items():
        L.append('| `%s` | %d | %s | %s |' % (
            t, len(d['columns']), ', '.join(d['pk']) or '无',
            '导出' if d['from_dump'] else '实体推导'))

    L += ['', '## 待复核 1：导出与实体推导的类型不一致（%d 处）' % len(type_warn), '',
          '这些表在 `script/snowy.sql` 里有真实定义，脚本保留导出类型，只是记录下来。', '']
    L += ['- %s' % w for w in type_warn] or ['- 无']

    L += ['', '## 待复核 2：枚举 / 自定义类型（%d 处）' % len(enum_warn), '',
          '代码里是 Java 枚举，库里按 `varchar(50)` 存。MyBatis-Flex 默认存枚举的 `name()`；'
          '如果某个枚举用了 `@EnumValue` 落了别的值，报告里这几列要改成对应类型。', '']
    L += ['- %s' % w for w in enum_warn] or ['- 无']

    L += ['', '## 其他告警（%d 处）' % len(other_warn), '']
    L += ['- %s' % w for w in other_warn] or ['- 无']

    with open(os.path.join(OUT, 'REPORT.md'), 'w', encoding='utf-8') as fh:
        fh.write('\n'.join(L) + '\n')

    print('tables: %d (dump %d / inferred %d)' % (
        len(tables), sum(1 for t in tables.values() if t['from_dump']),
        sum(1 for t in tables.values() if not t['from_dump'])))
    print('warnings: %d' % len(warn))


if __name__ == '__main__':
    main()
