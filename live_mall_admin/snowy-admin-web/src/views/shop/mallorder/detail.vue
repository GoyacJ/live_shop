<template>
	<a-drawer
		title="订单信息"
		:width="900"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="订单编号：" name="orderNumber">
						<a-textarea v-model:value="formData.orderNumber" placeholder="请输入订单编号" disabled />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="订单金额：" name="orderFee">
						<a-input-number v-model:value="formData.orderFee" :min="1" :max="10000" style="width: 100%" disabled />
					</a-form-item>
				</a-col>
<!--				<a-col :span="12">-->
<!--					<a-form-item label="物流编号：" name="logisticsNumber">-->
<!--						<a-textarea v-model:value="formData.logisticsNumber" placeholder="请输入物理编号" disabled />-->
<!--					</a-form-item>-->
<!--				</a-col>-->
<!--				<a-col :span="12">-->
<!--					<a-form-item label="物流状态：" name="logisticsStatus">-->
<!--						<a-select-->
<!--							v-model:value="formData.logisticsStatus"-->
<!--							placeholder="请选择状态"-->
<!--							:options="logisticsStatusOptions"-->
<!--							disabled-->
<!--						/>-->
<!--					</a-form-item>-->
<!--				</a-col>-->
				<a-col :span="12">
					<a-form-item label="手机号：" name="mobile">
						<a-textarea v-model:value="formData.mobile" placeholder="请输入手机号" disabled />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="收货人昵称：" name="nickname">
						<a-textarea v-model:value="formData.nickname" placeholder="请输入收货人昵称" disabled />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="详细地址：" name="address">
						<a-textarea v-model:value="formData.address" placeholder="请输入详细地址" disabled />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="订单状态：" name="status">
						<a-select
							v-model:value="formData.status"
							placeholder="请选择状态"
							:options="formStatusOptions"
							:disabled="!isAdmin"
						/>
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
		<a-table :columns="columns" :data-source="mallGoodsList" size="small" :pagination="false" :scroll="{ y: 260 }">
			<template #title><span style="font-size: 15px; font-weight: 600">&map;订单详情</span></template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'goodsCover'">
					<a-image :src="record.goodsCover" style="margin-bottom: -5px; margin-top: -5px; width: 50px" />
				</template>
			</template>
		</a-table>

		<!-- 物流信息区域 -->
		<div v-if="logisticsData && logisticsData.success === true" style="margin-top: 20px">
			<div style="font-size: 15px; font-weight: 600; margin-bottom: 16px">物流信息</div>
			<a-descriptions bordered :column="2">
				<a-descriptions-item label="快递公司" :span="1">{{ logisticsData.shipperCode }}</a-descriptions-item>
				<a-descriptions-item label="物流单号" :span="1">{{ logisticsData.logisticCode }}</a-descriptions-item>
				<a-descriptions-item label="配送员" :span="1">{{ logisticsData.deliveryMan }}</a-descriptions-item>
				<a-descriptions-item label="配送员电话" :span="1">{{ logisticsData.deliveryManTel }}</a-descriptions-item>
				<a-descriptions-item label="当前状态" :span="2">
					<a-tag :color="getStateColor(logisticsData.state)">{{ logisticsData.state }}</a-tag>
					{{ logisticsData.stateEx }}
				</a-descriptions-item>
			</a-descriptions>

			<!-- 物流轨迹时间轴 -->
			<div style="margin-top: 16px">
				<a-timeline mode="left">
					<a-timeline-item
						v-for="(item, index) in logisticsData.traces"
						:key="index"
						:color="index === 0 ? 'green' : 'blue'"
					>
						<template #dot v-if="item.action === '正常签收'">
							<CheckCircleOutlined style="fontsize: 16px" />
						</template>
						<div style="font-weight: 500">{{ item.acceptTime }}</div>
						<div>{{ item.acceptStation }}</div>
						<div v-if="item.location">{{ item.location }}</div>
					</a-timeline-item>
				</a-timeline>
			</div>
		</div>
		<div v-else-if="logisticsLoading" style="margin-top: 20px; text-align: center">
			<a-spin tip="加载物流信息中..." />
		</div>
		<div v-else-if="formData.orderNumber" style="margin-top: 20px">
			<a-empty description="暂无物流信息" />
		</div>

		<template #footer>
			<a-space v-if="isAdmin">
				<a-button @click="onClose">取消</a-button>
				<a-button type="primary" :loading="submitLoading" @click="onSubmit">保存</a-button>
			</a-space>
			<a-button v-else @click="onClose">关闭</a-button>
		</template>
	</a-drawer>
</template>

