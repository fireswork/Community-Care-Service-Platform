<template>
  <div class="info">
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
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'

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

const dataSource = ref([
  {
    id: 1,
    name: '张三',
    gender: '男',
    age: 72,
    idCard: '110101195001011234',
    phone: '13800138000',
    category: '老年人',
    address: '北京市东城区东华门街道',
    remark: '独居老人'
  },
  {
    id: 2,
    name: '李四',
    gender: '女',
    age: 45,
    idCard: '110101197801011234',
    phone: '13900139000',
    category: '残疾人',
    address: '北京市西城区西华门街道',
    remark: '肢体残疾'
  }
])

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

const handleEdit = (record) => {
  modalTitle.value = '编辑人员'
  currentId.value = record.id
  Object.assign(formState, record)
  modalVisible.value = true
}

const handleDelete = (record) => {
  loading.value = true
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
    if (currentId.value) {
      const index = dataSource.value.findIndex(item => item.id === currentId.value)
      if (index > -1) {
        dataSource.value[index] = {
          ...dataSource.value[index],
          ...formState
        }
        message.success('编辑成功')
      }
    } else {
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

const handleSearch = () => {
  loading.value = true
  // 这里应该调用搜索 API
  loading.value = false
}
</script>

<style lang="less" scoped>
.info {
  .operation-bar {
    margin-bottom: 16px;
  }

  .danger {
    color: #ff4d4f;
  }
}
</style> 