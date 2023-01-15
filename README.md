<div align=center>

<img src="https://static.rtast.cn/static/rmusic/icon.png" alt="icon">

<h2>Made by RTAkland</h2>

<h2>可以在游戏内播放自己想听的音乐！</h2>

<img src="https://static.rtast.cn/static/kotlin/made-with-kotlin.svg" alt="made-with-kt">
<br>
<img src="https://img.shields.io/github/actions/workflow/status/RTAkland/RMusic/build.yml" alt="buildStatus">
<img src="https://img.shields.io/github/last-commit/RTAkland/RMusic" alt="lastCommit">
<img src="https://img.shields.io/github/license/RTAkland/RMusic?label=license&logo=apache" alt="license">
<img src="https://img.shields.io/github/v/release/RTAkland/RMusic?include_prereleases" alt="release">
<img src="https://img.shields.io/badge/MC-1.19.2-pink?logo=minecraft" alt="mcVersion">

<h3>当前正处于早期正式版, 如果有bug或者需求请务必提交 issue !</h3>

</div>

<!-- TOC -->
* [注意事项](#注意事项)
* [已经支持的功能](#已经支持的功能)
  * [使用例](#使用例)
* [开发](#开发)
  * [克隆项目](#克隆项目)
  * [手动编译](#手动编译)
  * [VS code](#vs-code)
  * [Eclipse](#eclipse)
* [开源](#开源)
* [鸣谢](#鸣谢)
<!-- TOC -->

# 注意事项

> `RMusic Mod` 仅支持 `Fabric 1.19.2` 版本, 后续会支持更高版本, 也可以自行 Clone 仓库到本地修改版本后自行编译使用, 详见 [开发](#开发)

* 无论是客户端/服务器使用本mod的`login` `logout`命令时, 存储的`cookie.json`都是在客户端本地, 但是音乐API时取决于配置文件内的地址
* 如果想要让mod正常工作, 请在装有`fabric-language-kotlin >=1.9.0+kotlin.1.8.0` 和 `fabric-loader >=0.14.10`
  的`服务器` `客户端` 同时安装
* 服务端可以使用所有命令, 命令下发后, 所有装有 `Rmusic`版本 `>=0.1.1`以上的玩家将会执行操作
    * `login` `logout` 命令会让玩家`登录` `登出`, 但是操作的是客户端本地的 `cookie.json`
* 服务端客户端同时安装了 `RMusic` 配置取决于服务端的 api 地址

# 已经支持的功能

1. [x] 播放来自网易云的音乐
2. [x] 对音乐播放的各种操作
3. ~~歌词显示~~ (暂时鸽了)

> 所有子命令都以 `/rmusic` 或 `/rm` 命令开头

- `/rm`
    - `play`
        - `<id>`  播放网易云的音乐
    - `stop`  停止播放, 无法继续播放
    - `pause`  暂停播放, 继续播放请使用 `resume`
    - `resume`  继续播放
    - `volume`
        - `<volume>`  设置音量, 设置范围为 `0~2`, 浮点型参数
    - `search`
        - `<keyword>`  从网易云搜索音乐
    - `login`
        - `<platform>`
            - `<email>` `<password>`  登录, 暂时只能使用 `邮箱` 登录
    - `logout`  登出, 登出将删除本地cookie.json文件
    - `set-url` 设置新的API地址 ***仅管理员/单人可用***
    - `reload` 重新载入修改后的配置文件 ***仅管理员/单人可用***
      ***注: 此命令适用于手动修改配置文件内的API地址后进行重载, 使用set-url修改后会自动重新载入***

## 使用例

* `/rm play 114514`
* `/rm pause`
* `/rm resume`
* `/rm mute`
* `/rm stop`
* `/rm search "恶臭的野兽先辈"`   ***如果是中文或者其他特殊符号请使用双引号/单引号括起来***
* `/rm login 163 "114514@114514.com" "1145141919810"`  ***账号密码必须用引号括起来***
* `/rm logout`
* `/rm set-url "https://114514.space"`  ***地址使用引号括起来***
* `/rm reload`

> ***暂时没办法快进或者快退音乐***

# 开发

* 建议使用`Intellij IDEA` 可进行开发

## 克隆项目

```shell
$ git clone https://github.com/RTAkland/RMusic.git
```

## 手动编译

> Linux/Unix/Mac OS

```shell
$ chmod +x ./gradlew
$ ./gradlew build
```

> Windows

```shell
$ .\gradlew.bat build
```

> 使用`Intellij IDEA`打开项目, IDEA会自动构建项目并设置好运行配置

## VS code

```shell
$ cd RMusic
$ code .
$ ./gradlew vscode
```

## Eclipse

```shell
$ cd RMusic
$ ./gradlew eclipse
```

> 输出的构建文件在 [build/libs/*.jar](build/libs), 请运行文件名内没有`source`字样的jar文件

# 开源

- 本项目以[Apache-2.0](./LICENSE)许可开源, 即:
    - 你可以直接使用该项目提供的功能, 无需任何授权
    - 你可以在**注明来源版权信息**的情况下对源代码进行任意分发和修改以及衍生

# 鸣谢

* [JetBrains Open Source](https://www.jetbrains.com/opensource/) 项目提供的IDE支持.
