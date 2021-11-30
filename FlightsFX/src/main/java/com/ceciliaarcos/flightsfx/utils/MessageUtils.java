package com.ceciliaarcos.flightsfx.utils;

import javafx.scene.control.Alert;


/**
 * Clase MensajeUtiles
 * @author Cecilia Arcos
 */




/**
 * MessageUtils sirve para mostrar diferentes avisos/errores
 */
public class MessageUtils {

    /**
     * @param message muestra el mensaje de error conveniente
     */
    public static void showError(String message){
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setTitle("Error");
        dialog.setHeaderText("Error adding data");
        dialog.setContentText(message);
        dialog.showAndWait();
    }

    /**
     * @param message muestra el mensaje de información elegido.
     */
    public static void showMessage(String message){
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Información");
        dialog.setHeaderText("Información de utilidad:");
        dialog.setContentText(message);
        dialog.showAndWait();

    }

}
