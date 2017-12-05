/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_cementerio.ejb.cementerio;
import paq_registros.ejb.ServicioRegistros;

/**
 *
 * @author l-suntaxi
 */
public class pre_catastro_fallecido extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla4 = new Tabla();
    private SeleccionTabla setRegistros = new SeleccionTabla();
    private SeleccionTabla setSolicitud = new SeleccionTabla();
    private SeleccionTabla setSolicitud1 = new SeleccionTabla();
    private SeleccionTabla setSolicitud2 = new SeleccionTabla();
    private Tabla setSolicitu = new Tabla();
    private Combo cmbTipo = new Combo();
    private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
    private AutoCompletar aut_busca = new AutoCompletar();
    private SeleccionCalendario sec_rango = new SeleccionCalendario();
    //Contiene todos los elementos de la plantilla
    private Panel panOpcion = new Panel();
    private Dialogo diaDialogoca = new Dialogo();
    private Grid gridCa = new Grid();
    private Combo cmbSeleccion = new Combo();
    private Texto texBusqueda = new Texto();
    private Texto texBusqueda1 = new Texto();
    private Texto texBusqueda2 = new Texto();
    private Dialogo diaDialogoso = new Dialogo();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private cementerio cementerioM = (cementerio) utilitario.instanciarEJB(cementerio.class);
    private ServicioRegistros servicioregistros = (ServicioRegistros) utilitario.instanciarEJB(ServicioRegistros.class);

    public pre_catastro_fallecido() {

//         Imagen de encabezado
//        Imagen quinde = new Imagen();
//        quinde.setValue("imagenes/logo_transporte.png");
//        agregarComponente(quinde);
        bar_botones.quitarBotonInsertar();

        sec_rango.setId("sec_rango");
        sec_rango.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango.setFechaActual();
        agregarComponente(sec_rango);
        agregarComponente(sef_reporte);

        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Disponibles");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("buscaRegistro");
        bar_botones.agregarBoton(bot_busca);


        Boton bot_busca2 = new Boton();
        bot_busca2.setValue("Baja Representante");
        bot_busca2.setExcluirLectura(true);
        bot_busca2.setIcon("ui-icon-search");
        bot_busca2.setMetodo("abrirBusqueda1");
        bar_botones.agregarBoton(bot_busca2);

        Boton bot_busca3 = new Boton();
        bot_busca3.setValue("Cambiar Representante");
        bot_busca3.setExcluirLectura(true);
        bot_busca3.setIcon("ui-icon-search");
        bot_busca3.setMetodo("abrirBusqueda2");
        bar_botones.agregarBoton(bot_busca3);

        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT TOP 10 a.IDE_fallecido,IDE_DET_MOVIMIENTO,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES  , DETALLE_CMACC,FECHA_INGRESA FROM CMT_DETALLE_MOVIMIENTO A\n"
                + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                + "LEFT JOIN  CMT_REPRESENTANTE C  ON A.IDE_FALLECIDO=C.IDE_FALLECIDO\n"
                + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC ");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(70);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        bar_botones.agregarComponente(new Etiqueta("Buscador:"));
        bar_botones.agregarComponente(aut_busca);
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);


        sef_reporte.setId("sef_reporte");
        bar_botones.agregarReporte();

        cmbTipo.setId("cmbTipo");
        cmbTipo.setCombo("SELECT distinct IDE_LUGAR  as id,DETALLE_LUGAR  FROM CMT_LUGAR WHERE DETALLE_LUGAR='SITIO' OR DETALLE_LUGAR='NICHO'");

        //Ingreso y busqueda de solicitudes 
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("mostrarDatos");
        bar_botones.agregarBoton(bot_buscar);

        gri_busca.getChildren().add(cmbTipo);
        gri_busca.getChildren().add(bot_buscar);

        setRegistros.setId("setRegistros");
        setRegistros.setSeleccionTabla("select top 10 IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO,DISPONIBLE_OCUPADO  from CMT_CATASTRO a, cmt_lugar b\n"
                + "where a.ide_lugar=b.ide_lugar AND DISPONIBLE_OCUPADO='DISPONIBLE' AND (DETALLE_LUGAR='SITIO' OR DETALLE_LUGAR='NICHO') ORDER BY DETALLE_LUGAR", "IDE_CATASTRO");
        setRegistros.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setRegistros.getTab_seleccion().getColumna("SECTOR").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("NUMERO").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("MODULO").setFiltro(true);
        setRegistros.getTab_seleccion().setRows(20);
        setRegistros.setWidth("50%");
        setRegistros.setRadio();
        setRegistros.setResizable(false);
        setRegistros.getGri_cuerpo().setHeader(gri_busca);
        setRegistros.getBot_aceptar().setMetodo("consultaCatastro");
        setRegistros.setHeader("BUSCAR CATASTRO CEMENTERIO");
        agregarComponente(setRegistros);



