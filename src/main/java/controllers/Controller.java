package controllers;

public interface Controller {

    String standardMessage();

    byte[] checkEndpoint(String endPoint);

    String[] getAllowedCheckpoints();
    byte[] getEndpointPrefix();
}
