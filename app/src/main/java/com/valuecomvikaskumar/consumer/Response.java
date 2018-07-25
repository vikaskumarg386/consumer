package com.valuecomvikaskumar.consumer;

/**
 * Created by vikas on 28/6/18.
 */

public class Response {
    String message;
    String time;
    String userId;

    public Response() {
    }

    public Response(String message, String time, String userId) {
        this.message = message;
        this.time = time;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
