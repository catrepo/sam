/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_varios;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class DatosCorreo extends Pantalla {

    private Tabla tabTabla = new Tabla();

    public DatosCorreo() {
        tabTabla.setId("tabTabla");
        tabTabla.setTabla("sis_correo", "ide_corr", 0);
        tabTabla.setRows(10);
        tabTabla.setHeader("Datos para configuraciòn de cuntas de correo");
        tabTabla.dibujar();

        PanelTabla pntMail = new PanelTabla();
        pntMail.setPanelTabla(tabTabla);

        Division div = new Division();
        div.setId("div");
        div.dividir1(pntMail);
        agregarComponente(div);
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

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }
}
