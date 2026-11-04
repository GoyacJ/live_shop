<template>
	<a-drawer
		:title="formData.id ? '编辑商城-商品' : '增加商城-商品'"
		:width="900"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="商品标题：" name="title">
						<a-input v-model:value="formData.title" placeholder="请输入商品标题" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="主图：" name="hostImage">
						<xn-upload v-model:value="formData.hostImage" uploadMode="image" />
					</a-form-item>
				</a-col>

				<!--				<a-col>
					<a-col class="form-grid">
						<a-form-item v-for="(item, index) in items" :key="index" label="轮播图：" name="image">
							<xn-upload v-model:value="item.url" uploadMode="image" />
							<template v-if="index === items.length - 1" name="newImage">
								<xn-upload v-model:value="formData.newImage" uploadMode="image" />
							</template>
						</a-form-item>
					</a-col>
				</a-col>-->

				<a-col>
					<a-col class="form-grid">
						轮播图：
						<div class="inline-block-container">
							<a-form-item v-for="(item, index) in items" :key="index" name="image">
								<xn-upload v-model:value="item.value.url" uploadMode="image" @click="addNewUploadInput(item)" />
								<!--								<template v-if="index === items.length - 1 || items.length === 0" name="newImage" style="float: left">
									<xn-upload v-model:value="formData.newImage" uploadMode="image" />
								</template>-->
							</a-form-item>
						</div>
					</a-col>
				</a-col>

				<a-col :span="24">
					<a-form-item label="商品详情：" name="detail">
						<xn-editor v-model:value="formData.detail" placeholder="请输入商品详细内容" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="商品简述：" name="description">
						<a-textarea v-model:value="formData.description" placeholder="请输入商品简述" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="商品所属商家：" name="merchant">
						<a-textarea v-model:value="formData.merchant" placeholder="请输入商家名称" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="成本价：" name="costPrice">
						<a-input-number v-model:value="formData.costPrice" style="width: 100%" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="售价：" name="price">
						<a-input-number v-model:value="formData.price" style="width: 100%" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="排序：" name="sort">
						<a-input-number v-model:value="formData.sort" :min="1" :max="10000" style="width: 100%" />
					</a-form-item>
				</a-col>

				<a-col :span="12">
					<a-form-item label="赠送的积分：" name="giftPoints">
						<a-input-number
							v-model:value="formData.giftPoints"
							:min="1"
							placeholder="请输入赠送的积分"
							allow-clear
							style="width: 200px"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="推广奖励积分：" name="spreadGift">
						<a-input-number
							v-model:value="formData.spreadGift"
							:min="1"
							placeholder="请输入推广奖励积分"
							allow-clear
							style="width: 200px"
						/>
					</a-form-item>
				</a-col>

				<a-col :span="12">
					<a-form-item label="库存" name="stock">
						<a-input-number
							v-model:value="formData.stock"
							:min="1"
							placeholder="请输入库存"
							allow-clear
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="库存预警值" name="lowStock">
						<a-input-number
							v-model:value="formData.lowStock"
							:min="1"
							placeholder="请输入库存预警值"
							allow-clear
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="销量" name="sales">
						<a-input-number
							v-model:value="formData.sales"
							:min="1"
							placeholder="请输入销量"
							allow-clear
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="虚拟销量：" name="virtualSales">
						<a-input-number
							v-model:value="formData.virtualSales"
							:min="1"
							placeholder="请输入虚拟销量"
							allow-clear
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="状态：" name="status">
						<a-select v-model:value="formData.status" placeholder="请选择状态" :options="statusOptions" />
					</a-form-item>
				</a-col>
				<a-col :span="24">
					<a-form-item v-if="formData.status === '2'" label="下架原因：" name="delistReason">
						<xn-editor v-model:value="formData.delistReason" />
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</a-drawer>
</template>

