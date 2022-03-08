package br.com.alura.ecommerce;

public class Message<T> {

    private final CorrelationId correlationId;
    private final T payload;

    public Message(CorrelationId correlationId, T payload) {
        this.correlationId = correlationId;
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Message{" +
                "correlationId=" + correlationId +
                ", payload=" + payload +
                '}';
    }
}
