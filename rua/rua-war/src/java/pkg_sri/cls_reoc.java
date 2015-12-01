/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_sri;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.text.SimpleDateFormat;
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
public class cls_reoc {

    private Document doc_reoc;
    private String path = "";
    private String nombre = "";
    private String fecha_inicio = "";
    private String fecha_fin = "";
    private Utilitario utilitario = new Utilitario();
    private boolean validarComprobantesRetención = false;
    private String opcionAnexo = "1";

    public String Reoc(String anio, String mes) {
        String mensaje = "";

        fecha_inicio = utilitario.getFormatoFecha(anio + "-" + mes + "-01");
        fecha_fin = utilitario.getUltimaFechaMes(fecha_inicio);

        try {
            TablaGenerica tab_empresa = utilitario.consultar("SELECT identificacion_empr,nom_empr from sis_empresa where ide_empr=" + utilitario.getVariable("ide_empr"));
            if (tab_empresa.getTotalFilas() > 0) {
                doc_reoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                doc_reoc.setXmlVersion("1.0");
                doc_reoc.setXmlStandalone(true);

                Element raiz = doc_reoc.createElement("reoc");
                raiz.appendChild(crearElemento("numeroRuc", null, tab_empresa.getValor("identificacion_empr")));
                raiz.appendChild(crearElemento("anio", null, anio));
                raiz.appendChild(crearElemento("mes", null, mes));
                doc_reoc.appendChild(raiz);

                //COMPRAS
                Element compras = doc_reoc.createElement("compras");
                raiz.appendChild(compras);
                ////////////////////BUSCAR TODAS LAS COMPRAS ESTO ES EN UN FOR
                TablaGenerica tab_compras = utilitario.consultar("Select cabece.ide_cncre,iden.alterno1_getid,prove.identificac_geper,docu.alter_tribu_cntdo,  "
                        + " cabece.fecha_trans_cpcfa,cabece.numero_cpcfa,cabece.fecha_emisi_cpcfa,cabece.autorizacio_cpcfa,valor_ice_cpcfa, "
                        + " cabece.base_grabada_cpcfa,cabece.base_tarifa0_cpcfa,cabece.base_no_objeto_iva_cpcfa,cabece.valor_iva_cpcfa, "
                        + " rete.numero_cncre,rete.autorizacion_cncre,rete.fecha_emisi_cncre "
                        + " from cxp_cabece_factur cabece "
                        + " left join gen_persona prove on cabece.ide_geper= prove.ide_geper "
                        + " left join gen_tipo_identifi iden on prove.ide_getid=iden.ide_getid "
                        + " left join con_tipo_document docu on cabece.ide_cntdo=docu.ide_cntdo "
                        + " left join con_cabece_retenc rete on cabece.ide_cncre=rete.ide_cncre "
                        + " where  cabece.fecha_emisi_cpcfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'");
                for (int i = 0; i < tab_compras.getTotalFilas(); i++) {

                    Element detalleCompras = doc_reoc.createElement("detalleCompras");
                    compras.appendChild(detalleCompras);
                    detalleCompras.appendChild(crearElemento("tpIdProv", null, tab_compras.getValor(i, "alterno1_getid")));
                    detalleCompras.appendChild(crearElemento("idProv", null, tab_compras.getValor(i, "identificac_geper")));
                    detalleCompras.appendChild(crearElemento("tipoComp", null, tab_compras.getValor(i, "alter_tribu_cntdo")));
                    detalleCompras.appendChild(crearElemento("aut", null, tab_compras.getValor(i, "autorizacio_cpcfa")));
                    String numero = tab_compras.getValor(i, "numero_cpcfa");
                    detalleCompras.appendChild(crearElemento("estab", null, numero.substring(0, 3)));
                    detalleCompras.appendChild(crearElemento("ptoEmi", null, numero.substring(3, 6)));
                    detalleCompras.appendChild(crearElemento("sec", null, numero.substring(6, numero.length())));
                    detalleCompras.appendChild(crearElemento("fechaEmiCom", null, getFormatoFecha(tab_compras.getValor(i, "fecha_emisi_cpcfa"))));

                    Element air = doc_reoc.createElement("air");
                    detalleCompras.appendChild(air);
                    String ide_retencion = tab_compras.getValor(i, "ide_cncre");
                    if (ide_retencion == null) {
                        ide_retencion = "-1";
                    }

                    TablaGenerica tab_retencion = utilitario.consultar("SELECT impuesto.casillero_cncim,base_cndre,porcentaje_cndre,valor_cndre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
                            + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
                            + "where impuesto.ide_cnimp=" + utilitario.getVariable("p_con_impuesto_renta") + " and cabece.ide_cncre=" + ide_retencion);
                    if (tab_retencion.getTotalFilas() > 0) {
                        for (int j = 0; j < tab_retencion.getTotalFilas(); j++) {
                            Element detalleAir = doc_reoc.createElement("detalleAir");
                            air.appendChild(detalleAir);
                            detalleAir.appendChild(crearElemento("codRetAir", null, tab_retencion.getValor(j, "casillero_cncim")));
                            detalleAir.appendChild(crearElemento("porcentaje", null, utilitario.getFormatoNumero(tab_retencion.getValor(j, "porcentaje_cndre"), 1)));
                            detalleAir.appendChild(crearElemento("base0", null, tab_compras.getValor(i, "base_tarifa0_cpcfa")));
                            detalleAir.appendChild(crearElemento("baseGrav", null, tab_compras.getValor(i, "base_grabada_cpcfa")));
                            detalleAir.appendChild(crearElemento("baseNoGrav", null, tab_compras.getValor(i, "base_no_objeto_iva_cpcfa")));
                            detalleAir.appendChild(crearElemento("valRetAir", null, tab_retencion.getValor(j, "valor_cndre")));
                        }
                        String numero_retencion = tab_compras.getValor(i, "numero_cncre");

                        detalleCompras.appendChild(crearElemento("autRet", null, tab_compras.getValor(i, "autorizacion_cncre")));
                        detalleCompras.appendChild(crearElemento("estabRet", null, numero_retencion.substring(0, 3)));
                        detalleCompras.appendChild(crearElemento("ptoEmiRet", null, numero_retencion.substring(3, 6)));
                        detalleCompras.appendChild(crearElemento("secRet", null, Integer.parseInt(numero_retencion.substring(6, numero_retencion.length())) + ""));
                        detalleCompras.appendChild(crearElemento("fechaEmiRet", null, getFormatoFecha(tab_compras.getValor(i, "fecha_emisi_cncre"))));

                    } else {
                        //si no hay retención
                        Element detalleAir = doc_reoc.createElement("detalleAir");
                        air.appendChild(detalleAir);
                        detalleAir.appendChild(crearElemento("codRetAir", null, "332"));//OTRAS COMPRAS Y SERVICIOS NO SUJETAS A RETENCIÓN.
                        detalleAir.appendChild(crearElemento("porcentaje", null, "0.0"));
                        detalleAir.appendChild(crearElemento("base0", null, tab_compras.getValor(i, "base_tarifa0_cpcfa")));
                        detalleAir.appendChild(crearElemento("baseGrav", null, tab_compras.getValor(i, "base_grabada_cpcfa")));
                        detalleAir.appendChild(crearElemento("baseNoGrav", null, tab_compras.getValor(i, "base_no_objeto_iva_cpcfa")));
                        detalleAir.appendChild(crearElemento("valRetAir", null, "0.00"));
//                        detalleCompras.appendChild(crearElemento("autRet", null, "0000"));
//                        detalleCompras.appendChild(crearElemento("estabRet", null, "0000"));
//                        detalleCompras.appendChild(crearElemento("ptoEmiRet", null, "000"));
//                        detalleCompras.appendChild(crearElemento("secRet", null, "0000000"));
//                        detalleCompras.appendChild(crearElemento("fechaEmiRet", null, "xxx"));

                    }

                }
                ///ESCRIBE EL DOCUMENTO
                Source source = new DOMSource(doc_reoc);
                String master = System.getProperty("user.dir");
                nombre = "REOC" + mes + anio + ".xml";
                Result result = new StreamResult(new java.io.File(master + "/" + nombre)); //nombre del archivo
                path = master + "/" + nombre;
                Result console = new StreamResult(System.out);
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(source, result);
                transformer.transform(source, console);
            } else {
                mensaje = "Debe ingresar la información de la empresa";
            }

        } catch (Exception e) {
            utilitario.agregarMensajeError("No se pudo generar el Anexo", "No hay información para generar el anexo");
        }

        return mensaje;
    }

