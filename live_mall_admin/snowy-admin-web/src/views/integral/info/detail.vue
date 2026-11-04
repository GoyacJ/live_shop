<template>
	<xn-form-container :title="'积分信息'" :width="550" :visible="visible" :destroy-on-close="true" @close="onClose">
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-form-item label="用户Id:" name="userId">
				<a-input-number class="xn-wd" v-model:value="formData.userId" :max="100" disabled />
			</a-form-item>
			<a-form-item label="积分" name="integral">
				<a-input v-model:value="formData.integral" placeholder="请输入积分" allow-clear disabled />
			</a-form-item>
			<a-form-item label="冻结积分" name="frozenIntegral">
				<a-input v-model:value="formData.frozenIntegral" placeholder="请输入冻结积分" allow-clear disabled />
			</a-form-item>
			<a-form-item label="过期积分" name="expireIntegral">
				<a-input v-model:value="formData.expireIntegral" placeholder="请输入过期积分" allow-clear disabled />
			</a-form-item>
			<a-form-item label="最后修改时间" name="lastModifyTime">
				<a-date-picker
					v-model:value="formData.lastModifyTime"
					show-time
					format="YYYY-MM-DD HH:mm:ss"
					class="xn-wd"
					disabled
				/>
			</a-form-item>
			<a-form-item label="最后修改人ID" name="lastModifyUserId">
				<a-input v-model:value="formData.lastModifyUserId" placeholder="请输入最后修改人ID" allow-clear disabled />
			</a-form-item>
			<a-form-item label="状态" name="status">
				<a-select
					v-model:value="formData.status"
					:options="statusOptions"
					placeholder="请选择状态"
					allow-clear
					disabled
				/>
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button class="xn-mr8" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">调整</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="IntegralInfoForm">
	import { required, rules } from '@/utils/formRules'
	import tool from '@/utils/tool'
	import integralInfoApi from '@/api/integral/IntegralInfoApi.js'

	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	const formData = ref({})
	const submitLoading = ref(false)

	// 打开弹窗，支持编辑和新增
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			submitLoading.value = true
			const param = {
				id: record.id
			}
			// 编辑，回显详情
			integralInfoApi
				.detail(param)
				.then((data) => {
					formData.value = Object.assign({}, data)
					formData.value.status = String(data.status)
				})
				.finally(() => {
					submitLoading.value = false
				})
		}
	}

	const formRules = {
		userId: [required('用户Id'), rules.number],
		integral: [required('请输入积分'), rules.price],
		frozenIntegral: [required('请输入冻结积分'), rules.price],
		expireIntegral: [required('请输入过期积分'), rules.price],
		lastModifyTime: [required('请输入最后修改时间')],
		lastModifyUserId: [required('请选择字典')],
		status: [required('请选择状态')]
	}

	const statusOptions = tool.dictList('SIMPLE_STATUS')

	const onClose = () => {
		formData.value = {}
		formRef.value.resetFields()
		visible.value = false
	}

	const onSubmit = () => {
		formRef.value
			.validate()
			.then(() => {
				submitLoading.value = true
				integralInfoApi
					.adjust(formData.value, formData.value.id)
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

	defineExpose({
		onOpen
	})
</script>
