# Git指南

## 基本概念

* Git是一个分布式代码管理工具。

### 文件状态

* 已修改：Git将修改的文件加入到modified区域。
* 已暂存：通过add命令将工作目录中修改的文件提交到暂存区。
* 已提交：将暂存区文件commit到Git目录中永久保存。

### commit节点

* 每次提交都会生成一个节点，每个节点都会有一个哈希值作为唯一标识。

### HEAD

* HEAD可以称它为指针或者引用，它可以指向任意一个节点，指向的节点始终为当前工作目录。
* 当前工作目录就是HEAD指向的节点。

### 远程仓库

* 远程仓库作为中介，可以进行协同开发。

## 分支

* 当一个分支指向一个节点时，当前节点的内容即是该分支的内容。

## 命令详解

### 提交相关

* 添加某个文件到暂存区

```text
git add 文件路径
```

* 添加所有文件到暂存区

```text
git add .
```

* Git提供了撤销工作去和暂存区命令
* 撤销工作区改动

```text
git checkout -- 文件名
```

* 清空暂存区

```text
git reset HEAD 文件名
```

* 提交

```text
git commit -m "该节点的描述信息"
```

### 分支相关

* 创建分支
  * 创建一个分支后该分支会与HEAD指向同一节点

```text
git branch 分支名
```

* 切换分支

```text
git checkout 分支名
```

* 创建一个分支后立即切换

```text
git checkout -b 分支名
```

* 删除分支

```text
git branch -d 分支名
```

### 合并相关

* merge：可以将某个分支或者某个节点的代码合并至当前分支。

```text
git merge 分支名/节点哈希值
```

* rebase：是一种合并指令。rebase合并看起来不会产生新的节点，而是将需要合并的节点直接累加。
* 会使提交历史看起来跟更加线程、干净。

```text
git rebase 分支名/节点哈希值
```

* cherry-pick：不同于merge和rebase，它可以选择某几个节点进行合并。

```text
git cherry-pick 节点哈希值
```

### 回退相关

* 分离HEAD：在默认情况下 HEAD 是指向分支的，但也可以将 HEAD 从分支上取下来直接指向某个节点，此过程就是分离 HEAD。

```text
git checkout  节点哈希值
```

* Git 也提供了 HEAD 基于某一特殊位置(分支/HEAD)直接指向前一个或前 N 个节点的命令。

```text
//HEAD分离并指向前一个节点
git checkout 分支名/HEAD^
//HEAD分离并指向前N个节点
git checkout 分支名～N
```

* 将 HEAD 分离出来指向节点有什么用呢？举个例子：如果开发过程发现之前的提交有问题，此时可以将 HEAD 指向对应的节点，修改完毕后再提交，此时你肯定不希望再生成一个新的节点，而你只需在提交时加上--amend 即可。

```text
git commit --amend
```

* 回退
  * 将代码回退到前一个提交

```text
// 回退N个提交
git reset HEAD~N
```

### 远程相关

* 从远程仓库复制一份代码到本地

```text
git clone 仓库地址
```

* fetch 命令就是一次下载操作，它会将远程新增加的节点以及引用(分支/HEAD)的状态下载到本地

```text
git fetch 远程仓库地址/分支名
```

* pull命令可以从远程仓库的某个引用拉取代码

```text
git pull 远程分支名
```

* push 命令可以将本地提交推送至远程

```text
git push 远程分支名
```



