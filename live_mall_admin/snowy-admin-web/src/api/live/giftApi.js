import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/live/gift/` + url, ...arg)

/**
 * 礼物表Api接口管理器
 *
 * @author lgq
 * @date  2025/03/04 14:13
 **/
export default {
	// 获取礼物表分页
	giftPage(data) {
		return request('page', data, 'get')
	},
	// 提交礼物表表单 edit为true时为编辑，默认为新增
	giftSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除礼物表
	giftDelete(data) {
		return request('delete', data)
	},
	// 获取礼物表详情
	giftDetail(data) {
		return request('detail', data, 'get')
	}
}
