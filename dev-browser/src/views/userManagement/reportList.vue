<template >
  <div id="backgounduser" class="main" ref="height">
    <el-form
      :inline="true"
      :model="formInline"
      ref="formInline"
      class="demo-form-inline">
      <el-form-item
        label="查询单号"
        prop="username">
        <el-input v-model="formInline.name" placeholder="请输入查询单号"></el-input>
      </el-form-item>
      <el-form-item
        label="查询账户"
        prop="name">
        <el-input v-model="formInline.username" placeholder="请输入查询账户"></el-input>
      </el-form-item>
      <!--<el-form-item-->
        <!--label="企业名称"-->
        <!--prop="name">-->
        <!--<el-input v-model="formInline.createdByCompany.name" placeholder="请输入企业名称"></el-input>-->
      <!--</el-form-item>-->
      <el-form-item
        label="被查询人名称"
        prop="name">
        <el-input v-model="formInline.customerName" placeholder="请输入被查询人名称"></el-input>
      </el-form-item>
      <el-form-item
        label="状态"
        prop="enabled">
        <el-select
          v-model="formInline.status"
          placeholder="选择状态">
          <el-option label="待录入" value='ENTERING'></el-option>
          <el-option label="查询中" value='PROCESSING'></el-option>
          <el-option label="查询完成" value="FINISHED"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit('formInline')">查询</el-button>
        <el-button @click="onSubmit()">清除</el-button>
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
        prop="name"
        label="报告编号"
        width="200">
      </el-table-column>
      <el-table-column
        align="center"
        prop="customerName"
        label="被查询人"
        width="150">
      </el-table-column>
      <el-table-column
        align="center"
        label="操作人"
        prop=""
        width="150">
        <template slot-scope="scope">
          {{ scope.row.createdBy.name }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="查询账户"
        prop=""
        width="200">
        <template slot-scope="scope">
          {{ scope.row.createdBy.username?scope.row.createdBy.username:'-' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop=""
        label="机构名称"
        width="100">
        <template slot-scope="scope">
          {{ scope.row.createdByCompany.name?scope.row.createdByCompany.name:'-' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="createdAt"
        label="查询关键字"
        width="300">
        <template slot-scope="scope">
          {{'--'}}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop=""
        label="产品名称"
        width="150">
        <template slot-scope="scope">
          {{scope.row.productionManager.name?scope.row.productionManager.name:'-'}}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="价格（元）"
        width="150">
        <template slot-scope="scope">
          {{scope.row.productionManager.price?scope.row.productionManager.price:'-'}}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="查询时间"
        width="250">
        <template slot-scope="scope">
          <span>{{ timestampToTime(scope.row.createdAt) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="报告生成时间"
        width="250">
        <template slot-scope="scope">
          <span>{{ timestampToTime(scope.row.finishedAt) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop=""
        label="状态">
        <template slot-scope="scope">
          <span>{{ scope.row.status=='ENTERING'?'待录入':scope.row.status=="PROCESSING"?'查询中':'查询完成' }}</span>
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
        example: 'report',
        tableHeight: 500,
        ACTIVE: true,
        tableData: [],

        formInline: {
          page:'0',
          size:10,
          username: '',
          name: '',
          status:'',
          fields: '*'
        },
        //查询账户：createdBy.username  查询机构createdByCompany.name
        total:0,
        listQuery:{
          page:'0',
          size:10,
          sort:'',
          fields: '*,createdBy,createdByCompany,productionManager'
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
          fetchList( this.example,filterForm(this.formInline) ).then((res) => {
            this.tableData = res.data;
            this.total = res.total;
          })
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

