<template>
  <div ref="height" class="main">
    <el-form :inline="true" :model="pushModel" class="demo-form-inline">
      <el-form-item label="模板名称">
        <el-input v-model="pushModel.name" placeholder="请输入模板名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
        <el-button type="primary" @click="onClear">清除</el-button>
      </el-form-item>
      <el-select v-model="value" @change="selectChange">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
          :disabled="item.disabled">
        </el-option>
      </el-select>
      <el-form-item class="right">
        <el-button v-if="getButtons('add')" type="success" @click="onAdd">新增</el-button>
        <el-button v-if="getButtons('delete')" type="warning" @click="onRemove">删除</el-button>
      </el-form-item>
    </el-form>

    <el-table :height="tableHeight" border
              ref="pushModelTable"
              :data="pushModelTable"
              tooltip-effect="dark"
              style="width: 100%;"
              @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column
        label="模板名称"
        align="center"
        prop="name">
        <template slot-scope="scope">
          <span>{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <!--阿里模板-->
      <el-table-column
        v-if="state === 0 ? true : false"
        label="阿里服务签名名称"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.aliSmsSignName}}</span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="state === 0 ? true : false"
        label="阿里服务模板编码"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.aliSmsTemplateCode}}</span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="state === 0 ? true : false"
        label="阿里服务模板内容"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.aliSmsTemplateContent}}</span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="state === 0 ? true : false"
        label="阿里服务模板参数"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.aliSmsTemplateParam}}</span>
        </template>
      </el-table-column>
      <!--站内信-->
      <el-table-column
        v-if="state === 1 ? true : false"
        label="站内信模板"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.messageTemplate}}</span>
        </template>
      </el-table-column>
      <!--企业微信-->
      <el-table-column
        v-if="state === 2 ? true : false"
        label="企业微信代理编码"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.wxworkAgentId}}</span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="state === 2 ? true : false"
        label="企业微信代理秘钥"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.wxworkAgentSecret}}</span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="state === 2 ? true : false"
        label="企业微信代理模板"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.wxworkTemplate}}</span>
        </template>
      </el-table-column>
      <!--微信公众号-->
      <el-table-column
        v-if="state === 3 ? true : false"
        label="微信公众号模板编码"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.wxpublicTemplateId}}</span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="state === 3 ? true : false"
        label="微信公众号模板内容"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.wxpublicTemplateContent}}</span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="state === 3 ? true : false"
        label="微信公众号模板JSON数据"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.wxpublicTemplateData}}</span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="state === 3 ? true : false"
        label="微信公众号模板链接"
        align="center">
        <template slot-scope="scope">
          <span>{{scope.row.wxpublicTemplateUrl}}</span>
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
      title="添加推送模板"
      width="600px"
      :visible.sync="dialogAddData">

      <!--:rules="rules"-->
      <el-form ref="addForm" :model="addForm" class="demo-form-inline" label-width="140px">
        <el-form-item label="模板类型" prop="noticeChannel">
          <el-select class="width" v-model="addForm.noticeChannel" @change="selectChangeDialog">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
              :disabled="item.disabled">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="模板名称" prop="name">
          <el-input class="width" v-model="addForm.name" placeholder="请输入用户"></el-input>
        </el-form-item>
        <!--阿里-->
        <el-form-item v-if="formState == 0 ? true : false" label="阿里服务签名名称" prop="aliSmsSignName">
          <el-input class="width" v-model="addForm.aliSmsSignName" placeholder="请输入阿里服务签名名称"></el-input>
        </el-form-item>
        <el-form-item v-if="formState == 0 ? true : false" label="阿里服务模板编码" prop="aliSmsTemplateCode">
          <el-input class="width" v-model="addForm.aliSmsTemplateCode" placeholder="请输入阿里服务模板编码"></el-input>
        </el-form-item>
        <el-form-item v-if="formState == 0 ? true : false" label="阿里服务模板内容" prop="aliSmsTemplateContent">
          <el-input class="width" v-model="addForm.aliSmsTemplateContent" placeholder="请输入阿里服务模板内容"></el-input>
        </el-form-item>
        <el-form-item v-if="formState == 0 ? true : false" label="阿里服务模板参数" prop="aliSmsTemplateParam">
          <el-input class="width" v-model="addForm.aliSmsTemplateParam" placeholder="请输入阿里服务模板参数"></el-input>
        </el-form-item>
        <!--站内信-->
        <el-form-item v-if="formState == 1 ? true : false" label="站内信模板" prop="messageTemplate">
          <el-input class="width" v-model="addForm.messageTemplate" placeholder="请输入站内信模板"></el-input>
        </el-form-item>
        <!--企业微信-->
        <el-form-item v-if="formState == 2 ? true : false" label="企业微信代理编码" prop="wxworkAgentId">
          <el-input class="width" v-model="addForm.wxworkAgentId" placeholder="请输入企业微信代理编码"></el-input>
        </el-form-item>
        <el-form-item v-if="formState == 2 ? true : false" label="企业微信代理秘钥" prop="wxworkAgentSecret">
          <el-input class="width" v-model="addForm.wxworkAgentSecret" placeholder="请输入企业微信代理秘钥"></el-input>
        </el-form-item>
        <el-form-item v-if="formState == 2 ? true : false" label="企业微信代理模板" prop="wxworkTemplate">
          <el-input class="width" v-model="addForm.wxworkTemplate" placeholder="请输入企业微信代理模板"></el-input>
        </el-form-item>
        <!--微信公众号-->
        <el-form-item v-if="formState == 3 ? true : false" label="模板编码" prop="wxpublicTemplateId">
          <el-input class="width" v-model="addForm.wxpublicTemplateId" placeholder="请输入微信公众号模板编码"></el-input>
        </el-form-item>
        <el-form-item v-if="formState == 3 ? true : false" label="模板内容" prop="wxpublicTemplateContent">
          <el-input class="width" v-model="addForm.wxpublicTemplateContent" placeholder="请输入微信公众号模板内容"></el-input>
        </el-form-item>
        <el-form-item v-if="formState == 3 ? true : false" label="模板JSON数据" prop="wxpublicTemplateData">
          <el-input class="width" v-model="addForm.wxpublicTemplateData" placeholder="请输入微信公众号模板JSON数据"></el-input>
        </el-form-item>
        <el-form-item v-if="formState == 3 ? true : false" label="模板链接" prop="wxpublicTemplateUrl">
          <el-input class="width" v-model="addForm.wxpublicTemplateUrl" placeholder="请输入微信公众号模板链接"></el-input>
        </el-form-item>

        <!--<el-form-item label="推送内容" prop="name">
          <el-input type="textarea" class="width" v-model="addForm.name" placeholder="请输入推送内容"></el-input>
        </el-form-item>
        <el-form-item label="用户角色" prop="name">
          <el-checkbox-group v-model="addForm.name">
            <el-checkbox label="管理员"></el-checkbox>
            <el-checkbox label="普通用户"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="触发行为" prop="industryType">
          <el-select class="width" v-model="addForm.industryType" placeholder="请选择行业">
            <el-option label="用户注册机构时" value="FINANCE"></el-option>
            <el-option label="机构审核通过时" value="LEASE"></el-option>
            <el-option label="机构属性变化时(企业-集团)" value="TALENTRECRUITMENT"></el-option>
            <el-option label="机构创建团队时" value="INTERMEDIARIES"></el-option>
          </el-select>
        </el-form-item>-->
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="cancelCreate">取 消</el-button>
          <el-button type="primary" @click="createData">确 定</el-button>
        </span>
    </el-dialog>

  </div>
