/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_rrhh;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Ajax;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Tabla;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.mindmap.Mindmap;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.mindmap.DefaultMindmapNode;
import org.primefaces.model.mindmap.MindmapNode;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author stalin
 */
public class pre_demo extends Pantalla {

    private Mindmap mind_mapa = new Mindmap();
    private MindmapNode root;
    private MindmapNode selectedNode;
    private Tabla tab_tabla = new Tabla();
    private Dialogo dia_datos_empl = new Dialogo();
    private Grid gri_datos_padre = new Grid();
    private Grid gri_datos_empl = new Grid();
    private Imagen foto = new Imagen();

    public pre_demo() {

        TablaGenerica tab_tabla = utilitario.consultar("select DISTINCT org.nombre_georg ,org.ide_georg from gen_organigrama org, gen_persona pers "
                + "where pers.es_empleado_geper is true "
                + "and org.ide_georg=pers.ide_georg "
                + "and ide_rheem=0 ");

        root = new DefaultMindmapNode("Departamentos", "DEPARTAMENTOS EMPRESA", "FFCC00", false);

        MindmapNode ips;
        for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
            ips = new DefaultMindmapNode(tab_tabla.getValor(i, "nombre_georg"), tab_tabla.getValor(i, "ide_georg"), "6e9ebf", true);

            root.addNode(ips);
        }

        mind_mapa.setValueExpression("value", crearValueExpression("pre_index.clase.root"));
        //AJAX
        Ajax seleccionarnodo = new Ajax();
        seleccionarnodo.setMetodo("onNodeSelect");
        Ajax seleccionarnodo2 = new Ajax();
        seleccionarnodo2.setMetodo("onNodeDblselect");

        mind_mapa.addClientBehavior("dblselect", seleccionarnodo2);
        mind_mapa.addClientBehavior("select", seleccionarnodo);
        mind_mapa.setStyle("width:100%;height:600px;border:5px solid black;");

        mind_mapa.setRendered(true);
        agregarComponente(mind_mapa);

//        tab_empl.setId("tab_empl");
//        tab_empl.setSql("select ide_geper,nom_geper from gen_persona where ide_geper=-1");
//        tab_empl.setCampoPrimaria("ide_geper");
//        tab_empl.dibujar();
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonsNavegacion();
        dia_datos_empl.setId("dia_datos_empl");
        dia_datos_empl.setWidth("30");
        dia_datos_empl.setHeight("40");

        gri_datos_empl.setId("gri_datos_empl");

        gri_datos_empl.setColumns(2);

        dia_datos_empl.setDialogo(gri_datos_empl);
        dia_datos_empl.getBot_aceptar().setRendered(false);
        dia_datos_empl.getBot_cancelar().setRendered(true);
        agregarComponente(dia_datos_empl);

    }

    public void onNodeDblselect(SelectEvent event) {
        this.selectedNode = (MindmapNode) event.getObject();
        System.out.println();
        System.out.println("doble click metodo " + selectedNode.getLabel());
        System.out.println("doble clik metodo " + selectedNode.getData());
        TablaGenerica tab_empl = utilitario.consultar("select ide_geper,foto_geper,nom_geper,nombre_rheor,sueldo_geper,telefono_geper,direccion_geper "
                + "from gen_persona per "
                + "left join reh_estruc_organi eorg on eorg.ide_rheor=per.ide_rheor "
                + "where per.ide_geper=" + selectedNode.getData());

        if (tab_empl.getTotalFilas() > 0) {

            gri_datos_empl.getChildren().clear();
            foto.setValue(tab_empl.getValor(0, "foto_geper"));

            foto.setWidth("80");
            foto.setHeight("100");

            gri_datos_empl.getChildren().add(new Etiqueta("Codigo: "));
            gri_datos_empl.getChildren().add(new Etiqueta(tab_empl.getValor(0, "ide_geper")));
            gri_datos_empl.getChildren().add(new Etiqueta("Nombre: "));
            gri_datos_empl.getChildren().add(new Etiqueta(tab_empl.getValor(0, "nom_geper")));
            gri_datos_empl.getChildren().add(new Etiqueta("Cargo: "));
            gri_datos_empl.getChildren().add(new Etiqueta(tab_empl.getValor(0, "nombre_rheor")));
            gri_datos_empl.getChildren().add(new Etiqueta("Sueldo: "));
            gri_datos_empl.getChildren().add(new Etiqueta(tab_empl.getValor(0, "sueldo_geper")));
            gri_datos_empl.getChildren().add(new Etiqueta("Teléfono: "));
            gri_datos_empl.getChildren().add(new Etiqueta(tab_empl.getValor(0, "telefono_geper")));
            gri_datos_empl.getChildren().add(new Etiqueta("Dirección: "));
            gri_datos_empl.getChildren().add(new Etiqueta(tab_empl.getValor(0, "direccion_geper")));
            gri_datos_empl.getChildren().add(new Etiqueta("Foto: "));
            gri_datos_empl.getChildren().add(foto);

            dia_datos_empl.setTitle("DATOS DEL EMPLEADO");
            dia_datos_empl.dibujar();
            utilitario.addUpdate("dia_datos_empl");

        }

    }

    public void onNodeSelect(SelectEvent event) {

        MindmapNode node = (MindmapNode) event.getObject();

        if (node.getChildren().isEmpty()) {
            Object label = node.getLabel();
            Object ide_georg = node.getData();
            System.out.println("nombre georg" + label);
            System.out.println("ide_georg " + ide_georg);

            TablaGenerica tab_empl = utilitario.consultar("select * from gen_persona where ide_georg=" + ide_georg + " "
                    + "and ide_rheem=0 "
                    + "and es_empleado_geper is TRUE");

            for (int i = 0; i < tab_empl.getTotalFilas(); i++) {
                node.addNode(new DefaultMindmapNode(tab_empl.getValor(i, "nom_geper"), tab_empl.getValor(i, "ide_geper"), "82c542", true));
            }

        }

    }

    @Override
    public void insertar() {
//        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
//        tab_tabla.guardar();
//        guardarPantalla();
    }

    @Override
    public void eliminar() {
//        tab_tabla.eliminar();
    }

    public MindmapNode getRoot() {
        return root;
    }

    public void setRoot(MindmapNode root) {
        this.root = root;
    }

    public MindmapNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(MindmapNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    public Dialogo getDia_datos_empl() {
        return dia_datos_empl;
    }

    public void setDia_datos_empl(Dialogo dia_datos_empl) {
        this.dia_datos_empl = dia_datos_empl;
    }

}
