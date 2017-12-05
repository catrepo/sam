/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_varios;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import sistema.aplicacion.Pantalla;
import paq_utilitario.ejb.ClaseGenerica;

/**
 *
 * @author p-chumana
 */
public class AccesoSistema extends Pantalla {

    //Tablas
    private Tabla tabSolicitud = new Tabla();
    private List<SelectItem> listaDatos;
//    private Tabla tabConsulta = new Tabla();
//    private SeleccionTabla setTabla = new SeleccionTabla();
//    //buscar solicitud
//    private AutoCompletar autBusca = new AutoCompletar();
//    //Contiene todos los elementos de la plantilla
//    private Panel panOpcion = new Panel();
    @EJB
    private ClaseGenerica lista = (ClaseGenerica) utilitario.instanciarEJB(ClaseGenerica.class);

    public AccesoSistema() {
        tabSolicitud.setId("tabSolicitud");
        tabSolicitud.setTabla("sca_solicitud_acceso", "id_solicitud_acceso", 1);
        tabSolicitud.getColumna("cedula_solicitante").setMetodoChange("busca");
        tabSolicitud.setTipoFormulario(true);
        tabSolicitud.dibujar();
        PanelTabla tps = new PanelTabla();
        tps.setPanelTabla(tabSolicitud);
        agregarComponente(tps);
    }

    public void busca() {
        if (utilitario.validarCedula(tabSolicitud.getValor("cedula_solicitante"))) {
            if (lista.buscaCiudadano(tabSolicitud.getValor("cedula_solicitante")).size() != 0) {
                Iterator datos = lista.buscaCiudadano(tabSolicitud.getValor("cedula_solicitante")).iterator();
                while (datos.hasNext()) {
                    Object object = (Object)datos.next();
                }
//                Object[] valor = lista.buscaCiudadano(tabSolicitud.getValor("cedula_solicitante")).toArray();
//                for (Object val : valor) {
//                    System.out.println(val.toString()); 
//                }
                for (int i = 0; i < lista.buscaCiudadano(tabSolicitud.getValor("cedula_solicitante")).size(); i++) {
                    System.err.println(lista.buscaCiudadano(tabSolicitud.getValor("cedula_solicitante")).get(i));
                }
            } else {
                utilitario.agregarMensaje("Datos no encontrados", null);
            }
        } else {
            utilitario.agregarMensaje("Cedula Incorrecta", null);
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTabSolicitud() {
        return tabSolicitud;
    }

    public void setTabSolicitud(Tabla tabSolicitud) {
        this.tabSolicitud = tabSolicitud;
    }
}
