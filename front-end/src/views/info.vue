<template>
  <div class="info">
    <div class="page-header">
      <h1>{{ isAdmin ? '人员信息管理' : '人员信息' }}</h1>
    </div>
    <div class="operation-bar">
      <a-space>
        <a-button type="primary" @click="showModal">
          <plus-outlined /> 新增人员
        </a-button>
        <a-input-search
          v-model:value="searchKeyword"
          placeholder="请输入姓名或身份证号搜索"
          style="width: 250px"
          @search="handleSearch"
        />
      </a-space>
    </div>

    <a-table
      :columns="columns"
      :data-source="dataSource"
      :loading="loading"
      rowKey="id"
      bordered
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'category'">
          <a-tag>{{ record.category }}</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical" />
            <a-popconfirm
              title="确定要删除这条记录吗？"
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
      width="800px"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
    >
      <a-form
        ref="formRef"
        :model="formState"
        :rules="rules"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 19 }"
      >
        <a-form-item label="姓名" name="name">
          <a-input v-model:value="formState.name" placeholder="请输入姓名" />
        </a-form-item>
        <a-form-item label="身份证号" name="idCard">
          <a-input v-model:value="formState.idCard" placeholder="请输入身份证号" />
        </a-form-item>
        <a-form-item label="性别" name="gender">
          <a-radio-group v-model:value="formState.gender">
            <a-radio value="男">男</a-radio>
            <a-radio value="女">女</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="年龄" name="age">
          <a-input-number v-model:value="formState.age" :min="1" :max="150" style="width: 100%" />
        </a-form-item>
        <a-form-item label="联系电话" name="phone">
          <a-input v-model:value="formState.phone" placeholder="请输入联系电话" />
        </a-form-item>
        <a-form-item label="所属分类" name="category">
          <a-select v-model:value="formState.category" placeholder="请选择所属分类">
            <a-select-option value="老年人">老年人</a-select-option>
            <a-select-option value="残疾人">残疾人</a-select-option>
            <a-select-option value="低保户">低保户</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="家庭住址" name="address">
          <a-textarea
            v-model:value="formState.address"
            placeholder="请输入家庭住址"
            :rows="2"
          />
        </a-form-item>
        <a-form-item label="备注" name="remark">
          <a-textarea
            v-model:value="formState.remark"
            placeholder="请输入备注信息"
            :rows="3"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import axios from 'axios'

const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
const isAdmin = userInfo.role === 'ADMIN'

const columns = [
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name'
  },
  {
    title: '性别',
    dataIndex: 'gender',
    key: 'gender',
    width: 80
  },
  {
    title: '年龄',
    dataIndex: 'age',
    key: 'age',
    width: 80
  },
  {
    title: '身份证号',
    dataIndex: 'idCard',
    key: 'idCard',
    width: 180
  },
  {
    title: '联系电话',
    dataIndex: 'phone',
    key: 'phone',
    width: 130
  },
  {
    title: '所属分类',
    dataIndex: 'category',
    key: 'category',
    width: 100
  },
  {
    title: '家庭住址',
    dataIndex: 'address',
    key: 'address'
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
    fixed: 'right'
  }
]

const dataSource = ref([])
const loading = ref(false)
const modalVisible = ref(false)
const modalTitle = ref('新增人员')
const formRef = ref(null)
const currentId = ref(null)
const searchKeyword = ref('')

const formState = reactive({
  name: '',
  gender: '男',
  age: 0,
  idCard: '',
  phone: '',
  category: undefined,
  address: '',
  remark: ''
})

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  category: [{ required: true, message: '请选择所属分类', trigger: 'change' }],
  address: [{ required: true, message: '请输入家庭住址', trigger: 'blur' }]
}

// 获取API基础URL
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

// 获取token
const getToken = () => {
  return localStorage.getItem('token')
}

// 获取API请求配置（包含认证头）
const getRequestConfig = () => {
  return {
    headers: {
      'Authorization': `Bearer ${getToken()}`
    }
  }
}

// 加载人员列表
const loadPersons = async (keyword) => {
  loading.value = true
  try {
    let url = `${apiBaseUrl}/api/persons`
    if (keyword) {
      url += `?keyword=${encodeURIComponent(keyword)}`
    }
    
    const response = await axios.get(url, getRequestConfig())
    if (response.data.code === 200) {
      dataSource.value = response.data.data
    } else {
      message.error(response.data.message || '获取人员列表失败')
    }
  } catch (error) {
    console.error('获取人员列表失败:', error)
    message.error('获取人员列表失败')
  } finally {
    loading.value = false
  }
}

