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
@Table(name = "identificador_archivo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IdentificadorArchivo.findAll", query = "SELECT i FROM IdentificadorArchivo i"),
    @NamedQuery(name = "IdentificadorArchivo.findByIdIdentificadorArchivo", query = "SELECT i FROM IdentificadorArchivo i WHERE i.idIdentificadorArchivo = :idIdentificadorArchivo"),
    @NamedQuery(name = "IdentificadorArchivo.findByDescripcion", query = "SELECT i FROM IdentificadorArchivo i WHERE i.descripcion = :descripcion")})
public class IdentificadorArchivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_identificador_archivo")
    private Integer idIdentificadorArchivo;
    @Size(max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idIdentificadorArchivo")
    private List<Archivo> archivoList;

    public IdentificadorArchivo() {
    }

    public IdentificadorArchivo(Integer idIdentificadorArchivo) {
        this.idIdentificadorArchivo = idIdentificadorArchivo;
    }

    public Integer getIdIdentificadorArchivo() {
        return idIdentificadorArchivo;
    }

    public void setIdIdentificadorArchivo(Integer idIdentificadorArchivo) {
        this.idIdentificadorArchivo = idIdentificadorArchivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Archivo> getArchivoList() {
        return archivoList;
    }

    public void setArchivoList(List<Archivo> archivoList) {
        this.archivoList = archivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idIdentificadorArchivo != null ? idIdentificadorArchivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IdentificadorArchivo)) {
            return false;
        }
        IdentificadorArchivo other = (IdentificadorArchivo) object;
        if ((this.idIdentificadorArchivo == null && other.idIdentificadorArchivo != null) || (this.idIdentificadorArchivo != null && !this.idIdentificadorArchivo.equals(other.idIdentificadorArchivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ENTITIES.IdentificadorArchivo[ idIdentificadorArchivo=" + idIdentificadorArchivo + " ]";
    }
    
}
