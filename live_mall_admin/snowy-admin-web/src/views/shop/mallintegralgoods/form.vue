<template>
	<xn-form-container
		:title="formData.id ? '编辑积分商城' : '增加积分商城'"
		:width="700"
		v-model:open="open"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="积分商品标题：" name="title">
						<a-input v-model:value="formData.title" placeholder="请输入积分商品标题" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="封面：" name="cover">
						<xn-upload v-model:value="formData.cover" uploadMode="image" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="市场价：" name="marketPrice">
						<a-input v-model:value="formData.marketPrice" placeholder="请输入市场价" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="兑换所需积分：" name="integral">
						<a-input v-model:value="formData.integral" placeholder="请输入兑换所需积分" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="商品描述：" name="detail">
						<a-input v-model:value="formData.detail" placeholder="请输入商品描述" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="库存：" name="stock">
						<a-input v-model:value="formData.stock" placeholder="请输入库存" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="限购数量：" name="userLimit">
						<a-input v-model:value="formData.userLimit" placeholder="请输入限购数量" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="商品状态：" name="status">
						<a-select v-model:value="formData.status" placeholder="请选择商品状态" :options="statusOptions" />
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="mallIntegralGoodsForm">
	import tool from '@/utils/tool'
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import mallIntegralGoodsApi from '@/api/shop/mallIntegralGoodsApi'
	import {pattern} from "@antv/g2plot/lib/adaptor/pattern";
	// 抽屉状态
	const open = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const statusOptions = ref([])

	// 打开抽屉
	const onOpen = (record) => {
		open.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
		}
		statusOptions.value = tool.dictList('whether_it_is_enabled')
	}
	// 关闭抽屉
	const onClose = () => {
		formRef.value.resetFields()
		formData.value = {}
		open.value = false
	}
	// 默认要校验的
	const formRules = {
		title: [required('积分商品标题')],
		cover: [required('封面')],
		marketPrice: {
			required: true,
			message: '请输入数字，最多保留两位小数',
			pattern: /^[0-9]+(\.[0-9]{1,2})?$/
		},
		integral: {
			required: true,
			message: '请输入数字，最多保留两位小数',
			pattern: /^[0-9]+(\.[0-9]{1,2})?$/
		},
		stock: {
			required: true,
			message: '请输入数字，最多保留两位小数',
			pattern: /^[0-9]+(\.[0-9]{1,2})?$/
		},
		userLimit: {
			required: true,
			message: '请输入数字，最多保留两位小数',
			pattern: /^[0-9]+(\.[0-9]{1,2})?$/
		},
		status: [required('商品状态')]
	}
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value
			.validate()
			.then(() => {
				submitLoading.value = true
				const formDataParam = cloneDeep(formData.value)
				mallIntegralGoodsApi
					.mallIntegralGoodsSubmitForm(formDataParam, formDataParam.id)
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
