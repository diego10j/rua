/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gestion;

import framework.aplicacion.Framework;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Grupo;
import framework.componentes.Tabla;
import java.io.IOException;
import java.util.Map;
import javax.el.MethodExpression;
import javax.faces.application.Resource;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.remotecommand.RemoteCommand;

/**
 *
 * @author Diego
 */
public class Organigrama extends Grupo {

	private Resource res_script = FacesContext.getCurrentInstance().getApplication().getResourceHandler().createResource("primitives.min.js", "organigrama");
	private Resource res_estilo = FacesContext.getCurrentInstance().getApplication().getResourceHandler().createResource("primitives.latest.css", "organigrama");
	private int altoCuadro = 35;
	private int anchoCuadro = 160;
	private String colorFondo = "e1e3e8";
	private String colorLetra = "1c1d36";
	private Framework framework = new Framework();
	private HtmlInputHidden hti_selecci_organ = new HtmlInputHidden();
	private RemoteCommand rec_click = new RemoteCommand();
	private boolean dibuja = false;

	public String validar(){
		//
		String str_mensaje="";
		TablaGenerica tab_nodos = framework.consultar("SELECT * FROM GEN_DEPARTAMENTO WHERE ACTIVO_GEDEP=true AND NIVEL_GEDEP=0 order by NIVEL_GEDEP,TIPO_GEDEP,ORDEN_GEDEP");
		//Validar que exista raiz y q tipo sea centro
		if(!tab_nodos.isEmpty()){
			//Para nodo raiz			
			if(tab_nodos.getValor("TIPO_GEDEP").equalsIgnoreCase("CENTRO")){
			}
			else{
				str_mensaje="El nivel 0 debe tener como tipo Centro";            		  
			}
		}
		else{
			str_mensaje="No existe un nivel 0";            	  
		} 
		return str_mensaje;
	}

	public Organigrama() {
		hti_selecci_organ.setId("hti_selecci_organ");
		this.getChildren().add(hti_selecci_organ);
		rec_click.setName("seleccionarElemento");
		this.getChildren().add(rec_click);
	}

	public void setMetodo(String metodo) {
		MethodExpression methodExpression = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
				createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{pre_index.clase." + metodo + "}", null, new Class<?>[0]);
		rec_click.setActionExpression(methodExpression);
	}

	public void setMetodoRuta(String metodo) {
		MethodExpression methodExpression = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
				createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{" + metodo + "}", null, new Class<?>[0]);
		rec_click.setActionExpression(methodExpression);
	}

	private String getIzquierda(String nombre, String padre, String codigo, String posicionHijos) {
		//estilo Vertical - Horizontal
		//System.out.println("getIzquierda  " + nombre + "  " + padre + "   " + codigo);
		posicionHijos = posicionHijos != null ? posicionHijos : "Horizontal";
		return " var elemento" + codigo + " = new primitives.orgdiagram.ItemConfig();"
		+ " elemento" + codigo + ".description ='" + nombre + "';"
		+ " elemento" + codigo + ".childrenPlacementType = primitives.orgdiagram.ChildrenPlacementType." + posicionHijos + ";"
		+ " elemento" + codigo + ".itemType = primitives.orgdiagram.ItemType.Assistant; "
		+ " elemento" + codigo + ".adviserPlacementType = primitives.orgdiagram.AdviserPlacementType.Left;	"
		+ " elemento" + codigo + ".templateName ='organigramaTemplate';"
		+ " elemento" + padre + ".items.push(elemento" + codigo + ");";
	}

	private String getDerecha(String nombre, String padre, String codigo, String posicionHijos) {
		//System.out.println("getDerecha  " + nombre + "  " + padre + "   " + codigo);
		posicionHijos = posicionHijos != null ? posicionHijos : "Horizontal";
		//estilo Vertical - Horizontal
		return " var elemento" + codigo + " = new primitives.orgdiagram.ItemConfig();"
		+ " elemento" + codigo + ".description ='" + nombre + "'; "
		+ " elemento" + codigo + ".childrenPlacementType = primitives.orgdiagram.ChildrenPlacementType." + posicionHijos + ";"
		+ " elemento" + codigo + ".itemType = primitives.orgdiagram.ItemType.Assistant; "
		+ " elemento" + codigo + ".adviserPlacementType = primitives.orgdiagram.AdviserPlacementType.Right;	"
		+ " elemento" + codigo + ".templateName ='organigramaTemplate';"
		+ " elemento" + padre + ".items.push(elemento" + codigo + ");";
	}

