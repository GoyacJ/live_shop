<template>
	<a-drawer
		:title="formData.id ? '编辑商品图' : '增加商品图'"
		:width="600"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">

			<a-form-item label="商品：" name="positionId">
				<xn-page-select
					ref="xnPositionPageSelectRef"
					v-model:value="formData.directorId"
					placeholder="请选择职位"
					allow-clear
					:page-function="selectApiFunction.positionSelector"
					:echo-function="selectApiFunction.echoPosition"
				/>
<!--				<a-select v-model:value="formData.goodsId" placeholder="请选择商品" :options="typeOptions" />-->
			</a-form-item>

			<a-form-item label="商品图片" name="url">
				<xn-upload v-model:value="formData.url" uploadMode="image" />
			</a-form-item>

			<a-form-item label="类型" name="type">
				<a-select v-model:value="formData.type" placeholder="请选择类型" :options="typeOptions" />
			</a-form-item>
			<a-form-item label="排序" name="sort">
				<a-input-number v-model:value="formData.sort" :min="1" :max="10000" placeholder="请输入排序" allow-clear />
			</a-form-item>
			<a-form-item label="状态：" name="status">
				<a-select v-model:value="formData.status" placeholder="请选择状态" :options="statusOptions" />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</a-drawer>
</template>

<script setup name="mallGoodsSlideForm">
	import tool from '@/utils/tool'
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import mallGoodsSlideApi from '@/api/shop/mallGoodsSlideApi'
	import bizUserApi from '@/api/biz/bizUserApi'
	// 分页select组件dom定义
	const xnPositionPageSelectRef = ref()
	// 抽屉状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const typeOptions = ref([])
	const statusOptions = ref([])
	const xnPositionPageSelectRef666 = ref()

	const test = ref(null)


	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		console.log('测试、测试、测试、测试345345345')

		const xnPositionPageSelectParam = {
			orgId: 232
		}
		console.log('测试1：', xnPositionPageSelectRef)
		// formData.value.positionId = null;
		xnPositionPageSelectRef.value.onPage(xnPositionPageSelectParam)
		formData.value.directorId = undefined


		if (record) {
			console.log('eeee', record);
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
		}
		typeOptions.value = tool.dictList('file_type')
		statusOptions.value = tool.dictList('whether_it_is_enabled')
		console.log('测试2：', typeof xnPositionPageSelectRef666)
		xnPositionPageSelectRef.value.onPage({})

	}

	// 传递选择组件需要的API
	const selectApiFunction = {
		positionSelector: (param) => {
			console.log('api: ', param)
			return bizUserApi.userPositionSelector(param).then((data) => {
				return Promise.resolve(data)
			})
		},
		userSelector: (param) => {
			return bizUserApi.userSelector(param).then((data) => {
				return Promise.resolve(data)
			})
		},
		childPositionSelector: (param) => {
			return bizUserApi.userPositionSelector(param).then((data) => {
				return Promise.resolve(data)
			})
		},
		childUserSelector: (param) => {
			return bizUserApi.userSelector(param).then((data) => {
				return Promise.resolve(data)
			})
		},
		// 通过id回显数据接口
		echoPosition: (param) => {
			return userCenterApi.userCenterGetPositionListByIdList(param).then((data) => {
				return Promise.resolve(data)
			})
		},
		echoUser: (param) => {
			return userCenterApi.userCenterGetUserListByIdList(param).then((data) => {
				return Promise.resolve(data)
			})
		}
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
			mallGoodsSlideApi
				.mallGoodsSlideSubmitForm(formDataParam, formDataParam.id)
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
