package dev.jeppu.wizard;

import dev.jeppu.artifact.Artifact;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Wizard implements Serializable {
    @Id
    private Integer id;

    private String name;

    @OneToMany(
            mappedBy = "owner",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Artifact> artifacts = new ArrayList<>();

    public Wizard(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addArtifact(Artifact artifact) {
        artifact.setOwner(this);
        this.artifacts.add(artifact);
    }

    public Integer getNumberOfArtifacts() {
        return this.artifacts.size();
    }
}
