package com.netio;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * NIO的网络编程
 * TCP连接通讯
 * 目的：接收客户端的请求，并返回相同信息，客户端写入byebye接收接收，结束本次操作
 * @author Nuri
 * @CreateTime 2020/4/19
 * @Describe
 */
public class Server {
    // 初始化线程池
    private  static  ThreadPoolExecutor pool = new ThreadPoolExecutor(10,50,5, TimeUnit.SECONDS,new LinkedBlockingDeque<>(100));

    // 在服务端启动时需绑定一个端口用于提供服务，客户通过IP+port连接服务
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9001);
        // 接收不断发送过来的请求，因为accept会阻塞，使用线程池
        while(!serverSocket.isClosed()){
            // 未关闭serve监听
            Socket accept = serverSocket.accept();
            if (accept.isConnected()){
                // 已经连接
                System.out.println("连接为："+accept.toString());
            }
            // 未连接
            pool.execute(()->{
                // 发送的数据->以打印流的方式
                PrintStream printStream = null;
                try {
                    printStream = new PrintStream(String.valueOf(accept.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 扫描流负责接收客户端发送的请求
                Scanner in = null;
                try {
                    in = new Scanner(accept.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true){
                    if (in.hasNext()) { // 阻塞的方法，InputStream的read方法
                        String next = in.next();
                        System.out.println("客户端发送信息："+next);
                        // 接收消息，回复相同信息
                        printStream.println(next);
                        if(next.equals("bye")){
                            break;
                        }
                    }
                }
                printStream.close();
                in.close();
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println("服务端关闭");
        serverSocket.close();
    }
}
