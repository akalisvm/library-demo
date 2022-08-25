<template>
  <div style="height: 58px;
    line-height: 50px;
    border-bottom: 1px solid #dcdfe6;
    display: flex;">
    <div style="padding-top: 18px">
      <el-breadcrumb separator-icon="ArrowRight">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <template v-for="(item, index) in breadList">
          <el-breadcrumb-item
            v-if="item.name !== 'layout' && item.name !== 'home'"
            :key="index"
            :to="{ path: '/' + item.name }"
            >{{ item.name }}</el-breadcrumb-item>
        </template>
      </el-breadcrumb>
    </div>
    <div style="flex: 1" />
    <div style="min-width: 80px; padding-top: 10px">
      <el-dropdown>
        <el-button type="text">
          <el-avatar :src="this.user.avatar" v-if="this.user.avatar !== ''" />
          <el-avatar :src="this.defaultAvatar" v-else />
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="info">个人资料</el-dropdown-item>
            <el-dropdown-item @click="exit">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: "Header",
  data() {
    return {
      username: JSON.parse(sessionStorage.getItem("user")).username,
      path: this.$route.path,
      form: {},
      user: {},
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      breadList: []
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
    request.get("/user/" + this.username).then(res => {
      if(res.code === '0') {
        this.user = res.data
      }
    })
    this.getMatched()
  },
  watch: {
    $route(to, form) {
      this.breadList = this.$route.matched
    }
  },
  methods: {
    getMatched() {
      this.breadList = this.$route.matched
    },
    info() {
      this.$router.push("/person")
    },
    exit() {
      this.$router.push("/login")
    }
  }
}
</script>
