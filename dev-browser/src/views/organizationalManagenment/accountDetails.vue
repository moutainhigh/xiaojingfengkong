<template>
  <div ref="height" class="main">
    <el-form class="demo-form-inline">
      <el-form-item>
        <el-button @click="prePage">返回</el-button>
      </el-form-item>
    </el-form>
    <el-table :height="tableHeightTWo" border
              ref="accountDetailsTable"
              :data="accountDetailsTable"
              tooltip-effect="dark"
              style="width: 100%;">
      <el-table-column
        label="充值 / 消费"
        align="center">
        <template slot-scope="scope">
          <span>{{setMoneyState(scope.row.amount, scope.row.type)}}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="消费类型"
        align="center">
        <template slot-scope="scope">
          <span>{{consumeType(scope.row.type)}}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作人"
        align="center">
        <template slot-scope="scope">
          <span>{{JSON.stringify(scope.row.createdBy) == '{}' ? '-' : scope.row.createdBy.name}}</span>
        </template>
      </el-table-column>
      <!--<el-table-column
        label="账号类型"
        align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.historyGive}}</span>
        </template>
      </el-table-column>-->
      <el-table-column
        align="center"
        label="操作时间">
        <template slot-scope="scope">
          <span>{{timestampToTime(scope.row.createdAt)}}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="备注">
        <template slot-scope="scope">
          <span>{{scope.row.comment ? scope.row.comment : "-"}}</span>
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
  </div>
</template>

<script>
  import {
    timestampToTime, fetchList, filterForm, getDetail
  } from '@/api/common';

  export default {
    name: "account-details",
    props: ['detailId'],
    data() {
      return {
        example: "companyBill",
        searchParams: {
          'company.id': "",
          page: 0,
          size: 10,
          fields: '*,createdBy'
        },
        tableHeightTWo: 500,
        total: 0,
        listQuery: {
          page: 1,
          sizes: [10, 20, 30, 40],
          size: 10,
        },
        accountDetailsTable: []
      }
    },
    computed: {
      timestampToTime: function () {
        return timestampToTime
      }
    },
    methods: {
      getDataInfo() {
        this.searchParams['company.id'] = this.detailId.split('-')[0];
        fetchList(this.example, this.searchParams).then(res => {
          this.accountDetailsTable = res.data;
          this.total = res.total;
        })
      },
      handleSizeChange(val) {
        this.searchParams.size = val;
        this.getDataInfo();
      },
      handleCurrentChange(val) {
        this.searchParams.page = val - 1;
        this.getDataInfo();
      },
      prePage() {
        this.$emit('changeFlag');
      },
      consumeType(key) {
        let json = {
          PCDEPOSIT: "客户充值",
          BACKDEPOSIT: "后台充值",
          WITHDRAW: "查询扣款",
          CONSUME: "后台扣款"
        };
        return json[key];
      },
      setMoneyState(val,state){
        if(state == "PCDEPOSIT" || state == "BACKDEPOSIT"){
          return "+" + val;
        }else{
          return "-" + val;
        }
      }
    },
    watch: {
      detailId(id) {
        if (id != '') {
          let height = this.$refs.height.offsetHeight;
          this.tableHeightTWo = height - 63 - 128 - 30;
          this.getDataInfo();
        }
      }
    }
  }
</script>

<style scoped>

</style>
