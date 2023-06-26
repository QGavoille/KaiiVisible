package fr.ellipsialepoulet.kaiivisible.commands;

import de.bluecolored.bluemap.api.BlueMapAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Visibility implements CommandExecutor, TabCompleter {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    // Check for instance of BlueMapAPI
    if (BlueMapAPI.getInstance().isEmpty()) {
      sender.sendMessage(ChatColor.RED + "BlueMapAPI is not loaded!");
      return true;
    }

    // Get BlueMapAPI instance
    BlueMapAPI api = BlueMapAPI.getInstance().get();

    // Process arguments, command structure is: /visibility [player]? [show|hide]?
    // Check if a player name is provided as first argument
    Player playerArg = args.length > 0 ? sender.getServer().getPlayer(args[0]) : null;
    if (playerArg == null) { // Self case
      if (sender instanceof Player senderPlayer) {
        if (hasSelfPermission(sender)) {
          if (args.length > 0) {
            if (args[0].equalsIgnoreCase("show")) {
              showPlayer(senderPlayer, api);
              sender.sendMessage(ChatColor.GREEN + "You are now visible on BlueMap!");
              return true;
            } else if (args[0].equalsIgnoreCase("hide")) {
              hidePlayer(senderPlayer, api);
              sender.sendMessage(ChatColor.GREEN + "You are now hidden on BlueMap!");
              return true;
            } else {
              sender.sendMessage(ChatColor.RED + "Invalid argument: " + args[1]);
              return false;
            }
          } else {
            if (api.getWebApp().getPlayerVisibility(senderPlayer.getUniqueId())) {
              hidePlayer(senderPlayer, api);
              sender.sendMessage(ChatColor.GREEN + "You are now hidden on BlueMap!");
            } else {
              showPlayer(senderPlayer, api);
              sender.sendMessage(ChatColor.GREEN + "You are now visible on BlueMap!");
            }

            return true;
          }
        } else {
          sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
        }
      }
    } else { // Others case
      if (hasOthersPermission(sender)) {
        if (args.length > 1) {
          if (args[1].equalsIgnoreCase("show")) {
            showPlayer(playerArg, api);
            sender.sendMessage(ChatColor.GREEN + "Player " + playerArg.getName() + " is now visible on BlueMap!");
            return true;
          } else if (args[1].equalsIgnoreCase("hide")) {
            hidePlayer(playerArg, api);
            sender.sendMessage(ChatColor.GREEN + "Player " + playerArg.getName() + " is now hidden on BlueMap!");
            return true;
          } else {
            sender.sendMessage(ChatColor.RED + "Invalid argument: " + args[1]);
            return false;
          }
        } else {
          if (api.getWebApp().getPlayerVisibility(playerArg.getUniqueId())) {
            hidePlayer(playerArg, api);
            sender.sendMessage(ChatColor.GREEN + "Player " + playerArg.getName() + " is now hidden on BlueMap!");
          } else {
            showPlayer(playerArg, api);
            sender.sendMessage(ChatColor.GREEN + "Player " + playerArg.getName() + " is now visible on BlueMap!");
          }
        }
        return true;
      } else {
        sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
        return false;
      }
    }

    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    ArrayList<String> completions = new ArrayList<>();
    if (args.length == 1) {
      if (hasOthersPermission(sender)) {
        for (Player player : sender.getServer().getOnlinePlayers()) {
          completions.add(player.getName());
        }
      }
      completions.add("show");
      completions.add("hide");
    } else if (args.length == 2 && sender.getServer().getPlayer(args[0]) != null) {
      completions.add("show");
      completions.add("hide");
    }

    return completions;
  }

  private static void showPlayer(Player player, BlueMapAPI api) {
    api.getWebApp().setPlayerVisibility(player.getUniqueId(), true);
  }

  private static void hidePlayer(Player player, BlueMapAPI api) {
    api.getWebApp().setPlayerVisibility(player.getUniqueId(), false);
  }

  private static boolean hasSelfPermission(CommandSender sender) {
    return sender.hasPermission("visibility.self");
  }

  private static boolean hasOthersPermission(CommandSender sender) {
    return sender.hasPermission("visibility.others");
  }
}
