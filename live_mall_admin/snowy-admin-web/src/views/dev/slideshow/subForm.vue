<template>
	<a-form :model="formData" ref="formRef" name="basic" autocomplete="off">
		<a-table :columns="columns" :dataSource="formData" size="middle">
			<template #bodyCell="{ text, record, index, column }">
				<template v-if="column.dataIndex === 'whetherToClick'">
					<a-form-item :validate-status="validateStatus(record, 'whetherToClick')">
						<a-radio-group
							v-model:value="record.whetherToClick"
							placeholder="请选择跳转方式"
							:options="whetherToClickOptions"
						/>
					</a-form-item>
				</template>
				<template v-if="column.dataIndex === 'skipMode'">
					<a-form-item :validate-status="validateStatus(record, 'skipMode')">
						<a-select
							v-model:value="record.skipMode"
							placeholder="请选择跳转方式"
							:disabled="record.whetherToClick === 'DISABLE'"
							:options="skipModeOptions"
						/>
					</a-form-item>
				</template>
				<template v-if="column.dataIndex === 'url'">
					<a-form-item :validate-status="validateStatus(record, 'url')">
						<template v-if="['SHOP', 'LIVE', 'TOPIC'].includes(record.skipMode)">
							<XnPageSelect
								v-if="record.skipMode === 'SHOP'"
								v-model:value="formData[index].url"
								:page-function="shopPageFunction"
								:echo-function="shopEchoFunction"
								placeholder="请选择商品"
								show-search
								searchKeyName="title"
								@change="(id, obj) => onSelectChange('shopObj', index, obj)"
							/>
							<XnPageSelect
								v-else-if="record.skipMode === 'LIVE'"
								v-model:value="formData[index].url"
								:page-function="livePageFunction"
								:echo-function="liveEchoFunction"
								placeholder="请选择直播"
								show-search
								searchKeyName="searchKey"
								@change="(id, obj) => onSelectChange('liveObj', index, obj)"
							/>
							<XnPageSelect
								v-else-if="record.skipMode === 'TOPIC'"
								v-model:value="formData[index].url"
								:page-function="topicPageFunction"
								:echo-function="topicEchoFunction"
								placeholder="请选择专题"
								show-search
								searchKeyName="name"
								@change="(id, obj) => onSelectChange('topicObj', index, obj)"
							/>
							<div v-if="record.shopObj && record.skipMode === 'SHOP'" style="color: #888; font-size: 12px">
								已选：{{ record.shopObj.title }}
							</div>
							<div v-if="record.liveObj && record.skipMode === 'LIVE'" style="color: #888; font-size: 12px">
								已选：{{ record.liveObj.mainTitle }}
							</div>
							<div v-if="record.videoObj && record.skipMode === 'TOPIC'" style="color: #888; font-size: 12px">
								已选：{{ record.videoObj.name }}
							</div>
						</template>
						<template v-else>
							<a-input
								v-model:value="formData[index].url"
								:disabled="record.whetherToClick === 'DISABLE'"
								placeholder="请输入地址或参数"
							/>
						</template>
					</a-form-item>
				</template>
			</template>
		</a-table>
	</a-form>
</template>

