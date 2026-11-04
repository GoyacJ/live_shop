<template>
	<a-spin :spinning="loadSpinning">
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical" :label-col="labelCol">
			<a-row :gutter="16">
				<a-col :span="8">
					<a-form-item label="App分享海报：" name="APP_POSTER">
						<div class="poster-editor">
							<div class="poster-canvas-wrapper">
								<img v-if="posterBgUrl" :src="posterBgUrl" class="poster-bg" />
								<div
									class="qrcode-draggable"
									:style="qrcodeStyle"
									@mousedown="onDragStart"
									@touchstart="onTouchStart"
								>
									<canvas
										ref="qrcodeCanvas"
										:style="{ width: '100%', height: '100%', display: 'block' }"
									></canvas>
									<div class="resize-handle" @mousedown.stop="onResizeStart" @touchstart.stop="onResizeTouchStart"></div>
								</div>
							</div>
							<a-upload
								v-model:file-list="formData.APP_POSTER"
								class="avatar-uploader"
								list-type="picture-card"
								:show-upload-list="false"
								:custom-request="customPosterRequest"
								accept="image/png, image/jpeg, image/jpg"
							>
								<img v-if="posterBgUrl" :src="posterBgUrl" alt="poster" style="max-height: 100px; max-width: 100px" />
								<div v-else>
									<plus-outlined />
									<div class="ant-upload-text">上传背景</div>
								</div>
							</a-upload>
						</div>
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-form-item label="直播海报：" name="LIVE_POSTER">
						<a-upload
							v-model:file-list="formData.LIVE_POSTER"
							class="avatar-uploader"
							list-type="picture-card"
							:show-upload-list="false"
							:custom-request="customLivePosterRequest"
							accept="image/png, image/jpeg, image/jpg"
						>
							<img v-if="livePosterUrl" :src="livePosterUrl" alt="live-poster" style="max-height: 100px; max-width: 100px" />
							<div v-else>
								<plus-outlined />
								<div class="ant-upload-text">上传</div>
							</div>
						</a-upload>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item>
						<a-button type="primary" :loading="submitLoading" @click="onSubmit">保存</a-button>
						<a-button class="xn-ml10" @click="resetForm">重置</a-button>
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
	</a-spin>
</template>

