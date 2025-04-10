# 社区关怀服务平台 (Community Care Service Platform)

社区关怀服务平台是一个专为社区工作者设计的管理系统，旨在帮助社区工作者更好地管理和关怀社区内的特殊群体，如老年人、残疾人和低保户等。该平台提供了人员管理、分类管理、关怀记录和统计分析等功能，方便社区工作者高效地开展工作，提升社区服务水平。

## 技术栈

### 前端
- **框架**: Vue 3 (使用 Composition API 和 `<script setup>` 语法)
- **构建工具**: Vite
- **UI组件库**: Ant Design Vue 4.x
- **状态管理**: Pinia
- **路由**: Vue Router 4.x
- **HTTP请求**: Axios
- **图表**: ECharts + Vue-ECharts
- **样式**: Less
- **工具库**: Dayjs (日期处理)

### 后端
- **框架**: Spring Boot 2.7.0
- **安全认证**: Spring Security + JWT
- **数据库**: MySQL 8.0
- **ORM**: MyBatis
- **API文档**: SpringDoc (可选)
- **日志**: SLF4J + Logback
- **构建工具**: Maven

## 功能特点

- **用户管理**：管理员可以创建、编辑、删除用户，以及重置用户密码
- **人员管理**：维护社区内特殊群体的基本信息
- **分类管理**：灵活管理人员分类，如老年人、残疾人、低保户等
- **关怀记录**：记录关怀活动，包括走访、电话慰问、物资帮助和医疗救助等
- **权限控制**：基于角色的权限管理，管理员和普通用户权限区分
- **统计分析**：统计人员分布、关怀类型和月度关怀数据等
- **个人信息管理**：用户可以修改个人信息和密码

## 系统角色

- **管理员(ADMIN)**：拥有所有功能的访问权限，包括用户管理、分类管理和统计分析
- **普通用户(USER)**：仅拥有人员管理和关怀记录功能的权限，且只能查看和编辑自己创建的记录

## 安装与部署

### 环境要求

- JDK 8+
- Node.js 16+
- MySQL 8.0
- Maven 3.6+
- npm / yarn / pnpm

### 后端部署

1. 克隆代码仓库
```
git clone https://github.com/yourusername/Community-Care-Service-Platform.git
cd Community-Care-Service-Platform
```

2. 配置数据库
```sql
CREATE DATABASE community_care DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. 修改配置文件（如需要）
编辑 `server/src/main/resources/application.yml` 文件，根据你的环境配置数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/community_care?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

4. 编译和运行后端
```
cd server
mvn clean package
java -jar target/server-1.0.0.jar
```
或者使用Maven插件运行：
```
mvn spring-boot:run
```

5. 后端将在 http://localhost:8080 上运行

### 前端部署

1. 进入前端目录
```
cd front-end
```

2. 安装依赖
```
npm install
# 或
yarn install
# 或
pnpm install
```

3. 开发环境运行
```
npm run dev
# 或
yarn dev
# 或
pnpm dev
```

4. 构建生产环境代码
```
npm run build
# 或
yarn build
# 或
pnpm build
```

5. 本地预览生产环境构建结果
```
npm run preview
# 或
yarn preview
# 或
pnpm preview
```

6. 前端开发服务器将在 http://localhost:3000 上运行

## 使用说明

### 初始账号
- 用户名: admin
- 密码: 123456
- 角色: 管理员(ADMIN)

登录后，您可以先创建分类，然后添加人员信息，记录关怀活动，并使用统计分析功能查看数据。

### 用户操作流程
1. 新用户登录系统后，可以先在"分类管理"中创建人员分类
2. 在"人员管理"中添加需要关怀的人员信息
3. 在"关怀记录"中记录对特定人员的关怀活动
4. 在"统计分析"中查看各类统计数据
5. 管理员可以在"用户管理"中创建和管理其他用户账号

## 项目目录结构

```
Community-Care-Service-Platform/
├── front-end/                  # 前端项目
│   ├── public/                 # 静态资源
│   │   ├── api/                # API接口
│   │   ├── assets/             # 资源文件
│   │   ├── components/         # 通用组件
│   │   ├── layouts/            # 布局组件
│   │   ├── router/             # 路由配置
│   │   ├── stores/             # Pinia状态管理
│   │   ├── styles/             # 全局样式
│   │   ├── utils/              # 工具函数
│   │   ├── views/              # 页面组件
│   │   ├── App.vue             # 根组件
│   │   └── main.js             # 入口文件
│   ├── index.html              # HTML模板
│   └── vite.config.js          # Vite配置
│
└── server/                     # 后端项目
    ├── src/                    # 源代码
    │   └── main/
    │       ├── java/com/community/server/
    │       │   ├── common/     # 通用类
    │       │   ├── config/     # 配置类
    │       │   ├── controller/ # 控制器
    │       │   ├── exception/  # 异常处理
    │       │   ├── mapper/     # MyBatis映射
    │       │   ├── model/      # 数据模型
    │       │   ├── repository/ # 数据访问
    │       │   ├── security/   # 安全配置
    │       │   ├── service/    # 业务逻辑
    │       │   └── ServerApplication.java  # 启动类
    │       └── resources/
    │           ├── mapper/     # MyBatis XML映射文件
    │           ├── application.yml  # 应用配置
    │           └── schema.sql  # 数据库初始化脚本
    └── pom.xml                 # Maven配置
```

## 许可证

MIT License

## 联系方式与贡献

如有问题或建议，请通过issue提交，或者联系项目维护者。欢迎提交Pull Request贡献代码。
