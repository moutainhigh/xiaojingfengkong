import request from '../utils/request'

export function enable(example,query) {
  return request({
    url:'entity/'+example+'/enable',
    method:'post',
    data:query
  })
}
