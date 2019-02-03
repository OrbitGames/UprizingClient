package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandSaveOff extends CommandBase
{

    public String getCommandName()
    {
        return "save-off";
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.save-off.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        MinecraftServer var3 = MinecraftServer.getServer();
        boolean var4 = false;

        for (int var5 = 0; var5 < var3.worldServers.length; ++var5)
        {
            if (var3.worldServers[var5] != null)
            {
                WorldServer var6 = var3.worldServers[var5];

                if (!var6.levelSaving)
                {
                    var6.levelSaving = true;
                    var4 = true;
                }
            }
        }

        if (var4)
        {
            notifyOperators(sender, this, "commands.save.disabled");
        }
        else
        {
            throw new CommandException("commands.save-off.alreadyOff");
        }
    }
}