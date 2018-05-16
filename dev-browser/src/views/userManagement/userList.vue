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
          placeholder="请选择状态">
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
      <el-form-item style="float:right;">
        <el-button  v-if="getButtons('on')" @click="enabledF(true)">启用</el-button>
        <el-button  v-if="getButtons('off')" type="warning" @click="enabledF(false)">禁用</el-button>
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
        prop="username"
        label="账号"
        width="180"
        sortable="custom">
      </el-table-column>
      <el-table-column
        align="center"
        prop="name"
        label="用户名"
        width="180">
      </el-table-column>
      <el-table-column
        align="center"
        label="所属机构"
        width="280">
        <template slot-scope="scope">
          <span>{{scope.row.company.name?scope.row.company.name:'-'}}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="顶级机构"
        width="280">
        <template slot-scope="scope">
          <span>{{scope.row.topCompany.name?scope.row.topCompany.name:'-'}}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="enabled"
        label="状态"
        width="180"
        sortable="custom">
        <template slot-scope="scope">
          <span :class="{forbidden:!scope.row.enabled,normalStatus:scope.row.enabled}">{{ scope.row.enabled==true?'正常':'锁定'}}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="reportCount"
        label="查询次数"
        sortable="custom"
        width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.reportCount?scope.row.reportCount:'0'}}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="createdAt"
        label="创建时间"
        sortable="custom">
        <template slot-scope="scope">
          <span>{{ timestampToTime(scope.row.createdAt) }}</span>
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
  </div>
</template>

<script>
  import { getButtons,timestampToTime,fetchList,updateList,createNow,deleteNow,enabledNow,filterForm } from '@/api/common';
  import { fetchRole,fetchDepartment } from "../../api/user";

  export default {
    name: "index",
    data() {
      return {
        example: 'user',
        tableHeight: 500,
        ACTIVE: true,
        tableData: [],
        //状态变更
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
        formInline: {
          page:'0',
          size:10,
          username: '',
          name: '',
          enabled: '',
          fields: '*,company,topCompany',
          source: 'WEB',
          backend: '0'
        },
        total:0,
        listQuery:{
          page:'0',
          size:10,
          sort:'',
          fields: '*,company,topCompany',
          source: 'WEB',
          backend: '0'
        },
        loadingInstance:''
      }
    },
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

