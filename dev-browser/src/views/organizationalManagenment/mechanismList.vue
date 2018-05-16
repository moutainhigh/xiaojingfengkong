<template>
  <div ref="height" class="mechanism-list main">
    <div v-show="mechanismDetail">
      <el-form :inline="true" :model="mechanismForm" class="demo-form-inline">
        <el-form-item label="机构名称">
          <el-input v-model="mechanismForm.name" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="机构属性">
          <el-select v-model="mechanismForm.organizationType" placeholder="请选择机构属性">
            <el-option label="全部" value=""></el-option>
            <el-option label="企业" value="ENTERPRISE"></el-option>
            <el-option label="集团" value="GROUP"></el-option>
            <el-option label="团队" value="TEAM"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
          <el-button type="primary" @click="onClear">清除</el-button>
        </el-form-item>
        <el-form-item class="right">
          <el-button v-if="getButtons('add')" type="success" @click="onAdd">新增</el-button>
          <el-button v-if="getButtons('on')" type="primary" @click="onOpen">启用</el-button>
          <el-button v-if="getButtons('off')" type="warning" @click="onDisable">禁用</el-button>
        </el-form-item>
      </el-form>
      <el-table :height="tableHeight" border
                ref="multipleTable"
                :data="mechanismTable"
                tooltip-effect="dark"
                style="width: 100%;"
                @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          label="机构名称"
          align="center"
          prop="name">
          <template slot-scope="scope">
            <span @click="handleUpdate(scope.row)" style="display: block;cursor: pointer">{{scope.row.name}}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="机构属性"
          align="center"
          width="80">
          <template slot-scope="scope">
            <span>{{organizationType(scope.row.organizationType)}}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="上级机构"
          align="center">
          <template slot-scope="scope">
            <span @click="handleUpdate(scope.row)" style="display: block;cursor: pointer">{{ scope.row.parent.name ? scope.row.parent.name : "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          width="80"
          label="机构人数">
          <template slot-scope="scope">
            <span>{{JSON.stringify(scope.row.employees) == "{}" ? 0 : scope.row.employees.length}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="开通产品"
          show-overflow-tooltip>
          <template slot-scope="scope">
            <el-popover trigger="hover" :disabled="scope.row.productionManagers.length !=undefined ? false : true"
                        placement="top">
              <p v-show="scope.row.productionManagers.length !=undefined ? true : false"
                 v-for="el in scope.row.productionManagers">{{el.name }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">
                  {{scope.row.productionManagers.length!=undefined ? scope.row.productionManagers[0].name : '-' }}
                </el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop="discount"
          label="折扣"
          width="80">
        </el-table-column>
        <el-table-column
          prop="enabled"
          align="center"
          label="状态"
          width="80">
          <template slot-scope="scope">
            <span v-show="scope.row.enabled == 1 ? true : false" style="color: #67C23A;">正常</span>
            <span v-show="scope.row.enabled == 0 ? true : false" style="color: red;">锁定</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="searchTimes"
          align="center"
          label="查询次数"
          width="80">
          <!--<template slot-scope="scope">{{scope.row.searchTimes}}</template>-->
        </el-table-column>
        <el-table-column
          align="center"
          label="认证时间"
          width="180">
          <template slot-scope="scope">
            <span>{{ timestampToTime(scope.row.createdAt) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="licensePicURL"
          align="center"
          label="营业执照"
          width="80">
          <template slot-scope="scope">
            <el-button type="primary" @click="voucherFn(scope.row.files)" icon="el-icon-picture"
                       circle="true"></el-button>
          </template>
        </el-table-column>
        <el-table-column
          prop="industryType"
          align="center"
          label="行业"
          show-overflow-tooltip>
          <template slot-scope="scope">
            {{industryType(scope.row.industryType)}}
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          width="80"
          label="账户余额">
          <template slot-scope="scope">{{ (scope.row.account.remainder).toFixed(3) }}</template>
        </el-table-column>
        <el-table-column
          align="center"
          width="100"
          v-if="getButtons('edit')"
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
        title="营业执照"
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

      <el-dialog
        title="添加机构"
        width="600px"
        :visible.sync="dialogAddData">

        <el-form ref="addForms" :rules="rules" :model="addForm" class="demo-form-inline" label-width="120px">
          <el-form-item label="机构名称" prop="name">
            <el-input class="width" v-model="addForm.name" placeholder="请输入名称"></el-input>
          </el-form-item>
          <el-form-item label="机构负责人" prop="manager.name">
            <el-input class="width" v-model="addForm.manager.name" placeholder="请输入机构负责人"></el-input>
          </el-form-item>
          <el-form-item label="联系电话" prop="manager.username">
            <el-input class="width" v-model="addForm.manager.username" placeholder="请输入联系电话"></el-input>
          </el-form-item>
          <el-form-item label="企业地址" prop="selectedOptions">
            <el-cascader
              placeholder="请选择地址"
              size="large"
              style="width: 300px;"
              :options="options"
              v-model="addForm.selectedOptions"
              @change="handleChange">
            </el-cascader>
          </el-form-item>
          <el-form-item label="详细地址" prop="address">
            <el-input class="width" v-model="addForm.address" placeholder="请输入详细地址"></el-input>
          </el-form-item>
          <el-form-item label="机构属性" prop="organizationType">
            <el-select class="width" v-model="addForm.organizationType" placeholder="请选择机构属性">
              <el-option label="企业" value="ENTERPRISE"></el-option>
              <el-option label="集团" value="GROUP"></el-option>
              <!--<el-option label="团队" value="TEAM"></el-option>-->
            </el-select>
          </el-form-item>
          <el-form-item label="行业" prop="industryType">
            <el-select class="width" v-model="addForm.industryType" placeholder="请选择行业">
              <el-option label="金融行业" value="FINANCE"></el-option>
              <el-option label="租赁行业" value="LEASE"></el-option>
              <el-option label="人才招聘" value="TALENTRECRUITMENT"></el-option>
              <el-option label="服务租赁" value="INTERMEDIARIES"></el-option>
              <el-option label="其他行业" value="OTHERS"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="折扣" prop="discount">
            <el-input class="width" placeholder="请输入折扣" v-model="addForm.discount">
              <template slot="append">%</template>
            </el-input>
          </el-form-item>
          <el-form-item label="证件类型" prop="licenseType">
            <el-select class="width" v-model="addForm.licenseType" placeholder="请选择行业">
              <el-option label="普通营业执照" value="GENERAL"></el-option>
              <el-option label="多证合一营业执照" value="MULTIPLE"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="营业执照号码" prop="licenseNumber">
            <el-input class="width" v-model="addForm.licenseNumber" placeholder="请输入营业执照号码"></el-input>
          </el-form-item>
          <el-form-item label="营业执照照片" prop="uploadFiles">
            <el-upload
              ref="upload"
              action="fileUpload"
              list-type="picture-card"
              :on-preview="handlePictureCardPreview"
              :before-upload="beforeUpload"
              :on-success="uploadSuccess"
              :on-remove="fileRemove">
              <i class="el-icon-plus"></i>
            </el-upload>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="cancelCreate">取 消</el-button>
          <el-button type="primary" @click="createData">确 定</el-button>
        </span>
      </el-dialog>

      <el-dialog :visible.sync="dialogVisible">
        <img width="100%" :src="dialogImageUrl" alt="">
      </el-dialog>
    </div>

    <mechanismDetail v-show="!mechanismDetail" v-on:changeFlag="changeFlag"
                     :detailId="detailId"></mechanismDetail>
  </div>
</template>

<script>
  import {
    timestampToTime,
    fetchList,
    updateList,
    createNow,
    deleteNow,
    enabledNow,
    filterForm,
    getButtons,
    reform,
    vagueList,
    vagueFormat
  } from '@/api/common';
  import {enable} from '@/api/mechanism';
  import {provinceAndCityData} from 'element-china-area-data'
  import {validPhone, validNum, validStr, validNUmber} from '@/utils/validate'
  import mechanismDetail from './mechanismDetail';

  export default {
    name: "mechanism-list",
    data() {
      let validUserName = (rule, value, callback) => {
        if (validPhone(value)) {
          callback();
        } else {
          callback(new Error("请输入正确的电话号码形式(只能为11位数字)"));
        }
      };
      let discountValid = (rule, value, callback) => {
        if (validNUmber(value)) {
          callback();
        } else {
          callback(new Error("只能输入整数数字且不能为空。"));
        }
      };
      let licenseNumber = (rule, value, callback) => {
        if (validStr(value)) {
          callback()
        } else {
          callback(new Error("营业执照号码由字母和数字组成且不能为空。"))
        }
      };
      return {
        dialogAddData: false,
        mechanismDetail: true,
        detailId: '',
        example: 'company',
        tableHeight: 500,
        imgsrc: [],
        voucherShow: false,
        mechanismForm: {
          name: '',
          organizationType: '',
        },
        mechanismTable: [],
        multipleSelection: [],
        total: 0,
        listQuery: {
          page: 1,
          sizes: [10, 20, 30, 40],
          size: 10,
        },
        searchParams: {
          name: '',
          organizationType: '',
          page: 0,
          size: 10,
          fields: '*,parent,productionManagers,employees,account.remainder,files'
        },
        addForm: {
          fromBackend: true,
          enabled: 1,
          searchTimes: 0,
          name: '',
          manager: {
            name: '',
            username: ''
          },
          province: '',
          city: '',
          address: '',
          organizationType: '',
          industryType: '',
          discount: '',
          licenseNumber: '',
          licenseType: '',
          uploadFiles: [],
          selectedOptions: []
        },
        options: provinceAndCityData,
        imgNum: 1,
        dialogImageUrl: '',
        dialogVisible: false,
        rules: {
          name: [{required: true, message: '请输入机构名称', trigger: 'blur'}],
          manager: {
            name: [{required: true, message: '请输入机构负责人', trigger: 'blur'}],
            username: [
              {required: true, trigger: 'blur', validator: validUserName}
            ]
          },
          selectedOptions: [{required: true, message: '请选择企业地址', trigger: 'blur'}],
          organizationType: [{required: true, message: '请选择机构属性', trigger: 'blur'}],
          industryType: [{required: true, message: '请选择行业', trigger: 'blur'}],
          discount: [{required: true, trigger: 'blur', validator: discountValid}],
          licenseType: [{required: true, message: '请选择证件类型', trigger: 'blur'}],
          licenseNumber: [{required: true, trigger: 'blur', validator: licenseNumber}]
        }
      }
    },
    components: {
      mechanismDetail
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
      this.getMechanismData();
    },
    methods: {
      changeFlag: function (val) {
        this.mechanismDetail = true;
        if (val) {
          this.getMechanismData();
        }
      },
      industryType: function (key) {
        let val = {
          FINANCE: "金融行业",
          LEASE: "租赁行业",
          INTERMEDIARIES: "中介服务",
          TALENTRECRUITMENT: "人才招聘",
          OTHERS: "其他行业"
        };
        return val[key];
      },
      organizationType: function (key) {
        let val = {
          GROUP: "集团",
          ENTERPRISE: "企业",
          TEAM: "团队"
        };
        return val[key];
      },
      voucherFn(src) {
        this.imgsrc = src;
        this.voucherShow = true;
      },
      getMechanismData() {
        vagueList(this.example, vagueFormat(filterForm(this.searchParams), ['name'])).then((res) => {
          this.mechanismTable = res.data;
          console.log(res.data);
          this.mechanismTable.forEach(function (val) {
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
      onSubmit() {
        this.searchParams.name = this.mechanismForm.name;
        this.searchParams.organizationType = this.mechanismForm.organizationType;
        this.getMechanismData();
      },
      onClear() {
        this.mechanismForm.name = '';
        this.mechanismForm.organizationType = '';
        this.searchParams.name = '';
        this.searchParams.organizationType = '';
        this.getMechanismData();
      },
      onDisable() {
        this.multipleSelection.forEach((val, index) => {
          enable(this.example, {id: val.id, enabled: 0}).then(res => {
            this.multipleSelection[index].enabled = false;
            if (index == (this.multipleSelection.length - 1)) {
              this.getMechanismData();
              this.$notify({
                title: '成功',
                message: '禁用成功',
                type: 'success',
                duration: 2000
              })
            }
          })
        })
      },
      onOpen() {
        this.multipleSelection.forEach((val, index) => {
          enable(this.example, {id: val.id, enabled: 1}).then(res => {
            this.multipleSelection[index].enabled = true;
            if (index == (this.multipleSelection.length - 1)) {
              this.getMechanismData();
              this.$notify({
                title: '成功',
                message: '启用成功',
                type: 'success',
                duration: 2000
              })
            }
          })
        })
      },
      onAdd() {
        this.dialogAddData = true;
      },
      handleSelectionChange(selection) {
        this.multipleSelection = selection;
      },
      handleUpdate(val) {
        this.detailId = val.id + '-' + (new Date()).valueOf();
        this.mechanismDetail = false;
      },
      handleSizeChange(val) {
        this.searchParams.size = val;
        this.getMechanismData();
      },
      handleCurrentChange(val) {
        this.searchParams.page = val - 1;
        this.getMechanismData();
      },
      handleChange() {
        this.addForm.province = this.addForm.selectedOptions[0];
        this.addForm.city = this.addForm.selectedOptions[1];
      },
      beforeUpload() {
        if (this.imgNum > 3) {
          this.$message.error('上传图片数量不能超过三张图片!');
          return false;
        } else {
          return true;
        }
      },
      uploadSuccess(res) {
        this.imgNum++;
        this.addForm.uploadFiles.push(res.data);
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      fileRemove(file, fileList) {
        if (fileList.length >= 3) {
          this.imageNum = 4;
        } else {
          this.imageNum--;
        }
      },
      createData() {
        this.$refs["addForms"].validate(valid => {
          if (valid) {
            createNow(this.example, reform(this.addForm, ["selectedOptions"])).then(res => {
              console.log(res);
              this.$notify({
                title: '成功',
                message: '机构创建成功！',
                type: 'success'
              });
              this.resetForm();
              this.dialogAddData = false;
              this.getMechanismData();
            })
          } else {
            this.$notify({
              title: '警告',
              message: '数据填写出错。',
              type: 'warning'
            });
          }
        });
      },
      cancelCreate() {
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
      resetForm() {
        this.$refs['addForms'].resetFields();
        this.addForm.uploadFiles = [];
        this.$refs.upload.clearFiles();
      }
    }
  }
</script>

<style type="stylesheet/scss" lang="scss" scoped>
  .mechanism-list {
    /*min-height: calc(100vh - 84px - 25px);*/
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
