<template>
  <div>

    <div style="margin-top: 10px" @keyup.enter.native="load">
      <el-input v-model="searchUsername" placeholder="请输入用户姓名" clearable style="width: 10%" />
      <el-input v-model="searchPhone" placeholder="请输入用户电话号码" clearable style="width: 10%; margin-left: 5px" />
      <el-input v-model="searchEmail" placeholder="请输入用户邮箱" clearable style="width: 10%; margin-left: 5px" />
      <el-button style="margin-left: 5px; margin-bottom: 5px" @click="load">
        查询<el-icon style="margin-left: 5px"><Search /></el-icon>
      </el-button>
      <el-button style="margin-left: 5px; margin-bottom: 5px" @click="reset">
        重置<el-icon style="margin-left: 5px"><RefreshLeft /></el-icon>
      </el-button>
      <el-button style="margin-left: 5px; margin-bottom: 5px" @click="create" type="primary">
        新增用户<el-icon style="margin-left: 5px"><Plus /></el-icon>
      </el-button>
      <el-button style="margin-left: 5px; margin-bottom: 5px" @click="deleteBatch" type="danger">
        批量删除用户<el-icon style="margin-left: 5px"><Delete /></el-icon>
      </el-button>
    </div>

    <el-table
        :data="tableData"
        stripe
        style="width: 100%"
        fit
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="用户ID" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="age" label="年龄" />
      <el-table-column prop="gender" label="性别" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="address" label="地址" />
      <el-table-column label="角色">
        <template #default="scope">
          <span v-if="scope.row.role === 'admin'">管理员</span>
          <span v-if="scope.row.role === 'user'">普通用户</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="210">
        <template #default="scope">
          <el-button size="small" @click="edit(scope.row)" type="primary">
            编辑<el-icon style="margin-left: 5px"><Edit /></el-icon>
          </el-button>
          <el-popconfirm title="您是否确认删除？" @confirm="remove(scope.row.id)">
            <template #reference>
              <el-button type="danger" size="small">
                删除<el-icon style="margin-left: 5px"><Delete /></el-icon>
              </el-button>
            </template>
          </el-popconfirm>
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

      <el-dialog v-model="dialogVisible" width="50%" :before-close="close">
        <template #header="{ titleId, titleClass }">
          <div>
            <span :id="titleId" :class="titleClass" v-if="dialogTitle === 'create'">新增用户信息</span>
            <span :id="titleId" :class="titleClass" v-if="dialogTitle === 'edit'">修改用户信息</span>
          </div>
        </template>
        <el-form :model="form" label-width="120px" @keyup.enter.native="save">
          <el-form-item label="用户名">
            <el-input v-model="form.username" style="width: 80%;" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" style="width: 80%;" type="password" show-password />
          </el-form-item>
          <el-form-item label="年龄">
            <el-input-number v-model="form.age" :min="0" style="width: 20%;" />
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="form.phone" style="width: 80%;" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" style="width: 80%;" />
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="form.gender">
              <el-radio label="男">男</el-radio>
              <el-radio label="女">女</el-radio>
              <el-radio label="未知">未知</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="地址">
            <el-input type="textarea" v-model="form.address" style="width: 80%;" />
          </el-form-item>
          <el-form-item label="角色">
            <el-radio-group v-model="form.role">
              <el-radio label="admin">管理员</el-radio>
              <el-radio label="user">普通用户</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确定</el-button>
        </span>
        </template>
      </el-dialog>

    </div>

  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: 'User',
  components: {},
  data() {
    return {
      dialogVisible: false,
      dialogTitle: 'create',
      form: {},
      searchUsername: '',
      searchPhone: '',
      searchEmail: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      records: [],
      tableData : [],
      ids: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    create() {
      this.dialogVisible = true
      this.dialogTitle = "create"
      this.form = {}
    },
    close() {
      this.dialogVisible = false
    },
    save() {
      if(this.form.id) {
        request.put("/user", this.form).then(res => {
          console.log(res)
          if(res.code === '0') {
            this.$message({
              type: "success",
              message: "编辑用户成功！"
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
      } else {
        request.post("/user", this.form).then(res => {
          console.log(res)
          if(res.code === '0') {
            this.$message({
              type: "success",
              message: "新增用户成功！"
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
      }
    },
    load() {
      request.get("/user", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          searchUsername: this.searchUsername,
          searchPhone: this.searchPhone,
          searchEmail: this.searchEmail
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    reset() {
      this.searchUsername = ''
      this.searchPhone = ''
      this.searchEmail = ''
    },
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.dialogTitle = "edit"
    },
    remove(id) {
      request.delete("/user/" + id).then(res => {
        console.log(res)
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "删除用户成功！"
          })
          this.load()
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    deleteBatch() {
      if(!this.ids.length) {
        this.$message.warning("请先选择要删除的用户！");
        return
      }
      request.post("/user/deleteBatchIds", this.ids).then(res => {
        if(res.code === '0') {
          this.$message.success("批量删除用户成功！")
          this.load()
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    handleSelectionChange(val) {
      this.ids = val.map(v => v.id)
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
