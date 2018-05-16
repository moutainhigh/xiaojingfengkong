/**
 * Created by jiachenpan on 16/11/18.
 */

export function isvalidUsername(str) {
	// const valid_map = ['admin', 'editor', 'developer']
	// return valid_map.indexOf(str.trim()) >= 0
	return true
}

/* 合法uri*/
export function validateURL(textval) {
	const urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
	return urlregex.test(textval)
}

/* 小写字母*/
export function validateLowerCase(str) {
	const reg = /^[a-z]+$/
	return reg.test(str)
}

/* 大写字母*/
export function validateUpperCase(str) {
	const reg = /^[A-Z]+$/
	return reg.test(str)
}

/* 大小写字母*/
export function validatAlphabets(str) {
	const reg = /^[A-Za-z]+$/
	return reg.test(str)
}
// 验证电话号码
export function validPhone(str){
  let reg = /^1[3|4|5|7|8][0-9]\d{8}$/;
  return reg.test(str)
}
//验证有1-2位小数的正实数
export function validNum(str){
  let reg = /^[0-9]+(.[0-9]{1,2})?$/;
  return reg.test(str);
}

export function validNUmber(str){
  let reg = /^\d+$/;
  return reg.test(str);
}

//验证由数字和26个英文字母组成的字符串
export function validStr(str){
  let reg = /^[A-Za-z0-9]+$/;
  return reg.test(str);
}

// 验证数字/26个英文字母/下划线。一般用于密码。
export function validPsd(str){
  let reg = /^\w{6,18}$/
  return reg.test(str);

}

// 验证价格 允许一到两位的小数
export function validPrice(str) {
  let reg = /^\d+(\.\d{1,2})?$/
  return reg.test(str)
}

// 验证大于0的整数
export function validTimes(str) {
  let reg = /^\+?[1-9]\d*$/
  return reg.test(str)
}
