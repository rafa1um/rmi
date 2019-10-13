package blackboard_server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Boards extends Remote {
    String sayHello() throws RemoteException;
}
