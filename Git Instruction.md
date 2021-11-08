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

  ```shell
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

### How To Fork

#### 总体步骤

```shell
# first create a repo on eHealth Bitbucket server from the interface
# clone that empty repo from eHealth Bitbucket
git clone ssh://git@github.com/landy8530/wx-api.git
# get into the new repo
cd wx-api
# look at the existing remotes, you should see only wx-api repo as a remote
git remote -v
# Add the original project's git address as another remote called upstream
git remote add upstream ssh://git@github.com/niefy/wx-api.git
# look at the actual remotes, you should see both wx-api repo and original repo
git remote -v
# pull the master branch of upstream which is the forked project
git pull upstream master
# set an upstream branch to track the future changes in upstream repo
git branch --track github-master upstream/master
# push these files to eHealth repo
git push -u origin master
#you should regularly fetch the changes in upstream/master and rebase your master onto upstream master. That way your repo will get the latest changes of upstream open source project
```

#### 详细步骤

##### A 克隆项目，增加代码

> 1. 在 Github 上 fork 想要参与的项目仓库 **niefy/wx-api**, fork后会生成自己的项目 **landy8530/wx-api**
> 2. `git clone 自己的项目`
> 3. `git add XX`，`git commit -m ""` 进行更新，提交
> 4. `git push origin master` 推送到自己的远程仓库 **landy8530/wx-api**
> 5. 在 github 上新建 `Pull Request` 请求
> 6. 项目管理员会审核你提交的代码，如果合适就会同意合并，这样你的代码就会出现在源项目中。

```undefined
当我们睡了一觉起来，niefy/wx-api仓库可能已经更新，我们要同步最新代码
```

##### B 获取最新代码

> 1. 给远程的上游仓库**niefy/wx-api**配置一个 remote 。
>    - `git remote -v` 查看远程状态
>    - `git remote add upstream 远程仓库niefy/wx-api链接`
>      - 例如：`git remote add upstream ssh://git@github.com/niefy/wx-api.git`
> 2. `git fetch upstream` 将远程所有的分支fetch下来
> 3. `git merge upstream/master` 合并非master分支的代码
> 4. `git pull upstream master` 可以代替流程B的步骤 2+3。 `git pull = fetch + merge`
>
> 此时自己本地的代码就是最新的了，修改完代码后，`重复A流程中的步骤 3-5`