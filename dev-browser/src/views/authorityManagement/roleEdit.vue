<template>
  <div id="menuManagement" class="main" ref="childHeight">
    <el-form :inline="true" :model="listQuery" ref="listQuery" class="demo-form-inline">
      <el-form-item label="角色名称" prop="name">
        <el-input v-model="listQuery.name" placeholder="请输入角色名称"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="node">
        <el-input v-model="listQuery.note" placeholder="请输入备注"></el-input>
      </el-form-item>
    </el-form>

    <el-table
      :data="roleData"
      :row-style="showRow"
      border
      ref="multipleTable"
      :height="childTableHeight"
      @selection-change="handleSelectionChange"
      v-bind="$attrs">

      <el-table-column
        align="center"
        type="selection"
        width="100">
      </el-table-column>

      <el-table-column v-if="columns.length===0" width="150">
        <template slot-scope="scope">
          <span v-for="space in scope.row._level" class="ms-tree-space" :key="space"></span>
          <span class="tree-ctrl" v-if="iconShow(0,scope.row)" @click="toggleExpanded(scope.$index)">
          <i v-if="!scope.row._expanded" class="el-icon-plus"></i>
          <i v-else class="el-icon-minus"></i>
        </span>
          {{scope.$index}}
        </template>
      </el-table-column>

      <el-table-column v-else v-for="(column, index) in columns" :key="column.name" :label="column.name" :width="column.width">

        <template slot-scope="scope">

          <span v-if="index === 0" v-for="space in scope.row._level" class="ms-tree-space" :key="space"></span>

          <span class="tree-ctrl" v-if="iconShow(index,scope.row)" @click="toggleExpanded(scope.$index)">

          <i v-if="!scope.row._expanded" class="el-icon-plus"></i>
          <i v-else class="el-icon-minus"></i>

        </span>
          {{scope.row.name}}
        </template>
      </el-table-column>

      <el-table-column
        align="left"
        label="按钮权限">
        <template slot-scope="scope">

          <el-checkbox-group class="checkgroup" v-model="checkList" >
            <template v-for="el in scope.row.buttons">
              <el-checkbox :key="el.id" :label="el.id">{{el.name}}</el-checkbox>
            </template>
          </el-checkbox-group>

        </template>
      </el-table-column>

      <slot></slot>
    </el-table>

    <el-row type="flex" justify="center" style="margin: 50px 0">
      <el-button @click="$emit('changeShow');reform()">返回</el-button>
      <el-button type="primary" @click="submitFn">保存</el-button>
    </el-row>

  </div>
</template>

