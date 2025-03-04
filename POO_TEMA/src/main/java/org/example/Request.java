package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Request {
    private RequestType requestType;
    private LocalDateTime requestDate;
    private String username;
    private String to;
    private String description;
    private String object;

    public String getUsername() {
        return username;
    }

    public Request(RequestType requestType, LocalDateTime requestDate, String username, String to, String description) {
        this.requestType = requestType;
        this.requestDate = requestDate;
        this.username = username;
        this.to = to;
        this.description = description;
    }

    public static class RequestsHolder {
        private List<Request> requestList;

        public RequestsHolder(List<Request> requestList) {
            this.requestList = requestList;
        }

        public void addRequest(Request request) {
            requestList.add(request);
        }
        public void removeRequest(Request request) {
            requestList.remove(request);
        }
        public List<Request> getRequestList() {
            return requestList;
        }

    }

    public void displayInfo() {
        System.out.println("Request type: " + requestType);
        System.out.println("Request date: " + requestDate);
        System.out.println("Request description: " + description);
        System.out.println("Request added by: " + username);
        System.out.println("");
    }

    public String getFormattedBirthDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return requestDate.format(formatter);
    }
}
