import request from '@/utils/request'

export function fetchParentMenu(query) {
  return request({
    url:'entity/menu/list?fields=*,parent',
    method:'get',
    params:query
  })
}
