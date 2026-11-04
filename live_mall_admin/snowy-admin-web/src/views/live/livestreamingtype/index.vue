<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form mb-4">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item label="直播类型" name="title">
						<a-input v-model:value="searchFormState.title" placeholder="请输入直播类型" />
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
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('LiveStreamingTypeAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						v-if="hasPerm('LiveTypeBatchDelete')"
						buttonName="批量删除"
						icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchLiveStrreamingType"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'status'">
					{{ $TOOL.dictTypeData('COMMON_STATUS', record.status) }}
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)" v-if="hasPerm('liveStreamingTypeEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['liveStreamingTypeEdit', 'liveStreamingTypeDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteLiveStreamingType(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('liveStreamingTypeDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="mallOrder">
	import { message } from 'ant-design-vue'
	import Form from './form.vue'
	import liveStreamingTypeApi from '@/api/live/liveStreamingTypeApi'
	let searchFormState = reactive({})
	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()

	import { ref } from 'vue'

	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		{
			title: '直播类型',
			dataIndex: 'title'
		},
		{
			title: '描述',
			dataIndex: 'describe'
		},
		{
			title: '状态',
			dataIndex: 'status'
		},
		{
			title: '创建时间',
			dataIndex: 'createTime'
		}
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['liveStreamingTypeEdit', 'liveStreamingTypeDelete'])) {
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
		//查询直播类型信息
		return liveStreamingTypeApi.liveStreamingTypePage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 删除
	const deleteLiveStreamingType = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		liveStreamingTypeApi.liveStreamingTypeDelete(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchLiveStrreamingType = () => {
		if (selectedRowKeys.value.length < 1) {
			message.warning('请选择一条或多条数据')
			return false
		}
		const params = selectedRowKeys.value.map((m) => {
			return {
				id: m
			}
		})
		liveStreamingTypeApi.liveStreamingTypeDelete(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
</script>
