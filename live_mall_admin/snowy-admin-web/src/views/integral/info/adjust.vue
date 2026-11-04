<template>
	<a-modal
		:title="'调整积分'"
		:width="550"
		:visible="visible"
		:destroy-on-close="true"
		@cancel="onClose"
		@ok="onSubmit"
		:confirmLoading="submitLoading"
	>
		<a-form-item label="用户Id" name="userId">
			<a-input v-model:value="formData.userId" disabled />
		</a-form-item>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-form-item label="积分类型" name="integralType">
				<a-select
					v-model:value="formData.integralCategory"
					:options="integralCategoryCondition"
					placeholder="请选择积分类型"
					allow-clear
				/>
			</a-form-item>

			<a-form-item label="积分" name="integral">
				<a-input-number v-model:value="formData.integral" placeholder="请输入积分" :min="0" class="xn-wd" />
			</a-form-item>

			<a-form-item v-if="formData.integralCategory !== '3'" label="过期时间" name="expireTime">
				<a-date-picker
					v-model:value="formData.expireTime"
					show-time
					format="YYYY-MM-DD HH:mm:ss"
					placeholder="请选择过期时间"
					class="xn-wd"
				/>
			</a-form-item>

			<a-form-item label="备注" name="remark">
				<a-input v-model:value="formData.remark" />
			</a-form-item>
		</a-form>
	</a-modal>
</template>

<script setup name="IntegralAdjustForm">
	import { required } from '@/utils/formRules'
	import tool from '@/utils/tool'
	import integralInfoApi from '@/api/integral/IntegralInfoApi'

	const visible = ref(false)
	const emit = defineEmits(['successful'])
	const formRef = ref()
	const formData = ref({})
	const submitLoading = ref(false)

	// 积分类型选项
	const integralCategoryCondition = tool.dictList('INTEGRAL_CATEGORY')

	// 表单校验规则
	const formRules = {
		userId: [required('用户Id')],
		integralCategory: [required('请选择积分类型')],
		integral: [required('请输入积分')]
	}

	// 打开弹窗
	const onOpen = (record) => {
		visible.value = true
		formData.value = {
			userId: record.userId,
			integralCategory: undefined,
			integral: undefined,
			expireTime: undefined
		}
	}

	// 关闭弹窗
	const onClose = () => {
		formData.value = {}
		formRef.value?.resetFields()
		visible.value = false
	}

	// 提交表单
	const onSubmit = () => {
		formRef.value
			.validate()
			.then(() => {
				submitLoading.value = true
				integralInfoApi
					.adjust(formData.value)
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
