<template>
	<a-modal
		:title="formState.isUpdate ? '编辑版本' : '新增版本'"
		:width="620"
		:visible="visible"
		:confirm-loading="submitLoading"
		:mask-closable="false"
		@cancel="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" :label-col="{ span: 5 }" :wrapper-col="{ span: 19 }">
			<a-form-item label="版本名" name="versionName">
				<a-input v-model:value="formData.versionName" placeholder="请输入版本名，例如：1.0.0" allow-clear />
			</a-form-item>
			<a-form-item label="版本号" name="versionCode">
				<a-input v-model:value="formData.versionCode" placeholder="请输入版本号" allow-clear />
			</a-form-item>
			<a-form-item label="版本类型" name="appType">
				<a-select v-model:value="formData.appType" placeholder="请选择版本类型" allow-clear>
					<a-select-option value="Android">Android</a-select-option>
					<a-select-option value="iOS">iOS</a-select-option>
				</a-select>
			</a-form-item>
			<a-form-item label="安装包上传" name="appFile">
				<div class="upload-container">
					<a-upload
						:file-list="fileList"
						:before-upload="beforeUpload"
						:remove="handleRemove"
						:maxCount="1"
						:showUploadList="true"
						:accept="formData.appType === 'Android' ? '.apk' : '.ipa'"
					>
						<a-button :loading="uploading">
							<upload-outlined />
							上传安装包
						</a-button>
						<div class="upload-hint">
							支持{{ formData.appType === 'Android' ? 'APK' : 'IPA' }}文件格式，上传成功后将自动填充下载地址
						</div>
					</a-upload>
				</div>
			</a-form-item>
			<a-form-item label="版本地址" name="appUrl">
				<a-input v-model:value="formData.appUrl" placeholder="请输入安装包下载地址" allow-clear />
			</a-form-item>
			<a-form-item label="版本描述" name="appDesc">
				<a-textarea
					v-model:value="formData.appDesc"
					placeholder="请填写版本更新内容"
					:rows="4"
					allow-clear
					show-count
				/>
			</a-form-item>
			<a-form-item label="强制更新" name="forceUpdate">
				<a-switch v-model:checked="formData.forceUpdate" />
			</a-form-item>
			<a-form-item label="排序" name="sortCode">
				<a-input-number v-model:value="formData.sortCode" placeholder="请输入排序" :min="1" :max="999" />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-space>
				<a-button key="cancel" @click="onClose">取消</a-button>
				<a-button key="submit" type="primary" :loading="submitLoading" @click="onSubmit">确定</a-button>
			</a-space>
		</template>
	</a-modal>
</template>

