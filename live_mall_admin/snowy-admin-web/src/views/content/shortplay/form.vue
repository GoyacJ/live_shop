<template>
	<xn-form-container
		:title="formData.id ? '编辑短剧' : '新增短剧'"
		:width="720"
		:visible="visible"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-tabs v-model:activeKey="activeKey">
				<!-- 基本信息 -->
				<a-tab-pane key="basic" tab="基本信息">
					<!-- 短剧类型选择 -->
					<a-form-item label="短剧类型：" name="playType" v-if="false">
						<a-radio-group v-model:value="formData.playType" @change="onPlayTypeChange">
							<a-radio :value="1">单集短剧</a-radio>
							<a-radio :value="2">多集短剧</a-radio>
						</a-radio-group>
						<div class="type-description">
							{{
								formData.playType === 1
									? '单集短剧类似抖音短视频，只包含一个视频'
									: '多集短剧包含多个视频，可在创建后管理各集信息'
							}}
						</div>
					</a-form-item>

					<a-row :gutter="16">
						<a-col :span="16">
							<a-form-item label="短剧名称：" name="title">
								<a-input v-model:value="formData.title" placeholder="请输入短剧名称" allow-clear />
							</a-form-item>

							<a-form-item label="短剧分类：" name="categoryId">
								<a-select
									v-model:value="formData.category"
									placeholder="请选择短剧分类"
									style="width: 100%"
									allow-clear
								>
									<a-select-option v-for="item in categoryList" :key="item.value" :value="item.value">
										{{ item.label }}
									</a-select-option>
								</a-select>
							</a-form-item>

							<a-form-item label="短剧标签：" name="tags">
								<a-select
									v-model:value="formData.tags"
									mode="multiple"
									placeholder="请选择标签"
									style="width: 100%"
									allow-clear
									:options="tagList"
								>
								</a-select>
							</a-form-item>

							<a-form-item label="短剧简介：" name="description">
								<a-textarea v-model:value="formData.description" placeholder="请输入短剧简介" :rows="4" allow-clear />
							</a-form-item>
						</a-col>
						<a-col :span="8">
							<a-form-item label="封面图片：" name="cover">
								<xn-upload
									v-model:value="formData.cover"
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
						</a-col>
					</a-row>

					<a-row :gutter="16">
						<a-col :span="8">
							<a-form-item label="排序：" name="sortCode">
								<a-input-number v-model:value="formData.sort" :min="0" :max="9999" style="width: 100%" />
							</a-form-item>
						</a-col>
						<a-col :span="8">
							<a-form-item label="状态：" name="status">
								<a-switch v-model:checked="formData.status" :checked-value="1" :unchecked-value="0" />
								<span class="ml-2">{{ formData.status === 1 ? '发布' : '未发布' }}</span>
							</a-form-item>
						</a-col>
					</a-row>

					<!-- 集数管理提示 -->
					<a-alert class="multi-episode-tip" type="info" show-icon>
						<template #message>
							<div>
								<div>短剧创建后，需要在集数管理中添加各集视频</div>
								<div v-if="formData.id">点击<a @click="goToEpisodeManager">管理集数</a>添加或编辑视频</div>
								<div v-else>保存短剧基本信息后，可以进入集数管理添加视频</div>
							</div>
						</template>
					</a-alert>
				</a-tab-pane>

				<!-- 其他设置 -->
				<a-tab-pane key="advanced" tab="高级设置">
					<a-row :gutter="16">
						<a-col :span="12">
							<a-form-item label="推荐指数：" name="recommendLevel">
								<a-rate v-model:value="formData.recommendLevel" :count="5" />
							</a-form-item>
						</a-col>
						<a-col :span="12">
							<a-form-item label="上线时间：" name="publishTime">
								<a-date-picker
									v-model:value="formData.publishTime"
									style="width: 100%"
									show-time
									format="YYYY-MM-DD HH:mm:ss"
									placeholder="请选择上线时间"
								/>
							</a-form-item>
						</a-col>
					</a-row>
					<a-row :gutter="16">
						<a-col :span="12">
							<a-form-item label="允许评论：" name="allowComment">
								<a-switch v-model:checked="formData.allowComment" />
								<span class="ml-2">{{ formData.allowComment ? '允许' : '禁止' }}</span>
							</a-form-item>
						</a-col>
						<a-col :span="12">
							<a-form-item label="允许分享：" name="allowShare">
								<a-switch v-model:checked="formData.allowShare" />
								<span class="ml-2">{{ formData.allowShare ? '允许' : '禁止' }}</span>
							</a-form-item>
						</a-col>
					</a-row>

					<a-form-item label="短剧海报：" name="poster">
						<xn-upload
							v-model:value="formData.poster"
							:upload-mode="'image'"
							:upload-number="1"
							:upload-text="'上传海报'"
							accept="image/*"
						>
							<template #explain>
								<div class="upload-tip">建议尺寸：3:4，大小不超过2MB</div>
							</template>
						</xn-upload>
					</a-form-item>
				</a-tab-pane>
			</a-tabs>
		</a-form>
		<template #footer>
			<a-space>
				<a-button @click="onClose">取消</a-button>
				<a-button type="primary" :loading="submitLoading" @click="onSubmit">保存</a-button>
			</a-space>
		</template>
	</xn-form-container>
</template>

