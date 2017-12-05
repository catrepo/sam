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
     * Variable que permite conectar a base de datos diferente
     */

    private Conexion conPostgres = new Conexion();
    /*
     * Declaración de Tablas, para el formulario a utilizar
     */
    private Tabla tabTabla = new Tabla();

    public TipoDocumento() {
        /*
         * Cadena de conexión para otra base de datos
         */
        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        /*
         * formulario con ordenes de pago
         */
        tabTabla.setId("tabTabla");
        tabTabla.setConexion(conPostgres);
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
        conPostgres.guardarPantalla();
    }

    @Override
    public void eliminar() {
        tabTabla.eliminar();
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }
}
