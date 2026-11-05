<template>
  <view class="login-container">
    <!-- 顶部标题 -->
    <view class="top-title">
      <image src="/static/logo.jpg" class="logo"></image>
    </view>
    <view class="welcome-title">颐心相伴·相伴一生</view>

    <!-- 表单区域 -->
    <view class="form-container">
      <!-- 账号输入 -->
      <view class="input-item">
        <input
          v-model="phone"
          placeholder="请输入账号"
          placeholderClass="placeholder-style"
          border="none"
          :clearable="false"
          class="custom-input"
        >
        </input>
        <!-- <uni-icons type="phone" color="#999" size="30"></uni-icons> -->
      </view>

      <!-- 验证码输入 -->
      <view class="input-item">
        <input
          v-model="code"
          placeholder="请输入验证码"
          placeholderClass="placeholder-style"
          border="none"
          :clearable="false"
          class="custom-input"
        >
        </input>
        <view class="sms-code" @click="getCode" v-if="!isCounting">获取验证码</view>
        <view class="sms-code-no" v-else>已发送{{ countdown }}秒</view>
      </view>

      <!-- 登录按钮 -->
      <button @click="handleLogin" class="login-btn">注册并下载</button>
      
      <view class="btn-download" @click="downloadApp">已有帐号，下载APP</view>
    </view>
  </view>
  
  <view class="modal-notice" v-show="showNotice">
    <image class="img" src="/static/share.png"></image>
    <view class="notice-text">点击右上角···<br />选择再浏览器打开</view>
  </view>
</template>

<script>
import { apiUrl } from '../../api'

	export default {
		data() {
			return {
				phone: '',
        code: '',
        rcode: '',
        showNotice: false,
        isCounting: false,
        countdown: 60,
        downloadUrl: '',
			}
		},
		onLoad(options) {
      this.rcode = options.r || ''
      this.getDownloadUrl()
		},
		methods: {
      // 获取下载链接
      getDownloadUrl() {
        uni.request({
          url: apiUrl('/getAppVersion'),
          method:'GET',
          success: (result) => {
            if (result.data.code==200) {
              this.downloadUrl = result.data.data?.android?.appUrl || ''
            }
          }
        })
      },
      // 获取验证码
      getCode() {
        if (this.isCounting) return;
        
        let _this = this
        // 发送验证码
        uni.request({
          url: apiUrl('/h5/getCode?phone=' + encodeURIComponent(this.phone)),
          dataType: "json",
          method:'GET',
          success: (result) => {
            console.log(result);
            if (result.data.code == 200) {
              uni.showToast({
                title: "验证码已发送",
                icon: "success"
              })
              
              _this.isCounting = true;
              _this.countdown = 60;
              const timer = setInterval(() => {
                if (_this.countdown > 0) {
                  _this.countdown--;
                } else {
                  clearInterval(timer);
                  _this.isCounting = false;
                }
              }, 1000);
            } else {
              uni.showToast({
                title: result.data.msg || "验证码发送失败",
                icon: "fail"
              })
            }
          }
        })
      },
      
      handleLogin() {
        uni.request({
          url: apiUrl('/h5/login'),
          data: {
            "phone": this.phone,
            "code": this.code,
            "rcode": this.rcode
          },
          dataType: "json",
          method:'POST',
          success: (result) => {
            console.log(result);
            if (result.data.code == 200) {
              uni.showToast({
                title: "注册成功",
                icon: "success"
              })
              this.downloadApp()
            } else {
              uni.showToast({
                title: result.data.msg || "注册失败",
                icon: "error"
              })
            }
          }
        })
      },
      // APP下载
      downloadApp() {
        // #ifndef MP-WEIXIN
        let browser = window.navigator.userAgent.toLowerCase();
        if (browser.match(/MicroMessenger/i) == 'micromessenger') {
          this.showNotice = true;
        } else {
          console.log('非微信')
          window.location.href = this.downloadUrl
        }
        // #endif
      }
		}
	}
</script>

<style lang="scss" scoped>
/* 样式保持不变 */
</style>
<style lang="scss" scoped>
.login-container {
  padding: 80rpx 60rpx;
  background-color: #fff;
  min-height: 100vh;
  box-sizing: border-box;
  .top-title {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 40rpx;
    image {
      width: 141px;
      height: 141px;
    }
  }
}

.welcome-title {
  font-size: 40rpx;
  font-weight: bold;
  color: #333;
  text-align: center;
  margin-bottom: 80rpx;
}

.form-container {
  display: flex;
  flex-direction: column;
}

.input-item {
  margin-bottom: 40rpx;
  border-bottom: 1rpx solid #eee;
  padding-bottom: 20rpx;
  display: flex;
  flex-direction: row;
  align-items: center;

  .custom-input {
    font-size: 30rpx;
    padding: 20rpx 0;
  }
  .sms-code {
    height: 30rpx;
    line-height: 30rpx;
    color: #1989fa;
  }
  .sms-code-no {
    height: 30rpx;
    line-height: 30rpx;
    color: #999;
  }
}

.placeholder-style {
  color: #999;
  font-size: 30rpx;
}

.login-btn {
  margin-top: 60rpx;
  height: 80rpx;
  font-size: 32rpx;
  background-color: #1989fa;
  border-radius: 8rpx;
  color: #fff;
}

.btn-download {
  margin-top: 60rpx;
  text-align: center;
  text-decoration: underline;
  color: #1989fa;
}

.login-tip {
  text-align: center;
  margin-top: 40rpx;
  font-size: 28rpx;
  color: #666;

  .login-link {
    color: #1989fa;
    margin-left: 10rpx;
  }
}

.modal-notice {
  position: fixed;
  top: 0;
  bottom: 0;
  right: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.9);
  
  
  .img {
    width: 200rpx;
    height: 200rpx;
    position: absolute;
    right: 20rpx;
    top: 20rpx;
  }
  
  .notice-text {
    color: #fff;
    font-size: 18px;
    width: 400rpx;
    padding: 30rpx 0;
    text-align: center;
    position: absolute;
    top: 280rpx;
    left: 175rpx;
    border: 2px dashed #fff;
    border-radius: 100rpx;
  }
}
</style>