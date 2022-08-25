<template>
  <div>

    <div style="margin-top: 10px" @keyup.enter.native="load">
      <el-input v-model="search" placeholder="请输入搜索公告标题" clearable style="width: 20%" />
      <el-button style="margin-left: 5px; margin-bottom: 5px" @click="load">
        查询<el-icon style="margin-left: 5px"><Search /></el-icon>
      </el-button>
      <el-button style="margin-left: 5px; margin-bottom: 5px" @click="this.search = ''">
        重置<el-icon style="margin-left: 5px"><RefreshLeft /></el-icon>
      </el-button>
      <el-button style="margin-left: 5px; margin-bottom: 5px" @click="create" type="primary" v-if="user.role === 'admin'">
        新增公告<el-icon style="margin-left: 5px"><Plus /></el-icon>
      </el-button>
    </div>

    <el-table :data="tableData" stripe style="width: 100%" fit>
      <el-table-column type="expand">
        <template #default="props">
          <div v-html="props.row.title" style="text-align: center; font-weight: bold;"></div>
          <div v-html="props.row.content" style="min-height: 100px"></div>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="time" label="发布时间" />
      <el-table-column fixed="right" label="操作" width="210" v-if="user.role === 'admin'">
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
            <span :id="titleId" :class="titleClass" v-if="dialogTitle === 'create'">新增公告</span>
            <span :id="titleId" :class="titleClass" v-if="dialogTitle === 'edit'">修改公告</span>
          </div>
        </template>
        <el-form :model="form" label-width="120px">
          <el-form-item label="公告标题">
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

      <el-dialog title="评论详情" v-model="commentVisible">
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

let editor;

export default {
  name: 'Comment',
  data() {
    return {
      dialogVisible: false,
      dialogTitle: '',
      form: {},
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      records: [],
      tableData : [],
      commentVisible: false,
      detail: {},
      count: 0,
      user: {}
    }
  },
  created() {
    this.user = JSON.parse(getCookie("user"))
    this.load()
  },
  methods: {
    initEditor() {
      editor = new E('#content')
      editor.config.lang = 'en'
      editor.i18next = window.i18next
      editor.config.uploadImgServer = 'http://' + window.server.ip +'/api/upload/editor'
      editor.config.uploadFileName = 'file'
      editor.create()
    },
    create() {
      this.dialogVisible = true
      this.dialogTitle = "create"
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
        request.put("/comment", this.form).then(res => {
          console.log(res)
          if(res.code === '0') {
            this.$message({
              type: "success",
              message: "编辑公告成功！"
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
        this.form.author = JSON.parse(sessionStorage.getItem("user")).username
        request.post("/comment", this.form).then(res => {
          console.log(res)
          if(res.code === '0') {
            this.$message({
              type: "success",
              message: "新增公告成功！"
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
      request.get("/comment", {
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
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.dialogTitle = "edit"
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
      request.delete("/comment/" + id).then(res => {
        console.log(res)
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "删除公告成功！"
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

