package com.example.crud.contract;

public abstract class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final Object resourceId;

    protected ResourceNotFoundException(String resourceName, Object resourceId) {
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Object getResourceId() {
        return resourceId;
    }
}
