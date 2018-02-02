/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class TipoEstados extends Pantalla{

    private Tabla tabTabla = new Tabla();
    
    public TipoEstados() {
        tabTabla.setId("tabTabla");
        tabTabla.setTabla("tes_estado_listado", "ide_estado_listado", 1);
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
