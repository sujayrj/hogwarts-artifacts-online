package dev.jeppu.wizard.service;

import dev.jeppu.system.exception.ObjectNotFoundException;
import dev.jeppu.wizard.Wizard;
import dev.jeppu.wizard.WizardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WizardService {

    private final WizardRepository wizardRepository;

    public List<Wizard> getAllWizards() {
        return wizardRepository.findAll();
    }

    public Wizard getWizardById(Integer wizardId) {
        return wizardRepository
                .findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("Wizard", String.valueOf(wizardId)));
    }

    public Wizard saveWizard(Wizard newWizard) {
        return wizardRepository.save(newWizard);
    }

    public Wizard updateWizard(Integer wizardId, Wizard newWizard) {
        return this.wizardRepository
                .findById(wizardId)
                .map(wizard -> {
                    wizard.setName(newWizard.getName());
                    wizard.setArtifacts(newWizard.getArtifacts());
                    return this.wizardRepository.save(wizard);
                })
                .orElseThrow(() -> new ObjectNotFoundException("Wizard", String.valueOf(wizardId)));
    }

    public void deleteWizard(Integer wizardId) {
        Wizard wizard = this.wizardRepository
                .findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("Wizard", String.valueOf(wizardId)));
        wizard.removeAllArtifacts();
        this.wizardRepository.deleteById(wizardId);
    }
}