<script setup>
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import { message } from 'ant-design-vue'
	import appVersionApi from '@/api/dev/appVersionApi'
	import { UploadOutlined } from '@ant-design/icons-vue'

	// 定义emit
	const emit = defineEmits(['successful'])

	// 表单状态
	const formState = ref({
		isUpdate: false,
		currentRecord: null
	})

	// 弹窗控制
	const visible = ref(false)
	const submitLoading = ref(false)
	const formRef = ref()
	const fileList = ref([])
	const uploading = ref(false)

	// 表单数据
	const formData = ref({
		versionName: '',
		versionCode: '',
		appType: 'Android',
		appUrl: '',
		appDesc: '',
		forceUpdate: false,
		sortCode: 99
	})

	// 处理上传前的逻辑
	const beforeUpload = (file) => {
		// 验证文件类型
		let isValidType = true
		if (formData.value.appType === 'Android') {
			isValidType = file.type === 'application/vnd.android.package-archive' || file.name.endsWith('.apk')
		} else if (formData.value.appType === 'iOS') {
			isValidType = file.type === 'application/octet-stream' || file.name.endsWith('.ipa')
		}

		if (!isValidType) {
			message.error(`请上传${formData.value.appType === 'Android' ? 'APK' : 'IPA'}格式的安装包文件`)
			return false
		}

		// 开始上传
		uploading.value = true

		// 创建FormData对象
		const uploadFormData = new FormData()
		uploadFormData.append('version', file)

		// 调用上传API
		appVersionApi
			.upload(uploadFormData)
			.then((res) => {
				if (res) {
					// 更新appUrl
					formData.value.appUrl = res

					// 更新文件列表
					fileList.value = [
						{
							uid: '-1',
							name: file.name,
							status: 'done',
							url: res
						}
					]

					message.success('文件上传成功，已自动填充下载地址')
				}
			})
			.catch(() => {
				message.error('文件上传失败，请重试')
			})
			.finally(() => {
				uploading.value = false
			})

		// 阻止默认上传行为
		return false
	}

	// 处理移除文件
	const handleRemove = () => {
		fileList.value = []
		formData.value.appUrl = ''
	}

	// 监听版本类型变化，重置上传组件
	watch(
		() => formData.value.appType,
		() => {
			fileList.value = []
			// 如果改变类型后URL不符合新类型的格式要求，可以清空URL
			if (formData.value.appUrl) {
				const isAndroidUrl = formData.value.appUrl.toLowerCase().includes('.apk')
				const isIOSUrl = formData.value.appUrl.toLowerCase().includes('.ipa')

				if (
					(formData.value.appType === 'Android' && !isAndroidUrl) ||
					(formData.value.appType === 'iOS' && !isIOSUrl)
				) {
					formData.value.appUrl = ''
				}
			}
		}
	)

	// 表单校验规则
	const formRules = {
		versionName: [
			required('请输入版本号'),
			{ pattern: /^\d+\.\d+\.\d+$/, message: '版本号格式应为 x.y.z', trigger: 'blur' }
		],
		versionCode: [required('请输入版本号')],
		appType: [required('请选择版本类型')],
		appUrl: [required('请输入版本地址'), { pattern: /^https?:\/\//, message: '请输入有效的URL地址', trigger: 'blur' }],
		appDesc: [required('请填写版本描述')],
		sortCode: [required('请输入排序')]
	}

	// 打开弹窗
	const onOpen = (record) => {
		visible.value = true
		resetForm()

		if (record) {
			// 编辑模式
			formState.value.isUpdate = true
			formState.value.currentRecord = record
			nextTick(() => {
				formData.value = {
					id: record.id,
					versionName: record.versionName,
					versionCode: record.versionCode,
					appType: record.appType,
					appUrl: record.appUrl,
					appDesc: record.appDesc,
					forceUpdate: record.forceUpdate,
					sortCode: record.sortCode || 99
				}

				// 如果有URL，设置到上传组件
				if (record.appUrl) {
					fileList.value = [{ url: record.appUrl }]
				}
			})
		} else {
			// 新增模式
			formState.value.isUpdate = false
			formState.value.currentRecord = null
		}
	}

	// 关闭弹窗
	const onClose = () => {
		visible.value = false
		resetForm()
	}

	// 提交表单
	const onSubmit = () => {
		formRef.value
			.validate()
			.then(() => {
				submitLoading.value = true
				let submitParam = cloneDeep(formData.value)

				appVersionApi
					.submitForm(submitParam, formState.value.isUpdate)
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

	// 重置表单
	const resetForm = () => {
		if (formRef.value) {
			formRef.value.resetFields()
		}
		formData.value = {
			versionName: '',
			versionCode: '',
			appType: 'Android',
			appUrl: '',
			appDesc: '',
			forceUpdate: false,
			sortCode: 1
		}
		fileList.value = []
	}

	// 向父组件暴露方法
	defineExpose({
		onOpen
	})
</script>

<style scoped>
	.upload-hint {
		color: #666;
		font-size: 12px;
		margin-top: 8px;
	}

	.upload-container {
		display: flex;
		align-items: center;
	}

	.upload-container .ant-upload {
		margin-right: 8px;
	}
</style>
