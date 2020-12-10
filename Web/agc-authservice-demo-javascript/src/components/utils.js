export function providerChangeUtil(self) {
  if (self.provider == 'phone' || self.provider == 'email') {
    self.$router.push({path: '/', query: {provider: self.provider}})
  } else if (self.provider == 'QQ') {
    self.$router.push('/QQLogin')
  } else if (self.provider == 'weChat') {
    self.$router.push('/weChatLogin')
  }
}
