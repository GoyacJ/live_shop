<template>
	<xn-form-container
		:title="formData.id ? '编辑推广记录' : '新增推广记录'"
		:width="550"
		:visible="visible"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" layout="vertical" disabled>
			<a-form-item label="ID" name="id">
				<a-input v-model:value="formData.id" />
			</a-form-item>
			<a-form-item label="任务ID" name="taskId">
				<a-input v-model:value="formData.taskId" />
			</a-form-item>
			<a-form-item label="操作时间" name="operateTime">
				<a-date-picker
					v-model:value="formData.operateTime"
					show-time
					format="YYYY-MM-DD HH:mm:ss"
					placeholder=""
					class="xn-wd"
				/>
			</a-form-item>
			<a-form-item label="用户ID" name="userId">
				<a-input v-model:value="formData.userId" />
			</a-form-item>
			<a-form-item label="任务名称" name="taskName">
				<a-input v-model:value="formData.taskName" />
			</a-form-item>
			<a-form-item label="任务类型" name="taskType">
				<a-select v-model:value="formData.taskType" :options="taskTypeOptions" class="xn-wd" />
			</a-form-item>
			<a-form-item label="任务分类" name="taskCategory">
				<a-select v-model:value="formData.taskCategory" :options="taskCategoryOptions" class="xn-wd" />
			</a-form-item>
			<a-form-item label="任务业务分类" name="taskBusinessCategory">
				<a-select v-model:value="formData.taskBusinessCategory" :options="taskBusinessCategoryOptions" class="xn-wd" />
			</a-form-item>
			<a-form-item label="触发条件" name="triggerCondition">
				<a-select v-model:value="formData.triggerCondition" :options="triggerConditionOptions" class="xn-wd" />
			</a-form-item>
			<a-form-item label="奖励积分" name="rewardIntegral">
				<a-input-number v-model:value="formData.rewardIntegral" allow-clear />
			</a-form-item>
			<a-form-item label="是否刷新" name="isRefresh">
				<a-select v-model:value="formData.isRefresh" :options="isRefreshOptions" class="xn-wd" />
			</a-form-item>
			<a-form-item label="刷新时间" name="refreshTime">
				<a-date-picker
					v-model:value="formData.refreshTime"
					show-time
					format="YYYY-MM-DD HH:mm:ss"
					placeholder="刷新时间"
					class="xn-wd"
				/>
			</a-form-item>
			<a-form-item label="次数限制" name="frequencyLimit">
				<a-input-number v-model:value="formData.frequencyLimit" allow-clear />
			</a-form-item>
			<a-form-item label="是否限时" name="isLimitTime">
				<a-select v-model:value="formData.isLimitTime" :options="isRefreshOptions" class="xn-wd" />
			</a-form-item>
			<a-form-item label="任务有效期开始" name="validityPeriodStart">
				<a-date-picker
					v-model:value="formData.validityPeriodStart"
					show-time
					format="YYYY-MM-DD HH:mm:ss"
					placeholder="任务有效期开始"
					class="xn-wd"
				/>
			</a-form-item>
			<a-form-item label="任务有效期截止" name="validityPeriodEnd">
				<a-date-picker
					v-model:value="formData.validityPeriodEnd"
					show-time
					format="YYYY-MM-DD HH:mm:ss"
					placeholder="任务有效期截止"
					class="xn-wd"
				/>
			</a-form-item>
			<a-form-item label="数据类型参数" name="numberParms">
				<a-input-number v-model:value="formData.numberParms" allow-clear />
			</a-form-item>
			<a-form-item label="积分有效期" name="validityPeriod">
				<a-date-picker
					v-model:value="formData.validityPeriod"
					show-time
					format="YYYY-MM-DD HH:mm:ss"
					placeholder="积分有效期"
					class="xn-wd"
				/>
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button class="xn-mr8" @click="onClose">关闭</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="DistributionRecordForm">
	import tool from '@/utils/tool'
	import taskRecordApi from '@/api/task/TaskRecordApi.js'
	import dayjs from 'dayjs'

	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	const formData = ref({})
	const submitLoading = ref(false)

	const taskTypeOptions = tool.dictList('TASK_TYPE')
	const taskCategoryOptions = tool.dictList('TASK_CATEGORY')
	const taskBusinessCategoryOptions = tool.dictList('TASK_BUSINESS_CATEGORY')
	const triggerConditionOptions = tool.dictList('TASK_TRIGGER_CONDITION')
	const isRefreshOptions = tool.dictList('SIMPLE_STATUS')

	// 打开弹窗，支持编辑和新增
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			submitLoading.value = true
			const param = {
				id: record.id
			}
			// 编辑，回显详情
			taskRecordApi
				.detail(param)
				.then((data) => {
					formData.value = Object.assign({}, data)
					formData.value.taskType = String(data.taskType)
					formData.value.isRefresh = String(data.isRefresh)
					formData.value.isLimitTime = String(data.isLimitTime)
					if (data.operateTime) {
						formData.value.operateTime = dayjs(data.operateTime)
					} else {
						formData.value.operateTime = null
					}
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
				})
				.finally(() => {
					submitLoading.value = false
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
