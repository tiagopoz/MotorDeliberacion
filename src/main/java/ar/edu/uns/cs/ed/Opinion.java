package ar.edu.uns.cs.ed;

public class Opinion {
    private String idArgumento;
    private String texto;
    private String postura; // "A FAVOR", "EN CONTRA", "ROOT"
    private Usuario usuario;

    public Opinion() {
    }

    public Opinion(String idArgumento, String texto, String postura, Usuario usuario) {
        this.idArgumento = idArgumento;
        this.texto = texto;
        this.postura = postura;
        this.usuario = usuario;
    }

    public String getIdArgumento() {
        return idArgumento;
    }

    public void setIdArgumento(String idArgumento) {
        this.idArgumento = idArgumento;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getPostura() {
        return postura;
    }

    public void setPostura(String postura) {
        this.postura = postura;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
