package com.sample.nio.buffer;


import java.nio.CharBuffer;

/**
 * 例子：填充和释放缓冲区
 *
 * Created by maozy on 2018/5/16.
 */
public class BufferFillDrain {

    private static int index = 0;

    private static String [] strings = {
            "A random string value",
            "The product of an infinite number of monkeys",
            "Hey hey we're the Monkees",
            "Opening act for the Monkees: Jimi Hendrix",
            "'Scuse me while I kiss this fly", // Sorry Jimi ;-)
            "Help Me! Help Me!",
    };

    public static void main(String[] args) {


        //初始化容量为100
        CharBuffer buffer = CharBuffer.allocate(100);

        while (fillBuffer(buffer)) {
            //翻转，将limit = position;position = 0;mark = -1;
            buffer.flip();
            drainBuffer(buffer);
            //清理缓存区，一般在重新对缓冲区填充数据前执行
            //position = 0;limit = capacity;mark = -1;
            buffer.clear();
        }



    }

    /**
     * 释放缓冲区
     * @param buffer
     */
    private static void drainBuffer(CharBuffer buffer) {

        while (buffer.hasRemaining()) {
            //读取缓冲区内容
            System.out.print(buffer.get());
        }
        System.out.println("");

    }

    /**
     * 填充缓冲区
     * @param buffer
     * @return
     */
    private static boolean fillBuffer(CharBuffer buffer) {

        if (index >= strings.length) {
            return false;
        }

        String str = strings[index++];

        for (int i = 0; i < str.length(); i++) {
            //将字符写入缓冲区
            buffer.put(str.charAt(i));
        }

        return true;

    }


}
