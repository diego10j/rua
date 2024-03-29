/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_sri;

import framework.aplicacion.TablaGenerica;
import java.util.List;
import javax.ejb.EJB;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import servicios.contabilidad.ServicioConfiguracion;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Diego
 */
public class cls_formulario104 {

    private Document doc_formulario104;
    private String path = "";
    private String nombre = "";
    private String[] nom_mes = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
    private String tipoDeclaracion = "O";
    private String numSustituye = "";
    private Utilitario utilitario = new Utilitario();
    private String fecha_inicio = "";
    private String fecha_fin = "";
    private String porcentaje_iva = "";
    private String v401, v402, v403, v404, v405, v406, v407, v408, v409, v411, v412, v413, v414, v415, v416, v417, v418, v419, v421, v422, v423, v429, v431, v480, v481, v482, v483, v484, v485, v499;
    private String v501, v502, v503, v504, v505, v506, v507, v508, v509, v510, v511, v512, v513, v514, v515, v516, v517, v518, v519, v521, v522, v523, v524, v525, v529, v531, v532, v533, v534, v535, v544, v545, v554, v563, v543, v564, v526, v527, v520;
    private String v601, v602, v605, v607, v609, v611, v613, v615, v617, v620, v621, v699;
    private String v721, v723, v725, v799, v859, v801, v731, v729;
    private String v890, v897, v898, v899, v880;
    private String v902, v903, v904, v999, v905, v906, v907, v908, v909, v910, v911, v912, v913, v915, v916, v917, v918, v919;
    @EJB
    private final ServicioConfiguracion ser_configuracion = (ServicioConfiguracion) utilitario.instanciarEJB(ServicioConfiguracion.class);

