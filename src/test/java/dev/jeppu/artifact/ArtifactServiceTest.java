package dev.jeppu.artifact;

import dev.jeppu.artifact.dto.ArtifactDTO;
import dev.jeppu.artifact.util.IdWorker;
import dev.jeppu.wizard.Wizard;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription(
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("imageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("imageUrl");

        this.artifacts = new ArrayList<>();
        this.artifacts.add(a1);
        this.artifacts.add(a2);
    }

    @Test
    void testDeleteArtifactByIdWhenArtifactNotFound() {
        //Given
        BDDMockito.given(artifactRepository.findById("1250808601744904191")).willThrow(new ArtifactNotFoundException("1250808601744904191"));
        //BDDMockito.willDoNothing().given(artifactRepository).deleteById("1250808601744904191");

        //then
        //1st option - using Assertions from Junit
        //org.junit.jupiter.api.Assertions.assertThrows(ArtifactNotFoundException.class, () -> artifactService.deleteArtifactById("1250808601744904191"));

        //2nd option - using Assertions from AssertJ, you can verify the message as well in this case
        Throwable catchThrowable = Assertions.catchThrowable(() -> artifactService.deleteArtifactById("1250808601744904191"));
        Assertions.assertThat(catchThrowable).isInstanceOf(ArtifactNotFoundException.class);
        Assertions.assertThat(catchThrowable).hasMessage("Could not find Artifact with Id 1250808601744904191");
    }

    @Test
    void testDeleteArtifactByIdWhenArtifactExists() {
        //Given
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904191");
        artifact.setName("Deluminator");
        artifact.setDescription(
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        artifact.setImageUrl("imageUrl");

        BDDMockito.given(artifactRepository.findById(BDDMockito.anyString())).willReturn(Optional.of(artifact));
        BDDMockito.willDoNothing().given(artifactRepository).deleteById(artifact.getId());

        //When
        artifactService.deleteArtifactById(artifact.getId());

        //Then
        BDDMockito.verify(artifactRepository, BDDMockito.times(1)).findById("1250808601744904191");
        BDDMockito.verify(artifactRepository, BDDMockito.times(1)).deleteById("1250808601744904191");
    }

    @Test
    void testUpdatedArtifactWhenArtifactIdNotFound() {
        Artifact artifact = new Artifact(
                "1250808601744904191",
                "Deluminator",
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.",
                "imageUrl",
                null);

        BDDMockito.given(artifactRepository.findById(BDDMockito.anyString())).willReturn(Optional.empty());

        Throwable throwable =
                Assertions.catchThrowable(() -> artifactService.updateArtifact("1250808601744904191", artifact));
        Assertions.assertThat(throwable).isInstanceOf(ArtifactNotFoundException.class);

        BDDMockito.verify(artifactRepository, BDDMockito.times(1)).findById(BDDMockito.anyString());
    }

    @Test
    void testUpdatedArtifact() {
        ArtifactDTO artifactDTO = new ArtifactDTO(
                "1250808601744904191",
                "Deluminator",
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.",
                "imageUrl",
                null);

        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904191");
        artifact.setName("Del");
        artifact.setDescription("A Deluminator");
        artifact.setImageUrl("Url");

        Artifact savedArtifact = new Artifact();
        savedArtifact.setId("1250808601744904191");
        savedArtifact.setName("Deluminator");
        savedArtifact.setDescription(
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        savedArtifact.setImageUrl("imageUrl");

        BDDMockito.given(artifactRepository.findById(BDDMockito.anyString())).willReturn(Optional.of(artifact));
        BDDMockito.given(artifactRepository.save(BDDMockito.any(Artifact.class)))
                .willReturn(savedArtifact);

        Artifact updatedArtifact = artifactService.updateArtifact("1250808601744904191", artifact);

        Assertions.assertThat(updatedArtifact).isNotNull();
        Assertions.assertThat(updatedArtifact.getId()).isEqualTo("1250808601744904191");
        Assertions.assertThat(updatedArtifact.getName()).isEqualTo("Deluminator");
        Assertions.assertThat(updatedArtifact.getDescription())
                .isEqualTo(
                        "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        Assertions.assertThat(updatedArtifact.getImageUrl()).isEqualTo("imageUrl");

        BDDMockito.verify(artifactRepository, BDDMockito.times(1)).findById(BDDMockito.anyString());
        BDDMockito.verify(artifactRepository, BDDMockito.times(1)).save(BDDMockito.any(Artifact.class));
    }

    @Test
    void testSaveArtifactSuccess() {
        // Given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Deluminator");
        newArtifact.setDescription(
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        newArtifact.setImageUrl("imageUrl");

        BDDMockito.given(idWorker.nextId()).willReturn(123456L);
        BDDMockito.given(artifactRepository.save(BDDMockito.any(Artifact.class)))
                .willReturn(newArtifact);

        // When
        Artifact savedArtifact = artifactService.saveArtifact(newArtifact);

        // Then
        Assertions.assertThat(savedArtifact.getId()).isEqualTo("123456");
        Assertions.assertThat(savedArtifact.getName()).isEqualTo("Deluminator");
        Assertions.assertThat(savedArtifact.getDescription())
                .isEqualTo(
                        "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        Assertions.assertThat(savedArtifact.getImageUrl()).isEqualTo("imageUrl");

        BDDMockito.verify(artifactRepository, Mockito.times(1)).save(Mockito.any(Artifact.class));
    }

    @Test
    void findAllArtifacts() {
        // Given
        BDDMockito.given(artifactRepository.findAll()).willReturn(this.artifacts);

        // When
        List<Artifact> allArtifacts = artifactService.findAllArtifacts();

        // Assert
        Assertions.assertThat(allArtifacts.size()).isEqualTo(2);
        BDDMockito.verify(artifactRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testFindByArtifact() {
        // Given
        Wizard wizard = new Wizard(2, "Harry Potter");
        Artifact artifact = new Artifact(
                "1250808601744904192",
                "Invisibility Cloak",
                "An invisibility cloak is used to make the wearer invisible",
                "image1",
                wizard);
        wizard.setArtifacts(Arrays.asList(artifact));
        BDDMockito.given(artifactRepository.findById(artifact.getId())).willReturn(Optional.of(artifact));

        // Then
        Artifact actualArtifact = artifactService.findById(artifact.getId());

        // Assert
        Assertions.assertThat(actualArtifact.getId()).isEqualTo(artifact.getId());
        Assertions.assertThat(actualArtifact.getName()).isEqualTo(artifact.getName());
        Assertions.assertThat(actualArtifact.getDescription()).isEqualTo(artifact.getDescription());
        Assertions.assertThat(actualArtifact.getImageUrl()).isEqualTo(artifact.getImageUrl());
        Assertions.assertThat(actualArtifact.getOwner().getId())
                .isEqualTo(artifact.getOwner().getId());
        Assertions.assertThat(actualArtifact.getOwner().getName())
                .isEqualTo(artifact.getOwner().getName());
        Assertions.assertThat(actualArtifact.getOwner().getArtifacts().size()).isEqualTo(1);
    }

    @Test
    void testFindByArtifactNotFound() {
        // Given
        BDDMockito.given(artifactRepository.findById(BDDMockito.anyString())).willReturn(Optional.empty());
        // Then
        Throwable actualThrowable = Assertions.catchThrowable(() -> artifactService.findById("1250808601744904192"));
        // Assert
        Assertions.assertThat(actualThrowable).isInstanceOf(ArtifactNotFoundException.class);
        Assertions.assertThat(actualThrowable.getMessage())
                .isEqualTo("Could not find Artifact with Id 1250808601744904192");
    }
}
