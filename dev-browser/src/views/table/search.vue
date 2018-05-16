<template>
  <div>
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item v-for="item in items" :label="item.label" :key="item.label">
        <el-input v-model="lineInfo[item.name]" :placeholder="item.placeholder"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit(lineInfo)">查询</el-button>
        <el-button @click="resetForm(lineInfo)">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    name:"searchLine",
    props: ['items','lineInfo']
  }
</script>
