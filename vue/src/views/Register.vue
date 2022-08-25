<template>
  <div style="width: 100%; height: 100vh; overflow: hidden">
    <div style="width: 400px; margin: 150px auto">
      <el-card>
        <div style="color: black; font-size: 30px; font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif; text-align: center; padding-bottom: 30px">
          欢迎注册图书管理系统
        </div>
        <el-form ref="form" :model="form" :rules="rules" @keyup.enter.native="register">
          <el-form-item prop="username">
            <el-input prefix-icon="UserFilled" v-model="form.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="Lock" v-model="form.password" type="password" autocomplete="off" show-password placeholder="请输入密码" />
          </el-form-item>
          <el-form-item prop="confirm">
            <el-input prefix-icon="Lock" v-model="form.confirm" type="password" autocomplete="off" show-password placeholder="请输入确认密码" />
          </el-form-item>
          <el-form-item style="text-align: center">
            <el-radio-group v-model="form.role">
              <el-radio label="admin">管理员</el-radio>
              <el-radio label="user">普通用户</el-radio>
            </el-radio-group>
          </el-form-item>
          <div style="text-align: center">
            <el-button type="primary" @click="register" style="width: 100%">立即注册</el-button>
          </div>
          <div style="text-align: center; padding-top: 10px">
            <el-button type="text" @click="goToLogin" style="width: 100%">返回登录</el-button>
          </div>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: 'Register',
  data() {
    return {
      form: {},
      rules: {
        username: [
          { required: true, message: '请输入用户名称', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur'}
        ],
        confirm: [
          { required: true, message: '请输入确认密码', trigger: 'blur'}
        ]
      },
    }
  },
  methods: {
    register() {
      this.$refs['form'].validate((valid) => {
        if(valid) {
          if(this.form.password !== this.form.confirm) {
            this.$message({
              type: "error",
              message: "两次密码输入不一致！"
            })
            return;
          }
          request.post("/user/register", this.form).then(res => {
            if(res.code === "0") {
              this.$message({
                type: "success",
                message: "注册成功！"
              })
              this.$router.push("/login")
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }
      })
    },
    goToLogin() {
      this.$router.push("/login")
    }
  }
}
</script>