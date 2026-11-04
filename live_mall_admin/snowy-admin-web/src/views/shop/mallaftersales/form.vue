<template>
	<a-drawer
		:title="formData.id ? '编辑售后' : '增加售后'"
		:width="900"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item label="反馈信息：" name="feedbackMessage">
						<xn-editor v-model:value="formData.feedbackMessage" placeholder="请输入商品详细内容" disabled />
					</a-form-item>
				</a-col>
				<a-col :span="24">
					<a-form-item label="回复信息：" name="replyMessage">
						<xn-editor v-model:value="formData.replyMessage" placeholder="请输入回复内容" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="售后类型：" name="afterSaleType">
						<a-tree-select
							v-model:value="formData.afterSaleType"
							class="xn-wd"
							:dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
							placeholder="请选择售后类型"
							allow-clear
							tree-default-expand-all
							:tree-data="treeData"
							:tree-default-expanded-keys="treeDefaultExpandedKeys"
							:field-names="{
								label: 'dictLabel',
								value: 'dictValue'
							}"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="处理状态：" name="afterSaleStatus">
						<a-select v-model:value="formData.afterSaleStatus" placeholder="请选择状态" :options="salesTypeOptions" />
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

<script setup name="mallAfterSalesForm">
	import tool from '@/utils/tool'
	import { cloneDeep, isEmpty } from 'lodash-es'
	import mallAfterSalesApi from '@/api/shop/mallAfterSalesApi'
	import mallSalesTypeApi from '@/api/shop/mallSalesTypeApi'
	// 抽屉状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const salesTypeOptions = ref([])
	const treeData = ref([])
	const treeDefaultExpandedKeys = ref([])
	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
			salesTypeOptions.value = tool.dictList('AFTER_SALE_TYPE')
		}
		nextTick(() => {
			mallSalesTypeApi.mallSalesTypeList().then((res) => {
				if (res !== null) {
					treeData.value = res
					treeData.value.forEach((item) => {
						treeDefaultExpandedKeys.value.push(item.id)
					})
				}
			})
		})
	}
	// 关闭抽屉
	const onClose = () => {
		formRef.value.resetFields()
		formData.value = {}
		visible.value = false
		treeDefaultExpandedKeys.value = []
	}
	// 默认要校验的
	const formRules = {}
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value.validate().then(() => {
			submitLoading.value = true
			const formDataParam = cloneDeep(formData.value)
			mallAfterSalesApi
				.mallAfterSalesSubmitForm(formDataParam, formDataParam.id)
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
