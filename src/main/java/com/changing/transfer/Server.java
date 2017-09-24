package com.changing.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description : 服务器端程序，暂时考虑用BIO模型，客户端连接不是非常多
 * @Author : wuchangqing
 * @Date : 2017/9/22
 */
public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public void start(){
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket clientSocket = serverSocket.accept();
            new Thread(new ServerRunnable(clientSocket)).start();
        } catch (IOException e) {
            LOGGER.error("start error",e);
        }

    }


}
