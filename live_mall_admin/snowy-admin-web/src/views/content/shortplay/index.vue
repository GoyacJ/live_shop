<template>
	<div class="shortplay-container">
		<s-table
			ref="tableRef"
			:columns="columns"
			:data="loadData"
			row-key="id"
			:alert="false"
			bordered
			:row-selection="rowSelection"
			:tool-config="{ refresh: true, columnSetting: true, height: true, striped: true }"
		>
			<!-- 表格上方操作区 -->
			<template #operator>
				<a-space>
					<a-button type="primary" @click="handleAdd">
						<template #icon>
							<plus-outlined />
						</template>
						新增短剧
					</a-button>
					<a-button danger @click="handleBatchDelete" :disabled="!selectedRowKeys.length">
						<template #icon>
							<delete-outlined />
						</template>
						批量删除
					</a-button>
				</a-space>
			</template>

			<!-- 搜索区域 -->
			<template #search>
				<a-form ref="searchFormRef" :model="searchFormState" layout="inline" class="search-form">
					<a-form-item label="短剧名称" name="title">
						<a-input v-model:value="searchFormState.title" placeholder="请输入短剧名称" allow-clear />
					</a-form-item>
					<a-form-item label="分类" name="categoryId">
						<a-select
							v-model:value="searchFormState.category"
							placeholder="请选择分类"
							style="width: 200px"
							allow-clear
						>
							<a-select-option v-for="item in categoryList" :key="item.value" :value="item.value">
								{{ item.label }}
							</a-select-option>
						</a-select>
					</a-form-item>
					<a-form-item label="状态" name="status">
						<a-select v-model:value="searchFormState.status" placeholder="请选择状态" style="width: 150px" allow-clear>
							<a-select-option value="1">已发布</a-select-option>
							<a-select-option value="0">未发布</a-select-option>
						</a-select>
					</a-form-item>
					<a-form-item>
						<a-space>
							<a-button type="primary" @click="reloadTable">查询</a-button>
							<a-button @click="resetSearch">重置</a-button>
						</a-space>
					</a-form-item>
				</a-form>
			</template>

			<!-- 表格内容自定义 -->
			<template #bodyCell="{ column, record }">
				<!-- 短剧封面 -->
				<template v-if="column.dataIndex === 'cover'">
					<div class="shortplay-cover">
						<img :src="record.cover" alt="短剧封面" class="cover-img" />
					</div>
				</template>

				<!-- 标签展示 -->
				<template v-if="column.dataIndex === 'tags'">
					<a-space wrap>
						<a-tag v-for="tag in getTagsArray(record.tags)" :key="tag" color="blue">
							{{ $TOOL.dictTypeData('SHORTPLAY_LABEL', tag) }}
						</a-tag>
					</a-space>
				</template>

				<!-- 分类展示 -->
				<template v-if="column.dataIndex === 'categoryName'">
					<a-tag color="green">{{ $TOOL.dictTypeData('SHORTPLAY_TYPE', record.category) }}</a-tag>
				</template>

				<!-- 集数展示 -->
				<template v-if="column.dataIndex === 'episodeCount'">
					<a-badge :count="record.episodeCount" :number-style="{ backgroundColor: '#52c41a' }" />
				</template>

				<!-- 数据统计 -->
				<template v-if="column.dataIndex === 'stats'">
					<a-tooltip placement="top">
						<template #title>
							<div>观看: {{ record.contentStats.viewCount || 0 }}</div>
							<div>点赞: {{ record.contentStats.likeCount || 0 }}</div>
							<div>评论: {{ record.contentStats.commentCount || 0 }}</div>
							<div>分享: {{ record.contentStats.shareCount || 0 }}</div>
							<div>收藏: {{ record.contentStats.collectCount || 0 }}</div>
							<div>追剧: {{ record.contentStats.followCount || 0 }}</div>
						</template>
						<a-button type="link">
							<template #icon>
								<bar-chart-outlined />
							</template>
							查看数据
						</a-button>
					</a-tooltip>
				</template>

				<!-- 状态 -->
				<template v-if="column.dataIndex === 'status'">
					<a-switch :checked="record.status === 1" @change="(checked) => handleStatusChange(checked, record)" />
				</template>

				<!-- 操作列 -->
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="handleEdit(record)">编辑</a>
						<a-divider type="vertical" />
						<a @click="handleManageEpisodes(record)">集数管理</a>
						<a-divider type="vertical" />
						<a-popconfirm
							title="确定要删除这个短剧吗?"
							@confirm="handleDelete(record)"
							ok-text="确定"
							cancel-text="取消"
						>
							<a style="color: #ff4d4f">删除</a>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>

		<!-- 表单弹窗 -->
		<shortplay-form ref="formRef" @successful="reloadTable" @manageEpisodes="handleManageEpisodes" />

		<!-- 集数管理弹窗 -->
		<episode-manager ref="episodeManagerRef" @successful="reloadTable" />
	</div>
