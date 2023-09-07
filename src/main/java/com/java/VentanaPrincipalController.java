package com.java;

import com.java.dao.ClienteDAO;
import com.java.factory.DAOFactory;
import com.java.factory.MySQLDAOFactory;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class VentanaPrincipalController implements Initializable {

//    private final ClienteDAO clienteDAO = ClienteDAOFactory.getClienteDAO();


    // Obtén una instancia de MySQLDAOFactory
    DAOFactory factory = new MySQLDAOFactory();


    // Obtener una instancia de ClienteDAO
    ClienteDAO clienteDAO = factory.getClienteDAO();

//    private final ClienteDAO clienteDAO = new ClienteDAOImpl();


    // Crear una tabla para mostrar los datos
    TableView<Cliente> tablaClientes = new TableView<>();


    @FXML
    void onAgregarCliente() {

        // Crear un nuevo Stage para la ventana de agregar cliente
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Agregar Cliente");


        // Crear los componentes de la ventana
        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField();

        Label lblApellido = new Label("Apellido:");
        TextField txtApellido = new TextField();

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();

        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField();

        Label lblFechaRegistro = new Label("Fecha de Registro:");
        DatePicker datePicker = new DatePicker();
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate object) {
                return object != null ? object.toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string) : null;
            }
        });


        //el boton
        Button btnGuardar = new Button("Guardar");
        btnGuardar.setOnAction(e -> {
            // Obtener los datos ingresados por el usuario
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String email = txtEmail.getText();
            String telefono = txtTelefono.getText();
            LocalDate fechaRegistro = datePicker.getValue();

            // Validar que todos los campos no estén vacíos
            if (!nombre.isEmpty() || !apellido.isEmpty() || !email.isEmpty() || !telefono.isEmpty() || fechaRegistro != null) {
                // Crear un nuevo cliente con los datos ingresados
                Cliente nuevoCliente = new Cliente(nombre, apellido, email, telefono, fechaRegistro);

                // Llamar al método del DAO para agregar el cliente a la base de datos
                clienteDAO.agregarCliente(nuevoCliente);

                // Limpia la tabla existente
                tablaClientes.getItems().clear();

                // Obtiene la lista actualizada de clientes desde el clienteDAO
                List<Cliente> listaClientesActualizada = clienteDAO.listarTodosLosClientes();

                // Agrega los nuevos datos a la tabla
                tablaClientes.getItems().addAll(listaClientesActualizada);

                // Mostrar un mensaje de éxito si el cliente se actualizo correctamente
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText(null);
                alert.setContentText("El cliente fue agregado correctamente.");
                alert.showAndWait();


            } else {
                // Mostrar un mensaje de error si la fecha es nula
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Campos vacios");
                alert.setContentText("Completa todos los campos.");
                alert.showAndWait();
            }
        });

        // Agregar la tabla a un VBox
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.getChildren().addAll(lblNombre, txtNombre, lblApellido, txtApellido, lblEmail, txtEmail, lblTelefono, txtTelefono, lblFechaRegistro, datePicker, btnGuardar, tablaClientes);

        // Crea una escena y establecerla en el Stage
        Scene scene = new Scene(layout, 600, 600);
        stage.setScene(scene);

        // Mostra la ventana de agregar cliente
        stage.show();
    }


    @FXML
    void onEditarCliente() {
        // Crear un nuevo Stage para la ventana de edición de cliente
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Editar Cliente");

        // Crear los componentes de la ventana de edición
        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField();

        Label lblApellido = new Label("Apellido:");
        TextField txtApellido = new TextField();

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();

        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField();

        Label lblFechaRegistro = new Label("Fecha de Registro:");
        DatePicker datePicker = new DatePicker();
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate object) {
                return object != null ? object.toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string) : null;
            }
        });

        // Manejar la selección de una fila en la tabla
        tablaClientes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Cliente>() {
            @Override
            public void changed(ObservableValue<? extends Cliente> observable, Cliente oldValue, Cliente newValue) {
                if (newValue != null) {
                    // Cuando se selecciona una fila, establecer los valores en los TextField
                    txtNombre.setText(newValue.getNombre());
                    txtApellido.setText(newValue.getApellido());
                    txtEmail.setText(newValue.getEmail());
                    txtTelefono.setText(newValue.getTelefono());
                    datePicker.setValue(newValue.getFechaRegistro());
                } else {
                    // Si no hay ninguna fila seleccionada, borrar los TextField
                    txtNombre.clear();
                    txtApellido.clear();
                    txtEmail.clear();
                    txtTelefono.clear();
                    datePicker.setValue(null);
                }
            }
        });


        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setOnAction(e -> {
            // Obtener la fila seleccionada
            Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();

            // Verificar que se haya seleccionado un cliente
            if (clienteSeleccionado != null) {
                // Obtener los nuevos valores desde los TextField
                String nuevoNombre = txtNombre.getText();
                String nuevoApellido = txtApellido.getText();
                String nuevoEmail = txtEmail.getText();
                String nuevoTelefono = txtTelefono.getText();
                LocalDate nuevaFechaRegistro = datePicker.getValue();

                // Actualizar los datos del cliente seleccionado
                clienteSeleccionado.setNombre(nuevoNombre);
                clienteSeleccionado.setApellido(nuevoApellido);
                clienteSeleccionado.setEmail(nuevoEmail);
                clienteSeleccionado.setTelefono(nuevoTelefono);
                clienteSeleccionado.setFechaRegistro(nuevaFechaRegistro);

                // Llamar al método del DAO para actualizar el cliente en la base de datos
                clienteDAO.actualizarCliente(clienteSeleccionado);

                // Actualizar la tabla
                tablaClientes.refresh();

                // Mostrar un mensaje de éxito si el cliente se actualizo correctamente
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText(null);
                alert.setContentText("El cliente se actualizo correctamente.");
                alert.showAndWait();


            } else {
                // Mostrar un mensaje de error si no se selecciona un cliente
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cliente no seleccionado");
                alert.setContentText("Por favor, seleccione un cliente para editar.");
                alert.showAndWait();
            }
        });


        // Agregar la tabla a un VBox
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.getChildren().addAll(lblNombre, txtNombre, lblApellido, txtApellido, lblEmail, txtEmail, lblTelefono, txtTelefono, lblFechaRegistro, datePicker, btnGuardar, tablaClientes);

        // Crea una escena y establecerla en el Stage
        Scene scene = new Scene(layout, 600, 600);
        stage.setScene(scene);

        // Mostrar la ventana de edición de cliente
        stage.show();
    }


    @FXML
    void onBuscarCliente(ActionEvent event) {
        // Crear un nuevo Stage para la ventana de búsqueda de cliente
        Stage buscarStage = new Stage();
        buscarStage.initModality(Modality.APPLICATION_MODAL);
        buscarStage.setTitle("Buscar Cliente");

        // Crear los componentes de la ventana de búsqueda
        Label lblBuscar = new Label("Buscar:");
        TextField txtBuscar = new TextField();
        Button btnBuscar = new Button("Buscar");


        // Agregar la lógica de búsqueda al botón
        btnBuscar.setOnAction(e -> {
            String busqueda = txtBuscar.getText();
            List<Cliente> resultados = clienteDAO.buscarClientesPorCodigoNombreApellido(busqueda);

            // Limpiar la tabla y agregar los resultados de la búsqueda
            tablaClientes.getItems().clear();
            tablaClientes.getItems().addAll(resultados);
        });

        // Agregar la lógica de búsqueda en tiempo real al TextField
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            String busqueda = newValue;

            // Ejecutar la búsqueda en segundo plano
            new Thread(() -> {
                // Realizar la búsqueda de clientes por código, nombre o apellido
                List<Cliente> resultados = clienteDAO.buscarClientesPorCodigoNombreApellido(busqueda);
                // Actualizar la UI con los resultados en el hilo de JavaFX
                Platform.runLater(() -> {
                    // Limpiar la tabla y agregar los resultados de la búsqueda
                    tablaClientes.getItems().clear();
                    tablaClientes.getItems().addAll(resultados);
                });
            }).start();


        });


        // Crear un VBox para organizar los componentes
        VBox buscarLayout = new VBox(10);
        buscarLayout.setPadding(new Insets(15));
        buscarLayout.getChildren().addAll(lblBuscar, txtBuscar, btnBuscar, tablaClientes);

        // Crear una escena y establecerla en el Stage de búsqueda
        Scene buscarScene = new Scene(buscarLayout, 600, 400);
        buscarStage.setScene(buscarScene);

        // Mostrar la ventana de búsqueda de cliente
        buscarStage.showAndWait();
    }


    @FXML
    void onEliminarCliente(ActionEvent event) {
        // Crear un nuevo Stage para la ventana de eliminación de cliente
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Eliminar Cliente");

        // Crear los componentes de la ventana
        Label lblCodigo = new Label("Código del Cliente:");
        TextField txtCodigo = new TextField();



        // Botón para eliminar el cliente
        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setOnAction(e -> {
            // Obtener el código del cliente a eliminar desde el TextField
            String codigoCliente = txtCodigo.getText();

            // Validar si el código ingresado es válido (puedes agregar más validaciones)
            if (!codigoCliente.isEmpty()) {
                int cod = Integer.parseInt(codigoCliente);
                // Llamar al método del DAO para eliminar el cliente por código
                boolean eliminado = clienteDAO.eliminarClientePorCodigo(cod);

                if (eliminado) {
                    // Mostrar un mensaje de éxito si el cliente se eliminó correctamente
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Éxito");
                    alert.setHeaderText(null);
                    alert.setContentText("El cliente se eliminó exitosamente.");
                    alert.showAndWait();

                    // Limpiar el TextField después de eliminar
                    txtCodigo.clear();

                    // Obtiene la lista actualizada de clientes desde el clienteDAO
                    List<Cliente> listaClientesActualizada = clienteDAO.listarTodosLosClientes();
                    // Limpia la tabla existente
                    tablaClientes.getItems().clear();
                    // Agrega los nuevos datos a la tabla
                    tablaClientes.getItems().addAll(listaClientesActualizada);


                } else {
                    // Mostrar un mensaje de error si la eliminación falló
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("No se pudo eliminar el cliente. Verifica el código.");
                    alert.showAndWait();
                }
            } else {
                // Mostrar un mensaje de error si el código está vacío
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Por favor, ingresa el código del cliente a eliminar.");
                alert.showAndWait();
            }
        });


        tablaClientes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Cliente>() {
            @Override
            public void changed(ObservableValue<? extends Cliente> observable, Cliente oldValue, Cliente newValue) {
                if (newValue != null) {
                    // Cuando se selecciona una fila, establecer los valores en los TextField
                    txtCodigo.setText(String.valueOf(newValue.getCodigo()));

                } else {
                    // Si no hay ninguna fila seleccionada, borrar los TextField
                    txtCodigo.clear();

                }
            }
        });






        // Agregar los componentes a un VBox
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.getChildren().addAll(lblCodigo, txtCodigo, btnEliminar, tablaClientes);

        // Crear una escena y establecerla en el Stage de búsqueda
        Scene buscarScene = new Scene(layout, 600, 400);
        stage.setScene(buscarScene);
        // Mostrar la ventana de eliminación de cliente
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<Cliente, Integer> columnaCodigo = new TableColumn<>("Código");
        TableColumn<Cliente, String> columnaNombre = new TableColumn<>("Nombre");
        TableColumn<Cliente, String> columnaApellido = new TableColumn<>("Apellido");
        TableColumn<Cliente, String> columnaEmail = new TableColumn<>("Email");
        TableColumn<Cliente, String> columnaTelefono = new TableColumn<>("Teléfono");
        TableColumn<Cliente, LocalDate> columnaFechaRegistro = new TableColumn<>("Fecha de Registro");
        tablaClientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Configurar las propiedades de las columnas
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        columnaFechaRegistro.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));
        tablaClientes.getColumns().addAll(columnaCodigo, columnaNombre, columnaApellido, columnaEmail, columnaTelefono, columnaFechaRegistro);
        // Llama al método para cargar todos los clientes desde la base de datos
        List<Cliente> clientes = clienteDAO.listarTodosLosClientes();

        // Asigna los datos a la tabla
        tablaClientes.getItems().addAll(clientes);
    }
}
