package com.lc.lrpc.server.tcp;

import com.lc.lrpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

public class VertxTcpServer implements HttpServer {
    /**
     * 启动服务器
     * @param port
     */
    public void doStar(int port){
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();

        //创建 TCP 服务器
        NetServer server = vertx.createNetServer();
        //监听端口并处理请求
        server.connectHandler(socket ->{
            //处理链接
            socket.handler(buffer -> {
                //处理接收到的字节数组
                byte[] requestData = buffer.getBytes();
                byte[] responseData = handleRequest(requestData);
                //发送响应
            socket.write(Buffer.buffer(responseData));
            });
        });
        //启动TCP服务器并监听指定端口
        server.listen(port,result->{
            if (result.succeeded()){
                System.out.println("TCP server is now listening on port"+port);
            } else {
                System.err.println("Failed to start TCP server :" +result.cause());
            }
        });

    }

    private byte[] handleRequest(byte[] requestData){
        return "hello, client".getBytes();
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStar(8888);
    }
}
