<template>
	<xn-form-container :title="'积分记录详情'" :width="550" :visible="visible" :destroy-on-close="true" @close="onClose">
		<a-form ref="formRef" :model="formData" layout="vertical" disabled>
			<a-form-item label="积分信息Id" name="integralInfoId">
				<a-input class="xn-wd" v-model:value="formData.integralInfoId" :max="100" />
			</a-form-item>
			<a-form-item label="用户Id" name="userId">
				<a-input class="xn-wd" v-model:value="formData.userId" :max="100" />
			</a-form-item>
			<a-form-item label="积分来源" name="integralSource">
				<a-select v-model:value="formData.integralSource" :options="integralSourceOptions" allow-clear />
			</a-form-item>
			<a-form-item label="积分来源Id" name="integralSourceId">
				<a-input class="xn-wd" v-model:value="formData.integralSourceId" :max="100" />
			</a-form-item>
			<a-form-item label="变动类型" name="changeType">
				<a-select v-model:value="formData.changeType" :options="changeTypeOptions" allow-clear />
			</a-form-item>
			<a-form-item label="积分分类" name="integralCategory">
				<a-select v-model:value="formData.integralCategory" :options="integralCategoryOptions" allow-clear />
			</a-form-item>
			<a-form-item label="操作时间" name="operationTime">
				<a-date-picker v-model:value="formData.operationTime" show-time format="YYYY-MM-DD HH:mm:ss" class="xn-wd" />
			</a-form-item>
			<a-form-item label="积分" name="integral">
				<a-input-number class="xn-wd" v-model:value="formData.integral" :max="100" />
			</a-form-item>
			<a-form-item label="操作来源" name="operationSource">
				<a-select v-model:value="formData.operationSource" :options="operationSourceOptions" allow-clear />
			</a-form-item>
			<a-form-item label="操作人ID" name="operationUserId">
				<a-input class="xn-wd" v-model:value="formData.operationUserId" :max="100" />
			</a-form-item>
			<a-form-item label="积分余额" name="integralBalance">
				<a-input-number class="xn-wd" v-model:value="formData.integralBalance" />
			</a-form-item>
			<a-form-item label="冻结积分余额" name="frozenIntegralBalance">
				<a-input-number class="xn-wd" v-model:value="formData.frozenIntegralBalance" />
			</a-form-item>
			<a-form-item label="过期积分余额" name="expireIntegralBalance">
				<a-input-number class="xn-wd" v-model:value="formData.expireIntegralBalance" />
			</a-form-item>
			<a-form-item label="过期积分清除状态" name="clearExpireStatus">
				<a-select v-model:value="formData.clearExpireStatus" :options="clearExpireStatusOptions" allow-clear />
			</a-form-item>
			<a-form-item label="积分有效期" name="validityPeriod">
				<a-date-picker v-model:value="formData.validityPeriod" show-time format="YYYY-MM-DD HH:mm:ss" class="xn-wd" />
			</a-form-item>
			<a-form-item label="备注" name="remark">
				<a-input class="xn-wd" v-model:value="formData.remark" :max="100" />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button class="xn-mr8" @click="onClose">关闭</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="IntegralRecordForm">
	import { required, rules } from '@/utils/formRules'
	import tool from '@/utils/tool'
	import integralRecordApi from '@/api/integral/IntegralRecordApi.js'
	import dayjs from 'dayjs'

	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	const formData = ref({})

	const integralSourceOptions = tool.dictList('INTEGRAL_SOURCE')
	const changeTypeOptions = tool.dictList('INTEGRAL_CHANGE_TYPE')
	const integralCategoryOptions = tool.dictList('INTEGRAL_CATEGORY')
	const operationSourceOptions = tool.dictList('INTEGRAL_OPERATION_SOURCE')
	const clearExpireStatusOptions = tool.dictList('SIMPLE_STATUS')
	// 打开弹窗，支持编辑和新增
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			const param = {
				id: record.id
			}
			// 编辑，回显详情
			integralRecordApi.detail(param).then((data) => {
				const operationTime = data.operationTime ? dayjs(data.operationTime) : undefined
				const validityPeriod = data.validityPeriod ? dayjs(data.validityPeriod) : undefined
				formData.value = {
					...data,
					operationTime,
					validityPeriod
				}
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
