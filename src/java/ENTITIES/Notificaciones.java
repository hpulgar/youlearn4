/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ENTITIES;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felipe
 */
@Entity
@Table(name = "notificaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notificaciones.findAll", query = "SELECT n FROM Notificaciones n"),
    @NamedQuery(name = "Notificaciones.findByIdNotificacion", query = "SELECT n FROM Notificaciones n WHERE n.idNotificacion = :idNotificacion"),
    @NamedQuery(name = "Notificaciones.findByTipo", query = "SELECT n FROM Notificaciones n WHERE n.tipo = :tipo"),
    @NamedQuery(name = "Notificaciones.findByDetalle", query = "SELECT n FROM Notificaciones n WHERE n.detalle = :detalle"),
    @NamedQuery(name = "Notificaciones.findByFecha", query = "SELECT n FROM Notificaciones n WHERE n.fecha = :fecha"),
    @NamedQuery(name = "Notificaciones.findByLeida", query = "SELECT n FROM Notificaciones n WHERE n.leida = :leida"),
    @NamedQuery(name = "Notificaciones.findByIdusuario", query = "SELECT n FROM Notificaciones n WHERE n.leida=0"),
    @NamedQuery(name = "Notificaciones.findBySuscrito", query = "SELECT n FROM InscripcionCurso n WHERE n.idUsuario.idUsuario = :idUsuario AND n.idCurso.idCurso = :idCurso AND n.tipoAlumno.idTipo =2"),
    @NamedQuery(name = "Notificaciones.findBySeguidor", query = "SELECT n FROM InscripcionCurso n WHERE n.idUsuario.idUsuario = :idUsuario AND n.idCurso.idCurso = :idCurso AND n.tipoAlumno.idTipo =1"),
    @NamedQuery(name = "Notificaciones.findByIdentificador", query = "SELECT n FROM Notificaciones n WHERE n.identificador = :identificador")})
public class Notificaciones implements Serializable {

    @Column(name = "id_usuario_trigger")
    private Integer idUsuarioTrigger;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_notificacion")
    private Integer idNotificacion;
    @Size(max = 45)
    @Column(name = "tipo")
    private String tipo;
    @Size(max = 300)
    @Column(name = "detalle")
    private String detalle;
    @Size(max = 90)
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "leida")
    private Integer leida;
    @Column(name = "identificador")
    private Integer identificador;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuario;

    public Notificaciones() {
    }

    public Notificaciones(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Integer getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getLeida() {
        return leida;
    }

    public void setLeida(Integer leida) {
        this.leida = leida;
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Integer identificador) {
        this.identificador = identificador;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNotificacion != null ? idNotificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notificaciones)) {
            return false;
        }
        Notificaciones other = (Notificaciones) object;
        if ((this.idNotificacion == null && other.idNotificacion != null) || (this.idNotificacion != null && !this.idNotificacion.equals(other.idNotificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ENTITIES.Notificaciones[ idNotificacion=" + idNotificacion + " ]";
    }

    public Integer getIdUsuarioTrigger() {
        return idUsuarioTrigger;
    }

    public void setIdUsuarioTrigger(Integer idUsuarioTrigger) {
        this.idUsuarioTrigger = idUsuarioTrigger;
    }
    
}
