# 劳务需求模块接口文档

## 基础信息

- 基础路径: `/labor-demand`
- 数据模型:
  - 新增字段: `title` (需求标题)，非空字段，位于id字段之后
  - 状态枚举: `open`(开放中), `filled`(已满), `cancelled`(已取消), `expired`(已过期)

## 接口列表

### 1. 添加劳务需求

- **URL**: `/labor-demand/add`
- **Method**: POST
- **描述**: 创建一个新的劳务需求
- **权限**: 需要登录
- **请求体**:
```json
{
  "title": "招募木工4名",
  "projectId": 1,
  "jobTypeId": 2,
  "headcount": 4,
  "dailyWage": 300.00,
  "startDate": "2023-07-10",
  "endDate": "2023-08-10",
  "workHours": "8:00-17:00",
  "requirements": "2年以上工作经验",
  "accommodation": true,
  "meals": true
}
```
- **响应**:
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "title": "招募木工4名",
    "projectId": 1,
    "projectName": "XX建筑工程",
    "projectAddress": "北京市海淀区",
    "jobTypeId": 2,
    "jobTypeName": "木工",
    "jobTypeCategory": "建筑工",
    "headcount": 4,
    "dailyWage": 300.00,
    "startDate": "2023-07-10",
    "endDate": "2023-08-10",
    "workHours": "8:00-17:00",
    "requirements": "2年以上工作经验",
    "accommodation": true,
    "meals": true,
    "status": "open",
    "createTime": "2023-06-15 10:00:00",
    "updateTime": "2023-06-15 10:00:00",
    "companyName": "XX建筑公司"
  }
}
```

### 2. 创建劳务需求（同添加）

- **URL**: `/labor-demand/create`
- **Method**: POST
- **描述**: 创建一个新的劳务需求（与add接口功能相同）
- **参数和响应**: 同添加接口

### 3. 获取劳务需求详情

- **URL**: `/labor-demand/info/{id}`
- **Method**: GET
- **描述**: 根据ID获取劳务需求详情
- **参数**: 
  - id: 路径参数，劳务需求ID
- **响应**: 同添加接口的响应

### 4. 获取劳务需求详情(别名)

- **URL**: `/labor-demand/{id}`
- **Method**: GET
- **描述**: 根据ID获取劳务需求详情（与info接口功能相同）
- **参数和响应**: 同上

### 5. 更新劳务需求信息

- **URL**: `/labor-demand/update`
- **Method**: PUT
- **描述**: 更新劳务需求信息
- **权限**: 需要登录且有权限
- **请求体**:
```json
{
  "id": 1,
  "title": "急招木工4名",
  "projectId": 1,
  "jobTypeId": 2,
  "headcount": 4,
  "dailyWage": 320.00,
  "startDate": "2023-07-12",
  "endDate": "2023-08-12",
  "workHours": "8:00-17:00",
  "requirements": "3年以上工作经验",
  "accommodation": true,
  "meals": true
}
```
- **响应**: 同添加接口的响应

### 6. 更新劳务需求状态

- **URL**: `/labor-demand/status`
- **Method**: PUT
- **描述**: 更新劳务需求状态
- **权限**: 需要登录且有权限
- **请求体**:
```json
{
  "id": 1,
  "status": "filled"
}
```
- **响应**: 同添加接口的响应

### 7. 删除劳务需求

- **URL**: `/labor-demand/delete/{id}`
- **Method**: DELETE
- **描述**: 删除劳务需求
- **权限**: 需要登录且有权限
- **参数**: 
  - id: 路径参数，劳务需求ID
- **响应**:
```json
{
  "code": 0,
  "message": "success",
  "data": true
}
```

### 8. 分页查询劳务需求

- **URL**: `/labor-demand/list`
- **Method**: POST
- **描述**: 根据多条件分页查询劳务需求
- **请求体**:
```json
{
  "pageNum": 1,
  "pageSize": 10,
  "companyId": 2,
  "projectId": 1,
  "jobTypeId": 2,
  "minDailyWage": 100,
  "maxDailyWage": 500,
  "startDate": "2023-07-01",
  "endDate": "2023-08-31",
  "accommodation": true,
  "meals": null,
  "status": "open"
}
```
- **响应**:
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "急招木工4名",
        "projectId": 1,
        "projectName": "XX建筑工程",
        "projectAddress": "北京市海淀区",
        "jobTypeId": 2,
        "jobTypeName": "木工",
        "jobTypeCategory": "建筑工",
        "headcount": 4,
        "dailyWage": 320.00,
        "startDate": "2023-07-12",
        "endDate": "2023-08-12",
        "workHours": "8:00-17:00",
        "requirements": "3年以上工作经验",
        "accommodation": true,
        "meals": true,
        "status": "open",
        "createTime": "2023-06-15 10:00:00",
        "updateTime": "2023-06-16 10:00:00",
        "companyName": "XX建筑公司"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 9. 获取项目的所有劳务需求

- **URL**: `/labor-demand/project/{projectId}`
- **Method**: GET
- **描述**: 获取特定项目的所有劳务需求
- **参数**: 
  - projectId: 路径参数，项目ID
- **响应**:
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "急招木工4名",
      "projectId": 1,
      "projectName": "XX建筑工程",
      "projectAddress": "北京市海淀区",
      "jobTypeId": 2,
      "jobTypeName": "木工",
      "jobTypeCategory": "建筑工",
      "headcount": 4,
      "dailyWage": 320.00,
      "startDate": "2023-07-12",
      "endDate": "2023-08-12",
      "workHours": "8:00-17:00",
      "requirements": "3年以上工作经验",
      "accommodation": true,
      "meals": true,
      "status": "open",
      "createTime": "2023-06-15 10:00:00",
      "updateTime": "2023-06-16 10:00:00",
      "companyName": "XX建筑公司"
    }
  ]
}
```

