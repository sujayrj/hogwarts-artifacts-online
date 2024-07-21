package dev.jeppu.wizard.service;

import dev.jeppu.system.exception.ObjectNotFoundException;
import dev.jeppu.wizard.Wizard;
import dev.jeppu.wizard.WizardRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {
    @Mock
    WizardRepository wizardRepository;

    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");

        this.wizards = new ArrayList<>();
        this.wizards.add(w1);
        this.wizards.add(w2);
        this.wizards.add(w3);
    }

    @Test
    void testDeleteWizardByIdSuccess() {
        // given
        BDDMockito.given(wizardRepository.findById(BDDMockito.anyInt())).willReturn(Optional.of(this.wizards.get(0)));
        BDDMockito.willDoNothing().given(wizardRepository).deleteById(BDDMockito.anyInt());
        // when
        wizardService.deleteWizard(1);
        // then
        BDDMockito.verify(wizardRepository, BDDMockito.times(1)).deleteById(1);
    }

    @Test
    void testUpdateWizardSuccess() {
        // given
        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");

        Wizard w4 = new Wizard();
        w4.setId(4);
        w4.setName("Neville L");

        BDDMockito.given(wizardRepository.findById(BDDMockito.anyInt())).willReturn(Optional.of(w3));
        BDDMockito.given(wizardRepository.save(BDDMockito.any(Wizard.class))).willReturn(w4);

        // when
        Wizard updatedWizard = wizardService.updateWizard(3, w4);

        // then
        Assertions.assertThat(updatedWizard).isNotNull();
        Assertions.assertThat(updatedWizard.getId()).isEqualTo(4);
        Assertions.assertThat(updatedWizard.getName()).isEqualTo("Neville L");
    }

    @Test
    void testUpdateArtifactWhenArtifactNotFound() {
        // given
        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");

        BDDMockito.given(wizardRepository.findById(BDDMockito.anyInt())).willReturn(Optional.empty());
        // when
        Throwable throwable = Assertions.catchThrowable(() -> this.wizardService.updateWizard(4, w3));
        // then
        Assertions.assertThat(throwable).isInstanceOf(ObjectNotFoundException.class);
        Assertions.assertThat(throwable).hasMessage("Could not find Wizard with Id : 4");
    }

    @Test
    void testCreateNewWizard() {
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");
        // given
        BDDMockito.given(wizardRepository.save(BDDMockito.any(Wizard.class))).willReturn(wizard);
        // when
        Wizard savedWizard = wizardService.saveWizard(wizard);
        // then
        Assertions.assertThat(savedWizard).isNotNull();
        Assertions.assertThat(savedWizard.getId()).isEqualTo(wizard.getId());
        Assertions.assertThat(savedWizard.getName()).isEqualTo(wizard.getName());
    }

    @Test
    void testAllWizardsSuccess() {
        // given
        BDDMockito.given(wizardRepository.findAll()).willReturn(wizards);

        // when
        List<Wizard> allWizards = wizardService.getAllWizards();

        // then
        Assertions.assertThat(allWizards).isNotNull();
        Assertions.assertThat(allWizards.size()).isEqualTo(3);
    }

    @Test
    void testAllWizardsWhenNoArtifactsFound() {
        // give
        BDDMockito.given(wizardRepository.findAll()).willReturn(Collections.emptyList());
        // when
        List<Wizard> allWizards = wizardService.getAllWizards();
        // then
        Assertions.assertThat(allWizards).isNotNull();
        Assertions.assertThat(allWizards.size()).isEqualTo(0);
    }

    @Test
    void testWizardByIdWhenWizardExists() {
        // given
        Wizard givenWizard = new Wizard();
        givenWizard.setId(1);
        givenWizard.setName("Albus Dumbledore");
        givenWizard.setArtifacts(Collections.emptyList());

        BDDMockito.given(wizardRepository.findById(BDDMockito.anyInt())).willReturn(Optional.of(givenWizard));

        // when
        Wizard wizard = wizardService.getWizardById(1);

        // then
        Assertions.assertThat(wizard.getId()).isEqualTo(1);
        Assertions.assertThat(wizard.getName()).isEqualTo("Albus Dumbledore");
        Assertions.assertThat(wizard.getNumberOfArtifacts()).isEqualTo(0);
    }

    @Test
    void testWizardByIdWhenWizardDoesNotExist() {
        // given
        BDDMockito.given(wizardRepository.findById(1111)).willReturn(Optional.empty());
        // when and then
        Throwable throwable = Assertions.catchThrowable(() -> wizardService.getWizardById(1111));
        Assertions.assertThat(throwable).isInstanceOf(ObjectNotFoundException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("Could not find Wizard with Id : 1111");
    }
}
