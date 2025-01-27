package es.mesacarlos.webconsole.websocket.command;

//------------------------------
//
// This class was developed by Rafael K.
// On 1/8/2022 at 10:22 PM
// In the project WebConsole
//
//------------------------------

import es.mesacarlos.webconsole.util.Internationalization;
import es.mesacarlos.webconsole.websocket.WSServer;
import es.mesacarlos.webconsole.websocket.response.Tps;
import org.java_websocket.WebSocket;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TpsCommand implements WSCommand {
	private static final String mcVer = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    @Override
    public void execute(WSServer wsServer, WebSocket conn, String params) {
        try {
            double tps = getTps()[0];
            wsServer.sendToClient(conn, new Tps(Internationalization.getPhrase("tps-message", tps), tps));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Current server Tps
     */
    public double[] getTps() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Class<?> minecraftServerClass = Class.forName("net.minecraft.server." + mcVer + ".MinecraftServer");
        Method getServerMethod = minecraftServerClass.getDeclaredMethod("getServer");
        Object serverInstance = getServerMethod.invoke(null);
        Field recentTpsField = serverInstance.getClass().getField("recentTps");
        double[] recentTps = (double[]) recentTpsField.get(serverInstance);
        for (int i = 0; i < recentTps.length; i++) {
            recentTps[i] = Math.round(recentTps[i]);
        }
        return recentTps;
    }

}
