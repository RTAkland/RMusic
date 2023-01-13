<!-- TOC -->
* [操作码](#操作码)
* [技术点](#技术点)
  * [实现](#实现)
  * [取消播放原版音乐事件](#取消播放原版音乐事件)
  * [播放音乐](#播放音乐)
<!-- TOC -->

# 操作码

1. `0` -> `play` with body: [name, url, artists]
2. `1` -> `stop` without body
3. `2` -> `resume` without body
4. `3` -> `pause` without body
5. `4` -> `mute` without body
6. `5` -> `search` with body: [keyword]
7. `6` -> `volume` with body: [volume]
8. `7` -> `login` with body: [email, password]
9. `8` -> `logout` without body

> `body`的数据使用 `^` 符号分割


# 技术点
## 实现

- 客户端
    - 客户端对音乐的操作均在[RMusicCommand.kt](../src/main/kotlin/cn/rtast/rmusic/client/commands/RMusicCommand.kt)

-客户端加入服务端执行命令
- 玩家执行服务端`RMusic`注册的命令, 命令内容是, 上方操作码定义的内容, 客户端接收到数据包之后会根据操作码做出不同的相应
- 对音乐的操作均在[OnOPPacket.kt](../src/main/kotlin/cn/rtast/rmusic/client/events/OnOPPacket.kt)

> 注: 每次使用命令都会创建一个对象, 所以将[RMusicClient.kt](../src/main/kotlin/cn/rtast/rmusic/client/RMusicClient.kt)
> 以及
> [RMusic.kt](../src/main/kotlin/cn/rtast/rmusic/RMusic.kt) 定义为静态类, 在初始化是创建一个对象, 后续访问无需创建新的对象


## 取消播放原版音乐事件
* 使用 `Fabric Mixin` 把对应的代码插入到`play`方法中, 如果`RMusic`正在播放音乐, 则会取消播放背景音乐的事件
* 具体代码见[SoundEventMixin.java](../src/main/java/cn/rtast/rmusic/mixins/SoundEventMixin.java)

## 播放音乐

* 使用了 Java 音频库 `com.github.goxr3plus:java-stream-player` `10.0.2`