<script setup name="mallOrderReadonlyForm">
	import tool from '@/utils/tool'
	import { cloneDeep, isEmpty } from 'lodash-es'
	import mallOrderGoodsApi from '@/api/shop/mallOrderGoodsApi'
	import mallOrderLogisticsApi from '@/api/shop/mallOrderLogisticsApi'
	import mallOrderApi from '@/api/shop/mallOrderApi'
	import { CheckCircleOutlined } from '@ant-design/icons-vue'
	import { message } from 'ant-design-vue'

	// 抽屉状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const mallGoodsList = ref([])
	const submitLoading = ref(false)
	const formStatusOptions = ref([])
	const logisticsStatusOptions = ref([])
	// 物流信息
	const logisticsData = ref({
		success: false,
		traces: []
	})
	const logisticsLoading = ref(false)
	// 判断当前用户是否为管理员
	const isAdmin = ref(false)

	// 表格列
	const columns = [
		{
			title: '商品名称',
			dataIndex: 'goodsTitle'
		},
		{
			title: '商品图片',
			dataIndex: 'goodsCover'
			// customRender: ({text, record, index, column}) => {
			// 	return `<img src="${record[column['dataIndex']]}" style="width: 40px; height: 40px" />`
			// }
		},
		{
			title: '商品价格',
			dataIndex: 'goodsPrice'
		},
		{
			title: '商品数量',
			dataIndex: 'goodsCount'
		}
	]

	// 根据物流状态获取标签颜色
	const getStateColor = (state) => {
		const stateColorMap = {
			签收: 'success',
			派送中: 'processing',
			运输中: 'blue',
			揽收: 'warning',
			退回: 'error'
		}
		return stateColorMap[state] || 'default'
	}

	// 获取物流详情
	const getLogisticsDetail = (orderNumber) => {
		if (!orderNumber) return

		logisticsLoading.value = true
		logisticsData.value = { success: false, traces: [] }

		mallOrderLogisticsApi
			.detail({ orderNumber })
			.then((res) => {
				// 直接将返回的数据赋值给logisticsData
				logisticsData.value = res || { success: false, traces: [] }
				console.log('物流数据已更新:', logisticsData.value)
				// 确保物流轨迹按时间倒序排列（最新的在前面）
				if (logisticsData.value.traces && logisticsData.value.traces.length > 0) {
					logisticsData.value.traces.sort((a, b) => {
						return new Date(b.acceptTime) - new Date(a.acceptTime)
					})
				}
			})
			.catch((error) => {
				console.error('获取物流信息失败:', error)
				logisticsData.value = { success: false, traces: [] }
			})
			.finally(() => {
				logisticsLoading.value = false
			})
	}

	// 判断当前用户是否有权限修改订单状态
	const checkAdminPermission = () => {
		// 检查用户是否有修改订单的权限
		const userInfo = tool.data.get('USER_INFO')
		if (!userInfo) return false

		// 检查是否有订单编辑权限
		const { buttonCodeList } = userInfo
		if (!buttonCodeList) return false

		// 判断是否有订单编辑权限
		isAdmin.value = buttonCodeList.includes('mallOrderEdit') || buttonCodeList.includes('mallGoodsEdit')
	}

	// 打开抽屉
	const onOpen = (record, type) => {
		checkAdminPermission()
		visible.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
			formStatusOptions.value = tool.dictList('ORDER_STATUS_TYPE')
			logisticsStatusOptions.value = tool.dictList('LOGISTICS_TYPE')

			// 获取商品详情
			mallOrderGoodsApi.mallOrderGoodsListByOrderId({ orderId: record.id }).then((res) => {
				mallGoodsList.value = res
			})

			// 获取物流信息
			getLogisticsDetail(record.orderNumber)
		}
	}

	// 关闭抽屉
	const onClose = () => {
		formRef.value.resetFields()
		formData.value = {}
		logisticsData.value = { success: false, traces: [] }
		logisticsLoading.value = false
		visible.value = false
	}

	// 验证并提交表单
	const onSubmit = () => {
		if (!isAdmin.value) {
			message.warning('您没有权限执行此操作')
			return
		}

		formRef.value.validate().then(() => {
			submitLoading.value = true

			// 只提交必要的字段
			const updateData = {
				id: formData.value.id,
				status: formData.value.status
			}

			mallOrderApi
				.mallOrderEdit(updateData)
				.then(() => {
					message.success('订单状态修改成功')
					onClose()
					emit('successful')
				})
				.catch((error) => {
					console.error('订单状态修改失败:', error)
					message.error('订单状态修改失败')
				})
				.finally(() => {
					submitLoading.value = false
				})
		})
	}

	// 默认要校验的
	const formRules = {}
	// 抛出函数
	defineExpose({
		onOpen
	})
</script>
