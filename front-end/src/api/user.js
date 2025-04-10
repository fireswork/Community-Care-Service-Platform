import request from '@/utils/request'

export function getUserList() {
  return request({
    url: '/users',
    method: 'get'
  })
}

export function createUser(data) {
  return request({
    url: '/users',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: `/users/${data.id}`,
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/users/${id}`,
    method: 'delete'
  })
}

export function resetUserPassword(id) {
  return request({
    url: `/users/${id}/reset-password`,
    method: 'post'
  })
}

export function updateUserInfo(data) {
  return request({
    url: '/users/update-info',
    method: 'put',
    data
  })
}

export function changePassword(data) {
  return request({
    url: '/users/change-password',
    method: 'put',
    data
  })
} 