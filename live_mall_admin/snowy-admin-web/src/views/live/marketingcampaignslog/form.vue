<template>
	<xn-form-container
		:title="formData.id ? '编辑营销活动记录表' : '增加营销活动记录表'"
		:width="700"
		v-model:open="open"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-form-item label="活动id：" name="marketingId">
				<a-input v-model:value="formData.marketingId" placeholder="请输入活动id" allow-clear />
			</a-form-item>
			<a-form-item label="用户id：" name="userId">
				<a-input v-model:value="formData.userId" placeholder="请输入用户id" allow-clear />
			</a-form-item>
			<a-form-item label="营销类型：" name="marketingType">
				<a-input v-model:value="formData.marketingType" placeholder="请输入营销类型" allow-clear />
			</a-form-item>
			<a-form-item label="营销价值：" name="marketingValue">
				<a-input v-model:value="formData.marketingValue" placeholder="请输入营销价值" allow-clear />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="liveMarketingCampaignsLogForm">
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import liveMarketingCampaignsLogApi from '@/api/live/liveMarketingCampaignsLogApi'
	// 抽屉状态
	const open = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)

	// 打开抽屉
	const onOpen = (record) => {
		open.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
		}
	}
	// 关闭抽屉
	const onClose = () => {
		formRef.value.resetFields()
		formData.value = {}
		open.value = false
	}
	// 默认要校验的
	const formRules = {
	}
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value
			.validate()
			.then(() => {
				submitLoading.value = true
				const formDataParam = cloneDeep(formData.value)
				liveMarketingCampaignsLogApi
					.liveMarketingCampaignsLogSubmitForm(formDataParam, formDataParam.id)
					.then(() => {
						onClose()
						emit('successful')
					})
					.finally(() => {
						submitLoading.value = false
					})
			})
			.catch(() => {})
	}
	// 抛出函数
	defineExpose({
		onOpen
	})
</script>
