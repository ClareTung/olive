## Redis-保护Redis

### 指令安全

```
rename-command keys abckeysabc # 使用abckeysabc替换keys
rename-command flushall "" # 完全禁用flushall
```

### 端口安全

* Redis默认会监听6379，必须在配置文件中指定监听的IP地址，同时增加密码访问限制

### Lua脚本安全

* 开发者必须禁止 Lua 脚本由用户输入的内容 (UGC) 生成，这可能会被黑客利用以植入 恶意的攻击代码来得到 Redis 的主机权限。 
* 同时，我们应该让 Redis 以普通用户的身份启动，这样即使存在恶意代码黑客也无法拿 到 root 权限。

### SSL代理

* Redis 并不支持 SSL 链接，意味着客户端和服务器之间交互的数据不应该直接暴露在公 网上传输，否则会有被窃听的风险。如果必须要用在公网上，可以考虑使用 SSL 代理。
* SSL 代理比较常见的有 ssh，不过 Redis 官方推荐使用 spiped 工具。

