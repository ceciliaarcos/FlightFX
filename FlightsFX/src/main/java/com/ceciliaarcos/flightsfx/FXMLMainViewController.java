package com.ceciliaarcos.flightsfx;

import com.ceciliaarcos.flightsfx.model.Flight;
import com.ceciliaarcos.flightsfx.utils.FileUtils;
import com.ceciliaarcos.flightsfx.utils.MessageUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase controlador
 * @author Cecilia Arcos
 */


public class FXMLMainViewController implements Initializable {
    ObservableList<Flight> flight;

    /**
     * @return Observ. List flight
     */
    public List<Flight> getFlights() {
        return flight;
    }

    @FXML
    private TableView<Flight> tableFlight;

    @FXML
    private TableColumn<Flight, String> colFlightNumber;

    @FXML
    private TableColumn<Flight, String> colDestination;

    @FXML
    private TableColumn<Flight, LocalDateTime> colDeparture;

    @FXML
    private TableColumn<Flight, LocalTime> colDuration;

    @FXML
    private TextField txtFlightNumber;

    @FXML
    private TextField txtDestination;

    @FXML
    private TextField txtDepartures;

    @FXML
    private TextField txtDuration;

    @FXML
    private Button btnDelete;

    @FXML
    private ChoiceBox<String> filterFlight;

    @FXML
    private Button btnUpdate;

    /**
     * función al pulsar el botón Add valora si hay algun campo vacio,
     * si hay alguno se pone en rojo los textField y muestra un mensaje de error.
     * Si todo está OK entonces añade un nuevo vuelo.
     */
    @FXML
    void addFlight(ActionEvent event) {
        if (txtFlightNumber.getText().equals("") || txtDestination.getText().equals("")
                || txtDepartures.getText().equals("") || txtDuration.getText().equals("")) {
                enRojo();
            MessageUtils.showError("hay algún parámetro vacio");
            sinNada();
        } else {
            try {
                flight.add(
                        new Flight(
                                txtFlightNumber.getText(), txtDestination.getText(),
                                LocalDateTime.parse((txtDepartures.getText()), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                                LocalTime.parse((txtDuration.getText()), DateTimeFormatter.ofPattern("H:mm"))
                        )
                );
                enBlanco();
            }catch(Exception e){
                MessageUtils.showError("introduzca los carácteres tal y como se muestran");
            }
        }
    }

    /**
     * función para que al cerrarse Y SOLO al cerrarse
     * la información se guarde en el fichero, para evitar así posibles filtraciones
     */
    public void setOnCloseListener(Stage stage) {
        stage.setOnCloseRequest(e -> {
            FileUtils.saveFlights(flight);
        });
    }


    /**
     * Funcion de aplicación del filtro, al haber tantas opciones he decidido hacer un switch
     * El try catch sería para cuando no hay ningún filtro seleccionado.
     */
    @FXML
    void applyFilter(ActionEvent event) {
      try {
          ObservableList<Flight> flightSt = FXCollections.observableArrayList();
          String opcion;
          opcion = filterFlight.getValue();


          switch (opcion) {
              case "Show all flights":
                  tableFlight.setItems(flight);
                  btnDelete.setDisable(false);
                  btnUpdate.setDisable(false);
                  break;

              case "Show flights to currently selected city":
                  ObservableList<Flight> row;
                  row = tableFlight.getSelectionModel().getSelectedItems();
                  if (!row.isEmpty()) {
                      flightSt = flight.stream()
                              .filter(f -> f.getDestino().equals(txtDestination.getText()))
                              .collect(Collectors.toCollection(FXCollections::observableArrayList));
                      tableFlight.setItems(flightSt);
                      btnDelete.setDisable(true);
                      btnUpdate.setDisable(true);
                  } else {
                      MessageUtils.showError("Debes seleccionar una ciudad!");
                  }
                  break;

              case "Show next 5 flights":
                flightSt= flight.stream()
                        .filter(f -> f.salidaNOformateada().isAfter(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Flight::salidaNOformateada)).limit(5)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                    tableFlight.setItems(flightSt);
                  btnDelete.setDisable(true);
                  btnUpdate.setDisable(true);
                  break;

              case "Show long flights":

                  flightSt = flight.stream()
                          .filter(f -> f.duracionNOformateado().isAfter(LocalTime.parse("03:00")))
                          .collect(Collectors.toCollection(FXCollections::observableArrayList));
                  tableFlight.setItems(flightSt);
                  btnDelete.setDisable(true);
                  btnUpdate.setDisable(true);
                  break;

              case "Show flight duration average":
                  List<LocalTime> horas = flight.stream()
                          .map(Flight::duracionNOformateado)
                          .collect(Collectors.toList());

                  Integer hora;
                  Integer min;
                  Integer operacion;

                  List<Integer> resultado = new ArrayList<>();
                  for (LocalTime cadena : horas) {
                      hora = cadena.getHour();
                      min = cadena.getMinute();
                      operacion = (hora * 60) + min;
                      resultado.add(operacion);
                  }

                  Integer sumaMins = resultado.stream()
                          .mapToInt(m -> m)
                          .sum();

                  Integer cantidad = resultado.size();
                  Integer media = sumaMins / cantidad;
                  Integer sacarHoras = media / 60;
                  Integer sacarMin = media - (sacarHoras * 60);

                  LocalTime lt = LocalTime.of(sacarHoras, sacarMin);
                  MessageUtils.showMessage("La duración media de los vuelos es: "+lt);
                  btnDelete.setDisable(true);
                  btnUpdate.setDisable(true);
                  break;
          }
      }catch (Exception e){
          MessageUtils.showError("No puede dejar el filtro vacio si desea buscar");
      }
    }

    /**
     * Funcion de borrado, muestra todas las filas, luego selecionamos una fila, se abre una alerta de confirmación
     * si se confirma, entonces se borra la fila seleccionada.
     */
    @FXML
    void deleteRow(ActionEvent event) {
        ObservableList<Flight> row, allRows;
        allRows = tableFlight.getItems();
        row = tableFlight.getSelectionModel().getSelectedItems();
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Estás seguro que deseas borrar?");
        dialog.setContentText("Confirme si desea borrar");

        try{
            Optional<ButtonType> accion = dialog.showAndWait();
            if (accion.get() == ButtonType.OK) {
                row.forEach(allRows::remove);}
        }catch (Exception e){
            MessageUtils.showError("Si desea borrar, no filtre");
        }
    }

    /**
     * función de selección si la fila seleccionada tiene datos entonces se activan los botones de
     * borrado y edición. Si no hay nigún dato en la fila seleccionada entonces salta un error de seleccion.
     */
    @FXML
    void selection(MouseEvent event) {
        ObservableList<Flight> row;
        row = tableFlight.getSelectionModel().getSelectedItems();
        if (!row.isEmpty()) {
            txtFlightNumber.setText(row.get(0).getNumeroVuelo());
            txtDestination.setText(row.get(0).getDestino());
            txtDepartures.setText(String.valueOf(row.get(0).getSalida()));
            txtDuration.setText(String.valueOf(row.get(0).getDuracion()));
            if(filterFlight.getSelectionModel().isEmpty() || filterFlight.getValue().equals("Show all flights")){
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);}
        } else {
            MessageUtils.showError("No puede seleccionar una columna vacia");
        }
    }

