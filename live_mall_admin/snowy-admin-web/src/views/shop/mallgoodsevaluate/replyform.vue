<template>
	<a-drawer
		:title="'回复评价'"
		:width="900"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@close="onClose"
	>
		<a-form ref="replyFormRef" :model="replyFormData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="商品标题：" name="title">
						<a-input v-model:value="replyFormData.title" placeholder="请输入商品标题" allow-clear disabled />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="商品所属商家：" name="merchant">
						<a-input v-model:value="replyFormData.merchant" placeholder="请输入商家名称" allow-clear disabled />
					</a-form-item>
				</a-col>
				<a-col :span="24">
					<a-form-item label="评价信息：" name="evaluateInformation">
						<a-textarea
							v-model:value="replyFormData.evaluateInformation"
							placeholder="请输入商品评价信息"
							allow-clear
							disabled
						/>
					</a-form-item>
				</a-col>
				<a-col :span="24">
					<a-form-item label="回复信息：" name="evaluateReplyInformation">
						<a-textarea
							v-model:value="replyFormData.evaluateReplyInformation"
							placeholder="请输入商品回复信息"
							allow-clear
						/>
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</a-drawer>
</template>

<script setup name="mallGoodsEvaluateForm">
	import tool from '@/utils/tool'
	import { cloneDeep } from 'lodash-es'
	import mallGoodsEvaluateApi from '@/api/shop/mallGoodsEvaluateApi'
	// 抽屉状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const replyFormRef = ref()
	// 表单数据
	const replyFormData = ref({})
	const submitLoading = ref(false)
	const statusOptions = ref([])
	const typeOptions = ref([])

	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			let recordData = cloneDeep(record)
			replyFormData.value = Object.assign({}, recordData)
		}
		statusOptions.value = tool.dictList('EVALUATE_STATUS')
		typeOptions.value = tool.dictList('EVALUATE_TYPE')
	}
	// 关闭抽屉
	const onClose = () => {
		replyFormRef.value.resetFields()
		replyFormData.value = {}
		visible.value = false
	}
	// 默认要校验的
	const formRules = {}
	// 验证并提交数据
	const onSubmit = () => {
		replyFormRef.value.validate().then(() => {
			submitLoading.value = true
			const formDataParam = cloneDeep(replyFormData.value)
			formDataParam.evaluateInformation = replyFormData.value.evaluateReplyInformation
			//将回复人员信息置为0
			formDataParam.createUser = '0'
			//评价类型为回复，状态默认为显示
			formDataParam.evaluateType = '2'
			formDataParam.status = '1'
			mallGoodsEvaluateApi
				.mallGoodsEvaluaAddForm(formDataParam, formDataParam.id)
				.then(() => {
					onClose()
					emit('successful')
				})
				.finally(() => {
					submitLoading.value = false
				})
		})
	}
	// 抛出函数
	defineExpose({
		onOpen
	})
</script>
