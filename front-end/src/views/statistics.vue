<template>
  <div class="statistics">
    <a-row :gutter="16">
      <a-col :span="6">
        <a-card>
          <statistic
            title="总关怀人数"
            :value="statsData.totalPersons"
            :value-style="{ color: '#3f8600' }"
          >
            <template #prefix>
              <team-outlined />
            </template>
          </statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card>
          <statistic
            title="本月新增"
            :value="statsData.newPersonsThisMonth"
            :value-style="{ color: '#cf1322' }"
          >
            <template #prefix>
              <rise-outlined />
            </template>
          </statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card>
          <statistic
            title="本月关怀次数"
            :value="statsData.careCountThisMonth"
          >
            <template #prefix>
              <heart-outlined />
            </template>
          </statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card>
          <statistic
            title="关怀覆盖率"
            :value="statsData.coverageRate"
            :precision="1"
            suffix="%"
          >
            <template #prefix>
              <like-outlined />
            </template>
          </statistic>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="16" style="margin-top: 16px">
      <a-col :span="12">
        <a-card title="人员分类统计">
          <v-chart class="chart" :option="categoryOption" />
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="关怀类型统计">
          <v-chart class="chart" :option="typeOption" />
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="16" style="margin-top: 16px">
      <a-col :span="24">
        <a-card title="近12个月关怀记录统计">
          <v-chart class="chart" :option="monthOption" />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Statistic } from 'ant-design-vue'
import { TeamOutlined, RiseOutlined, HeartOutlined, LikeOutlined } from '@ant-design/icons-vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart, BarChart, LineChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import { getOverviewStatistics, getCategoryStatistics, getCareTypeStatistics, getMonthlyStatistics } from '@/api/statistics'

use([
  CanvasRenderer,
  PieChart,
  BarChart,
  LineChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

// 统计数据
const statsData = reactive({
  totalPersons: 0,
  newPersonsThisMonth: 0,
  careCountThisMonth: 0,
  coverageRate: 0
})

// 图表数据
const categoryOption = ref({
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '人员分类',
      type: 'pie',
      radius: ['50%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 20,
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: []
    }
  ]
})

const typeOption = ref({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: [
    {
      type: 'category',
      data: [],
      axisTick: {
        alignWithLabel: true
      }
    }
  ],
  yAxis: [
    {
      type: 'value'
    }
  ],
  series: [
    {
      name: '次数',
      type: 'bar',
      barWidth: '60%',
      data: []
    }
  ]
})

const monthOption = ref({
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['关怀次数']
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '关怀次数',
      type: 'line',
      stack: 'Total',
      data: [],
      smooth: true,
      areaStyle: {}
    }
  ]
})

// 加载统计概览数据
const loadOverviewData = async () => {
  try {
    const res = await getOverviewStatistics()
    if (res.code === 200) {
      const data = res.data
      statsData.totalPersons = data.totalPersons
      statsData.newPersonsThisMonth = data.newPersonsThisMonth
      statsData.careCountThisMonth = data.careCountThisMonth
      statsData.coverageRate = data.coverageRate
    }
  } catch (error) {
    console.error('获取统计概览数据失败:', error)
  }
}

// 加载人员分类统计
const loadCategoryStatistics = async () => {
  try {
    const res = await getCategoryStatistics()
    if (res.code === 200 && res.data.seriesData) {
      categoryOption.value.series[0].data = res.data.seriesData
    }
  } catch (error) {
    console.error('获取人员分类统计失败:', error)
  }
}

// 加载关怀类型统计
const loadCareTypeStatistics = async () => {
  try {
    const res = await getCareTypeStatistics()
    if (res.code === 200) {
      if (res.data.xAxisData) {
        typeOption.value.xAxis[0].data = res.data.xAxisData
      }
      if (res.data.seriesData) {
        typeOption.value.series[0].data = res.data.seriesData
      }
    }
  } catch (error) {
    console.error('获取关怀类型统计失败:', error)
  }
}

// 加载月度统计
const loadMonthlyStatistics = async () => {
  try {
    const res = await getMonthlyStatistics()
    if (res.code === 200) {
      if (res.data.xAxisData) {
        monthOption.value.xAxis.data = res.data.xAxisData
      }
      if (res.data.seriesData) {
        monthOption.value.series[0].data = res.data.seriesData
      }
    }
  } catch (error) {
    console.error('获取月度统计失败:', error)
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadOverviewData()
  loadCategoryStatistics()
  loadCareTypeStatistics()
  loadMonthlyStatistics()
})
</script>

<style lang="less" scoped>
.statistics {
  .chart {
    height: 300px;
  }
}
</style> 