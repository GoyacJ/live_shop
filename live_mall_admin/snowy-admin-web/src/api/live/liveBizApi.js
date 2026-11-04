import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/live/livebiz/` + url, ...arg)

/**
 * 直播列表Api接口管理器
 *
 * @author zsj
 * @date  2025/04/02 16:57
 **/
export default {
	// 获取直播列表分页
	liveBizPage(data) {
		return request('page', data, 'get')
	},
	// 获取直播列表分页
	liveBizList(data) {
		return request('list', data, 'get')
	},
	// 提交直播列表表单 edit为true时为编辑，默认为新增
	liveBizSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除直播列表
	liveBizDelete(data) {
		return request('delete', data)
	},
	// 获取直播列表详情
	liveBizDetail(data) {
		return request('detail', data, 'get')
	}
}
