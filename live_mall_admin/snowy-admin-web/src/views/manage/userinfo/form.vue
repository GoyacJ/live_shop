<template>
    <a-drawer
        :title="formData.id ? '编辑用户列表' : '增加用户列表'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-form-item label="手机号：" name="mobile">
                <a-input v-model:value="formData.mobile" placeholder="请输入手机号" allow-clear />
            </a-form-item>
			<a-form-item label="上传头像：" name="mobile">
				<img :src="formData.avatar" />
				<div style="margin: 20px">
				<a-button @click="uploadLogo" type="primary">上传头像</a-button>
				</div>
			</a-form-item>

			<a-form-item label="等级id：" name="levelId">
                <a-input v-model:value="formData.levelId" placeholder="请输入等级id" allow-clear />
            </a-form-item>
            <a-form-item label="昵称：" name="nickname">
                <a-input v-model:value="formData.nickname" placeholder="请输入昵称" allow-clear />
            </a-form-item>
            <a-form-item label="积分账户：" name="integral">
                <a-input v-model:value="formData.integral" placeholder="请输入积分账户" allow-clear />
            </a-form-item>
            <a-form-item label="冻结积分：" name="frozenIntegral">
                <a-input v-model:value="formData.frozenIntegral" placeholder="请输入冻结积分" allow-clear />
            </a-form-item>
            <a-form-item label="现金账户：" name="cash">
                <a-input v-model:value="formData.cash" placeholder="请输入现金账户" allow-clear />
            </a-form-item>
            <a-form-item label="冻结现金：" name="frozenCash">
                <a-input v-model:value="formData.frozenCash" placeholder="请输入冻结现金" allow-clear />
            </a-form-item>
            <a-form-item label="上次登录ip：" name="lastLoginIp">
                <a-input v-model:value="formData.lastLoginIp" placeholder="请输入上次登录ip" allow-clear />
            </a-form-item>
            <a-form-item label="上次登录地点：" name="lastLoginAddress">
                <a-input v-model:value="formData.lastLoginAddress" placeholder="请输入上次登录地点" allow-clear />
            </a-form-item>
            <a-form-item label="上次登录时间：" name="lastLoginTime">
                <a-date-picker v-model:value="formData.lastLoginTime" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择上次登录时间" style="width: 100%" />
            </a-form-item>
            <a-form-item label="上次登录设备：" name="lastLoginDevice">
                <a-input v-model:value="formData.lastLoginDevice" placeholder="请输入上次登录设备" allow-clear />
            </a-form-item>
            <a-form-item label="用户状态：" name="userStatus">
                <a-input v-model:value="formData.userStatus" placeholder="请输入用户状态" allow-clear />
            </a-form-item>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </a-drawer>
	<CropUpload ref="cropUploadRef" :img-src="img" @successful="cropUploadSuccess" />
</template>

<script setup name="userInfoForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import userInfoApi from '@/api/manage/userInfoApi'
	import tool from "@/utils/tool";
	import CropUpload from '@/components/CropUpload/index.vue'
	import * as avatarLoading from "lodash-es";
	import userCenterApi from "@/api/sys/userCenterApi";

	const cropUploadRef = ref("")

	const img = ref('')

	const uploadLogo = () => {

		cropUploadRef.value.show()

	}
	// 截图组件回调方法
	const cropUploadSuccess = (data) => {
		// 转换为file类型
		const result = new File([data.blobData], data.fileName, { type: 'image/jpeg', lastModified: Date.now() })
		const fileData = new FormData()
		fileData.append('file', result)
		console.log(data)
		userCenterApi.userUpdateAvatar(fileData).then((data) => {
			// 更新缓存
			console.log("=========",data)
			formData.value.avatar=data
		})
	}

	// 抽屉状态
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)

    // 打开抽屉
    const onOpen = (record) => {
        visible.value = true

		if (record) {
            let recordData = cloneDeep(record)
            formData.value = Object.assign({}, recordData)
        }
    }
    // 关闭抽屉
    const onClose = () => {
        formRef.value.resetFields()
        formData.value = {}
        visible.value = false
    }
    // 默认要校验的
    const formRules = {
    }
    // 验证并提交数据
    const onSubmit = () => {
        formRef.value
            .validate()
            .then(() => {
                submitLoading.value = true
                const formDataParam = cloneDeep(formData.value)
                userInfoApi
                    .userInfoSubmitForm(formDataParam, formDataParam.id)
                    .then(() => {
                        onClose()
                        emit('successful')
                    })
                    .finally(() => {
                        submitLoading.value = false
                    })
            })
    }
    // 抛出函数
    defineExpose({
        onOpen
    })
</script>
