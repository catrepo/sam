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
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_cementerio.ejb.cementerio;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author l-suntaxi
 */
public class pre_exhumacion_orden extends Pantalla {

    private Tabla tabConsulta = new Tabla();
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla4 = new Tabla();
    private Conexion conSqler = new Conexion();
    private SeleccionTabla setRegistros1 = new SeleccionTabla();
    private SeleccionTabla setSolicitud = new SeleccionTabla();
    private SeleccionTabla setSolicitud1 = new SeleccionTabla();
    private Tabla setSolicitu = new Tabla();
    private Tabla tab_fallecido = new Tabla();
    private Combo cmbOpcion = new Combo();
    private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
    private AutoCompletar aut_busca = new AutoCompletar();
    private SeleccionCalendario sec_rango = new SeleccionCalendario();
    //Contiene todos los elementos de la plantilla
    private Panel panOpcion = new Panel();
    private Dialogo diaDialogoca = new Dialogo();
    private Dialogo dia_fallecido = new Dialogo();
    private Grid gridCa = new Grid();
    private Combo cmbSeleccion = new Combo();
    private Texto texBusqueda = new Texto();
    private Dialogo diaDialogoso = new Dialogo();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private cementerio cementerioM = (cementerio) utilitario.instanciarEJB(cementerio.class);

    public pre_exhumacion_orden() {

//         Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logoactual.png");
        agregarComponente(quinde);

        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonsNavegacion();

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();


        sec_rango.setId("sec_rango");
        sec_rango.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango.setFechaActual();
        agregarComponente(sec_rango);
        agregarComponente(sef_reporte);

        Boton bot_busca2 = new Boton();
        bot_busca2.setValue("Check List");
        bot_busca2.setExcluirLectura(true);
        bot_busca2.setIcon("ui-icon-search");
        bot_busca2.setMetodo("buscaRegistro1");
        bar_botones.agregarBoton(bot_busca2);

        Boton bot_busca1 = new Boton();
        bot_busca1.setValue("Busqueda");
        bot_busca1.setExcluirLectura(true);
        bot_busca1.setIcon("ui-icon-search");
        bot_busca1.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca1);

        List list = new ArrayList();
        Object fila1[] = {
            "0", "Nombres Fallecido"
        };
        Object fila2[] = {
            "1", "Cédula Fallecido"
        };
        Object fila3[] = {
            "2", "Nombres Representante"
        };
        Object fila4[] = {
            "3", "Cedula Representante"
        };
        Object fila5[] = {
            "4", "Numero Liquidacion"
        };
        list.add(fila1);;
        list.add(fila2);;
        list.add(fila3);;
        list.add(fila4);;
        list.add(fila5);;

        cmbOpcion.setCombo(list);
        cmbOpcion.eliminarVacio();

        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT TOP 10 a.IDE_fallecido,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES  , DETALLE_CMACC,FECHA_desde FROM CMT_DETALLE_MOVIMIENTO A\n"
                + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                + "LEFT JOIN  CMT_REPRESENTANTE C  ON A.IDE_FALLECIDO=C.IDE_FALLECIDO\n"
                + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC  ");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(70);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        sef_reporte.setId("sef_reporte");
        bar_botones.agregarReporte();

        Grid griPri = new Grid();
        griPri.setColumns(3);

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

        Boton bot_buscar1 = new Boton();
        bot_buscar1.setValue("Buscar");
        bot_buscar1.setIcon("ui-icon-search");
        bot_buscar1.setMetodo("busquedaInfo");
        bar_botones.agregarBoton(bot_buscar1);
        gri_busca1.getChildren().add(bot_buscar1);

