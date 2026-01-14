package com.daniel.ui;

import com.daniel.dao.LibroDAO;
import com.daniel.model.Libro;
import com.daniel.util.AppLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class LibrosView {

    private final LibroDAO libroDAO = new LibroDAO();

    private final TableView<Libro> tabla = new TableView<>();
    private final ObservableList<Libro> datos = FXCollections.observableArrayList();

    // Botones
    private final Button btnNuevo = new Button("Nuevo");
    private final Button btnEditar = new Button("Editar");
    private final Button btnEliminar = new Button("Eliminar");
    private final Button btnRefrescar = new Button("Refrescar");
    private final Button btnVerLog = new Button("Ver log");

    // Campos de búsqueda (arriba)
    private final TextField txtBuscarTitulo = new TextField();
    private final TextField txtBuscarAutor  = new TextField();

    // Barra de estado (abajo)
    private final Label lblEstado = new Label("Listo");

    private void setEstado(String msg) {
        lblEstado.setText(msg);
    }

    // Métode refrescar tabla
    private void refrescarTabla() {
        datos.setAll(libroDAO.findAll());
        setEstado("Lista actualizada (" + datos.size() + " registros)");
    }

    // Métode nuevo libro
    private void mostrarDialogoNuevoLibro() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo libro");
        dialog.setHeaderText("Introduce los datos del libro");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        // Campos
        TextField txtTitulo = new TextField();
        TextField txtIdioma = new TextField();
        TextField txtPaginas = new TextField();
        TextField txtIsbn = new TextField();
        TextField txtEditorial = new TextField();
        TextField txtCategoria = new TextField();
        DatePicker dpFecha = new DatePicker();
        CheckBox chkDisponible = new CheckBox("Disponible");
        chkDisponible.setSelected(true);

        // Layout del formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("Título:"), 0, 0);
        grid.add(txtTitulo, 1, 0);

        grid.add(new Label("Fecha publicación:"), 0, 1);
        grid.add(dpFecha, 1, 1);

        grid.add(new Label("Idioma:"), 0, 2);
        grid.add(txtIdioma, 1, 2);

        grid.add(new Label("Nº páginas:"), 0, 3);
        grid.add(txtPaginas, 1, 3);

        grid.add(new Label("ISBN:"), 0, 4);
        grid.add(txtIsbn, 1, 4);

        grid.add(new Label("Editorial:"), 0, 5);
        grid.add(txtEditorial, 1, 5);

        grid.add(new Label("Categoría:"), 0, 6);
        grid.add(txtCategoria, 1, 6);

        grid.add(chkDisponible, 1, 7);

        // Poner el contenido en el interior del diálogo
        dialog.getDialogPane().setContent(grid);

        // Acción guardar: Mostrar y esperar respuesta
        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnGuardar) {

                // Validación mínima
                String titulo = txtTitulo.getText().trim();
                if (titulo.isEmpty()) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incompletos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("El título es obligatorio.");
                    aviso.showAndWait();
                    setEstado("Validación: faltan campos obligatorios");
                    return;
                }

                String idioma = txtIdioma.getText().trim().toUpperCase();

                if (idioma.isEmpty()) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incompletos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("El idioma es obligatorio (ES o EN).");
                    aviso.showAndWait();
                    setEstado("Validación: idioma obligatorio");
                    return;
                }

                if (!idioma.equals("ES") && !idioma.equals("EN") && !idioma.equals("FR") && !idioma.equals("DE") && !idioma.equals("IT") && !idioma.equals("PT") && !idioma.equals("OTRO")) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incorrectos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("Idioma debe ser ES, EN, FR, DE, IT, PT u OTRO.");
                    aviso.showAndWait();
                    setEstado("Validación: idioma inválido");
                    return;
                }

                String paginasTxt = txtPaginas.getText().trim();
                if (!paginasTxt.matches("\\d+")) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incorrectos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("El número de páginas debe ser un número.");
                    aviso.showAndWait();
                    setEstado("Validación: páginas inválidas");
                    return;
                }

                // Páginas debe ser mayor que 0
                int paginas = Integer.parseInt(paginasTxt);
                if (paginas <= 0) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incorrectos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("El número de páginas debe ser mayor que 0.");
                    aviso.showAndWait();
                    setEstado("Validación: páginas <= 0");
                    return;
                }

                String isbn = txtIsbn.getText().trim();
                if (isbn.isEmpty()) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incompletos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("El ISBN es obligatorio.");
                    aviso.showAndWait();
                    setEstado("Validación: ISBN obligatorio");
                    return;
                }

                if (libroDAO.existsIsbn(isbn)) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("ISBN duplicado");
                    aviso.setHeaderText(null);
                    aviso.setContentText("Ya existe un libro con ese ISBN.");
                    aviso.showAndWait();
                    setEstado("Validación: ISBN duplicado");
                    return;
                }

                String nombreEditorial = txtEditorial.getText().trim();
                if (nombreEditorial.isEmpty()) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incompletos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("La editorial es obligatoria (escribe su nombre).");
                    aviso.showAndWait();
                    setEstado("Validación: editorial inválida");
                    return;
                }

                String nombreCategoria = txtCategoria.getText().trim();
                if (nombreCategoria.isEmpty()) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incompletos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("La categoría es obligatoria (escribe su nombre).");
                    aviso.showAndWait();
                    setEstado("Validación: categoría inválida");
                    return;
                }

                // Convertir nombres -> IDs (consultando la BD)
                Integer idEditorial = libroDAO.findEditorialIdByNombre(nombreEditorial);
                if (idEditorial == null) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Editorial no encontrada");
                    aviso.setHeaderText(null);
                    aviso.setContentText("No existe la editorial: " + nombreEditorial);
                    aviso.showAndWait();
                    setEstado("Validación: editorial inválida");
                    return;
                }

                Integer idCategoria = libroDAO.findCategoriaIdByNombre(nombreCategoria);
                if (idCategoria == null) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Categoría no encontrada");
                    aviso.setHeaderText(null);
                    aviso.setContentText("No existe la categoría: " + nombreCategoria);
                    aviso.showAndWait();
                    setEstado("Validación: categoría inválida");
                    return;
                }

                // Crear el objeto Libro con lo que el usuario escribió
                Libro nuevo = new Libro();
                nuevo.setTitulo(titulo);
                nuevo.setFechaPublicacion(dpFecha.getValue());
                nuevo.setIdioma(idioma);
                nuevo.setNumeroPaginas(paginas);
                nuevo.setIsbn(isbn);
                nuevo.setDisponible(chkDisponible.isSelected());
                nuevo.setIdEditorial(idEditorial);
                nuevo.setIdCategoria(idCategoria);

                // 4) Insertar en BD y refrescar
                int idGenerado = libroDAO.insert(nuevo);

                if (idGenerado > 0) {
                    refrescarTabla();
                    setEstado("Creado correctamente (ID " + idGenerado + ")");
                    AppLog.info("LIBROS/INSERT", "Creado libro ID=" + idGenerado + " Titulo=\"" + nuevo.getTitulo() + "\"");
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText(null);
                    error.setContentText("No se pudo insertar el libro.");
                    error.showAndWait();
                    setEstado("Error al crear");
                }
            } else {
                setEstado("Acción cancelada");
            }
        });

    }

    // Métode para controlar que el libro está seleccionado antes de editar
    private void editarLibroSeleccionado() {
        Libro l = tabla.getSelectionModel().getSelectedItem();
        if (l == null) return;
        mostrarDialogoEditarLibro(l);
    }

    // Métode editar Libro
    private void mostrarDialogoEditarLibro(Libro libro) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar libro");
        dialog.setHeaderText("Modifica los datos del libro");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        // Campos (Iguales que en Nuevo)
        TextField txtTitulo = new TextField(libro.getTitulo());
        TextField txtIdioma = new TextField(libro.getIdioma());
        TextField txtPaginas = new TextField(String.valueOf(libro.getNumeroPaginas()));
        TextField txtIsbn = new TextField(libro.getIsbn());
        TextField txtEditorial = new TextField(libro.getEditorialNombre());
        TextField txtCategoria = new TextField(libro.getCategoriaNombre());
        DatePicker dpFecha = new DatePicker(libro.getFechaPublicacion());
        CheckBox chkDisponible = new CheckBox("Disponible");
        chkDisponible.setSelected(libro.isDisponible());

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("Título:"), 0, 0);
        grid.add(txtTitulo, 1, 0);

        grid.add(new Label("Fecha publicación:"), 0, 1);
        grid.add(dpFecha, 1, 1);

        grid.add(new Label("Idioma:"), 0, 2);
        grid.add(txtIdioma, 1, 2);

        grid.add(new Label("Nº páginas:"), 0, 3);
        grid.add(txtPaginas, 1, 3);

        grid.add(new Label("ISBN:"), 0, 4);
        grid.add(txtIsbn, 1, 4);

        // CAMBIO: etiquetas ya no dicen "ID ..."
        grid.add(new Label("Editorial:"), 0, 5);
        grid.add(txtEditorial, 1, 5);

        grid.add(new Label("Categoría:"), 0, 6);
        grid.add(txtCategoria, 1, 6);

        grid.add(chkDisponible, 1, 7);

        // Poner el contenido en el interior del diálogo
        dialog.getDialogPane().setContent(grid);

        // Acción Guardar
        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnGuardar) {

                // Validación mínima
                String titulo = txtTitulo.getText().trim();
                if (titulo.isEmpty()) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incompletos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("El título es obligatorio.");
                    aviso.showAndWait();
                    setEstado("Validación: faltan campos obligatorios");
                    return;
                }

                String idioma = txtIdioma.getText().trim().toUpperCase();

                if (idioma.isEmpty()) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incompletos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("El idioma es obligatorio (ES o EN).");
                    aviso.showAndWait();
                    setEstado("Validación: idioma obligatorio");
                    return;
                }

                if (!idioma.equals("ES") && !idioma.equals("EN") && !idioma.equals("FR") && !idioma.equals("DE") && !idioma.equals("IT") && !idioma.equals("PT") && !idioma.equals("OTRO")) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incorrectos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("Idioma debe ser ES, EN, FR, DE, IT, PT u OTRO.");
                    aviso.showAndWait();
                    setEstado("Validación: idioma inválido");
                    return;
                }

                String isbn = txtIsbn.getText().trim();
                if (isbn.isEmpty()) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incompletos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("El ISBN es obligatorio.");
                    aviso.showAndWait();
                    setEstado("Validación: ISBN obligatorio");
                    return;
                }

                String paginasTxt = txtPaginas.getText().trim();
                if (!paginasTxt.matches("\\d+")) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incorrectos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("El número de páginas debe ser un número.");
                    aviso.showAndWait();
                    setEstado("Validación: páginas inválidas");
                    return;
                }

                String nombreEditorial = txtEditorial.getText().trim();
                if (nombreEditorial.isEmpty()) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incompletos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("La editorial es obligatoria (escribe su nombre).");
                    aviso.showAndWait();
                    setEstado("Validación: editorial inválida");
                    return;
                }

                String nombreCategoria = txtCategoria.getText().trim();
                if (nombreCategoria.isEmpty()) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incompletos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("La categoría es obligatoria (escribe su nombre).");
                    aviso.showAndWait();
                    setEstado("Validación: categoría inválida");
                    return;
                }

                // Convertir nombres -> IDs (consultando la BD)
                Integer idEditorial = libroDAO.findEditorialIdByNombre(nombreEditorial);
                if (idEditorial == null) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Editorial no encontrada");
                    aviso.setHeaderText(null);
                    aviso.setContentText("No existe la editorial: " + nombreEditorial);
                    aviso.showAndWait();
                    setEstado("Validación: editorial inválida");
                    return;
                }

                Integer idCategoria = libroDAO.findCategoriaIdByNombre(nombreCategoria);
                if (idCategoria == null) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Categoría no encontrada");
                    aviso.setHeaderText(null);
                    aviso.setContentText("No existe la categoría: " + nombreCategoria);
                    aviso.showAndWait();
                    setEstado("Validación: categoría inválida");
                    return;
                }

                // Actualizar el objeto existente
                libro.setTitulo(titulo);
                libro.setFechaPublicacion(dpFecha.getValue());
                libro.setIdioma(idioma);
                libro.setNumeroPaginas(Integer.parseInt(paginasTxt));
                libro.setIsbn(isbn);
                libro.setDisponible(chkDisponible.isSelected());

                // Guardar IDs reales en BD
                libro.setIdEditorial(idEditorial);
                libro.setIdCategoria(idCategoria);

                // (Útil para que la tabla muestre el texto correcto)
                libro.setEditorialNombre(nombreEditorial);
                libro.setCategoriaNombre(nombreCategoria);

                boolean ok = libroDAO.update(libro);

                if (ok) {
                    refrescarTabla();
                    setEstado("Actualizado correctamente");
                    AppLog.info("LIBROS/UPDATE", "Actualizado libro ID=" + libro.getIdLibro() + " Titulo=\"" + libro.getTitulo() + "\"");
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText(null);
                    error.setContentText("No se pudo actualizar el libro.");
                    error.showAndWait();
                    setEstado("No se pudo actualizar");
                }
            } else {
                setEstado("Acción cancelada");
            }
        });
    }

    // Métode para eliminar libro
    private void eliminarLibroSeleccionado() {

        Libro l = tabla.getSelectionModel().getSelectedItem();
        if (l == null) return;

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Seguro que quieres eliminar el libro:\n\n" + l.getTitulo(),
                ButtonType.OK,
                ButtonType.CANCEL
        );

        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                libroDAO.deleteById(l.getIdLibro());
                refrescarTabla();
                setEstado("Eliminado correctamente");
                AppLog.info("LIBROS/DELETE", "Eliminado libro ID=" + l.getIdLibro() + " Titulo=\"" + l.getTitulo() + "\"");
            } else {
                setEstado("Eliminación cancelada");
            }
        });
    }

    // Construir la interfaz
    public Parent createContent() {

        // Columnas
        TableColumn<Libro, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idLibro"));

        TableColumn<Libro, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<Libro, String> colIdioma = new TableColumn<>("Idioma");
        colIdioma.setCellValueFactory(new PropertyValueFactory<>("idioma"));

        TableColumn<Libro, Integer> colPaginas = new TableColumn<>("Páginas");
        colPaginas.setCellValueFactory(new PropertyValueFactory<>("numeroPaginas"));

        TableColumn<Libro, Boolean> colDisponible = new TableColumn<>("Disponible");
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        TableColumn<Libro, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaPublicacion"));

        TableColumn<Libro, String> colEditorial = new TableColumn<>("Editorial");
        colEditorial.setCellValueFactory(new PropertyValueFactory<>("editorialNombre"));

        TableColumn<Libro, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoriaNombre"));

        tabla.getColumns().addAll(
                colId, colTitulo, colIdioma, colPaginas, colDisponible, colFecha, colEditorial, colCategoria
        );

        // Cargar datos desde BD
        refrescarTabla();
        tabla.setItems(datos);

        // Barra HBox. Espaciado y Padding 10px
        HBox barraBotones = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnRefrescar, btnVerLog);
        barraBotones.setPadding(new Insets(10));

        // Barra de búsqueda (hasta 2 campos)
        txtBuscarTitulo.setPromptText("Buscar por título...");
        txtBuscarAutor.setPromptText("Buscar por autor (nombre o apellidos)...");

        Button btnBuscar = new Button("Buscar");
        Button btnLimpiar = new Button("Limpiar");

        HBox barraBusqueda = new HBox(10, txtBuscarTitulo, txtBuscarAutor, btnBuscar, btnLimpiar);
        barraBusqueda.setPadding(new Insets(10));

        // Barra de estado (footer)
        HBox barraEstado = new HBox(lblEstado);
        barraEstado.setPadding(new Insets(6, 10, 6, 10));
        barraEstado.setStyle("-fx-background-color: #f2f2f2; -fx-border-color: #d0d0d0;");

        // Deshabilitar botones que requieren selección
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);

        // Detectar selección de fila y habilitar botones
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, ant, sel) -> {
            boolean haySeleccion = sel != null;
            btnEditar.setDisable(!haySeleccion);
            btnEliminar.setDisable(!haySeleccion);
        });

        // Funciones de los botones
        btnRefrescar.setOnAction(e -> refrescarTabla());
        btnNuevo.setOnAction(e -> mostrarDialogoNuevoLibro());
        btnEditar.setOnAction(e -> editarLibroSeleccionado());
        btnEliminar.setOnAction(e -> eliminarLibroSeleccionado());

        // Función de VerLog
        btnVerLog.setOnAction(e -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Log de operaciones");
            a.setHeaderText(null);

            TextArea ta = new TextArea(AppLog.readAll());
            ta.setEditable(false);
            ta.setWrapText(false);
            ta.setPrefWidth(800);
            ta.setPrefHeight(500);

            a.getDialogPane().setContent(ta);
            a.showAndWait();
        });

        // Funciones de búsqueda
        btnBuscar.setOnAction(e -> {
            String titulo = txtBuscarTitulo.getText();
            String autor  = txtBuscarAutor.getText();

            boolean tituloVacio = (titulo == null || titulo.isBlank());
            boolean autorVacio  = (autor == null || autor.isBlank());

            if (tituloVacio && autorVacio) {
                refrescarTabla();
                return;
            }

            datos.setAll(libroDAO.findByFilters(titulo, autor));
            setEstado("Búsqueda realizada (" + datos.size() + " resultados)");
        });

        btnLimpiar.setOnAction(e -> {
            txtBuscarTitulo.clear();
            txtBuscarAutor.clear();
            refrescarTabla();
            setEstado("Filtros limpiados");
        });

        // Layout donde se encuentra la tabla
        BorderPane root = new BorderPane();

        // Dos filas arriba: búsqueda + botones
        VBox top = new VBox(barraBusqueda, barraBotones);
        root.setTop(top);

        root.setCenter(tabla);

        // Layout barra de estado
        root.setBottom(barraEstado);

        // Devolver la interfaz completa
        return root;
    }
}
