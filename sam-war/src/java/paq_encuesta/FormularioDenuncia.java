/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_encuesta;

import framework.componentes.AutoCompletar;
import framework.componentes.Division;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.ws.WebServiceRef;
import sistema.aplicacion.Pantalla;
import paq_webservice.ClassCiudadania;
import paq_webservice.ConsultaCiudadano;

/**
 *
 * @author KEJA
 */
public class FormularioDenuncia extends Pantalla {

    private Tabla tabFormulario = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();
    
// @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/186.46.84.37/ConsultaCiudadania/ciudadania.asmx.wsdl")
//    private ConsultaCiudadano service;
    public FormularioDenuncia() {
        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT id_formulario,cedula,nombre FROM resug_formulario order by fecha");
        autBusca.setSize(80);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("Ingreso de Denuncias/Reclamos");
        agregarComponente(panOpcion);

        dibujarPantalla();
    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabFormulario.setId("tabFormulario");
        tabFormulario.setTabla("resug_formulario", "id_formulario", 1);
        if (autBusca.getValue() == null) {
            tabFormulario.setCondicion("id_formulario=-1");
        } else {
            tabFormulario.setCondicion("id_formulario=" + autBusca.getValor());
        }
        tabFormulario.getColumna("cedula").setMetodoChange("buscarCiudadano");
        List list = new ArrayList();
        Object fil1[] = {
            "DNU", "Denuncia"
        };
        Object fil2[] = {
            "RSU", "Reclamo"
        };
        list.add(fil1);;
        list.add(fil2);;
        tabFormulario.getColumna("tipo").setCombo(list);
        tabFormulario.getColumna("adjunto").setUpload("formulario");
        tabFormulario.getColumna("login").setValorDefecto(utilitario.getVariable("NICK"));
        tabFormulario.getColumna("estado").setValorDefecto("6");
        tabFormulario.getColumna("direccion").setLectura(true);
        tabFormulario.getColumna("estado").setVisible(false);
        tabFormulario.getColumna("fecha").setVisible(false);
        tabFormulario.getColumna("login").setVisible(false);
        tabFormulario.getColumna("ver").setVisible(false);
        tabFormulario.getColumna("ide_responsable").setVisible(false);
        tabFormulario.getColumna("login_actu").setVisible(false);
        tabFormulario.getColumna("nombre").setLongitud(50);
        tabFormulario.getColumna("observacion").setLongitud(550);
        tabFormulario.setTipoFormulario(true);
        tabFormulario.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabFormulario);

        Division div = new Division();
        div.setId("div");
        div.dividir1(pnt);

        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);
    }

    public void buscarCiudadano() {
        if (utilitario.validarCedula(tabFormulario.getValor("cedula"))) {
            String usu = "reclamos";
            String pass = "buzon2016$";
            String cedula = tabFormulario.getValor("cedula");

            paq_webservice.ConsultaCiudadano service = new paq_webservice.ConsultaCiudadano();
            ClassCiudadania persona = service.getConsultaCiudadanoSoap().busquedaPorCedula(cedula, usu, pass);
                
            tabFormulario.setValor("nombre", persona.getNombre());
            tabFormulario.setValor("direccion", persona.getDireccion());
            tabFormulario.setValor("telefono", persona.getTelefono());
            tabFormulario.setValor("celular", persona.getTelefonoTrabajo());
            tabFormulario.setValor("mail", persona.getMail());
            utilitario.addUpdate("tabFormulario");

        } else {
            utilitario.agregarMensaje("Número de Cédula Incorrecto", null);
        }
    }

    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    @Override
    public void insertar() {
        tabFormulario.insertar();
    }

    @Override
    public void guardar() {
        if (utilitario.validarCedula(tabFormulario.getValor("cedula"))) {
            if (tabFormulario.guardar()) {
                guardarPantalla();
            }
        } else {
            utilitario.agregarMensaje("Cedula Incorrecta", "No se puede guardar");
        }
    }

    @Override
    public void eliminar() {
        tabFormulario.eliminar();
    }

    public Tabla getTabFormulario() {
        return tabFormulario;
    }

    public void setTabFormulario(Tabla tabFormulario) {
        this.tabFormulario = tabFormulario;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

//    private static ClassCiudadania busquedaCedula(java.lang.String cedula, java.lang.String usuario, java.lang.String password) {
//        paq_webservice.ConsultaCiudadano service = new paq_webservice.ConsultaCiudadano();
//        paq_webservice.ConsultaCiudadanoSoap port = service.getConsultaCiudadanoSoap12();
//        return port.busquedaPorCedula(cedula, usuario, password);
//    }
//     private ClassCiudadania busquedaPorCedula(java.lang.String cedula, java.lang.String usuario, java.lang.String password) {
//        paq_webservice.ConsultaCiudadanoSoap port = service.getConsultaCiudadanoSoap12();
//        return port.busquedaPorCedula(cedula, usuario, password);
//    }
}
