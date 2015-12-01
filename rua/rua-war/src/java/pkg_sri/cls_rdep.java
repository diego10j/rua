/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_sri;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.List;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Diego
 */
public class cls_rdep {

    private Document doc_rdep;
    private String path = "";
    private String nombre = "";
    private String fecha_inicio = "";
    private String fecha_fin = "";
    private Utilitario utilitario = new Utilitario();

    public void Rdep(String anioDesde, String anioHasta) {
        try {
            fecha_inicio = utilitario.getFormatoFecha(anioDesde + "-01-01");
            fecha_fin = utilitario.getFormatoFecha(anioHasta + "-12-31");

            List lis_sql = utilitario.getConexion().consultar("SELECT identificacion_empr from sis_empresa where ide_empr=" + utilitario.getVariable("ide_empr"));
            if (lis_sql != null && !lis_sql.isEmpty()) {
                doc_rdep = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                doc_rdep.setXmlVersion("1.0");
                doc_rdep.setXmlStandalone(true);

                Element raiz = doc_rdep.createElement("rdep");
                raiz.appendChild(crearElemento("numRuc", null, lis_sql.get(0) + ""));
                raiz.appendChild(crearElemento("anio", null, anioHasta));

                //      raiz.setAttribute("version", "2.0");
                doc_rdep.appendChild(raiz);

                //CABECERA
                // Element rdep = doc_rdep.createElement("rdep");
                //  raiz.appendChild(rdep);
                //DETALLE
                Element retRelDep = doc_rdep.createElement("retRelDep");
                raiz.appendChild(retRelDep);

                TablaGenerica tab_roles = utilitario.consultar("select ero.ide_geper,ide.alterno1_getid,identificac_geper,direccion_geper,numero_geper,"
                        + "ciu.codigo_geubi as ciudad, prov.codigo_geubi as provi,telefono_geper,tsa.alter_segur_rhtsa,count(identificac_geper) as meses "
                        + "from reh_cab_rol_pago  cro "
                        + "inner join reh_empleados_rol ero on cro.ide_rhcrp=ero.ide_rhcrp "
                        + "inner JOIN gen_persona emp on ero.ide_geper=emp.ide_geper "
                        + "inner join gen_tipo_identifi ide on emp.ide_getid=ide.ide_getid "
                        + "left join gen_ubicacion ciu on emp.ide_geubi =ciu.ide_geubi "
                        + "left join gen_ubicacion prov on ciu.gen_ide_geubi=prov.ide_geubi "
                        + "left join reh_tipo_salario tsa on emp.ide_rhtsa= tsa.ide_rhtsa "
                        + "where cro.fecha_inicio_rhcrp BETWEEN '" + fecha_inicio + "' and '" + fecha_fin + "' "
                        + "group by ero.ide_geper,ide.alterno1_getid,identificac_geper,direccion_geper,numero_geper, "
                        + "ciu.codigo_geubi, prov.codigo_geubi,telefono_geper,tsa.alter_segur_rhtsa ");
                System.out.println(tab_roles.getSql());
                for (int i = 0; i < tab_roles.getTotalFilas(); i++) {
                    Element datRetRelDep = doc_rdep.createElement("datRetRelDep");
                    retRelDep.appendChild(datRetRelDep);

                    datRetRelDep.appendChild(crearElemento("tipIdRet", null, Integer.parseInt(tab_roles.getValor(i, "alterno1_getid")) + ""));
                    datRetRelDep.appendChild(crearElemento("idRet", null, tab_roles.getValor(i, "identificac_geper")));
                    datRetRelDep.appendChild(crearElemento("dirCal", null, tab_roles.getValor(i, "direccion_geper")));
                    datRetRelDep.appendChild(crearElemento("dirNum", null, tab_roles.getValor(i, "numero_geper")));
                    datRetRelDep.appendChild(crearElemento("dirCiu", null, tab_roles.getValor(i, "ciudad")));
                    datRetRelDep.appendChild(crearElemento("dirProv", null, tab_roles.getValor(i, "provi")));
                    datRetRelDep.appendChild(crearElemento("tel", null, tab_roles.getValor(i, "telefono_geper")));
                    datRetRelDep.appendChild(crearElemento("sisSalNet", null, tab_roles.getValor(i, "alter_segur_rhtsa")));
                    String ideEmpleado = tab_roles.getValor(i, "ide_geper");
                    datRetRelDep.appendChild(crearElemento("suelSal", null, sumarRubro(ideEmpleado, "7")));
                    datRetRelDep.appendChild(crearElemento("sobSuelComRemu", null, sumarRubro(ideEmpleado, "48")));
                    datRetRelDep.appendChild(crearElemento("decimTer", null, sumarRubro(ideEmpleado, "58")));
                    datRetRelDep.appendChild(crearElemento("decimCuar", null, sumarRubro(ideEmpleado, "38")));
                    datRetRelDep.appendChild(crearElemento("fondoReserva", null, sumarRubro(ideEmpleado, "26")));
                    datRetRelDep.appendChild(crearElemento("partUtil", null, sumarRubro(ideEmpleado, "59")));
                    datRetRelDep.appendChild(crearElemento("desauOtras", null, sumarRubro(ideEmpleado, "52")));
                    datRetRelDep.appendChild(crearElemento("apoPerIess", null, sumarRubro(ideEmpleado, "28")));
                    datRetRelDep.appendChild(crearElemento("deducVivienda", null, sumarRubro(ideEmpleado, "40")));
                    datRetRelDep.appendChild(crearElemento("deducSalud", null, sumarRubro(ideEmpleado, "42")));
                    datRetRelDep.appendChild(crearElemento("deducEduca", null, sumarRubro(ideEmpleado, "41")));
                    datRetRelDep.appendChild(crearElemento("deducAliement", null, sumarRubro(ideEmpleado, "43")));
                    datRetRelDep.appendChild(crearElemento("deducVestim", null, sumarRubro(ideEmpleado, "39")));
                    datRetRelDep.appendChild(crearElemento("rebEspDiscap", null, sumarRubro(ideEmpleado, "45")));
                    datRetRelDep.appendChild(crearElemento("rebEspTerEd", null, sumarRubro(ideEmpleado, "46")));
                    datRetRelDep.appendChild(crearElemento("impRentEmpl", null, sumarRubro(ideEmpleado, "47")));
                    datRetRelDep.appendChild(crearElemento("subTotal", null, sumarRubro(ideEmpleado, "37")));
                    datRetRelDep.appendChild(crearElemento("numRet", null, sumarRubro(ideEmpleado, "-11111")));
                    datRetRelDep.appendChild(crearElemento("numMesEmplead", null, tab_roles.getValor(i, "meses")));
                    datRetRelDep.appendChild(crearElemento("intGrabGen", null, sumarRubro(ideEmpleado, "49")));
                    datRetRelDep.appendChild(crearElemento("deduccGastosOtrEmpl", null, sumarRubro(ideEmpleado, "50")));
                    datRetRelDep.appendChild(crearElemento("otrRebjOtrEmpl", null, sumarRubro(ideEmpleado, "51")));
                    String baseImponimbe = sumarRubro(ideEmpleado, "53");
                    datRetRelDep.appendChild(crearElemento("basImp", null, baseImponimbe));
                    datRetRelDep.appendChild(crearElemento("impRentCaus", null, sumarRubro(ideEmpleado, "53")));
                    datRetRelDep.appendChild(crearElemento("valRet", null, sumarRubro(ideEmpleado, "31")));
                    datRetRelDep.appendChild(crearElemento("valorImpempAnter", null, sumarRubro(ideEmpleado, "59")));
                    datRetRelDep.appendChild(crearElemento("anioRet", null, anioHasta));
                }
                ///ESCRIBE EL DOCUMENTO
                Source source = new DOMSource(doc_rdep);
                String master = System.getProperty("user.dir");
                System.out.println(master);
                nombre = "Rdep" + anioHasta + ".xml";
                Result result = new StreamResult(new java.io.File(master + "/" + nombre)); //nombre del archivo
                path = master + "/" + nombre;
                Result console = new StreamResult(System.out);
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(source, result);
                transformer.transform(source, console);

            }
        } catch (Exception e) {
            utilitario.agregarMensajeError("No se pudo generar el Anexo", "No hay información para generar el anexo");
        }
    }

