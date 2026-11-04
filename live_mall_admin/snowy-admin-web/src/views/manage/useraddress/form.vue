<template>
	<a-drawer
		:title="formData.id ? '编辑商城-用户收获地址' : '增加商城-用户收获地址'"
		:width="600"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-form-item label="联系方式：" name="mobile">
				<a-input v-model:value="formData.mobile" placeholder="请输入联系方式" allow-clear />
			</a-form-item>
			<a-form-item label="收货人昵称：" name="nickname">
				<a-input v-model:value="formData.nickname" placeholder="请输入收货人昵称" allow-clear />
			</a-form-item>
<!--			<a-form-item label="省份：" name="provinceName">
				<a-select v-model:value="formData.provinceName" placeholder="请选择省份" :options="provinceNameOptions" />
			</a-form-item>
			<a-form-item label="市：" name="cityName">
				<a-select v-model:value="formData.cityName" placeholder="请选择市" :options="cityNameOptions" />
			</a-form-item>
			<a-form-item label="区县：" name="countiesName">
				<a-select v-model:value="formData.countiesName" placeholder="请选择区县" :options="countiesNameOptions" />
			</a-form-item>-->
			<a-form-item label="详细地址：" name="address">
				<a-textarea
					v-model:value="formData.address"
					placeholder="请输入详细地址"
					:auto-size="{ minRows: 3, maxRows: 5 }"
				/>
			</a-form-item>
			<a-form-item label="是否为默认地址：" name="isDefault">
				<a-select v-model:value="formData.isDefault" placeholder="请选择是否为默认地址" :options="isDefaultOptions" />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</a-drawer>
</template>

<script setup name="userAddressForm">
	import tool from '@/utils/tool'
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import userAddressApi from '@/api/manage/userAddressApi'
	// 抽屉状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const provinceNameOptions = ref([])
	const cityNameOptions = ref([])
	const countiesNameOptions = ref([])
	const isDefaultOptions = ref([])

	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
		}
		/*provinceNameOptions.value = tool.dictList('GENDER')
		cityNameOptions.value = tool.dictList('GENDER')
		countiesNameOptions.value = tool.dictList('GENDER')*/
		isDefaultOptions.value = tool.dictList('IS_DEFAULT')
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
			userAddressApi
				.userAddressSubmitForm(formDataParam, formDataParam.id)
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
