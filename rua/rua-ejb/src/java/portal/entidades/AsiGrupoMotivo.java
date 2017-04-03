/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author User
 */
@Entity
@Table(name = "asi_grupo_motivo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsiGrupoMotivo.findAll", query = "SELECT a FROM AsiGrupoMotivo a"),
    @NamedQuery(name = "AsiGrupoMotivo.findByIdeAsgrm", query = "SELECT a FROM AsiGrupoMotivo a WHERE a.ideAsgrm = :ideAsgrm"),
    @NamedQuery(name = "AsiGrupoMotivo.findByDetalleAsgrm", query = "SELECT a FROM AsiGrupoMotivo a WHERE a.detalleAsgrm = :detalleAsgrm"),
    @NamedQuery(name = "AsiGrupoMotivo.findByActivoAsgrm", query = "SELECT a FROM AsiGrupoMotivo a WHERE a.activoAsgrm = :activoAsgrm"),
    @NamedQuery(name = "AsiGrupoMotivo.findByUsuarioIngre", query = "SELECT a FROM AsiGrupoMotivo a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiGrupoMotivo.findByFechaIngre", query = "SELECT a FROM AsiGrupoMotivo a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiGrupoMotivo.findByUsuarioActua", query = "SELECT a FROM AsiGrupoMotivo a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiGrupoMotivo.findByFechaActua", query = "SELECT a FROM AsiGrupoMotivo a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiGrupoMotivo.findByHoraIngre", query = "SELECT a FROM AsiGrupoMotivo a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiGrupoMotivo.findByHoraActua", query = "SELECT a FROM AsiGrupoMotivo a WHERE a.horaActua = :horaActua")})
public class AsiGrupoMotivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_asgrm")
    private Integer ideAsgrm;
    @Column(name = "detalle_asgrm")
    private String detalleAsgrm;
    @Column(name = "activo_asgrm")
    private Boolean activoAsgrm;
    @Column(name = "usuario_ingre")
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "usuario_actua")
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideAsgrm")
    private Collection<AsiMotivo> asiMotivoCollection;

    public AsiGrupoMotivo() {
    }

    public AsiGrupoMotivo(Integer ideAsgrm) {
        this.ideAsgrm = ideAsgrm;
    }

    public Integer getIdeAsgrm() {
        return ideAsgrm;
    }

    public void setIdeAsgrm(Integer ideAsgrm) {
        this.ideAsgrm = ideAsgrm;
    }

    public String getDetalleAsgrm() {
        return detalleAsgrm;
    }

    public void setDetalleAsgrm(String detalleAsgrm) {
        this.detalleAsgrm = detalleAsgrm;
    }

    public Boolean getActivoAsgrm() {
        return activoAsgrm;
    }

    public void setActivoAsgrm(Boolean activoAsgrm) {
        this.activoAsgrm = activoAsgrm;
    }

    public String getUsuarioIngre() {
        return usuarioIngre;
    }

    public void setUsuarioIngre(String usuarioIngre) {
        this.usuarioIngre = usuarioIngre;
    }

    public Date getFechaIngre() {
        return fechaIngre;
    }

    public void setFechaIngre(Date fechaIngre) {
        this.fechaIngre = fechaIngre;
    }

    public String getUsuarioActua() {
        return usuarioActua;
    }

    public void setUsuarioActua(String usuarioActua) {
        this.usuarioActua = usuarioActua;
    }

    public Date getFechaActua() {
        return fechaActua;
    }

    public void setFechaActua(Date fechaActua) {
        this.fechaActua = fechaActua;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    @XmlTransient
    public Collection<AsiMotivo> getAsiMotivoCollection() {
        return asiMotivoCollection;
    }

    public void setAsiMotivoCollection(Collection<AsiMotivo> asiMotivoCollection) {
        this.asiMotivoCollection = asiMotivoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsgrm != null ? ideAsgrm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiGrupoMotivo)) {
            return false;
        }
        AsiGrupoMotivo other = (AsiGrupoMotivo) object;
        if ((this.ideAsgrm == null && other.ideAsgrm != null) || (this.ideAsgrm != null && !this.ideAsgrm.equals(other.ideAsgrm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiGrupoMotivo[ ideAsgrm=" + ideAsgrm + " ]";
    }
    
}
