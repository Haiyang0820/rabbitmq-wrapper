package vn.sanobl.rabbitmq;

/**
 * Created by bangnk on 7/11/17.
 */
public class RBConfiguration {

    private String listhost; //localhost:5672;localhost:5673
    private String username;
    private String password;
    private String virtualhost;

    public RBConfiguration() {
    }

    public RBConfiguration(String listhost, String username, String password, String virtualhost) {
        this.listhost = listhost;
        this.username = username;
        this.password = password;
        this.virtualhost = virtualhost;
    }

    public String getListhost() {
        return listhost;
    }

    public void setListhost(String listhost) {
        this.listhost = listhost;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVirtualhost() {
        return virtualhost;
    }

    public void setVirtualhost(String virtualhost) {
        this.virtualhost = virtualhost;
    }
}
