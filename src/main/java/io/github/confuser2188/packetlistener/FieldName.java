package io.github.confuser2188.packetlistener;

import org.bukkit.Bukkit;

public enum FieldName {

    CHANNEL("channel");

    private String name;
    FieldName(String name)
    {
        this.name = name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return this.name;
    }

    public static void setup()
    {
        String version = getServerVersion();

        switch(version)
        {
            case "v1_8_R1":
                CHANNEL.setName("i");
                break;
            case "v1_8_R2":
                CHANNEL.setName("k");
                break;
        }
    }

    public static String getServerVersion() {
        try {
            return Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
        } catch (Exception ex) {
            ex.printStackTrace();
            return "null";
        }
    }
}
