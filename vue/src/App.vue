<template>
  <router-view/>
</template>

<style>

</style>

<script>

import * as echarts from 'echarts'
import { provide } from 'vue'

export default {
  name: 'App',
  setup() {
    provide('ec', echarts)
  }
}

</script>
