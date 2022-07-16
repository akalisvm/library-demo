<template>
  <div>

    <!-- 功能区域 -->
    <div>
      <el-button @click="create">新增新闻</el-button>
      <el-button>导入</el-button>
      <el-button>导出</el-button>
    </div>

    <!-- 搜索区域 -->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入搜索内容" clearable style="width: 20%" />
      <el-button style="margin-left: 5px;" @click="load">查询<el-icon style="margin-left: 5px"><Search/></el-icon></el-button>
    </div>

    <el-table :data="tableData" stripe style="width: 100%" fit>
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="author" label="发布者" />
      <el-table-column prop="time" label="发布时间" />
      <el-table-column fixed="right" label="操作" width="210">
        <template #default="scope">
          <el-button size="small" @click="check(scope.row)">详情</el-button>
          <el-button size="small" @click="edit(scope.row)">编辑</el-button>
          <el-popconfirm title="您是否确认删除？" @confirm="remove(scope.row.id)">
            <template #reference>
              <el-button type="danger" size="small">删除</el-button>
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

      <el-dialog title="新增新闻" v-model="dialogVisible" width="50%" :before-close="close">
        <el-form :model="form" label-width="120px">
          <el-form-item label="新闻标题">
            <el-input v-model="form.title" />
          </el-form-item>
          <div id="content"></div>
        </el-form>
        <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确定</el-button>
        </span>
        </template>
      </el-dialog>

      <el-dialog title="详情" v-model="commentVisible">
        <el-card>
          <div v-html="detail.content" style="min-height: 100px"></div>
        </el-card>
      </el-dialog>

    </div>

  </div>
</template>

<script>

import request from "@/utils/request"
import E from 'wangeditor'

let editor;


export default {
  name: 'News',
  data() {
    return {
      dialogVisible: false,
      form: {},
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      records: [],
      tableData : [],
      commentVisible: false,
      detail: {}
    }
  },
  created() {
    this.load()
  },
  methods: {
    initEditor() {
      editor = new E('#content')
      editor.config.lang = 'en'
      editor.i18next = window.i18next
      editor.config.uploadImgServer = 'http://localhost:9090/files/editor/upload'
      editor.config.uploadFileName = 'file'
      editor.create()
    },
    create() {
      this.dialogVisible = true
      this.form = {}
      this.$nextTick(() => {
        if(editor == null) {
          this.initEditor()
        } else {
          editor.destroy()
          this.initEditor()
        }
      })
    },
    close() {
      this.dialogVisible = false
    },
    save() {
      this.form.content = editor.txt.html()
      if(this.form.id) {
        request.put("/news", this.form).then(res => {
          console.log(res)
          if(res.code === '0') {
            this.$message({
              type: "success",
              message: "更新新闻成功！"
            })
            this.dialogVisible = false
            this.load()
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
            this.dialogVisible = false
            this.load()
          }
        })
      } else {
        let userStr = sessionStorage.getItem("user") || "{}"
        let user = JSON.parse(userStr)
        this.form.author = user.username
        request.post("/news", this.form).then(res => {
          console.log(res)
          if(res.code === '0') {
            this.$message({
              type: "success",
              message: "新增新闻成功！"
            })
            this.dialogVisible = false
            this.load()
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
            this.dialogVisible = false
            this.load()
          }
        })
      }
    },
    load() {
      request.get("/news", {
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
    check(row) {
      this.detail = row;
      this.commentVisible = true
    },
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.$nextTick(() => {
        if(editor == null) {
          this.initEditor()
          editor.txt.html(row.content)
        } else {
          editor.destroy()
          this.initEditor()
          editor.txt.html(row.content)
        }
      })
    },
    remove(id) {
      request.delete("/news/" + id).then(res => {
        console.log(res)
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "删除新闻成功！"
          })
          this.load()
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
          this.load()
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

