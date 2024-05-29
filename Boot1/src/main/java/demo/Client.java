package demo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;

/**
 *
 */
public class Client {

    public static void main(String[] args) {
        Socket socket = null;
        ObjectOutputStream objectOutputStream = null;
        ObjectInputStream inputStream = null;
        InetSocketAddress endpoint = new InetSocketAddress("127.0.0.1", 10001);
        try {
            socket = new Socket();
            socket.connect(endpoint);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeUTF("啊哈哈哈");
            objectOutputStream.flush();
            String string = inputStream.readUTF();
            System.err.println("Client :" + string);
        } catch (Exception e) {

        } finally {
            if (null != socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (null != objectOutputStream) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
