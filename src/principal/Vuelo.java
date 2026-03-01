// Codigo sin excepciones

package principal;

import java.time.LocalDateTime;
import java.util.Arrays;

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

    public void setAtrasado(LocalDateTime NewSalida){
        this.fechaHoraSalida = NewSalida;
        this.fechaHoraLlegada = null; //TODO
        this.atrasado();
    }

    // Estado de vuelo
    public void cancelado(){this.estadoVuelo="Cancelado";}
    public void confirmado(){this.estadoVuelo="Confirmado";}
    public void atrasado(){this.estadoVuelo="Atrasado";}
    public void terminado(){this.estadoVuelo="Terminado";}

  //Getters
	public String getId() {return id;}
	public String getNumVuelo() {return numVuelo;}
	public Aeropuerto getOrigen() {return origen;}
	public Aeropuerto getDestino() {return destino;}
	public LocalDateTime getFechaHoraSalida() {return fechaHoraSalida;}
	public LocalDateTime getFechaHoraLlegada() {return fechaHoraLlegada;}
	public Avion getAvion() {return avion;}
	public String getEstadoVuelo() {return estadoVuelo;}
	public String getPuertaEmbarque() {return puertaEmbarque;}
	public TripulanteCabina[] getTripulacion() {return tripulacion;}
	public Piloto[] getPilotos() {return pilotos;}
	public Reserva[] getReservas() {return reservas;}
	
	
	
	// Reservas y Asientos
	
	// Tripulantes
	public boolean hasPilotosMin() {
		int minPilotos = 2;
		// if (ruta.getDuracion() > 8) minPilotos =3;
		return pilotos.length  >= minPilotos;
	}
	
	public boolean hasTripulacionMin() {
		return tripulacion.length >= (avion.getCapacidad() /50);
	}
	
	public void addCapitan(Piloto newCapitan) {
		if(hasPilotosMin()) throw new IllegalStateException("");
		if(pilotos[0] != null) throw new IllegalStateException("");
		pilotos[0] = newCapitan;
	}
	
	public void remCapitan() {
		if(!hasPilotosMin()) throw new IllegalStateException("");
		
		pilotos = Arrays.copyOfRange(pilotos, 1, pilotos.length)
	}
	
	public void addCopiloto(Piloto newCopiloto) {
		if(hasPilotosMin()) throw new IllegalStateException("");
		if(pilotos[1] != null) throw new IllegalStateException("");
		pilotos[1] = newCopiloto;
	}
	
	public void remCopiloto() {
		pilotos[0] = null;
	}
	
	
	
	public void addTripulante(TripulanteCabina newTripulante) {
		if(hasPilotosMin()) throw new IllegalStateException("");
		
		tripulacion = Arrays.copyOf(tripulacion, tripulacion.length+1);
		tripulacion[tripulacion.length-1] = newTripulante;
	}

	
    
    
}

