import Vue from 'vue';
import Router from 'vue-router';
import cloudFunction from '@/components/cloudFunction';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'cloudFunction',
      component: cloudFunction,
    },
  ],
});
