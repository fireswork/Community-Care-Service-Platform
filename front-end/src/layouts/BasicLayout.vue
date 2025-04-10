<template>
  <a-layout class="layout">
    <a-layout-sider v-model:collapsed="collapsed" :trigger="null" collapsible>
      <div class="logo">
        <heart-filled class="logo-icon" />
        <h1 v-show="!collapsed">社区关怀服务平台</h1>
      </div>
      <a-menu
        v-model:selectedKeys="selectedKeys"
        v-model:openKeys="openKeys"
        mode="inline"
        theme="dark"
        @click="handleMenuClick"
      >
        <template v-for="item in routes" :key="item.path">
          <template v-if="!item.children">
            <a-menu-item :key="item.path">
              <template #icon>
                <component :is="item.meta?.icon" />
              </template>
              <span>{{ item.meta?.title }}</span>
            </a-menu-item>
          </template>
          <template v-else>
            <a-sub-menu :key="item.path">
              <template #icon>
                <component :is="item.meta?.icon" />
              </template>
              <template #title>{{ item.meta?.title }}</template>
              <a-menu-item v-for="child in item.children" :key="child.path">
                <span>{{ child.meta?.title }}</span>
              </a-menu-item>
            </a-sub-menu>
          </template>
        </template>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header class="header">
        <menu-unfold-outlined
          v-if="collapsed"
          class="trigger"
          @click="() => (collapsed = !collapsed)"
        />
        <menu-fold-outlined
          v-else
          class="trigger"
          @click="() => (collapsed = !collapsed)"
        />
        <div class="right">
          <a-avatar class="avatar">
            <template #icon><user-outlined /></template>
          </a-avatar>
          <a-dropdown>
            <a class="ant-dropdown-link" @click.prevent>
              Admin <down-outlined />
            </a>
            <template #overlay>
              <a-menu>
                <a-menu-item key="1" @click="showUserInfoModal">
                  <user-outlined />
                  个人信息
                </a-menu-item>
                <a-menu-item key="2" @click="showChangePasswordModal">
                  <key-outlined />
                  修改密码
                </a-menu-item>
                <a-menu-item key="3" @click="handleLogout">
                  <logout-outlined />
                  退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view></router-view>
      </a-layout-content>
    </a-layout>

    <!-- 个人信息 Modal -->
    <a-modal
      v-model:visible="userInfoModalVisible"
      title="个人信息"
      @ok="handleUserInfoSubmit"
      :confirmLoading="userInfoConfirmLoading"
    >
      <a-form
        ref="userInfoFormRef"
        :model="userInfoForm"
        :rules="userInfoRules"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="用户名">
          <span>{{ userInfoForm.username }}</span>
        </a-form-item>
        <a-form-item label="姓名" name="name">
          <a-input v-model:value="userInfoForm.name" />
        </a-form-item>
        <a-form-item label="手机号" name="phone">
          <a-input v-model:value="userInfoForm.phone" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 修改密码 Modal -->
    <a-modal
      v-model:visible="changePasswordModalVisible"
      title="修改密码"
      @ok="handleChangePasswordSubmit"
      :confirmLoading="changePasswordConfirmLoading"
    >
      <a-form
        ref="changePasswordFormRef"
        :model="changePasswordForm"
        :rules="changePasswordRules"
        :label-col="{ span: 8 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="原密码" name="oldPassword">
          <a-input-password v-model:value="changePasswordForm.oldPassword" />
        </a-form-item>
        <a-form-item label="新密码" name="newPassword">
          <a-input-password v-model:value="changePasswordForm.newPassword" />
        </a-form-item>
        <a-form-item label="确认新密码" name="confirmPassword">
          <a-input-password v-model:value="changePasswordForm.confirmPassword" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  MenuUnfoldOutlined,
  MenuFoldOutlined,
  UserOutlined,
  SettingOutlined,
  LogoutOutlined,
  DownOutlined,
  HeartFilled,
  TeamOutlined,
  ProfileOutlined,
  ScheduleOutlined,
  BarChartOutlined,
  KeyOutlined
} from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()
const collapsed = ref(false)
const selectedKeys = ref([route.path])
const openKeys = ref([])

