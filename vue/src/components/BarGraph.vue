<template>
  <div class="echarts-box">
    <div id="myEcharts" :style="{ width: this.width, height: this.height }"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import {onMounted, onUnmounted, getCurrentInstance} from "vue";

export default {
  name: "App",
  props: ["width", "height"],
  setup() {
    let myEcharts = echarts;

    onMounted(() => {
      initChart();
    });

    onUnmounted(() => {
      myEcharts.dispose;
    });

    function initChart() {
      let chart = myEcharts.init(document.getElementById("myEcharts"), "purple-passion");
      chart.setOption({
        title: {
          text: "图书馆藏书分类（单位：本）",
          left: "center",
        },
        xAxis: {
          data: [
            "历史类", "文学类", "社会科学类", "哲学类",
            "军事类", "法律类", "经济类", "教育类",
            "语言艺术类", "天文学类", "生物学类",
            "医学卫生类", "农业类", "地理类", "政治类",
          ]
        },
        tooltip: {
          trigger: "axis"
        },
        yAxis: {
          type: "value",
          minInterval: 1
        },
        series: [
          {
            name: '分类',
            type: "bar",
            data: []
          }
        ]
      });

      const proxy = getCurrentInstance()
      proxy.appContext.config.globalProperties.$axios
          .get("http://" + window.server.ip + "/api/book/classificationCounts").then((res) => {
        console.log(res.data.data)
        chart.setOption({
          series: [
            {
              name: '分类',
              type: "bar",
              data: res.data.data
            }
          ]
        })
      })

      window.onresize = function () {
        chart.resize();
      };
    }

    return {
      initChart
    };
  }
};
</script>