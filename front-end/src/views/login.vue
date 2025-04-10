<template>
  <div class="login-container">
    <div class="animated-bg">
      <div class="wave"></div>
      <div class="wave"></div>
      <div class="wave"></div>
      <div class="floating-light"></div>
      <div class="floating-light"></div>
      <div class="floating-light"></div>
    </div>
    <div class="login-box">
      <h2 class="title">社区重点关怀人员管理系统</h2>
      <div class="form-container">
        <h3 class="sub-title">用户登录</h3>
        <a-form
          ref="formRef"
          :model="formState"
          :rules="rules"
          class="login-form"
        >
          <a-form-item name="username">
            <a-input
              v-model:value="formState.username"
              placeholder="请输入用户名"
              size="large"
            >
              <template #prefix>
                <user-outlined />
              </template>
            </a-input>
          </a-form-item>
          <a-form-item name="password">
            <a-input-password
              v-model:value="formState.password"
              placeholder="请输入密码"
              size="large"
            >
              <template #prefix>
                <lock-outlined />
              </template>
            </a-input-password>
          </a-form-item>

          <a-form-item>
            <a-button
              type="primary"
              html-type="submit"
              :loading="loading"
              class="login-button"
              @click="handleSubmit"
              size="large"
              block
            >
              登录
            </a-button>
          </a-form-item>
          <div class="form-footer">
            <router-link to="/register" class="register-link">
              没有账号？立即注册
            </router-link>
          </div>
        </a-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { login } from '@/api/auth'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const formState = reactive({
  username: '',
  password: '',
  remember: false
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleSubmit = () => {
  formRef.value.validate().then(() => {
    loading.value = true
    login(formState)
      .then(res => {
        localStorage.setItem('token', res.data.token)
        message.success('登录成功')
        router.push('/')
      })
      .catch(() => {
        loading.value = false
      })
  })
}
</script>

<style lang="less" scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #1890ff 0%, #722ed1 100%);
  position: relative;
  overflow: hidden;

  .animated-bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 1;

    .wave {
      position: absolute;
      width: 200%;
      height: 200%;
      background: radial-gradient(circle at center, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
      animation: wave 15s linear infinite;
      transform-origin: center;

      &:nth-child(1) {
        animation-delay: 0s;
        opacity: 0.4;
      }

      &:nth-child(2) {
        animation-delay: -5s;
        opacity: 0.3;
      }

      &:nth-child(3) {
        animation-delay: -10s;
        opacity: 0.2;
      }
    }

    .floating-light {
      position: absolute;
      width: 4px;
      height: 4px;
      background: rgba(255, 255, 255, 0.8);
      border-radius: 50%;
      animation: float 6s ease-in-out infinite;

      &:nth-child(4) {
        left: 10%;
        top: 20%;
        animation-delay: 0s;
      }

      &:nth-child(5) {
        left: 80%;
        top: 60%;
        animation-delay: -2s;
      }

      &:nth-child(6) {
        left: 50%;
        top: 30%;
        animation-delay: -4s;
      }
    }
  }
  
  .login-box {
    position: relative;
    z-index: 2;
    width: 400px;
    padding: 40px;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10px);
    
    .title {
      text-align: center;
      color: #1890ff;
      margin-bottom: 30px;
      font-size: 24px;
      font-weight: bold;
    }
    
    .sub-title {
      text-align: center;
      color: #333;
      margin-bottom: 24px;
      font-size: 18px;
      font-weight: 500;
    }
    
    .login-form {
      .ant-form-item {
        margin-bottom: 24px;
      }
      
      .login-button {
        height: 44px;
        font-size: 16px;
        border-radius: 8px;
        background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
        border: none;
        
        &:hover {
          background: linear-gradient(135deg, #40a9ff 0%, #1890ff 100%);
        }
      }
      
      .form-footer {
        text-align: center;
        margin-top: 16px;
        
        .register-link {
          color: #1890ff;
          text-decoration: none;
          transition: color 0.3s;
          font-size: 14px;
          
          &:hover {
            color: #40a9ff;
          }
        }
      }
    }
  }
}

@keyframes wave {
  0% {
    transform: translate(-50%, -50%) rotate(0deg);
  }
  100% {
    transform: translate(-50%, -50%) rotate(360deg);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) scale(1);
    opacity: 0.8;
  }
  50% {
    transform: translateY(-20px) scale(1.2);
    opacity: 0.4;
  }
}
</style> 