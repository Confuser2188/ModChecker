package io.github.confuser2188.packetlistener;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflection {

    public static Object getEntityPlayer(Player p) {
        try{
            Method getHandle = p.getClass().getMethod("getHandle");
            return getHandle.invoke(p);
        }catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Object getWorldServer(World world) {
        try{
            Method getHandle = world.getClass().getMethod("getHandle");
            return getHandle.invoke(world);
        }catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Class<?> getNMSClass(String className) {
        try {
            return Class.forName("net.minecraft.server." + FieldName.getServerVersion() + "." + className);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Object getSuperFieldValue(Object instance, String fieldName) throws Exception {
        Field field = instance.getClass().getSuperclass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    public static Object getFieldValue(Object instance, String fieldName) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    public static void setFieldValue(Object instance, String fieldName, Object newObj) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, newObj);
    }

    public static <T> T getFieldValue(Field field, Object obj) {
        try {
            return (T) field.get(obj);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Field getField(Class<?> clazz, String fieldName) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static void sendPacket(Player p, Object packet) throws  Exception{
        Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
        Object plrConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
        plrConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(plrConnection, packet);
    }
}
