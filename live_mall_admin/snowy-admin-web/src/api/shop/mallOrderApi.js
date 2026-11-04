import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/shop/order/` + url, ...arg)

/**
 * 商城-订单接口管理器
 *
 **/
export default {
	// 获取订单信息page
	mallOrderPage(data) {
		return request('page', data, 'get')
	},
	mallIntegralOrderPage(data) {
		return request('integral/page', data, 'get')
	},
	liveOrderPage(data) {
		return request('live/page', data, 'get')
	},
	// 修改订单信息
	mallOrderEdit(data, edit) {
		return request('edit', data)
	},
	// 新增订单信息
	mallOrderAdd(data, edit) {
		return request('add', data)
	},
	// 获取订单信息详情
	mallOrderDetail(data) {
		return request('detail', data, 'get')
	},
	// 删除订单信息
	mallOrderDelete(data) {
		return request('delete', data)
	},
	//获取订单list
	mallOrderList(data) {
		return request('list', data, 'get')
	},
	// 订单信息导出
	exportOrders(params) {
		return request('export', params, 'get', {
			responseType: 'blob'
		})
	}
}
