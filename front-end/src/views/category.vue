<template>
  <div class="category">
    <div class="operation-bar">
      <a-row :gutter="16">
        <a-col :span="8">
          <a-input-search
            v-model:value="searchKeyword"
            placeholder="请输入分类名称或描述"
            enter-button
            @search="handleSearch"
          />
        </a-col>
        <a-col :span="16" style="text-align: right;">
          <a-button type="primary" @click="showModal">
            <plus-outlined /> 新增分类
          </a-button>
        </a-col>
      </a-row>
    </div>

    <a-table
      :columns="columns"
      :data-source="dataSource"
      :loading="loading"
      rowKey="id"
      bordered
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <a-space>
            <a @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical" />
            <a-popconfirm
              title="确定要删除这个分类吗？"
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
        <a-form-item label="分类名称" name="name">
          <a-input v-model:value="formState.name" placeholder="请输入分类名称" />
        </a-form-item>
        <a-form-item label="分类描述" name="description">
          <a-textarea
            v-model:value="formState.description"
            placeholder="请输入分类描述"
            :rows="4"
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
import { getCategories, createCategory, updateCategory, deleteCategory } from '@/api/category'
import dayjs from 'dayjs'

const columns = [
  {
    title: '分类名称',
    dataIndex: 'name',
    key: 'name'
  },
  {
    title: '描述',
    dataIndex: 'description',
    key: 'description'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    customRender: ({text}) => {
      return dayjs(text).format('YYYY-MM-DD HH:mm')
    }
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    key: 'updateTime',
    customRender: ({text}) => {
      return dayjs(text).format('YYYY-MM-DD HH:mm')
    }
  },
  {
    title: '操作',
    key: 'action',
    width: 200
  }
]

const dataSource = ref([])
const loading = ref(false)
const modalVisible = ref(false)
const modalTitle = ref('新增分类')
const formRef = ref(null)
const currentId = ref(null)
const searchKeyword = ref('')

const formState = reactive({
  name: '',
  description: '',
})

const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
  description: [{ required: true, message: '请输入分类描述', trigger: 'blur' }],
}

const showModal = () => {
  modalTitle.value = '新增分类'
  currentId.value = null
  formState.name = ''
  formState.description = ''
  modalVisible.value = true
}

const handleEdit = (record) => {
  modalTitle.value = '编辑分类'
  currentId.value = record.id
  formState.name = record.name
  formState.description = record.description
  modalVisible.value = true
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getCategories(searchKeyword.value)
    dataSource.value = res.data
  } catch (error) {
    console.error('加载数据失败:', error)
    message.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})

const handleDelete = async (record) => {
  loading.value = true
  try {
    await deleteCategory(record.id)
    message.success('删除成功')
    loadData()
  } catch (error) {
    console.error('删除失败:', error)
    message.error('删除失败')
    loading.value = false
  }
}

const handleModalOk = () => {
  formRef.value.validate().then(async () => {
    loading.value = true
    try {
      if (currentId.value) {
        // 编辑
        await updateCategory(currentId.value, formState)
        message.success('编辑成功')
      } else {
        // 新增
        await createCategory(formState)
        message.success('新增成功')
      }
      modalVisible.value = false
      loadData()
    } catch (error) {
      console.error('操作失败:', error)
      message.error('操作失败')
      loading.value = false
    }
  })
}

const handleModalCancel = () => {
  modalVisible.value = false
}

const handleSearch = () => {
  loadData()
}
</script>

<style lang="less" scoped>
.category {
  .operation-bar {
    margin-bottom: 16px;
    
    .ant-input-search {
      width: 100%;
    }
  }

  .danger {
    color: #ff4d4f;
  }
}
</style> 