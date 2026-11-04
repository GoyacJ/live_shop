import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/manage/feedback/` + url, ...arg)

/**
 * 反馈意见Api接口管理器
 *
 **/
export default {
	// 获取反馈意见分页
	feedbackPage(data) {
		return request('page', data, 'get')
	},
	// 提交反馈意见表单 edit为true时为编辑，默认为新增
	feedbackSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除反馈意见
	feedbackDelete(data) {
		return request('delete', data)
	},
	// 获取反馈意见详情
	feedbackDetail(data) {
		return request('detail', data, 'get')
	}
}
