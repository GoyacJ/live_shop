import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/manage/userwithdrawallog/` + url, ...arg)

/**
 * 用户提现记录Api接口管理器
 *
 * @author yuezisheng
 * @date  2025/02/18 20:31
 **/
export default {
	// 获取用户提现记录分页
	userWithdrawalLogPage(data) {
		return request('page', data, 'get')
	},
	// 提交用户提现记录表单 edit为true时为编辑，默认为新增
	userWithdrawalLogSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除用户提现记录
	userWithdrawalLogDelete(data) {
		return request('delete', data)
	},
	// 获取用户提现记录详情
	userWithdrawalLogDetail(data) {
		return request('detail', data, 'get')
	}
}
