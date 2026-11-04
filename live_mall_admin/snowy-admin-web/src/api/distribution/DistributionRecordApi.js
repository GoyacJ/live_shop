import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/distribution/record/` + url, ...arg)

/**
 * 接口管理器
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
	}
}
