<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" :model="searchFormState" class="mb-4">
			<a-row :gutter="24">
				<a-col :span="5">
					<a-form-item label="关键词" name="searchKey">
						<a-input v-model:value="searchFormState.searchKey" placeholder="请输入关键词" />
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-button type="primary" @click="tableRef.refresh(true)">
						<template #icon>
							<SearchOutlined />
						</template>
						查询
					</a-button>
					<a-button class="snowy-button-left" @click="reset">
						<template #icon>
							<redo-outlined />
						</template>
						重置
					</a-button>
				</a-col>
			</a-row>
		</a-form>
		<s-table
			ref="tableRef"
			:columns="columns"
			:data="loadData"
			:expand-row-by-click="true"
			:alert="options.alert.show"
			bordered
			:row-key="(record) => record.id"
			:row-selection="options.rowSelection"
			:tool-config="toolConfig"
		>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)">详情</a>
						<a-divider type="vertical" />
					</a-space>
				</template>
			</template>
		</s-table>
		<Form ref="formRef" @successful="tableRef.refresh(true)" />
	</a-card>
</template>

<script setup name="DistributionSettlementIndex">
	import tool from '@/utils/tool'
	import Form from './form.vue'
	import distributionSettlementApi from '@/api/distribution/DistributionSettlementApi.js'
	import integralInfoApi from '@/api/integral/IntegralInfoApi'

	const tableRef = ref()
	const formRef = ref()
	const searchFormState = ref({})
	const searchFormRef = ref()
	const selectedRowKeys = ref([])
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }

	const columns = [
		{
			title: '推广类型',
			dataIndex: 'type',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('DISTRIBUTION_TYPE', text)
		},
		{ title: '用户ID', dataIndex: 'userId', sorter: true },
		{ title: '推广人ID', dataIndex: 'promoterId', sorter: true },
		{
			title: '积分返佣方式',
			dataIndex: 'recommendCommission',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('RECOMMEND_COMMISSION', text)
		},
		{ title: '返佣积分/比例', dataIndex: 'commissionIntegral', sorter: true },
		{
			title: '积分结算方式',
			dataIndex: 'settlementType',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('SETTLEMENT_TYPE', text)
		},
		{
			title: '结算状态',
			dataIndex: 'settlementStatus',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('SETTLEMENT_STATUS', text)
		},
		{ title: '结算时间', dataIndex: 'settlementTime', sorter: true },
		{ title: '结算积分', dataIndex: 'integral', sorter: true },
		{
			title: '积分结算方式',
			dataIndex: 'settlementType',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('SETTLEMENT_TYPE', text)
		},
		{
			title: '积分发放状态',
			dataIndex: 'commissionTimeStatus',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('COMMISSION_TIME_STATUS', text)
		},
		{ title: '操作', dataIndex: 'action', align: 'center', width: 150 }
	]

	// 列表选择配置
	const options = {
		alert: {
			show: false,
			clear: () => {
				selectedRowKeys.value = ref([])
			}
		},
		rowSelection: {
			onChange: (selectedRowKey, selectedRows) => {
				selectedRowKeys.value = selectedRowKey
			}
		}
	}

	const loadData = (parameter) => {
		return distributionSettlementApi.page(Object.assign(parameter, searchFormState.value)).then((data) => data)
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}

	// 删除
	const deleteOne = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		distributionSettlementApi.delete(params).then(() => {
			tableRef.value.refresh(true)
		})
	}

	// 批量删除
	const deleteBatch = (params) => {
		distributionSettlementApi.delete(params).then(() => {
			tableRef.value.clearRefreshSelected()
		})
	}

	const manualSettlement = (record) => {
		distributionSettlementApi.manualSettlement({ id: record.userId }).then(() => {
			tableRef.value.refresh(true)
		})
	}
</script>

<style scoped>
	.table-operator {
		margin-bottom: 16px;
	}

	.mb-4 {
		margin-bottom: 16px;
	}

	.w-8 {
		width: 32px;
	}

	.h-8 {
		height: 32px;
	}
</style>