<script setup lang="ts" name="liveConfig">
	import { ref, reactive, onMounted, nextTick } from 'vue'
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import { message } from 'ant-design-vue'
	import { globalStore } from '@/store'
	import configApi from '@/api/dev/configApi'
	import tool from '@/utils/tool'

	const emit = defineEmits({ successful: null })
	const formRef = ref()
	const formData = ref<any>({})
	const submitLoading = ref(false)
	const posterBgUrl = ref('')
	const livePosterUrl = ref('')
	const loadSpinning = ref(true)
	const store = globalStore()
	const qrcodeCanvas = ref<HTMLCanvasElement | null>(null)

	const posterWrapperWidth = 320
	const posterWrapperHeight = 480

	// 拖拽与缩放参数
	const qrcodePos = reactive({ x: 40, y: 40 })
	const qrcodeSize = reactive({ width: 80, height: 80 })
	const qrcodeParams = reactive({ x: 40, y: 40, width: 80, height: 80 })
	let dragging = false
	let resizing = false
	let dragOffset = { x: 0, y: 0 }
	let dragStart = { x: 0, y: 0 }
	let dragOrigin = { x: 0, y: 0 }
	let dragRAF: number | null = null

	const labelCol = ref({ style: { width: '150px' } })

	const formRules = {
		APP_POSTER: [required('请上传App分享海报')],
		LIVE_POSTER: [required('请上传直播海报')]
	}

	const param = { category: 'BIZ_DEFINE' }

	onMounted(() => {
		configApi.configList(param)
			.then((data) => {
				let ratioParams: any = null
				if (data && data.length > 0) {
					data.forEach((item: any) => {
						if (item.configKey === 'APP_POSTER') {
							formData.value[item.configKey] = [item.configValue]
							posterBgUrl.value = item.configValue
						} else if (item.configKey === 'LIVE_POSTER') {
							formData.value[item.configKey] = [item.configValue]
							livePosterUrl.value = item.configValue
						} else if (item.configKey === 'APP_POSTER_QRCODE_RATIO') {
							// 优先使用比例参数
							try {
								ratioParams = JSON.parse(item.configValue)
							} catch {}
						} else if (item.configKey === 'APP_POSTER_QRCODE') {
							// 兼容旧像素参数
							try {
								const params = JSON.parse(item.configValue)
								Object.assign(qrcodeParams, params)
								qrcodePos.x = params.x
								qrcodePos.y = params.y
								qrcodeSize.width = params.width
								qrcodeSize.height = params.height
							} catch {}
						} else {
							formData.value[item.configKey] = item.configValue
						}
					})
					// 如果有比例参数，优先用比例参数换算像素
					if (ratioParams) {
						qrcodePos.x = ratioParams.x * posterWrapperWidth
						qrcodePos.y = ratioParams.y * posterWrapperHeight
						qrcodeSize.width = ratioParams.width * posterWrapperWidth
						qrcodeSize.height = ratioParams.height * posterWrapperHeight
						Object.assign(qrcodeParams, {
							x: qrcodePos.x,
							y: qrcodePos.y,
							width: qrcodeSize.width,
							height: qrcodeSize.height
						})
					}
				} else {
					message.warning('表单项不存在，请初始化数据库')
				}
				loadSpinning.value = false
				nextTick(drawQrcode)
			})
			.catch(() => {
				message.error('获取配置失败，请重试')
				loadSpinning.value = false
			})
	})

	function drawQrcode() {
		if (!qrcodeCanvas.value) return
		// 让canvas实际像素与外框一致，避免缩放模糊和错位
		qrcodeCanvas.value.width = qrcodeSize.width
		qrcodeCanvas.value.height = qrcodeSize.height
		const ctx = qrcodeCanvas.value.getContext('2d')
		if (!ctx) return
		ctx.clearRect(0, 0, qrcodeSize.width, qrcodeSize.height)
		ctx.fillStyle = '#000'
		for (let i = 0; i < 8; i++) {
			for (let j = 0; j < 8; j++) {
				if ((i + j) % 2 === 0) {
					ctx.fillRect(i * qrcodeSize.width / 8, j * qrcodeSize.height / 8, qrcodeSize.width / 8, qrcodeSize.height / 8)
				}
			}
		}
	}

	function onDragStart(e: MouseEvent) {
		dragging = true
		dragStart.x = e.clientX
		dragStart.y = e.clientY
		dragOrigin.x = qrcodePos.x
		dragOrigin.y = qrcodePos.y
		document.body.style.userSelect = 'none'
		document.addEventListener('mousemove', onDragging)
		document.addEventListener('mouseup', onDragEnd)
	}
	function onDragging(e: MouseEvent) {
		if (!dragging) return
		if (dragRAF) cancelAnimationFrame(dragRAF)
		dragRAF = requestAnimationFrame(() => {
			const parent = (qrcodeCanvas.value as HTMLCanvasElement).parentElement as HTMLElement
			const rect = parent.parentElement!.getBoundingClientRect()
			let x = dragOrigin.x + (e.clientX - dragStart.x)
			let y = dragOrigin.y + (e.clientY - dragStart.y)
			x = Math.max(0, Math.min(x, rect.width - qrcodeSize.width))
			y = Math.max(0, Math.min(y, rect.height - qrcodeSize.height))
			qrcodePos.x = x
			qrcodePos.y = y
			qrcodeParams.x = x
			qrcodeParams.y = y
		})
	}
	function onDragEnd() {
		dragging = false
		document.body.style.userSelect = ''
		document.removeEventListener('mousemove', onDragging)
		document.removeEventListener('mouseup', onDragEnd)
		if (dragRAF) cancelAnimationFrame(dragRAF)
		dragRAF = null
	}
	function onResizeStart(e: MouseEvent) {
		resizing = true
		dragOffset.x = e.offsetX
		dragOffset.y = e.offsetY
		document.addEventListener('mousemove', onResizing)
		document.addEventListener('mouseup', onResizeEnd)
	}
	function onResizing(e: MouseEvent) {
		if (!resizing) return
		const parent = (qrcodeCanvas.value as HTMLCanvasElement).parentElement as HTMLElement
		const rect = parent.parentElement!.getBoundingClientRect()
		let width = e.clientX - rect.left - qrcodePos.x
		let height = e.clientY - rect.top - qrcodePos.y
		width = Math.max(40, Math.min(width, rect.width - qrcodePos.x))
		height = Math.max(40, Math.min(height, rect.height - qrcodePos.y))
		qrcodeSize.width = width
		qrcodeSize.height = height
		qrcodeParams.width = width
		qrcodeParams.height = height
		nextTick(drawQrcode)
	}
	function onResizeEnd() {
		resizing = false
		document.removeEventListener('mousemove', onResizing)
		document.removeEventListener('mouseup', onResizeEnd)
	}
	// 移动端支持
	function onTouchStart(e: TouchEvent) {
		if (e.touches.length === 1) {
			dragging = true
			const touch = e.touches[0]
			dragStart.x = touch.clientX
			dragStart.y = touch.clientY
			dragOrigin.x = qrcodePos.x
			dragOrigin.y = qrcodePos.y
			document.body.style.userSelect = 'none'
			document.addEventListener('touchmove', onTouchMove)
			document.addEventListener('touchend', onTouchEnd)
		}
	}
	function onTouchMove(e: TouchEvent) {
		if (!dragging) return
		if (dragRAF) cancelAnimationFrame(dragRAF)
		dragRAF = requestAnimationFrame(() => {
			const touch = e.touches[0]
			const parent = (qrcodeCanvas.value as HTMLCanvasElement).parentElement as HTMLElement
			const rect = parent.parentElement!.getBoundingClientRect()
			let x = dragOrigin.x + (touch.clientX - dragStart.x)
			let y = dragOrigin.y + (touch.clientY - dragStart.y)
			x = Math.max(0, Math.min(x, rect.width - qrcodeSize.width))
			y = Math.max(0, Math.min(y, rect.height - qrcodeSize.height))
			qrcodePos.x = x
			qrcodePos.y = y
			qrcodeParams.x = x
			qrcodeParams.y = y
		})
	}
	function onTouchEnd() {
		dragging = false
		document.body.style.userSelect = ''
		document.removeEventListener('touchmove', onTouchMove)
		document.removeEventListener('touchend', onTouchEnd)
		if (dragRAF) cancelAnimationFrame(dragRAF)
		dragRAF = null
	}
	function onResizeTouchStart(e: TouchEvent) {
		if (e.touches.length === 1) {
			resizing = true
			const touch = e.touches[0]
			dragOffset.x = touch.clientX
			dragOffset.y = touch.clientY
			document.addEventListener('touchmove', onResizeTouchMove)
			document.addEventListener('touchend', onResizeTouchEnd)
		}
	}
	function onResizeTouchMove(e: TouchEvent) {
		if (!resizing) return
		const touch = e.touches[0]
		const parent = (qrcodeCanvas.value as HTMLCanvasElement).parentElement as HTMLElement
		const rect = parent.parentElement!.getBoundingClientRect()
		let width = touch.clientX - rect.left - qrcodePos.x
		let height = touch.clientY - rect.top - qrcodePos.y
		width = Math.max(40, Math.min(width, rect.width - qrcodePos.x))
		height = Math.max(40, Math.min(height, rect.height - qrcodePos.y))
		qrcodeSize.width = width
		qrcodeSize.height = height
		qrcodeParams.width = width
		qrcodeParams.height = height
		nextTick(drawQrcode)
	}
	function onResizeTouchEnd() {
		resizing = false
		document.removeEventListener('touchmove', onResizeTouchMove)
		document.removeEventListener('touchend', onResizeTouchEnd)
	}

	const qrcodeStyle = computed(() => ({
		position: 'absolute',
		left: qrcodePos.x + 'px',
		top: qrcodePos.y + 'px',
		width: qrcodeSize.width + 'px',
		height: qrcodeSize.height + 'px',
		cursor: dragging ? 'move' : 'pointer',
		border: '1px dashed #1890ff',
		background: '#fff',
		zIndex: 2,
		boxShadow: dragging ? '0 4px 16px rgba(24,144,255,0.18)' : 'none'
	}))

	function customPosterRequest(data: any) {
		formData.value.APP_POSTER = ref([])
		getBase64(data.file)
			.then((res: string) => {
				posterBgUrl.value = res
				formData.value.APP_POSTER.push(res)
			})
			.catch(() => {})
	}
	function customLivePosterRequest(data: any) {
		formData.value.LIVE_POSTER = ref([])
		getBase64(data.file)
			.then((res: string) => {
				livePosterUrl.value = res
				formData.value.LIVE_POSTER.push(res)
			})
			.catch(() => {})
	}
	function getBase64(file: File) {
		return new Promise<string>((resolve, reject) => {
			const reader = new FileReader()
			reader.readAsDataURL(file)
			reader.onload = () => resolve(reader.result as string)
			reader.onerror = (error) => reject(error)
		})
	}

	function getQrcodeRatioParams() {
		return {
			x: qrcodePos.x / posterWrapperWidth,
			y: qrcodePos.y / posterWrapperHeight,
			width: qrcodeSize.width / posterWrapperWidth,
			height: qrcodeSize.height / posterWrapperHeight
		}
	}

	function onSubmit() {
		formRef.value
			.validate()
			.then(() => {
				submitLoading.value = true
				const submitParam = cloneDeep(formData.value)
				submitParam.APP_POSTER = submitParam.APP_POSTER[0]
				submitParam.LIVE_POSTER = submitParam.LIVE_POSTER[0]
				submitParam.APP_POSTER_QRCODE = JSON.stringify(qrcodeParams)
				submitParam.APP_POSTER_QRCODE_RATIO = JSON.stringify(getQrcodeRatioParams())
				const param = Object.entries(submitParam).map((item) => ({
					configKey: item[0],
					configValue: item[1]
				}))
				configApi
					.configEditForm(param)
					.then(() => {
						tool.data.set('SNOWY_SYS_BASE_CONFIG', submitParam)
						store.setSysBaseConfig(submitParam)
						emit('successful')
					})
					.finally(() => {
						submitLoading.value = false
					})
			})
			.catch(() => {
				message.error('表单验证失败，请检查输入')
			})
	}
	function resetForm() {
		posterBgUrl.value = ''
		livePosterUrl.value = ''
		qrcodePos.x = 40
		qrcodePos.y = 40
		qrcodeSize.width = 80
		qrcodeSize.height = 80
		Object.assign(qrcodeParams, { x: 40, y: 40, width: 80, height: 80 })
		nextTick(drawQrcode)
	}
</script>

<style scoped>
.poster-editor {
	display: flex;
	flex-direction: column;
	align-items: flex-start;
}
.poster-canvas-wrapper {
	position: relative;
	width: 320px;
	height: 480px;
	background: #f5f5f5;
	border: 1px solid #eee;
	margin-bottom: 12px;
}
.poster-bg {
	width: 100%;
	height: 100%;
	object-fit: contain;
	position: absolute;
	left: 0;
	top: 0;
	z-index: 1;
}
.qrcode-draggable {
	position: absolute;
	z-index: 2;
	box-sizing: border-box;
	background: #fff;
	border: 1px dashed #1890ff;
	display: flex;
	align-items: flex-end;
	justify-content: flex-end;
}
.resize-handle {
	width: 16px;
	height: 16px;
	background: #1890ff;
	border-radius: 50%;
	position: absolute;
	right: -8px;
	bottom: -8px;
	cursor: se-resize;
	z-index: 3;
	border: 2px solid #fff;
	box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}
</style>
