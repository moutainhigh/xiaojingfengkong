<template>
  <div>
    <div v-show="status" ref="height" class="main">
      <el-form :inline="true" :model="search" class="demo-form-inline">
        <el-form-item label="机构名称">
          <el-input v-model="search.name" placeholder="请输入机构名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
          <el-button type="primary" @click="onClear">清除</el-button>
        </el-form-item>
      </el-form>

      <el-table :height="tableHeight" border
                ref="accountTable"
                :data="accountTable"
                tooltip-effect="dark"
                style="width: 100%;">
        <el-table-column
          label="账号名称"
          align="center"
          prop="name">
          <template slot-scope="scope">
            <span>{{scope.row.name}}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="账号属性"
          align="center">
          <template slot-scope="scope">
            <span>{{type(scope.row.accountType)}}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="历史充值金额"
          align="center">
          <template slot-scope="scope">
            <span>{{numDigit(scope.row.historyPay)}}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="历史赠送金额"
          align="center">
          <template slot-scope="scope">
            <span>{{numDigit(scope.row.historyGive)}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="充值账号余额">
          <template slot-scope="scope">
            <span>{{numDigit(scope.row.payRemainder)}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="赠送账号余额">
          <template slot-scope="scope">
            <span>{{numDigit(scope.row.giveRemainder)}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="账户总余额">
          <template slot-scope="scope">
            <span>{{numDigit(scope.row.remainder)}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          width="200"
          label="操作">
          <template slot-scope="scope">
            <el-button v-if="getButtons('recharge')" size="small" @click="moneyFun(scope.row, true)" icon="" type="text">充值</el-button>
            <el-button v-if="getButtons('withdrawing')" size="small" @click="moneyFun(scope.row, false)" icon="" type="text">扣款</el-button>
            <el-button size="small" @click="details(scope.row)" icon="" type="text">详情</el-button>
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
        :title="title"
        width="600px"
        :visible.sync="dialogData">

        <el-form ref="form" :rules="rules" :model="form" class="demo-form-inline" label-width="120px">
          <el-form-item :label="label" prop="payAmount">
            <el-input class="width" v-model="form.payAmount" placeholder="请输入金额"></el-input>
          </el-form-item>

          <!-- <el-form-item label="操作人" prop="name">
             <el-input class="width" v-model="form.payAmount" placeholder="请输入操作人"></el-input>
           </el-form-item>-->
          <el-form-item label="备注" prop="note">
            <el-input type="textarea" class="width" v-model="form.note" placeholder="请输入备注"></el-input>
          </el-form-item>

        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="cancelCreate">取 消</el-button>
          <el-button type="primary" @click="createData">确 定</el-button>
        </span>
      </el-dialog>
    </div>
    <accountDetails v-show="!status" v-on:changeFlag="changeFlag"
                    :detailId="detailId"></accountDetails>
  </div>

</template>

<script>
  import {
    getButtons,
    timestampToTime,
    fetchList,
    updateList,
    createNow,
    deleteNow,
    enabledNow,
    filterForm,
    reform,
    vagueList,
    vagueFormat
  } from '@/api/common';
  import {accountPay, deduct} from '@/api/account'
  import accountDetails from './accountDetails'
  import {validNum} from '@/utils/validate'

  export default {
    name: "account",
    data() {
      let moneyValid = (rule, value, callback) => {
        if (validNum(value)) {
          callback();
        } else {
          callback(new Error("只能输入大于零的数且不为空(小数点最多两位)"));
        }
      };
      return {
        example: "account",
        tableHeight: 500,
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
          fields: '*'
        },
        search: {
          name: ''
        },
        accountTable: [],
        title: "充值",
        label: '充值金额',
        dialogData: false,
        form: {
          id: '',
          payAmount: '',
          note: "",
          payType: "BACKDEPOSIT"
        },
        status: true,
        detailId: '',
        rules: {
          payAmount: [{required: "true", trigger: 'blur', validator: moneyValid}],
          note: [
            {required: "true", trigger: 'blur', message: "输入不能为空！"},
            {required: 'true', trigger: 'blur', message: "最多输入五十个字！", max: 50}
          ]
        }
      }
    },
    computed: {
      getButtons: function () {
        return getButtons
      }
    },
    components: {
      accountDetails
    },
    mounted() {
      let height = this.$refs.height.offsetHeight;
      this.tableHeight = height - 63 - 128 - 30;
      this.getData();
    },
    methods: {
      handleSizeChange(val) {
        this.searchParams.size = val;
        this.getData();
      },
      handleCurrentChange(val) {
        this.searchParams.page = val - 1;
        this.getData();
      },
      getData() {
        vagueList(this.example, vagueFormat(this.searchParams,['name'])).then(res => {
          console.log(res);
          this.accountTable = res.data;
          this.total = res.total;
        })
      },
      onSubmit() {
        this.searchParams.name = this.search.name;
        this.getData();
      },
      onClear() {
        this.search.name = "";
        this.searchParams.name = '';
        this.getData();
      },
      moneyFun(val, state) {
        if (state) {
          this.title = "充值";
          this.label = "充值金额";
        } else {
          this.title = "扣款";
          this.label = "扣款金额";
        }
        this.form.id = val.id;
        this.dialogData = true;
      },
      details(val) {
        this.detailId = val.id + '-' + (new Date()).valueOf();
        this.status = false;
      },
      cancelCreate() {
        /*this.$confirm('此操作将关闭表单和清空表单的数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {

        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消该操作'
          });
        });*/
        this.dialogData = false;
        this.resetForm();
      },
      createData() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.title == "充值") {
              this.form.payType = "BACKDEPOSIT";
              accountPay(this.example, this.form).then(res => {
                this.$notify({
                  title: '成功',
                  message: '充值成功！',
                  type: 'success'
                });
                this.dialogData = false;
                this.resetForm();
                this.getData();
              })
            } else {
              this.form.payType = "CONSUME";
              deduct(this.example, this.form).then(res => {
                this.$notify({
                  title: '成功',
                  message: '扣款成功！',
                  type: 'success'
                });
                this.dialogData = false;
                this.resetForm();
                this.getData();
              })
            }
          }
        });
      },
      type(key) {
        let json = {
          GROUP: "集团",
          ENTERPRISE: "企业",
          TEAM: "团队",
          PERSON: "个人"
        };
        return json[key];
      },
      resetForm() {
        this.$refs['form'].resetFields();
      },
      changeFlag() {
        this.status = true;
      },
      numDigit(val) {
        return val.toFixed(3)
      }
    }
  }
</script>

<style type="stylesheet/scss" lang="scss" scoped>
  .main {
    .width {
      width: 300px
    }
  }
</style>
