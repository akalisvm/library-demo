<template>
  <div>

    <div style="margin-top: 10px" @keyup.enter.native="load">
      <el-input v-model="searchTitle" placeholder="请输入图书名称" clearable style="width: 10%" />
      <el-input v-model="searchAuthor" placeholder="请输入图书作者" clearable style="width: 10%; margin-left: 5px" />
      <el-input v-model="searchPress" placeholder="请输入图书出版社" clearable style="width: 10%; margin-left: 5px" />
      <el-select v-model="searchClassification" :options="options" placeholder="请选择图书类型" clearable ref="select" style="margin-left: 5px">
        <el-option
            v-for="option in options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
        />
      </el-select>
      <el-switch v-model="onlyCollection" active-text="收藏" inactive-text="所有" style="margin-left: 5px" v-if="user.role === 'user'" />
      <el-button style="margin-left: 5px; margin-bottom: 5px" @click="load">
        查询<el-icon style="margin-left: 5px"><Search /></el-icon>
      </el-button>
      <el-button style="margin-left: 5px; margin-bottom: 5px" @click="reset">
        重置<el-icon style="margin-left: 5px"><RefreshLeft /></el-icon>
      </el-button>
      <el-button style="margin-left: 5px; margin-bottom: 5px" @click="create" type="primary" v-if="user.role === 'admin'">
        新增图书<el-icon style="margin-left: 5px"><Plus /></el-icon>
      </el-button>
    </div>

    <el-table
        :data="tableData"
        stripe
        style="width: 100%"
        fit
    >
      <el-table-column prop="id" label="图书ID" />
      <el-table-column label="封面">
        <template #default="scope">
          <el-image style="width: 8vw; height: 14vh" :src="scope.row.cover" />
        </template>
      </el-table-column>
      <el-table-column prop="title" label="书名" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="press" label="出版社" />
      <el-table-column prop="classification" label="分类" />
      <el-table-column prop="price" label="价格" />
      <el-table-column fixed="right" label="操作" width="210">
        <template #default="scope" v-if="user.role === 'admin'">
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
        <template #default="scope" v-if="user.role === 'user'">
          <el-button size="small" @click="collection(scope.row.id)" v-if="scope.row.collection === 0">
            收藏<el-icon style="margin-left: 5px"><Star /></el-icon>
          </el-button>
          <el-button size="small" @click="cancelCollection(scope.row.id)" v-if="scope.row.collection === 1">
            收藏<el-icon style="margin-left: 5px"><StarFilled /></el-icon>
          </el-button>
          <el-button @click="pickDate(scope.row.id)" size="small" type="primary" style="margin-left: 5px">
            申请借阅<el-icon style="margin-left: 5px"><CirclePlusFilled /></el-icon>
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

      <el-dialog v-model="dialogVisible" width="50%" :before-close="close">
        <template #header="{ titleId, titleClass }">
          <div>
            <span :id="titleId" :class="titleClass" v-if="dialogTitle === 'create'">新增图书</span>
            <span :id="titleId" :class="titleClass" v-if="dialogTitle === 'edit'">编辑图书</span>
          </div>
        </template>
        <el-form :model="form" label-width="120px" @keyup.enter.native="save">
          <el-form-item label="书名">
            <el-input v-model="form.title" style="width: 80%;" />
          </el-form-item>
          <el-form-item label="作者">
            <el-input v-model="form.author" style="width: 80%;" />
          </el-form-item>
          <el-form-item label="出版社">
            <el-input v-model="form.press" style="width: 80%;" />
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="form.classification" :options="options" placeholder="请选择图书类型">
              <el-option
                  v-for="option in options"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="价格">
            <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 30%;" />
          </el-form-item>
          <el-form-item label="封面">
            <el-upload
                :action="url"
                ref="upload"
                :on-success="uploaded"
            >
              <el-button type="primary">点击上传</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确定</el-button>
        </span>
        </template>
      </el-dialog>

      <el-dialog v-model="dateDialogVisible" title="请选择借阅时间段" width="40%" :before-close="close">
        <el-form :model="borrowForm" @keyup.enter.native="applyBorrow">
          <el-form-item>
            <el-date-picker
                v-model="borrowForm.dates"
                type="daterange"
                value-format="YYYY-MM-DD"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="width: 100%;"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dateDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="applyBorrow">确定</el-button>
        </template>
      </el-dialog>

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
  name: 'Book',
  components: {},
  data() {
    return {
      dialogVisible: false,
      dialogTitle: '',
      dateDialogVisible: false,
      form: {},
      borrowForm: {},
      searchTitle: '',
      searchAuthor: '',
      searchPress: '',
      searchClassification: '',
      onlyCollection: false,
      currentPage: 1,
      pageSize: 5,
      userId: 0,
      bookId: 0,
      total: 0,
      records: [],
      tableData : [],
      user: {},
      book: {},
      classification: '',
      url: 'http://' + window.server.ip + '/api/upload/cover',
      options: [
        {
          value: "历史类",
          label: "历史类"
        },
        {
          value: "文学类",
          label: "文学类"
        },
        {
          value: "社会科学类",
          label: "社会科学类"
        },
        {
          value: "哲学类",
          label: "哲学类"
        },
        {
          value: "军事类",
          label: "军事类"
        },
        {
          value: "法律类",
          label: "法律类"
        },
        {
          value: "经济类",
          label: "经济类"
        },
        {
          value: "教育类",
          label: "教育类"
        },
        {
          value: "语言艺术类",
          label: "语言艺术类"
        },
        {
          value: "天文学类",
          label: "天文学类"
        },
        {
          value: "生物学类",
          label: "生物学类"
        },
        {
          value: "医学卫生类",
          label: "医学卫生类"
        },
        {
          value: "农业类",
          label: "农业类"
        },
        {
          value: "地理类",
          label: "地理类"
        },
        {
          value: "政治类",
          label: "政治类"
        }
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
    create() {
      this.dialogVisible = true
      this.dialogTitle = "create"
      this.form = {}
      this.$nextTick(() => {
        this.$refs['upload'].clearFiles()
      })
    },
    close() {
      this.dialogVisible = false
      this.dateDialogVisible = false
    },
    save() {
      if(this.form.id) {
        request.put("/book", this.form).then(res => {
          console.log(res)
          if(res.code === '0') {
            this.$message({
              type: "success",
              message: "编辑图书成功！"
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
        request.post("/book", this.form).then(res => {
          console.log(res)
          if(res.code === '0') {
            this.$message({
              type: "success",
              message: "新增图书成功！"
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
      request.get("/book", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          userId: this.user.id,
          searchTitle: this.searchTitle,
          searchAuthor: this.searchAuthor,
          searchPress: this.searchPress,
          searchClassification: this.searchClassification,
          onlyCollection: this.onlyCollection
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    reset() {
      this.searchTitle = ''
      this.searchAuthor = ''
      this.searchPress = ''
    },
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.dialogTitle = "edit"
      this.$nextTick(() => {
        this.$refs['upload'].clearFiles()
      })
    },
    remove(id) {
      request.delete("/book/" + id).then(res => {
        console.log(res)
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "删除图书成功！"
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
    collection(id) {
      request.post("/book/collection/" + this.user.id, id).then(res => {
        console.log(res)
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "收藏图书成功！"
          })
          this.dialogVisible = false
          this.load()
        } else {
          this.$message({
            type: "warning",
            message: res.msg
          })
          this.dialogVisible = false
        }
      })
    },
    cancelCollection(id) {
      request.delete("/book/collection/" + this.user.id + "/" + id).then(res => {
        console.log(res)
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "取消收藏图书成功！"
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
    pickDate(id) {
      this.borrowForm = {
        'userId': this.user.id,
        'bookId': id,
        'returnDate': ''
      }
      this.dateDialogVisible = true
    },
    applyBorrow() {
      console.log(this.borrowForm)
      this.borrowForm.startDate = this.borrowForm.dates[0]
      this.borrowForm.endDate = this.borrowForm.dates[1]
      request.post("/borrow/" + this.user.id, this.borrowForm).then(res => {
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "申请借阅图书成功！"
          })
          this.dateDialogVisible = false
          this.load()
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
          this.dateDialogVisible = false
        }
      })
    },
    uploaded(response) {
      this.form.cover = response.data
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
