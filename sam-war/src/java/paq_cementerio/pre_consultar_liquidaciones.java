/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

/**
 *
 * @author l-suntaxi
 */
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.ejb.EJB;
import paq_cementerio.ejb.cementerio;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 *
 */
public class pre_consultar_liquidaciones extends Pantalla {

    private Texto texBusqueda = new Texto();
    private Texto texBusqueda1 = new Texto();
    private Etiqueta txArchivo = new Etiqueta("Estado :");
    private Etiqueta txRentas = new Etiqueta("Estado :");
    private Etiqueta etiLugar = new Etiqueta("<BR>Lugar :</BR>");
    private Etiqueta etiSector = new Etiqueta("<BR>Sector :</BR>");
    private Etiqueta etiModulo = new Etiqueta("<BR>Modulo :</BR>");
    private Etiqueta etiDesde = new Etiqueta("<BR>Desde :</BR>");
    private Etiqueta etiHasta = new Etiqueta("<BR>Hasta :</BR>");
    private Texto txtDesde = new Texto();
    private Texto txtHasta = new Texto();
    private Combo cmbLugar = new Combo();
    private Combo cmbSector = new Combo();
    private Combo cmbModulo = new Combo();
    private Etiqueta txInformacion = new Etiqueta("<CENTER><B>El lugar se encuentra ocupado dentro del catastro de cementerio</B></CENTER> \n"
            + " <CENTER>¿Desea ingresar el registro en el mismo lugar? </CENTER>");
    private Conexion conSqlapli = new Conexion();
    private Combo cmbOpcion = new Combo();
    private Combo cmbOpcion1 = new Combo();
    private Combo cmbRentas = new Combo();
    private Combo cmbArchivo = new Combo();
    private Calendario fecha1 = new Calendario();
    private Calendario fecha2 = new Calendario();
    private Tabla tabFallecido = new Tabla();
    private Tabla tabMovimientoFallecido = new Tabla();
    private Tabla tabRepresentante = new Tabla();
    private Tabla tabLevantamiento = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla setFallecido = new Tabla();
    private SeleccionTabla setSolicitud = new SeleccionTabla();
    private SeleccionTabla setExcel = new SeleccionTabla();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();
    private Dialogo diaRentas = new Dialogo();
    private Dialogo diaArchivo = new Dialogo();
    private Dialogo diaInfor = new Dialogo();
    private Dialogo diaFallecidos = new Dialogo();
    private Dialogo diaReporte = new Dialogo();
    private Grid gridR = new Grid();
    private Grid gridA = new Grid();
    private Grid gridFa = new Grid();
    private Grid gridRe = new Grid();
    private Grid gridI = new Grid();
    private Grid gria = new Grid();
    private Grid grir = new Grid();
    private Grid gridre = new Grid();
    private Grid gri = new Grid();
    private Grid gridfa = new Grid();
    private int intRegistro, intCatastro;
    @EJB
    private cementerio cementerioM = (cementerio) utilitario.instanciarEJB(cementerio.class);
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    public pre_consultar_liquidaciones() {
        //         Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logoactual.png");
        agregarComponente(quinde);

        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();

        conSqlapli.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcapli"));
        conSqlapli.NOMBRE_MARCA_BASE = "sqlserver";

        autBusca.setId("autBusca");
        autBusca.setConexion(conSqlapli);
        autBusca.setAutoCompletar("select id_fallecido,cedula_fallecido,nombre_fallecido,fecha_defuncion,lugar_actual,representante_actual,cedula_representante from fallecidos ");
//        autBusca.setMetodoChange("buscarPersona");
        autBusca.setSize(100);

//        bar_botones.agregarComponente(new Etiqueta("Buscador Fallecidos:"));
//        bar_botones.agregarComponente(autBusca);

        cmbLugar.setId("cmbLugar");
        cmbLugar.setCombo("select ide_lugar,detalle_lugar from cmt_lugar where cod_rubro = 'CMTR' order by detalle_lugar");
        cmbLugar.setMetodo("verSector");
//        cmbLugar.eliminarVacio();

        cmbSector.setId("cmbSector");
        cmbSector.setCombo("SELECT distinct SECTOR as id,SECTOR FROM CMT_CATASTRO order by SECTOR");
        cmbSector.setMetodo("verModulo");
//        cmbSector.eliminarVacio();

        cmbModulo.setId("cmbModulo");
        cmbModulo.setCombo("SELECT distinct MODULO as id,MODULO FROM CMT_CATASTRO order by MODULO");
//        cmbModulo.eliminarVacio();

        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        Boton bot_Limpiar = new Boton();
        bot_Limpiar.setValue("Limpiar");
        bot_Limpiar.setExcluirLectura(true);
        bot_Limpiar.setIcon("ui-icon-document");
        bot_Limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_Limpiar);

        Boton bot_busca1 = new Boton();
        bot_busca1.setValue("Busq. Rentas Cementerio");
        bot_busca1.setExcluirLectura(true);
        bot_busca1.setIcon("ui-icon-search");
        bot_busca1.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca1);

        Boton bot_busca = new Boton();
        bot_busca.setValue("Busq. Levantamientio Excel");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusquedaExcel");
        bar_botones.agregarBoton(bot_busca);

        Boton bot_migrar = new Boton();
        bot_migrar.setValue("Migrar SIMUR Cementerio");
        bot_migrar.setExcluirLectura(true);
        bot_migrar.setIcon("ui-icon-gear");
        bot_migrar.setMetodo("ejecutaSp");
        bar_botones.agregarBoton(bot_migrar);


        List list = new ArrayList();
        Object fila1[] = {
            "0", "Cédula Fallecido"
        };
        Object fila2[] = {
            "1", "Nombres Fallecido"
        };
        Object fila3[] = {
            "2", "Fecha Defuncion"
        };
        Object fila4[] = {
            "3", "Nombres Representante"
        };
        Object fila5[] = {
            "4", "Cedula Representante"
        };
        list.add(fila2);;
        list.add(fila1);;
        list.add(fila3);;
        list.add(fila4);;
        list.add(fila5);;
        Grid griPri = new Grid();
        griPri.setColumns(3);

        cmbOpcion.setCombo(list);
        cmbOpcion.eliminarVacio();

        Grid gri_busca1 = new Grid();
        gri_busca1.setColumns(5);
        gri_busca1.getChildren().add(new Etiqueta("Buscar por :"));
        gri_busca1.getChildren().add(cmbOpcion);
        griPri.getChildren().add(gri_busca1);

        gri_busca1.getChildren().add(new Etiqueta("Ingrese: "));
        texBusqueda.setSize(50);
        gri_busca1.getChildren().add(texBusqueda);

        griPri.getChildren().add(gri_busca1);
        bar_botones.agregarComponente(griPri);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
