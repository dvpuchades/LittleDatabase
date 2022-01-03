import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WrapperInterface extends Remote {
    public void setObject(Object object) throws RemoteException;
    public Object getObject() throws RemoteException;
}
