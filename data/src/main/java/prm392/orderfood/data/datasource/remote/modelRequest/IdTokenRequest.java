package prm392.orderfood.data.datasource.remote.modelRequest;

public class IdTokenRequest {
    private String idToken;

    public IdTokenRequest(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
