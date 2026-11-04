<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form mb-4">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item label="直播间名称" name="liveName">
						<a-input v-model:value="searchFormState.liveName" placeholder="请输入直播间名称" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item label="订单编号" name="orderNumber">
						<a-input v-model:value="searchFormState.orderNumber" placeholder="请输入订单编号" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item label="创建时间" name="createTime">
						<a-range-picker v-model:value="searchFormState.createTime" value-format="YYYY-MM-DD HH:mm:ss" show-time />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item name="status" label="订单状态">
						<a-select
							v-model:value="searchFormState.status"
							placeholder="请选择订单状态"
							:options="statusOptions"
							:getPopupContainer="(trigger) => trigger.parentNode"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="4">
					<a-form-item label="手机号" name="mobile">
						<a-input v-model:value="searchFormState.mobile" placeholder="请输入手机号" />
					</a-form-item>
				</a-col>
				<a-col :span="20">
					<a-space>
						<a-button type="primary" @click="table.refresh(true)">查询</a-button>
						<a-button style="margin: 0 8px" @click="() => searchFormRef.resetFields()">重置</a-button>
					</a-space>
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
				<a-space size="middle">
					<a-upload :before-upload="beforeUpload" :show-upload-list="false" accept=".xls,.xlsx">
						<a-button type="primary">
							<template #icon><upload-outlined /></template>
							导入物流信息
						</a-button>
					</a-upload>
					<a-button type="default" @click="downloadTemplate">
						<template #icon><file-excel-outlined /></template>
						下载导入模板
					</a-button>
					<a-button type="primary" style="background-color: #52c41a; border-color: #52c41a" @click="exportOrderData">
						<template #icon><export-outlined /></template>
						导出所选订单信息
					</a-button>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'status'">
					{{ $TOOL.dictTypeData('ORDER_STATUS_TYPE', record.status) }}
				</template>
				<!--				<template v-if="column.dataIndex === 'logisticsStatus'">-->
				<!--					{{ $TOOL.dictTypeData('LOGISTICS_TYPE', record.logisticsStatus) }}-->
				<!--				</template>-->
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a-button type="primary" size="small" @click="showModal(record)">设置物流编号</a-button>
						<a-button type="link" @click="detailFormRef.onOpen(record)">详情</a-button>
					</a-space>
				</template>
			</template>
		</s-table>

		<a-collapse ghost style="margin-top: 16px">
			<a-collapse-panel key="1" header="操作说明">
				<div class="operation-guide">
					<h4>导入与导出说明：</h4>
					<ul>
						<li><b>导入物流信息</b>：上传Excel表格批量导入物流信息，支持.xls和.xlsx格式，文件大小不超过10MB</li>
						<li><b>下载导入模板</b>：下载标准导入模板，请按照模板格式填写物流信息</li>
						<li><b>导出所选订单信息</b>：将表格中已选中的订单数据导出为Excel文件，使用前请先选择需要导出的订单</li>
					</ul>
				</div>
			</a-collapse-panel>
		</a-collapse>
	</a-card>
	<a-modal v-model:open="open" title="设置物流信息" :width="500" :maskClosable="false" :destroyOnClose="true">
		<a-form
			ref="logisticsFormRef"
			:model="logisticsForm"
			:rules="logisticsRules"
			:label-col="{ span: 6 }"
			:wrapper-col="{ span: 18 }"
		>
			<a-form-item label="订单编号" name="orderCode">
				<a-input v-model:value="logisticsForm.orderCode" disabled />
			</a-form-item>
			<a-form-item label="快递公司" name="shipper">
				<a-select
					v-model:value="logisticsForm.shipper"
					placeholder="请选择快递公司"
					:options="shipperOptions"
					:getPopupContainer="(trigger) => trigger.parentNode"
					allow-clear
				/>
			</a-form-item>
			<a-form-item label="物流编号" name="logisticCode">
				<a-input
					v-model:value="logisticsForm.logisticCode"
					placeholder="请输入物流编号"
					allow-clear
					@blur="detectShipperByCode"
					@input="detectShipperByCode"
				/>
			</a-form-item>
			<a-form-item label="收货人" name="customerInfo">
				<a-input v-model:value="logisticsForm.customerInfo" disabled />
			</a-form-item>
		</a-form>
		<template #footer>
			<a-space>
				<a-button @click="handleCancel">取消</a-button>
				<a-button type="primary" :loading="loading" @click="onSubmit">确定</a-button>
			</a-space>
		</template>
	</a-modal>
	<Form ref="formRef" @successful="table.refresh(true)" />
	<Detail ref="detailFormRef" @successful="table.refresh(true)" />
</template>

