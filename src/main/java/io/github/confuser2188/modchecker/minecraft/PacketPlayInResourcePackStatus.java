package io.github.confuser2188.modchecker.minecraft;

import io.github.confuser2188.packetlistener.Reflection;

public class PacketPlayInResourcePackStatus {

    private Object packet;

    public PacketPlayInResourcePackStatus(Object packet){
        this.packet = packet;
    }

    public String getHash() {
        try{
            return (String) Reflection.getFieldValue(packet, "a");
        }catch(Exception ex){
            return null;
        }
    }

    public ResourcePackStatus getStatus() throws Exception{
        int enumOrdinal;
        try{
            enumOrdinal = ((Enum)Reflection.getFieldValue(packet, "b")).ordinal();
        }catch(Exception ex){
            enumOrdinal = ((Enum)Reflection.getFieldValue(packet, "status")).ordinal();
        }

        return ResourcePackStatus.values()[enumOrdinal];
    }
}
