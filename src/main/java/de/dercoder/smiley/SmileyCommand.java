package de.dercoder.smiley;

import com.google.inject.Inject;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SmileyCommand implements CommandExecutor {
  private final SmileyRepository smileyRepository;
  private final SmileySessionRegistry smileySessionRegistry;

  @Inject
  private SmileyCommand(
    SmileyRepository smileyRepository,
    SmileySessionRegistry smileySessionRegistry
  ) {
    this.smileyRepository = smileyRepository;
    this.smileySessionRegistry = smileySessionRegistry;
  }

  private static final String SMILEY_COMMAND_PERMISSION = "smiley.command";

  @Override
  public boolean onCommand(
    CommandSender commandSender,
    Command command,
    String label,
    String[] arguments
  ) {
    if (!(commandSender instanceof Player)) {
      commandSender.sendMessage("You need to be a player.");
      return false;
    }
    if (!commandSender.hasPermission(SMILEY_COMMAND_PERMISSION)) {
      commandSender.sendMessage(command.getPermissionMessage());
      return false;
    }
    if (arguments.length == 1) {
      return smile(commandSender, arguments);
    } else {
      commandSender.sendMessage("Syntax: /smiley <name>");
      return false;
    }
  }

  private boolean smile(
    CommandSender commandSender, String[] arguments
  ) {
    var smileyName = arguments[0];
    var smileyOptional = smileyRepository.find(smileyName);
    smileyOptional.ifPresentOrElse(smiley -> {
      var player = (Player) commandSender;
      smileySessionRegistry.find(player.getUniqueId())
        .ifPresent(smileySession -> {
          if (smileySession.smiling()) {
            commandSender.sendMessage("You are already smiling");
            return;
          }
          smileySession.smile(smiley);
          commandSender.sendMessage("You are now smiling with the smiley " + smileyName);
        });
    }, () -> {
      commandSender.sendMessage("Can't find smiley " + smileyName);
    });
    return true;
  }
}
