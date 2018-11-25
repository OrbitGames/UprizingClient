package optifine;

import net.minecraft.client.ClientBrandRetriever;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionCheckThread extends Thread
{
    public void run()
    {
        HttpURLConnection conn = null;

        try
        {
            Config.dbg("Checking for new version");
            URL e = new URL("http://optifine.net/version/1.7.10/HD_U.txt");
            conn = (HttpURLConnection)e.openConnection();

            if (Config.getGameSettings().snooperEnabled)
            {
                conn.setRequestProperty("OF-MC-Version", "1.7.10");
                conn.setRequestProperty("OF-MC-Brand", "" + ClientBrandRetriever.getClientModName());
                conn.setRequestProperty("OF-Edition", "HD_U");
                conn.setRequestProperty("OF-Release", "D4");
                conn.setRequestProperty("OF-Java-Version", "" + System.getProperty("java.version"));
                conn.setRequestProperty("OF-CpuCount", "" + Config.getAvailableProcessors());
                conn.setRequestProperty("OF-OpenGL-Version", "" + Config.openGlVersion);
                conn.setRequestProperty("OF-OpenGL-Vendor", "" + Config.openGlVendor);
            }

            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.connect();

            try
            {
                InputStream in = conn.getInputStream();
                String verStr = Config.readInputStream(in);
                in.close();
                String[] verLines = Config.tokenize(verStr, "\n\r");

                if (verLines.length < 1)
                {
                    return;
                }

                String newVer = verLines[0].trim();
                Config.dbg("Version found: " + newVer);

                if (Config.compareRelease(newVer, "D4") > 0)
                {
                    Config.setNewRelease(newVer);
                    return;
                }
            }
            finally
            {
                if (conn != null)
                {
                    conn.disconnect();
                }
            }
        }
        catch (Exception var11)
        {
            Config.dbg(var11.getClass().getName() + ": " + var11.getMessage());
        }
    }
}