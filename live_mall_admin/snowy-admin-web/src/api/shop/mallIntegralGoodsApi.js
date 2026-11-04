import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/shop/mallintegralgoods/` + url, ...arg)

/**
 * 积分商城Api接口管理器
 *
 * @author 陈金坤
 * @date  2025/04/08 14:04
 **/
export default {
	// 获取积分商城分页
	mallIntegralGoodsPage(data) {
		return request('page', data, 'get')
	},
	// 提交积分商城表单 edit为true时为编辑，默认为新增
	mallIntegralGoodsSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除积分商城
	mallIntegralGoodsDelete(data) {
		return request('delete', data)
	},
	// 获取积分商城详情
	mallIntegralGoodsDetail(data) {
		return request('detail', data, 'get')
	}
}
