package com.maozy.httpClient;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 实例链接：http://hc.apache.org/httpcomponents-client-4.5.x/httpclient/examples/org/apache/http/examples/client/ClientAuthentication.java
 *
 * Created by maozy on 2018/5/22.
 */
public class ClientAuthentication {

    public static void main(String[] args) throws IOException {

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope("httpbin.org", 80),
                new UsernamePasswordCredentials("user", "passwd"));

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();

        HttpGet httpGet = new HttpGet("http://httpbin.org/basic-auth/user/passwd");
        System.out.println("Executing request " + httpGet.getRequestLine());
        CloseableHttpResponse response = httpClient.execute(httpGet);

        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
        response.close();
        httpClient.close();


    }


}
