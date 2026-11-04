import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/live/spectatorgrade/` + url, ...arg)

/**
 * 直播观众成长表Api接口管理器
 *
 * @author GMQ
 * @date  2025/04/06 13:06
 **/
export default {
	// 获取直播观众成长表分页
	liveSpectatorGradePage(data) {
		return request('page', data, 'get')
	},
	// 提交直播观众成长表表单 edit为true时为编辑，默认为新增
	liveSpectatorGradeSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除直播观众成长表
	liveSpectatorGradeDelete(data) {
		return request('delete', data)
	},
	// 获取直播观众成长表详情
	liveSpectatorGradeDetail(data) {
		return request('detail', data, 'get')
	}
}
