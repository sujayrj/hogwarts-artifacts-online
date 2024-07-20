package dev.jeppu.artifact.dto;

import dev.jeppu.wizard.dto.WizardDTO;
import jakarta.validation.constraints.NotEmpty;

public record ArtifactDTO(
        String id,
        @NotEmpty(message = "name is required") String name,
        @NotEmpty(message = "description is required") String description,
        @NotEmpty(message = "imageUrl is required") String imageUrl,
        WizardDTO owner) {}
