* mvn install  会将项目生成的构件安装到本地Maven仓库
* mvn deploy 用来将项目生成的构件分发到远程Maven仓库
* 本地Maven仓库的构件只能供当前用户使用，在分发到远程Maven仓库之后，所有能访问该仓库的用户都能使用你的构件。
我们需要配置POM的**distributionManagement来指定Maven分发构件的位置**，如下：

```xml
<project>    
  ...    
  <distributionManagement>    
    <repository>    
      <id>nexus-releases</id>    
      <name>Nexus Release Repository</name>    
      <url>http://127.0.0.1:8080/nexus/content/repositories/releases/</url>    
    </repository>    
    <snapshotRepository>    
      <id>nexus-snapshots</id>    
      <name>Nexus Snapshot Repository</name>    
      <url>http://127.0.0.1:8080/nexus/content/repositories/snapshots/</url>    
    </snapshotRepository>    
  </distributionManagement>    
  ...    
</project>    
```

* Maven区别对待release版本的构件和snapshot版本的构件，snapshot为开发过程中的版本，实时，但不稳定，release版本则比较稳定。Maven会根据你项目的版本来判断将构件分发到哪个仓库。
* 一般来说，分发构件到远程仓库需要认证，如果你没有配置任何认证信息，你往往会得到401错误。这个时候，如下在settings.xml中配置认证信息：

```xml
<settings>    
  ...    
  <servers>    
    <server>    
      <id>nexus-releases</id>    
      <username>admin</username>    
      <password>admin123</password>    
    </server>    
    <server>    
      <id>nexus-snapshots</id>    
      <username>admin</username>    
      <password>admin123</password>    
    </server>      
  </servers>    
  ...    
</settings> 
```

* 需要注意的是，settings.xml中server元素下id的值必须与POM中repository或snapshotRepository下id的值完全一致。将认证信息放到settings下而非POM中，是因为POM往往是它人可见的，而settings.xml是本地的。
