/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "gth_nivel_zona_viatico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthNivelZonaViatico.findAll", query = "SELECT g FROM GthNivelZonaViatico g"),
    @NamedQuery(name = "GthNivelZonaViatico.findByIdeGtnzv", query = "SELECT g FROM GthNivelZonaViatico g WHERE g.ideGtnzv = :ideGtnzv"),
    @NamedQuery(name = "GthNivelZonaViatico.findByValorGtnzv", query = "SELECT g FROM GthNivelZonaViatico g WHERE g.valorGtnzv = :valorGtnzv"),
    @NamedQuery(name = "GthNivelZonaViatico.findByInteriorExteriorGtnzv", query = "SELECT g FROM GthNivelZonaViatico g WHERE g.interiorExteriorGtnzv = :interiorExteriorGtnzv"),
    @NamedQuery(name = "GthNivelZonaViatico.findByActivoGtnzv", query = "SELECT g FROM GthNivelZonaViatico g WHERE g.activoGtnzv = :activoGtnzv"),
    @NamedQuery(name = "GthNivelZonaViatico.findByUsuarioIngre", query = "SELECT g FROM GthNivelZonaViatico g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthNivelZonaViatico.findByFechaIngre", query = "SELECT g FROM GthNivelZonaViatico g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthNivelZonaViatico.findByUsuarioActua", query = "SELECT g FROM GthNivelZonaViatico g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthNivelZonaViatico.findByFechaActua", query = "SELECT g FROM GthNivelZonaViatico g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthNivelZonaViatico.findByHoraIngre", query = "SELECT g FROM GthNivelZonaViatico g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthNivelZonaViatico.findByHoraActua", query = "SELECT g FROM GthNivelZonaViatico g WHERE g.horaActua = :horaActua")})
public class GthNivelZonaViatico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtnzv")
    private Integer ideGtnzv;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_gtnzv")
    private BigDecimal valorGtnzv;
    @Column(name = "interior_exterior_gtnzv")
    private Integer interiorExteriorGtnzv;
    @Column(name = "activo_gtnzv")
    private Boolean activoGtnzv;
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
    @JoinColumn(name = "ide_gtzov", referencedColumnName = "ide_gtzov")
    @ManyToOne
    private GthZonaViatico ideGtzov;
    @JoinColumn(name = "ide_gtniv", referencedColumnName = "ide_gtniv")
    @ManyToOne
    private GthNivelViatico ideGtniv;

    public GthNivelZonaViatico() {
    }

    public GthNivelZonaViatico(Integer ideGtnzv) {
        this.ideGtnzv = ideGtnzv;
    }

    public Integer getIdeGtnzv() {
        return ideGtnzv;
    }

    public void setIdeGtnzv(Integer ideGtnzv) {
        this.ideGtnzv = ideGtnzv;
    }

    public BigDecimal getValorGtnzv() {
        return valorGtnzv;
    }

    public void setValorGtnzv(BigDecimal valorGtnzv) {
        this.valorGtnzv = valorGtnzv;
    }

    public Integer getInteriorExteriorGtnzv() {
        return interiorExteriorGtnzv;
    }

    public void setInteriorExteriorGtnzv(Integer interiorExteriorGtnzv) {
        this.interiorExteriorGtnzv = interiorExteriorGtnzv;
    }

    public Boolean getActivoGtnzv() {
        return activoGtnzv;
    }

    public void setActivoGtnzv(Boolean activoGtnzv) {
        this.activoGtnzv = activoGtnzv;
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

    public GthZonaViatico getIdeGtzov() {
        return ideGtzov;
    }

    public void setIdeGtzov(GthZonaViatico ideGtzov) {
        this.ideGtzov = ideGtzov;
    }

    public GthNivelViatico getIdeGtniv() {
        return ideGtniv;
    }

    public void setIdeGtniv(GthNivelViatico ideGtniv) {
        this.ideGtniv = ideGtniv;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtnzv != null ? ideGtnzv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthNivelZonaViatico)) {
            return false;
        }
        GthNivelZonaViatico other = (GthNivelZonaViatico) object;
        if ((this.ideGtnzv == null && other.ideGtnzv != null) || (this.ideGtnzv != null && !this.ideGtnzv.equals(other.ideGtnzv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthNivelZonaViatico[ ideGtnzv=" + ideGtnzv + " ]";
    }
    
}
