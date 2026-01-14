package com.daniel.ui;

import com.daniel.dao.PrestamoDAO;
import com.daniel.model.Prestamo;
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

import java.math.BigDecimal;
import java.time.LocalDate;

public class PrestamosView {

    private final PrestamoDAO prestamoDAO = new PrestamoDAO();

    private final TableView<Prestamo> tabla = new TableView<>();
    private final ObservableList<Prestamo> datos = FXCollections.observableArrayList();

    // Botones
    private final Button btnNuevo = new Button("Nuevo");
    private final Button btnEditar = new Button("Editar");
    private final Button btnEliminar = new Button("Eliminar");
    private final Button btnRefrescar = new Button("Refrescar");

    // Campos de búsqueda (arriba)
    private final TextField txtBuscarSocio = new TextField();
    private final TextField txtBuscarLibro = new TextField();

    // Barra de estado (abajo)
    private final Label lblEstado = new Label("Listo");

    private void setEstado(String msg) {
        lblEstado.setText(msg);
    }

    // Métode refrescar tabla
    private void refrescarTabla() {
        datos.setAll(prestamoDAO.findAll());
        setEstado("Lista actualizada (" + datos.size() + " registros)");
    }

    // Métode para controlar que el préstamo está seleccionado antes de editar
    private void editarPrestamoSeleccionado() {
        Prestamo p = tabla.getSelectionModel().getSelectedItem();
        if (p == null) return;
        mostrarDialogoEditarPrestamo(p);
    }

