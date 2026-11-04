import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/manage/questionanswer/` + url, ...arg)

/**
 * 商城-问题答案Api接口管理器
 *
 **/
export default {
	// 问题答案分页
	questionAnswerPage(data) {
		return request('page', data, 'get')
	},
	// 提交商城-问题答案表单 edit为true时为编辑，默认为新增
	questionAnswerSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除商城-问题答案
	questionAnswerDelete(data) {
		return request('delete', data)
	},
	// 获取商城-问题答案详情
	questionAnswerDetail(data) {
		return request('detail', data, 'get')
	}
}
