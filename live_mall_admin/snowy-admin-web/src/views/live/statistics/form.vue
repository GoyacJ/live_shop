<template>
	<a-form ref="searchFormRef" name="advanced_search" :model="formData" class="mb-2" :colon="false">
		<a-row :gutter="24">
			<a-col :span="7">
				<a-form-item label="查询日期" name="dateRange">
					<a-range-picker
						v-model:value="formData.dateRange"
						value-format="YYYY-MM-DD HH:mm:ss"
						:disabled-date="disabledDate"
						show-time
						@change="onDateRangeChange"
						style="width: 100%"
					/>
				</a-form-item>
			</a-col>
			<a-col :span="7">
				<a-form-item label="直播间" name="liveId">
					<a-select v-model:value="formData.liveId" placeholder="请选择直播间" allow-clear style="width: 100%">
						<a-select-option v-for="live in liveRooms" :key="live.id" :value="live.id">
							{{ live.name }}
						</a-select-option>
					</a-select>
				</a-form-item>
			</a-col>
			<a-col :span="10">
				<a-form-item label=" ">
					<a-space>
						<a-button type="primary" @click="search">查询</a-button>
						<a-button @click="reset">重置</a-button>
					</a-space>
				</a-form-item>
			</a-col>
		</a-row>
	</a-form>
</template>

<script setup name="liveStatisticsForm">
	import { ref, reactive, onMounted, watch } from 'vue'
	import dayjs from 'dayjs'
	import liveBizApi from '@/api/live/liveBizApi'

	const emit = defineEmits(['search', 'reset'])

	// 接收父组件传递的属性
	const props = defineProps({
		modelValue: {
			type: Object,
			default: () => ({})
		}
	})

	// 表单数据
	const formData = reactive({
		dateRange: [
			dayjs().subtract(7, 'day').hour(0).minute(0).second(0).format('YYYY-MM-DD HH:mm:ss'),
			dayjs().hour(23).minute(59).second(59).format('YYYY-MM-DD HH:mm:ss')
		],
		liveId: undefined
	})

	// 监听modelValue变化，更新formData
	watch(
		() => props.modelValue,
		(val) => {
			if (val) {
				Object.assign(formData, val)
			}
		},
		{ deep: true }
	)

	// 表单ref
	const searchFormRef = ref()

	// 直播间数据
	const liveRooms = ref([])

	// 禁用日期选择
	const disabledDate = (current) => {
		return current && current > dayjs().endOf('day')
	}

	// 日期范围变更
	const onDateRangeChange = () => {
		// 可以根据需要在此处理日期变更逻辑
	}

	// 查询
	const search = () => {
		emit('search', {
			beginTime: formData.dateRange?.[0] || undefined,
			endTime: formData.dateRange?.[1] || undefined,
			liveId: formData.liveId
		})
	}

	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		emit('reset')
	}

	// 获取直播间列表数据
	const getLiveRooms = () => {
		liveBizApi
			.liveBizList()
			.then((res) => {
				liveRooms.value = res.map((item) => ({
					id: item.id,
					name: item.mainTitle
				}))
			})
			.catch((err) => {
				console.error('获取直播间列表失败:', err)
			})
	}

	// 初始化
	onMounted(() => {
		// 获取直播间列表
		getLiveRooms()
	})

	// 暴露方法
	defineExpose({
		formData,
		searchFormRef,
		reset
	})
</script>

<style scoped>
	:deep(.ant-form-item-label > label) {
		color: rgba(0, 0, 0, 0.85);
		font-weight: 500;
	}
	:deep(.ant-btn-primary) {
		background-color: var(--primary-color);
		border-color: var(--primary-color);
	}
	:deep(.ant-form-item) {
		margin-bottom: 12px;
	}
</style>
