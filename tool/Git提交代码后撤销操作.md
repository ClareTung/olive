# Git提交代码后撤销操作

* 撤销本地commit
    * HEAD 代表：上一次提交，这样刚刚提交的就又回到本地的local changes 列表中。 

```
git reset HEAD~
```

* 修改`git commit `信息

```
git commit --amend -m "新的提交信息"
```

* 本来打算Commit了五个文件，但是只提交了四个。不想生成一次Commit，可以使用下面的命令

```
git add 忘记提交的文件
git commit --amend --no-edit
```

* 对提交者信息有要求

```
git commit --amend --author "ClareTung"
```

* 撤回本次提交

```
git reset --soft 目标提交commitid
```

* 代码已经推送到远程服务器了，撤回某个文件

```
# 查看文件历史版本
git log <filename>
# 回滚到指定commitId
git checkout <commitId> <filename>
# 提交被修改的文件
git commit -m "回滚指定的文件"
# 推送
git push
```

