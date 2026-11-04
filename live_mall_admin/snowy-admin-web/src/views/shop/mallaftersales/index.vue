<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form mb-4">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item label="订单编号" name="orderNumber">
						<a-input v-model:value="searchFormState.orderNumber" placeholder="请输入订单编号" />
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
					<!--					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('mallAfterSalesAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						v-if="hasPerm('mallAfterSalesBatchDelete')"
						buttonName="批量删除"
						icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchAfterSales"
					/>-->
					<a-button type="primary" @click="showModal()">新增售后类型</a-button>
					<a-modal v-model:open="open" title="" @ok="handleOk()">
						<template #footer>
							<a-button key="back" @click="handleCancel">取消</a-button>
							<a-button key="submit" type="primary" :loading="loading" @click="onSubmit">确定</a-button>
						</template>
						<a-form-item label="售后类型名称：" name="afterSalesTypeLabel">
							<a-input v-model:value="textBox.dictLabel" placeholder="请输入售后类型名称" allow-clear/>
						</a-form-item>
<!--						<a-form-item label="售后类型值：" name="afterSalesTypeValue">
							<a-input v-model:value="textBox.dictValue" placeholder="请输入售后类型值" allow-clear/>
						</a-form-item>-->
					</a-modal>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'orderType'">
					{{ $TOOL.dictTypeData('MALL_ORDER_TYPE', record.orderType) }}
				</template>
				<template v-if="column.dataIndex === 'afterSaleStatus'">
					{{ $TOOL.dictTypeData('AFTER_SALE_TYPE', record.afterSaleStatus) }}
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)" v-if="hasPerm('mallAfterSalesEdit')">回复</a>
						<a-divider type="vertical" v-if="hasPerm(['mallAfterSalesEdit', 'mallAfterSalesDelete'], 'and')" />
						<!--						<a-popconfirm title="确定要删除吗？" @confirm="deleteAfterSales(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('mallAfterSalesDelete')">删除</a-button>
						</a-popconfirm>-->
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="mallAfterSales">
	import { message } from 'ant-design-vue'
	import Form from './form.vue'
	import mallAfterSalesApi from '@/api/shop/mallAfterSalesApi'
	import mallOrderApi from '@/api/shop/mallOrderApi'
	import mallSalesTypeApi from '@/api/shop/mallSalesTypeApi'
	import { ref } from 'vue'
	import tool from '@/utils/tool'
	let searchFormState = reactive({})
	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()
	const orderListData = ref({})
	const salesTypeListData = ref({})
	const afterSalesData = ref({})
	const loading = ref(false)
	const open = ref(false)
	const textBox = ref({})
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		{
			title: '订单编号',
			dataIndex: 'orderNumber'
		},
		{
			title: '订单类型',
			dataIndex: 'orderType'
		},
		{
			title: '反馈信息',
			dataIndex: 'feedbackMessage'
		},
		{
			title: '回复信息',
			dataIndex: 'replyMessage'
		},
		{
			title: '售后类型',
			dataIndex: 'dictLabel'
		},
		{
			title: '处理状态',
			dataIndex: 'afterSaleStatus'
		},
		{
			title: '创建时间',
			dataIndex: 'createTime'
		}
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['mallAfterSalesEdit', 'mallAfterSalesDelete'])) {
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
	const showModal = () => {
		open.value = true
	}
	const onSubmit = () => {
		//自动生成textBox.dictValue
		textBox.value.dictValue = textBox.value.dictLabel + '_' + tool.generateString(3)
		mallSalesTypeApi.mallSalesTypeAdd(textBox.value).then(() => {
			open.value = false
			table.value.refresh(true)
		})
	}
	const handleCancel = () => {
		open.value = false
	}
	const handleOk = (record) => {
		loading.value = true
		setTimeout(() => {
			loading.value = false
			open.value = false
		}, 2000)
	}
	const loadData = (parameter) => {
		const searchFormParam = JSON.parse(JSON.stringify(searchFormState))
		// createTime范围查询条件重载
		if (searchFormParam.createTime) {
			searchFormParam.startCreateTime = searchFormParam.createTime[0]
			searchFormParam.endCreateTime = searchFormParam.createTime[1]
			delete searchFormParam.createTime
		}
		//查询售后类型信息
		mallSalesTypeApi.mallSalesTypeList(Object.assign(parameter, searchFormParam)).then((data) => {
			salesTypeListData.value = data
		})
		//查询订单信息
		mallOrderApi.mallOrderList(Object.assign(parameter, searchFormParam)).then((data) => {
			orderListData.value = data
		})
		return mallAfterSalesApi.mallAfterSalesPage(Object.assign(parameter, searchFormParam)).then((data) => {
			afterSalesData.value = data
			afterSalesData.value.records.forEach((item) => {
				/*// 拼接订单信息
				orderListData.value.forEach((order) => {
					if (order.id === item.orderId) {
						item.orderNumber = order.orderNumber
					}
				})*/
				//拼接售后类型信息
				salesTypeListData.value.forEach((salesType) => {
					if (salesType.dictValue === item.afterSaleType) {
						item.dictLabel = salesType.dictLabel
					}
				})
			})
			return afterSalesData.value
		})
	}
	// 删除
	const deleteAfterSales = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		mallAfterSalesApi.mallAfterSalesDelete(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchAfterSales = () => {
		if (selectedRowKeys.value.length < 1) {
			message.warning('请选择一条或多条数据')
			return false
		}
		const params = selectedRowKeys.value.map((m) => {
			return {
				id: m
			}
		})
		mallAfterSalesApi.mallAfterSalesDelete(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
</script>
