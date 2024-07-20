package dev.jeppu.artifact;

import dev.jeppu.wizard.Wizard;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Artifact implements Serializable {
    @Id
    private String id;

    private String name;
    private String description;
    private String imageUrl;

    @ManyToOne
    private Wizard owner;
}
