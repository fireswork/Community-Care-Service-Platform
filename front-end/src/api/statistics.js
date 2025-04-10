import request from '@/utils/request'

/**
 * 获取统计概览数据
 */
export function getOverviewStatistics() {
  return request({
    url: '/statistics/overview',
    method: 'get'
  })
}

/**
 * 获取人员分类统计数据
 */
export function getCategoryStatistics() {
  return request({
    url: '/statistics/category',
    method: 'get'
  })
}

/**
 * 获取关怀类型统计数据
 */
export function getCareTypeStatistics() {
  return request({
    url: '/statistics/care-type',
    method: 'get'
  })
}

/**
 * 获取月度关怀统计数据
 */
export function getMonthlyStatistics() {
  return request({
    url: '/statistics/monthly',
    method: 'get'
  })
} 