/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.servicios;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sistema.aplicacion.Utilitario;
import portal.entidades.GenDivisionPolitica;
import portal.entidades.GenEmpleadosDepartamentoPar;
import portal.entidades.GthCargasFamiliares;
import portal.entidades.GthCargo;
import portal.entidades.GthConyuge;
import portal.entidades.GthCorreo;
import portal.entidades.GthDireccion;
import portal.entidades.GthEmpleado;
import portal.entidades.GthEstadoCivil;
import portal.entidades.GthGenero;
import portal.entidades.GthNacionalidad;
import portal.entidades.GthTelefono;
import portal.entidades.GthTipoDocumentoIdentidad;
import portal.entidades.GthTipoParentescoRelacion;
import portal.entidades.GthTipoTelefono;
import portal.entidades.GthUnionLibre;

/**
 *
 * @author Diego
 */
@Stateless

public class ServicioEmpleadoJPA {

    private Utilitario utilitario = new Utilitario();
    @PersistenceContext
    protected EntityManager manejador;

    public GthEmpleado getEmpleado(String ideGtemp) {

        try {
            Query q = manejador.createQuery("SELECT g FROM GthEmpleado g WHERE g.ideGtemp=" + ideGtemp);
            return (GthEmpleado) q.getSingleResult();
        } catch (Exception e) {
            System.out.println("ERROR getEmpleado : " + e.getMessage());
        } finally {
            
        }
        return null;
    }

