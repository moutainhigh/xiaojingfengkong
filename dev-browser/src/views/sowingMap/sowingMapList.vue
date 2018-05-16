<template>
  <div ref="height" class="main">
    <el-form :inline="true" :model="sowingMap" class="demo-form-inline">
      <el-form-item label="广告名称">
        <el-input v-model="sowingMap.name" placeholder="请输入名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
        <el-button type="primary" @click="onClear">清除</el-button>
      </el-form-item>
      <el-form-item class="right">
        <el-button v-if="getButtons('add')" type="success" @click="handleUpdate(false)">新增</el-button>
        <el-button v-if="getButtons('delete')" type="danger" @click="delModel">删除</el-button>
        <el-button v-if="getButtons('grounding')" type="primary" @click="openModel">上架</el-button>
        <el-button v-if="getButtons('undercarriage')" type="primary" @click="offModel">下架</el-button>
      </el-form-item>
    </el-form>

    <el-table :height="tableHeight" border
              ref="multipleTable"
              :data="sowingMApTable"
              tooltip-effect="dark"
              style="width: 100%;"
              @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column
        label="广告名称"
        align="center"
        prop="name">
        <template slot-scope="scope">
          <span>{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="轮播排序"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.sort}}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="licensePicURL"
        align="center"
        label="广告图片">
        <template slot-scope="scope">
          <el-button type="primary" @click="voucherFn(scope.row.files)" icon="el-icon-picture"
                     circle="true"></el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="创建时间">
        <template slot-scope="scope">
          <span>{{ timestampToTime(scope.row.createdAt) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="轮播类型"
        align="center">
        <template slot-scope="scope">
          <span>{{ spwingMapType(scope.row.turnType) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="状态"
        align="center">
        <template slot-scope="scope">
          <span>{{ spwingMapStatus(scope.row.turnStatus) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="点击次数">
        <template slot-scope="scope">{{ scope.row.clickTimes }}</template>
      </el-table-column>
      <el-table-column
        align="center"
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
      title="广告图片"
      :visible.sync="voucherShow"
      width="50%"
      center>
      <img alt="没有图片或图片已经损坏" style="width: 100%;" :src="imgUrl">
      <span slot="footer" class="dialog-footer"></span>
    </el-dialog>

    <el-dialog
      :title="sowingMapName"
      :visible.sync="sowingMapState"
      @close="resetForm"
      width="500px">
      <el-form ref="form" :rules="rules" :model="addForm" class="demo-form-inline" label-width="120px">
        <el-form-item label="广告名称" prop="name">
          <el-input class="width" v-model="addForm.name" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="广告序号" prop="sort">
          <el-input class="width" v-model="addForm.sort" placeholder="请输入广告序号"></el-input>
        </el-form-item>
        <el-form-item label="轮播类型" prop="turnType">
          <el-select class="width" v-model="addForm.turnType" placeholder="请选择轮播类型">
            <el-option label="PC端" value="WEB"></el-option>
            <el-option label="移动端" value="APP"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="广告链接" prop="jumpUrl">
          <el-input class="width" v-model="addForm.jumpUrl" placeholder="请输入广告链接"></el-input>
        </el-form-item>
        <el-form-item label="上传广告图片" prop="uploadFiles">
          <el-upload
            ref="upload"
            :limit="1"
            :file-list="addForm.uploadFiles"
            action="fileUpload"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-success="uploadSuccess"
            :on-remove="fileRemove">
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelData">取 消</el-button>
          <el-button type="primary" @click="saveData">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
  import {
    timestampToTime,
    updateData,
    createNow,
    deleteNow,
    enabledNow,
    reform,
    vagueList,
    vagueFormat,
    getButtons
  } from '@/api/common';
  import {validNUmber} from '@/utils/validate'

  export default {
    name: "sowing-map-list",
    data() {
      let sortValid = (rule, value, callback) => {
        if (validNUmber(value)) {
          callback()
        } else {
          callback(new Error("只能输入数字且不能为空"));
        }
      };
      return {
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
          fields: '*,files'
        },
        tableHeight: 500,
        example: 'pictureTurns',
        sowingMapName: "新增轮播图",
        sowingMapState: false,
        sowingMap: {
          name: ''
        },
        sowingMApTable: [],
        selectTable: [],
        voucherShow: false,
        imgUrl: '',
        addForm: {
          name: '',
          sort: 1,
          turnType: "APP",
          jumpUrl: '',
          uploadFiles: [],
          clickTimes: 0,
          turnStatus: 'THESHELVES'
        },
        addFormId: '',
        rules: {
          name: [{required: true, message: '请输入广告名称', trigger: 'blur'}],
          sort: [{required: true, trigger: 'blur', validator: sortValid}],
          turnType: [{required: true, message: '请选择轮播类型', trigger: 'blur'}],
          uploadFiles: [{required: true, message: '请选择图片上传', trigger: 'blur'}]
        },
        dialogVisible: false,
        dialogImageUrl: ''
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
      this.getSowingMapList();
    },
    methods: {
      onSubmit() {
        this.searchParams.name = this.sowingMap.name;
        this.getSowingMapList();
      },
      onClear() {
        this.sowingMap.name = '';
        this.searchParams.name = '';
        this.getSowingMapList();
      },
      handleSizeChange(val) {
        this.searchParams.size = val;
        this.getSowingMapList();
      },
      handleCurrentChange(val) {
        this.searchParams.page = val - 1;
        this.getSowingMapList();
      },
      handleSelectionChange(selection) {
        this.selectTable = selection;
      },
      handleUpdate(val) {
        if (val) {
          this.addFormId = val.id;
          this.addForm.name = val.name;
          this.addForm.sort = val.sort;
          this.addForm.turnType = val.turnType;
          this.addForm.jumpUrl = val.jumpUrl;
          if (JSON.stringify(val.files) != '{}') {
            this.addForm.uploadFiles = val.files;
          } else {
            this.addForm.uploadFiles = [];
          }
          this.sowingMapName = "编辑轮播图";
        } else {
          this.sowingMapName = "新增轮播图";
          this.addFormId = '';
        }
        this.sowingMapState = true;
      },
      getSowingMapList() {
        vagueList(this.example, vagueFormat(this.searchParams, ['name'])).then(res => {
          this.sowingMApTable = res.data;
          this.sowingMApTable.forEach(function (data) {
            if (JSON.stringify(data.files) != "{}") {
              for (let i = data.files.length - 1; i >= 0; i--) {
                if (data.files[i].fileSize == 0) {
                  data.files.splice(i, 1);
                }else{
                  data.files[i].url = "http://" + data.files[i].url;
                }
              }
            }
          });
          this.total = res.total;
        })
      },
      openModel() {
        let _this = this;
        this.selectTable.forEach(function (data, index) {
          let json = {
            id: data.id,
            turnStatus: "SHELVES"
          };
          enabledNow(_this.example, json).then(res => {
            if (index == (_this.selectTable.length - 1)) {
              _this.getSowingMapList();
              _this.$notify({
                title: '成功',
                message: '轮播图上架成功',
                type: 'success'
              });
            }
          })
        })
      },
      offModel() {
        let _this = this;
        this.selectTable.forEach(function (data, index) {
          let json = {
            id: data.id,
            turnStatus: "THESHELVES"
          };
          enabledNow(_this.example, json).then(res => {
            if (index == (_this.selectTable.length - 1)) {
              _this.getSowingMapList();
              _this.$notify({
                title: '成功',
                message: '轮播图下架成功',
                type: 'success'
              });
            }
          })
        })
      },
      delModel() {
        let _this = this;
        this.selectTable.forEach(function (data, index) {
          deleteNow(_this.example, {id: data.id}).then(res => {
            if (index == (_this.selectTable.length - 1)) {
              _this.getSowingMapList();
              _this.$notify({
                title: '成功',
                message: '轮播图删除成功',
                type: 'success'
              });
            }
          })
        })
      },
      voucherFn(files) {
        if (JSON.stringify(files) != "{}") {
          this.imgUrl = files[0].url;
        } else {
          this.imgUrl = "";
        }
        this.voucherShow = true;
      },
      spwingMapType(key) {
        let json = {
          "WEB": "PC端",
          "APP": "移动端"
        };
        return json[key];
      },
      spwingMapStatus(key){
        let json = {
          "SHELVES": "上架中",
          "THESHELVES": "下架中"
        };
        return json[key];
      },
      cancelData() {
        this.resetForm();
        this.sowingMapState = false;
      },
      saveData() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.addForm.uploadFiles[0] = reform(this.addForm.uploadFiles[0], ["uid", "status"]);
            this.addForm.uploadFiles[0].url = this.addForm.uploadFiles[0].url.substr(7);
            if (this.sowingMapName == "新增轮播图") {
              console.log(this.addForm);
              createNow(this.example, reform(this.addForm,["id"])).then(res => {
                this.getSowingMapList();
                this.resetForm();
                this.sowingMapState = false;
                this.$notify({
                  title: '成功',
                  message: '轮播图新增成功',
                  type: 'success'
                });
              })
            } else {
              let json = this.addForm;
              json.id = this.addFormId;
              if (json.uploadFiles[0].id) {
                json.uploadFiles[0] = {imgId: json.uploadFiles[0].id};
              }
              updateData(this.example, json).then(res => {
                this.getSowingMapList();
                this.resetForm();
                this.sowingMapState = false;
                this.$notify({
                  title: '成功',
                  message: '轮播图编辑成功',
                  type: 'success'
                });
              })
            }
          } else {
            this.$notify({
              title: '提示',
              message: '信息没有停写完整。',
              type: 'warning'
            });
          }
        })
      },
      uploadSuccess(res) {
        res.data.url = "http://" + res.data.url;
        this.addForm.uploadFiles.push(res.data);
        console.log(res);
        this.$refs["form"].validateField('uploadFiles', function (valid) {
        })
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      fileRemove(file, fileList) {
        this.addForm.uploadFiles = [];
        if (file.id) {
          deleteNow("pictureTurnsFile", {id: file.id}).then(res => {
            this.$message({
              type: 'success',
              message: '图片删除成功!'
            });
          })
        }
      },
      resetForm() {
        this.$refs['form'].resetFields();
        this.$refs.upload.clearFiles();
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
