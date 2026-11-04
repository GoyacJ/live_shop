<template>
	<xn-form-container
		:title="formData.id ? '编辑营销活动表' : '增加营销活动表'"
		:width="700"
		v-model:open="open"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-form-item label="活动id：" name="marketingId">
				<a-input v-model:value="formData.marketingId" placeholder="请输入活动id" allow-clear />
			</a-form-item>
			<a-form-item label="营销类型：" name="marketingType">
				<a-input v-model:value="formData.marketingType" placeholder="请输入营销类型" allow-clear />
			</a-form-item>
			<a-form-item label="营销状态：" name="marketingState">
				<a-input v-model:value="formData.marketingState" placeholder="请输入营销状态" allow-clear />
			</a-form-item>
			<a-form-item label="营销价值：" name="marketingValue">
				<a-input v-model:value="formData.marketingValue" placeholder="请输入营销价值" allow-clear />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="marketingCampaignsForm">
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import marketingCampaignsApi from '@/api/live/marketingCampaignsApi'
	// 抽屉状态
	const open = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)

	const chooseLiveRoom = computed({
		get: () => {
			return formData.value.liveId
		},
		set(val) {
			if (val) {
				formData.value.liveId = val.id
			}
		}
	})

	// 打开抽屉
	const onOpen = (record, liveRoomRecord) => {
		open.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
		}
		if (liveRoomRecord) {
			chooseLiveRoom.value = liveRoomRecord
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
		liveId: {
			required: true,
			message: "请选择直播间",
			trigger: "blur"
		},
		marketingId: [required("请输入活动id")],
		marketingType: [required("请输入营销类型")],
		marketingState: [required("请输入营销状态")],
		marketingValue: [required("请输入营销价值")]

	}
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value
			.validate()
			.then(() => {
				submitLoading.value = true
				const formDataParam = cloneDeep(formData.value)
				marketingCampaignsApi
					.marketingCampaignsSubmitForm(formDataParam, formDataParam.id)
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
