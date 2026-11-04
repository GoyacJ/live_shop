import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/shop/mallordergoods/` + url, ...arg)

/**
 * 商城-订单商品接口管理器
 *
 **/
export default {
	// 获取订单商品信息page
	mallOrderGoodsPage(data) {
		return request('page', data, 'get')
	},
	// 修改订单商品信息
	mallOrderGoodsEdit(data, edit) {
		return request('edit', data)
	},
	// 新增订单商品信息
	mallOrderGoodsAdd(data, edit) {
		return request('add', data)
	},
	// 获取订单商品信息详情
	mallOrderGoodsDetail(data) {
		return request('detail', data, 'get')
	},
	mallOrderGoodsListByOrderId(data) {
		return baseRequest(`/shop/ordergoods/ordergoodslistbyorderid`, data, 'get')
	},
	// 删除订单商品信息
	mallOrderGoodsDelete(data) {
		return request('delete', data)
	},
	//获取订单商品list
	mallOrderGoodsList(data) {
		return request('list', data, 'get')
	}
}
