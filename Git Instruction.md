## Git操作说明

### 切换分支

fork本工程后可以按照如下操作即可，

- 切换到master分支，并且更新最新远程库中的代码

  - git checkout master
  - git pull/git fetch

### 创建分支

- 创建自己的本地分支，以master为源创建

  - git checkout -b fortune-commons-export

- 查看是否创建成功

  - git branch

  ```
    fortune-commons-beanutils
  * fortune-commons-export
    fortune-commons-memcached
    master
  ```

  星号(*)表示当前所在分支。现在的状态是成功创建的新的分支并且已经切换到新分支上。

### 同步分支

- 把新建的本地分支push到远程服务器，远程分支与本地分支同名（当然可以随意起名）

  - git push origin fortune-commons-export:fortune-commons-export

### 创建标签

- git tag -a v1.0.1 -m "fortune commons v1.0.1"
- git push origin v1.0.1