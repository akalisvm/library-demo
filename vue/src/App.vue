<template>
  <div class="common-layout">
    <el-container>
      <el-header><Header/></el-header>
      <el-container>
        <el-aside width="200px"><Aside/></el-aside>
        <el-main><router-view/></el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style>

</style>

<script>
import Header from "@/components/Header";
import Aside from "@/components/Aside";

export default {
  name: "Layout",
  components: {
    Header,
    Aside
  }
}
</script>
