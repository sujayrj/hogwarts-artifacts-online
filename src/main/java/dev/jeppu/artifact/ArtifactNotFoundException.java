package dev.jeppu.artifact;

public class ArtifactNotFoundException extends RuntimeException {
    public ArtifactNotFoundException(String id) {
        super("Could not find Artifact with Id " + id);
    }
}
