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
			><!--
			<template #operator class="table-operator">
				<a-space>
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('mallGoodsAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						v-if="hasPerm('mallGoodsBatchDelete')"
						buttonName="批量删除"
						icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchOrder"
					/>
				</a-space>
			</template>-->
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'status'">
					{{ $TOOL.dictTypeData('ORDER_STATUS_TYPE', record.status) }}
				</template>
				<template v-if="column.dataIndex === 'logisticsStatus'">
					{{ $TOOL.dictTypeData('LOGISTICS_TYPE', record.logisticsStatus) }}
				</template>
				<template v-if="column.dataIndex === 'action'">
					<!--					<a-space>
						<a @click="formRef.onOpen(record)" v-if="hasPerm('mallGoodsEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['mallGoodsEdit', 'mallGoodsDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteOrder(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('mallGoodsDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>-->
					<a-button type="primary" @click="showModal(record)">设置物流编号</a-button>
					<a-button type="link" @click="detailFormRef.onOpen(record)">详情</a-button>
				</template>
			</template>
		</s-table>
	</a-card>
	<a-modal v-model:open="open" title="" @ok="handleOk">
		<template #footer>
			<a-button key="back" @click="handleCancel">取消</a-button>
			<a-button key="submit" type="primary" :loading="loading" @click="onSubmit">确定</a-button>
		</template>
		<a-form-item label="物流编号：" name="logisticsNumber">
			<a-textarea v-model:value="textBox.logisticsNumber" placeholder="请输入物流编号" allow-clear />
		</a-form-item>
	</a-modal>
	<Form ref="formRef" @successful="table.refresh(true)" />
	<Detail ref="detailFormRef" @successful="table.refresh(true)" />
</template>

<script setup name="mallOrder">
	import { message } from 'ant-design-vue'
	import Form from './form.vue'
	import Detail from './detail.vue'
	import mallOrderApi from '@/api/shop/mallOrderApi'
	let searchFormState = reactive({})
	const textBox = ref({})
	const searchFormRef = ref()
	const detailFormRef = ref()
	const table = ref()
	const formRef = ref()
	const orderData = ref({})

	import { ref } from 'vue'
	import { cloneDeep } from 'lodash-es'
	const loading = ref(false)
	const open = ref(false)

	const showModal = (record) => {
		orderData.value = cloneDeep(record)
		open.value = true
	}

	const handleOk = () => {
		loading.value = true
		setTimeout(() => {
			loading.value = false
			open.value = false
		}, 2000)
	}

	const handleCancel = () => {
		open.value = false
	}

	const onSubmit = () => {
		let recordData = cloneDeep(orderData.value)
		recordData.logisticsNumber = textBox.value.logisticsNumber
		mallOrderApi.mallOrderEdit(recordData).then(() => {
			open.value = false
			table.value.refresh(true)
		})
	}

	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		{
			title: '订单编号',
			dataIndex: 'orderNumber'
		},
		{
			title: '订单价格',
			dataIndex: 'orderFee'
		},
		{
			title: '物流编号',
			dataIndex: 'logisticsNumber'
		},
		// {
		// 	title: '物流状态',
		// 	dataIndex: 'logisticsStatus'
		// },
		{
			title: '下单手机号',
			dataIndex: 'mobile'
		},
		{
			title: '收货人昵称',
			dataIndex: 'nickname'
		},
		{
			title: '收货地址',
			dataIndex: 'address'
		},
		{
			title: '订单状态',
			dataIndex: 'status'
		},
		{
			title: '创建时间',
			dataIndex: 'createTime'
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
		//查询订单表信息
		return mallOrderApi.mallOrderPage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 删除
	const deleteOrder = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		mallOrderApi.mallOrderDelete(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchOrder = () => {
		if (selectedRowKeys.value.length < 1) {
			message.warning('请选择一条或多条数据')
			return false
		}
		const params = selectedRowKeys.value.map((m) => {
			return {
				id: m
			}
		})
		mallOrderApi.mallOrderDelete(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
</script>
