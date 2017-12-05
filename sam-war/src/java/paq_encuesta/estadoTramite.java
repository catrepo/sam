/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_encuesta;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class estadoTramite extends Pantalla {

    private Tabla tabEstado = new Tabla();

    public estadoTramite() {

        tabEstado.setId("tabEstado");
        tabEstado.setTabla("resug_estado", "id_estado", 1);
        tabEstado.dibujar();
        PanelTabla patreg = new PanelTabla();
        patreg.setMensajeWarn("Información de Estados y Actividades de Tramite");
        patreg.setPanelTabla(tabEstado);
        agregarComponente(patreg);
    }

    @Override
    public void insertar() {
        tabEstado.insertar();
    }

    @Override
    public void guardar() {
        if (tabEstado.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tabEstado.eliminar();
    }

    public Tabla getTabEstado() {
        return tabEstado;
    }

    public void setTabEstado(Tabla tabEstado) {
        this.tabEstado = tabEstado;
    }
}
