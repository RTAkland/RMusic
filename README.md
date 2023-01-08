# RMusic

可以在游戏内播放自己想听的音乐！

# 原理

- 客户端/单人模式
    - 客户端注册命令执行各种操作

- 客户端/服务器/局域网房主
    - 客户端使用服务端注册的命令, 执行后服务端将发送对应的操作数据包发送给客户端, 客户端接收数据包按照对应的操作执行

- 服务端
    - 仅将各种操作数据包发送给客户端, 一些特殊的操作会携带数据

# 注意事项

* 无论是客户端/服务器使用本mod的`login` `logout`命令时, 存储的`profile.json`都是在客户端本地, 但是音乐API时取决于配置文件内的地址
* 如果想要让mod正常工作, 请在装有`fabric-language-kotlin` 版本 `>=1.9.0+kotlin.1.8.0` 和 `fabric-loader`
  版本 `>=0.14.10` 的`服务器` `客户端` 同时安装

# 构建

* 建议使用`Intellij IDEA` 可进行开发

## 克隆项目

```shell
$ git clone https://github.com/RTAkland/RMusic.git
```

## 手动编译

```shell
$ ./gradlew build
```

> 使用`Intellij IDEA`打开项目, IDEA会自动构建项目并设置好运行配置

## 使用`VS code`

```shell
$ cd RMusic
$ code .
$ ./gradlew vscode
```

## 使用`Eclipse`

```shell
$ cd RMusic
$ ./gradlew eclipse
```

> 输出的构建文件在 [build/libs/*.jar](build/libs), 请运行文件名内没有`source`字样的jar文件