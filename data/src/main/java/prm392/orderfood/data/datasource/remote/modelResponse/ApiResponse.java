package prm392.orderfood.data.datasource.remote.modelResponse;

public class ApiResponse<T> {
    private boolean success;
    private String messageId;
    private String message;
    private T data;

    public boolean isSuccess() { return success; }
    public String getMessageId() { return messageId; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}