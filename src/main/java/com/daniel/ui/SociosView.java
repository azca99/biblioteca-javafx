package com.daniel.ui;

import com.daniel.dao.SocioDAO;
import com.daniel.model.Socio;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class SociosView {

    private final SocioDAO socioDAO = new SocioDAO();

    private final TableView<Socio> tabla = new TableView<>();
    private final ObservableList<Socio> datos = FXCollections.observableArrayList();

    // Botones
    private final Button btnNuevo = new Button("Nuevo");
    private final Button btnEditar = new Button("Editar");
    private final Button btnEliminar = new Button("Eliminar");
    private final Button btnRefrescar = new Button("Refrescar");

    // Campos de búsqueda (arriba)
    private final TextField txtBuscarDni = new TextField();
    private final TextField txtBuscarNombreApellidos = new TextField();

    // Barra de estado (abajo)
    private final Label lblEstado = new Label("Listo");

    private void setEstado(String msg) {
        lblEstado.setText(msg);
    }

    // Métode refrescar tabla
    private void refrescarTabla() {
        datos.setAll(socioDAO.findAll());
        setEstado("Lista actualizada (" + datos.size() + " registros)");
    }

    // Métode crear nuevo usuario
    private void mostrarDialogoNuevoSocio() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo socio");
        dialog.setHeaderText("Introduce los datos del socio");

        // Botones del diálogo
        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        // Campos del formulario
        TextField txtDni = new TextField();
        TextField txtNombre = new TextField();
        TextField txtApellidos = new TextField();
        TextField txtEmail = new TextField();
        TextField txtTelefono = new TextField();
        DatePicker dpFechaAlta = new DatePicker(LocalDate.now());
        CheckBox chkActivo = new CheckBox("Activo");
        chkActivo.setSelected(true);

        // Layout del formulario (grid simple)
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("DNI:"), 0, 0);
        grid.add(txtDni, 1, 0);

        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(txtNombre, 1, 1);

        grid.add(new Label("Apellidos:"), 0, 2);
        grid.add(txtApellidos, 1, 2);

        grid.add(new Label("Email:"), 0, 3);
        grid.add(txtEmail, 1, 3);

        grid.add(new Label("Teléfono:"), 0, 4);
        grid.add(txtTelefono, 1, 4);

        grid.add(new Label("Fecha alta:"), 0, 5);
        grid.add(dpFechaAlta, 1, 5);

        grid.add(chkActivo, 1, 6);

        // Poner el contenido en el interior del diálogo
        dialog.getDialogPane().setContent(grid);

        // Mostrar y esperar respuesta
        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnGuardar) {

                // Validación mínima (lo imprescindible)
                String dni = txtDni.getText().trim();
                String nombre = txtNombre.getText().trim();
                String apellidos = txtApellidos.getText().trim();
                String email = txtEmail.getText().trim();
                String telefono = txtTelefono.getText().trim();

                if (dni.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || telefono.isEmpty()) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incompletos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("DNI, Nombre, Apellidos, Email y Teléfono son obligatorios.");
                    aviso.showAndWait();
                    setEstado("Validación: faltan campos obligatorios");
                    return;
                }

                if (!email.contains("@")) {
                    new Alert(Alert.AlertType.WARNING, "Email no válido.").showAndWait();
                    setEstado("Validación: email no válido");
                    return;
                }

                if (!telefono.matches("\\d+")) {
                    new Alert(Alert.AlertType.WARNING, "Teléfono debe contener solo números.").showAndWait();
                    setEstado("Validación: teléfono no válido");
                    return;
                }

                // Crear el objeto Socio con lo que el usuario escribió
                Socio nuevo = new Socio();
                nuevo.setDni(dni);
                nuevo.setNombre(nombre);
                nuevo.setApellidos(apellidos);
                nuevo.setEmail(email);
                nuevo.setTelefono(telefono);
                nuevo.setActivo(chkActivo.isSelected());
                nuevo.setFechaAlta(dpFechaAlta.getValue());

                // Insertar en BD y refrescar
                int idGenerado = socioDAO.insert(nuevo);

                if (idGenerado > 0) {
                    refrescarTabla();
                    setEstado("Creado correctamente (ID " + idGenerado + ")");
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText(null);
                    error.setContentText("No se pudo insertar el socio.");
                    error.showAndWait();
                    setEstado("Error al crear");
                }
            } else {
                setEstado("Acción cancelada");
            }
        });
    }

    // Métode para controlar que el Socio está seleccionado antes de editar
    private void editarSocioSeleccionado() {
        Socio s = tabla.getSelectionModel().getSelectedItem();
        if (s == null) return;
        mostrarDialogoEditarSocio(s);
    }

    // Métode editar socio
    private void mostrarDialogoEditarSocio(Socio socio) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar socio");
        dialog.setHeaderText("Modifica los datos del socio");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        // Campos (IGUALES que en Nuevo)
        TextField txtDni = new TextField(socio.getDni());
        TextField txtNombre = new TextField(socio.getNombre());
        TextField txtApellidos = new TextField(socio.getApellidos());
        TextField txtEmail = new TextField(socio.getEmail());
        TextField txtTelefono = new TextField(socio.getTelefono());

        DatePicker dpFechaAlta = new DatePicker(socio.getFechaAlta());
        CheckBox chkActivo = new CheckBox("Activo");
        chkActivo.setSelected(socio.isActivo());

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("DNI:"), 0, 0);
        grid.add(txtDni, 1, 0);

        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(txtNombre, 1, 1);

        grid.add(new Label("Apellidos:"), 0, 2);
        grid.add(txtApellidos, 1, 2);

        grid.add(new Label("Email:"), 0, 3);
        grid.add(txtEmail, 1, 3);

        grid.add(new Label("Teléfono:"), 0, 4);
        grid.add(txtTelefono, 1, 4);

        grid.add(new Label("Fecha alta:"), 0, 5);
        grid.add(dpFechaAlta, 1, 5);

        grid.add(chkActivo, 1, 6);

        dialog.getDialogPane().setContent(grid);

        // Acción Guardar
        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnGuardar) {

                // Validación mínima
                if (txtDni.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty() || txtApellidos.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()) {
                    Alert aviso = new Alert(Alert.AlertType.WARNING);
                    aviso.setTitle("Datos incompletos");
                    aviso.setHeaderText(null);
                    aviso.setContentText("DNI, Nombre, Apellidos, Email y Teléfono son obligatorios.");
                    aviso.showAndWait();
                    setEstado("Validación: faltan campos obligatorios");
                    return;
                }

                // Actualizar el objeto existente
                socio.setDni(txtDni.getText().trim());
                socio.setNombre(txtNombre.getText().trim());
                socio.setApellidos(txtApellidos.getText().trim());
                socio.setEmail(txtEmail.getText().trim());
                socio.setTelefono(txtTelefono.getText().trim());
                socio.setFechaAlta(dpFechaAlta.getValue());
                socio.setActivo(chkActivo.isSelected());

                boolean ok = socioDAO.update(socio);

                if (ok) {
                    refrescarTabla();
                    setEstado("Actualizado correctamente");
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText(null);
                    error.setContentText("No se pudo actualizar el socio.");
                    error.showAndWait();
                    setEstado("No se pudo actualizar");
                }
            } else {
                setEstado("Acción cancelada");
            }
        });
    }

    // Eliminar socio
    private void eliminarSocioSeleccionado() {

        Socio s = tabla.getSelectionModel().getSelectedItem();
        if (s == null) return;

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Seguro que quieres eliminar al socio:\n\n"
                        + s.getNombre() + " " + s.getApellidos(),
                ButtonType.OK,
                ButtonType.CANCEL
        );

        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                socioDAO.deleteById(s.getIdSocio());
                refrescarTabla();
                setEstado("Eliminado correctamente");
            } else {
                setEstado("Eliminación cancelada");
            }
        });
    }

    // Construir la interfaz
    public Parent createContent() {

        // Columnas
        TableColumn<Socio, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idSocio")); //Busca getter para idSocio

        TableColumn<Socio, String> colDni = new TableColumn<>("DNI");
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));

        TableColumn<Socio, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Socio, String> colApellidos = new TableColumn<>("Apellidos");
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));

        TableColumn<Socio, Boolean> colActivo = new TableColumn<>("Activo");
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        TableColumn<Socio, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Socio, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        TableColumn<Socio, LocalDate> colFechaAlta = new TableColumn<>("Fecha alta");
        colFechaAlta.setCellValueFactory(new PropertyValueFactory<>("fechaAlta"));

        tabla.getColumns().addAll(colId, colDni, colNombre, colApellidos, colActivo, colEmail, colTelefono, colFechaAlta);

        // Cargar datos desde BD
        datos.setAll(socioDAO.findAll());
        tabla.setItems(datos);

        // Barra de búsqueda (hasta 2 campos)
        txtBuscarDni.setPromptText("Buscar por DNI...");
        txtBuscarNombreApellidos.setPromptText("Buscar por nombre o apellidos...");

        Button btnBuscar = new Button("Buscar");
        Button btnLimpiar = new Button("Limpiar");

        HBox barraBusqueda = new HBox(10, txtBuscarDni, txtBuscarNombreApellidos, btnBuscar, btnLimpiar);
        barraBusqueda.setPadding(new Insets(10));

        // Barra HBox. Espaciado 10 Padding 10
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
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            boolean haySeleccion = (seleccionado != null);
            btnEditar.setDisable(!haySeleccion);
            btnEliminar.setDisable(!haySeleccion);
        });

        // Funciones de los botones
        btnRefrescar.setOnAction(e -> refrescarTabla());
        btnNuevo.setOnAction(e -> mostrarDialogoNuevoSocio());
        btnEditar.setOnAction(e -> editarSocioSeleccionado());
        btnEliminar.setOnAction(e -> eliminarSocioSeleccionado());

        // Funciones de búsqueda
        btnBuscar.setOnAction(e -> {
            String dni = txtBuscarDni.getText();
            String texto = txtBuscarNombreApellidos.getText();

            boolean dniVacio = (dni == null || dni.isBlank());
            boolean textoVacio = (texto == null || texto.isBlank());

            // Si no hay filtros, mostrar all
            if (dniVacio && textoVacio) {
                refrescarTabla();
                return;
            }

            // Buscar con hasta 2 campos
            datos.setAll(socioDAO.findByFilters(dni, texto));
            setEstado("Búsqueda realizada (" + datos.size() + " resultados)");
        });

        btnLimpiar.setOnAction(e -> {
            txtBuscarDni.clear();
            txtBuscarNombreApellidos.clear();
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
