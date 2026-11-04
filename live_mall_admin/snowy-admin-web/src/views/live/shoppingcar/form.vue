<template>
	<xn-form-container
		:title="formData.id ? '编辑直播间购物车表' : '增加直播间购物车表'"
		:width="700"
		v-model:open="open"
		:destroy-on-close="true"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
<!--			<a-form-item label="购物车id：" name="shoppingId">-->
<!--				<a-input v-model:value="formData.shoppingId" placeholder="请输入购物车id" allow-clear />-->
<!--			</a-form-item>-->
			<a-form-item label="选择直播间：" name="liveId">
				<a-select
					v-model:value="formData.liveId"
					placeholder="请输入直播间标题检索"
					:options="searchLiveList"
					optionFilterProp="label"
					label-in-value
					@change="selectLives"
					@search="searchLives"
					:disabled="chooseLiveRoom != null"
					show-search
					allow-clear
				>
					<template #option="{ label, tag }">
						<span style="display: flex;justify-content: space-between;">
							<span style="color: red">{{ label }}</span>
							<a-tag color="green">{{ tag }}</a-tag>
						</span>
					</template>
				</a-select>
			</a-form-item>
			<a-form-item label="选择商品：" name="goodsId">
				<a-select
					v-model:value="formData.goodsId"
					placeholder="请输入商品标题或者商品简述检索"
					:options="searchGoodsList"
					optionFilterProp="label"
					label-in-value
					@change="selectGoods"
					@search="searchGoods"
					show-search
					allow-clear
				>
					<template #option="{ label, stock, unitPrice }">
						<span style="display: flex;justify-content: space-between;">
							<span style="color: red; width: 50%;">{{ label }}</span>
							<span style="">{{ "价格: " + (unitPrice) }}</span>
							<span style="">{{ "库存: " + (stock) }}</span>
						</span>
					</template>
				</a-select>
			</a-form-item>
			<a-form-item label="数量：" name="quantity">
				<a-input v-model:value="formData.quantity" placeholder="请输入数量" allow-clear />
			</a-form-item>
			<a-form-item label="价格：" name="price">
				<a-input v-model:value="formData.price" placeholder="请输入价格" allow-clear />
			</a-form-item>
			<a-form-item label="排序：" name="sort">
				<a-input-number v-model:value="formData.sort" :min="0" :max="9999" style="width: 100%" placeholder="请输入排序" />
			</a-form-item>
			<a-form-item label="小黄车标志：" name="yellowBikeState">
				<a-radio-group v-model:value="formData.yellowBikeState">
					<a-radio :value="0">是</a-radio>
					<a-radio :value="1">否</a-radio>
				</a-radio-group>
			</a-form-item>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="shoppingCarForm">
	import { cloneDeep, debounce } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import shoppingCarApi from '@/api/live/shoppingCarApi'
	import mallGoodsApi from "@/api/shop/mallGoodsApi";
	import liveBizApi from "@/api/live/liveBizApi";
	// 抽屉状态
	const open = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const searchGoodsList = ref([])
	const searchLiveList = ref([])

	const chooseLiveRoom = ref(null)

	// 打开抽屉
	const onOpen = (record, liveRoomRecord) => {


		// 生成直播列表的下拉列表
		const _genLiveList = (__data = []) => {
			for (let i = 0; i < __data.length; i++) {
				if (__data[i]) {
					searchLiveList.value = [
						{
							label: __data[i].liveName || __data[i].mainTitle,
							value: __data[i].liveId || __data[i].id,
							tag: '',
							// rdata: {
							// 	...__data[i],
							// 	rtype: 'live'
							// }
						}
					]
					break
				}
			}
		}

		open.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
			searchGoodsList.value = [
				{
					label: recordData.goodsName,
					value: recordData.goodsId,
					unitPrice: recordData.price,
					stock: recordData.stock || '',
					// rdata: {
					// 	...recordData,
					// 	rtype: 'car'
					// }
				}
			]
		}
		if (liveRoomRecord) {
			chooseLiveRoom.value = liveRoomRecord
			// 购物车新增商品反显直播间名称
			if (!record) {
				formData.value = Object.assign({}, {
					liveId: liveRoomRecord.id,
					liveName: liveRoomRecord.mainTitle,
				})
			}
		} else {
			chooseLiveRoom.value = null
		}
		_genLiveList([liveRoomRecord, record]);
	}
	// 关闭抽屉
	const onClose = () => {
		formRef.value.resetFields()
		formData.value = {}
		open.value = false
	}
	// 默认要校验的
	const formRules = {
		liveId: {
			required: true,
			message: "请选择直播间",
			trigger: "blur"
		},
		goodsId: [required("请选择商品")],
		quantity: {
			required: true,
			message: "请输入数字",
			trigger: "change",
			pattern: /^[0-9]*$/
		},
		price: {
			required: true,
			trigger: "change",
			message: '请输入数字，最多保留两位小数',
			pattern: /^[0-9]+(\.[0-9]{1,2})?$/
		},
		sort: {
			required: true,
			message: '请输入排序',
			trigger: 'change',
			type: 'number',
		},
		yellowBikeState: {
			required: true,
			message: '请选择小黄车标志',
			trigger: 'change',
		},
	}
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value
			.validate()
			.then(() => {
				submitLoading.value = true
				const formDataParam = cloneDeep(formData.value)
				shoppingCarApi
					.shoppingCarSubmitForm(formDataParam, formDataParam.id)
					.then(() => {
						onClose()
						emit('successful')
					})
					.finally(() => {
						submitLoading.value = false
					})
			})
			.catch(() => {})
	}

	const selectGoods = (o) => {
		formData.value.goodsId = o.value
		formData.value.goodsName = o.label
		console.log('选择的商品', o)
	}
	const selectLives = (o) => {
		formData.value.liveId = o.value
		formData.value.liveName = o.label
		console.log('选择的直播间', o)
	}

	const searchGoods = debounce( (query) => {
		if (!query) return
		mallGoodsApi.mallGoodsPage({
			pageNo: 1,
			pageSize: 10,
			searchKey: query,
			status: 2
		}).then(res => {
			searchGoodsList.value = res.records.map(item => {
				return {
					label: item.title,
					value: item.id,
					stock: item.stock,
					unitPrice: item.price,
					// rdata: {
					// 	...item,
					// 	rtype: 'goods'
					// },
				}
			})
		})
	}, 300)

	const searchLives = debounce( (query) => {
		if (!query) return
		liveBizApi.liveBizPage({
			pageNo: 1,
			pageSize: 10,
			searchKey: query
		}).then(res => {
			searchLiveList.value = res.records.map(item => {
				return {
					label: item.mainTitle,
					value: item.id,
					tag: item.liveStateName,
					// rdata: {
					// 	...item,
					// 	rtype: 'goods'
					// },
				}
			})
		})
	}, 300)

	// 抛出函数
	defineExpose({
		onOpen
	})
</script>
