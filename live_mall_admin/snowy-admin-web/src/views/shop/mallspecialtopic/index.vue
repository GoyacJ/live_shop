<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form mb-4">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item label="专题名称" name="name">
						<a-input v-model:value="searchFormState.name" placeholder="请输入专题名称" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-button type="primary" @click="tableRef.refresh(true)">查询</a-button>
					<a-button style="margin: 0 8px" @click="() => searchFormRef.resetFields()">重置</a-button>
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
					<a-button type="primary" @click="formRef.onOpen()">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						buttonName="批量删除"
						icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchSpecialTopic"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'carousel'">
					<a-tag :color="record.carousel === 0 ? 'green' : 'red'">{{ record.carousel === 0 ? '是' : '否' }}</a-tag>
				</template>
				<template v-if="column.dataIndex === 'background'">
					<a-avatar :src="record.background" shape="square" size="large" />
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space size="small">
						<a-button type="link" size="small" @click="formRef.onOpen(record)">编辑</a-button>
						<a-divider type="vertical" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteSpecialTopic(record)">
							<a-button type="link" danger size="small">删除</a-button>
						</a-popconfirm>
						<a-divider type="vertical" />
						<a-button type="link" size="small" @click="openBindDrawer(record)">绑定</a-button>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="tableRef.refresh(true)" />
	<a-drawer v-model:visible="bindDrawerVisible" title="绑定商品" width="800" @close="closeBindDrawer">
		<a-row :gutter="16" class="mb-4">
			<a-col :span="24">
				<a-alert :message="`当前绑定专题：${currentSpecialTopic?.name || ''}`" type="info" />
			</a-col>
		</a-row>

		<a-card title="添加商品" class="mb-4" :bordered="false">
			<a-form layout="vertical" @submit.prevent>
				<a-row :gutter="16">
					<a-col :span="12">
						<a-form-item label="商品名称">
							<a-select
								v-model:value="goodsSearchKey"
								show-search
								placeholder="请输入商品名称搜索"
								:options="goodsSearchOptions"
								:filter-option="false"
								@search="onGoodsSearch"
								@select="onGoodsSelect"
								@clear="onGoodsClear"
								style="width: 100%"
								:loading="searchLoading"
								not-found-content="未找到商品，请重新搜索"
								allow-clear
							>
								<template #option="{ label, value, option }">
									<div style="display: flex; align-items: center">
										<a-avatar :src="option.hostImage" shape="square" size="small" style="margin-right: 8px" />
										<span>{{ label }}</span>
									</div>
								</template>
							</a-select>
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item label="绑定数量">
							<a-input-number v-model:value="bindForm.goodsNum" :min="1" style="width: 100%" />
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item label="绑定价格">
							<a-input-number v-model:value="bindForm.goodsPrice" :min="0" style="width: 100%" />
						</a-form-item>
					</a-col>
				</a-row>
				<a-form-item>
					<a-button type="primary" @click="bindGoods" :disabled="!bindForm.goodsId">
						<template #icon><plus-outlined /></template>添加商品
					</a-button>
				</a-form-item>
			</a-form>
		</a-card>

		<a-card title="已绑定商品列表" :bordered="false">
			<template #extra>
				<a-button type="danger" :disabled="bindSelectedRowKeys.length === 0" @click="deleteBatchBindGoods">
					<template #icon><delete-outlined /></template>批量删除
				</a-button>
			</template>

			<a-table
				:columns="bindTableColumns"
				:data-source="bindGoodsList"
				row-key="id"
				:row-selection="{
					selectedRowKeys: bindSelectedRowKeys,
					onChange: onBindSelectionChange
				}"
				size="small"
				bordered
				:pagination="{ defaultPageSize: 5, showSizeChanger: true, pageSizeOptions: ['5', '10', '20'] }"
			>
				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'hostImage'">
						<a-avatar :src="record.hostImage" shape="square" size="small" />
					</template>
					<template v-if="column.dataIndex === 'action'">
						<a-popconfirm title="确定要删除该商品吗？" @confirm="() => deleteBindGoods(record)">
							<a-button type="link" danger size="small">删除</a-button>
						</a-popconfirm>
					</template>
				</template>
			</a-table>
		</a-card>
	</a-drawer>
</template>

