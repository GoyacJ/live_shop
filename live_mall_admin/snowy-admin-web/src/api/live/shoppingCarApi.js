import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/live/shoppingcar/` + url, ...arg)

/**
 * 直播间购物车表Api接口管理器
 *
 * @author lgq
 * @date  2025/03/04 14:17
 **/
export default {
	// 获取直播间购物车表分页
	shoppingCarPage(data) {
		return request('page', data, 'get')
	},
	// 提交直播间购物车表表单 edit为true时为编辑，默认为新增
	shoppingCarSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除直播间购物车表
	shoppingCarDelete(data) {
		return request('delete', data)
	},
	// 获取直播间购物车表详情
	shoppingCarDetail(data) {
		return request('detail', data, 'get')
	},

	// 直播间购物车商品置顶
	shoppingCarTop(data) {
		return request('top', data)
	},

	// 直播间购物车商品小黄车提示
	shoppingCarTips(data) {
		return request('tips', data)
	}
}
