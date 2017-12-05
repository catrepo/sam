/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_varios;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class OrientacionGasto extends Pantalla {

    private Tabla tabRegistro = new Tabla();
    private Arbol arbArbol = new Arbol();

    public OrientacionGasto() {
        tabRegistro.setId("tabRegistro");
        tabRegistro.setNumeroTabla(1);
        tabRegistro.agregarArbol(arbArbol);
        tabRegistro.dibujar();
        PanelTabla patreg = new PanelTabla();
        patreg.setMensajeWarn("Clasficador de Orientación del Gasto");
        patreg.setPanelTabla(tabRegistro);

        arbArbol.setId("arbArbol");
        arbArbol.dibujar();

        Division div = new Division();
        div.setId("div");
        div.dividir2(arbArbol, patreg, "20%", "V");
        agregarComponente(div);
    }

    @Override
    public void insertar() {
        tabRegistro.insertar();
    }

    @Override
    public void guardar() {
        if (tabRegistro.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tabRegistro.eliminar();
    }

    public Tabla getTabRegistro() {
        return tabRegistro;
    }

    public void setTabRegistro(Tabla tabRegistro) {
        this.tabRegistro = tabRegistro;
    }

    public Arbol getArbArbol() {
        return arbArbol;
    }

    public void setArbArbol(Arbol arbArbol) {
        this.arbArbol = arbArbol;
    }
}
