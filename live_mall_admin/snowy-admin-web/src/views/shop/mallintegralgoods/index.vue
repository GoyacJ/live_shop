<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :rules="rules" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item label="积分商品标题" name="title">
						<a-input v-model:value="searchFormState.title" placeholder="请输入积分商品标题" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item label="市场价区间" class="__price-form-item">
						<a-form-item name="minMarketPrice">
							<a-input v-model:value="searchFormState.minMarketPrice" placeholder="最低价" allow-clear />
						</a-form-item>
						&ensp;-&ensp;
						<a-form-item name="maxMarketPrice" :colon="false">
							<a-input v-model:value="searchFormState.maxMarketPrice" placeholder="最高价" allow-clear />
						</a-form-item>
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item label="兑换所需积分" name="integral">
						<a-input v-model:value="searchFormState.integral" placeholder="请输入兑换所需积分" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="6" v-show="advanced">
					<a-form-item label="商品描述" name="detail">
						<a-input v-model:value="searchFormState.detail" placeholder="请输入商品描述" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="6" v-show="advanced">
					<a-form-item label="商品状态" name="status">
						<a-select v-model:value="searchFormState.status" placeholder="请选择商品状态" :options="statusOptions" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-button type="primary" @click="tableRef.refresh()">查询</a-button>
					<a-button style="margin: 0 8px" @click="reset">重置</a-button>
					<a @click="toggleAdvanced" style="margin-left: 8px">
						{{ advanced ? '收起' : '展开' }}
						<component :is="advanced ? 'up-outlined' : 'down-outlined'"/>
					</a>
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
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('mallIntegralGoodsAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						v-if="hasPerm('mallIntegralGoodsBatchDelete')"
						buttonName="批量删除"
                        icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchMallIntegralGoods"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'status'">
					{{ $TOOL.dictTypeData('whether_it_is_enabled', record.status) }}
				</template>
				<template v-if="column.dataIndex === 'cover'">
					<a-image-preview-group>
						<a-image :src="record.cover" style="margin-bottom: -5px; margin-top: -5px" height="100px" />
					</a-image-preview-group>
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)" v-if="hasPerm('mallIntegralGoodsEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['mallIntegralGoodsEdit', 'mallIntegralGoodsDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteMallIntegralGoods(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('mallIntegralGoodsDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="tableRef.refresh()" />
</template>

<script setup name="mallintegralgoods">
	import tool from '@/utils/tool'
	import { cloneDeep } from 'lodash-es'
	import Form from './form.vue'
	import mallIntegralGoodsApi from '@/api/shop/mallIntegralGoodsApi'
	const searchFormState = ref({})
	const searchFormRef = ref()
	const tableRef = ref()
	const formRef = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	// 查询区域显示更多控制
	const advanced = ref(false)
	const toggleAdvanced = () => {
		advanced.value = !advanced.value
	}
	const columns = [
		{
			title: '积分商品标题',
			dataIndex: 'title'
		},
		{
			title: '封面',
			dataIndex: 'cover'
		},
		{
			title: '市场价',
			dataIndex: 'marketPrice'
		},
		{
			title: '兑换所需积分',
			dataIndex: 'integral'
		},
		{
			title: '商品描述',
			dataIndex: 'detail'
		},
		{
			title: '库存',
			dataIndex: 'stock'
		},
		{
			title: '限购数量',
			dataIndex: 'userLimit'
		},
		{
			title: '商品状态',
			dataIndex: 'status'
		},
	]

	const rules = {
		minMarketPrice: [
			{
				required: false,
				message: '请输入数字，最多保留两位小数',
				pattern: /^[0-9]+(\.[0-9]{1,2})?$/,
				trigger: 'change'
			}
		],
		maxMarketPrice: [
			{
				required: false,
				message: '请输入数字，最多保留两位小数',
				pattern: /^[0-9]+(\.[0-9]{1,2})?$/,
				trigger: 'change'
			}
		],
		marketPrice: [
			{
				required: false,
				message: '请输入数字，最多保留两位小数',
				pattern: /^[0-9]+(\.[0-9]{1,2})?$/,
				trigger: 'change'
			}
		],
		integral: [
			{
				required: false,
				message: '请输入数字，最多保留两位小数',
				pattern: /^[0-9]+(\.[0-9]{1,2})?$/,
				trigger: 'change'
			}
		]
	}

	const validPrice = (key) => {
		if (/^[0-9]+(\.[0-9]{1,2})?$/.test(searchFormState.value[key])) {
			console.log('校验通过')
		} else {
			searchFormState.value[key] = null
		}
	}
	// 操作栏通过权限判断是否显示
	if (hasPerm(['mallIntegralGoodsEdit', 'mallIntegralGoodsDelete'])) {
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
		return mallIntegralGoodsApi.mallIntegralGoodsPage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}
	// 删除
	const deleteMallIntegralGoods = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		mallIntegralGoodsApi.mallIntegralGoodsDelete(params).then(() => {
			tableRef.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchMallIntegralGoods = (params) => {
		mallIntegralGoodsApi.mallIntegralGoodsDelete(params).then(() => {
			tableRef.value.clearRefreshSelected()
		})
	}
	const statusOptions = tool.dictList('whether_it_is_enabled')
</script>
<style>
 .__price-form-item .ant-form-item-control-input .ant-form-item-control-input-content {
	 display: flex;
 }
</style>
