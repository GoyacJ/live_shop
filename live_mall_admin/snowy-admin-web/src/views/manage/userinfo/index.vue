<template>
    <a-card :bordered="false">
        <a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form mb-4">
            <a-row :gutter="24">
                <a-col :span="6">
                    <a-form-item label="手机号" name="mobile">
                        <a-input v-model:value="searchFormState.mobile" placeholder="请输入手机号" />
                    </a-form-item>
                </a-col>
                <a-col :span="6">
                    <a-form-item label="等级id" name="levelId">
                        <a-input v-model:value="searchFormState.levelId" placeholder="请输入等级id" />
                    </a-form-item>
                </a-col>
                <a-col :span="6">
                    <a-form-item label="昵称" name="nickname">
                        <a-input v-model:value="searchFormState.nickname" placeholder="请输入昵称" />
                    </a-form-item>
                </a-col>
                <template v-if="advanced">
                    <a-col :span="6">
                        <a-form-item label="创建时间" name="createTime">
                            <a-range-picker v-model:value="searchFormState.createTime" show-time />
                        </a-form-item>
                    </a-col>
                </template>
                <a-col :span="6">
                    <a-button type="primary" @click="table.refresh(true)">查询</a-button>
                    <a-button style="margin: 0 8px" @click="() => searchFormRef.resetFields()">重置</a-button>
                    <a @click="toggleAdvanced" style="margin-left: 8px">
                        {{ advanced ? '收起' : '展开' }}
                        <component :is="advanced ? 'up-outlined' : 'down-outlined'"/>
                    </a>
                </a-col>
            </a-row>
        </a-form>
        <s-table
            ref="table"
            :columns="columns"
            :data="loadData"
            :alert="options.alert.show"
            bordered
            :row-key="(record) => record.id"
            :tool-config="toolConfig"
            :row-selection="options.rowSelection"
        >
            <template #operator class="table-operator">
           <a-space>
                <a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('userInfoAdd')">
                    <template #icon><plus-outlined /></template>
                    新增
                </a-button>
                <xn-batch-button
                    v-if="hasPerm('userInfoBatchDelete')"
                    buttonName="批量删除"
                       icon="DeleteOutlined"
                    :selectedRowKeys="selectedRowKeys"
                    @batchCallBack="deleteBatchUserInfo"
                />
            </a-space>
            </template>
            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'action'">
                    <a-space>
                        <a @click="formRef.onOpen(record)" v-if="hasPerm('userInfoEdit')">编辑</a>
                        <a-divider type="vertical" v-if="hasPerm(['userInfoEdit', 'userInfoDelete'], 'and')" />
                        <a-popconfirm title="确定要删除吗？" @confirm="deleteUserInfo(record)">
                            <a-button type="link" danger size="small" v-if="hasPerm('userInfoDelete')">删除</a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="userinfo">
    import { message } from 'ant-design-vue'
    import Form from './form.vue'
    import userInfoApi from '@/api/manage/userInfoApi'
    let searchFormState = reactive({})
    const searchFormRef = ref()
    const table = ref()
    const formRef = ref()
    const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
    // 查询区域显示更多控制
    const advanced = ref(false)
    const toggleAdvanced = () => {
        advanced.value = !advanced.value
    }
    const columns = [
        {
            title: '手机号',
            dataIndex: 'mobile'
        },
        // {
        //     title: '头像',
        //     dataIndex: 'avatar'
        // },
        {
            title: '等级id',
            dataIndex: 'levelId'
        },
        {
            title: '昵称',
            dataIndex: 'nickname'
        },
        {
            title: '积分账户',
            dataIndex: 'integral'
        },
        {
            title: '冻结积分',
            dataIndex: 'frozenIntegral'
        },
        {
            title: '现金账户',
            dataIndex: 'cash'
        },
        {
            title: '冻结现金',
            dataIndex: 'frozenCash'
        },
        {
            title: '上次登录ip',
            dataIndex: 'lastLoginIp'
        },
        {
            title: '上次登录地点',
            dataIndex: 'lastLoginAddress'
        },
        {
            title: '上次登录时间',
            dataIndex: 'lastLoginTime'
        },
        {
            title: '上次登录设备',
            dataIndex: 'lastLoginDevice'
        },
        {
            title: '用户状态',
            dataIndex: 'userStatus'
        },
        {
            title: '创建时间',
            dataIndex: 'createTime'
        },
    ]
    // 操作栏通过权限判断是否显示
    if (hasPerm(['userInfoEdit', 'userInfoDelete'])) {
        columns.push({
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            width: '150px'
        })
    }
    let selectedRowKeys = ref([])
    // 列表选择配置
    const options = {
        alert: {
            show: false,
            clear: () => {
                selectedRowKeys = ref([])
            }
        },
        rowSelection: {
            onChange: (selectedRowKey, selectedRows) => {
                selectedRowKeys.value = selectedRowKey
            }
        }
    }
    const loadData = (parameter) => {
        const searchFormParam = JSON.parse(JSON.stringify(searchFormState))
        // createTime范围查询条件重载
        if (searchFormParam.createTime) {
            searchFormParam.startCreateTime = searchFormParam.createTime[0]
            searchFormParam.endCreateTime = searchFormParam.createTime[1]
            delete searchFormParam.createTime
        }
        return userInfoApi.userInfoPage(Object.assign(parameter, searchFormParam)).then((data) => {
            return data
        })
    }
    // 删除
    const deleteUserInfo = (record) => {
        let params = [
            {
                id: record.id
            }
        ]
        userInfoApi.userInfoDelete(params).then(() => {
            table.value.refresh(true)
        })
    }
    // 批量删除
    const deleteBatchUserInfo = () => {
        if (selectedRowKeys.value.length < 1) {
            message.warning('请选择一条或多条数据')
            return false
        }
        const params = selectedRowKeys.value.map((m) => {
            return {
                id: m
            }
        })
        userInfoApi.userInfoDelete(params).then(() => {
            table.value.clearRefreshSelected()
        })
    }
</script>
