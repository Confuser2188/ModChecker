package io.github.confuser2188.packetlistener;

import io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Field;

public class PacketListener implements Listener{

    private Field playerConnection;
    private Field networkManager;
    private Field channel;

    public PacketListener() {
        // Set up field names for reflection
        FieldName.setup();
        try {
            playerConnection = Reflection.getField(Reflection.getNMSClass("EntityPlayer"), "playerConnection");

            Class<?> PlayerConnection = Reflection.getNMSClass("PlayerConnection");
            networkManager = Reflection.getField(PlayerConnection, "networkManager");

            Class<?> NetworkManager = Reflection.getNMSClass("NetworkManager");
            channel = Reflection.getField(NetworkManager, FieldName.CHANNEL.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Channel getChannel(Object NetworkManager) throws Exception {
        return (Channel) Reflection.getFieldValue(channel, NetworkManager);
    }

    private Object getNetworkManager(Player p) {
        Object entityPlayer = Reflection.getEntityPlayer(p);

        Object s =  Reflection.getFieldValue(playerConnection, entityPlayer);
        return Reflection.getFieldValue(networkManager, s);
    }

    public void addPlayer(Player p) {
        try {
            Channel ch = getChannel(getNetworkManager(p));
            if(ch.pipeline().get("MCListener") == null) {
                PacketHandler h = new PacketHandler(p);
                ch.pipeline().addBefore("packet_handler", "MCListener", h);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void removePlayer(Player p) {
        try {
            Channel ch = getChannel(getNetworkManager(p));
            if(ch.pipeline().get("MCListener") != null) {
                ch.pipeline().remove("MCListener");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // Bukkit events
    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        addPlayer(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        if(e.isAsynchronous())
            removePlayer(e.getPlayer());
    }
}
