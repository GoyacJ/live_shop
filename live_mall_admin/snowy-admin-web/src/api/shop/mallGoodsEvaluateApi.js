import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/shop/mallgoodsEvaluate/` + url, ...arg)

/**
 * 商品评价api
 *
 **/
export default {
	// 获取商品评价信息分页
	mallGoodsEvaluePage(data) {
		return request('page', data, 'get')
	},
	// 获取商品评价信息详情
	mallGoodsEvalueDetail(data) {
		return request('detail', data, 'get')
	},

	// 修改商品评价信息
	mallGoodsEvaluaEditForm(data) {
		return request('edit', data)
	},

	// 新增商品评价信息
	mallGoodsEvaluaAddForm(data) {
		return request('add', data)
	},

	// 获取商品评价信息List
	mallGoodsEvalueList(data) {
		return request('list', data)
	},

	// 删除商城-商品
	mallGoodsEvaluateDelete(data) {
		return request('delete', data)
	},
	// 禁用用户
	mallGoodsDisableStatus(data) {
		return request('disablestatus', data)
	},
	// 启用用户
	mallGoodsEnableStatus(data) {
		return request('enablestatus', data)
	}
}