</template>

<script setup name="shortplay">
	import { message, Modal } from 'ant-design-vue'
	import shortplayApi from '@/api/content/shortplayApi'
	import ShortplayForm from './form.vue'
	import EpisodeManager from './episodeManager.vue'
	import tool from '@/utils/tool'

	// 表格ref
	const tableRef = ref()
	const formRef = ref()
	const episodeManagerRef = ref()

	// 搜索表单
	const searchFormRef = ref()
	const searchFormState = reactive({
		title: '',
		category: undefined,
		status: undefined
	})

	// 表格列定义
	const columns = ref([
		{
			title: '封面',
			dataIndex: 'cover',
			width: 120
		},
		{
			title: '短剧名称',
			dataIndex: 'title',
			ellipsis: true,
			width: 180
		},
		{
			title: '分类',
			dataIndex: 'categoryName',
			width: 100
		},
		{
			title: '标签',
			dataIndex: 'tags',
			width: 180
		},
		{
			title: '集数',
			dataIndex: 'episodeCount',
			width: 80,
			align: 'center'
		},
		{
			title: '简介',
			dataIndex: 'description',
			ellipsis: true,
			width: 200
		},
		{
			title: '数据统计',
			dataIndex: 'stats',
			width: 100,
			align: 'center'
		},
		{
			title: '状态',
			dataIndex: 'status',
			width: 80,
			align: 'center'
		},
		{
			title: '创建时间',
			dataIndex: 'createTime',
			width: 180
		},
		{
			title: '操作',
			dataIndex: 'action',
			fixed: 'right',
			width: 200
		}
	])

	// 批量操作选中的行
	const selectedRowKeys = ref([])
	const rowSelection = computed(() => {
		return {
			selectedRowKeys: selectedRowKeys.value,
			onChange: (keys) => {
				selectedRowKeys.value = keys
			}
		}
	})

	// 分类列表从字典获取
	const categoryList = computed(() => tool.dictList('SHORTPLAY_TYPE'))

	// 表格数据加载方法
	const loadData = (parameter) => {
		const params = {
			...parameter,
			...searchFormState
		}

		return shortplayApi.shortplayPage(params).then((res) => {
			return res
		})
	}

	// 重载表格
	const reloadTable = () => {
		tableRef.value.refresh(true)
	}

	// 重置搜索条件
	const resetSearch = () => {
		searchFormRef.value.resetFields()
		reloadTable()
	}

	// 新增短剧
	const handleAdd = () => {
		formRef.value.openForm()
	}

	// 编辑短剧
	const handleEdit = (record) => {
		formRef.value.openForm(record.id)
	}

	// 管理剧集
	const handleManageEpisodes = (record) => {
		episodeManagerRef.value.openManager(record.id, record.title)
	}

	// 删除单个短剧
	const handleDelete = async (record) => {
		try {
			await shortplayApi.shortplayDelete([{ id: record.id }])
			message.success('删除成功')
			await reloadTable()
		} catch (error) {
			message.error('操作失败')
		}
	}

	// 批量删除短剧
	const handleBatchDelete = () => {
		if (!selectedRowKeys.value.length) {
			message.warning('请至少选择一条记录')
			return
		}

		Modal.confirm({
			title: '确认删除',
			content: `是否确认删除选中的 ${selectedRowKeys.value.length} 条记录？`,
			okText: '确认',
			cancelText: '取消',
			onOk: async () => {
				try {
					const params = selectedRowKeys.value.map((id) => ({ id }))
					await shortplayApi.shortplayDelete(params)
					selectedRowKeys.value = []
					await reloadTable()
				} catch (error) {
					message.error('操作失败')
				}
			}
		})
	}

	// 修改短剧状态
	const handleStatusChange = async (checked, record) => {
		try {
			const res = await shortplayApi.changeStatus({
				id: record.id,
				status: checked ? 1 : 0
			})

			message.success(`短剧已${checked ? '发布' : '下架'}`)
			reloadTable()
		} catch (error) {
			message.error('操作失败')
		}
	}

	// 处理标签显示
	const getTagsArray = (tags) => {
		if (!tags) return []
		return typeof tags === 'string' ? tags.split(',') : tags
	}
</script>

<style lang="less" scoped>
	.shortplay-container {
		padding: 24px;
		background: #fff;

		.search-form {
			margin-bottom: 16px;
		}

		.shortplay-cover {
			width: 80px;
			height: 45px;
			overflow: hidden;
			border-radius: 4px;

			.cover-img {
				width: 100%;
				height: 100%;
				object-fit: cover;
			}
		}
	}
</style>
