# Springboot starter

[TOC]

## starter组成

* Auto-Configure Module
* Starter Module

### Auto-Configure Module

* 自动配置模块是包含自动配置类的 Maven 或 Gradle 模块。**可以构建可以自动贡献于应用程序上下文的模块，以及添加某个特性或提供对某个外部库的访问**。

### Starter Module

* 提供 "启动" 某个特性所需的所有依赖项。
* 可以包含一个或多个 Auto-Configure Module (自动配置模块)的依赖项，以及可能需要的任何其他依赖项。
* 在Spring 启动应用程序中，我们只需要添加这个 starter 依赖就可以使用其特性。

## starter命名

* Spring 官方定义的 starter 通常命名遵循的格式为 `spring-boot-starter-{name}`，例如 `spring-boot-starter-web`。
* 非官方 starter 命名应遵循 `{name}-spring-boot-starter `的格式，例如，`dubbo-spring-boot-starter`。

