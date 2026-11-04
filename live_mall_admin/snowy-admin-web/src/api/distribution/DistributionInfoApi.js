import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/distribution/info/` + url, ...arg)

/**
 * 接口管理器
 *
 **/
export default {
	// 分页
	page(data) {
		return request('page', data, 'get')
	},

	// 详情
	detail(data) {
		return request('detail', data, 'get')
	},

	// 提交反馈意见表单 edit为true时为编辑，默认为新增
	submitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},

	// 删除支持批量）
	delete(ids) {
		return request('delete', ids)
	},

	// 更改状态
	changeStatus(data) {
		return request('change-status', data)
	}
}