### 10. 获取项目的所有劳务需求(别名)

- **URL**: `/labor-demand/by-project/{projectId}`
- **Method**: GET
- **描述**: 获取特定项目的所有劳务需求（与project接口功能相同）
- **参数和响应**: 同上

### 11. 检查操作权限

- **URL**: `/labor-demand/check-permission/{id}`
- **Method**: GET
- **描述**: 检查当前用户是否有权限操作指定的劳务需求
- **权限**: 需要登录
- **参数**: 
  - id: 路径参数，劳务需求ID
- **响应**:
```json
{
  "code": 0,
  "message": "success",
  "data": true
}
```

### 12. 搜索劳务需求

- **URL**: `/labor-demand/search`
- **Method**: GET
- **描述**: 根据关键词、地区等条件搜索劳务需求
- **参数**: 
  - keyword: 关键词（可选），用于搜索标题、项目名称、工种名称等
  - province: 省份（可选）
  - city: 城市（可选）
  - district: 区县（可选）
  - minDailyWage: 最低日薪（可选）
  - maxDailyWage: 最高日薪（可选）
  - startDateFrom: 开始日期下限（可选）
  - startDateTo: 开始日期上限（可选）
  - page: 页码，默认1
  - size: 每页大小，默认10
- **响应**: 同分页查询接口的响应

### 13. 获取推荐劳务需求

- **URL**: `/labor-demand/recommended`
- **Method**: GET
- **描述**: 获取推荐的劳务需求
- **参数**: 
  - limit: 返回数量限制，默认10
- **响应**:
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "急招木工4名",
      "projectId": 1,
      "projectName": "XX建筑工程",
      "projectAddress": "北京市海淀区",
      "jobTypeId": 2,
      "jobTypeName": "木工",
      "jobTypeCategory": "建筑工",
      "headcount": 4,
      "dailyWage": 320.00,
      "startDate": "2023-07-12",
      "endDate": "2023-08-12",
      "workHours": "8:00-17:00",
      "requirements": "3年以上工作经验",
      "accommodation": true,
      "meals": true,
      "status": "open",
      "createTime": "2023-06-15 10:00:00",
      "updateTime": "2023-06-16 10:00:00",
      "companyName": "XX建筑公司"
    }
  ]
}
```

### 14. 获取项目需求统计

- **URL**: `/labor-demand/stats/project/{projectId}`
- **Method**: GET
- **描述**: 获取项目的劳务需求统计信息
- **权限**: 需要登录
- **参数**: 
  - projectId: 路径参数，项目ID
- **响应**:
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "totalDemandCount": 3,
    "totalHeadcount": 12,
    "openDemandCount": 2,
    "filledDemandCount": 1,
    "totalCost": 36000.00,
    "occupationDistribution": [
      {
        "occupationId": 2,
        "occupationName": "木工",
        "count": 8,
        "totalWage": 25600.00
      },
      {
        "occupationId": 3,
        "occupationName": "电工",
        "count": 4,
        "totalWage": 10400.00
      }
    ]
  }
}
```

### 15. 获取公司需求统计

- **URL**: `/labor-demand/stats/company/{companyId}`
- **Method**: GET
- **描述**: 获取公司的劳务需求统计信息
- **权限**: 需要登录
- **参数**: 
  - companyId: 路径参数，公司ID
