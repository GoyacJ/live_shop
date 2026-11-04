<template>
	<a-card :bordered="false">
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
					<a-button type="primary" @click="formRef.onOpen()">
						<template #icon><plus-outlined /></template>
						新增版本
					</a-button>
					<xn-batch-button
						buttonName="批量删除"
						icon="DeleteOutlined"
						buttonDanger
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchVersion"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'appType'">
					<a-tag :color="record.appType === 'Android' ? 'green' : 'blue'">
						{{ record.appType }}
					</a-tag>
				</template>
				<template v-if="column.dataIndex === 'forceUpdate'">
					<a-tag :color="record.forceUpdate ? 'red' : 'green'">
						{{ record.forceUpdate ? '是' : '否' }}
					</a-tag>
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="copyAppUrl(record)">复制链接</a>
						<a-divider type="vertical" />
						<a @click="formRef.onOpen(record)">编辑</a>
						<a-divider type="vertical" />
						<a-popconfirm title="确定要删除此版本吗？" @confirm="deleteVersion(record)">
							<a-button type="link" danger size="small">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="tableRef.refresh(true)" />
</template>

<script setup name="appVersionManage">
	import { PlusOutlined } from '@ant-design/icons-vue'
	import appVersionApi from '@/api/dev/appVersionApi'
	import { message } from 'ant-design-vue'
	import Form from './form.vue'

	const tableRef = ref()
	const formRef = ref()

	// 工具配置
	const toolConfig = { refresh: true, height: true, columnSetting: false, striped: false }

	// 表格列定义
	const columns = [
		{
			title: '版本名',
			dataIndex: 'versionName',
			ellipsis: true
		},
		{
			title: '版本号',
			dataIndex: 'versionCode',
			ellipsis: true
		},
		{
			title: '版本类型',
			dataIndex: 'appType',
			width: '100px'
		},
		{
			title: '版本描述',
			dataIndex: 'appDesc',
			ellipsis: true
		},
		{
			title: '版本地址',
			dataIndex: 'appUrl',
			ellipsis: true
		},
		{
			title: '强制更新',
			dataIndex: 'forceUpdate',
			width: '100px'
		},
		{
			title: '创建时间',
			dataIndex: 'createTime',
			width: '150px'
		},
		{
			title: '排序',
			dataIndex: 'sortCode',
			sorter: true,
			width: '80px'
		},
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: '220px'
		}
	]

	// 列表选择配置
	let selectedRowKeys = ref([])
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

	// 表格数据加载
	const loadData = (parameter) => {
		return appVersionApi.page(parameter).then((res) => {
			return res
		})
	}

	// 复制APP下载地址
	const copyAppUrl = (record) => {
		const input = document.createElement('input')
		input.value = record.appUrl
		document.body.appendChild(input)
		input.select()
		document.execCommand('copy')
		document.body.removeChild(input)
		message.success('链接已复制')
	}

	// 删除版本
	const deleteVersion = (record) => {
		// 修改为数组格式
		let params = [
			{
				id: record.id
			}
		]
		appVersionApi.delete(params).then(() => {
			tableRef.value.refresh(true)
		})
	}

	// 批量删除版本
	const deleteBatchVersion = (params) => {
		// 确保params是数组格式
		if (!Array.isArray(params) || params.length === 0) {
			message.warning('请选择要删除的记录')
			return
		}
		appVersionApi.delete(params).then(() => {
			tableRef.value.clearRefreshSelected()
		})
	}
</script>

<style scoped>
	.xn-mg08 {
		margin-left: 8px;
	}
</style>
