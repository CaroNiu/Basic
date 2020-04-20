package com.netio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 接收Http请求
 * @author Nuri
 * @CreateTime 2020/4/19
 * @Describe
 */
public class HttpServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9002);
        while (!serverSocket.isClosed()){
            Socket socket = serverSocket.accept();
            if (socket.isConnected()) {
                System.out.println("当前连接为："+socket.toString());
            }
            InputStream in = socket.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(in, "utf-8");
            BufferedReader reader = new BufferedReader(streamReader);
            String msg = "";
            while ( (msg = reader.readLine()) != ""){
                System.out.println("获取读取数据："+msg);
                if (msg.length() == 0){
                    break;
                }
            }
            System.out.println("接收到数据，来自："+socket.toString());
            OutputStream out = socket.getOutputStream();
            out.write("HTTP/1.1 500 OK\r\n".getBytes());
            out.write("Content-Length: 11\r\n\r\n".getBytes());
            out.write("Hello World Test".getBytes());
            out.flush();
        }
        System.out.println("结束服务端");
        serverSocket.close();
    }
}