<script setup name="mallSpecialTopic">
	import { ref, reactive } from 'vue'
	import { PlusOutlined, DeleteOutlined } from '@ant-design/icons-vue'
	import Form from './form.vue'
	import mallSpecialTopicApi from '@/api/shop/mallSpecialTopicApi.js'
	import mallGoodsApi from '@/api/shop/mallGoodsApi.js'
	const searchFormState = ref({})
	const searchFormRef = ref()
	const tableRef = ref()
	const formRef = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		{ title: '专题名称', dataIndex: 'name' },
		{ title: '商品数量', dataIndex: 'goodsNum' },
		{ title: '排序', dataIndex: 'sort', sorter: true },
		{ title: '是否轮播', dataIndex: 'carousel' },
		{ title: '专题图片', dataIndex: 'background' },
		{ title: '创建时间', dataIndex: 'createTime', align: 'center' },
		{ title: '操作', dataIndex: 'action', align: 'center', width: '220px', fixed: 'right' }
	]
	const selectedRowKeys = ref([])
	const options = {
		alert: {
			show: false,
			clear: () => {
				selectedRowKeys.value = []
			}
		},
		rowSelection: {
			onChange: (selectedRowKey, selectedRows) => {
				selectedRowKeys.value = selectedRowKey
			}
		}
	}
	const loadData = (parameter) => {
		const searchFormParam = { ...searchFormState.value }
		return mallSpecialTopicApi.mallSpecialTopicPage(Object.assign(parameter, searchFormParam)).then((data) => data)
	}
	const deleteSpecialTopic = (record) => {
		mallSpecialTopicApi.mallSpecialTopicDelete([{ id: record.id }]).then(() => {
			tableRef.value.refresh(true)
		})
	}
	const deleteBatchSpecialTopic = () => {
		if (selectedRowKeys.value.length < 1) return
		const params = selectedRowKeys.value.map((id) => ({ id }))
		mallSpecialTopicApi.mallSpecialTopicDelete(params).then(() => {
			tableRef.value.clearRefreshSelected()
		})
	}
	// 绑定商品相关
	const bindDrawerVisible = ref(false)
	const currentSpecialTopic = ref(null)
	const goodsSearchKey = ref('')
	const goodsSearchOptions = ref([])
	const searchLoading = ref(false)
	const bindForm = reactive({ goodsId: '', goodsNum: 1, goodsPrice: 0 })
	const bindGoodsList = ref([])
	const bindSelectedRowKeys = ref([])

	// 绑定商品表格多选回调函数
	const onBindSelectionChange = (selectedRowKeys) => {
		bindSelectedRowKeys.value = selectedRowKeys
	}

	// 绑定商品表格列
	const bindTableColumns = [
		{ title: '商品图片', dataIndex: 'hostImage', width: '80px', align: 'center' },
		{ title: '商品标题', dataIndex: 'title', ellipsis: true },
		{ title: '原价', dataIndex: 'price' },
		{ title: '绑定数量', dataIndex: 'goodsNum' },
		{ title: '绑定价格', dataIndex: 'goodsPrice' },
		{ title: '操作', dataIndex: 'action', align: 'center', width: '80px', fixed: 'right' }
	]
	function openBindDrawer(record) {
		currentSpecialTopic.value = record
		bindDrawerVisible.value = true
		fetchBindGoodsList()
		resetBindForm()
	}
	function closeBindDrawer() {
		bindDrawerVisible.value = false
		goodsSearchKey.value = ''
		goodsSearchOptions.value = []
		bindForm.goodsId = ''
		bindForm.goodsNum = 1
		bindForm.goodsPrice = 0
		bindGoodsList.value = []
		bindSelectedRowKeys.value = []
	}
	function onGoodsSearch(value) {
		if (!value || value.length < 1) {
			goodsSearchOptions.value = []
			return
		}

		searchLoading.value = true
		mallGoodsApi
			.mallGoodsSearch({ title: value })
			.then((data) => {
				const datas = data.records || []
				goodsSearchOptions.value = datas.map((item) => ({
					label: item.title,
					value: item.id,
					option: item
				}))
			})
			.catch((err) => {
				console.error('商品搜索出错:', err)
				goodsSearchOptions.value = []
			})
			.finally(() => {
				searchLoading.value = false
			})
	}
	function onGoodsSelect(value, option) {
		bindForm.goodsId = value
		bindForm.goodsNum = 1
		bindForm.goodsPrice = option.option.price
	}
	function onGoodsClear() {
		resetBindForm()
	}
	function bindGoods() {
		if (!bindForm.goodsId || !currentSpecialTopic.value) return
		mallSpecialTopicApi
			.mallSpecialTopicBinkForm({
				specialTopicId: currentSpecialTopic.value.id,
				goodsId: bindForm.goodsId,
				goodsNum: bindForm.goodsNum,
				goodsPrice: bindForm.goodsPrice
			})
			.then(() => {
				fetchBindGoodsList()
				resetBindForm()
				goodsSearchKey.value = ''
			})
	}
	function fetchBindGoodsList() {
		if (!currentSpecialTopic.value) return
		mallSpecialTopicApi.mallSpecialTopicDetailPage({ specialTopicId: currentSpecialTopic.value.id }).then((res) => {
			bindGoodsList.value = res.records || []
			// 清空已选择的数据，避免索引错误
			bindSelectedRowKeys.value = []
		})
	}
	function deleteBindGoods(record) {
		mallSpecialTopicApi.mallSpecialTopicDetailDelete([{ id: record.id }]).then(() => {
			fetchBindGoodsList()
		})
	}
	function deleteBatchBindGoods() {
		if (bindSelectedRowKeys.value.length < 1) return
		// 构造需要删除的数据参数，确保格式与单个删除一致
		const params = bindSelectedRowKeys.value.map((id) => ({ id }))
		mallSpecialTopicApi.mallSpecialTopicDetailDelete(params).then(() => {
			fetchBindGoodsList()
		})
	}
	function resetBindForm() {
		bindForm.goodsId = ''
		bindForm.goodsPrice = 0
	}
</script>

<style scoped>
	.mb-4 {
		margin-bottom: 16px;
	}
</style>
