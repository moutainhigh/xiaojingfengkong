<template>
  <div>
    <div class="main" ref="height" v-if="!editShow">
      <div id="backgounduser" >
        <el-form :inline="true" :model="listQuery" ref="listQuery" class="demo-form-inline">
          <el-form-item label="角色名称" prop="name">
            <el-input v-model="listQuery.name" placeholder="请输入角色名称"></el-input>
          </el-form-item>
          <el-form-item label="角色标识" prop="name">
            <el-input v-model="listQuery.code" placeholder="请输入角色标识"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="getRole">查询</el-button>
            <el-button @click="resetForm(listQuery)">清除</el-button>
          </el-form-item>
          <el-form-item style="float:right;">
            <el-button  v-if="getButtons('add')" type="success"  @click="handleCreate('新增角色')">新增</el-button>
            <el-button  v-if="getButtons('delete')" type="danger" @click="handleDelete">删除</el-button>
          </el-form-item>
        </el-form>

        <el-table
          :data="tableData"
          border
          :height="tableHeight"
          @selection-change="handleSelectionChange">
          <el-table-column align="center" type="selection" width="55">
          </el-table-column>
          <el-table-column align="center" prop="name" label="角色名称">

          </el-table-column>

          <el-table-column align="center" prop="code" label="角色标识">

          </el-table-column>

          <el-table-column align="center" label="授权用户">
            <template slot-scope="scope">
              <span>{{ scope.row.users.length>0? scope.row.users.length : '0'}}</span>
            </template>
          </el-table-column>

          <el-table-column align="center" prop="note" label="备注">

          </el-table-column>

          <el-table-column
            align="center"
            label="创建时间"
            :width="getButtons('edit')?200:''">
            <template slot-scope="scope">
              <span>{{ timestampToTime(scope.row.createdAt) }}</span>
            </template>
          </el-table-column>

          <el-table-column
            align="center"
            label="操作">
            <template slot-scope="scope">
              <el-button v-if="getButtons('edit')" size="small" @click="editShow=true;roleID=scope.row.id" icon="el-icon-edit" type="text">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="1"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="10"
          layout="total, sizes, prev, pager, next, jumper"
          :total='total'>
        </el-pagination>

        <el-dialog
          :title="dialogTitle"
          :visible.sync="dialogVisible"
          width="30%">
          <el-form
            ref="dataForm"
            :rules="rules"
            :model="temp"
            label-position="right"
            label-width="100px"
            style='max-width: 400px'>
            <el-form-item label="角色名称" prop="name" >
              <el-input v-model="temp.name"></el-input>
            </el-form-item>
            <el-form-item label="角色标识" prop="code" >
              <el-input v-model="temp.code"></el-input>
            </el-form-item>
            <el-form-item label="备注" prop="note" >
              <el-input v-model="temp.note"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="createDate('dataForm')">确 定</el-button>
          </span>
        </el-dialog>
      </div>
    </div>
    <roleEdit :id="roleID" :editShow="editShow" v-show="editShow" v-on:changeShow="editShow=false" v-cloak></roleEdit>
  </div>

</template>

<script>
  import { getButtons,timestampToTime } from '@/api/common';
  import {getItemsId} from '@/utils/getItemsId';
  import { fetchRoleList, createRole, deleteRole, updateRole } from "../../api/role";
  import router from '@/router/index';
  import roleEdit from './roleEdit'
  import store from '../../store'

  export default {
      name: "role-management",
      data(){
        return {
          tableHeight: 500,
          tableData: [],
          dialogVisible: false,
          listQuery: {
            page:0,
            size:10,
            name: null,
            code: null,
            fields: '*,users,note'
          },
          total: 0,
          temp:{},
          rules:{
            name:[
              {required: true, message: '请输入角色名称', trigger: 'blur'},
            ],
            code:[
              {required: true, message: '请输入角色标识', trigger: 'blur'},
            ]
          },
          dialogTitle: '',
          //选中行
          multipleSelection:[],
          roleID:"",
          editShow:false
        }
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
        this.getRole();
        let height = this.$refs.height.offsetHeight;
        this.tableHeight = height - 63 - 128 - 30;
      },
      components:{
        roleEdit
      },
      methods:{
        getRole(){
          fetchRoleList( this.listQuery ).then((res) => {
            this.tableData = []
            let i = 0;
            let userFlag = false;
            this.$store.getters.roles.forEach( (key) => {
              if( key.code == 'DEVELOPER') {
                userFlag = true;
                return false
              }
            })

            if(userFlag) {
              this.tableData = res.data;
              this.total = res.total;
            }else {
              res.data.forEach((key) => {
                console.log(key.code)
                if(key.code != 'DEVELOPER' && key.code != 'GUEST' && key.code != 'ANONYMOUS') {
                  this.tableData.push(key)
                  i++;
                }
              })
              this.total = i;
            }
          })
        },
        resetForm(obj){
          obj.name = null;
          obj.code = null;
          this.getRole();
        },
        //每页显示多少条变动
        handleSizeChange(val) {
          this.listQuery.size = val;
          this.getRole()
        },

        //当前页变动
        handleCurrentChange(page) {
          this.listQuery.page = page-1;
          this.getRole();
        },
        handleDelete(){
          if(this.multipleSelection.length>0){

              const ids = getItemsId(this.multipleSelection);
              deleteRole(ids).then((res)=>{
                if(res.errorCode === 0){
                  this.$notify.success({
                    title: '消息',
                    message: '删除成功',
                    showClose: false,
                    duration: 2000
                  });
                  this.getRole();
                }else{
                  this.$notify.error({
                    title: '消息',
                    message: res.errorMessage,
                    showClose: false,
                    duration: 2000
                  });
                }
              }).catch(() => {
              this.$message({
                type: '消息',
                message: '已取消删除',
                duration: 2000
              });
            });
          }else{
            this.$notify.error({
              title: '消息',
              message: '请选择需要删除的角色',
              duration: 2000,
              showClose: false
            });
          }
        },
        handleCreate(title){
          this.dialogTitle = title;
          this.dialogVisible = true;
        },
        handleSelectionChange(val) {
          this.multipleSelection = val;
        },
        createDate(formName) {
          this.$refs[formName].validate((valid) => {
            if (valid) {
              if(this.dialogTitle === '新增角色') {
                createRole(this.temp).then((res) => {
                  if(res.errorCode === 0){
                    this.dialogVisible = false;
                    this.temp = {};
                    this.$notify.success({
                      title: '消息',
                      message: '新增成功',
                      showClose: false
                    });
                    this.getRole();
                  }
                }).catch(err => {
                  console.log(err)
                })
              }else if(this.dialogTitle === '编辑角色') {
                updateRole(this.temp).then((res) => {
                  if(res.errorCode === 0){
                    this.dialogVisible = false;
                    this.temp = {};
                    this.$notify.success({
                      title: '消息',
                      message: '编辑成功',
                      showClose: false
                    });
                    this.getRole();
                  }
                }).catch( err => {
                  console.log(err)
                })
              }
            }
          });
        }
      },
      watch:{
        dialogVisible(currVal,oldVal){
          if(currVal !== oldVal){
            this.$refs.dataForm.resetFields();
          }
        },
        editShow(currVal,oldVal){
          if(!currVal){
            store.dispatch('GetInfo').then(res => { // 拉取用户信息
              window.myinfo = res.data
            })
            this.getRole();
          }
        }
      }
    }
</script>

<style scoped>
  [v-cloak] {
    display: none;
  }
</style>