// 监听路由变化，更新选中的菜单项
watch(
  () => route.path,
  (path) => {
    selectedKeys.value = [path]
  }
)

const routes = computed(() => {
  return router.options.routes.find(route => route.path === '/').children
})

const handleMenuClick = ({ key }) => {
  router.push(key)
}

const handleLogout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}

// 个人信息相关
const userInfoModalVisible = ref(false)
const userInfoConfirmLoading = ref(false)
const userInfoFormRef = ref(null)
const userInfoForm = reactive({
  username: 'admin', // 这里应该从实际用户数据中获取
  name: '',
  phone: ''
})

const userInfoRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

const showUserInfoModal = () => {
  userInfoModalVisible.value = true
  // 这里应该调用API获取用户信息
  userInfoForm.name = '管理员'
  userInfoForm.phone = '13800138000'
}

const handleUserInfoSubmit = () => {
  userInfoFormRef.value.validate().then(() => {
    userInfoConfirmLoading.value = true
    // 这里应该调用修改用户信息的API
    setTimeout(() => {
      message.success('个人信息修改成功')
      userInfoConfirmLoading.value = false
      userInfoModalVisible.value = false
    }, 1500)
  })
}

// 修改密码相关
const changePasswordModalVisible = ref(false)
const changePasswordConfirmLoading = ref(false)
const changePasswordFormRef = ref(null)
const changePasswordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = async (rule, value) => {
  if (!value) {
    return Promise.reject('请确认新密码')
  }
  if (value !== changePasswordForm.newPassword) {
    return Promise.reject('两次输入的密码不一致')
  }
  return Promise.resolve()
}

const changePasswordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码至少6个字符', trigger: 'blur' }
  ],
  confirmPassword: [{ required: true, validator: validateConfirmPassword, trigger: 'blur' }]
}

const showChangePasswordModal = () => {
  changePasswordModalVisible.value = true
  changePasswordForm.oldPassword = ''
  changePasswordForm.newPassword = ''
  changePasswordForm.confirmPassword = ''
}

const handleChangePasswordSubmit = () => {
  changePasswordFormRef.value.validate().then(() => {
    changePasswordConfirmLoading.value = true
    // 这里应该调用修改密码的API
    setTimeout(() => {
      message.success('密码修改成功')
      changePasswordConfirmLoading.value = false
      changePasswordModalVisible.value = false
    }, 1500)
  })
}
</script>

<style lang="less" scoped>
.layout {
  min-height: 100vh;

  .logo {
    height: 64px;
    margin: 0;
    padding: 16px;
    display: flex;
    align-items: center;
    background: #002140;
    
    .logo-icon {
      width: 32px;
      height: 32px;
      color: #1890ff;
      display: flex;
      align-items: center;
    }

    h1 {
      color: white;
      margin: 0;
      font-size: 18px;
      font-weight: 600;
    }
  }

  .ant-layout-sider {
    min-width: 250px !important;
    width: 250px !important;
    max-width: 250px !important;
  }

  .header {
    background: #fff;
    padding: 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

    .trigger {
      padding: 0 24px;
      font-size: 18px;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: #1890ff;
      }
    }

    .right {
      padding-right: 24px;
      display: flex;
      align-items: center;

      .avatar {
        margin-right: 8px;
        background-color: #1890ff;
      }

      .ant-dropdown-link {
        display: flex;
        align-items: center;
        
        .anticon {
          margin-left: 4px;
        }
      }
    }
  }

  .content {
    margin: 24px 16px;
    padding: 24px;
    background: #fff;
    min-height: 280px;
  }
}

.ant-dropdown-link {
  color: rgba(0, 0, 0, 0.85);
  cursor: pointer;
  padding: 0 12px;
  display: inline-flex;
  align-items: center;
  
  .anticon {
    margin-left: 4px;
  }
  
  &:hover {
    color: #1890ff;
  }
}

:deep(.ant-modal-body) {
  padding: 24px;
  
  .ant-form-item:last-child {
    margin-bottom: 0;
  }
}
</style> 