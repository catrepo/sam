/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class TipoDocumento extends Pantalla {
    /*
     * Declaración de Tablas, para el formulario a utilizar
     */
    private Tabla tabTabla = new Tabla();

    public TipoDocumento() {

        /*
         * formulario con ordenes de pago
         */
        tabTabla.setId("tabTabla");
        tabTabla.setTabla("tes_tipo_documento", "id_tipo", 1);
        tabTabla.setRows(14);
        tabTabla.dibujar();
        PanelTabla pto = new PanelTabla();
        pto.setPanelTabla(tabTabla);
        agregarComponente(pto);
    }

    @Override
    public void insertar() {
        tabTabla.insertar();
    }

    @Override
    public void guardar() {
        tabTabla.guardar();
        guardarPantalla();
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
