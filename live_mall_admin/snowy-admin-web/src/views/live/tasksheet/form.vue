<template>
	<xn-form-container
		:title="formData.id ? '编辑任务表' : '增加任务表'"
		:width="700"
		v-model:open="open"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-form-item label="任务id：" name="taskId">
				<a-input v-model:value="formData.taskId" placeholder="请输入任务id" allow-clear />
			</a-form-item>
			<a-form-item label="任务类型：" name="taskType">
				<a-input v-model:value="formData.taskType" placeholder="请输入任务类型" allow-clear />
			</a-form-item>
			<a-form-item label="任务状态：" name="taskState">
				<a-input v-model:value="formData.taskState" placeholder="请输入任务状态" allow-clear />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="taskSheetForm">
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import taskSheetApi from '@/api/live/taskSheetApi'
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
				taskSheetApi
					.taskSheetSubmitForm(formDataParam, formDataParam.id)
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
