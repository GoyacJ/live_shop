<template>
	<a-spin :spinning="loadSpinning">
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical" :label-col="labelCol">
			<a-row :gutter="16">
				<a-col :span="3">
					<a-form-item label="客服二维码：" name="CUSTOMER_QR_CODE">
						<a-upload
							v-model:file-list="formData.CUSTOMER_QR_CODE"
							class="avatar-uploader"
							list-type="picture-card"
							:show-upload-list="false"
							:custom-request="customRequest"
							accept="image/png, image/jpeg, image/jpg"
						>
							<img v-if="imageUrl" :src="imageUrl" alt="avatar" style="max-height: 100px; max-width: 100px" />
							<div v-else>
								<plus-outlined />
								<div class="ant-upload-text">上传</div>
							</div>
						</a-upload>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="8">
					<a-form-item label="客服电话：" name="CESTOMER_MOBILE">
						<a-input v-model:value="formData.CESTOMER_MOBILE" placeholder="请输入系统名称" />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item>
						<a-button type="primary" :loading="submitLoading" @click="onSubmit()">保存</a-button>
						<a-button class="xn-ml10" @click="resetForm">重置</a-button>
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
	</a-spin>
</template>

<script setup name="customerConfig">
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import { message } from 'ant-design-vue'
	import { globalStore } from '@/store'
	import configApi from '@/api/dev/configApi'
	import tool from '@/utils/tool'
	// 定义emit事件
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据，也就是默认给一些数据
	const formData = ref({})
	const submitLoading = ref(false)
	const imageUrl = ref('')
	const menuTreeSelectRef = ref()
	const loadSpinning = ref(true)
	const store = globalStore()
	// 查询此界面的配置项,并转为表单
	const param = {
		category: 'CUSTOMER'
	}
	configApi.configList(param).then((data) => {
		loadSpinning.value = false
		if (data) {
			data.forEach((item) => {
				// 如果是系统的logo，它这个组件要一个数组，我将其单独处理
				if (item.configKey === 'CUSTOMER_QR_CODE') {
					formData.value[item.configKey] = [item.configValue]
					// 让其回显
					imageUrl.value = item.configValue
				} else {
					formData.value[item.configKey] = item.configValue
				}
			})
		} else {
			message.warning('表单项不存在，请初始化数据库')
		}
	})

	const customRequest = (data) => {
		formData.value.CUSTOMER_QR_CODE = ref([])
		getBase64(data.file)
			.then((res) => {
				imageUrl.value = res
				formData.value.CUSTOMER_QR_CODE.push(res)
			})
			.catch((err) => {})
	}

	// 文件转base64，用于显示图片
	const getBase64 = (file) => {
		return new Promise((resolve, reject) => {
			// FileReader类就是专门用来读文件的
			const reader = new FileReader()
			reader.readAsDataURL(file)
			// 成功和失败返回对应的信息，reader.result一个base64，可以直接使用
			reader.onload = () => resolve(reader.result)
			// 失败返回失败的信息
			reader.onerror = (error) => reject(error)
		})
	}

	// 默认要校验的
	const formRules = {
		CUSTOMER_QR_CODE: [required('请上传客服二维码')]
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
				submitParam.CUSTOMER_QR_CODE = submitParam.CUSTOMER_QR_CODE[0]
				const param = Object.entries(submitParam).map((item) => {
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
		imageUrl.value = ''
		/*formData.value = {
			SNOWY_SYS_DEFAULT_CAPTCHA_OPEN: 'true',
			SNOWY_SYS_DEFAULT_FILE_ENGINE: 'LOCAL'
		}*/
	}
</script>
