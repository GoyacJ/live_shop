#!/usr/bin/env bash
# =============================================================
# live_mall_admin 一键建库
#
#   MYSQL_HOST=127.0.0.1 MYSQL_PORT=3306 MYSQL_USER=root \
#   MYSQL_PASSWORD=xxx MYSQL_DB=live \
#   ./script/init/run_init.sh            # 带演示数据
#   ./script/init/run_init.sh --no-demo  # 只要系统基础数据
#
# --no-demo 用于生产：跳过 03_business_data.sql（早期联调的商品、公告、测试用户）
#                     和 05_demo_mobile_data.sql（移动端完整演示数据）
# =============================================================
set -euo pipefail

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

MYSQL_HOST="${MYSQL_HOST:-127.0.0.1}"
MYSQL_PORT="${MYSQL_PORT:-3306}"
MYSQL_USER="${MYSQL_USER:-root}"
MYSQL_PASSWORD="${MYSQL_PASSWORD:-}"
MYSQL_DB="${MYSQL_DB:-live}"

WITH_DEMO=1
for arg in "$@"; do
  case "$arg" in
    --no-demo) WITH_DEMO=0 ;;
    -h|--help) sed -n '2,12p' "$0"; exit 0 ;;
    *) echo "未知参数: $arg" >&2; exit 2 ;;
  esac
done

run_sql() {
  local file="$1"
  echo ">> $(basename "$file")"
  MYSQL_PWD="$MYSQL_PASSWORD" mysql \
    --host="$MYSQL_HOST" --port="$MYSQL_PORT" --user="$MYSQL_USER" \
    --default-character-set=utf8mb4 --show-warnings \
    "$MYSQL_DB" < "$file"
}

echo ">> 目标: ${MYSQL_USER}@${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DB}"
MYSQL_PWD="$MYSQL_PASSWORD" mysql \
  --host="$MYSQL_HOST" --port="$MYSQL_PORT" --user="$MYSQL_USER" \
  --default-character-set=utf8mb4 \
  -e "CREATE DATABASE IF NOT EXISTS \`${MYSQL_DB}\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"

run_sql "$HERE/01_schema.sql"
run_sql "$HERE/02_base_data.sql"
if [ "$WITH_DEMO" -eq 1 ]; then
  run_sql "$HERE/03_business_data.sql"
  run_sql "$HERE/05_demo_mobile_data.sql"
else
  echo ">> 跳过 03_business_data.sql / 05_demo_mobile_data.sql (--no-demo)"
fi
run_sql "$HERE/04_patch_2025-04-09.sql"

echo
echo ">> 校验"
run_sql "$HERE/99_verify.sql"
echo ">> 完成"
