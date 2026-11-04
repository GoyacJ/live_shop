<template>
    <a-drawer
        :title="formData.id ? '编辑平台-用户等级' : '增加平台-用户等级'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-form-item label="等级名称：" name="name">
                <a-input v-model:value="formData.name" placeholder="请输入等级名称" allow-clear />
            </a-form-item>
            <a-form-item label="图标：" name="icon">
				<a-image v-if="formData.icon" :src="formData.icon" :preview="{ visible: false }" />
				<a-upload
					v-model:file-list="fileList"
					action="http://127.0.0.1:8080/dev/file/uploadMinioReturnUrl"
					:headers="headers"
					list-type="picture"
					:maxCount="1"
					@change="handleChange"
					:before-upload="beforeUpload"
				>
					<a-button>
						<upload-outlined></upload-outlined>
						upload
					</a-button>
				</a-upload>
            </a-form-item>
            <a-form-item label="升级条件-积分：" name="integralCondition">
                <a-input-number v-model:value="formData.integralCondition" placeholder="请输入升级条件-积分" :min="1" :max="10000" style="width: 100%" />
            </a-form-item>
            <a-form-item label="升级条件-邀请人数：" name="userCondition">
                <a-input-number v-model:value="formData.userCondition" placeholder="请输入升级条件-邀请人数" :min="1" :max="10000" style="width: 100%" />
            </a-form-item>
            <a-form-item label="升级条件方式：" name="conditionMode">
                <a-input v-model:value="formData.conditionMode" placeholder="请输入升级条件方式" allow-clear />
            </a-form-item>
            <a-form-item label="权益描述：" name="equityIntroduction">
                <a-input v-model:value="formData.equityIntroduction" placeholder="请输入权益描述" allow-clear />
            </a-form-item>
            <a-form-item label="排序：" name="sort">
                <a-input v-model:value="formData.sort" placeholder="请输入排序" allow-clear />
            </a-form-item>
            <a-form-item label="状态：" name="status">
                <a-select v-model:value="formData.status" placeholder="请选择状态" :options="statusOptions" />
            </a-form-item>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </a-drawer>
</template>

<script setup name="userLevelForm">
    import tool from '@/utils/tool'
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import userLevelApi from '@/api/manage/userLevelApi'
	import { message } from "ant-design-vue";
    // 抽屉状态
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)
    const statusOptions = ref([])
	const headers = ref({
		token: tool.data.get('TOKEN')
	})

	const fileList = ref([]);

	const handleChange = info  => {
		if (info.file.status === 'done') {
			// Get this url from response in real world.
			console.log(info.file.response);
			formData.value.icon = info.file.response.data
		}
	};
	const beforeUpload = file => {
		console.log('---------------',fileList.value)
		fileList.value=[]
		const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
		if (!isJpgOrPng) {
			message.error('You can only upload JPG file!');
		}
		const isLt2M = file.size / 1024 / 1024 < 2;
		if (!isLt2M) {
			message.error('Image must smaller than 2MB!');
		}
		return isJpgOrPng && isLt2M;
	};

	// 打开抽屉
    const onOpen = (record) => {
        visible.value = true
        if (record) {
            let recordData = cloneDeep(record)
            formData.value = Object.assign({}, recordData)
        }
        statusOptions.value = tool.dictList('BIZ_NOTICE_PLACE')
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
                userLevelApi
                    .userLevelSubmitForm(formDataParam, formDataParam.id)
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
