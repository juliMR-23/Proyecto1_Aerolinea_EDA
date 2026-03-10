package principal;

import java.io.IOException;
import java.io.Serializable;

import excepciones.EInvalidDocumento;
import excepciones.EInvalidEmail;
import excepciones.EInvalidPass;
import excepciones.EInvalidTelefono;
import excepciones.EValorNulo;
import util.IDAsign;
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
    protected boolean isActive;

    // CONSTRUCTOR
    public Persona(String nombre, String tipoDocumento, String documento,
                   String telefono, String email, String password) throws EValorNulo, EInvalidPass, EInvalidTelefono, EInvalidEmail, EInvalidDocumento{

    	Valida.validarTexto(nombre, "El nombre no puede ser null ni vacío");
    	Valida.validarTexto(tipoDocumento, "El tipo de documento no puede ser null ni vacío");
    	
    	validarDocumento(documento);
    	validarTelefono(telefono);
    	validarEmail(email); 
    	validarPassword(password);
    	
    	this.id=IDAsign.asignar("PE",Aerolinea.getCont());
        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.isActive=true;
        Aerolinea.aumentaCont();
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

    public String getPassword() {
        return this.password;
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

    public void setTelefono(String telefono) throws EValorNulo, EInvalidTelefono {
    	validarTelefono(telefono);
        this.telefono = telefono;
    }

    public void setEmail(String email) throws EValorNulo, EInvalidEmail {
    	validarEmail(email); 
    	this.email = email;
    }
    
    public void setPassword(String password) throws EValorNulo, EInvalidPass {
    	validarPassword(password);
    	this.password = password;
    }
    
    private void validarDocumento(String documento) throws EValorNulo, EInvalidDocumento {
        Valida.validarTexto(documento, "El documento no puede ser null ni vacío");
        if(!documento.matches("[0-9]+"))
            throw new EInvalidDocumento("El documento solo puede contener números");
        if (documento.length()<7 || documento.length()>10)
            throw new EInvalidDocumento("El documento debe tener entre 7 y 10 dígitos");
    }
    
    private void validarTelefono(String telefono) throws EValorNulo, EInvalidTelefono {
        Valida.validarTexto(telefono, "El teléfono no puede ser null ni vacío");
        if(!telefono.matches("[0-9]+"))
            throw new EInvalidTelefono("El teléfono solo puede contener números");
        if (telefono.length()<7 || telefono.length()>10)
            throw new EInvalidTelefono("El teléfono debe tener entre 7 y 10 dígitos");
    }
    
    private void validarEmail(String email) throws EValorNulo, EInvalidEmail {
    	Valida.validarTexto(email, "El email no puede ser null ni vacío"); 
    	if(email.length()<8)
    		throw new EInvalidEmail("El email debe tener al menos 8 caracteres");
    	if (!email.matches(".+@.+\\..+"))//regex
            throw new EInvalidEmail("Formato inválido para email");
    }
    
    private void validarPassword(String password) throws EValorNulo, EInvalidPass {
    	Valida.validarTexto(password, "La contraseña no puede ser null ni vacía");
    	if(password.length()<6)
    		throw new EInvalidPass("La contraseña debe tener al menos 6 caracteres");
    	if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+"))//regex
            throw new EInvalidPass("La contraseña debe contener mayúsculas, minúsculas y números");
    }
    
    public void wFicheroPersona(String dir) throws IOException {

    }
    
    public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
    
}
