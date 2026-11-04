<template>
	<a-drawer
		:title="'查看商城-商品详情'"
		:width="900"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@close="onClose"
	>
		<a-form ref="goodsDetailsformRef" :model="goodsDetailsformData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="商品标题111：" name="title">
						<a-input v-model:value="goodsDetailsformData.title" placeholder="请输入商品标题" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="24">
					<a-form-item label="商品详情：" name="detail">
						<xn-editor v-model:value="goodsDetailsformData.detail" placeholder="请输入商品详细内容" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="成本价：" name="costPrice">
						<a-input-number v-model:value="goodsDetailsformData.costPrice" :min="1" :max="10000" style="width: 100%" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="售价：" name="price">
						<a-input-number v-model:value="goodsDetailsformData.price" :min="1" :max="10000" style="width: 100%" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="排序：" name="sort">
						<a-input-number v-model:value="goodsDetailsformData.sort" :min="1" :max="10000" style="width: 100%" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="商品所属商家：" name="merchant">
						<a-textarea v-model:value="goodsDetailsformData.merchant" placeholder="请输入商家名称" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="赠送的积分：" name="giftPoints">
						<a-input-number
							v-model:value="goodsDetailsformData.giftPoints"
							placeholder="请输入赠送的积分"
							allow-clear
							style="width: 200px"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="推广奖励积分：" name="spreadGift">
						<a-input-number
							v-model:value="goodsDetailsformData.spreadGift"
							placeholder="请输入推广奖励积分"
							allow-clear
							style="width: 200px"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="商品简述：" name="description">
						<a-textarea v-model:value="goodsDetailsformData.description" placeholder="请输入商品简述" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="库存" name="stock">
						<a-input-number
							v-model:value="goodsDetailsformData.stock"
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
							v-model:value="goodsDetailsformData.lowStock"
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
							v-model:value="goodsDetailsformData.sales"
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
							v-model:value="goodsDetailsformData.virtualSales"
							:min="1"
							placeholder="请输入虚拟销量"
							allow-clear
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="状态：" name="status">
						<a-select v-model:value="goodsDetailsformData.status" placeholder="请选择状态" :options="statusOptions" />
					</a-form-item>
				</a-col>

				<a-col :span="12">
					<a-form-item label="更新人" name="updateUser">
						<a-input-number
							v-model:value="goodsDetailsformData.updateUser"
							placeholder=""
							allow-clear
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="更新时间" name="sales">
						<a-date-picker
							v-model:value="goodsDetailsformData.updateTime"
							value-format="YYYY-MM-DD HH:mm:ss"
							show-time
							placeholder="更新时间"
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="24">
					<a-form-item label="下架原因：" name="delistReason">
						<xn-editor v-model:value="goodsDetailsformData.delistReason" />
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
		</template>
	</a-drawer>
</template>

<script setup name="mallGoodsForm">
	import tool from '@/utils/tool'
	import { cloneDeep } from 'lodash-es'
	import mallGoodsChangeApi from '@/api/shop/mallGoodsChangeApi'
	// 抽屉状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const goodsDetailsformRef = ref()
	// 表单数据
	const goodsDetailsformData = ref({})
	const merchantOptions = ref([])
	const statusOptions = ref([])
	const mallGoodsChangeData = ref([])

	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			let recordData = cloneDeep(record)
			goodsDetailsformData.value = Object.assign({}, recordData)
		}
		statusOptions.value = tool.dictList('GOODS_STATUS')
		const param = {
			goodsId: record.id
		}
		mallGoodsChangeApi.mallGoodsChangeDetail(param).then((data) => {
			if (data !== null) {
				mallGoodsChangeData.value = data
				goodsDetailsformData.value.delistReason = mallGoodsChangeData.value.delistReason
				goodsDetailsformData.value.updateUser = mallGoodsChangeData.value.createUser
				goodsDetailsformData.value.updateTime = mallGoodsChangeData.value.createTime
				return data
			}
		})
		return goodsDetailsformData
	}
	// 关闭抽屉
	const onClose = () => {
		goodsDetailsformRef.value.resetFields()
		goodsDetailsformData.value = {}
		visible.value = false
	}
	// 默认要校验的
	const formRules = {
	}
	// 抛出函数
	defineExpose({
		onOpen
	})
</script>
