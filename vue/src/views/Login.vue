<template>
  <div style="width: 100%; height: 100vh; overflow: hidden">
    <div style="width: 400px; margin: 150px auto">
      <el-card>
        <div style="color: black; font-size: 30px; font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif; text-align: center; padding-bottom: 30px">
          欢迎登录图书管理系统
        </div>
        <el-form ref="form" :model="form" :rules="rules" @keyup.enter.native="login">
          <el-form-item>
            <el-input prefix-icon="UserFilled" v-model="form.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item>
            <el-input prefix-icon="Lock" v-model="form.password" type="password" autocomplete="off" show-password placeholder="请输入密码" />
          </el-form-item>
          <el-form-item>
            <div style="display: flex">
              <el-input prefix-icon="Key" v-model="form.validCode" style="width: 50%" placeholder="请输入验证码"></el-input>
              <ValidCode @input="createValidCode" style="padding-left: 50px" ref="code" />
            </div>
          </el-form-item>
          <div style="text-align: center">
            <el-button type="primary" @click="login" style="width: 100%">立即登录</el-button>
          </div>
          <div style="text-align: center; padding-top: 10px">
            <el-button type="text" @click="goToRegister" style="width: 100%">前往注册</el-button>
          </div>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>

import request from "@/utils/request";
import ValidCode from "@/components/ValidCode";
import {ref} from "vue";

export default {
  name: 'Login',
  components: { ValidCode },
  data() {
    return {
      form: {},
      rules: {
        username: [
          { required: true, message: '请输入用户名称', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur'}
        ]
      },
      validCode: ''
    }
  },
  created() {
    sessionStorage.removeItem("user")
  },
  methods: {
    createValidCode(data) {
      this.validCode = data
    },
    login() {
      this.$refs['form'].validate((valid) => {
        if(valid) {
          if(!this.form.validCode) {
            this.$message.error("请填写验证码")
            this.$refs['code'].refreshValidCode()
            return
          }
          if(this.form.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
            this.$message.error("验证码错误")
            this.$refs['code'].refreshValidCode()
            return
          }
          request.post("/user/login", this.form).then(res => {
            if(res.code === "0") {
              this.$message({
                type: "success",
                message: "登录成功！"
              })
              sessionStorage.setItem("user", JSON.stringify(this.form))
              this.$router.push("/home")
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
              this.$refs['code'].refreshValidCode()
            }
          })
        }
      })
    },
    goToRegister() {
      this.$router.push("/register")
    }
  }
}
</script>