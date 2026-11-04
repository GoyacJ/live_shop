<template>
	<a-spin :spinning="loadSpinning">
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical" :label-col="labelCol">
			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item label="用户协议：" name="USER_AGREEMENT">
						<XnEditor v-model:value="formData.USER_AGREEMENT" placeholder="请输入用户协议内容" :height="300" />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item label="隐私政策：" name="PRIVACY_POLICY">
						<XnEditor v-model:value="formData.PRIVACY_POLICY" placeholder="请输入隐私政策内容" :height="300" />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item>
						<a-button type="primary" :loading="submitLoading" @click="onSubmit()">保存</a-button>
						<a-button class="xn-ml10" @click="resetForm" disabled>重置</a-button>
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
	</a-spin>
</template>

<script setup name="privacyAgreementConfig">
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import { message } from 'ant-design-vue'
	import { globalStore } from '@/store'
	import configApi from '@/api/dev/configApi'
	import XnEditor from '@/components/XnEditor/index.vue'
	// 定义emit事件
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据，也就是默认给一些数据
	const formData = ref({})
	const submitLoading = ref(false)
	const loadSpinning = ref(true)
	const store = globalStore()
	// 查询此界面的配置项,并转为表单
	const param = {
		category: 'PRIVACY_AGREEMENT'
	}
	configApi.configList(param).then((data) => {
		loadSpinning.value = false
		if (data) {
			data.forEach((item) => {
				if (['USER_AGREEMENT', 'PRIVACY_POLICY'].includes(item.configKey)) {
					formData.value[item.configKey] = item.configValue
				}
			})
		} else {
			message.warning('表单项不存在，请初始化数据库')
		}
	})

	// 默认要校验的
	const formRules = {
		USER_AGREEMENT: [required('请填写用户协议')],
		PRIVACY_POLICY: [required('请填写隐私政策')]
	}
	// 表单固定label实现
	const labelCol = ref({
		style: {
			width: '150px'
		}
	})
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value
			.validate()
			.then(() => {
				submitLoading.value = true
				let submitParam = cloneDeep(formData.value)
				const param = Object.entries(submitParam)
					.filter(([key]) => ['USER_AGREEMENT', 'PRIVACY_POLICY'].includes(key))
					.map((item) => {
						return {
							configKey: item[0],
							configValue: item[1]
						}
					})
				configApi
					.configEditForm(param)
					.then(() => {
						// 执行成功后改变界面使其生效
						/*tool.data.set('SNOWY_SYS_BASE_CONFIG', submitParam)
						store.setSysBaseConfig(submitParam)*/
					})
					.finally(() => {
						submitLoading.value = false
					})
			})
			.catch(() => {})
	}
	// 重置表单，且设置默认值
	const resetForm = () => {
		formRef.value.resetFields()
		formData.value = {}
	}
</script>
