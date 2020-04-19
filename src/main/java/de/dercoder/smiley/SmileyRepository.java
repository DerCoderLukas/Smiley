package de.dercoder.smiley;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public final class SmileyRepository {
    private final Set<Smiley> smileys;
    private final ObjectMapper objectMapper;
    private final Path repositoryPath;

    @Inject
    private SmileyRepository(
           ObjectMapper objectMapper,
           @Named("smileysPath") Path repositoryPath
    ) {
        this.smileys = Sets.newHashSet();
        this.objectMapper = objectMapper;
        this.repositoryPath = repositoryPath;
    }

    public void loadAll() {
        var configurationOptional = readConfiguration();
        configurationOptional.ifPresent(this::registerSmileys);
    }

    private Optional<SmileyConfiguration> readConfiguration() {
        try {
            return Optional.of(objectMapper.readValue(
                    repositoryPath.toFile(),
                    SmileyConfiguration.class
            ));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    private void registerSmileys(SmileyConfiguration smileyConfiguration) {
        for(SmileyModel smileyModel : smileyConfiguration.getSmileys()) {
            register(smileyModel.toSmiley());
        }
    }

    public void saveAll() {
        var configuration = buildConfiguration();
        writeConfiguration(configuration);
    }

    private SmileyConfiguration buildConfiguration() {
        var smileys = this.smileys.stream()
                .map(SmileyModel::ofSmiley)
                .collect(Collectors.toList());
        return new SmileyConfiguration(smileys);
    }

    private void writeConfiguration(SmileyConfiguration configuration) {
        try {
            objectMapper.writeValue(
                    repositoryPath.toFile(),
                    configuration
            );
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Optional<Smiley> find(String name) {
        Preconditions.checkNotNull(name);
        return smileys.stream()
                .filter(smiley -> smiley.name().equals(name))
                .findFirst();
    }

    public void register(Smiley smiley) {
        Preconditions.checkNotNull(smiley);
        smileys.add(smiley);
    }

    public void unregister(Smiley smiley) {
        Preconditions.checkNotNull(smiley);
        smileys.remove(smiley);
    }
}
