<template>
  <div class="record">
    <div class="operation-bar">
      <a-space>
        <a-button type="primary" @click="showModal">
          <plus-outlined /> 新增记录
        </a-button>
        <a-range-picker
          v-model:value="dateRange"
          style="width: 250px"
          @change="handleDateChange"
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
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import dayjs from 'dayjs'

const columns = [
  {
    title: '关怀对象',
    dataIndex: 'personName',
    key: 'personName'
  },
  {
    title: '所属分类',
    dataIndex: 'category',
    key: 'category',
    width: 100
  },
  {
    title: '关怀类型',
    dataIndex: 'type',
    key: 'type',
    width: 100
  },
  {
    title: '关怀时间',
    dataIndex: 'careTime',
    key: 'careTime',
    width: 180
  },
  {
    title: '关怀内容',
    dataIndex: 'content',
    key: 'content'
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
    fixed: 'right'
  }
]

const peopleOptions = [
  { value: 1, label: '张三（老年人）' },
  { value: 2, label: '李四（残疾人）' }
]

const dataSource = ref([
  {
    id: 1,
    personId: 1,
    personName: '张三',
    category: '老年人',
    type: '走访',
    careTime: '2024-04-09 10:00:00',
    content: '上门走访，了解生活情况',
    remark: '身体状况良好'
  },
  {
    id: 2,
    personId: 2,
    personName: '李四',
    category: '残疾人',
    type: '物资帮助',
    careTime: '2024-04-08 14:30:00',
    content: '送去生活必需品',
    remark: '需要定期关注'
  }
])

const loading = ref(false)
const modalVisible = ref(false)
const modalTitle = ref('新增记录')
const formRef = ref(null)
const currentId = ref(null)
const dateRange = ref(null)

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
    const selectedPerson = peopleOptions.find(item => item.value === formState.personId)
    const data = {
      ...formState,
      careTime: dayjs(formState.careTime).format('YYYY-MM-DD HH:mm:ss'),
      personName: selectedPerson?.label.split('（')[0],
      category: selectedPerson?.label.match(/（(.+)）/)[1]
    }

    if (currentId.value) {
      const index = dataSource.value.findIndex(item => item.id === currentId.value)
      if (index > -1) {
        dataSource.value[index] = {
          ...dataSource.value[index],
          ...data
        }
        message.success('编辑成功')
      }
    } else {
      dataSource.value.push({
        id: Date.now(),
        ...data
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

const handleDateChange = () => {
  loading.value = true
  // 这里应该调用按日期范围查询的 API
  loading.value = false
}
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