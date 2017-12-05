/**
 *
 * @author l-suntaxi
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_cementerio.ejb.cementerio;
import paq_webservice.ClassCiudadania;
import persistencia.Conexion;

public class pre_renovacion extends Pantalla {

    private Tabla tabConsulta = new Tabla();
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla setDetalle = new Tabla();
    private Tabla tab_tabla4 = new Tabla();
    private Conexion conSqler = new Conexion();
    private SeleccionTabla setRegistros = new SeleccionTabla();
    private SeleccionTabla setSolicitud = new SeleccionTabla();
    private SeleccionTabla setSolicitud1 = new SeleccionTabla();
    private Tabla setSolicitu = new Tabla();
    private Combo cmbTipo = new Combo();
    private Combo cmbOpcion = new Combo();
    private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
    private AutoCompletar aut_busca = new AutoCompletar();
    private SeleccionCalendario sec_rango = new SeleccionCalendario();
    //Contiene todos los elementos de la plantilla
    private Panel panOpcion = new Panel();
    private Dialogo diaDialogoca = new Dialogo();
    private Grid gridCa = new Grid();
    private Combo cmbSeleccion = new Combo();
    private Texto texBusqueda = new Texto();
    private Dialogo diaDialogoso = new Dialogo();
    private Dialogo diaDetalle = new Dialogo();
    private Grid gridDE = new Grid();
    private Grid griDe = new Grid();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private cementerio cementerioM = (cementerio) utilitario.instanciarEJB(cementerio.class);
    String giro_producto, dep_codigo, dep_abreviatura, maximo_bloq, id, periodo, num, parametro, cod, str_fecha1, str_fecha2, compuestoPlazo, perLiq;

    public pre_renovacion() {

        //cadena de conexión para otra base de datos
        conSqler.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcer"));
        conSqler.NOMBRE_MARCA_BASE = "sqlserver";

//         Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logoactual.png");
        agregarComponente(quinde);
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonsNavegacion();

        sec_rango.setId("sec_rango");
        sec_rango.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango.setFechaActual();
        agregarComponente(sec_rango);
        agregarComponente(sef_reporte);

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        Boton bot_busca1 = new Boton();
        bot_busca1.setValue("Para Renovación");
        bot_busca1.setExcluirLectura(true);
        bot_busca1.setIcon("ui-icon-search");
        bot_busca1.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca1);

        bar_botones.agregarComponente(aut_busca);
        bar_botones.agregarComponente(new Etiqueta("Buscar Fallecido re-impresion:"));

        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT IDE_FALLECIDO,FECHA_INGRE,liquidacion,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,CATASTRO,FECHA_DESDE,FECHA_HASTA\n"
                + "FROM CMT_FALLECIDO_RENOVACION");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(70);

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
//        list.add(fila4);;
        list.add(fila5);;

        cmbOpcion.setCombo(list);
        cmbOpcion.eliminarVacio();

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
        setSolicitud.setSeleccionTabla("SELECT DISTINCT IDE_FALLECIDO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,CATASTRO,FECHA_DESDE,FECHA_HASTA,\n"
                + "representante,DETALLE_CMACC\n"
                + "FROM CMT_FALLECIDO_RENOVACION", "IDE_FALLECIDO");
        setSolicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setSolicitud.getTab_seleccion().getColumna("LIQUIDACION").setLongitud(30);
        setSolicitud.getTab_seleccion().getColumna("CEDULA_FALLECIDO").setLongitud(15);
        setSolicitud.getTab_seleccion().getColumna("CATASTRO").setLongitud(30);
        setSolicitud.getTab_seleccion().getColumna("FECHA_DESDE").setLongitud(20);
        setSolicitud.getTab_seleccion().getColumna("FECHA_HASTA").setLongitud(20);
        setSolicitud.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
        setSolicitud.getTab_seleccion().getColumna("representante").setFiltro(true);
        setSolicitud.getTab_seleccion().setRows(10);
        setSolicitud.setRadio();
        setSolicitud.getGri_cuerpo().setHeader(gri_busca1);//consultaFallecido
        setSolicitud.getBot_aceptar().setMetodo("consultaFallecido");
        setSolicitud.setWidth("90%");
        setSolicitud.setHeader("BUSCAR POR PARAMETROS");
        agregarComponente(setSolicitud);


//        rep_reporte.setId("rep_reporte");
//        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
//        agregarComponente(rep_reporte);

        sef_reporte.setId("sef_reporte");
        bar_botones.agregarReporte();
        /*
         * Configuración y llamado de objeto reporte
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conSqler);
        agregarComponente(sef_formato);

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("<CENTER>FALLECIDOS RENOVACION - CEMENTERIO MUNICIPAL </CENTER>");
        agregarComponente(panOpcion);
        dibujarPantalla();

        diaDetalle.setId("diaDetalle");
        diaDetalle.setTitle("DETALLE DE MOVIMIENTOS DE FALLECIDO");
        diaDetalle.setWidth("90%"); //siempre en porcentajes  ancho
//        diaDetalle.setHeight("20%");//siempre porcentaje   alto
//        diaDetalle.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDetalle.getBot_aceptar().setDisabled(true);
        gridDE.setColumns(4);
        agregarComponente(diaDetalle);
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
        tab_tabla1.getColumna("IDE_CATASTRO").setCombo("SELECT IDE_CATASTRO,DETALLE_LUGAR+'-'+SECTOR+'-'+convert(varchar,MODULO)+'-'+convert(varchar,NUMERO) FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR = B.IDE_LUGAR ");
        tab_tabla1.getColumna("IDE_CATASTRO").setLectura(true);
        tab_tabla1.getColumna("IDE_CATASTRO").setLongitud(70);
        tab_tabla1.getColumna("FECHA_DOCUMENTO_CMARE").setVisible(false);
        tab_tabla1.getColumna("CEDULA_FALLECIDO").setRequerida(true);
        tab_tabla1.getColumna("CEDULA_FALLECIDO").setMetodoChange("buscaPersona");
        tab_tabla1.getColumna("CERTIFICADO_DEFUN").setRequerida(true);
        tab_tabla1.getColumna("NOMBRES").setLectura(true);
        tab_tabla1.getColumna("FECHA_NACIMIENTO").setLectura(true);
        tab_tabla1.getColumna("FECHA_DEFUNCION").setLectura(true);
        tab_tabla1.getColumna("cedula_representante").setVisible(false);
        tab_tabla1.getColumna("representante_actual").setVisible(false);
        tab_tabla1.getColumna("ide_fallecido").setVisible(false);
        tab_tabla1.getColumna("tipo_pago").setVisible(false);
        tab_tabla1.getColumna("TIPO_FALLECIDO").setVisible(false);
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(6);
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

//        tab_tabla2.getColumna("IDE_CMREP").setNombreVisual("CODIGO");
        tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setMetodoChange("buscaPersonaDatos");
        tab_tabla2.getColumna("IDE_CMTID").setNombreVisual("TIPO DOCUMENTO");
        tab_tabla2.getColumna("NOMBRES_APELLIDOS_CMREP").setNombreVisual("APELLIDOS");
        tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setNombreVisual("CEDULA");
        tab_tabla2.getColumna("TELEFONOS_CMREP").setNombreVisual("TELEFONO");
        tab_tabla2.getColumna("DIRECCION_CMREP").setNombreVisual("DIRECCION");
        tab_tabla2.getColumna("CELULAR_CMREP").setNombreVisual("CELULAR");
        tab_tabla2.getColumna("EMAIL_CMREP").setNombreVisual("EMAIL");
        tab_tabla2.getColumna("ESTADO").setValorDefecto("ACTIVO");
        tab_tabla2.getColumna("ESTADO").setVisible(false);
        tab_tabla2.getColumna("ORDEN_REPRESENTANTE").setVisible(false);
        tab_tabla2.getColumna("IDE_CMREP").setVisible(false);
        tab_tabla2.getColumna("IDE_CMARE").setVisible(false);
        tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setRequerida(true);
        tab_tabla2.getColumna("IDE_CMTID").setRequerida(true);
        tab_tabla2.getColumna("TELEFONOS_CMREP").setRequerida(true);
        tab_tabla2.getColumna("NOMBRES_APELLIDOS_CMREP").setLectura(true);
        tab_tabla2.getGrid().setColumns(6);
        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setMensajeWarn("DATOS DE REPRESENTANTE");
        Boton botNuevo = new Boton();
        botNuevo.setValue("Nuevo");
        botNuevo.setIcon("ui-icon-document");
        botNuevo.setMetodo("insertar");
        pat_panel2.getChildren().add(botNuevo);
        pat_panel2.setPanelTabla(tab_tabla2);

        tab_tabla4.setId("tab_tabla4");
        tab_tabla4.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 3);

        tab_tabla4.getColumna("IDE_DET_MOVIMIENTO").setNombreVisual("CODIGO");
        tab_tabla4.getColumna("FECHA_INGRESA").setNombreVisual("FECHA ACTUAL");
        tab_tabla4.getColumna("FECHA_DESDE").setNombreVisual("DESDE");
        tab_tabla4.getColumna("FECHA_HASTA").setNombreVisual("HASTA");
        tab_tabla4.getColumna("NUM_LIQUIDACION").setNombreVisual("N. LIQUIDACION");
        tab_tabla4.getColumna("VALOR_LIQUIDACION").setNombreVisual("VALOR");
        tab_tabla4.getColumna("ANTECEDENTES").setNombreVisual("CONCEPTO");
        tab_tabla4.getColumna("PERIODO_ARRENDAMIENTO").setNombreVisual("PERIODO ARRENDAMIENTO");
        tab_tabla4.getColumna("ESTADO_PAGO").setNombreVisual("ESTADO PAGO");
        tab_tabla4.getColumna("IDE_CATASTRO").setVisible(false);
        tab_tabla4.getColumna("IDE_CMREP").setVisible(false);
        tab_tabla4.getColumna("REPRESENTANTE").setVisible(false);
//        tab_tabla4.getColumna("IDE_TIPO_MOVIMIENTO").setCombo("SELECT IDE_TIPO_PAGO, DESCRIPCION FROM dbo.CMT_TIPO_PAGO where IDE_TIPO_PAGO in (5,6)");
        tab_tabla4.getColumna("IDE_TIPO_MOVIMIENTO").setCombo("SELECT IDE_TIPO_PAGO, DESCRIPCION FROM dbo.CMT_TIPO_PAGO where IDE_TIPO_PAGO in (3,4)");
        tab_tabla4.getColumna("IDE_TIPO_MOVIMIENTO").setMetodoChange("validaPago");
        tab_tabla4.getColumna("FECHA_INGRESA").setValorDefecto(utilitario.getFechaActual());
        tab_tabla4.getColumna("FECHA_INGRESA").setLectura(true);
        tab_tabla4.getColumna("PERIODO_ARRENDAMIENTO").setLectura(true);
        tab_tabla4.getColumna("PERIODO_ARRENDAMIENTO").setMetodoChange("plazoExhumacion");
        tab_tabla4.getColumna("FECHA_HASTA").setLectura(true);
        tab_tabla4.getColumna("NUM_LIQUIDACION").setLectura(true);
        tab_tabla4.getColumna("CODIGO_LIQUIDACION").setVisible(false);
        tab_tabla4.getColumna("PERIODO_LIQUIDACION ").setVisible(false);
        tab_tabla4.getColumna("PERIODO_TITULO").setVisible(false);
        tab_tabla4.getColumna("IDE_CATASTRO2").setVisible(false);
        tab_tabla4.getColumna("DOCUMENTOS").setVisible(false);
        tab_tabla4.getColumna("VER").setVisible(false);
        tab_tabla4.getColumna("TIPO_PAGO").setVisible(false);
        tab_tabla4.getColumna("ESTADO_PAGO").setVisible(false);
        tab_tabla4.getColumna("VALOR_LIQUIDACION_CALCULADO").setVisible(false);
        tab_tabla4.getColumna("TIPO_RENOVACION").setVisible(false);
        tab_tabla4.setCondicion("IDE_DET_MOVIMIENTO is null");
//        tab_tabla4.getColumna("tipo_renovacion").setCombo("SELECT ide_tipo_renovacion,descripcion_renovacion FROM CMT_TIPO_RENOVACION");
        tab_tabla4.getColumna("VER").setCheck();
        tab_tabla4.getColumna("VER").setMetodoChange("mostrarRegistro");
        tab_tabla4.getGrid().setColumns(4);
        tab_tabla4.setTipoFormulario(true);
        tab_tabla4.dibujar();
        PanelTabla pat_panel4 = new PanelTabla();
        pat_panel4.setMensajeWarn("DATOS DE MOVIMIENTO");
        Boton botDetalle = new Boton();
        botDetalle.setValue("Ver Detalles");
        botDetalle.setIcon("ui-icon-search");
        botDetalle.setMetodo("detalleLista");
        pat_panel4.getChildren().add(botDetalle);
        pat_panel4.setPanelTabla(tab_tabla4);

//        Division div = new Division();
//        div.setId("div");
//        div.dividir2(pat_panel4, pat_panel3,"70%", "V");

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(pat_panel1, pat_panel2, pat_panel4, "23%", "50%", "H");
        agregarComponente(div_division);

        Grupo gru = new Grupo();
        gru.getChildren().add(div_division);
        panOpcion.getChildren().add(gru);
    }

    public void buscarPersona(SelectEvent evt) {
        limpiar();
        aut_busca.onSelect(evt);
        System.out.println("aut_busca.getValor()" + aut_busca.getValor());
        dibujarPantalla();
        actualizaLista1();
        actualizaLista2();
    }

    public void buscaPersonaDatos() {
        String usu, pass, cedula, nombre, direccion, telefono, correo, lugar;
        if (tab_tabla2.getValor("IDE_CMREP") != null) {
            utilitario.agregarMensajeInfo("Para ingresar nuevo representante", "Ingrese nuevo registro");
        } else {
            if (tab_tabla2.getValor("IDE_CMTID").equals("3")) {
                if (utilitario.validarCedula(tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP"))) {
                    usu = "cementerio";
                    pass = "cmtr2016$";
                    cedula = tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP");
                    paq_webservice.ConsultaCiudadano service = new paq_webservice.ConsultaCiudadano();
                    ClassCiudadania persona = service.getConsultaCiudadanoSoap().busquedaPorCedula(cedula, usu, pass);

                    tab_tabla2.setValor("NOMBRES_APELLIDOS_CMREP", persona.getNombre());
                    tab_tabla2.setValor("DIRECCION_CMREP", persona.getDireccion());
                    tab_tabla2.setValor("TELEFONOS_CMREP", persona.getTelefono());
                    tab_tabla2.setValor("EMAIL_CMREP", persona.getMail());
                    utilitario.addUpdate("tab_tabla2");
                } else {
                    utilitario.agregarMensaje("Número de Cédula Incorrecto", null);
                }
            } else if (tab_tabla2.getValor("IDE_CMTID").equals("1")) {
                if (utilitario.validarRUC(tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP"))) {
                } else {
                    utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe", "");
                }
            }
        }
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
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT IDE_FALLECIDO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,CATASTRO,FECHA_DESDE,FECHA_HASTA,\n"
                    + "representante,DETALLE_CMACC\n"
                    + "FROM CMT_FALLECIDO_RENOVACION\n"
                    + "WHERE NOMBRES LIKE '%" + texBusqueda.getValue() + "%' order by NOMBRES");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud1() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT IDE_FALLECIDO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,CATASTRO,FECHA_DESDE,FECHA_HASTA,\n"
                    + "representante,DETALLE_CMACC\n"
                    + "FROM CMT_FALLECIDO_RENOVACION\n"
                    + "WHERE CEDULA_FALLECIDO = '" + texBusqueda.getValue() + "' order by NOMBRES");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud2() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT IDE_FALLECIDO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,CATASTRO,FECHA_DESDE,FECHA_HASTA,\n"
                    + "representante,DETALLE_CMACC\n"
                    + "FROM CMT_FALLECIDO_RENOVACION\n"
                    + "WHERE representante LIKE '%" + texBusqueda.getValue() + "%' order by NOMBRES");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud3() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT IDE_FALLECIDO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,CATASTRO,FECHA_DESDE,FECHA_HASTA,\n"
                    + "representante,DETALLE_CMACC\n"
                    + "FROM CMT_FALLECIDO_RENOVACION\n"
                    + "WHERE liquidacion LIKE '%" + texBusqueda.getValue() + "%' order by NOMBRES");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud4() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT IDE_FALLECIDO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,CATASTRO,FECHA_DESDE,FECHA_HASTA,\n"
                    + "representante,DETALLE_CMACC\n"
                    + "FROM CMT_FALLECIDO_RENOVACION\n"
                    + "WHERE liquidacion LIKE '%" + texBusqueda.getValue() + "%' order by NOMBRES");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscaPersona() {
        System.out.println("<<<<<<<<<<<<<buscapersona");
        String cedulaF = cementerioM.cedulaFallecido(tab_tabla1.getValor("CEDULA_FALLECIDO"));
        if (!cedulaF.equals(tab_tabla1.getValor("CEDULA_FALLECIDO"))) {
            if (utilitario.validarCedula(tab_tabla1.getValor("CEDULA_FALLECIDO"))) {
                String usu = "cementerio";
                String pass = "cmtr2016$";
                String cedula = tab_tabla1.getValor("CEDULA_FALLECIDO");
                int sexo;
                if (busquedaCedulaActual(cedula, usu, pass).getSexo().equals("FEMENINO")) {
                    sexo = 2;
                } else {
                    sexo = 1;
                }
                String sexo1 = String.valueOf(sexo);
                tab_tabla1.setValor("NOMBRES", busquedaCedulaActual(cedula, usu, pass).getNombre());
                tab_tabla1.setValor("IDE_CMGEN", sexo1);


                System.out.println("busquedaCedulaActual(cedula, usu, pass).getFechaNacimiento()<<<<<" + busquedaCedulaActual(cedula, usu, pass).getFechaNacimiento());

                Date str_fecha1 = utilitario.DeStringADateformato3(busquedaCedulaActual(cedula, usu, pass).getFechaNacimiento());
                System.out.println("str_fecha1<<<<<" + str_fecha1);

                String str_fecha2 = utilitario.DeDateAString(str_fecha1);
                System.out.println("str_fecha2<<<<<" + str_fecha2);

                Date str_fechad1 = utilitario.DeStringADateformato3(busquedaCedulaActual(cedula, usu, pass).getFechaFallecido());
                String str_fechad2 = utilitario.DeDateAString(str_fechad1);
                System.out.println("str_fechad2<<<<<" + str_fechad2);

                tab_tabla1.setValor("FECHA_NACIMIENTO", str_fecha2);
                tab_tabla1.setValor("FECHA_DEFUNCION", str_fechad2);
                utilitario.addUpdate("tab_tabla1");
            } else {
                utilitario.agregarMensaje("Número de Cédula Incorrecto", null);
            }
        } else {
            tab_tabla1.setValor("NOMBRES", null);
            tab_tabla1.setValor("IDE_CMGEN", null);
            tab_tabla1.setValor("FECHA_NACIMIENTO", null);
            tab_tabla1.setValor("FECHA_DEFUNCION", null);
            utilitario.addUpdate("tab_tabla1");
            utilitario.agregarMensajeInfo("Fallecido", "ya se encuentra registrado");
        }
    }

//    public void plazoLugar() {
//        TablaGenerica tabTipo = utilitario.consultar("select ide_tipo_pago,descripcion from CMT_TIPO_PAGO where ide_tipo_pago = " + tab_tabla4.getValor("ide_tipo_movimiento"));
//        if (tabTipo.getValor("descripcion").equals("EXHUMACION")) {
//            fechaDesdeHasta();
//        } else {
//            generaPlazo();
//        }
//    }
    public void plazoLugar() {
        TablaGenerica tabInfo = cementerioM.getTipoPago(Integer.parseInt(tab_tabla4.getValor("IDE_TIPO_MOVIMIENTO")));//  utilitario.consultar("select IDE_TIPO_PAGO,DESCRIPCION from CMT_TIPO_PAGO where IDE_TIPO_PAGO =" + tab_tabla4.getValor("IDE_TIPO_MOVIMIENTO"));
        if (!tabInfo.isEmpty()) {
            tab_tabla4.setValor("TIPO_PAGO", tabInfo.getValor("DESCRIPCION"));
            TablaGenerica tabDato = cementerioM.periodoCatastro(tab_tabla1.getValor("IDE_CATASTRO"));
            int valor1 = Integer.parseInt(tabDato.getValor("periodo"));
            int valor2 = Integer.parseInt(tab_tabla4.getValor("PERIODO_ARRENDAMIENTO"));
            System.out.println("tab_tabla4.getValor(\"tipo_renovacion\")<<<<<<<<<<<<<<" + tab_tabla4.getValor("tipo_renovacion"));
            TablaGenerica tab_lugar = utilitario.consultar("select ide_lugar,valor,codigofiscal_cuenta,dsc_cuenta from cmt_lugar where ide_lugar in (select ide_lugar from CMT_CATASTRO where ide_catastro=" + tab_tabla1.getValor("IDE_CATASTRO") + ")");
            double valor = Double.parseDouble(tab_lugar.getValor("valor"));
            double PERIODO_ARRENDAMIENTO = Double.parseDouble(tab_tabla4.getValor("PERIODO_ARRENDAMIENTO"));
            double resultado = valor * PERIODO_ARRENDAMIENTO;
            tab_tabla4.setValor("VALOR_LIQUIDACION", String.valueOf(resultado));
            tab_tabla4.setValor("VALOR_LIQUIDACION_CALCULADO", String.valueOf(resultado));

            utilitario.addUpdate("tab_tabla4");
            System.out.println("valorLiquidacion  " + resultado);
            fechaDesdeHasta();
            creaConcepto();

        } else {
            utilitario.agregarMensaje("no Se encuentra tipo de pago", "");
        }
    }

    public void fechaDesdeHasta() {
        String fecha = utilitario.nuevaFecha(tab_tabla4.getValor("FECHA_DESDE"));
        System.err.println("" + fecha);
        int periodo1 = Integer.parseInt(tab_tabla4.getValor("PERIODO_ARRENDAMIENTO"));
        TablaGenerica tabFecha = utilitario.consultar("select 0 as id, DATEADD(YEAR, " + periodo1 + ", '" + fecha + "') as fecha");
        System.err.println("NF " + tabFecha.getValor("fecha"));
        tab_tabla4.setValor("FECHA_HASTA", tabFecha.getValor("fecha"));
        utilitario.addUpdateTabla(tab_tabla4, "FECHA_HASTA", "");
        System.out.println("FECHA_HASTA  " + tabFecha.getValor("fecha"));
    }

    public void validaPago() {
        TablaGenerica tabCatastro = cementerioM.periodoCatastro(tab_tabla1.getValor("IDE_CATASTRO"));
        if (!tabCatastro.isEmpty()) {
            TablaGenerica tabInfo = cementerioM.getTipoPago(Integer.parseInt(tab_tabla4.getValor("IDE_TIPO_MOVIMIENTO")));//utilitario.consultar("select IDE_TIPO_PAGO,DESCRIPCION from CMT_TIPO_PAGO where IDE_TIPO_PAGO =" + tab_tabla4.getValor("IDE_TIPO_MOVIMIENTO"));
            if (!tabInfo.isEmpty()) {
                if (tabInfo.getValor("DESCRIPCION").equals("RENOVACION")) {
                    TablaGenerica tadLugar = cementerioM.getRecuperaLugar(Integer.parseInt(tabCatastro.getValor("ide_lugar")));
                    if (!tadLugar.isEmpty()) {
                        if (Double.valueOf(tadLugar.getValor("valor")) > 0.0) {
                            if (tadLugar.getValor("codigofiscal_cuenta") != null) {
                                TablaGenerica tabCuentas = cementerioM.getVerificaCuentas(tadLugar.getValor("codigofiscal_cuenta"));
                                if (!tabCuentas.isEmpty()) {
                                } else {
                                    utilitario.agregarMensajeInfo("codigo fiscal cuenta no es el correcto, verificar con ER", "Solo generar movimiento");
                                }
                            } else {
                                utilitario.agregarMensajeInfo("Para generar liquidación cuenta debe poseer una codigo fiscal cuenta", "Solo generar movimiento");
                            }
                        } else {
                            utilitario.agregarMensajeInfo("Para generar liquidación valor de cuenta debe ser mayor a cero", "Solo generar movimiento");
                        }
                    }
                    tab_tabla4.setValor("PERIODO_ARRENDAMIENTO", tabCatastro.getValor("PERIODO"));
                    utilitario.addUpdate("tab_tabla4");
                    System.err.println(tab_tabla4.getValor("PERIODO_ARRENDAMIENTO") + "PERIODO ARRENDAMIENTO");
                    System.out.println("plazoLugar++++++++++++++++++++++");
                    plazoLugar();

                } else {
                    TablaGenerica tadLugar = cementerioM.getRecuperaLugar(Integer.parseInt(tabCatastro.getValor("ide_lugar")));
                    if (!tadLugar.isEmpty()) {
                        if (Double.valueOf(tadLugar.getValor("valor")) > 0.0) {
                            if (tadLugar.getValor("codigofiscal_cuenta") != null) {
                                TablaGenerica tabCuentas = cementerioM.getVerificaCuentas(tadLugar.getValor("codigofiscal_cuenta"));
                                if (!tabCuentas.isEmpty()) {
                                    tab_tabla4.setValor("PERIODO_ARRENDAMIENTO", null);
                                    tab_tabla4.setValor("FECHA_HASTA", null);
                                    tab_tabla4.setValor("VALOR_LIQUIDACION", null);
                                    tab_tabla4.setValor("OBSERVACION", null);
                                    tab_tabla4.setValor("ANTECEDENTES", null);
                                    utilitario.addUpdate("tab_tabla4");
                                    veriExhumacion();
                                    System.out.println("plazoExhumacionLugar++++++++++++++++++++++");
                                    plazoLugar();
                                } else {
                                    utilitario.agregarMensajeInfo("codigo fiscal cuenta no es el correcto, verificar con ER", "Solo generar movimiento");
                                }
                            } else {
                                utilitario.agregarMensajeInfo("Para generar liquidación cuenta debe poseer una codigo fiscal cuenta", "Solo generar movimiento");
                            }
                        } else {
                            utilitario.agregarMensajeInfo("Para generar liquidación valor de cuenta debe ser mayor a cero", "Solo generar movimiento");
                        }
                    }
                }
            } else {
                utilitario.agregarMensajeInfo("Error al buscar datos", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Error al buscar datos pago", "");
        }
    }

    public void veriExhumacion() {
        if (calcularMeses(utilitario.DeStringADate(tab_tabla4.getValor("fecha_desde")), utilitario.DeStringADate(utilitario.getFechaActual())) > 12) {
            System.err.println("mese" + calcularMeses(utilitario.DeStringADate(tab_tabla4.getValor("fecha_desde")), utilitario.DeStringADate(utilitario.getFechaActual())));
            tab_tabla4.setValor("PERIODO_ARRENDAMIENTO", String.valueOf((utilitario.getEdad(tab_tabla4.getValor("fecha_desde"))) + 1));
            utilitario.addUpdate("tab_tabla4");
        } else {
            System.err.println("meses" + calcularMeses(utilitario.DeStringADate(tab_tabla4.getValor("fecha_desde")), utilitario.DeStringADate(utilitario.getFechaActual())));
            tab_tabla4.setValor("PERIODO_ARRENDAMIENTO", "1");
            utilitario.addUpdate("tab_tabla4");
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
            case "REPORTE RENOVACION":
                aceptoOrden();
                break;
        }
    }

    public void aceptoOrden() {
        System.out.println("voy por  akkk" + tab_tabla4.getValor("NUM_LIQUIDACION"));
        switch (rep_reporte.getNombre()) {
            case "REPORTE RENOVACION":
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("NUM_LIQUIDACION", tab_tabla4.getValor("NUM_LIQUIDACION") + "");
                p_parametros.put("PERIODO", String.valueOf(utilitario.getAnio(utilitario.getFechaActual())) + "");
                System.out.println(p_parametros);
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

    public void consultaCatastro() {
        tab_tabla1.insertar();
        tab_tabla2.insertar();
        tab_tabla4.insertar();
        System.out.println("setRegistros.getValorSeleccionado()<<<<<<<" + setRegistros.getValorSeleccionado());
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
        if (setSolicitud.getValorSeleccionado() != null) {
            limpiar();
            System.out.println("setSolicitud.getValorSeleccionado()<<" + setSolicitud.getValorSeleccionado());
            aut_busca.setValor(setSolicitud.getValorSeleccionado());
            dibujarPantalla();
            setSolicitud.cerrar();
            datosRepresentante();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Solicitud", "");
        }
    }

    public void datosRepresentante() {
//        tab_tabla1.insertar();
//        tab_tabla2.insertar();
        System.out.println("setRegistros.getValorSeleccionado()<<<<<<<" + setSolicitud.getValorSeleccionado());

        TablaGenerica tabDato = cementerioM.consultaFallecido(Integer.parseInt(setSolicitud.getValorSeleccionado()));
        if (!tabDato.isEmpty()) {
            if (tabDato.getValor("fecha_hasta") != null) {
                TablaGenerica tabInfo = utilitario.consultar("select IDE_TIPO_PAGO,DESCRIPCION from CMT_TIPO_PAGO where DESCRIPCION = 'RENOVACION'");
                tab_tabla1.setValor("TIPO_PAGO", tabInfo.getValor("IDE_TIPO_PAGO"));
                utilitario.addUpdate("tab_tabla1");
                tab_tabla4.insertar();

                Date fecha1 = utilitario.DeStringADateformato2(tabDato.getValor("fecha_hasta"));
                Date str_fecha1 = utilitario.sumarRestarDiasFecha(fecha1, 1);
                String fechacal = utilitario.DeDateAString(str_fecha1);

                tab_tabla4.setValor("FECHA_DESDE", fechacal + "");
                tab_tabla4.setValor("IDE_CATASTRO", tab_tabla1.getValor("IDE_CATASTRO") + "");
                tab_tabla4.setValor("IDE_fallecido", tab_tabla1.getValor("IDE_fallecido") + "");

                utilitario.addUpdate("tab_tabla4");
                actualizaLista();
            } else {
                utilitario.agregarMensajeInfo("no tiene datos de movimiento,representantes", null);
            }
        }
    }

    @Override
    public void insertar() {
        tab_tabla2.limpiar();
        tab_tabla2.insertar();
        tab_tabla2.setValor("ESTADO","ACTIVO");
        TablaGenerica tabDatos = utilitario.consultar("select 0 as id,COUNT(*)+1 AS orden from CMT_REPRESENTANTE where IDE_FALLECIDO = "+tab_tabla1.getValor("IDE_FALLECIDO")+" AND ESTADO ='ACTIVO'");
        tab_tabla2.setValor("orden_representante",tabDatos.getValor("orden"));
        utilitario.addUpdate("tab_tabla2");
    }

    @Override
    public void guardar() {
        int ban;
        if (utilitario.validarCedula(tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP"))) {
            ban = 0;
        } else if (utilitario.validarRUC(tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP"))) {
            ban = 0;
        } else {
            if (sumaCadena(tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP")) > 0) {
                ban = 0;
            } else {
                ban = 1;
                utilitario.agregarMensajeInfo("El Número de identificacion del representante no es correcto", "");
            }
        }

        if (ban == 0) {
            if (!tab_tabla1.getValor("certificado_defun").toString().isEmpty() && !tab_tabla2.getValor("documento_identidad_cmrep").toString().isEmpty() && !tab_tabla2.getValor("telefonos_cmrep").toString().isEmpty()
                    && tab_tabla1.getValor("certificado_defun") != null && tab_tabla2.getValor("documento_identidad_cmrep") != null && tab_tabla2.getValor("telefonos_cmrep") != null) {
                if (tab_tabla4.getValor("IDE_DET_MOVIMIENTO") != null) {
                    utilitario.agregarMensajeError("Liquidacion se encuentra generada", "No se permite hacer cambios,Anule");
                } else {
                    TablaGenerica tabCatastro = cementerioM.periodoCatastro(tab_tabla1.getValor("IDE_CATASTRO"));
                    if (!tabCatastro.isEmpty()) {
                        TablaGenerica tadLugar = cementerioM.getRecuperaLugar(Integer.parseInt(tabCatastro.getValor("ide_lugar")));
                        if (!tadLugar.isEmpty()) {
                            if (Double.valueOf(tadLugar.getValor("valor")) > 0.0) {
                                if (tadLugar.getValor("codigofiscal_cuenta") != null) {
                                    TablaGenerica tabCuentas = cementerioM.getVerificaCuentas(tadLugar.getValor("codigofiscal_cuenta"));
                                    if (!tabCuentas.isEmpty()) {
                                        insertaLiquidacion();
                                    }
                                }
                            }
                            if (tab_tabla2.getValor("IDE_CMREP") != null) {
                                if (tab_tabla1.guardar()) {
                                    if (tab_tabla4.guardar()) {
                                        guardarPantalla();
                                        cementerioM.setRepresentante(Integer.parseInt(tab_tabla2.getValor("IDE_CMREP")), tab_tabla2.getValor("NOMBRES_APELLIDOS_CMREP"), tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP"), tab_tabla2.getValor("DIRECCION_CMREP"), tab_tabla2.getValor("TELEFONOS_CMREP"), tab_tabla2.getValor("CELULAR_CMREP"), tab_tabla2.getValor("EMAIL_CMREP"), tabConsulta.getValor("NICK_USUA"));
                                    }
                                }
                            } else {
                                if (tab_tabla1.guardar()) {
                                    if (tab_tabla2.guardar()) {
                                        if (tab_tabla4.guardar()) {
                                            guardarPantalla();
                                        }
                                    }
                                }
                            }
                            if (!tab_tabla4.getValor("NUM_LIQUIDACION").toString().isEmpty() && tab_tabla4.getValor("NUM_LIQUIDACION") != null) {

                                if (Double.valueOf(tadLugar.getValor("valor")) > 0.0) {
                                    if (tadLugar.getValor("codigofiscal_cuenta") != null) {
                                        TablaGenerica tabCuentas = cementerioM.getVerificaCuentas(tadLugar.getValor("codigofiscal_cuenta"));
                                        if (!tabCuentas.isEmpty()) {
                                            System.err.println("entrando sp liquidacion  ->> Renovación");
                                            ejecutaSP();
                                            System.err.println("entrando sp detalle liquidacion ->> Renovación");
                                            ejecutarProceso();
                                            cementerioM.setUpdateRuta(tab_tabla4.getValor("IDE_DET_MOVIMIENTO"), utilitario.getAnio(utilitario.getFechaActual()) + "", tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP"), tab_tabla4.getValor("NUM_LIQUIDACION"));
                                        } else {
                                            utilitario.agregarMensajeInfo("Se genero solo movimiento y no liquidacion", null);
                                        }
                                    } else {
                                        utilitario.agregarMensajeInfo("Se genero solo movimiento y no liquidacion", null);
                                    }
                                } else {
                                    utilitario.agregarMensajeInfo("Se genero solo movimiento y no liquidacion", null);
                                }

                                cementerioM.set_updateDetalleMovCheck(tab_tabla4.getValor("IDE_DET_MOVIMIENTO"));
                            } else {
                                utilitario.agregarMensajeError("Liquidacion no generada", null);
                            }
                        } else {
                            utilitario.agregarMensajeInfo("datos de lugar no disponibles", null);
                        }
                    }
                }
            } else {
                utilitario.agregarMensajeError("Los campos obligatorio deben ser llenados", null);
            }
        }
    }

    public int sumaCadena(String x) {
        int i, suma = 0;
        for (i = 0; i < x.replace(" ", "").length(); i++) {
            suma += Integer.parseInt(x.substring(i, i + 1));
        }
        return suma;
    }

    public void insertaLiquidacion() {
        giro_producto = "";
        dep_codigo = "";
        dep_abreviatura = "";
        maximo_bloq = "";
        id = "";
        periodo = "";
        cod = "";
        str_fecha1 = "";
        str_fecha2 = "";
        compuestoPlazo = "";
        perLiq = "";
        TablaGenerica tabInfo = cementerioM.getDatosAdicionales(Integer.parseInt(tab_tabla4.getValor("IDE_TIPO_MOVIMIENTO")), tab_tabla1.getValor("NOMBRES"), tab_tabla4.getValor("fecha_desde"), tab_tabla4.getValor("fecha_hasta"), Integer.parseInt(tab_tabla4.getValor("PERIODO_ARRENDAMIENTO")), Integer.parseInt(tab_tabla1.getValor("IDE_CATASTRO")));
        if (!tabInfo.isEmpty()) {
            TablaGenerica tabUsuInfo = cementerioM.getUsuarioInfor(tabConsulta.getValor("NICK_USUA"));
            if (!tabUsuInfo.isEmpty()) {
//        TablaGenerica tabDato = cementerioM.getMovimiento(Integer.parseInt(tab_tabla4.getValor("IDE_DET_MOVIMIENTO")));
                TablaGenerica tabIde = cementerioM.getIdeDetalleMovimiento(tab_tabla4.getTabla());
                if (!tabIde.isEmpty()) {
                    Date fecha1 = utilitario.DeStringADateformato2(tab_tabla4.getValor("fecha_desde"));
                    str_fecha1 = utilitario.DeDateAStringformato2(fecha1);
                    Date fecha2 = utilitario.DeStringADateformato2(tab_tabla4.getValor("fecha_hasta"));
                    str_fecha2 = utilitario.DeDateAStringformato2(fecha2);
                    compuestoPlazo = str_fecha1 + " AL " + str_fecha2 + " POR " + tab_tabla4.getValor("PERIODO_ARRENDAMIENTO") + " AÑOS ";
                    dep_codigo = tabUsuInfo.getValor("dep_codigo");
                    dep_abreviatura = tabUsuInfo.getValor("dep_abreviatura");
                    maximo_bloq = tabIde.getValor("MAXIMO_BLOQ");
                    TablaGenerica tabCodigo = cementerioM.getCodLiquidacion();
                    if (!tabCodigo.isEmpty()) {
                        id = tabCodigo.getValor("id");
                        periodo = tabCodigo.getValor("periodo");
                        TablaGenerica tabLiquidacion = cementerioM.getNumLiquidacion(dep_codigo);
                        if (!tabLiquidacion.isEmpty()) {
                            num = tabLiquidacion.getValor("num");
                            parametro = tabLiquidacion.getValor("parametro");
                            perLiq = tabLiquidacion.getValor("periodo");
                            TablaGenerica tabRubro = cementerioM.getRubroCodigo(Integer.parseInt(tab_tabla1.getValor("IDE_CATASTRO")));
                            if (!tabRubro.isEmpty()) {
                                cod = tabRubro.getValor("cod_rubro");
                                tab_tabla4.setValor("CODIGO_LIQUIDACION", id);
                                tab_tabla4.setValor("NUM_LIQUIDACION", num + "-" + dep_abreviatura + "-" + parametro);
                                tab_tabla4.setValor("PERIODO_LIQUIDACION",""+utilitario.getAnio(utilitario.getFechaActual()));
                                tab_tabla4.setValor("PERIODO_TITULO",""+utilitario.getAnio(utilitario.getFechaActual()));
                                utilitario.addUpdate("tab_tabla4");
                                giro_producto = tabInfo.getValor("giro_producto");
                                System.err.println(giro_producto + "" + dep_codigo + "" + dep_abreviatura + "" + maximo_bloq + "" + id + "" + periodo + "" + num + "" + parametro + "" + cod + "" + str_fecha1 + "" + str_fecha2 + "" + compuestoPlazo + "" + perLiq);
                            } else {
                                utilitario.agregarMensajeError("Codigo rubro falta", null);
                            }
                        } else {
                            utilitario.agregarMensajeError("Codigo de liquidacion falta", null);
                        }
                    } else {
                        utilitario.agregarMensajeError("Num de Liquidacion falta", null);
                    }

                } else {
                    utilitario.agregarMensajeError("ide de detalle falta para codigo de ruta ", null);
                }
            } else {
                utilitario.agregarMensajeError("Codigo y abreviatura de dependencia faltante", null);
            }
        } else {
            utilitario.agregarMensajeError("Faltan datos de catastro o lugar ", null);
        }
    }

    public void ejecutaSP() {
        cementerioM.setRegistroLiquidacion(dep_codigo, "'" + utilitario.getAnio(utilitario.getFechaActual()) + "'", "'" + dep_abreviatura + "'", null, "'" + tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP") + "'", "'" + tab_tabla2.getValor("NOMBRES_APELLIDOS_CMREP") + "'", null, null, "'" + tab_tabla2.getValor("DIRECCION_CMREP") + "'", null, "'" + tab_tabla4.getValor("antecedentes") + "'", null, null, "'" + tabConsulta.getValor("NICK_USUA") + "'", Double.parseDouble(tab_tabla4.getValor("VALOR_LIQUIDACION")), "'" + cod + "'", "'" + compuestoPlazo + "'", "'" + giro_producto + "'", "'" + tab_tabla1.getValor("CEDULA_FALLECIDO") + "'", "'" + tab_tabla1.getValor("NOMBRES") + "'", "'" + tab_tabla1.getValor("IDE_CATASTRO") + "'", "'" + (Integer.parseInt(maximo_bloq) + 1) + "'", "'" + str_fecha1 + "'", "'" + str_fecha2 + "'", null, "'" + tab_tabla4.getValor("PERIODO_ARRENDAMIENTO") + "'", "'" + tab_tabla4.getValor("CODIGO_LIQUIDACION") + "'", "'" + tab_tabla4.getValor("NUM_LIQUIDACION") + "'");
        System.out.println("actualizando numericos");
        cementerioM.setUpdateNumLiquidacion(dep_codigo, perLiq);
        cementerioM.setUpdateCodLiquidacion(periodo);
    }

    public void ejecutarProceso() {

        if (!tab_tabla1.getValor("TIPO_PAGO").equals("1")) {
            System.err.println(tab_tabla4.getValor("CODIGO_LIQUIDACION") + " ->>codigo liquidacion");
            System.err.println(tab_tabla4.getValor("NUM_LIQUIDACION") + " ->>numero liquidacion");

            TablaGenerica tab_lugar = cementerioM.getLugarCatastro(Integer.parseInt(tab_tabla1.getValor("IDE_CATASTRO")));
            if (!tab_lugar.isEmpty()) {
                String maximo = cementerioM.getDetalleMaxLiqui(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())), String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
                cementerioM.setGuardaDetalle(tab_tabla4.getValor("CODIGO_LIQUIDACION"), String.valueOf(utilitario.getAnio(utilitario.getFechaActual())), maximo, String.valueOf(utilitario.getAnio(utilitario.getFechaActual())), tab_lugar.getValor("codigofiscal_cuenta"), tab_lugar.getValor("dsc_cuenta"), tab_tabla4.getValor("VALOR_LIQUIDACION") + "", "1");
            } else {
                utilitario.agregarMensajeError("Faltan datos de catastro o lugar ", null);
            }
        }
        cementerioM.set_updateDisponible(tab_tabla1.getValor("IDE_CATASTRO"));
        String total_ingresa = cementerioM.maxTotalLugar(tab_tabla1.getValor("IDE_CATASTRO"));
        System.out.println("total_ingresa" + total_ingresa);
        cementerioM.set_updateTotalLugar(tab_tabla1.getValor("IDE_CATASTRO"), total_ingresa);
    }

    public void actualizaLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tab_tabla2.setCondicion(getFiltrosAcceso());
            tab_tabla2.ejecutarSql();
            utilitario.addUpdate("tab_tabla2");
        }
    }

    private String getFiltrosAcceso() {
        String str_filtros = "";
        if (setSolicitud.getValorSeleccionado() != null) {
            str_filtros = "AND IDE_CMREP = (SELECT DISTINCT (SELECT top 1 IDE_CMREP FROM CMT_REPRESENTANTE where IDE_FALLECIDO = " + Integer.parseInt(setSolicitud.getValorSeleccionado()) + " order by IDE_CMREP desc)as codigo\n"
                    + "FROM CMT_FALLECIDO)";
        }
//        else {
//            utilitario.agregarMensajeInfo("Filtros no válidos",
//                    "Debe seleccionar valor");
//        }
        return str_filtros;
    }

    public void actualizaLista1() {
        if (!getFiltrosAcceso1().isEmpty()) {
            tab_tabla2.setCondicion(getFiltrosAcceso1());
            tab_tabla2.ejecutarSql();
            utilitario.addUpdate("tab_tabla2");
        }
    }

    private String getFiltrosAcceso1() {
        System.err.println(aut_busca.getValor());
        String str_filtros = "";
        if (aut_busca.getValor() != null) {
            str_filtros = "AND IDE_CMREP = (SELECT DISTINCT (SELECT top 1 IDE_CMREP FROM CMT_REPRESENTANTE where IDE_FALLECIDO = " + Integer.parseInt(aut_busca.getValor()) + " order by IDE_CMREP desc)as codigo\n"
                    + "FROM CMT_FALLECIDO)";
        }
//        else {
//            utilitario.agregarMensajeInfo("Filtros no válidos",
//                    "Debe seleccionar valor");
//        }
        return str_filtros;
    }

    public void actualizaLista2() {
        if (!getFiltrosAcceso2().isEmpty()) {
            tab_tabla4.setCondicion(getFiltrosAcceso2());
            tab_tabla4.ejecutarSql();
            utilitario.addUpdate("tab_tabla4");
        }
    }

    private String getFiltrosAcceso2() {
        System.err.println(aut_busca.getValor());
        String str_filtros = "";
        if (aut_busca.getValor() != null) {
            str_filtros = "AND IDE_DET_MOVIMIENTO = (SELECT (SELECT top 1 IDE_DET_MOVIMIENTO FROM CMT_DETALLE_MOVIMIENTO where IDE_FALLECIDO = CMT_FALLECIDO.IDE_FALLECIDO order by FECHA_DESDE desc)as ide\n"
                    + "FROM CMT_FALLECIDO\n"
                    + "where IDE_FALLECIDO =" + Integer.parseInt(aut_busca.getValor()) + ")";
        }
        return str_filtros;
    }

    public void detalleLista() {
        diaDetalle.setDialogo(griDe);
        setDetalle.setId("setDetalle");
        setDetalle.setSql("SELECT IDE_DET_MOVIMIENTO,PERIODO_ARRENDAMIENTO as Periodo, FECHA_DESDE as Fec_Desde, FECHA_HASTA as Fec_Hasta, NUM_LIQUIDACION as Liquidacion\n"
                + ", VALOR_LIQUIDACION as Valor, OBSERVACION, ANTECEDENTES, \n"
                + "(SELECT DETALLE_LUGAR+'-'+SECTOR+'-'+convert(varchar,MODULO)+'-'+convert(varchar,NUMERO) FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR = B.IDE_LUGAR where a.IDE_CATASTRO = CMT_DETALLE_MOVIMIENTO.IDE_CATASTRO) as CATASTRO,  REPRESENTANTE,TIPO_PAGO \n"
                + "FROM CMT_DETALLE_MOVIMIENTO \n"
                + "WHERE  IDE_FALLECIDO =" + tab_tabla1.getValor("IDE_FALLECIDO") + " ORDER BY FECHA_INGRESA");
        setDetalle.getColumna("IDE_DET_MOVIMIENTO").setVisible(false);
        setDetalle.getColumna("CATASTRO").setLongitud(25);
        setDetalle.getColumna("OBSERVACION").setLongitud(45);
        setDetalle.getColumna("ANTECEDENTES").setLongitud(50);
        setDetalle.getColumna("REPRESENTANTE").setLongitud(50);
        setDetalle.getColumna("TIPO_PAGO").setLongitud(25);
        setDetalle.setLectura(true);
        setDetalle.setRows(4);
        gridDE.getChildren().add(setDetalle);
        diaDetalle.setDialogo(gridDE);
        setDetalle.dibujar();
        diaDetalle.dibujar();
    }

    public void creaConcepto() {
        String rows = "";
        String Texto = "";

        TablaGenerica tab_lugar = cementerioM.getListaFallecidos(Integer.parseInt(tab_tabla1.getValor("IDE_CATASTRO")));
        if (!tab_lugar.isEmpty()) {
            for (int i = 0; i < tab_lugar.getTotalFilas(); i++) {
                rows += (tab_lugar.getValor(i, "NOMBRES ") + ",");
            }
        }

        TablaGenerica tadDato = utilitario.consultar("select (select DETALLE_LUGAR from CMT_LUGAR where IDE_LUGAR = CMT_CATASTRO.IDE_LUGAR) as IDE_LUGAR ,SECTOR,modulo,NUMERO\n"
                + "from CMT_CATASTRO \n"
                + "where IDE_CATASTRO= " + tab_tabla1.getValor("IDE_CATASTRO"));

        TablaGenerica tabInfo = cementerioM.getTipoPago(Integer.parseInt(tab_tabla4.getValor("IDE_TIPO_MOVIMIENTO")));
        System.err.println("" + tabInfo.getValor("descripcion"));

        Texto = "PAGO POR " + tabInfo.getValor("descripcion") + " DE UN " + tadDato.getValor("IDE_LUGAR") + " EN EL CEMENTERIO MUNICIPAL SECTOR: " + tadDato.getValor("SECTOR") + " MODULO: " + tadDato.getValor("modulo") + "\n"
                + "NUMERO: " + tadDato.getValor("NUMERO") + "(" + tab_tabla1.getValor("catastro_anterior") + ")" + ". PARA LOS RESTOS DEL SR(A)(S): " + rows + " POR " + tab_tabla4.getValor("PERIODO_ARRENDAMIENTO") + " AÑOS.\n"
                + "DESDE EL " + tab_tabla4.getValor("FECHA_DESDE") + " HASTA EL " + tab_tabla4.getValor("FECHA_HASTA") + "";
// 
        tab_tabla4.setValor("antecedentes", Texto);
        System.err.println("" + Texto);
        utilitario.addUpdate("tab_tabla4");
    }

    @Override
    public void actualizar() {
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
        aut_busca.actualizar();
        aut_busca.setSize(70);
        utilitario.addUpdate("aut_busca");
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
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public int calcularMeses(Date fechaInicio, Date fechaFin) {
        try {
            //Fecha inicio en objeto Calendar
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(fechaInicio);
            //Fecha finalización en objeto Calendar
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(fechaFin);
            //Cálculo de meses para las fechas de inicio y finalización
            int startMes = (startCalendar.get(Calendar.YEAR) * 12) + startCalendar.get(Calendar.MONTH);
            int endMes = (endCalendar.get(Calendar.YEAR) * 12) + endCalendar.get(Calendar.MONTH);
            //Diferencia en meses entre las dos fechas
            int diffMonth = endMes - startMes;
            System.err.println("MESES " + diffMonth);
            return diffMonth;
        } catch (Exception e) {
            return 0;
        }
    }

    public SeleccionFormatoReporte getSef_reporte() {
        return sef_reporte;
    }

    public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
        this.sef_reporte = sef_reporte;
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

    public Conexion getConSqler() {
        return conSqler;
    }

    public void setConSqler(Conexion conSqler) {
        this.conSqler = conSqler;
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

    public Tabla getSetDetalle() {
        return setDetalle;
    }

    public void setSetDetalle(Tabla setDetalle) {
        this.setDetalle = setDetalle;
    }

    private static ClassCiudadania busquedaCedulaActual(java.lang.String cedula, java.lang.String usuario, java.lang.String password) {
        paq_webservice.ConsultaCiudadano service = new paq_webservice.ConsultaCiudadano();
        paq_webservice.ConsultaCiudadanoSoap port = service.getConsultaCiudadanoSoap();
        return port.busquedaCedulaActual(cedula, usuario, password);
    }
}