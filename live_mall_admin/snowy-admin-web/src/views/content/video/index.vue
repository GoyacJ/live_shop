<template>
	<div class="video-container">
		<!-- 搜索区域 -->
		<a-card class="search-card" :bordered="false">
			<a-form layout="inline" :model="searchFormState">
				<a-form-item label="视频标题">
					<a-input v-model:value="searchFormState.title" allow-clear placeholder="请输入视频标题" />
				</a-form-item>
				<a-form-item label="分类">
					<a-select
						v-model:value="searchFormState.category"
						placeholder="请选择分类"
						style="min-width: 150px"
						allow-clear
					>
						<a-select-option v-for="item in categoryOptions" :key="item.value" :value="item.value">
							{{ item.label }}
						</a-select-option>
					</a-select>
				</a-form-item>
				<a-form-item label="状态">
					<a-select
						v-model:value="searchFormState.status"
						placeholder="请选择状态"
						style="min-width: 120px"
						allow-clear
					>
						<a-select-option :value="0">未发布</a-select-option>
						<a-select-option :value="1">已发布</a-select-option>
					</a-select>
				</a-form-item>
				<a-form-item>
					<a-space>
						<a-button type="primary" @click="reloadTable">
							<template #icon>
								<search-outlined />
							</template>
							搜索
						</a-button>
						<a-button @click="resetSearchForm">
							<template #icon>
								<redo-outlined />
							</template>
							重置
						</a-button>
					</a-space>
				</a-form-item>
			</a-form>
		</a-card>

		<!-- 操作按钮区域 -->
		<a-card class="table-card" :bordered="false">
			<template #title>
				<a-space>
					<a-button type="primary" @click="openForm()">
						<template #icon>
							<plus-outlined />
						</template>
						新增
					</a-button>
					<a-button danger :disabled="selectedRowKeys.length === 0" @click="batchDelete">
						<template #icon>
							<delete-outlined />
						</template>
						批量删除
					</a-button>
					<a-button @click="openBatchUploadModal">
						<template #icon>
							<upload-outlined />
						</template>
						批量上传
					</a-button>
				</a-space>
			</template>

			<!-- 表格区域 -->
			<s-table
				ref="tableRef"
				:columns="columns"
				:data="loadData"
				row-key="id"
				:alert="false"
				bordered
				:row-selection="{ selectedRowKeys, onChange: onSelectChange }"
			>
				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'title'">
						<a-space>
							<a-avatar :src="record.cover" v-if="record.cover" shape="square" :size="36" />
							<span>{{ record.title }}</span>
						</a-space>
					</template>
					<template v-if="column.dataIndex === 'category'">
						<a-tag>{{ getCategoryLabel(record.category) }}</a-tag>
					</template>
					<template v-if="column.dataIndex === 'tags'">
						<a-space wrap>
							<a-tag v-for="tag in getTagsArray(record.tags)" :key="tag" color="blue">
								{{ getTagLabel(tag) }}
							</a-tag>
						</a-space>
					</template>
					<template v-if="column.dataIndex === 'stats'">
						<a-space>
							<span title="播放量"><video-camera-outlined /> {{ record.contentStats.viewCount || 0 }}</span>
							<span title="点赞数"><like-outlined /> {{ record.contentStats.likeCount || 0 }}</span>
							<span title="收藏数"><star-outlined /> {{ record.contentStats.collectCount || 0 }}</span>
							<span title="分享数"><share-alt-outlined /> {{ record.contentStats.shareCount || 0 }}</span>
							<span title="评论次数"><comment-outlined /> {{ record.contentStats.commentCount || 0 }}</span>
						</a-space>
					</template>
					<template v-if="column.dataIndex === 'status'">
						<a-tag :color="record.status === 1 ? 'green' : 'orange'">
							{{ record.status === 1 ? '已发布' : '未发布' }}
						</a-tag>
					</template>
					<template v-if="column.dataIndex === 'action'">
						<a-space>
							<a-button type="link" size="small" @click="openForm(record)">编辑</a-button>
							<a-button type="link" size="small" @click="changeStatus(record)">
								{{ record.status === 1 ? '下架' : '发布' }}
							</a-button>
							<a-popconfirm
								title="确定要删除此视频吗？"
								ok-text="确定"
								cancel-text="取消"
								@confirm="deleteItem(record.id)"
							>
								<a-button type="link" danger size="small">删除</a-button>
							</a-popconfirm>
						</a-space>
					</template>
				</template>
			</s-table>
		</a-card>

		<!-- 表单弹窗 -->
		<video-form :visible="formVisible" :data="currentRecord" @close="closeForm" @success="handleFormSuccess" />
		<!-- 批量上传弹窗 -->
		<a-modal
			v-model:open="batchUploadVisible"
			title="批量上传视频"
			:width="600"
			:footer="null"
			@cancel="resetBatchUpload"
		>
			<a-form layout="vertical">
				<a-form-item label="视频分类" required>
					<a-select v-model:value="batchCategory" :options="categoryOptions" placeholder="请选择分类" allow-clear />
				</a-form-item>
				<a-form-item label="视频标签" required>
					<a-select
						v-model:value="batchTags"
						placeholder="请选择标签"
						:options="tagsOptions"
						mode="multiple"
						allow-clear
					></a-select>
				</a-form-item>
				<a-form-item label="视频文件（可多选）" required>
					<xn-upload
						ref="batchUploadRef"
						uploadMode="drag"
						:uploadNumber="20"
						:accept="'video/*'"
						:uploadText="'选择或拖拽视频文件到此处'"
						:uploadResultType="'url'"
						:uploadResultCategory="'array'"
						:showUploadList="true"
						:completeResult="true"
					/>
				</a-form-item>
				<a-form-item>
					<a-button type="primary" :loading="batchUploading" @click="handleBatchUpload" :disabled="!batchCategory"
						>上传
					</a-button>
					<a-button style="margin-left: 8px" @click="resetBatchUpload">取消</a-button>
				</a-form-item>
			</a-form>
		</a-modal>
	</div>
