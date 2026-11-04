<template>
	<a-drawer
		:title="formData.id ? '编辑视频' : '新增视频'"
		:width="900"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" :label-col="{ span: 4 }" :wrapper-col="{ span: 19 }">
			<a-form-item label="视频标题" name="title">
				<a-input v-model:value="formData.title" placeholder="请输入视频标题" allow-clear />
			</a-form-item>

			<a-form-item label="分类" name="category">
				<a-select
					v-model:value="formData.category"
					placeholder="请选择分类"
					:options="categoryOptions"
					allow-clear
				></a-select>
			</a-form-item>

			<a-form-item label="标签" name="tags">
				<a-select
					v-model:value="formData.tags"
					placeholder="请选择标签"
					:options="tagsOptions"
					mode="multiple"
					allow-clear
				></a-select>
			</a-form-item>

			<a-form-item label="视频封面" name="cover">
				<xn-upload
					v-model:value="formData.cover"
					uploadMode="image"
					:accept="'image/*'"
					@onChange="handleCoverChange"
				/>
			</a-form-item>

			<a-form-item label="描述" name="description">
				<a-textarea
					v-model:value="formData.description"
					placeholder="请输入视频描述"
					:auto-size="{ minRows: 3, maxRows: 5 }"
				></a-textarea>
			</a-form-item>

			<a-form-item label="视频文件" name="videoUrl">
				<div class="video-upload-container">
					<xn-upload v-model:value="formData.videoUrl" uploadMode="file" :accept="'video/*'" />
					<div v-if="formData.videoUrl && formData.id" style="margin-top: 10px">
						<video :src="formData.videoUrl" controls style="max-width: 100%; max-height: 200px"></video>
					</div>
				</div>
			</a-form-item>

			<a-form-item label="是否可下载" name="downloadable">
				<a-radio-group v-model:value="formData.downloadable">
					<a-radio :value="0">是</a-radio>
					<a-radio :value="1">否</a-radio>
				</a-radio-group>
			</a-form-item>

			<a-form-item label="是否可分享" name="shareable">
				<a-radio-group v-model:value="formData.shareable">
					<a-radio :value="0">是</a-radio>
					<a-radio :value="1">否</a-radio>
				</a-radio-group>
			</a-form-item>

			<a-form-item label="是否可评论" name="allowComment">
				<a-radio-group v-model:value="formData.allowComment">
					<a-radio :value="0">是</a-radio>
					<a-radio :value="1">否</a-radio>
				</a-radio-group>
			</a-form-item>

			<a-form-item label="视频来源" name="source">
				<a-input v-model:value="formData.source" placeholder="请输入视频来源" allow-clear />
			</a-form-item>

			<a-form-item label="排序" name="sort">
				<a-input-number v-model:value="formData.sort" placeholder="请输入排序" style="width: 100%" :min="0" />
			</a-form-item>

			<!-- 剧集表单新增商品相关字段 -->
			<a-form-item label="选择商品" name="goodsId">
				<a-row gutter="8">
					<a-col :span="18">
						<a-input
							v-model:value="formData.goodsName"
							placeholder="请选择商品"
							readonly
							allow-clear
							@click="showGoodsSelector"
						/>
					</a-col>
					<a-col :span="6">
						<a-button @click="showGoodsSelector">选择商品</a-button>
						<a-button v-if="formData.goodsId" @click="clearSelectedGoods" style="margin-left: 8px">清除</a-button>
					</a-col>
				</a-row>
			</a-form-item>
			<a-form-item label="商品开始秒数" name="startShowGoodsSecond">
				<a-input-number v-model:value="formData.startShowGoodsSecond" :min="0" />
			</a-form-item>
			<a-form-item label="商品展示秒数" name="showGoodsSecond">
				<a-input-number v-model:value="formData.showGoodsSecond" :min="0" />
			</a-form-item>
		</a-form>

		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">取消</a-button>
			<a-button type="primary" :loading="submitLoading" @click="onSubmit">确定</a-button>
		</template>
	</a-drawer>

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
</template>

