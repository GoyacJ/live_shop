<template>
	<a-card
		class="xn-wd"
		:bordered="false"
		:tab-list="tabListNoTitle"
		:active-tab-key="noTitleKey"
		@tabChange="(key) => onTabChange(key, 'noTitleKey')"
	>
		<p v-if="noTitleKey === 'sysConfig'">
			<SysConfig />
		</p>
		<p v-else-if="noTitleKey === 'emailConfig'">
			<EmailConfig />
		</p>
		<p v-else-if="noTitleKey === 'smsConfig'">
			<SmsConfig />
		</p>
		<p v-else-if="noTitleKey === 'fileConfig'">
			<FileConfig />
		</p>
		<p v-else-if="noTitleKey === 'thirdConfig'">
			<ThirdConfig />
		</p>
		<p v-else-if="noTitleKey === 'otherConfig'">
			<other-config />
		</p>
		<p v-else-if="noTitleKey === 'customerConfig'">
			<customerConfig />
		</p>
		<p v-else-if="noTitleKey === 'liveConfig'">
			<live-config />
		</p>
		<p v-else-if="noTitleKey === 'privacyAgreementConfig'">
			<privacy-agreement-config />
		</p>
		<p v-else-if="noTitleKey === 'appVersionConfig'">
			<app-version-config />
		</p>
	</a-card>
</template>

<script setup name="devConfig">
	import SysConfig from './sysConfig.vue'
	import EmailConfig from './emailConfig/index.vue'
	import SmsConfig from './smsConfig/index.vue'
	import FileConfig from './fileConfig/index.vue'
	import ThirdConfig from './thirdConfig/index.vue'
	import otherConfig from './otherConfig/index.vue'
	import liveConfig from './liveConfig/index.vue'
	import customerConfig from './customerConfig/index.vue'
	import privacyAgreementConfig from './privacyAgreementConfig/index.vue'
	import AppVersionConfig from './appVersionConfig/index.vue'

	const key = ref('sysConfig')
	const noTitleKey = ref('sysConfig')

	const tabListNoTitle = [
		{
			key: 'sysConfig',
			tab: '系统配置'
		},
		{
			key: 'emailConfig',
			tab: '邮件配置'
		},
		{
			key: 'smsConfig',
			tab: '短信配置'
		},
		{
			key: 'fileConfig',
			tab: '文件配置'
		},
		{
			key: 'thirdConfig',
			tab: '第三方配置'
		},
		{
			key: 'otherConfig',
			tab: '其他配置'
		},
		{
			key: 'customerConfig',
			tab: '客服配置'
		},
		{
			key: 'liveConfig',
			tab: '分享配置'
		},
		{
			key: 'privacyAgreementConfig',
			tab: '隐私协议'
		},
		{
			key: 'appVersionConfig',
			tab: '移动端版本配置'
		}
	]

	const onTabChange = (value, type) => {
		if (type === 'key') {
			key.value = value
		} else if (type === 'noTitleKey') {
			noTitleKey.value = value
		}
	}
</script>
