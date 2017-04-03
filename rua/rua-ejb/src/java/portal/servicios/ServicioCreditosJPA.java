/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.servicios;

import java.math.BigDecimal;
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
import portal.entidades.GenEmpleadosDepartamentoPar;
import portal.entidades.GthTelefono;
import portal.entidades.NrhAnticipo;
import portal.entidades.NrhGarante;
import portal.entidades.NrhMotivoAnticipo;
import portal.entidades.NrhTipoGarante;


@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ServicioCreditosJPA {

    private Utilitario utilitario = new Utilitario();
    @PersistenceUnit(unitName="sistema")
    private EntityManagerFactory fabrica;
    @Resource
    private UserTransaction utx;

    public List<NrhAnticipo> getSolicitudesAnticipo(String ideGeedp) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createQuery("SELECT n FROM NrhAnticipo n WHERE n.ideGeedp.ideGeedp =" + ideGeedp + " and n.anticipoNrant=1 order by n.nroAnticipoNrant desc ");
            return q.getResultList();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public String guardarSolicitudAnticipo(NrhAnticipo solicitud, NrhGarante garante, GthTelefono telefono) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            utx.begin();
            manejador.joinTransaction();
            solicitud.setIdeNrmoa(manejador.find(NrhMotivoAnticipo.class, solicitud.getIdeNrmoa().getIdeNrmoa()));
            //Guarda o modifica solicitud
            if (solicitud.getIdeNrant() == null) {

            	long idenrant=new Long(utilitario.getConexion().getMaximo("NRH_ANTICIPO", "IDE_NRANT", 1));
            	  Integer conertidenrant= (int) idenrant;
                solicitud.setIdeNrant(conertidenrant); //maximo de utilitario
                manejador.persist(solicitud);
                if (garante != null && garante.getDocumentoIdentidadcNrgar() != null) {
                    garante.setIdeNrant(solicitud);
                    long idenrgar=new Long(utilitario.getConexion().getMaximo("NRH_GARANTE", "IDE_NRGAR", 1));
              	  Integer conertidenrgar= (int) idenrgar;
                    garante.setIdeNrgar(conertidenrgar); //maximo de utilitario                    
                    manejador.persist(garante);
//                    telefono.setIdeNrgar(garante);
                    long idegttel=new Long(utilitario.getConexion().getMaximo("GTH_TELEFONO", "IDE_GTTEL", 1));
                	  Integer conertidegttel= (int) idegttel;
                    telefono.setIdeGttel(conertidegttel); //maximo de utilitario
                    manejador.persist(telefono);
                }
            } else {
                manejador.merge(solicitud);
                manejador.merge(garante);
                manejador.merge(telefono);
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

    public NrhTipoGarante getGarante(String ideNrtig) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createNamedQuery("NrhTipoGarante.findByIdeNrtig");
            q.setParameter("ideNrtig", new BigDecimal(ideNrtig));
            return (NrhTipoGarante) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public GenEmpleadosDepartamentoPar getEmpleadoDepartamentoPartida(String ideGeedp) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createNamedQuery("GenEmpleadosDepartamentoPar.findByIdeGeedp");
            q.setParameter("ideGeedp", new BigDecimal(ideGeedp));
            return (GenEmpleadosDepartamentoPar) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public List getMotivosAnticipo() {
        return utilitario.getConexion().consultar("select IDE_NRMOA,DETALLE_NRMOA from NRH_MOTIVO_ANTICIPO order by DETALLE_NRMOA");
    }

    public List getTiposGarante() {
        return utilitario.getConexion().consultar("select IDE_NRTIG,DETALLE_NRTIG from NRH_TIPO_GARANTE  order by DETALLE_NRTIG");
    }

    public List getEmpleadosDepartamentos() {
        return utilitario.getConexion().consultar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
                + "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
                + "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || "
                + "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
                + "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
                + "SUCU.NOM_SUCU, AREA.DETALLE_GEARE, "
                + "DEPA.DETALLE_GEDEP "
                + "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
                + "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
                + "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
                + "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
                + "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
                + "WHERE EPAR.ACTIVO_GEEDP=true");
    }

    public List getCreditosAprobado(String IDE_GEEDP) {
        return utilitario.getConexion().consultar("SELECT ide_nrant,nro_anticipo_nrant,fecha_aprobacion_nrant,fecha_vencimiento_nrant,monto_aprobado_nrant,observacion_nrant,e.jefe_gerente, "
                + "d.jefe_inmediato, "
                + "c.jefe_talento_humano "
                + "FROM nrh_anticipo  a "
                + "left join( "
                + "SELECT ide_gtemp,apellido_paterno_gtemp ||' '|| (case when  apellido_materno_gtemp is null then '' else  apellido_materno_gtemp end) ||' '|| primer_nombre_gtemp ||' '|| (case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as empleado FROM gth_empleado "
                + ")b ON b.ide_gtemp=a.ide_gtemp "
                + "left join( "
                + "SELECT a.ide_geedp as ide_geedp_talento, b.apellido_paterno_gtemp ||' '||(case when  b.apellido_materno_gtemp is null then '' else  b.apellido_materno_gtemp end)  ||' '|| b.primer_nombre_gtemp ||' '|| (case when b.segundo_nombre_gtemp is null then '' else b.segundo_nombre_gtemp end) as jefe_talento_humano FROM gen_empleados_departamento_par a, gth_empleado b "
                + "where a.ide_gtemp=b.ide_gtemp  "
                + ")c ON c.ide_geedp_talento=a.gen_ide_geedp3 "
                + "left join( "
                + "SELECT a.ide_geedp as ide_geedp_inmediato, b.apellido_paterno_gtemp ||' '|| (case when  b.apellido_materno_gtemp is null then '' else  b.apellido_materno_gtemp end) ||' '|| b.primer_nombre_gtemp ||' '|| (case when b.segundo_nombre_gtemp is null then '' else b.segundo_nombre_gtemp end) as jefe_inmediato FROM gen_empleados_departamento_par a, gth_empleado b "
                + "where a.ide_gtemp=b.ide_gtemp  "
                + ")d ON d.ide_geedp_inmediato=a.gen_ide_geedp2 "
                + "left join( "
                + "SELECT a.ide_geedp as ide_geedp_gerente, b.apellido_paterno_gtemp ||' '|| (case when  b.apellido_materno_gtemp is null then '' else  b.apellido_materno_gtemp end) ||' '|| b.primer_nombre_gtemp ||' '||(case when b.segundo_nombre_gtemp is null then '' else b.segundo_nombre_gtemp end) as jefe_gerente FROM gen_empleados_departamento_par a, gth_empleado b "
                + "where a.ide_gtemp=b.ide_gtemp  "
                + ")e ON e.ide_geedp_gerente=a.gen_ide_geedp "
                + "where a.aprobado_nrant=true and a.ide_geedp="+ IDE_GEEDP+" "
                + "order by nro_anticipo_nrant desc");
    }

    public List getTablaAmortizacionCredito(String IDE_NRANT) {
        return utilitario.getConexion().consultar("SELECT capital_nramo,interes_nramo,principal_nramo,cuota_nramo,fecha_cancelado_nramo, "
                + "nro_cuota_nramo,b.detalle_nrrub,fecha_vencimiento_nramo "
                + "FROM nrh_amortizacion a "
                + "left join ( SELECT ide_nrrub as ide_rubro,detalle_nrrub FROM nrh_rubro )b ON b.ide_rubro=a.ide_nrrub "
                + "where ide_nrani in ( "
                + "select ide_nrani from  nrh_anticipo_interes where ide_nrant in (select ide_nrant from nrh_anticipo where ide_nrant=" + IDE_NRANT + ") "
                + ") ORDER BY nro_cuota_nramo");        
    }

    public GthTelefono getTelefonoGarate(String ideNrgar) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createQuery("SELECT g FROM GthTelefono g WHERE g.ideNrgar.ideNrgar =" + ideNrgar);
            return (GthTelefono) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }
}
