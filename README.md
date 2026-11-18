# 今天 · 每日日程

一个适合 Java 初学者练习的本地全栈项目：记录日程和笔记、标记完成、查看今日进度，并在页面打开时接收浏览器提醒。

## 技术栈与版本

| 技术 | 本项目的版本或要求 | 用途 |
| --- | --- | --- |
| Java / JDK | 本机 JDK 17.0.21；`java.version` 为 17，但 Maven 编译插件目前仍显式设为 source/target 8，待在学习中统一 | 后端开发与运行 |
| Spring Boot | **3.5.7**，在 `pom.xml` 中指定 | 启动应用并提供 Web、数据库等集成 |
| Spring Framework / Spring MVC | 由 Spring Boot 3.5.7 管理 | Web 请求处理 |
| MyBatis Spring Boot Starter | **3.0.5** | 通过 SQL 操作数据库 |
| Maven | **3.9.11**（开发环境版本；项目没有 Maven Wrapper） | 构建项目、下载依赖 |
| MySQL | **8.4.13** | 日程数据库；H2 仅用于自动化测试和原数据保留 |
| 前端 | 原生 HTML、CSS、JavaScript；**未使用 Vue** | 页面和交互 |
| Node.js / npm | 本项目不需要 | 当前前端无需构建工具 |

Spring Boot 的版本和 Java 配置可在 `pom.xml` 中查看。注意：当前 `java.version=17` 与编译插件中的 `source/target=8` 不一致；会在 Maven 学习阶段说明并统一。可以运行 Maven 的 `dependency:tree` 查看实际解析的依赖版本。

Vue 可作为后续学习和重构方向，但目前没有 Vue 依赖，因此没有可填写的 Vue 版本。建议完成现有项目后再新建 Vue 3 前端，届时将实际使用的具体版本写入 README。

## 准备环境

需要 JDK 17、Maven 3.9 或兼容版本，以及可连接的 MySQL 8 数据库。首次构建需要联网下载 Maven 依赖。

### 配置数据库连接

先创建本地配置目录，再复制模板并填写本机数据库密码：

```powershell
New-Item -ItemType Directory -Force '.\本地开发' | Out-Null
Copy-Item '.\db-local.properties.example' '.\本地开发\db-local.properties'
```

`本地开发/` 已加入 Git 忽略规则，配置不会被提交。也可以通过 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD` 环境变量覆盖连接信息。

执行 `sql/init.sql` 创建数据库和表，并准备一个有该库读写权限的数据库用户。不要把真实密码写入 README 或提交到远端。

### 启动应用

在项目根目录打开终端：

```powershell
cd daily-checkin
mvn spring-boot:run
```

也可以在 IDEA 中运行 `DailyApplication`，并将工作目录设为项目根目录。启动前确认本机 MySQL 已运行，且 `本地开发/db-local.properties` 中的连接配置正确。

看到应用启动成功后，访问 <http://localhost:8080>。按 `Ctrl+C` 停止应用。数据库库名为 `daily_checkin`；实际数据库文件由本机 MySQL 管理，不包含在项目仓库中，请按自己的数据库环境备份。

Navicat 连接：主机 `127.0.0.1`，端口 `3306`，数据库 `daily_checkin`，用户使用你在本机配置的数据库账号。应用账号和管理账号的密码都只保存在本机凭据配置中，不放入仓库。建库建表脚本为 `sql/init.sql`，需要有建库权限的账号执行。

## 使用提醒

新建日程时设置日期和提醒时间，再点击页面右上角「开启提醒」并允许浏览器通知。开启后按钮变为「关闭提醒」，点击可暂停所有日程通知；开关状态保存在当前浏览器中。此开关不改变浏览器的网站通知权限，如需撤销权限请到浏览器的网站设置中操作。提醒只在页面保持打开、浏览器允许通知且该分钟内页面正在运行时生效；关闭页面、电脑休眠或错过时间不会补发。它不是后台推送，也不是每天自动重复的闹钟。

## 项目怎么运作

1. `src/main/resources/static/index.html` 定义页面结构；`style.css` 控制外观；`app.js` 处理按钮、调用接口和浏览器通知。
2. `EntryController.java` 接收浏览器的 HTTP 请求，`EntryService.java` 组织日程业务步骤，`EntryRepository.java` 是 MyBatis Mapper，使用 SQL 操作 MySQL。
3. `Entry.java` 描述一条日程和输入校验；MySQL 保存数据。
4. `application.properties` 保存数据库配置；`pom.xml` 声明项目依赖。

接口：`GET /api/entries` 查询；`POST /api/entries` 新建；`PUT /api/entries/{id}` 更新；`DELETE /api/entries/{id}` 删除。前端通过 `fetch` 发送 JSON，后端返回 JSON。

## 建议的学习顺序

你已有前端基础、Git 经验和 MySQL 学习经历，因此直接使用当前页面做接口联调。后端学习按概念顺序推进：Java 应用巩固 → Maven 与项目结构 → Spring IoC/Bean/依赖注入 → Spring Boot → HTTP/Tomcat/Servlet 必要概念与 Spring MVC → Controller/Service/Mapper 分层 → SQL 与 MyBatis → 测试和打包。

详细阶段目标、已学进度和下一步见 [后端学习计划表](后端学习计划表.md)。个人每日记录和主题笔记保存在本地 `本地开发/学习笔记/`，为保护个人学习资料不会推送到远端仓库。当前项目采用 MyBatis，不需要先学 JPA；Servlet 和 JDBC 只补充理解现有技术链路所需的概念。

这是单人本地学习项目，没有账号、权限控制或后台推送，不适合直接公开到互联网。
