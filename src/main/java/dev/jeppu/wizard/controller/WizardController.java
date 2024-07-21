package dev.jeppu.wizard.controller;

import dev.jeppu.system.Result;
import dev.jeppu.system.StatusCode;
import dev.jeppu.wizard.Wizard;
import dev.jeppu.wizard.convertor.WizardDTOToWizardConverter;
import dev.jeppu.wizard.convertor.WizardToWizardDTOConvertor;
import dev.jeppu.wizard.dto.WizardDTO;
import dev.jeppu.wizard.service.WizardService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.endpoint.base_url}/wizards")
public class WizardController {

    private final WizardService wizardService;
    private final WizardToWizardDTOConvertor wizardToDTOConvertor;
    private final WizardDTOToWizardConverter wizardDTOToWizardConverter;

    @GetMapping
    public Result getAllWizards() {
        List<WizardDTO> allWizardDTOList = wizardService.getAllWizards().stream()
                .map(wizard -> wizardToDTOConvertor.convert(wizard))
                .collect(Collectors.toUnmodifiableList());
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "All Wizards", allWizardDTOList);
    }

    @GetMapping("/{wizardId}")
    public Result getWizardById(@PathVariable Integer wizardId) {
        Wizard wizard = wizardService.getWizardById(wizardId);
        WizardDTO wizardDTO = wizardToDTOConvertor.convert(wizard);
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "Wizard found", wizardDTO);
    }

    @PostMapping
    public Result createWizard(@RequestBody WizardDTO wizardDTO) {
        Wizard newWizard = this.wizardDTOToWizardConverter.convert(wizardDTO);
        Wizard savedWizard = wizardService.saveWizard(newWizard);
        WizardDTO savedWizardDTO = this.wizardToDTOConvertor.convert(savedWizard);
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "Wizard Created", savedWizardDTO);
    }

    @PutMapping("/{wizardId}")
    public Result updateWizard(@PathVariable Integer wizardId, @RequestBody WizardDTO wizardDTO) {
        Wizard newWizard = this.wizardDTOToWizardConverter.convert(wizardDTO);
        Wizard updatedWizard = this.wizardService.updateWizard(wizardId, newWizard);
        WizardDTO updatedWizardDTO = this.wizardToDTOConvertor.convert(updatedWizard);
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "Wizard Updated", updatedWizardDTO);
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId) {
        this.wizardService.deleteWizard(wizardId);
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "Wizard Deleted");
    }

    @PutMapping("/{wizardId}/artifacts/{artifactId}")
    public Result assignArtifact(@PathVariable Integer wizardId, @PathVariable String artifactId) {
        this.wizardService.assignArtifact(wizardId, artifactId);
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "Artifact Assignment to Success", null);
    }
}
