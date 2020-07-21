package de.dercoder.smiley;

import com.google.common.base.Preconditions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.UUID;

public final class SmileySession {
  private final Player player;
  private final SmileySessionRegistry smileySessionRegistry;

  private boolean smiling;
  private Smiley smiley;
  private int smileTaskId;
  private ItemStack lastHelmet;

  private SmileySession(
    Player player, SmileySessionRegistry smileySessionRegistry
  ) {
    this.player = player;
    this.smileySessionRegistry = smileySessionRegistry;
    this.smiling = false;
  }

  public void smile(Smiley smiley) {
    Preconditions.checkNotNull(smiley);
    this.smiley = smiley;
    smiling = true;
    lastHelmet = player.getInventory().getHelmet();
    processSmile();
  }

  private void processSmile() {
    var iterator = smiley.itemStacks().iterator();
    smileTaskId = Bukkit.getScheduler()
      .scheduleAsyncRepeatingTask(SmileyPlugin.getPlugin(SmileyPlugin.class),
        () -> {
          if (!iterator.hasNext()) {
            reset();
            return;
          }
          smileNext(iterator);
        },
        0,
        smiley.speed()
      );
  }

  private void smileNext(Iterator<ItemStack> iterator) {
    var itemStack = iterator.next();
    player.getInventory().setHelmet(itemStack);
  }

  public void close() {
    reset();
    smileySessionRegistry.unregister(this);
  }

  private void reset() {
    Bukkit.getScheduler().cancelTask(smileTaskId);
    player.getInventory().setHelmet(lastHelmet);
    smiling = false;
  }

  public UUID id() {
    return player.getUniqueId();
  }

  public boolean smiling() {
    return smiling;
  }

  public static SmileySession of(
    Player player, SmileySessionRegistry smileySessionRegistry
  ) {
    Preconditions.checkNotNull(player);
    Preconditions.checkNotNull(smileySessionRegistry);
    return new SmileySession(player, smileySessionRegistry);
  }
}
