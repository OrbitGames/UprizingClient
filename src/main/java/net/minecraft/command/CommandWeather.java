package net.minecraft.command;

import java.util.List;
import java.util.Random;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandWeather extends CommandBase
{
    private static final String __OBFID = "CL_00001185";

    public String getCommandName()
    {
        return "weather";
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
        return "commands.weather.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length >= 1 && args.length <= 2)
        {
            int var3 = (300 + (new Random()).nextInt(600)) * 20;

            if (args.length >= 2)
            {
                var3 = parseIntBounded(sender, args[1], 1, 1000000) * 20;
            }

            WorldServer var4 = MinecraftServer.getServer().worldServers[0];
            WorldInfo var5 = var4.getWorldInfo();

            if ("clear".equalsIgnoreCase(args[0]))
            {
                var5.setRainTime(0);
                var5.setThunderTime(0);
                var5.setRaining(false);
                var5.setThundering(false);
                notifyOperators(sender, this, "commands.weather.clear");
            }
            else if ("rain".equalsIgnoreCase(args[0]))
            {
                var5.setRainTime(var3);
                var5.setRaining(true);
                var5.setThundering(false);
                notifyOperators(sender, this, "commands.weather.rain");
            }
            else
            {
                if (!"thunder".equalsIgnoreCase(args[0]))
                {
                    throw new WrongUsageException("commands.weather.usage");
                }

                var5.setRainTime(var3);
                var5.setThunderTime(var3);
                var5.setRaining(true);
                var5.setThundering(true);
                notifyOperators(sender, this, "commands.weather.thunder");
            }
        }
        else
        {
            throw new WrongUsageException("commands.weather.usage");
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "clear", "rain", "thunder"): null;
    }
}