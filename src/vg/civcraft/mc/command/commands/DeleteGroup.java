package vg.civcraft.mc.command.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vg.civcraft.mc.GroupManager.PlayerType;
import vg.civcraft.mc.NameAPI;
import vg.civcraft.mc.command.PlayerCommand;
import vg.civcraft.mc.group.Group;
import vg.civcraft.mc.permission.GroupPermission;
import vg.civcraft.mc.permission.PermissionType;

public class DeleteGroup extends PlayerCommand{

	public DeleteGroup(String name) {
		super(name);
		setDescription("This command is used to delete a group.");
		setUsage("/groupsdeletegroup <group>");
		setIdentifier("groupsdeletegroup");
		setArguments(1,1);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)){
			sender.sendMessage(ChatColor.DARK_AQUA + "I grow tired of this, NO.");
			return true;
		}
		Player p = (Player) sender;
		String x = args[0];
		Group g = gm.getGroup(x);
		if (g == null){
			p.sendMessage(ChatColor.RED + "That group does not exist.");
			return true;
		}
		UUID uuid = NameAPI.getUUID(p.getName());
		PlayerType pType = g.getPlayerType(uuid);
		if (pType == null){
			p.sendMessage(ChatColor.RED + "You are not on that group.");
			return true;
		}
		if (g.isDisiplined()){
			p.sendMessage(ChatColor.RED + "Group is disiplined.");
			return true;
		}
		GroupPermission gPerm = gm.getPermissionforGroup(g);
		if (!gPerm.isAccessible(PermissionType.DELETE, pType) && !(p.isOp() || p.hasPermission("namelayer.admin"))){
			p.sendMessage(ChatColor.RED + "You do not have permission to run that command.");
			return true;
		}
		if(gm.deleteGroup(g.getName()))
			p.sendMessage(ChatColor.GREEN + "Group was successfully deleted.");
		else
			p.sendMessage(ChatColor.GREEN + "Group was unable to be deleted.");
		return true;
	}

}