<script setup name="shortplayForm">
	import { message, Modal } from 'ant-design-vue'
	import shortplayApi from '@/api/content/shortplayApi'
	import tool from '@/utils/tool'
	import dayjs from 'dayjs'
	import { SearchOutlined, VideoCameraOutlined } from '@ant-design/icons-vue'

	// 定义事件
	const emit = defineEmits(['successful', 'manageEpisodes'])

	// 表单可见性
	const visible = ref(false)
	const activeKey = ref('basic')
	const formRef = ref(null)
	const submitLoading = ref(false)

	// 分类和标签列表从字典获取
	const categoryList = computed(() => tool.dictList('SHORTPLAY_TYPE'))
	const tagList = computed(() => tool.dictList('SHORTPLAY_LABEL'))

	// 表单数据
	const formData = reactive({
		id: null,
		title: '',
		category: undefined,
		tags: [],
		cover: '',
		poster: '',
		description: '',
		sort: 99,
		status: 0, // 0-未发布 1-已发布
		recommendLevel: 3,
		publishTime: null,
		allowComment: true,
		allowShare: true,
		viewCount: 0,
		likeCount: 0,
		commentCount: 0,
		shareCount: 0,
		followCount: 0,
		playType: 2 // 默认设为多集模式
	})

	// 表单校验规则
	const formRules = {
		title: [{ required: true, message: '请输入短剧名称', trigger: 'blur' }],
		category: [{ required: true, message: '请选择短剧分类', trigger: 'change' }],
		cover: [{ required: true, message: '请上传短剧封面', trigger: 'change' }]
	}

	// 打开表单
	const openForm = async (id) => {
		resetForm()
		visible.value = true

		if (id) {
			await loadDetail(id)
		}
	}

	// 重置表单
	const resetForm = () => {
		formData.id = null
		formData.title = ''
		formData.categoryId = undefined
		formData.tags = []
		formData.cover = ''
		formData.poster = ''
		formData.description = ''
		formData.sortCode = 99
		formData.status = 0
		formData.recommendLevel = 3
		formData.publishTime = null
		formData.allowComment = true
		formData.allowShare = true
		formData.viewCount = 0
		formData.likeCount = 0
		formData.commentCount = 0
		formData.shareCount = 0
		formData.followCount = 0
		formData.playType = 2

		if (formRef.value) {
			formRef.value.resetFields()
		}
	}

	// 加载短剧详情
	const loadDetail = async (id) => {
		try {
			const res = await shortplayApi.shortplayDetail(id)
			if (res) {
				const data = res
				Object.keys(formData).forEach((key) => {
					if (key in data) {
						if (key === 'tags' && data[key]) {
							// 处理标签，后端可能是字符串，需要转为数组
							formData[key] = typeof data[key] === 'string' ? (data[key] ? data[key].split(',') : []) : data[key]
						} else if (key === 'publishTime' && data[key]) {
							// 转换日期格式
							formData[key] = dayjs(data[key])
						} else {
							formData[key] = data[key]
						}
					}
				})
			}
		} catch (error) {
			message.error('获取短剧详情失败')
		}
	}

	// 关闭表单
	const onClose = () => {
		visible.value = false
		resetForm()
	}

	// 短剧类型改变
	const onPlayTypeChange = (e) => {
		const playType = e.target.value
		if (playType === 1) {
			// 切换到单集模式
			// 无需清除视频信息，可能用户只是临时切换查看
		} else if (playType === 2) {
			// 切换到多集模式
			// 多集短剧将在创建后管理视频，暂不清除现有信息
		}
	}

	// 前往集数管理
	const goToEpisodeManager = () => {
		if (formData.id) {
			emit('manageEpisodes', formData.id, formData.title)
			onClose()
		}
	}

	// 提交表单
	const onSubmit = () => {
		formRef.value
			.validate()
			.then(async () => {
				submitLoading.value = true
				try {
					// 准备提交数据
					const submitData = { ...formData }

					// 处理日期
					if (submitData.publishTime) {
						submitData.publishTime = submitData.publishTime.format('YYYY-MM-DD HH:mm:ss')
					}

					// 处理标签格式，转为字符串
					if (Array.isArray(submitData.tags)) {
						submitData.tags = submitData.tags.join(',')
					}

					// 调用API保存数据
					const api = formData.id ? shortplayApi.shortplayEdit : shortplayApi.shortplayAdd
					await api(submitData)

					// 提示创建成功，去管理剧集
					if (!formData.id) {
						message.info('短剧创建成功，请在列表页面点击"管理集数"添加剧集')
					}

					emit('successful')
					onClose()
				} catch (error) {
					message.error('操作失败')
				} finally {
					submitLoading.value = false
				}
			})
			.catch(() => {
				// 表单验证失败
				const firstError = formRef.value.getFieldsError().filter((item) => item.errors.length)[0]
				if (firstError) {
					message.warning(firstError.errors[0])
				}
			})
	}

	// 暴露方法
	defineExpose({
		openForm
	})
</script>

<style lang="less" scoped>
	.upload-tip {
		font-size: 12px;
		color: rgba(0, 0, 0, 0.45);
		margin-top: 8px;
	}

	.video-select-container {
		margin-top: 8px;
	}

	.selected-video {
		margin-top: 8px;
	}

	.video-selector {
		.search-bar {
			margin-bottom: 16px;
		}

		.video-preview {
			width: 100px;
			height: 56px;
			overflow: hidden;
			border-radius: 4px;
			background-color: #f0f0f0;
			display: flex;
			align-items: center;
			justify-content: center;

			.preview-img {
				width: 100%;
				height: 100%;
				object-fit: cover;
			}

			.no-preview {
				font-size: 12px;
				color: #999;
			}
		}
	}

	.mt-2 {
		margin-top: 8px;
	}

	.ml-2 {
		margin-left: 8px;
	}

	.type-description {
		font-size: 12px;
		color: rgba(0, 0, 0, 0.45);
		margin-top: 4px;
	}

	.multi-episode-tip {
		margin: 16px 0;
	}
</style>
