<template>
  <div id="menuManagement" class="main" ref="height">
    <div id="menuList" v-show="menuListFlag">
      <el-form
        :inline="true"
        :rules="rules"
        :model="formInline"
        ref="formInline"
        class="demo-form-inline">
        <el-form-item
          label="菜单名称"
          prop="name">
          <el-input v-model="formInline.name" placeholder="请输入菜单名称"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit('formInline')">查询</el-button>
          <el-button @click="onSubmit()">清除</el-button>
        </el-form-item>
        <el-form-item style="float:right;">
          <el-button v-if="getButtons('add')" type="success" @click="createMenu()">新增</el-button>
          <el-button v-if="getButtons('delete')" type="danger" @click="deleteUser">删除</el-button>
        </el-form-item>
      </el-form>

      <treeTable
        :height="tableHeight"
        :data="data"
        :columns="columns"
        v-on:editFlag="changeFn"
        v-on:multipleSelect="multipleSelectFn"
        border></treeTable>

      <el-dialog
        title="新增"
        width="30%"
        :visible.sync="menuVisible">
        <el-form
          :rules="createRule"
          ref="createForm"
          :model="createTemp"
          label-position="right"
          label-width="100px"
          style='max-width: 400px; ' >

          <el-form-item label="菜单名称" prop="name">
            <el-input v-model="createTemp.name"></el-input>
          </el-form-item>
          <el-form-item label="排序" prop="sortNumber" >
            <el-input v-model="createTemp.sortNumber" ></el-input>
          </el-form-item>
          <el-form-item label="路径" prop="path" >
            <el-input v-model="createTemp.path" ></el-input>
          </el-form-item>
          <el-form-item label="父菜单"   >
            <el-select v-model="createTemp.parentId" placeholder="选择父菜单">
            <el-option :label="el.name"
                       v-for="el in parentMenu"
                       :key="el.id"
                       :value='el.id'>
            </el-option>
          </el-select>
          </el-form-item>
          <!--<el-form-item label="图标"  >-->
          <!--<el-select placeholder="选择图标">-->
          <!--<el-option label="无" value=''></el-option>-->
          <!--</el-select>-->
          <!--</el-form-item>-->

        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="resetForm('createForm')">取 消</el-button>
          <el-button type="primary" @click="createDate">确 定</el-button>
        </span>
      </el-dialog>
    </div>

    <menuEdit v-show="!menuListFlag"  v-on:changeFlag="menuListFlag=true" :menuId="menuId"></menuEdit>
  </div>
</template>


