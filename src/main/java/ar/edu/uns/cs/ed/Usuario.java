package ar.edu.uns.cs.ed;

public class Usuario {
    private String id;
    private String nombreUsuario;

    public Usuario() {
    }

    public Usuario(String id, String nombreUsuario) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
