<template>
  <div>

    <!-- 功能区域 -->
    <div>
      <el-button @click="handleCreate">新增</el-button>
      <el-button>导入</el-button>
      <el-button>导出</el-button>
    </div>

    <!-- 搜索区域 -->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入搜索内容" clearable style="width: 20%" />
      <el-button style="margin-left: 5px;" @click="handleLoad">查询<el-icon style="margin-left: 5px"><Search/></el-icon></el-button>
    </div>

    <el-table :data="tableData" stripe style="width: 100%" fit>
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="age" label="年龄" />
      <el-table-column prop="gender" label="性别" />
      <el-table-column prop="address" label="地址" />
      <el-table-column fixed="right" label="操作" width="140">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="您是否确认删除？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button type="danger" size="small">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          v-model:currentPage="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
      />

      <el-dialog title="新增用户" v-model="dialogVisible" width="30%" :before-close="handleClose">
        <el-form :model="form" label-width="120px">
          <el-form-item label="用户名">
            <el-input v-model="form.username" style="width: 80%;" />
          </el-form-item>
          <el-form-item label="年龄">
            <el-input v-model="form.age" style="width: 80%;" />
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
        </el-form>
        <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">确定</el-button>
        </span>
        </template>
      </el-dialog>

    </div>

  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: 'Home',
  components: {},
  data() {
    return {
      dialogVisible: false,
      form: {},
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      records: [],
      tableData : []
    }
  },
  created() {
    this.handleLoad()
  },
  methods: {
    handleCreate() {
      this.dialogVisible = true
      this.form = {}
    },
    handleClose() {
      this.dialogVisible = false
    },
    handleSave() {
      if(this.form.id) {
        request.put("/user", this.form).then(res => {
          console.log(res)
          if(res.code === '0') {
            this.$message({
              type: "success",
              message: "更新用户信息成功！"
            })
            this.dialogVisible = false
            this.handleLoad()
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
            this.dialogVisible = false
            this.handleLoad()
          }
        })
      } else {
        request.post("/user", this.form).then(res => {
          console.log(res)
          if(res.code === '0') {
            this.$message({
              type: "success",
              message: "新增用户信息成功！"
            })
            this.dialogVisible = false
            this.handleLoad()
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
            this.dialogVisible = false
            this.handleLoad()
          }
        })
      }
    },
    handleLoad() {
      request.get("/user", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleDelete(id) {
      request.delete("/user/" + id).then(res => {
        console.log(res)
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "删除用户信息成功！"
          })
          this.handleLoad()
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
          this.handleLoad()
        }
      })
    },
    handleCurrentChange(pageNum) {
      this.currentPage = pageNum
      this.handleLoad()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.handleLoad()
    }
  }
}
</script>
