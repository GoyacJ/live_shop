import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/manage/userinfo/` + url, ...arg)

/**
 * 用户列表Api接口管理器
 *
 * @author zhaishaojiang
 * @date  2025/02/17 17:28
 **/
export default {
	// 获取用户列表分页
	userInfoPage(data) {
		return request('page', data, 'get')
	},
	// 提交用户列表表单 edit为true时为编辑，默认为新增
	userInfoSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除用户列表
	userInfoDelete(data) {
		return request('delete', data)
	},
	// 获取用户列表详情
	userInfoDetail(data) {
		return request('detail', data, 'get')
	}
}
