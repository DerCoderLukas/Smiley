package de.dercoder.smiley;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import javax.inject.Singleton;
import java.nio.file.Path;

public final class SmileyModule extends AbstractModule {
    private final SmileyPlugin plugin;

    private SmileyModule(SmileyPlugin plugin) {
        this.plugin = plugin;
    }

    @Provides
    @Singleton
    PluginManager providePluginManager() {
        return Bukkit.getPluginManager();
    }

    @Provides
    @Singleton
    YAMLFactory provideYamlFactory() {
        return YAMLFactory.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build();
    }

    @Provides
    @Singleton
    ObjectMapper provideObjectMapper(YAMLFactory yamlFactory) {
        return new ObjectMapper(yamlFactory);
    }
    
    @Provides
    @Singleton
    SmileySessionRegistry provideSmileySessionRegistry() {
        return SmileySessionRegistry.empty();
    }

    private static final String SMILEYS_REPOSITORY_PATH = "smileys.yml";

    @Provides
    @Singleton
    @Named("smileysPath")
    Path provideSmileysPath() {
        return Path.of(plugin.getDataFolder().getAbsolutePath(), SMILEYS_REPOSITORY_PATH);
    }

    public static SmileyModule withPlugin(SmileyPlugin plugin) {
        return new SmileyModule(plugin);
    }
}
