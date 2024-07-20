package dev.jeppu.artifact.convertor;

import dev.jeppu.artifact.Artifact;
import dev.jeppu.artifact.dto.ArtifactDTO;
import dev.jeppu.wizard.convertor.WizardToWizardDTOConvertor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ArtifactToArtifactDTOConvertor implements Converter<Artifact, ArtifactDTO> {

    private final WizardToWizardDTOConvertor wizardToWizardDTOConvertor;

    @Override
    public ArtifactDTO convert(Artifact artifact) {
        return new ArtifactDTO(
                artifact.getId(),
                artifact.getName(),
                artifact.getDescription(),
                artifact.getImageUrl(),
                artifact.getOwner() != null ? this.wizardToWizardDTOConvertor.convert(artifact.getOwner()) : null);
    }
}
