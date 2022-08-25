<template>
  <div class="ValidCode disabled-select" :style="`width: ${width}; height: ${height}`" @click="refreshValidCode">
    <span v-for="(item, index) in codeList" :key="index" :style="getStyle(item)">
      {{ item.code }}
    </span>
  </div>
</template>

<script>

export default {
  name: "ValidCode",
  model: {
    prop: 'value',
    event: 'input'
  },
  props: {
    width: {
      type: String,
      default: '120px'
    },
    height: {
      type: String,
      default: '40px'
    },
    length: {
      type: Number,
      default: 4
    },
    refresh: {
      type: Number
    }
  },
  data() {
    return {
      codeList: []
    }
  },
  watch: {
    refresh() {
      this.createValidCode()
    }
  },
  mounted() {
    this.createValidCode()
  },
  methods: {
    createValidCode() {
      const len = this.length
      const codeList = []
      const chars = 'ABCDEHGHIJKLMNOPQRSTUWXYZabcdefghijklmnopqrstuvwxyz0123456789'
      const charsLen = chars.length
      for(let i = 0; i < len; i++) {
        const rgb = [Math.round(Math.random() * 150), Math.round(Math.random() * 150), Math.round(Math.random() * 150)]
        codeList.push({
          code: chars.charAt(Math.floor(Math.random() * charsLen)),
          color: `rgb(${rgb})`,
          fontSize: `${10 + (+[Math.floor(Math.random() * 10)] + 10)}px`,
          padding: `${[Math.floor((Math.random() * 10))]}px`,
          transform: `rotate(${Math.floor(Math.random() * 90) - Math.floor(Math.random() * 90)}deg)`
        })
      }
      this.codeList = codeList
      this.$emit('input', codeList.map(item => item.code).join(''))
    },
    refreshValidCode() {
      this.createValidCode()
    },
    getStyle(data) {
      return `color: ${data.color}; font-size: ${data.fontSize}; padding: ${data.padding}; transform: ${data.transform}`
    }
  }
}
const refreshValidCode = () => {
  this.refreshValidCode()
}

</script>

<style scoped>

.ValidCode {
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}
.ValidCode span {
  display: inline-block;
}

</style>