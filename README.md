# 养鸽器

## [X岛](https://www.nmbxd.com)专属应用

### 使用java编写，功能仅完成主体功能，有概率闪退

### 请手动关闭该应用的电量优化，打开该应用的后台访问网络权限

### 高版本Android需要手动开启通知权限

## 已知BUG：

 - 闪退
 
 - 莫名其妙的新消息数量

## 使用说明：

 - 主体功能：跟踪串回复数，更新时通知

 - 点击通知会跳转到应用界面并将相应通知设为已读

## 会实现的功能：

 - 1.应用饼干以访问部分板块

 - 2.点击通知栏的通知跳转到岛客户端（现为将相应串的通知设为已读)

 - 3.更美观的UI

 - 4.添加可选功能「提交到服务器进行统一请求」，减少因重复请求同一串而给岛服务器带来的不必要的负载。此功能的核心在于客户端将追更列表提交给中转服务器，服务端整合所有客户端提交的列表并进行去重，确保相同的串不会被多次请求。理论上可以有效降低重复请求的数量，减轻岛服务器的压力，同时提升整体的响应速度
 
 - 5.编辑列表时添加上下移动排列功能

## 更新：

 - 2024.9.17 10:24：完善功能，优化设计
 
 - 2024.9.22 20:56：完善功能，优化设计
 
   - 修复了编辑列表界面的UI问题
   
   - 新增导入导出功能
   
   - 编辑列表完成后，有改动的串的第一次更新将不会统计新消息数量（例如避免了切换到只看Po后提示有负数条消息未读）
   
   - 其他优化
