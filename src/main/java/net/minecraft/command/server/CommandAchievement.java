package net.minecraft.command.server;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

public class CommandAchievement extends CommandBase
{

    public String getCommandName()
    {
        return "achievement";
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
        return "commands.achievement.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length >= 2)
        {
            StatBase var3 = StatList.getOneShotStat(args[1]);

            if (var3 == null && !args[1].equals("*"))
            {
                throw new CommandException("commands.achievement.unknownAchievement", args[1]);
            }

            EntityPlayerMP var4;

            if (args.length >= 3)
            {
                var4 = getPlayer(sender, args[2]);
            }
            else
            {
                var4 = getCommandSenderAsPlayer(sender);
            }

            if (args[0].equalsIgnoreCase("give"))
            {
                if (var3 == null)
                {
                    Iterator var5 = AchievementList.achievementList.iterator();

                    while (var5.hasNext())
                    {
                        Achievement var6 = (Achievement)var5.next();
                        var4.triggerAchievement(var6);
                    }

                    notifyOperators(sender, this, "commands.achievement.give.success.all", var4.getCommandSenderName());
                }
                else
                {
                    if (var3 instanceof Achievement)
                    {
                        Achievement var9 = (Achievement)var3;
                        ArrayList var10;

                        for (var10 = Lists.newArrayList(); var9.parentAchievement != null && !var4.getStatFile().hasAchievementUnlocked(var9.parentAchievement); var9 = var9.parentAchievement)
                        {
                            var10.add(var9.parentAchievement);
                        }

                        Iterator var7 = Lists.reverse(var10).iterator();

                        while (var7.hasNext())
                        {
                            Achievement var8 = (Achievement)var7.next();
                            var4.triggerAchievement(var8);
                        }
                    }

                    var4.triggerAchievement(var3);
                    notifyOperators(sender, this, "commands.achievement.give.success.one", var4.getCommandSenderName(), var3.func_150955_j());
                }

                return;
            }
        }

        throw new WrongUsageException("commands.achievement.usage");
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "give");
        }
        else if (args.length != 2)
        {
            return args.length == 3 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
        }
        else
        {
            ArrayList var3 = Lists.newArrayList();
            Iterator var4 = StatList.allStats.iterator();

            while (var4.hasNext())
            {
                StatBase var5 = (StatBase)var4.next();
                var3.add(var5.statId);
            }

            return getListOfStringsFromIterableMatchingLastWord(args, var3);
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 2;
    }
}