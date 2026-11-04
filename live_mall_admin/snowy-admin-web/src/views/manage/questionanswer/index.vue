<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form mb-4">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item label="问题文本" name="questionContent">
						<a-input v-model:value="searchFormState.questionContent" placeholder="请输入问题文本" />
					</a-form-item>
				</a-col>
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
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('questionAnswerAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						v-if="hasPerm('questionAnswerBatchDelete')"
						buttonName="批量删除"
						icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchquestionAnswer"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
<!--				<template v-if="column.dataIndex === 'isDefault'">
					{{ $TOOL.dictTypeData('IS_DEFAULT', record.isDefault) }}
				</template>-->
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)" v-if="hasPerm('questionAnswerEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['questionAnswerEdit', 'questionAnswerDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deletequestionAnswer(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('questionAnswerDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="useraddress">
	import { message } from 'ant-design-vue'
	import tool from '@/utils/tool'
	import Form from './form.vue'
	import userQuestionAnswerApi from '@/api/manage/userQuestionAnswerApi'
	let searchFormState = reactive({})
	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		{
			title: '问题',
			dataIndex: 'questionContent'
		},
		{
			title: '答案',
			dataIndex: 'answerContent'
		},
		{
			title: '创建时间',
			dataIndex: 'createTime'
		}
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['questionAnswerEdit', 'questionAnswerDelete'])) {
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
		return userQuestionAnswerApi.questionAnswerPage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 删除
	const deletequestionAnswer = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		userQuestionAnswerApi.questionAnswerDelete(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchquestionAnswer = () => {
		if (selectedRowKeys.value.length < 1) {
			message.warning('请选择一条或多条数据')
			return false
		}
		const params = selectedRowKeys.value.map((m) => {
			return {
				id: m
			}
		})
		userQuestionAnswerApi.questionAnswerDelete(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
</script>
