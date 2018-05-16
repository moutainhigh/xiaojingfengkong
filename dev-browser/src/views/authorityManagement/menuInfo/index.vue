<template>
  <el-container>
    <el-aside width="50%" style="align:center;">
      <el-form align="left"
               :rules="editRule"
               :model="menuDetail"
               ref="editForm"
               label-position="right"
               label-width="100px"
               style="max-width:450px;" >
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="menuDetail.name"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sortNumber" >
          <el-input v-model="menuDetail.sortNumber" ></el-input>
        </el-form-item>
        <el-form-item label="路径" prop="path" >
          <el-input v-model="menuDetail.path" ></el-input>
        </el-form-item>
        <el-form-item label="父菜单"  >
          <el-select v-model="menuDetail.parentId" placeholder="选择父菜单">
            <el-option :label="el.name"
                       v-for="el in parentMenu"
                       :value='el.id'
                       :key="el.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="图标"  >
          <el-input v-model="menuDetail.icon"></el-input>
          <!--<el-select placeholder="选择图标">-->
            <!--<el-option label="无" value=''></el-option>-->
          <!--</el-select>-->
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updateMenu" >确 定</el-button>
          <el-button @click="$emit('changeFlag')" style="margin:0 0 30px 20px;">返回</el-button>
        </el-form-item>

      </el-form>
    </el-aside>

    <el-main>
      <el-form align="left" style="margin:0 0 30px 35px;">
        <el-button @click="dialogFlag=true;isCreate=true;dialogTemp.name='';dialogTemp.code=''" type="primary" >新 增</el-button>
      </el-form>
      <el-table :data="tableData"
                max-height="620"
                border style="width: 91%;"
                @selection-change="handleSelectionChange" >
        <el-table-column
          align="center"
          prop="id"
          label="ID"
          width="55">
        </el-table-column>
        <el-table-column
          align="center"
          prop="name"
          label="控件名称"
          width="230">
        </el-table-column>
        <el-table-column
          align="center"
          label="操作">
          <template slot-scope="scope">
            <el-button size="small" icon="el-icon-edit" type="success" @click="isCreate=false;handleUpdate(scope.row);dialogFlag=true;">edit</el-button>
            <!--<el-button size="small" icon="el-icon-delete" @click="deleteUser(scope.row.id)" type="danger">delete</el-button>-->
          </template>
        </el-table-column>
      </el-table>
    </el-main>

    <el-dialog
      :title="dialogText[isCreate]"
      :visible.sync="dialogFlag"
      width="30%">
      <el-form
        :rules="dialogRule"
        ref="dialogForm"
        :model="dialogTemp"
        label-position="right"
        label-width="100px"
        style='max-width: 400px;' >
        <el-form-item
          label="控件名称"
          prop="name" >
          <el-input v-model="dialogTemp.name"></el-input>
        </el-form-item>
        <el-form-item
          label="code"
          prop="code" >
          <el-input v-model="dialogTemp.code"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="resetForm('dialogForm')">取 消</el-button>
          <el-button v-if="isCreate == true" type="primary" @click="createDate">确 定</el-button>
          <el-button v-else type="primary" @click="updateDate">确 定</el-button>
        </span>
    </el-dialog>

  </el-container>
</template>

