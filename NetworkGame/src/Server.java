import java.net.ServerSocket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by codecadet on 20/06/16.
 */
public class Server {

    private final int portNumber = 8080;
    private ServerSocket serverSocket = null;
    private List<ClientConnection> clientList = Collections.synchronizedList(new LinkedList<>());
    //pool

    public Server(){

    }

    public void init(){

    }

    public synchronized void seendToAll(){

    }

    public void removeFromServer(){

    }
}
