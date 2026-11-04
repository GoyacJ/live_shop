<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form mb-4">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item label="商品标题" name="title">
						<a-input v-model:value="searchFormState.title" placeholder="请输入商品标题" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item label="创建时间" name="createTime">
						<a-range-picker v-model:value="searchFormState.createTime" value-format="YYYY-MM-DD HH:mm:ss" show-time />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-button type="primary" @click="table.refresh(true)">查询</a-button>
					<a-button style="margin: 0 8px" @click="() => searchFormRef.resetFields()">重置</a-button>
				</a-col>
			</a-row>
		</a-form>
		<s-table
			ref="table"
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
					<!--					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('mallGoodsEvaluateAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>-->
					<xn-batch-button
						v-if="hasPerm('mallGoodsBatchDelete')"
						buttonName="批量删除"
						icon="DeleteOutlined"
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchMallGoodsEvaluate"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'status'">
					<a-switch :loading="loading" :checked="record.status === '1'" @change="editStatus(record)" />
				</template>
				<template v-if="column.dataIndex === 'evaluateType'">
					{{ $TOOL.dictTypeData('EVALUATE_TYPE', record.evaluateType) }}
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="replyFormRef.onOpen(record)" v-if="hasPerm('mallGoodsEvaluateReply')">回复</a>
<!--						<a @click="formRef.onOpen(record)" v-if="hasPerm('mallGoodsEvaluateEdit')">编辑</a>-->
						<a-divider
							type="vertical"
							v-if="hasPerm(['mallGoodsEvaluateReply', 'mallGoodsEvaluateEdit', 'mallGoodsEvaluateDelete'], 'and')"
						/>
						<a-popconfirm title="确定要删除吗？" @confirm="deleteMallGoodsEvaluate(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('mallGoodsEvaluateDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="table.refresh(true)" />
	<ReplyForm ref="replyFormRef" @successful="table.refresh(true)" />
</template>

<script setup name="mallgoods">
	import { message } from 'ant-design-vue'
	import Form from './form.vue'
	import ReplyForm from '@/views/shop/mallgoodsevaluate/replyform.vue'
	import mallGoodsApi from '@/api/shop/mallGoodsApi'
	import mallGoodsEvaluateApi from '@/api/shop/mallGoodsEvaluateApi'
	import userInfoApi from '@/api/manage/userInfoApi'
	let searchFormState = reactive({})
	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()
	const replyFormRef = ref()
	const goodsEvaluateData = ref({})
	const goodsListData = ref({})
	const userData = ref({})
	const loading = ref(false)
	const tableRef = ref(null)
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		{
			title: '商品标题',
			dataIndex: 'title'
		},
		{
			title: '商品所属商家',
			dataIndex: 'merchant'
		},

		{
			title: '评价信息',
			dataIndex: 'evaluateInformation'
		},
		{
			title: '评价人',
			dataIndex: 'createUser'
		},
		/*		{
			title: '电话',
			dataIndex: 'phone'
		},*/
		{
			title: '创建时间',
			dataIndex: 'createTime'
		},
		{
			title: '评价类型',
			dataIndex: 'evaluateType'
		},
		{
			title: '状态',
			dataIndex: 'status'
		}
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['mallGoodsEvaluateReply', 'mallGoodsEvaluateEdit', 'mallGoodsEvaluateDelete'])) {
		columns.push({
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: '150px'
		})
	}
	let selectedRowKeys = ref([])
	// 列表选择配置
	const options = {
		alert: {
			show: false,
			clear: () => {
				selectedRowKeys = ref([])
			}
		},
		rowSelection: {
			onChange: (selectedRowKey, selectedRows) => {
				selectedRowKeys.value = selectedRowKey
			}
		}
	}
	//修改状态
	const editStatus = (record) => {
		loading.value = true
		if (record.status === '1') {
			mallGoodsEvaluateApi
				.mallGoodsDisableStatus(record)
				.then(() => {
					table.value.refresh()
				})
				.finally(() => {
					loading.value = false
				})
		} else {
			mallGoodsEvaluateApi
				.mallGoodsEnableStatus(record)
				.then(() => {
					table.value.refresh()
				})
				.finally(() => {
					loading.value = false
				})
		}
	}

	const loadData = (parameter) => {
		const searchFormParam = JSON.parse(JSON.stringify(searchFormState))
		// createTime范围查询条件重载
		if (searchFormParam.createTime) {
			searchFormParam.startCreateTime = searchFormParam.createTime[0]
			searchFormParam.endCreateTime = searchFormParam.createTime[1]
			delete searchFormParam.createTime
		}
		//查询商品信息
		mallGoodsApi.mallGoodsList(Object.assign(parameter, searchFormParam)).then((goodsdata) => {
			goodsListData.value = goodsdata
		})
		//查询商品评价信息
		return mallGoodsEvaluateApi.mallGoodsEvaluePage(Object.assign(parameter, searchFormParam)).then((data) => {
			goodsEvaluateData.value = data
			goodsEvaluateData.value.records.forEach((item) => {
				//加入人员信息
				if (item.createUser === '0') {
					item.createUser = '系统人员'
				} else {
					const queryUserParam = {
						id: item.createUser
					}
					userInfoApi.userInfoDetail(queryUserParam).then((userdata) => {
						userData.value = userdata
						item.createUser = userData.value.nickname
						item.phone = userData.value.mobile
					})
				}
			})
			return goodsEvaluateData.value
		})
	}
	// 删除
	const deleteMallGoodsEvaluate = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		mallGoodsEvaluateApi.mallGoodsEvaluateDelete(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchMallGoodsEvaluate = () => {
		if (selectedRowKeys.value.length < 1) {
			message.warning('请选择一条或多条数据')
			return false
		}
		const params = selectedRowKeys.value.map((m) => {
			return {
				id: m
			}
		})
		mallGoodsEvaluateApi.mallGoodsEvalueDetail(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
</script>
