package com.ceciliaarcos.flightsfx.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase Vuelos
 * @author Cecilia Arcos
 */

/**
 * es una clase creada para administrar vuelos, crear, modoficar, borrar y leer sus atributos.
 */

public class Flight {
    private String numeroVuelo;
    private String destino;
    private LocalDateTime salida;
    private LocalTime duracion;

    public Flight(String numeroVuelo){
        this.numeroVuelo = numeroVuelo;
    }

    public Flight (String numeroVuelo, String destino, LocalDateTime salida, LocalTime duracion){
        this.numeroVuelo=numeroVuelo;
        this.destino=destino;
        this.salida=salida;
        this.duracion=duracion;
    }

    public Flight(){}

    public LocalTime duracionNOformateado(){
        return this.duracion;
    }
    public LocalDateTime salidaNOformateada() { return this.salida;}

    public String getSalida() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return salida.format(formatter);
    }

    public String getDuracion() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        return duracion.format(formatter);
    }

    public String getDestino() {
        return destino;
    }

    public String getNumeroVuelo() {
        return numeroVuelo;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setDuracion(LocalTime duracion) {
        this.duracion = duracion;
    }

    public void setNumeroVuelo(String numeroVuelo) {
        this.numeroVuelo = numeroVuelo;
    }

    public void setSalida(LocalDateTime salida) {
        this.salida = salida;
    }

    @Override
    public String toString() {
        return numeroVuelo + ';' + destino + ';' + getSalida() + ';'+ getDuracion() ;
    }

}
