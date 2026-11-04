<template>
    <a-card :bordered="false">
        <a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form mb-4">
            <a-row :gutter="24">
                <a-col :span="6">
                    <a-form-item label="创建时间" name="createTime">
                        <a-range-picker v-model:value="searchFormState.createTime" value-format="YYYY-MM-DD HH:mm:ss" show-time />
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
                <a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('userIntegralAdd')">
                    <template #icon><plus-outlined /></template>
                    新增
                </a-button>
                <xn-batch-button
                    v-if="hasPerm('userIntegralBatchDelete')"
                    buttonName="批量删除"
                       icon="DeleteOutlined"
                    :selectedRowKeys="selectedRowKeys"
                    @batchCallBack="deleteBatchUserIntegral"
                />
            </a-space>
            </template>
            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'status'">
                    {{ $TOOL.dictTypeData('points_redemption_status', record.status) }}
                </template>

                <template v-if="column.dataIndex === 'action'">
                    <a-space>
                        <a @click="formRef.onOpen(record)" v-if="hasPerm('userIntegralEdit')">编辑</a>
                        <a-divider type="vertical" v-if="hasPerm(['userIntegralEdit', 'userIntegralDelete'], 'and')" />
                        <a-popconfirm title="确定要删除吗？" @confirm="deleteUserIntegral(record)">
                            <a-button type="link" danger size="small" v-if="hasPerm('userIntegralDelete')">删除</a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="userintegral">
    import { message } from 'ant-design-vue'
    import tool from '@/utils/tool'
    import Form from './form.vue'
    import userIntegralApi from '@/api/manage/userIntegralApi'
    let searchFormState = reactive({})
    const searchFormRef = ref()
    const table = ref()
    const formRef = ref()
    const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
    const columns = [
        {
            title: '提现金额',
            dataIndex: 'amount'
        },
		{
			title: '兑换积分',
			dataIndex: 'integral'
		},
        {
            title: '审核原因',
            dataIndex: 'reason'
        },
        {
            title: '状态',
            dataIndex: 'status'
        },
        {
            title: '创建时间',
            dataIndex: 'createTime'
        },
        {
            title: '创建用户',
            dataIndex: 'createUserName'
        },
        {
            title: '修改时间',
            dataIndex: 'updateTime'
        },
    ]
    // 操作栏通过权限判断是否显示
    if (hasPerm(['userIntegralEdit', 'userIntegralDelete'])) {
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
        return userIntegralApi.userIntegralPage(Object.assign(parameter, searchFormParam)).then((data) => {
            return data
        })
    }
    // 删除
    const deleteUserIntegral = (record) => {
        let params = [
            {
                id: record.id
            }
        ]
        userIntegralApi.userIntegralDelete(params).then(() => {
            table.value.refresh(true)
        })
    }
    // 批量删除
    const deleteBatchUserIntegral = () => {
        if (selectedRowKeys.value.length < 1) {
            message.warning('请选择一条或多条数据')
            return false
        }
        const params = selectedRowKeys.value.map((m) => {
            return {
                id: m
            }
        })
        userIntegralApi.userIntegralDelete(params).then(() => {
            table.value.clearRefreshSelected()
        })
    }
</script>
