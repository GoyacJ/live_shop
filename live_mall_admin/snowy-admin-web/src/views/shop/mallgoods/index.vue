<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form mb-4">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item label="商品标题" name="title">
						<a-input v-model:value="searchFormState.title" placeholder="请输入商品标题" />
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
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('mallGoodsAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
<!--					<a-button @click="exportBatchUserVerify">-->
<!--						<template #icon><export-outlined /></template>-->
<!--						批量导入-->
<!--					</a-button>-->
<!--					<a-button @click="exportBatchGoodsVerify">-->
<!--						<template #icon><export-outlined /></template>-->
<!--						批量导出-->
<!--					</a-button>-->
					<xn-batch-button
						v-if="hasPerm('mallGoodsBatchDelete')"
						buttonName="批量删除"
						icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchMallGoods"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'status'">
					{{ $TOOL.dictTypeData('GOODS_STATUS', record.status) }}
				</template>
				<template v-if="column.dataIndex === 'hostImage'">
					<a-avatar :src="record.hostImage" style="margin-bottom: -5px; margin-top: -5px" />
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
<!--						<a @click="goodsDetailsformRef.onOpen(record)">查看</a>-->
						<a @click="formRef.onOpen(record)" v-if="hasPerm('mallGoodsEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['detailGoodsEdit', 'mallGoodsEdit', 'mallGoodsDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteMallGoods(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('mallGoodsDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="table.refresh(true)" />
	<GoodsDetailsForm ref="goodsDetailsformRef" @successful="table.refresh(true)" />
</template>

<script setup name="mallgoods">
	import { message } from 'ant-design-vue'
	import Form from './form.vue'
	import mallGoodsApi from '@/api/shop/mallGoodsApi'
	import GoodsDetailsForm from '@/views/shop/mallgoods/goodsDetailsForm.vue'
	let searchFormState = reactive({})
	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()
	const goodsDetailsformRef = ref()
	import downloadUtil from '@/utils/downloadUtil'
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		{
			title: '商品标题',
			dataIndex: 'title'
		},
		{
			title: '商品主图',
			dataIndex: 'hostImage',
			align: 'center',
			width: '80px'
		},
		{
			title: '成本价',
			dataIndex: 'costPrice'
		},
		{
			title: '售价',
			dataIndex: 'price'
		},
		{
			title: '商品所属商家',
			dataIndex: 'merchant'
		},
		{
			title: '赠送的积分',
			dataIndex: 'giftPoints'
		},
		{
			title: '推广奖励积分',
			dataIndex: 'spreadGift'
		},
		{
			title: '商品简述',
			dataIndex: 'description'
		},
		{
			title: '库存',
			dataIndex: 'stock'
		},
		{
			title: '库存预警值',
			dataIndex: 'lowStock'
		},
		{
			title: '销量',
			dataIndex: 'sales'
		},
		{
			title: '虚拟销量',
			dataIndex: 'virtualSales'
		},
		{
			title: '状态',
			dataIndex: 'status'
		},
		{
			title: '排序',
			dataIndex: 'sort',
			sorter: true
		},
		{
			title: '创建时间',
			dataIndex: 'createTime',
			sorter: true
		}
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['mallGoodsEdit', 'mallGoodsDelete'])) {
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
		return mallGoodsApi.mallGoodsPage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 删除
	const deleteMallGoods = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		mallGoodsApi.mallGoodsDelete(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchMallGoods = () => {
		if (selectedRowKeys.value.length < 1) {
			message.warning('请选择一条或多条数据')
			return false
		}
		const params = selectedRowKeys.value.map((m) => {
			return {
				id: m
			}
		})
		mallGoodsApi.mallGoodsDelete(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
	// 批量导出校验并加参数
	const exportBatchGoodsVerify = () => {
		if (selectedRowKeys.value.length < 1) {
			message.warning('请输入查询条件或勾选要导出的信息')
		}
		if (selectedRowKeys.value.length > 0) {
			const params = {
				goodsIds: selectedRowKeys.value
					.map((m) => {
						return m
					})
					.join()
			}
			exportBatchUser(params)
			return
		}
	}
	// 批量导出
	const exportBatchUser = (params) => {
		mallGoodsApi.mallGoodsExport(params).then((res) => {
			downloadUtil.resultDownload(res)
			tableRef.value.clearSelected()
		})
	}
</script>
