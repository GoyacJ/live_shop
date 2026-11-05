<template>
  <view class="agreement-container">
    <view class="agreement-title">用户协议</view>
    <scroll-view scroll-y class="agreement-content">
      <view v-html="content"></view>
    </scroll-view>
  </view>
</template>

<script>
import { apiUrl } from '../../api'

export default {
  data() {
    return {
      content: ''
    }
  },
  onLoad() {
    this.fetchAgreement()
  },
  methods: {
    async fetchAgreement() {
      try {
        const res = await uni.request({
          url: apiUrl('/index/privacy-agreement'),
          method: 'GET'
        })
       if (res.data.code === 200) {
         this.content = res.data.data.userAgreement
       }
      } catch (e) {
        this.content = '<p>加载失败，请稍后重试。</p>'
      }
    }
  }
}
</script>

<style scoped>
.agreement-container {
  background: #fff;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.agreement-title {
  font-size: 20px;
  font-weight: bold;
  text-align: center;
  padding: 32rpx 0 24rpx 0;
  border-bottom: 1px solid #f0f0f0;
}
.agreement-content {
  flex: 1;
  padding: 32rpx;
  font-size: 15px;
  color: #333;
  line-height: 1.8;
  overflow-y: auto;
  overflow-x: hidden;
  width: 100%;
  box-sizing: border-box;
}
.agreement-content [v-html],
.agreement-content * {
  max-width: 100% !important;
  box-sizing: border-box;
  word-break: break-all;
  overflow-wrap: break-word;
}
.agreement-content img,
.agreement-content table,
.agreement-content pre,
.agreement-content code,
.agreement-content a,
.agreement-content video,
.agreement-content iframe {
  max-width: 100% !important;
  box-sizing: border-box;
  word-break: break-all;
  overflow-x: auto;
  display: block;
}
.agreement-content table {
  table-layout: fixed;
}
</style> 