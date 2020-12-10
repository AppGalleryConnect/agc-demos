import Vue from 'vue';
import Router from 'vue-router';
import login from '@/components/login';
import QQLogin from '@/components/QQLogin';
import weChatLogin from '@/components/weChatLogin';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'login',
      component: login,
    },
    {
      path: '/QQLogin',
      name: 'QQLogin',
      component: QQLogin,
    },
    {
      path: '/weChatLogin',
      name: 'weChatLogin',
      component: weChatLogin,
    },
    {
      path: '/QQLogin',
      name: 'QQLogin',
      component: QQLogin,
    },
  ],
});
