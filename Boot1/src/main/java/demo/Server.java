package demo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 */
public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(10001);
        Socket accept = socket.accept();
        ObjectInputStream objectInputStream = new ObjectInputStream(accept.getInputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(accept.getOutputStream());
        System.err.println("Server " + objectInputStream.readUTF());
        objectOutputStream.writeUTF("回答啦");
        objectOutputStream.flush();
        objectOutputStream.close();
        objectInputStream.close();
        socket.close();
    }

}
