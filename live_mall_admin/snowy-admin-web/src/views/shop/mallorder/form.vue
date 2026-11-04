<template>
	<a-drawer
		:title="formData.id ? '编辑订单' : '增加订单'"
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
						<a-textarea v-model:value="formData.orderNumber" placeholder="请输入订单编号" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="订单金额：" name="orderFee">
						<a-input-number v-model:value="formData.orderFee" :min="1" :max="10000" style="width: 100%" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="物流编号：" name="logisticsNumber">
						<a-textarea v-model:value="formData.logisticsNumber" placeholder="请输入物理编号" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="物流状态：" name="logisticsStatus">
						<a-select
							v-model:value="formData.logisticsStatus"
							placeholder="请选择状态"
							:options="logisticsStatusOptions"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="手机号：" name="mobile">
						<a-textarea v-model:value="formData.mobile" placeholder="请输入手机号" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="收货人昵称：" name="nickname">
						<a-textarea v-model:value="formData.nickname" placeholder="请输入收货人昵称" allow-clear />
					</a-form-item>
				</a-col>
<!--				<a-col :span="12">
					<a-form-item label="省：" name="provinceName">
						<a-textarea v-model:value="formData.provinceName" placeholder="" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="市：" name="cityName">
						<a-textarea v-model:value="formData.cityName" placeholder="" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="县区：" name="countiesName">
						<a-textarea v-model:value="formData.countiesName" placeholder="" allow-clear />
					</a-form-item>
				</a-col>-->
				<a-col :span="12">
					<a-form-item label="详细地址：" name="address">
						<a-textarea v-model:value="formData.address" placeholder="请输入详细地址" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="订单状态：" name="status">
						<a-select v-model:value="formData.status" placeholder="请选择状态" :options="formStatusOptions" />
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</a-drawer>
</template>

<script setup name="mallOrderForm">
	import tool from '@/utils/tool'
	import { cloneDeep, isEmpty } from 'lodash-es'
	import mallOrderApi from '@/api/shop/mallOrderApi'
	// 抽屉状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const formStatusOptions = ref([])
	const logisticsStatusOptions = ref([])

	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
			formStatusOptions.value = tool.dictList('ORDER_STATUS_TYPE')
			logisticsStatusOptions.value = tool.dictList('LOGISTICS_TYPE')
		}
	}
	// 关闭抽屉
	const onClose = () => {
		formRef.value.resetFields()
		formData.value = {}
		visible.value = false
	}
	// 默认要校验的
	const formRules = {}
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value.validate().then(() => {
			submitLoading.value = true
			const formDataParam = cloneDeep(formData.value)
			mallOrderApi
				.mallOrderSubmitForm(formDataParam, formDataParam.id)
				.then(() => {
					onClose()
					emit('successful')
				})
				.finally(() => {
					submitLoading.value = false
				})
		})
	}
	// 抛出函数
	defineExpose({
		onOpen
	})
</script>
