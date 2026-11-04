import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/shop/paymentconfig/` + url, ...arg)

/**
 * 商城-支付接口管理器
 *
 **/
export default {
	// 获取支付信息page
	mallPaymentConfigPage(data) {
		return request('page', data, 'get')
	},
	// 提交支付信息 edit为true时为编辑，默认为新增
	mallPaymentConfigSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 获取支付信息详情
	mallPaymentConfigDetail(data) {
		return request('detail', data, 'get')
	},
	// 删除支付信息
	mallPaymentConfigDelete(data) {
		return request('delete', data)
	}
}
