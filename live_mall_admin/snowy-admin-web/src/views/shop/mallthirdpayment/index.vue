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
				<a-col :span="4">
					<a-form-item name="status" label="支付状态">
						<a-select
							v-model:value="searchFormState.status"
							placeholder="请选择支付状态"
							:options="statusOptions"
							:getPopupContainer="(trigger) => trigger.parentNode"
						/>
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
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('mallThirdPaymentAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						v-if="hasPerm('mallThirdPaymentBatchDelete')"
						buttonName="批量删除"
						icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchThirdPayment"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'payType'">
					{{ $TOOL.dictTypeData('PAYMENT_SIGN', record.payType) }}
				</template>
				<template v-if="column.dataIndex === 'status'">
					{{ $TOOL.dictTypeData('PAYMENT_TYPE', record.status) }}
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)" v-if="hasPerm('mallThirdPaymentEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['mallThirdPaymentEdit', 'mallThirdPaymentDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteThirdPayment(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('mallThirdPaymentDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="mallThirdPayment">
	import { message } from 'ant-design-vue'
	import Form from './form.vue'
	import mallThirdPaymentApi from '@/api/shop/mallThirdPaymentApi'
	import mallOrderApi from '@/api/shop/mallOrderApi'
	import tool from '@/utils/tool'
	let searchFormState = reactive({})
	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()
	const orderListData = ref({})
	const thirdPaymentData = ref({})
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		{
			title: '订单编号',
			dataIndex: 'orderNumber'
		},
		{
			title: '公众号id',
			dataIndex: 'appid'
		},
		{
			title: '商户id',
			dataIndex: 'mchid'
		},
		{
			title: '商户服务订单号',
			dataIndex: 'outOrderNo'
		},
		{
			title: '支付金额',
			dataIndex: 'totalFee'
		},
		{
			title: '支付类型',
			dataIndex: 'payType'
		},
		{
			title: '支付状态',
			dataIndex: 'status'
		},
		{
			title: '创建时间',
			dataIndex: 'createTime'
		}
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['mallThirdPaymentEdit', 'mallThirdPaymentDelete'])) {
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
		//查询订单信息
		mallOrderApi.mallOrderList(Object.assign(parameter, searchFormParam)).then((data) => {
			orderListData.value = data
		})
		return mallThirdPaymentApi.mallThirdPaymentPage(Object.assign(parameter, searchFormParam)).then((data) => {
			thirdPaymentData.value = data
			thirdPaymentData.value.records.forEach((item) => {
				// 拼接订单信息
				orderListData.value.forEach((order) => {
					if (order.id === item.orderId) {
						item.orderNumber = order.orderNumber
					}
				})
			})
			return thirdPaymentData.value
		})
	}
	// 删除
	const deleteThirdPayment = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		mallThirdPaymentApi.mallThirdPaymentDelete(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchThirdPayment = () => {
		if (selectedRowKeys.value.length < 1) {
			message.warning('请选择一条或多条数据')
			return false
		}
		const params = selectedRowKeys.value.map((m) => {
			return {
				id: m
			}
		})
		mallThirdPaymentApi.mallThirdPaymentDelete(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
	const statusOptions = tool.dictList('PAYMENT_TYPE')
</script>
