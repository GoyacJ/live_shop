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

<script setup name="DistributionRecordIndex">
	import tool from '@/utils/tool'
	import Form from './form.vue'
	import taskRecordApi from '@/api/task/TaskRecordApi.js'

	const tableRef = ref()
	const formRef = ref()
	const searchFormState = ref({})
	const searchFormRef = ref()
	const selectedRowKeys = ref([])
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }

	const statusMap = {
		0: '正常',
		1: '异常'
		// 其他状态...
	}

	const columns = [
		{ title: '任务ID', dataIndex: 'taskId', sorter: true },
		{ title: '操作时间', dataIndex: 'operateTime', sorter: true },
		{ title: '用户ID', dataIndex: 'userId', sorter: true },
		{ title: '任务名称', dataIndex: 'taskName', sorter: true },
		{
			title: '任务类型',
			dataIndex: 'taskType',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('TASK_TYPE', text)
		},
		{
			title: '任务分类',
			dataIndex: 'taskCategory',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('TASK_CATEGORY', text)
		},
		{
			title: '任务业务分类',
			dataIndex: 'taskBusinessCategory',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('TASK_BUSINESS_CATEGORY', text)
		},
		{
			title: '触发条件',
			dataIndex: 'triggerCondition',
			sorter: true,
			customRender: ({ text }) => tool.dictTypeData('TASK_TRIGGER_CONDITION', text)
		},
		{ title: '奖励积分', dataIndex: 'rewardIntegral', sorter: true },
		{ title: '错误原因', dataIndex: 'errReason', sorter: true },
		{
			title: '执行状态',
			dataIndex: 'executeStatus',
			sorter: true,
			customRender: ({ text }) => statusMap[text] || tool.dictTypeData('SIMPLE_STATUS', text)
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
		return taskRecordApi.page(Object.assign(parameter, searchFormState.value)).then((data) => data)
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
