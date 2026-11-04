import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/shop/logistics/` + url, ...arg)

/**
 * 商城-订单物流信息控制器
 *
 **/
export default {
	// 设置物流信息
	setting(data, setting) {
		return request('setting', data)
	},
	// 获取订单信息详情
	detail(data) {
		return request('detail', data, 'get')
	},
	// 物流信息导入-发货模板
	importLogistics(file) {
		const formData = new FormData()
		formData.append('file', file)
		return request('import', formData)
	}
}
