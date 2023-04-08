package com.example.client_fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Classe principale du client_fx
 */
public class HelloApplication extends Application {

    /**
     * cette fonction initialise et charge le code de l'interface fx
     *  * contenu dans le fichier "hello-view.fxml"
     * @param stage c'est l'interface principale qui va contenir les éléments graphique du fichier fxml
     * @throws IOException capture toute les erreur liées au chargement de l'interface fxml
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Inscription UDEM!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * fonction principale de la classe qui lance le programme
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}