import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/integral/info/` + url, ...arg)

/**
 * 积分管理接口管理器
 *
 **/
export default {
	// 分页
	page(data) {
		return request('page', data, 'get')
	},

	// 详情
	detail(data) {
		return request('detail', data, 'get')
	},

	// 调整积分
	adjust(data) {
		return request('adjust', data)
	},

	// 更改积分状态
	changeStatus(data) {
		return request('change-status', data)
	}
}
