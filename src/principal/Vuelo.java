// Codigo sin excepciones

package principal;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

import excepciones.EPilotosInsuficientes;
import excepciones.EValorNegativo;
import excepciones.EValorNulo;
import util.IDAsign;
import util.Valida;

public class Vuelo implements Serializable{
    private final String id;
    private final Aeropuerto origen, destino;
    private LocalDateTime fechaHoraSalida, fechaHoraLlegada;
    private Avion avion;
    private String estadoVuelo; 
    private String puertaEmbarque;
    private TripulanteCabina[] tripulacion;
    private Piloto[] pilotos;
    private Reserva[] reservas;
    private double precio;
    private static int cont = 0;

    // Constructor
    public Vuelo(String id, Aeropuerto origen, Aeropuerto destino, LocalDateTime fechaHoraSalida, Avion avion,TripulanteCabina[] tripulacion, Piloto[] pilotos, double precio) throws EValorNulo, EPilotosInsuficientes, EValorNegativo{
        
    	Valida.validarTexto(id, "El id no puede estar vacío");

        if (origen == null)
            throw new EValorNulo("El aeropuerto de origen no puede estar vacío");
        if (destino == null)
            throw new EValorNulo("El aeropuerto de destino no puede estar vacío");
        if (avion == null)
            throw new EValorNulo("El avión no puede estar vacío");
        if (tripulacion == null || tripulacion.length == 0)
            throw new EValorNulo("La tripulación no puede ser nula");
        if (pilotos == null || pilotos.length == 0)
            throw new EValorNulo("Los pilotos no pueden ser nulo");  
        if(!hasPilotosMin())
        	throw new EPilotosInsuficientes();
        if(precio <= 0) throw new EValorNegativo("El precio no puede ser 0 o negativo");
        
    	this.id = IDAsign.asignar("VU", cont);
        this.origen = origen;
        this.destino = destino;
        this.fechaHoraSalida = fechaHoraSalida;
        this.fechaHoraLlegada = calcularHoraLlegada();
        this.avion = avion;
        this.estadoVuelo = "Programado";
        this.puertaEmbarque = null;
        this.tripulacion = tripulacion;
        this.pilotos = pilotos;
        this.reservas = new Reserva[0];
        this.precio = precio;
        
        cont++;
        
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
        this.fechaHoraLlegada = calcularHoraLlegada(); 
        this.atrasado();
    }
    public double getPrecio() {
    	return precio;
    }
    
    // Estado de vuelo
    public void cancelado(){this.estadoVuelo="Cancelado";}
    public void confirmado(){this.estadoVuelo="Confirmado";}
    public void atrasado(){this.estadoVuelo="Atrasado";}
    public void terminado(){this.estadoVuelo="Terminado";}

  //Getters
	public String getId() {return id;}
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
	
	// Tripulantes
	public boolean hasPilotosMin() {
		int minPilotos = 2;
		// TODO: Duracion mayor a 8 horas = 3
		return pilotos.length  >= minPilotos;
	}
	
	public boolean hasTripulacionMin() {
		return tripulacion.length >= (avion.getCapacidad() /50);
	}
	
	//TODO: Cambiar metodo para pilotos en general
	public void addPiloto(Piloto newCapitan) throws EPilotosInsuficientes {
		if(hasPilotosMin()) throw new EPilotosInsuficientes();
		if(pilotos[0] != null) throw new IllegalStateException("");
		pilotos[0] = newCapitan;
	}
	
	public void remPiloto(int i) {
	    if (!hasPilotosMin()) throw new IllegalStateException("");
	    
	    Piloto[] nuevo = new Piloto[pilotos.length - 1];
	    int j = 0;
	    for (int k = 0; k < pilotos.length; k++) {
	        if (k != i) {
	            nuevo[j++] = pilotos[k];
	        }
	    }
	    pilotos = nuevo;
	}
	
	public void addTripulante(TripulanteCabina newTripulante) {
		if(hasPilotosMin()) throw new IllegalStateException("");
		
		tripulacion = Arrays.copyOf(tripulacion, tripulacion.length+1);
		tripulacion[tripulacion.length-1] = newTripulante;
	}
	
	public void remTripulante(int i) {
	    if (!hasTripulacionMin()) throw new IllegalStateException("");
	    
	    TripulanteCabina[] nuevo = new TripulanteCabina[tripulacion.length - 1];
	    int j = 0;
	    for (int k = 0; k < tripulacion.length; k++) {
	        if (k != i) {
	            nuevo[j++] = tripulacion[k];
	        }
	    }
	    tripulacion = nuevo;
	}
	
	public int calcularDuracion() {
		double distancia = Math.sqrt(Math.pow((origen.getLatitud()-destino.getLatitud()),2)+Math.pow((origen.getLongitud()-destino.getLongitud()),2));
		distancia = Math.toRadians(distancia)*6371;
		return (int)((distancia/(avion.getVelocidad()*1.852))*60);
	}
	
	public LocalDateTime calcularHoraLlegada() {
		return fechaHoraSalida.plusMinutes(calcularDuracion());
	}
    
    
}

