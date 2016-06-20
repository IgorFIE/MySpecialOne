import java.net.Socket;

/**
 * Created by codecadet on 20/06/16.
 */
public class ClientConnection implements Runnable{

    private Socket clientSocker;

    public ClientConnection(Socket clientSocket){
        this.clientSocker = clientSocket;
    }

    public void run() {
        //in
        //out
    }
}