//        panOpcion.setHeader("<CENTER><B>DATOS DE FALLECIDO RENTAS CEMENTERIO</CENTER></B>");
        agregarComponente(panOpcion);


        dibujarPantalla();
        Boton bot_buscar1 = new Boton();
        bot_buscar1.setValue("Buscar");
        bot_buscar1.setIcon("ui-icon-search");
        bot_buscar1.setMetodo("busquedaInfo");
        bar_botones.agregarBoton(bot_buscar1);
        gri_busca1.getChildren().add(bot_buscar1);

        setSolicitud.setId("setSolicitud");
        setSolicitud.getTab_seleccion().setConexion(conSqlapli);
        setSolicitud.setSeleccionTabla("SELECT TOP 20 id_fallecido,cedula_fallecido,nombre_fallecido,fecha_defuncion,lugar_actual,representante_actual from FALLECIDOS where estado_fallecido = 'ACTIVO'", "id_fallecido");
        setSolicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setSolicitud.getTab_seleccion().getColumna("nombre_fallecido").setLongitud(50);
        setSolicitud.getTab_seleccion().getColumna("cedula_fallecido").setLongitud(25);
        setSolicitud.getTab_seleccion().getColumna("fecha_defuncion").setLongitud(25);
        setSolicitud.getTab_seleccion().getColumna("lugar_actual").setLongitud(50);
        setSolicitud.getTab_seleccion().getColumna("representante_actual").setLongitud(60);
        setSolicitud.getTab_seleccion().getColumna("id_fallecido").setNombreVisual("Codigo Fallecido");
        setSolicitud.getTab_seleccion().getColumna("cedula_fallecido").setNombreVisual("Cedula Fallecido");
        setSolicitud.getTab_seleccion().getColumna("nombre_fallecido").setNombreVisual("Nombre Fallecido");
        setSolicitud.getTab_seleccion().getColumna("fecha_defuncion").setNombreVisual("Fecha defuncion");
        setSolicitud.getTab_seleccion().getColumna("representante_actual").setNombreVisual("Representante");
        setSolicitud.getTab_seleccion().getColumna("nombre_fallecido").setFiltro(true);
        setSolicitud.getTab_seleccion().setRows(12);
        setSolicitud.setRadio();
        setSolicitud.getGri_cuerpo().setHeader(gri_busca1);
        setSolicitud.getBot_aceptar().setMetodo("cemeneterioMovimiento");
        setSolicitud.setWidth("65%");
        setSolicitud.setHeader("BUSQUEDA POR PARAMETROS");
        agregarComponente(setSolicitud);

        List lista = new ArrayList();
        Object fila7[] = {
            "0", "Cédula Fallecido"
        };
        Object fila8[] = {
            "1", "Nombres Fallecido"
        };
        Object fila9[] = {
            "2", "Fecha Defuncion"
        };

        lista.add(fila8);;
        lista.add(fila7);;
        lista.add(fila9);;


        cmbOpcion1.setCombo(lista);
        cmbOpcion1.eliminarVacio();

        Grid griPriEx = new Grid();
        griPriEx.setColumns(3);

        Grid gri_busca = new Grid();
        gri_busca.setColumns(5);
        gri_busca.getChildren().add(new Etiqueta("Buscar por :"));
        gri_busca.getChildren().add(cmbOpcion1);
        griPriEx.getChildren().add(gri_busca);

        gri_busca.getChildren().add(new Etiqueta("Ingrese: "));
        texBusqueda1.setSize(50);
        gri_busca.getChildren().add(texBusqueda1);

        griPriEx.getChildren().add(gri_busca);
        bar_botones.agregarComponente(griPriEx);

        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("busquedaInfoExcel");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca.getChildren().add(bot_buscar);

        setExcel.setId("setExcel");
        setExcel.setSeleccionTabla("SELECT cod_tabla,ex_cedula,ex_nombres,ex_fecha_defuncion,(CASE WHEN ex_lugar = 'NICHO' THEN ex_lugar + '   SERIE:' + ex_bloque + '-' + ex_fila + '-' + ex_numero ELSE ex_lugar + '   Bloque:' + ex_bloque + '   Fila:' + ex_fila + '  Numero:' + ex_numero END) AS LUGAR,ex_piso,(CASE WHEN ex_lugar = 'NICHO' THEN ex_lugar + '   SERIE:' + nuevo_bloque + '-' + nuevo_fila + '-' +  convert(VARCHAR,nuevo_numero) ELSE ex_lugar + '   Bloque:' + nuevo_bloque + '   Fila:' + nuevo_fila + '  Numero:' + convert(VARCHAR,nuevo_numero) END) AS LUGAR_nuevo,nuevo_piso FROM CMT_MIGRACION_DATOS ", "cod_tabla");
        setExcel.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setExcel.getTab_seleccion().getColumna("ex_nombres").setLongitud(50);
        setExcel.getTab_seleccion().getColumna("ex_cedula").setLongitud(25);
        setExcel.getTab_seleccion().getColumna("ex_fecha_defuncion").setLongitud(25);
        setExcel.getTab_seleccion().getColumna("LUGAR").setLongitud(50);
        setExcel.getTab_seleccion().getColumna("LUGAR_nuevo").setLongitud(50);
        setExcel.getTab_seleccion().getColumna("ex_cedula").setNombreVisual("Cedula");
        setExcel.getTab_seleccion().getColumna("ex_nombres").setNombreVisual("Fallecido");
        setExcel.getTab_seleccion().getColumna("ex_fecha_defuncion").setNombreVisual("Fecha defuncion");
        setExcel.getTab_seleccion().getColumna("LUGAR").setNombreVisual("Catastro Antiguo");
        setExcel.getTab_seleccion().getColumna("LUGAR_nuevo").setNombreVisual("Catastro Nuevo");
        setExcel.getTab_seleccion().setRows(14);
        setExcel.setRadio();
        setExcel.getGri_cuerpo().setHeader(gri_busca);//consultaFallecido
        setExcel.getBot_aceptar().setMetodo("insertCatastroNuevo");
        setExcel.setWidth("65%");
        setExcel.setHeader("BUSQUEDA POR PARAMETROS EN ARCHIVO EXCEL");
        agregarComponente(setExcel);

        List lis = new ArrayList();
        Object fil1[] = {
            "Pendiente", "Pendiente"
        };
        Object fil2[] = {
            "Subido", "Subido"
        };
        lis.add(fil1);;
        lis.add(fil2);;
        cmbRentas.setCombo(lis);

        List li = new ArrayList();
        Object fi1[] = {
            "Pendiente", "Pendiente"
        };
        Object fi2[] = {
            "Subido", "Subido"
        };
        li.add(fi1);;
        li.add(fi2);;
        cmbArchivo.setCombo(lis);

        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);


        diaArchivo.setId("diaArchivo");
        diaArchivo.setTitle("Parametros Catastro Levantado");
        diaArchivo.setWidth("20%"); //siempre en porcentajes  ancho
        diaArchivo.setHeight("20%");//siempre porcentaje   alto
        diaArchivo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaArchivo.getBot_aceptar().setMetodo("aceptoAnticipo");
        gridA.setColumns(4);
        agregarComponente(diaArchivo);

        diaRentas.setId("diaRentas");
        diaRentas.setTitle("Parametros Rentas Cementerios");
        diaRentas.setWidth("20%"); //siempre en porcentajes  ancho
        diaRentas.setHeight("20%");//siempre porcentaje   alto
        diaRentas.setResizable(false); //para que no se pueda cambiar el tamaño
        diaRentas.getBot_aceptar().setMetodo("aceptoAnticipo");
        gridR.setColumns(4);
        agregarComponente(diaRentas);

        diaInfor.setId("diaInfor");
        diaInfor.setTitle("Comentario");
        diaInfor.setWidth("20%"); //siempre en porcentajes  ancho
        diaInfor.setHeight("20%");//siempre porcentaje   alto
        diaInfor.setResizable(false); //para que no se pueda cambiar el tamaño
        diaInfor.getBot_aceptar().setMetodo("ejecutarProceso");
        gridI.setColumns(4);
        agregarComponente(diaInfor);

        diaFallecidos.setId("diaFallecidos");
        diaFallecidos.setTitle("DATOS DE OCUPACIÓN DE SITIO"); //titulo
        diaFallecidos.setResizable(false); //para que no se pueda cambiar el tamaño
        diaFallecidos.getBot_aceptar().setDisabled(true);
        diaFallecidos.getBot_cancelar().setMetodo("regresaForma");
        gridfa.setColumns(4);
        agregarComponente(diaFallecidos);

        diaReporte.setId("diaReporte");
        diaReporte.setTitle("PARAMETROS PARA REPORTE"); //titulo
        diaReporte.setWidth("30%"); //siempre en porcentajes  ancho
        diaReporte.setHeight("30%");//siempre porcentaje   alto
        diaReporte.setResizable(false); //para que no se pueda cambiar el tamaño
        diaReporte.getBot_aceptar().setMetodo("aceptoAnticipo");
        gridre.setColumns(4);
        agregarComponente(diaReporte);
    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabFallecido.setId("tabFallecido");
        tabFallecido.setConexion(conSqlapli);
        tabFallecido.setTabla("FALLECIDOS", "ID_FALLECIDO", 1);
        if (autBusca.getValue() == null) {
            tabFallecido.setCondicion("ID_FALLECIDO=-1");
        } else {
            tabFallecido.setCondicion("ID_FALLECIDO in (" + autBusca.getValor() + ")");
        }

        tabFallecido.getColumna("cedula_fallecido").setNombreVisual("CEDULA FALLECIDO");
        tabFallecido.getColumna("nombre_fallecido").setNombreVisual("FALLECIDO");
        tabFallecido.getColumna("fecha_defuncion").setNombreVisual("FECHA DEFUNCION");
        tabFallecido.getColumna("lugar_actual").setNombreVisual("CATASTRO");
        tabFallecido.getColumna("cedula_representante").setNombreVisual("C.I. REPRESESNTANTE");
        tabFallecido.getColumna("representante_actual").setNombreVisual("REPRESENTANTE");
        tabFallecido.getColumna("estado_fallecido").setVisible(false);
        tabFallecido.getColumna("login_usuario").setVisible(false);
        tabFallecido.getColumna("tipo_pago").setNombreVisual("TIPO PAGO");
        tabFallecido.getColumna("autorizacion_pago").setVisible(false);
        tabFallecido.getColumna("fecha_sistema").setVisible(false);
        tabFallecido.getColumna("login_act").setVisible(false);
        tabFallecido.getColumna("fecha_act").setVisible(false);
        tabFallecido.getColumna("login_del").setVisible(false);
        tabFallecido.getColumna("fecha_del").setVisible(false);
        tabFallecido.getColumna("bloqueo").setEtiqueta();
        tabFallecido.getColumna("nuevo_piso").setVisible(false);
        tabFallecido.getColumna("ID_FALLECIDO").setVisible(false);
        tabFallecido.getColumna("NUEVO_LUGAR").setLongitud(10);
        tabFallecido.getColumna("lugar_actual").setLongitud(70);
        tabFallecido.getColumna("cedula_fallecido").setLongitud(20);
        tabFallecido.getColumna("nombre_fallecido").setLongitud(80);
        tabFallecido.getColumna("fecha_defuncion").setLongitud(20);
        tabFallecido.getColumna("lugar_actual").setLongitud(50);
        tabFallecido.getColumna("cedula_representante").setLongitud(20);
        tabFallecido.getColumna("representante_actual").setLongitud(80);
        tabFallecido.getColumna("tipo_pago").setLongitud(25);
        tabFallecido.getColumna("nombre_fallecido").setFiltro(true);
        tabFallecido.getColumna("cedula_fallecido").setFiltro(true);
        tabFallecido.getColumna("cedula_representante").setFiltro(true);
        tabFallecido.getColumna("representante_actual").setFiltro(true);
        tabFallecido.setCampoOrden("nombre_fallecido asc");
