import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/live/marketingcampaignslog/` + url, ...arg)

/**
 * 营销活动记录表Api接口管理器
 *
 * @author lgq
 * @date  2025/03/13 21:51
 **/
export default {
	// 获取营销活动记录表分页
	liveMarketingCampaignsLogPage(data) {
		return request('page', data, 'get')
	},
	// 提交营销活动记录表表单 edit为true时为编辑，默认为新增
	liveMarketingCampaignsLogSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除营销活动记录表
	liveMarketingCampaignsLogDelete(data) {
		return request('delete', data)
	},
	// 获取营销活动记录表详情
	liveMarketingCampaignsLogDetail(data) {
		return request('detail', data, 'get')
	}
}
