import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/live/tasksheet/` + url, ...arg)

/**
 * 任务表Api接口管理器
 *
 * @author lgq
 * @date  2025/03/04 14:20
 **/
export default {
	// 获取任务表分页
	taskSheetPage(data) {
		return request('page', data, 'get')
	},
	// 提交任务表表单 edit为true时为编辑，默认为新增
	taskSheetSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除任务表
	taskSheetDelete(data) {
		return request('delete', data)
	},
	// 获取任务表详情
	taskSheetDetail(data) {
		return request('detail', data, 'get')
	}
}
