/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
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
@Table(name = "nrh_precancelacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhPrecancelacion.findAll", query = "SELECT n FROM NrhPrecancelacion n"),
    @NamedQuery(name = "NrhPrecancelacion.findByIdeNrpre", query = "SELECT n FROM NrhPrecancelacion n WHERE n.ideNrpre = :ideNrpre"),
    @NamedQuery(name = "NrhPrecancelacion.findByFechaPrecanceladoNrpre", query = "SELECT n FROM NrhPrecancelacion n WHERE n.fechaPrecanceladoNrpre = :fechaPrecanceladoNrpre"),
    @NamedQuery(name = "NrhPrecancelacion.findByDocDepositoNrpre", query = "SELECT n FROM NrhPrecancelacion n WHERE n.docDepositoNrpre = :docDepositoNrpre"),
    @NamedQuery(name = "NrhPrecancelacion.findByFechaDepositoNrpre", query = "SELECT n FROM NrhPrecancelacion n WHERE n.fechaDepositoNrpre = :fechaDepositoNrpre"),
    @NamedQuery(name = "NrhPrecancelacion.findByPathFotoNrpre", query = "SELECT n FROM NrhPrecancelacion n WHERE n.pathFotoNrpre = :pathFotoNrpre"),
    @NamedQuery(name = "NrhPrecancelacion.findByObservacionesNrpre", query = "SELECT n FROM NrhPrecancelacion n WHERE n.observacionesNrpre = :observacionesNrpre"),
    @NamedQuery(name = "NrhPrecancelacion.findByActivoNrpre", query = "SELECT n FROM NrhPrecancelacion n WHERE n.activoNrpre = :activoNrpre"),
    @NamedQuery(name = "NrhPrecancelacion.findByUsuarioIngre", query = "SELECT n FROM NrhPrecancelacion n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhPrecancelacion.findByFechaIngre", query = "SELECT n FROM NrhPrecancelacion n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhPrecancelacion.findByUsuarioActua", query = "SELECT n FROM NrhPrecancelacion n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhPrecancelacion.findByFechaActua", query = "SELECT n FROM NrhPrecancelacion n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhPrecancelacion.findByHoraIngre", query = "SELECT n FROM NrhPrecancelacion n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhPrecancelacion.findByHoraActua", query = "SELECT n FROM NrhPrecancelacion n WHERE n.horaActua = :horaActua")})
public class NrhPrecancelacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrpre")
    private Integer ideNrpre;
    @Basic(optional = false)
    @Column(name = "fecha_precancelado_nrpre")
    @Temporal(TemporalType.DATE)
    private Date fechaPrecanceladoNrpre;
    @Basic(optional = false)
    @Column(name = "doc_deposito_nrpre")
    private String docDepositoNrpre;
    @Basic(optional = false)
    @Column(name = "fecha_deposito_nrpre")
    @Temporal(TemporalType.DATE)
    private Date fechaDepositoNrpre;
    @Column(name = "path_foto_nrpre")
    private String pathFotoNrpre;
    @Column(name = "observaciones_nrpre")
    private String observacionesNrpre;
    @Basic(optional = false)
    @Column(name = "activo_nrpre")
    private boolean activoNrpre;
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
    @JoinColumn(name = "ide_nramo", referencedColumnName = "ide_nramo")
    @ManyToOne(optional = false)
    private NrhAmortizacion ideNramo;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne(optional = false)
    private GenInstitucion ideGeins;

    public NrhPrecancelacion() {
    }

    public NrhPrecancelacion(Integer ideNrpre) {
        this.ideNrpre = ideNrpre;
    }

    public NrhPrecancelacion(Integer ideNrpre, Date fechaPrecanceladoNrpre, String docDepositoNrpre, Date fechaDepositoNrpre, boolean activoNrpre) {
        this.ideNrpre = ideNrpre;
        this.fechaPrecanceladoNrpre = fechaPrecanceladoNrpre;
        this.docDepositoNrpre = docDepositoNrpre;
        this.fechaDepositoNrpre = fechaDepositoNrpre;
        this.activoNrpre = activoNrpre;
    }

    public Integer getIdeNrpre() {
        return ideNrpre;
    }

    public void setIdeNrpre(Integer ideNrpre) {
        this.ideNrpre = ideNrpre;
    }

    public Date getFechaPrecanceladoNrpre() {
        return fechaPrecanceladoNrpre;
    }

    public void setFechaPrecanceladoNrpre(Date fechaPrecanceladoNrpre) {
        this.fechaPrecanceladoNrpre = fechaPrecanceladoNrpre;
    }

    public String getDocDepositoNrpre() {
        return docDepositoNrpre;
    }

    public void setDocDepositoNrpre(String docDepositoNrpre) {
        this.docDepositoNrpre = docDepositoNrpre;
    }

    public Date getFechaDepositoNrpre() {
        return fechaDepositoNrpre;
    }

    public void setFechaDepositoNrpre(Date fechaDepositoNrpre) {
        this.fechaDepositoNrpre = fechaDepositoNrpre;
    }

    public String getPathFotoNrpre() {
        return pathFotoNrpre;
    }

    public void setPathFotoNrpre(String pathFotoNrpre) {
        this.pathFotoNrpre = pathFotoNrpre;
    }

    public String getObservacionesNrpre() {
        return observacionesNrpre;
    }

    public void setObservacionesNrpre(String observacionesNrpre) {
        this.observacionesNrpre = observacionesNrpre;
    }

    public boolean getActivoNrpre() {
        return activoNrpre;
    }

    public void setActivoNrpre(boolean activoNrpre) {
        this.activoNrpre = activoNrpre;
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

    public NrhAmortizacion getIdeNramo() {
        return ideNramo;
    }

    public void setIdeNramo(NrhAmortizacion ideNramo) {
        this.ideNramo = ideNramo;
    }

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrpre != null ? ideNrpre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhPrecancelacion)) {
            return false;
        }
        NrhPrecancelacion other = (NrhPrecancelacion) object;
        if ((this.ideNrpre == null && other.ideNrpre != null) || (this.ideNrpre != null && !this.ideNrpre.equals(other.ideNrpre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhPrecancelacion[ ideNrpre=" + ideNrpre + " ]";
    }
    
}
