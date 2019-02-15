package io.github.confuser2188.modchecker.minecraft;

import io.github.confuser2188.packetlistener.Reflection;

public class PacketPlayOutResourcePackSend {

    private String url, hash;

    public PacketPlayOutResourcePackSend(String url, String hash){
        this.url = url;
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public String getURL() {
        return url;
    }

    public Object getMinecraftPacket() throws Exception{
        return Reflection.getNMSClass("PacketPlayOutResourcePackSend").getConstructor(String.class, String.class).newInstance(url, hash);
    }
}