<script>
  import { getButtons,timestampToTime,fetchList, fetchTree, updateList,createNow,deleteNow,filterForm } from '@/api/common';
  import { fetchRole,fetchDepartment,enabledNow } from "../../api/department";
  import { fetchParentMenu } from "../../api/menuMannage";
  import router from '@/router/index';
  import menuEdit from './menuInfo/index'

  import treeTable from './treeTable'

  export default {
    name: "index",
    data() {
      return {
        example:'menu',

        columns: [
          {
            text: '菜单名称',
            value: 'name',
            width: 200
          },
          {
            text: 'Url',
            value: 'path'
          },
          {
            text: '排序',
            value: 'sortNumber'
          },
          {
            text: '创建时间',
            value: 'createdAt'
          }
        ],
        data:[],

        menuListFlag:true,
        menuId:'',
        ACTIVE: true,
        tableData: [],
        tableHeight: 500,
        //新增  编辑
        menuVisible:false,
        dialogFlag:'',//true 是新增，false是更新
        deleteQuery: {
          id:''
        },
        //多项选择
        multipleSelection:[],
        parentMenu:[],
        createTemp: {
          name: '',
          path: '',
          sortNumber: '',
          parentId:null
        },
        createObject:{},
        formInline: {
          page:'0',
          size:100,
          name: '',
          fields: '*'
        },
        listQuery:{
          page:0,
          size:100,
          fields: '*'
        },
        loadingInstance:'',
        rules: {
          username: [
            { message: '请输入账号', trigger: 'blur'}
          ],
          name: [
            { message: '请输入姓名', trigger: 'blur'}
          ],
          status: [
            { message: '请选择状态', trigger: 'chagne'}
          ]
        },
        createRule: {
           name: [
             { required: true, message: '请输入菜单名称', trigger: 'blur'}
           ],
           sortNumber: [
             { required: true, message: '请输入排序', trigger: 'blur'}
           ],
            path: [
              { required: true, message: '请输入路径', trigger: 'blur'}
            ]
        }
      }
    },
    components:{
      menuEdit,
      treeTable
    },
    computed: {
      timestampToTime:function() {
        return timestampToTime
      },
      getButtons:function() {
        return getButtons
      }
    },
    mounted() {
      this.getUser();

      let height = this.$refs.height.offsetHeight;
      this.tableHeight = height - 63 - 128 - 30;
    },
    methods:{
      changeFn(val) {
        this.menuListFlag = false;
        this.menuId = val;
      },
      sortList(data) {
        let order = '';
        if( data.order && data.prop ) {
          order = data.order === 'descending'? ',desc':data.order === 'ascending'?',asc':'';
          this.listQuery.sort = data.prop + order;
        }else{
          this.listQuery.sort = 'createdAt,desc'
        }
        this.getUser()
      },

      //批量删除用户
      deleteUser(){
        if( this.multipleSelection.length == 0 ) {
          return false;
        }else{
          this.multipleSelection.forEach((value,index) => {
            this.deleteQuery = {
              id:value.id
            }
            deleteNow(this.example,this.deleteQuery).then(response => {
              //this.tableData.splice(index,1)
              this.getUser()
              if(index == (this.multipleSelection.length -1)) {
                this.$notify({
                  title: '成功',
                  message: '删除成功',
                  type: 'success',
                  duration: 2000
                })
              }
            })
          })
        }
      },
      createMenu(){

        this.menuVisible=true;
        this.dialogFlag=true;

        if(this.parentMenu.length == 0){
          fetchParentMenu({size:1000,page:0}).then(response => {
            response.data.forEach( (value,index) => {
              if( JSON.stringify(value.parent) == '{}' ){
                this.parentMenu.push(value)
              }
            })
          })
        }
      },
      //确认新增
      createDate() {
        if( this.createTemp.parentId ) {
          this.createObject =  {
            name: this.createTemp.name,
            path: this.createTemp.path,
            sortNumber: this.createTemp.sortNumber,
            parent:{
              "id": this.createTemp.parentId
            }
          }
        }else {
          this.createObject =  {
            name: this.createTemp.name,
            path: this.createTemp.path,
            sortNumber: this.createTemp.sortNumber
          }
        }
        this.$refs['createForm'].validate( (valid) => {
          if(valid){
            createNow(this.example,this.createObject).then(response => {
              this.menuVisible = false;
              this.resetForm('createForm');
              this.getUser()
              this.$notify({
                title: '成功',
                message: '新增成功',
                type: 'success',
                duration: 2000
              })
            })
          }
        })
      },

      //每页显示多少条变动
      handleSizeChange(val) {
        this.listQuery.size = val;
        this.getUser()
      },

      //当前页变动
      handleCurrentChange(page) {
        this.listQuery.page = page-1;
        this.getUser();
      },

      //获取菜单数据
      getUser() {
        fetchTree(this.example,this.listQuery ).then((res) => {

          this.tableData = res.data;
          this.data = res.data;
        })
      },

      //查询
      onSubmit(formInline) {
        if(formInline) {
          this.$refs[formInline].validate((valid) => {
            if (valid) {
              fetchTree( this.example,filterForm(this.formInline) ).then((res) => {
                this.tableData = res.data;
                this.data = res.data;
              })
            } else {
              console.log('error submit!!');
              return false;
            }
          });
        }else{
          this.formInline.name = '';
          fetchTree( this.example ,filterForm(this.formInline)).then((res) => {
            this.tableData = res.data;
            this.data = res.data;
          })
        }
      },

      //重置
      resetForm(formInline) {
        this.menuVisible = false;
        this.$refs[formInline].resetFields();
      },

      //多项选择
      multipleSelectFn(val) {
        this.multipleSelection = val;
      }
    },
    watch:{
      menuListFlag(newVal,oldVal){
        if(newVal == true && oldVal == false) {
          this.getUser()
        }
      }
    }
  }
</script>

<style scoped>
  table{
    margin-top:20px;
  }
</style>
