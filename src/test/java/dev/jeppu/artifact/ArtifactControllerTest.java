package dev.jeppu.artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jeppu.artifact.dto.ArtifactDTO;
import dev.jeppu.artifact.util.IdWorker;
import dev.jeppu.system.StatusCode;
import dev.jeppu.system.exception.ObjectNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
class ArtifactControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;

    List<Artifact> artifacts = new ArrayList<>();

    @Autowired
    private IdWorker idWorker;

    @Value("${api.endpoint.base_url}")
    private String baseUrl;

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription(
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a1);

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");
        this.artifacts.add(a2);

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription(
                "The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");
        this.artifacts.add(a3);

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription(
                "A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");
        this.artifacts.add(a4);

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription(
                "A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");
        this.artifacts.add(a5);

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription(
                "The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");
        this.artifacts.add(a6);
    }

    @Test
    void testArtifactDeleteByIdWhenArtifactDoesNotExists() throws Exception {
        // Given
        BDDMockito.willThrow(new ObjectNotFoundException("Artifact", "1250808601744904196"))
                .given(artifactService)
                .deleteArtifactById("1250808601744904196");

        // then
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/api/v1/artifacts/1250808601744904196")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Could not find Artifact with Id : 1250808601744904196"));
    }

    @Test
    void testArtifactDeleteByIdWhenArtifactExists() throws Exception {
        // Given
        BDDMockito.willDoNothing().given(artifactService).deleteArtifactById("1250808601744904196");

        // then
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete(baseUrl + "/artifacts/1250808601744904196")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Artifact Deleted"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(Boolean.TRUE));
    }

    @Test
    void testUpdateArtifactWhenArtifactIdDoesnotExist() throws Exception {
        ArtifactDTO artifactDTO = new ArtifactDTO(
                "1250808601744904191",
                "Deluminator",
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.",
                "imageUrl",
                null);

        String json = this.objectMapper.writeValueAsString(artifactDTO);

        BDDMockito.given(artifactService.updateArtifact(BDDMockito.anyString(), BDDMockito.any(Artifact.class)))
                .willThrow(new ObjectNotFoundException("Artifact", "1250808601744904191"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.put(baseUrl + "/artifacts/1250808601744904191")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Could not find Artifact with Id : 1250808601744904191"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void testUpdateArtifactIsSuccess() throws Exception {
        ArtifactDTO artifactDTO = new ArtifactDTO(
                "1250808601744904191",
                "Deluminator",
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.",
                "imageUrl",
                null);
        String json = objectMapper.writeValueAsString(artifactDTO);

        Artifact artifact = new Artifact(
                "1250808601744904191",
                "Deluminator",
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.",
                "imageUrl",
                null);

        BDDMockito.given(artifactService.updateArtifact(BDDMockito.anyString(), BDDMockito.any(Artifact.class)))
                .willReturn(artifact);

        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/artifacts/1250808601744904191")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Artifact Updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Deluminator"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data.description")
                                .value(
                                        "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user."));
    }

    @Test
    void testSaveArtifactIsSuccess() throws Exception {
        // given
        ArtifactDTO artifactDTO = new ArtifactDTO(
                null,
                "Resurrection Stone",
                "The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.",
                "ImageUrl",
                null);

        String json = objectMapper.writeValueAsString(artifactDTO);

        Artifact newArtifact = new Artifact();
        newArtifact.setId("1250808601744904199");
        newArtifact.setName("The Marauder's Map");
        newArtifact.setDescription(
                "A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        newArtifact.setImageUrl("ImageUrl");

        BDDMockito.given(artifactService.saveArtifact(BDDMockito.any(Artifact.class)))
                .willReturn(newArtifact);

        // when & then
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(baseUrl + "/artifacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Artifact Created"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(newArtifact.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(newArtifact.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.imageUrl").value(newArtifact.getImageUrl()));
    }

    @Test
    void testFindAllArtifacts() throws Exception {
        // Given
        BDDMockito.given(artifactService.findAllArtifacts()).willReturn(artifacts);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Find All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value("1250808601744904191"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("Deluminator"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[0].description")
                                .value(
                                        "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].imageUrl").value("ImageUrl"));
    }

    @Test
    void testFindByIdWhenArtifactExists() throws Exception {
        // Given
        BDDMockito.given(artifactService.findById("1250808601744904196")).willReturn(artifacts.get(5));
        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/artifacts/1250808601744904196")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Find One Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id")
                        .value(artifacts.get(5).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name")
                        .value(artifacts.get(5).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description")
                        .value(artifacts.get(5).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.imageUrl")
                        .value(artifacts.get(5).getImageUrl()));
    }

    @Test
    void testFindByIdWhenArtifactNotFound() throws Exception {
        // Given
        BDDMockito.given(artifactService.findById("1250808601744904196"))
                .willThrow(new ObjectNotFoundException("Artifact", "1250808601744904196"));
        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/artifacts/1250808601744904196")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Could not find Artifact with Id : 1250808601744904196"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }
}
