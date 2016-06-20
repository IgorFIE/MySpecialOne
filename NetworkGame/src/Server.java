import java.net.ServerSocket;
import java.util.Collections;

/**
 * Created by codecadet on 20/06/16.
 */
public class Server {

    private final int portNumber = 8080;
    private ServerSocket serverSocket = null;
    private List<Client> clientList = Collections.synchronizedList(new LinkedList<>());

}
