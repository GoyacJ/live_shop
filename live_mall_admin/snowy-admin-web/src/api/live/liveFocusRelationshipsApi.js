import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/live/focusrelationships/` + url, ...arg)

/**
 * 关注关系表Api接口管理器
 *
 * @author lgq
 * @date  2025/03/04 14:07
 **/
export default {
	// 获取关注关系表分页
	liveFocusRelationshipsPage(data) {
		return request('page', data, 'get')
	},
	// 提交关注关系表表单 edit为true时为编辑，默认为新增
	liveFocusRelationshipsSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除关注关系表
	liveFocusRelationshipsDelete(data) {
		return request('delete', data)
	},
	// 获取关注关系表详情
	liveFocusRelationshipsDetail(data) {
		return request('detail', data, 'get')
	}
}
