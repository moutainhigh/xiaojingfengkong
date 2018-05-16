import request from '@/utils/request'

export function update(query) {
  return request({
    url:'entity/department/update',
    method:'post',
    data:query
  })
}

export function fetchRole(query) {
  return request({
    url:'entity/department/list?fields=id,name',
    method:'get'
  })
}
export function fetchDepartment(query) {
  return request({
    url:'entity/department/list?fields=id,name',
    method:'get'
  })
}

export function createUser(query) {
  return request({
    url:'entity/department/create',
    method:'post',
    data:query
  })
}

export function enabledNow(query) {
  return request({
    url:'entity/department/update',
    method:'post',
    data:query
  })
}

export function deleteNow(query) {
  return request({
    url:'entity/department/delete',
    method:'post',
    data:query
  })
}