<script>
  import { timestampToTime,fetchList,updateList,createNow,deleteNow,getDetail } from '@/api/common';
  import { fetchRole,fetchDepartment,enabledNow } from "../../../api/department";
  import { fetchParentMenu } from "../../../api/menuMannage";

  export default {
    name: "index",
    props: ['menuId'],
    data() {
      return {
        example:'button',
        example2:'menu',
        ACTIVE: true,
        tableData: [],
        parentMenu:[],
        //新增  编辑
        isCreate:'',//true 是新增，false是更新
        dialogFlag:false,

        dialogText:{
          true: '新增',
          false: '编辑'
        },
        deleteQuery: {
          id:''
        },
        dialogTemp:{
          name: '',
          code: ''
        },
        total:0,
        listQuery:{
          page:0,
          size:10,
          id:'',
          fields: '*,buttons,parent'
        },
        //菜单详情
        menuDetail:{
          name: '',
          sortNumber: '',
          path: '',
          icon: ''
        },

        loadingInstance:'',
        rules: {
          username: [
            { message: '请输入账号', trigger: 'blur'}
          ],
          name: [
            { message: '请输入姓名', trigger: 'blur'},
            { min: 2, max:4, message: '长度在2到4个字符之间', trigger: 'change'}
          ],
          status: [
            { message: '请选择状态', trigger: 'change'}
          ]
        },
        dialogRule: {
          name: [
            { required: true, message: '请输入控件名称', trigger: 'blur'}
          ],
          code: [
            { required: true, message: '请输入code', trigger: 'blur'}
          ]
        },
        editRule: {
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
    computed: {
      timestampToTime:function() {
        return timestampToTime
      }
    },
    mounted() {
      this.getUser();
      fetchParentMenu({size:1000,page:0}).then(response => {
        response.data.forEach( (value,index) => {
          if( JSON.stringify(value.parent) == '{}' ){
            this.parentMenu.push(value)
          }
        })
      })
    },
    methods:{
      //批量删除用户
      deleteUser(id){
        this.deleteQuery.id = id;
        deleteNow(this.example,this.deleteQuery).then(response => {
          this.getUser();
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
        })
      },
      //更新点击
      handleUpdate(data) {
        this.dialogTemp = {
          id: data.id,
          name: data.name,
          code: data.code
        }
        this.$nextTick(() => {
          this.$refs['dialogForm'].clearValidate();
        })
      },
      //确认更新
      updateDate() {
        this.$refs['dialogForm'].validate((valid) => {
          if(valid) {
            updateList(this.example,this.dialogTemp).then(response =>{
              this.dialogFlag = false;
              this.dialogTemp = {
                name: '',
                code: ''
              }
              this.resetForm('dialogForm');
              this.getUser()
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
            }).catch(err=>{
              console.log(err)
            })
          }
        })
      },

      //更新菜单
      updateMenu() {
        let menuObject = {
          id:this.menuId,
          name:this.menuDetail.name,
          sortNumber:this.menuDetail.sortNumber,
          path:this.menuDetail.path,
          parent:{
            "id":this.menuDetail.parentId
          },
          icon:this.menuDetail.icon
        }
        this.$refs['editForm'].validate((valid) => {
          if(valid) {
            updateList(this.example2,menuObject).then(response =>{
              //this.resetForm('editForm');
              //window.location.reload();
              this.$notify({
                title: '成功',
                message: '菜单更新成功',
                type: 'success',
                duration: 2000
              })
            }).catch(err=>{
              console.log(err)
            })
          }
        })
      },
      //确认新增
      createDate() {
        let createTemp = {
          name: this.dialogTemp.name,
          code: this.dialogTemp.code,
          menu:{
            id:this.menuId
          }
        }
        this.$refs.dialogForm.validate( (valid) =>{
          if(valid){
            createNow(this.example,createTemp).then(response => {
              this.dialogFlag = false;
              createTemp = {};
              this.resetForm('dialogForm');
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

      //获取后台用户数据
      getUser() {
        if(this.menuId==''){
          return false
        }
        this.listQuery.id = this.menuId;

        getDetail(this.example2,this.listQuery ).then((res) => {
          this.menuDetail = {
            name: res.data.name,
            sortNumber: res.data.sortNumber,
            path: res.data.path,
            parentId: res.data.parent.id,
            icon: res.data.icon
          }
          if( JSON.stringify(res.data.buttons) != '{}'){
            this.tableData = res.data.buttons;
          }else{
            this.tableData = [];
          }
          this.total = res.total;
        })
      },
      //重置
      resetForm(formInline) {
        this.dialogFlag = false;
        this.$refs[formInline].resetFields();
      },
      //多项选择
      handleSelectionChange(val) {
        this.multipleSelection = val;
      }
    },
    watch:{
      menuId(val, oldVal){
        if(val!='') {
          this.getUser();
        }
      }
    }
  }
</script>

<style scoped>
  table{
    margin-top:20px;
  }
  .el-aside {
    color: #333;
    text-align: center;
    margin-top:50px;
    margin-left:75px;
  }
  .el-main {
    color: #333;
    text-align: center;
    padding-top:50px;
  }

  label{
    text-align:right;
  }
</style>
