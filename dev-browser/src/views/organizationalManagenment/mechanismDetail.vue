<template>
  <div class="main-">
    <div class="detail">
      <div class="enterpriseInfo">
        <h3>企业信息</h3>
        <el-form ref="formOne" :rules="formValidOne" :model="form" label-width="120px">
          <el-form-item label="企业名称" prop="name">
            <el-input class="width" v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="企业负责人" prop="manager.name">
            <el-input class="width" v-model="form.manager.name"></el-input>
          </el-form-item>
          <el-form-item label="联系电话" prop="manager.username">
            <el-input class="width" v-model="form.manager.username"></el-input>
          </el-form-item>
          <el-form-item label="企业地址" prop="selectedOptions">
            <el-cascader
              size="large"
              style="width: 300px;"
              :options="options"
              v-model="form.selectedOptions"
              @change="handleChange">
            </el-cascader>
          </el-form-item>
          <el-form-item label="详细地址" prop="address">
            <el-input class="width" v-model="form.address"></el-input>
          </el-form-item>
          <el-form-item label="营业执照号码" prop="licenseNumber">
            <el-input class="width" v-model="form.licenseNumber"></el-input>
          </el-form-item>
          <el-form-item label="营业执照照片">
            <el-upload
              action="fileUpload"
              list-type="picture-card"
              :on-preview="handlePictureCardPreview"
              :before-upload="beforeUpload"
              :on-success="handleSuccess"
              :file-list="files"
              :on-remove="handleFileRemove">
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible">
              <img width="100%" :src="dialogImageUrl" alt="">
            </el-dialog>
          </el-form-item>
        </el-form>
      </div>
      <div class="otherInfo">
        <h3>其他信息</h3>
        <el-form ref="formTwo" :rules="formValidTwo" :model="form" label-width="120px">
          <el-form-item label="机构属性" prop="organizationType">
            <el-select v-model="form.organizationType" placeholder="请选择机构属性">
              <el-option label="企业" value="ENTERPRISE"></el-option>
              <el-option label="集团" value="GROUP"></el-option>
              <!--<el-option label="团队" value="TEAM"></el-option>-->
            </el-select>
          </el-form-item>
          <el-form-item label="行业" prop="industryType">
            <el-select v-model="form.industryType" placeholder="请选择行业">
              <el-option label="金融行业" value="FINANCE"></el-option>
              <el-option label="租赁行业" value="LEASE"></el-option>
              <el-option label="人才招聘" value="TALENTRECRUITMENT"></el-option>
              <el-option label="中介服务" value="INTERMEDIARIES"></el-option>
              <el-option label="其他行业" value="OTHERS"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="折扣" prop="discount">
            <el-input placeholder="请输入折扣" v-model="form.discount" style="width: 140px;">
              <template slot="append">%</template>
            </el-input>
          </el-form-item>
          <el-form-item label="账户余额" prop="account.remainder">
            <span>{{form.account.remainder}} 元</span>
           <!-- <el-input placeholder="请输入余额" v-model="form.account.remainder" style="width: 140px;">
              <template slot="append">元</template>
            </el-input>-->
          </el-form-item>
          <el-form-item label="开通产品">
            <el-popover
              :disabled="(JSON.stringify(form.productionManagers) == '{}') || (form.productionManagers.length < 2)? true : false"
              ref="product"
              placement="right"
              width="300"
              trigger="hover">
              <p v-for="item in form.productionManagers">产品名称：{{item.name}}</p>
            </el-popover>
            <el-tag size="medium" v-popover:product>{{JSON.stringify(form.productionManagers) == "{}" ? "暂没有开通产品" :
              form.productionManagers[0].name}}
            </el-tag>
            <el-button size="small" icon="el-icon-edit" type="success" style="margin-left: 40px"
                       @click="showProductList">编辑
            </el-button>
          </el-form-item>
          <el-form-item label="机构人数">
            <span>{{JSON.stringify(form.employees) == "{}" ? 0 : form.employees.length}}&nbsp;人</span>
          </el-form-item>
          <el-form-item label="上级机构">
            <span>{{JSON.stringify(form.parent) == '{}' ? "无" : form.parent.name}}</span>
          </el-form-item>
          <el-form-item label="下级机构">
            {{JSON.stringify(form.childrens) == '{}' ? "无" : form.childrens[0].name}}
          </el-form-item>
          <el-form-item label="申请时间">
            {{JSON.stringify(form.companyApplyForm) == '{}' ? timestampToTime(form.createdAt) :
            timestampToTime(form.companyApplyForm.createdAt)}}
          </el-form-item>
          <el-form-item label="认证时间">
            {{timestampToTime(form.createdAt)}}
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="form.enabled" placeholder="请选择状态">
              <el-option label="正常" :value="1"></el-option>
              <el-option label="锁定" :value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="buttons">
      <el-button type="primary" @click="submitData">确定</el-button>
      <el-button @click="cancelPage">取消</el-button>
    </div>

    <el-dialog title="产品列表" :visible.sync="product">
      <el-transfer filterable filter-placeholder="请输入产品名称" @change="transferChange" :props="{key: 'id',label: 'name'}"
                   :titles="['未添加的产品','添加的产品']" v-model="productValue"
                   :data="productData" style="display: flex;justify-content: center"></el-transfer>
    </el-dialog>
  </div>
