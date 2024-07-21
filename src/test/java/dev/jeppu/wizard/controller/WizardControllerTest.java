package dev.jeppu.wizard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jeppu.artifact.Artifact;
import dev.jeppu.system.StatusCode;
import dev.jeppu.system.exception.ObjectNotFoundException;
import dev.jeppu.wizard.Wizard;
import dev.jeppu.wizard.dto.WizardDTO;
import dev.jeppu.wizard.service.WizardService;
import java.util.ArrayList;
import java.util.List;
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

@SpringBootTest
@AutoConfigureMockMvc
class WizardControllerTest {

    @MockBean
    WizardService wizardService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    List<Wizard> wizards;

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

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription(
                "The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription(
                "A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription(
                "A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription(
                "The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");

        this.wizards = new ArrayList<>();

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        w1.addArtifact(a1);
        w1.addArtifact(a3);
        this.wizards.add(w1);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a2);
        w2.addArtifact(a4);
        this.wizards.add(w2);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");
        w3.addArtifact(a5);
        this.wizards.add(w3);
    }

    @Test
    void testDeleteWizardByIdSuccess() throws Exception {
        // given
        BDDMockito.willDoNothing().given(wizardService).deleteWizard(BDDMockito.anyInt());
        // when
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete(this.baseUrl + "/wizards/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Wizard Deleted"));
    }

    @Test
    void testCreateWizard() throws Exception {
        // given
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");

        WizardDTO wizardDTO = new WizardDTO(null, "Albus Dumbledore", 0);

        BDDMockito.given(wizardService.saveWizard(BDDMockito.any(Wizard.class))).willReturn(wizard);
        String json = objectMapper.writeValueAsString(wizardDTO);

        // when
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(this.baseUrl + "/wizards")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Wizard Created"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Albus Dumbledore"));
    }

    @Test
    void testWizardByIdForSuccess() throws Exception {
        // given
        BDDMockito.given(wizardService.getWizardById(BDDMockito.anyInt())).willReturn(this.wizards.get(0));
        // when and then
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(
                                this.baseUrl + "/wizards/" + this.wizards.get(0).getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Wizard found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id")
                        .value(this.wizards.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Albus Dumbledore"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.numberOfArtifacts")
                        .value(2));
    }

    @Test
    void testWizardByIdWhenNotFound() throws Exception {
        // given
        BDDMockito.willThrow(new ObjectNotFoundException(
                        "Wizard", String.valueOf(this.wizards.get(0).getId())))
                .given(wizardService)
                .getWizardById(BDDMockito.anyInt());
        // when and then
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(
                                this.baseUrl + "/wizards/" + this.wizards.get(0).getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Could not find Wizard with Id : "
                                + this.wizards.get(0).getId()));
    }

    @Test
    void testAllWizardsSuccess() throws Exception {
        // given
        BDDMockito.given(wizardService.getAllWizards()).willReturn(this.wizards);

        // when and then
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(this.baseUrl + "/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("All Wizards"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1));
    }
}
