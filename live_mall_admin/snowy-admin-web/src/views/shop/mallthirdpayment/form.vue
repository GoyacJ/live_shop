<template>
	<a-drawer
		:title="formData.id ? '编辑支付信息' : '增加支付信息'"
		:width="900"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="订单id：" name="orderId">
						<a-textarea v-model:value="formData.orderId" placeholder="请输入订单编号" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="公众号id：" name="appid">
						<a-input-number v-model:value="formData.appid" :min="1" :max="10000" style="width: 100%" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="商户id：" name="mchid">
						<a-textarea v-model:value="formData.mchid" placeholder="请输入物理编号" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="商户服务订单号：" name="outOrderNo">
						<a-textarea v-model:value="formData.outOrderNo" placeholder="请输入物流状态" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="支付金额：" name="totalFee">
						<a-textarea v-model:value="formData.totalFee" placeholder="请输入手机号" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="支付类型：" name="payType">
						<a-textarea v-model:value="formData.payType" placeholder="请输入收货人昵称" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="24">
					<a-form-item label="支付状态：" name="status">
						<xn-editor v-model:value="formData.status" placeholder="" />
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

<script setup name="mallThirdPaymentForm">
	import tool from '@/utils/tool'
	import { cloneDeep, isEmpty } from 'lodash-es'
	import mallThirdPaymentApi from '@/api/shop/mallThirdPaymentApi'
	// 抽屉状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const statusOptions = ref([])

	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
			statusOptions.value = tool.dictList('GOODS_STATUS')
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
			mallThirdPaymentApi
				.mallThirdPaymentSubmitForm(formDataParam, formDataParam.id)
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
