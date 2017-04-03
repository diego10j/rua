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
@Table(name = "gth_beneficiario_seguro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthBeneficiarioSeguro.findAll", query = "SELECT g FROM GthBeneficiarioSeguro g"),
    @NamedQuery(name = "GthBeneficiarioSeguro.findByIdeGtbes", query = "SELECT g FROM GthBeneficiarioSeguro g WHERE g.ideGtbes = :ideGtbes"),
    @NamedQuery(name = "GthBeneficiarioSeguro.findByPrimerNombreGtbes", query = "SELECT g FROM GthBeneficiarioSeguro g WHERE g.primerNombreGtbes = :primerNombreGtbes"),
    @NamedQuery(name = "GthBeneficiarioSeguro.findBySegundoNombreGtbes", query = "SELECT g FROM GthBeneficiarioSeguro g WHERE g.segundoNombreGtbes = :segundoNombreGtbes"),
    @NamedQuery(name = "GthBeneficiarioSeguro.findByApellidoPaternoGtbes", query = "SELECT g FROM GthBeneficiarioSeguro g WHERE g.apellidoPaternoGtbes = :apellidoPaternoGtbes"),
    @NamedQuery(name = "GthBeneficiarioSeguro.findByApellidoMaternoGtbes", query = "SELECT g FROM GthBeneficiarioSeguro g WHERE g.apellidoMaternoGtbes = :apellidoMaternoGtbes"),
    @NamedQuery(name = "GthBeneficiarioSeguro.findByPorcentajeSeguroGtbes", query = "SELECT g FROM GthBeneficiarioSeguro g WHERE g.porcentajeSeguroGtbes = :porcentajeSeguroGtbes"),
    @NamedQuery(name = "GthBeneficiarioSeguro.findByValorSeguroGtbes", query = "SELECT g FROM GthBeneficiarioSeguro g WHERE g.valorSeguroGtbes = :valorSeguroGtbes"),
    @NamedQuery(name = "GthBeneficiarioSeguro.findByActivoGtbes", query = "SELECT g FROM GthBeneficiarioSeguro g WHERE g.activoGtbes = :activoGtbes"),
    @NamedQuery(name = "GthBeneficiarioSeguro.findByUsuarioIngre", query = "SELECT g FROM GthBeneficiarioSeguro g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthBeneficiarioSeguro.findByFechaIngre", query = "SELECT g FROM GthBeneficiarioSeguro g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthBeneficiarioSeguro.findByUsuarioActua", query = "SELECT g FROM GthBeneficiarioSeguro g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthBeneficiarioSeguro.findByFechaActua", query = "SELECT g FROM GthBeneficiarioSeguro g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthBeneficiarioSeguro.findByHoraIngre", query = "SELECT g FROM GthBeneficiarioSeguro g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthBeneficiarioSeguro.findByHoraActua", query = "SELECT g FROM GthBeneficiarioSeguro g WHERE g.horaActua = :horaActua")})
public class GthBeneficiarioSeguro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtbes")
    private Integer ideGtbes;
    @Column(name = "primer_nombre_gtbes")
    private String primerNombreGtbes;
    @Column(name = "segundo_nombre_gtbes")
    private String segundoNombreGtbes;
    @Column(name = "apellido_paterno_gtbes")
    private String apellidoPaternoGtbes;
    @Column(name = "apellido_materno_gtbes")
    private String apellidoMaternoGtbes;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje_seguro_gtbes")
    private BigDecimal porcentajeSeguroGtbes;
    @Column(name = "valor_seguro_gtbes")
    private BigDecimal valorSeguroGtbes;
    @Column(name = "activo_gtbes")
    private Boolean activoGtbes;
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
    @JoinColumn(name = "ide_gttpr", referencedColumnName = "ide_gttpr")
    @ManyToOne
    private GthTipoParentescoRelacion ideGttpr;
    @JoinColumn(name = "ide_gtsev", referencedColumnName = "ide_gtsev")
    @ManyToOne
    private GthSeguroVida ideGtsev;

    public GthBeneficiarioSeguro() {
    }

    public GthBeneficiarioSeguro(Integer ideGtbes) {
        this.ideGtbes = ideGtbes;
    }

    public Integer getIdeGtbes() {
        return ideGtbes;
    }

    public void setIdeGtbes(Integer ideGtbes) {
        this.ideGtbes = ideGtbes;
    }

    public String getPrimerNombreGtbes() {
        return primerNombreGtbes;
    }

    public void setPrimerNombreGtbes(String primerNombreGtbes) {
        this.primerNombreGtbes = primerNombreGtbes;
    }

    public String getSegundoNombreGtbes() {
        return segundoNombreGtbes;
    }

    public void setSegundoNombreGtbes(String segundoNombreGtbes) {
        this.segundoNombreGtbes = segundoNombreGtbes;
    }

    public String getApellidoPaternoGtbes() {
        return apellidoPaternoGtbes;
    }

    public void setApellidoPaternoGtbes(String apellidoPaternoGtbes) {
        this.apellidoPaternoGtbes = apellidoPaternoGtbes;
    }

    public String getApellidoMaternoGtbes() {
        return apellidoMaternoGtbes;
    }

    public void setApellidoMaternoGtbes(String apellidoMaternoGtbes) {
        this.apellidoMaternoGtbes = apellidoMaternoGtbes;
    }

    public BigDecimal getPorcentajeSeguroGtbes() {
        return porcentajeSeguroGtbes;
    }

    public void setPorcentajeSeguroGtbes(BigDecimal porcentajeSeguroGtbes) {
        this.porcentajeSeguroGtbes = porcentajeSeguroGtbes;
    }

    public BigDecimal getValorSeguroGtbes() {
        return valorSeguroGtbes;
    }

    public void setValorSeguroGtbes(BigDecimal valorSeguroGtbes) {
        this.valorSeguroGtbes = valorSeguroGtbes;
    }

    public Boolean getActivoGtbes() {
        return activoGtbes;
    }

    public void setActivoGtbes(Boolean activoGtbes) {
        this.activoGtbes = activoGtbes;
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

    public GthTipoParentescoRelacion getIdeGttpr() {
        return ideGttpr;
    }

    public void setIdeGttpr(GthTipoParentescoRelacion ideGttpr) {
        this.ideGttpr = ideGttpr;
    }

    public GthSeguroVida getIdeGtsev() {
        return ideGtsev;
    }

    public void setIdeGtsev(GthSeguroVida ideGtsev) {
        this.ideGtsev = ideGtsev;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtbes != null ? ideGtbes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthBeneficiarioSeguro)) {
            return false;
        }
        GthBeneficiarioSeguro other = (GthBeneficiarioSeguro) object;
        if ((this.ideGtbes == null && other.ideGtbes != null) || (this.ideGtbes != null && !this.ideGtbes.equals(other.ideGtbes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthBeneficiarioSeguro[ ideGtbes=" + ideGtbes + " ]";
    }
    
}
