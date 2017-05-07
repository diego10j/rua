/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.servicios;

import framework.aplicacion.TablaGenerica;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sistema.aplicacion.Utilitario;
import portal.entidades.AsiDetalleHorasExtras;
import portal.entidades.AsiGrupoIntervalo;
import portal.entidades.AsiMotivo;
import portal.entidades.AsiPermisosVacacionHext;

/**
 *
 * @author Diego
 */
@Stateless

public class ServicioHorasExtraJPA {

    private Utilitario utilitario = new Utilitario();
    @PersistenceContext
    protected EntityManager manejador;

    public List getMarcacionesHorasExtra(String IDE_GEEDP, String fechaInicio, String fechaFin) {
        //Busca las marcaciones que son consideradas para horas Extra

        return utilitario.getConexion().consultar("select IDE_ASVAA,FECHA_MARCACION_ASVAA,EVENTO_ASVAA,TO_CHAR(HORA_MARCA_SALIDA_ASVAA,'HH24:MM:SS'),DIFERENCIA_SALIDA_ASVAA from ASI_VALIDA_ASISTENCIA where  IMPORTO_ASVAA != 1 AND DIFERENCIA_SALIDA_ASVAA>0 and IDE_GEEDP=" + IDE_GEEDP + " and IDE_ASNOV is NUll and FECHA_MARCACION_ASVAA BETWEEN TO_DATE('" + fechaInicio + "', 'YYYY:MM:DD') AND TO_DATE('" + fechaFin + "', 'YYYY:MM:DD') order by FECHA_MARCACION_ASVAA desc");
    }

    public void cambiarEstadoImportado(String ide_asvaa) {
        utilitario.getConexion().ejecutarSql("UPDATE ASI_VALIDA_ASISTENCIA SET IMPORTO_ASVAA=1 WHERE IDE_ASVAA IN(" + ide_asvaa + ")");
    }

    public AsiMotivo getMotivo(String ideAsmot) {
       
        try {
            Query q = manejador.createNamedQuery("AsiMotivo.findByIdeAsmot");
            q.setParameter("ideAsmot", new BigDecimal(ideAsmot));
            return (AsiMotivo) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public AsiGrupoIntervalo getTipoHora(String ideAsgri) {
       
        try {
            Query q = manejador.createNamedQuery("AsiGrupoIntervalo.findByIdeAsgri");
            q.setParameter("ideAsgri", new BigDecimal(ideAsgri));
            return (AsiGrupoIntervalo) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public String guardarHorasExtra(AsiPermisosVacacionHext solicitud, List<AsiDetalleHorasExtras> detalles) {
       
        try {
           
            manejador.joinTransaction();
            long ideaspvh=new Long(utilitario.getConexion().getMaximo("ASI_PERMISOS_VACACION_HEXT", "IDE_ASPVH", 1));
            Integer conertideaspvh= (int) ideaspvh;
            solicitud.setIdeAspvh(conertideaspvh); //maximo de utilitario
            manejador.persist(solicitud);

            for (AsiDetalleHorasExtras detalleActual : detalles) {
                detalleActual.setIdeAspvh(solicitud);
                long ideasdhe= new Long(utilitario.getConexion().getMaximo("ASI_DETALLE_HORAS_EXTRAS", "IDE_ASDHE", 1));
                Integer convertideasdhe= (int) ideasdhe;
                detalleActual.setIdeAsdhe(convertideasdhe); //maximo de utilitario                
                manejador.persist(detalleActual);
            }
           
        } catch (Exception e) {
           
            return e.getMessage();
        } finally {
            
        }
        return "";
    }

    public List getListaMotivos() {
        return utilitario.getConexion().consultar("select IDE_ASMOT,DETALLE_ASMOT from ASI_MOTIVO order by DETALLE_ASMOT");
    }

    public TablaGenerica getHorariosHorasExtraporDia(String IDE_GEDIA, String IDE_SUCU, String IDE_GTGRE) {
        //Busca los horarios de un empleado en un dia determinado
        String p_asi_hora_normal = "3";
        return utilitario.consultar("SELECT DH.IDE_ASHOR,DH.IDE_GEDIA,HO.IDE_ASGRI,HO.IDE_SUCU,TO_CHAR(HO.HORA_INICIAL_ASHOR ,'HH24:Mi:SS') AS HORA_INICIO,TO_char(HO.HORA_FINAL_ASHOR,'HH24:Mi:SS') AS HORA_FIN,TUR.IDE_GTGRE "
                + "from ASI_DIA_HORARIO dh "
                + "INNER JOIN ASI_HORARIO ho on DH.IDE_ASHOR=HO.IDE_ASHOR "
                + "INNER JOIN ASI_TURNOS TUR on DH.IDE_ASHOR=TUR.IDE_ASHOR "
                + "inner join GTH_GRUPO_EMPLEADO GRU on GRU.IDE_GTGRE= TUR.IDE_GTGRE "
                + "where HO.IDE_SUCU=" + IDE_SUCU + " and TUR.IDE_GTGRE=" + IDE_GTGRE + " "
                + "and DH.IDE_GEDIA=" + IDE_GEDIA + " "
                + "and IDE_ASGRI!=" + p_asi_hora_normal + " "//NO CONSIDERA HORA NORMAL 
                + "ORDER BY IDE_SUCU,IDE_GTGRE,HO.IDE_ASHOR,IDE_GEDIA");
    }

    public List<AsiPermisosVacacionHext> getSolicitudesHorasExtra(String ideGeedp) {
       
        try {
            Query q = manejador.createQuery("SELECT a FROM AsiPermisosVacacionHext a WHERE a.ideGeedp.ideGeedp =" + ideGeedp + " and a.tipoAspvh=3 order by a.fechaSolicitudAspvh");
            return q.getResultList();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public List<AsiDetalleHorasExtras> getDetallesHorasExtra(String ideAspvh) {
       
        try {
            Query q = manejador.createQuery("SELECT a FROM AsiDetalleHorasExtras a WHERE a.ideAspvh.ideAspvh=" + ideAspvh + "");
            return q.getResultList();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }
}
