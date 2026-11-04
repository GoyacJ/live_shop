import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/shop/mallgoodsslide/` + url, ...arg)

/**
 * 商品轮播图Api接口管理器
 *
 * @author yuezisheng
 * @date  2025/02/20 15:30
 **/
export default {
	// 获取商品轮播图分页
	mallGoodsSlidePage(data) {
		return request('page', data, 'get')
	},
	// 提交商品轮播图表单 edit为true时为编辑，默认为新增
	mallGoodsSlideSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除商品轮播图
	mallGoodsSlideDelete(data) {
		return request('delete', data)
	},
	// 获取商品轮播图详情
	mallGoodsSlideDetail(data) {
		return request('detail', data, 'get')
	},
	// 根据商品id获取商品轮播图
	queryEntityListByGoodsId(data) {
		return request('queryEntityListByGoodsId', data, 'get')
	},
	// 根据商品id删除商品轮播图
	deleteEntityListByGoodsId(data) {
		return request('deleteEntityListByGoodsId', data)
	}
}
