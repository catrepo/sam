/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class pre_inserta_detalle_faltante extends Pantalla {

    private Tabla tabConsulta = new Tabla();
    private Tabla tabCatastro = new Tabla();
    private Tabla tabFallecido = new Tabla();
    private Tabla tabMovimiento = new Tabla();
    private SeleccionTabla setFallecido = new SeleccionTabla();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();

    public pre_inserta_detalle_faltante() {

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("<CENTER>FALLECIDOS - REGISTRO DE MOVIMIENTOS FALTANTES - CEMENTERIO MUNICIPAL </CENTER>");
        agregarComponente(panOpcion);

        dibujarPantalla();
    }

  public void dibujarPantalla() {
        limpiarPanel();
        tabCatastro.setId("tabCatastro");
        tabCatastro.setTabla("cmt_catastro", "ide_catastro", 1);
        if (autBusca.getValue() == null) {
            tabCatastro.setCondicion("ide_catastro=-1");
        } else {
            tabCatastro.setCondicion("ide_catastro=" + autBusca.getValor());
        }
        tabCatastro.getColumna("IDE_LUGAR").setCombo("SELECT IDE_LUGAR,DETALLE_LUGAR FROM CMT_LUGAR");
        tabCatastro.agregarRelacion(tabFallecido);
        tabCatastro.getGrid().setColumns(4);
        tabCatastro.setTipoFormulario(true);
        tabCatastro.dibujar();
        PanelTabla pntCatas = new PanelTabla();
        pntCatas.setPanelTabla(tabCatastro);

        tabFallecido.setId("tabFallecido");
        tabFallecido.setTabla("cmt_fallecido", "ide_fallecido", 2);
        tabFallecido.getColumna("CEDULA_REPRESENTANTE").setVisible(false);
        tabFallecido.setRows(7);
        tabFallecido.dibujar();
        PanelTabla pntFalle = new PanelTabla();
        pntFalle.setPanelTabla(tabFallecido);

        tabMovimiento.setId("tabMovimiento");
        tabMovimiento.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 3);
        tabMovimiento.setCondicion("IDE_DET_MOVIMIENTO=-1");
        tabMovimiento.getColumna("IDE_TIPO_MOVIMIENTO").setCombo("SELECT IDE_TIPO_PAGO, DESCRIPCION FROM dbo.CMT_TIPO_PAGO");
        tabMovimiento.getGrid().setColumns(4);
        tabMovimiento.setTipoFormulario(true);
        tabMovimiento.dibujar();
        PanelTabla pntMovimiento = new PanelTabla();
        pntMovimiento.setPanelTabla(tabMovimiento);

        Division div = new Division();
        div.setId("div");
//        div.dividir3(pntCatas, pntFalle, pntMovimiento, "30%", "43%", "H");
        div.dividir2(pntFalle, pntMovimiento, "23%", "H");
        agregarComponente(div);

        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);

    }

    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    @Override
    public void insertar() {
        tabMovimiento.insertar();
    }

    @Override
    public void guardar() {
        if (tabMovimiento.isFilaInsertada()) {
            if (tabMovimiento.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
