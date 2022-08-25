<template>
  <el-row>
    <el-col :span="4" />
    <el-col :span="16">
      <div>
        <el-card style=" margin: 10px">
          <el-form ref="form" :model="form" label-width="80px" @keyup.enter.native="save">
            <el-row>
              <el-col :span="10" />
              <el-col :span="4">
                <el-avatar :size="180" style="margin-bottom: 20px" v-if="this.form.avatar !== ''">
                  <el-image :src="this.form.avatar" :preview-src-list="[this.form.avatar]" />
                </el-avatar>
                <el-avatar :size="180" style="margin-bottom: 20px" v-else>
                  <el-image :src="this.defaultAvatar" />
                </el-avatar>
              </el-col>
              <el-col :span="10" />
            </el-row>
            <el-form-item label="头像">
              <el-upload :action="url"
                         ref="upload"
                         :on-success="uploaded"
                         :limit="1"
                         :on-exceed="handleExceed">
                <el-button type="primary">点击上传</el-button>
              </el-upload>
            </el-form-item>
            <el-form-item label="用户名">
              <el-input v-model="form.username" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="form.password" type="password" autocomplete="off" show-password />
            </el-form-item>
            <el-form-item label="年龄">
              <el-input v-model="form.age"></el-input>
            </el-form-item>
            <el-form-item label="电话">
              <el-input v-model="form.phone"></el-input>
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="form.email"></el-input>
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="form.gender">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
                <el-radio label="未知">未知</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="地址">
              <el-input v-model="form.address" type="textarea"></el-input>
            </el-form-item>
          </el-form>
          <div style="text-align: center">
            <el-button @click="this.$router.back()">返回</el-button>
            <el-button type="primary" @click="save">保存</el-button>
          </div>
        </el-card>
      </div>
    </el-col>
    <el-col :span="4" />
  </el-row>
</template>

<script>

import request from "@/utils/request";

function getCookie(cookie_name) {
  const allCookies = document.cookie;
  let cookie_pos = allCookies.indexOf(cookie_name);
  if (cookie_pos !== -1) {
    cookie_pos = cookie_pos + cookie_name.length + 1;
    let cookie_end = allCookies.indexOf(";", cookie_pos);
    if (cookie_end === -1) {
      cookie_end = allCookies.length;
    }
    var value = unescape(allCookies.substring(cookie_pos, cookie_end));
  }
  return value;
}

export default {
  name: "Person",
  data() {
    return {
      form: {},
      user: {},
      url: 'http://' + window.server.ip + '/api/upload/avatar',
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}"
    this.form = JSON.parse(str)
    this.user.username = this.form.username
    this.load()
  },
  methods: {
    load() {
      request.get("/user/" + this.user.username).then(res => {
        if(res.code === '0') {
          this.user = res.data
          console.log(this.user)
          this.form = this.user
        }
      })
    },
    save() {
      request.put("/user", this.form).then(res => {
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "更新用户信息成功！"
          })
          sessionStorage.setItem("user", JSON.stringify(this.form))
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    uploaded(response) {
      this.form.avatar = response.data
    },
    handleExceed() {
      this.$message({
        type: "warning",
        message: "请先删除当前图片再上传新头像"
      })
    }
  }
}

</script>

