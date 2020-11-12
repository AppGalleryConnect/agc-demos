import VueRouter from 'vue-router'

export default new VueRouter({
  mode: 'hash',
  routes: [{
    path: '/pageA', component: () => import('../components/PageA')
  }, {
    path: '/pageB', component: () => import('../components/PageB')
  }]
})