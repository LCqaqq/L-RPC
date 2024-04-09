package com.lc.lrpc.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.lrpc.model.RpcRequest;
import com.lc.lrpc.model.RpcResponse;

import java.io.IOException;

/**
 * json 序列化器
 */
public class JsonSerializer implements Serializer{

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        T obj = OBJECT_MAPPER.readValue(bytes,type);
        if (obj instanceof RpcRequest){
            return handleRequest((RpcRequest) obj,type);
        }
        if (obj instanceof RpcResponse){
            return  handleResponse((RpcResponse) obj,type);
        }
        return null;
    }



    private <T> T handleRequest(RpcRequest rpcRequest, Class<T> type) throws IOException{
        Class<?>[] parametTypes = rpcRequest.getParameterType();
        Object[] args = rpcRequest.getArgs();

        for (int i=0;i<parametTypes.length;i++){
            Class<?> clazz = parametTypes[i];
            if (!clazz.isAssignableFrom(args[i].getClass())){
                byte[] argBytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                args[i] = OBJECT_MAPPER.readValue(argBytes,clazz);
            }
        }
        return type.cast(rpcRequest);
    }

    private <T> T handleResponse(RpcResponse rpcResponse, Class<T> type) throws IOException {
        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
        rpcResponse.setData(OBJECT_MAPPER.readValue(dataBytes,rpcResponse.getDataType()));
        return type.cast(rpcResponse);
    }
}
