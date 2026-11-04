<template>
    <a-card :bordered="false">
        <a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form mb-4">
            <a-row :gutter="24">
                <a-col :span="6">
                    <a-form-item label="变更类型" name="type">
                        <a-select v-model:value="searchFormState.type" placeholder="请选择变更类型" :options="typeOptions" />
                    </a-form-item>
                </a-col>
                <a-col :span="6">
                    <a-form-item label="余额类型" name="balanceType">
                        <a-select v-model:value="searchFormState.balanceType" placeholder="请选择余额类型" :options="balanceTypeOptions" />
                    </a-form-item>
                </a-col>
                <a-col :span="6">
                    <a-button type="primary" @click="table.refresh(true)">查询</a-button>
                    <a-button style="margin: 0 8px" @click="() => searchFormRef.resetFields()">重置</a-button>
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
                <a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('userBalanceLogAdd')">
                    <template #icon><plus-outlined /></template>
                    新增
                </a-button>
                <xn-batch-button
                    v-if="hasPerm('userBalanceLogBatchDelete')"
                    buttonName="批量删除"
                       icon="DeleteOutlined"
                    :selectedRowKeys="selectedRowKeys"
                    @batchCallBack="deleteBatchUserBalanceLog"
                />
            </a-space>
            </template>
            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'type'">
                    {{ $TOOL.dictTypeData('balance_change_type', record.type) }}
                </template>
                <template v-if="column.dataIndex === 'balance type'">
                    {{ $TOOL.dictTypeData('balance_type', record.balanceType) }}
                </template>
                <template v-if="column.dataIndex === 'action'">
                    <a-space>
                        <a @click="formRef.onOpen(record)" v-if="hasPerm('userBalanceLogEdit')">编辑</a>
                        <a-divider type="vertical" v-if="hasPerm(['userBalanceLogEdit', 'userBalanceLogDelete'], 'and')" />
                        <a-popconfirm title="确定要删除吗？" @confirm="deleteUserBalanceLog(record)">
                            <a-button type="link" danger size="small" v-if="hasPerm('userBalanceLogDelete')">删除</a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="userbalancelog">
    import { message } from 'ant-design-vue'
    import tool from '@/utils/tool'
    import Form from './form.vue'
    import userBalanceLogApi from '@/api/manage/userBalanceLogApi'
    let searchFormState = reactive({})
    const searchFormRef = ref()
    const table = ref()
    const formRef = ref()
    const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
    const columns = [
        {
            title: '变更类型',
            dataIndex: 'type'
        },
        {
            title: '余额类型',
            dataIndex: 'balance type'
        },
        {
            title: '变更前',
            dataIndex: 'beforeBalance'
        },
        {
            title: '变更',
            dataIndex: 'changeBalance'
        },
        {
            title: '变更后',
            dataIndex: 'afterBalance'
        },
        {
            title: '变更原因',
            dataIndex: 'reason'
        },
        {
            title: '订单id/提现记录id/任务奖励记录id···',
            dataIndex: 'reasonId'
        },
    ]
    // 操作栏通过权限判断是否显示
    if (hasPerm(['userBalanceLogEdit', 'userBalanceLogDelete'])) {
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
        return userBalanceLogApi.userBalanceLogPage(Object.assign(parameter, searchFormParam)).then((data) => {
            return data
        })
    }
    // 删除
    const deleteUserBalanceLog = (record) => {
        let params = [
            {
                id: record.id
            }
        ]
        userBalanceLogApi.userBalanceLogDelete(params).then(() => {
            table.value.refresh(true)
        })
    }
    // 批量删除
    const deleteBatchUserBalanceLog = () => {
        if (selectedRowKeys.value.length < 1) {
            message.warning('请选择一条或多条数据')
            return false
        }
        const params = selectedRowKeys.value.map((m) => {
            return {
                id: m
            }
        })
        userBalanceLogApi.userBalanceLogDelete(params).then(() => {
            table.value.clearRefreshSelected()
        })
    }
    const typeOptions = tool.dictList("balance_change_type")
    const balanceTypeOptions = tool.dictList('balance_type')
</script>
