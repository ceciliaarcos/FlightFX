package com.ceciliaarcos.flightsfx;

import com.ceciliaarcos.flightsfx.model.Flight;
import com.ceciliaarcos.flightsfx.utils.MessageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Clase controlador del gráfico
 * @author Cecilia Arcos
 */

public class ChartController {

    @FXML
    private PieChart pieChart;

    /**
     * @param event
     * @throws IOException
     * Esta funcion llama al archivo del menú principal para poder volver atrás.
     */

    @FXML
    private void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("FXMLMainView.fxml"));
        Parent view1 = loader.load();
        Scene view1Scene = new Scene(view1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(view1Scene);
        stage.show();
    }


    /**
     * Funcion para inicializar el gráfico.
     * Recoge información del archivo (FXMLMainView)
     * para poder mostrarlo en forma de gráfico
     */
    public void initialize() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLMainView.fxml"));
        try {
            Parent root = (Parent) loader.load();
        } catch (Exception e) {
            MessageUtils.showError("Ha surgido un error al cargar!");
        }
        FXMLMainViewController controller = (FXMLMainViewController) loader.getController();
        List<Flight> flights = controller.getFlights();

        pieChart.getData().clear();

        Map<String, Long> result;
        result = flights.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getDestino(),
                        Collectors.counting()
                ));

        result.forEach((cat, sum) -> {
            pieChart.getData().add(new PieChart.Data(cat, sum));
        });
    }
}