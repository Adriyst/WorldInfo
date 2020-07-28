package controllers;

public class StandardController implements Controller {

    private final static String STANDARD_ENDPOINT_PREFIX = "/";
    private final static String[] STANDARD_ALLOWED_ENDPOINTS = {};

    public StandardController() {}

    public String standardMessage() {
        return "{ \"error\": " +
                "\"You have reached the standard controller. " +
                "That probably means that something unintentional happened.\"}";
    }

    @Override
    public byte[] checkEndpoint(String endPoint) {
        return this.standardMessage().getBytes();
    }

    @Override
    public String[] getAllowedCheckpoints() {
        return STANDARD_ALLOWED_ENDPOINTS;
    }

    @Override
    public byte[] getEndpointPrefix() {
        return STANDARD_ENDPOINT_PREFIX.getBytes();
    }
}
