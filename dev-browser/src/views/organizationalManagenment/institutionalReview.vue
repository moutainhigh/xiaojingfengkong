<template>
  <div ref="height" class="main">
    <el-form :inline="true" :model="mechanismForm" class="demo-form-inline">
      <el-form-item label="机构名称">
        <el-input v-model="mechanismForm.name" placeholder="请输入名称"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="mechanismForm.state" placeholder="请选择状态">
          <el-option
            v-for="item in selectData"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
        <el-button type="primary" @click="onClear">清除</el-button>
      </el-form-item>
    </el-form>

    <el-table :height="tableHeight" border
              ref="multipleTable"
              :data="mechanismTable"
              tooltip-effect="dark"
              style="width: 100%;">
      <el-table-column
        label="机构名称"
        align="center"
        prop="name">
      </el-table-column>
      <el-table-column
        label="创建人"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.createdBy.name}}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="联系电话"
        align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createdBy.username }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="申请时间">
        <template slot-scope="scope">
          <span>{{ timestampToTime(scope.row.createdAt) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="状态">
        <template slot-scope="scope">{{formatData(scope.row.state.code)}}</template>
      </el-table-column>
      <el-table-column
        align="center"
        label="企业资料">
        <template slot-scope="scope">
          <el-button size="small" @click="showInfo(scope.row,true)" type="text">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="scope.row.state.code == 'CREATED' ? true : false" size="small"
                     @click="auditingClick(scope.row)" type="text">审核
          </el-button>
          <el-button v-if="scope.row.state.code != 'CREATED' ? true : false" size="small"
                     @click="showInfo(scope.row, false)" type="text">详情
          </el-button>
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
      title="机构信息"
      width="600px"
      :visible.sync="mechanismInfo">
      <el-form :model="info" class="demo-form-inline" label-width="120px">
        <el-form-item label="企业地址：">
          <span>{{addressFormat(info.province, info.city)}}</span>
        </el-form-item>
        <el-form-item label="详细地址：">
          <span>{{info.address}}</span>
        </el-form-item>
        <el-form-item label="证件类型：">
          <span>{{textFormat(info.licenseType)}}</span>
        </el-form-item>
        <el-form-item label="营业执照号码：">
          <span>{{info.licenseNumber}}</span>
        </el-form-item>
        <el-form-item label="营业执照照片：">
          <div v-if="JSON.stringify(info.files) == '{}' ? false : true">
            <img class="img" v-for="(file,index) in info.files" :key="index" :src="'http://' + file.url" />
          </div>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="mechanismInfo=false">关 闭</el-button>
        </span>
    </el-dialog>

    <el-dialog
      title="审核详情"
      width="600px"
      :visible.sync="auditingInfo">
      <el-form :model="info" class="demo-form-inline" label-width="120px">
        <el-form-item v-if="info.state.code == 'NOPASS' ? true : false" label="审核内容：">
          <span>{{info.reason}}</span>
        </el-form-item>
        <el-form-item label="审核结果：">
          <span>{{formatData(info.state.code)}}</span>
        </el-form-item>
        <el-form-item label="审核时间：">
          <span>{{timestampToTime(info.verifiedAt)}}</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="auditingInfo=false">关 闭</el-button>
        </span>
    </el-dialog>
  </div>

</template>

<script>
  import {
    timestampToTime,
    fetchList,
    updateList,
    updateData,
    createNow,
    deleteNow,
    enabledNow,
    filterForm,
    reform,
    verifyData
  } from '@/api/common';
  import {CodeToText} from 'element-china-area-data'
  import {validNum} from '@/utils/validate'

  export default {
    name: "institutional-review",
    data() {
      return {
        info: {
          state: {
            code: ''
          }
        },
        tableHeight: 500,
        example: 'companyApplyForm',
        mechanismForm: {
          name: '',
          state: ''
        },
        mechanismTable: [{
          name: '',
          createdBy: {
            name: '',
            username: ''
          },
          createdAt: '',
          state: {
            code: ''
          }
        }],
        selectData: [{label: "全部", value: ''}],
        listQuery: {
          page: 1,
          sizes: [10, 20, 30, 40],
          size: 10,
        },
        total: 0,
        searchParams: {
          name: '',
          "state.code": '',
          page: 0,
          size: 10,
          fields: '*,state,createdBy,files'
        },
        mechanismInfo: false,
        auditingInfo: false,
        auditingDialog: false,
        auditingForm: {
          reason: '',
          discount: 100,
          id: ''
        },
        rules: {
          reason: [{required: true, message: "请输入拒绝的理由。", trigger: 'click'}]
        }
      }
    },
    computed: {
      timestampToTime: function () {
        return timestampToTime
      }
    },
    mounted() {
      this.getConfig();
      let height = this.$refs.height.offsetHeight;
      this.tableHeight = height - 63 - 128 - 30;
      this.getMechanismData();
    },
    methods: {
      handleSizeChange(val) {
        this.searchParams.size = val;
        this.getMechanismData();
      },
      handleCurrentChange(val) {
        this.searchParams.page = val - 1;
        this.getMechanismData();
      },
      getConfig() {
        fetchList("state", {fields: '*', "entityManager.name": "companyApplyForm"}).then(res => {
          let _this = this;
          res.data.forEach(function (val) {
            let json = {};
            if (val.code == "CREATED") {
              json = {
                label: "未审核",
                value: "CREATED"
              }
            }
            if (val.code == "NOPASS") {
              json = {
                label: "审核未通过",
                value: "NOPASS"
              }
            }
            if (val.code == "FINISHED") {
              json = {
                label: "审核通过",
                value: "FINISHED"
              }
            }
            _this.selectData.push(json);
          })
        })
      },
      getMechanismData() {
        fetchList(this.example, filterForm(this.searchParams)).then(res => {
          this.total = res.total;
          this.mechanismTable = res.data;
        })
      },
      formatData(key) {
        let json = {
          "CREATED": "未审核",
          "NOPASS": "审核未通过",
          "FINISHED": "审核通过"
        };
        return json[key];
      },
      textFormat(key) {
        let json = {
          "MULTIPLE": "多证合一营业执照",
          "GENERAL": "普通营业执照"
        };
        return json[key];
      },
      addressFormat(province, city) {
        if (province && city) {
          return CodeToText[province] + CodeToText[city];
        }
      },
      onSubmit() {
        this.searchParams.name = this.mechanismForm.name;
        this.searchParams["state.code"] = this.mechanismForm.state;
        this.getMechanismData();
      },
      onClear() {
        this.mechanismForm.name = '';
        this.mechanismForm.state = '';
        this.searchParams.name = '';
        this.searchParams.state = '';
        this.getMechanismData();
      },
      showInfo(val, state) {
        this.info = val;
        if (state) {
          this.mechanismInfo = true;
        } else {
          this.auditingInfo = true;
        }
      },
      auditingClick(val) {
        this.auditingForm.id = val.id;
        // this.auditingDialog = true;
        this.$prompt('请输入折扣', '审核', {
          // center: true,
          confirmButtonText: '通过',
          cancelButtonText: '拒绝',
          inputValidator: this.validDiscount,
          inputErrorMessage: '折扣不能为空且小于100'
        }).then(({value}) => {
          this.auditingForm.discount = value;
          this.adoptAudit();
        }).catch(() => {
          this.$prompt('请输入拒绝理由', '审核', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            inputValidator: this.validReason
          }).then(({value}) => {
            this.auditingForm.reason = value;
            this.refuseAudit();
          }).catch(() => {
            this.$notify({
              title: '提示',
              message: '审核操作已取消。',
              type: 'success'
            });
          });
        });
      },
      validDiscount(val) {
        if (validNum(val)) {
          if (val <= 100) {
            return true;
          } else {
            return "折扣大于100"
          }
        } else {
          return "折扣不能为空且只能为数字小数点后两位"
        }
      },
      validReason(val){
        if(val){
          return true;
        }else{
          return "拒绝理由不能为空"
        }
      },
      adoptAudit() {
        let json = {
          id: this.auditingForm.id,
          verifiedResult: 1,
          discount: this.auditingForm.discount
        };
        verifyData(this.example, json).then(res => {
          this.getMechanismData();
          this.$notify({
            title: '提示',
            message: '审核通过成功',
            type: 'success'
          });
        })
      },
      refuseAudit() {
        let json = {
          id: this.auditingForm.id,
          reason: this.auditingForm.reason,
          verifiedResult: 0,
          discount: this.auditingForm.discount
        };
        verifyData(this.example, json).then(res => {
          this.getMechanismData();
          this.$notify({
            title: '提示',
            message: '审核未通过成功',
            type: 'success'
          });
        })
      }
    }
  }
</script>

<style scoped>
  .img{
    width: 100px;
    height: 120px;
    border: 1px solid #65ff00db;
    border-radius: 5px;
    margin-right: 10px;
    padding: 2px;
  }
</style>
