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
@Table(name = "nrh_anticipo_abono")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhAnticipoAbono.findAll", query = "SELECT n FROM NrhAnticipoAbono n"),
    @NamedQuery(name = "NrhAnticipoAbono.findByIdeNrana", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.ideNrana = :ideNrana"),
    @NamedQuery(name = "NrhAnticipoAbono.findBySaldoAnteriorNrana", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.saldoAnteriorNrana = :saldoAnteriorNrana"),
    @NamedQuery(name = "NrhAnticipoAbono.findByValorAbonoNrana", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.valorAbonoNrana = :valorAbonoNrana"),
    @NamedQuery(name = "NrhAnticipoAbono.findByFechaAbonoNrana", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.fechaAbonoNrana = :fechaAbonoNrana"),
    @NamedQuery(name = "NrhAnticipoAbono.findByMontoPendienteNrana", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.montoPendienteNrana = :montoPendienteNrana"),
    @NamedQuery(name = "NrhAnticipoAbono.findByPlazoNrana", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.plazoNrana = :plazoNrana"),
    @NamedQuery(name = "NrhAnticipoAbono.findByFechaPagoNrana", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.fechaPagoNrana = :fechaPagoNrana"),
    @NamedQuery(name = "NrhAnticipoAbono.findByFechaDepositoNrana", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.fechaDepositoNrana = :fechaDepositoNrana"),
    @NamedQuery(name = "NrhAnticipoAbono.findByDocDepositoNrana", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.docDepositoNrana = :docDepositoNrana"),
    @NamedQuery(name = "NrhAnticipoAbono.findByPathNrana", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.pathNrana = :pathNrana"),
    @NamedQuery(name = "NrhAnticipoAbono.findByActivoNrana", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.activoNrana = :activoNrana"),
    @NamedQuery(name = "NrhAnticipoAbono.findByUsuarioIngre", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhAnticipoAbono.findByFechaIngre", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhAnticipoAbono.findByUsuarioActua", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhAnticipoAbono.findByFechaActua", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhAnticipoAbono.findByHoraIngre", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhAnticipoAbono.findByHoraActua", query = "SELECT n FROM NrhAnticipoAbono n WHERE n.horaActua = :horaActua")})
public class NrhAnticipoAbono implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrana")
    private Integer ideNrana;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo_anterior_nrana")
    private BigDecimal saldoAnteriorNrana;
    @Column(name = "valor_abono_nrana")
    private BigDecimal valorAbonoNrana;
    @Column(name = "fecha_abono_nrana")
    @Temporal(TemporalType.DATE)
    private Date fechaAbonoNrana;
    @Column(name = "monto_pendiente_nrana")
    private BigDecimal montoPendienteNrana;
    @Column(name = "plazo_nrana")
    private Integer plazoNrana;
    @Column(name = "fecha_pago_nrana")
    @Temporal(TemporalType.DATE)
    private Date fechaPagoNrana;
    @Column(name = "fecha_deposito_nrana")
    @Temporal(TemporalType.DATE)
    private Date fechaDepositoNrana;
    @Column(name = "doc_deposito_nrana")
    private String docDepositoNrana;
    @Column(name = "path_nrana")
    private String pathNrana;
    @Basic(optional = false)
    @Column(name = "activo_nrana")
    private boolean activoNrana;
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
    @JoinColumn(name = "ide_nrant", referencedColumnName = "ide_nrant")
    @ManyToOne
    private NrhAnticipo ideNrant;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;

    public NrhAnticipoAbono() {
    }

    public NrhAnticipoAbono(Integer ideNrana) {
        this.ideNrana = ideNrana;
    }

    public NrhAnticipoAbono(Integer ideNrana, boolean activoNrana) {
        this.ideNrana = ideNrana;
        this.activoNrana = activoNrana;
    }

    public Integer getIdeNrana() {
        return ideNrana;
    }

    public void setIdeNrana(Integer ideNrana) {
        this.ideNrana = ideNrana;
    }

    public BigDecimal getSaldoAnteriorNrana() {
        return saldoAnteriorNrana;
    }

    public void setSaldoAnteriorNrana(BigDecimal saldoAnteriorNrana) {
        this.saldoAnteriorNrana = saldoAnteriorNrana;
    }

    public BigDecimal getValorAbonoNrana() {
        return valorAbonoNrana;
    }

    public void setValorAbonoNrana(BigDecimal valorAbonoNrana) {
        this.valorAbonoNrana = valorAbonoNrana;
    }

    public Date getFechaAbonoNrana() {
        return fechaAbonoNrana;
    }

    public void setFechaAbonoNrana(Date fechaAbonoNrana) {
        this.fechaAbonoNrana = fechaAbonoNrana;
    }

    public BigDecimal getMontoPendienteNrana() {
        return montoPendienteNrana;
    }

    public void setMontoPendienteNrana(BigDecimal montoPendienteNrana) {
        this.montoPendienteNrana = montoPendienteNrana;
    }

    public Integer getPlazoNrana() {
        return plazoNrana;
    }

    public void setPlazoNrana(Integer plazoNrana) {
        this.plazoNrana = plazoNrana;
    }

    public Date getFechaPagoNrana() {
        return fechaPagoNrana;
    }

    public void setFechaPagoNrana(Date fechaPagoNrana) {
        this.fechaPagoNrana = fechaPagoNrana;
    }

    public Date getFechaDepositoNrana() {
        return fechaDepositoNrana;
    }

    public void setFechaDepositoNrana(Date fechaDepositoNrana) {
        this.fechaDepositoNrana = fechaDepositoNrana;
    }

    public String getDocDepositoNrana() {
        return docDepositoNrana;
    }

    public void setDocDepositoNrana(String docDepositoNrana) {
        this.docDepositoNrana = docDepositoNrana;
    }

    public String getPathNrana() {
        return pathNrana;
    }

    public void setPathNrana(String pathNrana) {
        this.pathNrana = pathNrana;
    }

    public boolean getActivoNrana() {
        return activoNrana;
    }

    public void setActivoNrana(boolean activoNrana) {
        this.activoNrana = activoNrana;
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

    public NrhAnticipo getIdeNrant() {
        return ideNrant;
    }

    public void setIdeNrant(NrhAnticipo ideNrant) {
        this.ideNrant = ideNrant;
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
        hash += (ideNrana != null ? ideNrana.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhAnticipoAbono)) {
            return false;
        }
        NrhAnticipoAbono other = (NrhAnticipoAbono) object;
        if ((this.ideNrana == null && other.ideNrana != null) || (this.ideNrana != null && !this.ideNrana.equals(other.ideNrana))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhAnticipoAbono[ ideNrana=" + ideNrana + " ]";
    }
    
}
