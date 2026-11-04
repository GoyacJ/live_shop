import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/manage/useraddress/` + url, ...arg)

/**
 * 商城-用户收获地址Api接口管理器
 *
 * @author yuezisheng
 * @date  2025/02/18 21:28
 **/
export default {
	// 获取商城-用户收获地址分页
	userAddressPage(data) {
		return request('page', data, 'get')
	},
	// 提交商城-用户收获地址表单 edit为true时为编辑，默认为新增
	userAddressSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除商城-用户收获地址
	userAddressDelete(data) {
		return request('delete', data)
	},
	// 获取商城-用户收获地址详情
	userAddressDetail(data) {
		return request('detail', data, 'get')
	}
}
