/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.ejb.EJB;
import org.primefaces.component.panel.Panel;
import paq_cementerio.ejb.cementerio;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class pre_generacion_catastro extends Pantalla {

    private Tabla tabTipo = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Dialogo diaDatos = new Dialogo();
    private Dialogo diaInfor = new Dialogo();
    private Dialogo diaDelete = new Dialogo();
    private Dialogo diaData = new Dialogo();
    private Grid grid = new Grid();
    private Grid grida = new Grid();
    private Grid gride = new Grid();
    private Grid gridE = new Grid();
    private Grid gridi = new Grid();
    private Etiqueta etiSector = new Etiqueta("<B>Sector : </B>");
    private Etiqueta etiModulo = new Etiqueta("<B>Modulo : </B>");
    private Etiqueta lbDesde = new Etiqueta("<B>Desde : </B>");
    private Etiqueta lbHasta = new Etiqueta("<B>Hasta : </B>");
    private Etiqueta lbLugar = new Etiqueta("<B>Lugar : </B>");
    private Etiqueta lbLugarC = new Etiqueta("<B>Lugar : </B>");
    private Etiqueta lbSector = new Etiqueta("<B>Sector : </B>");
    private Etiqueta lbModulo = new Etiqueta("<B>Modulo : </B>");
    private Etiqueta lbMausoleo = new Etiqueta("<B>Mausoleo : </B>");
    private Etiqueta lbTotal = new Etiqueta("<B>Total Lugares : </B>");
    private Etiqueta lbInforma = new Etiqueta("<center><B>¿ SEGURO DE GENERAR LUGARES ? </B></center>");
    private Etiqueta lbData = new Etiqueta("<center><B>¿ SEGURO DE ELIMINAR LUGARES ? </B></center>");
    private Etiqueta lbTotSector = new Etiqueta();
    private Etiqueta lbTotModulo = new Etiqueta();
    private Etiqueta lbTotLugar = new Etiqueta();
    private Etiqueta lbTotGen = new Etiqueta();
    private Etiqueta lbTotMausoleon = new Etiqueta();
    private Texto txtSector = new Texto();
    private Texto txtModulo = new Texto();
    private Texto txtHasta = new Texto();
    private Texto txtDesde = new Texto();
    private Texto txtMausoleo = new Texto();
    private Panel panSitios = new Panel();
    private Panel panPanel = new Panel();
    private Panel panRango = new Panel();
    private Panel panMausoleo = new Panel();
    private Combo cmbLugar = new Combo();
    private Combo cmbLugarD = new Combo();
    Integer valinicio, valfinal;
    @EJB
    private cementerio adminCmt = (cementerio) utilitario.instanciarEJB(cementerio.class);

    public pre_generacion_catastro() {
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        bar_botones.agregarComponente(new Etiqueta("<B>LUGAR : </B>"));
        cmbLugar.setId("cmbLugar");
        cmbLugar.setCombo("SELECT IDE_LUGAR,DETALLE_LUGAR FROM CMT_LUGAR order by DETALLE_LUGAR");
        bar_botones.agregarComponente(cmbLugar);

        Boton botVer = new Boton();
        botVer.setValue("Ver");
        botVer.setIcon("ui-icon-search");
        botVer.setMetodo("filtraLista");
        bar_botones.agregarBoton(botVer);

        bar_botones.agregarSeparador();

        Boton botCrear = new Boton();
        botCrear.setValue("Ingresar Grupo");
        botCrear.setIcon("ui-icon-search");
        botCrear.setMetodo("abrirVentana");
        bar_botones.agregarBoton(botCrear);

        Boton botEliminar = new Boton();
        botEliminar.setValue("Eliminar Grupo");
        botEliminar.setIcon("ui-icon-search");
        botEliminar.setMetodo("openVentana");
        bar_botones.agregarBoton(botEliminar);

        cmbLugarD.setId("cmbLugarD");
        cmbLugarD.setCombo("SELECT IDE_LUGAR,DETALLE_LUGAR FROM CMT_LUGAR order by DETALLE_LUGAR");

        diaDatos.setId("diaDatos");
        diaDatos.setTitle("<center>Generar sitios,sectores y números</center>"); //titulo
        diaDatos.setWidth("30%"); //siempre en porcentajes  ancho
        diaDatos.setHeight("45%");//siempre porcentaje   alto
        diaDatos.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDatos.getBot_aceptar().setMetodo("inforDatos");
        grid.setColumns(4);
        agregarComponente(diaDatos);

        diaData.setId("diaData");
        diaData.setTitle("<center>Eliminar sitios,sectores y números</center>"); //titulo
        diaData.setWidth("30%"); //siempre en porcentajes  ancho
        diaData.setHeight("35%");//siempre porcentaje   alto
        diaData.setResizable(false); //para que no se pueda cambiar el tamaño
        diaData.getBot_aceptar().setMetodo("elimaVentana");
        grida.setColumns(4);
        agregarComponente(diaData);

        panPanel.setId("panPanel");
        panPanel.setStyle("width: 350px;");

        panSitios.setId("panSitios");
        panSitios.setStyle("width: 350px;");

        panRango.setId("panRango");
        panRango.setStyle("width: 350px;");
        panRango.setHeader("Ingreso Rango de Números");

        panMausoleo.setId("panMausoleo");
        panMausoleo.setStyle("width: 350px;");
        panMausoleo.setHeader("Nombre de Mausoleo");

        diaInfor.setId("diaInfor");
        diaInfor.setWidth("30%"); //siempre en porcentajes  ancho
        diaInfor.setHeight("30%");//siempre porcentaje   alto
        diaInfor.setResizable(false); //para que no se pueda cambiar el tamaño
        diaInfor.getBot_aceptar().setMetodo("generaRegistros");
        gridi.setColumns(4);
        agregarComponente(diaInfor);

        diaDelete.setId("diaDelete");
        diaDelete.setWidth("30%"); //siempre en porcentajes  ancho
        diaDelete.setHeight("30%");//siempre porcentaje   alto
        diaDelete.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDelete.getBot_aceptar().setMetodo("elimaRegistros");
        gride.setColumns(4);
        agregarComponente(diaDelete);

        tabTipo.setId("tabTipo");
        tabTipo.setTabla("cmt_catastro", "ide_catastro", 1);
        tabTipo.getColumna("ide_lugar").setCombo("SELECT IDE_LUGAR, DETALLE_LUGAR FROM CMT_LUGAR");
        tabTipo.getColumna("sector").setFiltro(true);
        tabTipo.getColumna("modulo").setFiltro(true);
        tabTipo.getColumna("numero").setFiltro(true);

        tabTipo.getColumna("ver").setVisible(false);
        tabTipo.getColumna("catastro_anterior").setLectura(true);
        tabTipo.getColumna("total_ingresa").setVisible(false);
        tabTipo.getColumna("habilita").setVisible(false);
        tabTipo.getColumna("pagado").setVisible(false);
        tabTipo.getColumna("fec_desde").setVisible(false);
        tabTipo.getColumna("fec_hasta").setVisible(false);

        tabTipo.setRows(30);
        tabTipo.dibujar();
        PanelTabla patPanel = new PanelTabla();
        patPanel.setPanelTabla(tabTipo);


        Division div = new Division();
        div.setId("div");
        div.dividir1(patPanel);
        agregarComponente(div);

        filtraDatos();
    }

    public void abrirVentana() {
        diaDatos.Limpiar();
        txtSector.setSize(5);
        txtModulo.setSize(5);
        txtDesde.setSize(5);
        txtHasta.setSize(5);

        Grid griPanel = new Grid();
        griPanel.setColumns(1);

        Grid griLugar = new Grid();
        griLugar.setColumns(2);
        griLugar.getChildren().add(lbLugar);
        griLugar.getChildren().add(cmbLugarD);
        panPanel.getChildren().add(griLugar);

        Grid griSitios = new Grid();
        griSitios.setColumns(4);
        griSitios.getChildren().add(etiSector);
        griSitios.getChildren().add(txtSector);
        griSitios.getChildren().add(etiModulo);
        griSitios.getChildren().add(txtModulo);
        panSitios.getChildren().add(griSitios);

        Grid griRango = new Grid();
        griRango.setColumns(4);
        griRango.getChildren().add(lbDesde);
        griRango.getChildren().add(txtDesde);
        griRango.getChildren().add(lbHasta);
        griRango.getChildren().add(txtHasta);
        panRango.getChildren().add(griRango);

        Grid griMausoleo = new Grid();
        griMausoleo.setColumns(4);
        griMausoleo.getChildren().add(lbMausoleo);
        griMausoleo.getChildren().add(txtMausoleo);
        panMausoleo.getChildren().add(griMausoleo);

        griPanel.getChildren().add(panPanel);
        griPanel.getChildren().add(panSitios);
        griPanel.getChildren().add(panRango);
        griPanel.getChildren().add(panMausoleo);

        diaDatos.setDialogo(grid);
        grid.getChildren().add(griPanel);
        diaDatos.dibujar();
    }

    public void openVentana() {
        diaData.Limpiar();
        txtSector.setSize(5);
        txtModulo.setSize(5);
        txtDesde.setSize(5);
        txtHasta.setSize(5);

        Grid griPanel = new Grid();
        griPanel.setColumns(1);

        Grid griLugar = new Grid();
        griLugar.setColumns(2);
        griLugar.getChildren().add(lbLugar);
        griLugar.getChildren().add(cmbLugarD);
        panPanel.getChildren().add(griLugar);

        Grid griSitios = new Grid();
        griSitios.setColumns(4);
        griSitios.getChildren().add(etiSector);
        griSitios.getChildren().add(txtSector);
        griSitios.getChildren().add(etiModulo);
        griSitios.getChildren().add(txtModulo);
        panSitios.getChildren().add(griSitios);

        Grid griRango = new Grid();
        griRango.setColumns(4);
        griRango.getChildren().add(lbDesde);
        griRango.getChildren().add(txtDesde);
        griRango.getChildren().add(lbHasta);
        griRango.getChildren().add(txtHasta);
        panRango.getChildren().add(griRango);

        griPanel.getChildren().add(panPanel);
        griPanel.getChildren().add(panSitios);
        griPanel.getChildren().add(panRango);

        diaData.setDialogo(grida);
        grida.getChildren().add(griPanel);
        diaData.dibujar();
    }

    public void inforDatos() {
        lbTotLugar.clearInitialState();
        lbTotSector.clearInitialState();
        lbTotModulo.clearInitialState();
        lbTotGen.clearInitialState();
        diaInfor.Limpiar();
        if (cmbLugarD.getValue() != null && txtSector.getValue() != null && txtModulo.getValue() != null && txtDesde.getValue() != null
                && txtHasta.getValue() != null && txtSector.getValue() != "" && txtModulo.getValue() != "" && txtDesde.getValue() != ""
                && txtHasta.getValue() != "") {
            TablaGenerica tabDatos = adminCmt.getRecuperaLugar(Integer.parseInt(cmbLugarD.getValue() + ""));
            if (!tabDatos.isEmpty()) {
                lbTotLugar.setValue(tabDatos.getValor("DETALLE_LUGAR"));
                lbTotSector.setValue(txtSector.getValue());
                lbTotModulo.setValue(txtModulo.getValue());
                lbTotMausoleon.setValue(txtMausoleo.getValue());
                lbTotGen.setValue(totalPuestos(Integer.parseInt(txtDesde.getValue() + "")));
                diaInfor.Limpiar();
                Grid griPanel = new Grid();
                griPanel.setColumns(1);
                diaInfor.setDialogo(gridi);
                Grid griTotal = new Grid();
                griTotal.setColumns(2);
                griTotal.getChildren().add(lbLugarC);
                griTotal.getChildren().add(lbTotLugar);
                griTotal.getChildren().add(lbSector);
                griTotal.getChildren().add(lbTotSector);
                griTotal.getChildren().add(lbModulo);
                griTotal.getChildren().add(lbTotModulo);
                griTotal.getChildren().add(lbTotal);
                griTotal.getChildren().add(lbTotGen);
                griPanel.getChildren().add(lbInforma);
                griPanel.getChildren().add(lbTotMausoleon);
                griPanel.getChildren().add(griTotal);
                gridi.getChildren().add(griPanel);
                diaInfor.dibujar();
            } else {
                utilitario.agregarMensajeInfo("No se encuentra registro", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Todos los campos deben esta llenos", "");
        }
    }

    public Integer totalPuestos(Integer valor) {
        Integer total = 0;
        if (valor == 1) {
            total = Integer.parseInt(txtHasta.getValue() + "");
        } else {
            total = (Integer.parseInt(txtHasta.getValue() + "") + 1) - Integer.parseInt(txtDesde.getValue() + "");
        }
        return total;
    }

    public void generaRegistros() {
        int valor = adminCmt.getVerificar_sitios(Integer.parseInt(cmbLugarD.getValue() + ""), txtSector.getValue() + "", txtModulo.getValue() + "", txtDesde.getValue() + "", txtHasta.getValue() + "");
        if (valor == 0) {
            try {
                diaInfor.cerrar();
                valinicio = Integer.parseInt(txtDesde.getValue() + "");
                valfinal = Integer.parseInt(txtHasta.getValue() + "");
                for (int i = valinicio; i <= valfinal; i++) {
                    adminCmt.setInserCatastro_SP("'" + txtSector.getValue() + "'", "'" + txtModulo.getValue() + "'", String.valueOf(i), null, Integer.parseInt(cmbLugarD.getValue() + ""), txtMausoleo.getValue() + "", "'" + tabConsulta.getValor("NICK_USUA") + "'");
                }
                utilitario.agregarMensajeInfo("Registros Guardados", null);
            } catch (Exception e) {
                utilitario.agregarMensajeInfo("Error", e.getMessage());
            }
        } else {
            utilitario.agregarMensaje("Existen Lugares ya ingresado porfavor verificar", null);
        }
    }

    public void filtraLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabTipo.setCondicion(getFiltrosAcceso());
            tabTipo.ejecutarSql();
            utilitario.addUpdate("tabTipo");
        }
    }

    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (cmbLugar.getValue() != null) {
            str_filtros = "ide_lugar =" + Integer.parseInt(cmbLugar.getValue() + "");
        } else {
            utilitario.agregarMensajeInfo("Filtros no válidos",
                    "Debe seleccionar valor");
        }
        return str_filtros;
    }

    public void filtraDatos() {
        if (!getDatosAcceso().isEmpty()) {
            tabTipo.setCondicion(getDatosAcceso());
            tabTipo.ejecutarSql();
            utilitario.addUpdate("tabTipo");
        }
    }

    private String getDatosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        Integer lugar = 0;
        if (cmbLugar.getValue() != null) {
            lugar = Integer.parseInt(cmbLugar.getValue() + "");
        } else {
            lugar = 0;
        }
        str_filtros = "ide_lugar =" + lugar;
        return str_filtros;
    }

    public void elimaVentana() {
        if (cmbLugarD.getValue() != null && txtSector.getValue() != null && txtModulo.getValue() != null && txtDesde.getValue() != null
                && txtHasta.getValue() != null && txtSector.getValue() != "" && txtModulo.getValue() != "" && txtDesde.getValue() != ""
                && txtHasta.getValue() != "") {
            TablaGenerica tabCatas = adminCmt.getInforCatastro(Integer.parseInt(cmbLugarD.getValue() + ""), txtSector.getValue() + "", txtModulo.getValue() + "", Integer.parseInt(txtDesde.getValue() + ""), Integer.parseInt(txtHasta.getValue() + ""));
            if (!tabCatas.isEmpty()) {
                if (Integer.parseInt(tabCatas.getValor("conteo")) == 0) {
                    TablaGenerica tabDatos = adminCmt.getRecuperaLugar(Integer.parseInt(cmbLugarD.getValue() + ""));
                    if (!tabDatos.isEmpty()) {
                        lbTotLugar.setValue(tabDatos.getValor("DETALLE_LUGAR"));
                        lbTotSector.setValue(txtSector.getValue());
                        lbTotModulo.setValue(txtModulo.getValue());
                        lbTotGen.setValue(totalPuestos(Integer.parseInt(txtDesde.getValue() + "")));
                        diaDelete.Limpiar();
                        Grid griPanel = new Grid();
                        griPanel.setColumns(1);
                        diaDelete.setDialogo(gride);
                        Grid griTotal = new Grid();
                        griTotal.setColumns(2);
                        griTotal.getChildren().add(lbLugarC);
                        griTotal.getChildren().add(lbTotLugar);
                        griTotal.getChildren().add(lbSector);
                        griTotal.getChildren().add(lbTotSector);
                        griTotal.getChildren().add(lbModulo);
                        griTotal.getChildren().add(lbTotModulo);
                        griTotal.getChildren().add(lbTotal);
                        griTotal.getChildren().add(lbTotGen);
                        griPanel.getChildren().add(lbData);
                        griPanel.getChildren().add(griTotal);
                        gride.getChildren().add(griPanel);
                        diaDelete.dibujar();
                    } else {
                        utilitario.agregarMensajeInfo("No se encuentra registro", "");
                    }
                } else {
                    utilitario.agregarMensajeInfo("Algunos lugares se encuentran ocupado", "Revisar");
                }
            } else {
                utilitario.agregarMensajeInfo("Error con la informacion buscada", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Todos los campos deben esta llenos", "");
        }
    }

    public void elimaRegistros() {
        try {
            diaDelete.cerrar();
            valinicio = Integer.parseInt(txtDesde.getValue() + "");
            valfinal = Integer.parseInt(txtHasta.getValue() + "");
            for (int i = valinicio; i <= valfinal; i++) {
                adminCmt.setDeleteCatastro(Integer.parseInt(cmbLugarD.getValue() + ""), txtSector.getValue() + "", txtModulo.getValue() + "", i);
            }
            utilitario.agregarMensajeInfo("Registros Guardados", null);
        } catch (Exception e) {
            utilitario.agregarMensajeInfo("Error", e.getMessage());
        }
    }

    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        if (tabTipo.guardar()) {
            guardarPantalla();
        }//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Tabla getTabTipo() {
        return tabTipo;
    }

    public void setTabTipo(Tabla tabTipo) {
        this.tabTipo = tabTipo;
    }
}
