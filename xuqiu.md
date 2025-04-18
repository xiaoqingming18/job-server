# 劳务需求模块接口文档

## 1. 劳务需求管理接口

### 1.1 创建劳务需求
- **接口地址**: `/labor-demand/create`
- **请求方法**: POST
- **接口描述**: 创建新的劳务需求
- **请求参数**:
  ```json
  {
    "projectId": 1,
    "jobTypeId": 1,
    "headcount": 10,
    "dailyWage": 300.00,
    "startDate": "2023-06-01",
    "endDate": "2023-08-30",
    "workHours": "8:00-18:00",
    "requirements": "需要有高空作业证书",
    "accommodation": true,
    "meals": true
  }
  ```
- **响应格式**:
  ```json
  {
    "code": 0,
    "data": {
      "id": 1,
      "projectId": 1,
      "projectName": "某建筑项目",
      "jobTypeId": 1,
      "jobTypeName": "木工",
      "headcount": 10,
      "dailyWage": 300.00,
      "startDate": "2023-06-01",
      "endDate": "2023-08-30",
      "workHours": "8:00-18:00",
      "requirements": "需要有高空作业证书",
      "accommodation": true,
      "meals": true,
      "status": "open",
      "createTime": "2023-05-15T12:30:45",
      "updateTime": null
    },
    "message": "success"
  }
  ```

### 1.2 获取劳务需求详情
- **接口地址**: `/labor-demand/{id}`
- **请求方法**: GET
- **接口描述**: 获取指定ID的劳务需求详情
- **请求参数**: 
  - id: 路径参数，劳务需求ID
- **响应格式**: 与创建接口响应格式相同

### 1.3 更新劳务需求
- **接口地址**: `/labor-demand/update`
- **请求方法**: PUT
- **接口描述**: 更新既有劳务需求
- **请求参数**:
  ```json
  {
    "id": 1,
    "headcount": 15,
    "dailyWage": 320.00,
    "startDate": "2023-06-10",
    "endDate": "2023-09-10",
    "workHours": "8:00-18:00",
    "requirements": "需要有高空作业证书和相关工作经验",
    "accommodation": true,
    "meals": true
  }
  ```
- **响应格式**: 与创建接口响应格式相同

### 1.4 删除劳务需求
- **接口地址**: `/labor-demand/delete/{id}`
- **请求方法**: DELETE
- **接口描述**: 删除指定ID的劳务需求
- **请求参数**: 
  - id: 路径参数，劳务需求ID
- **响应格式**:
  ```json
  {
    "code": 0,
    "data": true,
    "message": "success"
  }
  ```

### 1.5 更改劳务需求状态
- **接口地址**: `/labor-demand/status`
- **请求方法**: PUT
- **接口描述**: 修改需求状态（开放/已满/已取消/已过期）
- **请求参数**:
  ```json
  {
    "id": 1,
    "status": "filled"
  }
  ```
- **响应格式**: 与创建接口响应格式相同

## 2. 劳务需求查询接口

### 2.1 分页查询劳务需求
- **接口地址**: `/labor-demand/list`
- **请求方法**: POST
- **接口描述**: 按条件分页查询劳务需求
- **请求参数**:
  ```json
  {
    "page": 1,
    "size": 10,
    "projectId": 1,
    "status": "open",
    "jobTypeId": 2
  }
  ```
- **响应格式**:
  ```json
  {
    "code": 0,
    "data": {
      "records": [
        {
          "id": 1,
          "projectId": 1,
          "projectName": "某建筑项目",
          "jobTypeId": 2,
          "jobTypeName": "电工",
          "headcount": 5,
          "dailyWage": 350.00,
          "startDate": "2023-06-01",
          "endDate": "2023-08-30",
          "workHours": "8:00-18:00",
          "requirements": "需要持有电工证",
          "accommodation": true,
          "meals": true,
          "status": "open",
          "createTime": "2023-05-15T12:30:45",
          "updateTime": null
        }
      ],
      "total": 1,
      "size": 10,
      "current": 1,
      "orders": [],
      "searchCount": true,
      "pages": 1
    },
    "message": "success"
  }
  ```

### 2.2 查询项目下的所有劳务需求
- **接口地址**: `/labor-demand/by-project/{projectId}`
- **请求方法**: GET
- **接口描述**: 获取特定项目下的所有劳务需求
- **请求参数**:
  - projectId: 路径参数，项目ID
- **响应格式**:
  ```json
  {
    "code": 0,
    "data": [
      {
        "id": 1,
        "projectId": 1,
        "projectName": "某建筑项目",
        "jobTypeId": 1,
        "jobTypeName": "木工",
        "headcount": 10,
        "dailyWage": 300.00,
        "startDate": "2023-06-01",
        "endDate": "2023-08-30",
        "workHours": "8:00-18:00",
        "requirements": "需要有高空作业证书",
        "accommodation": true,
        "meals": true,
        "status": "open",
        "createTime": "2023-05-15T12:30:45",
        "updateTime": null
      }
    ],
    "message": "success"
  }
  ```

### 2.3 搜索劳务需求
- **接口地址**: `/labor-demand/search`
- **请求方法**: GET
- **接口描述**: 多条件搜索劳务需求
- **请求参数**:
  - keyword: 关键词
  - province: 省份
  - city: 城市
  - district: 区县
  - minDailyWage: 最低日薪
  - maxDailyWage: 最高日薪
  - startDateFrom: 开始日期下限
  - startDateTo: 开始日期上限
  - page: 页码，默认1
  - size: 每页大小，默认10
