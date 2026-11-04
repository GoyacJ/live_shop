import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/shop/thirdPayment/` + url, ...arg)

/**
 * 商城-支付接口管理器
 *
 **/
export default {
	// 获取支付信息page
	mallThirdPaymentPage(data) {
		return request('page', data, 'get')
	},
	// 提交支付信息 edit为true时为编辑，默认为新增
	mallThirdPaymentSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 获取支付信息详情
	mallThirdPaymentDetail(data) {
		return request('detail', data, 'get')
	},
	// 删除支付信息
	mallThirdPaymentDelete(data) {
		return request('delete', data)
	}
}
