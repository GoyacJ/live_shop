import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/live/statistics/` + url, ...arg)

/**
 * 直播用户统计Api接口管理器
 *
 * @author goya
 * @since 2025/4/30 09:40
 **/
export default {
	// 获取直播用户信息统计
	info(data) {
		return request('info', data, 'get')
	}
}
