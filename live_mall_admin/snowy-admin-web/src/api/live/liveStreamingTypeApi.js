import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/live/livestreamingtype/` + url, ...arg)

/**
 * 直播-直播类型Api接口管理器
 *
 **/
export default {
	// 直播类型分页
	liveStreamingTypePage(data) {
		return request('page', data, 'get')
	},
	// 直播类型表单 edit为true时为编辑，默认为新增
	liveStreamingTypeSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除直播类型
	liveStreamingTypeDelete(data) {
		return request('delete', data)
	},
	// 直播类型详情
	liveStreamingTypeDetail(data) {
		return request('detail', data, 'get')
	},
	// 直播类型List
	liveStreamingTypeList(data) {
		return request('list', data, 'get')
	}
}