	private String getRaiz(String nombre) {
		//  System.out.println("getRaiz()  " + nombre);
		//     hti_selecci_organ.setValue(nombre);
		return " var elemento0 = new primitives.orgdiagram.ItemConfig();"
		+ " elemento0.description = '" + nombre + "';"
		+ " elemento0.templateName = 'organigramaTemplate';";
	}

	private String getConfiguraciones() {
		return " var options = new primitives.orgdiagram.Config();"
				+ " options.rootItem = elemento0;"
				//     + " options.cursorItem = elemento0;"
				+ " options.normalLevelShift = 20;"
				+ " options.dotLevelShift = 10;"
				+ " options.lineLevelShift = 10;"
				+ " options.normalItemsInterval = 20;"
				+ " options.dotItemsInterval = 10;"
				+ " options.lineItemsInterval = 5;"
				+ " options.buttonsPanelSize = 48;"
				+ " options.pageFitMode = primitives.orgdiagram.PageFitMode.None;"
				+ " options.hasSelectorCheckbox =primitives.common.Enabled.False;"
				+ " options.templates = [getOrganigramaTemplate()];"
				+ " options.onItemRender = onTemplateRender;"
				+ " options.onCursorChanged = function onClick(e, data) {  document.getElementById('formulario:hti_selecci_organ').value=data.context.description; seleccionarElemento();} ;";
	}

	private String getNivel(String nombre, String padre, String codigo) {
		//    System.out.println("getNivel  " + nombre + "  " + padre + "   " + codigo);
		return " var elemento" + codigo + " = new primitives.orgdiagram.ItemConfig();"
		+ " elemento" + codigo + ".description = '" + nombre + "';"
		+ " elemento" + codigo + ".childrenPlacementType = primitives.orgdiagram.ChildrenPlacementType.Horizontal;"
		+ " elemento" + codigo + ".templateName = 'organigramaTemplate';"
		+ " elemento" + padre + ".items.push(elemento" + codigo + ");";
	}

	private String getHijo(String nombre, String padre, String codigo, String posicionHijos) {
		//      System.out.println("getHijo  " + nombre + "  " + padre + "   " + codigo);
		posicionHijos = posicionHijos != null ? posicionHijos : "Vertical";
		return " var elemento" + codigo + " = new primitives.orgdiagram.ItemConfig();"
		+ " elemento" + codigo + ".description = '" + nombre + "';"
		+ " elemento" + codigo + ".templateName = 'organigramaTemplate';"
		+ " elemento" + codigo + ".childrenPlacementType = primitives.orgdiagram.ChildrenPlacementType." + posicionHijos + ";"
		+ " elemento" + padre + ".items.push(elemento" + codigo + ");";
	}

	private String getNivelOculto(String padre, String codigo) {
		//        System.out.println("getNivelOCULTO " + padre + "   " + codigo);
		return " var elemento" + codigo + " = new primitives.orgdiagram.ItemConfig();"
		+ " elemento" + codigo + ".isVisible = false;"
		+ " elemento" + codigo + ".description = 'OCULTO';"
		+ " elemento" + codigo + ".childrenPlacementType = primitives.orgdiagram.ChildrenPlacementType.Horizontal;"
		+ " elemento" + codigo + ".templateName = 'organigramaTemplate';"
		+ " elemento" + padre + ".items.push(elemento" + codigo + ");";
	}

	private String getResizePlaceholder() {
		return " function ResizePlaceholder() {"
				+ " var bodyWidth = 100; "
				+ "	var bodyHeight = " + framework.getVariable("ALTO_PANTALLA") + ";"
				+ "	jQuery('#orgdiagram').css("
				+ "	{"
				+ "	'width': bodyWidth + '%',"
				+ "	'height': bodyHeight + 'px'"
				+ "	});"
				+ "	SetupWidget();"
				+ "	}";
	}

	private String getTemplate() {
		return " function getOrganigramaTemplate() {"
				+ " var result = new primitives.orgdiagram.TemplateConfig();"
				+ " result.name = 'organigramaTemplate';"
				+ " result.itemSize = new primitives.common.Size(" + anchoCuadro + "," + altoCuadro + ");"
				+ " result.minimizedItemSize = new primitives.common.Size(3, 3);"
				+ " result.highlightPadding = new primitives.common.Thickness(2, 2, 2, 2);"
				+ " var itemTemplate = jQuery("
				+ " '<div><div align=\"center\" name=\"description\" class=\"bp-item\" style=\"display: block;width: 98%;height:100%; padding:5px;  font-size: 10px;background:" + colorFondo + ";color:" + colorLetra + ";\"></div></div>'"
				+ " ).css({"
				+ " width: result.itemSize.width + 'px',"
				+ " height: result.itemSize.height + 'px'"
				+ " }).addClass('bp-item bp-corner-all bt-item-frame');"
				+ " result.itemTemplate = itemTemplate.wrap('<div>').parent().html();"
				+ " return result;"
				+ " }";
	}