//        bot_busca.setMetodo("Busca_tipo");


        Boton bot_busca1 = new Boton();
        bot_busca1.setValue("Busqueda Fallecidos");
        bot_busca1.setExcluirLectura(true);
        bot_busca1.setIcon("ui-icon-search");
        bot_busca1.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca1);





        //Ingreso y busqueda de solicitudes 
        Grid gri_busca1 = new Grid();
        gri_busca1.setColumns(2);
        texBusqueda.setSize(35);
        gri_busca1.getChildren().add(texBusqueda);

        Boton bot_buscar1 = new Boton();
        bot_buscar1.setValue("Buscar");
        bot_buscar1.setIcon("ui-icon-search");
        bot_buscar1.setMetodo("buscarSolicitud");
        bar_botones.agregarBoton(bot_buscar1);
        gri_busca1.getChildren().add(bot_buscar1);

        setSolicitud.setId("setSolicitud");
        setSolicitud.setSeleccionTabla("SELECT TOP 10 IDE_DET_MOVIMIENTO,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP\n"
                + "  , DETALLE_CMACC,FECHA_INGRESA FROM CMT_DETALLE_MOVIMIENTO A\n"
                + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                + "LEFT JOIN  CMT_REPRESENTANTE C  ON A.IDE_FALLECIDO=C.IDE_FALLECIDO\n"
                + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC", "IDE_DET_MOVIMIENTO");
        setSolicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setSolicitud.getTab_seleccion().getColumna("NOMBRES_APELLIDOS_CMREP").setLongitud(50);
        setSolicitud.getTab_seleccion().getColumna("NOMBRES").setLongitud(50);
        setSolicitud.getTab_seleccion().setRows(10);
        setSolicitud.setRadio();
        setSolicitud.getGri_cuerpo().setHeader(gri_busca1);//consultaFallecido
        setSolicitud.getBot_aceptar().setMetodo("consultaFallecido");
        setSolicitud.setWidth("80%");
        setSolicitud.setHeader("BUSCAR POR NOMBRES");
        agregarComponente(setSolicitud);

        //Ingreso y busqueda de solicitudes 
        Grid gri_busca2 = new Grid();
        gri_busca2.setColumns(2);
        texBusqueda1.setSize(35);
        gri_busca2.getChildren().add(texBusqueda1);

        Boton bot_buscar2 = new Boton();
        bot_buscar2.setValue("Buscar");
        bot_buscar2.setIcon("ui-icon-search");
        bot_buscar2.setMetodo("buscarSolicitud2");
        bar_botones.agregarBoton(bot_buscar2);
        gri_busca2.getChildren().add(bot_buscar2);

        setSolicitud1.setId("setSolicitud1");
        setSolicitud1.setSeleccionTabla("SELECT TOP 10 IDE_DET_MOVIMIENTO,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP\n"
                + "  , DETALLE_CMACC,FECHA_INGRESA FROM CMT_DETALLE_MOVIMIENTO A\n"
                + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                + "LEFT JOIN  CMT_REPRESENTANTE C  ON A.IDE_FALLECIDO=C.IDE_FALLECIDO\n"
                + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC ", "IDE_DET_MOVIMIENTO");
        setSolicitud1.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setSolicitud1.getTab_seleccion().getColumna("NOMBRES_APELLIDOS_CMREP").setLongitud(50);
        setSolicitud1.getTab_seleccion().getColumna("NOMBRES").setLongitud(50);
        setSolicitud1.getTab_seleccion().setRows(10);
        setSolicitud1.setRadio();
        setSolicitud1.getGri_cuerpo().setHeader(gri_busca2);
        setSolicitud1.getBot_aceptar().setMetodo("consultaFallecidoRepresentante");
        setSolicitud1.setWidth("80%");
        setSolicitud1.setHeader("BUSCAR POR NOMBRE REPRESENTANTE");
        agregarComponente(setSolicitud1);

        //Ingreso y busqueda de solicitudes 
        Grid gri_busca3 = new Grid();
        gri_busca3.setColumns(2);
        texBusqueda2.setSize(35);
        gri_busca3.getChildren().add(texBusqueda2);

        Boton bot_buscar3 = new Boton();
        bot_buscar3.setValue("Buscar");
        bot_buscar3.setIcon("ui-icon-search");
        bot_buscar3.setMetodo("buscarSolicitud3");
        bar_botones.agregarBoton(bot_buscar3);
        gri_busca3.getChildren().add(bot_buscar3);

        setSolicitud2.setId("setSolicitud2");
        setSolicitud2.setSeleccionTabla("  SELECT TOP 10 IDE_DET_MOVIMIENTO,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP\n"
                + "  , DETALLE_CMACC,FECHA_INGRESA FROM CMT_DETALLE_MOVIMIENTO A\n"
                + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                + "LEFT JOIN  CMT_REPRESENTANTE C  ON A.IDE_FALLECIDO=C.IDE_FALLECIDO\n"
                + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC", "IDE_DET_MOVIMIENTO");
        setSolicitud2.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setSolicitud2.getTab_seleccion().getColumna("NOMBRES_APELLIDOS_CMREP").setLongitud(50);
        setSolicitud2.getTab_seleccion().getColumna("NOMBRES").setLongitud(50);
        setSolicitud2.getTab_seleccion().setRows(10);
        setSolicitud2.setRadio();
        setSolicitud2.getGri_cuerpo().setHeader(gri_busca3);
        setSolicitud2.getBot_aceptar().setMetodo("consultaCambiaRepresentante");
        setSolicitud2.setWidth("80%");
        setSolicitud2.setHeader("BUSCAR POR NOMBRE FALLECIDO");
        agregarComponente(setSolicitud2);
        /*
         * Configuración y llamado de objeto reporte
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);


        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("INGRESO DE MOVIMIENTOS AL CEMENTERIO MUNICIPAL - CATASTRO CEMENTERIO");
        agregarComponente(panOpcion);
        dibujarPantalla();
    }

    /*
     * Formulario de ingreso
     */
    public void dibujarPantalla() {
        limpiarPanel();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("CMT_FALLECIDO", "IDE_FALLECIDO", 1);
        if (aut_busca.getValue() == null) {
            tab_tabla1.setCondicion("IDE_FALLECIDO=-1");
        } else {
            tab_tabla1.setCondicion("IDE_FALLECIDO=" + aut_busca.getValor());
        }
        tab_tabla1.getColumna("IDE_CMGEN").setCombo("select IDE_CMGEN,DETALLE_CMGEN from CMT_GENERO");
        tab_tabla1.getColumna("CEDULA_FALLECIDO").setMetodoChange("buscaPersona");
        tab_tabla1.getColumna("IDE_CATASTRO").setCombo("SELECT IDE_CATASTRO,DETALLE_LUGAR+'-'+SECTOR+'-'+convert(varchar,NUMERO)+'-'+convert(varchar,MODULO) FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR = B.IDE_LUGAR ");
        tab_tabla1.getColumna("IDE_CATASTRO").setLectura(true);
        tab_tabla1.getColumna("FECHA_DOCUMENTO_CMARE").setVisible(false);
        tab_tabla1.getColumna("FECHA_DEFUNCION").setRequerida(true);

        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.agregarRelacion(tab_tabla4);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setMensajeWarn("FALLECIDO");
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("CMT_REPRESENTANTE", "IDE_CMREP", 2);
        tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setMetodoChange("buscaPersonaDatos");
        tab_tabla2.getColumna("IDE_CMTID").setCombo("select IDE_CMTID,DETALLE_CMTID from CMT_TIPO_DOCUMENTO");
        List lista = new ArrayList();
        Object fila1[] = {"1", "ACTIVO"};
        Object fila2[] = {"2", "PASIVO"};
        lista.add(fila1);
        lista.add(fila2);
        tab_tabla2.getColumna("ESTADO").setCombo(lista);
        tab_tabla2.getColumna("IDE_CMARE").setVisible(false);
        tab_tabla2.getGrid().setColumns(4);
        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setMensajeWarn("DATOS DE REPRESENTANTE");
        pat_panel2.setPanelTabla(tab_tabla2);

        tab_tabla4.setId("tab_tabla4");
        tab_tabla4.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 3);
        tab_tabla4.getColumna("IDE_CATASTRO").setVisible(false);
        tab_tabla4.getColumna("IDE_CMREP").setVisible(false);
        tab_tabla4.getColumna("IDE_TIPO_MOVIMIENTO").setCombo("SELECT IDE_CMACC,DETALLE_CMACC FROM CMT_ACCION ");
        tab_tabla4.getColumna("IDE_TIPO_MOVIMIENTO").setRequerida(true);
        tab_tabla4.getColumna("FECHA_INGRESA ").setValorDefecto(utilitario.getFechaActual());
        tab_tabla4.getColumna("FECHA_INGRESA").setLectura(false);
        tab_tabla4.getGrid().setColumns(6);
        tab_tabla4.setTipoFormulario(true);
        tab_tabla4.dibujar();
        PanelTabla pat_panel4 = new PanelTabla();
        pat_panel4.setMensajeWarn("DATOS DE MOVIMIENTO");
        pat_panel4.setPanelTabla(tab_tabla4);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(pat_panel1, pat_panel2, pat_panel4, "38%", "30%", "H");
        agregarComponente(div_division);

        Grupo gru = new Grupo();
        gru.getChildren().add(div_division);
        panOpcion.getChildren().add(gru);
    }

    public void aceptarBusqueda() {
        if (cmbSeleccion.getValue().equals("1")) {
            if (setSolicitud.getValorSeleccionado() != null) {
                aut_busca.setValor(setSolicitud.getValorSeleccionado());
                setSolicitud.cerrar();
                dibujarPantalla();
                diaDialogoca.cerrar();
                utilitario.addUpdate("aut_busca,pan_opcion");
            } else {
                utilitario.agregarMensajeInfo("Debe seleccionar una solicitud", "");
            }
        } else {
            if (setSolicitu.getValorSeleccionado() != null) {
                aut_busca.setValor(setSolicitu.getValorSeleccionado());
                diaDialogoso.cerrar();
                dibujarPantalla();
                diaDialogoca.cerrar();
                utilitario.addUpdate("aut_busca,pan_opcion");
            } else {
                utilitario.agregarMensajeInfo("Debe seleccionar una Solicitud", "");
            }
        }
    }

    public void abrirBusqueda() {
        setSolicitud.dibujar();
        texBusqueda.limpiar();
        setSolicitud.getTab_seleccion().limpiar();
    }

    public void abrirBusqueda1() {
        setSolicitud1.dibujar();
        texBusqueda1.limpiar();
        setSolicitud1.getTab_seleccion().limpiar();
    }

    public void abrirBusqueda2() {
        setSolicitud2.dibujar();
        texBusqueda2.limpiar();
        setSolicitud2.getTab_seleccion().limpiar();
    }

    public void buscarSolicitud() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT TOP 15 IDE_DET_MOVIMIENTO,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP\n"
                    + "  , DETALLE_CMACC,FECHA_INGRESA FROM CMT_DETALLE_MOVIMIENTO A\n"
                    + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                    + "LEFT JOIN  CMT_REPRESENTANTE C  ON A.IDE_FALLECIDO=C.IDE_FALLECIDO\n"
                    + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                    + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC where ESTADO=1 AND  NOMBRES LIKE '" + texBusqueda.getValue() + "%'");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud2() {
        System.out.println("SALI buscarSolicitud2");
        if (texBusqueda1.getValue() != null && texBusqueda1.getValue().toString().isEmpty() == false) {
            setSolicitud1.getTab_seleccion().setSql("SELECT TOP 15 IDE_DET_MOVIMIENTO,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP\n"
                    + "  , DETALLE_CMACC,FECHA_INGRESA FROM CMT_DETALLE_MOVIMIENTO A\n"
                    + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                    + "LEFT JOIN  CMT_REPRESENTANTE C  ON A.IDE_FALLECIDO=C.IDE_FALLECIDO\n"
                    + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                    + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC where ESTADO=1 AND NOMBRES LIKE '%" + texBusqueda1.getValue() + "%'");
            setSolicitud1.getTab_seleccion().ejecutarSql();
            setSolicitud1.getTab_seleccion().imprimirSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud3() {
        System.out.println("SALI buscarSolicitud3");
        if (texBusqueda2.getValue() != null && texBusqueda2.getValue().toString().isEmpty() == false) {
            setSolicitud2.getTab_seleccion().setSql("SELECT TOP 15 IDE_DET_MOVIMIENTO,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP\n"
                    + "  , DETALLE_CMACC,FECHA_INGRESA FROM CMT_DETALLE_MOVIMIENTO A\n"
                    + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                    + "LEFT JOIN  CMT_REPRESENTANTE C  ON A.IDE_FALLECIDO=C.IDE_FALLECIDO\n"
                    + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                    + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC where ESTADO=1 AND NOMBRES LIKE '%" + texBusqueda2.getValue() + "%'");
            setSolicitud2.getTab_seleccion().ejecutarSql();
            setSolicitud2.getTab_seleccion().imprimirSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    @Override
    public void abrirListaReportes() {
        System.out.println("entro akkk");
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        System.out.println("sali akkk");
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "MOVIMIENTO":
                aceptoOrden();
                break;
        }
    }

    public void aceptoOrden() {
        System.out.println("voy por  akkk" + tab_tabla4.getValor("IDE_DET_MOVIMIENTO"));
        switch (rep_reporte.getNombre()) {
            case "MOVIMIENTO":
//              p_parametros.put("nom_resp", tab_tabla2.getValor("NICK_USUA") + "");
                p_parametros.put("ide_det_mov", Integer.parseInt(tab_tabla4.getValor("IDE_DET_MOVIMIENTO") + ""));
                System.out.println(p_parametros);
                System.out.println(rep_reporte.getPath());
                rep_reporte.cerrar();

                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }
    //Permite Buscar solicitud que se encuentra Ingresada o Pendiente

    public void Busca_tipo() {
        diaDialogoca.Limpiar();
        gridCa.getChildren().add(new Etiqueta("ELEGIR PARAMETRO DE BUSQUEDA:"));
        gridCa.getChildren().add(cmbSeleccion);
        diaDialogoca.setDialogo(gridCa);
        diaDialogoca.dibujar();

    }

    public void buscarPersona(SelectEvent evt) {
        limpiar();
        aut_busca.onSelect(evt);
        System.out.println("aut_busca.getValor()" + aut_busca.getValor());
        dibujarPantalla();

    }

    @Override
    public void insertar() {
        if (tab_tabla1.isFocus()) {
            aut_busca.limpiar();
            utilitario.addUpdate("aut_busca");
            tab_tabla1.limpiar();
            tab_tabla1.insertar();
            tab_tabla2.limpiar();
            tab_tabla2.insertar();
            tab_tabla4.limpiar();
            tab_tabla4.insertar();
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla1.getValor("IDE_FALLECIDO") != null && tab_tabla4.getValor("IDE_TIPO_MOVIMIENTO").equals("5")) {
            if (tab_tabla4.guardar()) {
                tab_tabla2.guardar();
            }
            guardarPantalla();
            cementerioM.ingresaMovimiento(tab_tabla1.getValor("IDE_CATASTRO"), tab_tabla2.getValor("IDE_CMREP"), tab_tabla4.getValor("IDE_DET_MOVIMIENTO"), tab_tabla1.getValor("IDE_fallecido"));
        } else if (tab_tabla1.getValor("IDE_FALLECIDO") != null && tab_tabla2.getValor("IDE_CMREP") != null && tab_tabla4.getValor("IDE_DET_MOVIMIENTO") != null) {
            cementerioM.set_updateEstadoRep(tab_tabla2.getValor("IDE_CMREP"), tab_tabla2.getValor("ESTADO"));
            utilitario.agregarMensaje("Registro actualizado correctamente", "");

        } else if (tab_tabla1.getValor("IDE_FALLECIDO") != null && tab_tabla2.getValor("IDE_CMREP") != null) {

            if (tab_tabla4.guardar()) {
                cementerioM.set_updateDisponibleExhumado(tab_tabla1.getValor("IDE_CATASTRO"));
            }
            guardarPantalla();
            cementerioM.ingresaMovimiento(tab_tabla1.getValor("IDE_CATASTRO"), tab_tabla2.getValor("IDE_CMREP"), tab_tabla4.getValor("IDE_DET_MOVIMIENTO"), tab_tabla1.getValor("IDE_fallecido"));
        } else {
            if (tab_tabla1.guardar()) {
                if (tab_tabla2.guardar()) {
                    if (tab_tabla4.guardar()) {
                        cementerioM.set_updateDisponible(tab_tabla1.getValor("IDE_CATASTRO"));
                    }
                    guardarPantalla();
                    cementerioM.ingresaMovimiento(tab_tabla1.getValor("IDE_CATASTRO"), tab_tabla2.getValor("IDE_CMREP"), tab_tabla4.getValor("IDE_DET_MOVIMIENTO"), tab_tabla1.getValor("IDE_fallecido"));
                }
            }
        }
    }

    @Override
    public void actualizar() {
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
        aut_busca.actualizar();
        aut_busca.setSize(70);
        utilitario.addUpdate("aut_busca");
    }

    public void consultaCatastro() {
        tab_tabla1.insertar();
        tab_tabla2.insertar();
        tab_tabla4.insertar();
        tab_tabla4.setValor("NUMERO_FORMULARIO", cementerioM.maxNumF());

        TablaGenerica tabDato = cementerioM.consultaCatastro(Integer.parseInt(setRegistros.getValorSeleccionado()));
        if (!tabDato.isEmpty()) {
            tab_tabla1.setValor("IDE_CATASTRO ", tabDato.getValor("IDE_CATASTRO"));
            setRegistros.cerrar();
            utilitario.addUpdate("tab_tabla1");
        } else {
            utilitario.agregarMensajeError("Datos", "No Se Encuentra Registrado");
        }

    }

    public void consultaFallecido() {
        tab_tabla1.insertar();
        tab_tabla2.insertar();
        tab_tabla4.insertar();
        tab_tabla4.setValor("NUMERO_FORMULARIO", cementerioM.maxNumF());
        System.out.println("setSolicitud<<<<<<<" + setSolicitud.getValorSeleccionado());
        TablaGenerica tabDato = cementerioM.consultaFallecido(Integer.parseInt(setSolicitud.getValorSeleccionado()));
        if (!tabDato.isEmpty()) {
            tab_tabla1.setValor("IDE_CATASTRO ", tabDato.getValor("IDE_CATASTRO"));
            tab_tabla1.setValor("IDE_FALLECIDO ", tabDato.getValor("IDE_FALLECIDO"));
            tab_tabla1.setValor("IDE_CMGEN ", tabDato.getValor("IDE_CMGEN"));
            tab_tabla1.setValor("CEDULA_FALLECIDO", tabDato.getValor("CEDULA_FALLECIDO"));
            tab_tabla1.setValor("NOMBRES", tabDato.getValor("NOMBRES"));
            tab_tabla1.setValor("FECHA_NACIMIENTO ", tabDato.getValor("FECHA_NACIMIENTO"));
            tab_tabla1.setValor("FECHA_DEFUNCION ", tabDato.getValor("FECHA_DEFUNCION"));
            tab_tabla1.setValor("FECHA_DOCUMENTO_CMARE ", tabDato.getValor("FECHA_DOCUMENTO_CMARE"));

            tab_tabla2.setValor("IDE_CMREP ", tabDato.getValor("IDE_CMREP"));
            tab_tabla2.setValor("IDE_CMTID ", tabDato.getValor("IDE_CMTID"));
            tab_tabla2.setValor("NOMBRES_APELLIDOS_CMREP", tabDato.getValor("NOMBRES_APELLIDOS_CMREP"));
            tab_tabla2.setValor("DOCUMENTO_IDENTIDAD_CMREP", tabDato.getValor("DOCUMENTO_IDENTIDAD_CMREP"));
            tab_tabla2.setValor("DIRECCION_CMREP", tabDato.getValor("DIRECCION_CMREP"));
            tab_tabla2.setValor("TELEFONOS_CMREP ", tabDato.getValor("TELEFONOS_CMREP"));
            tab_tabla2.setValor("CELULAR_CMREP ", tabDato.getValor("CELULAR_CMREP"));
            tab_tabla2.setValor("EMAIL_CMREP", tabDato.getValor("EMAIL_CMREP"));
            tab_tabla2.setValor("ESTADO", tabDato.getValor("ESTADO"));

            utilitario.addUpdate("tab_tabla1");
            utilitario.addUpdate("tab_tabla2");
            setSolicitud.cerrar();
        }
    }
    //BUSQUEDA DE PERSONAS EN BASE DE DATOS DEL MUNICIPIO

    public void consultaFallecidoRepresentante() {
        tab_tabla1.insertar();
        tab_tabla2.insertar();
        tab_tabla4.insertar();
        tab_tabla4.setValor("NUMERO_FORMULARIO", cementerioM.maxNumF());
        System.out.println("setSolicitud1<<<<<<<" + setSolicitud1.getValorSeleccionado());
        TablaGenerica tabDato = cementerioM.consultaFallecido(Integer.parseInt(setSolicitud1.getValorSeleccionado()));
        if (!tabDato.isEmpty()) {
            tab_tabla1.setValor("IDE_CATASTRO ", tabDato.getValor("IDE_CATASTRO"));
            tab_tabla1.setValor("IDE_FALLECIDO ", tabDato.getValor("IDE_FALLECIDO"));
            tab_tabla1.setValor("IDE_CMGEN ", tabDato.getValor("IDE_CMGEN"));
            tab_tabla1.setValor("CEDULA_FALLECIDO", tabDato.getValor("CEDULA_FALLECIDO"));
            tab_tabla1.setValor("NOMBRES", tabDato.getValor("NOMBRES"));
            tab_tabla1.setValor("FECHA_NACIMIENTO ", tabDato.getValor("FECHA_NACIMIENTO"));
            tab_tabla1.setValor("FECHA_DEFUNCION ", tabDato.getValor("FECHA_DEFUNCION"));
            tab_tabla1.setValor("FECHA_DOCUMENTO_CMARE ", tabDato.getValor("FECHA_DOCUMENTO_CMARE"));

            tab_tabla2.setValor("IDE_CMREP ", tabDato.getValor("IDE_CMREP"));
            tab_tabla2.setValor("IDE_CMTID ", tabDato.getValor("IDE_CMTID"));
            tab_tabla2.setValor("NOMBRES_APELLIDOS_CMREP", tabDato.getValor("NOMBRES_APELLIDOS_CMREP"));
            tab_tabla2.setValor("DOCUMENTO_IDENTIDAD_CMREP", tabDato.getValor("DOCUMENTO_IDENTIDAD_CMREP"));
            tab_tabla2.setValor("DIRECCION_CMREP", tabDato.getValor("DIRECCION_CMREP"));
            tab_tabla2.setValor("TELEFONOS_CMREP ", tabDato.getValor("TELEFONOS_CMREP"));
            tab_tabla2.setValor("CELULAR_CMREP ", tabDato.getValor("CELULAR_CMREP"));
            tab_tabla2.setValor("EMAIL_CMREP", tabDato.getValor("EMAIL_CMREP"));
            tab_tabla2.setValor("ESTADO", tabDato.getValor("ESTADO"));

            tab_tabla4.setValor("IDE_DET_MOVIMIENTO", tabDato.getValor("IDE_DET_MOVIMIENTO"));
            tab_tabla4.setValor("IDE_TIPO_MOVIMIENTO", tabDato.getValor("IDE_TIPO_MOVIMIENTO"));
            tab_tabla4.setValor("FECHA_INGRESA", tabDato.getValor("FECHA_INGRESA"));
            tab_tabla4.setValor("ANO_INICIA", tabDato.getValor("ANO_INICIA"));
            tab_tabla4.setValor("ANO_FIN", tabDato.getValor("ANO_FIN"));
            tab_tabla4.setValor("NUMERO_FORMULARIO", tabDato.getValor("NUMERO_FORMULARIO"));
            tab_tabla4.setValor("NUM_LIQUIDACION", tabDato.getValor("NUM_LIQUIDACION"));

            utilitario.addUpdate("tab_tabla1");
            utilitario.addUpdate("tab_tabla2");
            utilitario.addUpdate("tab_tabla4");

            setSolicitud1.cerrar();
        }
    }

    public void consultaCambiaRepresentante() {
        System.out.println("getValorSeleccionado<<<<<<<" + setSolicitud2.getValorSeleccionado());

        String tabDato1 = cementerioM.consultaRepAc(Integer.parseInt(setSolicitud2.getValorSeleccionado()));

        System.out.println("tabDato1<<<<<<<" + tabDato1);
        if (tabDato1.equals("1")) {
            utilitario.agregarMensajeError("El fallecido tiene ", "Representante activo");
        } else {
            tab_tabla1.insertar();
            tab_tabla2.insertar();
            tab_tabla4.insertar();
            tab_tabla4.setValor("NUMERO_FORMULARIO", cementerioM.maxNumF());
            System.out.println("setSolicitud<<<<<<<" + setSolicitud1.getValorSeleccionado());
            TablaGenerica tabDato = cementerioM.consultaFallecido(Integer.parseInt(setSolicitud2.getValorSeleccionado()));

            if (!tabDato.isEmpty()) {
                tab_tabla1.setValor("IDE_CATASTRO ", tabDato.getValor("IDE_CATASTRO"));
                tab_tabla1.setValor("IDE_FALLECIDO ", tabDato.getValor("IDE_FALLECIDO"));
                tab_tabla1.setValor("IDE_CMGEN ", tabDato.getValor("IDE_CMGEN"));
                tab_tabla1.setValor("CEDULA_FALLECIDO", tabDato.getValor("CEDULA_FALLECIDO"));
                tab_tabla1.setValor("NOMBRES", tabDato.getValor("NOMBRES"));
                tab_tabla1.setValor("FECHA_NACIMIENTO ", tabDato.getValor("FECHA_NACIMIENTO"));
                tab_tabla1.setValor("FECHA_DEFUNCION ", tabDato.getValor("FECHA_DEFUNCION"));
                tab_tabla1.setValor("FECHA_DOCUMENTO_CMARE ", tabDato.getValor("FECHA_DOCUMENTO_CMARE"));

                tab_tabla4.setValor("ANO_INICIA", tabDato.getValor("ANO_INICIA"));
                tab_tabla4.setValor("ANO_FIN", tabDato.getValor("ANO_FIN"));
                tab_tabla4.setValor("NUM_LIQUIDACION", tabDato.getValor("NUM_LIQUIDACION"));

                utilitario.addUpdate("tab_tabla1");
                utilitario.addUpdate("tab_tabla2");

                setSolicitud2.cerrar();
            }
        }
    }

//    public void buscaPersonaSD() {
//        if (utilitario.validarCedula(tab_tabla1.getValor("CEDULA_FALLECIDO"))) {
//            String usu = "p-chumana";
//            String pass = "17171922392016";
//            String cedula = tab_tabla1.getValor("CEDULA_FALLECIDO");
//            int sexo;
//            if (busquedaCedulaActual(cedula, usu, pass).getSexo().equals("FEMENINO")) {
//                sexo = 2;
//            } else {
//                sexo = 1;
//            }
//            String sexo1 = String.valueOf(sexo);
//
//            tab_tabla1.setValor("NOMBRES", busquedaCedulaActual(cedula, usu, pass).getNombre());
//            tab_tabla1.setValor("IDE_CMGEN", sexo1);
//            System.out.println("busquedaCedulaActual(cedula, usu, pass).getFechaNacimiento()<<<<<" + busquedaCedulaActual(cedula, usu, pass).getFechaNacimiento());
//
//            Date str_fecha1 = utilitario.DeStringADateformato3(busquedaCedulaActual(cedula, usu, pass).getFechaNacimiento());
//            String str_fecha2 = utilitario.DeDateAString(str_fecha1);
//            String str_fecha3 = utilitario.DeDateAStringformato2(str_fecha1);
//
//
//            tab_tabla1.setValor("FECHA_NACIMIENTO", "'"+str_fecha2+"'");
//            tab_tabla1.setValor("FECHA_DEFUNCION", busquedaCedulaActual(cedula, usu, pass).getFechaFallecido());
//            utilitario.addUpdate("tab_tabla1");
//
//        } else {
//            utilitario.agregarMensaje("Número de Cédula Incorrecto", null);
//        }
//    }
    public void buscaPersona() {
        if (utilitario.validarCedula(tab_tabla1.getValor("CEDULA_FALLECIDO"))) {
            TablaGenerica tab_dato = servicioregistros.getPersona(tab_tabla1.getValor("CEDULA_FALLECIDO"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_tabla1.setValor("NOMBRES", tab_dato.getValor("nombre"));
                tab_tabla1.setValor("IDE_CMGEN", tab_dato.getValor("sexo"));
                tab_tabla1.setValor("FECHA_NACIMIENTO", tab_dato.getValor("fecha"));
                utilitario.addUpdate("tab_tabla1");
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else {
            utilitario.agregarMensajeInfo("El Número de Cédula ingresado no es correcto", "");
        }
    }

    public void buscaPersonaDatos() {
        if (utilitario.validarCedula(tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP"))) {
            TablaGenerica tab_dato = servicioregistros.getPersona(tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_tabla2.setValor("NOMBRES_APELLIDOS_CMREP", tab_dato.getValor("nombre"));
                tab_tabla2.setValor("DIRECCION_CMREP", tab_dato.getValor("CALLE"));
                utilitario.addUpdate("tab_tabla2");
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else if (utilitario.validarRUC(tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP"))) {
            TablaGenerica tab_dato = servicioregistros.getEmpresa(tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_tabla2.setValor("NOMBRES_APELLIDOS_CMREP", tab_dato.getValor("RAZON_SOCIAL"));
                tab_tabla2.setValor("DIRECCION_CMREP", tab_dato.getValor("DIRECCION"));
                tab_tabla2.setValor("TELEFONOS_CMREP", tab_dato.getValor("TELEFONO_TRABAJO"));
                tab_tabla2.setValor("CELULAR_CMREP", tab_dato.getValor("TELEFONO"));
                tab_tabla2.setValor("EMAIL_CMREP", tab_dato.getValor("MAIL"));
                utilitario.addUpdate("tab_tabla2");
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Cedula no valida", "");
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public void buscaRegistro() {
        setRegistros.dibujar();
    }

    public void mostrarDatos() {
        if (cmbTipo.getValue() != null && cmbTipo.getValue().toString().isEmpty() == false) {
            setRegistros.getTab_seleccion().setSql(" select IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO,DISPONIBLE_OCUPADO from CMT_CATASTRO a, cmt_lugar b\n"
                    + "where a.ide_lugar=b.ide_lugar AND DISPONIBLE_OCUPADO='DISPONIBLE' and  a.IDE_lugar ='" + cmbTipo.getValue() + "'");
            setRegistros.getTab_seleccion().ejecutarSql();
            setRegistros.getTab_seleccion().imprimirSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public Tabla getTab_tabla4() {
        return tab_tabla4;
    }

    public void setTab_tabla4(Tabla tab_tabla4) {
        this.tab_tabla4 = tab_tabla4;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public SeleccionCalendario getSec_rango() {
        return sec_rango;
    }

    public void setSec_rango(SeleccionCalendario sec_rango) {
        this.sec_rango = sec_rango;
    }

    public SeleccionTabla getSetRegistros() {
        return setRegistros;
    }

    public void setSetRegistros(SeleccionTabla setRegistros) {
        this.setRegistros = setRegistros;
    }

    public SeleccionTabla getSetSolicitud() {
        return setSolicitud;
    }

    public void setSetSolicitud(SeleccionTabla setSolicitud) {
        this.setSolicitud = setSolicitud;
    }

    public Tabla getSetSolicitu() {
        return setSolicitu;
    }

    public void setSetSolicitu(Tabla setSolicitu) {
        this.setSolicitu = setSolicitu;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }

    public SeleccionTabla getSetSolicitud1() {
        return setSolicitud1;
    }

    public void setSetSolicitud1(SeleccionTabla setSolicitud1) {
        this.setSolicitud1 = setSolicitud1;
    }

    public SeleccionTabla getSetSolicitud2() {
        return setSolicitud2;
    }

    public void setSetSolicitud2(SeleccionTabla setSolicitud2) {
        this.setSolicitud2 = setSolicitud2;
    }
}
