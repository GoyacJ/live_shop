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
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('liveSpectatorGradeAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						v-if="hasPerm('liveSpectatorGradeBatchDelete')"
						buttonName="批量删除"
                        icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchLiveSpectatorGrade"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)" v-if="hasPerm('liveSpectatorGradeEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['liveSpectatorGradeEdit', 'liveSpectatorGradeDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteLiveSpectatorGrade(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('liveSpectatorGradeDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="tableRef.refresh()" />
</template>

<script setup name="spectatorgrade">
	import { cloneDeep } from 'lodash-es'
	import Form from './form.vue'
	import liveSpectatorGradeApi from '@/api/live/liveSpectatorGradeApi'
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
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['liveSpectatorGradeEdit', 'liveSpectatorGradeDelete'])) {
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
		return liveSpectatorGradeApi.liveSpectatorGradePage(parameter).then((data) => {
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}
	// 删除
	const deleteLiveSpectatorGrade = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		liveSpectatorGradeApi.liveSpectatorGradeDelete(params).then(() => {
			tableRef.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchLiveSpectatorGrade = (params) => {
		liveSpectatorGradeApi.liveSpectatorGradeDelete(params).then(() => {
			tableRef.value.clearRefreshSelected()
		})
	}
</script>
