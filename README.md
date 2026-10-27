# 体彩投注记录管理系统

一个基于Vue 3 + Spring Boot的体彩投注记录管理系统，支持投注记录的增删改查、统计分析、数据可视化等功能。

## 技术栈

### 前端
- Vue 3 + Composition API
- TypeScript
- Vite
- Element Plus
- Tailwind CSS
- Pinia (状态管理)
- Vue Router
- ECharts (图表)
- Axios (HTTP客户端)

### 后端
- Spring Boot 2.7.18
- MyBatis Plus
- MySQL 8.0
- JWT认证
- BCrypt密码加密
- Maven

## 功能特性

### 用户管理
- 用户注册/登录
- JWT Token认证
- 用户信息管理
- 密码修改

### 投注记录管理
- 投注记录的增删改查
- 批量操作支持
- 多条件筛选查询
- 分页显示

### 统计分析
- 总体统计数据（投注次数、金额、盈利等）
- 按日期统计
- 按联赛统计
- 按投注类型统计
- 胜率分析

### 数据可视化
- 盈利趋势图表
- 联赛分布饼图
- 投注类型统计图
- 日期统计柱状图

## 项目结构

```
sports_lottery_website_backend/
├── src/
│   ├── main/
│   │   ├── java/com/sports/lottery/
│   │   │   ├── common/         # 通用响应封装
│   │   │   ├── controller/     # 控制器
│   │   │   ├── service/        # 服务层
│   │   │   ├── mapper/         # 数据访问层
│   │   │   ├── entity/         # 实体类
│   │   │   ├── dto/            # 数据传输对象
│   │   │   ├── config/         # 配置类
│   │   │   └── util/           # 工具类
│   │   └── resources/
│   │       ├── mapper/         # MyBatis XML映射文件
│   │       ├── sql/            # 数据库脚本
│   │       └── application.yml # 配置文件
│   └── test/                   # 单元测试
├── docs/                       # 项目文档
│   └── 后端技术架构.md          # 后端技术架构说明
├── pom.xml
├── .gitignore
├── README.md
└── 项目完成总结.md
```

## 快速开始

### 环境要求
- Node.js 16+
- Java 8+
- MySQL 8.0+
- Maven 3.6+

### 数据库配置

1. 创建MySQL数据库：
```sql
CREATE DATABASE sports_lottery DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行初始化脚本：
```bash
mysql -u root -p sports_lottery < src/main/resources/sql/schema.sql
```

### 后端启动

1. 在项目根目录执行：

2. 修改数据库配置（如需要）：
编辑 `src/main/resources/application.yml` 文件中的数据库连接信息。

3. 编译并启动：
```bash
mvn clean compile
mvn spring-boot:run
```

后端服务将在 http://localhost:8080 启动

### 前端启动

1. 进入前端目录：
```bash
cd frontend
```

2. 安装依赖：
```bash
npm install
```

3. 启动开发服务器：
```bash
npm run dev
```

前端应用将在 http://localhost:5173 启动

## API接口

### 用户相关
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `GET /api/user/info` - 获取用户信息
- `PUT /api/user/info` - 更新用户信息
- `PUT /api/user/password` - 修改密码

### 投注记录相关
- `GET /api/betting/records` - 分页查询投注记录
- `POST /api/betting/records` - 添加投注记录
- `POST /api/betting/records/batch` - 批量添加投注记录
- `PUT /api/betting/records/{id}` - 更新投注记录
- `DELETE /api/betting/records/{id}` - 删除投注记录
- `DELETE /api/betting/records/batch` - 批量删除投注记录

### 统计相关
- `GET /api/betting/statistics` - 获取用户统计数据
- `GET /api/betting/statistics/daily` - 获取日期统计数据
- `GET /api/betting/statistics/league` - 获取联赛统计数据
- `GET /api/betting/statistics/bettype` - 获取投注类型统计数据

## 默认账户

系统初始化时会创建以下测试账户：

- 用户名：`admin`，密码：`123456`
- 用户名：`testuser`，密码：`123456`

## 开发说明

### 前端开发
- 使用Vue 3 Composition API编写组件
- 使用TypeScript提供类型安全
- 使用Element Plus组件库
- 使用Tailwind CSS进行样式设计
- 使用Pinia进行状态管理

### 后端开发
- 使用Spring Boot 2.7.18（兼容Java 8）
- 使用MyBatis Plus进行数据访问
- 使用JWT进行身份认证
- 使用BCrypt进行密码加密
- 遵循RESTful API设计规范

## 部署说明

### 前端部署
```bash
cd frontend
npm run build
```
将 `frontend/dist` 目录部署到Web服务器。

### 后端部署
```bash
mvn clean package
java -jar target/lottery-backend-1.0.0.jar
```

## 许可证

MIT License

## 贡献

欢迎提交Issue和Pull Request来改进这个项目。