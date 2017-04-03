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
@Table(name = "gth_cuenta_bancaria_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthCuentaBancariaEmpleado.findAll", query = "SELECT g FROM GthCuentaBancariaEmpleado g"),
    @NamedQuery(name = "GthCuentaBancariaEmpleado.findByIdeGtcbe", query = "SELECT g FROM GthCuentaBancariaEmpleado g WHERE g.ideGtcbe = :ideGtcbe"),
    @NamedQuery(name = "GthCuentaBancariaEmpleado.findByNumeroCuentaGtcbe", query = "SELECT g FROM GthCuentaBancariaEmpleado g WHERE g.numeroCuentaGtcbe = :numeroCuentaGtcbe"),
    @NamedQuery(name = "GthCuentaBancariaEmpleado.findBySaldoPromedioGtcbe", query = "SELECT g FROM GthCuentaBancariaEmpleado g WHERE g.saldoPromedioGtcbe = :saldoPromedioGtcbe"),
    @NamedQuery(name = "GthCuentaBancariaEmpleado.findByIndividualConjuntaGtcbe", query = "SELECT g FROM GthCuentaBancariaEmpleado g WHERE g.individualConjuntaGtcbe = :individualConjuntaGtcbe"),
    @NamedQuery(name = "GthCuentaBancariaEmpleado.findByActivoGtcbe", query = "SELECT g FROM GthCuentaBancariaEmpleado g WHERE g.activoGtcbe = :activoGtcbe"),
    @NamedQuery(name = "GthCuentaBancariaEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthCuentaBancariaEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthCuentaBancariaEmpleado.findByFechaIngre", query = "SELECT g FROM GthCuentaBancariaEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthCuentaBancariaEmpleado.findByUsuarioActua", query = "SELECT g FROM GthCuentaBancariaEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthCuentaBancariaEmpleado.findByFechaActua", query = "SELECT g FROM GthCuentaBancariaEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthCuentaBancariaEmpleado.findByHoraIngre", query = "SELECT g FROM GthCuentaBancariaEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthCuentaBancariaEmpleado.findByHoraActua", query = "SELECT g FROM GthCuentaBancariaEmpleado g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthCuentaBancariaEmpleado.findByAcreditacionGtcbe", query = "SELECT g FROM GthCuentaBancariaEmpleado g WHERE g.acreditacionGtcbe = :acreditacionGtcbe")})
public class GthCuentaBancariaEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtcbe")
    private Integer ideGtcbe;
    @Basic(optional = false)
    @Column(name = "numero_cuenta_gtcbe")
    private String numeroCuentaGtcbe;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo_promedio_gtcbe")
    private BigDecimal saldoPromedioGtcbe;
    @Basic(optional = false)
    @Column(name = "individual_conjunta_gtcbe")
    private int individualConjuntaGtcbe;
    @Basic(optional = false)
    @Column(name = "activo_gtcbe")
    private boolean activoGtcbe;
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
    @Column(name = "acreditacion_gtcbe")
    private Boolean acreditacionGtcbe;
    @JoinColumn(name = "ide_gttcb", referencedColumnName = "ide_gttcb")
    @ManyToOne(optional = false)
    private GthTipoCuentaBancaria ideGttcb;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne(optional = false)
    private GenInstitucion ideGeins;
    @JoinColumn(name = "ide_geben", referencedColumnName = "ide_geben")
    @ManyToOne
    private GenBeneficiario ideGeben;

    public GthCuentaBancariaEmpleado() {
    }

    public GthCuentaBancariaEmpleado(Integer ideGtcbe) {
        this.ideGtcbe = ideGtcbe;
    }

    public GthCuentaBancariaEmpleado(Integer ideGtcbe, String numeroCuentaGtcbe, int individualConjuntaGtcbe, boolean activoGtcbe) {
        this.ideGtcbe = ideGtcbe;
        this.numeroCuentaGtcbe = numeroCuentaGtcbe;
        this.individualConjuntaGtcbe = individualConjuntaGtcbe;
        this.activoGtcbe = activoGtcbe;
    }

    public Integer getIdeGtcbe() {
        return ideGtcbe;
    }

    public void setIdeGtcbe(Integer ideGtcbe) {
        this.ideGtcbe = ideGtcbe;
    }

    public String getNumeroCuentaGtcbe() {
        return numeroCuentaGtcbe;
    }

    public void setNumeroCuentaGtcbe(String numeroCuentaGtcbe) {
        this.numeroCuentaGtcbe = numeroCuentaGtcbe;
    }

    public BigDecimal getSaldoPromedioGtcbe() {
        return saldoPromedioGtcbe;
    }

    public void setSaldoPromedioGtcbe(BigDecimal saldoPromedioGtcbe) {
        this.saldoPromedioGtcbe = saldoPromedioGtcbe;
    }

    public int getIndividualConjuntaGtcbe() {
        return individualConjuntaGtcbe;
    }

    public void setIndividualConjuntaGtcbe(int individualConjuntaGtcbe) {
        this.individualConjuntaGtcbe = individualConjuntaGtcbe;
    }

    public boolean getActivoGtcbe() {
        return activoGtcbe;
    }

    public void setActivoGtcbe(boolean activoGtcbe) {
        this.activoGtcbe = activoGtcbe;
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

    public Boolean getAcreditacionGtcbe() {
        return acreditacionGtcbe;
    }

    public void setAcreditacionGtcbe(Boolean acreditacionGtcbe) {
        this.acreditacionGtcbe = acreditacionGtcbe;
    }

    public GthTipoCuentaBancaria getIdeGttcb() {
        return ideGttcb;
    }

    public void setIdeGttcb(GthTipoCuentaBancaria ideGttcb) {
        this.ideGttcb = ideGttcb;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    public GenBeneficiario getIdeGeben() {
        return ideGeben;
    }

    public void setIdeGeben(GenBeneficiario ideGeben) {
        this.ideGeben = ideGeben;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtcbe != null ? ideGtcbe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthCuentaBancariaEmpleado)) {
            return false;
        }
        GthCuentaBancariaEmpleado other = (GthCuentaBancariaEmpleado) object;
        if ((this.ideGtcbe == null && other.ideGtcbe != null) || (this.ideGtcbe != null && !this.ideGtcbe.equals(other.ideGtcbe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthCuentaBancariaEmpleado[ ideGtcbe=" + ideGtcbe + " ]";
    }
    
}