<script name="subForm" setup>
	import tool from '@/utils/tool'
	import { remove, isEmpty, cloneDeep } from 'lodash-es'
	import XnPageSelect from '@/components/XnPageSelect/index.vue'
	import mallGoodsApi from '@/api/shop/mallGoodsApi'
	import liveBizApi from '@/api/live/liveBizApi'
	import mallSpecialTopicApi from '@/api/shop/mallSpecialTopicApi'
	import { onMounted } from 'vue'
	const formRef = ref()
	const formData = ref([])
	const skipModeOptions = ref(tool.dictList('SKIP_MODE'))
	const whetherToClickOptions = ref(tool.dictList('WHETHER_TO_CLICK'))
	const props = defineProps({
		dataArray: {
			type: Object,
			default: []
		},
		place: {
			type: Object,
			default: []
		}
	})
	const columns = ref([
		{
			title: '位置',
			dataIndex: 'label',
			width: '20%'
		},
		{
			title: '点击事件',
			dataIndex: 'whetherToClick',
			width: '25%'
		},
		{
			title: '跳转方式',
			dataIndex: 'skipMode',
			width: '20%'
		},
		{
			title: 'URL',
			dataIndex: 'url'
		}
	])
	// 每行的校验
	const validateStatus = (record, name) => {
		if (record[name]) {
			return 'success'
		} else {
			return 'error'
		}
	}
	// 返回多的数据，用于增减行
	const dataFiltrate = (newVal, oldVal) => {
		let result = ''
		oldVal.forEach((data) => {
			if (!newVal.some((item) => item === data)) {
				result = data
			}
		})
		return result
	}
	watch(
		() => props.place,
		(newVal, oldVal) => {
			if (!isEmpty(props.dataArray) && isEmpty(formData.value)) {
				formData.value = cloneDeep(props.dataArray)
			} else {
				if (typeof newVal === 'object') {
					if (!isEmpty(formData.value)) {
						formData.value.forEach(() => {
							// 如果包含
							if (!newVal.some((item) => item === item.key)) {
								// 需要减少的
								if (formData.value.length > newVal.length) {
									const deleteData = dataFiltrate(newVal, oldVal)
									remove(formData.value, (item) => item.key === deleteData)
								}
								// 需要增加的
								if (formData.value.length < newVal.length) {
									const deleteData = dataFiltrate(oldVal, newVal)
									// 如果没有，就不增加
									if (!formData.value.some((item) => item === deleteData)) {
										const obj = {
											key: deleteData,
											label: tool.dictTypeData('DEV_SLIDESHOW_PLACE', deleteData),
											whetherToClick: 'DISABLE',
											skipMode: 'URL',
											url: ''
										}
										formData.value.push(obj)
									}
								}
							}
						})
					} else {
						newVal.forEach((item) => {
							const obj = {
								key: item,
								label: tool.dictTypeData('DEV_SLIDESHOW_PLACE', item),
								whetherToClick: 'DISABLE',
								skipMode: 'URL',
								url: ''
							}
							formData.value.push(obj)
						})
					}
				}
			}
		},
		{ immediate: true, deep: true }
	)
	// 修正watch，监听每一行的skipMode变化
	watch(
		() => formData.value.map((item) => item.skipMode),
		(newVal, oldVal) => {
			formData.value.forEach((item, idx) => {
				if (oldVal && item && oldVal[idx] && item.skipMode !== oldVal[idx]) {
					item.url = ''
					item.shopObj = undefined
					item.liveObj = undefined
					item.topicObj = undefined
				}
			})
		}
	)
	// 补充mounted时的回显逻辑
	onMounted(() => {
		formData.value.forEach(async (item, idx) => {
			if (item.url && ['SHOP', 'LIVE', 'TOPIC'].includes(item.skipMode)) {
				if (item.skipMode === 'SHOP' && !item.shopObj) {
					const arr = await shopEchoFunction({ idList: [item.url] })
					if (arr && arr[0]) item.shopObj = arr[0]
				}
				if (item.skipMode === 'LIVE' && !item.liveObj) {
					const arr = await liveEchoFunction({ idList: [item.url] })
					if (arr && arr[0]) item.liveObj = arr[0]
				}
				if (item.skipMode === 'TOPIC' && !item.topicObj) {
					const arr = await topicEchoFunction({ idList: [item.url] })
					if (arr && arr[0]) item.topicObj = arr[0]
				}
			}
		})
	})
	// 获取值，校验不通过返回false
	const getData = () => {
		if (isEmpty(formData.value)) {
			return false
		} else {
			let result = true
			formData.value.forEach((item) => {
				if (item.whetherToClick === 'ENABLE' && isEmpty(item.url)) {
					result = false
				}
			})
			if (result === false) {
				return false
			} else {
				return formData.value.map(({ shopObj, liveObj, topicObj, ...rest }) => rest)
			}
		}
	}
	const shopPageFunction = (param) =>
		mallGoodsApi.mallGoodsPage(param).then((data) => {
			data.records = data.records.map((item) => ({ name: item.title, id: item.id, ...item }))
			return data
		})
	const shopEchoFunction = (param) =>
		mallGoodsApi.mallGoodsDetail({ id: param.idList[0] }).then((data) => [{ name: data.title, id: data.id, ...data }])

	const livePageFunction = (param) =>
		liveBizApi.liveBizPage(param).then((data) => {
			data.records = data.records.map((item) => ({ name: item.mainTitle, id: item.id, ...item }))
			return data
		})
	const liveEchoFunction = (param) =>
		liveBizApi.liveBizDetail({ id: param.idList[0] }).then((data) => [{ name: data.mainTitle, id: data.id, ...data }])

	const topicPageFunction = (param) =>
		mallSpecialTopicApi.mallSpecialTopicPage(param).then((data) => {
			data.records = data.records.map((item) => ({ name: item.topicName, id: item.id, ...item }))
			return data
		})
	const topicEchoFunction = (param) =>
		mallSpecialTopicApi
			.mallSpecialTopicDetailPage({ id: param.idList[0] })
			.then((data) => [{ name: data.topicName, id: data.id, ...data }])

	const onSelectChange = (objKey, index, obj) => {
		formData.value[index][objKey] = obj
	}
	defineExpose({
		getData
	})
</script>
<style lang="less" scoped>
	.ant-form-item {
		margin-bottom: 0 !important;
	}
</style>
