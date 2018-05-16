<template >
  <div id="backgounduser" class="main" ref="height">
    <div v-show="!editFlag">
      <el-form
        :inline="true"
        :model="formInline"
        ref="formInline"
        class="demo-form-inline">
        <el-form-item
          label="模块名称"
          prop="username">
          <el-input
            v-model="formInline.name"
            placeholder="请输入模块名称">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="onSubmit('formInline')">查询</el-button>
          <el-button @click="onSubmit()">清除</el-button>
        </el-form-item>
        <el-form-item style="float:right">
          <el-button v-if="getButtons('add')" type="success" @click="handleCreate">新增</el-button>
          <el-button v-if="getButtons('on')" @click="enabledF(true)">启用</el-button>
          <el-button v-if="getButtons('off')" type="warning" @click="enabledF(false)">禁用</el-button>
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
          prop="name"
          label="模块名称"
          width="250"
          sortable="custom">
        </el-table-column>
        <el-table-column
          align="center"
          prop="description"
          label="模块描述"
          width="250">
          <template slot-scope="scope">
            <span v-if="scope.row.description">
              <el-popover
                v-if="scope.row.description.split('').length>20"
                trigger="hover"
                placement="top">
              <p style="width:300px;">{{ scope.row.description }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag  style="width:200px;white-space:nowrap;overflow: hidden;text-overflow: ellipsis;" size="medium">{{ scope.row.description }}</el-tag>
              </div>
            </el-popover>
              <span v-else>{{scope.row.description}}</span>
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop="price"
          label="模块成本（元）"
          width="150">
        </el-table-column>
        <el-table-column
          align="center"
          prop="code"
          label="code"
          width="200">
        </el-table-column>
        <el-table-column
          prop="licensePicURL"
          align="center"
          label="模块图片"
          width="100">
          <template slot-scope="scope">
            <el-button v-if="scope.row.files.length==undefined?false:true"
                       type="primary"
                       @click="voucherFn(scope.row.files)"
                       icon="el-icon-picture"
                       circle="true"></el-button>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="选用次数"
          width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.productionManagers.length==undefined?'0':scope.row.productionManagers.length }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="状态"
          prop="enabled"
          width="150"
          sortable="custom">
          <template slot-scope="scope">
            <span :class="{forbidden:!scope.row.enabled,normalStatus:scope.row.enabled}">{{ scope.row.enabled==true?'正常':'锁定'}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop="price"
          label="修改时间"
          :width="getButtons('edit')?200:''">
          <template slot-scope="scope">
            <span>{{ timestampToTime(scope.row.lastModifiedAt)}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          v-if="getButtons('edit')"
          label="操作">
          <template slot-scope="scope">
            <el-button
              size="small"
              icon="el-icon-edit"
              @click="editFlag=true;moduleData=scope.row"
              type="text">编辑</el-button>
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
        :visible.sync="dialogVisible">
        <el-form
          :rules="updateRule"
          ref="updateForm"
          :model="updateTemp"
          label-position="right"
          label-width="150px"
          style='max-width: 400px;' >
          <el-form-item label="模块名称" prop="name">
            <el-input v-model="updateTemp.name"></el-input>
          </el-form-item>
          <el-form-item label="code" prop="code" >
            <el-input v-model="updateTemp.code"></el-input>
          </el-form-item>
          <el-form-item label="模块成本（元）" prop="price">
            <el-input v-model="updateTemp.price"></el-input>
          </el-form-item>
          <el-form-item label="模块描述" prop="description">
            <el-input
              type="textarea"
              :rows="6"
              max-length="100"
              placeholder="最多输入100个字"
              v-model="updateTemp.description"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="resetForm('updateForm')">取 消</el-button>
          <el-button type="primary" @click="createDate">确 定</el-button>
        </span>
      </el-dialog>

      <el-dialog
        title="模块图片"
        :visible.sync="voucherShow"
        width="50%"
        center>
        <el-row>
          <el-col :span="(24 - (imgsrc.length - 1) * 2) / imgsrc.length" v-for="(src,index) in imgsrc" :key="src.id"
                  :offset="index == 0 ? 0 : 2">
            <el-card :body-style="{ padding: '0px' }">
              <img style="width: 100%;" :src="'http://' + src.url">
            </el-card>
          </el-col>
        </el-row>
        <span slot="footer" class="dialog-footer"></span>
      </el-dialog>
    </div>

    <moduleEdit v-show="editFlag" :moduleData="moduleData" v-on:changeFlag="editFlag=false" ></moduleEdit>

  </div>
</template>

<script>
  import { getButtons, timestampToTime,fetchList,updateList,createNow,deleteNow,enabledNow,filterForm } from '@/api/common';
  import { fetchRole,fetchDepartment } from "../../api/user";
  import moduleEdit from './moduleEdit'
  import { validPrice } from '@/utils/validate'

  export default {
    name: "index",
    data() {
      var validatePrice = (rule, value, callback) => {
        if(value === '') {
          callback ( new Error('请输入价格'))
        }else if( validPrice(value) ) {
          callback()
        }else{
          callback(new Error('请输入最多带有两位小数的数字'))
        }
      }
      return {
        example: 'moduleManager',
        editFlag: false,
        imgsrc: '',
        voucherShow: false,
        moduleData:'',
        tableHeight: 500,
        ACTIVE: true,
        tableData: [],
        dialogVisible: false,
        dialogFlag:false,
        dialogText:{
          true: '新增模块',
          false: '编辑模块'
        },
        //状态变更
        openQuery:{
          id:'',
          enabled:''
        },
        closeQuery:{
          id:'',
          enabled:''
        },
        //禁用或启用
        enabledText:{
          true: '启用',
          false: '禁用'
        },
        //多项选择
        multipleSelection:[],
        updateTemp: {
          description:'',
          name: '',
          code: '',
          price:'',
        },
        formInline: {
          page:'0',
          size:10,
          name: '',
          fields: '*,productionManagers,files',
        },
        total:0,
        listQuery:{
          page:'0',
          size:10,
          fields: '*,productionManagers,files'
        },
        updateRule: {
          name: [
            { required: true, message: '请输入接口名称', trigger: 'blur'}
          ],
          code: [
            { required: true, message: '请输入code', trigger: 'blur'}
          ],
          price: [
            { required: true, validator:validatePrice, trigger: 'blur'}
          ],
          totalCount: [
            { required: true, message: '请输入总次数', trigger: 'blur'}
          ]
        }
      }
    },
    components: { moduleEdit },
    computed: {
      timestampToTime:function() {
        return timestampToTime
      },
      getButtons: function() {
        return getButtons
      }
    },
    mounted() {
      this.getUser();
      let height = this.$refs.height.offsetHeight;
      this.tableHeight = height - 63 - 128 - 30;
    },
    methods:{
      voucherFn(src) {
        this.imgsrc = src;
        this.voucherShow = true;
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

      //禁用  启用
      enabledF(val) {
        if( this.multipleSelection.length == 0 ) {
          return false;
        }else{
          this.multipleSelection.forEach((value,index) => {
            this.closeQuery = {
              id:value.id,
              enabled:val
            }
            enabledNow(this.example,this.closeQuery).then(response => {
              this.multipleSelection[index].enabled = val;
              if(index == (this.multipleSelection.length -1)) {
                this.$notify({
                  title: '成功',
                  message: this.enabledText[val] + '成功',
                  type: 'success',
                  duration: 2000
                })
              }
            })
          })
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

      //点击新增
      handleCreate(data) {
        this.dialogVisible = true;
        this.dialogFlag = true;
        this.updateTemp = {};

        this.$nextTick(() => {
          this.$refs['updateForm'].clearValidate();
        })
      },
      //确认新增
      createDate() {
        this.$refs['updateForm'].validate( (valid) => {
          if(valid) {
            createNow(this.example,this.updateTemp).then(response => {
              this.dialogVisible = false;
              this.resetForm('updateForm');
              this.getUser()
              this.$notify({
                title: '成功',
                message: '新增成功',
                type: 'success',
                duration: 2000
              })
            }).catch(err=> {
              console.log(err)
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
        fetchList( this.example,this.listQuery ).then((res) => {

          this.tableData = res.data;
          this.tableData.forEach(function (val) {
            if (JSON.stringify(val.files) != "{}") {
              let files = val.files;
              for (let i = val.files.length - 1; i >= 0; i--) {
                if (val.files[i].fileSize <= 0) {
                  files.splice(i, 1);
                }
              }
              val.files = files;
            }
          })

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
          this.formInline.name = '';
          fetchList( this.example ,filterForm(this.formInline)).then((res) => {
            this.tableData = res.data;
            this.total = res.total;
          })
        }
      },

      //重置
      resetForm(formInline) {
        this.dialogVisible = false;
        this.$refs[formInline].resetFields();
      },

      //多项选择
      handleSelectionChange(val) {
        this.multipleSelection = val;
      }
    },
    watch:{
      editFlag:function(v,o) {
          this.getUser();
      }
    }
  }
</script>
<style scoped>
  #backgounduser{
    min-height: calc(100vh - 84px - 25px);
  }
</style>

