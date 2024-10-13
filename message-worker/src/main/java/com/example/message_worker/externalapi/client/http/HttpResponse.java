package com.example.message_worker.externalapi.client.http;

public class HttpResponse<T> {
    private int statusCode;
    private T body;

    public HttpResponse(int statusCode, T body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public T getBody() {
        return body;
    }
}

