import request from '@/utils/request'

// 获取所有分类
export function getCategories(keyword) {
  return request({
    url: '/categories',
    method: 'get',
    params: {
      keyword
    }
  })
}

// 获取单个分类
export function getCategory(id) {
  return request({
    url: `/categories/${id}`,
    method: 'get'
  })
}

// 创建分类
export function createCategory(data) {
  return request({
    url: '/categories',
    method: 'post',
    data
  })
}

// 更新分类
export function updateCategory(id, data) {
  return request({
    url: `/categories/${id}`,
    method: 'put',
    data
  })
}

// 删除分类
export function deleteCategory(id) {
  return request({
    url: `/categories/${id}`,
    method: 'delete'
  })
} 