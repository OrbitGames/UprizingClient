package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;

public class CommandSetSpawnpoint extends CommandBase
{

    public String getCommandName()
    {
        return "spawnpoint";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.spawnpoint.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        EntityPlayerMP var3 = args.length == 0 ? getCommandSenderAsPlayer(sender) : getPlayer(sender, args[0]);

        if (args.length == 4)
        {
            if (var3.worldObj != null)
            {
                byte var4 = 1;
                int var5 = 30000000;
                int var9 = var4 + 1;
                int var6 = parseIntBounded(sender, args[var4], -var5, var5);
                int var7 = parseIntBounded(sender, args[var9++], 0, 256);
                int var8 = parseIntBounded(sender, args[var9++], -var5, var5);
                var3.setSpawnChunk(new ChunkCoordinates(var6, var7, var8), true);
                notifyOperators(sender, this, "commands.spawnpoint.success", var3.getCommandSenderName(), Integer.valueOf(var6), Integer.valueOf(var7), Integer.valueOf(var8));
            }
        }
        else
        {
            if (args.length > 1)
            {
                throw new WrongUsageException("commands.spawnpoint.usage");
            }

            ChunkCoordinates var10 = var3.getPlayerCoordinates();
            var3.setSpawnChunk(var10, true);
            notifyOperators(sender, this, "commands.spawnpoint.success", var3.getCommandSenderName(), Integer.valueOf(var10.posX), Integer.valueOf(var10.posY), Integer.valueOf(var10.posZ));
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length != 1 && args.length != 2 ? null : getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}