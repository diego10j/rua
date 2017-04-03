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
@Table(name = "gth_situacion_financiera_emple")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthSituacionFinancieraEmple.findAll", query = "SELECT g FROM GthSituacionFinancieraEmple g"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByIdeGtsfe", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.ideGtsfe = :ideGtsfe"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByGastoViviendaGtsfe", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.gastoViviendaGtsfe = :gastoViviendaGtsfe"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByMontoArriendoGtsfe", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.montoArriendoGtsfe = :montoArriendoGtsfe"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByDetalleInmuebleGtsfe", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.detalleInmuebleGtsfe = :detalleInmuebleGtsfe"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByGastoSaludGtsfe", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.gastoSaludGtsfe = :gastoSaludGtsfe"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByGastoEducacionGtsfe", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.gastoEducacionGtsfe = :gastoEducacionGtsfe"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByGastoAlimentacionGtsfe", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.gastoAlimentacionGtsfe = :gastoAlimentacionGtsfe"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByGastoVestimentaGtsfe", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.gastoVestimentaGtsfe = :gastoVestimentaGtsfe"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByOtroGastoGtsfe", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.otroGastoGtsfe = :otroGastoGtsfe"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByPeriodoInicialGtsfe", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.periodoInicialGtsfe = :periodoInicialGtsfe"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByPeriodoFinalGtsfe", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.periodoFinalGtsfe = :periodoFinalGtsfe"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByActivoGtsfe", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.activoGtsfe = :activoGtsfe"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByUsuarioIngre", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByFechaIngre", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByUsuarioActua", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByFechaActua", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByHoraIngre", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthSituacionFinancieraEmple.findByHoraActua", query = "SELECT g FROM GthSituacionFinancieraEmple g WHERE g.horaActua = :horaActua")})
public class GthSituacionFinancieraEmple implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtsfe")
    private Integer ideGtsfe;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "gasto_vivienda_gtsfe")
    private BigDecimal gastoViviendaGtsfe;
    @Column(name = "monto_arriendo_gtsfe")
    private BigDecimal montoArriendoGtsfe;
    @Column(name = "detalle_inmueble_gtsfe")
    private String detalleInmuebleGtsfe;
    @Column(name = "gasto_salud_gtsfe")
    private BigDecimal gastoSaludGtsfe;
    @Column(name = "gasto_educacion_gtsfe")
    private BigDecimal gastoEducacionGtsfe;
    @Column(name = "gasto_alimentacion_gtsfe")
    private BigDecimal gastoAlimentacionGtsfe;
    @Column(name = "gasto_vestimenta_gtsfe")
    private BigDecimal gastoVestimentaGtsfe;
    @Column(name = "otro_gasto_gtsfe")
    private BigDecimal otroGastoGtsfe;
    @Column(name = "periodo_inicial_gtsfe")
    @Temporal(TemporalType.DATE)
    private Date periodoInicialGtsfe;
    @Column(name = "periodo_final_gtsfe")
    @Temporal(TemporalType.DATE)
    private Date periodoFinalGtsfe;
    @Column(name = "activo_gtsfe")
    private Boolean activoGtsfe;
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
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;

    public GthSituacionFinancieraEmple() {
    }

    public GthSituacionFinancieraEmple(Integer ideGtsfe) {
        this.ideGtsfe = ideGtsfe;
    }

    public Integer getIdeGtsfe() {
        return ideGtsfe;
    }

    public void setIdeGtsfe(Integer ideGtsfe) {
        this.ideGtsfe = ideGtsfe;
    }

    public BigDecimal getGastoViviendaGtsfe() {
        return gastoViviendaGtsfe;
    }

    public void setGastoViviendaGtsfe(BigDecimal gastoViviendaGtsfe) {
        this.gastoViviendaGtsfe = gastoViviendaGtsfe;
    }

    public BigDecimal getMontoArriendoGtsfe() {
        return montoArriendoGtsfe;
    }

    public void setMontoArriendoGtsfe(BigDecimal montoArriendoGtsfe) {
        this.montoArriendoGtsfe = montoArriendoGtsfe;
    }

    public String getDetalleInmuebleGtsfe() {
        return detalleInmuebleGtsfe;
    }

    public void setDetalleInmuebleGtsfe(String detalleInmuebleGtsfe) {
        this.detalleInmuebleGtsfe = detalleInmuebleGtsfe;
    }

    public BigDecimal getGastoSaludGtsfe() {
        return gastoSaludGtsfe;
    }

    public void setGastoSaludGtsfe(BigDecimal gastoSaludGtsfe) {
        this.gastoSaludGtsfe = gastoSaludGtsfe;
    }

    public BigDecimal getGastoEducacionGtsfe() {
        return gastoEducacionGtsfe;
    }

    public void setGastoEducacionGtsfe(BigDecimal gastoEducacionGtsfe) {
        this.gastoEducacionGtsfe = gastoEducacionGtsfe;
    }

    public BigDecimal getGastoAlimentacionGtsfe() {
        return gastoAlimentacionGtsfe;
    }

    public void setGastoAlimentacionGtsfe(BigDecimal gastoAlimentacionGtsfe) {
        this.gastoAlimentacionGtsfe = gastoAlimentacionGtsfe;
    }

    public BigDecimal getGastoVestimentaGtsfe() {
        return gastoVestimentaGtsfe;
    }

    public void setGastoVestimentaGtsfe(BigDecimal gastoVestimentaGtsfe) {
        this.gastoVestimentaGtsfe = gastoVestimentaGtsfe;
    }

    public BigDecimal getOtroGastoGtsfe() {
        return otroGastoGtsfe;
    }

    public void setOtroGastoGtsfe(BigDecimal otroGastoGtsfe) {
        this.otroGastoGtsfe = otroGastoGtsfe;
    }

    public Date getPeriodoInicialGtsfe() {
        return periodoInicialGtsfe;
    }

    public void setPeriodoInicialGtsfe(Date periodoInicialGtsfe) {
        this.periodoInicialGtsfe = periodoInicialGtsfe;
    }

    public Date getPeriodoFinalGtsfe() {
        return periodoFinalGtsfe;
    }

    public void setPeriodoFinalGtsfe(Date periodoFinalGtsfe) {
        this.periodoFinalGtsfe = periodoFinalGtsfe;
    }

    public Boolean getActivoGtsfe() {
        return activoGtsfe;
    }

    public void setActivoGtsfe(Boolean activoGtsfe) {
        this.activoGtsfe = activoGtsfe;
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

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtsfe != null ? ideGtsfe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthSituacionFinancieraEmple)) {
            return false;
        }
        GthSituacionFinancieraEmple other = (GthSituacionFinancieraEmple) object;
        if ((this.ideGtsfe == null && other.ideGtsfe != null) || (this.ideGtsfe != null && !this.ideGtsfe.equals(other.ideGtsfe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthSituacionFinancieraEmple[ ideGtsfe=" + ideGtsfe + " ]";
    }
    
}
