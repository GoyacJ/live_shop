<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item label="观众id" name="spectatorId">
						<a-input v-model:value="searchFormState.spectatorId" placeholder="请输入观众id" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item label="主播id" name="anchorId">
						<a-input v-model:value="searchFormState.anchorId" placeholder="请输入主播id" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-button type="primary" @click="tableRef.refresh()">查询</a-button>
					<a-button style="margin: 0 8px" @click="reset">重置</a-button>
				</a-col>
			</a-row>
		</a-form>
		<s-table
			ref="tableRef"
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
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('spectatorInformationAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						v-if="hasPerm('spectatorInformationBatchDelete')"
						buttonName="批量删除"
                        icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchSpectatorInformation"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)" v-if="hasPerm('spectatorInformationEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['spectatorInformationEdit', 'spectatorInformationDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteSpectatorInformation(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('spectatorInformationDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="tableRef.refresh()" />
</template>

<script setup name="spectatorinformation">
	import { cloneDeep } from 'lodash-es'
	import Form from './form.vue'
	import spectatorInformationApi from '@/api/live/spectatorInformationApi'
	const searchFormState = ref({})
	const searchFormRef = ref()
	const tableRef = ref()
	const formRef = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		{
			title: '观众id',
			dataIndex: 'spectatorId'
		},
		{
			title: '主播id',
			dataIndex: 'anchorId'
		},
		{
			title: '观众等级',
			dataIndex: 'spectatorGrade'
		},
		{
			title: '成长值',
			dataIndex: 'growthValue'
		},
		{
			title: '总消费',
			dataIndex: 'totalConsumption'
		},
		{
			title: '观看时长',
			dataIndex: 'watchingTime'
		},
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['spectatorInformationEdit', 'spectatorInformationDelete'])) {
		columns.push({
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: 150
		})
	}
	const selectedRowKeys = ref([])
	// 列表选择配置
	const options = {
		// columns数字类型字段加入 needTotal: true 可以勾选自动算账
		alert: {
			show: true,
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
		const searchFormParam = cloneDeep(searchFormState.value)
		return spectatorInformationApi.spectatorInformationPage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}
	// 删除
	const deleteSpectatorInformation = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		spectatorInformationApi.spectatorInformationDelete(params).then(() => {
			tableRef.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchSpectatorInformation = (params) => {
		spectatorInformationApi.spectatorInformationDelete(params).then(() => {
			tableRef.value.clearRefreshSelected()
		})
	}
</script>