<script setup name="liveOrder">
	import { message } from 'ant-design-vue'
	import Form from './form.vue'
	import Detail from '../../shop/mallorder/detail.vue'
	import mallOrderApi from '@/api/shop/mallOrderApi'
	import mallOrderLogisticsApi from '@/api/shop/mallOrderLogisticsApi'
	import {
		UploadOutlined,
		DownloadOutlined,
		InfoCircleOutlined,
		FileExcelOutlined,
		ExportOutlined
	} from '@ant-design/icons-vue'
	import { h } from 'vue'
	let searchFormState = reactive({})
	const searchFormRef = ref()
	const detailFormRef = ref()
	const table = ref()
	const formRef = ref()
	const orderData = ref({})

	import { ref } from 'vue'
	import { cloneDeep } from 'lodash-es'
	import tool from '@/utils/tool'
	import downloadUtil from '@/utils/downloadUtil'
	const loading = ref(false)
	const open = ref(false)
	const logisticsFormRef = ref()

	// 物流编辑表单
	const logisticsForm = ref({
		id: '',
		orderCode: '',
		shipper: '',
		logisticCode: '',
		customerInfo: ''
	})

	// 表单校验规则
	const logisticsRules = {
		shipper: [{ required: true, message: '请选择快递公司', trigger: 'change' }],
		logisticCode: [{ required: true, message: '请输入物流编号', trigger: 'blur' }]
	}

	// 快递公司选项
	const shipperOptions = [
		{ label: '中通快递', value: 'ZTO' },
		{ label: '申通快递', value: 'STO' },
		{ label: '圆通速递', value: 'YTO' },
		{ label: '韵达速递', value: 'YD' },
		{ label: '邮政快递包裹', value: 'YZPY' },
		{ label: '顺丰速运', value: 'SF' },
		{ label: '京东物流', value: 'JD' },
		{ label: 'EMS', value: 'EMS' }
	]

	const showModal = (record) => {
		// 深拷贝记录，避免直接修改原数据
		orderData.value = cloneDeep(record)
		// 设置表单数据
		logisticsForm.value = {
			id: record.id,
			orderCode: record.orderNumber,
			shipper: record.shipper || '',
			logisticCode: record.logisticCode || '',
			customerInfo: `${record.mobile.substr(-4)}`
		}
		// 打开弹窗
		open.value = true
	}

	const handleCancel = () => {
		// 关闭弹窗前重置表单
		if (logisticsFormRef.value) {
			logisticsFormRef.value.resetFields()
		}
		open.value = false
	}

	const onSubmit = () => {
		logisticsFormRef.value
			.validate()
			.then(() => {
				loading.value = true
				// 构建提交数据
				let recordData = {
					shipper: logisticsForm.value.shipper,
					logisticCode: logisticsForm.value.logisticCode,
					customerName: logisticsForm.value.customerInfo,
					orderCode: logisticsForm.value.orderCode
				}

				mallOrderLogisticsApi
					.setting(recordData)
					.then(() => {
						// 成功后关闭弹窗并刷新表格
						handleCancel()
						message.success('物流编号设置成功')
						table.value.refresh(true)
					})
					.finally(() => {
						loading.value = false
					})
			})
			.catch((error) => {
				console.log('表单验证失败', error)
			})
	}

	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const columns = [
		{
			title: '直播间',
			dataIndex: 'orderSourceName'
		},
		{
			title: '订单编号',
			dataIndex: 'orderNumber'
		},
		{
			title: '订单价格',
			dataIndex: 'orderFee'
		},
		{
			title: '物流编号',
			dataIndex: 'logisticsNumber'
		},
		// {
		// 	title: '物流状态',
		// 	dataIndex: 'logisticsStatus'
		// },
		{
			title: '下单手机号',
			dataIndex: 'mobile'
		},
		{
			title: '收货人昵称',
			dataIndex: 'nickname'
		},
		{
			title: '收货地址',
			dataIndex: 'address'
		},
		{
			title: '订单状态',
			dataIndex: 'status'
		},
		{
			title: '创建时间',
			dataIndex: 'createTime'
		}
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['mallLiveGoodsEdit', 'mallLiveGoodsDelete'])) {
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
	const loadData = (parameter) => {
		const searchFormParam = JSON.parse(JSON.stringify(searchFormState))
		// createTime范围查询条件重载
		if (searchFormParam.createTime) {
			searchFormParam.startCreateTime = searchFormParam.createTime[0]
			searchFormParam.endCreateTime = searchFormParam.createTime[1]
			delete searchFormParam.createTime
		}
		//查询订单表信息
		return mallOrderApi.liveOrderPage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 删除
	const deleteOrder = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		mallOrderApi.mallOrderDelete(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchOrder = () => {
		if (selectedRowKeys.value.length < 1) {
			message.warning('请选择一条或多条数据')
			return false
		}
		const params = selectedRowKeys.value.map((m) => {
			return {
				id: m
			}
		})
		mallOrderApi.mallOrderDelete(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}

	const statusOptions = tool.dictList('ORDER_STATUS_TYPE')

	const detectShipperByCode = () => {
		const logisticCode = logisticsForm.value.logisticCode
		if (!logisticCode || logisticCode.length < 2) {
			return
		}

		// 提取前两位字符（大写处理便于匹配）
		const prefix = logisticCode.slice(0, 2).toUpperCase()

		// 快递公司前缀映射表
		const shipperPrefixMap = {
			SF: 'SF', // 顺丰
			ZT: 'ZTO', // 中通
			ST: 'STO', // 申通
			YT: 'YTO', // 圆通
			YD: 'YD', // 韵达
			YZ: 'YZPY', // 邮政
			JD: 'JD', // 京东
			EM: 'EMS' // EMS
		}

		// 特殊规则：顺丰单号可能只有S开头
		if (logisticCode.toUpperCase().startsWith('S') && !logisticCode.toUpperCase().startsWith('ST')) {
			logisticsForm.value.shipper = 'SF'
		}
		// 匹配前缀
		else if (shipperPrefixMap[prefix]) {
			logisticsForm.value.shipper = shipperPrefixMap[prefix]
		}
		// 处理其他特殊情况，如纯数字可能是中通、申通等
		else if (/^\d+$/.test(logisticCode)) {
			// 根据数字长度判断可能的快递公司
			const length = logisticCode.length
			if (length === 12) {
				// 可能是申通或中通，默认选择中通
				logisticsForm.value.shipper = 'ZTO'
			} else if (length === 13) {
				// 可能是韵达
				logisticsForm.value.shipper = 'YD'
			}
		}
	}

	// 上传文件前的处理
	const beforeUpload = (file) => {
		const isExcel =
			file.type === 'application/vnd.ms-excel' ||
			file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'

		if (!isExcel) {
			message.error('请上传Excel文件!')
			return false
		}

		// 文件大小限制（10MB）
		const isLt10M = file.size / 1024 / 1024 < 10
		if (!isLt10M) {
			message.error('文件必须小于10MB!')
			return false
		}

		// 显示加载中
		loading.value = true
		message.loading('文件上传中...', 0)

		// 调用导入API
		mallOrderLogisticsApi
			.importLogistics(file)
			.then((res) => {
				if (res && res.code === 200) {
					// 导入成功后显示详细信息
					const successCount = res.data?.successCount || 0
					const failCount = res.data?.failCount || 0

					if (successCount > 0 && failCount === 0) {
						message.success(`导入成功！共导入${successCount}条物流信息`)
					} else if (successCount > 0 && failCount > 0) {
						message.warning(`成功导入${successCount}条物流信息，${failCount}条导入失败`)
					} else if (successCount === 0 && failCount > 0) {
						message.error(`导入失败！${failCount}条记录无法导入`)
					} else {
						message.warning('导入完成，但未有数据变更')
					}

					// 刷新表格数据
					table.value.refresh(true)
				} else {
					message.error('物流信息导入失败：' + (res?.message || '未知错误'))
				}
			})
			.catch((err) => {
				console.error('导入失败:', err)
				message.error('导入失败: ' + (err.message || '未知错误'))
			})
			.finally(() => {
				loading.value = false
				message.destroy() // 关闭所有消息
			})

		return false // 阻止自动上传
	}

	// 导出订单数据
	const exportOrderData = () => {
		// 检查是否有选中的订单
		if (selectedRowKeys.value.length < 1) {
			message.warning('请至少选择一条订单记录进行导出')
			return
		}

		// 显示加载中
		loading.value = true
		message.loading('正在导出数据...', 0)

		if (selectedRowKeys.value.length > 0) {
			const params = {
				orderIds: selectedRowKeys.value
					.map((m) => {
						return m
					})
					.join()
			}
			exportBatchOrder(params)
		}
		loading.value = false
		message.destroy() // 关闭所有消息
	}

	// 批量导出
	const exportBatchOrder = (params) => {
		mallOrderApi.exportOrders(params).then((res) => {
			downloadUtil.resultDownload(res)
			table.value.clearSelected()
		})
	}
	// 下载导入模板
	const downloadTemplate = () => {
		// 使用public目录下的模板文件
		const link = document.createElement('a')
		link.href = '/templates/导入发货模板.xlsx'
		link.download = '导入发货模板.xlsx'
		document.body.appendChild(link)
		link.click()
		document.body.removeChild(link)
	}
</script>

<style lang="less" scoped>
	.operation-guide {
		padding: 0 8px;

		h4 {
			color: #1890ff;
			margin-bottom: 12px;
		}

		ul {
			padding-left: 20px;

			li {
				margin-bottom: 8px;
				line-height: 1.5;

				b {
					color: #262626;
				}
			}
		}
	}

	.table-operator {
		margin-bottom: 16px;

		.ant-btn {
			margin-right: 8px;
		}
	}

	.ant-advanced-search-form {
		.ant-form-item {
			margin-bottom: 16px;
		}

		.ant-form-item-label > label {
			color: rgba(0, 0, 0, 0.85);
		}
	}

	.ant-collapse-ghost > .ant-collapse-item > .ant-collapse-header {
		padding: 8px 16px;
	}

	a-card {
		margin-bottom: 24px;
	}
</style>
