<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item label="被关注者id" name="followedId">
						<a-input v-model:value="searchFormState.followedId" placeholder="请输入被关注者id" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item label="关注者id" name="followersId">
						<a-input v-model:value="searchFormState.followersId" placeholder="请输入关注者id" />
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
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('liveFocusRelationshipsAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						v-if="hasPerm('liveFocusRelationshipsBatchDelete')"
						buttonName="批量删除"
                        icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchLiveFocusRelationships"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)" v-if="hasPerm('liveFocusRelationshipsEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['liveFocusRelationshipsEdit', 'liveFocusRelationshipsDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteLiveFocusRelationships(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('liveFocusRelationshipsDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="tableRef.refresh()" />
</template>

<script setup name="focusrelationships">
	import { cloneDeep } from 'lodash-es'
	import Form from './form.vue'
	import liveFocusRelationshipsApi from '@/api/live/liveFocusRelationshipsApi'
	const searchFormState = ref({})
	const searchFormRef = ref()
	const tableRef = ref()
	const formRef = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		{
			title: '被关注者id',
			dataIndex: 'followedId'
		},
		{
			title: '关注者id',
			dataIndex: 'followersId'
		},
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['liveFocusRelationshipsEdit', 'liveFocusRelationshipsDelete'])) {
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
		return liveFocusRelationshipsApi.liveFocusRelationshipsPage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}
	// 删除
	const deleteLiveFocusRelationships = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		liveFocusRelationshipsApi.liveFocusRelationshipsDelete(params).then(() => {
			tableRef.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchLiveFocusRelationships = (params) => {
		liveFocusRelationshipsApi.liveFocusRelationshipsDelete(params).then(() => {
			tableRef.value.clearRefreshSelected()
		})
	}
</script>
