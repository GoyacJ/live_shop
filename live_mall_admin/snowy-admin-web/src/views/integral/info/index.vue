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
				<template v-if="column.dataIndex === 'status'">
					<a-switch
						:checked="record.status === 0"
						checked-children="启用"
						un-checked-children="禁用"
						@change="changeStatus(record)"
					/>
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="detailFormRef.onOpen(record)">详情</a>
					</a-space>
					<a-divider type="vertical" />
					<a-space>
						<a @click="adjustFormRef.onOpen(record)">调整积分</a>
					</a-space>
				</template>
			</template>
		</s-table>
		<detail-form ref="detailFormRef" @successful="tableRef.refresh(true)" />
		<adjust-from ref="adjustFormRef" @successful="tableRef.refresh(true)" />
	</a-card>
</template>

<script setup name="IntegralInfoIndex">
	import tool from '@/utils/tool'
	import detailForm from './detail.vue'
	import adjustFrom from './adjust.vue'
	import integralInfoApi from '@/api/integral/IntegralInfoApi.js'

	const tableRef = ref()
	const detailFormRef = ref()
	const adjustFormRef = ref()
	const searchFormState = ref({})
	const searchFormRef = ref()
	const selectedRowKeys = ref([])
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }

	const columns = [
		{ title: 'ID', dataIndex: 'id', sorter: true },
		{ title: '用户ID', dataIndex: 'userId', sorter: true },
		{ title: '积分', dataIndex: 'integral', sorter: true },
		{ title: '冻结积分', dataIndex: 'frozenIntegral', sorter: true },
		{ title: '过期积分', dataIndex: 'expireIntegral', sorter: true },
		{ title: '最后修改时间', dataIndex: 'lastModifyTime', sorter: true },
		{ title: '最后修改人ID', dataIndex: 'lastModifyUserId', sorter: true },
		{
			title: '状态',
			dataIndex: 'status',
			align: 'center',
			customRender: ({ text }) => tool.dictTypeData('SIMPLE_STATUS', text)
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
		return integralInfoApi.page(Object.assign(parameter, searchFormState.value)).then((data) => data)
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}

	const changeStatus = (record) => {
		const newStatus = record.status === 0 ? 1 : 0
		integralInfoApi.changeStatus({ id: record.userId, status: newStatus }).then(() => {
			tableRef.value.refresh(true)
		})
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
