<template>
	<a-drawer
		:title="formData.id ? '编辑反馈意见信息' : '增加反馈意见信息'"
		:width="900"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="反馈类型：" name="feedbackType">
						<a-select v-model:value="formData.feedbackType" placeholder="请选择状态" :options="feedbackOptions" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="反馈信息：" name="feedbackMessage">
						<a-textarea v-model:value="formData.feedbackMessage" placeholder="请输入支付标识" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="手机号：" name="mobile">
						<a-textarea v-model:value="formData.mobile" placeholder="请输入物流状态" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="处理状态：" name="status">
						<a-select v-model:value="formData.status" placeholder="请选择状态" :options="statusOptions" />
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

<script setup name="feedbackForm">
	import { cloneDeep, isEmpty } from 'lodash-es'
	import feedbackApi from '@/api/manage/feedbackApi'
	import tool from '@/utils/tool'
	// 抽屉状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const feedbackOptions = ref([])
	const statusOptions = ref([])
	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
		}
		feedbackOptions.value = tool.dictList('FEEDBACK_TYPE')
		statusOptions.value = tool.dictList('FEEDBACK_STATUS')
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
			feedbackApi
				.feedbackSubmitForm(formDataParam, formDataParam.id)
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
