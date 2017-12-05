/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_encuesta;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author KEJA
 */
public class DocumentoFinalizado extends Pantalla {

    private Tabla tabDenuncia = new Tabla();
    private Tabla tabMovimiento = new Tabla();
    private Panel panOpcion = new Panel();
    private Calendario fecha1 = new Calendario();
    private Calendario fecha2 = new Calendario();
    private Combo cmbDireccion = new Combo();
    private Combo cmbEstado = new Combo();
    private Combo cmbOpcion = new Combo();
    private AutoCompletar autBusca = new AutoCompletar();

    public DocumentoFinalizado() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();

        cmbDireccion.setId("cmbDireccion");
        cmbDireccion.setCombo("SELECT ID_CODIGO,DESCRIPCION FROM RESUG_AREA_RESPON where ide_codigo is null order by descripcion");

        cmbEstado.setId("cmbEstado");
        cmbEstado.setCombo("SELECT ID_ESTADO,ESTADO FROM RESUG_ESTADO where ID_ESTADO in (9)");

        List list = new ArrayList();
        Object fila1[] = {
            "0", "Dirección"
        };
        Object fila2[] = {
            "1", "Ran. Fechas"
        };
        list.add(fila1);;
        list.add(fila2);;
        cmbOpcion.setCombo(list);

        Grid griPri = new Grid();
        griPri.setColumns(3);

        Grid griPan = new Grid();
        griPan.setColumns(2);
        griPan.getChildren().add(new Etiqueta("Buscar por :"));
        griPan.getChildren().add(cmbOpcion);
        griPri.getChildren().add(griPan);

        Grid griSear = new Grid();
        griSear.setColumns(2);
        
        Grid griComp = new Grid();
        griComp.setColumns(4);
        
        griComp.getChildren().add(new Etiqueta("Direccion: "));
        griComp.getChildren().add(cmbDireccion);

        Grid griDate = new Grid();
        griDate.setColumns(4);
        griDate.getChildren().add(new Etiqueta("Fecha Inicial: "));
        fecha1.setSize(11);
        griDate.getChildren().add(fecha1);
        griDate.getChildren().add(new Etiqueta("Fecha Final: "));
        fecha1.setSize(11);
        griDate.getChildren().add(fecha2);
        griDate.getChildren().add(fecha2);
        
        Grid griEsta = new Grid();
        griEsta.setColumns(2);
        griEsta.getChildren().add(new Etiqueta("Estado: "));
        griEsta.getChildren().add(cmbEstado);
        griComp.getChildren().add(griEsta);
        griSear.getChildren().add(griDate);
        griSear.getChildren().add(griComp);

        griPri.getChildren().add(griSear);

        Grid griBot = new Grid();
        griBot.setColumns(2);
        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar ");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("busquedaInfo");
        griBot.getChildren().add(botBuscar);

        griPri.getChildren().add(griBot);
        bar_botones.agregarComponente(griPri);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("REPORTE GENERAL DE RECLAMO/S");
        agregarComponente(panOpcion);

