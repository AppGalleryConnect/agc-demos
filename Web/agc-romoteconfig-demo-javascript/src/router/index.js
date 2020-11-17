import Vue from 'vue';
import Router from 'vue-router';
import remoteconfigDemo from '@/components/remoteconfigDemo';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'remoteconfigDemo',
      component: remoteconfigDemo,
    },
  ],
});
