<template>
	<xn-form-container
		:title="`${shortplayTitle} - 集数管理`"
		:width="800"
		:visible="visible"
		:destroy-on-close="true"
		@close="onClose"
	>
		<div class="episode-manager">
			<!-- 操作按钮 -->
			<div class="operation-bar">
				<a-button type="primary" @click="handleAddEpisode">
					<template #icon>
						<plus-outlined />
					</template>
					添加剧集
				</a-button>
				<a-button style="margin-left: 8px" @click="openBatchAddModal">
					<template #icon>
						<plus-outlined />
					</template>
					批量添加剧集
				</a-button>
			</div>

			<!-- 剧集列表 -->
			<s-table ref="episodeTableRef" :columns="columns" :data="loadEpisodeData" row-key="id" :alert="false" bordered>
				<template #bodyCell="{ column, record }">
					<!-- 剧集封面 -->
					<template v-if="column.dataIndex === 'cover'">
						<div class="episode-cover">
							<img :src="record.cover" alt="剧集封面" class="cover-img" />
						</div>
					</template>
					<!-- 收费标志 -->
					<template v-if="column.dataIndex === 'isFree'">
						<a-tag :color="record.isFree ? 'green' : 'orange'">
							{{ record.isFree ? '免费' : '收费' }}
						</a-tag>
					</template>
					<!-- 操作列 -->
					<template v-if="column.dataIndex === 'action'">
						<a-space>
							<a @click="handleEditEpisode(record)">编辑</a>
							<a-divider type="vertical" />
							<a-popconfirm
								title="确定要删除这个剧集吗?"
								@confirm="handleDeleteEpisode(record)"
								ok-text="确定"
								cancel-text="取消"
							>
								<a style="color: #ff4d4f">删除</a>
							</a-popconfirm>
						</a-space>
					</template>
				</template>
			</s-table>
		</div>

		<!-- 剧集表单 -->
		<a-modal
			v-model:visible="episodeFormVisible"
			:title="episodeForm.id ? '编辑剧集' : '添加剧集'"
			@ok="submitEpisodeForm"
			:confirm-loading="submitLoading"
			@cancel="closeEpisodeForm"
			:width="680"
		>
			<a-form
				ref="episodeFormRef"
				:model="episodeForm"
				:rules="episodeFormRules"
				:label-col="{ span: 4 }"
				:wrapper-col="{ span: 20 }"
			>
				<a-form-item label="剧集标题" name="title">
					<a-input v-model:value="episodeForm.title" placeholder="请输入剧集标题" allow-clear />
				</a-form-item>

				<a-row>
					<a-col :span="12">
						<a-form-item label="剧集编号" name="episodeNumber" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
							<a-input-number v-model:value="episodeForm.episodeNumber" :min="1" style="width: 100%" />
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="收费设置" name="isFree" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
							<a-radio-group v-model:value="episodeForm.isFree">
								<a-radio :value="true">免费</a-radio>
								<a-radio :value="false">收费</a-radio>
							</a-radio-group>
						</a-form-item>
					</a-col>
				</a-row>

				<a-row>
					<a-col :span="12">
						<a-form-item label="时长(秒)" name="duration" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
							<a-input-number v-model:value="episodeForm.duration" :min="0" style="width: 100%" />
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="排序" name="sortCode" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
							<a-input-number v-model:value="episodeForm.sort" :min="0" style="width: 100%" />
						</a-form-item>
					</a-col>
				</a-row>

				<a-form-item label="剧集封面" name="cover">
					<xn-upload
						v-model:value="episodeForm.cover"
						:upload-mode="'image'"
						:upload-number="1"
						:upload-text="'上传封面'"
						accept="image/*"
					>
						<template #explain>
							<div class="upload-tip">建议尺寸：16:9，大小不超过2MB</div>
						</template>
					</xn-upload>
				</a-form-item>

				<a-form-item label="视频文件" name="videoUrl">
					<a-radio-group v-model:value="videoInputMode" @change="onVideoInputModeChange">
						<a-radio :value="'select'">选择视频</a-radio>
						<a-radio :value="'manual'">手动输入</a-radio>
					</a-radio-group>

					<!-- 选择视频模式 -->
					<div v-if="videoInputMode === 'select'" class="video-select-container mt-2">
						<a-row :gutter="16">
							<a-col :span="18">
								<a-input-search
									v-model:value="videoSearchKeyword"
									placeholder="请输入视频名称搜索"
									enter-button="搜索"
									@search="searchVideos"
									allow-clear
								/>
							</a-col>
							<a-col :span="6">
								<a-button @click="showVideoSelector">
									<template #icon>
										<search-outlined />
									</template>
									浏览视频
								</a-button>
							</a-col>
						</a-row>

						<div v-if="episodeForm.videoUrl" class="selected-video mt-2">
							<a-tag closable @close="clearSelectedVideo">
								<video-camera-outlined />
								{{ episodeForm.videoName || episodeForm.videoUrl }}
							</a-tag>
						</div>
					</div>

					<!-- 手动输入模式 -->
					<div v-if="videoInputMode === 'manual'" class="manual-video-input mt-2">
						<a-input v-model:value="episodeForm.videoUrl" placeholder="请输入视频URL" allow-clear />
						<div class="upload-tip">支持mp4、m3u8等格式，如需上传请先到视频管理上传</div>
					</div>
				</a-form-item>

				<a-form-item label="剧集简介" name="description">
					<a-textarea v-model:value="episodeForm.description" placeholder="请输入剧集简介" :rows="3" allow-clear />
				</a-form-item>

				<!-- 剧集表单新增商品相关字段 -->
				<a-form-item label="选择商品" name="goodsId">
					<a-row gutter="8">
						<a-col :span="18">
							<a-input
								v-model:value="episodeForm.goodsName"
								placeholder="请选择商品"
								readonly
								allow-clear
								@click="showGoodsSelector"
							/>
						</a-col>
						<a-col :span="6">
							<a-button @click="showGoodsSelector">选择商品</a-button>
							<a-button v-if="episodeForm.goodsId" @click="clearSelectedGoods" style="margin-left: 8px">清除</a-button>
						</a-col>
					</a-row>
				</a-form-item>
				<a-form-item label="商品开始秒数" name="startShowGoodsSecond">
					<a-input-number v-model:value="episodeForm.startShowGoodsSecond" :min="0" />
				</a-form-item>
				<a-form-item label="商品展示秒数" name="showGoodsSecond">
					<a-input-number v-model:value="episodeForm.showGoodsSecond" :min="0" />
				</a-form-item>
			</a-form>
		</a-modal>

		<!-- 视频选择器模态框 -->
		<a-modal
			v-model:visible="videoSelectorVisible"
			title="选择视频"
			width="800px"
			@ok="confirmSelectVideo"
			@cancel="cancelSelectVideo"
		>
			<div class="video-selector">
				<!-- 搜索栏 -->
				<div class="search-bar">
					<a-row :gutter="16">
						<a-col :span="18">
							<a-input-search
								v-model:value="videoSelectorKeyword"
								placeholder="请输入视频名称搜索"
								enter-button="搜索"
								@search="searchSelectorVideos"
								allow-clear
							/>
						</a-col>
					</a-row>
				</div>

				<!-- 视频列表 -->
				<a-table
					:columns="videoColumns"
					:data-source="videoList"
					:pagination="{
						total: videoTotal,
						current: videoPage,
						pageSize: videoPageSize,
						onChange: onVideoPageChange
					}"
					:loading="videoLoading"
					:row-key="(record) => record.id"
					:row-selection="{
						type: 'radio',
						selectedRowKeys: selectedVideoKeys,
						onChange: onSelectedVideoChange
					}"
					:scroll="{ y: 400 }"
				>
					<template #bodyCell="{ column, record }">
						<!-- 视频预览 -->
						<template v-if="column.dataIndex === 'preview'">
							<div class="video-preview">
								<img v-if="record.cover" :src="record.cover" alt="视频预览" class="preview-img" />
								<div v-else class="no-preview">无预览</div>
							</div>
						</template>

						<!-- 分类 -->
						<template v-if="column.dataIndex === 'category'">
							<a-tag>{{ $TOOL.dictTypeData('VIDEO_TYPE', record.category) }}</a-tag>
						</template>

						<!-- 时长 -->
						<template v-if="column.dataIndex === 'duration'">
							{{ formatDuration(record.duration) }}
						</template>
					</template>
				</a-table>
			</div>
		</a-modal>

		<!-- 商品选择器模态框 -->
		<a-modal
			v-model:visible="goodsSelectorVisible"
			title="选择商品"
			width="800px"
			@ok="confirmSelectGoods"
			@cancel="cancelSelectGoods"
		>
			<div class="goods-selector">
				<!-- 搜索栏 -->
				<div class="search-bar">
					<a-row :gutter="16">
						<a-col :span="18">
							<a-input-search
								v-model:value="goodsSelectorKeyword"
								placeholder="请输入商品名称搜索"
								enter-button="搜索"
								@search="searchSelectorGoodsList"
								allow-clear
							/>
						</a-col>
					</a-row>
				</div>
				<!-- 商品列表 -->
				<a-table
					:columns="goodsColumns"
					:data-source="goodsList"
					:pagination="{
						total: goodsTotal,
						current: goodsPage,
						pageSize: goodsPageSize,
						onChange: onGoodsPageChange
					}"
					:loading="goodsLoading"
					:row-key="(record) => record.id"
					:row-selection="{
						type: 'radio',
						selectedRowKeys: selectedGoodsKeys,
						onChange: onSelectedGoodsChange
					}"
					:scroll="{ y: 400 }"
				>
					<template #bodyCell="{ column, record }">
						<template v-if="column.dataIndex === 'hostImage'">
							<div class="goods-cover">
								<img :src="record.hostImage" alt="商品图片" class="cover-img" />
							</div>
						</template>
					</template>
				</a-table>
			</div>
		</a-modal>

		<!-- 批量添加剧集弹窗 -->
		<a-modal v-model:visible="batchAddVisible" title="批量添加剧集" :width="900" :footer="null" @cancel="resetBatchAdd">
			<a-form layout="vertical">
				<a-form-item label="收费设置">
					<a-radio-group v-model:value="batchCommonFields.isFree">
						<a-radio :value="true">免费</a-radio>
						<a-radio :value="false">收费</a-radio>
					</a-radio-group>
				</a-form-item>
				<a-row :gutter="16">
					<a-col :span="12">
						<a-form-item label="起始编号">
							<a-input-number v-model:value="batchCommonFields.startNumber" :min="1" style="width: 100%" />
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="起始排序">
							<a-input-number v-model:value="batchCommonFields.startSort" :min="0" style="width: 100%" />
						</a-form-item>
					</a-col>
				</a-row>
				<a-form-item label="批量选择视频" required>
					<a-button type="primary" @click="openBatchVideoSelector">批量选择视频</a-button>
				</a-form-item>
				<!-- 批量预览与编辑区 -->
				<div v-if="batchEpisodes.length">
					<a-table :data-source="batchEpisodes" :pagination="false" row-key="videoId">
						<a-table-column title="标题" dataIndex="title" key="title">
							<template #default="{ record }">
								<a-input v-model:value="record.title" />
							</template>
						</a-table-column>
						<a-table-column title="视频URL" dataIndex="videoUrl" key="videoUrl">
							<template #default="{ record }">
								<a-input v-model:value="record.videoUrl" disabled />
							</template>
						</a-table-column>
						<a-table-column title="编号" dataIndex="episodeNumber" key="episodeNumber">
							<template #default="{ record }">
								<a-input-number v-model:value="record.episodeNumber" :min="1" />
							</template>
						</a-table-column>
						<a-table-column title="排序" dataIndex="sort" key="sort">
							<template #default="{ record }">
								<a-input-number v-model:value="record.sort" :min="0" />
							</template>
						</a-table-column>
						<a-table-column title="收费" dataIndex="isFree" key="isFree">
							<template #default="{ record }">
								<a-radio-group v-model:value="record.isFree">
									<a-radio :value="true">免费</a-radio>
									<a-radio :value="false">收费</a-radio>
								</a-radio-group>
							</template>
						</a-table-column>
					</a-table>
					<a-button type="primary" style="margin-top: 16px" :loading="batchAdding" @click="handleBatchAddSubmit"
						>批量提交</a-button
					>
				</div>
			</a-form>
			<!-- 批量视频选择器弹窗 -->
			<a-modal
				v-model:visible="batchVideoSelectorVisible"
				title="批量选择视频"
				:width="900"
				@ok="confirmBatchVideoSelect"
				@cancel="batchVideoSelectorVisible = false"
			>
				<a-input-search
					v-model:value="batchVideoSearchKeyword"
					placeholder="搜索视频标题"
					enter-button="搜索"
					@search="searchBatchVideos"
					style="margin-bottom: 12px; width: 300px"
				/>
				<s-table
					ref="batchVideoTableRef"
					:columns="batchVideoColumns"
					:data="loadBatchVideoData"
					row-key="id"
					:row-selection="{ type: 'checkbox', selectedRowKeys: selectedBatchVideoKeys, onChange: onBatchVideoSelect }"
					bordered
					:alert="false"
					:pagination="{ showSizeChanger: true }"
					:loading="batchVideoLoading.value"
					:scroll="{ y: 400 }"
				/>
			</a-modal>
		</a-modal>
	</xn-form-container>
