package dev.jeppu.artifact.convertor;

import dev.jeppu.artifact.Artifact;
import dev.jeppu.artifact.dto.ArtifactDTO;
import dev.jeppu.wizard.Wizard;
import dev.jeppu.wizard.convertor.WizardDTOToWizardConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactDTOToArtifactConverter implements Converter<ArtifactDTO, Artifact> {
    private final WizardDTOToWizardConverter wizardDTOToWizardConverter;

    public ArtifactDTOToArtifactConverter(WizardDTOToWizardConverter wizardDTOToWizardConverter) {
        this.wizardDTOToWizardConverter = wizardDTOToWizardConverter;
    }

    @Override
    public Artifact convert(ArtifactDTO source) {
        Wizard wizard = source.owner() != null ? this.wizardDTOToWizardConverter.convert(source.owner()) : null;
        Artifact artifact = new Artifact();
        artifact.setName(source.name());
        artifact.setDescription(source.description());
        artifact.setImageUrl(source.imageUrl());
        artifact.setOwner(wizard);
        return artifact;
    }
}
