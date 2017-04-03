/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "sis_usuario_clave")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisUsuarioClave.findAll", query = "SELECT s FROM SisUsuarioClave s"),
    @NamedQuery(name = "SisUsuarioClave.findByIdeUscl", query = "SELECT s FROM SisUsuarioClave s WHERE s.ideUscl = :ideUscl"),
    @NamedQuery(name = "SisUsuarioClave.findByIdePecl", query = "SELECT s FROM SisUsuarioClave s WHERE s.idePecl = :idePecl"),
    @NamedQuery(name = "SisUsuarioClave.findByIdeUsua", query = "SELECT s FROM SisUsuarioClave s WHERE s.ideUsua = :ideUsua"),
    @NamedQuery(name = "SisUsuarioClave.findByFechaRegistroUscl", query = "SELECT s FROM SisUsuarioClave s WHERE s.fechaRegistroUscl = :fechaRegistroUscl"),
    @NamedQuery(name = "SisUsuarioClave.findByFechaVenceUscl", query = "SELECT s FROM SisUsuarioClave s WHERE s.fechaVenceUscl = :fechaVenceUscl"),
    @NamedQuery(name = "SisUsuarioClave.findByClaveUscl", query = "SELECT s FROM SisUsuarioClave s WHERE s.claveUscl = :claveUscl"),
    @NamedQuery(name = "SisUsuarioClave.findByActivoUscl", query = "SELECT s FROM SisUsuarioClave s WHERE s.activoUscl = :activoUscl")})
public class SisUsuarioClave implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_uscl")
    private Long ideUscl;
    @Column(name = "ide_pecl")
    private Integer idePecl;
    @Column(name = "ide_usua")
    private BigInteger ideUsua;
    @Basic(optional = false)
    @Column(name = "fecha_registro_uscl")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistroUscl;
    @Column(name = "fecha_vence_uscl")
    @Temporal(TemporalType.DATE)
    private Date fechaVenceUscl;
    @Basic(optional = false)
    @Column(name = "clave_uscl")
    private String claveUscl;
    @Column(name = "activo_uscl")
    private Boolean activoUscl;

    public SisUsuarioClave() {
    }

    public SisUsuarioClave(Long ideUscl) {
        this.ideUscl = ideUscl;
    }

    public SisUsuarioClave(Long ideUscl, Date fechaRegistroUscl, String claveUscl) {
        this.ideUscl = ideUscl;
        this.fechaRegistroUscl = fechaRegistroUscl;
        this.claveUscl = claveUscl;
    }

    public Long getIdeUscl() {
        return ideUscl;
    }

    public void setIdeUscl(Long ideUscl) {
        this.ideUscl = ideUscl;
    }

    public Integer getIdePecl() {
        return idePecl;
    }

    public void setIdePecl(Integer idePecl) {
        this.idePecl = idePecl;
    }

    public BigInteger getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(BigInteger ideUsua) {
        this.ideUsua = ideUsua;
    }

    public Date getFechaRegistroUscl() {
        return fechaRegistroUscl;
    }

    public void setFechaRegistroUscl(Date fechaRegistroUscl) {
        this.fechaRegistroUscl = fechaRegistroUscl;
    }

    public Date getFechaVenceUscl() {
        return fechaVenceUscl;
    }

    public void setFechaVenceUscl(Date fechaVenceUscl) {
        this.fechaVenceUscl = fechaVenceUscl;
    }

    public String getClaveUscl() {
        return claveUscl;
    }

    public void setClaveUscl(String claveUscl) {
        this.claveUscl = claveUscl;
    }

    public Boolean getActivoUscl() {
        return activoUscl;
    }

    public void setActivoUscl(Boolean activoUscl) {
        this.activoUscl = activoUscl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideUscl != null ? ideUscl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisUsuarioClave)) {
            return false;
        }
        SisUsuarioClave other = (SisUsuarioClave) object;
        if ((this.ideUscl == null && other.ideUscl != null) || (this.ideUscl != null && !this.ideUscl.equals(other.ideUscl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisUsuarioClave[ ideUscl=" + ideUscl + " ]";
    }
    
}
