package dev.jeppu.artifact;

import dev.jeppu.artifact.convertor.ArtifactDTOToArtifactConverter;
import dev.jeppu.artifact.convertor.ArtifactToArtifactDTOConvertor;
import dev.jeppu.artifact.dto.ArtifactDTO;
import dev.jeppu.system.Result;
import dev.jeppu.system.StatusCode;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.endpoint.base_url}/artifacts")
public class ArtifactController {

    private final ArtifactService artifactService;

    private final ArtifactToArtifactDTOConvertor artifactToArtifactDTOConvertor;
    private final ArtifactDTOToArtifactConverter artifactDTOToArtifactConverter;

    @GetMapping(value = "/{artifactId}")
    public Result findArtifactById(@PathVariable("artifactId") String artifactId) {
        Artifact foundArtifact = artifactService.findById(artifactId);
        ArtifactDTO artifactDTO = this.artifactToArtifactDTOConvertor.convert(foundArtifact);
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "Find One Success", artifactDTO);
    }

    @GetMapping
    public Result findAllArtifacts() {
        List<ArtifactDTO> artifactDTOS = artifactService.findAllArtifacts().stream()
                .map(artifactToArtifactDTOConvertor::convert)
                .toList();
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "Find All Success", artifactDTOS);
    }

    @PostMapping
    public Result createArtifact(@Valid @RequestBody ArtifactDTO artifactDTO) {
        Artifact convertedArtifact = artifactDTOToArtifactConverter.convert(artifactDTO);
        Artifact savedArtifact = this.artifactService.saveArtifact(convertedArtifact);
        ArtifactDTO savedArtifactDTO = this.artifactToArtifactDTOConvertor.convert(savedArtifact);

        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "Artifact Created", savedArtifactDTO);
    }

    @PutMapping("/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId, @Valid @RequestBody ArtifactDTO artifactDTO) {
        Artifact convertedArtifact = this.artifactDTOToArtifactConverter.convert(artifactDTO);
        Artifact updatedArtifact = artifactService.updateArtifact(artifactId, convertedArtifact);
        ArtifactDTO updatedArtifactDTO = this.artifactToArtifactDTOConvertor.convert(updatedArtifact);
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "Artifact Updated", updatedArtifactDTO);
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId) {
        artifactService.deleteArtifactById(artifactId);
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "Artifact Deleted");
    }
}
