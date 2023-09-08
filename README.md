# FullColorTcpServer
全彩控制版 TCP Server
## 登录
- 登录后使用 pid 作为唯一的设备标识，该标识需要记录起来
- 将连接的channel 和 pid 保存到内存中

## 指令下发
- 请求地址 device/instruct/{pid}
- 请求方法 post
- 请求数据 就是文档中下发的指令的json 格式，比如name input 
- 使用示范 发送屏幕关闭指令
```
POST http://localhost:9000/device/instruct/5011303034474130020BAD10B9A9E719
Content-Type: application/json

{
  "name":"screenOnOff",
  "input": {
    "screenonoffstatus": "off"
  }
}
```
- 返回结果 boolean, true 代表发送到设备 false 代表发送超时或者没发送
- 没发送的原因有 设备的channel 找不到，或者网络问题

## 指令结果查看
- 请求地址 device/instruct/{pid}
- 请求方法 get
- 会展示设备上报的指令结果与请求指令
- 请求示范 GET http://127.0.0.1:9000/device/instruct/5011303034474130020BAD10B9A9E719
