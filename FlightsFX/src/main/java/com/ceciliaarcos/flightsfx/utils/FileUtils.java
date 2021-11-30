package com.ceciliaarcos.flightsfx.utils;

import com.ceciliaarcos.flightsfx.FXMLMainViewController;
import com.ceciliaarcos.flightsfx.model.Flight;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Clase Archivos útiles
 * @author Cecilia Arcos
 */


/**
 * FileUtils es una clase creada para recuperar y guardar info en un archivo de texto.
 * Es un conjunto de métodos estáticos de utilidad.
 *
 */
public class FileUtils {

    /**
     * @return cargar el archivo (flights.txt) quitando la separación ; y convirtiendo las fechas
     * y horas en formato más amigable.
     */
    public static List<Flight> loadFlights() {

            try {
                return Files.lines(Paths.get("flights.txt"))
                        .map(line -> new Flight(line.split(";")[0],
                                line.split(";")[1],
                                LocalDateTime.parse((line.split(";")[2]), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                                LocalTime.parse((line.split(";")[3]), DateTimeFormatter.ofPattern("H:mm"))))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

    }


    /**
     * @param flight es la lista NO filtrada que contiene todos los datos introducidos correctamente
     *               y está lista para ser guardada en la el fichero (flights.txt).
     */
    public static void saveFlights(List<Flight> flight){
        try (PrintWriter pw= new PrintWriter("flights.txt")){
            flight.stream().forEach(f->pw.println(f.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
