<template>
	<xn-form-container
		:title="formData.id ? '编辑专题' : '新增专题'"
		:width="600"
		v-model:open="open"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-form-item label="专题名称" name="name">
				<a-input v-model:value="formData.name" placeholder="请输入专题名称" allow-clear />
			</a-form-item>
			<a-form-item label="商品数量" name="goodsNum">
				<a-input-number
					v-model:value="formData.goodsNum"
					:min="0"
					:max="9999"
					style="width: 100%"
					placeholder=""
					disabled
				/>
			</a-form-item>
			<a-form-item label="排序" name="sort">
				<a-input-number
					v-model:value="formData.sort"
					:min="0"
					:max="9999"
					style="width: 100%"
					placeholder="请输入排序"
				/>
			</a-form-item>
			<a-form-item label="是否轮播" name="carousel">
				<a-radio-group v-model:value="formData.carousel">
					<a-radio :value="0">是</a-radio>
					<a-radio :value="1">否</a-radio>
				</a-radio-group>
			</a-form-item>
			<a-form-item label="专题图片" name="background">
				<xn-upload v-model:value="formData.background" uploadMode="image" />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="mallSpecialTopicForm">
	import { ref } from 'vue'
	import mallSpecialTopicApi from '@/api/shop/mallSpecialTopicApi.js'
	const open = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	const formData = ref({})
	const submitLoading = ref(false)
	const formRules = {
		name: { required: true, message: '请输入专题名称', trigger: 'blur' },
		sort: { required: true, message: '请输入排序', trigger: 'change', type: 'number' },
		carousel: { required: true, message: '请选择是否轮播', trigger: 'change', type: 'number' },
		background: { required: true, message: '请上传专题图片', trigger: 'change' }
	}
	const onOpen = (record) => {
		open.value = true
		formData.value = record ? { ...record } : {}
	}
	const onClose = () => {
		formRef.value.resetFields()
		formData.value = {}
		open.value = false
	}
	const onSubmit = () => {
		formRef.value.validate().then(() => {
			submitLoading.value = true
			const formDataParam = { ...formData.value }
			mallSpecialTopicApi
				.mallSpecialTopicSubmitForm(formDataParam, !!formDataParam.id)
				.then(() => {
					onClose()
					emit('successful')
				})
				.finally(() => {
					submitLoading.value = false
				})
		})
	}
	defineExpose({ onOpen })
</script>
