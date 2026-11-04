<template>
	<xn-form-container
		:title="formData.id ? '编辑任务配置信息' : '新增任务配置信息'"
		:width="550"
		:visible="visible"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-form-item label="排序:" name="sort">
				<a-input-number class="xn-wd" v-model:value="formData.sort" :max="100" />
			</a-form-item>
			<a-form-item label="图标" name="icon">
				<xn-upload v-model:value="formData.icon" uploadMode="image" />
			</a-form-item>
			<a-form-item label="任务名称" name="taskName">
				<a-input v-model:value="formData.taskName" placeholder="请输入任务名称" allow-clear />
			</a-form-item>
			<a-form-item label="任务介绍" name="taskIntroduction">
				<a-input v-model:value="formData.taskIntroduction" placeholder="请输入任务介绍" allow-clear />
			</a-form-item>
			<a-form-item label="任务类型" name="taskType">
				<a-select
					v-model:value="formData.taskType"
					:options="taskTypeOptions"
					class="xn-wd"
					placeholder="请选择任务类型"
					allow-clear
				/>
			</a-form-item>
			<a-form-item label="任务分类" name="taskCategory">
				<a-select
					v-model:value="formData.taskCategory"
					:options="taskCategoryOptions"
					class="xn-wd"
					placeholder="请选择任务分类"
					allow-clear
				/>
			</a-form-item>
			<a-form-item label="任务业务分类" name="taskBusinessCategory">
				<a-select
					v-model:value="formData.taskBusinessCategory"
					:options="filteredBusinessCategoryOptions"
					class="xn-wd"
					placeholder="请选择任务业务分类"
					allow-clear
				/>
			</a-form-item>
			<a-form-item label="任务触发条件" name="triggerCondition">
				<a-select
					v-model:value="formData.triggerCondition"
					:options="triggerConditionOptions"
					class="xn-wd"
					placeholder="请选择任务触发条件"
					allow-clear
				/>
			</a-form-item>
			<a-form-item label="奖励积分" name="rewardIntegral">
				<a-input-number class="xn-wd" v-model:value="formData.rewardIntegral" allow-clear />
			</a-form-item>
			<a-form-item label="是否刷新" name="isRefresh">
				<a-select
					v-model:value="formData.isRefresh"
					:options="statusOptions"
					class="xn-wd"
					placeholder="请选择是否刷新"
					allow-clear
				/>
			</a-form-item>
			<a-form-item v-if="formData.isRefresh === '0'" label="刷新时间" name="refreshTime" class="indent-form-item">
				<a-select
					v-model:value="formData.refreshTime"
					:options="refreshTimeOptions"
					class="xn-wd"
					placeholder="请选择刷新时间"
					allow-clear
				/>
			</a-form-item>
			<a-form-item
				v-if="formData.refreshTime && formData.refreshTime !== '5'"
				label="次数限制"
				name="frequencyLimit"
				class="indent-form-item"
			>
				<a-input-number class="xn-wd" v-model:value="formData.frequencyLimit" allow-clear />
			</a-form-item>
			<a-form-item label="是否限时" name="isLimitTime">
				<a-select
					v-model:value="formData.isLimitTime"
					:options="statusOptions"
					class="xn-wd"
					placeholder="请选择是否限时"
					allow-clear
				/>
			</a-form-item>
			<a-form-item
				v-if="formData.isLimitTime === '0'"
				label="任务有效期开始"
				name="validityPeriodStart"
				class="indent-form-item"
			>
				<a-date-picker
					v-model:value="formData.validityPeriodStart"
					show-time
					format="YYYY-MM-DD HH:mm:ss"
					placeholder="任务有效期开始"
					class="xn-wd"
					allow-clear
				/>
			</a-form-item>
			<a-form-item
				v-if="formData.isLimitTime === '0'"
				label="任务有效期截止"
				name="validityPeriodEnd"
				class="indent-form-item"
			>
				<a-date-picker
					v-model:value="formData.validityPeriodEnd"
					show-time
					format="YYYY-MM-DD HH:mm:ss"
					placeholder="任务有效期截止"
					class="xn-wd"
					allow-clear
				/>
			</a-form-item>
			<a-form-item label="数据类型参数" name="numberParms">
				<a-input-number class="xn-wd" v-model:value="formData.numberParms" allow-clear />
			</a-form-item>
			<a-form-item label="积分有效期" name="validityPeriod">
				<a-date-picker
					v-model:value="formData.validityPeriod"
					show-time
					format="YYYY-MM-DD HH:mm:ss"
					placeholder="积分有效期"
					class="xn-wd"
					allow-clear
				/>
			</a-form-item>
			<a-form-item label="跳转对象" name="jumpRouting">
				<template v-if="formData.taskCategory === '3'">
					<XnPageSelect
						v-model:value="formData.jumpRouting"
						:page-function="goodsPageFunction"
						:echo-function="goodsEchoFunction"
						placeholder="请选择商品"
						show-search
						searchKeyName="title"
						label-in-value
						@change="
							(val, obj) => {
								formData.goodsObj = obj
							}
						"
					/>
				</template>
				<template v-else-if="formData.taskCategory === '4'">
					<XnPageSelect
						v-model:value="formData.jumpRouting"
						:page-function="livePageFunction"
						:echo-function="liveEchoFunction"
						placeholder="请选择直播"
						show-search
						searchKeyName="searchKey"
						label-in-value
						@change="
							(val, obj) => {
								formData.liveObj = obj
							}
						"
					/>
				</template>
				<template v-else-if="formData.taskCategory === '5'">
					<XnPageSelect
						v-model:value="formData.jumpRouting"
						:page-function="videoPageFunction"
						:echo-function="videoEchoFunction"
						placeholder="请选择视频"
						show-search
						searchKeyName="title"
						label-in-value
						@change="
							(val, obj) => {
								formData.videoObj = obj
							}
						"
					/>
				</template>
				<template v-else>
					<a-input v-model:value="formData.jumpRouting" placeholder="请输入跳转路由" allow-clear />
				</template>
			</a-form-item>
			<a-form-item label="状态" name="status">
				<a-select v-model:value="formData.status" :options="statusOptions" placeholder="请选择状态" allow-clear />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button class="xn-mr8" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="TaskInfoForm">
	import { required, rules } from '@/utils/formRules'
	import tool from '@/utils/tool'
	import taskInfoApi from '@/api/task/TaskInfoApi.js'
	import dayjs from 'dayjs'
	import { onMounted } from 'vue'
	import XnPageSelect from '@/components/XnPageSelect/index.vue'
	import mallGoodsApi from '@/api/shop/mallGoodsApi'
	import liveBizApi from '@/api/live/liveBizApi'
	import videoApi from '@/api/content/videoApi.js'

	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	const formData = ref({})
	const submitLoading = ref(false)

	// 监听任务类型变化
	watch(
		() => formData.value.taskType,
		(newVal) => {
			if (newVal === '3') {
				formData.value.isLimitTime = '0'
			}
		}
	)

	// watch(
	// 	() => formData.value.taskCategory,
	// 	(newVal, oldVal) => {
	// 		// 只在 taskCategory 真正改变时才清空业务分类
	// 		if (newVal !== oldVal) {
	// 			formData.value.taskBusinessCategory = undefined
	// 		}
	// 	}
	// )

	// 根据任务分类过滤业务分类选项
	const filteredBusinessCategoryOptions = computed(() => {
		return taskBusinessCategoryOptions.filter((item) => item.value.startsWith(formData.value.taskCategory))
	})

	watch(
		() => formData.value.taskCategory,
		(newVal, oldVal) => {
			formData.value.jumpRouting = ''
			formData.value.goodsObj = undefined
			formData.value.liveObj = undefined
			formData.value.videoObj = undefined
			if (newVal === '101') {
				formData.value.triggerCondition = '0'
				formData.value.isRefresh = '1'
			}
			if (newVal === '201') {
				formData.value.triggerCondition = '1'
				formData.value.isRefresh = '0'
				formData.value.refreshTime = '1'
				formData.value.frequencyLimit = '1'
			}
		}
	)

	watch(
		() => formData.value.isRefresh,
		(newVal, oldVal) => {
			if (newVal === '1') {
				formData.value.refreshTime = ''
				formData.value.frequencyLimit = ''
			}
		}
	)

	// 打开弹窗，支持编辑和新增
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			submitLoading.value = true
			const param = {
				id: record.id
			}
			taskInfoApi
				.detail(param)
				.then(async (data) => {
					formData.value = Object.assign({}, data)
					formData.value.status = String(data.status)
					formData.value.taskCategory = String(data.taskCategory)
					formData.value.taskBusinessCategory = String(data.taskBusinessCategory)
					formData.value.isRefresh = String(data.isRefresh)
					formData.value.isLimitTime = String(data.isLimitTime)
					formData.value.refreshTime = String(data.refreshTime)
					if (data.validityPeriodStart) {
						formData.value.validityPeriodStart = dayjs(data.validityPeriodStart)
					} else {
						formData.value.validityPeriodStart = null
					}
					if (data.validityPeriodEnd) {
						formData.value.validityPeriodEnd = dayjs(data.validityPeriodEnd)
					} else {
						formData.value.validityPeriodEnd = null
					}
					if (data.validityPeriod) {
						formData.value.validityPeriod = dayjs(data.validityPeriod)
					} else {
						formData.value.validityPeriod = null
					}
					if (['3', '4', '5'].includes(String(data.taskCategory)) && data.jumpRouting) {
						let echoFunc = null
						if (data.taskCategory === '3') echoFunc = goodsEchoFunction
						if (data.taskCategory === '4') echoFunc = liveEchoFunction
						if (data.taskCategory === '5') echoFunc = videoEchoFunction
						if (echoFunc) {
							const arr = await echoFunc({ idList: [data.jumpRouting] })
							if (arr && arr.length > 0) {
								formData.value.jumpRouting = { label: arr[0].name, value: arr[0].id }
							} else {
								formData.value.jumpRouting = undefined
							}
						}
					} else {
						formData.value.jumpRouting = data.jumpRouting
					}
				})
				.finally(() => {
					submitLoading.value = false
				})
		} else {
			formData.value = {
				status: '0'
			}
		}
	}

	const formRules = {
		sort: [required('请输入排序'), rules.number],
		icon: [required('请上传图标')],
		taskName: [required('请输入任务名称')],
		taskIntroduction: [required('请输入任务介绍')],
		taskType: [required('请选择任务类型')],
		taskCategory: [required('请选择任务分类')],
		taskBusinessCategory: [required('请选择任务业务分类')],
		triggerCondition: [required('请选择触发条件')],
		rewardIntegral: [required('请输入奖励积分'), rules.number],
		frequencyLimit: [rules.number],
		refreshTime: [],
		isRefresh: [required('请选择是否刷新')],
		isLimitTime: [required('请选择是否限时')],
		validityPeriodStart: [],
		validityPeriodEnd: [],
		numberParms: [],
		validityPeriod: [],
		jumpRouting: [],
		status: [required('请选择状态')]
	}

	const taskTypeOptions = tool.dictList('TASK_TYPE')
	const statusOptions = tool.dictList('SIMPLE_STATUS')
	const taskCategoryOptions = tool.dictList('TASK_CATEGORY')
	const taskBusinessCategoryOptions = tool.dictList('TASK_BUSINESS_CATEGORY')
	const triggerConditionOptions = tool.dictList('TASK_TRIGGER_CONDITION')
	const refreshTimeOptions = tool.dictList('REFRESH_TIME_TYPE')

	const onClose = () => {
		formData.value = {}
		formRef.value.resetFields()
		visible.value = false
	}

	const onSubmit = () => {
		formRef.value
			.validate()
			.then(() => {
				submitLoading.value = true
				const submitData = { ...formData.value }
				if (['3', '4', '5'].includes(submitData.taskCategory)) {
					if (typeof submitData.jumpRouting === 'object' && submitData.jumpRouting.value) {
						submitData.jumpRouting = submitData.jumpRouting.value
					}
				}
				taskInfoApi
					.submitForm(submitData, submitData.id)
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

	// 商品分页与回显
	const goodsPageFunction = (param) =>
		mallGoodsApi.mallGoodsPage(param).then((data) => {
			data.records = data.records.map((item) => ({ name: item.title, id: item.id, ...item }))
			return data
		})
	const goodsEchoFunction = (param) => {
		let id = param.idList[0]
		if (typeof id === 'object' && id.value) id = id.value
		return mallGoodsApi.mallGoodsDetail({ id }).then((data) => [{ name: data.title, id: data.id, ...data }])
	}
	// 直播分页与回显
	const livePageFunction = (param) =>
		liveBizApi.liveBizPage(param).then((data) => {
			data.records = data.records.map((item) => ({ name: item.mainTitle, id: item.id, ...item }))
			return data
		})
	const liveEchoFunction = (param) => {
		let id = param.idList[0]
		if (typeof id === 'object' && id.value) id = id.value
		return liveBizApi.liveBizDetail({ id }).then((data) => [{ name: data.mainTitle, id: data.id, ...data }])
	}
	// 专题分页与回显
	const videoPageFunction = (param) =>
		videoApi.videoPage(param).then((data) => {
			data.records = data.records.map((item) => ({ name: item.title, id: item.id, ...item }))
			return data
		})
	const videoEchoFunction = (param) => {
		let id = param.idList[0]
		if (typeof id === 'object' && id.value) id = id.value
		return videoApi.videoDetail({ id }).then((data) => [{ name: data.title, id: data.id, ...data }])
	}

	defineExpose({
		onOpen
	})
</script>

<style scoped>
	/* 控制表单项的缩进 */
	.indent-form-item :deep(.ant-form-item-control-input) {
		margin-left: 20px;
	}
</style>
