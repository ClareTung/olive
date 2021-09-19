## Macbook pro m1安装jdk、maven、git

### 安装jdk

* 下载：
  * https://www.oracle.com/java/technologies/downloads/#java8
* 找到Java8
  * [jdk-8u301-macosx-x64.dmg](https://www.oracle.com/java/technologies/downloads/#license-lightbox)

* 双击dmg，然后弹出pkg文件
* command+space打开搜索，找到终端

* 自动配置好了环境

```
clare@ClareTungdeMacBook-Pro ~ % java -version
java version "1.8.0_301"
Java(TM) SE Runtime Environment (build 1.8.0_301-b09)
Java HotSpot(TM) 64-Bit Server VM (build 25.301-b09, mixed mode)
```

* /Library/Java/JavaVirtualMachines/jdk1.8.0_301.jdk/Contents/Home

### 安装Maven

* 下载maven：https://maven.apache.org/download.cgi
  * [apache-maven-3.8.2-bin.tar.gz](https://mirror-hk.koddos.net/apache/maven/maven-3/3.8.2/binaries/apache-maven-3.8.2-bin.tar.gz) 

* 打开终端

```
clare@ClareTungdeMacBook-Pro ~ % vim ~/.bash_profile
```

```
export MAVEN_HOME=/Applications/apache-maven-3.8.2
export PATH=$PATH:$MAVEN_HOME/bin
```

```
clare@ClareTungdeMacBook-Pro ~ % source ~/.bash_profile
clare@ClareTungdeMacBook-Pro ~ % mvn -v
Apache Maven 3.8.2 (ea98e05a04480131370aa0c110b8c54cf726c06f)
Maven home: /Applications/apache-maven-3.8.2
Java version: 1.8.0_301, vendor: Oracle Corporation, runtime: /Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home
Default locale: zh_CN, platform encoding: UTF-8
OS name: "mac os x", version: "10.16", arch: "x86_64", family: "mac"
clare@ClareTungdeMacBook-Pro ~ % 
```

### 安装Git

```
clare@ClareTungdeMacBook-Pro ~ % brew install git
clare@ClareTungdeMacBook-Pro ~ % git --version 
git version 2.33.0
```

* 配置信息

```
git config --global user.name "ClareTung"
git config --global user.email "xxx@yeah.net"
```

* 生成秘钥

```
ssh-keygen -t rsa -C "xxx@yeah.net"
clare@ClareTungdeMacBook-Pro ~ % cd .ssh
vim id_rsa.pub
```

* 拷贝秘钥到github



### 遇到的问题

* 需要复制路径，在finder里，对选中的文件，cmd+opt+c，就复制到剪贴板了
* 报错：zsh: command not found: brew

```
/bin/zsh -c "$(curl -fsSL https://gitee.com/cunkai/HomebrewCN/raw/master/Homebrew.sh)"
```

```
 brew -v
Homebrew >=2.5.0 (shallow or no git repository)
xcode-select: note: no developer tools were found at '/Applications/Xcode.app', requesting install. Choose an option in the dialog to download the command line developer tools.
Homebrew/homebrew-core (no Git repository)
xcode-select: note: no developer tools were found at '/Applications/Xcode.app', requesting install. Choose an option in the dialog to download the command line developer tools.
Homebrew/homebrew-cask (no Git repository)
```

