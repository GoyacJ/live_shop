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
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('liveSpectatorWatchingAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						v-if="hasPerm('liveSpectatorWatchingBatchDelete')"
						buttonName="批量删除"
                        icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchLiveSpectatorWatching"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)" v-if="hasPerm('liveSpectatorWatchingEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['liveSpectatorWatchingEdit', 'liveSpectatorWatchingDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteLiveSpectatorWatching(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('liveSpectatorWatchingDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="tableRef.refresh()" />
</template>

<script setup name="spectatorwatching">
	import { cloneDeep } from 'lodash-es'
	import Form from './form.vue'
	import liveSpectatorWatchingApi from '@/api/live/liveSpectatorWatchingApi'
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
			title: '开始时间',
			dataIndex: 'startTime'
		},
		{
			title: '结束时间',
			dataIndex: 'endTime'
		},
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['liveSpectatorWatchingEdit', 'liveSpectatorWatchingDelete'])) {
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
		return liveSpectatorWatchingApi.liveSpectatorWatchingPage(parameter).then((data) => {
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}
	// 删除
	const deleteLiveSpectatorWatching = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		liveSpectatorWatchingApi.liveSpectatorWatchingDelete(params).then(() => {
			tableRef.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchLiveSpectatorWatching = (params) => {
		liveSpectatorWatchingApi.liveSpectatorWatchingDelete(params).then(() => {
			tableRef.value.clearRefreshSelected()
		})
	}
</script>
