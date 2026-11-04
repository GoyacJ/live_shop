import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/live/spectatorwatching/` + url, ...arg)

/**
 * 直播观众观看时间表Api接口管理器
 *
 * @author GMQ
 * @date  2025/04/06 13:10
 **/
export default {
	// 获取直播观众观看时间表分页
	liveSpectatorWatchingPage(data) {
		return request('page', data, 'get')
	},
	// 提交直播观众观看时间表表单 edit为true时为编辑，默认为新增
	liveSpectatorWatchingSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除直播观众观看时间表
	liveSpectatorWatchingDelete(data) {
		return request('delete', data)
	},
	// 获取直播观众观看时间表详情
	liveSpectatorWatchingDetail(data) {
		return request('detail', data, 'get')
	}
}
