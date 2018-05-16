<template >
  <div id="backgounduser" class="main" ref="height">
    <div v-show="!editFlag" >
      <el-form
        :inline="true"
        :model="formInline"
        ref="formInline"
        class="demo-form-inline">
        <el-form-item
          label="产品名称"
          prop="username">
          <el-input v-model="formInline.name" placeholder="请输入接口名称"></el-input>
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
          <el-button v-if="getButtons('add')" type="success" @click="handleCreate">新增</el-button>
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
          prop="name"
          label="产品名称"
          width="300"
          sortable="custom">
        </el-table-column>
        <el-table-column
          align="center"
          prop="price"
          label="产品价格(元)"
          width="150">
        </el-table-column>
        <el-table-column
          align="center"
          prop="description"
          label="产品描述"
          width="300">
          <template slot-scope="scope">
            <el-popover
              v-if="scope.row.description.split('').length>20"
              trigger="hover"
              placement="top">
              <p style="width:300px;">{{ scope.row.description }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag  style="width:200px;white-space:nowrap;overflow: hidden;text-overflow: ellipsis;" size="medium">{{ scope.row.description }}</el-tag>
              </div>
            </el-popover>
            <span v-else>{{scope.row.description?scope.row.description:'-'}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop=""
          label="包含模块"
          width="300">
          <template slot-scope="scope" >
            <el-popover
              trigger="hover"
              placement="top"
              v-if="scope.row.moduleManagers.length!=undefined">
              <p v-for="el in scope.row.moduleManagers">{{ el.name }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.moduleManagers[0].name }}</el-tag>
              </div>
            </el-popover>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          width="200"
          label="修改时间">
          <template slot-scope="scope">
            <span>{{ scope.row.lastModifiedAt?timestampToTime(scope.row.lastModifiedAt):'-' }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop="enabled"
          label="状态"
          :width="getButtons('edit')?150:''"
          sortable="custom">
          <template slot-scope="scope">
            <span :class="{forbidden:!scope.row.enabled,normalStatus:scope.row.enabled}">{{ scope.row.enabled==true?'正常':'锁定'}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          v-if="getButtons('edit')"
          label="操作">
          <template slot-scope="scope">
            <el-button
              size="small"
              @click="productDate=scope.row;editFlag=true;"
              icon="el-icon-edit"
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
        title="新增产品"
        width="30%"
        :visible.sync="dialogVisible">
        <el-form
          :rules="createRule"
          ref="createForm"
          :model="createTemp"
          label-position="right"
          label-width="150px"
          style='max-width: 400px;' >
          <el-form-item label="产品名称" prop="name">
            <el-input v-model="createTemp.name"></el-input>
          </el-form-item>
          <el-form-item label="产品价格" prop="price" >
            <el-input v-model="createTemp.price"></el-input>
          </el-form-item>
          <el-form-item label="产品描述" prop="description">
            <el-input type="textarea" v-model="createTemp.description"></el-input>
          </el-form-item>
          <!--<el-form-item label="总次数" prop="totalCount">-->
            <!--<el-input v-model="createTemp.totalCount"></el-input>-->
          <!--</el-form-item>-->
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="resetForm('createForm')">取 消</el-button>
          <el-button type="primary" @click="createDate">确 定</el-button>
        </span>
      </el-dialog>
    </div>

    <productEdit v-show="editFlag" :productDate="productDate" v-on:changeFlag="editFlag=false;"></productEdit>
  </div>
</template>

<script>
  import { getButtons, timestampToTime,fetchList,updateList,createNow,deleteNow,enabledNow,filterForm } from '@/api/common';
  import { fetchRole,fetchDepartment } from "../../api/user";
  import productEdit from './productEdit'
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
        example: 'productionManager',

        productDate: '',
        editFlag: false,

        tableHeight: 500,
        ACTIVE: true,
        tableData: [],
        dialogVisible: false,

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
        createTemp: {
          id: '',
          name: '',
          code: '',
          price:'',
          totalCount:''
        },
        formInline: {
          page:'0',
          size:10,
          name: '',
          fields: '*,moduleManagers',
        },
        total:0,
        listQuery:{
          page:'0',
          size:10,
          fields: '*,moduleManagers'
        },
        loadingInstance:'',
        createRule: {
          name: [
            { required: true, message: '请输入产品名称', trigger: 'blur'}
          ],
          description: [
            { required: true, message: '请输入产品描述', trigger: 'blur'}
          ],
          price: [
            { required: true, validator: validatePrice, trigger: 'blur'}
          ]
        }
      }
    },
    components:{ productEdit },
    computed: {
      timestampToTime:function() {
        return timestampToTime
      },
      getButtons: function() {
        return getButtons
      }
    },
    mounted() {
      this.loadingInstance = this.$loading({ fullscreen: true });
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
      //点击新增
      handleCreate(data) {
        this.dialogVisible = true;
        this.createTemp = {};

        this.$nextTick(() => {
          this.$refs['createForm'].clearValidate();
        })
      },
      //确认新增
      createDate() {
        this.$refs['createForm'].validate( (valid) => {
          if(valid) {
            createNow(this.example,this.createTemp).then(response => {
              this.dialogVisible = false;
              this.resetForm('createForm');
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
          //关闭loading
          this.loadingInstance.close();

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
          this.formInline.name = '';
          this.formInline.enabled = '';
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
        this.getUser()
      }
    }
  }
</script>
<style scoped>
  #backgounduser{
    min-height: calc(100vh - 84px - 25px);
  }
</style>