</template>

<script setup name="video">
	import { ref, reactive, onMounted } from 'vue'
	import { message, Modal } from 'ant-design-vue'
	import {
		SearchOutlined,
		RedoOutlined,
		PlusOutlined,
		DeleteOutlined,
		VideoCameraOutlined,
		LikeOutlined,
		StarOutlined,
		UploadOutlined
	} from '@ant-design/icons-vue'
	import videoApi from '@/api/content/videoApi'
	import tool from '@/utils/tool'
	import VideoForm from './form.vue'

	// 表格ref
	const tableRef = ref()

	// 表格列定义
	const columns = [
		{
			title: '视频标题',
			dataIndex: 'title',
			width: 300,
			ellipsis: true
		},
		{
			title: '分类',
			dataIndex: 'category',
			width: 120
		},
		{
			title: '标签',
			dataIndex: 'tags',
			width: 200
		},
		{
			title: '数据统计',
			dataIndex: 'stats',
			width: 150
		},
		{
			title: '排序',
			dataIndex: 'sort',
			width: 80
		},
		{
			title: '状态',
			dataIndex: 'status',
			width: 100
		},
		{
			title: '创建时间',
			dataIndex: 'createTime',
			width: 170
		},
		{
			title: '操作',
			dataIndex: 'action',
			width: 180,
			fixed: 'right'
		}
	]

	// 搜索表单
	const searchFormState = reactive({
		title: '',
		category: undefined,
		status: undefined
	})

	// 字典数据
	const categoryOptions = ref([])
	const tagsOptions = ref([])

	// 表单弹窗状态
	const formVisible = ref(false)
	const currentRecord = ref({})

	// 选中行
	const selectedRowKeys = ref([])
	const onSelectChange = (keys) => {
		selectedRowKeys.value = keys
	}

	// 获取字典数据
	const loadDictData = async () => {
		try {
			categoryOptions.value = tool.dictTypeList('VIDEO_TYPE').map((item) => ({
				label: item.dictLabel,
				value: item.dictValue
			}))
			tagsOptions.value = tool.dictTypeList('VIDEO_LABEL').map((item) => ({
				label: item.dictLabel,
				value: item.dictValue
			}))
		} catch (error) {
			console.error('获取字典数据失败', error)
		}
	}

	// 获取分类标签
	const getCategoryLabel = (value) => {
		const category = categoryOptions.value.find((item) => item.value === value)
		return category ? category.label : value
	}

	// 获取标签标签
	const getTagLabel = (value) => {
		const tag = tagsOptions.value.find((item) => item.value === value)
		return tag ? tag.label : value
	}

	// 处理标签显示
	const getTagsArray = (tags) => {
		if (!tags) return []
		return typeof tags === 'string' ? tags.split(',') : tags
	}

	// s-table数据加载方法
	const loadData = (params) => {
		return videoApi
			.videoPage({
				...params,
				...searchFormState
			})
			.then((res) => res)
	}

	// 刷新表格
	const reloadTable = () => {
		tableRef.value && tableRef.value.refresh(true)
	}

	// 重置搜索表单
	const resetSearchForm = () => {
		Object.assign(searchFormState, {
			title: '',
			category: undefined,
			status: undefined
		})
		reloadTable()
	}

	// 打开表单弹窗
	const openForm = (record) => {
		currentRecord.value = record || {}
		formVisible.value = true
	}

	// 关闭表单弹窗
	const closeForm = () => {
		formVisible.value = false
		currentRecord.value = {}
	}

	// 表单提交成功回调
	const handleFormSuccess = () => {
		reloadTable()
	}

	// 改变视频状态（发布/下架）
	const changeStatus = async (record) => {
		const newStatus = record.status === 1 ? 0 : 1
		await videoApi.changeStatus({ id: record.id, status: newStatus })
		message.success(newStatus === 1 ? '发布成功' : '下架成功')
		reloadTable()
	}

	// 删除单个视频
	const deleteItem = async (id) => {
		await videoApi.videoDelete([{ id }])
		reloadTable()
	}

	// 批量删除视频
	const batchDelete = () => {
		if (selectedRowKeys.value.length === 0) {
			message.warning('请选择要删除的视频')
			return
		}

		Modal.confirm({
			title: '确认删除',
			content: `确定要删除选中的 ${selectedRowKeys.value.length} 条视频记录吗？`,
			okText: '确定',
			cancelText: '取消',
			onOk: async () => {
				try {
					const params = selectedRowKeys.value.map((id) => ({ id }))
					await videoApi.videoDelete(params)
					message.success('批量删除成功')
					selectedRowKeys.value = []
					reloadTable()
				} catch (error) {
					console.error('批量删除失败', error)
					message.error('批量删除失败')
				}
			}
		})
	}

	const batchUploadVisible = ref(false)
	const batchCategory = ref()
	const batchTags = ref([])
	const batchUploading = ref(false)
	const batchUploadRef = ref()

	const openBatchUploadModal = () => {
		batchUploadVisible.value = true
		if (batchUploadRef.value && batchUploadRef.value.uploadFileList) {
			batchUploadRef.value.uploadFileList().splice(0)
		}
	}
	const resetBatchUpload = () => {
		batchUploadVisible.value = false
		batchCategory.value = undefined
		batchTags.value = []
		batchUploading.value = false
		if (batchUploadRef.value && batchUploadRef.value.uploadFileList) {
			batchUploadRef.value.uploadFileList().splice(0)
		}
	}
	const handleBatchUpload = async () => {
		if (!batchCategory.value) {
			message.warning('请先选择分类')
			return
		}
		if (!batchTags.value.length) {
			message.warning('请先选择标签')
			return
		}
		const files = batchUploadRef.value?.uploadFileList?.() || []
		if (!files.length) {
			message.warning('请先上传视频文件')
			return
		}
		batchUploading.value = true
		try {
			const data = files.map((f) => ({
				title: f.name,
				videoUrl: f.url,
				category: batchCategory.value,
				tags: batchTags.value.join(','),
				duration: 0,
				viewCount: 0,
				likeCount: 0,
				followCount: 0,
				shareCount: 0,
				commentCount: 0,
				collectCount: 0,
				status: 0,
				source: '',
				autoGenerateCover: 0,
				downloadable: 0,
				shareable: 0,
				allowComment: 0,
				sort: 0,
				cover: '',
				description: ''
			}))
			await videoApi.batchAdd(data)
			resetBatchUpload()
			reloadTable()
		} catch (e) {
			console.error(e)
			message.error('批量上传失败')
		} finally {
			batchUploading.value = false
		}
	}

	onMounted(() => {
		loadDictData()
	})
</script>

<style lang="less" scoped>
	.video-container {
		padding: 16px;

		.search-card {
			margin-bottom: 16px;
		}

		.table-card {
			min-height: 500px;
		}
	}

	/* 批量上传弹窗内上传进度条高度和字体优化 */
	:deep(.ant-upload-list-item-progress .ant-progress-inner) {
		height: 20px !important;
		min-height: 20px !important;
	}

	:deep(.ant-upload-list-item-progress .ant-progress-text) {
		font-size: 14px !important;
		line-height: 20px !important;
	}
</style>