    /**
     * Funcion de inicialización del programa, es lo que se muestra nada más abrir la ventana
     * 1) se cargan los vuelos
     * 2) las columnas adquieren el valor "numeroVuelo", "salida".....
     * 3) el desplegable adquiere las siguientes opciones: "show all flights", "show..." ....
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            flight = FXCollections.observableArrayList(
                    FileUtils.loadFlights()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        colFlightNumber.setCellValueFactory(new PropertyValueFactory("numeroVuelo"));
        colDeparture.setCellValueFactory(new PropertyValueFactory("salida"));
        colDestination.setCellValueFactory(new PropertyValueFactory("destino"));
        colDuration.setCellValueFactory(new PropertyValueFactory("duracion"));
        tableFlight.setItems(flight);

        filterFlight.setItems(
                FXCollections.observableArrayList(
                        "Show all flights", "Show flights to currently selected city", "Show long flights",
                        "Show next 5 flights", "Show flight duration average"
                )
        );
    }


    /**
     * Función para editar un vuelo ya introducido basándome en el índice de la tabla para saber cual
     * ha sido el vuelo seleccionado-
     */
    @FXML
    void update(ActionEvent event) {
        try{
        int selected = tableFlight.getSelectionModel().getSelectedIndex();
        Flight f = tableFlight.getSelectionModel().getSelectedItem();
        f.setNumeroVuelo(txtFlightNumber.getText());
        f.setDestino(txtDestination.getText());
        f.setSalida(LocalDateTime.parse((txtDepartures.getText()), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        f.setDuracion(LocalTime.parse((txtDuration.getText()), DateTimeFormatter.ofPattern("H:mm")));
        getFlights().set(selected,f);
        tableFlight.setItems(flight);
        enBlanco();
        }catch (Exception e){
            MessageUtils.showError("ha intentado actualizar los datos erroneos!");
        }

    }


    /**
     * Función para cargar la vista del gráfico.
     */
    @FXML
    void goToChartView(ActionEvent event) throws IOException {
        FileUtils.saveFlights(flight);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Chart.fxml"));
        Parent view1 = loader.load();
        Scene view1Scene = new Scene(view1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(view1Scene);
        stage.show();
    }

    /**
     * Funcion para poner en rojo los bordes de los textField
     */
    public void enRojo(){
        PseudoClass errorClass = PseudoClass.getPseudoClass("error");
        txtDepartures.pseudoClassStateChanged(errorClass, true);
        txtFlightNumber.pseudoClassStateChanged(errorClass, true);
        txtDuration.pseudoClassStateChanged(errorClass, true);
        txtDestination.pseudoClassStateChanged(errorClass, true);
    }

    /**
     * Funcion para poner normal los bordes de los textField
     */
    public void sinNada(){
        PseudoClass errorClass = PseudoClass.getPseudoClass("error");
        txtDepartures.pseudoClassStateChanged(errorClass, false);
        txtFlightNumber.pseudoClassStateChanged(errorClass, false);
        txtDuration.pseudoClassStateChanged(errorClass, false);
        txtDestination.pseudoClassStateChanged(errorClass, false);
    }

    /**
     * Funcion para poner en blanco los textField para poder seguir rellenando
     */
    public void enBlanco() {
        txtDuration.setText("");
        txtDepartures.setText("");
        txtDestination.setText("");
        txtFlightNumber.setText("");
        filterFlight.getSelectionModel().clearSelection();
    }

}



