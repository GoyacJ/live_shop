<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item label="直播间状态" name="liveRoomState">
						<a-input v-model:value="searchFormState.liveRoomState" placeholder="请输入直播间状态" />
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
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('liveBizAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						v-if="hasPerm('liveBizBatchDelete')"
						buttonName="批量删除"
                        icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchLiveBiz"
					/>
				</a-space>
			</template>

			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'coverUrl'">
					<a-image :src="record.coverUrl" style="margin-bottom: -5px; margin-top: -5px" />
				</template>
				<template v-if="column.dataIndex === 'liveRoomState'">
					{{ $TOOL.dictTypeData('LIVE_ROOM_STATE', record.liveRoomState) }}
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="operateIndexRef.onOpen(record, 'car')" v-if="hasPerm('liveBizEdit')">购物车</a>
						<a @click="operateIndexRef.onOpen(record, 'activity')" v-if="hasPerm('liveBizEdit')">营销活动</a>
					</a-space>
					<a-space>
						<a @click="formRef.onOpen(record)" v-if="hasPerm('liveBizEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['liveBizEdit', 'liveBizDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteLiveBiz(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('liveBizDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="tableRef.refresh()" />
	<OperateIndex ref="operateIndexRef" />
</template>

<script setup name="livebiz">
	import { cloneDeep } from 'lodash-es'
	import Form from './form.vue'
	import OperateIndex from './operate-index.vue'
	import liveBizApi from '@/api/live/liveBizApi'
	const tableRef = ref()
	const formRef = ref()
	const searchFormState = ref({})
	const searchFormRef = ref()
	const operateIndexRef = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		// {
		// 	title: '直播场次ID',
		// 	dataIndex: 'liveBroadcasts',
		// 	width: '110px'
		// },
		{
			title: '主标题',
			dataIndex: 'mainTitle',
			width: '100px'
		},
		{
			title: '主播',
			dataIndex: 'anchorName',
			width: '130px'
		},
		{
			title: '推流url',
			dataIndex: 'ingestUrl',
			width: '200px'
		},
		{
			title: '观看url',
			dataIndex: 'liveUrl',
			width: '200px'
		},
		{
			title: '直播封面',
			dataIndex: 'coverUrl',
			align: 'center',
			width: '80px'
		},
		{
			title: '直播间状态',
			dataIndex: 'liveRoomState',
			width: '100px'
		},
		{
			title: '直播类型',
			dataIndex: 'liveStateName'
		},
		{
			title: '直播开始时间',
			dataIndex: 'liveBeginTime'
		},
		{
			title: '直播结束时间',
			dataIndex: 'liveOverTime'
		}
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['liveBizEdit', 'liveBizDelete'])) {
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
		const param = cloneDeep(searchFormState.value)
		return liveBizApi.liveBizPage(Object.assign(parameter, param)).then((data) => {
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}
	// 删除
	const deleteLiveBiz = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		liveBizApi.liveBizDelete(params).then(() => {
			tableRef.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchLiveBiz = (params) => {
		liveBizApi.liveBizDelete(params).then(() => {
			tableRef.value.clearRefreshSelected()
		})
	}
</script>
