package io.github.confuser2188.modchecker;

import io.github.confuser2188.modchecker.minecraft.PacketPlayInResourcePackStatus;
import io.github.confuser2188.modchecker.minecraft.PacketPlayOutResourcePackSend;
import io.github.confuser2188.modchecker.minecraft.ResourcePackStatus;
import io.github.confuser2188.packetlistener.Reflection;
import io.github.confuser2188.packetlistener.event.ReceivedPacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class CheckModule implements Listener{

    private final String hash = "MC";
    private ArrayList<Player> punishmentList = new ArrayList<>();
    private ArrayList<Player> checkDone = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        try{
            List<String> blacklist = Main.plugin.getConfig().getStringList("blacklist");

            // Sending too much packets to player is not safe
            if(blacklist.size() > 300)
                throw new Exception("Blacklist is too long!");

            for(String str : blacklist)
                Reflection.sendPacket(event.getPlayer(), new PacketPlayOutResourcePackSend("level://../" + str, hash).getMinecraftPacket());
        }catch(Exception ex) { ex.printStackTrace(); }
    }

    @EventHandler
    public void onPacketReceive(ReceivedPacketEvent event){
        if(checkDone.contains(event.getPlayer())) return;
        try{
            if(event.getPacketName().equals("PacketPlayInResourcePackStatus")){
                PacketPlayInResourcePackStatus packet = new PacketPlayInResourcePackStatus(event.getPacket());

                if(packet.getStatus() == ResourcePackStatus.ACCEPTED){
                    if(packet.getHash() == null || (packet.getHash() != null && packet.getHash().equals(hash)))
                        punishmentList.add(event.getPlayer());
                }
            }else if(event.getPacketName().equals("PacketPlayInKeepAlive")){
                if(punishmentList.contains(event.getPlayer())){
                    punishmentList.remove(event.getPlayer());
                    Bukkit.getScheduler().runTaskLater(Main.plugin, () ->
                    {
                        String cmd = Main.applyColor(Main.plugin.getConfig().getString("punishment")
                                .replace("<player>", event.getPlayer().getName())
                                .replace("\\n", "\n"));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                    }, 20L);
                }else
                    checkDone.add(event.getPlayer());
            }
        }catch(Exception ex) { ex.printStackTrace(); }
    }
}
