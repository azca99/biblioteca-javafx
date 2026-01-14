package com.daniel.app;

import com.daniel.ui.PrestamosView;
import com.daniel.ui.SociosView;
import com.daniel.ui.LibrosView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

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
        stage.show();
    }
}