// 获取人员详情
const getPersonDetail = async (id) => {
  try {
    const response = await axios.get(`${apiBaseUrl}/api/persons/${id}`, getRequestConfig())
    if (response.data.code === 200) {
      return response.data.data
    } else {
      message.error(response.data.message || '获取人员详情失败')
      return null
    }
  } catch (error) {
    console.error('获取人员详情失败:', error)
    message.error('获取人员详情失败')
    return null
  }
}

// 创建人员
const createPerson = async (personData) => {
  try {
    const response = await axios.post(`${apiBaseUrl}/api/persons`, personData, getRequestConfig())
    if (response.data.code === 200) {
      message.success('新增成功')
      return response.data.data
    } else {
      message.error(response.data.message || '新增失败')
      return null
    }
  } catch (error) {
    console.error('新增失败:', error)
    message.error('新增失败')
    return null
  }
}

// 更新人员
const updatePerson = async (id, personData) => {
  try {
    const response = await axios.put(`${apiBaseUrl}/api/persons/${id}`, personData, getRequestConfig())
    if (response.data.code === 200) {
      message.success('更新成功')
      return response.data.data
    } else {
      message.error(response.data.message || '更新失败')
      return null
    }
  } catch (error) {
    console.error('更新失败:', error)
    if (error.response && error.response.status === 403) {
      message.error('无权修改此人员信息')
    } else {
      message.error('更新失败')
    }
    return null
  }
}

// 删除人员
const deletePerson = async (id) => {
  try {
    const response = await axios.delete(`${apiBaseUrl}/api/persons/${id}`, getRequestConfig())
    if (response.data.code === 200) {
      message.success('删除成功')
      return true
    } else {
      message.error(response.data.message || '删除失败')
      return false
    }
  } catch (error) {
    console.error('删除失败:', error)
    if (error.response && error.response.status === 403) {
      message.error('无权删除此人员信息')
    } else {
      message.error('删除失败')
    }
    return false
  }
}

// 显示新增模态框
const showModal = () => {
  modalTitle.value = '新增人员'
  currentId.value = null
  Object.assign(formState, {
    name: '',
    gender: '男',
    age: 0,
    idCard: '',
    phone: '',
    category: undefined,
    address: '',
    remark: ''
  })
  modalVisible.value = true
}

// 处理编辑操作
const handleEdit = async (record) => {
  loading.value = true
  try {
    const personDetail = await getPersonDetail(record.id)
    if (personDetail) {
      modalTitle.value = '编辑人员'
      currentId.value = personDetail.id
      Object.assign(formState, {
        name: personDetail.name,
        gender: personDetail.gender,
        age: personDetail.age,
        idCard: personDetail.idCard,
        phone: personDetail.phone,
        category: personDetail.category,
        address: personDetail.address,
        remark: personDetail.remark
      })
      modalVisible.value = true
    }
  } finally {
    loading.value = false
  }
}

// 处理删除操作
const handleDelete = async (record) => {
  loading.value = true
  try {
    const success = await deletePerson(record.id)
    if (success) {
      await loadPersons(searchKeyword.value)
    }
  } finally {
    loading.value = false
  }
}

// 处理模态框确认
const handleModalOk = () => {
  formRef.value.validate().then(async () => {
    loading.value = true
    try {
      if (currentId.value) {
        // 编辑现有记录
        const updatedPerson = await updatePerson(currentId.value, formState)
        if (updatedPerson) {
          modalVisible.value = false
          await loadPersons(searchKeyword.value)
        }
      } else {
        // 新增记录
        const newPerson = await createPerson(formState)
        if (newPerson) {
          modalVisible.value = false
          await loadPersons(searchKeyword.value)
        }
      }
    } finally {
      loading.value = false
    }
  });
}

// 处理模态框取消
const handleModalCancel = () => {
  modalVisible.value = false
}

// 处理搜索
const handleSearch = () => {
  loadPersons(searchKeyword.value)
}

// 组件挂载时加载数据
onMounted(() => {
  loadPersons()
})
</script>

<style lang="less" scoped>
.info {
  .page-header {
    margin-bottom: 24px;
    
    h1 {
      font-size: 20px;
      font-weight: 500;
    }
  }
  
  .operation-bar {
    margin-bottom: 16px;
  }

  .danger {
    color: #ff4d4f;
  }
}
</style> 