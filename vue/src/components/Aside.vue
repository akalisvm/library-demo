<template>
  <el-row class="tac">
    <el-col :span="12">
      <el-menu
          style="width: 200px; min-height: calc(100vh - 50px)"
          default-active="2"
          class="el-menu-vertical-demo"
          @open="handleOpen"
          @close="handleClose"
      >
        <el-sub-menu index="1">
          <template #title>
            <el-icon><location /></el-icon>
            <span>导航一</span>
          </template>
          <el-menu-item index="1-1">选项一</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="3" disabled>
          <el-icon><document /></el-icon>
          <span>导航二</span>
        </el-menu-item>
      </el-menu>
    </el-col>
  </el-row>
</template>



