package org.libigot;


public class Libigot {

    private static LibigotServer server;
    static LibigotScheduler scheduler;

    /**
     * Returns the LibigotServer instance.
     *
     * @return The LibigotServer.
     */
    public static LibigotServer getServer() {
        return Libigot.server;
    }

    public static void setServer(LibigotServer server) {
        if (server != null) {
            if (Libigot.server == null) {
                Libigot.server = server;
            } else {
                Libigot.server.setDebug(server.getDebug());
            }
        }
    }

    public static LibigotScheduler setScheduler(LibigotScheduler scheduler) {
        if(scheduler != null && Libigot.scheduler == null) {
            Libigot.scheduler = scheduler;
        }
        return Libigot.scheduler;
    }
}
