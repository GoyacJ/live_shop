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
			<template #operator>
				<a-space>
					<a-button type="primary" @click="formRef.onOpen()">新增</a-button>
					<xn-batch-button
						buttonName="批量删除"
						icon="DeleteOutlined"
						buttonDanger
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatch"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'status'">
					<a-switch
						:checked="record.status === 0"
						checked-children="启用"
						un-checked-children="禁用"
						@change="changeStatus(record)"
					/>
				</template>
				<template v-if="column.dataIndex === 'logo'">
					<div v-if="record.logo">
						<a-image :src="record.logo" style="width: 30px; height: 30px" />
					</div>
					<span v-else>未上传</span>
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)">编辑</a>
						<a-divider type="vertical" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteOne(record)">
							<a-button type="link" danger size="small">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
		<Form ref="formRef" @successful="tableRef.refresh(true)" />
	</a-card>
</template>

<script setup name="DistributionInfoIndex">
	import tool from '@/utils/tool'
	import Form from './form.vue'
	import distributionInfoApi from '@/api/distribution/DistributionInfoApi.js'
	import integralInfoApi from '@/api/integral/IntegralInfoApi'

	const tableRef = ref()
	const formRef = ref()
	const searchFormState = ref({})
	const searchFormRef = ref()
	const selectedRowKeys = ref([])
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }

	const columns = [
		{ title: '排序', dataIndex: 'sort', sorter: true },
		{ title: '推广名称', dataIndex: 'name', sorter: true },
		{
			title: '推广类型',
			dataIndex: 'type',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('DISTRIBUTION_TYPE', text)
		},
		{ title: '推广LOGO', dataIndex: 'logo', sorter: true },
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
			title: '积分结算时间类型',
			dataIndex: 'settlementTimeType',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('SETTLEMENT_TIME_TYPE', text)
		},
		{
			title: '积分发放时间类型',
			dataIndex: 'commissionTimeType',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('COMMISSION_TIME_TYPE', text)
		},
		{ title: '积分发放时间', dataIndex: 'commissionFixTime', sorter: true },
		{
			title: '推广状态',
			dataIndex: 'status',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('SIMPLE_STATUS', text)
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
		return distributionInfoApi.page(Object.assign(parameter, searchFormState.value)).then((data) => data)
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
		distributionInfoApi.delete(params).then(() => {
			tableRef.value.refresh(true)
		})
	}

	// 批量删除
	const deleteBatch = (params) => {
		distributionInfoApi.delete(params).then(() => {
			tableRef.value.clearRefreshSelected()
		})
	}

	const changeStatus = (record) => {
		const newStatus = record.status === 0 ? 1 : 0
		distributionInfoApi.changeStatus({ id: record.id, status: newStatus }).then(() => {
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
