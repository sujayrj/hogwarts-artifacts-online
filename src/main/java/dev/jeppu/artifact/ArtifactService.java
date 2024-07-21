package dev.jeppu.artifact;

import dev.jeppu.artifact.convertor.ArtifactToArtifactDTOConvertor;
import dev.jeppu.artifact.util.IdWorker;
import dev.jeppu.system.exception.ObjectNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ArtifactService {
    private final ArtifactRepository artifactRepository;
    private final ArtifactToArtifactDTOConvertor convertor;
    private final IdWorker idWorker;

    public Artifact findById(String artifactId) {
        return artifactRepository
                .findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("Artifact", artifactId));
    }

    public List<Artifact> findAllArtifacts() {
        return artifactRepository.findAll();
    }

    public Artifact saveArtifact(Artifact artifact) {
        artifact.setId(String.valueOf(idWorker.nextId()));
        return artifactRepository.save(artifact);
    }

    public Artifact updateArtifact(String artifactId, Artifact newArtifact) {
        return artifactRepository
                .findById(artifactId)
                .map(oldArtifact -> {
                    oldArtifact.setName(newArtifact.getName());
                    oldArtifact.setDescription(newArtifact.getDescription());
                    oldArtifact.setImageUrl(newArtifact.getImageUrl());
                    oldArtifact.setOwner(newArtifact.getOwner());
                    return artifactRepository.save(oldArtifact);
                })
                .orElseThrow(() -> new ObjectNotFoundException("Artifact", artifactId));
    }

    public void deleteArtifactById(String artifactId) {
        Artifact artifact = artifactRepository
                .findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("Artifact", artifactId));
        artifactRepository.deleteById(artifactId);
    }
}
