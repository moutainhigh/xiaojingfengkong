<template >
    <div id="backgounduser" class="main" ref="height">
      <el-form
        :inline="true"
        :model="formInline"
        ref="formInline"
        class="demo-form-inline">
        <el-form-item
          label="账号"
          prop="username">
          <el-input v-model="formInline.username" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item
          label="用户名"
          prop="name">
          <el-input v-model="formInline.name" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item
          label="用户状态"
          prop="enabled">
          <el-select
            v-model="formInline.enabled"
            placeholder="选择状态">
            <el-option label="正常" value=1></el-option>
            <el-option label="锁定" value=0></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="onSubmit('formInline')">查询</el-button>
          <el-button @click="onSubmit()">清除</el-button>
        </el-form-item>
        <el-form-item style="float:right">
          <el-button v-if="getButtons('on')" type="success" @click="handleCreate">新增</el-button>
          <el-button v-if="getButtons('delete')" type="danger" @click="deleteUser">删除</el-button>
          <el-button v-if="getButtons('on')" @click="enabledF(1)">启用</el-button>
          <el-button v-if="getButtons('off')" type="warning" @click="enabledF(0)">禁用</el-button>
        </el-form-item>
      </el-form>

      <el-table
        :data="tableData"
        :height="tableHeight"
        border
        @selection-change="handleSelectionChange"
        @sort-change="sortList" >
          <el-table-column
            align="center"
            type="selection"
            width="100">
          </el-table-column>
          <el-table-column
            align="center"
            prop="username"
            label="账号"
            width="200"
            sortable="custom">
          </el-table-column>
          <el-table-column
            align="center"
            prop="name"
            label="用户名"
            width="200">
          </el-table-column>
          <el-table-column
            align="center"
            label="角色"
            width="200">
            <template slot-scope="scope">
              <el-popover v-if="scope.row.roles.length<1" trigger="hover" placement="top">
                <p v-for="el in scope.row.roles" :key="el.id">{{ el.name }}</p>
                <div slot="reference" class="name-wrapper">
                  <el-tag size="medium">{{ scope.row.roles[0].name }}</el-tag>
                </div>
              </el-popover>
              <span v-else>{{ scope.row.roles.length==undefined?'-':scope.row.roles[0].name }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="部门"
            width="200">
            <template slot-scope="scope">
              <el-popover  v-if="scope.row.departments.length<1" trigger="hover" placement="top">
                <p v-for="el in scope.row.departments" :key="el.id">{{ el.name }}</p>
                <div slot="reference" class="name-wrapper">
                  <el-tag size="medium">{{scope.row.departments[0].name }}</el-tag>
                </div>
              </el-popover>
              <span v-else>{{ scope.row.departments.length==undefined?'-':scope.row.departments[0].name }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            prop="enabled"
            label="状态"
            width="100"
            sortable="custom">
            <template slot-scope="scope">
              <span :class="{active:!scope.row.enabled}">{{ scope.row.enabled==true?'正常':'锁定'}}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            prop="createdAt"
            label="创建时间"
            width="200"
            sortable="custom">
            <template slot-scope="scope">
              <span>{{ timestampToTime(scope.row.createdAt) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            prop="lastLoginAt"
            label="最后登录时间"
            :width="getButtons('edit')?200:''"
            sortable="custom">
            <template slot-scope="scope">
              <span>{{ scope.row.lastLoginAt?timestampToTime(scope.row.lastLoginAt):'未知' }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="getButtons('edit')"
            align="center"
            label="操作">
            <template slot-scope="scope">
              <el-button size="small" @click="handleUpdate(scope.row)" icon="el-icon-edit" type="text">编辑</el-button>
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
        title="新增后台用户"
        width="30%"
        :visible.sync="createVisible">
        <el-form
          :rules="addRule"
          ref="createForm"
          :model="createTemp"
          label-position="right"
          label-width="100px"
          style='max-width: 400px;' >
          <el-form-item label="手机号" prop="username">
            <el-input v-model="createTemp.username"></el-input>
          </el-form-item>
          <el-form-item label="用户名" prop="name" >
            <el-input v-model="createTemp.name"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="createTemp.password"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="password2">
            <el-input type="password" v-model="createTemp.password2"></el-input>
          </el-form-item>
          <el-form-item label="部门" prop="createDepartmentsId">
            <el-select v-model="createTemp.createDepartmentsId" placeholder="选择部门">
              <el-option :label="el.name" v-for="el in departsmentList" :key="el.id" :value='el.id'></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="角色" prop="createRolesId">
            <el-select v-model="createTemp.createRolesId" placeholder="选择角色">
              <el-option :label="el.name" v-for="el in rolesList" :key="el.id" :value='el.id'></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="resetForm('createForm')">取 消</el-button>
          <el-button type="primary" @click="createDate">确 定</el-button>
        </span>
      </el-dialog>

      <el-dialog
        title="编辑后台用户"
        width="30%"
        :visible.sync="updateVisible">
        <el-form
          :rules="updateRule"
          ref="updateForm"
          :model="updateTemp"
          label-position="right"
          label-width="100px">
          <el-form-item
            label="用户名"
            prop="username" >
              <el-input v-model="updateTemp.username"></el-input>
          </el-form-item>

          <el-form-item
            label="部门"
            prop="departmentsId">
            <el-select
              v-model="updateTemp.departmentsId"
              placeholder="选择部门">
              <el-option :label="el.name"
                         v-for="el in departsmentList"
                         :key="el.id"
                         :value='el.id'>
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="角色" prop="rolesId">
            <el-select v-model="updateTemp.rolesId" placeholder="选择角色">
              <el-option :label="el.name"
                         v-for="el in rolesList"
                         :key="el.id"
                         :value='el.id'>
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="resetForm('updateForm')">取 消</el-button>
          <el-button type="primary" @click="updateDate">确 定</el-button>
        </span>
      </el-dialog>
    </div>
</template>

<script>
    import { jsonFormat,vagueList,getButtons,timestampToTime,fetchList,updateList,createNow,deleteNow,enabledNow,filterForm } from '@/api/common';
    import { fetchRole,fetchDepartment } from "../../api/user";
    import { validPhone, validNum, validStr, validNUmber,validPsd } from '@/utils/validate'

    export default {
      name: "index",
      data() {
          var validatePhone = (rule, value, callback) => {
            if (value === '') {
              callback( new Error('请输入手机号'))
            } else if( validPhone(value) ) {
              callback()
            }else{
              callback( new Error('请输入正确的电话号码形式(只能为11位数字)') )
            }
          }

          var validatePass = (rule, value, callback) => {
            if ( value === '' ) {
              callback( new Error('请输入密码'))
            } else if( !validPsd(value) ) {
              callback( new Error('请输入由数字,英文字母，下划线组成的6~18密码'))
            } else if( this.createTemp.password2 != ''){
                this.$refs.createForm.validateField('password2');
                callback()
            } else{
              callback()
            }
          }

          var validatePass2 = (rule, value, callback) => {
            if (value === '') {
              callback(new Error('请再次输入密码'));
            } else if( !validPsd(value) ) {
              callback( new Error('请输入由数字,英文字母，下划线组成的6~18密码'))
            } else if (value !== this.createTemp.password) {
              callback(new Error('两次输入密码不一致!'));
            } else {
              callback()
            }
          }


          return {
            example: 'user',
            tableHeight: 500,
            ACTIVE: true,
            tableData: [],
            updateVisible: false,
            createVisible: false,

            //新增用户
            createDepartmentsId: '',
            createRolesId: '',

            departsmentList: [],
            rolesList: [],

            //状态变更
            openQuery:{
              id:'',
              enabled:''
            },
            closeQuery:{
              id:'',
              enabled:''
            },
            deleteQuery: {
              id:''
            },

            //禁用或启用
            enabledText:{
              true: '启用',
              false: '禁用'
            },

            //用于更新
            departmentsId:"",
            rolesId:'',
            updateName: '',
            updateId: '',

            //多项选择
            multipleSelection:[],

            createTemp:{
              username: '',
              name: '',
              password: '',
              password2: '',
              source: 'WEB',
              backend: true,
              roles: [],
              departments: []
            },
            createObject:{},
            updateTemp: {
              id: '',
              username: '',
              departmentsId:'',
              rolesId:''
            },
            updateObject:{},

            formInline: {
              page:'0',
              size:10,
              username: '',
              name: '',
              enabled: '',
              fields: '*,roles,departments',
              source: 'WEB',
              backend: '1'
            },
            total:0,
            listQuery:{
              page:'0',
              size:10,
              sort:'',
              fields: '*,roles,departments',
              source: 'WEB',
              backend: '1',
              id: 3
            },
            loadingInstance:'',
            addRule: {
              username: [
                { required: true, validator: validatePhone, trigger: 'blur'}
              ],
              name: [
                { required: true, message: '请输入用户名', trigger: 'blur'}
              ],
              password: [
                { required: true, validator: validatePass, trigger: 'blur'}
              ],
              password2: [
                { required: true, validator: validatePass2, trigger: 'blur'}
              ],
              createDepartmentsId: [
                { required: true, message: '请选择部门', trigger: 'blur'}
              ],
              createRolesId: [
                { required: true, message: '请选择角色', trigger: 'blur'}
              ]
            },
            updateRule: {
              username: [
                { required: true, message: '请输入用户名', trigger: 'blur'}
              ],
              departmentsId: [
                { required: true, message: '请选择部门', trigger: 'blur'}
              ],
              rolesId: [
                { required: true, message: '请选择角色', trigger: 'blur'}
              ]
            }
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
        this.getUser();
        let height = this.$refs.height.offsetHeight;
        this.tableHeight = height - 63 - 128 - 30;
      },
      methods:{
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

        //禁用  启用
        enabledF(date) {
          if( this.multipleSelection.length == 0 ) {
            return false;
          }else{
            this.multipleSelection.forEach((value,index) => {
              this.closeQuery = {
                id:value.id,
                enabled:date
              }
              enabledNow(this.example,this.closeQuery).then(response => {
                this.multipleSelection[index].enabled = date;
                if(index == (this.multipleSelection.length -1)) {
                  this.$notify({
                    title: '成功',
                    message: this.enabledText[date] + '成功',
                    type: 'success',
                    duration: 2000
                  })
                }
              })
            });
          }
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
                this.tableData.splice(index,1)
                if(index == (this.multipleSelection.length -1)) {
                  this.$notify({
                    title: '成功',
                    message: '删除成功',
                    type: 'success',
                    duration: 2000
                  })
                }
              })
            });
          }
        },
        //点击更新
        handleUpdate(data) {
          if( this.rolesList.length == 0) {
            fetchRole().then(response => {
              this.rolesList = response.data || [];
            })
            fetchDepartment().then(response => {
              this.departsmentList = response.data || [];
            })
          }
          this.updateTemp = {
            id: data.id,
            username: data.name,
            departmentsId: data.departments=='{}'?data.departments[0].id : '',
            rolesId: data.roles=='{}'?data.roles[0].id : ''
          }

          this.updateVisible = true;

          this.$nextTick(() => {
            this.$refs['updateForm'].clearValidate();
          })
        },
        //确认更新
        updateDate() {
          this.updateObject = {
            id: this.updateTemp.id,
            backend: true,
            name: this.updateTemp.username,
            roles: [
              {id: this.updateTemp.rolesId}
            ],
            departments: [{
              id: this.updateTemp.departmentsId
            }]
          };


          this.$refs['updateForm'].validate((valid) => {
            if(valid) {
              updateList(this.example,this.updateObject).then(response =>{
                this.updateVisible = false;
                this.resetForm('updateForm');
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
        //点击新增
        handleCreate(data) {
          this.createTemp.roles = [];
          this.createTemp.departments = [];
          this.createVisible = true;

          if( this.rolesList.length == 0) {
            fetchRole().then(response => {
              this.rolesList = response.data || [];
            })
            fetchDepartment().then(response => {
              this.departsmentList = response.data || [];
            })

          }
          this.$nextTick(() => {
            this.$refs['createForm'].clearValidate();
          })
        },
        //确认新增
        createDate() {
          this.createObject = {
              username: this.createTemp.username,
              name: this.createTemp.name,
              password: this.createTemp.password,
              password2: this.createTemp.password2,
              source: 'WEB',
              backend: true,
              departments:[{
              "id": this.createTemp.createDepartmentsId
            }],
            roles:[{
              "id": this.createTemp.createRolesId
            }]
          };
          this.$refs.createForm.validate( (valid) => {
            if(valid) {
              createNow(this.example,this.createObject).then(response => {
                this.createVisible = false;
                this.resetForm('createForm');
                this.getUser()
                this.$notify({
                  title: '成功',
                  message: '更新成功',
                  type: 'success',
                  duration: 2000
                })
              }).catch(err=> {
                this.createTemp.roles = [];
                this.createTemp.departments = [];
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
          vagueList( this.example,jsonFormat(this.listQuery, {id: '!'}) ).then((res) => {

            this.tableData = res.data;
            this.total = res.total;
          })
        },

        //查询
        onSubmit(formInline) {
          if(formInline) {
            this.$refs[formInline].validate((valid) => {
              if (valid) {
                fetchList( this.example,filterForm(this.formInline) ).then((res) => {
                  this.tableData = res.data;
                  this.total = res.total;
                })
              } else {
                console.log('error submit!!');
                return false;
              }
            });
          }else{
            this.formInline.username = '';
            this.formInline.enabled = '';
            this.formInline.name = '';
            fetchList( this.example ,filterForm(this.formInline)).then((res) => {
              this.tableData = res.data;
              this.total = res.total;
            })
          }
        },

        //重置
        resetForm(formInline) {
          this.updateVisible = false;
          this.createVisible = false;
          this.$refs[formInline].resetFields();
        },

        //多项选择
        handleSelectionChange(val) {
          this.multipleSelection = val;
        }
      }
    }
</script>
<style scoped>
  #backgounduser{
    min-height: calc(100vh - 84px - 25px);
  }
</style>

