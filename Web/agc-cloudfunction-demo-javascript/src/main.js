// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.

import Vue from 'vue';
import App from './App';
import router from './router';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import Vconsole from 'vconsole';
import "babel-polyfill"

Vue.use(ElementUI);
Vue.config.productionTip = false;

new Vue({
    el: '#app',
    router,
    render: (h) => h(App),
});

let vConsole = new Vconsole();

export default vConsole;
