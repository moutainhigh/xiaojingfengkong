import request from '../utils/request'
import store from "../store";

export function timestampToTime(timestamp) {
  var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
  var Y = date.getFullYear() + '-';
  var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) + '-' : date.getMonth()+1 + '-');
  var D = date.getDate()<10 ? '0'+date.getDate() + ' ' : date.getDate() + ' ';
  var h = date.getHours()<10 ? '0'+date.getHours() + ':' : date.getHours()+ ':';
  var m = date.getMinutes()<10 ? '0'+date.getMinutes() + ':' : date.getMinutes() + ':';
  var s = date.getSeconds()<10 ? '0'+date.getSeconds() : date.getSeconds();
  return Y+M+D+h+m+s;
}

//模糊查询list
export function vagueList(exmaple,query) {
  return request({
    url:'entity/'+exmaple+'/list?' + query,
    method:'get',
  })
}

//通用json格式化
export function jsonFormat(form,arr = {}){
  let str = '';
  let keys = Object.keys(arr);
  for(let key in form){
    if(keys.indexOf(key) === -1){
      str = str + `&${key}=${form[key]}`;
    }else{
      if(typeof arr[key] === 'object'){
        str = str + `&${key}${arr[key]['min']}=${form[key]['min']}&${key}${arr[key]['max']}=${form[key]['max']}`;
      }else{
        str = str + `&${key}${arr[key]}=${form[key]}`;
      }
    }
  }
  return str;
}

var form = {
  a: 1,
  b: 2,
  c: 3,
  d: 4,
  e:{
    min: 0,
    max: 5
  },
  g: {
    min: 0,
    max: 6
  },
  f: 7
};

var arr = {
  a: '>',
  b: '<',
  c: '!',
  d: '~',
  e: {
    min: '>',
    max: '<'
  },
  g: {
    min: '>',
    max: '<'
  }
}

//模糊查询参数格式化；
export function vagueFormat(form,arr) {
  let str = '';
  for(let key in form){
    if(arr){
      if(arr.indexOf(key) == -1){
        str = str + `&${key}=${form[key]}`;
      }else{
        str = str + `&${key}~=${form[key]}`;
      }
    }else{
      str = str + `&${key}=${form[key]}`;
    }
  }
  return str.slice(1);
}

//不等于查询参数格式化；
export function notEqualFormat(form,arr) {
  let str = '';
  for(let key in form){
    if(arr){
      if(arr.indexOf(key) == -1){
        str = str + `&${key}=${form[key]}`;
      }else{
        str = str + `&${key}!=${form[key]}`;
      }
    }else{
      str = str + `&${key}=${form[key]}`;
    }
  }
  return str.slice(1);
}

//查 list
export function fetchList(exmaple,query) {
  return request({
    url:'entity/'+exmaple+'/list',
    method:'get',
    params:query
  })
}
export function fetchTree(exmaple,query) {
  return request({
    url:'entity/'+exmaple+'/tree',
    method:'get',
    params:query
  })
}

// 更新，编辑
export function updateList(example,query) {
  return request({
    url:'entity/'+example+'/update',
    method:'post',
    data:query
  })
}

// 通用更新
export function updateData(example,query) {
  return request({
    url:'entity/'+example+'/update',
    method:'post',
    data:query
  })
}

// verify
export function verifyData(example,query) {
  return request({
    url:'entity/'+example+'/verify',
    method:'post',
    data:query
  })
}
//新增
export function createNow(example,query) {
  return request({
    url:'entity/'+example+'/create',
    method:'post',
    data:query
  })
}

//删除
export function deleteNow(example,query) {
  return request({
    url:'entity/'+example+'/delete',
    method:'post',
    data:query
  })
}

//详情
export function getDetail(example,query) {
  return request({
    url:'entity/'+example+'/detail',
    method:'get',
    params:query
  })
}


//禁用 启用
export function enabledNow(example,query) {
  return request({
    url:'entity/'+example+'/update',
    method:'post',
    data:query
  })
}


export function arrayDeleteItem(array,item) {
  var index = array.indexOf(item);
  array.splice(index,1);
  return array;
}


export function filterForm(form) {
  var obj = {};
  for(let Key in form){
    if(form[Key] != ''){
      obj[Key]=form[Key]
    }
  }
  return obj
}

// form为json对象，keys数组为需要去掉的键
export function reform(form, keys){
  let json = {};
  for(let key in form){
    if(keys.indexOf(key) == -1){
      json[key] = form[key];
    }
  }
  return json;
}

// 秒转换到天，时，分，秒；
export function timeFormat(val){
  let s = val % 60;
  let m = parseInt(val / 60) % 60;
  let h = parseInt(val / 3600) % 24;
  let day = parseInt(val / 3600 / 24);
  if(day < 1){
    if(h < 1){
      if(m < 1){
        return s + '秒';
      }
      return m + '分' + s + '秒';
    }
    return h + '时' + m + '分' + s + '秒';
  }
  return day + '天' + h + '时' + m + '分' + s + '秒';
}


//页面操作按钮是否显示
//新增 add
//启用 上架 on
//禁用 下架 off
//编辑 edit
//删除 delete
//充值 recharge
//扣款 withdrawing
//上架 grounding
//下架 undercarriage
export function getButtons(btnCode) {
  let newPath = window.location.href.split('/')[(window.location.href.split('/')).length - 1];
  let buttons = [];
  let buttonCode = [];
  let buttonFlag = false;

  store.getters.listMenus.forEach((menu, index) => {
    if( newPath == menu.path) {
      buttons = menu.buttons;
    }
  });

  if( buttons.length > 0) {
   buttons.forEach((value, index) => {
      buttonCode.push(value.code)
    })
  }

  buttonCode.forEach((value,index) => {
    if(btnCode == value){
      buttonFlag = true
    }
  });
  return buttonFlag
}

