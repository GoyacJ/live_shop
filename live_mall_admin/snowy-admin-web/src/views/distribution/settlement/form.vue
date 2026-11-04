<template>
	<xn-form-container
		:title="formData.id ? '编辑推广结算' : '新增推广结算'"
		:width="550"
		:visible="visible"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" layout="vertical" disabled>
			<a-form-item label="推广类型" name="type">
				<a-select v-model:value="formData.type" :options="typeOptions" class="xn-wd" />
			</a-form-item>
			<a-form-item label="推广信息ID" name="infoId">
				<a-input v-model:value="formData.infoId" />
			</a-form-item>
			<a-form-item label="用户ID" name="userId">
				<a-input v-model:value="formData.userId" />
			</a-form-item>
			<a-form-item label="推广人ID" name="promoterId">
				<a-input v-model:value="formData.promoterId" />
			</a-form-item>
			<a-form-item label="积分返佣方式" name="recommendCommission">
				<a-select v-model:value="formData.recommendCommission" :options="recommendCommissionOptions" class="xn-wd" />
			</a-form-item>
			<a-form-item label="返佣积分/比例" name="integral">
				<a-input-number v-model:value="formData.integral" placeholder="请输入返佣积分" allow-clear />
			</a-form-item>
			<a-form-item label="积分结算方式" name="settlementType">
				<a-select v-model:value="formData.settlementType" :options="settlementTypeOptions" class="xn-wd" />
			</a-form-item>
			<a-form-item label="结算状态" name="settlementStatus">
				<a-select v-model:value="formData.settlementStatus" :options="settlementStatusOptions" class="xn-wd" />
			</a-form-item>
			<a-form-item label="积分结算时间类型" name="settlementTimeType">
				<a-select v-model:value="formData.settlementTimeType" :options="settlementTimeTypeOptions" class="xn-wd" />
			</a-form-item>
			<a-form-item label="积分发放时间类型" name="commissionTimeType">
				<a-select v-model:value="formData.commissionTimeType" :options="commissionTimeTypeOptions" class="xn-wd" />
			</a-form-item>
			<a-form-item label="积分发放状态" name="commissionTimeStatus">
				<a-select v-model:value="formData.commissionTimeStatus" :options="commissionTimeStatusOptions" class="xn-wd" />
			</a-form-item>
			<a-form-item label="积分发放时间" name="commissionTime">
				<a-date-picker
					v-model:value="formData.commissionTime"
					show-time
					format="YYYY-MM-DD HH:mm:ss"
					placeholder="积分发放时间"
					class="xn-wd"
				/>
			</a-form-item>
			<a-form-item label="结算时间" name="settlementTime">
				<a-date-picker
					v-model:value="formData.settlementTime"
					show-time
					format="YYYY-MM-DD HH:mm:ss"
					placeholder="结算时间"
					class="xn-wd"
				/>
			</a-form-item>
			<a-form-item label="结算积分" name="integral">
				<a-input-number v-model:value="formData.integral" />
			</a-form-item>
			<a-form-item label="业务ID" name="businessId">
				<a-input v-model:value="formData.businessId" />
			</a-form-item>
			<a-form-item label="备注" name="remark">
				<a-input v-model:value="formData.remark" />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button class="xn-mr8" @click="onClose">关闭</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="DistributionSettlementForm">
	import { required, rules } from '@/utils/formRules'
	import tool from '@/utils/tool'
	import distributionSettlementApi from '@/api/distribution/DistributionSettlementApi.js'

	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	const formData = ref({})
	const submitLoading = ref(false)

	const typeOptions = tool.dictList('DISTRIBUTION_TYPE')
	const recommendCommissionOptions = tool.dictList('RECOMMEND_COMMISSION')
	const settlementTypeOptions = tool.dictList('SETTLEMENT_TYPE')
	const settlementTimeTypeOptions = tool.dictList('SETTLEMENT_TIME_TYPE')
	const commissionTimeTypeOptions = tool.dictList('COMMISSION_TIME_TYPE')
	const commissionTimeStatusOptions = tool.dictList('COMMISSION_TIME_STATUS')
	const settlementStatusOptions = tool.dictList('SETTLEMENT_STATUS')

	// 打开弹窗，支持编辑和新增
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			submitLoading.value = true
			const param = {
				id: record.id
			}
			// 编辑，回显详情
			distributionSettlementApi
				.detail(param)
				.then((data) => {
					formData.value = Object.assign({}, data)
				})
				.finally(() => {
					submitLoading.value = false
				})
		}
	}

	const onClose = () => {
		formData.value = {}
		formRef.value.resetFields()
		visible.value = false
	}

	defineExpose({
		onOpen
	})
</script>
