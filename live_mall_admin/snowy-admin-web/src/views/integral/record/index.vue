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
					</a-space>
				</template>
			</template>
		</s-table>
		<Form ref="formRef" @successful="tableRef.refresh(true)" />
	</a-card>
</template>

<script setup name="IntegralRecordIndex">
	import tool from '@/utils/tool'
	import Form from './form.vue'
	import integralRecordApi from '@/api/integral/IntegralRecordApi.js'

	const tableRef = ref()
	const formRef = ref()
	const searchFormState = ref({})
	const searchFormRef = ref()
	const selectedRowKeys = ref([])
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }

	const columns = [
		{ title: '用户Id', dataIndex: 'userId', sorter: true },
		{
			title: '积分来源',
			dataIndex: 'integralSource',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('INTEGRAL_SOURCE', text)
		},
		{
			title: '变动类型',
			dataIndex: 'changeType',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('INTEGRAL_CHANGE_TYPE', text)
		},
		{
			title: '积分分类',
			dataIndex: 'integralCategory',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('INTEGRAL_CATEGORY', text)
		},
		{ title: '操作时间', dataIndex: 'operationTime', sorter: true },
		{ title: '积分', dataIndex: 'integral', sorter: true },
		{
			title: '操作来源',
			dataIndex: 'operationSource',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('INTEGRAL_OPERATION_SOURCE', text)
		},
		{ title: '积分余额', dataIndex: 'integralBalance', sorter: true },
		{ title: '冻结积分余额', dataIndex: 'frozenIntegralBalance', sorter: true },
		{ title: '过期积分余额', dataIndex: 'expireIntegralBalance', sorter: true },
		{ title: '积分有效期', dataIndex: 'validityPeriod', sorter: true },
		{ title: '备注', dataIndex: 'remark' },
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
		return integralRecordApi.page(Object.assign(parameter, searchFormState.value)).then((data) => data)
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
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
