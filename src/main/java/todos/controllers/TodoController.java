package todos.controllers;

import controllers.Controller;

public class TodoController implements Controller {

    private static final String[] TODO_ALLOWED_ENDPOINTS = {};
    private static final String TODO_ENDPOINT_PREFIX = "/todo/";
    @Override
    public String standardMessage() {
        return "{ \"error\": \"You have reached the controller for todos, but the endpoint does not" +
                "match any correct ones\"}";
    }

    @Override
    public byte[] checkEndpoint(String endPoint) {
        return this.standardMessage().getBytes();
    }

    @Override
    public String[] getAllowedCheckpoints() {
        return TODO_ALLOWED_ENDPOINTS;
    }

    @Override
    public byte[] getEndpointPrefix() {
        return TODO_ENDPOINT_PREFIX.getBytes();
    }
}
