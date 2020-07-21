package de.dercoder.smiley;

import com.google.common.base.Preconditions;

import com.google.inject.Inject;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class SmileySessionTrigger implements Listener {
  private final SmileySessionFactory smileySessionFactory;
  private final SmileySessionRegistry smileySessionRegistry;

  @Inject
  private SmileySessionTrigger(
    SmileySessionFactory smileySessionFactory,
    SmileySessionRegistry smileySessionRegistry
  ) {
    this.smileySessionFactory = smileySessionFactory;
    this.smileySessionRegistry = smileySessionRegistry;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent playerJoin) {
    Preconditions.checkNotNull(playerJoin);
    var player = playerJoin.getPlayer();
    smileySessionFactory.createSmileySession(player);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent playerQuit) {
    Preconditions.checkNotNull(playerQuit);
    var player = playerQuit.getPlayer();
    smileySessionRegistry.find(player.getUniqueId())
      .ifPresent(SmileySession::close);
  }
}
