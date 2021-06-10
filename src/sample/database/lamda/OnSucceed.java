package sample.database.lamda;

public interface OnSucceed<T> {
    void operate(T result);
}
