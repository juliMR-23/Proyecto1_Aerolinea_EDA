// Codigo sin excepciones

package principal;

import java.time.LocalDateTime;

public class Vuelo {
    private final String id;
    private final String numVuelo;
    private final Aeropuerto origen, destino;
    private LocalDateTime fechaHoraSalida;
    private LocalDateTime fechaHoraLlegada;
    private Avion avion;
    private String estadoVuelo; //enum?
    private String puertaEmbarque;
    private TripulanteCabina[] tripulacion;
    private Piloto[] pilotos;
    private Reserva[] reservas;

    // Constructor
    public Vuelo(Aeropuerto origen, Aeropuerto destino, LocalDateTime fechaHoraSalida, Avion avion,TripulanteCabina[] tripulacion, Piloto[] pilotos){
        this.id = null; //TODO
        this.numVuelo = null; //TODO
        this.origen = origen;
        this.destino = destino;
        this.fechaHoraSalida = fechaHoraSalida;
        this.fechaHoraLlegada = null; //TODO
        this.avion = avion;
        this.estadoVuelo = "Programado";
        this.puertaEmbarque = null;
        this.tripulacion = tripulacion;
        this.pilotos = pilotos;
        this.reservas = new Reserva[0];
    }

    // Setters
    public void setAvion(Avion avion){
        this.avion = avion;
    }

    public void setPuertaEmbarque(String puertaEmbarque){
        this.puertaEmbarque = puertaEmbarque;
        this.confirmado();
    }

    public void setAtrado(LocalDateTime NewSalida){
        this.fechaHoraSalida = NewSalida;
        this.fechaHoraLlegada = null; //TODO
        this.atrasado();
    }

    // Estado de vuelo
    public void cancelado(){this.estadoVuelo="Cancelado";}
    public void confirmado(){this.estadoVuelo="Confirmado";}
    public void atrasado(){this.estadoVuelo="Atrasado";}
    public void terminado(){this.estadoVuelo="Terminado";}
}

