webpackJsonp([11],{"1xRF":function(e,t,n){(e.exports=n("FZ+f")(!1)).push([e.i,"\n#backgounduser[data-v-9a7209a8]{\n  min-height: calc(100vh - 84px - 25px);\n}\n",""])},"2fS/":function(e,t,n){var a=n("1xRF");"string"==typeof a&&(a=[[e.i,a,""]]),a.locals&&(e.exports=a.locals);n("rjj0")("e86ba12a",a,!0)},aHzV:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=n("t5DY"),l=(n("vMJZ"),n("E4LH")),i={name:"index",data:function(){return{example:"supplyAPI",tableHeight:500,ACTIVE:!0,tableData:[],dialogVisible:!1,dialogFlag:!1,dialogText:{true:"新增接口",false:"编辑接口"},addBtn:!1,onBtn:!1,offBtn:!1,editBtn:!1,openQuery:{id:"",enabled:""},closeQuery:{id:"",enabled:""},enabledText:{true:"启用",false:"禁用"},multipleSelection:[],updateTemp:{id:"",name:"",code:"",price:"",totalCount:""},formInline:{page:"0",size:10,name:"",fields:"*"},total:0,listQuery:{page:"0",size:10,fields:"*"},loadingInstance:"",updateRule:{name:[{required:!0,message:"请输入接口名称",trigger:"blur"}],code:[{required:!0,message:"请输入code",trigger:"blur"}],price:[{required:!0,validator:function(e,t,n){""===t?n(new Error("请输入价格")):Object(l.e)(t)?n():n(new Error("请输入最多带有两位小数的数字"))},trigger:"blur"}],totalCount:[{required:!0,validator:function(e,t,n){""===t?n(new Error("请输入次数")):Object(l.h)(t)?n():n(new Error("请输入大于0的整数"))},trigger:"blur"}]}}},computed:{timestampToTime:function(){return a.l},getButtons:function(){return a.g}},mounted:function(){this.loadingInstance=this.$loading({fullscreen:!0}),this.getUser();var e=this.$refs.height.offsetHeight;this.tableHeight=e-63-128-30},methods:{sortList:function(e){var t="";e.order&&e.prop?(t="descending"===e.order?",desc":"ascending"===e.order?",asc":"",this.listQuery.sort=e.prop+t):this.listQuery.sort="createdAt,desc",this.getUser()},enabledF:function(e){var t=this;if(0==this.multipleSelection.length)return!1;this.multipleSelection.forEach(function(n,l){t.closeQuery={id:n.id,enabled:e},Object(a.c)(t.example,t.closeQuery).then(function(n){t.multipleSelection[l].enabled=e,l==t.multipleSelection.length-1&&t.$notify({title:"成功",message:t.enabledText[e]+"成功",type:"success",duration:2e3})})})},deleteUser:function(){var e=this;if(0==this.multipleSelection.length)return!1;this.multipleSelection.forEach(function(t,n){e.deleteQuery={id:t.id},Object(a.b)(e.example,e.deleteQuery).then(function(t){e.tableData.splice(n,1),n==e.multipleSelection.length-1&&e.$notify({title:"成功",message:"删除成功",type:"success",duration:2e3})})})},handleUpdate:function(e){var t=this;this.updateTemp={id:e.id,name:e.name,price:e.price,code:e.code,totalCount:e.totalCount},this.dialogVisible=!0,this.dialogFlag=!1,this.$nextTick(function(){t.$refs.updateForm.clearValidate()})},updateDate:function(){var e=this;this.$refs.updateForm.validate(function(t){t&&Object(a.n)(e.example,e.updateTemp).then(function(t){e.dialogVisible=!1,e.resetForm("updateForm"),e.getUser(),e.$notify({title:"成功",message:"更新成功",type:"success",duration:2e3})}).catch(function(e){console.log(e)})})},handleCreate:function(e){var t=this;this.dialogVisible=!0,this.dialogFlag=!0,this.updateTemp={},this.$nextTick(function(){t.$refs.updateForm.clearValidate()})},createDate:function(){var e=this;this.$refs.updateForm.validate(function(t){t&&Object(a.a)(e.example,e.updateTemp).then(function(t){e.dialogVisible=!1,e.resetForm("updateForm"),e.getUser(),e.$notify({title:"成功",message:"更新成功",type:"success",duration:2e3})}).catch(function(e){console.log(e)})})},handleSizeChange:function(e){this.listQuery.size=e,this.getUser()},handleCurrentChange:function(e){this.listQuery.page=e-1,this.getUser()},getUser:function(){var e=this;Object(a.d)(this.example,this.listQuery).then(function(t){e.loadingInstance.close(),e.tableData=t.data,e.total=t.total})},onSubmit:function(e){var t=this;e?this.$refs[e].validate(function(e){if(!e)return console.log("error submit!!"),!1;Object(a.d)(t.example,Object(a.f)(t.formInline)).then(function(e){t.tableData=e.data,t.total=e.total})}):(this.formInline.name="",Object(a.d)(this.example,Object(a.f)(this.formInline)).then(function(e){t.tableData=e.data,t.total=e.total}))},resetForm:function(e){this.dialogVisible=!1,this.$refs[e].resetFields()},handleSelectionChange:function(e){this.multipleSelection=e}}},o={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{ref:"height",staticClass:"main",attrs:{id:"backgounduser"}},[n("el-form",{ref:"formInline",staticClass:"demo-form-inline",attrs:{inline:!0,model:e.formInline}},[n("el-form-item",{attrs:{label:"接口名称",prop:"username"}},[n("el-input",{attrs:{placeholder:"请输入接口名称"},model:{value:e.formInline.name,callback:function(t){e.$set(e.formInline,"name",t)},expression:"formInline.name"}})],1),e._v(" "),n("el-form-item",[n("el-button",{attrs:{type:"primary"},on:{click:function(t){e.onSubmit("formInline")}}},[e._v("查询")]),e._v(" "),n("el-button",{on:{click:function(t){e.onSubmit()}}},[e._v("清除")])],1),e._v(" "),n("el-form-item",{staticStyle:{float:"right"}},[e.getButtons("add")?n("el-button",{attrs:{type:"success"},on:{click:e.handleCreate}},[e._v("新增")]):e._e(),e._v(" "),e.getButtons("on")?n("el-button",{on:{click:function(t){e.enabledF(!0)}}},[e._v("启用")]):e._e(),e._v(" "),e.getButtons("off")?n("el-button",{attrs:{type:"warning"},on:{click:function(t){e.enabledF(!1)}}},[e._v("禁用")]):e._e()],1)],1),e._v(" "),n("el-table",{attrs:{data:e.tableData,height:e.tableHeight,border:""},on:{"selection-change":e.handleSelectionChange,"sort-change":e.sortList}},[n("el-table-column",{attrs:{align:"center",type:"selection",width:"100"}}),e._v(" "),n("el-table-column",{attrs:{align:"center",prop:"name",label:"接口名称",width:"300",sortable:"custom"}}),e._v(" "),n("el-table-column",{attrs:{align:"center",prop:"price",label:"接口成本（元）",width:"200"}}),e._v(" "),n("el-table-column",{attrs:{align:"center",prop:"callCount",label:"已用次数",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v(e._s(t.row.callCount?t.row.remainderCount:"0"))])]}}])}),e._v(" "),n("el-table-column",{attrs:{align:"center",label:"剩余次数",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v(e._s(t.row.remainderCount?t.row.remainderCount:"-"))])]}}])}),e._v(" "),n("el-table-column",{attrs:{align:"center",label:"总次数",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v(e._s(t.row.totalCount?t.row.totalCount:"-"))])]}}])}),e._v(" "),n("el-table-column",{attrs:{align:"center",prop:"code",label:"code",width:"300",sortable:"custom"}}),e._v(" "),n("el-table-column",{attrs:{align:"center",label:"状态",prop:"enabled",width:e.getButtons("edit")?150:"",sortable:"custom"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",{class:{forbidden:!t.row.enabled,normalStatus:t.row.enabled}},[e._v(e._s(1==t.row.enabled?"正常":"锁定"))])]}}])}),e._v(" "),e.getButtons("edit")?n("el-table-column",{attrs:{align:"center",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[e.getButtons("edit")?n("el-button",{attrs:{size:"small",icon:"el-icon-edit",type:"text"},on:{click:function(n){e.handleUpdate(t.row)}}},[e._v("编辑")]):e._e()]}}])}):e._e()],1),e._v(" "),n("el-pagination",{attrs:{background:"","current-page":1,"page-sizes":[10,20,30,40],"page-size":10,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}}),e._v(" "),n("el-dialog",{attrs:{title:e.dialogText[e.dialogFlag],width:"30%",visible:e.dialogVisible},on:{"update:visible":function(t){e.dialogVisible=t}}},[n("el-form",{ref:"updateForm",staticStyle:{"max-width":"400px"},attrs:{rules:e.updateRule,model:e.updateTemp,"label-position":"right","label-width":"150px"}},[n("el-form-item",{attrs:{label:"接口名称",prop:"name"}},[n("el-input",{model:{value:e.updateTemp.name,callback:function(t){e.$set(e.updateTemp,"name",t)},expression:"updateTemp.name"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"code",prop:"code"}},[n("el-input",{model:{value:e.updateTemp.code,callback:function(t){e.$set(e.updateTemp,"code",t)},expression:"updateTemp.code"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"接口成本（元）",prop:"price"}},[n("el-input",{model:{value:e.updateTemp.price,callback:function(t){e.$set(e.updateTemp,"price",t)},expression:"updateTemp.price"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"总次数",prop:"totalCount"}},[n("el-input",{model:{value:e.updateTemp.totalCount,callback:function(t){e.$set(e.updateTemp,"totalCount",t)},expression:"updateTemp.totalCount"}})],1)],1),e._v(" "),n("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(t){e.resetForm("updateForm")}}},[e._v("取 消")]),e._v(" "),e.dialogFlag?n("el-button",{attrs:{type:"primary"},on:{click:e.createDate}},[e._v("确 定")]):n("el-button",{attrs:{type:"primary"},on:{click:e.updateDate}},[e._v("确 定")])],1)],1)],1)},staticRenderFns:[]};var r=n("VU/8")(i,o,!1,function(e){n("2fS/")},"data-v-9a7209a8",null);t.default=r.exports}});