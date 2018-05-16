<template>
  <scroll-bar>
    <el-menu mode="vertical" :default-active="$route.path" :collapse="isCollapse" background-color="#304156" text-color="#fff" active-text-color="#409EFF">
      <div class="app-information" style="font-size:18px;">
        小金数据管理平台
      </div>
      <sidebar-item :routes="routes"></sidebar-item>
    </el-menu>
  </scroll-bar>
</template>

<script>
  import { mapGetters } from 'vuex'
  import SidebarItem from './SidebarItem'
  import ScrollBar from '@/components/ScrollBar'

  export default {
    components: { SidebarItem, ScrollBar },
    computed: {
      ...mapGetters([
        'sidebar'
      ]),
      routes() {
        return this.$store.getters.menus
        // return this.$router.options.routes
      },
      isCollapse() {
        return !this.sidebar.opened
      }
    }
  }
</script>
<style scoped>
  .app-information {
    height: 49px;
    line-height: 49px;
    text-align: center;
    color: #fff;
    font-weight: bold;
    font-size: 20px;
  }
</style>
