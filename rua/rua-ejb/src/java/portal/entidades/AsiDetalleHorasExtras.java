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
@Table(name = "asi_detalle_horas_extras")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsiDetalleHorasExtras.findAll", query = "SELECT a FROM AsiDetalleHorasExtras a"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByIdeAsdhe", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.ideAsdhe = :ideAsdhe"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByFechaAsdhe", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.fechaAsdhe = :fechaAsdhe"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByNroHorasAsdhe", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.nroHorasAsdhe = :nroHorasAsdhe"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByActividadesAsdhe", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.actividadesAsdhe = :actividadesAsdhe"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByNroHorasAprobadasAsdhe", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.nroHorasAprobadasAsdhe = :nroHorasAprobadasAsdhe"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByActivoAsdhe", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.activoAsdhe = :activoAsdhe"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByUsuarioIngre", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByFechaIngre", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByUsuarioActua", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByFechaActua", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByHoraIngre", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByHoraActua", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.horaActua = :horaActua"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByHoraInicialAsdhe", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.horaInicialAsdhe = :horaInicialAsdhe"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByHoraFinalAsdhe", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.horaFinalAsdhe = :horaFinalAsdhe"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByRegistroNovedadAsdhe", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.registroNovedadAsdhe = :registroNovedadAsdhe"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByAprobadoAsdhe", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.aprobadoAsdhe = :aprobadoAsdhe"),
    @NamedQuery(name = "AsiDetalleHorasExtras.findByNominaAsdhe", query = "SELECT a FROM AsiDetalleHorasExtras a WHERE a.nominaAsdhe = :nominaAsdhe")})
public class AsiDetalleHorasExtras implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_asdhe")
    private Integer ideAsdhe;
    @Column(name = "fecha_asdhe")
    @Temporal(TemporalType.DATE)
    private Date fechaAsdhe;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "nro_horas_asdhe")
    private BigDecimal nroHorasAsdhe;
    @Column(name = "actividades_asdhe")
    private String actividadesAsdhe;
    @Column(name = "nro_horas_aprobadas_asdhe")
    private BigDecimal nroHorasAprobadasAsdhe;
    @Column(name = "activo_asdhe")
    private Boolean activoAsdhe;
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
    @Column(name = "hora_inicial_asdhe")
    @Temporal(TemporalType.TIME)
    private Date horaInicialAsdhe;
    @Column(name = "hora_final_asdhe")
    @Temporal(TemporalType.TIME)
    private Date horaFinalAsdhe;
    @Column(name = "registro_novedad_asdhe")
    private Boolean registroNovedadAsdhe;
    @Column(name = "aprobado_asdhe")
    private Boolean aprobadoAsdhe;
    @Column(name = "nomina_asdhe")
    private Boolean nominaAsdhe;
    @JoinColumn(name = "ide_aspvh", referencedColumnName = "ide_aspvh")
    @ManyToOne
    private AsiPermisosVacacionHext ideAspvh;
    @JoinColumn(name = "ide_asmot", referencedColumnName = "ide_asmot")
    @ManyToOne
    private AsiMotivo ideAsmot;
    @JoinColumn(name = "ide_asgri", referencedColumnName = "ide_asgri")
    @ManyToOne
    private AsiGrupoIntervalo ideAsgri;

    public AsiDetalleHorasExtras() {
    }

    public AsiDetalleHorasExtras(Integer ideAsdhe) {
        this.ideAsdhe = ideAsdhe;
    }

    public Integer getIdeAsdhe() {
        return ideAsdhe;
    }

    public void setIdeAsdhe(Integer ideAsdhe) {
        this.ideAsdhe = ideAsdhe;
    }

    public Date getFechaAsdhe() {
        return fechaAsdhe;
    }

    public void setFechaAsdhe(Date fechaAsdhe) {
        this.fechaAsdhe = fechaAsdhe;
    }

    public BigDecimal getNroHorasAsdhe() {
        return nroHorasAsdhe;
    }

    public void setNroHorasAsdhe(BigDecimal nroHorasAsdhe) {
        this.nroHorasAsdhe = nroHorasAsdhe;
    }

    public String getActividadesAsdhe() {
        return actividadesAsdhe;
    }

    public void setActividadesAsdhe(String actividadesAsdhe) {
        this.actividadesAsdhe = actividadesAsdhe;
    }

    public BigDecimal getNroHorasAprobadasAsdhe() {
        return nroHorasAprobadasAsdhe;
    }

    public void setNroHorasAprobadasAsdhe(BigDecimal nroHorasAprobadasAsdhe) {
        this.nroHorasAprobadasAsdhe = nroHorasAprobadasAsdhe;
    }

    public Boolean getActivoAsdhe() {
        return activoAsdhe;
    }

    public void setActivoAsdhe(Boolean activoAsdhe) {
        this.activoAsdhe = activoAsdhe;
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

    public Date getHoraInicialAsdhe() {
        return horaInicialAsdhe;
    }

    public void setHoraInicialAsdhe(Date horaInicialAsdhe) {
        this.horaInicialAsdhe = horaInicialAsdhe;
    }

    public Date getHoraFinalAsdhe() {
        return horaFinalAsdhe;
    }

    public void setHoraFinalAsdhe(Date horaFinalAsdhe) {
        this.horaFinalAsdhe = horaFinalAsdhe;
    }

    public Boolean getRegistroNovedadAsdhe() {
        return registroNovedadAsdhe;
    }

    public void setRegistroNovedadAsdhe(Boolean registroNovedadAsdhe) {
        this.registroNovedadAsdhe = registroNovedadAsdhe;
    }

    public Boolean getAprobadoAsdhe() {
        return aprobadoAsdhe;
    }

    public void setAprobadoAsdhe(Boolean aprobadoAsdhe) {
        this.aprobadoAsdhe = aprobadoAsdhe;
    }

    public Boolean getNominaAsdhe() {
        return nominaAsdhe;
    }

    public void setNominaAsdhe(Boolean nominaAsdhe) {
        this.nominaAsdhe = nominaAsdhe;
    }

    public AsiPermisosVacacionHext getIdeAspvh() {
        return ideAspvh;
    }

    public void setIdeAspvh(AsiPermisosVacacionHext ideAspvh) {
        this.ideAspvh = ideAspvh;
    }

    public AsiMotivo getIdeAsmot() {
        return ideAsmot;
    }

    public void setIdeAsmot(AsiMotivo ideAsmot) {
        this.ideAsmot = ideAsmot;
    }

    public AsiGrupoIntervalo getIdeAsgri() {
        return ideAsgri;
    }

    public void setIdeAsgri(AsiGrupoIntervalo ideAsgri) {
        this.ideAsgri = ideAsgri;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsdhe != null ? ideAsdhe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiDetalleHorasExtras)) {
            return false;
        }
        AsiDetalleHorasExtras other = (AsiDetalleHorasExtras) object;
        if ((this.ideAsdhe == null && other.ideAsdhe != null) || (this.ideAsdhe != null && !this.ideAsdhe.equals(other.ideAsdhe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiDetalleHorasExtras[ ideAsdhe=" + ideAsdhe + " ]";
    }
    
}
