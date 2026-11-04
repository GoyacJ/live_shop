<template>
	<a-card :bordered="false">
		<!-- 统计卡片展示区域 -->
		<a-row :gutter="16" class="mb-4">
			<a-col :span="6">
				<a-card class="statistic-card">
					<a-statistic :value="statisticsData.h5VisitsNo" title="H5访问人数">
						<template #prefix>
							<mobile-outlined style="color: #1890ff" />
						</template>
					</a-statistic>
				</a-card>
			</a-col>
			<a-col :span="6">
				<a-card class="statistic-card">
					<a-statistic :value="statisticsData.appDownloadNo" title="APP下载人数">
						<template #prefix>
							<download-outlined style="color: #52c41a" />
						</template>
					</a-statistic>
				</a-card>
			</a-col>
			<a-col :span="6">
				<a-card class="statistic-card">
					<a-statistic :value="statisticsData.appRegisterNo" title="APP注册人数">
						<template #prefix>
							<user-add-outlined style="color: #faad14" />
						</template>
					</a-statistic>
				</a-card>
			</a-col>
			<a-col :span="6">
				<a-card class="statistic-card">
					<a-statistic :value="statisticsData.appVisitsNo" title="APP访问人数">
						<template #prefix>
							<team-outlined style="color: #f5222d" />
						</template>
					</a-statistic>
				</a-card>
			</a-col>
		</a-row>

		<!-- 搜索表单 -->
		<statistics-form ref="formRef" @search="handleSearch" @reset="handleReset" />

		<!-- 直播信息表格 -->
		<a-table
			:columns="columns"
			:data-source="statisticsData.liveInfos"
			:pagination="false"
			:loading="loading"
			bordered
			row-key="liveId"
		>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'liveTime'">
					{{ formatDuration(record.liveTime) }}
				</template>
				<template v-if="column.dataIndex === 'visitTime'">
					{{ formatDuration(record.visitTime) }}
				</template>
			</template>
		</a-table>
	</a-card>
</template>

<script setup name="liveStatistics">
	import { ref, reactive, onMounted } from 'vue'
	import { MobileOutlined, DownloadOutlined, UserAddOutlined, TeamOutlined } from '@ant-design/icons-vue'
	import liveStatisticsApi from '@/api/live/liveStatisticsApi'
	import StatisticsForm from './form.vue'

	// 表单引用
	const formRef = ref()

	// 统计数据
	const statisticsData = reactive({
		h5VisitsNo: 0,
		appDownloadNo: 0,
		appRegisterNo: 0,
		appVisitsNo: 0,
		liveInfos: []
	})

	// 加载状态
	const loading = ref(false)

	// 表格列定义
	const columns = [
		{
			title: '直播间',
			dataIndex: 'liveName',
			key: 'liveName'
		},
		{
			title: '直播场次',
			dataIndex: 'liveBroadcasts',
			key: 'liveBroadcasts'
		},
		{
			title: '在线人数',
			dataIndex: 'liveOnlineNo',
			key: 'liveOnlineNo'
		},
		{
			title: '累计观看人数',
			dataIndex: 'liveVisitNo',
			key: 'liveVisitNo'
		},
		{
			title: '开播时长(小时)',
			dataIndex: 'liveTime',
			key: 'liveTime'
		},
		{
			title: '平均观看时长(分钟)',
			dataIndex: 'visitTime',
			key: 'visitTime'
		}
	]

	// 处理搜索
	const handleSearch = (params) => {
		loading.value = true
		liveStatisticsApi
			.info(params)
			.then((res) => {
				Object.assign(statisticsData, res)
			})
			.catch((err) => {
				console.error('获取数据失败:', err)
			})
			.finally(() => {
				loading.value = false
			})
	}

	// 处理重置
	const handleReset = () => {
		handleSearch({})
	}

	// 格式化时长显示
	const formatDuration = (seconds) => {
		if (!seconds) return '0'

		const hours = Math.floor(seconds / 3600)
		const minutes = Math.floor((seconds % 3600) / 60)

		if (seconds >= 3600) {
			return `${hours}小时${minutes > 0 ? minutes + '分钟' : ''}`
		} else {
			return `${minutes}分钟`
		}
	}

	// 初始化
	onMounted(() => {
		handleSearch({})
	})
</script>

<style scoped>
	.statistic-card {
		border-radius: 4px;
		margin-bottom: 16px;
	}
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
	:deep(.ant-form) {
		margin-bottom: 8px;
	}
</style>
