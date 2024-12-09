# RMusic

> 在我的世界中播放来自网易云音乐的歌曲

# 配置RMusic

项目集成了3个音乐API, 如下表所示

| API名称                | 项目地址                                                     | 备注    |
|----------------------|----------------------------------------------------------|-------|
| NeteaseCloudMusicAPI | [NCM](https://gitlab.com/Binaryify/neteasecloudmusicapi) | 网易云音乐 |
| qq-music-api         | [qq-music-api](https://github.com/Rain120/qq-music-api)  | QQ音乐  |
| KuGouMusicApi        | [KuGouMusicApi](https://github.com/MakcRe/KuGouMusicApi) | 酷狗音乐  |

> 你需要手动搭建好API后在config.yml中配置

# 使用

1. /rm login # 登录
2. /rm search # 搜索
3. /rm stop # 停止播放音乐
4. /rm pause # 暂停播放音乐
5. /rm resume # 继续播放音乐
6. /rm reload # 重新加载配置文件 (需要管理员权限)

> 在设置中调整`唱片机/音符盒`的音量可以调整音乐的声音大小

# 展示

![showcase](public/showcase.png)

# 注意事项

> 模组在单人客户端中可以使用, 但是在没有安装RMusic的服务器中无法使用, 服务器安装后
> 可以分享音乐给其他安装了RMusic的客户端

# 开源

- 本项目以[Apache-2.0](./LICENSE)许可开源, 即:
    - 你可以直接使用该项目提供的功能, 无需任何授权
    - 你可以在**注明来源版权信息**的情况下对源代码进行任意分发和修改以及衍生

# 鸣谢

<div>

<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jetbrains.png" alt="JetBrainsIcon" width="128">

<a href="https://www.jetbrains.com/opensource/"><code>JetBrains Open Source</code></a> 提供的强大IDE支持

</div>