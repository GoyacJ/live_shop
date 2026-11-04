<template>
	<xn-form-container
		:title="formData.id ? '编辑主播信息表' : '增加主播信息表'"
		:width="700"
		v-model:open="open"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-form-item label="主播name：" name="username">
				<a-input v-model:value="formData.username" placeholder="请输入主播name" allow-clear />
			</a-form-item>
			<a-form-item label="昵称：" name="realName">
				<a-input v-model:value="formData.realName" placeholder="请输入昵称" allow-clear />
			</a-form-item>
			<a-form-item label="账号：" name="account">
				<a-input v-model:value="formData.account" placeholder="请输入账号" allow-clear />
			</a-form-item>
			<a-form-item label="密码：" name="password">
				<a-input v-model:value="formData.password" placeholder="请输入密码" allow-clear />
			</a-form-item>
			<a-form-item label="头像url：" name="pictureUrl">
				<a-input v-model:value="formData.pictureUrl" placeholder="请输入头像url" allow-clear />
			</a-form-item>
			<a-form-item label="简介：" name="bio">
				<a-input v-model:value="formData.bio" placeholder="请输入简介" allow-clear />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="anchorForm">
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import anchorApi from '@/api/live/anchorApi'
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
				anchorApi
					.anchorSubmitForm(formDataParam, formDataParam.id)
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
