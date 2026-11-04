<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form mb-4">
			<a-row :gutter="24">
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
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('mallGoodsSlideAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						v-if="hasPerm('mallGoodsSlideBatchDelete')"
						buttonName="批量删除"
						icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchMallGoodsSlide"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'type'">
					{{ $TOOL.dictTypeData('file_type', record.type) }}
				</template>
				<template v-if="column.dataIndex === 'status'">
					{{ $TOOL.dictTypeData('whether_it_is_enabled', record.status) }}
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)" v-if="hasPerm('mallGoodsSlideEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['mallGoodsSlideEdit', 'mallGoodsSlideDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteMallGoodsSlide(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('mallGoodsSlideDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="mallgoodsslide">
	import { message } from 'ant-design-vue'
	import tool from '@/utils/tool'
	import Form from './form.vue'
	import mallGoodsSlideApi from '@/api/shop/mallGoodsSlideApi'
	let searchFormState = reactive({})
	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		{
			title: '商品',
			dataIndex: 'goodsId'
		},
		{
			title: '商品图片链接',
			dataIndex: 'url'
		},
		{
			title: '类型',
			dataIndex: 'type'
		},
		{
			title: '排序',
			dataIndex: 'sort'
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
	if (hasPerm(['mallGoodsSlideEdit', 'mallGoodsSlideDelete'])) {
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
		return mallGoodsSlideApi.mallGoodsSlidePage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 删除
	const deleteMallGoodsSlide = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		mallGoodsSlideApi.mallGoodsSlideDelete(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchMallGoodsSlide = () => {
		if (selectedRowKeys.value.length < 1) {
			message.warning('请选择一条或多条数据')
			return false
		}
		const params = selectedRowKeys.value.map((m) => {
			return {
				id: m
			}
		})
		mallGoodsSlideApi.mallGoodsSlideDelete(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
</script>