        dibujarPantalla();
    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabDenuncia.setId("tabDenuncia");
        tabDenuncia.setTabla("RESUG_FORMULARIO", "ID_FORMULARIO", 1);
        if (autBusca.getValue() == null) {
            tabDenuncia.setCondicion("ID_FORMULARIO=-1");
        } else {
            tabDenuncia.setCondicion("ID_FORMULARIO in (" + autBusca.getValor() + ")");
        }
        tabDenuncia.getColumna("estado").setCombo("SELECT ID_ESTADO,ESTADO FROM RESUG_ESTADO");
        tabDenuncia.getColumna("ide_responsable").setCombo("SELECT ID_CODIGO,DESCRIPCION FROM RESUG_AREA_RESPON");
        List list = new ArrayList();
        Object fil1[] = {
            "DNU", "DENUNCIA "
        };
        Object fil2[] = {
            "RSU", "RECLAMO "
        };
        list.add(fil1);;
        list.add(fil2);;
        tabDenuncia.getColumna("tipo").setCombo(list);
        tabDenuncia.getColumna("login").setFiltro(true);
        tabDenuncia.getColumna("fecha1").setFiltro(true);
        tabDenuncia.getColumna("cedula").setFiltro(true);
        tabDenuncia.getColumna("nombre").setFiltro(true);
        tabDenuncia.getColumna("celular").setFiltro(true);
        tabDenuncia.getColumna("telefono").setFiltro(true);
        tabDenuncia.getColumna("observacion").setFiltro(true);
        tabDenuncia.getColumna("ide_responsable").setFiltro(true);
        tabDenuncia.getColumna("id_formulario").setFiltro(true);
        tabDenuncia.getColumna("ver").setVisible(false);
        tabDenuncia.getColumna("clave").setVisible(false);
        tabDenuncia.getColumna("adjunto").setVisible(false);
        tabDenuncia.getColumna("direccion").setVisible(false);
        tabDenuncia.getColumna("direccion1").setVisible(false);
        tabDenuncia.getColumna("login_actu").setVisible(false);
        tabDenuncia.getColumna("mail").setLongitud(30);
        tabDenuncia.getColumna("nombre").setLongitud(50);
        tabDenuncia.getColumna("observacion").setLongitud(70);
        tabDenuncia.setRows(10);
        tabDenuncia.agregarRelacion(tabMovimiento);
        tabDenuncia.setLectura(true);
        tabDenuncia.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabDenuncia);

        tabMovimiento.setId("tabMovimiento");
        tabMovimiento.setTabla("resug_actividades", "id_actividad", 2);
        tabMovimiento.getColumna("tipo_actividad").setCombo("SELECT ID_ESTADO,ESTADO FROM RESUG_ESTADO");
        tabMovimiento.getColumna("uni_responsable").setCombo("SELECT ID_CODIGO,DESCRIPCION FROM RESUG_AREA_RESPON");
        tabMovimiento.getColumna("usu_adjunto").setUpload("formulario");
        tabMovimiento.getColumna("usu_adjunto").setImagen("", "");
        tabMovimiento.getColumna("usu_asignacion").setVisible(false);
        tabMovimiento.getColumna("usu_responsable").setVisible(false);
        tabMovimiento.setRows(10);
        tabMovimiento.setLectura(true);
        tabMovimiento.dibujar();

        PanelTabla pnr = new PanelTabla();
        pnr.setPanelTabla(tabMovimiento);

        Division div = new Division();
        div.setId("div");
        div.dividir2(pnt, pnr, "60%", "h");

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

    /*
     * Metodo para busqueda
     */
    public void busquedaInfo() {
        if (cmbOpcion.getValue().equals("0")) {
            System.err.println("ho");
            buscaCedula();
        } else if (cmbOpcion.getValue().equals("1")) {
            buscaNumero();
        }
    }

    /*
     * Opciones de busqueda
     */
    public void buscaNumero() {
        if (!getBuscaNumero().isEmpty()) {
            tabDenuncia.setCondicion(getBuscaNumero());
            tabDenuncia.ejecutarSql();
            utilitario.addUpdate("tabDenuncia");
        }
    }

    private String getBuscaNumero() {
        String str_filtros = "";
        str_filtros = "fecha between '" + fecha1.getFecha() + "' and '" + fecha2.getFecha() + "' and IDE_RESPONSABLE = " + Integer.parseInt(cmbDireccion.getValue() + "") + "";
        return str_filtros;
    }

    public void buscaCedula() {
        if (!getBuscaCedula().isEmpty()) {
            tabDenuncia.setCondicion(getBuscaCedula());
            tabDenuncia.ejecutarSql();
            utilitario.addUpdate("tabDenuncia");
        }
    }

    private String getBuscaCedula() {
        String str_filtros = "";
        str_filtros = "IDE_RESPONSABLE = " + Integer.parseInt(cmbDireccion.getValue() + "") + " and estado = " + Integer.parseInt(cmbEstado.getValue() + "") + "";
        return str_filtros;
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

    public Tabla getTabDenuncia() {
        return tabDenuncia;
    }

    public void setTabDenuncia(Tabla tabDenuncia) {
        this.tabDenuncia = tabDenuncia;
    }

    public Tabla getTabMovimiento() {
        return tabMovimiento;
    }

    public void setTabMovimiento(Tabla tabMovimiento) {
        this.tabMovimiento = tabMovimiento;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }
}
