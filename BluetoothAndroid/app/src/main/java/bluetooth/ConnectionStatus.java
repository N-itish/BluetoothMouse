package bluetooth;

public class ConnectionStatus {
    private boolean connectionStatus;
    public ConnectionStatus(){
        this.connectionStatus = false;
    }

    public void setConnectionStatus(boolean status){
        this.connectionStatus = status;
    }
    public boolean getConnectionStatus(){
        return connectionStatus;
    }
}
