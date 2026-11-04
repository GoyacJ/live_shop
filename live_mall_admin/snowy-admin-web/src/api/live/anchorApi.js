import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/live/anchor/` + url, ...arg)

/**
 * 主播信息表Api接口管理器
 *
 * @author lgq
 * @date  2025/03/21 14:22
 **/
export default {
	// 获取主播信息表分页
	anchorPage(data) {
		return request('page', data, 'get')
	},
	// 提交主播信息表表单 edit为true时为编辑，默认为新增
	anchorSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除主播信息表
	anchorDelete(data) {
		return request('delete', data)
	},
	// 获取主播信息表详情
	anchorDetail(data) {
		return request('detail', data, 'get')
	},
	// 获取主播信息表列表
	anchorList(data) {
		return request('list', data, 'get')
	}
}
