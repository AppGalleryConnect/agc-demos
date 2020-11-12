<template>
  <div id="app">
    <div>sdk状态: {{ enable ? '启用' : '禁用' }}</div>
    <div class="enable">
      <button @click="enableSdk(true)">启用</button>
      <button @click="enableSdk(false)">禁用</button>
    </div>
    <div class="router">
      <button>
        <router-link to="/pageA">页面A</router-link>
      </button>
      <button>
        <router-link to="/pageB">页面B</router-link>
      </button>
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: "App",
  components: {},
  data() {
    return {
      enable: true,
    };
  },
  methods: {
    enableSdk(val) {
      const { APM } = window;
      this.enable = val;
      APM.enable(this.enable);
    },
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.router {
  text-align: left;
  margin-bottom: 50px;
}
.router button {
  margin: 0 20px;
}
.enable button {
  margin: 10px;
}
</style>
