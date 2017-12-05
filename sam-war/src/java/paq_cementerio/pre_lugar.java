/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class pre_lugar extends Pantalla{

    private Tabla  tabTabla = new Tabla();
    private Tabla tabConsulta = new Tabla();
    
    public pre_lugar() {
        
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();
        
        tabTabla.setId("tabTabla");
        tabTabla.setTabla("cmt_lugar", "ide_lugar", 1);
        
        tabTabla.getColumna("USUARIO_INGRE").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabTabla.getColumna("FECHA_INGRE").setValorDefecto(utilitario.getFechaActual());
        tabTabla.getColumna("HORA_INGRE").setValorDefecto(utilitario.getFechaHoraActual());
        
        tabTabla.getColumna("USUARIO_INGRE").setVisible(false);
        tabTabla.getColumna("FECHA_INGRE").setVisible(false);
        tabTabla.getColumna("HORA_INGRE").setVisible(false);
        tabTabla.getColumna("USUARIO_ACTUA").setVisible(false);
        tabTabla.getColumna("FECHA_ACTUA").setVisible(false);
        tabTabla.getColumna("HORA_ACTUA").setVisible(false);
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
        if(tabTabla.guardar()){
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
