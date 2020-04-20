package com.io;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO学习
 * @author Nuri
 * @CreateTime 2020/4/19
 * @Describe
 */
public class NioTest {
    // 文件读取路径
    public static String readFilepath = "/Users/nuri/Downloads/now/src/main/resources/ReadFile.txt";
    // 文件输出路径
    public static String writeFilePath = "/Users/nuri/Downloads/now/src/main/resources/WriteFile.txt";
    public static void main(String[] args) {
        // 使用NIO读写文件
        readFileByNIO(readFilepath,writeFilePath);
    }

    /**
     * 使用NIO读写文件
     * @param readFilepath
     * @param writeFilepath
     */
    public static  void readFileByNIO(String readFilepath,String writeFilepath){
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            // 输入流
            inputStream = new FileInputStream(readFilepath);
            // 输出流
            outputStream = new FileOutputStream(writeFilepath);
            // 获取通道
            FileChannel inputStreamChannel = inputStream.getChannel();
            FileChannel outputStreamChannel = outputStream.getChannel();
            // 创建缓冲区 大小：1024
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 获取开始时间
            long start = System.currentTimeMillis();
            while (true){
                // 重设缓冲区，使它可以接收读入数据，Buffer对象中的limit = capacity
                buffer.clear();
                // 从输入通道读取数据
                int temp = inputStreamChannel.read(buffer);
                System.out.println("输入通道读取数据："+temp);
                if (temp == -1){
                    // 读取完成
                    break;
                }
                // 让缓冲区将读入的数据写到另一个通道中
                Buffer buffer1 = buffer.flip();
                System.out.println("将缓存区的数据写到另一个通道中，flip返回："+buffer1.toString());
                outputStreamChannel.write(buffer);
            }
            long end = System.currentTimeMillis();
            System.out.println("总共耗时："+(end-start)+"ms");
        } catch (FileNotFoundException e) {
            System.err.println("异常[FileNotFoundException]捕获："+e.getMessage());
        } catch (IOException e) {
            System.err.println("异常[IOException]捕获："+e.getMessage());
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
