package com.lc.lrpc.protocol;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {
    /**
     * 消息头
     */
    private Header header;
    private T body;
    public static class Header{
        private byte magic;
        private byte version;
        private byte serializer;
        private byte type;
        private byte status;
        private long requestId;
        private int bodyLength;
    }

}