    public String Formulario104(String anio, String mes) {
        try {
            fecha_inicio = utilitario.getFormatoFecha(anio + "-" + mes + "-01");
            fecha_fin = utilitario.getUltimaFechaMes(fecha_inicio);

//            utilitario.getConexion().ejecutarSql("update cxp_detall_factur set\n"
//                    + "alter_tribu_cpdfa='503', usuario_actua='sa'\n"
//                    + "where ide_cpdfa in\n"
//                    + "(\n"
//                    + "select ide_cpdfa from cxp_detall_factur a\n"
//                    + "inner join inv_articulo b on a.ide_inarti=b.ide_inarti\n"
//                    + "where alter_tribu_cpdfa='00'\n"
//                    + "and iva_inarti_cpdfa=1  --IVA SI\n"
//                    + ")");
//
//            utilitario.getConexion().ejecutarSql("update cxp_detall_factur set\n"
//                    + "alter_tribu_cpdfa='507', usuario_actua='sa'\n"
//                    + "where ide_cpdfa in\n"
//                    + "(\n"
//                    + "select ide_cpdfa from cxp_detall_factur a\n"
//                    + "inner join inv_articulo b on a.ide_inarti=b.ide_inarti\n"
//                    + "where alter_tribu_cpdfa='00'\n"
//                    + "and iva_inarti_cpdfa=-1  --IVA NO\n"
//                    + ")");
            TablaGenerica tab_empresa = utilitario.consultar("SELECT identificacion_empr,nom_empr,identi_repre_empr from sis_empresa where ide_empr=" + utilitario.getVariable("ide_empr"));

            porcentaje_iva = ser_configuracion.getPorcentajeIva(fecha_fin) + "";
            if (tab_empresa.getTotalFilas() == 1) {
                doc_formulario104 = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                doc_formulario104.setXmlVersion("1.0");
                doc_formulario104.setXmlStandalone(true);

                Element raiz = doc_formulario104.createElement("formulario");
                raiz.setAttribute("version", "2.0");
                doc_formulario104.appendChild(raiz);

                //CABECERA
                Element cabecera = doc_formulario104.createElement("cabecera");
                raiz.appendChild(cabecera);
                cabecera.appendChild(crearElemento("codigo_version_formulario", null, "04201701"));
                cabecera.appendChild(crearElemento("ruc", null, tab_empresa.getValor("identificacion_empr")));
                cabecera.appendChild(crearElemento("codigo_moneda", null, "1"));

                //DETALLE
                Element detalle = doc_formulario104.createElement("detalle");
                raiz.appendChild(detalle);

                /**
                 * VENTAS
                 */
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "102"}, anio)); // AÑO
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "101"}, mes)); // MES
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "31"}, getTipoDeclaracion())); // O ORIGINAL S SUTITUTIVA
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "104"}, getNumSustituye())); //NUMERO DE FORMULARIO QUE LO SUSTITUYE
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "202"}, tab_empresa.getValor("nom_empr")));
//detalle.appendChild(crearElementoCDATA("campo", new String[]{"numero", "202"}, tab_empresa.getValor("nom_empr")));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "201"}, tab_empresa.getValor("identificacion_empr")));

                //Consulta las ventas por el campo alterno
                v401 = getValor401(); //
                ////v402 = consultarAlternoVentas("402");
                v402 = "0.00";
                v403 = getValor403();
                v404 = consultarAlternoVentas("404");
                v405 = consultarAlternoVentas("405");
                v406 = consultarAlternoVentas("406");
                v407 = consultarAlternoVentas("407");
                v408 = consultarAlternoVentas("408");
                //v409 = consultarAlternoVentas("401,402,403,404,405,406,407");
                v409 = utilitario.getFormatoNumero(Double.parseDouble(v401) + Double.parseDouble(v403));
                v431 = getValor431();
                //Valor Bruto  -N/C  => valores brutos restados los descuentos y devoluciones que afecten a las ventas del período declarado
                v411 = getValor411();
                v412 = v402;
                v413 = getValor413();
                v414 = v404;
                v415 = v405;
                v416 = v406;
                v417 = v407;
                v418 = v408;
                v419 = utilitario.getFormatoNumero(Double.parseDouble(v411) + Double.parseDouble(v412) + Double.parseDouble(v413) + Double.parseDouble(v414) + Double.parseDouble(v415) + Double.parseDouble(v416) + Double.parseDouble(v417) + Double.parseDouble(v418));
                //Calculo del IVA
                v421 = getValor421();
                //****** v421 = utilitario.getFormatoNumero(Double.parseDouble(v411) * Double.parseDouble(porcentaje_iva));
                v422 = utilitario.getFormatoNumero(Double.parseDouble(v412) * Double.parseDouble(porcentaje_iva));
                v429 = utilitario.getFormatoNumero((Double.parseDouble(v421) + Double.parseDouble(v422)));

                v423 = "0.00"; //**************!!!!BUSCAR
                v480 = v401;
                v481 = "0.00"; //**************!!!!BUSCAR
                v482 = v429;
                v483 = consultarImpuestoMesAnterior();
                v484 = utilitario.getFormatoNumero(Double.parseDouble(v480) * Double.parseDouble(porcentaje_iva));
                v485 = utilitario.getFormatoNumero(Double.parseDouble(v482) - Double.parseDouble(v484));
                v499 = utilitario.getFormatoNumero(Double.parseDouble(v483) + Double.parseDouble(v484));

                detalle.appendChild(crearElemento("campo", new String[]{"numero", "411"}, v411));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "401"}, v401));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "421"}, v421));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "422"}, v422));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "412"}, v412));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "402"}, v402));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "423"}, v423));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "413"}, v413));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "403"}, v403));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "404"}, v404));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "414"}, v414));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "405"}, v405));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "415"}, v415));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "406"}, v406));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "416"}, v416));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "417"}, v417));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "407"}, v407));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "418"}, v418));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "408"}, v408));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "419"}, v419));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "409"}, v409));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "429"}, v429));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "431"}, v431));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "441"}, "0.00"));//!!!!!****** BUSCAR
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "442"}, "0.00"));//!!!!!****** BUSCAR
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "443"}, "0.00"));//Ingresos por rembolsos
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "453"}, "0.00"));//!!!!!****** BUSCAR
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "434"}, "0.00"));//IVA Notas de crédito tarífa 12% 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "444"}, "0.00"));//IVA Ingresos por rembolsos
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "454"}, "0.00"));//!!!!!****** BUSCAR
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "499"}, v499));//483+484
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "485"}, v485));//482 - 484
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "484"}, v484)); //IVA Ventas 12% contado
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "483"}, v483));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "482"}, v482)); //= 429 TOTAL IVA
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "481"}, v481));//Ventas 12% crédito
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "480"}, v480));//Ventas 12% contado 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "510"}, v510));//!!!!!****** BUSCAR 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "500"}, getValor500()));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "520"}, v520));//!!!!!****** BUSCAR 
                /**
                 * COMPRAS
                 */
                v501 = getValor501();
                v502 = getValor502();
                v503 = "0.00";
                v504 = "0.00";
                v505 = "0.00";
                v506 = "0.00";
                v507 = getValor507();
                v508 = getValor508();
                v509 = utilitario.getFormatoNumero((Double.parseDouble(v501) + Double.parseDouble(v502) + Double.parseDouble(v503) + Double.parseDouble(v504) + Double.parseDouble(v505) + Double.parseDouble(v506) + Double.parseDouble(v507)) + Double.parseDouble(v508));
                //v509 = consultarAlternoCompras("501,502,503,504,505,506,507,508");
                v531 = getValor531();
                v532 = consultarAlternoCompras("532");
                //Valor Bruto  -N/C  => valores brutos restados los descuentos y devoluciones que afecten a las ventas del período declarado
                v510 = getValor510();
                v511 = getValor511();
                v512 = getValor512();
                v513 = v503;
                v514 = v504;
                v515 = v505;
                v516 = v506;
                v517 = getValor517();
                v518 = getValor518();
                v519 = utilitario.getFormatoNumero(Double.parseDouble(v511) + Double.parseDouble(v512) + Double.parseDouble(v513) + Double.parseDouble(v514) + Double.parseDouble(v515) + Double.parseDouble(v516) + Double.parseDouble(v517) + Double.parseDouble(v518));
                //Calculo del IVA
                v520 = getValor520();
                v521 = getValor521();
                v522 = getValor522();
                v523 = utilitario.getFormatoNumero(Double.parseDouble(v513) * Double.parseDouble(porcentaje_iva));
                v524 = utilitario.getFormatoNumero(Double.parseDouble(v514) * Double.parseDouble(porcentaje_iva));
                v525 = utilitario.getFormatoNumero(Double.parseDouble(v515) * Double.parseDouble(porcentaje_iva));
                v529 = utilitario.getFormatoNumero((Double.parseDouble(v521) + Double.parseDouble(v522) + Double.parseDouble(v523) + Double.parseDouble(v524) + Double.parseDouble(v525)));
                if (Double.parseDouble(v419) > 0) {
                    v563 = utilitario.getFormatoNumero((Double.parseDouble(v411) + Double.parseDouble(v412) + Double.parseDouble(v415) + Double.parseDouble(v416) + Double.parseDouble(v417) + Double.parseDouble(v418)) / Double.parseDouble(v419), 4);
                } else {
                    v563 = "0.00";
                }

                v543 = getValor543();
                v544 = getValor544();
                v554 = getValor554();

                //////v564 = utilitario.getFormatoNumero((Double.parseDouble(v521) + Double.parseDouble(v522) + Double.parseDouble(v524) + Double.parseDouble(v525)) * Double.parseDouble(v553));
                //////v554 = "0.00";
                v526 = "0.00";
                v527 = "0.00";

                v564 = utilitario.getFormatoNumero((Double.parseDouble(v520) + Double.parseDouble(v521) + Double.parseDouble(v523) + Double.parseDouble(v524) + Double.parseDouble(v525) + Double.parseDouble(v526) + Double.parseDouble(v527)) * Double.parseDouble(v563));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "511"}, v511));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "521"}, v521));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "501"}, v501));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "512"}, v512));  //nota credito este mes IVA
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "502"}, v502));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "522"}, v522));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "523"}, v523));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "503"}, v503));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "513"}, v513));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "514"}, v514));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "504"}, v504));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "524"}, v524));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "525"}, v525));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "505"}, v505));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "515"}, v515));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "526"}, "0.00"));//!!!!!****** BUSCAR 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "506"}, v506));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "516"}, v516));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "517"}, v517));   //nota credito este mes IVA 0
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "507"}, v507));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "508"}, v508));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "518"}, v518));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "529"}, v529));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "519"}, v519));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "509"}, v509));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "541"}, getValor541()));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "531"}, v531));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "532"}, v532));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "542"}, "0.00"));//!!!!!****** BUSCAR 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "543"}, v543));//!!!!!****** notas de credito 0% 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "544"}, v544));//!!!!!****** notas de credito 12% 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "554"}, v554));//!!!!!****** iva notas de credito 12% 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "555"}, "0.00"));//!!!!!****** BUSCAR 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "545"}, "0.00"));//IVA REMBOLSOS
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "535"}, "0.00"));//REMBOLSOS
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "563"}, v563));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "564"}, v564));//!!!!!****** BUSCAR 
                //RESUMEN 
                v601 = utilitario.getFormatoNumero((Double.parseDouble(v499) - Double.parseDouble(v564)));
                v602 = v601;
                if (Double.parseDouble(v601) > 0.00) {
                    v602 = "0.00";
                } else {
                    v601 = "0.00";
                }
                v605 = "0.00";
                v607 = "0.00";
                v609 = consultarRetencionVentasIVA();
                v611 = getValor611();
                v613 = "0.00";
                v615 = "0.00";
                v617 = "0.00";
                v620 = utilitario.getFormatoNumero(Double.parseDouble(v601) - Double.parseDouble(v602) - Double.parseDouble(v605) - Double.parseDouble(v607) - Double.parseDouble(v609) + Double.parseDouble(v611) + Double.parseDouble(v613));
                if (Double.parseDouble(v620) < 0) {
                    v620 = "0.00";
                }
                v621 = "0.00";
                v699 = utilitario.getFormatoNumero(Double.parseDouble(v620) + Double.parseDouble(v621));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "601"}, v601));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "602"}, v602));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "603"}, "0.00"));//!!!!!****** BUSCAR 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "604"}, "0.00"));//!!!!!****** BUSCAR 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "605"}, v605));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "606"}, "0.00"));//!!!!!****** BUSCAR 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "607"}, v607));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "608"}, "0.00"));//!!!!!****** BUSCAR 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "609"}, v609));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "610"}, "0.00"));//!!!!!****** BUSCAR 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "611"}, v611));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "612"}, "0.00"));//!!!!!****** BUSCAR                 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "613"}, v613));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "614"}, "0.00"));//!!!!!****** BUSCAR 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "615"}, v615));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "617"}, v617));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "618"}, "0.00"));//!!!!!****** BUSCAR 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "619"}, "0.00"));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "620"}, v620));//!!!!!****** BUSCAR 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "621"}, v621));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "699"}, v699));
                //AGENTE DE RETENCIÓN DE IVA
                v721 = consultarRetencionIVA("721");
                v723 = consultarRetencionIVA("723");
                v725 = consultarRetencionIVA("725");
                v729 = consultarRetencionIVA("729");
                v731 = consultarRetencionIVA("731");
                v799 = consultarRetencionIVA("721,723,725,729,731");
                v801 = v799;
                v859 = utilitario.getFormatoNumero((Double.parseDouble(v699) + Double.parseDouble(v801)));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "721"}, v721));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "723"}, v723));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "725"}, v725));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "729"}, v729));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "731"}, v731));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "727"}, "0.00"));//!!!!!****** BUSCAR 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "799"}, v799));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "800"}, "0.00"));//!!!!!****** BUSCAR
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "801"}, v801));//!!!!!****** BUSCAR
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "859"}, v859));
                //INTERESES POR SUSTITUCIÓN DE FORMULARIO
                v890 = "0.00";
                v897 = "0.00";
                v898 = "0.00";
                v899 = "0.00";
                v880 = "0.00";
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "890"}, v890));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "897"}, v897));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "898"}, v898));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "899"}, v899));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "880"}, v880));
                //VALORES A PAGAR Y FORMA DE PAGO
                v902 = utilitario.getFormatoNumero((Double.parseDouble(v859) - Double.parseDouble(v898)));
                v903 = "0.00";
                v904 = "0.00";
                v999 = utilitario.getFormatoNumero((Double.parseDouble(v902) + Double.parseDouble(v903) + Double.parseDouble(v904)));
                v905 = v999;
                v906 = "0.00";
                v907 = "0.00";
                v908 = "";
                v909 = "0.00";
                v910 = "";
                v911 = "0.00";
                v912 = "";
                v913 = "0.00";
                v915 = "0.00";
                v916 = "";
                v917 = "0.00";
                v918 = "";
                v919 = "0.00";
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "902"}, v859));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "903"}, v903));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "904"}, v904));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "999"}, v999));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "905"}, v905));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "906"}, v906));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "907"}, v907));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "925"}, "0.00"));//!!!!!****** BUSCAR 
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "912"}, v912));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "910"}, v910));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "908"}, v908));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "915"}, v915));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "913"}, v913));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "911"}, v911));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "909"}, v909));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "916"}, v916));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "918"}, v918));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "919"}, v919));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "917"}, v917));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "920"}, "0.00"));//!!!!!****** BUSCAR
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "198"}, tab_empresa.getValor("identi_repre_empr")));//IDENTIDFICACION REPRESENTANTE
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "199"}, "0200749620001")); //RUC CONTADOR
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "922"}, "16")); //BANCO  === RED BANCARIA

                //  detalle.appendChild(crearElemento("campo", new String[]{"numero", "432"}, "0.00")); //Notas de crédito tarífa 0% 
                // detalle.appendChild(crearElemento("campo", new String[]{"numero", "433"}, "0.00"));//Notas de crédito tarífa 12% 
                //detalle.appendChild(crearElemento("campo", new String[]{"numero", "533"}, "0.00"));//NOTAS DE CREDITO 0%
                // detalle.appendChild(crearElemento("campo", new String[]{"numero", "534"}, "0.00"));//NOTAS DE CREDITO 12%
                // detalle.appendChild(crearElemento("campo", new String[]{"numero", "553"}, v553));
                ///ESCRIBE EL DOCUMENTO
                Source source = new DOMSource(doc_formulario104);
                //System.out.println(source + ".. " + doc_formulario104 + " --- " + doc_formulario104.getTextContent());
                //String master = "D:";
                String master = System.getProperty("user.dir");
                nombre = "04ORI_" + nom_mes[Integer.parseInt(mes) - 1] + anio + ".xml";
                Result result = new StreamResult(new java.io.File(master + "/" + nombre)); //nombre del archivo
                path = master + "/" + nombre;
                Result console = new StreamResult(System.out);
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(source, result);
                transformer.transform(source, console);
                return utilitario.getStringFromDocument(doc_formulario104);
            }
        } catch (Exception e) {
            System.err.println("Error al generar el Formulario 104: " + e.getMessage());
            utilitario.agregarMensajeError("No se pudo generar el Formulario", "No hay información para generar el formulario");
            e.printStackTrace();
        }
        return null;
    }

    private String consultarAlternoCompras(String alterno) {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("SELECT SUM(df.valor_cpdfa) as valor "
                + " FROM cxp_cabece_factur f "
                + " LEFT JOIN cxp_detall_factur df ON (df.ide_cpcfa = f.ide_cpcfa) "
                + " WHERE df.alter_tribu_cpdfa in(" + utilitario.generarComillaSimple(alterno) + ")"
                + " AND f.fecha_emisi_cpcfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' and ide_rem_cpcfa is null and ide_cpefa=0  and ide_cntdo !=0");  //filtra no anuladas
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    private String consultarAlternoVentas(String alterno) {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("SELECT SUM(df.total_ccdfa ) as valor "
                + " FROM cxc_cabece_factura cf "
                + " LEFT JOIN cxc_deta_factura df ON (df.ide_cccfa = cf.ide_cccfa) "
                + " WHERE df.alterno_ccdfa in(" + utilitario.generarComillaSimple(alterno) + ")"
                + " and  cf.ide_ccefa !=1" //no anuladas
                + " AND cf.fecha_trans_cccfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' ");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    private String getValor611() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(monto_com_cpcfa) from cxp_cabece_factur\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null AND liquida_nota_cpcfa=0 \n"
                + "and fecha_emisi_cpcfa BETWEEN  '" + fecha_inicio + "' AND '" + fecha_fin + "' ");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    //ADQUISICIONES Y PAGOS (EXCLUYE ACTIVOS FIJOS) TARIFA DIFERENTE DE CERO CON DERECHO A CREDITO TRIBUTARIO
    private String getValor500() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(base_grabada_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null \n"
                + "and ide_cntdo not in (0,5)  " //NO NOTAS DE CREDITO NI NOTAS DE VENTA
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n"
                + "and ide_srtst = 2"); //  CRÉDITO TRIBUTARIO PARA DECLARACIÓN DE IVA (SERVICIOS Y BIENES DISTINTOS DE INVENTARIOS Y ACTIVOS FIJOS )
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    //ADQUISICIONES Y PAGOS (EXCLUYE ACTIVOS FIJOS) TARIFA DIFERENTE DE CERO CON DERECHO A CREDITO TRIBUTARIO - NOTAS DE CREDITO 
    private String getValor510() {
        double dou_valor = Double.parseDouble(getValor500());
        double dou_notas = 0;
        List lis_sql1 = utilitario.getConexion().consultar("select sum(base_grabada_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null  AND liquida_nota_cpcfa=0\n"
                + "and ide_cntco !=5 and  ide_cntdo= 0 " //SOLO NOTAS DE CREDITO
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n"
                + "and ide_srtst = 2"); //  CRÉDITO TRIBUTARIO PARA DECLARACIÓN DE IVA (SERVICIOS Y BIENES DISTINTOS DE INVENTARIOS Y ACTIVOS FIJOS )
        if (lis_sql1 != null && !lis_sql1.isEmpty()) {
            try {
                dou_notas = Double.parseDouble(lis_sql1.get(0) + "");
            } catch (Exception e) {
            }
        }
        dou_valor = dou_valor - dou_notas;
        return utilitario.getFormatoNumero(dou_valor);
    }

    //IVA ADQUISICIONES Y PAGOS (EXCLUYE ACTIVOS FIJOS) TARIFA DIFERENTE DE CERO CON DERECHO A CREDITO TRIBUTARIO
    private String getValor520() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(valor_iva_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null \n"
                + "and ide_cntdo not in (0,5)  " //NO NOTAS DE CREDITO NI NOTAS DE VENTA
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n"
                + "and ide_srtst = 2"); //  CRÉDITO TRIBUTARIO PARA DECLARACIÓN DE IVA (SERVICIOS Y BIENES DISTINTOS DE INVENTARIOS Y ACTIVOS FIJOS )
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }

        double dou_notas = 0;
        List lis_sql1 = utilitario.getConexion().consultar("select sum(valor_iva_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null  AND liquida_nota_cpcfa=0\n"
                + "and ide_cntco !=5 and  ide_cntdo= 0 " //SOLO NOTAS DE CREDITO
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n"
                + "and ide_srtst = 2"); //  CRÉDITO TRIBUTARIO PARA DECLARACIÓN DE IVA (SERVICIOS Y BIENES DISTINTOS DE INVENTARIOS Y ACTIVOS FIJOS )
        if (lis_sql1 != null && !lis_sql1.isEmpty()) {
            try {
                dou_notas = Double.parseDouble(lis_sql1.get(0) + "");
            } catch (Exception e) {
            }
        }
        dou_valor = dou_valor - dou_notas;

        return utilitario.getFormatoNumero(dou_valor);
    }

    //ADQUISICIONES LOCALES DE ACTIVOS FIJOS GRAVADOS TARIFA DIFERENTE DE CERO CON DERECHO A CREDITO TRIBUTARIO
    private String getValor501() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(base_grabada_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null \n"
                + "and ide_cntdo not in (0,5)  " //NO NOTAS DE CREDITO NI NOTAS DE VENTA
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n"
                + "and ide_srtst = 0"); //  ACTIVO FIJO - CRÉDITO TRIBUTARIO PARA DECLARACIÓN DE IVA
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    //ADQUISICIONES LOCALES DE ACTIVOS FIJOS GRAVADOS TARIFA DIFERENTE DE CERO CON DERECHO A CREDITO TRIBUTARIO MENOS NOTAS DE CREDITO
    private String getValor511() {
        double dou_valor = Double.parseDouble(getValor501());

        double dou_notas = 0;
        List lis_sql1 = utilitario.getConexion().consultar("select sum(base_grabada_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null  AND liquida_nota_cpcfa=0\n"
                + "and ide_cntco !=5 and  ide_cntdo= 0 " //SOLO NOTAS DE CREDITO
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n"
                + "and ide_srtst = 0"); //  ACTIVO FIJO - CRÉDITO TRIBUTARIO PARA DECLARACIÓN DE IVA
        if (lis_sql1 != null && !lis_sql1.isEmpty()) {
            try {
                dou_notas = Double.parseDouble(lis_sql1.get(0) + "");
            } catch (Exception e) {
            }
        }
        dou_valor = dou_valor - dou_notas;
        return utilitario.getFormatoNumero(dou_valor);
    }

    //IVA ADQUISICIONES LOCALES DE ACTIVOS FIJOS GRAVADOS TARIFA DIFERENTE DE CERO CON DERECHO A CREDITO TRIBUTARIO
    private String getValor521() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(valor_iva_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null  \n"
                + "and ide_cntdo not in (0,5)  " //NO NOTAS DE CREDITO NI NOTAS DE VENTA
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n"
                + "and ide_srtst = 0"); //  ACTIVO FIJO - CRÉDITO TRIBUTARIO PARA DECLARACIÓN DE IVA
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        double dou_notas = 0;
        List lis_sql1 = utilitario.getConexion().consultar("select sum(valor_iva_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null  AND liquida_nota_cpcfa=0\n"
                + "and ide_cntco !=5 and  ide_cntdo= 0 " //SOLO NOTAS DE CREDITO
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n"
                + "and ide_srtst = 0"); //  ACTIVO FIJO - CRÉDITO TRIBUTARIO PARA DECLARACIÓN DE IVA
        if (lis_sql1 != null && !lis_sql1.isEmpty()) {
            try {
                dou_notas = Double.parseDouble(lis_sql1.get(0) + "");
            } catch (Exception e) {
            }
        }
        dou_valor = dou_valor - dou_notas;

        return utilitario.getFormatoNumero(dou_valor);
    }

    //OTRAS ADQUISICIONES LOCALES DE ACTIVOS FIJOS GRAVADOS TARIFA DIFERENTE DE SIN  DERECHO A CREDITO TRIBUTARIO
    private String getValor502() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(base_grabada_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null  \n"
                + "and ide_cntdo not in (0,5)  " //NO NOTAS DE CREDITO NI NOTAS DE VENTA
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n"
                + "and ide_srtst in (1,4) "); //  COSTO O GASTO PARA DECLARACIÓN DE IMP. A LA RENTA (SERVICIOS Y BIENES DISTINTOS DE INVENTARIOS Y ACTIVOS FIJOS )

        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    //OTRAS ADQUISICIONES LOCALES DE ACTIVOS FIJOS GRAVADOS TARIFA DIFERENTE DE SIN  DERECHO A CREDITO TRIBUTARIO - notas de credito
    private String getValor512() {
        double dou_valor = Double.parseDouble(getValor502());
        double dou_notas = 0;
        List lis_sql1 = utilitario.getConexion().consultar("select sum(base_grabada_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null  AND liquida_nota_cpcfa=0\n"
                + "and ide_cntco !=5 and  ide_cntdo= 0 " //SOLO NOTAS DE CREDITO
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n"
                + "and ide_srtst = 1"); //  COSTO O GASTO PARA DECLARACIÓN DE IMP. A LA RENTA (SERVICIOS Y BIENES DISTINTOS DE INVENTARIOS Y ACTIVOS FIJOS )
        if (lis_sql1 != null && !lis_sql1.isEmpty()) {
            try {
                dou_notas = Double.parseDouble(lis_sql1.get(0) + "");
            } catch (Exception e) {
            }
        }
        dou_valor = dou_valor - dou_notas;
        return utilitario.getFormatoNumero(dou_valor);
    }

    //OTRAS ADQUISICIONES LOCALES DE ACTIVOS FIJOS GRAVADOS TARIFA DIFERENTE DE SIN  DERECHO A CREDITO TRIBUTARIO
    private String getValor522() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(valor_iva_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null \n"
                + "and ide_cntdo not in (0,5)  " //NO NOTAS DE CREDITO NI NOTAS DE VENTA
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n"
                + "and ide_srtst = 1"); //  COSTO O GASTO PARA DECLARACIÓN DE IMP. A LA RENTA (SERVICIOS Y BIENES DISTINTOS DE INVENTARIOS Y ACTIVOS FIJOS )

        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }

        double dou_notas = 0;
        List lis_sql1 = utilitario.getConexion().consultar("select sum(valor_iva_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null  AND liquida_nota_cpcfa=0\n"
                + "and ide_cntco !=5 and  ide_cntdo= 0 " //SOLO NOTAS DE CREDITO
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n"
                + "and ide_srtst = 1"); //  COSTO O GASTO PARA DECLARACIÓN DE IMP. A LA RENTA (SERVICIOS Y BIENES DISTINTOS DE INVENTARIOS Y ACTIVOS FIJOS )
        if (lis_sql1 != null && !lis_sql1.isEmpty()) {
            try {
                dou_notas = Double.parseDouble(lis_sql1.get(0) + "");
            } catch (Exception e) {
            }
        }
        dou_valor = dou_valor - dou_notas;

        return utilitario.getFormatoNumero(dou_valor);
    }

    //ADQUISICIONES Y PAGOS (INCLUYE ACTIVOS FIJOS) TARIFA 0%
    private String getValor507() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(base_tarifa0_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null  \n"
                + "and ide_cntdo not in (0,5)  " //NO NOTAS DE CREDITO NI NOTAS DE VENTA
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n"); //  CRÉDITO TRIBUTARIO PARA DECLARACIÓN DE IVA (SERVICIOS Y BIENES DISTINTOS DE INVENTARIOS Y ACTIVOS FIJOS )
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    //ADQUISICIONES Y PAGOS (INCLUYE ACTIVOS FIJOS) TARIFA 0%  - NOTAS DE CREDITO
    private String getValor517() {
        double dou_valor = Double.parseDouble(getValor507());
        double dou_notas = 0;
        List lis_sql1 = utilitario.getConexion().consultar("select sum(base_tarifa0_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null  AND liquida_nota_cpcfa=0\n"
                + "and ide_cntco !=5 and  ide_cntdo= 0 " //SOLO NOTAS DE CREDITO
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n");
        if (lis_sql1 != null && !lis_sql1.isEmpty()) {
            try {
                dou_notas = Double.parseDouble(lis_sql1.get(0) + "");
            } catch (Exception e) {
            }
        }
        dou_valor = dou_valor - dou_notas;
        return utilitario.getFormatoNumero(dou_valor);
    }

    //ADQUISICIONES NO OBJETO DE IVA
    private String getValor531() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(base_no_objeto_iva_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null \n"
                + "and ide_cntdo not in (0,5)  " //NO NOTAS DE CREDITO NI NOTAS DE VENTA
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    //ADQUISICIONES NO OBJETO DE IVA -NOTAS DE CREDITO
    private String getValor541() {
        double dou_valor = Double.parseDouble(getValor531());
        double dou_notas = 0;
        List lis_sql1 = utilitario.getConexion().consultar("select sum(base_no_objeto_iva_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null  AND liquida_nota_cpcfa=0\n"
                + "and ide_cntco !=5  and  ide_cntdo= 0 " //SOLO NOTAS DE CREDITO
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n");
        if (lis_sql1 != null && !lis_sql1.isEmpty()) {
            try {
                dou_notas = Double.parseDouble(lis_sql1.get(0) + "");
            } catch (Exception e) {
            }
        }
        dou_valor = dou_valor - dou_notas;
        return utilitario.getFormatoNumero(dou_valor);
    }

    //ADQUISICIONES RISE
    private String getValor508() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(base_no_objeto_iva_cpcfa+base_tarifa0_cpcfa+base_grabada_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null  \n"
                + "and ide_cntdo=5"
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    //ADQUISICIONES RISE - NOTAS DE CREDITO
    private String getValor518() {
        double dou_valor = Double.parseDouble(getValor508());

        double dou_notas = 0;
        List lis_sql1 = utilitario.getConexion().consultar("select sum(base_no_objeto_iva_cpcfa+base_tarifa0_cpcfa+base_grabada_cpcfa) from cxp_cabece_factur a \n"
                + "inner join gen_persona b on a.ide_geper= b.ide_geper\n"
                + "where ide_cpefa=0 and ide_rem_cpcfa is null  AND liquida_nota_cpcfa=0\n"
                + "and ide_cntco =5  and  ide_cntdo= 0 " //SOLO NOTAS DE CREDITO
                + "and fecha_emisi_cpcfa  BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'\n");
        if (lis_sql1 != null && !lis_sql1.isEmpty()) {
            try {
                dou_notas = Double.parseDouble(lis_sql1.get(0) + "");
            } catch (Exception e) {
            }
        }
        dou_valor = dou_valor - dou_notas;
        return utilitario.getFormatoNumero(dou_valor);
    }

    //VENTAS LOCALES (EXCLUYE ACTIVOS FIJOS) TARIFA DIFERENTE DE CERO  
    private String getValor401() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(base_grabada_cccfa) from cxc_cabece_factura\n"
                + "where ide_ccefa=0\n"
                + "and fecha_emisi_cccfa BETWEEN  '" + fecha_inicio + "' AND '" + fecha_fin + "' ");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    private String getValor411() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(base_grabada_cccfa) from cxc_cabece_factura\n"
                + "where ide_ccefa=0\n"
                + "and fecha_emisi_cccfa BETWEEN  '" + fecha_inicio + "' AND '" + fecha_fin + "' ");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        //Se resta el valor de las notas de credito base_grabada_cccfa

        List lis_sql2 = utilitario.getConexion().consultar(
                "select  sum(base_grabada_cpcno) as total \n"
                + "from cxp_cabecera_nota where fecha_emisi_cpcno BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' and ide_cpeno=1");
        double dou_notas = 0;
        try {
            dou_notas = Double.parseDouble(lis_sql2.get(0) + "");
        } catch (Exception e) {
        }
        dou_valor = dou_valor - dou_notas;

        return utilitario.getFormatoNumero(dou_valor);
    }

    private String getValor421() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(valor_iva_cccfa) from cxc_cabece_factura\n"
                + "where ide_ccefa=0\n"
                + "and fecha_emisi_cccfa BETWEEN  '" + fecha_inicio + "' AND '" + fecha_fin + "' ");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    private String getValor403() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(base_tarifa0_cccfa) from cxc_cabece_factura\n"
                + "where ide_ccefa=0\n"
                + "and fecha_emisi_cccfa BETWEEN  '" + fecha_inicio + "' AND '" + fecha_fin + "' ");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    private String getValor413() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(base_tarifa0_cccfa) from cxc_cabece_factura\n"
                + "where ide_ccefa=0\n"
                + "and fecha_emisi_cccfa BETWEEN  '" + fecha_inicio + "' AND '" + fecha_fin + "' ");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }

        //Se resta el valor de las notas de credito base_tarifa0_cpcno
        List lis_sql2 = utilitario.getConexion().consultar(
                "select  sum(base_tarifa0_cpcno) as total \n"
                + "from cxp_cabecera_nota where fecha_emisi_cpcno BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' and ide_cpeno=1");
        double dou_notas = 0;
        try {
            dou_notas = Double.parseDouble(lis_sql2.get(0) + "");
        } catch (Exception e) {
        }
        dou_valor = dou_valor - dou_notas;

        return utilitario.getFormatoNumero(dou_valor);
    }

    //TRANSFERENCIAS NO OBJETO DE IVA
    private String getValor431() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(base_no_objeto_iva_cccfa) from cxc_cabece_factura\n"
                + "where ide_ccefa=0\n"
                + "and fecha_emisi_cccfa BETWEEN  '" + fecha_inicio + "' AND '" + fecha_fin + "' ");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    //NOTAS DE CREDITO TARIFA 0 POR COMPESNAR PROXIMO MES
    private String getValor543() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(base_no_objeto_iva_cpcfa+base_tarifa0_cpcfa) from cxp_cabece_factur where ide_cpefa=0 and ide_cntdo=0\n"
                + "and fecha_emisi_cpcfa BETWEEN   '" + fecha_inicio + "' AND '" + fecha_fin + "' AND liquida_nota_cpcfa=1 ");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    //NOTAS DE CREDITO TARIFA DIFERENTE DE 0 POR COMPESNAR PROXIMO MES
    private String getValor544() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(base_grabada_cpcfa) from cxp_cabece_factur where ide_cpefa=0 and ide_cntdo=0\n"
                + "and fecha_emisi_cpcfa BETWEEN'" + fecha_inicio + "' AND '" + fecha_fin + "' AND liquida_nota_cpcfa=1  ");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    //IVA NOTAS DE CREDITO TARIFA DIFERENTE DE 0 POR COMPESNAR PROXIMO MES
    private String getValor554() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(valor_iva_cpcfa) from cxp_cabece_factur where ide_cpefa=0 and ide_cntdo=0\n"
                + "and fecha_emisi_cpcfa BETWEEN'" + fecha_inicio + "' AND '" + fecha_fin + "' AND liquida_nota_cpcfa=1  ");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    private String consultarVentas12Contado() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("SELECT  SUM(df.total_ccdfa ) as valor "
                + "FROM cxc_cabece_factura cf "
                + "LEFT JOIN cxc_deta_factura df ON (df.ide_cccfa = cf.ide_cccfa) "
                + "LEFT JOIN con_deta_forma_pago fp ON (cf.ide_cndfp = fp.ide_cndfp) "
                + "WHERE df.alterno_ccdfa in('401','402') "
                + " and  cf.ide_ccefa !=1" //no anuladas
                + "AND cf.fecha_trans_cccfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' "
                + "AND fp.dias_cndfp=0");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    private String consultarVentas12Credito() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("SELECT  SUM(df.total_ccdfa ) as valor "
                + "FROM cxc_cabece_factura cf "
                + "LEFT JOIN cxc_deta_factura df ON (df.ide_cccfa = cf.ide_cccfa) "
                + "LEFT JOIN con_deta_forma_pago fp ON (cf.ide_cndfp = fp.ide_cndfp) "
                + "WHERE df.alterno_ccdfa in('401','402') "
                + " and  cf.ide_ccefa !=1" //no anuladas
                + "AND cf.fecha_trans_cccfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' "
                + "AND fp.dias_cndfp!=0");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    public String consultarRetencionIVA(String casillero) {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("SELECT  SUM(dr.valor_cndre) as valor_cndre"
                + " FROM con_cabece_retenc cr "
                + " LEFT JOIN con_detall_retenc dr ON (dr.ide_cncre = cr.ide_cncre) "
                + " JOIN con_cabece_impues ci ON (ci.ide_cncim = dr.ide_cncim) "
                + " WHERE cr.fecha_emisi_cncre BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' "
                + " AND ci.casillero_cncim in(" + utilitario.generarComillaSimple(casillero) + ") "
                + " AND ide_cnere=0 "
                + " AND es_venta_cncre=false");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    private String consultarImpuestoMesAnterior() {
        double dou_valor = 0;
        int mes_actual = utilitario.getMes(fecha_inicio);
        int anio_actual = utilitario.getAnio(fecha_inicio);
        if (mes_actual == 1) {
            mes_actual = 12;
            anio_actual--;
        } else {
            mes_actual--;
        }
        String fecha_inicio_anterior = utilitario.getFormatoFecha(anio_actual + "-" + mes_actual + "-01");
        String fecha_fin_anterior = utilitario.getUltimaFechaMes(fecha_inicio_anterior);
        List lis_sql = utilitario.getConexion().consultar("SELECT  SUM(df.total_ccdfa ) as valor "
                + "FROM cxc_cabece_factura cf "
                + "LEFT JOIN cxc_deta_factura df ON (df.ide_cccfa = cf.ide_cccfa) "
                + "LEFT JOIN con_deta_forma_pago fp ON (cf.ide_cndfp = fp.ide_cndfp) "
                + "WHERE df.alterno_ccdfa in('401','402') "
                + " and  cf.ide_ccefa !=1" //no anuladas
                + "AND cf.fecha_trans_cccfa BETWEEN '" + fecha_inicio_anterior + "' AND '" + fecha_fin_anterior + "' "
                + "AND fp.dias_cndfp!=0 ");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

        public String consultarRetencionVentasIVA() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("SELECT  SUM(dr.valor_cndre) as valor_cndre"
                + " FROM con_cabece_retenc cr "
                + " LEFT JOIN con_detall_retenc dr ON (dr.ide_cncre = cr.ide_cncre) "
                + " WHERE cr.fecha_emisi_cncre BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' "
                + " AND ide_cnere=0 "
                + " AND es_venta_cncre=true");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    
    
    private Element crearElemento(String nombre, String[] atributos, String texto) {
        Element elemento = doc_formulario104.createElement(nombre);
        if (atributos != null) {
            for (int i = 0; i < atributos.length; i++) {
                elemento.setAttribute(atributos[0], atributos[1]);
            }
        }
        if (texto == null) {
            texto = "";
        }
        elemento.appendChild(doc_formulario104.createTextNode(texto));
        return elemento;
    }

    private Element crearElementoCDATA(String nombre, String[] atributos, String texto) {
        Element elemento = doc_formulario104.createElement(nombre);
        if (atributos != null) {
            for (int i = 0; i < atributos.length; i++) {
                elemento.setAttribute(atributos[0], atributos[1]);
            }
        }
        elemento.appendChild(doc_formulario104.createCDATASection(texto));
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

    public String getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public void setTipoDeclaracion(String tipoDeclaracion) {
        this.tipoDeclaracion = tipoDeclaracion;
    }

    public String getNumSustituye() {
        return numSustituye;
    }

    public void setNumSustituye(String numSustituye) {
        if (numSustituye == null) {
            numSustituye = "";
        }
        this.numSustituye = numSustituye;
    }

}
//ALTER TABLE "public"."cxp_cabece_factur"
//ADD COLUMN "liquida_nota_cpcfa" int2;
//update cxp_cabece_factur set liquida_nota_cpcfa = 0 