	private String getOnTemplateRender() {
		return " function onTemplateRender(event, data) {"
				+ " switch (data.renderingMode) {"
				+ " case primitives.common.RenderingMode.Create:"
				+ " break;"
				+ " case primitives.common.RenderingMode.Update:      "
				+ " break;"
				+ " }"
				+ " var itemConfig = data.context;"
				+ " var element = data.element.find('[name=description]');"
				+ " if (element.text() != itemConfig['description']) {"
				+ " element.text(itemConfig['description']);"
				+ " }"
				+ " }";
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		String str_mensaje=validar();
		System.out.println("XXXX  "+str_mensaje);
		if(!str_mensaje.isEmpty()){
			framework.agregarMensajeError("No se puede graficar",str_mensaje );
			super.encodeBegin(context);
		}
		else{    	  

			dibuja = false;
			ResponseWriter writer = context.getResponseWriter();
			//importo el script y el estilo del organigrama
			Map<Object, Object> contextMap = context.getAttributes();
			String key = res_script.getResourceName() + res_script.getLibraryName();
			if (!contextMap.containsKey(key)) {
				contextMap.put(key, Boolean.TRUE);
				writer.startElement("script", null);
				writer.writeAttribute("type", "text/javascript", "type");
				writer.writeURIAttribute("src",
						((res_script != null) ? res_script.getRequestPath()
								: "RES_NOT_FOUND"), "src");
				writer.endElement("script");
			}
			key = res_estilo.getResourceName() + res_estilo.getLibraryName();
			if (!contextMap.containsKey(key)) {
				contextMap.put(key, Boolean.TRUE);
				writer.startElement("link", null);
				writer.writeAttribute("type", "text/css", "type");
				writer.writeAttribute("rel", "Stylesheet", "rel");
				writer.writeURIAttribute("href",
						((res_estilo != null) ? res_estilo.getRequestPath()
								: "RES_NOT_FOUND"), "href");
				writer.endElement("link");
			}
			//genera el script del organigrama 
			escribirOrganigrama(writer);
			super.encodeBegin(context);
			if (dibuja) {
				writer.startElement("script", this);
				writer.write("$(document).ready(function(){"
						+ " ResizePlaceholder();"
						+ "});");
				writer.endElement("script");
				writer.startElement("div", this);
				writer.writeAttribute("id", "orgdiagram", "id");
				writer.writeAttribute("style", "position: absolute; overflow: auto; left: 0px; padding: 0px; margin: 0px;background: transparent;", "style");
			}
		}
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException {
		if (dibuja) {
			ResponseWriter writer = context.getResponseWriter();
			writer.endElement("div");
		}
		super.encodeEnd(context);
	}

	private void escribirOrganigrama(ResponseWriter writer) throws IOException {
		/////AQUI SQL
		TablaGenerica tab_nodos = framework.consultar("SELECT * FROM GEN_DEPARTAMENTO WHERE ACTIVO_GEDEP=true order by NIVEL_GEDEP,TIPO_GEDEP,ORDEN_GEDEP");
		if (tab_nodos.isEmpty()) {
			return;
		}
		dibuja = true;
		writer.startElement("script", this);
		String str_scrip = "";
		String str_nivel = "";
		String str_padre_actual = "";
		try {
			for (int i = 0; i < tab_nodos.getTotalFilas(); i++) {
				//Para nodo raiz
				if (tab_nodos.getValor(i, "NIVEL_GEDEP").equals("0") && tab_nodos.getValor(i, "TIPO_GEDEP").equalsIgnoreCase("CENTRO")) {
					str_padre_actual = "0";
					str_nivel = tab_nodos.getValor(i, "NIVEL_GEDEP");
					str_scrip += getRaiz(tab_nodos.getValor(i, "DETALLE_GEDEP"));
					continue;
				}
				//nuevo nivel
				if (!str_nivel.equals(tab_nodos.getValor(i, "NIVEL_GEDEP"))) {
					str_nivel = tab_nodos.getValor(i, "NIVEL_GEDEP");
					if (tab_nodos.getValor(i, "TIPO_GEDEP").equalsIgnoreCase("CENTRO")) {
						if (tab_nodos.getValor(i, "GEN_IDE_GEDEP") == null) {
							//Si tiene nivel centro   
							str_scrip += getNivel(tab_nodos.getValor(i, "DETALLE_GEDEP"), str_padre_actual, tab_nodos.getValor(i, "IDE_GEDEP"));
							str_padre_actual = tab_nodos.getValor(i, "IDE_GEDEP");
							continue;
						} else {
							str_scrip += getNivel(tab_nodos.getValor(i, "DETALLE_GEDEP"), tab_nodos.getValor(i, "GEN_IDE_GEDEP"), tab_nodos.getValor(i, "IDE_GEDEP"));
							str_padre_actual = tab_nodos.getValor(i, "IDE_GEDEP");
							continue;
						}
					} else {
						//agrego nivel oculto
						str_scrip += getNivelOculto(str_padre_actual, (i * 1000) + "");
						str_padre_actual = (i * 1000) + "";
					}
				}
				if (tab_nodos.getValor(i, "TIPO_GEDEP").equalsIgnoreCase("DERECHA")) {
					if (tab_nodos.getValor(i, "GEN_IDE_GEDEP") != null) {
						//sabemos cual es el padre
						str_scrip += getDerecha(tab_nodos.getValor(i, "DETALLE_GEDEP"), tab_nodos.getValor(i, "GEN_IDE_GEDEP"), tab_nodos.getValor(i, "IDE_GEDEP"), tab_nodos.getValor(i, "POSICION_HIJOS_GEDEP"));
					} else {
						//se agrega al padre oculto actual

						str_scrip += getDerecha(tab_nodos.getValor(i, "DETALLE_GEDEP"), str_padre_actual, tab_nodos.getValor(i, "IDE_GEDEP"), tab_nodos.getValor(i, "POSICION_HIJOS_GEDEP"));
					}
					continue;
				}
				if (tab_nodos.getValor(i, "TIPO_GEDEP").equalsIgnoreCase("IZQUIERDA")) {
					if (tab_nodos.getValor(i, "GEN_IDE_GEDEP") != null) {
						//sabemos cual es el padre
						str_scrip += getIzquierda(tab_nodos.getValor(i, "DETALLE_GEDEP"), tab_nodos.getValor(i, "GEN_IDE_GEDEP"), tab_nodos.getValor(i, "IDE_GEDEP"), tab_nodos.getValor(i, "POSICION_HIJOS_GEDEP"));
					} else {
						//se agrega al padre oculto actual
						str_scrip += getIzquierda(tab_nodos.getValor(i, "DETALLE_GEDEP"), str_padre_actual, tab_nodos.getValor(i, "IDE_GEDEP"), tab_nodos.getValor(i, "POSICION_HIJOS_GEDEP"));
					}
					continue;
				}
				if (tab_nodos.getValor(i, "TIPO_GEDEP").equalsIgnoreCase("NODO")) {
					if (tab_nodos.getValor(i, "GEN_IDE_GEDEP") != null) {
						//sabemos cual es el padre
						str_scrip += getHijo(tab_nodos.getValor(i, "DETALLE_GEDEP"), tab_nodos.getValor(i, "GEN_IDE_GEDEP"), tab_nodos.getValor(i, "IDE_GEDEP"), tab_nodos.getValor(i, "POSICION_HIJOS_GEDEP"));
					} else {
						//se agrega al padre oculto actual
						str_scrip += getHijo(tab_nodos.getValor(i, "DETALLE_GEDEP"), str_padre_actual, tab_nodos.getValor(i, "IDE_GEDEP"), tab_nodos.getValor(i, "POSICION_HIJOS_GEDEP"));
					}
				}
			}
		} catch (Exception e) {
		}

		writer.write("function SetupWidget() { "
				+ " " + str_scrip
				+ " " + getConfiguraciones()
				+ "jQuery('#orgdiagram').orgDiagram(options); } "
				+ getTemplate()
				+ getOnTemplateRender() + getResizePlaceholder());
		writer.endElement("script");
	}

	public String getSeleccionado() {
		if (hti_selecci_organ.getValue() != null) {
			return hti_selecci_organ.getValue().toString();
		}
		return null;
	}

	public int getAltoCuadro() {
		return altoCuadro;
	}

	public void setAltoCuadro(int altoCuadro) {
		this.altoCuadro = altoCuadro;
	}

	public int getAnchoCuadro() {
		return anchoCuadro;
	}

	public void setAnchoCuadro(int anchoCuadro) {
		this.anchoCuadro = anchoCuadro;
	}

	public String getColorFondo() {
		return colorFondo;
	}

	public void setColorFondo(String colorFondo) {
		this.colorFondo = colorFondo;
	}

	public String getColorLetra() {
		return colorLetra;
	}

	public void setColorLetra(String colorLetra) {
		this.colorLetra = colorLetra;
	}
}
