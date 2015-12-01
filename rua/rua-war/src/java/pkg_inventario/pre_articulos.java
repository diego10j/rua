/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_inventario;

import framework.componentes.Arbol;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.event.SelectEvent;
import pkg_contabilidad.cls_contabilidad;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_articulos extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Division div_division = new Division();
    private Arbol arb_arbol = new Arbol();
    private Boton bot_cuenta_contable = new Boton();
    private Dialogo dia_cuenta_contable = new Dialogo();
    private AutoCompletar aut_cuenta_contable = new AutoCompletar();
    private AutoCompletar aut_filtar_articulo = new AutoCompletar();
    private Boton bot_clean = new Boton();

    public pre_articulos() {
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");
        if (lis_plan != null && !lis_plan.isEmpty()) {
            bar_botones.agregarComponente(bot_cuenta_contable);

            aut_filtar_articulo.setId("aut_filtar_articulo");
            aut_filtar_articulo.setAutoCompletar("SELECT ide_inarti,nombre_inarti  FROM inv_articulo WHERE ide_empr="+utilitario.getVariable("ide_empr") +" ORDER BY nombre_inarti");
            aut_filtar_articulo.setMetodoChange("filtrarArticulo");
            bar_botones.agregarComponente(new Etiqueta("Articulo: "));
            bar_botones.agregarComponente(aut_filtar_articulo);
            
            bot_clean.setIcon("ui-icon-cancel");
            bot_clean.setTitle("Limpiar");
            bot_clean.setMetodo("limpiar");
            bar_botones.agregarComponente(bot_clean);
            
            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("inv_articulo", "ide_inarti", 1);
            tab_tabla1.getColumna("nivel_inarti").setCombo(utilitario.getListaNiveles());
            tab_tabla1.setCampoNombre("nombre_inarti"); //Para que se configure el arbol
            tab_tabla1.setCampoPadre("inv_ide_inarti"); //Para que se configure el arbol
            tab_tabla1.agregarArbol(arb_arbol); //Para que se configure el arbol
            tab_tabla1.getColumna("ide_infab").setCombo("inv_fabricante", "ide_infab", "nombre_infab", "");
            tab_tabla1.getColumna("ide_inmar").setCombo("inv_marca", "ide_inmar", "nombre_invmar", "");
            tab_tabla1.getColumna("ide_inuni").setCombo("inv_unidad", "ide_inuni", "nombre_inuni", "");
            tab_tabla1.getColumna("ide_intpr").setCombo("inv_tipo_producto", "ide_intpr", "nombre_intpr", "");
            tab_tabla1.getColumna("ide_inepr").setCombo("inv_estado_produc", "ide_inepr", "nombre_inepr", "");
            tab_tabla1.getColumna("nivel_inarti").setValorDefecto("HIJO");
            tab_tabla1.getColumna("hace_kardex_inarti").setValorDefecto("true");
            tab_tabla1.getColumna("es_combo_inarti").setValorDefecto("false");
            List lista = new ArrayList();
            Object fila1[] = {
                "1", "SI"
            };
            Object fila2[] = {
                "-1", "NO"
            };
            Object fila3[] = {
                "0", "NO  OBJETO"
            };
            lista.add(fila1);
            lista.add(fila2);
            lista.add(fila3);
            tab_tabla1.getColumna("iva_inarti").setRadio(lista, "1");
            tab_tabla1.getColumna("iva_inarti").setRadioVertical(true);
            tab_tabla1.setTipoFormulario(true);
            tab_tabla1.getGrid().setColumns(4);
            tab_tabla1.getColumna("ide_georg").setCombo("gen_organigrama", "ide_georg", "nombre_georg", "");// cargar un combo de una con el ide, nombre



            tab_tabla1.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla1);

            arb_arbol.setId("arb_arbol");
            arb_arbol.setCondicion("ide_inarti!=53");
            arb_arbol.dibujar();

            bot_cuenta_contable.setId("bot_cuenta_contable");
            bot_cuenta_contable.setValue("Cuenta Contable");
            bot_cuenta_contable.setMetodo("abrirCuentaContable");

            dia_cuenta_contable.setId("dia_cuenta_contable");
            dia_cuenta_contable.setTitle("Cuentas Contables");
            dia_cuenta_contable.setWidth("40%");
            dia_cuenta_contable.setHeight("20%");

            aut_cuenta_contable.setId("aut_cuenta_contable");
            aut_cuenta_contable.setAutoCompletar("SELECT ide_cndpc,codig_recur_cndpc,nombre_cndpc FROM con_det_plan_cuen WHERE nivel_cndpc='HIJO' and ide_cncpc=" + lis_plan.get(0));

            Grid gri_matriz = new Grid();
            gri_matriz.setColumns(2);
            gri_matriz.getChildren().add(new Etiqueta("Nombre:"));
            gri_matriz.getChildren().add(aut_cuenta_contable);
            dia_cuenta_contable.setDialogo(gri_matriz);

            div_division.setId("div_division");
            div_division.dividir2(arb_arbol, pat_panel, "21%", "V");


            agregarComponente(div_division);
            agregarComponente(dia_cuenta_contable);



        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }

    }

    @Override
    public void insertar() {
        tab_tabla1.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla1.eliminar();
    }

        public void limpiar() {
        if (aut_filtar_articulo.getValue() != null) {
            aut_filtar_articulo.setValue(null);            
            utilitario.addUpdate("aut_filtar_articulo");
        }
    }
    
    public void filtrarArticulo(SelectEvent evt){
        aut_filtar_articulo.onSelect(evt);
        if (aut_filtar_articulo.getValue()!=null) {
            tab_tabla1.setFilaActual(aut_filtar_articulo.getValor());
            utilitario.addUpdate("tab_tabla1");
        }else{
            utilitario.agregarMensajeInfo("No existe el Articulo", "Ingrese otro Artículo");
        }        
    }
    
    public void abrirCuentaContable() {
        cls_contabilidad conta = new cls_contabilidad();
        String str_cuenta = conta.buscarCuentaProducto("INVENTARIO-GASTO-ACTIVO", tab_tabla1.getValorSeleccionado());
        aut_cuenta_contable.setValor(str_cuenta);
        aut_cuenta_contable.setDisabled(true);
        dia_cuenta_contable.dibujar();
        utilitario.addUpdate("dia_cuenta_contable");
    }

    public Arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(Arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Dialogo getDia_cuenta_contable() {
        return dia_cuenta_contable;
    }

    public void setDia_cuenta_contable(Dialogo dia_cuenta_contable) {
        this.dia_cuenta_contable = dia_cuenta_contable;
    }

    public AutoCompletar getAut_cuenta_contable() {
        return aut_cuenta_contable;
    }

    public void setAut_cuenta_contable(AutoCompletar aut_cuenta_contable) {
        this.aut_cuenta_contable = aut_cuenta_contable;
    }

    public AutoCompletar getAut_filtar_articulo() {
        return aut_filtar_articulo;
    }

    public void setAut_filtar_articulo(AutoCompletar aut_filtar_articulo) {
        this.aut_filtar_articulo = aut_filtar_articulo;
    } 
}