<script setup name="videoForm">
	import { ref, reactive, watch, computed } from 'vue'
	import { message } from 'ant-design-vue'
	import videoApi from '@/api/content/videoApi'
	import tool from '@/utils/tool'
	import mallGoodsApi from '@/api/shop/mallGoodsApi.js'

	// 定义组件属性
	const props = defineProps({
		// 弹窗是否可见
		visible: {
			type: Boolean,
			default: false
		},
		// 表单数据
		data: {
			type: Object,
			default: () => ({})
		}
	})

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

	// 定义组件事件
	const emit = defineEmits(['close', 'success'])

	// 表单引用
	const formRef = ref()

	// 提交状态
	const submitLoading = ref(false)

	// 字典项
	const categoryOptions = ref([])
	const tagsOptions = ref([])

	// 获取字典数据
	const loadDictData = async () => {
		try {
			// 获取视频分类字典
			categoryOptions.value = tool.dictTypeList('VIDEO_TYPE').map((item) => ({
				label: item.dictLabel,
				value: item.dictValue
			}))
			// 获取视频标签字典
			tagsOptions.value = tool.dictTypeList('VIDEO_LABEL').map((item) => ({
				label: item.dictLabel,
				value: item.dictValue
			}))
		} catch (error) {
			console.error('获取字典数据失败', error)
		}
	}

	// 表单数据
	const formData = reactive({
		id: '',
		title: '',
		category: '',
		tags: [],
		cover: '',
		description: '',
		videoUrl: '',
		duration: 0,
		playCount: 0,
		likeCount: 0,
		followCount: 0,
		shareCount: 0,
		commentCount: 0,
		status: 0,
		source: '',
		autoGenerateCover: 0,
		downloadable: 0,
		shareable: 0,
		allowComment: 0,
		sort: 0,
		goodsId: null,
		goodsName: '',
		startShowGoodsSecond: 0,
		showGoodsSecond: 0
	})

	// 表单校验规则
	const formRules = {
		title: [{ required: true, message: '请输入视频标题', trigger: 'blur' }],
		category: [{ required: true, message: '请选择视频分类', trigger: 'change' }],
		videoUrl: [{ required: true, message: '请上传视频文件', trigger: 'change' }]
	}

	// 监听传入的数据变化
	watch(
		() => props.data,
		(val) => {
			if (val && val.id) {
				Object.assign(formData, val)

				// 处理标签，后端可能是字符串，需要转为数组
				if (typeof formData.tags === 'string') {
					// 如果标签是空字符串，设置为空数组
					formData.tags = formData.tags ? formData.tags.split(',') : []
				}

				// 商品名称回显
				// 新增：如果有goodsId但没有goodsName，自动查一次商品详情
				if (formData.goodsId && !formData.goodsName) {
					mallGoodsApi.mallGoodsDetail({ id: formData.goodsId }).then(res => {
						if (res && res.title) formData.goodsName = res.title
					})
				}
			} else {
				// 新增时重置表单
				resetForm()
			}
		},
		{ deep: true, immediate: true }
	)

	// 监听弹窗显示状态
	watch(
		() => props.visible,
		(val) => {
			if (val) {
				// 弹窗显示时加载字典数据
				loadDictData()
			}
		}
	)

	// 重置表单
	const resetForm = () => {
		Object.assign(formData, {
			id: '',
			title: '',
			category: '',
			tags: [],
			cover: '',
			description: '',
			videoUrl: '',
			duration: 0,
			playCount: 0,
			likeCount: 0,
			followCount: 0,
			shareCount: 0,
			commentCount: 0,
			status: 0,
			source: '',
			autoGenerateCover: 0,
			downloadable: 0,
			shareable: 0,
			allowComment: 0,
			sort: 0,
			goodsId: null,
			goodsName: '',
			startShowGoodsSecond: 0,
			showGoodsSecond: 0
		})
		formRef.value && formRef.value.resetFields()
	}

	// 处理封面上传变化
	const handleCoverChange = (value) => {
		// XnUpload组件已经处理了上传逻辑，这里只需处理自动生成封面逻辑
		if (formData.autoGenerateCover === 0 && value && !formData.cover) {
			formData.cover = value
		}
	}

	// 提交表单
	const onSubmit = async () => {
		try {
			await formRef.value.validate()

			submitLoading.value = true

			// 处理标签格式，转为字符串
			const submitData = { ...formData }
			if (Array.isArray(submitData.tags)) {
				submitData.tags = submitData.tags.join(',')
			}

			// 根据ID判断是新增还是编辑
			if (submitData.id) {
				await videoApi.videoEdit(submitData)
			} else {
				await videoApi.videoAdd(submitData)
			}

			emit('success')
			onClose()
		} catch (error) {
			console.error('表单提交失败', error)
		} finally {
			submitLoading.value = false
		}
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
				if (formData.goodsId) {
					selectedGoodsKeys.value = [formData.goodsId]
					selectedGoods.value = goodsList.value.find((item) => item.id === formData.goodsId)
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
			formData.goodsId = selectedGoods.value.id
			formData.goodsName = selectedGoods.value.title
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
		formData.goodsId = null
		formData.goodsName = ''
		selectedGoodsKeys.value = []
		selectedGoods.value = null
	}
	// 关闭弹窗
	const onClose = () => {
		resetForm()
		emit('close')
	}
</script>

<style lang="less" scoped>
	.upload-hint {
		color: #666;
		font-size: 12px;
		margin-top: 8px;
	}

	.video-upload-container {
		:deep(.ant-upload-list-item-progress) {
			height: 20px;
			padding: 2px 0;
		}

		:deep(.ant-upload-list-item) {
			margin-top: 8px;
			margin-bottom: 8px;
			padding: 4px 0;
		}

		:deep(.ant-upload-list) {
			overflow: visible;
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
