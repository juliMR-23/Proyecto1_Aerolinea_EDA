

package principal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

import excepciones.EEmpleadosInsuficientes;
import excepciones.EValorNulo;
import util.IDAsign;
import util.Valida;

public class Vuelo implements Serializable{

	private static final long serialVersionUID = 1L;
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

    // Constructor
    public Vuelo(Aeropuerto origen, Aeropuerto destino, LocalDateTime fechaHoraSalida, Avion avion,TripulanteCabina[] tripulacion, Piloto[] pilotos) throws EValorNulo, EEmpleadosInsuficientes{

        if (origen == null)
            throw new EValorNulo("El aeropuerto de origen no puede estar vacío");
        if (destino == null)
            throw new EValorNulo("El aeropuerto de destino no puede estar vacío");
        if (avion == null)
            throw new EValorNulo("El avión no puede ser nulo");
        if (tripulacion == null || tripulacion.length == 0)
            throw new EValorNulo("La tripulación no puede ser nula");
        if (pilotos == null || pilotos.length == 0)
            throw new EValorNulo("Los pilotos no pueden ser nulo"); 
        if(!hasPilotosMin())
        	throw new EEmpleadosInsuficientes("pilotos");
        if (!hasTripulacionMin())
	        throw new EEmpleadosInsuficientes("tripulantes de cabina");

        
    	this.id = IDAsign.asignar("VU", Aerolinea.getCont());
        this.origen = origen;
        this.destino = destino;
        this.fechaHoraSalida = fechaHoraSalida;
        this.avion = avion;
        this.fechaHoraLlegada = calcularHoraLlegada();
        this.estadoVuelo = "Programado";
        this.puertaEmbarque = null;
        this.tripulacion = tripulacion;
        this.pilotos = pilotos;
        this.reservas = new Reserva[0];
        precio=500+calcularDuracion();
        Aerolinea.aumentaCont();
    }

 // Setters
    public void setAvion(Avion avion) throws EValorNulo{
        if (avion == null)
            throw new EValorNulo("El avión no puede ser nulo");
        this.avion = avion;
    }

    public void setPuertaEmbarque(String puertaEmbarque) throws EValorNulo{
    	Valida.validarTexto(puertaEmbarque, "La puerta de embarque no puede ser nula o vacía");
        this.puertaEmbarque = puertaEmbarque;
        this.confirmado();
    }

    public void setAtrasado(LocalDateTime newSalida) throws EValorNulo{
    	if (newSalida == null)
            throw new EValorNulo("La nueva fecha de salida no puede ser nula");
        this.fechaHoraSalida = newSalida;
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
	
	
	public void addPiloto(Piloto nuevoPiloto) throws EValorNulo{
        if (nuevoPiloto == null)
            throw new EValorNulo("El piloto no puede ser nulo");
        if(indexPiloto(nuevoPiloto.getId())==-1){
        	pilotos = Arrays.copyOf(pilotos, pilotos.length + 1);
            pilotos[pilotos.length - 1] = nuevoPiloto;
        }
    }
	public int indexPiloto(String id) {
        int n = 0;
        while(n < pilotos.length && !pilotos[n].getId().equalsIgnoreCase(id)) {
            n++;
        }
        if(n < pilotos.length)
            return n;
        return -1;
    }

	
	public void deletePiloto(String id) throws EEmpleadosInsuficientes {
	    if (!hasPilotosMin()) throw new EEmpleadosInsuficientes("pilotos");
	    if(pilotos.length-1>=2) {
	    	int i = indexPiloto(id);
	        if (i != -1) {
	            for (int j = i; j < pilotos.length-1; j++)
	                pilotos[j] = pilotos[j + 1];
	            
	            pilotos = Arrays.copyOf(pilotos, pilotos.length -1);
	        }
	    } 
	}
	// Buscar índice de un tripulante por ID
	public int indexTripulante(String id) {
	    int n = 0;
	    while (n < tripulacion.length && !tripulacion[n].getId().equalsIgnoreCase(id)) {
	        n++;
	    }
	    if (n < tripulacion.length)
	        return n;
	    return -1;
	}

	public void addTripulante(TripulanteCabina nuevoTripulante) throws EValorNulo {
	    if (nuevoTripulante == null)
	        throw new EValorNulo("El tripulante no puede ser nulo");

	    if (indexTripulante(nuevoTripulante.getId())== -1) {
	        tripulacion = Arrays.copyOf(tripulacion, tripulacion.length + 1);
	        tripulacion[tripulacion.length - 1] = nuevoTripulante;
	    }
	}


	public void deleteTripulante(String id) throws EEmpleadosInsuficientes {
	    if (!hasTripulacionMin())
	        throw new EEmpleadosInsuficientes("tripulantes de cabina");

	    int i = indexTripulante(id);
	    if (i != -1) {
	        for (int j = i; j < tripulacion.length - 1; j++)
	            tripulacion[j] = tripulacion[j + 1];
	        tripulacion = Arrays.copyOf(tripulacion, tripulacion.length - 1);
	    }
	}
	
	public int calcularDuracion() {
		double distancia = Math.sqrt(Math.pow((origen.getLatitud()-destino.getLatitud()),2)+Math.pow((origen.getLongitud()-destino.getLongitud()),2));
		distancia = Math.toRadians(distancia)*6371;
		return (int)((distancia/(avion.getVelocidad()*1.852))*72+30);
	}
	
	public LocalDateTime calcularHoraLlegada() {
		return fechaHoraSalida.plusMinutes(calcularDuracion());
	}
    

	public void copiarFicheroVuelo(String dir) throws IOException, EValorNulo {
	    Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
	    FileOutputStream f = new FileOutputStream(dir);
	    ObjectOutputStream b = new ObjectOutputStream(f);
	    b.writeObject((Vuelo)this);
	    b.close();
	    f.close();
	}

	public static Vuelo leerFicheroVuelo(String dir) throws IOException, ClassNotFoundException, EValorNulo {
	    Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
	    FileInputStream f = new FileInputStream(dir);
	    ObjectInputStream b = new ObjectInputStream(f);
	    Vuelo vuelo = (Vuelo) b.readObject();
	    b.close();
	    f.close();
	    return vuelo;
	}
}

