package principal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import excepciones.EValorNegativo;
import excepciones.EValorNulo;

import util.Valida;

public class Avion implements Serializable{
	private String matricula;
	private String marca;
	private String modelo;
	private int capacidad;//cantidad de pasajeros que puede llevar
	private boolean disponible;
	//velocidad Ground Speed en NUDOS
	private double velocidad;
	private static final long serialVersionUID = 1L;
	
	public Avion(String matricula, String marca, String modelo, int capacidad, boolean disponible, double velocidad) throws EValorNulo, EValorNegativo {
		Valida.validarTexto(matricula, "La matrícula no puede estar vacía");
		Valida.validarTexto(marca, "La marca no puede estar vacía");
		Valida.validarTexto(modelo, "El modelo no puede estar vacío");
		if(capacidad<=0)
	    	throw new EValorNegativo("La capacidad debe ser mayor a cero");
		if(velocidad<=0)
	    	throw new EValorNegativo("La velocidad debe ser mayor a cero");
	    
		this.matricula = matricula;
		this.marca = marca;
		this.modelo = modelo;
		this.capacidad = capacidad;
		this.disponible = disponible;
		this.velocidad = velocidad;
	}
	
	public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) throws EValorNulo {
    	Valida.validarTexto(matricula, "La matrícula no puede estar vacía");
        this.matricula = matricula;
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) throws EValorNulo {
		Valida.validarTexto(marca, "La marca no puede estar vacía");
		this.marca = marca;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) throws EValorNulo {
        Valida.validarTexto(modelo, "El modelo no puede estar vacío");
        this.modelo = modelo;
    }
    public int getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(int capacidad) throws EValorNegativo {
        if (capacidad <= 0)
            throw new EValorNegativo("La capacidad debe ser mayor a cero");
        this.capacidad = capacidad;
    }
	public boolean isDisponible() {
		return disponible;
	}
	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	
	public double getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(double velocidad) throws EValorNegativo {
		if (velocidad <= 0)
            throw new EValorNegativo("La velocidad debe ser mayor a cero");
		this.velocidad = velocidad;
	}
	
	public void wFicheroAvion(String dir) throws IOException {
		FileOutputStream f = new FileOutputStream(dir);
		ObjectOutputStream b = new ObjectOutputStream(f);
		b.writeObject((Avion)this);
		f.close();
		b.close();
	}
	public static Avion rFicheroAvion(String dir) throws IOException, ClassNotFoundException, EValorNulo {
		Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
	    FileInputStream f = new FileInputStream(dir);
	    ObjectInputStream b = new ObjectInputStream(f);
	    Avion a = (Avion) b.readObject();
	    f.close();
	    b.close();
	    return a;
	}
	
}
