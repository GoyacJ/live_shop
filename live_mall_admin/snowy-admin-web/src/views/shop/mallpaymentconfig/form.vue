<template>
	<a-drawer
		:title="formData.id ? '编辑支付配置信息' : '增加支付配置信息'"
		:width="900"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="支付名称：" name="paymentName">
						<a-input v-model:value="formData.paymentName" placeholder="请输入支付名称" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="支付类型：" name="paymentValue">
						<a-select v-model:value="formData.paymentValue" placeholder="请选择状态" :options="paymentValueOptions" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="商户id：" name="mchid">
						<a-input v-model:value="formData.mchid" placeholder="请输入商户id" allow-clear />
					</a-form-item>
				</a-col>
<!--				<a-col :span="12">
					<a-form-item label="应用id：" name="appId">
						<a-textarea v-model:value="formData.appId" placeholder="请输入物流状态" allow-clear />
					</a-form-item>
				</a-col>-->
				<a-col :span="12">
					<a-form-item label="支付平台公钥：" name="publicKey">
						<a-input v-model:value="formData.publicKey" placeholder="请输入支付平台公钥" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="支付平台私钥：" name="privateKey">
						<a-input v-model:value="formData.privateKey" placeholder="请输入支付平台私钥" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="异步回调地址：" name="notifyUrl">
						<a-input v-model:value="formData.notifyUrl" placeholder="请输入异步回调地址" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="同步回调地址：" name="returnUrl">
						<a-input v-model:value="formData.returnUrl" placeholder="请输入同步回调地址" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="收款账号：" name="seller">
						<a-input v-model:value="formData.seller" placeholder="请输入收款账号" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="签名：" name="sign">
						<a-input v-model:value="formData.sign" placeholder="请输入签名" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="签名类型：" name="signType">
						<a-select v-model:value="formData.signType" placeholder="请选择签名类型" :options="signTypeOptions" />
					</a-form-item>
				</a-col>
<!--				<a-col :span="12">
					<a-form-item label="字符类型：" name="inputCharset">
						<a-textarea v-model:value="formData.inputCharset" placeholder="请输入物流状态" allow-clear />
					</a-form-item>
				</a-col>-->
<!--				<a-col :span="12">
					<a-form-item label="消息类型：" name="msgType">
						<a-textarea v-model:value="formData.msgType" placeholder="请输入物流状态" allow-clear />
					</a-form-item>
				</a-col>-->
				<a-col :span="12">
					<a-form-item label="证书地址：" name="keystorePath">
						<a-input v-model:value="formData.keystorePath" placeholder="请输入证书地址" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="证书密码：" name="storePassword">
						<a-input v-model:value="formData.storePassword" placeholder="请输入证书密码" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="差异字段：" name="fieldJson">
						<a-textarea v-model:value="formData.inputCharset" placeholder="请输入差异字段" allow-clear />
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

<script setup name="mallPaymentConfigForm">
	import tool from '@/utils/tool'
	import { cloneDeep, isEmpty } from 'lodash-es'
	import mallPatymentConfigApi from '@/api/shop/mallPaymentConfigApi'
	// 抽屉状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const paymentValueOptions = ref([])
	const signTypeOptions = ref([])

	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
		}
		paymentValueOptions.value = tool.dictList('PAYMENT_SIGN')
		signTypeOptions.value = tool.dictList('SIGN_TYPE')
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
			mallPatymentConfigApi
				.mallPaymentConfigSubmitForm(formDataParam, formDataParam.id)
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
