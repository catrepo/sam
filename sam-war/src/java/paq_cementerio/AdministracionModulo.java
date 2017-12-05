/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class AdministracionModulo extends Pantalla {

    private Tabla tabModulo = new Tabla();

    public AdministracionModulo() {

        tabModulo.setId("tabModulo");
        tabModulo.setTabla("CMT_MODULO", "IDXBLOQUE", 1);
        tabModulo.getColumna("IDXBLOQUE").setVisible(false);
//        tabModulo.getColumna("sector").setCombo("select ide_lugar,detalle_lugar from CMT_LUGAR");
//        tabModulo.getColumna("sector").setMetodoChange("cargaModulo");
//        tabModulo.getColumna("modulo").setCombo("select  DISTINCT modulo as id, modulo from CMT_CATASTRO");
        tabModulo.setRows(10);
        tabModulo.dibujar();

        PanelTabla pntModulo = new PanelTabla();
        pntModulo.setMensajeWarn("CEMENTERIO MUNICIPAL - ADMINISTRACIÓN DE MAPA");
        pntModulo.setPanelTabla(tabModulo);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pntModulo);
        agregarComponente(div_division);

    }

    public void cargaModulo() {
        tabModulo.getColumna("modulo").setCombo("select  DISTINCT modulo as id, modulo from CMT_CATASTRO where ide_lugar = "+tabModulo.getValor("sector"));
        utilitario.addUpdateTabla(tabModulo, "modulo", "");//actualiza solo componentes
    }

    @Override
    public void insertar() {
        tabModulo.insertar();
    }

    @Override
    public void guardar() {
        if (tabModulo.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tabModulo.eliminar();
    }

    public Tabla getTabModulo() {
        return tabModulo;
    }

    public void setTabModulo(Tabla tabModulo) {
        this.tabModulo = tabModulo;
    }
}
