import request from '@/utils/request'


export function fetchRole(query) {
  return request({
    url:'entity/role/list?fields=id,name',
    method:'get'
  })
}
export function fetchDepartment(query) {
  return request({
    url:'entity/department/list?fields=id,name',
    method:'get'
  })
}



