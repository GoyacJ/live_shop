<template>
    <a-drawer
        :title="formData.id ? '编辑用户积分兑换' : '增加用户积分兑换'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-form-item label="提现金额：" name="amount">
                <a-input v-model:value="formData.amount" placeholder="请输入提现金额" allow-clear />
            </a-form-item>
            <a-form-item label="审核原因：" name="reason">
                <a-textarea v-model:value="formData.reason" placeholder="请输入审核原因" :auto-size="{ minRows: 3, maxRows: 5 }" />
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

<script setup name="userIntegralForm">
    import tool from '@/utils/tool'
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import userIntegralApi from '@/api/manage/userIntegralApi'
    // 抽屉状态
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)
    const statusOptions = ref([])

    // 打开抽屉
    const onOpen = (record) => {
        visible.value = true
        if (record) {
            let recordData = cloneDeep(record)
            formData.value = Object.assign({}, recordData)
        }
        statusOptions.value = tool.dictList('points_redemption_status')
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
                userIntegralApi
                    .userIntegralSubmitForm(formDataParam, formDataParam.id)
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