//        tabFallecido.setGenerarPrimaria(true);
        tabFallecido.setCampoPrimaria("ID_FALLECIDO");
        tabFallecido.setTipoFormulario(true);
        tabFallecido.getGrid().setColumns(6);
        tabFallecido.agregarRelacion(tabMovimientoFallecido);
        tabFallecido.agregarRelacion(tabRepresentante);
        tabFallecido.setLectura(true);
        tabFallecido.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setMensajeWarn("<CENTER><B>DATOS DE FALLECIDO RENTAS CEMENTERIO<CENTER><B>");
        pnt.setPanelTabla(tabFallecido);

        tabRepresentante.setId("tabRepresentante");
        tabRepresentante.setConexion(conSqlapli);
        tabRepresentante.setTabla("REPRESENTANTE", "id_representante", 2);
        tabRepresentante.getColumna("cedula_represent").setNombreVisual("Cedula Representante");
        tabRepresentante.getColumna("nombres_represent").setNombreVisual("Representante");
        tabRepresentante.getColumna("direccion_represent").setNombreVisual("Direccion");
        tabRepresentante.getColumna("telefono_represent").setNombreVisual("Telefono");
        tabRepresentante.getColumna("estado_represent").setNombreVisual("Estado");
        tabRepresentante.getColumna("estado_represent").setVisible(false);
        tabRepresentante.setRows(10);
        tabRepresentante.setLectura(true);
        tabRepresentante.dibujar();
        PanelTabla pnre = new PanelTabla();
        pnre.setMensajeWarn("DATOS REPRESENTANTE");
        pnre.setPanelTabla(tabRepresentante);

        tabMovimientoFallecido.setId("tabMovimientoFallecido");
        tabMovimientoFallecido.setConexion(conSqlapli);
        tabMovimientoFallecido.setTabla("CONTROL_ARRENDAMIENTO", "ID_CONTROL", 3);
        tabMovimientoFallecido.getColumna("fecha_ingreso").setNombreVisual("FECHA INGRESO");
        tabMovimientoFallecido.getColumna("num_titulo").setNombreVisual("NUM. LIQUIDACION");
        tabMovimientoFallecido.getColumna("valor_pago").setNombreVisual("VALOR");
        tabMovimientoFallecido.getColumna("anio_inicio").setNombreVisual("DESDE");
        tabMovimientoFallecido.getColumna("anio_fin").setNombreVisual("HASTA");
        tabMovimientoFallecido.getColumna("observaciones").setNombreVisual("OBSERVACIONES");
        tabMovimientoFallecido.getColumna("anio_arrendamiento").setNombreVisual("PERIODO(AÑOS)");
        tabMovimientoFallecido.getColumna("moneda").setVisible(false);
        tabMovimientoFallecido.getColumna("fecha_sistema").setVisible(false);
        tabMovimientoFallecido.getColumna("fecha_act").setVisible(false);
        tabMovimientoFallecido.getColumna("fecha_ingreso").setLongitud(30);
        tabMovimientoFallecido.getColumna("num_titulo").setLongitud(30);
        tabMovimientoFallecido.getColumna("valor_pago").setLongitud(25);
        tabMovimientoFallecido.getColumna("anio_inicio").setLongitud(30);
        tabMovimientoFallecido.getColumna("anio_fin").setLongitud(30);
        tabMovimientoFallecido.getColumna("observaciones").setLongitud(80);
        tabMovimientoFallecido.getColumna("anio_arrendamiento").setLongitud(25);
        tabMovimientoFallecido.getColumna("antecedentes").setLongitud(90);
