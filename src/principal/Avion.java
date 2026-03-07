package principal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import excepciones.EValorNegativo;
import excepciones.EValorNulo;

public class Avion implements Serializable{
	private String matricula;
	private String marca;
	private String modelo;
	private int capacidad;//cantidad de pasajeros que puede llevar
	private boolean disponible;
	
	public Avion(String matricula, String marca, String modelo, int capacidad, boolean disponible) throws EValorNulo, EValorNegativo {
		validarTexto(matricula, "La matrícula no puede estar vacía");
        validarTexto(marca, "La marca no puede estar vacía");
        validarTexto(modelo, "El modelo no puede estar vacío");
		if(capacidad<=0)
	    	throw new EValorNegativo("La capacidad debe ser mayor a cero");
	    
		this.matricula = matricula.replace(" ", "");
		this.marca = marca;
		this.modelo = modelo;
		this.capacidad = capacidad;
		this.disponible = disponible;
	}
	
	public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) throws EValorNulo {
        validarTexto(matricula, "La matrícula no puede estar vacía");
        this.matricula = matricula.replace(" ", "");
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) throws EValorNulo {
        validarTexto(marca, "La marca no puede estar vacía");
        this.marca = marca;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) throws EValorNulo {
        validarTexto(modelo, "El modelo no puede estar vacío");
        this.modelo = modelo;
    }
    public int getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(int capacidad) throws EValorNegativo {
        if (capacidad <= 0)
            throw new EValorNegativo("La capacidad no puede ser menor a cero");
        this.capacidad = capacidad;
    }
	public boolean isDisponible() {
		return disponible;
	}
	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	private void validarTexto(String valor, String msg) throws EValorNulo {
	    if (valor == null || valor.trim().isEmpty())
	        throw new EValorNulo(msg);
	}
	
	public void wFicheroAvion(String dir) throws IOException {
		FileOutputStream f = new FileOutputStream(dir);
		ObjectOutputStream b = new ObjectOutputStream(f);
		b.writeObject((Avion)this);
		b.close();
		f.close();
	}
	public static Avion rFicheroAvion(String dir) throws IOException, ClassNotFoundException {
	    FileInputStream f = new FileInputStream(dir);
	    ObjectInputStream b = new ObjectInputStream(f);
	    Avion a = (Avion) b.readObject();
	    f.close();
	    b.close();
	    return a;
	}
	
}