    // Métode para eliminar préstamo
    private void eliminarPrestamoSeleccionado() {
        // Control de selección
        Prestamo p = tabla.getSelectionModel().getSelectedItem();
        if (p == null) return;

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Seguro que quieres eliminar el préstamo ID " + p.getIdPrestamo() + "?",
                ButtonType.OK,
                ButtonType.CANCEL
        );
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                prestamoDAO.deleteById(p.getIdPrestamo());
                refrescarTabla();
                setEstado("Eliminado correctamente");
            } else {
                setEstado("Eliminación cancelada");
            }
        });
    }

    // Métode insertar nuevo préstamo
    private void mostrarDialogoNuevoPrestamo() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo préstamo");
        dialog.setHeaderText("Introduce los datos del préstamo");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        // Campos
        TextField txtIdSocio = new TextField();
        TextField txtIdLibro = new TextField();

        ComboBox<String> cbEstado = new ComboBox<>();
        cbEstado.getItems().addAll("ABIERTO", "DEVUELTO", "ATRASADO");
        cbEstado.setValue("ABIERTO");

        DatePicker dpFechaPrestamo = new DatePicker(LocalDate.now());
        DatePicker dpFechaPrevista = new DatePicker(LocalDate.now().plusDays(15));
        DatePicker dpFechaDevolucion = new DatePicker(); // vacío

        TextField txtPenalizacion = new TextField("0.00");
        TextArea txtObservaciones = new TextArea();
        txtObservaciones.setPrefRowCount(3);

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("ID Socio (obligatorio):"), 0, 0);
        grid.add(txtIdSocio, 1, 0);

        grid.add(new Label("ID Libro (obligatorio):"), 0, 1);
        grid.add(txtIdLibro, 1, 1);

        grid.add(new Label("Estado:"), 0, 2);
        grid.add(cbEstado, 1, 2);

        grid.add(new Label("Fecha préstamo:"), 0, 3);
        grid.add(dpFechaPrestamo, 1, 3);

        grid.add(new Label("Fecha prevista devolución (obligatorio):"), 0, 4);
        grid.add(dpFechaPrevista, 1, 4);

        grid.add(new Label("Fecha devolución:"), 0, 5);
        grid.add(dpFechaDevolucion, 1, 5);

        grid.add(new Label("Penalización (€):"), 0, 6);
        grid.add(txtPenalizacion, 1, 6);

        grid.add(new Label("Observaciones:"), 0, 7);
        grid.add(txtObservaciones, 1, 7);

        // Poner el contenido en el interior del diálogo
        dialog.getDialogPane().setContent(grid);

        // Acción guardar: mostrar y esperar respuesta
        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnGuardar) {

                // Validaciones mínimas
                String idSocioStr = txtIdSocio.getText().trim();
                String idLibroStr = txtIdLibro.getText().trim();

                if (idSocioStr.isEmpty() || idLibroStr.isEmpty() || dpFechaPrevista.getValue() == null) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incompletos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("ID Socio, ID Libro y Fecha prevista de devolución son obligatorios.");
                    aviso.showAndWait();
                    setEstado("Validación: faltan campos obligatorios");
                    return;
                }

                int idSocio, idLibro;
                try {
                    idSocio = Integer.parseInt(idSocioStr);
                    idLibro = Integer.parseInt(idLibroStr);
                } catch (NumberFormatException ex) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Formato incorrecto");
                    aviso.setHeaderText(null);
                    aviso.setContentText("ID Socio e ID Libro deben ser números.");
                    aviso.showAndWait();
                    setEstado("Validación: ID no numérico");
                    return;
                }

                BigDecimal penalizacion;
                try {
                    penalizacion = new BigDecimal(txtPenalizacion.getText().trim().replace(",", "."));
                } catch (Exception ex) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Formato incorrecto");
                    aviso.setHeaderText(null);
                    aviso.setContentText("Penalización debe ser un número (ej: 0.00).");
                    aviso.showAndWait();
                    setEstado("Validación: penalización inválida");
                    return;
                }

                if (penalizacion.compareTo(BigDecimal.ZERO) < 0) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incorrectos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("La penalización no puede ser negativa.");
                    aviso.showAndWait();
                    setEstado("Validación: penalización negativa");
                    return;
                }

                LocalDate fPrestamo = dpFechaPrestamo.getValue();
                LocalDate fPrevista = dpFechaPrevista.getValue();
                LocalDate fDevolucion = dpFechaDevolucion.getValue();

                // Fecha prevista no puede ser anterior al préstamo
                if (fPrestamo != null && fPrevista != null && fPrevista.isBefore(fPrestamo)) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Fechas incorrectas");
                    aviso.setHeaderText(null);
                    aviso.setContentText("La fecha prevista de devolución no puede ser anterior a la fecha de préstamo.");
                    aviso.showAndWait();
                    setEstado("Validación: fechas incoherentes (prevista < préstamo)");
                    return;
                }

                // Si hay devolución, no puede ser anterior al préstamo
                if (fDevolucion != null && fPrestamo != null && fDevolucion.isBefore(fPrestamo)) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Fechas incorrectas");
                    aviso.setHeaderText(null);
                    aviso.setContentText("La fecha de devolución no puede ser anterior a la fecha de préstamo.");
                    aviso.showAndWait();
                    setEstado("Validación: fechas incoherentes (devolución < préstamo)");
                    return;
                }

                // Crear nuevo préstamo
                Prestamo nuevo = new Prestamo();
                nuevo.setIdSocio(idSocio);
                nuevo.setIdLibro(idLibro);
                nuevo.setFechaPrestamo(fPrestamo);
                nuevo.setFechaPrevistaDevolucion(fPrevista);
                nuevo.setFechaDevolucion(fDevolucion);
                // si el usuario no pone fecha devolución, queda null
                nuevo.setFechaDevolucion(dpFechaDevolucion.getValue());
                nuevo.setPenalizacionEuros(penalizacion);
                nuevo.setObservaciones(txtObservaciones.getText());

                int idGenerado = prestamoDAO.insert(nuevo);

                if (idGenerado > 0) {
                    refrescarTabla();
                    setEstado("Creado correctamente (ID " + idGenerado + ")");
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText(null);
                    error.setContentText("No se pudo insertar el préstamo. Revisa IDs (socio/libro existentes).");
                    error.showAndWait();
                    setEstado("Error al crear");
                }
            } else {
                setEstado("Acción cancelada");
            }
        });
    }

    // Métode editar préstamo
    private void mostrarDialogoEditarPrestamo(Prestamo prestamo) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar préstamo");
        dialog.setHeaderText("Modifica los datos del préstamo");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        // Campos
        TextField txtIdSocio = new TextField(String.valueOf(prestamo.getIdSocio()));
        TextField txtIdLibro = new TextField(String.valueOf(prestamo.getIdLibro()));
        txtIdLibro.setDisable(true);

        ComboBox<String> cbEstado = new ComboBox<>();
        cbEstado.getItems().addAll("ABIERTO", "DEVUELTO", "ATRASADO");
        cbEstado.setValue(prestamo.getEstado() != null ? prestamo.getEstado() : "ABIERTO");

        DatePicker dpFechaPrestamo = new DatePicker(prestamo.getFechaPrestamo());
        DatePicker dpFechaPrevista = new DatePicker(prestamo.getFechaPrevistaDevolucion());
        DatePicker dpFechaDevolucion = new DatePicker(prestamo.getFechaDevolucion());

        TextField txtPenalizacion = new TextField(
                prestamo.getPenalizacionEuros() != null ? prestamo.getPenalizacionEuros().toString() : "0.00"
        );
        TextArea txtObservaciones = new TextArea(prestamo.getObservaciones());
        txtObservaciones.setPrefRowCount(3);

        // Layout del formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("ID Socio (obligatorio):"), 0, 0);
        grid.add(txtIdSocio, 1, 0);

        grid.add(new Label("ID Libro (obligatorio):"), 0, 1);
        grid.add(txtIdLibro, 1, 1);

        grid.add(new Label("Estado:"), 0, 2);
        grid.add(cbEstado, 1, 2);

        grid.add(new Label("Fecha préstamo:"), 0, 3);
        grid.add(dpFechaPrestamo, 1, 3);

        grid.add(new Label("Fecha prevista devolución (obligatorio):"), 0, 4);
        grid.add(dpFechaPrevista, 1, 4);

        grid.add(new Label("Fecha devolución:"), 0, 5);
        grid.add(dpFechaDevolucion, 1, 5);

        grid.add(new Label("Penalización (€):"), 0, 6);
        grid.add(txtPenalizacion, 1, 6);

        grid.add(new Label("Observaciones:"), 0, 7);
        grid.add(txtObservaciones, 1, 7);

        // Mostrar contenido en el diálogo
        dialog.getDialogPane().setContent(grid);

        // Acción guardar: mostrar y esperar respuesta
        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnGuardar) {

                // Validaciones mínimas
                String idSocioStr = txtIdSocio.getText().trim();
                String idLibroStr = txtIdLibro.getText().trim();

                if (idSocioStr.isEmpty() || idLibroStr.isEmpty() || dpFechaPrevista.getValue() == null) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incompletos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("ID Socio, ID Libro y Fecha prevista de devolución son obligatorios.");
                    aviso.showAndWait();
                    setEstado("Validación: faltan campos obligatorios");
                    return;
                }

                int idSocio, idLibro;
                try {
                    idSocio = Integer.parseInt(idSocioStr);
                    idLibro = Integer.parseInt(idLibroStr);
                } catch (NumberFormatException ex) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Formato incorrecto");
                    aviso.setHeaderText(null);
                    aviso.setContentText("ID Socio e ID Libro deben ser números.");
                    aviso.showAndWait();
                    setEstado("Validación: ID no numérico");
                    return;
                }

                BigDecimal penalizacion;
                try {
                    penalizacion = new BigDecimal(txtPenalizacion.getText().trim().replace(",", "."));
                } catch (Exception ex) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Formato incorrecto");
                    aviso.setHeaderText(null);
                    aviso.setContentText("Penalización debe ser un número (ej: 0.00).");
                    aviso.showAndWait();
                    setEstado("Validación: penalización inválida");
                    return;
                }

                if (penalizacion.compareTo(BigDecimal.ZERO) < 0) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incorrectos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("La penalización no puede ser negativa.");
                    aviso.showAndWait();
                    setEstado("Validación: penalización negativa");
                    return;
                }

                LocalDate fPrestamo = dpFechaPrestamo.getValue();
                LocalDate fPrevista = dpFechaPrevista.getValue();
                LocalDate fDevolucion = dpFechaDevolucion.getValue();

                // Fecha prevista no puede ser anterior al préstamo
                if (fPrestamo != null && fPrevista != null && fPrevista.isBefore(fPrestamo)) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Fechas incorrectas");
                    aviso.setHeaderText(null);
                    aviso.setContentText("La fecha prevista de devolución no puede ser anterior a la fecha de préstamo.");
                    aviso.showAndWait();
                    setEstado("Validación: fechas incoherentes (prevista < préstamo)");
                    return;
                }

                // Si hay devolución, no puede ser anterior al préstamo
                if (fDevolucion != null && fPrestamo != null && fDevolucion.isBefore(fPrestamo)) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Fechas incorrectas");
                    aviso.setHeaderText(null);
                    aviso.setContentText("La fecha de devolución no puede ser anterior a la fecha de préstamo.");
                    aviso.showAndWait();
                    setEstado("Validación: fechas incoherentes (devolución < préstamo)");
                    return;
                }

                // Actualizar objeto
                prestamo.setIdSocio(idSocio);
                prestamo.setEstado(cbEstado.getValue());
                prestamo.setFechaPrestamo(fPrestamo);
                prestamo.setFechaPrevistaDevolucion(fPrevista);
                prestamo.setFechaDevolucion(fDevolucion);
                prestamo.setPenalizacionEuros(penalizacion);
                prestamo.setObservaciones(txtObservaciones.getText());

                boolean ok = prestamoDAO.update(prestamo);

                if (ok) {
                    refrescarTabla();
                    setEstado("Actualizado correctamente");
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText(null);
                    error.setContentText("No se pudo actualizar el préstamo.");
                    error.showAndWait();
                    setEstado("No se pudo actualizar");
                }
            } else {
                setEstado("Acción cancelada");
            }
        });
    }

    // Construir la interfaz
    public Parent createContent() {

        // Columnas
        TableColumn<Prestamo, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idPrestamo"));

        TableColumn<Prestamo, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        TableColumn<Prestamo, LocalDate> colFechaPrestamo = new TableColumn<>("Fecha préstamo");
        colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));

        TableColumn<Prestamo, LocalDate> colPrevista = new TableColumn<>("Prevista devolución");
        colPrevista.setCellValueFactory(new PropertyValueFactory<>("fechaPrevistaDevolucion"));

        TableColumn<Prestamo, LocalDate> colDevolucion = new TableColumn<>("Fecha devolución");
        colDevolucion.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));

        TableColumn<Prestamo, BigDecimal> colPenal = new TableColumn<>("Penalización (€)");
        colPenal.setCellValueFactory(new PropertyValueFactory<>("penalizacionEuros"));

        TableColumn<Prestamo, String> colSocio = new TableColumn<>("Socio");
        colSocio.setCellValueFactory(new PropertyValueFactory<>("socioNombreCompleto"));

        TableColumn<Prestamo, String> colLibro = new TableColumn<>("Libro");
        colLibro.setCellValueFactory(new PropertyValueFactory<>("tituloLibro"));

        TableColumn<Prestamo, String> colObservaciones = new TableColumn<>("Observaciones");
        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));

        tabla.getColumns().addAll(colId, colEstado, colFechaPrestamo, colPrevista, colDevolucion, colPenal, colSocio, colLibro, colObservaciones);

        // Cargar datos de la BD
        datos.setAll(prestamoDAO.findAll());
        tabla.setItems(datos);

        // Barra de búsqueda (hasta 2 campos)
        txtBuscarSocio.setPromptText("Buscar por socio (nombre o apellidos)...");
        txtBuscarLibro.setPromptText("Buscar por título del libro...");

        Button btnBuscar = new Button("Buscar");
        Button btnLimpiar = new Button("Limpiar");

        HBox barraBusqueda = new HBox(10, txtBuscarSocio, txtBuscarLibro, btnBuscar, btnLimpiar);
        barraBusqueda.setPadding(new Insets(10));

        // Barra HBox. Espaciado y Padding 10px
        HBox barraBotones = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnRefrescar);
        barraBotones.setPadding(new Insets(10));

        // Barra de estado (footer)
        HBox barraEstado = new HBox(lblEstado);
        barraEstado.setPadding(new Insets(6, 10, 6, 10));
        barraEstado.setStyle("-fx-background-color: #f2f2f2; -fx-border-color: #d0d0d0;");

        // Deshabilitar botones que requieren selección
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);

        // Detectar selección de fila y habilitar botones
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, ant, sel) -> {
            boolean hay = (sel != null);
            btnEditar.setDisable(!hay);
            btnEliminar.setDisable(!hay);
        });

        // Funciones de los botones
        btnRefrescar.setOnAction(e -> refrescarTabla());
        btnNuevo.setOnAction(e -> mostrarDialogoNuevoPrestamo());
        btnEditar.setOnAction(e -> editarPrestamoSeleccionado());
        btnEliminar.setOnAction(e -> eliminarPrestamoSeleccionado());

        // Funciones de búsqueda
        btnBuscar.setOnAction(e -> {
            String socio = txtBuscarSocio.getText();
            String libro = txtBuscarLibro.getText();

            boolean socioVacio = (socio == null || socio.isBlank());
            boolean libroVacio = (libro == null || libro.isBlank());

            if (socioVacio && libroVacio) {
                refrescarTabla();
                return;
            }

            datos.setAll(prestamoDAO.findByFilters(socio, libro));
            setEstado("Búsqueda realizada (" + datos.size() + " resultados)");
        });

        btnLimpiar.setOnAction(e -> {
            txtBuscarSocio.clear();
            txtBuscarLibro.clear();
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
