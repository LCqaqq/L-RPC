package com.lc.lrpc.fault.tolerant;


import com.lc.lrpc.spi.SpiLoader;

public class TolerantStrategyFactory {
    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    /**
     * 默认重试器
     */
    private static final TolerantStrategy DEFAULT_TOLERANT_STRATEGY = new FastTolerantStrategy();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }
}
