package com.daniel.app;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import com.daniel.ui.PrestamosView;
import com.daniel.ui.SociosView;
import com.daniel.ui.LibrosView;
import javafx.application.Application;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        // Crear una ventana (Stage) aparte para el Splash, sin bordes
        Stage splash = new Stage(StageStyle.UNDECORATED);

        // Crear los elementos visuales del Splash
        Label lbl = new Label("Cargando Biblioteca...");
        ProgressIndicator pi = new ProgressIndicator();
        pi.setPrefSize(50, 50);

        // Colocarlos en un contenedor vertical (VBox)
        VBox box = new VBox(12, lbl, pi);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));

        // Crear una escena para el Splash y la mostrarla
        Scene splashScene = new Scene(box, 260, 140);
        splash.setScene(splashScene);
        splash.centerOnScreen();
        splash.show();

        PauseTransition espera = new PauseTransition(Duration.seconds(1.2));
        espera.setOnFinished(ev -> {

            SociosView sociosView = new SociosView();
            LibrosView librosView = new LibrosView();
            PrestamosView prestamosView = new PrestamosView();

            TabPane tabs = new TabPane();

            Tab tabSocios = new Tab("Socios", sociosView.createContent());
            tabSocios.setClosable(false);

            Tab tabLibros = new Tab("Libros", librosView.createContent());
            tabLibros.setClosable(false);

            Tab tabPrestamos = new Tab("Préstamos", prestamosView.createContent());
            tabPrestamos.setClosable(false);

            tabs.getTabs().addAll(tabSocios, tabLibros, tabPrestamos);

            Scene scene = new Scene(tabs, 900, 500);

            stage.setTitle("Biblioteca");
            stage.setScene(scene);
            splash.close();
            stage.centerOnScreen();
            stage.show();
        });
    espera.play();
    }
}