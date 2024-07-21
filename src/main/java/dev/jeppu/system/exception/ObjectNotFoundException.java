package dev.jeppu.system.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String objectName, String id) {
        super(String.format("Could not find %s with Id : %s", objectName, id));
    }
}