</template>

<script>
  import {
    getButtons,
    timestampToTime,
    createNow,
    deleteNow,
    vagueList,
    vagueFormat
  } from '@/api/common';

  export default {
    name: "push-model",
    data() {
      return {
        example: 'noticeTemplate',
        tableHeight: 500,
        pushModel: {
          name: ''
        },
        pushModelTable: [],
        total: 0,
        listQuery: {
          page: 1,
          sizes: [10, 20, 30, 40],
          size: 10,
        },
        state: 0,
        searchParams: {
          name: '',
          noticeChannel: "SMS",
          page: 0,
          size: 10,
          fields: '*'
        },
        addForm: {
          name: '',
          noticeChannel: 'SMS',
          aliSmsSignName: '',
          aliSmsTemplateCode: '',
          aliSmsTemplateContent: '',
          aliSmsTemplateParam: '',

          messageTemplate: '',

          wxworkAgentId: '',
          wxworkAgentSecret: '',
          wxworkTemplate: '',

          wxpublicTemplateId: '',
          wxpublicTemplateContent: '',
          wxpublicTemplateData: '',
          wxpublicTemplateUrl: ''
        },
        dialogAddData: false,
        options: [
          {
            value: 'SMS',
            label: '阿里云'
          }, {
            value: 'MESSAGE',
            label: '站内信'
          }, {
            value: 'WXWORK',
            label: '企业微信'
          }, {
            value: 'WXPUBLIC',
            label: '微信公众号'
          }, {
            value: 'EMAIL',
            label: '邮箱',
            disabled: true
          },
          {
            value: 'APP',
            label: '移动端',
            disabled: true
          }
        ],
        value: "SMS",
        formState: 0,
        multipleSelection: []
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
      this.getPushModelData();
    },
    methods: {
      selectChange(val){
        let select = ["SMS","MESSAGE","WXWORK","WXPUBLIC","EMAIL","APP"];
        this.searchParams.noticeChannel = val;
        this.state = select.indexOf(val);
        this.getPushModelData();
      },
      onSubmit() {
        this.searchParams.name = this.pushModel.name;
        this.getPushModelData();
      },
      onClear() {
        this.pushModel.name = '';
        this.getPushModelData();
      },
      onAdd() {
        this.dialogAddData = true;
      },
      onRemove(){
        this.multipleSelection.forEach((val, index) => {
          deleteNow(this.example, {id: val.id}).then(res => {
            if (index == (this.multipleSelection.length - 1)) {
              this.getPushModelData();
              this.$notify({
                title: '成功',
                message: '模板移除成功',
                type: 'success',
                duration: 2000
              })
            }
          })
        })
      },
      getPushModelData() {
        vagueList(this.example,vagueFormat(this.searchParams,['name'])).then( res => {
          console.log(res);
          this.pushModelTable = res.data;
          this.total = res.total;
        })
      },
      handleSizeChange(val) {
        this.searchParams.size = val;
        this.getPushModelData();
      },
      handleCurrentChange(val) {
        this.searchParams.page = val - 1;
        this.getPushModelData();
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
      createData() {
        createNow(this.example, this.addForm).then( res => {
          this.$notify({
            title: '成功',
            message: '模板添加成功',
            type: 'success',
            duration: 2000
          });
          this.dialogAddData = false;
          this.getPushModelData();
          this.resetForm();
        })
      },
      resetForm() {
        this.$refs['addForm'].resetFields();
      },
      selectChangeDialog(val){
        let select = ["SMS","MESSAGE","WXWORK","WXPUBLIC","EMAIL","APP"];
        this.formState = select.indexOf(val);
      },
      handleSelectionChange(selection){
        this.multipleSelection = selection;
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
