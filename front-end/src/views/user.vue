<template>
  <div class="user">
    <div class="operation-bar">
      <a-button type="primary" @click="showModal">
        <plus-outlined /> 新增用户
      </a-button>
    </div>

    <a-table
      :columns="columns"
      :data-source="dataSource"
      :loading="loading"
      rowKey="id"
      bordered
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'role'">
          <a-tag :color="record.role === 'admin' ? 'blue' : 'green'">
            {{ record.role === 'admin' ? '管理员' : '普通用户' }}
          </a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical" />
            <a @click="handleResetPassword(record)">重置密码</a>
            <a-divider type="vertical" />
            <a-popconfirm
              title="确定要删除这个用户吗？"
              @confirm="handleDelete(record)"
            >
              <a class="danger">删除</a>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <a-modal
      v-model:visible="modalVisible"
      :title="modalTitle"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
    >
      <a-form
        ref="formRef"
        :model="formState"
        :rules="rules"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="用户名" name="username">
          <a-input
            v-model:value="formState.username"
            placeholder="请输入用户名"
            :disabled="!!currentId"
          />
        </a-form-item>
        <a-form-item
          v-if="!currentId"
          label="密码"
          name="password"
        >
          <a-input-password
            v-model:value="formState.password"
            placeholder="请输入密码"
          />
        </a-form-item>
        <a-form-item label="姓名" name="name">
          <a-input v-model:value="formState.name" placeholder="请输入姓名" />
        </a-form-item>
        <a-form-item label="手机号" name="phone">
          <a-input v-model:value="formState.phone" placeholder="请输入手机号" />
        </a-form-item>
        <a-form-item label="角色" name="role">
          <a-select v-model:value="formState.role" placeholder="请选择角色">
            <a-select-option value="admin">管理员</a-select-option>
            <a-select-option value="user">普通用户</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal
      v-model:visible="resetPasswordVisible"
      title="重置密码"
      @ok="handleResetPasswordOk"
      @cancel="handleResetPasswordCancel"
    >
      <a-form
        ref="resetPasswordFormRef"
        :model="resetPasswordForm"
        :rules="resetPasswordRules"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="新密码" name="password">
          <a-input-password
            v-model:value="resetPasswordForm.password"
            placeholder="请输入新密码"
          />
        </a-form-item>
        <a-form-item label="确认密码" name="confirmPassword">
          <a-input-password
            v-model:value="resetPasswordForm.confirmPassword"
            placeholder="请再次输入新密码"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getUserList, createUser, updateUser, deleteUser, resetUserPassword } from '@/api/user'

const columns = [
  {
    title: '用户名',
    dataIndex: 'username',
    key: 'username'
  },
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name'
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    key: 'phone'
  },
  {
    title: '角色',
    dataIndex: 'role',
    key: 'role',
    width: 100
  },
  {
    title: '操作',
    key: 'action',
    width: 250
  }
]

const dataSource = ref([])
const loading = ref(false)
const modalVisible = ref(false)
const modalTitle = ref('新增用户')
const formRef = ref(null)
const currentId = ref(null)

const formState = reactive({
  username: '',
  password: '',
  name: '',
  phone: '',
  role: undefined
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

const resetPasswordVisible = ref(false)
const resetPasswordFormRef = ref(null)
const resetPasswordUserId = ref(null)

const resetPasswordForm = reactive({
  password: '',
  confirmPassword: ''
})

const resetPasswordRules = {
  password: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (_, value) => {
        if (value && value !== resetPasswordForm.password) {
          return Promise.reject('两次输入的密码不一致')
        }
        return Promise.resolve()
      },
      trigger: 'blur'
    }
  ]
}

const fetchData = async () => {
  try {
    loading.value = true
    const response = await getUserList()
    dataSource.value = response.data
  } catch (error) {
    message.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

const showModal = () => {
  modalTitle.value = '新增用户'
  currentId.value = null
  Object.assign(formState, {
    username: '',
    password: '',
    name: '',
    phone: '',
    role: undefined
  })
  modalVisible.value = true
}

const handleEdit = (record) => {
  modalTitle.value = '编辑用户'
  currentId.value = record.id
  Object.assign(formState, record)
  modalVisible.value = true
}

const handleDelete = async (record) => {
  try {
    loading.value = true
    await deleteUser(record.id)
    message.success('删除成功')
    fetchData()
  } catch (error) {
    message.error('删除失败')
  } finally {
    loading.value = false
  }
}

const handleModalOk = () => {
  formRef.value.validate().then(async () => {
    try {
      loading.value = true
      if (currentId.value) {
        await updateUser({
          id: currentId.value,
          ...formState
        })
        message.success('编辑成功')
      } else {
        await createUser(formState)
        message.success('新增成功')
      }
      modalVisible.value = false
      fetchData()
    } catch (error) {
      message.error(error.response?.data?.message || '操作失败')
    } finally {
      loading.value = false
    }
  })
}

const handleModalCancel = () => {
  modalVisible.value = false
}

const handleResetPassword = async (record) => {
  try {
    loading.value = true
    await resetUserPassword(record.id)
    message.success('密码重置成功')
  } catch (error) {
    message.error('密码重置失败')
  } finally {
    loading.value = false
  }
}

const handleResetPasswordOk = () => {
  resetPasswordFormRef.value.validate().then(() => {
    loading.value = true
    // 这里应该调用重置密码 API
    message.success('密码重置成功')
    loading.value = false
    resetPasswordVisible.value = false
  })
}

const handleResetPasswordCancel = () => {
  resetPasswordVisible.value = false
}

// 页面加载时获取数据
fetchData()
</script>

<style lang="less" scoped>
.user {
  .operation-bar {
    margin-bottom: 16px;
  }

  .danger {
    color: #ff4d4f;
  }
}
</style> 