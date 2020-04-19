package de.dercoder.smiley;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.*;

public final class SmileySessionRegistry {
    private final Set<SmileySession> smileySessions;

    private SmileySessionRegistry(Set<SmileySession> smileySessions) {
        this.smileySessions = smileySessions;
    }

    public void register(SmileySession smileySession) {
        Preconditions.checkNotNull(smileySession);
        smileySessions.add(smileySession);
    }

    public void unregister(SmileySession smileySession) {
        Preconditions.checkNotNull(smileySession);
        smileySessions.remove(smileySession);
    }

    public Optional<SmileySession> find(UUID uuid) {
        Preconditions.checkNotNull(uuid);
        return smileySessions
                .stream()
                .filter(smileySession -> uniqueIdEquality(smileySession.id(), uuid))
                .findFirst();
    }

    private boolean uniqueIdEquality(
            UUID firstUniqueId,
            UUID secondUniqueId
    ) {
        return firstUniqueId.toString()
                .equals(secondUniqueId.toString());
    }

    public Collection<SmileySession> findAll() {
        return List.copyOf(smileySessions);
    }

    public static SmileySessionRegistry empty() {
        return new SmileySessionRegistry(Sets.newHashSet());
    }
}
