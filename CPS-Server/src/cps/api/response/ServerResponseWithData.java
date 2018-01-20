package cps.api.response;

public abstract class ServerResponseWithData<T> extends ServerResponse {
  private static final long serialVersionUID = 1L;
  private T                 data             = null;

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

}
