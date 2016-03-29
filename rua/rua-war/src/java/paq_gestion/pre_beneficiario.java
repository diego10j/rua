package paq_gestion;

import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.event.AjaxBehaviorEvent;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_beneficiario extends Pantalla {
	private Tabla tab_beneficiario = new Tabla();
	public pre_beneficiario() {
		// TODO Auto-generated constructor stub
		tab_beneficiario.setId("tab_beneficiario");
		tab_beneficiario.setTipoFormulario(true);
		tab_beneficiario.getGrid().setColumns(4);
		tab_beneficiario.setTabla("GEN_BENEFICIARIO", "IDE_GEBEN", 1);
		tab_beneficiario.getColumna("ACTIVO_GEBEN").setCheck();
		tab_beneficiario.getColumna("ACTIVO_GEBEN").setValorDefecto("true");
		tab_beneficiario.getColumna("IDE_GETIC").setCombo("GEN_TIPO_CONTRIBUYENTE","IDE_GETIC","DETALLE_GETIC", "");
		tab_beneficiario.getColumna("IDE_GETIC").setMetodoChange("filtrarTipoContribuyente");
		tab_beneficiario.getColumna("IDE_GTTAE").setCombo("GTH_TIPO_ACTIVIDAD_ECONOMICA","IDE_GTTAE","DETALLE_GTTAE", "");
		tab_beneficiario.getColumna("IDE_GTTDI").setCombo("GTH_TIPO_DOCUMENTO_IDENTIDAD","IDE_GTTDI","DETALLE_GTTDI", "");
		tab_beneficiario.getColumna("GTH_IDE_GTTDI").setCombo("GTH_TIPO_DOCUMENTO_IDENTIDAD","IDE_GTTDI","DETALLE_GTTDI", "");
		tab_beneficiario.getColumna("GTH_IDE_GTTDI").setLectura(true);
		tab_beneficiario.getColumna("REPRESENTANTE_LEGAL_GEBEN").setLectura(true);
		tab_beneficiario.getColumna("DIRECCION_REPRESENTANTE_GEBEN ").setLectura(true);
		tab_beneficiario.getColumna("TELEFONO_REPRESENTANTE_GEBEN").setLectura(true);
		tab_beneficiario.getColumna("DOCUMENTO_REPRESENTANTE_GEBEN").setLectura(true);
		tab_beneficiario.dibujar();
		PanelTabla pan_panel = new PanelTabla();
		pan_panel.setMensajeWarn("BENEFICIARIO");
		pan_panel.setPanelTabla(tab_beneficiario);
		Division div_division = new Division();
		div_division.dividir1(pan_panel);
		agregarComponente(div_division);

	}

	public void filtrarTipoContribuyente(AjaxBehaviorEvent evt){
		tab_beneficiario.modificar(evt);
		if(	tab_beneficiario.getValor("IDE_GETIC").equals(utilitario.getVariable("p_gen_tipo_contribuyente_p_juridica"))){
			tab_beneficiario.getColumna("GTH_IDE_GTTDI").setLectura(false);
			tab_beneficiario.getColumna("REPRESENTANTE_LEGAL_GEBEN").setLectura(false);
			tab_beneficiario.getColumna("DIRECCION_REPRESENTANTE_GEBEN ").setLectura(false);
			tab_beneficiario.getColumna("TELEFONO_REPRESENTANTE_GEBEN").setLectura(false);
			tab_beneficiario.getColumna("DOCUMENTO_REPRESENTANTE_GEBEN").setLectura(false);
			utilitario.addUpdate("tab_beneficiario");
		}else{
			tab_beneficiario.getColumna("GTH_IDE_GTTDI").setLectura(true);
			tab_beneficiario.getColumna("REPRESENTANTE_LEGAL_GEBEN").setLectura(true);
			tab_beneficiario.getColumna("DIRECCION_REPRESENTANTE_GEBEN ").setLectura(true);
			tab_beneficiario.getColumna("TELEFONO_REPRESENTANTE_GEBEN").setLectura(true);
			tab_beneficiario.getColumna("DOCUMENTO_REPRESENTANTE_GEBEN").setLectura(true);
			tab_beneficiario.setValor("GTH_IDE_GTTDI", "");
			tab_beneficiario.setValor("REPRESENTANTE_LEGAL_GEBEN", "");
			tab_beneficiario.setValor("DIRECCION_REPRESENTANTE_GEBEN", "");
			tab_beneficiario.setValor("TELEFONO_REPRESENTANTE_GEBEN", "");
			tab_beneficiario.setValor("DOCUMENTO_REPRESENTANTE_GEBEN", "");
			tab_beneficiario.setValor("DIRECCION_REPRESENTANTE_GEBEN", "");
			utilitario.addUpdate("tab_beneficiario");
		}
	}

	public Tabla getTab_beneficiario() {
		return tab_beneficiario;
	}
	public void setTab_beneficiario(Tabla tab_beneficiario) {
		this.tab_beneficiario = tab_beneficiario;
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_beneficiario.insertar();

	}

	public boolean validarBeneficiario(){
		if (tab_beneficiario.getValor("DOCUMENTO_TITULAR_GEBEN")== null  || tab_beneficiario.getValor("DOCUMENTO_TITULAR_GEBEN").isEmpty()) {
			utilitario.agregarMensajeInfo("No se puede guardar ","Campo documento identidad requerido");	
			return false;
		}
		if(	tab_beneficiario.getValor("IDE_GTTDI").equals(utilitario.getVariable("p_gth_tipo_documento_cedula"))){
			if(!utilitario.validarCedula(tab_beneficiario.getValor("DOCUMENTO_TITULAR_GEBEN"))){
				utilitario.agregarMensajeInfo("No se puede guardar ","Cedula ingresada invalida");	
				return false;
			}
		}

		if(	tab_beneficiario.getValor("IDE_GTTDI").equals(utilitario.getVariable("p_gth_tipo_documento_ruc"))){
			if(!utilitario.validarRUC(tab_beneficiario.getValor("DOCUMENTO_TITULAR_GEBEN"))){
				utilitario.agregarMensajeInfo("No se puede guardar ","Ruc ingresado invalido");	
				return false;
			}
		}

		if (tab_beneficiario.getValor("IDE_GETIC").equals(utilitario.getVariable("p_gen_tipo_contribuyente_p_juridica"))) {
			if (tab_beneficiario.getValor("DOCUMENTO_REPRESENTANTE_GEBEN ")== null  || tab_beneficiario.getValor("DOCUMENTO_REPRESENTANTE_GEBEN ").isEmpty()) {
				utilitario.agregarMensajeInfo("No se puede guardar ","Campo documento identidad requerido");	
				return false;
			}
			if(	tab_beneficiario.getValor("GTH_IDE_GTTDI").equals(utilitario.getVariable("p_gth_tipo_documento_cedula"))){
				if(!utilitario.validarCedula(tab_beneficiario.getValor("DOCUMENTO_REPRESENTANTE_GEBEN "))){
					utilitario.agregarMensajeInfo("No se puede guardar ","Cedula ingresada invalida");	
					return false;
				}
			}
			if(	tab_beneficiario.getValor("GTH_IDE_GTTDI").equals(utilitario.getVariable("p_gth_tipo_documento_ruc"))){
				if(!utilitario.validarRUC(tab_beneficiario.getValor("DOCUMENTO_REPRESENTANTE_GEBEN "))){
					utilitario.agregarMensajeInfo("No se puede guardar ","Ruc ingresado invalido");	
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void guardar() {
		if (validarBeneficiario()) {
			tab_beneficiario.guardar();
			guardarPantalla();
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_beneficiario.eliminar();
	}

}