- **响应**:
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "totalProjects": 2,
    "totalDemands": 5,
    "totalHeadcount": 20,
    "totalCost": 60000.00,
    "projectDistribution": [
      {
        "projectId": 1,
        "projectName": "XX建筑工程",
        "demandCount": 3,
        "headcount": 12,
        "cost": 36000.00
      },
      {
        "projectId": 2,
        "projectName": "YY装修工程",
        "demandCount": 2,
        "headcount": 8,
        "cost": 24000.00
      }
    ]
  }
}
```

### 16. 分析工种需求热度

- **URL**: `/labor-demand/stats/occupation-heat`
- **Method**: GET
- **描述**: 分析工种需求热度
- **参数**: 
  - period: 统计周期（天），默认30
- **响应**:
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "occupationId": 2,
      "occupationName": "木工",
      "demandCount": 15,
      "averageWage": 320.00,
      "totalHeadcount": 60
    },
    {
      "occupationId": 3,
      "occupationName": "电工",
      "demandCount": 10,
      "averageWage": 350.00,
      "totalHeadcount": 40
    }
  ]
}
```

### 17. 根据工种查询劳务需求

- **URL**: `/labor-demand/by-occupation/{occupationId}`
- **Method**: GET
- **描述**: 根据工种获取劳务需求
- **参数**: 
  - occupationId: 路径参数，工种ID
  - status: 需求状态（可选）
  - page: 页码，默认1
  - size: 每页大小，默认10
- **响应**: 同分页查询接口的响应

### 18. 推荐工种

- **URL**: `/labor-demand/suggest-occupations`
- **Method**: POST
- **描述**: 根据项目特征和需求描述推荐合适的工种
- **请求体**:
```json
{
  "projectScale": "中型",
  "buildingType": "住宅",
  "demandDescription": "需要能够进行木结构安装和门窗制作的工人"
}
```
- **响应**:
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "occupationId": 2,
      "occupationName": "木工",
      "matchScore": 0.9,
      "averageWage": 320.00,
      "description": "负责木结构制作、安装等工作",
      "requiredCertificates": "中级木工证"
    },
    {
      "occupationId": 5,
      "occupationName": "门窗工",
      "matchScore": 0.8,
      "averageWage": 300.00,
      "description": "负责门窗安装、调试等工作",
      "requiredCertificates": "门窗安装证"
    }
  ]
}
```

### 19. 根据地区获取劳务需求

- **URL**: `/labor-demand/by-location`
- **Method**: GET
- **描述**: 根据地区获取劳务需求
- **参数**: 
  - province: 省份（可选）
  - city: 城市（可选）
  - district: 区县（可选）
  - page: 页码，默认1
  - size: 每页大小，默认10
- **响应**: 同分页查询接口的响应

### 20. 获取热门地区

- **URL**: `/labor-demand/hot-locations`
- **Method**: GET
- **描述**: 获取劳务需求最多的热门地区
- **参数**: 
  - limit: 返回数量限制，默认10
- **响应**:
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "province": "北京市",
      "city": "北京市",
      "demandCount": 30,
      "projectCount": 8,
      "averageWage": 350.00
    },
    {
      "province": "浙江省",
      "city": "杭州市",
      "demandCount": 25,
      "projectCount": 6,
      "averageWage": 320.00
    }
  ]
}
```

### 21. 综合条件查询劳务需求

- **URL**: `/labor-demand/criteria-query`
- **Method**: GET
- **描述**: 根据项目类型、工种和地区综合查询劳务需求
- **参数**: 
  - projectType: 项目类型ID（可选）
  - occupationId: 工种ID（可选）
  - location: 地区，可以是省/市/区任一级（可选）
  - page: 页码，默认1
  - size: 每页大小，默认10
- **响应**: 同分页查询接口的响应

## 数据模型说明

### 劳务需求(LaborDemand)

```json
{
  "id": 1,                          // 需求ID
  "title": "急招木工4名",             // 需求标题（新增字段，非空）
  "projectId": 1,                   // 项目ID
  "jobTypeId": 2,                   // 工种ID
  "headcount": 4,                   // 需求人数
  "dailyWage": 320.00,              // 日薪
  "startDate": "2023-07-12",        // 开始日期
  "endDate": "2023-08-12",          // 结束日期
  "workHours": "8:00-17:00",        // 工作时间
  "requirements": "3年以上工作经验",  // 特殊要求
  "accommodation": true,            // 是否提供住宿
  "meals": true,                    // 是否提供餐食
  "status": "open",                 // 状态：open/filled/cancelled/expired
  "createTime": "2023-06-15",       // 创建时间
  "updateTime": "2023-06-16"        // 更新时间
}
```

### 劳务需求视图对象(LaborDemandVO)

包含上述所有字段，并附加以下信息:

```json
{
  // ... 基础字段
  "projectName": "XX建筑工程",       // 项目名称
  "projectAddress": "北京市海淀区",   // 项目地址
  "jobTypeName": "木工",            // 工种名称
  "jobTypeCategory": "建筑工",      // 工种类别
  "companyName": "XX建筑公司"        // 企业名称
}
```