        setSolicitud.setId("setSolicitud");
        setSolicitud.setSeleccionTabla("SELECT TOP 10 a.IDE_fallecido,NUM_LIQUIDACION AS LIQUIDACION,CEDULA_FALLECIDO AS CEDULA,NOMBRES AS FALLECIDO  , "
                + "DOCUMENTO_IDENTIDAD_CMREP AS CEDULA_R,NOMBRES_APELLIDOS_CMREP AS REPRESENTANTE,DETALLE_CMACC AS MOVIMIENTO,FECHA_DEFUNCION,FECHA_desde AS DESDE,FECHA_HASTA AS HASTA FROM CMT_DETALLE_MOVIMIENTO A\n"
                + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                + "LEFT JOIN  CMT_REPRESENTANTE C  ON A.IDE_FALLECIDO=C.IDE_FALLECIDO\n"
                + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC ", "IDE_FALLECIDO");
        setSolicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setSolicitud.getTab_seleccion().getColumna("LIQUIDACION").setLongitud(25);
        setSolicitud.getTab_seleccion().getColumna("CEDULA").setLongitud(15);
        setSolicitud.getTab_seleccion().getColumna("FALLECIDO").setLongitud(50);
        setSolicitud.getTab_seleccion().getColumna("REPRESENTANTE").setLongitud(50);
        setSolicitud.getTab_seleccion().getColumna("CEDULA_R").setLongitud(15);
        setSolicitud.getTab_seleccion().getColumna("MOVIMIENTO").setLongitud(30);
        setSolicitud.getTab_seleccion().getColumna("DESDE").setLongitud(15);
        setSolicitud.getTab_seleccion().getColumna("HASTA").setLongitud(15);
        setSolicitud.getTab_seleccion().setRows(10);
        setSolicitud.setRadio();
        setSolicitud.getGri_cuerpo().setHeader(gri_busca1);//consultaFallecido
        setSolicitud.getBot_aceptar().setMetodo("consultaFallecido");
        setSolicitud.setWidth("80%");
        setSolicitud.setHeader("BUSCAR POR NOMBRES");
        agregarComponente(setSolicitud);

        setRegistros1.setId("setRegistros1");
        setRegistros1.setSeleccionTabla("select ide_check,descripcion from CMT_ACCION a, cmt_check b where a.IDE_CMACC=b.IDE_ACCION and IDE_ACCION=5", "ide_check");
        setRegistros1.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setRegistros1.getTab_seleccion().getColumna("descripcion").setLongitud(30);
        setRegistros1.getBot_aceptar().setMetodo("validaDocumentos");
        setRegistros1.getTab_seleccion().setRows(20);
        setRegistros1.setWidth("30%");
        setRegistros1.setHeight("30%");

        setRegistros1.setCheck();
        setRegistros1.setResizable(false);
        setRegistros1.setHeader("CHECK LIST");
        agregarComponente(setRegistros1);
        /*
         * Configuración y llamado de objeto reporte
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);

        dia_fallecido.setId("dia_fallecido");
        dia_fallecido.setTitle("PROGRAMACION EXHUMACION");
        dia_fallecido.setWidth("40%");
        dia_fallecido.setHeight("35%");
        dia_fallecido.getBot_aceptar().setMetodo("aceptarFallecido");
        dia_fallecido.setDialogo(tab_fallecido);
        agregarComponente(dia_fallecido);

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("INGRESO DE MOVIMIENTOS AL CEMENTERIO MUNICIPAL - EXHUMACION POR ORDEN JUDICIAL");
        agregarComponente(panOpcion);
        dibujarPantalla();
    }

    public void aceptarFallecido() {
        if (tab_fallecido.guardar()) {
            if (guardarPantalla().isEmpty()) {
            }
        }
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
        tab_tabla1.getColumna("IDE_FALLECIDO").setNombreVisual("CODIGO");
        tab_tabla1.getColumna("CEDULA_FALLECIDO").setNombreVisual("CEDULA");
        tab_tabla1.getColumna("IDE_CMGEN").setNombreVisual("GENERO");
        tab_tabla1.getColumna("IDE_CATASTRO").setNombreVisual("CATASTRO");
        tab_tabla1.getColumna("CERTIFICADO_DEFUN").setNombreVisual("CERT. DEFUNCION");
        tab_tabla1.getColumna("NOMBRES").setNombreVisual("APELLIDOS");
        tab_tabla1.getColumna("FECHA_NACIMIENTO").setNombreVisual("FECHA NACIMIENTO");
        tab_tabla1.getColumna("FECHA_DEFUNCION").setNombreVisual("FECHA DEFUNCION");

        tab_tabla1.getColumna("IDE_CMGEN").setCombo("select IDE_CMGEN,DETALLE_CMGEN from CMT_GENERO");
        tab_tabla1.getColumna("IDE_CATASTRO").setCombo("SELECT IDE_CATASTRO,DETALLE_LUGAR+'-'+SECTOR+'-'+convert(varchar,NUMERO)+'-'+convert(varchar,MODULO) FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR = B.IDE_LUGAR ");
        tab_tabla1.getColumna("IDE_CATASTRO").setLectura(true);
        tab_tabla1.getColumna("FECHA_DOCUMENTO_CMARE").setVisible(false);
        tab_tabla1.getColumna("CEDULA_FALLECIDO").setRequerida(true);
        tab_tabla1.getColumna("CERTIFICADO_DEFUN").setRequerida(true);
        tab_tabla1.getColumna("NOMBRES").setLectura(true);
        tab_tabla1.getColumna("FECHA_NACIMIENTO").setLectura(true);
        tab_tabla1.getColumna("FECHA_DEFUNCION").setLectura(true);
        tab_tabla1.getColumna("IDE_CMGEN").setLectura(true);
        tab_tabla1.getColumna("CEDULA_REPRESENTANTE").setVisible(false);
        tab_tabla1.getColumna("REPRESENTANTE_ACTUAL").setVisible(false);
        tab_tabla1.getColumna("TIPO_PAGO").setVisible(false);
        tab_tabla1.getColumna("TIPO_FALLECIDO").setVisible(false);
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.agregarRelacion(tab_tabla4);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setMensajeWarn("DATOS DE FALLECIDO");
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("CMT_REPRESENTANTE", "IDE_CMREP", 2);
        tab_tabla2.getColumna("IDE_CMTID").setCombo("select IDE_CMTID,DETALLE_CMTID from CMT_TIPO_DOCUMENTO");
        List lista = new ArrayList();
        Object fila1[] = {"1", "ACTIVO"};
        Object fila2[] = {"2", "PASIVO"};
        lista.add(fila1);
        lista.add(fila2);
        tab_tabla2.getColumna("ESTADO").setCombo(lista);
        tab_tabla2.getColumna("IDE_CMREP").setNombreVisual("CODIGO");
        tab_tabla2.getColumna("IDE_CMTID").setNombreVisual("TIPO DOCUMENTO");
        tab_tabla2.getColumna("NOMBRES_APELLIDOS_CMREP").setNombreVisual("APELLIDOS");
        tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setNombreVisual("CEDULA");
        tab_tabla2.getColumna("TELEFONOS_CMREP").setNombreVisual("TELEFONO");
        tab_tabla2.getColumna("DIRECCION_CMREP").setNombreVisual("DIRECCION");
        tab_tabla2.getColumna("CELULAR_CMREP").setNombreVisual("CELULAR");
        tab_tabla2.getColumna("EMAIL_CMREP").setNombreVisual("EMAIL");
        tab_tabla2.getColumna("ESTADO").setValorDefecto("1");
        tab_tabla2.getColumna("ESTADO").setVisible(false);
        tab_tabla2.getColumna("IDE_CMARE").setVisible(false);
        tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setRequerida(true);
        tab_tabla2.getColumna("IDE_CMTID").setRequerida(true);
        tab_tabla2.getColumna("TELEFONOS_CMREP").setRequerida(true);
        tab_tabla2.getColumna("NOMBRES_APELLIDOS_CMREP").setLectura(true);
        tab_tabla2.getGrid().setColumns(4);
        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setMensajeWarn("DATOS DE REPRESENTANTE");
        pat_panel2.setPanelTabla(tab_tabla2);

        tab_tabla4.setId("tab_tabla4");
        tab_tabla4.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 3);

        tab_tabla4.getColumna("IDE_DET_MOVIMIENTO").setNombreVisual("CODIGO");
        tab_tabla4.getColumna("FECHA_INGRESA").setNombreVisual("FECHA ACTUAL");
        tab_tabla4.getColumna("FECHA_DESDE").setNombreVisual("DESDE");
        tab_tabla4.getColumna("FECHA_HASTA").setNombreVisual("HASTA");
        tab_tabla4.getColumna("NUM_LIQUIDACION").setNombreVisual("N. LIQUIDACION");
        tab_tabla4.getColumna("VALOR_LIQUIDACION").setNombreVisual("VALOR");
        tab_tabla4.getColumna("PERIODO_ARRENDAMIENTO").setNombreVisual("PERIODO ARRENDAMIENTO");
        tab_tabla4.getColumna("ESTADO_PAGO").setNombreVisual("ESTADO PAGO");

        tab_tabla4.getColumna("IDE_CATASTRO").setVisible(false);
        tab_tabla4.getColumna("IDE_CMREP").setVisible(false);
        tab_tabla4.getColumna("IDE_TIPO_MOVIMIENTO").setVisible(false);
        tab_tabla4.getColumna("IDE_TIPO_MOVIMIENTO").setValorDefecto("7");
        tab_tabla4.getColumna("IDE_TIPO_MOVIMIENTO").setRequerida(true);
        tab_tabla4.getColumna("FECHA_INGRESA ").setValorDefecto(utilitario.getFechaActual());
        tab_tabla4.getColumna("FECHA_INGRESA").setLectura(true);
        tab_tabla4.getColumna("NUM_LIQUIDACION").setLectura(true);
        tab_tabla4.getColumna("CODIGO_LIQUIDACION").setVisible(false);
        tab_tabla4.getColumna("PERIODO_LIQUIDACION ").setVisible(false);
        tab_tabla4.getColumna("PERIODO_TITULO").setVisible(false);
        tab_tabla4.getColumna("IDE_CATASTRO2").setVisible(false);
        tab_tabla4.getColumna("PERIODO_ARRENDAMIENTO").setVisible(false);
        tab_tabla4.getColumna("FECHA_DESDE").setVisible(false);
        tab_tabla4.getColumna("FECHA_HASTA").setVisible(false);
        tab_tabla4.getColumna("VALOR_LIQUIDACION").setVisible(false);
        tab_tabla4.getColumna("NUM_LIQUIDACION").setVisible(false);
        tab_tabla4.getColumna("antecedentes").setVisible(false);

        tab_tabla4.getColumna("ver").setLectura(true);
        tab_tabla4.getColumna("ver").setRequerida(true);
        tab_tabla4.getColumna("DOCUMENTOS").setVisible(false);
        tab_tabla4.getColumna("ver").setVisible(false);
        tab_tabla4.getColumna("TIPO_PAGO").setVisible(false);
        tab_tabla4.getColumna("ESTADO_PAGO").setVisible(false);
        tab_tabla4.getColumna("ver").setCheck();
        tab_tabla4.getColumna("DOCUMENTOS").setUpload("certificados");
        tab_tabla4.getColumna("VALOR_LIQUIDACION_CALCULADO").setVisible(false);
        tab_tabla4.getColumna("TIPO_RENOVACION").setVisible(false);
        tab_tabla4.getGrid().setColumns(8);
        tab_tabla4.setTipoFormulario(true);
        tab_tabla4.dibujar();

        PanelTabla pat_panel4 = new PanelTabla();
        pat_panel4.setMensajeWarn("DATOS DE MOVIMIENTO");
        pat_panel4.setPanelTabla(tab_tabla4);

        tab_fallecido.setId("tab_fallecido");
        tab_fallecido.setTabla("CMT_DETALLE_EXHUMACION", "IDE_DETALLE_EXHUMACION", 4);
        tab_fallecido.setMostrarNumeroRegistros(false);
        tab_fallecido.setTipoFormulario(true);
        tab_fallecido.getColumna("IDE_RESPONSABLE").setCombo("SELECT IDE_AUTORIZADO,nombre_responsable FROM CMT_AUTORIZA WHERE CARGO='DIRECTOR' AND ESTADO='ACTIVO'");
        tab_fallecido.getColumna("TRABAJADOR1").setCombo("SELECT IDE_AUTORIZADO,nombre_responsable FROM CMT_AUTORIZA WHERE ESTADO='ACTIVO'");
        tab_fallecido.getColumna("TRABAJADOR2").setCombo("SELECT IDE_AUTORIZADO,nombre_responsable FROM CMT_AUTORIZA WHERE ESTADO='ACTIVO'");
        tab_fallecido.getColumna("TRABAJADOR3").setCombo("SELECT IDE_AUTORIZADO,nombre_responsable FROM CMT_AUTORIZA WHERE ESTADO='ACTIVO'");
        tab_fallecido.setCondicion("IDE_DETALLE_EXHUMACION=-1");
        tab_fallecido.dibujar();


        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(pat_panel1, pat_panel2, pat_panel4, "28%", "43%", "H");
        agregarComponente(div_division);

        Grupo gru = new Grupo();
        gru.getChildren().add(div_division);
        panOpcion.getChildren().add(gru);
    }

    public void buscaRegistro1() {
        setRegistros1.dibujar();
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

    public void busquedaInfo() {
        if (cmbOpcion.getValue().equals("0")) {
            buscarSolicitud();
        } else if (cmbOpcion.getValue().equals("1")) {
            buscarSolicitud1();
        } else if (cmbOpcion.getValue().equals("2")) {
            buscarSolicitud2();
        } else if (cmbOpcion.getValue().equals("3")) {
            buscarSolicitud3();
        } else if (cmbOpcion.getValue().equals("4")) {
            buscarSolicitud4();
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
        }
    }

    public void buscarSolicitud() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT TOP 15 a.IDE_fallecido,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP\n"
                    + "  , DETALLE_CMACC AS LUGAR,FECHA_DEFUNCION,FECHA_desde,FECHA_HASTA AS VENCIMIENTO FROM CMT_DETALLE_MOVIMIENTO A\n"
                    + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                    + "LEFT JOIN  CMT_REPRESENTANTE C  ON a.IDE_CMREP=C.IDE_CMREP\n"
                    + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                    + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC where IDE_DET_MOVIMIENTO in (select max(IDE_DET_MOVIMIENTO) from CMT_DETALLE_MOVIMIENTO group by ide_fallecido)  AND IDE_CMACC IN (select IDE_CMACC from CMT_ACCION WHERE DETALLE_CMACC IN ('INHUMACION','RENOVACION') OR DETALLE_CMACC LIKE '%REPRESENTANTE%') AND DETALLE_CMACC!= 'Exhumacion' and NOMBRES LIKE '%" + texBusqueda.getValue() + "%'");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud1() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT TOP 15 a.IDE_fallecido,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP\n"
                    + "  , DETALLE_CMACC AS LUGAR,FECHA_DEFUNCION,FECHA_desde,FECHA_HASTA AS VENCIMIENTO FROM CMT_DETALLE_MOVIMIENTO A\n"
                    + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                    + "LEFT JOIN  CMT_REPRESENTANTE C  ON a.IDE_CMREP=C.IDE_CMREP\n"
                    + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                    + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC where  IDE_DET_MOVIMIENTO in (select max(IDE_DET_MOVIMIENTO) from CMT_DETALLE_MOVIMIENTO group by ide_fallecido)  AND IDE_CMACC IN (select IDE_CMACC from CMT_ACCION WHERE DETALLE_CMACC IN ('INHUMACION','RENOVACION') OR DETALLE_CMACC LIKE '%REPRESENTANTE%') and  CEDULA_FALLECIDO LIKE '" + texBusqueda.getValue() + "%'");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud2() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT TOP 15 a.IDE_fallecido,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP\n"
                    + "  , DETALLE_CMACC AS LUGAR,FECHA_DEFUNCION,FECHA_desde,FECHA_HASTA AS VENCIMIENTO FROM CMT_DETALLE_MOVIMIENTO A\n"
                    + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                    + "LEFT JOIN  CMT_REPRESENTANTE C ON a.IDE_CMREP=C.IDE_CMREP\n"
                    + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                    + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC where  IDE_DET_MOVIMIENTO in (select max(IDE_DET_MOVIMIENTO) from CMT_DETALLE_MOVIMIENTO group by ide_fallecido)  AND IDE_CMACC IN (select IDE_CMACC from CMT_ACCION WHERE DETALLE_CMACC IN ('INHUMACION','RENOVACION') OR DETALLE_CMACC LIKE '%REPRESENTANTE%') and  NOMBRES_APELLIDOS_CMREP LIKE '" + texBusqueda.getValue() + "%'");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud3() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT TOP 15 a.IDE_fallecido,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP\n"
                    + "  , DETALLE_CMACC AS LUGAR ,FECHA_DEFUNCION,FECHA_desde,FECHA_HASTA AS VENCIMIENTO FROM CMT_DETALLE_MOVIMIENTO A\n"
                    + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                    + "LEFT JOIN  CMT_REPRESENTANTE C  ON a.IDE_CMREP=C.IDE_CMREP\n"
                    + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                    + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC where  IDE_DET_MOVIMIENTO in (select max(IDE_DET_MOVIMIENTO) from CMT_DETALLE_MOVIMIENTO group by ide_fallecido)  AND IDE_CMACC IN (select IDE_CMACC from CMT_ACCION WHERE DETALLE_CMACC IN ('INHUMACION','RENOVACION') OR DETALLE_CMACC LIKE '%REPRESENTANTE%') and  DOCUMENTO_IDENTIDAD_CMREP LIKE '" + texBusqueda.getValue() + "%'");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud4() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT TOP 15 a.IDE_fallecido,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP\n"
                    + "  , DETALLE_CMACC AS LUGAR,FECHA_DEFUNCION,FECHA_desde,FECHA_HASTA AS VENCIMIENTO FROM CMT_DETALLE_MOVIMIENTO A\n"
                    + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                    + "LEFT JOIN  CMT_REPRESENTANTE C  ON a.IDE_CMREP=C.IDE_CMREP\n"
                    + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                    + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC where  IDE_DET_MOVIMIENTO in (select max(IDE_DET_MOVIMIENTO) from CMT_DETALLE_MOVIMIENTO group by ide_fallecido)  AND IDE_CMACC IN (select IDE_CMACC from CMT_ACCION WHERE DETALLE_CMACC IN ('INHUMACION','RENOVACION') OR DETALLE_CMACC LIKE '%REPRESENTANTE%') and  NUM_LIQUIDACION LIKE '" + texBusqueda.getValue() + "%'");
            setSolicitud.getTab_seleccion().ejecutarSql();
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
        try {
            if (tab_tabla4.isFilaInsertada()) {
                if (tab_tabla4.guardar()) {
                    guardarPantalla();
                    cementerioM.ingresaMovimiento(tab_tabla1.getValor("IDE_CATASTRO"), tab_tabla2.getValor("IDE_CMREP"), tab_tabla4.getValor("IDE_DET_MOVIMIENTO"), tab_tabla1.getValor("IDE_fallecido"));
                    cementerioM.set_updateDetalleMovCheck(tab_tabla4.getValor("IDE_DET_MOVIMIENTO"));
                    utilitario.addUpdate("tab_tabla4");
                }
            } else {
                utilitario.agregarMensajeError("", "No se permite hacer cambios,Anule");
            }
        } catch (Exception e) {
            System.out.println("bloque de código donde se trata el problema");
        }
    }

    @Override
    public void actualizar() {
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
        aut_busca.actualizar();
        aut_busca.setSize(70);
        utilitario.addUpdate("aut_busca");
    }

    public void validaDocumentos() {
        System.out.println("ingreso metodo ok");
        String Seleccionados = setRegistros1.getSeleccionados();
        System.out.println("tab_tabla1.getValor(\"ide_catastro\") " + tab_tabla1.getValor("ide_catastro"));
        if (!Seleccionados.equals("")) {
            String maxdetmov = cementerioM.maxDetalleMovimiento();
            TablaGenerica documentos = utilitario.consultar("select ide_check,descripcion from CMT_ACCION a, cmt_check b where a.IDE_CMACC=b.IDE_ACCION and ide_check in (" + Seleccionados + ")");
            for (int i = 0; i < documentos.getTotalFilas(); i++) {
                String maxCheck = cementerioM.maxCheckDetalle();
                int ide_check = Integer.parseInt(maxCheck);
                int ide = ide_check;
                String doc = documentos.getValor(i, "descripcion");
                System.out.println("documentos<<<<<<<<<<<<<<<" + doc);
                cementerioM.guardaCheck(ide, maxdetmov, doc);
            }
            System.out.println("ingreso metodo if");
            setRegistros1.cerrar();
            tab_tabla4.setValor("ver", "true");
            utilitario.addUpdateTabla(tab_tabla4, "ver", "");
        } else {
            utilitario.agregarMensajeError("Seleccione un registro", "Debe seleccionar al menos un registro");
        }
    }

    public void consultaFallecido() {
        limpiar();
        TablaGenerica tabDato = cementerioM.consultaFallecido(Integer.parseInt(setSolicitud.getValorSeleccionado()));
        System.out.println("setSolicitud.getValorSeleccionado()<<" + setSolicitud.getValorSeleccionado());
        aut_busca.setValor(setSolicitud.getValorSeleccionado());
        dibujarPantalla();
        setSolicitud.cerrar();
        datosRepresentante();

    }

    public void datosRepresentante() {
        tab_tabla1.insertar();
        tab_tabla2.insertar();

        tab_tabla4.insertar();
        TablaGenerica tabDato = cementerioM.consultaFallecido(Integer.parseInt(setSolicitud.getValorSeleccionado()));

        if (!tabDato.isEmpty()) {
            tab_tabla1.setValor("IDE_FALLECIDO ", tabDato.getValor("IDE_FALLECIDO"));
            tab_tabla1.setValor("CEDULA_FALLECIDO", tabDato.getValor("CEDULA_FALLECIDO"));
            tab_tabla1.setValor("NOMBRES", tabDato.getValor("NOMBRES"));
            tab_tabla1.setValor("FECHA_NACIMIENTO", tabDato.getValor("FECHA_NACIMIENTO"));
            tab_tabla1.setValor("FECHA_DEFUNCION ", tabDato.getValor("FECHA_DEFUNCION"));
            tab_tabla1.setValor("CERTIFICADO_DEFUN ", tabDato.getValor("CERTIFICADO_DEFUN"));
            tab_tabla1.setValor("IDE_CATASTRO ", tabDato.getValor("IDE_CATASTRO"));

            tab_tabla2.setValor("IDE_CMREP ", tabDato.getValor("IDE_CMREP"));
            tab_tabla2.setValor("IDE_CMTID ", tabDato.getValor("IDE_CMTID"));
            tab_tabla2.setValor("NOMBRES_APELLIDOS_CMREP", tabDato.getValor("NOMBRES_APELLIDOS_CMREP"));
            tab_tabla2.setValor("DOCUMENTO_IDENTIDAD_CMREP", tabDato.getValor("DOCUMENTO_IDENTIDAD_CMREP"));
            tab_tabla2.setValor("DIRECCION_CMREP", tabDato.getValor("DIRECCION_CMREP"));
            tab_tabla2.setValor("TELEFONOS_CMREP ", tabDato.getValor("TELEFONOS_CMREP"));
            tab_tabla2.setValor("CELULAR_CMREP ", tabDato.getValor("CELULAR_CMREP"));
            tab_tabla2.setValor("EMAIL_CMREP", tabDato.getValor("EMAIL_CMREP"));
            tab_tabla2.setValor("ESTADO", tabDato.getValor("ESTADO"));
            tab_tabla2.getColumna("IDE_CMTID").setLectura(true);
            tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setLectura(true);
            tab_tabla2.getColumna("DIRECCION_CMREP").setLectura(true);
            tab_tabla2.getColumna("TELEFONOS_CMREP").setLectura(true);
            utilitario.addUpdate("tab_tabla1");
            utilitario.addUpdate("tab_tabla2");
            setSolicitud.cerrar();
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
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

    public Tabla getTab_fallecido() {
        return tab_fallecido;
    }

    public void setTab_fallecido(Tabla tab_fallecido) {
        this.tab_fallecido = tab_fallecido;
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

//    private static ClassCiudadania busquedaCedulaActual(java.lang.String cedula, java.lang.String usuario, java.lang.String password) {
//        paq_webservice.ConsultaCiudadano service = new paq_webservice.ConsultaCiudadano();
//        paq_webservice.ConsultaCiudadanoSoap port = service.getConsultaCiudadanoSoap();
//        return port.busquedaCedulaActual(cedula, usuario, password);
//    }
    public SeleccionTabla getSetRegistros1() {
        return setRegistros1;
    }

    public void setSetRegistros1(SeleccionTabla setRegistros1) {
        this.setRegistros1 = setRegistros1;
    }
}