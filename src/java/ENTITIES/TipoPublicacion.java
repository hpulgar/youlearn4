/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ENTITIES;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Felipe
 */
@Entity
@Table(name = "tipo_publicacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPublicacion.findAll", query = "SELECT t FROM TipoPublicacion t"),
    @NamedQuery(name = "TipoPublicacion.findByIdTipoPublicacion", query = "SELECT t FROM TipoPublicacion t WHERE t.idTipoPublicacion = :idTipoPublicacion"),
    @NamedQuery(name = "TipoPublicacion.findByNombreTipo", query = "SELECT t FROM TipoPublicacion t WHERE t.nombreTipo = :nombreTipo")})
public class TipoPublicacion implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoPublicacion")
    private List<PublicacionPerfil> publicacionPerfilList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_publicacion")
    private Integer idTipoPublicacion;
    @Size(max = 45)
    @Column(name = "nombre_tipo")
    private String nombreTipo;

    public TipoPublicacion() {
    }

    public TipoPublicacion(Integer idTipoPublicacion) {
        this.idTipoPublicacion = idTipoPublicacion;
    }

    public Integer getIdTipoPublicacion() {
        return idTipoPublicacion;
    }

    public void setIdTipoPublicacion(Integer idTipoPublicacion) {
        this.idTipoPublicacion = idTipoPublicacion;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoPublicacion != null ? idTipoPublicacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoPublicacion)) {
            return false;
        }
        TipoPublicacion other = (TipoPublicacion) object;
        if ((this.idTipoPublicacion == null && other.idTipoPublicacion != null) || (this.idTipoPublicacion != null && !this.idTipoPublicacion.equals(other.idTipoPublicacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ENTITIES.TipoPublicacion[ idTipoPublicacion=" + idTipoPublicacion + " ]";
    }

    @XmlTransient
    public List<PublicacionPerfil> getPublicacionPerfilList() {
        return publicacionPerfilList;
    }

    public void setPublicacionPerfilList(List<PublicacionPerfil> publicacionPerfilList) {
        this.publicacionPerfilList = publicacionPerfilList;
    }
    
}
