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
@Table(name = "nrh_retencion_rubro_descuento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhRetencionRubroDescuento.findAll", query = "SELECT n FROM NrhRetencionRubroDescuento n"),
    @NamedQuery(name = "NrhRetencionRubroDescuento.findByIdeNrrrd", query = "SELECT n FROM NrhRetencionRubroDescuento n WHERE n.ideNrrrd = :ideNrrrd"),
    @NamedQuery(name = "NrhRetencionRubroDescuento.findByValorRubroNrrrd", query = "SELECT n FROM NrhRetencionRubroDescuento n WHERE n.valorRubroNrrrd = :valorRubroNrrrd"),
    @NamedQuery(name = "NrhRetencionRubroDescuento.findByPorcentajeNrrrd", query = "SELECT n FROM NrhRetencionRubroDescuento n WHERE n.porcentajeNrrrd = :porcentajeNrrrd"),
    @NamedQuery(name = "NrhRetencionRubroDescuento.findByValorDescuentoNrrrd", query = "SELECT n FROM NrhRetencionRubroDescuento n WHERE n.valorDescuentoNrrrd = :valorDescuentoNrrrd"),
    @NamedQuery(name = "NrhRetencionRubroDescuento.findByActivoNrrrd", query = "SELECT n FROM NrhRetencionRubroDescuento n WHERE n.activoNrrrd = :activoNrrrd"),
    @NamedQuery(name = "NrhRetencionRubroDescuento.findByUsuarioIngre", query = "SELECT n FROM NrhRetencionRubroDescuento n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhRetencionRubroDescuento.findByFechaIngre", query = "SELECT n FROM NrhRetencionRubroDescuento n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhRetencionRubroDescuento.findByUsuarioActua", query = "SELECT n FROM NrhRetencionRubroDescuento n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhRetencionRubroDescuento.findByFechaActua", query = "SELECT n FROM NrhRetencionRubroDescuento n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhRetencionRubroDescuento.findByHoraIngre", query = "SELECT n FROM NrhRetencionRubroDescuento n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhRetencionRubroDescuento.findByHoraActua", query = "SELECT n FROM NrhRetencionRubroDescuento n WHERE n.horaActua = :horaActua")})
public class NrhRetencionRubroDescuento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrrrd")
    private Integer ideNrrrd;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_rubro_nrrrd")
    private BigDecimal valorRubroNrrrd;
    @Column(name = "porcentaje_nrrrd")
    private BigDecimal porcentajeNrrrd;
    @Column(name = "valor_descuento_nrrrd")
    private BigDecimal valorDescuentoNrrrd;
    @Column(name = "activo_nrrrd")
    private Boolean activoNrrrd;
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
    @JoinColumn(name = "ide_nrrub", referencedColumnName = "ide_nrrub")
    @ManyToOne
    private NrhRubro ideNrrub;
    @JoinColumn(name = "ide_nrrej", referencedColumnName = "ide_nrrej")
    @ManyToOne
    private NrhRetencionJudicial ideNrrej;

    public NrhRetencionRubroDescuento() {
    }

    public NrhRetencionRubroDescuento(Integer ideNrrrd) {
        this.ideNrrrd = ideNrrrd;
    }

    public Integer getIdeNrrrd() {
        return ideNrrrd;
    }

    public void setIdeNrrrd(Integer ideNrrrd) {
        this.ideNrrrd = ideNrrrd;
    }

    public BigDecimal getValorRubroNrrrd() {
        return valorRubroNrrrd;
    }

    public void setValorRubroNrrrd(BigDecimal valorRubroNrrrd) {
        this.valorRubroNrrrd = valorRubroNrrrd;
    }

    public BigDecimal getPorcentajeNrrrd() {
        return porcentajeNrrrd;
    }

    public void setPorcentajeNrrrd(BigDecimal porcentajeNrrrd) {
        this.porcentajeNrrrd = porcentajeNrrrd;
    }

    public BigDecimal getValorDescuentoNrrrd() {
        return valorDescuentoNrrrd;
    }

    public void setValorDescuentoNrrrd(BigDecimal valorDescuentoNrrrd) {
        this.valorDescuentoNrrrd = valorDescuentoNrrrd;
    }

    public Boolean getActivoNrrrd() {
        return activoNrrrd;
    }

    public void setActivoNrrrd(Boolean activoNrrrd) {
        this.activoNrrrd = activoNrrrd;
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

    public NrhRubro getIdeNrrub() {
        return ideNrrub;
    }

    public void setIdeNrrub(NrhRubro ideNrrub) {
        this.ideNrrub = ideNrrub;
    }

    public NrhRetencionJudicial getIdeNrrej() {
        return ideNrrej;
    }

    public void setIdeNrrej(NrhRetencionJudicial ideNrrej) {
        this.ideNrrej = ideNrrej;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrrrd != null ? ideNrrrd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhRetencionRubroDescuento)) {
            return false;
        }
        NrhRetencionRubroDescuento other = (NrhRetencionRubroDescuento) object;
        if ((this.ideNrrrd == null && other.ideNrrrd != null) || (this.ideNrrrd != null && !this.ideNrrrd.equals(other.ideNrrrd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhRetencionRubroDescuento[ ideNrrrd=" + ideNrrrd + " ]";
    }
    
}
