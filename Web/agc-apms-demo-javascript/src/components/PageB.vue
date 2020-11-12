<template>
  <div class="pageB">
    <div id="img"></div>
  </div>
</template>

<script>
export default {
  mounted() {
    const script = document.createElement('script')
    script.src = 'https://cdn.bootcdn.net/ajax/libs/vue/2.6.9/vue.min.js'
    script.async = true
    const head = document.getElementsByTagName('head')[0]
    head.appendChild(script)

    const img = document.createElement('img')
    img.src = 'http://img.touxiangwu.com/2020/3/UnmuEv.jpeg'
    const positon = document.querySelector('#img')
    positon.appendChild(img)
    
  },
}
</script>

<style>
  .pageB {
    text-align: left;
  }
</style>