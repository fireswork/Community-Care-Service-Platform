<template>
  <div class="category">
    <div class="operation-bar">
      <a-button type="primary" @click="showModal">
        <plus-outlined /> 新增分类
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
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status ? 'success' : 'error'">
            {{ record.status ? '启用' : '禁用' }}
          </a-tag>
        </template>
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
        <a-form-item label="排序" name="sort">
          <a-input-number
            v-model:value="formState.sort"
            :min="1"
            :max="999"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-switch v-model:checked="formState.status" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'

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
    title: '排序',
    dataIndex: 'sort',
    key: 'sort',
    width: 100
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 100
  },
  {
    title: '操作',
    key: 'action',
    width: 200
  }
]

const dataSource = ref([
  {
    id: 1,
    name: '老年人',
    description: '年龄在60岁以上的老年人群体',
    sort: 1,
    status: true
  },
  {
    id: 2,
    name: '残疾人',
    description: '持有残疾证的残障人士',
    sort: 2,
    status: true
  },
  {
    id: 3,
    name: '低保户',
    description: '享受最低生活保障的家庭',
    sort: 3,
    status: true
  }
])

const loading = ref(false)
const modalVisible = ref(false)
const modalTitle = ref('新增分类')
const formRef = ref(null)
const currentId = ref(null)

const formState = reactive({
  name: '',
  description: '',
  sort: 1,
  status: true
})

const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
  description: [{ required: true, message: '请输入分类描述', trigger: 'blur' }],
  sort: [{ required: true, message: '请输入排序号', trigger: 'blur' }]
}

const showModal = () => {
  modalTitle.value = '新增分类'
  currentId.value = null
  formState.name = ''
  formState.description = ''
  formState.sort = 1
  formState.status = true
  modalVisible.value = true
}

const handleEdit = (record) => {
  modalTitle.value = '编辑分类'
  currentId.value = record.id
  formState.name = record.name
  formState.description = record.description
  formState.sort = record.sort
  formState.status = record.status
  modalVisible.value = true
}

const handleDelete = (record) => {
  loading.value = true
  // 这里应该调用删除 API
  const index = dataSource.value.findIndex(item => item.id === record.id)
  if (index > -1) {
    dataSource.value.splice(index, 1)
    message.success('删除成功')
  }
  loading.value = false
}

const handleModalOk = () => {
  formRef.value.validate().then(() => {
    loading.value = true
    // 这里应该调用保存 API
    if (currentId.value) {
      // 编辑
      const index = dataSource.value.findIndex(item => item.id === currentId.value)
      if (index > -1) {
        dataSource.value[index] = {
          ...dataSource.value[index],
          ...formState
        }
        message.success('编辑成功')
      }
    } else {
      // 新增
      dataSource.value.push({
        id: Date.now(),
        ...formState
      })
      message.success('新增成功')
    }
    loading.value = false
    modalVisible.value = false
  })
}

const handleModalCancel = () => {
  modalVisible.value = false
}
</script>

<style lang="less" scoped>
.category {
  .operation-bar {
    margin-bottom: 16px;
  }

  .danger {
    color: #ff4d4f;
  }
}
</style> 