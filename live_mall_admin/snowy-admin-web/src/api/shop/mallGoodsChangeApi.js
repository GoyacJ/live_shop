import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/shop/mallgoodsChange/` + url, ...arg)

/**
 * 商品变更记录api
 *
 **/
export default {
	// 获取商品变更信息详情
	mallGoodsChangeDetail(data) {
		return request('detail', data, 'get')
	},

	// 新增商品变更信息
	mallGoodsChangeAdd(data) {
		return request('add', data)
	}
}
