package com.netio;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * 客户端发送信息
 * @author Nuri
 * @CreateTime 2020/4/19
 * @Describe
 */
public class Client {
    public static void main(String[] args) throws Exception {
        InetSocketAddress localhost = new InetSocketAddress("localhost", 9001);
        Socket socket = new Socket();
        socket.connect(localhost,1000*5);
        // 发送输入的数据 已打印流的方式
        PrintStream printStream = new PrintStream(socket.getOutputStream());
        // 接收服务端的数据 以打印流的方式
        Scanner in = new Scanner(socket.getInputStream());
        System.out.println("请输入需要发送的数据：");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // 发送信息
            if(scanner.hasNext()){ // hasnext 和 next 为半阻塞方法，需要变量接收
                String next = scanner.next();
                printStream.println(next);
                if (next.equals("bye")){
                    System.out.println("服务端发送数据："+in.next());
                    break;// 不可立即退出，会导致客户端退出，服务端连接导致丢包
                }
                if (in.hasNext()){
                    System.out.println("服务端发送信息："+in.next());
                }
            }
        }
        socket.close();
        in.close();
        scanner.close();
        printStream.close();
    }
}
