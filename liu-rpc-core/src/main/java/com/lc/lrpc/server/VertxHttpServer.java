package com.lc.lrpc.server;

import io.vertx.core.Vertx;

/**
 *
 */
public class VertxHttpServer implements HttpServer{
    /**
     * 启动服务器
     * @param port
     */
    public void doStar(int port){
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();

        //创建 HTTP 服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();
        //监听端口并处理请求
        server.requestHandler(new HttpServerHandler());
//        server.requestHandler(request->{
//            //处理Http请求
//            System.out.println("Received request:"+request.method()+""+request.uri());
//            //发送http响应
//            request.response()
//                    .putHeader("content-type","text/plain")
//                    .end("hello from Verte.x Http server");
//        });
        //启动Http服务器并监听指定端口
        server.listen(port,result->{
            if (result.succeeded()){
                System.out.println("Server is now listening on port"+port);
            } else {
                System.err.println("faild to start server :" +result.cause());
            }
        });

    }
}
