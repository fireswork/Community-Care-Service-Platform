<template>
  <div class="record">
    <div class="operation-bar">
      <a-space>
        <a-button type="primary" @click="showModal">
          <plus-outlined /> 新增记录
        </a-button>
        <a-select
          v-model:value="searchCategory"
          placeholder="选择所属分类"
          style="width: 160px"
          allowClear
          @change="handleSearch"
        >
          <a-select-option v-for="category in categories" :key="category.id" :value="category.name">
            {{ category.name }}
          </a-select-option>
        </a-select>
        <a-select
          v-model:value="searchType"
          placeholder="选择关怀类型"
          style="width: 160px"
          allowClear
          @change="handleSearch"
        >
          <a-select-option value="走访">走访</a-select-option>
          <a-select-option value="电话慰问">电话慰问</a-select-option>
          <a-select-option value="物资帮助">物资帮助</a-select-option>
          <a-select-option value="医疗救助">医疗救助</a-select-option>
        </a-select>
      </a-space>
    </div>

    <a-table
      :columns="columns"
      :data-source="dataSource"
      :loading="loading"
      rowKey="id"
      :scroll="{x: 1300}"
      bordered
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'personId'">
          {{ getPerson(record.personId)?.label || '-' }}
        </template>
        <template v-if="column.key === 'category'">
          {{ getPerson(record.personId)?.category || '-' }}
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
        <a-form-item label="关怀对象" name="personId">
          <a-select
            v-model:value="formState.personId"
            placeholder="请选择关怀对象"
            :options="peopleOptions"
          />
        </a-form-item>
        <a-form-item label="关怀类型" name="type">
          <a-select v-model:value="formState.type" placeholder="请选择关怀类型">
            <a-select-option value="走访">走访</a-select-option>
            <a-select-option value="电话慰问">电话慰问</a-select-option>
            <a-select-option value="物资帮助">物资帮助</a-select-option>
            <a-select-option value="医疗救助">医疗救助</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="关怀时间" name="careTime">
          <a-date-picker
            v-model:value="formState.careTime"
            style="width: 100%"
            show-time
            format="YYYY-MM-DD HH:mm:ss"
          />
        </a-form-item>
        <a-form-item label="关怀内容" name="content">
          <a-textarea
            v-model:value="formState.content"
            placeholder="请输入关怀内容"
            :rows="4"
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
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import dayjs from 'dayjs'
import axios from 'axios'

const peopleOptions = ref([])

const columns = [
  {
    title: '关怀对象',
    dataIndex: 'personId',
    key: 'personId',
  },
  {
    title: '所属分类',
    dataIndex: 'category',
    key: 'category',
  },
  {
    title: '关怀类型',
    dataIndex: 'type',
    key: 'type',
  },
  {
    title: '关怀时间',
    dataIndex: 'careTime',
    key: 'careTime',
  },
  {
    title: '关怀内容',
    dataIndex: 'content',
    key: 'content'
  },
  {
    title: '操作',
    key: 'action',
    fixed: 'right'
  }
]

const categories = ref([])
const searchCategory = ref()
const searchType = ref()
const loading = ref(false)
const modalVisible = ref(false)
const modalTitle = ref('新增记录')
const formRef = ref(null)
const currentId = ref(null)
const dataSource = ref([])

// 获取API基础URL
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

// 获取token
const getToken = () => {
  return localStorage.getItem('token')
}

// 获取API请求配置
const getRequestConfig = () => {
  return {
    headers: {
      'Authorization': `Bearer ${getToken()}`
    }
  }
}

