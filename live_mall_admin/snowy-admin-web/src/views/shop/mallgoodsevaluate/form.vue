<template>
	<a-drawer
		:title="'编辑评价'"
		:width="900"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="商品标题：" name="title">
						<a-input v-model:value="formData.title" placeholder="请输入商品标题" allow-clear disabled />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="商品所属商家：" name="merchant">
						<a-input v-model:value="formData.merchant" placeholder="请输入商家名称" allow-clear disabled />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="状态：" name="status">
						<a-select v-model:value="formData.status" placeholder="请选择状态" :options="statusOptions" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="评价类型：" name="evaluateType">
						<a-select v-model:value="formData.evaluateType" placeholder="请选择状态" :options="typeOptions" disabled />
					</a-form-item>
				</a-col>
				<a-col :span="24">
					<a-form-item label="评价信息：" name="evaluateInformation">
						<xn-editor v-model:value="formData.evaluateInformation" placeholder="请输入商品评价信息" disabled />
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
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const statusOptions = ref([])
	const typeOptions = ref([])

	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
		}
		statusOptions.value = tool.dictList('EVALUATE_STATUS')
		typeOptions.value = tool.dictList('EVALUATE_TYPE')
	}
	// 关闭抽屉
	const onClose = () => {
		formRef.value.resetFields()
		formData.value = {}
		visible.value = false
	}
	// 默认要校验的
	const formRules = {}
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value.validate().then(() => {
			submitLoading.value = true
			const formDataParam = cloneDeep(formData.value)
			mallGoodsEvaluateApi
				.mallGoodsEvaluaEditForm(formDataParam, formDataParam.id)
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