    public List<GthTelefono> getTelefonos(String ideGtemp) {

        try {
            Query q = manejador.createQuery("SELECT g FROM GthTelefono g WHERE g.ideGtemp.ideGtemp =" + ideGtemp);
            return q.getResultList();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public List getTiposTelefono() {

        return utilitario.getConexion().consultar("select IDE_GTTIT,DETALLE_GTTIT from GTH_TIPO_TELEFONO");
    }

    public List getPaises() {
        return utilitario.getConexion().consultar("select IDE_GEDIP,DETALLE_GEDIP from GEN_DIVISION_POLITICA DP where DP.IDE_GETDP in (0) order by DP.DETALLE_GEDIP ASC ");
    }

    public List getProvincias(String idePais) {
        //Pner variable en el  0 y 1
        return utilitario.getConexion().consultar("select dp.IDE_GEDIP,dp.DETALLE_GEDIP as provincia from GEN_DIVISION_POLITICA DP INNER JOIN GEN_DIVISION_POLITICA DP1 ON DP1.IDE_GEDIP=DP.GEN_IDE_GEDIP where DP.IDE_GETDP in (0,1) and DP1.IDE_GEDIP=" + idePais + " order by DP.DETALLE_GEDIP ASC");
    }

    public List getCiudades(String ideProvincia) {
        //Pner variable en el  3
        return utilitario.getConexion().consultar("select DP1.IDE_GEDIP, "
                + "DP1.DETALLE_GEDIP AS CIUDAD_VIVE "
                + "from GEN_DIVISION_POLITICA DP "
                + "INNER JOIN GEN_TIPO_DIVISION_POLITICA TDP ON TDP.IDE_GETDP=DP.IDE_GETDP "
                + "INNER JOIN GEN_DIVISION_POLITICA DP1 ON DP1.IDE_GEDIP=DP.GEN_IDE_GEDIP "
                + "inner join GEN_DIVISION_POLITICA DP2 ON DP2.IDE_GEDIP=DP1.GEN_IDE_GEDIP "
                + "INNER join GEN_DIVISION_POLITICA DP3 ON DP3.IDE_GEDIP=DP2.GEN_IDE_GEDIP "
                + "INNER join GEN_DIVISION_POLITICA DP4 ON DP4.IDE_GEDIP=DP3.GEN_IDE_GEDIP "
                + "AND TDP.NIVEL_GETDP IN (5) where DP3.IDE_GEDIP=" + ideProvincia + " group by DP1.IDE_GEDIP, DP1.DETALLE_GEDIP "
                + "order by DP1.DETALLE_GEDIP ASC");
    }

    public List getParroquias(String ideCiudad) {
        //Pner variable en el  3        
        return utilitario.getConexion().consultar("select DP.IDE_GEDIP, DP.DETALLE_GEDIP AS PARROQUIA_VIVE from GEN_DIVISION_POLITICA DP INNER JOIN GEN_TIPO_DIVISION_POLITICA TDP ON TDP.IDE_GETDP=DP.IDE_GETDP INNER JOIN GEN_DIVISION_POLITICA DP1 ON DP1.IDE_GEDIP=DP.GEN_IDE_GEDIP inner join GEN_DIVISION_POLITICA DP2 ON DP2.IDE_GEDIP=DP1.GEN_IDE_GEDIP INNER join GEN_DIVISION_POLITICA DP3 ON DP3.IDE_GEDIP=DP2.GEN_IDE_GEDIP INNER join GEN_DIVISION_POLITICA DP4 ON DP4.IDE_GEDIP=DP3.GEN_IDE_GEDIP 				AND TDP.NIVEL_GETDP IN (5) WHERE DP1.IDE_GEDIP =" + ideCiudad + " order by DP.DETALLE_GEDIP ASC");
    }

    public GthCorreo getCorreoInstitucional(String ideGtemp) {
        try {
            Query q = manejador.createQuery("SELECT g FROM GthCorreo g WHERE g.ideGtemp.ideGtemp =" + ideGtemp + " and g.notificacionGtcor=1");
            if (q.getResultList().size() > 0) {
                return (GthCorreo) q.getResultList().get(0);
            }
            return (GthCorreo) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public GthCorreo getCorreoPersonal(String ideGtemp) {
        try {
            Query q = manejador.createQuery("SELECT g FROM GthCorreo g WHERE g.ideGtemp.ideGtemp =" + ideGtemp + " and g.notificacionGtcor=0");
            if (q.getResultList().size() > 0) {
                return (GthCorreo) q.getResultList().get(0);
            }
            return (GthCorreo) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public GthConyuge getConyuque(String ideGtemp) {

        try {
            Query q = manejador.createQuery("SELECT g FROM GthConyuge g WHERE g.ideGtemp.ideGtemp =" + ideGtemp);
            return (GthConyuge) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public GthDireccion getDireccionEmpleado(String ideGtemp) {

        try {
            Query q = manejador.createQuery("SELECT g FROM GthDireccion g WHERE g.ideGtemp.ideGtemp =" + ideGtemp);
            if (q.getResultList().size() > 0) {
                return (GthDireccion) q.getResultList().get(0);
            }
            return (GthDireccion) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public GenDivisionPolitica getDivisionPolitica(String ideGedip) {

        try {
            Query q = manejador.createNamedQuery("GenDivisionPolitica.findByIdeGedip");
            q.setParameter("ideGedip", new BigDecimal(ideGedip));
            return (GenDivisionPolitica) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public GthTipoTelefono getTipoTelefono(String ideGttit) {

        try {
            Query q = manejador.createNamedQuery("GthTipoTelefono.findByIdeGttit");
            q.setParameter("ideGttit", new BigDecimal(ideGttit));
            return (GthTipoTelefono) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public GthTipoParentescoRelacion getTipoParentesco(String ideGttpr) {

        try {
            Query q = manejador.createNamedQuery("GthTipoParentescoRelacion.findByIdeGttpr");
            q.setParameter("ideGttpr", new BigDecimal(ideGttpr));
            return (GthTipoParentescoRelacion) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public GthTipoDocumentoIdentidad getTipoDocumentoIdentidad(String ideGttdi) {

        try {
            Query q = manejador.createNamedQuery("GthTipoDocumentoIdentidad.findByIdeGttdi");
            q.setParameter("ideGttdi", new BigDecimal(ideGttdi));
            return (GthTipoDocumentoIdentidad) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public GthGenero getGenero(String ideGtgen) {

        try {
            Query q = manejador.createNamedQuery("GthGenero.findByIdeGtgen");
            q.setParameter("ideGtgen", new BigDecimal(ideGtgen));
            return (GthGenero) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public GthEstadoCivil getEstadoCivil(String ideGtesc) {

        try {
            Query q = manejador.createNamedQuery("GthEstadoCivil.findByIdeGtesc");
            q.setParameter("ideGtesc", new BigDecimal(ideGtesc));
            return (GthEstadoCivil) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public GthCargo getCargo(String ideGtcar) {

        try {
            Query q = manejador.createNamedQuery("GthCargo.findByIdeGtcar");
            q.setParameter("ideGtcar", new BigDecimal(ideGtcar));
            return (GthCargo) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public GthNacionalidad getNacionalidad(String ideGtnac) {

        try {
            Query q = manejador.createNamedQuery("GthNacionalidad.findByIdeGtnac");
            q.setParameter("ideGtnac", new BigDecimal(ideGtnac));
            return (GthNacionalidad) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public String guardarTelefono(GthTelefono telefono) {

        try {

            if (telefono.getIdeGttel() == null) {
                //asignar maximo

                long ideaspvh = new Long(utilitario.getConexion().getMaximo("GTH_TELEFONO", "IDE_GTTEL", 1));
                System.out.println("guardar telefono " + ideaspvh);
                Integer conertideaspvh = 1; // (int) ideaspvh;
                telefono.setIdeGttel(conertideaspvh); //maximo de utilitario
                System.out.println("telefono " + telefono);
                manejador.persist(telefono);
            } else {
                manejador.merge(telefono);
            }

        } catch (Exception e) {

            return e.getMessage();
        } finally {
            
        }
        return "";
    }

    public String eliminarTelefono(String telefono) {

        try {

            GthTelefono borraTelefono = manejador.find(GthTelefono.class, new BigDecimal(telefono));
            manejador.remove(borraTelefono);

        } catch (Exception e) {

            return e.getMessage();
        } finally {
            
        }
        return "";
    }

    public GenEmpleadosDepartamentoPar getEmpleadoDepartamentoPartida(String ideGtemp) {

        try {
            Query q = manejador.createQuery("SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.ideGtemp.ideGtemp =" + ideGtemp + " and g.activoGeedp=true");
            return (GenEmpleadosDepartamentoPar) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public String eliminarCargaFamiliar(String carga) {

        try {

            GthCargasFamiliares borraCarga = manejador.find(GthCargasFamiliares.class, new BigDecimal(carga));
            manejador.remove(borraCarga);

        } catch (Exception e) {

            return e.getMessage();
        } finally {
            
        }
        return "";
    }

    public String guardarDireccion(GthDireccion direccion) {

        try {

            manejador.joinTransaction();
            //Guarda o modifica direccion            
            if (direccion.getIdeGtdir() == null) {
                long idegtdir = new Long(utilitario.getConexion().getMaximo("GTH_DIRECCION", "IDE_GTDIR", 1));
                Integer conertideaspvh = (int) idegtdir;
                direccion.setIdeGtdir(conertideaspvh); //maximo de utilitario
                manejador.persist(direccion);
            } else {
                manejador.merge(direccion);

            }

        } catch (Exception e) {

            return e.getMessage();
        } finally {
            
        }
        return "";
    }

    public String guardarEmpleado(GthEmpleado empleado) {

        try {

            manejador.joinTransaction();
            manejador.merge(empleado);

        } catch (Exception e) {

            return e.getMessage();
        } finally {
            
        }
        return "";
    }

    public String guardarCorreo(GthCorreo correoInstitucional, GthCorreo correoPersonal) {

        try {

            manejador.joinTransaction();
            //Guarda Correos
            if (correoInstitucional.getIdeGtcor() == null) {
                long idegtcor = new Long(utilitario.getConexion().getMaximo("GTH_CORREO", "IDE_GTCOR", 1));
                Integer conertideaspvh = (int) idegtcor;
                correoInstitucional.setIdeGtcor(conertideaspvh); //maximo de utilitario
                manejador.persist(correoInstitucional);

            } else {
                manejador.merge(correoInstitucional);
            }

            if (correoPersonal.getIdeGtcor() == null) {
                //si hay valor en el correo guardo
                if (correoPersonal.getDetalleGtcor() != null && !correoPersonal.getDetalleGtcor().isEmpty()) {
                    long idegtcor = new Long(utilitario.getConexion().getMaximo("GTH_CORREO", "IDE_GTCOR", 1));
                    Integer conertideaspvh = (int) idegtcor;
                    correoPersonal.setIdeGtcor(conertideaspvh); //maximo de utilitario
                    manejador.persist(correoPersonal);
                }
            } else {
                //si hay valor en el correo modifico
                if (correoPersonal.getDetalleGtcor() != null && !correoPersonal.getDetalleGtcor().isEmpty()) {
                    manejador.merge(correoPersonal);
                }
            }

        } catch (Exception e) {

            return e.getMessage();
        } finally {
            
        }
        return "";
    }

    public String guardarCargaFamiliar(GthCargasFamiliares carga) {

        try {

            manejador.joinTransaction();
            //Guarda o modifica 
            if (carga.getIdeGtcaf() == null) {
                long idegtcaf = new Long(utilitario.getConexion().getMaximo("GTH_CARGAS_FAMILIARES", "IDE_GTCAF", 1));
                Integer conertideaspvh = (int) idegtcaf;
                carga.setIdeGtcaf(conertideaspvh);
                manejador.persist(carga);
            } else {
                manejador.merge(carga);
            }

        } catch (Exception e) {

            return e.getMessage();
        } finally {
            
        }
        return "";
    }

    public String guardarConyugue(GthConyuge conyugue, GthTelefono telefono) {

        try {

            manejador.joinTransaction();
            //Guarda o modifica 
            if (conyugue.getIdeGtcon() == null) {
                long idegtcon = new Long(utilitario.getConexion().getMaximo("GTH_CONYUGE", "IDE_GTCON", 1));
                Integer conertideaspvh = (int) idegtcon;
                conyugue.setIdeGtcon(conertideaspvh);
                manejador.persist(conyugue);
                telefono.setIdeGtcon(conyugue);
                long idegttel = new Long(utilitario.getConexion().getMaximo("GTH_TELEFONO", "IDE_GTTEL", 1));
                Integer conertideidegttel = (int) idegttel;
                telefono.setIdeGttel(conertideidegttel);
                manejador.persist(telefono);
            } else {
                manejador.merge(conyugue);
                //Si el telefono es nuevo
                if (telefono.getIdeGttel() == null) {
                    telefono.setIdeGtcon(conyugue);
                    long idegttel = new Long(utilitario.getConexion().getMaximo("GTH_TELEFONO", "IDE_GTTEL", 1));
                    Integer conertideidegttel = (int) idegttel;
                    telefono.setIdeGttel(conertideidegttel);
                    manejador.persist(telefono);
                } else {
                    manejador.merge(telefono);
                }
            }

        } catch (Exception e) {

            return e.getMessage();
        } finally {
            
        }
        return "";
    }

    public String guardarConyugueUnionLibre(GthConyuge conyugue, GthTelefono telefono, GthUnionLibre union) {

        try {

            manejador.joinTransaction();
            //Guarda o modifica 
            if (conyugue.getIdeGtcon() == null) {
                long idegtcon = new Long(utilitario.getConexion().getMaximo("GTH_CONYUGUE", "IDE_GTCON", 1));
                Integer conertideaspvh = (int) idegtcon;
                conyugue.setIdeGtcon(conertideaspvh);
                manejador.persist(conyugue);
                telefono.setIdeGtcon(conyugue);
                long idegttel = new Long(utilitario.getConexion().getMaximo("GTH_TELEFONO", "IDE_GTTEL", 1));
                Integer conertidegttel = (int) idegttel;
                telefono.setIdeGttel(conertidegttel);
                manejador.persist(telefono);
                long idegtunl = new Long(utilitario.getConexion().getMaximo("GTH_UNION_LIBRE", "IDE_GTUNL", 1));
                Integer conertidegtunl = (int) idegtunl;
                union.setIdeGtunl(conertidegtunl);
                union.setIdeGtcon(conyugue);
                manejador.persist(union);
            } else {
                manejador.merge(conyugue);
                //Si el telefono es nuevo
                if (telefono.getIdeGttel() == null) {
                    telefono.setIdeGtcon(conyugue);
                    long idegttel = new Long(utilitario.getConexion().getMaximo("GTH_TELEFONO", "IDE_GTTEL", 1));
                    Integer conertidegttel = (int) idegttel;
                    telefono.setIdeGttel(conertidegttel);
                    manejador.persist(telefono);
                } else {
                    manejador.merge(telefono);
                }
                //Union libre
                if (union.getIdeGtunl() == null) {
                    long idegttel = new Long(utilitario.getConexion().getMaximo("GTH_UNION_LIBRE", "IDE_GTUNL", 1));
                    Integer conertidegttel = (int) idegttel;
                    union.setIdeGtunl(conertidegttel);
                    union.setIdeGtcon(conyugue);
                    manejador.persist(union);
                } else {
                    manejador.merge(union);
                }
            }

        } catch (Exception e) {

            return e.getMessage();
        } finally {
            
        }
        return "";
    }

    public List<GthCargasFamiliares> getCargasFamiliares(String ideGtemp) {

        try {
            Query q = manejador.createQuery("SELECT g FROM GthCargasFamiliares g WHERE g.ideGtemp.ideGtemp =" + ideGtemp);
            return q.getResultList();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public GthUnionLibre getUnionLibre(String ideGtcon) {

        try {
            Query q = manejador.createQuery("SELECT g FROM GthUnionLibre g WHERE g.ideGtcon.ideGtcon =" + ideGtcon);
            return (GthUnionLibre) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public String eliminarConyugue(String ideGtemp) {

        try {
            GthConyuge conygue = getConyuque(ideGtemp);
            if (conygue != null) {

                manejador.joinTransaction();
                //Borra telefono de conyugue
                List<GthTelefono> telefonos = getListaTelefonoConyugue(conygue.getIdeGtcon().toString());
                if (telefonos != null && !telefonos.isEmpty()) {
                    for (GthTelefono telefonoActual : telefonos) {
                        manejador.remove(telefonoActual);
                    }
                }
                //Borra datos de union
                GthUnionLibre union = getUnionLibre(conygue.getIdeGtcon().toString());
                if (union != null) {
                    manejador.remove(union);
                }
                manejador.remove(conygue);

            }

        } catch (Exception e) {

            return e.getMessage();
        } finally {
            
        }
        return "";
    }

    public GthTelefono getTelefonoConyugue(String ideGtcon) {

        try {
            Query q = manejador.createQuery("SELECT g FROM GthTelefono g WHERE g.ideGtcon.ideGtcon =" + ideGtcon);
            return (GthTelefono) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public List<GthTelefono> getListaTelefonoConyugue(String ideGtcon) {

        try {
            Query q = manejador.createQuery("SELECT g FROM GthTelefono g WHERE g.ideGtcon.ideGtcon =" + ideGtcon);
            return q.getResultList();
        } catch (Exception e) {
        } finally {
            
        }
        return null;
    }

    public List getGeneros() {
        return utilitario.getConexion().consultar("select IDE_GTGEN,DETALLE_GTGEN from GTH_GENERO  order by DETALLE_GTGEN");
    }

    public List getTiposParentesco() {
        return utilitario.getConexion().consultar("select IDE_GTTPR,DETALLE_GTTPR from GTH_TIPO_PARENTESCO_RELACION where DEPENDIENTE_GTTPR=true order by DETALLE_GTTPR");
    }

    public List getTiposDocumentoIdentidad() {
        return utilitario.getConexion().consultar("select IDE_GTTDI,DETALLE_GTTDI from GTH_TIPO_DOCUMENTO_IDENTIDAD  order by DETALLE_GTTDI");
    }

    public List getEstadosCiviles() {
        return utilitario.getConexion().consultar("select IDE_GTESC,DETALLE_GTESC from GTH_ESTADO_CIVIL  order by DETALLE_GTESC");
    }

    public List getCargos() {
        return utilitario.getConexion().consultar("select IDE_GTCAR,DETALLE_GTCAR from GTH_CARGO  order by DETALLE_GTCAR");
    }

    public List getNacionalidades() {
        return utilitario.getConexion().consultar("select IDE_GTNAC,DETALLE_GTNAC from GTH_NACIONALIDAD order by DETALLE_GTNAC");
    }

    public List getActividadesLaborales() {
        return utilitario.getConexion().consultar("select IDE_GTACL,DETALLE_GTACL from GTH_ACTIVIDAD_LABORAL order by DETALLE_GTACL");
    }
}