// 加载分类列表
const loadCategories = async () => {
  try {
    const response = await axios.get(`${apiBaseUrl}/api/categories`, getRequestConfig())
    if (response.data.code === 200) {
      categories.value = response.data.data
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

// 加载关怀记录列表
const loadRecords = async () => {
  loading.value = true
  try {
    let url = `${apiBaseUrl}/api/records`
    const params = {}
    if (searchCategory.value) {
      params.category = searchCategory.value
    }
    if (searchType.value) {
      params.type = searchType.value
    }
    
    const response = await axios.get(url, { 
      ...getRequestConfig(),
      params
    })
    if (response.data.code === 200) {
      dataSource.value = response.data.data
    } else {
      message.error(response.data.message || '获取记录列表失败')
    }
  } catch (error) {
    console.error('获取记录列表失败:', error)
    message.error('获取记录列表失败')
  } finally {
    loading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  loadRecords()
}

const formState = reactive({
  personId: undefined,
  type: undefined,
  careTime: null,
  content: '',
  remark: ''
})

const rules = {
  personId: [{ required: true, message: '请选择关怀对象', trigger: 'change' }],
  type: [{ required: true, message: '请选择关怀类型', trigger: 'change' }],
  careTime: [{ required: true, message: '请选择关怀时间', trigger: 'change' }],
  content: [{ required: true, message: '请输入关怀内容', trigger: 'blur' }]
}

const showModal = () => {
  modalTitle.value = '新增记录'
  currentId.value = null
  Object.assign(formState, {
    personId: undefined,
    type: undefined,
    careTime: null,
    content: '',
    remark: ''
  })
  modalVisible.value = true
}

const handleEdit = (record) => {
  modalTitle.value = '编辑记录'
  currentId.value = record.id
  Object.assign(formState, {
    ...record,
    careTime: dayjs(record.careTime)
  })
  modalVisible.value = true
}

const handleDelete = async (record) => {
  loading.value = true
  try {
    const response = await axios.delete(`${apiBaseUrl}/api/records/${record.id}`, getRequestConfig())
    if (response.data.code === 200) {
      message.success('删除成功')
      await loadRecords()
    } else {
      message.error(response.data.message || '删除失败')
    }
  } catch (error) {
    console.error('删除失败:', error)
    message.error('删除失败')
  } finally {
    loading.value = false
  }
}

const handleModalOk = () => {
  formRef.value.validate().then(async () => {
    loading.value = true
    try {
      const data = {
        ...formState,
        careTime: dayjs(formState.careTime).format('YYYY-MM-DD HH:mm:ss')
      }

      let response
      if (currentId.value) {
        response = await axios.put(`${apiBaseUrl}/api/records/${currentId.value}`, data, getRequestConfig())
      } else {
        response = await axios.post(`${apiBaseUrl}/api/records`, data, getRequestConfig())
      }

      if (response.data.code === 200) {
        message.success(currentId.value ? '编辑成功' : '新增成功')
        modalVisible.value = false
        await loadRecords()
      } else {
        message.error(response.data.message || (currentId.value ? '编辑失败' : '新增失败'))
      }
    } catch (error) {
      console.error(currentId.value ? '编辑失败' : '新增失败', error)
      message.error(currentId.value ? '编辑失败' : '新增失败')
    } finally {
      loading.value = false
    }
  })
}

const handleModalCancel = () => {
  modalVisible.value = false
}

// 加载关怀对象列表
const loadPeople = async () => {
  try {
    const response = await axios.get(`${apiBaseUrl}/api/persons`, getRequestConfig())
    if (response.data.code === 200) {
      peopleOptions.value = response.data.data.map(person => ({
        value: person.id,
        label: `${person.name}（${person.category}）`
      }))
    } else {
      message.error(response.data.message || '获取关怀对象列表失败')
    }
  } catch (error) {
    console.error('获取关怀对象列表失败:', error)
    message.error('获取关怀对象列表失败')
  }
}

// 根据personId获取对应的人员信息
const getPerson = (personId) => {
  const option = peopleOptions.value.find(item => item.value === personId);
  if (!option) return null;
  
  // 解析label中的类别信息 "姓名（类别）"
  const match = option.label.match(/(.+)（(.+)）/);
  if (match) {
    return {
      ...option,
      name: match[1],
      category: match[2]
    };
  }
  
  return option;
}

// 组件挂载时加载数据
onMounted(() => {
  loadPeople()
  loadCategories()
  loadRecords()
})
</script>

<style lang="less" scoped>
.record {
  .operation-bar {
    margin-bottom: 16px;
  }

  .danger {
    color: #ff4d4f;
  }
}
</style> 