import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/manage/userlevel/` + url, ...arg)

/**
 * 平台-用户等级Api接口管理器
 *
 * @author yuezisheng
 * @date  2025/02/18 09:16
 **/
export default {
	// 获取平台-用户等级分页
	userLevelPage(data) {
		return request('page', data, 'get')
	},
	// 提交平台-用户等级表单 edit为true时为编辑，默认为新增
	userLevelSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除平台-用户等级
	userLevelDelete(data) {
		return request('delete', data)
	},
	// 获取平台-用户等级详情
	userLevelDetail(data) {
		return request('detail', data, 'get')
	}
}
