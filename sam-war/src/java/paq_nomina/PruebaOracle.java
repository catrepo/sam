/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class PruebaOracle extends Pantalla {

    private Conexion conOracle = new Conexion();
    private Tabla tabTabla = new Tabla();

    public PruebaOracle() {
        conOracle.setUnidad_persistencia(utilitario.getPropiedad("oraclejdbc"));
        conOracle.NOMBRE_MARCA_BASE = "oracle";
        
        tabTabla.setId("tabTabla");
        tabTabla.setConexion(conOracle);
        tabTabla.setTabla("USFIMRU.PRNUM", "id_prnum", 1);
        tabTabla.setTipoFormulario(true);
        tabTabla.getGrid().setColumns(8);
        tabTabla.dibujar();
        PanelTabla tpd = new PanelTabla();
        tpd.setPanelTabla(tabTabla);
        agregarComponente(tpd);
        
    }

    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Conexion getConOracle() {
        return conOracle;
    }

    public void setConOracle(Conexion conOracle) {
        this.conOracle = conOracle;
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }
    
}
