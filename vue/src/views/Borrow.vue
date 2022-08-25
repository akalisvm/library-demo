<template>

  <div style="margin-top: 10px" @keyup.enter.native="load">
    <el-input v-model="searchTitle" placeholder="请输入图书名称" clearable style="width: 10%" />
    <el-input v-model="searchUsername" placeholder="请输入用户名" clearable style="width: 10%; margin-left: 5px" v-if="user.role === 'admin'" />
    <el-input v-model="searchPhone" placeholder="请输入用户电话" clearable style="width: 10%; margin-left: 5px" v-if="user.role === 'admin'" />
    <el-input v-model="searchEmail" placeholder="请输入用户邮箱" clearable style="width: 10%; margin-left: 5px" v-if="user.role === 'admin'" />
    <el-select v-model="searchState" :options="options" placeholder="请选择图书类型" clearable ref="select" style="width: 10%; margin-left: 5px">
      <el-option
          v-for="option in options"
          :key="option.value"
          :label="option.label"
          :value="option.value"
      />
    </el-select>
    <el-button style="margin-left: 5px; margin-bottom: 5px" @click="load">
      查询<el-icon style="margin-left: 5px"><Search /></el-icon>
    </el-button>
    <el-button style="margin-left: 5px; margin-bottom: 5px" @click="reset">
      重置<el-icon style="margin-left: 5px"><RefreshLeft /></el-icon>
    </el-button>
  </div>

  <div>
    <el-table :data="tableData" stripe style="width: 100%" fit>
      <el-table-column prop="id" label="借阅ID" />
      <el-table-column prop="title" label="书名" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="startDate" label="借阅开始日期" />
      <el-table-column prop="endDate" label="借阅结束日期" />
      <el-table-column prop="state" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.state === '待审核'" type="warning">待审核</el-tag>
          <el-tag v-if="scope.row.state === '借阅中'" type="primary">借阅中</el-tag>
          <el-tag v-if="scope.row.state === '已归还'" type="success">已归还</el-tag>
          <el-tag v-if="scope.row.state === '未归还'" type="danger">未归还</el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="210" v-if="user.role === 'admin'">
        <template #default="scope">
          <el-button size="small" @click="approve(scope.row)" type="primary" v-if="scope.row.state === '待审核'">
            通过审核<el-icon style="margin-left: 5px"><EditPen /></el-icon>
          </el-button>
          <el-button size="small" @click="finish(scope.row)" type="primary" v-if="scope.row.state === '借阅中'">
            归还<el-icon style="margin-left: 5px"><Check /></el-icon>
          </el-button>
          <el-button size="small" @click="drop(scope.row)" type="danger" v-if="scope.row.state === '借阅中'">
            丢失<el-icon style="margin-left: 5px"><Close /></el-icon>
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          @size-change="sizeChange"
          @current-change="currentChange"
          v-model:currentPage="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
      />
    </div>
  </div>
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
  name: "Borrow",
  data() {
    return {
      user: {},
      userId: 0,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      records: [],
      tableData : [],
      searchTitle: '',
      searchUsername: '',
      searchPhone: '',
      searchEmail: '',
      searchState: '',
      options: [
        {
          value: "待审核",
          label: "待审核"
        },
        {
          value: "借阅中",
          label: "借阅中"
        },
        {
          value: "已归还",
          label: "已归还"
        },
        {
          value: "未归还",
          label: "未归还"
        },
      ]
    }
  },
  created() {
    this.user = JSON.parse(getCookie("user"))
    this.load()
    this.$nextTick(() => {
      this.load()
    })
  },
  methods: {
    load() {
      request.get("/borrow", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          userId: this.user.id,
          searchTitle: this.searchTitle,
          searchUsername: this.searchUsername,
          searchPhone: this.searchPhone,
          searchEmail: this.searchEmail,
          searchState: this.searchState,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    reset() {
      this.searchTitle = ''
      this.searchUsername = ''
      this.searchPhone = ''
      this.searchEmail = ''
    },
    approve(row) {
      request.put("/borrow/approve", row).then(res => {
        console.log(res)
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "审核借阅通过！"
          })
          this.dialogVisible = false
          this.load()
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
          this.dialogVisible = false
        }
      })
    },
    finish(row) {
      request.put("/borrow/finish", row).then(res => {
        console.log(res)
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "归还图书成功！"
          })
          this.dialogVisible = false
          this.load()
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
          this.dialogVisible = false
        }
      })
    },
    drop(row) {
      request.put("/borrow/drop", row).then(res => {
        console.log(res)
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "设置图书丢失成功！"
          })
          this.dialogVisible = false
          this.load()
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
          this.dialogVisible = false
        }
      })
    },
    currentChange(pageNum) {
      this.currentPage = pageNum
      this.load()
    },
    sizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    }
  }
}
</script>

<style scoped>

</style>