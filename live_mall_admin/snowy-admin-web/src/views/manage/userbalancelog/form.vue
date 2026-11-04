<template>
    <a-drawer
        :title="formData.id ? '编辑平台-用户余额变更记录' : '增加平台-用户余额变更记录'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-form-item label="变更类型：" name="type">
                <a-select v-model:value="formData.type" placeholder="请选择变更类型" :options="typeOptions" />
            </a-form-item>
            <a-form-item label="余额类型：" name="balance type">
                <a-select v-model:value="formData.balanceType" placeholder="请选择余额类型" :options="balanceTypeOptions" />
            </a-form-item>
            <a-form-item label="变更前：" name="beforeBalance">
                <a-input v-model:value="formData.beforeBalance" placeholder="请输入变更前" allow-clear />
            </a-form-item>
            <a-form-item label="变更：" name="changeBalance">
                <a-input v-model:value="formData.changeBalance" placeholder="请输入变更" allow-clear />
            </a-form-item>
            <a-form-item label="变更后：" name="afterBalance">
                <a-input v-model:value="formData.afterBalance" placeholder="请输入变更后" allow-clear />
            </a-form-item>
            <a-form-item label="变更原因：" name="reason">
                <a-input v-model:value="formData.reason" placeholder="请输入变更原因" allow-clear />
            </a-form-item>
            <a-form-item label="订单id/提现记录id/任务奖励记录id···：" name="reasonId">
                <a-input v-model:value="formData.reasonId" placeholder="请输入订单id/提现记录id/任务奖励记录id···" allow-clear />
            </a-form-item>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </a-drawer>
</template>

<script setup name="userBalanceLogForm">
    import tool from '@/utils/tool'
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import userBalanceLogApi from '@/api/manage/userBalanceLogApi'
    // 抽屉状态
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)
    const typeOptions = ref([])
    const balanceTypeOptions = ref([])

    // 打开抽屉
    const onOpen = (record) => {
        visible.value = true
        if (record) {
            let recordData = cloneDeep(record)
            formData.value = Object.assign({}, recordData)
        }
        typeOptions.value = tool.dictList('balance_change_type')
        balanceTypeOptions.value = tool.dictList('balance_type')
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
                userBalanceLogApi
                    .userBalanceLogSubmitForm(formDataParam, formDataParam.id)
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
