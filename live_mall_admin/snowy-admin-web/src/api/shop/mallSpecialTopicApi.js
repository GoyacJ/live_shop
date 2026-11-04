import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/shop/special/topic/` + url, ...arg)

/**
 * 商城-专题Api接口管理器
 *
 * @author yuezisheng
 * @date  2025/02/20 15:25
 **/
export default {
	// 获取商城-专题分页
	mallSpecialTopicPage(data) {
		return request('page', data, 'get')
	},
	// 提交商城-专题表单 edit为true时为编辑，默认为新增
	mallSpecialTopicSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除商城-专题
	mallSpecialTopicDelete(data) {
		return request('delete', data)
	},
	// 绑定专题商品
	mallSpecialTopicBinkForm(data) {
		return request('bind', data)
	},
	// 删除专题绑定商品
	mallSpecialTopicDetailDelete(data) {
		return request('detail/delete', data)
	},
	// 获取专题详情商品分页
	mallSpecialTopicDetailPage(data) {
		return request('detail/page', data, 'get')
	}
}
