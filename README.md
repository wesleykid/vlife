

# V-LIFE是为全面提升研发效能而生的平台级全栈解决方案。

- [使用指南] [http://vlife.wwwlike.cn](http://vlife.wwwlike.cn)
- [示例骨架] [https://oa.wwwlike.cn](https://oa.wwwlike.cn)
- [入门视频] [5分钟快速体验入门](https://www.bilibili.com/video/BV1sT411c71v/?vd_source=4c025d49e1ac4adb74b6dd2a39ce185e&t=119.6)

## 特点
1. 前后端全栈全量开源的低代码平台，能满足于功能个性化与本地化部署的实际场景需求。
2. 面向模型开发，写模型就能完成一般场景的业务功能。应用健壮、稳定性有保证。

## 主要技术栈
- springboot
- jpa/queryDsl
- react18/ts4/
- semi(字节跳动ui框架)
- hooks/ahooks
- formily(阿里表单组件)

## 服务端能力
1. 数据逻辑处理：对各种有关联数据逻辑的保存、查询、删除封装了开箱即用方法（非单表CRUD），其关联操作产生的SQL复杂程度超出想象。
2. 模型编程开发：编写模型(DO\VO\DTO\REQ)就能完成项目开发成为现实，研发行云流水。践行低代码。
3. 智能代码生成：依据模型可全量生成服务端代码
4. 模型校验分析：分析您提交的模型信息，校验模型信息，让不规范的模型无生存空间

## 客户端能力
1. 开放了管理类应用系统的权限骨架系统vlife-admin
2. 在semi,formily原子组件基础之上封装了常用的模块级的列表、搜索、表单等组件
3. 提供了模板组件，不用写前端代码即可实现前端功能页面的渲染

##  queryDSL介绍
![](http://vlife.wwwlike.cn/static/img/relation.png)
* V-LIFE在JPA与QueryDsl基础上封装了能够处理复杂数据逻辑场景的CRUD接口。重复繁琐数据逻辑操作交给vlife完成，可显著降低研发难度。

## 模块介绍 (最新版本1.0.4)
- vlife-base vlife平台的公用基础类库
- vlife-core 数据逻辑处理的核心类库
- vlife-plugin maven插件，可自动生成和注释提取
- vlife-spring-boot-starter vlife核心数据逻辑处理封装成starter,项目引入它即可
- vlife-boot-starter-web    vlife的web数据出参封装、异常处理、权限控制封装的starter,需要使用这些附加能力的可以引入

## 应用示例
- vlife-admin 基于vlife核心数据处理与web权限控制能力打造的权限管理骨架的后端应用
- vlife-admin-react vlife整个系统的reacts+ts4实现的前端应用，包含了大量封装的通用业务级组件和权限管理的模块

## 联系帮助
- QQ讨论群：786134846
  ![](http://vlife.wwwlike.cn/static/img/qq_qun_786134846.png)
