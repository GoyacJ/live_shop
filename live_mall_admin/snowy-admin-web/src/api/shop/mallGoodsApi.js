import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/shop/mallgoods/` + url, ...arg)

/**
 * 商城-商品Api接口管理器
 *
 * @author yuezisheng
 * @date  2025/02/20 15:25
 **/
export default {
	// 获取商城-商品分页
	mallGoodsPage(data) {
		return request('page', data, 'get')
	},
	// 提交商城-商品表单 edit为true时为编辑，默认为新增
	mallGoodsSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除商城-商品
	mallGoodsDelete(data) {
		return request('delete', data)
	},
	// 商品导出
	mallGoodsExport(data) {
		return request('export', data, 'get', {
			responseType: 'blob'
		})
	},
	// 获取商城-商品详情
	mallGoodsDetail(data) {
		return request('detail', data, 'get')
	},
	// 获取商城-商品List
	mallGoodsList(data) {
		return request('list', data, 'get')
	},
	// 商城-商品检索
	mallGoodsSearch(data) {
		return request('search', data, 'get')
	}
}
