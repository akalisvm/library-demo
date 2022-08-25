<template>
  <el-row>
    <div style="width: 199px;
    height: 60px;
    background-color: #2b2f3a;
    display: flex;
    font-weight: bold;
    font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif ;
    color: white;"
    >
      <span style="padding-top: 18px; padding-left: 25px">图书馆后台管理系统</span>
    </div>
  </el-row>
  <el-row>
    <el-menu
        style="width: 200px; min-height: calc(100vh - 80px); font-weight: bold"
        :default-active="path"
        router
        background-color="#304156"
        text-color="#ffffff"
        active-text-color="#3888e4"

    >

      <el-menu-item index="/home">
        <el-icon><HomeFilled /></el-icon>首页
      </el-menu-item>
      <el-menu-item index="/user" v-if="user.role === 'admin'">
        <el-icon><Avatar /></el-icon>用户列表
      </el-menu-item>
      <el-menu-item index="/book">
        <el-icon><Management /></el-icon>图书列表
      </el-menu-item>
      <el-menu-item index="/borrow">
        <el-icon><List /></el-icon>借阅管理
      </el-menu-item>
      <el-menu-item index="/comment">
        <el-icon><Comment /></el-icon>管理员公告
      </el-menu-item>
    </el-menu>
  </el-row>
</template>

<script>

import request from "@/utils/request";

export default {
  name: "Aside",
  data() {
    return {
      user: {},
      path: this.$route.path
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
    request.get("/user/" + this.user.username).then(res => {
      if(res.code === '0') {
        this.user = res.data
      }
    })
  }
}

</script>