<script>
  import {getMenu} from '@/api/role'
  import {arrayDeleteItem,updateList,getDetail} from '@/api/common'
  import treeToArray from './eval'
  import store  from '../../store'

  export default {
    name: 'roleEdit',
    props:{
      id:'',
      editShow:'',
      evalFunc: Function,
      evalArgs: Array,
      expandAll: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        roleData:[],
        childTableHeight: 500,
        listQuery: {
          name: null,
          note: null,
        },
        params:{
          fields:'*,buttons',
          page: 0,
          size: 1000
        },
        rules: {
          name: [
            {required: true, message: '请输入角色名称', trigger: 'blur'},
          ],
          code: [
            {required: true, message: '请输入角色标识', trigger: 'blur'},
          ]
        },
        dataList: [],
        itemMenus:[],
        itemBtns:[],

        data: [],
        columns: [
          {
            name: '菜单名称',
            value: 'name',
            width: 600
          }
        ],
        checked: false,
        //菜单选中后的id数组
        multipleSelection: [],
        checkList:[],
        //回填的菜单id数组
        menuSelectId: [],
        //所有的菜单tree数组
        allMenu: [],
        selectIndex: []
      }
    },
    computed: {
    },
    mounted() {
    },
    methods: {
      // 格式化数据源
      formatData: function(val) {
        let tmp
        this.allMenu = [];
        if (!Array.isArray(val)) {
          tmp = [val]
        } else {
          tmp = val
        }
        const func = this.evalFunc || treeToArray
        const args = this.evalArgs ? Array.concat([tmp, this.expandAll], this.evalArgs) : [tmp, this.expandAll]

        this.allMenu = func.apply(null, args);

        return func.apply(null, args)
      },

      // 多项选择
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },

      showRow: function (row) {

        //const show = (row.row.parent ? (row.row.parent._expanded && row.row.parent._show) : true)
        const show = true;
        row.row._show = show
        return show ? 'animation:treeTableShow 1s;-webkit-animation:treeTableShow 1s;' : 'display:none;'

      },

      // 切换下级是否展开
      toggleExpanded: function (trIndex) {

        const record = this.formatData[trIndex]
        record._expanded = !record._expanded

      },

      // 图标显示
      iconShow(index, record) {

        return (index === 0 && record.children && record.children.length > 0)

      },

      //保存
      submitFn() {
        let menusId = [];
        let buttonsId = []
        const roleId = this.id;

        this.checkList.forEach((val) => {
          buttonsId.push({'id': val})
        })

        //console.log(buttonsId)

        if (this.multipleSelection.length == 0) {
          return false
        } else {
          this.multipleSelection.forEach((val) => {
            menusId.push({'id': val.id});
          })
        }

        const query = {
          id: roleId,
          menus: menusId,
          buttons: buttonsId,
          ...this.listQuery
        }

        updateList('role', query).then(res => {
          if (res.errorCode === 0) {
            this.reform()
            this.$notify.success({
              title: 'Info',
              message: '编辑成功',
              showClose: false
            });
            this.$emit('changeShow')
          } else {
            this.$notify.error({
              title: 'Info',
              message: res.errorMessage,
              showClose: false
            })
          }
        })
      },


      getData() {//获取tree列表数据

        var code = store.getters.roles[0].code;

        if(code != "DEVELOPER") {
          this.params.developerMenu = false;
        }

        getMenu(this.params).then(res => {

          this.roleData = this.formatData(res.data);

        }).then(() => {
          this.getInfo()
        })
      },
      getInfo() {//获取该角色基本信息，包括角色名称、角色标识、备注和权限（menus、buttons）
        var query = {
          id: this.id,
          fields: "*,menus,buttons"
        };
        getDetail('role', query).then(res => {
          if (res.errorCode === 0) {
            this.listQuery.name = res.data.name;
            this.listQuery.note = res.data.note;

            let buttonId = [];
            if(res.data.buttons.length!=undefined) {
              res.data.buttons.forEach((val) => {
                buttonId.push(val.id)
              })
            }

            this.checkList = buttonId;
            this.menuSelectId = [];

            if(res.data.menus.length!=undefined) {
              res.data.menus.forEach((val) => {
                this.menuSelectId.push(val.id)
              })
            }

            //回填
            this.selectIndex = []
            this.menuSelectId.forEach( (val) => {
              this.allMenu.forEach( (el,index) => {
                if(val == el.id) {
                  this.selectIndex.push(index)
                  return
                }
              })
            })
            this.selectIndex.forEach( val => {
              this.$refs.multipleTable.toggleRowSelection(this.allMenu[val],true)
            })

          }
        })
      },
      //编辑页复原
      reform(){
        this.checkList = [];
        this.selectIndex.forEach( val => {
          this.$refs.multipleTable.toggleRowSelection(this.allMenu[val],false)
        })
        this.selectIndex = []
      }
    },
    watch:{//监控编辑按钮的点击，点击编辑的时候会传一个id值
      editShow(val,oval) {
        if(val == true){
          let height = this.$refs.childHeight.offsetHeight;
          this.childTableHeight = height - 63 - 140 - 30;
          console.log(height)
          console.log(this.childTableHeight)
          this.getData();
        }
      }
    }
  }
</script>

<style rel="stylesheet/css">
  @keyframes treeTableShow {
    from {opacity: 0;}
    to {opacity: 1;}
  }
  @-webkit-keyframes treeTableShow {
    from {opacity: 0;}
    to {opacity: 1;}
  }
</style>
<style lang="scss" rel="stylesheet/scss" scoped>
  $color-blue: #2196F3;
  $space-width: 18px;
  .ms-tree-space {
    position: relative;
    top: 1px;
    display: inline-block;
    font-style: normal;
    font-weight: 400;
    line-height: 1;
    width: $space-width;
    height: 14px;
    &::before {
      content: ""
    }
  }
  .processContainer{
    width: 100%;
    height: 100%;
  }
  table td {
    line-height: 26px;
  }

  .tree-ctrl{
    position: relative;
    cursor: pointer;
    color: $color-blue;
    margin-left: -$space-width;
  }
</style>
