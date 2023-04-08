package server;

/**
 * interface fonctionnel dans laquelle on implémente la méthode qui
 * servira le client
 */

@FunctionalInterface
public interface EventHandler {
    void handle(String cmd, String arg);
}
