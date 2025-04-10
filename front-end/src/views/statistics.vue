<template>
  <div class="statistics">
    <a-row :gutter="16">
      <a-col :span="6">
        <a-card>
          <statistic
            title="总关怀人数"
            :value="112"
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
            :value="11"
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
            :value="56"
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
            :value="95.6"
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
import { ref } from 'vue'
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

const categoryOption = {
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
      data: [
        { value: 45, name: '老年人' },
        { value: 35, name: '残疾人' },
        { value: 32, name: '低保户' }
      ]
    }
  ]
}

const typeOption = {
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
      data: ['走访', '电话慰问', '物资帮助', '医疗救助'],
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
      data: [25, 15, 10, 6]
    }
  ]
}

const monthOption = {
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
    data: ['2023-05', '2023-06', '2023-07', '2023-08', '2023-09', '2023-10', '2023-11', '2023-12', '2024-01', '2024-02', '2024-03', '2024-04']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '关怀次数',
      type: 'line',
      stack: 'Total',
      data: [45, 52, 48, 50, 55, 58, 62, 65, 50, 42, 48, 56],
      smooth: true,
      areaStyle: {}
    }
  ]
}
</script>

<style lang="less" scoped>
.statistics {
  .chart {
    height: 300px;
  }
}
</style> 