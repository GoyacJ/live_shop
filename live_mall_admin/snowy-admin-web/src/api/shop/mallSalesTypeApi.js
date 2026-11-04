import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/shop/salestype/` + url, ...arg)

/**
 * 商城-售后类型接口管理器
 *
 **/
export default {
	// 修改售后类型信息
	mallSalesTypeEdit(data, edit) {
		return request('edit', data)
	},
	// 新增售后类型信息
	mallSalesTypeAdd(data, edit) {
		return request('add', data)
	},
	// 获取售后类型信息详情
	mallSalesTypeDetail(data) {
		return request('detail', data, 'get')
	},
	// 删除售后类型信息
	mallSalesTypeDelete(data) {
		return request('delete', data)
	},
	//获取售后类型list
	mallSalesTypeList(data) {
		return request('list', data, 'get')
	},
	// 获取用户选择器
	mallSalesTypeSelector(data) {
		return request('mallSalesTypeSelector', data, 'get')
	}
}