- **响应格式**: 与分页查询响应格式相同

### 2.4 获取热门/推荐劳务需求
- **接口地址**: `/labor-demand/recommended`
- **请求方法**: GET
- **接口描述**: 获取系统推荐的劳务需求
- **请求参数**:
  - limit: 返回数量，默认10
- **响应格式**: 与项目下劳务需求响应格式相同

## 3. 需求统计分析接口

### 3.1 获取项目需求统计
- **接口地址**: `/labor-demand/stats/project/{projectId}`
- **请求方法**: GET
- **接口描述**: 获取特定项目的劳务需求统计信息
- **请求参数**:
  - projectId: 路径参数，项目ID
- **响应格式**:
  ```json
  {
    "code": 0,
    "data": {
      "totalDemandCount": 5,
      "totalHeadcount": 35,
      "openDemandCount": 3,
      "filledDemandCount": 2,
      "totalCost": 58500.00,
      "occupationDistribution": [
        {
          "occupationId": 1,
          "occupationName": "木工",
          "count": 10,
          "totalWage": 18000.00
        },
        {
          "occupationId": 2,
          "occupationName": "电工",
          "count": 5,
          "totalWage": 10500.00
        }
      ]
    },
    "message": "success"
  }
  ```

### 3.2 获取公司需求统计
- **接口地址**: `/labor-demand/stats/company/{companyId}`
- **请求方法**: GET
- **接口描述**: 获取特定公司所有项目的劳务需求统计
- **请求参数**:
  - companyId: 路径参数，公司ID
- **响应格式**:
  ```json
  {
    "code": 0,
    "data": {
      "totalProjects": 3,
      "totalDemands": 15,
      "totalHeadcount": 120,
      "totalCost": 258000.00,
      "projectDistribution": [
        {
          "projectId": 1,
          "projectName": "某建筑项目A",
          "demandCount": 8,
          "headcount": 60,
          "cost": 135000.00
        },
        {
          "projectId": 2,
          "projectName": "某建筑项目B",
          "demandCount": 7,
          "headcount": 60,
          "cost": 123000.00
        }
      ]
    },
    "message": "success"
  }
  ```

### 3.3 工种需求热度分析
- **接口地址**: `/labor-demand/stats/occupation-heat`
- **请求方法**: GET
- **接口描述**: 分析各工种需求热度
- **请求参数**:
  - period: 统计周期（天），默认30
- **响应格式**:
  ```json
  {
    "code": 0,
    "data": [
      {
        "occupationId": 1,
        "occupationName": "木工",
        "demandCount": 28,
        "averageWage": 320.50,
        "totalHeadcount": 156
      },
      {
        "occupationId": 2,
        "occupationName": "电工",
        "demandCount": 25,
        "averageWage": 350.80,
        "totalHeadcount": 125
      }
    ],
    "message": "success"
  }
  ```

## 4. 需求与工种关联接口

### 4.1 根据工种查询需求
- **接口地址**: `/labor-demand/by-occupation/{occupationId}`
- **请求方法**: GET
- **接口描述**: 获取指定工种的所有劳务需求
- **请求参数**:
  - occupationId: 路径参数，工种ID
  - status: 需求状态，可选
  - page: 页码，默认1
  - size: 每页大小，默认10
- **响应格式**: 与分页查询响应格式相同

### 4.2 获取匹配特定需求的工种建议
- **接口地址**: `/labor-demand/suggest-occupations`
- **请求方法**: POST
- **接口描述**: 根据项目特征和需求描述推荐合适的工种
- **请求参数**:
  ```json
  {
    "projectType": "residence",
    "projectScale": "medium",
    "demandDescription": "需要进行高空外墙作业，包括外墙涂料施工和排水管安装",
    "budget": 20000.00
  }
  ```
- **响应格式**:
  ```json
  {
    "code": 0,
    "data": [
      {
        "occupationId": 5,
        "occupationName": "外墙施工工",
        "matchScore": 0.95,
        "averageWage": 380.00,
        "description": "负责建筑外墙的装饰和涂料施工",
        "requiredCertificates": ["高空作业证"]
      },
      {
        "occupationId": 8,
        "occupationName": "管道安装工",
        "matchScore": 0.85,
        "averageWage": 350.00,
        "description": "负责建筑内外的管道安装和维护",
        "requiredCertificates": ["管道工证书"]
      }
    ],
    "message": "success"
  }
  ```

## 5. 地域相关劳务需求接口

### 5.1 按地区查询劳务需求
- **接口地址**: `/labor-demand/by-location`
- **请求方法**: GET
- **接口描述**: 获取指定地区的劳务需求
- **请求参数**:
  - province: 省份，可选
  - city: 城市，可选
  - district: 区县，可选
  - page: 页码，默认1
  - size: 每页大小，默认10
- **响应格式**: 与分页查询响应格式相同

### 5.2 获取热门需求地区
- **接口地址**: `/labor-demand/hot-locations`
- **请求方法**: GET
- **接口描述**: 获取劳务需求最多的地区信息
- **请求参数**:
  - limit: 返回数量，默认10
- **响应格式**:
  ```json
  {
    "code": 0,
    "data": [
      {
        "province": "浙江",
        "city": "杭州",
        "demandCount": 86,
        "projectCount": 12,
        "averageWage": 345.50
      },
      {
        "province": "江苏",
        "city": "南京",
        "demandCount": 72,
        "projectCount": 10,
        "averageWage": 330.80
      }
    ],
    "message": "success"
  }
  ```
