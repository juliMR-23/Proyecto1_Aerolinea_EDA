package principal;

import java.io.Serializable;

import excepciones.EPersonaInvalida;

abstract class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    // ATRIBUTOS
    protected String id;
    protected String nombre;
    protected String tipoDocumento;
    protected String documento;
    protected String telefono;
    protected String email;

    // CONSTRUCTOR
    public Persona(String id, String nombre, String tipoDocumento, String documento,
                   String telefono, String email) throws EPersonaInvalida {

    	if (id == null || nombre == null || tipoDocumento == null || documento == null || telefono == null || email == null) {
    		throw new EPersonaInvalida("No pueden haber valores null");
    	}

    	if (id.isEmpty() || nombre.isEmpty() || tipoDocumento.isEmpty() || documento.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
    		throw new EPersonaInvalida("No pueden haber valores vacíos");
    	}

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
    public void setNombre(String nombre) throws EPersonaInvalida {
        if (nombre == null || nombre.isEmpty()) {
            throw new EPersonaInvalida("El nombre no puede ser nulo ni vacío");
        }
        this.nombre = nombre;
    }

    public void setTipoDocumento(String tipoDocumento) throws EPersonaInvalida {
        if (tipoDocumento == null || tipoDocumento.isEmpty()) {
            throw new EPersonaInvalida("El tipo de documento no puede ser nulo ni vacío");
        }
        this.tipoDocumento = tipoDocumento;
    }

    public void setDocumento(String documento) throws EPersonaInvalida {
        if (documento == null || documento.isEmpty()) {
            throw new EPersonaInvalida("El documento no puede ser nulo ni vacío");
        }
        this.documento = documento;
    }

    public void setTelefono(String telefono) throws EPersonaInvalida {
        if (telefono == null || telefono.isEmpty()) {
            throw new EPersonaInvalida("El teléfono no puede ser nulo ni vacío");
        }
        this.telefono = telefono;
    }

    public void setEmail(String email) throws EPersonaInvalida {
        if (email == null || email.isEmpty()) {
            throw new EPersonaInvalida("El email no puede ser nulo ni vacío");
        }
        this.email = email;
    }
}
