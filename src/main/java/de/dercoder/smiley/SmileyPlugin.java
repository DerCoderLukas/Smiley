package de.dercoder.smiley;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmileyPlugin extends JavaPlugin {
    @Inject
    private SmileyRepository smileyRepository;
    @Inject
    private PluginManager pluginManager;
    @Inject
    private SmileySessionTrigger smileySessionTrigger;
    @Inject
    private SmileyCommand smileyCommand;

    @Override
    public void onEnable() {
        saveDefaultResources();
        var injector = Guice.createInjector(SmileyModule.withPlugin(this));
        injector.injectMembers(this);
        loadSmileys();
        registerListeners();
        registerCommands();
    }

    private void saveDefaultResources() {
        saveResource("smileys.yml", false);
    }

    private void loadSmileys() {
        smileyRepository.loadAll();
    }

    private void registerListeners() {
        pluginManager.registerEvents(smileySessionTrigger, this);
    }

    private void registerCommands() {
        var smileyPluginCommand = getCommand("smiley");
        smileyPluginCommand.setExecutor(smileyCommand);
    }

    @Override
    public void onDisable() {
        saveSmileys();
    }

    private void saveSmileys() {
        smileyRepository.saveAll();
    }
}
