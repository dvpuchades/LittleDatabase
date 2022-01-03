import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Wrapper extends UnicastRemoteObject implements WrapperInterface {
    private Object object;

    protected Wrapper(Object object) throws RemoteException {
        this.object = object;
    }

    @Override
    public void setObject(Object object) throws RemoteException {
        this.object = object;
    }

    @Override
    public Object getObject() throws RemoteException {
        return object;
    }
}
