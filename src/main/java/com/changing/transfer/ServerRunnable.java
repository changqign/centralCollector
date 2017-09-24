package com.changing.transfer;

import com.changing.model.Message;
import com.changing.store.StoreStrategy;
import com.changing.util.Constants;
import com.changing.util.ObjectStreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @Description :
 * @Author : wuchangqing
 * @Date : 2017/9/22
 */
public class ServerRunnable implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRunnable.class);

    private Socket clientSocket;

    private DataInputStream inputStream;

    @Resource(name = "esStoreStrategy")
    private StoreStrategy storeStrategy;

    public void readAndStore(){
        //循环获取数据，因为有readFully会造成阻塞，所以不会造成CPU飙升
        while(true){
            try {
                Message message = doRead(getInputStream());
                doStore(message);
            } catch (IOException e) {
                LOGGER.error("doRead error", e);
            }
        }
    }

    @Override
    public void run() {
        readAndStore();
    }

    /**
     * 存储到ES或
     * @param message
     */
    private void doStore(Message message) {
        storeStrategy.store(message);
    }

    public DataInputStream getInputStream() throws IOException {
        //暂时不用考虑线程安全问题
        if(inputStream == null){
            inputStream = new DataInputStream(clientSocket.getInputStream());
        }
        return inputStream;
    }

    public Message doRead(DataInputStream dataInputStream) throws IOException {
         int totalLength = inputStream.readInt();
         byte dataType = inputStream.readByte();
         byte[] bs = new byte[totalLength - 4 -1];
         if(Constants.DataType.TYPE_LOG == dataType){
             inputStream.readFully(bs);
         }
         Message message = ObjectStreamUtil.readClassAndObject(bs, Message.class);
         return message;
    }


    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public ServerRunnable() {
    }

    public ServerRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
}
