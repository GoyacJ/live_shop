import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/content/video/` + url, ...arg)

/**
 * 视频Api接口管理器
 *
 * @author lgq
 * @date  2025/03/21 14:22
 **/
export default {
	// 视频列表
	videoPage(data) {
		return request('page', data, 'get')
	},

	// 获取视频详情
	videoDetail(id) {
		return request('detail', { id }, 'get')
	},

	// 新增视频
	videoAdd(data) {
		return request('add', data)
	},

	// 批量新增
	batchAdd(data) {
		return request('batch-add', data)
	},

	// 编辑视频
	videoEdit(data) {
		return request('edit', data)
	},

	// 删除视频（支持批量）
	videoDelete(ids) {
		return request('delete', ids)
	},

	// 视频发布/下架
	changeStatus(data) {
		return request('change-status', data)
	}
}
