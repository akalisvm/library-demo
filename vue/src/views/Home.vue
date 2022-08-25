<template>
  <div>

    <div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card style="min-height: 20vh;
                          background-color: #409eff;
                          color: #dcdfe6;
                          font-weight: bold"
          >
            待审核:
            <el-row>
              <div style="font-size: 10vh; margin: auto">
                {{ this.waiting }}
              </div>
            </el-row>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card style="min-height: 20vh;
                          background-color: #409eff;
                          color: #dcdfe6;
                          font-weight: bold"
          >
            借阅中:
            <el-row>
              <div style="font-weight: bold; font-size: 10vh; margin: auto">
                {{ this.borrowing }}
              </div>
            </el-row>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card style="min-height: 20vh;
                          background-color: #409eff;
                          color: #dcdfe6;
                          font-weight: bold">
            已归还:
            <el-row>
              <div style="font-weight: bold; font-size: 10vh; margin: auto">
                {{ this.returned }}
              </div>
            </el-row>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card style="min-height: 20vh;
                          background-color: #f56c6c;
                          color: #dcdfe6;
                          font-weight: bold"
          >
            未归还:
            <el-row>
              <div style="font-weight: bold; font-size: 10vh; margin: auto">
                {{ this.unreturned }}
              </div>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div style="margin-top: 20px">
      <el-card>
        <BarGraph :width="'100%'" :height="'25vh'" />
      </el-card>
    </div>

    <el-card style="margin-top: 20px">
      <div style="font-weight: bold; margin-bottom: 5px">
        最新公告
      </div>
      <el-collapse v-model="activeName" accordion>
        <el-collapse-item :title="this.commentOne.title" name="1">
          <div v-html="this.commentOne.content" />
        </el-collapse-item>
        <el-collapse-item :title="this.commentTwo.title" name="2">
          <div v-html="this.commentTwo.content" />
        </el-collapse-item>
        <el-collapse-item :title="this.commentThree.title" name="3">
          <div v-html="this.commentThree.content" />
        </el-collapse-item>
      </el-collapse>
    </el-card>

  </div>
</template>

<script>

import request from "@/utils/request";
import BarGraph from "@/components/BarGraph";

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
  name: "Home",
  data() {
    return {
      user: {},
      userId: '',
      activeName: "1",
      waiting: 0,
      borrowing: 0,
      returned: 0,
      unreturned: 0,
      commentOne: {},
      commentTwo: {},
      commentThree: {},
    }
  },
  components: { BarGraph },
  created() {
    this.user = JSON.parse(getCookie("user"))
    this.load()
  },
  methods: {
    load() {
      request.get("/comment/latest", {})
          .then(res => {
        console.log(res);
        this.commentOne = res.data[0];
        this.commentTwo = res.data[1];
        this.commentThree = res.data[2];
      })
      request.get("/borrow/count", {
        params: {
          userId: this.user.id
        }
      }).then(res => {
        console.log(res.data);
        if(res.data.length === 1) {
          if(res.data[0].state === '待审核') {
            this.waiting = res.data[0].count;
          } else if(res.data[0].state === '借阅中') {
            this.borrowing = res.data[0].count;
          } else if(res.data[0].state === '已归还') {
            this.returned = res.data[0].count;
          } else if(res.data[0].state === '未归还') {
            this.unreturned = res.data[0].count;
          }
        }
        if(res.data.length === 2) {
          if(res.data[0].state === '待审核') {
            this.waiting = res.data[0].count;
          } else if(res.data[0].state === '借阅中') {
            this.borrowing = res.data[0].count;
          } else if(res.data[0].state === '已归还') {
            this.returned = res.data[0].count;
          } else if(res.data[0].state === '未归还') {
            this.unreturned = res.data[0].count;
          }

          if(res.data[1].state === '待审核') {
            this.waiting = res.data[1].count;
          } else if(res.data[1].state === '借阅中') {
            this.borrowing = res.data[1].count;
          } else if(res.data[1].state === '已归还') {
            this.returned = res.data[1].count;
          } else if(res.data[1].state === '未归还') {
            this.unreturned = res.data[1].count;
          }
        }
        if(res.data.length === 3) {
          if(res.data[0].state === '待审核') {
            this.waiting = res.data[0].count;
          } else if(res.data[0].state === '借阅中') {
            this.borrowing = res.data[0].count;
          } else if(res.data[0].state === '已归还') {
            this.returned = res.data[0].count;
          } else if(res.data[0].state === '未归还') {
            this.unreturned = res.data[0].count;
          }

          if(res.data[1].state === '待审核') {
            this.waiting = res.data[1].count;
          } else if(res.data[1].state === '借阅中') {
            this.borrowing = res.data[1].count;
          } else if(res.data[1].state === '已归还') {
            this.returned = res.data[1].count;
          } else if(res.data[1].state === '未归还') {
            this.unreturned = res.data[1].count;
          }

          if(res.data[2].state === '待审核') {
            this.waiting = res.data[2].count;
          } else if(res.data[2].state === '借阅中') {
            this.borrowing = res.data[2].count;
          } else if(res.data[2].state === '已归还') {
            this.returned = res.data[2].count;
          } else if(res.data[2].state === '未归还') {
            this.unreturned = res.data[2].count;
          }
        }
        if(res.data.length === 4) {
          this.returned = res.data[0].count;
          this.unreturned = res.data[1].count;
          this.borrowing = res.data[2].count;
          this.waiting = res.data[3].count;
        }
      })
    }
  }
}

</script>
