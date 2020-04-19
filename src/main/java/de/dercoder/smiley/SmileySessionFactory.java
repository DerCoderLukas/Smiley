package de.dercoder.smiley;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.bukkit.entity.Player;

import javax.inject.Singleton;

@Singleton
public final class SmileySessionFactory {
    private final SmileySessionRegistry registry;

    @Inject
    private SmileySessionFactory(SmileySessionRegistry registry) {
        this.registry = registry;
    }

    public SmileySession createSmileySession(
            Player player
    ) {
        Preconditions.checkNotNull(player);
        var session = SmileySession.of(player, registry);
        registry.register(session);
        return session;
    }
}
