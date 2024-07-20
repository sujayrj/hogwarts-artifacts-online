package dev.jeppu.wizard.convertor;

import dev.jeppu.wizard.Wizard;
import dev.jeppu.wizard.dto.WizardDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardToWizardDTOConvertor implements Converter<Wizard, WizardDTO> {
    @Override
    public WizardDTO convert(Wizard source) {
        return new WizardDTO(source.getId(), source.getName(), source.getNumberOfArtifacts());
    }
}
