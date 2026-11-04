import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/content/short-play/` + url, ...arg)

/**
 * 短剧集合Api接口管理器
 *
 * @author lgq
 * @date  2025/03/21 14:22
 **/
export default {
	// 短剧列表
	shortplayPage(data) {
		return request('page', data, 'get')
	},

	// 获取短剧详情
	shortplayDetail(id) {
		return request('detail', { id }, 'get')
	},

	// 新增短剧
	shortplayAdd(data) {
		return request('add', data)
	},

	// 编辑短剧
	shortplayEdit(data) {
		return request('edit', data)
	},

	// 删除短剧（支持批量）
	shortplayDelete(ids) {
		return request('delete', ids)
	},

	// 短剧发布/下架
	changeStatus(data) {
		return request('change-status', data)
	},

	// 获取单个短剧的所有集数
	episodePage(data) {
		return request('episode/page', data, 'get')
	},

	// 获取单集详情
	episodeDetail(id) {
		return request('episode/detail', { id }, 'get')
	},

	// 新增剧集
	episodeAdd(data) {
		return request('episode/add', data)
	},

	// 新增剧集
	episodeBatchAdd(data) {
		return request('episode/batch-add', data)
	},

	// 编辑剧集
	episodeEdit(data) {
		return request('episode/edit', data)
	},

	// 删除剧集
	episodeDelete(ids) {
		return request('episode/delete', ids)
	},

	// 获取视频列表
	getVideoList(data) {
		return baseRequest('/content/video/page', data, 'get')
	},

	// 获取视频详情
	getVideoDetail(id) {
		return baseRequest('/content/video/detail', { id }, 'get')
	}
}
