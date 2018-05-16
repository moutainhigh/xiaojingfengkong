webpackJsonp([6],{OSwH:function(t,e,n){var a=n("cJgl");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);n("rjj0")("af03d880",a,!0)},"R/LF":function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=n("t5DY"),s=n("vLgD");var i={name:"account-details",props:["detailId"],data:function(){return{example:"companyBill",searchParams:{"company.id":"",page:0,size:10,fields:"*,createdBy"},tableHeightTWo:500,total:0,listQuery:{page:1,sizes:[10,20,30,40],size:10},accountDetailsTable:[]}},computed:{timestampToTime:function(){return a.l}},methods:{getDataInfo:function(){var t=this;this.searchParams["company.id"]=this.detailId.split("-")[0],Object(a.d)(this.example,this.searchParams).then(function(e){t.accountDetailsTable=e.data,t.total=e.total})},handleSizeChange:function(t){this.searchParams.size=t,this.getDataInfo()},handleCurrentChange:function(t){this.searchParams.page=t-1,this.getDataInfo()},prePage:function(){this.$emit("changeFlag")},consumeType:function(t){return{PCDEPOSIT:"客户充值",BACKDEPOSIT:"后台充值",WITHDRAW:"查询扣款",CONSUME:"后台扣款"}[t]},setMoneyState:function(t,e){return"PCDEPOSIT"==e||"BACKDEPOSIT"==e?"+"+t:"-"+t}},watch:{detailId:function(t){if(""!=t){var e=this.$refs.height.offsetHeight;this.tableHeightTWo=e-63-128-30,this.getDataInfo()}}}},r={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{ref:"height",staticClass:"main"},[n("el-form",{staticClass:"demo-form-inline"},[n("el-form-item",[n("el-button",{on:{click:t.prePage}},[t._v("返回")])],1)],1),t._v(" "),n("el-table",{ref:"accountDetailsTable",staticStyle:{width:"100%"},attrs:{height:t.tableHeightTWo,border:"",data:t.accountDetailsTable,"tooltip-effect":"dark"}},[n("el-table-column",{attrs:{label:"充值 / 消费",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[n("span",[t._v(t._s(t.setMoneyState(e.row.amount,e.row.type)))])]}}])}),t._v(" "),n("el-table-column",{attrs:{label:"消费类型",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[n("span",[t._v(t._s(t.consumeType(e.row.type)))])]}}])}),t._v(" "),n("el-table-column",{attrs:{label:"操作人",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[n("span",[t._v(t._s("{}"==JSON.stringify(e.row.createdBy)?"-":e.row.createdBy.name))])]}}])}),t._v(" "),n("el-table-column",{attrs:{align:"center",label:"操作时间"},scopedSlots:t._u([{key:"default",fn:function(e){return[n("span",[t._v(t._s(t.timestampToTime(e.row.createdAt)))])]}}])}),t._v(" "),n("el-table-column",{attrs:{align:"center",label:"备注"},scopedSlots:t._u([{key:"default",fn:function(e){return[n("span",[t._v(t._s(e.row.comment?e.row.comment:"-"))])]}}])})],1),t._v(" "),n("el-pagination",{attrs:{background:"","current-page":t.listQuery.page,"page-sizes":t.listQuery.sizes,"page-size":t.listQuery.size,layout:"total, sizes, prev, pager, next, jumper",total:t.total},on:{"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}})],1)},staticRenderFns:[]};var l=n("VU/8")(i,r,!1,function(t){n("kj66")},"data-v-9e06cf2c",null).exports,o=n("E4LH"),c={name:"account",data:function(){return{example:"account",tableHeight:500,total:0,listQuery:{page:1,sizes:[10,20,30,40],size:10},searchParams:{name:"",page:0,size:10,fields:"*"},search:{name:""},accountTable:[],title:"充值",label:"充值金额",dialogData:!1,form:{id:"",payAmount:"",note:"",payType:"BACKDEPOSIT"},status:!0,detailId:"",rules:{payAmount:[{required:"true",trigger:"blur",validator:function(t,e,n){Object(o.c)(e)?n():n(new Error("只能输入大于零的数且不为空(小数点最多两位)"))}}],note:[{required:"true",trigger:"blur",message:"输入不能为空！"},{required:"true",trigger:"blur",message:"最多输入五十个字！",max:50}]}}},computed:{getButtons:function(){return a.g}},components:{accountDetails:l},mounted:function(){var t=this.$refs.height.offsetHeight;this.tableHeight=t-63-128-30,this.getData()},methods:{handleSizeChange:function(t){this.searchParams.size=t,this.getData()},handleCurrentChange:function(t){this.searchParams.page=t-1,this.getData()},getData:function(){var t=this;Object(a.p)(this.example,Object(a.o)(this.searchParams,["name"])).then(function(e){console.log(e),t.accountTable=e.data,t.total=e.total})},onSubmit:function(){this.searchParams.name=this.search.name,this.getData()},onClear:function(){this.search.name="",this.searchParams.name="",this.getData()},moneyFun:function(t,e){e?(this.title="充值",this.label="充值金额"):(this.title="扣款",this.label="扣款金额"),this.form.id=t.id,this.dialogData=!0},details:function(t){this.detailId=t.id+"-"+(new Date).valueOf(),this.status=!1},cancelCreate:function(){this.dialogData=!1,this.resetForm()},createData:function(){var t=this;this.$refs.form.validate(function(e){var n,a;e&&("充值"==t.title?(t.form.payType="BACKDEPOSIT",(n=t.example,a=t.form,Object(s.a)({url:"entity/"+n+"/pay",method:"post",data:a})).then(function(e){t.$notify({title:"成功",message:"充值成功！",type:"success"}),t.dialogData=!1,t.resetForm(),t.getData()})):(t.form.payType="CONSUME",function(t,e){return Object(s.a)({url:"entity/"+t+"/deduct",method:"post",data:e})}(t.example,t.form).then(function(e){t.$notify({title:"成功",message:"扣款成功！",type:"success"}),t.dialogData=!1,t.resetForm(),t.getData()})))})},type:function(t){return{GROUP:"集团",ENTERPRISE:"企业",TEAM:"团队",PERSON:"个人"}[t]},resetForm:function(){this.$refs.form.resetFields()},changeFlag:function(){this.status=!0},numDigit:function(t){return t.toFixed(3)}}},u={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("div",{directives:[{name:"show",rawName:"v-show",value:t.status,expression:"status"}],ref:"height",staticClass:"main"},[n("el-form",{staticClass:"demo-form-inline",attrs:{inline:!0,model:t.search}},[n("el-form-item",{attrs:{label:"机构名称"}},[n("el-input",{attrs:{placeholder:"请输入机构名称"},model:{value:t.search.name,callback:function(e){t.$set(t.search,"name",e)},expression:"search.name"}})],1),t._v(" "),n("el-form-item",[n("el-button",{attrs:{type:"primary"},on:{click:t.onSubmit}},[t._v("查询")]),t._v(" "),n("el-button",{attrs:{type:"primary"},on:{click:t.onClear}},[t._v("清除")])],1)],1),t._v(" "),n("el-table",{ref:"accountTable",staticStyle:{width:"100%"},attrs:{height:t.tableHeight,border:"",data:t.accountTable,"tooltip-effect":"dark"}},[n("el-table-column",{attrs:{label:"账号名称",align:"center",prop:"name"},scopedSlots:t._u([{key:"default",fn:function(e){return[n("span",[t._v(t._s(e.row.name))])]}}])}),t._v(" "),n("el-table-column",{attrs:{label:"账号属性",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[n("span",[t._v(t._s(t.type(e.row.accountType)))])]}}])}),t._v(" "),n("el-table-column",{attrs:{label:"历史充值金额",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[n("span",[t._v(t._s(t.numDigit(e.row.historyPay)))])]}}])}),t._v(" "),n("el-table-column",{attrs:{label:"历史赠送金额",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[n("span",[t._v(t._s(t.numDigit(e.row.historyGive)))])]}}])}),t._v(" "),n("el-table-column",{attrs:{align:"center",label:"充值账号余额"},scopedSlots:t._u([{key:"default",fn:function(e){return[n("span",[t._v(t._s(t.numDigit(e.row.payRemainder)))])]}}])}),t._v(" "),n("el-table-column",{attrs:{align:"center",label:"赠送账号余额"},scopedSlots:t._u([{key:"default",fn:function(e){return[n("span",[t._v(t._s(t.numDigit(e.row.giveRemainder)))])]}}])}),t._v(" "),n("el-table-column",{attrs:{align:"center",label:"账户总余额"},scopedSlots:t._u([{key:"default",fn:function(e){return[n("span",[t._v(t._s(t.numDigit(e.row.remainder)))])]}}])}),t._v(" "),n("el-table-column",{attrs:{align:"center",width:"200",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(e){return[t.getButtons("recharge")?n("el-button",{attrs:{size:"small",icon:"",type:"text"},on:{click:function(n){t.moneyFun(e.row,!0)}}},[t._v("充值")]):t._e(),t._v(" "),t.getButtons("withdrawing")?n("el-button",{attrs:{size:"small",icon:"",type:"text"},on:{click:function(n){t.moneyFun(e.row,!1)}}},[t._v("扣款")]):t._e(),t._v(" "),n("el-button",{attrs:{size:"small",icon:"",type:"text"},on:{click:function(n){t.details(e.row)}}},[t._v("详情")])]}}])})],1),t._v(" "),n("el-pagination",{attrs:{background:"","current-page":t.listQuery.page,"page-sizes":t.listQuery.sizes,"page-size":t.listQuery.size,layout:"total, sizes, prev, pager, next, jumper",total:t.total},on:{"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}}),t._v(" "),n("el-dialog",{attrs:{title:t.title,width:"600px",visible:t.dialogData},on:{"update:visible":function(e){t.dialogData=e}}},[n("el-form",{ref:"form",staticClass:"demo-form-inline",attrs:{rules:t.rules,model:t.form,"label-width":"120px"}},[n("el-form-item",{attrs:{label:t.label,prop:"payAmount"}},[n("el-input",{staticClass:"width",attrs:{placeholder:"请输入金额"},model:{value:t.form.payAmount,callback:function(e){t.$set(t.form,"payAmount",e)},expression:"form.payAmount"}})],1),t._v(" "),n("el-form-item",{attrs:{label:"备注",prop:"note"}},[n("el-input",{staticClass:"width",attrs:{type:"textarea",placeholder:"请输入备注"},model:{value:t.form.note,callback:function(e){t.$set(t.form,"note",e)},expression:"form.note"}})],1)],1),t._v(" "),n("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:t.cancelCreate}},[t._v("取 消")]),t._v(" "),n("el-button",{attrs:{type:"primary"},on:{click:t.createData}},[t._v("确 定")])],1)],1)],1),t._v(" "),n("accountDetails",{directives:[{name:"show",rawName:"v-show",value:!t.status,expression:"!status"}],attrs:{detailId:t.detailId},on:{changeFlag:t.changeFlag}})],1)},staticRenderFns:[]};var f=n("VU/8")(c,u,!1,function(t){n("OSwH")},"data-v-7449df64",null);e.default=f.exports},cJgl:function(t,e,n){(t.exports=n("FZ+f")(!1)).push([t.i,"\n.main .width[data-v-7449df64] {\n  width: 300px;\n}\n",""])},gjuU:function(t,e,n){(t.exports=n("FZ+f")(!1)).push([t.i,"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n",""])},kj66:function(t,e,n){var a=n("gjuU");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);n("rjj0")("1a372b4b",a,!0)}});