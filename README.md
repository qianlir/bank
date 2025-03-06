# 银行交易管理系统

这是一个基于Java 21和Spring Boot构建的银行交易管理系统，提供交易记录、账户管理等功能。

## 功能特性

- 交易管理
  - 创建交易（转账、存款、取款）
  - 查看交易记录
  - 分页查询交易
- 账户管理
  - 查看账户信息
  - 账户余额查询
- 高性能
  - 内存存储，快速响应
  - 高效分页查询
- 健壮性
  - 完善的异常处理
  - 全面的单元测试
  - 压力测试支持

## 技术栈

- 后端
  - Java 21
  - Spring Boot 3.x
  - Spring Web MVC
  - Spring Validation
- 前端
  - React
  - Material-UI
- 构建工具
  - Maven
- 容器化
  - Docker
  - Kubernetes

## API文档

### 交易相关API

#### 创建交易
- POST /api/transactions
- 请求体：
```json
{
  "type": "TRANSFER|DEPOSIT|WITHDRAWAL",
  "amount": 100.0,
  "description": "转账备注",
  "fromAccountNumber": "1234567890",
  "toAccountNumber": "0987654321"
}
```

#### 获取交易列表
- GET /api/transactions
- 查询参数：
  - page: 页码（从0开始）
  - size: 每页条数

### 账户相关API

#### 获取账户列表
- GET /api/accounts

## 快速开始

### 本地运行

1. 克隆仓库
```bash
git clone https://github.com/your-repo/bank-transaction-system.git
```

2. 启动后端服务
```bash
cd bank-transaction-system
./mvnw spring-boot:run
```

3. 启动前端服务
```bash
cd frontend
npm install
npm start
```

4. 访问应用
打开浏览器访问 http://localhost:3000

### Docker运行

```bash
docker-compose up -d
```

## 测试

### 单元测试
```bash
./mvnw test
```

### 压力测试
使用JMeter等工具进行压力测试

## 贡献指南

欢迎提交Pull Request。请确保：
1. 代码风格一致
2. 包含必要的单元测试
3. 更新相关文档

## 许可证

MIT License