//        tabMovimientoFallecido.setTipoFormulario(true);
//        tabMovimientoFallecido.getGrid().setColumns(2);
        tabMovimientoFallecido.setRows(10);
        tabMovimientoFallecido.setLectura(true);
        tabMovimientoFallecido.dibujar();
        PanelTabla pnr = new PanelTabla();
        pnr.setMensajeWarn("DETALLE ARRENDAMIENTO");
        pnr.setPanelTabla(tabMovimientoFallecido);

        tabLevantamiento.setId("tabLevantamiento");
        tabLevantamiento.setTabla("cmt_migracion_datos", "cod_tabla", 4);
        tabLevantamiento.getColumna("verifica").setMetodoChange("mostrarRegistro");
        tabLevantamiento.getColumna("verifica").setCheck();
        tabLevantamiento.getColumna("cod_tabla").setVisible(false);
        tabLevantamiento.getColumna("bloqueo").setVisible(false);
        tabLevantamiento.getColumna("ren_ide_fallecido").setVisible(false);
        tabLevantamiento.getColumna("ex_cedula").setVisible(false);
        tabLevantamiento.getColumna("ex_piso").setVisible(false);
        tabLevantamiento.getColumna("nuevo_piso").setVisible(false);
        tabLevantamiento.getColumna("nuevo_ubicacion").setVisible(false);
        tabLevantamiento.setCondicion("cod_tabla =0");

