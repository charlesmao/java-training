package com.maozy.study.rpc.server;

import java.io.IOException;

/**
 * 服务端启动类
 *
 * Created by maozy on 2018/5/15.
 */
public class ServerTest {

    public static void main(String[] args) throws IOException {
        RpcExporter.exporter("localhost", 8088);
    }

}
