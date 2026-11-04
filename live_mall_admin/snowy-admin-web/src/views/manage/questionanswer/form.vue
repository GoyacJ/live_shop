<template>
	<a-drawer
		:title="formData.id ? '编辑问题答案' : '增加问题答案'"
		:width="600"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-form-item label="问题：" name="questionContent">
				<a-textarea
					v-model:value="formData.questionContent"
					placeholder="请输入详细地址"
					:auto-size="{ minRows: 3, maxRows: 5 }"
				/>
			</a-form-item>
			<a-form-item label="答案：" name="answerContent">
				<a-textarea
					v-model:value="formData.answerContent"
					placeholder="请输入详细地址"
					:auto-size="{ minRows: 3, maxRows: 5 }"
				/>
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</a-drawer>
</template>

<script setup name="questionAnswerForm">
	import tool from '@/utils/tool'
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import userAddressApi from '@/api/manage/userAddressApi'
	import userQuestionAnswerApi from "@/api/manage/userQuestionAnswerApi";
	// 抽屉状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const isDefaultOptions = ref([])

	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
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
			userQuestionAnswerApi
				.questionAnswerSubmitForm(formDataParam, formDataParam.id)
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
