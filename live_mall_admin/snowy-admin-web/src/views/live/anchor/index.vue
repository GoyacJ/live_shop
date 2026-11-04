<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item label="昵称" name="realName">
						<a-input v-model:value="searchFormState.realName" placeholder="请输入昵称" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item label="账号" name="account">
						<a-input v-model:value="searchFormState.account" placeholder="请输入账号" />
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
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('anchorAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						v-if="hasPerm('anchorBatchDelete')"
						buttonName="批量删除"
                        icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchAnchor"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)" v-if="hasPerm('anchorEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['anchorEdit', 'anchorDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteAnchor(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('anchorDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="tableRef.refresh()" />
</template>

<script setup name="anchor">
	import { cloneDeep } from 'lodash-es'
	import Form from './form.vue'
	import anchorApi from '@/api/live/anchorApi'
	const searchFormState = ref({})
	const searchFormRef = ref()
	const tableRef = ref()
	const formRef = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		{
			title: '主播name',
			dataIndex: 'username'
		},
		{
			title: '昵称',
			dataIndex: 'realName'
		},
		{
			title: '账号',
			dataIndex: 'account'
		},
		{
			title: '密码',
			dataIndex: 'password'
		},
		{
			title: '头像url',
			dataIndex: 'pictureUrl'
		},
		{
			title: '简介',
			dataIndex: 'bio'
		},
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['anchorEdit', 'anchorDelete'])) {
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
		return anchorApi.anchorPage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}
	// 删除
	const deleteAnchor = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		anchorApi.anchorDelete(params).then(() => {
			tableRef.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchAnchor = (params) => {
		anchorApi.anchorDelete(params).then(() => {
			tableRef.value.clearRefreshSelected()
		})
	}
</script>
