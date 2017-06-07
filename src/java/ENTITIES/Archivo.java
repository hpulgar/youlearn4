/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ENTITIES;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Felipe
 */
@Entity
@Table(name = "archivo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Archivo.findAll", query = "SELECT a FROM Archivo a"),
    @NamedQuery(name = "Archivo.findByIdArchivo", query = "SELECT a FROM Archivo a WHERE a.idArchivo = :idArchivo"),
    @NamedQuery(name = "Archivo.findByIdAux", query = "SELECT a FROM Archivo a WHERE a.idAux = :idAux"),
    @NamedQuery(name = "Archivo.findByNomArchivo", query = "SELECT a FROM Archivo a WHERE a.nomArchivo = :nomArchivo"),
    @NamedQuery(name = "Archivo.findByUbicacion", query = "SELECT a FROM Archivo a WHERE a.ubicacion = :ubicacion"),
    @NamedQuery(name = "Archivo.findByAutorizado", query = "SELECT a FROM Archivo a WHERE a.autorizado = :autorizado"),
    @NamedQuery(name = "Archivo.findByNoAprobadoTodos", query = "SELECT c FROM Archivo c WHERE c.autorizado = 0"),
    @NamedQuery(name = "Archivo.findByAprobadoTodos", query = "SELECT c FROM Archivo c WHERE c.autorizado = 1"),
    @NamedQuery(name = "Archivo.findByIdentyAux", query = "SELECT a FROM Archivo a WHERE a.idIdentificadorArchivo.idIdentificadorArchivo = :idIdentificadorArchivo AND a.idAux = :idAux"),
    @NamedQuery(name = "Archivo.idcont", query = "SELECT a FROM Archivo a WHERE a.idContenido.idContenido = :idContenido"),
    @NamedQuery(name = "Archivo.findByIdContenido", query = "SELECT a FROM Archivo a WHERE a.idIdentificadorArchivo.idIdentificadorArchivo = :idIdentificador AND a.idUsuario = :idUsuario"),
    @NamedQuery(name = "Archivo.findByFecha", query = "SELECT a FROM Archivo a WHERE a.fecha = :fecha")})
public class Archivo implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idArchivo")
    private List<LogSubidas> logSubidasList;
    @JoinColumn(name = "id_contenido", referencedColumnName = "id_contenido")
    @ManyToOne
    private Contenidos idContenido;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_archivo")
    private Integer idArchivo;
    @Column(name = "id_aux")
    private Integer idAux;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nom_archivo")
    private String nomArchivo;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "ubicacion")
    private String ubicacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "autorizado")
    private boolean autorizado;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "id_identificador_archivo", referencedColumnName = "id_identificador_archivo")
    @ManyToOne(optional = false)
    private IdentificadorArchivo idIdentificadorArchivo;
    @JoinColumn(name = "id_tipo_archivo", referencedColumnName = "id_tipo")
    @ManyToOne(optional = false)
    private TipoArchivo idTipoArchivo;

    public Archivo() {
    }

    public Archivo(Integer idArchivo) {
        this.idArchivo = idArchivo;
    }

    public Archivo(Integer idArchivo, String nomArchivo, String descripcion, String ubicacion, boolean autorizado) {
        this.idArchivo = idArchivo;
        this.nomArchivo = nomArchivo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.autorizado = autorizado;
    }

    public Integer getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(Integer idArchivo) {
        this.idArchivo = idArchivo;
    }

    public Integer getIdAux() {
        return idAux;
    }

    public void setIdAux(Integer idAux) {
        this.idAux = idAux;
    }

    public String getNomArchivo() {
        return nomArchivo;
    }

    public void setNomArchivo(String nomArchivo) {
        this.nomArchivo = nomArchivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(boolean autorizado) {
        this.autorizado = autorizado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public IdentificadorArchivo getIdIdentificadorArchivo() {
        return idIdentificadorArchivo;
    }

    public void setIdIdentificadorArchivo(IdentificadorArchivo idIdentificadorArchivo) {
        this.idIdentificadorArchivo = idIdentificadorArchivo;
    }

    public TipoArchivo getIdTipoArchivo() {
        return idTipoArchivo;
    }

    public void setIdTipoArchivo(TipoArchivo idTipoArchivo) {
        this.idTipoArchivo = idTipoArchivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArchivo != null ? idArchivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Archivo)) {
            return false;
        }
        Archivo other = (Archivo) object;
        if ((this.idArchivo == null && other.idArchivo != null) || (this.idArchivo != null && !this.idArchivo.equals(other.idArchivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ENTITIES.Archivo[ idArchivo=" + idArchivo + " ]";
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    @XmlTransient
    public List<LogSubidas> getLogSubidasList() {
        return logSubidasList;
    }

    public void setLogSubidasList(List<LogSubidas> logSubidasList) {
        this.logSubidasList = logSubidasList;
    }

    public Contenidos getIdContenido() {
        return idContenido;
    }

    public void setIdContenido(Contenidos idContenido) {
        this.idContenido = idContenido;
    }
    
}
