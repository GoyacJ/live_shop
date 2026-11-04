<template>
	<xn-form-container
		:title="formData.id ? '编辑推广信息' : '新增推广信息'"
		:width="550"
		:visible="visible"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-form-item label="排序:" name="sort">
				<a-input-number class="xn-wd" v-model:value="formData.sort" :max="100" />
			</a-form-item>
			<a-form-item label="推广名称" name="name">
				<a-input v-model:value="formData.name" placeholder="请输入推广名称" allow-clear />
			</a-form-item>
			<a-form-item label="推广类型" name="type">
				<a-select
					v-model:value="formData.type"
					:options="typeOptions"
					class="xn-wd"
					placeholder="请选择积分返佣方式"
					allow-clear
				/>
			</a-form-item>
			<a-form-item label="推广简介" name="introduction">
				<a-input v-model:value="formData.introduction" placeholder="请输入推广简介" allow-clear />
			</a-form-item>
			<a-form-item label="Logo" name="logo">
				<xn-upload v-model:value="formData.logo" uploadMode="image" />
			</a-form-item>
			<a-form-item label="积分返佣方式" name="recommendCommission">
				<a-select
					v-model:value="formData.recommendCommission"
					:options="recommendCommissionOptions"
					class="xn-wd"
					placeholder="请选择积分返佣方式"
					allow-clear
				/>
			</a-form-item>
			<a-form-item label="返佣积分/比例" name="commissionIntegral">
				<a-input-number class="xn-wd" v-model:value="formData.commissionIntegral" allow-clear />
			</a-form-item>
			<a-form-item label="积分结算方式" name="settlementType">
				<a-select
					v-model:value="formData.settlementType"
					:options="settlementTypeOptions"
					class="xn-wd"
					placeholder="请选择积分结算方式"
					allow-clear
				/>
			</a-form-item>
			<a-form-item label="积分结算时间类型" name="settlementTimeType">
				<a-select
					v-model:value="formData.settlementTimeType"
					:options="settlementTimeTypeOptions"
					class="xn-wd"
					placeholder="请选择积分结算时间类型"
					allow-clear
				/>
			</a-form-item>
			<a-form-item label="积分发放时间类型" name="commissionTimeType">
				<a-select
					v-model:value="formData.commissionTimeType"
					:options="commissionTimeTypeOptions"
					class="xn-wd"
					placeholder="请选择积分发放时间类型"
					allow-clear
				/>
			</a-form-item>
			<a-form-item v-if="formData.commissionTimeType === '2'" label="积分发放时间" name="commissionFixTime">
				<a-input v-model:value="formData.commissionFixTime" placeholder="请输入积分发放时间" allow-clear />
			</a-form-item>
			<a-form-item label="状态" name="status">
				<a-select v-model:value="formData.status" :options="statusOptions" placeholder="请选择状态" allow-clear />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button class="xn-mr8" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="DistributionInfoForm">
	import { required, rules } from '@/utils/formRules'
	import tool from '@/utils/tool'
	import distributionInfoApi from '@/api/distribution/DistributionInfoApi.js'

	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	const formData = ref({})
	const submitLoading = ref(false)

	const statusOptions = tool.dictList('SIMPLE_STATUS')
	const recommendCommissionOptions = tool.dictList('RECOMMEND_COMMISSION')
	const settlementTypeOptions = tool.dictList('SETTLEMENT_TYPE')
	const settlementTimeTypeOptions = tool.dictList('SETTLEMENT_TIME_TYPE')
	const commissionTimeTypeOptions = tool.dictList('COMMISSION_TIME_TYPE')
	const typeOptions = tool.dictList('DISTRIBUTION_TYPE')

	// 打开弹窗，支持编辑和新增
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			submitLoading.value = true
			const param = {
				id: record.id
			}
			// 编辑，回显详情
			distributionInfoApi
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
		sort: [required('请输入排序'), rules.number],
		name: [required('请输入名称')],
		type: [required('请输入推广类型')],
		introduction: [required('请输入推广简介')],
		logo: [required('请上传图标')],
		recommendCommission: [required('请选择积分返佣方式')],
		commissionIntegral: [required('请输入返佣积分/比例')],
		settlementType: [required('请选择积分结算方式')],
		settlementTimeType: [required('请选择积分结算时间类型')],
		commissionTimeType: [required('请选择积分发放时间类型')],
		commissionFixTime: [],
		status: [required('请选择状态')]
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
				distributionInfoApi
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
