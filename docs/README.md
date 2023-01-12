# 操作码文档

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
