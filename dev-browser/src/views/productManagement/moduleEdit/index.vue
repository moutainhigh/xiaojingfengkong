<template>
  <div id="backgounduser"  ref="height" class="main">
    <el-container>
      <el-aside
        width="50%">
        <el-form align="left"
                 :rules="moduleRule"
                 :model="moduleDetail"
                 ref="moduleForm"
                 style="margin-top:50px;max-width:462px;"
                 label-width="150px" >
          <el-form-item
            label="模块名称"
            prop="name">
            <el-input v-model="moduleDetail.name"></el-input>
          </el-form-item>

          <el-form-item
            label="模块描述"
            prop="description">
            <el-input
              style="width:312px;"
              type="textarea"
              :rows="6"
              max-length="100"
              placeholder="最多输入100个字"
              v-model="moduleDetail.description"></el-input>
          </el-form-item>
          <el-form-item label="模块照片" prop="uploadFiles">
            <el-upload
              ref="upload"
              action="fileUpload"
              list-type="picture-card"
              :limit="3"
              :file-list="files"
              :on-exceed="onExceed"
              :before-upload="beforeUpload"
              :on-success="uploadSuccess"
              :on-preview="handlePictureCardPreview"
              :on-remove="handleRemove">
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible">
              <img width="100%" :src="dialogImageUrl" alt="">
            </el-dialog>
          </el-form-item>


          <el-form-item>
            <el-button type="primary" @click="updateDate" >确 定</el-button>
            <el-button @click="$emit('changeFlag')">返 回</el-button>
          </el-form-item>

        </el-form>
      </el-aside>


      <el-main>
        <el-table
          :height="tableHeight"
          border
          @sort-change
          :data="tableData">
          <el-table-column
            width="50"
            label="排序"
            type="index"
            align="center">
          </el-table-column>
          <el-table-column
            label="接口名称"
            align="center"
            prop="name"
            width="250">
          </el-table-column>
          <el-table-column
            label="接口时效（天）"
            align="center"
            prop="effectiveTime"
            width="150">
            <template slot-scope="scope">
              {{ (scope.row.effectiveTime/86400).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column
            label="接口成本（元）"
            align="center"
            prop="price"
            width="150">
          </el-table-column>
          <el-table-column
            align="center"
            label="操作">
            <template slot-scope="scope">
              <el-button
                v-if="scope.row.enabled==1"
                size="small"
                @click="enabledF(scope.row.id,false)"
                type="warning">禁用</el-button>
              <el-button
                v-else
                size="small"
                @click="enabledF(scope.row.id,true)">启用</el-button>
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
      </el-main>


    </el-container>
  </div>
</template>

<script>
  import { reform,getDetail,timestampToTime,fetchList,updateList,createNow,deleteNow,enabledNow,filterForm } from '@/api/common';

  export default {
    name: "index",
    props: [ 'moduleData' ],
    data(){
      return{
        example: 'moduleManager',
        example2: 'supplyAPI',

        //图片上传
        dialogImageUrl: '',
        dialogVisible: false,
        imgNum: '',
        detailId: '',
        files: [],
        params: {
          id: '',
          fields: '*,files'
        },

        tableHeight: 500,
        tableData:[],
        multipleSelection:'',
        loadingInstance:'',
        //禁用或启用
        enabledText:{
          true: '启用',
          false: '禁用'
        },
        //状态变更
        enabledQuery:{},
        moduleRule: {
          name: [
            { required: true, message: '请输入菜单名称', trigger: 'blur'}
          ]
          // ,
          // description: [
          //   { required: true, message: '请输入产品描述', trigger: 'blur'}
          // ]
        },
        moduleDetail:{
          name: '',
          description: '',
          uploadFiles: []
        },
        total:0,
        listQuery:{
          page:'0',
          size:10,
          fields: '*'
        }
      }
    },
    computed: {
      timestampToTime:function() {
        return timestampToTime
      }
    },
    mounted() {
      let height = this.$refs.height.offsetHeight;
      this.tableHeight = height - 63 - 128 - 30;
    },
    methods:{
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
            // if (this.form.enabled) {
            //   this.form.enabled = 1;
            // } else {
            //   this.form.enabled = 0;
            // }
            // if (JSON.stringify(this.form.productionManagers) != "{}") {
            //   for (let i = 0; i < this.form.productionManagers.length; i++) {
            //     this.$set(this.productValue, i, this.form.productionManagers[i].id);
            //   }
            // }
            //this.form.selectedOptions = ["", ""];
            // this.$set(this.form.selectedOptions, 0, this.form.province);
            // this.$set(this.form.selectedOptions, 1, this.form.city);
          })
        }
      },
      //上传方法
      handleRemove(file, fileList) {
        let _this = this;
          _this.files.forEach(function (item, index) {
            if (item.id == file.id) {
              _this.files.splice(index, 1);
            }
          });
          if (file.id) {
            deleteNow("moduleManagerFile", {id: file.id}).then(res => {
              this.$message({
                type: 'success',
                message: '图片删除成功!'
              });
            })
          }

      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      beforeUpload() {},
      uploadSuccess(res) {
        this.imgNum++;
        this.files.push(res.data);
        let num = this.files.length - 1;
        this.files[num].url = "http://" + this.files[num].url;
      },
      onExceed() {
        this.$message.error('上传图片数量不能超过三张图片!');
      },

      //禁用  启用
      enabledF(id,val) {
        this.enabledQuery = {
          id:id,
          enabled:val
        }
        enabledNow(this.example2,this.enabledQuery).then(response => {
          this.getUser()
          this.$notify({
            title: '成功',
            message: this.enabledText[data] + '成功',
            type: 'success',
            duration: 2000
          })
        })
      },

      //确认更新
      updateDate() {
        this.moduleDetail.id = this.moduleData.id;
        this.moduleDetail.uploadFiles = this.files;

        let json = {};
        for (let i = 0; i < this.moduleDetail.uploadFiles.length; i++) {
          if (this.moduleDetail.uploadFiles[i].id) {
            this.moduleDetail.uploadFiles[i] = {imgId: this.moduleDetail.uploadFiles[i].id};
          } else {
            for (let key in this.moduleDetail.uploadFiles[i]) {
              if (key != "uid" && key != "status") {
                json[key] = this.moduleDetail.uploadFiles[i][key];
              }
              if (key == "url") {
                json[key] = json[key].substr(7);
              }
            }
            this.moduleDetail.uploadFiles[i] = json;
          }
        }
        console.log(this.moduleDetail)

        this.$refs['moduleForm'].validate((valid) => {
          if(valid) {
            updateList(this.example,reform(this.moduleDetail, ['selectedOptions'])).then(response =>{
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
            }).catch(err=>{
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
        fetchList( this.example2,this.listQuery ).then((res) => {

          this.tableData = res.data;
          this.total = res.total;
        })
      },
      //重置
      resetForm(formInline) {
        this.dialogFlag = false;
        this.$refs[formInline].resetFields();
      }
    },
    watch:{
      moduleData:function(v,o) {
        this.getUser();
        this.detailId = v.id + '-' + (new Date()).valueOf();
        this.getMechanismDetail();
          this.moduleDetail = {
            name: v.name,
            description: v.description,
            uploadFiles: []
          }
      }
    }

  }
</script>

<style scoped>

</style>
