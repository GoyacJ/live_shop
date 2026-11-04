import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/live/marketingcampaigns/` + url, ...arg)

/**
 * 营销活动表Api接口管理器
 *
 * @author lgq
 * @date  2025/03/04 14:15
 **/
export default {
	// 获取营销活动表分页
	marketingCampaignsPage(data) {
		return request('page', data, 'get')
	},
	// 提交营销活动表表单 edit为true时为编辑，默认为新增
	marketingCampaignsSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除营销活动表
	marketingCampaignsDelete(data) {
		return request('delete', data)
	},
	// 获取营销活动表详情
	marketingCampaignsDetail(data) {
		return request('detail', data, 'get')
	}
}
