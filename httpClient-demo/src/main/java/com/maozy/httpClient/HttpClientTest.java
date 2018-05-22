package com.maozy.httpClient;


import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by maozy on 2018/5/21.
 */
public class HttpClientTest {

    public static void main(String[] args) throws IOException {

        //doGet(); //GET请求
        doPost(); //POST请求

    }

    private static void doPost() throws IOException {

        String jsonStr = "{\"jql\": \"project = BUPREQ and issuetype=epic\",\"maxResults\": 10,\"fields\": [\"summary\"]}";
        StringEntity entity = new StringEntity(jsonStr, HTTP.UTF_8);
        entity.setContentType("application/json");
        HttpPost httpPost = new HttpPost("https://jira.infinitus.com.cn/rest/api/2/search");
        httpPost.setEntity(entity);


        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope("jira.infinitus.com.cn", 80),
                new UsernamePasswordCredentials("zr_maozhongyong", "abc_123"));

        System.out.println("Executing request " + httpPost.getRequestLine());

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
        CloseableHttpResponse response = httpClient.execute(httpPost);

        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        System.out.println(formatJson(EntityUtils.toString(response.getEntity())));
        response.close();
        httpClient.close();

    }


    private static void doGet() throws IOException {

        HttpGet httpGet = new HttpGet("https://jira.infinitus.com.cn/rest/api/2/dashboard");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope("jira.infinitus.com.cn", 80),
                new UsernamePasswordCredentials("zr_maozhongyong", "abc_123"));

        System.out.println("Executing request " + httpGet.getRequestLine());

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
        CloseableHttpResponse response = httpClient.execute(httpGet);

        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        System.out.println(formatJson(EntityUtils.toString(response.getEntity())));
        response.close();
        httpClient.close();

    }

    /**
     * 格式化json输出
     * @param jsonStr
     * @return
     */
    private static String formatJson(String jsonStr) {
        int level = 0;
        StringBuffer jsonForMatStr = new StringBuffer();
        for(int i=0;i<jsonStr.length();i++){
            char c = jsonStr.charAt(i);
            if(level>0&&'\n'==jsonForMatStr.charAt(jsonForMatStr.length()-1)){
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c+"\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c+"\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }

        return jsonForMatStr.toString();
    }

    private static String getLevelStr(int level){
        StringBuffer levelStr = new StringBuffer();
        for(int levelI = 0;levelI<level ; levelI++){
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

}
