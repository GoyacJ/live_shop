<template>
	<xn-form-container
		:title="formData.id ? '编辑积分规则' : '新增积分规则'"
		:width="550"
		:visible="visible"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-form-item label="排序" name="sort">
				<a-input-number class="xn-wd" v-model:value="formData.sort" :max="100" />
			</a-form-item>
			<a-form-item label="限制用户类型" name="userLimitType">
				<a-select
					v-model:value="formData.userLimitType"
					:options="userLimitTypeOptions"
					class="xn-wd"
					placeholder="请选择限制用户类型"
				/>
			</a-form-item>
			<a-form-item v-if="formData.userLimitType === '4'" label="用户ID" name="userId">
				<a-input v-model:value="formData.userId" placeholder="请输入用户ID" allow-clear />
			</a-form-item>
			<a-form-item label="积分有效期" name="validityPeriod">
				<a-date-picker
					v-model:value="formData.validityPeriod"
					show-time
					format="YYYY-MM-DD HH:mm:ss"
					placeholder="请选择积分有效期"
					class="xn-wd"
				/>
			</a-form-item>
			<a-form-item label="积分增加限制时间类型" name="limitTimeType">
				<a-select
					v-model:value="formData.limitTimeType"
					:options="refreshTimeTypeOptions"
					class="xn-wd"
					placeholder="请选择积分增加限制时间类型"
				/>
			</a-form-item>
			<a-form-item label="积分增加限制" name="limitIntegral">
				<a-input-number v-model:value="formData.limitIntegral" placeholder="请输入积分增加限制" allow-clear />
			</a-form-item>
			<a-form-item label="积分提现限制时间类型" name="withdrawalTimeType">
				<a-select
					v-model:value="formData.withdrawalTimeType"
					:options="refreshTimeTypeOptions"
					class="xn-wd"
					placeholder="请选择积分提现限制时间类型"
				/>
			</a-form-item>
			<a-form-item label="积分提现限制" name="withdrawalIntegral">
				<a-input-number v-model:value="formData.withdrawalIntegral" placeholder="请输入积分提现限制" allow-clear />
			</a-form-item>
			<a-form-item label="积分换现金比例" name="pointsToCashRatio">
				<a-input-number v-model:value="formData.pointsToCashRatio" placeholder="请输入积分换现金比例" allow-clear />
			</a-form-item>
			<a-form-item label="状态" name="status">
				<a-select v-model:value="formData.status" :options="statusOptions" placeholder="请选择状态" allow-clear />
			</a-form-item>
			<a-form-item label="备注" name="remark">
				<a-input v-model:value="formData.remark" placeholder="请输入备注" allow-clear />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button class="xn-mr8" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="IntegralRuleForm">
	import { required, rules } from '@/utils/formRules'
	import tool from '@/utils/tool'
	import integralRulesApi from '@/api/integral/IntegralRulesApi.js'

	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	const formData = ref({})
	const submitLoading = ref(false)

	const userLimitTypeOptions = tool.dictList('USER_LIMIT_TYPE')
	const refreshTimeTypeOptions = tool.dictList('REFRESH_TIME_TYPE')
	const statusOptions = tool.dictList('SIMPLE_STATUS')

	// 打开弹窗，支持编辑和新增
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			submitLoading.value = true
			const param = {
				id: record.id
			}
			// 编辑，回显详情
			integralRulesApi
				.detail(param)
				.then((data) => {
					formData.value = Object.assign({}, data)
					formData.value.status = String(data.status)
					if (formData.value.limitTimeType) {
						formData.value.limitTimeType = String(data.limitTimeType)
					} else {
						formData.value.limitTimeType = ''
					}
					if (formData.value.withdrawalTimeType) {
						formData.value.withdrawalTimeType = String(data.withdrawalTimeType)
					} else {
						formData.value.withdrawalTimeType = ''
					}
				})
				.finally(() => {
					submitLoading.value = false
				})
		}
	}

	const formRules = {
		sort: [required('请输入排序'), rules.number],
		userLimitType: [required('请选择限制用户类型')],
		userId: [],
		validityPeriod: [],
		limitTimeType: [],
		limitIntegral: [],
		withdrawalTimeType: [],
		withdrawalIntegral: [],
		pointsToCashRatio: [],
		status: [required('请选择状态')],
		remark: []
	}

	const dictOptions = tool.dictList('DICT')

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
				integralRulesApi
					.submitForm(formData.value, formData.value.id)
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