</template>

<script setup name="episodeManager">
	import { message } from 'ant-design-vue'
	import shortplayApi from '@/api/content/shortplayApi'
	import mallGoodsApi from '@/api/shop/mallGoodsApi.js'
	import { PlusOutlined, SearchOutlined, VideoCameraOutlined } from '@ant-design/icons-vue'

	// 定义事件
	const emit = defineEmits(['successful'])

	// 短剧信息
	const shortplayId = ref(null)
	const shortplayTitle = ref('')
	const visible = ref(false)
	const episodeList = ref([])

	// 剧集分页相关变量
	const episodeTotal = ref(0)

	// 表格ref
	const episodeTableRef = ref()

	// 表格列定义
	const columns = ref([
		{
			title: '封面',
			dataIndex: 'cover',
			width: 100
		},
		{
			title: '标题',
			dataIndex: 'title',
			ellipsis: true
		},
		{
			title: '时长',
			dataIndex: 'duration',
			width: 80,
			customRender: ({ text }) => {
				const minutes = Math.floor(text / 60)
				const seconds = text % 60
				return `${minutes}:${seconds.toString().padStart(2, '0')}`
			}
		},
		{
			title: '类型',
			dataIndex: 'isFree',
			width: 80
		},
		{
			title: '排序',
			dataIndex: 'sort',
			width: 80
		},
		{
			title: '剧集编号',
			dataIndex: 'episodeNumber',
			width: 80
		},
		{
			title: '操作',
			dataIndex: 'action',
			width: 150
		}
	])

	// 剧集表单
	const episodeFormRef = ref(null)
	const episodeFormVisible = ref(false)
	const submitLoading = ref(false)
	const episodeForm = reactive({
		id: null,
		shortPlayId: null,
		title: '',
		episodeNumber: 1,
		cover: '',
		videoUrl: '',
		duration: 0,
		description: '',
		isFree: true,
		sort: 0,
		goodsId: null,
		goodsName: '',
		startShowGoodsSecond: 0,
		showGoodsSecond: 0
	})

	// 表单校验规则
	const episodeFormRules = {
		title: [{ required: true, message: '请输入剧集标题', trigger: 'blur' }],
		episodeNumber: [{ required: true, message: '请输入集数', trigger: 'change' }],
		cover: [{ required: true, message: '请上传剧集封面', trigger: 'change' }],
		videoUrl: [
			{
				required: true,
				validator: (rule, value) => {
					if (!value) {
						if (videoInputMode.value === 'select') {
							return Promise.reject('请选择一个视频')
						} else {
							return Promise.reject('请输入视频URL')
						}
					}
					return Promise.resolve()
				},
				trigger: 'change'
			}
		]
	}

	// 视频输入方式
	const videoInputMode = ref('select') // 'select' 或 'manual'
	const videoSearchKeyword = ref('')

	// 视频选择器
	const videoSelectorVisible = ref(false)
	const videoSelectorKeyword = ref('')
	const videoList = ref([])
	const videoTotal = ref(0)
	const videoPage = ref(1)
	const videoPageSize = ref(10)
	const videoLoading = ref(false)
	const selectedVideoKeys = ref([])
	const selectedVideo = ref(null)

	// 视频选择器列定义
	const videoColumns = [
		{
			title: '封面',
			dataIndex: 'preview',
			width: 120
		},
		{
			title: '视频标题',
			dataIndex: 'title',
			ellipsis: true
		},
		{
			title: '分类',
			dataIndex: 'category',
			width: 100
		},
		{
			title: '时长',
			dataIndex: 'duration',
			width: 100
		},
		{
			title: '创建时间',
			dataIndex: 'createTime',
			width: 180
		}
	]

	// 商品选择器
	const goodsSelectorVisible = ref(false)
	const goodsSelectorKeyword = ref('')
	const goodsList = ref([])
	const goodsTotal = ref(0)
	const goodsPage = ref(1)
	const goodsPageSize = ref(10)
	const goodsLoading = ref(false)
	const selectedGoodsKeys = ref([])
	const selectedGoods = ref(null)

	// 商品选择器列定义
	const goodsColumns = [
		{
			title: '封面',
			dataIndex: 'hostImage',
			width: 120
		},
		{
			title: '商品名称',
			dataIndex: 'title',
			ellipsis: true
		},
		{
			title: '价格',
			dataIndex: 'price',
			width: 100
		},
		{
			title: '库存',
			dataIndex: 'stock',
			width: 100
		}
	]

	// 批量添加相关变量
	const batchAddVisible = ref(false)
	const batchAdding = ref(false)
	const batchEpisodes = ref([])
	const batchCommonFields = reactive({
		isFree: true,
		startNumber: 1,
		startSort: 1
	})
	// 批量视频选择器相关
	const batchVideoSelectorVisible = ref(false)
	const batchVideoList = ref([])
	const batchVideoTotal = ref(0)
	const batchVideoPage = ref(1)
	const batchVideoPageSize = ref(10)
	batchVideoPageSize.value = 10
	const batchVideoLoading = ref(false)
	const batchVideoSearchKeyword = ref('')
	const selectedBatchVideoKeys = ref([])
	const selectedBatchVideoRows = ref([])
	const batchVideoColumns = [
		{
			title: '封面',
			dataIndex: 'cover',
			key: 'cover',
			width: 100,
			customRender: ({ record }) => h('img', { src: record.cover, style: 'width:80px;height:45px;object-fit:cover;' })
		},
		{ title: '标题', dataIndex: 'title', key: 'title', ellipsis: true },
		{ title: '分类', dataIndex: 'category', key: 'category', width: 100 },
		{ title: '时长', dataIndex: 'duration', key: 'duration', width: 100 },
		{ title: '创建时间', dataIndex: 'createTime', key: 'createTime', width: 180 }
	]

	// 加载剧集数据（分页、排序、筛选参数由表格自动传入）
	const loadEpisodeData = (params) => {
		return shortplayApi
			.episodePage({
				...params,
				shortPlayId: shortplayId.value
			})
			.then((res) => {
				episodeTotal.value = res.total || 0
				return res
			})
	}

	// 打开管理器
	const openManager = async (id, title) => {
		shortplayId.value = id
		shortplayTitle.value = title || '短剧'
		visible.value = true
		// 表格自动加载，无需手动loadEpisodeList
	}

	// 关闭管理器
	const onClose = () => {
		visible.value = false
		episodeList.value = []
		shortplayId.value = null
		shortplayTitle.value = ''
	}

	// 添加剧集
	const handleAddEpisode = () => {
		resetEpisodeForm()
		episodeForm.shortPlayId = shortplayId.value
		episodeForm.episodeNumber = episodeTotal.value + 1
		episodeForm.sortCode = episodeTotal.value + 1
		episodeFormVisible.value = true
	}

	// 编辑剧集
	const handleEditEpisode = async (record) => {
		resetEpisodeForm()
		try {
			const res = await shortplayApi.episodeDetail(record.id)
			if (res) {
				const data = res
				Object.keys(episodeForm).forEach((key) => {
					if (data[key] !== undefined) {
						episodeForm[key] = data[key]
					}
				})
				// 商品名称回显
				if (data.goodsId) {
					try {
						const goodsDetail = await mallGoodsApi.mallGoodsDetail({ id: data.goodsId })
						episodeForm.goodsName = goodsDetail?.title || ''
					} catch (e) {
						episodeForm.goodsName = ''
					}
				} else {
					episodeForm.goodsName = ''
				}
				episodeFormVisible.value = true
			} else {
				message.error(res.message || '获取剧集详情失败')
			}
		} catch (error) {
			message.error('操作失败')
		}
	}

	// 删除剧集
	const handleDeleteEpisode = async (record) => {
		try {
			const res = await shortplayApi.episodeDelete([{ id: record.id }])
			message.success('删除成功')
			// 刷新表格
			episodeTableRef.value && episodeTableRef.value.refresh(true)
			emit('successful')
		} catch (error) {
			message.error('操作失败')
		}
	}

	// 重置剧集表单
	const resetEpisodeForm = () => {
		episodeForm.id = null
		episodeForm.shortPlayId = shortplayId.value
		episodeForm.title = ''
		episodeForm.episodeNumber = 1
		episodeForm.cover = ''
		episodeForm.videoUrl = ''
		episodeForm.videoId = null
		episodeForm.videoName = ''
		episodeForm.duration = 0
		episodeForm.description = ''
		episodeForm.isFree = true
		episodeForm.sort = 0
		episodeForm.viewCount = 0
		episodeForm.likeCount = 0
		episodeForm.commentCount = 0
		episodeForm.shareCount = 0
		episodeForm.goodsId = null
		episodeForm.goodsName = ''
		episodeForm.startShowGoodsSecond = null
		episodeForm.showGoodsSecond = null

		videoInputMode.value = 'select'
		videoSearchKeyword.value = ''
		selectedVideoKeys.value = []
		selectedVideo.value = null
		selectedGoodsKeys.value = []
		selectedGoods.value = null

		if (episodeFormRef.value) {
			episodeFormRef.value.resetFields()
		}
	}

	// 关闭剧集表单
	const closeEpisodeForm = () => {
		episodeFormVisible.value = false
		resetEpisodeForm()
	}

	// 提交剧集表单
	const submitEpisodeForm = () => {
		episodeFormRef.value
			.validate()
			.then(async () => {
				submitLoading.value = true
				try {
					const submitData = { ...episodeForm }
					if (videoInputMode.value === 'manual') {
						submitData.videoId = null
					} else if (videoInputMode.value === 'select') {
						if (!submitData.videoId) {
							message.warning('请选择一个视频')
							submitLoading.value = false
							return
						}
					}
					const api = episodeForm.id ? shortplayApi.episodeEdit : shortplayApi.episodeAdd
					const res = await api(submitData)
					closeEpisodeForm()
					// 刷新表格
					episodeTableRef.value && episodeTableRef.value.refresh(true)
					emit('successful')
				} catch (error) {
					message.error('操作失败')
				} finally {
					submitLoading.value = false
				}
			})
			.catch(() => {
				const firstError = episodeFormRef.value.getFieldsError().filter((item) => item.errors.length)[0]
				if (firstError) {
					message.warning(firstError.errors[0])
				}
			})
	}

	// 视频输入模式改变
	const onVideoInputModeChange = (e) => {
		const mode = e.target.value
		if (mode === 'manual') {
			// 切换到手动输入模式，清除选择的视频ID和名称
			episodeForm.videoId = null
			episodeForm.videoName = ''
		} else if (mode === 'select') {
			// 切换到选择模式，清除手动输入的URL
			if (!episodeForm.videoId) {
				episodeForm.videoUrl = ''
			}
		}
	}

	// 搜索视频
	const searchVideos = (value) => {
		videoSearchKeyword.value = value
		showVideoSelector()
	}

	// 显示视频选择器
	const showVideoSelector = () => {
		videoSelectorVisible.value = true
		videoSelectorKeyword.value = videoSearchKeyword.value
		searchSelectorVideos(videoSelectorKeyword.value)
	}

	// 搜索选择器中的视频
	const searchSelectorVideos = async (title) => {
		videoLoading.value = true
		try {
			// 构建查询参数
			const params = {
				title: title,
				current: videoPage.value,
				size: videoPageSize.value
			}

			const res = await shortplayApi.getVideoList(params)
			if (res) {
				videoList.value = res.records || []
				videoTotal.value = res.total || 0

				// 如果已有选中的视频，预设选中状态
				if (episodeForm.videoId) {
					selectedVideoKeys.value = [episodeForm.videoId]
					selectedVideo.value = videoList.value.find((item) => item.id === episodeForm.videoId)
				}
			}
		} catch (error) {
			console.error('获取视频列表失败', error)
			message.error('获取视频列表失败')
		} finally {
			videoLoading.value = false
		}
	}

	// 分页变化
	const onVideoPageChange = (page) => {
		videoPage.value = page
		searchSelectorVideos(videoSelectorKeyword.value)
	}

	// 选择视频改变
	const onSelectedVideoChange = (selectedKeys, selectedRows) => {
		selectedVideoKeys.value = selectedKeys
		selectedVideo.value = selectedRows[0] || null
	}

	// 确认选择视频
	const confirmSelectVideo = () => {
		if (selectedVideo.value) {
			// 设置视频ID和视频URL
			episodeForm.videoId = selectedVideo.value.id
			episodeForm.videoUrl = selectedVideo.value.videoUrl
			episodeForm.videoName = selectedVideo.value.title

			// 同时更新时长
			if (selectedVideo.value.duration) {
				episodeForm.duration = selectedVideo.value.duration
			}

			// 如果没有封面，可以考虑使用视频封面
			if (!episodeForm.cover && selectedVideo.value.cover) {
				episodeForm.cover = selectedVideo.value.cover
			}

			message.success('已选择视频：' + selectedVideo.value.title)
		} else {
			message.warning('请选择一个视频')
			return
		}
		videoSelectorVisible.value = false
	}

	// 取消选择视频
	const cancelSelectVideo = () => {
		videoSelectorVisible.value = false
	}

	// 清除已选视频
	const clearSelectedVideo = () => {
		// 清除视频相关信息
		episodeForm.videoId = null
		episodeForm.videoUrl = ''
		episodeForm.videoName = ''
		selectedVideoKeys.value = []
		selectedVideo.value = null
	}

	// 格式化时长
	const formatDuration = (seconds) => {
		if (!seconds) return '00:00'
		const minutes = Math.floor(seconds / 60)
		const remainingSeconds = seconds % 60
		return `${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`
	}

	// 显示商品选择器
	const showGoodsSelector = () => {
		goodsSelectorVisible.value = true
		searchSelectorGoodsList(goodsSelectorKeyword.value)
	}

	// 搜索选择器中的商品
	const searchSelectorGoodsList = async (name) => {
		goodsLoading.value = true
		try {
			// 构建查询参数
			const params = {
				title: name,
				current: goodsPage.value,
				size: goodsPageSize.value
			}

			const res = await mallGoodsApi.mallGoodsSearch(params)
			if (res) {
				goodsList.value = res.records || []
				goodsTotal.value = res.total || 0

				// 如果已有选中的商品，预设选中状态
				if (episodeForm.goodsId) {
					selectedGoodsKeys.value = [episodeForm.goodsId]
					selectedGoods.value = goodsList.value.find((item) => item.id === episodeForm.goodsId)
				}
			}
		} catch (error) {
			console.error('获取商品列表失败', error)
			message.error('获取商品列表失败')
		} finally {
			goodsLoading.value = false
		}
	}

	// 分页变化
	const onGoodsPageChange = (page) => {
		goodsPage.value = page
		searchSelectorGoodsList(goodsSelectorKeyword.value)
	}

	// 选择商品改变
	const onSelectedGoodsChange = (selectedKeys, selectedRows) => {
		selectedGoodsKeys.value = selectedKeys
		selectedGoods.value = selectedRows[0] || null
	}

	// 确认选择商品
	const confirmSelectGoods = () => {
		if (selectedGoods.value) {
			// 设置商品ID和商品名称
			episodeForm.goodsId = selectedGoods.value.id
			episodeForm.goodsName = selectedGoods.value.title
			message.success('已选择商品：' + selectedGoods.value.title)
		} else {
			message.warning('请选择一个商品')
			return
		}
		goodsSelectorVisible.value = false
	}

	// 取消选择商品
	const cancelSelectGoods = () => {
		goodsSelectorVisible.value = false
	}

	// 清除已选商品
	const clearSelectedGoods = () => {
		// 清除商品相关信息
		episodeForm.goodsId = null
		episodeForm.goodsName = ''
		selectedGoodsKeys.value = []
		selectedGoods.value = null
	}

	// 打开批量添加剧集弹窗
	const openBatchAddModal = () => {
		batchAddVisible.value = true
		resetBatchAdd()
	}

	// 重置批量添加表单
	const resetBatchAdd = () => {
		batchEpisodes.value = []
		batchCommonFields.isFree = true
		batchCommonFields.startNumber = 1
		batchCommonFields.startSort = 1
		selectedBatchVideoKeys.value = []
		selectedBatchVideoRows.value = []
		batchVideoPage.value = 1
		batchVideoSearchKeyword.value = ''
	}

	// 打开批量视频选择器
	const openBatchVideoSelector = () => {
		batchVideoSelectorVisible.value = true
		searchBatchVideos()
	}

	// 搜索批量视频
	const searchBatchVideos = () => {
		batchVideoTableRef.value && batchVideoTableRef.value.refresh(true)
	}

	// 处理批量添加提交
	const handleBatchAddSubmit = async () => {
		if (!batchEpisodes.value.length) {
			message.warning('请先批量选择视频')
			return
		}
		batchAdding.value = true
		try {
			await shortplayApi.episodeBatchAdd(batchEpisodes.value)
			batchAddVisible.value = false
			resetBatchAdd()
			episodeTableRef.value && episodeTableRef.value.refresh(true)
		} catch (e) {
			console.error(e)
			message.error('批量添加失败')
		} finally {
			batchAdding.value = false
		}
	}

	// 暴露方法
	defineExpose({
		openManager
	})

	const batchVideoTableRef = ref()
	const loadBatchVideoData = (params) => {
		batchVideoLoading.value = true
		return shortplayApi
			.getVideoList({
				...params,
				title: batchVideoSearchKeyword.value
			})
			.then((res) => {
				batchVideoLoading.value = false
				return res
			})
			.catch((e) => {
				batchVideoLoading.value = false
				return { total: 0, records: [] }
			})
	}

	const onBatchVideoSelect = (selectedKeys, selectedRows) => {
		// 1. 合并所有已选key
		let allKeys = new Set([...selectedBatchVideoKeys.value, ...selectedKeys])
		// 2. 取消勾选时移除（当前页未勾选的key要从allKeys中移除）
		const currentPageRowIds = selectedRows.map(row => row.id)
		const pageData = (batchVideoTableRef.value?.getDataSource && batchVideoTableRef.value.getDataSource()) || []
		const pageIds = pageData.map(row => row.id)
		pageIds.forEach(id => {
			if (!currentPageRowIds.includes(id)) {
				allKeys.delete(id)
			}
		})
		selectedBatchVideoKeys.value = Array.from(allKeys)

		// 3. 合并所有已选行
		let mergedRows = selectedBatchVideoRows.value.filter(row => allKeys.has(row.id))
		selectedRows.forEach(row => {
			if (!mergedRows.find(r => r.id === row.id)) {
				mergedRows.push(row)
			}
		})
		selectedBatchVideoRows.value = mergedRows
	}

	const confirmBatchVideoSelect = () => {
		batchVideoSelectorVisible.value = false
		batchEpisodes.value = selectedBatchVideoRows.value.map((video, idx) => ({
			videoId: video.id,
			shortPlayId: shortplayId.value,
			title: video.title,
			videoUrl: video.videoUrl,
			cover: video.cover,
			duration: video.duration,
			episodeNumber: batchCommonFields.startNumber + idx,
			sort: batchCommonFields.startSort + idx,
			isFree: batchCommonFields.isFree
		}))
		// 关闭时清空多选
		selectedBatchVideoKeys.value = []
		selectedBatchVideoRows.value = []
	}

	// 弹窗关闭时也清空多选
	watch(batchVideoSelectorVisible, (val) => {
		if (!val) {
			selectedBatchVideoKeys.value = []
			selectedBatchVideoRows.value = []
		}
	})
</script>

<style lang="less" scoped>
	.episode-manager {
		min-height: 400px;

		.operation-bar {
			margin-bottom: 16px;
			text-align: right;
		}

		.episode-cover {
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

		.sort-tip {
			margin-top: 16px;
			display: flex;
			align-items: center;

			.sort-btn {
				margin-left: 16px;
			}
		}
	}

	.upload-tip {
		font-size: 12px;
		color: rgba(0, 0, 0, 0.45);
		margin-top: 8px;
	}

	.video-select-container {
		margin-top: 16px;
	}

	.video-preview {
		width: 100px;
		height: 56px;
		overflow: hidden;
		border-radius: 4px;
		margin-right: 16px;

		.preview-img {
			width: 100%;
			height: 100%;
			object-fit: cover;
		}
	}

	.no-preview {
		width: 100px;
		height: 56px;
		display: flex;
		align-items: center;
		justify-content: center;
		color: rgba(0, 0, 0, 0.45);
	}

	.video-selector {
		.search-bar {
			margin-bottom: 16px;
		}
	}

	.goods-selector {
		.search-bar {
			margin-bottom: 16px;
		}
	}

	.goods-cover {
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
</style>
