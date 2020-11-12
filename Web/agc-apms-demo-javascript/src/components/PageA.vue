<template>
  <div class="pageA">
    <div>
      <input type="file" @change="upload" />
    </div>
    <div>{{ loading }}</div>
    <div class="content" v-html="content">
      
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      content: '',
      loading: '',
    }
  },
  mounted() {
    const script = document.createElement('script')
    script.src = 'https://cdn.bootcdn.net/ajax/libs/jquery/3.5.1/jquery.min.js'
    script.async = true
    const head = document.getElementsByTagName('head')[0]
    head.appendChild(script)
  },
  methods: {
    upload(e) {
      const { APM } = window
      const uploadTrace = APM.createTrace('uploadTrace')
      this.loading = '上传中...'
      uploadTrace.start()
      setTimeout(() => {
        uploadTrace.setAttribute('fileName', e.target.files[0].name)
        uploadTrace.setAttribute('uploadTime', new Date().toString())
        uploadTrace.setAttribute('fileType', e.target.files[0].type)
        uploadTrace.setMetric('fileSize', e.target.files[0].size)
        uploadTrace.stop()
        this.loading = '上传成功！'
        this.content = `Attributes: ${JSON.stringify(uploadTrace.getAttributes())}\n Metrics: ${JSON.stringify(uploadTrace.getMetrics())}`
      }, 1535)
    }
  }
}
</script>

<style>
  .pageA {
    text-align: left;
  }
  .content {
    margin-top: 50px;
    white-space: pre-wrap;
  }
</style>