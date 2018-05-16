import request from '@/utils/request'

export function fetchRoleList(query) {
  return request({
    url:'entity/role/list',
    method:'GET',
    params:query
  })
}

export  function createRole(param) {
  return request({
    url: 'entity/role/create',
    method:'POST',
    data:param
  })
}

export  function deleteRole(ids) {
  return request({
    url: 'batch/role/batchDelete',
    method: 'POST',
    data: {ids:ids}
  })
}

export  function updateRole(obj) {
  return request({
    url: 'entity/role/update',
    method: 'POST',
    data: obj
  })
}

export function getMenu(query) {
  return request({
    url: 'entity/menu/tree',
    method: 'GET',
    params: query
  })
}
