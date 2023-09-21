package app.utils;

public interface TaskMessageCallback<T> {
    void callback(T object) throws InterruptedException;
}
