package principal;

abstract class Persona {
	
	// ATRIBUTOS
	private String id;
	private String nombre;
	private String tipoDocumento;
	private String documento;
	private String telefono;
	private String email;
	
	
	// CONSTRUCTOR
	public Persona (String id, String nombre, String tipoDocumento, String documento, String telefono, String email) {
		this.id = id;
		this.nombre = nombre;
		this.tipoDocumento = tipoDocumento;
		this.documento = documento;
		this.telefono = telefono;
		this.email = email;
	}
	
	
	// METODOS
	public void actualizarDatosContacto(String telefono, String mail) {
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
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public void setTelefono (String telefono) {
		this.telefono = telefono;
	}
	
	public void setEmail (String email) {
		this.email = email;
	}

}
