智慧农业管理系统
一个基于 Vue 3 + TypeScript + Element Plus 的现代化智慧农业管理系统。

功能特性
🌾 核心模块
库存管理 - 完整的仓储管理解决方案

库存查询与监控
仓库与库区管理
商品信息管理
往来单位管理
入库/出库管理
种植管理 - 农业生产管理

种植任务管理
种植批次管理
种质资源管理
地块信息管理
农资与机械管理
雇员管理
系统管理 - 用户权限与系统配置

用户管理
角色权限管理
字典管理
操作日志
登录日志
🚀 技术特性
现代化技术栈

Vue 3 组合式API
TypeScript 类型安全
Element Plus UI组件
Pinia 状态管理
Vue Router 路由管理
开发体验

Vite 快速构建
UnoCSS 原子化CSS
自动导入组件
热重载开发
TypeScript 支持
UI/UX 设计

响应式布局
现代化界面设计
暗色主题支持
丰富的交互反馈
移动端适配
技术栈
前端框架: Vue 3.4+
开发语言: TypeScript
构建工具: Vite 5.0+
UI 框架: Element Plus 2.4+
状态管理: Pinia 2.1+
路由管理: Vue Router 4.2+
CSS 框架: UnoCSS
图表库: ECharts + Vue-ECharts
HTTP 客户端: Axios
日期处理: Day.js
快速开始
环境要求
Node.js 16+
npm 8+ 或 yarn 1.22+ 或 pnpm 8+
安装依赖
# 使用 npm
npm install

# 使用 yarn
yarn install

# 使用 pnpm
pnpm install
开发运行
# 启动开发服务器
npm run dev

# 或使用其他包管理器
yarn dev
pnpm dev
访问 http://localhost:3000 查看应用。

构建部署
# 构建生产版本
npm run build

# 预览构建结果
npm run preview
项目结构
src/
├── api/                 # API接口定义
├── assets/             # 静态资源
├── components/         # 全局组件
├── layout/             # 布局组件
├── router/             # 路由配置
├── store/              # 状态管理
├── types/              # 类型定义
├── utils/              # 工具函数
├── views/              # 页面组件
│   ├── dashboard/      # 首页仪表板
│   ├── inventory/      # 库存管理
│   ├── plant/          # 种植管理
│   ├── system/         # 系统管理
│   └── login/          # 登录页面
├── App.vue             # 根组件
└── main.ts             # 入口文件
开发规范
代码规范
使用 Vue 3 组合式 API (<script setup>)
严格的 TypeScript 类型检查
ESLint + Prettier 代码格式化
组件使用 PascalCase 命名
文件夹使用 kebab-case 命名
样式规范
优先使用 UnoCSS 原子类
全局样式组合定义在 uno.config.ts
组件样式使用 scoped
响应式设计优先
提交规范
使用语义化提交信息：

feat: 新功能
fix: 修复问题
docs: 文档更新
style: 代码格式
refactor: 重构代码
test: 测试相关
chore: 构建过程或辅助工具的变动
API 集成
系统基于 RESTful API 设计，支持以下模块的完整 CRUD 操作：

库存管理相关接口
种植管理相关接口
系统管理相关接口
用户认证相关接口
API 基础地址配置在 .env 文件中：

VITE_API_BASE_URL=http://localhost:8080/api
浏览器支持
Chrome >= 87
Firefox >= 78
Safari >= 14
Edge >= 88
许可证
MIT License

贡献
欢迎提交 Issue 和 Pull Request！

更新日志
v1.0.0 (2024-01-01)
✨ 初始版本发布
✨ 完整的库存管理模块
✨ 种植管理模块
✨ 系统管理模块
✨ 用户认证与权限管理
✨ 响应式界面设计
✨ 现代化开发工具链
