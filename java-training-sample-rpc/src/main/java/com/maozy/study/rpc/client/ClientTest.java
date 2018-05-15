package com.maozy.study.rpc.client;

import com.maozy.study.rpc.server.EchoService;
import com.maozy.study.rpc.server.EchoServiceImpl;

import java.net.InetSocketAddress;

/**
 * 客户端调用类
 *
 * Created by maozy on 2018/5/14.
 */
public class ClientTest {

    public static void main(String[] args) {
        RpcImporter<EchoService> importer = new RpcImporter<>();
        EchoService echoService = importer.importer(EchoServiceImpl.class, new InetSocketAddress("localhost", 8088));
        System.out.println(echoService.echo("Are you ok?"));

    }

}
