/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class CuentasContables extends Pantalla {

    private Tabla tabTabla = new Tabla();
    private Arbol arbTabla = new Arbol();
    private Conexion conPostgres = new Conexion();

    public CuentasContables() {

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        tabTabla.setId("tabTabla");
        tabTabla.setConexion(conPostgres);
        tabTabla.setNumeroTabla(1);
        tabTabla.setHeader("Cuentas Para Asientos Automaticos Contables");
        tabTabla.agregarArbol(arbTabla);
        tabTabla.setRows(10);
        tabTabla.dibujar();

        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabTabla);

        arbTabla.setId("arbTabla");
        arbTabla.setConexion(conPostgres);
        arbTabla.dibujar();

        Division div = new Division();
        div.setId("div");
        div.dividir2(arbTabla, pnt, "21%", "V");
        agregarComponente(div);

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

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Arbol getArbTabla() {
        return arbTabla;
    }

    public void setArbTabla(Arbol arbTabla) {
        this.arbTabla = arbTabla;
    }
}
