import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/manage/userintegral/` + url, ...arg)

/**
 * 用户积分兑换Api接口管理器
 *
 * @author yuezisheng
 * @date  2025/02/18 11:07
 **/
export default {
	// 获取用户积分兑换分页
	userIntegralPage(data) {
		return request('page', data, 'get')
	},
	// 提交用户积分兑换表单 edit为true时为编辑，默认为新增
	userIntegralSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除用户积分兑换
	userIntegralDelete(data) {
		return request('delete', data)
	},
	// 获取用户积分兑换详情
	userIntegralDetail(data) {
		return request('detail', data, 'get')
	}
}
