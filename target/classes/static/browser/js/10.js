webpackJsonp([10],{"9AlZ":function(e,t,n){var a=n("uoS3");"string"==typeof a&&(a=[[e.i,a,""]]),a.locals&&(e.exports=a.locals);n("rjj0")("7a6e22ed",a,!0)},rS5m:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=n("t5DY"),l=(n("vMJZ"),{name:"index",data:function(){return{example:"report",tableHeight:500,ACTIVE:!0,tableData:[],formInline:{page:"0",size:10,username:"",name:"",status:"",fields:"*"},total:0,listQuery:{page:"0",size:10,sort:"",fields:"*,createdBy,createdByCompany,productionManager"},loadingInstance:""}},computed:{timestampToTime:function(){return a.l},getButtons:function(){return a.g}},mounted:function(){this.loadingInstance=this.$loading({fullscreen:!0}),this.getUser();var e=this.$refs.height.offsetHeight;this.tableHeight=e-63-128-30},methods:{sortList:function(e){var t="";e.order&&e.prop?(t="descending"===e.order?",desc":"ascending"===e.order?",asc":"",this.listQuery.sort=e.prop+t):this.listQuery.sort="createdAt,desc",this.getUser()},handleSizeChange:function(e){this.listQuery.size=e,this.getUser()},handleCurrentChange:function(e){this.listQuery.page=e-1,this.getUser()},getUser:function(){var e=this;Object(a.d)(this.example,this.listQuery).then(function(t){e.loadingInstance.close(),e.tableData=t.data,e.total=t.total})},onSubmit:function(e){var t=this;e?Object(a.d)(this.example,Object(a.f)(this.formInline)).then(function(e){t.tableData=e.data,t.total=e.total}):(this.formInline.username="",this.formInline.enabled="",this.formInline.name="",Object(a.d)(this.example,Object(a.f)(this.formInline)).then(function(e){t.tableData=e.data,t.total=e.total}))},resetForm:function(e){this.updateVisible=!1,this.createVisible=!1,this.$refs[e].resetFields()},handleSelectionChange:function(e){this.multipleSelection=e}}}),r={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{ref:"height",staticClass:"main",attrs:{id:"backgounduser"}},[n("el-form",{ref:"formInline",staticClass:"demo-form-inline",attrs:{inline:!0,model:e.formInline}},[n("el-form-item",{attrs:{label:"查询单号",prop:"username"}},[n("el-input",{attrs:{placeholder:"请输入查询单号"},model:{value:e.formInline.name,callback:function(t){e.$set(e.formInline,"name",t)},expression:"formInline.name"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"查询账户",prop:"name"}},[n("el-input",{attrs:{placeholder:"请输入查询账户"},model:{value:e.formInline.username,callback:function(t){e.$set(e.formInline,"username",t)},expression:"formInline.username"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"被查询人名称",prop:"name"}},[n("el-input",{attrs:{placeholder:"请输入被查询人名称"},model:{value:e.formInline.customerName,callback:function(t){e.$set(e.formInline,"customerName",t)},expression:"formInline.customerName"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"状态",prop:"enabled"}},[n("el-select",{attrs:{placeholder:"选择状态"},model:{value:e.formInline.status,callback:function(t){e.$set(e.formInline,"status",t)},expression:"formInline.status"}},[n("el-option",{attrs:{label:"待录入",value:"ENTERING"}}),e._v(" "),n("el-option",{attrs:{label:"查询中",value:"PROCESSING"}}),e._v(" "),n("el-option",{attrs:{label:"查询完成",value:"FINISHED"}})],1)],1),e._v(" "),n("el-form-item",[n("el-button",{attrs:{type:"primary"},on:{click:function(t){e.onSubmit("formInline")}}},[e._v("查询")]),e._v(" "),n("el-button",{on:{click:function(t){e.onSubmit()}}},[e._v("清除")])],1)],1),e._v(" "),n("el-table",{attrs:{data:e.tableData,height:e.tableHeight,border:""},on:{"selection-change":e.handleSelectionChange,"sort-change":e.sortList}},[n("el-table-column",{attrs:{align:"center",prop:"name",label:"报告编号",width:"200"}}),e._v(" "),n("el-table-column",{attrs:{align:"center",prop:"customerName",label:"被查询人",width:"150"}}),e._v(" "),n("el-table-column",{attrs:{align:"center",label:"操作人",prop:"",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.createdBy.name)+"\n      ")]}}])}),e._v(" "),n("el-table-column",{attrs:{align:"center",label:"查询账户",prop:"",width:"200"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.createdBy.username?t.row.createdBy.username:"-")+"\n      ")]}}])}),e._v(" "),n("el-table-column",{attrs:{align:"center",prop:"",label:"机构名称",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.createdByCompany.name?t.row.createdByCompany.name:"-")+"\n      ")]}}])}),e._v(" "),n("el-table-column",{attrs:{align:"center",prop:"createdAt",label:"查询关键字",width:"300"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s("--")+"\n      ")]}}])}),e._v(" "),n("el-table-column",{attrs:{align:"center",prop:"",label:"产品名称",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.productionManager.name?t.row.productionManager.name:"-")+"\n      ")]}}])}),e._v(" "),n("el-table-column",{attrs:{align:"center",label:"价格（元）",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.productionManager.price?t.row.productionManager.price:"-")+"\n      ")]}}])}),e._v(" "),n("el-table-column",{attrs:{align:"center",label:"查询时间",width:"250"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v(e._s(e.timestampToTime(t.row.createdAt)))])]}}])}),e._v(" "),n("el-table-column",{attrs:{align:"center",label:"报告生成时间",width:"250"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v(e._s(e.timestampToTime(t.row.finishedAt)))])]}}])}),e._v(" "),n("el-table-column",{attrs:{align:"center",prop:"",label:"状态"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v(e._s("ENTERING"==t.row.status?"待录入":"PROCESSING"==t.row.status?"查询中":"查询完成"))])]}}])})],1),e._v(" "),n("el-pagination",{attrs:{background:"","current-page":1,"page-sizes":[10,20,30,40],"page-size":10,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1)},staticRenderFns:[]};var o=n("VU/8")(l,r,!1,function(e){n("9AlZ")},"data-v-c0a48816",null);t.default=o.exports},uoS3:function(e,t,n){(e.exports=n("FZ+f")(!1)).push([e.i,"\n#backgounduser[data-v-c0a48816]{\n  min-height: calc(100vh - 84px - 25px);\n}\n",""])}});