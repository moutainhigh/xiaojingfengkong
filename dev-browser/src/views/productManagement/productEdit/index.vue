<template>
  <el-container>

    <el-form align="left"
             :rules="productRule"
             :model="productDetail"
             ref="productForm"
             style="margin-top:50px;"
             label-width="150px" >
      <el-form-item
        label="产品名称"
        prop="name">
        <el-input v-model="productDetail.name"></el-input>
      </el-form-item>

      <el-form-item
        label="产品描述"
        prop="description">
        <el-input
          style="width:300px;"
          type="textarea"
          :rows="6"
          max-length="100"
          placeholder="最多输入100个字"
          v-model="productDetail.description"></el-input>
      </el-form-item>


      <el-form-item
        label="产品价格(元)"
        prop="price">
        <el-input v-model="productDetail.price"></el-input>
      </el-form-item>

      <div class="dndList" style="margin-left:40px;" >
        <div class="dndList-list" :style="{width:width1}">
          <h3 style="width:200px;">已选择模块</h3>
          <draggable :list="list1" class="dragArea" :options="{group:'name'}">
            <div class="list-complete-item" v-for="element in list1" :key='element.id'>
              <div class="list-complete-item-handle"> {{element.name}}</div>
              <div style="position:absolute;right:0px;">
                <span style="float: right ;margin-top: -16px;margin-right:5px;" @click="deleteEle(element)">
                  <i style="color:#ff4949" class="el-icon-delete"></i>
                </span>
              </div>
            </div>
            <div v-show="list1.length==0">没有选择模块</div>
          </draggable>
        </div>
        <div class="dndList-list" :style="{width:width2}">
          <h3 style="width:200px;">所有模块</h3>
          <draggable :list="filterList2" class="dragArea" :options="{group:'name'}">
            <div class="list-complete-item" v-for="element in filterList2" :key='element.id'>
              <div class='list-complete-item-handle2'> {{element.name}}</div>
              <div style="position:absolute;right:0px;">
                <span style="float: right ;margin-top: -16px;margin-right:5px;" @click="pushEle(element)">
                  <i style="color:#ff4949" class="el-icon-plus"></i>
                </span>
              </div>
            </div>
          </draggable>
        </div>
      </div>

      <el-form-item>
        <el-button type="primary" @click="updateDate" >确 定</el-button>
        <el-button @click="$emit('changeFlag')">返 回</el-button>
      </el-form-item>

    </el-form>

  </el-container>
</template>

<script>
  import { fetchList,updateList,getDetail } from '@/api/common';
  import draggable from 'vuedraggable'
  import { validPrice } from '@/utils/validate'

  export default {
    name: "index",
    props: ['productDate'],
    components: { draggable },
    data() {
      var validatePrice = (rule, value, callback) => {
        if(value === '') {
          callback ( new Error('请输入价格'))
        }else if( validPrice(value) ) {
          callback()
        }else{
          callback(new Error('请输入最多带有两位小数的数字'))
        }
      }
      return {
        example:'productionManager',
        //产品详情
        productDetail:{
          moduleManagers:[]
        },

        loadingInstance:'',
        productRule: {
          name: [
            { required: true, message: '请输入菜单名称', trigger: 'blur'}
          ],
          description: [
            { required: true, message: '请输入产品描述', trigger: 'blur'}
          ],
          price: [
            { required: true, validator: validatePrice, trigger: 'blur'}
          ]
        },
        listQuery:{
          page:'0',
          size:10,
          fields: '*'
        },
        list1: [],
        list2: [],
        width1: {
          type: String,
          default: '48%'
        },
        width2: {
          type: String,
          default: '48%'
        }
      }
    },
    computed: {
      filterList2() {
        return this.list2.filter(v => {
          if (this.isNotInList1(v)) {
            return v
          }
          return false
        })
      }
    },
    mounted() {
      this.loadingInstance = this.$loading({ fullscreen: true });
      this.getModule();
    },
    methods:{
      //获取模块
      getModule() {
        fetchList('moduleManager',this.listQuery).then(response => {
          this.list2 = response.data;
        })
      },
      //确认更新
      updateDate() {
        this.$refs['productForm'].validate((valid) => {
          if(valid) {
            let listArr = [];
            if(this.list1.length!=0) {
              this.list1.forEach(function(val,index) {
                listArr.push({'id':val.id})
              })
            }

            this.productDetail.id = this.productDate.id;
            this.productDetail.moduleManagers = listArr;

            updateList(this.example,this.productDetail).then(response =>{
              //this.resetForm('productForm');
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
      //重置
      resetForm(formInline) {
        this.dialogFlag = false;
        this.$refs[formInline].resetFields();
      },
      //多项选择
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },

      isNotInList1(v) {
        return this.list1.every(k => v.id !== k.id)
      },
      isNotInList2(v) {
        return this.list2.every(k => v.id !== k.id)
      },
      deleteEle(ele) {
        for (const item of this.list1) {
          if (item.id === ele.id) {
            const index = this.list1.indexOf(item)
            this.list1.splice(index, 1)
            break
          }
        }
        if (this.isNotInList2(ele)) {
          this.list2.unshift(ele)
        }
      },
      pushEle(ele) {
        this.list1.push(ele)
      }
    },
    watch:{
      productDate:function(val, oldVal){
        let that = this;
        if(val!='') {

          this.list1.forEach(function(val) {
            that.filterList2.push(val)
          })
          this.list1 = []

          this.productDetail = {
            name: val.name || '',
            description: val.description || '',
            price: val.price || '',
          }
        }

        if(val.moduleManagers.length!=undefined) {
          val.moduleManagers.forEach(function(val,index) {
            that.list1.push(val)
          })
        }

      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .dndList {
    background: #fff;
    padding-bottom: 40px;
    &:after {
      content: "";
      display: table;
      clear: both;
    }
    .dndList-list {
      float: left;
      padding-bottom: 30px;
      &:first-of-type {
        margin-right: 2%;
      }
      .dragArea {
        margin-top: 15px;
        min-height: 50px;
        padding-bottom: 30px;
      }
    }
  }

  .list-complete-item {
    cursor: pointer;
    position: relative;
    font-size: 14px;
    padding: 5px 12px;
    margin-top: 4px;
    border: 1px solid #bfcbd9;
    transition: all 1s;
  }

  .list-complete-item-handle {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin-right: 50px;
  }

  .list-complete-item-handle2 {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin-right: 20px;
  }

  .list-complete-item.sortable-chosen {
    background: #4AB7BD;
  }

  .list-complete-item.sortable-ghost {
    background: #30B08F;
  }

  .list-complete-enter,
  .list-complete-leave-active {
    opacity: 0;
  }
</style>
