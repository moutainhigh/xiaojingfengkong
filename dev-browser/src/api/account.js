import request from '../utils/request'

export function accountPay(example,query) {
  return request({
    url:'entity/'+example+'/pay',
    method:'post',
    data:query
  })
}

export function deduct(example,query) {
  return request({
    url:'entity/'+example+'/deduct',
    method:'post',
    data:query
  })
}

