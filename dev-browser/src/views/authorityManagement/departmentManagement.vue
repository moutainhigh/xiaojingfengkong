<template >
  <div id="backgounduser" class="main" ref="height">
    <el-form
      :inline="true"
      :rules="rules"
      :model="formInline"
      ref="formInline"
      class="demo-form-inline">
      <el-form-item label="部门名称" prop="name">
        <el-input v-model="formInline.name" placeholder="请输入部门名称"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button  type="primary" @click="onSubmit('formInline')">查询</el-button>
        <el-button @click="onSubmit()">清除</el-button>
      </el-form-item>
      <el-form-item style="float:right;">
        <el-button v-if="getButtons('add')" type="success" @click="addNow('dialogForm');departmentsVisible=true;dialogFlag=true;">新增</el-button>
        <el-button v-if="getButtons('delete')" type="danger" @click="deleteUser">删除</el-button>
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
        width="55">
      </el-table-column>
      <el-table-column
        align="center"
        prop="name"
        label="部门名称"
        width="280">
      </el-table-column>
      <el-table-column
        align="center"
        prop="name"
        label="部门成员数量"
        width="280">
        <template slot-scope="scope" >
          <span>{{ scope.row.employees.length!=undefined?scope.row.employees.length:'0' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="note"
        label="备注"
        width="380">
        <template slot-scope="scope">
          <el-popover
            v-if="scope.row.note.split('').length>20"
            trigger="hover"
            placement="top">
            <p style="width:300px;">{{ scope.row.note }}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag  style="width:250px;white-space:nowrap;overflow: hidden;text-overflow: ellipsis;" size="medium">{{ scope.row.note }}</el-tag>
            </div>
          </el-popover>
          <span v-else>{{scope.row.note?scope.row.note:'-'}}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="创建时间"
        prop="createdAt"
        sortable="custom"
        :width="getButtons('edit')?200:''">
        <template slot-scope="scope">
          <span>{{ timestampToTime(scope.row.createdAt) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        v-if="getButtons('edit')"
        label="操作">
        <template slot-scope="scope">
          <el-button
            size="small"
            @click="handleUpdate(scope.row);departmentsVisible=true;dialogFlag=false"
            icon="el-icon-edit"
            type="text">编辑
          </el-button>
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
      :title="dialogText[dialogFlag]"
      width="30%"
      :visible.sync="departmentsVisible">
      <el-form :rules="dialogRule"
               ref="dialogForm"
               :model="dialogForm"
               label-position="right"
               label-width="100px"
               style='max-width: 400px;' >
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="dialogForm.name"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="note" >
          <el-input
            type="textarea"
            max-length="100"
            :rows="5"
            placeholder="最多输入100个字"
            v-model="dialogForm.note"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="resetForm('dialogForm')">取 消</el-button>
          <el-button v-if="dialogFlag == true" type="primary" @click="createDate">确 定</el-button>
          <el-button v-else type="primary" @click="updateDate">确 定</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
  import { getButtons,timestampToTime,fetchList,updateList,createNow,deleteNow,filterForm } from '@/api/common';
  import { fetchRole,fetchDepartment,enabledNow } from "../../api/department";

  export default {
    name: "index",
    data() {
      return {
        example:'department',
        ACTIVE: true,
        tableData: [],
        departmentsVisible:false,
        dialogFlag:'',//true 是新增，false是更新
        dialogText:{
          true: '新增部门',
          false: '编辑部门'
        },

        tableHeight: 500,
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
        //多项选择
        multipleSelection:[],

        dialogForm:{
          note: '',
          name: '',
        },
        formInline: {
          page:'0',
          size:10,
          name: '',
          fields: '*,employees'
        },
        total:0,
        listQuery:{
          page:0,
          size:10,
          fields: '*,employees'
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
        dialogRule: {
          name: [
            { required: true, message: '请输入部门名称', trigger: 'blur'}
          ],
          note: [
            { required: true, message: '请输入备注', trigger: 'blur'}
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
      addNow() {
        this.dialogForm={
          note: '',
          name: ''
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
          })
        }
      },
      //更新点击
      handleUpdate(data) {
        this.dialogForm = {
          id: data.id,
          name: data.name,
          note: data.note
        }
        this.$nextTick(() => {
          this.$refs['dialogForm'].clearValidate();
        })
      },
      //确认更新
      updateDate() {
        this.$refs['dialogForm'].validate((valid) => {
          if(valid) {
            updateList(this.example,this.dialogForm).then(response =>{
              this.departmentsVisible = false;
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
      //确认新增
      createDate() {
       if(this.dialogForm.id) {
         delete this.dialogForm.id
       }
        this.$refs.dialogForm.validate( (valid) =>{
          if(valid){
            createNow(this.example,this.dialogForm).then(response => {
              this.departmentsVisible = false;
              this.resetForm('dialogForm');
              this.getUser()
              this.$notify({
                title: '成功',
                message: '新建成功',
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
        fetchList(this.example,this.listQuery ).then((res) => {

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
          this.formInline.id = '';
          this.formInline.name = '';
          fetchList( this.example ,filterForm(this.formInline)).then((res) => {
            this.tableData = res.data;
            this.total = res.total;
          })
        }
      },
      //重置
      resetForm(formInline) {
        this.departmentsVisible = false;
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
  table{
    margin-top:20px;
  }
</style>