    private Element crearElemento(String nombre, String[] atributos, String texto) {
        Element elemento = doc_reoc.createElement(nombre);
        if (atributos != null) {
            for (int i = 0; i < atributos.length; i++) {
                elemento.setAttribute(atributos[0], atributos[1]);
            }
        }
        if (texto == null) {
            texto = "";
        }
        elemento.appendChild(doc_reoc.createTextNode(texto));
        return elemento;
    }

    private Element crearElementoCDATA(String nombre, String[] atributos, String texto) {
        Element elemento = doc_reoc.createElement(nombre);
        if (atributos != null) {
            for (int i = 0; i < atributos.length; i++) {
                elemento.setAttribute(atributos[0], atributos[1]);
            }
        }
        elemento.appendChild(doc_reoc.createCDATASection(texto));
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

    public boolean isValidarComprobantesRetención() {
        return validarComprobantesRetención;
    }

    public void setValidarComprobantesRetención(boolean validarComprobantesRetención) {
        this.validarComprobantesRetención = validarComprobantesRetención;
    }

    public String getOpcionAnexo() {
        return opcionAnexo;
    }

    public void setOpcionAnexo(String opcionAnexo) {
        this.opcionAnexo = opcionAnexo;
    }

    public String getFormatoFecha(String fecha) {
        SimpleDateFormat formatoFecha1 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatoFecha1.format(utilitario.getFecha(fecha));
        } catch (Exception e) {
        }
        return (String) fecha;
    }
}
