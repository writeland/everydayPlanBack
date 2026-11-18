# 本机 MySQL 启动说明

本机 MySQL 没有注册为 Windows 服务。电脑重启后，需重新启动数据库。

在 PowerShell 中运行：

```powershell
cd D:\Java学习\daily-checkin
powershell -ExecutionPolicy Bypass -File .\本地开发\scripts\start-mysql.ps1
```

看到 `MySQL is ready on 127.0.0.1:3306.` 表示连接检查通过。随后可以使用 Navicat 连接，或在 IDEA 中启动项目。

也可以运行本机的 `本地开发\scripts\启动项目.ps1`，脚本会先启动 MySQL，再启动应用。

这两个脚本是本机专用文件，位于 Git 忽略的 `本地开发` 目录中，不随仓库分发。其他电脑请根据自己的 MySQL 安装方式启动数据库，再按照 README 配置并启动项目。
