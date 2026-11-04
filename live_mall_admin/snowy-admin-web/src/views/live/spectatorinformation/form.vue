<template>
	<xn-form-container
		:title="formData.id ? '编辑观众信息表' : '增加观众信息表'"
		:width="700"
		v-model:open="open"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-form-item label="观众id：" name="spectatorId">
				<a-input v-model:value="formData.spectatorId" placeholder="请输入观众id" allow-clear />
			</a-form-item>
			<a-form-item label="主播id：" name="anchorId">
				<a-input v-model:value="formData.anchorId" placeholder="请输入主播id" allow-clear />
			</a-form-item>
			<a-form-item label="观众等级：" name="spectatorGrade">
				<a-input v-model:value="formData.spectatorGrade" placeholder="请输入观众等级" allow-clear />
			</a-form-item>
			<a-form-item label="成长值：" name="growthValue">
				<a-input v-model:value="formData.growthValue" placeholder="请输入成长值" allow-clear />
			</a-form-item>
			<a-form-item label="总消费：" name="totalConsumption">
				<a-input v-model:value="formData.totalConsumption" placeholder="请输入总消费" allow-clear />
			</a-form-item>
			<a-form-item label="观看时长：" name="watchingTime">
				<a-input v-model:value="formData.watchingTime" placeholder="请输入观看时长" allow-clear />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="spectatorInformationForm">
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import spectatorInformationApi from '@/api/live/spectatorInformationApi'
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
				spectatorInformationApi
					.spectatorInformationSubmitForm(formDataParam, formDataParam.id)
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