//        tabLevantamiento.setLectura(true);
        tabLevantamiento.setTipoFormulario(true);
        tabLevantamiento.getGrid().setColumns(6);
        tabLevantamiento.dibujar();

        PanelTabla pnl = new PanelTabla();
        pnl.setMensajeWarn("<CENTER><B>DATOS DE FALLECIDO LEVANTAMIENTO CEMENTERIO</B></CENTER>");
        pnl.setPanelTabla(tabLevantamiento);

        Division div = new Division();
        div.setId("div");
        div.dividir2(pnt, pnl, "30%", "h");

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

    public void abrirBusqueda() {
        System.out.println("estoy buscando");
        setSolicitud.dibujar();
        texBusqueda.limpiar();
        setSolicitud.getTab_seleccion().limpiar();
    }

    public void abrirBusquedaExcel() {
        setExcel.dibujar();
        String cadena = tabFallecido.getValor("nombre_fallecido");
        String separador = " "; // un espacio en blanco
        String[] arregloDeSubCadenas = cadena.split(separador);
        texBusqueda1.setValue(arregloDeSubCadenas[0]);
        setExcel.getTab_seleccion().limpiar();
    }
    /*
     * Metodo para busqueda
     */

    public void busquedaInfo() {
        if (cmbOpcion.getValue().equals("0")) {
            buscaCedula();
        } else if (cmbOpcion.getValue().equals("1")) {
            buscaApellidoFallecido();
        } else if (cmbOpcion.getValue().equals("2")) {
            buscaFecha();
        } else if (cmbOpcion.getValue().equals("3")) {
            buscarRepresentante();
        } else if (cmbOpcion.getValue().equals("4")) {
            buscarCedulaRepresentante();
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
        }
    }

    public void busquedaInfoExcel() {
        System.out.println("cmbOpcion1.getValue()<<<<<<<<<<<" + cmbOpcion1.getValue());
        if (cmbOpcion1.getValue().equals("0")) {
            buscaCedulaExcel();
        } else if (cmbOpcion1.getValue().equals("1")) {
            buscaApellidoFallecidoExcel();
        } else if (cmbOpcion1.getValue().equals("2")) {
            buscaFechaExcel();
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
        }
    }

    public void buscaCedula() {
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("select id_fallecido,cedula_fallecido,nombre_fallecido,fecha_defuncion,lugar_actual,representante_actual,cedula_representante "
                    + "from FALLECIDOS where estado_fallecido = 'ACTIVO' and bloqueo is null and  cedula_fallecido LIKE '" + texBusqueda.getValue() + "%' order by nombre_fallecido desc ");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscaApellidoFallecido() {

        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("select id_fallecido,cedula_fallecido,nombre_fallecido,fecha_defuncion,lugar_actual,representante_actual,cedula_representante "
                    + "from FALLECIDOS where estado_fallecido = 'ACTIVO' and bloqueo is null and nombre_fallecido LIKE '" + texBusqueda.getValue() + "%' order by nombre_fallecido desc");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscaFecha() {

        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("select id_fallecido,cedula_fallecido,nombre_fallecido,fecha_defuncion,lugar_actual,representante_actual,cedula_representante "
                    + "from FALLECIDOS where estado_fallecido = 'ACTIVO' and bloqueo is null and fecha_defuncion ='" + texBusqueda.getValue() + "' order by nombre_fallecido desc");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarCedulaRepresentante() {
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("select id_fallecido,cedula_fallecido,nombre_fallecido,fecha_defuncion,lugar_actual,representante_actual,cedula_representante "
                    + "from FALLECIDOS where estado_fallecido = 'ACTIVO' and bloqueo is null and cedula_representante LIKE '" + texBusqueda.getValue() + "%' order by nombre_fallecido desc");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarRepresentante() {
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("select id_fallecido,cedula_fallecido,nombre_fallecido,fecha_defuncion,lugar_actual,representante_actual,cedula_representante "
                    + "from FALLECIDOS where estado_fallecido = 'ACTIVO' and bloqueo is null and representante_actual LIKE '" + texBusqueda.getValue() + "%' order by nombre_fallecido");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscaCedulaExcel() {
        if (texBusqueda1.getValue() != null && texBusqueda1.getValue().toString().isEmpty() == false) {
            setExcel.getTab_seleccion().setSql("SELECT cod_tabla,ex_cedula,ex_nombres,ex_fecha_defuncion,(CASE WHEN ex_lugar = 'NICHO' THEN ex_lugar + '   SERIE:' + ex_bloque + '-' + ex_fila + '-' + ex_numero ELSE ex_lugar + '   Bloque:' + ex_bloque + '   Fila:' + ex_fila + '  Numero:' + ex_numero END) AS LUGAR,ex_piso,(CASE WHEN ex_lugar = 'NICHO' THEN ex_lugar + '   SERIE:' + nuevo_bloque + '-' + nuevo_fila + '-' +  convert(VARCHAR,nuevo_numero) ELSE ex_lugar + '   Bloque:' + nuevo_bloque + '   Fila:' + nuevo_fila + '  Numero:' + convert(VARCHAR,nuevo_numero) END) AS LUGAR_nuevo,nuevo_piso "
                    + "FROM CMT_MIGRACION_DATOS where isnull(bloqueo,'')='' and ex_cedula  LIKE '" + texBusqueda1.getValue() + "%' order by ex_nombres");
            setExcel.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscaApellidoFallecidoExcel() {
        if (texBusqueda1.getValue() != null && texBusqueda1.getValue().toString().isEmpty() == false) {
            setExcel.getTab_seleccion().setSql("SELECT cod_tabla,ex_cedula,ex_nombres,ex_fecha_defuncion,(CASE WHEN ex_lugar = 'NICHO' THEN ex_lugar + '   SERIE:' + ex_bloque + '-' + ex_fila + '-' + ex_numero ELSE ex_lugar + '   Bloque:' + ex_bloque + '   Fila:' + ex_fila + '  Numero:' + ex_numero END) AS LUGAR,ex_piso,(CASE WHEN ex_lugar = 'NICHO' THEN ex_lugar + '   SERIE:' + nuevo_bloque + '-' + nuevo_fila + '-' +  convert(VARCHAR,nuevo_numero) ELSE ex_lugar + '   Bloque:' + nuevo_bloque + '   Fila:' + nuevo_fila + '  Numero:' + convert(VARCHAR,nuevo_numero) END) AS LUGAR_nuevo,nuevo_piso "
                    + "FROM CMT_MIGRACION_DATOS where isnull(bloqueo,'')='' and ex_nombres LIKE '%" + texBusqueda1.getValue() + "%' order by ex_nombres");
            setExcel.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscaFechaExcel() {
        if (texBusqueda1.getValue() != null && texBusqueda1.getValue().toString().isEmpty() == false) {
            setExcel.getTab_seleccion().setSql("SELECT cod_tabla,ex_cedula,ex_nombres,ex_fecha_defuncion,(CASE WHEN ex_lugar = 'NICHO' THEN ex_lugar + '   SERIE:' + ex_bloque + '-' + ex_fila + '-' + ex_numero ELSE ex_lugar + '   Bloque:' + ex_bloque + '   Fila:' + ex_fila + '  Numero:' + ex_numero END) AS LUGAR,ex_piso,(CASE WHEN ex_lugar = 'NICHO' THEN ex_lugar + '   SERIE:' + nuevo_bloque + '-' + nuevo_fila + '-' +  convert(VARCHAR,nuevo_numero) ELSE ex_lugar + '   Bloque:' + nuevo_bloque + '   Fila:' + nuevo_fila + '  Numero:' + convert(VARCHAR,nuevo_numero) END) AS LUGAR_nuevo,nuevo_piso "
                    + "FROM CMT_MIGRACION_DATOS where isnull(bloqueo,'')='' and ex_fecha_defuncion ='" + texBusqueda1.getValue() + "' order by ex_nombres");
            setExcel.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void cemeneterioMovimiento() {
        limpiar();
        autBusca.setValor(setSolicitud.getValorSeleccionado());
        dibujarPantalla();
        setSolicitud.cerrar();
        tabFallecido.insertar();
        tabMovimientoFallecido.insertar();
        System.out.println("setTabla.getValorSeleccionado()<<<<<<<" + setSolicitud.getValorSeleccionado());
        TablaGenerica tabDato = cementerioM.getDatosFallecidos(setSolicitud.getValorSeleccionado());
        System.out.println("setTabla.tabDato<<<<<<<" + setSolicitud.getValorSeleccionado());

        if (!tabDato.isEmpty()) {
            System.out.println("id_fallecido()<<<<<<<" + tabDato.getValor("id_fallecido"));
//            tabFallecido.setValor("id_fallecido", tabDato.getValor("id_fallecido"));
            tabFallecido.setValor("cedula_fallecido", tabDato.getValor("cedula_fallecido").toLowerCase());
            tabFallecido.setValor("nombre_fallecido", tabDato.getValor("nombre_fallecido"));
            tabFallecido.setValor("fecha_defuncion", tabDato.getValor("fecha_defuncion"));
            tabFallecido.setValor("lugar_actual", tabDato.getValor("lugar_actual"));
            tabFallecido.setValor("tipo_pago", tabDato.getValor("tipo_pago"));
            tabFallecido.setValor("cedula_representante", tabDato.getValor("cedula_representante"));
            tabFallecido.setValor("representante_actual", tabDato.getValor("representante_actual"));

            tabMovimientoFallecido.setValor("id_control", tabDato.getValor("id_control"));
            tabMovimientoFallecido.setValor("fecha_ingreso", tabDato.getValor("fecha_ingreso"));
            tabMovimientoFallecido.setValor("num_titulo", tabDato.getValor("num_titulo"));
            tabMovimientoFallecido.setValor("valor_pago", tabDato.getValor("valor_pago"));
            tabMovimientoFallecido.setValor("anio_inicio", tabDato.getValor("anio_inicio"));
            tabMovimientoFallecido.setValor("anio_fin", tabDato.getValor("anio_fin"));
            tabMovimientoFallecido.setValor("anio_arrendamiento", tabDato.getValor("anio_arrendamiento"));

            setSolicitud.cerrar();
            utilitario.addUpdate("tabMovimiento");
        } else {
            utilitario.agregarMensajeError("datos", "No Se Encuentra Registrado");
        }
    }

    public void insertCatastroNuevo() {
        System.out.println("setRegistros.getValorSeleccionado()<<<<<<" + setExcel.getValorSeleccionado());
        intRegistro = Integer.parseInt(setExcel.getValorSeleccionado());
        TablaGenerica tabDato = cementerioM.insertCatastroNuevo(Integer.parseInt(setExcel.getValorSeleccionado()));
        if (!tabDato.isEmpty()) {
            tabFallecido.setValor("nuevo_lugar", tabDato.getValor("ex_lugar"));
            tabFallecido.setValor("nuevo_bloque", tabDato.getValor("nuevo_bloque"));
            tabFallecido.setValor("nuevo_fila", tabDato.getValor("nuevo_fila"));
            tabFallecido.setValor("nuevo_numero", tabDato.getValor("nuevo_numero"));
            tabFallecido.setValor("nuevo_piso", tabDato.getValor("nuevo_piso"));
            utilitario.addUpdate("tabFallecido");
            setExcel.cerrar();
            actualizaLista();
        } else {
            utilitario.agregarMensajeError("Datos", "No Se Encuentra Registrado");
        }
    }

    public void ejecutaSp() {
        //Compara si el catastro de rentas es igual al del levantamiento
        String lugar = "", cadena = "", sitio = "", numero = "", cadena1 = "", caden = "", ubicacion = "";
        ubicacion = tabLevantamiento.getValor("ex_lugar").replace(" ", "").toUpperCase();

        if (ubicacion.equals("NICHO")) {
            lugar = "SERIE:";
            sitio = "NIVEL:";
            numero = "NUM:";
        }

        if (ubicacion.equals("SITIO")) {
            lugar = "BLOQUE:";
            sitio = "FILA:";
            numero = "NUM:";
        }

        cadena = tabLevantamiento.getValor("ex_lugar") + "" + lugar + "" + tabLevantamiento.getValor("ex_bloque") + "" + sitio + "" + tabLevantamiento.getValor("ex_fila") + "" + numero + "" + tabLevantamiento.getValor("ex_numero");
        caden = cadena.replace(" ", "").toUpperCase();
        System.err.println("1" + caden);

        cadena1 = tabFallecido.getValor("lugar_actual").replace(" ", "").toUpperCase();
        System.err.println("2" + cadena1);

        if (cadena1.compareTo(caden) == 0) {
            //Verifica si el catastro existe o no
            if (tabLevantamiento.getValor("cod_tabla") != null) {
                Integer cod = cementerioM.getRegFallecido(Integer.parseInt(tabFallecido.getValor("id_fallecido") + ""), tabFallecido.getValor("cedula_fallecido"));
                System.err.println(cod);
                if (cod < 1) {
                    if (tabFallecido.getValor("bloqueo") == null) {
                        TablaGenerica tabDato = cementerioM.getVerDispSitio(tabFallecido.getValor("nuevo_lugar").toUpperCase(), tabFallecido.getValor("nuevo_bloque").toUpperCase(), tabFallecido.getValor("nuevo_numero").toUpperCase(), tabFallecido.getValor("nuevo_fila").toUpperCase());
                        if (!tabDato.isEmpty()) {
                            if (Integer.parseInt(tabDato.getValor("conteo")) > 0) {
                                intCatastro = Integer.parseInt(tabDato.getValor("IDE_CATASTRO"));
                                if (tabDato.getValor("DISPONIBLE_OCUPADO").equals("DISPONIBLE")) {
//                            ejecutarLugar();
                                    ejecutarProceso();
                                } else {
                                    diaInfor.setDialogo(gri);
                                    Grid griRentas = new Grid();
                                    griRentas.setColumns(2);
                                    griRentas.getChildren().add(txInformacion);
                                    gridI.getChildren().add(griRentas);
                                    diaInfor.setDialogo(gridI);
                                    diaInfor.dibujar();
                                }
                            } else {
                                intCatastro = 0;
                                ejecutarLugar();
                                ejecutarProceso();
                            }
                        } else {
                            intCatastro = 0;
                            ejecutarLugar();
                            ejecutarProceso();
                        }
                    } else {
                        utilitario.agregarMensaje("Este registro se encuentra guardado", "");
                    }
                } else {
                    utilitario.agregarMensaje("Fallecido se encuentra registrado", "");
                }
            } else {
                utilitario.agregarMensaje("Debe seleccionar un registro del levanatamiento de excel", "");
            }
        } else {
            utilitario.agregarMensaje("Registo de Catatastro de rentas debe ser igual ", "a registro de catastro levantamiento");
        }
    }

    public void ejecutarLugar() {
        System.out.println("ingresado catastro");
        cementerioM.setInsertCatastro_SP(tabFallecido.getValor("nuevo_lugar").toUpperCase(), tabFallecido.getValor("nuevo_bloque").toUpperCase(), tabFallecido.getValor("nuevo_fila").toUpperCase(), tabFallecido.getValor("nuevo_numero").toUpperCase(), tabFallecido.getValor("nuevo_piso").toUpperCase(), tabConsulta.getValor("NICK_USUA"), tabFallecido.getValor("lugar_actual").toUpperCase());
    }

    public void ejecutarProceso() {
        if (intCatastro > 0) {
            cementerioM.setUpdateCatastro(intCatastro,tabFallecido.getValor("lugar_actual").toUpperCase());
        }
        diaInfor.cerrar();
        System.out.println("entreamo a sp ");
        Date fecha1 = utilitario.DeStringADateformato2(tabFallecido.getValor("fecha_defuncion"));
        String str_fecha1 = utilitario.DeDateAStringformato2(fecha1);
        Integer codigo = 0;
        Integer codigo1 = 0;
        TablaGenerica tabDato = cementerioM.getIdeDetalleMovimiento("CMT_FALLECIDO");
        if (!tabDato.isEmpty()) {
            TablaGenerica tabDatos = cementerioM.getMaxFallecidosIde();
            if (!tabDatos.isEmpty()) {
                codigo1 = Integer.parseInt(tabDatos.getValor("maximo"));
                codigo = Integer.parseInt(tabDato.getValor("MAXIMO_BLOQ")) + 1;
                if (codigo1 == codigo) {
                    utilitario.agregarMensajeInfo("Clave primaria en cmt_fallecido duplicada", null);
                } else {
                    System.out.println("setRegistros.getValorSeleccionado()<<<<<<" + intRegistro);
                    cementerioM.set_updateBloqueExcel(intRegistro);
                    System.out.println("actualizando fallecido rentas");
                    cementerioM.set_updateFallecidoRentas(tabFallecido.getValor("id_fallecido"), tabFallecido.getValor("nuevo_lugar"),
                            tabFallecido.getValor("nuevo_bloque"), tabFallecido.getValor("nuevo_fila"), tabFallecido.getValor("nuevo_numero").toUpperCase(), tabFallecido.getValor("nuevo_piso"));

                    cementerioM.getDatosFallecido(Integer.parseInt(tabFallecido.getValor("id_fallecido")), "" + tabFallecido.getValor("cedula_fallecido") + ""
                            , "" + tabFallecido.getValor("nombre_fallecido") + "", "" + str_fecha1 + "", "" + tabConsulta.getValor("NICK_USUA") + ""
                            , "" + tabFallecido.getValor("lugar_actual").toUpperCase() + "", tabFallecido.getValor("nuevo_lugar").toUpperCase(), tabFallecido.getValor("nuevo_fila").toUpperCase()
                            , tabFallecido.getValor("nuevo_bloque").toUpperCase(), tabFallecido.getValor("nuevo_numero").toUpperCase(), intCatastro, codigo
                            , tabFallecido.getValor("lugar_actual").toUpperCase());//
                    for (int i = 0; i < tabRepresentante.getTotalFilas(); i++) {
                        cementerioM.CMT_DATOS_REPRESENTANTE_INSERT(tabRepresentante.getValor(i, "id_representante"), String.valueOf(codigo), tabConsulta.getValor("NICK_USUA"), tabRepresentante.getValor(i, "cedula_represent"), tabRepresentante.getValor(i, "nombres_represent"), tabRepresentante.getValor(i, "direccion_represent"), tabRepresentante.getValor(i, "telefono_represent"), tabRepresentante.getValor(i, "estado_represent"));
                    }
                    for (int i = 0; i < tabMovimientoFallecido.getTotalFilas(); i++) {
                        cementerioM.CMT_MOVIMIENTOS_FALLECIDOS_INSERT(tabMovimientoFallecido.getValor(i, "id_CONTROL"), tabFallecido.getValor("id_fallecido")
                                , tabConsulta.getValor("NICK_USUA"), tabFallecido.getValor("lugar_actual"), tabFallecido.getValor("tipo_pago")
                                , tabFallecido.getValor("cedula_fallecido"), tabFallecido.getValor("nombre_fallecido"), tabFallecido.getValor("fecha_defuncion")
                                , tabFallecido.getValor("NUEVO_LUGAR"), tabFallecido.getValor("NUEVO_BLOQUE"), tabFallecido.getValor("NUEVO_NUMERO").toUpperCase()
                                , tabFallecido.getValor("NUEVO_FILA"));
                    }
                    utilitario.agregarMensaje("Informacion ingresada correctamente", "");
                    cementerioM.set_updateBloque("'CMT_FALLECIDO'", codigo);

                    TablaGenerica tabInfor = cementerioM.getEstadoFallecido(tabFallecido.getValor("id_fallecido"));
                    if (!tabInfor.isEmpty()) {
                        tabFallecido.setValor("bloqueo", tabInfor.getValor("bloqueo"));
                        utilitario.addUpdate("tabFallecido");
                    }
                    cementerioM.setUpdateDesHabilita(tabFallecido.getValor("NUEVO_LUGAR").toUpperCase(),tabFallecido.getValor("NUEVO_BLOQUE")
                            ,tabFallecido.getValor("NUEVO_NUMERO").toUpperCase(),tabFallecido.getValor("NUEVO_FILA"));
                }
            } else {
                utilitario.agregarMensaje("Clave primaria no corresponde", "");
            }
        } else {
            utilitario.agregarMensaje("Clave primaria de fallecido no corresponde", "");
        }
    }

    public void buscarSolicitud() {
        System.out.println("sigo ak");
        if (!getBuscaFecha1().isEmpty()) {
            tabFallecido.setCondicion(getBuscaFecha1());
            tabFallecido.ejecutarSql();
            utilitario.addUpdate("tabFallecido");
        }
    }

    private String getBuscaFecha1() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        System.out.println("fecha1.getFecha()<<<<<<<<<" + fecha1.getFecha());
        System.out.println("fecha2.getFecha()<<<<<<<<<" + fecha2.getFecha());
        str_filtros = " where id_fallecido in (select ID_FALLECIDO from fallecidos a, (SELECT max (anio_fin)as anio,a.ID_FALLECIDO as fa FROM FALLECIDOs a , CONTROL_ARRENDAMIENTO b \n"
                + "WHERE a.id_fallecido=b.id_fallecido and anio_fin <'" + fecha2.getFecha() + "'  group by a.ID_FALLECIDO  )b \n"
                + "where a.ID_FALLECIDO=fa )";
        return str_filtros;
    }

    private String getBuscaGeneral() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "",
                anio = String.valueOf(utilitario.getAnio(utilitario.getFechaActual())),
                mes = String.valueOf(utilitario.getMes(utilitario.getFechaActual()) - 1),
                dia = "01";

//        str_filtros = "estado <> (SELECT ID_ESTADO FROM RESUG_ESTADO where estado = 'Negado') and estado <> (SELECT ID_ESTADO FROM RESUG_ESTADO where estado = 'Concluido') and fecha1 between '" + dia + "/" + mes + "/" + anio + "' and '" + utilitario.getFechaActual() + "'";
        return str_filtros;
    }

    public void actualizaLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabLevantamiento.setCondicion(getFiltrosAcceso());
            tabLevantamiento.ejecutarSql();
            utilitario.addUpdate("tabLevantamiento");
        }
    }

    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (setExcel.getValorSeleccionado() != null) {

            str_filtros = "cod_tabla = " + Integer.parseInt(setExcel.getValorSeleccionado());

        }
//        else {
//            utilitario.agregarMensajeInfo("Filtros no válidos",
//                    "Debe seleccionar valor");
//        }
        return str_filtros;
    }

    @Override
    public void insertar() {
//        if (tabFallecido.isFocus()) {
//            autBusca.limpiar();
//            utilitario.addUpdate("aut_busca");
//            tab_tabla1.limpiar();
        tabFallecido.insertar();
//            tab_tabla2.limpiar();
        tabMovimientoFallecido.insertar();
//        }
    }

    public void mostrarRegistro() {
        if (tabLevantamiento.getValor("verifica") != null && tabLevantamiento.getValor("verifica").toString().isEmpty() == false) {
            TablaGenerica tabDato = cementerioM.getCatasFallecido(tabLevantamiento.getValor("EX_LUGAR"), tabLevantamiento.getValor("NUEVO_BLOQUE"), tabLevantamiento.getValor("NUEVO_NUMERO"), Integer.parseInt(tabLevantamiento.getValor("NUEVO_FILA") + ""));
            if (!tabDato.isEmpty()) {
                diaFallecidos.Limpiar();
                diaFallecidos.setDialogo(gridfa);
                gridFa.getChildren().add(setFallecido);
                setFallecido.setId("setFallecido");
                setFallecido.setSql("SELECT CEDULA_FALLECIDO as CEDULA,NOMBRES,FECHA_INGRE as Fec_Ingreso,FECHA_DEFUNCION,DETALLE_CMACC\n"
                        + "from CMT_FALLECIDO_RENOVACION\n"
                        + "where IDE_CATASTRO = " + Integer.parseInt(tabDato.getValor("IDE_CATASTRO")) + "\n"
                        + "order by FECHA_DEFUNCION");
                setFallecido.getColumna("FECHA_DEFUNCION").setLongitud(30);
                setFallecido.getColumna("NOMBRES").setLongitud(70);
                setFallecido.getColumna("DETALLE_CMACC").setLongitud(30);
                setFallecido.setRows(10);
                setFallecido.setLectura(true);
                diaFallecidos.setDialogo(gridFa);
                setFallecido.dibujar();
                diaFallecidos.dibujar();

            } else {
                utilitario.agregarMensajeInfo("No se encuentra registro", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Lugar se encuentra disponible", "No se puede visualizar datos");
        }
    }

    public void regresaForma() {
        diaFallecidos.cerrar();
        tabLevantamiento.setValor("verifica", null);
        utilitario.addUpdate("tabLevantamiento");
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    public void verSector() {
        cmbSector.setCombo("SELECT distinct SECTOR as id,SECTOR FROM CMT_CATASTRO where IDE_LUGAR=" + cmbLugar.getValue() + " order by SECTOR");
        utilitario.addUpdate("cmbSector");//actualiza solo componentes
    }

    public void verModulo() {
        cmbModulo.setCombo("SELECT distinct MODULO as id,MODULO FROM CMT_CATASTRO where IDE_LUGAR=" + cmbLugar.getValue() + " and SECTOR = " + cmbSector.getValue() + "  order by MODULO");
        utilitario.addUpdate("cmbModulo");//actualiza solo componentes
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "RENTAS CEMENTERIO":
                diaRentas.setDialogo(grir);
                Grid griRentas = new Grid();
                griRentas.setColumns(2);
                griRentas.getChildren().add(txRentas);
                griRentas.getChildren().add(cmbRentas);
                gridR.getChildren().add(griRentas);
                diaRentas.setDialogo(gridR);
                diaRentas.dibujar();
                break;
            case "CATASTRO LEVANTADO":
                diaArchivo.setDialogo(gria);
                Grid griArchivo = new Grid();
                griArchivo.setColumns(2);
                griArchivo.getChildren().add(txArchivo);
                griArchivo.getChildren().add(cmbArchivo);
                gridA.getChildren().add(griArchivo);
                diaArchivo.setDialogo(gridA);
                diaArchivo.dibujar();
                break;
            case "REGISTRO SUBIDOS":
                diaReporte.setDialogo(gridre);
                Grid griReporte = new Grid();
                griReporte.setColumns(2);
                Grid griParametro = new Grid();
                griParametro.setColumns(4);
                txtDesde.setSize(5);
                txtHasta.setSize(5);
                griParametro.getChildren().add(etiSector);
                griParametro.getChildren().add(cmbSector);
                griParametro.getChildren().add(etiModulo);
                griParametro.getChildren().add(cmbModulo);
                griParametro.getChildren().add(etiDesde);
                griParametro.getChildren().add(txtDesde);
                griParametro.getChildren().add(etiHasta);
                griParametro.getChildren().add(txtHasta);
                griReporte.getChildren().add(etiLugar);
                griReporte.getChildren().add(cmbLugar);
                griReporte.getChildren().add(griParametro);
                gridRe.getChildren().add(griReporte);
                diaReporte.setDialogo(gridRe);
                diaReporte.dibujar();
                break;
        }
    }

    public void aceptoAnticipo() {
        switch (rep_reporte.getNombre()) {
            case "RENTAS CEMENTERIO":
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("estado", cmbRentas.getValue() + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "CATASTRO LEVANTADO":
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("estado", cmbArchivo.getValue() + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "REGISTRO SUBIDOS":
                Integer bandera = 0;
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("lugar", cmbLugar.getValue() + "");
                p_parametros.put("sector", cmbSector.getValue() + "");
                p_parametros.put("modulo", cmbModulo.getValue() + "");

                if (txtDesde.getValue() != null && !txtDesde.getValue().toString().isEmpty()
                        && txtHasta.getValue() != null && !txtHasta.getValue().toString().isEmpty()) {
                    bandera = 1;
                    p_parametros.put("desde", Integer.parseInt(txtDesde.getValue() + ""));
                    p_parametros.put("hasta", Integer.parseInt(txtHasta.getValue() + ""));
                } else {
                    bandera = 0;
                    p_parametros.put("desde", Integer.parseInt(0 + ""));
                    p_parametros.put("hasta", Integer.parseInt(0 + ""));
                }

                p_parametros.put("bandera", bandera);
                rep_reporte.cerrar();
//                System.err.println(p_parametros+"zcz");
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    public Tabla getTabFallecido() {
        return tabFallecido;
    }

    public void setTabFallecido(Tabla tabFallecido) {
        this.tabFallecido = tabFallecido;
    }

    public Tabla getTabMovimientoFallecido() {
        return tabMovimientoFallecido;
    }

    public void setTabMovimientoFallecido(Tabla tabMovimientoFallecido) {
        this.tabMovimientoFallecido = tabMovimientoFallecido;
    }

    public Tabla getTabRepresentante() {
        return tabRepresentante;
    }

    public void setTabRepresentante(Tabla tabRepresentante) {
        this.tabRepresentante = tabRepresentante;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

    public Conexion getConSqlapli() {
        return conSqlapli;
    }

    public void setConSqlapli(Conexion conSqlapli) {
        this.conSqlapli = conSqlapli;
    }

    public SeleccionTabla getSetSolicitud() {
        return setSolicitud;
    }

    public void setSetSolicitud(SeleccionTabla setSolicitud) {
        this.setSolicitud = setSolicitud;
    }

    public SeleccionTabla getSetExcel() {
        return setExcel;
    }

    public void setSetExcel(SeleccionTabla setExcel) {
        this.setExcel = setExcel;
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

    public int getIntRegistro() {
        return intRegistro;
    }

    public void setIntRegistro(int intRegistro) {
        this.intRegistro = intRegistro;
    }

    public Tabla getTabLevantamiento() {
        return tabLevantamiento;
    }

    public void setTabLevantamiento(Tabla tabLevantamiento) {
        this.tabLevantamiento = tabLevantamiento;
    }

    public Tabla getSetFallecido() {
        return setFallecido;
    }

    public void setSetFallecido(Tabla setFallecido) {
        this.setFallecido = setFallecido;
    }
}
