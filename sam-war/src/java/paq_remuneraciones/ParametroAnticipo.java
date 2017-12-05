/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_remuneraciones;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class ParametroAnticipo extends Pantalla {

    private Tabla tabTabla = new Tabla();

    public ParametroAnticipo() {
        tabTabla.setId("tabTabla");
        tabTabla.setTabla("nom_parametros", "id_parametro", 1);
        tabTabla.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabTabla);
        agregarComponente(pnt);
    }

    @Override
    public void insertar() {
        tabTabla.insertar();
    }

    @Override
    public void guardar() {
        if (tabTabla.guardar()) {
            guardarPantalla();
        } //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        tabTabla.eliminar();
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }
}
