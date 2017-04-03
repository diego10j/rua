/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.servicios;

import framework.aplicacion.TablaGenerica;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import sistema.aplicacion.Utilitario;
import portal.entidades.AsiMotivo;
import portal.entidades.AsiPermisosVacacionHext;
import portal.entidades.GenAnio;
import portal.entidades.GenMes;
import portal.entidades.GenPeriodo;
import portal.entidades.GenPeriodoPK;

/**
 *
 * @author Diego
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ServicioVacacionesJPA {

    private Utilitario utilitario = new Utilitario();
    @PersistenceUnit(unitName="sistema")
    private EntityManagerFactory fabrica;
    @Resource
    private UserTransaction utx;

    public List<AsiPermisosVacacionHext> getSolicitudesVacaciones(String ideGeedp) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createQuery("SELECT a FROM AsiPermisosVacacionHext a WHERE a.ideGeedp.ideGeedp =" + ideGeedp + " and a.tipoAspvh=2 order by a.fechaSolicitudAspvh");
            return q.getResultList();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public String guardarSolicitudVacaciones(AsiPermisosVacacionHext solicitud) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            utx.begin();
            manejador.joinTransaction();
            //solicitud.setIdeAsmot(manejador.find(AsiMotivo.class, solicitud.getIdeAsmot().getIdeAsmot()));
           // solicitud.getGenPeriodo().setGenAnio(manejador.find(GenAnio.class, solicitud.getGenPeriodo().getGenAnio().getIdeGeani()));
           // solicitud.getGenPeriodo().setGenMes(manejador.find(GenMes.class, solicitud.getGenPeriodo().getGenMes().getIdeGemes()));
           // solicitud.getGenPeriodo().setGenPeriodoPK(new GenPeriodoPK(new BigInteger(solicitud.getGenPeriodo().getGenMes().getIdeGemes().toString()), new BigInteger(solicitud.getGenPeriodo().getGenAnio().getIdeGeani().toString())));
           // solicitud.setGenPeriodo(manejador.find(GenPeriodo.class, solicitud.getGenPeriodo().getGenPeriodoPK()));
            //Guarda o modifica solicitud
            if (solicitud.getIdeAspvh() == null) {
            	long ideaspvh=new Long(utilitario.getConexion().getMaximo("ASI_PERMISOS_VACACION_HEXT", "IDE_ASPVH", 1));
            	  Integer conertideaspvh= (int) ideaspvh;
                solicitud.setIdeAspvh(conertideaspvh); //maximo de utilitario
                manejador.persist(solicitud);
            } else {
                manejador.merge(solicitud);
            }
            utx.commit();
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception e1) {
            }
            return e.getMessage();
        } finally {
            manejador.close();
        }
        return "";
    }

    public List getMotivosVacaciones() {
        return utilitario.getConexion().consultar("select IDE_ASMOT,DETALLE_ASMOT from ASI_MOTIVO order by DETALLE_ASMOT");
    }

    public List getAnios() {
        return utilitario.getConexion().consultar("select IDE_GEANI,DETALLE_GEANI from GEN_ANIO order by DETALLE_GEANI");
    }

    public List getMeses() {
        return utilitario.getConexion().consultar("select IDE_GEMES,DETALLE_GEMES from GEN_MES order by IDE_GEMES");
    }

    public Object getPeriodosVacaciones(String IDE_GEEDP) {
        TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP=" + IDE_GEEDP + ") AND ACTIVO_ASVAC=true");        
        if (!tab_periodo.isEmpty()) {
            return utilitario.getConexion().consultar("SELECT IDE_ASVAC,DIA_ACUMULADO,NRO_DIAS_ADICIONAL,DIA_DESCONTADO,DIA_SOLICITADO,  "
                    + " DIA_ACUMULADO+NRO_DIAS_ADICIONAL as NRO_TOTALES_VACACIONES, (DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_PENDIENTES  "
                    + " FROM (  "
                    + " SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO,  "
                    + " (case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL,  "
                    + " (case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO,  "
                    + " (case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO  "
                    + " FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true GROUP BY IDE_ASVAC  "
                    + " ) a where IDE_ASVAC=" + tab_periodo.getValor("IDE_ASVAC")).get(0);
        }
        return null;
    }
    
     public List getResumeVacaciones(String IDE_GEEDP) {
        TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP=" + IDE_GEEDP + ") AND ACTIVO_ASVAC=true");        
        if (!tab_periodo.isEmpty()) {
            return utilitario.getConexion().consultar("SELECT " +
					"(cast(PERIODO as integer) -1) ||' - '|| periodo AS PERIODO,DIA_ACUMULADO,NRO_DIAS_ADICIONAL,DIA_DESCONTADO,DIA_SOLICITADO, " +
					"DIA_ACUMULADO+NRO_DIAS_ADICIONAL as NRO_TOTALES_VACACIONES, (DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_PENDIENTES " +
					" FROM ( " +
					"SELECT IDE_ASVAC,cast(PERIODO as integer) -1 AS ANTERIOR,periodo,SUM(DIA_ACUMULADO) AS DIA_ACUMULADO,SUM(NRO_DIAS_ADICIONAL) AS NRO_DIAS_ADICIONAL, " +
					"SUM(DIA_DESCONTADO) AS DIA_DESCONTADO,SUM(DIA_SOLICITADO) AS DIA_SOLICITADO " +
					"FROM ( " +
					"SELECT IDE_ASVAC, " +
					"TO_CHAR(FECHA_NOVEDAD_ASDEV,'yyyy')AS periodo, " +
					"(case when DIA_ACUMULADO_ASDEV is null then 0 else DIA_ACUMULADO_ASDEV end)AS DIA_ACUMULADO, " +
					"(case when DIA_ADICIONAL_ASDEV is null then 0 else DIA_ADICIONAL_ASDEV end) as NRO_DIAS_ADICIONAL, " +
					"(case when DIA_DESCONTADO_ASDEV is null then 0 else DIA_DESCONTADO_ASDEV end)AS DIA_DESCONTADO, " +
					"(case when DIA_SOLICITADO_ASDEV is null then 0 else DIA_SOLICITADO_ASDEV end)AS DIA_SOLICITADO " +
					"FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true AND IDE_ASVAC="+tab_periodo.getValor("IDE_ASVAC")+
					")a GROUP BY a.periodo,a.IDE_ASVAC " +
					")b ORDER BY PERIODO DESC");
        }
        return null; 
     }
    
    
}