    private String calcularRenta(String baseImponible) {
        TablaGenerica tab_rango = utilitario.consultar("SELECT * FROM sri_cab_impuesto_renta tabla  "
                + "inner join deta_impuesto_renta deta on deta.ide_srcir=tabla.ide_srcir "
                + "where activo_srcir=true "
                + "and " + baseImponible + " BETWEEN fraccion_basica_srdir and exceso_hasta_srdir");
        if (tab_rango.getTotalFilas() > 0) {

            String fraccion_base = tab_rango.getValor(0, "fraccion_basica_srdir");
            String imp_frac = tab_rango.getValor(0, "impu_frac_srdir");
            String porcentaje = tab_rango.getValor(0, "impu_frac_exce_srdir");
            System.out.println(baseImponible + "    " + fraccion_base + "    " + porcentaje);
            double impuesto = (Double.parseDouble(baseImponible) - Double.parseDouble(fraccion_base));
            impuesto = (Double.parseDouble(porcentaje) * impuesto);
            impuesto += Double.parseDouble(imp_frac) + impuesto;
            return utilitario.getFormatoNumero(impuesto);
        }
        return "0.00";
    }

    private String sumarRubro(String ideEmpleado, String ideRubro) {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select  ide_geper,SUM(valor_rhrro) from reh_rubros_rol rrol "
                + "inner join reh_empleados_rol erol on rrol.ide_rherl=erol.ide_rherl "
                + "inner join reh_cab_rol_pago cro on cro.ide_rhcrp=erol.ide_rhcrp "
                + "inner join reh_cab_rubro rcr on rrol.ide_rhcru=rcr.ide_rhcru and rrol.ide_rhcru=" + ideRubro + " "
                + "where cro.fecha_inicio_rhcrp BETWEEN '" + fecha_inicio + "' and '" + fecha_fin + "' "
                + "and erol.ide_geper=" + ideEmpleado + " group by ide_geper ");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                Object obj_fila[] = (Object[]) lis_sql.get(0);
                dou_valor = Double.parseDouble(obj_fila[1] + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    private Element crearElemento(String nombre, String[] atributos, String texto) {
        Element elemento = doc_rdep.createElement(nombre);
        if (atributos != null) {
            for (int i = 0; i < atributos.length; i++) {
                elemento.setAttribute(atributos[0], atributos[1]);
            }
        }
        if (texto == null) {
            texto = "";
        }
        elemento.appendChild(doc_rdep.createTextNode(texto));
        return elemento;
    }

    private Element crearElementoCDATA(String nombre, String[] atributos, String texto) {
        Element elemento = doc_rdep.createElement(nombre);
        if (atributos != null) {
            for (int i = 0; i < atributos.length; i++) {
                elemento.setAttribute(atributos[0], atributos[1]);
            }
        }
        elemento.appendChild(doc_rdep.createCDATASection(texto));
        return elemento;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void generarEmpleadosTxt() {
        //    Tabla tab_empleado=utilitario.consultar("SELECT * FROM GEN_PERSONA WHERE ")
    }
}