<script setup name="mallGoodsForm">
	import tool from '@/utils/tool'
	import { cloneDeep, isEmpty } from 'lodash-es'
	import mallGoodsApi from '@/api/shop/mallGoodsApi'
	import mallGoodsChangeApi from '@/api/shop/mallGoodsChangeApi'
	// 抽屉状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const statusOptions = ref([])
	const mallGoodsChangeData = ref([])
	const olditems = ref([])
	const items = ref([])

	// 点击图片后添加新的图片上传输入框
	const addNewUploadInput = (item) => {
		if (item.value.url != null) {
			let data = ref({})
			items.value.push(data)
		}
	}

	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
			//处理商品轮播图信息
			if (formData.value.carouseImage != null) {
				olditems.value = formData.value.carouseImage.split(',')
				olditems.value.forEach((item) => {
					let data = ref({})
					data.value.url = item
					items.value.push(data)
				})
				items.value.forEach((item) => {
					item.value.url = item.value.url.replace('"', '')
				})
			}
			console.log('formData.value.carouseImage--->', JSON.stringify(formData.value.carouseImage))
			if (formData.value.carouseImage == null) {
				let data = ref({})
				items.value.push(data)
			}
			//查询商品变更信息
			const param = {
				goodsId: record.id
			}
			mallGoodsChangeApi.mallGoodsChangeDetail(param).then((data) => {
				if (data != null) {
					mallGoodsChangeData.value = data
					formData.value.delistReason = mallGoodsChangeData.value.delistReason
					formData.value.updateUser = mallGoodsChangeData.value.createUser
					formData.value.updateTime = mallGoodsChangeData.value.createTime
					return data
				}
			})
		} else {
			let data = ref({})
			items.value.push(data)
		}
		statusOptions.value = tool.dictList('GOODS_STATUS')
	}
	// 关闭抽屉
	const onClose = () => {
		formRef.value.resetFields()
		formData.value = {}
		visible.value = false
		items.value.splice(0, items.value.length)
	}
	// 默认要校验的
	const formRules = {}
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value.validate().then(() => {
			submitLoading.value = true
			const formDataParam = cloneDeep(formData.value)
			//商品状态为下架新增商品变更信息
			if (formData.value.status === '2') {
				formDataParam.goodsId = formData.value.id
				mallGoodsChangeApi.mallGoodsChangeAdd(formDataParam).then(() => {})
			}
			//处理主图信息
			if (isEmpty(formData.value.hostImage)) {
				formData.value.hostImage = ''
			}
			//处理轮播图信息
			const carouseImageData = ref({})
			items.value.forEach((item) => {
				let data = ref({})
				if (isEmpty(carouseImageData.value)) {
					if (!isEmpty(item.value.url)) {
						data.value = item.value.url + ','
						carouseImageData.value = data.value
					}
				} else {
					if (!isEmpty(item.value.url)) {
						carouseImageData.value = carouseImageData.value + item.value.url + ','
					}
				}
			})
			if (formData.value.newImage != null) {
				let data = ref({})
				data.value = carouseImageData.value + formData.value.newImage
				carouseImageData.value = data.value
			}
			formDataParam.carouseImage = carouseImageData.value
			mallGoodsApi
				.mallGoodsSubmitForm(formDataParam, formDataParam.id)
				.then(() => {
					onClose()
					emit('successful')
				})
				.finally(() => {
					submitLoading.value = false
				})
			//新增加商品图片信息(先删除再新增)
			/*			const deleteSlideparam = {
				goodsId: formDataParam.id
			}
			mallGoodsSildeApi.deleteEntityListByGoodsId(deleteSlideparam).then(() => {})
			if (items.value != null) {
				items.value.forEach((item) => {
					if (item.url != null) {
						const param = {
							goodsId: formDataParam.id,
							url: item.url,
							sort: item.sort
						}
						mallGoodsSildeApi.mallGoodsSlideSubmitForm(param, false).then(() => {})
					}
				})
			}
			if (formData.value.newImage != null) {
				const addparam = {
					goodsId: formDataParam.id,
					url: formData.value.newImage,
					sort: formData.value.imageSort
				}
				mallGoodsSildeApi.mallGoodsSlideSubmitForm(addparam, false).then(() => {})
			}*/
		})
	}
	// 抛出函数
	defineExpose({
		onOpen
	})
</script>
<style scoped lang="less">
	.form-grid {
		display: grid;
		/*grid-template-columns: auto 1fr; !* 第一列宽度自动，第二列占据剩余空间 *!*/
		align-items: center; /* 垂直居中对齐 */
		gap: 10px; /* 间距 */
		display: inline-flex;
	}
	.inline-block-container div {
		display: inline-block; /* 或者对具体组件使用 */
	}
</style>
