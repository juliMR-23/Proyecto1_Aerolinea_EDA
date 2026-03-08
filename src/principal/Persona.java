package principal;

import java.io.Serializable;

import excepciones.EValorNulo;
import util.Valida;

abstract class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    // ATRIBUTOS
    protected String id;
    protected String nombre;
    protected String tipoDocumento;
    protected String documento;
    protected String telefono;
    protected String email;
    protected String password;

    // CONSTRUCTOR
    public Persona(String id, String nombre, String tipoDocumento, String documento,
                   String telefono, String email)throws EValorNulo{

    	Valida.validarTexto(id, "El id no puede ser null ni vacío");
    	Valida.validarTexto(nombre, "El nombre no puede ser null ni vacío");
    	Valida.validarTexto(tipoDocumento, "El tipo de documento no puede ser null ni vacío");
    	Valida.validarTexto(documento, "El documento no puede ser null ni vacío");
    	Valida.validarTexto(telefono, "El teléfono no puede ser null ni vacío");
    	Valida.validarTexto(email, "El email no puede ser null ni vacío");       	

        this.id = id;
        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.telefono = telefono;
        this.email = email;
    }

    // GETTERS
    public String getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getTipoDocumento() {
        return this.tipoDocumento;
    }

    public String getDocumento() {
        return this.documento;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public String getEmail() {
        return this.email;
    }

    // SETTERS
    public void setNombre(String nombre) throws EValorNulo {
    	Valida.validarTexto(nombre, "El nombre no puede ser null ni vacío");
        this.nombre = nombre;
    }

    public void setTipoDocumento(String tipoDocumento) throws EValorNulo {
    	Valida.validarTexto(tipoDocumento, "El tipo de documento no puede ser null ni vacío");
    	this.tipoDocumento = tipoDocumento;
    }

    public void setDocumento(String documento) throws EValorNulo {
    	Valida.validarTexto(documento, "El documento no puede ser null ni vacío");
    	this.documento = documento;
    }

    public void setTelefono(String telefono) throws EValorNulo {
    	Valida.validarTexto(telefono, "El teléfono no puede ser null ni vacío");
        this.telefono = telefono;
    }

    public void setEmail(String email) throws EValorNulo {
    	Valida.validarTexto(email, "El email no puede ser null ni vacío");
    	this.email = email;
    }
}
