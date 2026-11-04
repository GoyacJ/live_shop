<template>
	<xn-form-container :title="title" :width="1000" v-model:open="open" :destroy-on-close="true" @close="onClose">
		<Shoppingcar :live-room-record="liveRoomRecord" ref="shoppingcarRef" v-if="title === '购物车管理'"></Shoppingcar>
		<Marketingcampaigns
			:live-room-record="liveRoomRecord"
			ref="marketingcampaignsRef"
			v-if="title === '营销活动管理'"
		></Marketingcampaigns>
	</xn-form-container>
</template>

<script setup name="liveoperateindex">
	import { message } from 'ant-design-vue'
	import Marketingcampaigns from '@/views/live/marketingcampaigns/index.vue'
	import Shoppingcar from '@/views/live/shoppingcar/index.vue'

	// 抽屉状态
	const open = ref(false)
	const title = ref('')
	const liveRoomRecord = ref({})

	const emit = defineEmits({ successful: null })

	// 打开抽屉
	const onOpen = (record, type) => {
		if (type === 'car') {
			liveRoomRecord.value = record
			title.value = '购物车管理'
		} else if (type === 'activity') {
			title.value = '营销活动管理'
			liveRoomRecord.value = record
		} else {
			liveRoomRecord.value = {}
		}
		open.value = true
	}
	// 关闭抽屉
	const onClose = () => {
		open.value = false
	}
	// 抛出函数
	defineExpose({
		onOpen
	})
</script>
