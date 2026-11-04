<template>
	<xn-form-container
		:title="formData.id ? '编辑直播列表' : '增加直播列表'"
		:width="700"
		v-model:open="open"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
				<!--				<a-col :span="12">-->
				<!--					<a-form-item label="直播场次ID：" name="liveBroadcasts">-->
				<!--						<a-input v-model:value="formData.liveBroadcasts" placeholder="请输入直播场次ID" allow-clear />-->
				<!--					</a-form-item>-->
				<!--				</a-col>-->
				<a-col :span="12">
					<a-form-item label="主播选择：" name="anchorId">
						<a-select
							ref="anchorRef"
							placeholder="请选择主播"
							v-model:value="formData.anchorId"
							:options="anchorOptions"
						></a-select>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="观众数量：" name="spectatorNumber">
						<a-input v-model:value="formData.spectatorNumber" placeholder="请输入观众数量" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="直播封面：" name="coverUrl">
						<xn-upload v-model:value="formData.coverUrl" uploadMode="image" />
					</a-form-item>
				</a-col>
				<a-col :span="12" v-if="formData.id">
					<a-form-item label="直播间状态：" name="liveRoomState">
						<a-select
							ref="selectLiveRoomStateRef"
							placeholder="请选择直播间状态"
							v-model:value="formData.liveRoomState"
							:options="liveRoomStateOptions"
						></a-select>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="主标题：" name="mainTitle">
						<a-input v-model:value="formData.mainTitle" placeholder="请输入主标题" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="直播类型：" name="liveState">
						<a-select
							ref="selectLiveStateRef"
							placeholder="请选择直播类型"
							v-model:value="formData.liveState"
							:options="liveStateOptions"
						></a-select>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="直播开始时间：" name="liveBeginTime">
						<a-date-picker v-model:value="formData.liveBeginTime" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择直播开始时间" style="width: 100%" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="直播结束时间：" name="liveOverTime">
						<a-date-picker v-model:value="formData.liveOverTime" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择直播结束时间" style="width: 100%" />
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

<script setup name="liveBizForm">
import { cloneDeep } from 'lodash-es'
import { required } from '@/utils/formRules'
import liveBizApi from '@/api/live/liveBizApi'
import anchorApi from '@/api/live/anchorApi'
import liveStreamingTypeApi from '@/api/live/liveStreamingTypeApi'
import tool from '@/utils/tool'
import { message } from 'ant-design-vue'
// 抽屉状态
const open = ref(false)
const liveStateOptions = reactive([]) // 直播类型
const liveRoomStateOptions = (tool.dictList('LIVE_ROOM_STATE') || [])
	.map((item) => {
		if (item.label == '待开播') {
			item.disabled = true
		}
		return item
	}) // 直播间状态
const anchorOptions = reactive([]) // 主播


const emit = defineEmits({ successful: null })
const formRef = ref()
// 表单数据
const formData = ref({})
const submitLoading = ref(false)

// 打开抽屉
const onOpen = (record) => {
	open.value = true
	if (record) {
		let recordData = cloneDeep(record)
		formData.value = Object.assign({}, recordData)
	}
	liveStreamingTypeApi.liveStreamingTypeList({}).then((data) => {
		// 清空 liveStateOptions 再进行添加
		liveStateOptions.splice(0)
		data.forEach((item) => {
			liveStateOptions.push({
				label: item.title,
				value: item.id
			})
		})
	})
	anchorApi.anchorList({}).then((data) => {
		// 清空 anchorOptions 再进行添加
		anchorOptions.splice(0)
		data.forEach((item) => {
			anchorOptions.push({
				label: item.realName,
				value: item.id
			})
		})
	})
}
// 关闭抽屉
const onClose = () => {
	formRef.value.resetFields()
	formData.value = {}
	open.value = false
}
// 默认要校验的
const formRules = {
	anchorId: [required('主播选择')],
	spectatorNumber: {
		required: true,
		message: '请输入数字',
		pattern: /^\d+$/
	},
	mainTitle: [required('主标题')],
	liveState: [required('直播类型')],
	liveBeginTime: {
		required: true,
		message: '直播开始时间不为空',
		trigger: 'change'
	},
	liveOverTime: {
		required: true,
		message: '直播结束时间不为空',
		trigger: 'change'
	}
}
// 验证并提交数据
const onSubmit = () => {
	formRef.value
		.validate()
		.then(() => {
			if (validateLiveTime(formData.value)) {
				submitLoading.value = true
				const formDataParam = cloneDeep(formData.value)
				liveBizApi
					.liveBizSubmitForm(formDataParam, formDataParam.id)
					.then(() => {
						onClose()
						emit('successful')
					})
					.finally(() => {
						submitLoading.value = false
					})
			}

		})
		.catch(() => {})
}

const validateLiveTime = (formDataParam) => {

	// todo一下， 后面得修改一下，开播后不可以修改开始时间，以及其他限制
	if (!formDataParam.liveBeginTime || !formDataParam.liveOverTime) {
		message.error('直播开始时间不能大于直播结束时间，并且不能小于当前时间');
		return false;
	} else if (formDataParam.liveBeginTime >= formDataParam.liveOverTime
		|| new Date(formDataParam.liveBeginTime) < new Date()
	) {
		message.error('直播开始时间不能大于直播结束时间，并且不能小于当前时间');
		return false;
	}
	return true;
}
// 抛出函数
defineExpose({
	onOpen
})
</script>