</template>

<script>
  import {
    timestampToTime,
    fetchList,
    updateData,
    createNow,
    deleteNow,
    getDetail,
    enabledNow,
    filterForm,
    reform
  } from '@/api/common';
  import {validPhone, validNum, validStr, validNUmber} from '@/utils/validate'

  import {provinceAndCityData} from 'element-china-area-data'

  export default {
    name: "mechanism-detail",
    props: ['detailId'],
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
        example: 'company',
        files: [],
        imageNum: 1,
        dialogVisible: false,
        dialogImageUrl: '',
        options: provinceAndCityData,
        form: {
          name: '',
          manager: {
            name: '',
            username: ''
          },
          selectedOptions: ["", ""],
          employees: [],
          childrens: [
            {name: ""}
          ],
          productionManagers: [{name: ''}],
          companyApplyForm: {
            createdAt: ''
          },
          organizationType: "", // 机构属性
          industryType: "", // 行业
          discount: 80.55, // 折扣
          account: {
            remainder: 100, //余额
          },
          parent: {
            name: "商道"
          },
          personNUm: 100,
          enabled: "0"
        },
        params: {
          id: '',
          fields: '*,parent,productionManagers,employees,account.remainder,manager,companyApplyForm,childrens,files'
        },
        product: false,
        productValue: [],
        productData: [],
        formValidOne: {
          name: [{required: true, message: '请输入机构名称', trigger: 'blur'}],
          manager: {
            name: [{required: true, message: '请输入机构负责人', trigger: 'blur'}],
            username: [
              {required: true, trigger: 'blur', validator: validUserName}
            ]
          },
          selectedOptions: [{required: true, message: '请选择企业地址', trigger: 'blur'}],
          licenseNumber: [{required: true, trigger: 'blur', validator: licenseNumber}]
        },
        formValidTwo: {
          organizationType: [{required: true, message: '请选择机构属性', trigger: 'blur'}],
          industryType: [{required: true, message: '请选择行业', trigger: 'blur'}],
          discount: [{required: true, trigger: 'blur', validator: discountValid}],
        }
      }
    },
    computed: {
      timestampToTime: function () {
        return timestampToTime
      }
    },
    mounted() {
      this.getMechanismDetail();
      this.getProductList();
    },
    methods: {
      getMechanismDetail() {
        if (this.detailId != '') {
          this.params.id = this.detailId.split('-')[0];
          this.files = [];
          getDetail(this.example, filterForm(this.params)).then(res => {
            this.form = res.data;
            console.log(res.data)
            if (res.data.groupFiles) {
              let _this = this;
              if (res.data.groupFiles["创建"]) {
                res.data.groupFiles['创建'].forEach(function (val) {
                  if (val.fileSize > 0) {
                    _this.files.push(val);
                  }
                });
              }
              if (res.data.groupFiles["编辑"]) {
                res.data.groupFiles['编辑'].forEach(function (val) {
                  if (val.fileSize > 0) {
                    _this.files.push(val);
                  }
                });
              }
              this.files.forEach(function (val) {
                val.url = "http://" + val.url;
              })
            } else {
              this.files = [];
            }
            if (this.form.enabled) {
              this.form.enabled = 1;
            } else {
              this.form.enabled = 0;
            }
            if (JSON.stringify(this.form.productionManagers) != "{}") {
              for (let i = 0; i < this.form.productionManagers.length; i++) {
                this.$set(this.productValue, i, this.form.productionManagers[i].id);
              }
            }
            this.form.selectedOptions = ["", ""];
            this.$set(this.form.selectedOptions, 0, this.form.province);
            this.$set(this.form.selectedOptions, 1, this.form.city);
          })
        }
      },
      handleChange(value) {
        this.form.province = value[0];
        this.form.city = value[1];
      },
      showProductList() {
        this.product = true;
      },
      getProductList() {
        fetchList("productionManager", filterForm({fields: "*"})).then(res => {
          this.productData = res.data;
        })
      },
      transferChange() {
        let productValue = [];
        let data = this.productData;
        this.productValue.forEach(function (val) {
          data.forEach(function (item) {
            if (val == item.id) {
              productValue.push(item);
            }
          })
        });
        console.log(productValue);
        if (productValue.length === 0) {
          this.form.productionManagers = {};
        } else {
          this.form.productionManagers = productValue;
        }
      },
      submitData() {
        let productValue = [];
        this.productValue.forEach(function (val) {
          productValue.push({id: val});
        });
        let data = {
          id: this.form.id,
          name: this.form.name,
          manager: {
            name: this.form.manager.name,
            username: this.form.manager.username
          },
          address: this.form.address,
          city: this.form.city,
          province: this.form.province,
          licenseNumber: this.form.licenseNumber,
          uploadFiles: this.files,
          organizationType: this.form.organizationType,
          industryType: this.form.industryType,
          discount: this.form.discount,
          account: {
            id: this.form.account.id,
            remainder: this.form.account.remainder
          },
          productionManagers: productValue,
          enabled: this.form.enabled
        };

        let json = {};
        for (let i = 0; i < data.uploadFiles.length; i++) {
          if (data.uploadFiles[i].id) {
            data.uploadFiles[i] = {imgId: data.uploadFiles[i].id};
          } else {
            for (let key in data.uploadFiles[i]) {
              if (key != "uid" && key != "status") {
                json[key] = data.uploadFiles[i][key];
              }
              if (key == "url") {
                json[key] = json[key].substr(7);
              }
            }
            data.uploadFiles[i] = json;
          }
        }
        console.log(data);
        let _this = this;
        this.$refs["formOne"].validate(valid => {
          if(valid){
            _this.$refs["formTwo"].validate(validTwo =>{
              if(validTwo){
                updateData(this.example, reform(data, ["selectedOptions"])).then(res => {
                  this.$notify({
                    title: '成功',
                    message: '编辑成功',
                    type: 'success'
                  });
                  this.files = [];
                  this.restForm();
                  this.$emit('changeFlag', true);
                })
              }else{
                this.$notify({
                  title: '警告',
                  message: '数据填写出错。',
                  type: 'warning'
                });
              }
            })
          }else{
            this.$notify({
              title: '警告',
              message: '数据填写出错。',
              type: 'warning'
            });
          }
        })
      },
      handleSuccess(res) {
        this.files.push(res.data);
        this.imageNum++;
        let num = this.files.length - 1;
        this.files[num].url = "http://" + this.files[num].url;
        console.log(this.files)
      },
      beforeUpload() {
        if (this.imageNum > 3 || this.files.length >= 3) {
          this.$message.error('上传图片数量不能超过三张图片!');
          return false;
        } else {
          return true;
        }
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      handleFileRemove(file, fileList) {
        let _this = this;
        if (fileList.length >= 3) {
          this.imageNum = 4;
        } else {
          this.imageNum--;
          _this.files.forEach(function (item, index) {
            if (item.id == file.id) {
              _this.files.splice(index, 1);
            }
          });
          if (file.id) {
            deleteNow("companyFile", {id: file.id}).then(res => {
              this.$message({
                type: 'success',
                message: '图片删除成功!'
              });
            })
          }
        }
      },
      cancelPage() {
        this.restForm();
        this.$emit('changeFlag', false);
        this.files = [];
      },
      restForm(){
        this.$refs["formOne"].resetFields();
        this.$refs["formTwo"].resetFields();
      }
    },
    watch: {
      detailId(id) {
        if (id != '') {
          this.getMechanismDetail();
        }
      }
    }
  }
</script>

<style type="stylesheet/scss" lang="scss" scoped>
  .main- {
    .detail {
      display: flex;
      padding: 20px;
      .width {
        max-width: 300px
      }
      .enterpriseInfo {
        width: calc(100% / 2);
        padding: 0 80px 0 20px;
      }
      .otherInfo {
        width: calc(100% / 2);
        padding: 0 20px;
      }
    }
    .buttons {
      display: flex;
      justify-content: center;
      padding: 20px;
    }
    .buttons button {
      margin: 0 40px;
    }
  }

</style>
