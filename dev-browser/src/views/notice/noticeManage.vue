<template>
  <div ref="height" class="main">
    <el-form :inline="true" :model="noticeManage" class="demo-form-inline">
      <el-form-item label="通知名称">
        <el-input v-model="noticeManage.name" placeholder="请输入通知名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
        <el-button type="primary" @click="onClear">清除</el-button>
      </el-form-item>
      <el-form-item class="right">
        <el-button type="success" @click="onAdd">新增</el-button>
        <el-button type="warning" @click="onRemove">删除</el-button>
      </el-form-item>
    </el-form>

    <el-table :height="tableHeight" border
              ref="noticeManageTable"
              :data="noticeManageTable"
              tooltip-effect="dark"
              style="width: 100%;"
              @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column
        label="通知名称"
        align="center"
        prop="name">
        <template slot-scope="scope">
          <span >{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="通知创建人"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.createdBy.name}}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="通知模板名称"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.noticeTemplate.name}}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="通知延时类型"
        align="center">
        <template slot-scope="scope">
          <span >{{ delayType(scope.row) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="通知角色">
        <template slot-scope="scope">
          <span>{{scope.row.roleCode}}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="该通知可取消">
        <template slot-scope="scope">
          <span>{{scope.row.canBeCancelled ? '是' : '否'}}</span>
        </template>
      </el-table-column>
      <el-table-column
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
      :current-page="listQuery.page"
      :page-sizes="listQuery.sizes"
      :page-size="listQuery.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total='total'>
    </el-pagination>


    <el-dialog
      title="公告管理"
      width="600px"
      :visible.sync="dialogAddData"
      :before-close="closeDialog">
      <el-form ref="addForm" :rules="rules" :model="addForm" class="demo-form-inline" label-width="120px">
        <el-form-item label="通知名称" prop="name">
          <el-input class="width" v-model="addForm.name" placeholder="请输入通知名称"></el-input>
        </el-form-item>
        <el-form-item label="通知延时类型" prop="delayType">
          <el-select class="width" v-model="addForm.delayType" placeholder="请选择通知延时类型">
            <el-option label="立即执行" value="IMMEDIATELY"></el-option>
            <el-option label="延时" value="DELAY"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="通知延时时间" prop="delayTime">
          <el-input class="width" v-model="addForm.delayTime" placeholder="选择延时类型必须输入。"></el-input>
        </el-form-item>
        <el-form-item label="用户角色" prop="checkList">
          <!--<el-checkbox-group v-model="addForm.checkList">
            <el-checkbox label="管理员"></el-checkbox>
            <el-checkbox label="普通用户"></el-checkbox>
          </el-checkbox-group>-->
          <el-radio-group v-model="addForm.checkList">
            <el-radio label="管理员">管理员</el-radio>
            <el-radio label="普通用户">普通用户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="模板类型" prop="industryType">
          <el-select class="width" v-model="value" @change="selectChange">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
              :disabled="item.disabled">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择模板" prop="noticeTemplate.id">
          <el-select class="width" v-model="addForm.noticeTemplate.id" filterable placeholder="请选择">
            <el-option
              v-for="item in optionVal"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择实体" prop="entityManager.id">
          <el-select class="width" v-model="addForm.entityManager.id" filterable placeholder="请选择" @change="selectEntityManager">
            <el-option
              v-for="item in entityManagerVal"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择行为" prop="act.id">
          <el-select class="width" v-model="addForm.act.id" filterable placeholder="请选择">
            <el-option
              v-for="item in actVal"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="cancelCreate">取 消</el-button>
          <el-button type="primary" @click="createData">确 定</el-button>
        </span>
    </el-dialog>

  </div>
</template>

<script>
  import {
    getButtons,
    timestampToTime,
    vagueList,
    vagueFormat,
    fetchList,
    createNow,
    deleteNow,
    getDetail,
    timeFormat,
    reform
  } from '@/api/common';
  import {validNUmber} from '@/utils/validate'
  export default {
    name: "notice-manage",
    data(){
      let delayTimeValid = (rule, value, callback) => {
        if(this.addForm.delayType == "DELAY"){
          if(validNUmber(value)){
            callback()
          }else{
            callback(new Error("只能输入正整数且不能为空！"));
          }
        }else {
          this.addForm.delayTime = 0;
          callback();
        }
      };
      return {
        example: 'actNotice',
        tableHeight: 500,
        noticeManage: {
          name: ''
        },
        noticeManageTable: [],
        total: 0,
        listQuery: {
          page: 1,
          sizes: [10, 20, 30, 40],
          size: 10,
        },
        searchParams: {
          name: '',
          page: 0,
          size: 10,
          fields: '*,act,createdBy,entityManager,noticeTemplate'
        },
        dialogAddData: false,
        addForm: {
          name: '',
          delayType: '',
          delayTime: 0,
          checkList: '',
          roleCode: '',
          noticeTemplate: {
            id: ''
          },
          entityManager: {
            id: ''
          },
          act: {
            id: ''
          },
          canBeCancelled: 0,
        },
        multipleSelection: [],
        options: [
          {
            value: 'SMS',
            label: '阿里云'
          }, {
            value: 'MESSAGE',
            label: '站内信'
          }, {
            value: 'WXWORK',
            label: '企业微信'
          }, {
            value: 'WXPUBLIC',
            label: '微信公众号'
          }, {
            value: 'EMAIL',
            label: '邮箱',
            disabled: true
          },
          {
            value: 'APP',
            label: '移动端',
            disabled: true
          }
        ],
        value: "SMS",
        optionVal: [],
        entityManagerVal: [],
        actVal: [],
        rules: {
          name: [{required: true, message: '请输入通知名称', trigger: 'blur'}],
          delayType: [{required: true, message: '请选择通知类型', trigger: 'change'}],
          delayTime: [{required: true, trigger: 'blur', validator: delayTimeValid}],
          checkList: [{required: true, message: '请勾选用户角色', trigger: "change"}],
          noticeTemplate: {
            id: [{required: true, message: '请选择模板', trigger: 'change'}]
          },
          entityManager: {
            id: [{required: true, message: '请选择实体', trigger: 'change'}]
          },
          act: {
            id: [{required: true, message: '请选择行为', trigger: 'change'}]
          }
        }
      }
    },
    computed: {
      timestampToTime: function () {
        return timestampToTime
      },
      getButtons: function () {
        return getButtons
      }
    },
    mounted() {
      let height = this.$refs.height.offsetHeight;
      this.tableHeight = height - 63 - 128 - 30;
      this.getNoticeManageData();
      this.getEntityManager();
      this.selectChange();
    },
    methods: {
      onSubmit(){
        this.searchParams.name = this.noticeManage.name;
        this.getNoticeManageData();
      },
      onClear(){
        this.noticeManage.name = '';
        this.searchParams.name = this.noticeManage.name;
        this.getNoticeManageData();
      },
      onAdd(){
        this.dialogAddData = true;
      },
      getNoticeManageData(){
        vagueList(this.example, vagueFormat(this.searchParams, ['name'])).then( res => {
          console.log(res);
          this.noticeManageTable = res.data;
          this.total = res.total;
        })
      },
      handleSizeChange(val) {
        this.searchParams.size = val;
        this.getNoticeManageData();
      },
      handleCurrentChange(val) {
        this.searchParams.page = val - 1;
        this.getNoticeManageData();
      },
      closeDialog(done){
        this.$confirm('此操作将关闭表单和清空表单的数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          done();
          this.resetForm();
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消该操作'
          });
        });
      },
      cancelCreate(){
        this.$confirm('此操作将关闭表单和清空表单的数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.dialogAddData = false;
          this.resetForm();
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消该操作'
          });
        });
      },
      createData(){
        this.addForm.roleCode = this.addForm.checkList;
        this.$refs["addForm"].validate(valid => {
          if (valid) {
            createNow(this.example,reform(this.addForm, ["checkList"])).then( res => {
              this.getNoticeManageData();
              this.resetForm();
              this.$notify({
                title: '成功',
                message: '新增通知成功！',
                type: 'success',
                duration: 2000
              })
            });
            this.dialogAddData = false;
          } else {
            this.$notify({
              title: '警告',
              message: '数据填写出错。',
              type: 'warning'
            });
          }
        });
      },
      resetForm(){
        this.$refs['addForm'].resetFields();
      },
      delayType(val){
        if(val.delayType === 'IMMEDIATELY'){
          return "立即执行";
        }
        if(val.delayType === 'DELAY'){
          return "延时" + timeFormat(val.delayTime);
        }
      },
      handleSelectionChange(selection) {
        this.multipleSelection = selection;
      },
      onRemove(){
        this.multipleSelection.forEach((val, index) => {
          deleteNow(this.example, {id: val.id}).then(res => {
            if (index == (this.multipleSelection.length - 1)) {
              this.getNoticeManageData();
              this.$notify({
                title: '成功',
                message: '通知删除成功',
                type: 'success',
                duration: 2000
              })
            }
          })
        })
      },
      selectChange(){
        fetchList("noticeTemplate",{fields: "*", noticeChannel: this.value}).then( res => {
          this.optionVal = res.data;
        })
      },
      getEntityManager(){
        fetchList("entityManager",{fields: "*", managerGroup: 'ADMIN'}).then( res => {
          this.entityManagerVal = res.data;
        })
      },
      selectEntityManager(val){
        fetchList("act",{fields: "*", "entityManager.id": val}).then( res => {
          this.actVal = res.data;
        })
      },
      handleUpdate(val){
        console.log(val);
        this.value = val.noticeTemplate.noticeChannel;
        this.addForm.name = val.name;
        this.addForm.delayType = val.delayType;
        this.addForm.delayTime = val.delayTime;
        this.addForm.checkList = val.roleCode;
        this.addForm.noticeTemplate.id = val.noticeTemplate.id;
        this.addForm.act.id = val.act.id;
        getDetail('act',{fields: '*,entityManager', id: val.act.id}).then( res =>{
          console.log(res);
          this.addForm.entityManager.id = res.data.entityManager.id;
          this.selectEntityManager(res.data.entityManager.id);
        });
        this.selectChange();
        this.dialogAddData = true;
      }
    },
    watch: {
      addForm: {
        handler(val, oldVal){
          if(val.delayType == "IMMEDIATELY"){
            this.addForm.canBeCancelled = 0;
          }else{
            this.addForm.canBeCancelled = 1;
          }
        },
        deep:true
      }
    }
  }
</script>

<style type="stylesheet/scss" lang="scss" scoped>
  .main {
    .right {
      float: right;
      margin-right: 20px;
    }

    .right:after {
      clear: both;
    }

    .width {
      width: 300px
    }
  }
</style>
