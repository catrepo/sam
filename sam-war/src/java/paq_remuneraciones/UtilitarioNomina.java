/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_remuneraciones;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_remuneraciones.ejb.BeanRemuneracion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class UtilitarioNomina extends Pantalla {

    private Tabla tabTabla = new Tabla();
    private Combo comboAcciones = new Combo();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private BeanRemuneracion adminRemuneracion = (BeanRemuneracion) utilitario.instanciarEJB(BeanRemuneracion.class);

    public UtilitarioNomina() {

        comboAcciones.setId("comboAcciones");
        List list = new ArrayList();
        Object filas1[] = {
            "1", "INFORMACIÓN"
        };

        Object filas2[] = {
            "2", "BORRAR"
        };
        list.add(filas1);;
        list.add(filas2);;
        comboAcciones.setCombo(list);
        bar_botones.agregarComponente(new Etiqueta("Acción : "));
        bar_botones.agregarComponente(comboAcciones);

        Boton botProceso = new Boton();
        botProceso.setId("botProceso");
        botProceso.setValue("PROCESAR");
        botProceso.setIcon("ui-icon-print");
        botProceso.setMetodo("procesoDatos");
        bar_botones.agregarBoton(botProceso);

        tabTabla.setId("tabTabla");
        tabTabla.setTabla("nom_actividades", "id_codigo", 1);
        tabTabla.getColumna("cedula").setFiltro(true);
        tabTabla.getColumna("nombre").setFiltro(true);
        tabTabla.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabTabla);
        agregarComponente(pnt);

        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);

    }

    public void procesoDatos() {
        if (comboAcciones.getValue().equals("1")) {
            actualizaDatos();
        } else {
            limpiaPantalla();
        }
    }

    public void actualizaDatos() {
        for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
            TablaGenerica tabValidar = adminRemuneracion.getVerificaDatos(tabTabla.getValor(i, "cedula"));
            if (!tabValidar.isEmpty()) {
                adminRemuneracion.setActualizaActividad(tabTabla.getValor(i, "cedula"), tabValidar.getValor("tipctt"));
            } else {
                utilitario.agregarMensaje(tabTabla.getValor(i, "nombre") + " No se encuentra", null);
            }
        }
    }

    public void limpiaPantalla() {
        adminRemuneracion.deleteActividades();
    }

    @Override
    public void insertar() {
        tabTabla.insertar();
    }

    @Override
    public void guardar() {
        if (tabTabla.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tabTabla.eliminar();
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "LISTADO DE DESCUENTO":
                aceptoAprobacion();
                break;

        }
    }

    public void aceptoAprobacion() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "LISTADO DE DESCUENTO":
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;

        }
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }
}
