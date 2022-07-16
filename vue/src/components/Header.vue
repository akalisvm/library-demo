<template>
  <div style="height: 50px; line-height: 50px; border-bottom: 1px solid #ccc; display: flex">
    <div style="width: 200px; padding-left: 50px; font-weight: bold; font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif ; color: dodgerblue">
      后台管理
    </div>
    <div style="flex: 1"></div>
    <div style="min-width: 200px; padding-left: 50px; padding-top: 10px">
      <el-dropdown>
        <el-button type="text">
          陈天昂<el-icon class="el-icon--right"><arrow-down /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>个人信息</el-dropdown-item>
            <el-dropdown-item>退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

  </div>
</template>

<script>
export default {
  name: "Header"
}
</script>

<style scoped>

.example-showcase .el-dropdown + .el-dropdown {
  margin-left: 15px;
}
.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}

</style>