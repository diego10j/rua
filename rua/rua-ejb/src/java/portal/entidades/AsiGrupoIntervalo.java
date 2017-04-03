/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "asi_grupo_intervalo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsiGrupoIntervalo.findAll", query = "SELECT a FROM AsiGrupoIntervalo a"),
    @NamedQuery(name = "AsiGrupoIntervalo.findByIdeAsgri", query = "SELECT a FROM AsiGrupoIntervalo a WHERE a.ideAsgri = :ideAsgri"),
    @NamedQuery(name = "AsiGrupoIntervalo.findByDetalleAsgri", query = "SELECT a FROM AsiGrupoIntervalo a WHERE a.detalleAsgri = :detalleAsgri"),
    @NamedQuery(name = "AsiGrupoIntervalo.findByPorcentajeAsgri", query = "SELECT a FROM AsiGrupoIntervalo a WHERE a.porcentajeAsgri = :porcentajeAsgri"),
    @NamedQuery(name = "AsiGrupoIntervalo.findByActivoAsgri", query = "SELECT a FROM AsiGrupoIntervalo a WHERE a.activoAsgri = :activoAsgri"),
    @NamedQuery(name = "AsiGrupoIntervalo.findByUsuarioIngre", query = "SELECT a FROM AsiGrupoIntervalo a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiGrupoIntervalo.findByFechaIngre", query = "SELECT a FROM AsiGrupoIntervalo a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiGrupoIntervalo.findByUsuarioActua", query = "SELECT a FROM AsiGrupoIntervalo a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiGrupoIntervalo.findByFechaActua", query = "SELECT a FROM AsiGrupoIntervalo a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiGrupoIntervalo.findByHoraIngre", query = "SELECT a FROM AsiGrupoIntervalo a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiGrupoIntervalo.findByHoraActua", query = "SELECT a FROM AsiGrupoIntervalo a WHERE a.horaActua = :horaActua")})
public class AsiGrupoIntervalo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_asgri")
    private Integer ideAsgri;
    @Column(name = "detalle_asgri")
    private String detalleAsgri;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje_asgri")
    private BigDecimal porcentajeAsgri;
    @Column(name = "activo_asgri")
    private Boolean activoAsgri;
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
    @OneToMany(mappedBy = "ideAsgri")
    private Collection<AsiDetalleHorasExtras> asiDetalleHorasExtrasCollection;

    public AsiGrupoIntervalo() {
    }

    public AsiGrupoIntervalo(Integer ideAsgri) {
        this.ideAsgri = ideAsgri;
    }

    public Integer getIdeAsgri() {
        return ideAsgri;
    }

    public void setIdeAsgri(Integer ideAsgri) {
        this.ideAsgri = ideAsgri;
    }

    public String getDetalleAsgri() {
        return detalleAsgri;
    }

    public void setDetalleAsgri(String detalleAsgri) {
        this.detalleAsgri = detalleAsgri;
    }

    public BigDecimal getPorcentajeAsgri() {
        return porcentajeAsgri;
    }

    public void setPorcentajeAsgri(BigDecimal porcentajeAsgri) {
        this.porcentajeAsgri = porcentajeAsgri;
    }

    public Boolean getActivoAsgri() {
        return activoAsgri;
    }

    public void setActivoAsgri(Boolean activoAsgri) {
        this.activoAsgri = activoAsgri;
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
    public Collection<AsiDetalleHorasExtras> getAsiDetalleHorasExtrasCollection() {
        return asiDetalleHorasExtrasCollection;
    }

    public void setAsiDetalleHorasExtrasCollection(Collection<AsiDetalleHorasExtras> asiDetalleHorasExtrasCollection) {
        this.asiDetalleHorasExtrasCollection = asiDetalleHorasExtrasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsgri != null ? ideAsgri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiGrupoIntervalo)) {
            return false;
        }
        AsiGrupoIntervalo other = (AsiGrupoIntervalo) object;
        if ((this.ideAsgri == null && other.ideAsgri != null) || (this.ideAsgri != null && !this.ideAsgri.equals(other.ideAsgri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiGrupoIntervalo[ ideAsgri=" + ideAsgri + " ]";
    }
    
}
