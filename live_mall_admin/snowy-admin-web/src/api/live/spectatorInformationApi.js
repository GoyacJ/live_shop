import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/live/spectatorinformation/` + url, ...arg)

/**
 * 观众信息表Api接口管理器
 *
 * @author lgq
 * @date  2025/03/04 14:18
 **/
export default {
	// 获取观众信息表分页
	spectatorInformationPage(data) {
		return request('page', data, 'get')
	},
	// 提交观众信息表表单 edit为true时为编辑，默认为新增
	spectatorInformationSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除观众信息表
	spectatorInformationDelete(data) {
		return request('delete', data)
	},
	// 获取观众信息表详情
	spectatorInformationDetail(data) {
		return request('detail', data, 'get')
	}
}
