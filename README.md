# 简易RPC框架



RPC （Remote Produce Call）既远程调用，允许程序在不同计算机、服务器节点之间像本地调用一样进行通信和交互，

1 主要模块 服务客户端 注册中心，序列化器，
2.采用 Vert.X 作为客户端

3.SPI (Service Provider Interface) 服务提供者接口 通过读取配置文件对框架进行配置

4. etdc 注册中心 提供服务注册 服务发现 心跳检测 服务下线等功能

45.序列化器 由用户自己决定对信息采用什么方式编码，编码之后的信息才能在网络上流通