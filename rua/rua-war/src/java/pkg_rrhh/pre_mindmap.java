/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_rrhh;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.mindmap.Mindmap;
import org.primefaces.model.mindmap.DefaultMindmapNode;
import org.primefaces.model.mindmap.MindmapNode;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author stalin
 */
public class pre_mindmap extends Pantalla{
    
    private  Mindmap mind_mapa= new Mindmap();
    
    private MindmapNode root;  
      
    private MindmapNode selectedNode;
    
    public pre_mindmap(){
    root = new DefaultMindmapNode("Departamentos", "Google WebSite", "FFCC00", false);  
        
        MindmapNode ips = new DefaultMindmapNode("IPs", "IP Numbers", "6e9ebf", false);  
        MindmapNode ns = new DefaultMindmapNode("NS(s)", "Namespaces", "6e9ebf", true);  
        MindmapNode malware = new DefaultMindmapNode("Malware", "Malicious Software", "6e9ebf", true);  
          
        root.addNode(ips);  
        root.addNode(ns);  
        root.addNode(malware); 
    
        mind_mapa.setValueExpression("value", crearValueExpression("pre_index.clase.root"));
//        mind_mapa.setStyle("width:100%;height:600px;border:1px solid black;");
        agregarComponente(mind_mapa);
    
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

    @Override
    public void eliminar() {
//        tab_tabla.eliminar();
    }

    private ValueExpression crearValueExpression(String expresion) {
FacesContext facesContext = FacesContext.getCurrentInstance();
return facesContext.getApplication().getExpressionFactory().createValueExpression(
facesContext.getELContext(), "#{" + expresion + "}", Object.class);
}




    
}
