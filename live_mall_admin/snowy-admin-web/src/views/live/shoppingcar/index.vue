<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<!--				<a-col :span="6">-->
				<!--					<a-form-item label="购物车id" name="shoppingId">-->
				<!--						<a-input v-model:value="searchFormState.shoppingId" placeholder="请输入购物车id" />-->
				<!--					</a-form-item>-->
				<!--				</a-col>-->
				<a-col :span="6" v-if="!liveRoomRecord">
					<a-form-item label="直播间id" name="liveId">
						<a-input v-model:value="searchFormState.liveId" placeholder="请输入直播间id" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item label="商品名称" name="goodsName">
						<a-input v-model:value="searchFormState.goodsName" placeholder="请输入商品名称" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-button type="primary" @click="tableRef.refresh()">查询</a-button>
					<a-button style="margin: 0 8px" @click="reset">重置</a-button>
				</a-col>
			</a-row>
		</a-form>
		<s-table
			ref="tableRef"
			:columns="columns"
			:data="loadData"
			:alert="options.alert.show"
			bordered
			:row-key="(record) => record.id"
			:tool-config="toolConfig"
			:row-selection="options.rowSelection"
		>
			<template #operator class="table-operator">
				<a-space>
					<a-button type="primary" @click="formRef.onOpen(null, liveRoomRecord)" v-if="hasPerm('shoppingCarAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						v-if="hasPerm('shoppingCarBatchDelete')"
						buttonName="批量删除"
						icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchShoppingCar"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'yellowBikeState'">
					<img
						v-if="record.yellowBikeState === 0"
						:src="yellowBikeIcon"
						alt="小黄车"
						style="width: 20px; height: 20px"
					/>
					<span v-else></span>
				</template>
				<template v-else-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record, liveRoomRecord)" v-if="hasPerm('shoppingCarEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['shoppingCarEdit', 'shoppingCarDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteShoppingCar(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('shoppingCarDelete')">删除</a-button>
						</a-popconfirm>
						<a-button type="link" @click="shoppingCarTop(record)" v-if="hasPerm('shoppingCarTop')">置顶</a-button>
						<a-button type="link" @click="showYellowBikeModal(record)" v-if="hasPerm('shoppingCarTips')">小黄车</a-button>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="tableRef.refresh()" />
	<a-modal v-model:visible="yellowBikeModalVisible" title="设置小黄车上架时间" @ok="handleYellowBikeOk" @cancel="handleYellowBikeCancel">
		<div style="margin-bottom: 12px;">请输入上架时间（分钟）：</div>
		<a-input-number v-model:value="yellowBikeMinute" :min="1" :max="1440" style="width: 100%;" />
	</a-modal>
</template>

<script setup name="shoppingcar">
	import { cloneDeep } from 'lodash-es'
	import Form from './form.vue'
	import shoppingCarApi from '@/api/live/shoppingCarApi'
	import yellowBikeIcon from '@/assets/icons/live/yellow-bike.png'
	import { ref } from 'vue'
	import { Modal, InputNumber, message } from 'ant-design-vue'
	const searchFormState = ref({})
	const searchFormRef = ref()
	const tableRef = ref()
	const formRef = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		// {
		// 	title: '购物车id',
		// 	dataIndex: 'shoppingId'
		// },
		{
			title: '直播间',
			dataIndex: 'liveName'
		},
		{
			title: '商品名称',
			dataIndex: 'goodsName'
		},
		{
			title: '数量',
			dataIndex: 'quantity'
		},
		{
			title: '价格',
			dataIndex: 'price'
		},
		{
			title: '排序',
			dataIndex: 'sort',
			align: 'center'
		},
		{
			title: '小黄车',
			dataIndex: 'yellowBikeState',
			align: 'center'
		},
		{
			title: '小黄车上架时间(min)',
			dataIndex: 'yellowBikeSecond',
			align: 'center',
			customRender: ({ text }) => text ? Math.floor(text / 60) + ' 分钟' : ''
		}
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['shoppingCarEdit', 'shoppingCarDelete'])) {
		columns.push({
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: 220,
			render: (text, record) => {
				return h('a-space', {}, [
					hasPerm('shoppingCarEdit') && h('a', { onClick: () => formRef.value.onOpen(record, liveRoomRecord) }, '编辑'),
					hasPerm(['shoppingCarEdit', 'shoppingCarDelete'], 'and') && h('a-divider', { type: 'vertical' }),
					hasPerm('shoppingCarDelete') &&
						h(
							'a-popconfirm',
							{
								title: '确定要删除吗？',
								onConfirm: () => deleteShoppingCar(record)
							},
							h('a-button', { type: 'link', danger: true, size: 'small' }, '删除')
						),
					hasPerm('shoppingCarTop') && h('a-button', { type: 'link', onClick: () => shoppingCarTop(record) }, '置顶'),
					hasPerm('shoppingCarTips') &&
						h('a-button', { type: 'link', onClick: () => showYellowBikeModal(record) }, '小黄车')
				])
			}
		})
	}
	// 从直播间进来需要按照直播间id查询
	const props = defineProps({
		liveRoomRecord: {
			type: Object,
			required: false
		}
	})

	const selectedRowKeys = ref([])
	// 列表选择配置
	const options = {
		// columns数字类型字段加入 needTotal: true 可以勾选自动算账
		alert: {
			show: true,
			clear: () => {
				selectedRowKeys.value = ref([])
			}
		},
		rowSelection: {
			onChange: (selectedRowKey, selectedRows) => {
				selectedRowKeys.value = selectedRowKey
			}
		}
	}
	const loadData = (parameter) => {
		const searchFormParam = cloneDeep(searchFormState.value)
		if (props.liveRoomRecord) {
			searchFormParam.liveId = props.liveRoomRecord.id
		}
		return shoppingCarApi.shoppingCarPage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}
	// 删除
	const deleteShoppingCar = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		shoppingCarApi.shoppingCarDelete(params).then(() => {
			tableRef.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchShoppingCar = (params) => {
		shoppingCarApi.shoppingCarDelete(params).then(() => {
			tableRef.value.clearRefreshSelected()
		})
	}
	// 设置小黄车标志
	const setYellowBikeState = (record) => {
		shoppingCarApi.setYellowBikeState({ id: record.id }).then(() => {
			tableRef.value.refresh()
		})
	}
	// 置顶
	const shoppingCarTop = (record) => {
		shoppingCarApi.shoppingCarTop({ id: record.id }).then(() => {
			tableRef.value.refresh()
		})
	}
	// 小黄车
	const yellowBikeMinute = ref(1)
	const yellowBikeModalVisible = ref(false)
	let yellowBikeRecord = null

	const showYellowBikeModal = (record) => {
		yellowBikeRecord = record
		yellowBikeMinute.value = record.yellowBikeSecond ? Math.floor(record.yellowBikeSecond / 60) : 1
		yellowBikeModalVisible.value = true
	}
	const handleYellowBikeOk = () => {
		if (!yellowBikeRecord) return
		const seconds = yellowBikeMinute.value * 60
		shoppingCarApi.shoppingCarTips({ id: yellowBikeRecord.id, yellowBikeSecond: seconds }).then(() => {
			yellowBikeModalVisible.value = false
			tableRef.value.refresh()
			message.success('设置成功')
		})
	}
	const handleYellowBikeCancel = () => {
		yellowBikeModalVisible.value = false
	}
</script>
