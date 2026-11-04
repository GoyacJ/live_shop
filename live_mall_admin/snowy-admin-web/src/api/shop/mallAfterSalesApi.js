import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/shop/afterSales/` + url, ...arg)

/**
 * 商城-售后接口管理器
 *
 **/
export default {
	// 获取售后信息page
	mallAfterSalesPage(data) {
		return request('page', data, 'get')
	},
	// 提交售后信息 edit为true时为编辑，默认为新增
	mallAfterSalesSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 获取售后信息详情
	mallAfterSalesDetail(data) {
		return request('detail', data, 'get')
	},
	// 删除售后信息
	mallAfterSalesDelete(data) {
		return request('delete', data)
	}
}
