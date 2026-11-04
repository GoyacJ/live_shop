import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/manage/userbalancelog/` + url, ...arg)

/**
 * 平台-用户余额变更记录Api接口管理器
 *
 * @author yuezisheng
 * @date  2025/02/18 17:55
 **/
export default {
	// 获取平台-用户余额变更记录分页
	userBalanceLogPage(data) {
		return request('page', data, 'get')
	},
	// 提交平台-用户余额变更记录表单 edit为true时为编辑，默认为新增
	userBalanceLogSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除平台-用户余额变更记录
	userBalanceLogDelete(data) {
		return request('delete', data)
	},
	// 获取平台-用户余额变更记录详情
	userBalanceLogDetail(data) {
		return request('detail', data, 'get')
	}
}
