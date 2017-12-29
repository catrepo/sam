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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_cementerio.ejb.cementerio;
import paq_webservice.ClassCiudadania;
import persistencia.Conexion;

public class pre_inhumacion extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla4 = new Tabla();
    private Conexion conSqler = new Conexion();
    private SeleccionTabla setRegistros = new SeleccionTabla();
    private SeleccionTabla setRegistros1 = new SeleccionTabla();
    private Tabla setSolicitu = new Tabla();
    private Combo cmbTipo = new Combo();
    private AutoCompletar aut_busca = new AutoCompletar();
    private SeleccionCalendario sec_rango = new SeleccionCalendario();
    //Contiene todos los elementos de la plantilla
    private Panel panOpcion = new Panel();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Dialogo diaRegistro = new Dialogo();
    private Grid gridre = new Grid();
    private Grid gridRe = new Grid();
    @EJB
    private cementerio cementerioM = (cementerio) utilitario.instanciarEJB(cementerio.class);
    String giro_producto, dep_codigo, dep_abreviatura, maximo_bloq, id, periodo, num, parametro, cod, str_fecha1, str_fecha2, compuestoPlazo, perLiq;

    public pre_inhumacion() {
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

        //cadena de conexión para otra base de datos
        conSqler.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcer"));
        conSqler.NOMBRE_MARCA_BASE = "sqlserver";


        bar_botones.agregarComponente(new Etiqueta("Buscador Personas:"));
        bar_botones.agregarComponente(aut_busca);

        Boton bot_busca1 = new Boton();
        bot_busca1.setValue("Check List");
        bot_busca1.setExcluirLectura(true);
        bot_busca1.setIcon("ui-icon-search");
        bot_busca1.setMetodo("buscaRegistro1");
        bar_botones.agregarBoton(bot_busca1);

        Boton bot_busca = new Boton();
        bot_busca.setValue("Buscar Lugar Cementerio");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("buscaRegistro");
        bar_botones.agregarBoton(bot_busca);


        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT a.IDE_fallecido,IDE_DET_MOVIMIENTO,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES  , FECHA_INGRESA,a.ver FROM CMT_DETALLE_MOVIMIENTO A\n"
                + "LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                + "LEFT JOIN  CMT_REPRESENTANTE C  ON A.IDE_FALLECIDO=C.IDE_FALLECIDO\n"
                + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                + "LEFT JOIN CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC where IDE_TIPO_MOVIMIENTO=1");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(70);


        cmbTipo.setId("cmbTipo");
        cmbTipo.setCombo("SELECT distinct IDE_LUGAR  as id,DETALLE_LUGAR  FROM CMT_LUGAR");
        cmbTipo.eliminarVacio();

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
        setRegistros.setSeleccionTabla("select a.IDE_CATASTRO,SECTOR,MODULO,NUMERO,max(case when fecha_hasta is null then DATEADD(month, 1, GETDATE()) else fecha_hasta end)as fecha_hasta ,(case when total_ingresa is null then 0 else total_ingresa end) as TOTAL_INGRESA\n"
                + "from CMT_CATASTRO a left join cmt_lugar b on a.ide_lugar=b.ide_lugar\n"
                + "left join  (select max(fecha_hasta)as fecha_hasta, ide_catastro from CMT_DETALLE_MOVIMIENTO  group by ide_catastro,ide_fallecido)c  on  c.IDE_CATASTRO=a.IDE_CATASTRO  \n"
                + "where catastro_habilita =1 or catastro_habilita is null \n"
                + "group by a.IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO,(case when total_ingresa is null then 0 else total_ingresa end) ,habilita order by SECTOR,MODULO", "IDE_CATASTRO");
        setRegistros.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setRegistros.getTab_seleccion().getColumna("SECTOR").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("NUMERO").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("MODULO").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("SECTOR").setLongitud(30);
        setRegistros.getTab_seleccion().getColumna("NUMERO").setLongitud(30);
        setRegistros.getTab_seleccion().getColumna("MODULO").setLongitud(30);
        setRegistros.getTab_seleccion().getColumna("fecha_hasta").setLongitud(30);
        setRegistros.getTab_seleccion().getColumna("TOTAL_INGRESA").setLongitud(30);
        setRegistros.getTab_seleccion().setRows(20);
        setRegistros.setWidth("60%");
        setRegistros.setRadio();
        setRegistros.setResizable(false);
        setRegistros.getGri_cuerpo().setHeader(gri_busca);
        setRegistros.getBot_aceptar().setMetodo("consultaCatastro");
        setRegistros.setHeader("BUSCAR LUGAR DISPONIBLE EN EL CEMENTERIO");
        agregarComponente(setRegistros);

        setRegistros1.setId("setRegistros1");
        setRegistros1.setSeleccionTabla("select ide_check,descripcion from CMT_ACCION a, cmt_check b where a.IDE_CMACC=b.IDE_ACCION and IDE_ACCION=1", "ide_check");
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

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("INGRESO DE MOVIMIENTOS AL CEMENTERIO MUNICIPAL - INHUMACION");
        agregarComponente(panOpcion);

        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conSqler);
        agregarComponente(sef_formato);

        diaRegistro.setId("diaRegistro");
        diaRegistro.setTitle("Acción"); //titulo
        diaRegistro.setWidth("50%"); //siempre en porcentajes  ancho
        diaRegistro.setHeight("50%");//siempre porcentaje   alto
        diaRegistro.setResizable(false); //para que no se pueda cambiar el tamaño
        diaRegistro.getBot_aceptar().setMetodo("regresaForma");
        gridre.setColumns(4);
        agregarComponente(diaRegistro);

        dibujarPantalla();
    }

    public void validaDocumentos() {
        System.out.println("ingreso metodo ok");
        String Seleccionados = setRegistros1.getSeleccionados();
        System.out.println(tab_tabla1.getValor("ide_catastro") + tab_tabla1.getValor("ide_catastro"));
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
        tab_tabla1.getColumna("TIPO_FALLECIDO").setCombo("SELECT IDE_TIPO_FALLECIDO,DESCRIPCION FROM CMT_TIPO_FALLECIDO order by DESCRIPCION ");
        tab_tabla1.getColumna("IDE_CMGEN").setCombo("select IDE_CMGEN,DETALLE_CMGEN from CMT_GENERO");
        tab_tabla1.getColumna("CEDULA_FALLECIDO").setMetodoChange("buscaPersona");
        tab_tabla1.getColumna("IDE_CATASTRO").setCombo("SELECT IDE_CATASTRO,DETALLE_LUGAR+'-'+SECTOR+'-'+convert(varchar,MODULO)+'-'+convert(varchar,NUMERO) FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR = B.IDE_LUGAR ");
        tab_tabla1.getColumna("IDE_CATASTRO").setLectura(true);
        tab_tabla1.getColumna("FECHA_DOCUMENTO_CMARE").setVisible(false);
        tab_tabla1.getColumna("NOMBRES").setRequerida(true);
        tab_tabla1.getColumna("TIPO_FALLECIDO").setRequerida(true);
        tab_tabla1.getColumna("CERTIFICADO_DEFUN").setRequerida(true);
        tab_tabla1.getColumna("CEDULA_REPRESENTANTE").setVisible(false);
        tab_tabla1.getColumna("REPRESENTANTE_ACTUAL").setVisible(false);
        tab_tabla1.getColumna("TIPO_PAGO").setCombo("select IDE_TIPO_PAGO,DESCRIPCION from CMT_TIPO_PAGO where DESCRIPCION ='ARRENDAMIENTO'");
        tab_tabla1.getColumna("TIPO_PAGO").setMetodoChange("validaPago");

        tab_tabla1.getColumna("TIPO_FALLECIDO").setMetodoChange("datosTipoFallecido");
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
        tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setMetodoChange("buscaPersonaDatos");
        tab_tabla2.getColumna("IDE_CMTID").setCombo("select IDE_CMTID,DETALLE_CMTID from CMT_TIPO_DOCUMENTO");
        tab_tabla2.getColumna("EMAIL_CMREP").setMetodoChange("validaemail");
//        tab_tabla2.getColumna("ESTADO").setCombo(lista);
        tab_tabla2.getColumna("ESTADO").setValorDefecto("ACTIVO");
        tab_tabla2.getColumna("ESTADO").setVisible(false);
        tab_tabla2.getColumna("IDE_CMARE").setVisible(false);
        tab_tabla2.getColumna("ORDEN_REPRESENTANTE").setVisible(false);

        tab_tabla2.getColumna("IDE_CMTID").setRequerida(true);
        tab_tabla2.getColumna("TELEFONOS_CMREP").setRequerida(true);
        tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setRequerida(true);
        tab_tabla2.getGrid().setColumns(4);
        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setMensajeWarn("DATOS DE REPRESENTANTE");
        pat_panel2.setPanelTabla(tab_tabla2);

        tab_tabla4.setId("tab_tabla4");
        tab_tabla4.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 3);
        tab_tabla4.setCondicion("IDE_DET_MOVIMIENTO=-1");
        tab_tabla4.getColumna("IDE_CATASTRO").setVisible(false);
        tab_tabla4.getColumna("IDE_CMREP").setVisible(false);
        tab_tabla4.getColumna("FECHA_INGRESA ").setValorDefecto(utilitario.getFechaActual());
        tab_tabla4.getColumna("FECHA_INGRESA").setLectura(true);
        tab_tabla4.getColumna("PERIODO_ARRENDAMIENTO").setRequerida(true);
//        tab_tabla4.getColumna("PERIODO_ARRENDAMIENTO").setMetodoChange("plazoLugar");
        tab_tabla4.getColumna("FECHA_DESDE").setValorDefecto(utilitario.getFechaActual());
        tab_tabla4.getColumna("FECHA_DESDE").setMetodoChange("fechaDesdeHasta");
        tab_tabla4.getColumna("FECHA_DESDE").setRequerida(true);
        tab_tabla4.getColumna("FECHA_HASTA").setRequerida(true);
        tab_tabla4.getColumna("VALOR_LIQUIDACION").setRequerida(true);
        tab_tabla4.getColumna("FECHA_HASTA").setLectura(true);
        tab_tabla4.getColumna("FECHA_DESDE").setMetodoChange("fechaVencimiento");
        tab_tabla4.getColumna("NUM_LIQUIDACION").setLectura(true);
        tab_tabla4.getColumna("CODIGO_LIQUIDACION").setVisible(false);
        tab_tabla4.getColumna("PERIODO_LIQUIDACION ").setVisible(false);
        tab_tabla4.getColumna("PERIODO_TITULO").setVisible(false);
        tab_tabla4.getColumna("IDE_CATASTRO2").setVisible(false);
        tab_tabla4.getColumna("DOCUMENTOS").setVisible(false);
        tab_tabla4.getColumna("DOCUMENTOS").setUpload("certificados");
        tab_tabla4.getColumna("ver").setLectura(true);
        tab_tabla4.getColumna("ver").setRequerida(true);
        tab_tabla4.getColumna("tipo_renovacion").setVisible(false);
        tab_tabla4.getColumna("VALOR_LIQUIDACION_CALCULADO").setVisible(false);
        tab_tabla4.getColumna("IDE_DET_MOVIMIENTO").setNombreVisual("CODIGO");
        tab_tabla4.getColumna("FECHA_INGRESA").setVisible(false);
        tab_tabla4.getColumna("TIPO_PAGO").setVisible(false);
        tab_tabla4.getColumna("FECHA_DESDE").setNombreVisual("DESDE");
        tab_tabla4.getColumna("FECHA_HASTA").setNombreVisual("HASTA");
        tab_tabla4.getColumna("NUM_LIQUIDACION").setNombreVisual("N. LIQUIDACION");
        tab_tabla4.getColumna("VALOR_LIQUIDACION").setNombreVisual("VALOR");
        tab_tabla4.getColumna("PERIODO_ARRENDAMIENTO").setNombreVisual("PERIODO ARRENDAMIENTO");
        tab_tabla4.getColumna("ver").setCheck();
        tab_tabla4.getColumna("ver").setVisible(false);
        tab_tabla4.getColumna("emitido").setVisible(false);
        tab_tabla4.getColumna("representante").setVisible(false);
        tab_tabla4.getColumna("estado_pago").setVisible(false);
        tab_tabla4.getGrid().setColumns(8);
        tab_tabla4.setTipoFormulario(true);
        tab_tabla4.dibujar();
        PanelTabla pat_panel4 = new PanelTabla();
        pat_panel4.setMensajeWarn("DATOS DE MOVIMIENTO");
        pat_panel4.setPanelTabla(tab_tabla4);

        Division div_horizontal = new Division();
        div_horizontal.dividir2(pat_panel1, pat_panel2, "50%", "V");
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(div_horizontal, pat_panel4, "35%", "H");
        agregarComponente(div_division);

        Grupo gru = new Grupo();
        gru.getChildren().add(div_division);
        panOpcion.getChildren().add(gru);
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
            case "REPORTE INHUMACION CEMENTERIO":
                aceptoOrden();
                break;
        }
    }

    public void aceptoOrden() {
        System.out.println("voy por  akkk" + tab_tabla4.getValor("NUM_LIQUIDACION"));
        switch (rep_reporte.getNombre()) {
            case "REPORTE INHUMACION CEMENTERIO":
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

    public void buscaRegistro() {
        setRegistros.dibujar();
    }

    public void fechaVencimiento() {
        Date fecha1 = utilitario.DeStringADateformato2(tab_tabla4.getValor("fecha_desde"));
        System.out.println(tab_tabla4.getValor("fecha_desde") + tab_tabla4.getValor("fecha_desde"));
        Date fecha2 = utilitario.DeStringADateformato2(tab_tabla1.getValor("FECHA_DEFUNCION"));
        System.out.println(tab_tabla1.getValor("FECHA_DEFUNCION") + tab_tabla1.getValor("FECHA_DEFUNCION"));

        System.out.println("fecha1  " + fecha1);
        System.out.println("fecha2  " + fecha2);
        if (utilitario.isFechaMayor(fecha1, fecha2)) {
            System.out.println("fecha  " + tab_tabla4.getValor("PERIODO_ARRENDAMIENTO"));
            String clave = tab_tabla4.getValor("PERIODO_ARRENDAMIENTO");
            int PERIODO_ARRENDAMIENTO = Integer.parseInt(clave);
            int numero_dias = 365 * PERIODO_ARRENDAMIENTO;
            String fecha = tab_tabla4.getValor("FECHA_DESDE");
            Date fecha_a_sumar = utilitario.DeStringADate(fecha);
            Date nueva_fecha = utilitario.sumarDiasFecha(fecha_a_sumar, numero_dias);
            String str_fecha = utilitario.DeDateAString(nueva_fecha);
            tab_tabla4.setValor("FECHA_HASTA", str_fecha);
            tab_tabla4.setValor("IDE_TIPO_MOVIMIENTO", tab_tabla1.getValor("TIPO_PAGO"));
            utilitario.addUpdateTabla(tab_tabla4, "FECHA_HASTA", "");
            System.out.println("FECHA_HASTA  " + str_fecha);
        } else {
            tab_tabla4.setValor("fecha_desde", null);
            tab_tabla4.setValor("fecha_hasta", null);

            utilitario.addUpdate("tab_tabla4");
            utilitario.agregarMensajeInfo("Fecha invalida", "Fecha de ingreso menor a la fecha defuncion");
        }
    }

    public void fechaDesdeHasta1() {
        int PERIODO_ARRENDAMIENTO = Integer.parseInt(tab_tabla4.getValor("PERIODO_ARRENDAMIENTO"));
        int numero_dias = 365 * PERIODO_ARRENDAMIENTO;
        String fecha = tab_tabla4.getValor("FECHA_DESDE");
        Date fecha_a_sumar = utilitario.DeStringADate(fecha);
        Date nueva_fecha = utilitario.sumarDiasFecha(fecha_a_sumar, numero_dias);
        System.err.println(">>nueva " + nueva_fecha);
        String str_fecha = utilitario.DeDateAString(nueva_fecha);
        System.err.println(">>fechas " + str_fecha);
        tab_tabla4.setValor("FECHA_HASTA", str_fecha);
        utilitario.addUpdateTabla(tab_tabla4, "FECHA_HASTA", "");
        System.out.println("FECHA_HASTA  " + str_fecha + " " + fecha);
        creaConcepto();
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
        creaConcepto();
    }

    public void validaemail() {
        if (!utilitario.isEmailValido(tab_tabla2.getValor("EMAIL_CMREP"))) {
            utilitario.agregarMensajeInfo("El correo es Invalido", "");
            return;
        }
    }

    public void valorLiquidacion() {
        TablaGenerica tab_lugar = utilitario.consultar("select ide_lugar,valor,codigofiscal_cuenta,dsc_cuenta from cmt_lugar where ide_lugar in (select ide_lugar from CMT_CATASTRO where ide_catastro=" + tab_tabla4.getValor("IDE_CATASTRO") + ")");
//        TablaGenerica tab_dato = cementerioM.getMaxdetalleli(tab_tabla4.getValor("PERIODO_LIQUIDACION"), tab_tabla4.getValor("PERIODO_TITULO"));

        double valor = Double.parseDouble(tab_lugar.getValor("valor"));
        double PERIODO_ARRENDAMIENTO = Double.parseDouble(tab_tabla4.getValor("PERIODO_ARRENDAMIENTO"));
        double resultado = valor * PERIODO_ARRENDAMIENTO;
        tab_tabla4.setValor("VALOR_LIQUIDACION", String.valueOf(resultado));
        utilitario.addUpdate("tab_tabla4");
        System.out.println("valorLiquidacion  " + resultado);
    }

    public void validaPago() {
        TablaGenerica tabInfo = cementerioM.getTipoPago(Integer.parseInt(tab_tabla4.getValor("IDE_TIPO_MOVIMIENTO")));//utilitario.consultar("select IDE_TIPO_PAGO,DESCRIPCION from CMT_TIPO_PAGO where IDE_TIPO_PAGO =" + tab_tabla4.getValor("IDE_TIPO_MOVIMIENTO"));
        if (!tabInfo.isEmpty()) {
            TablaGenerica tabCatastro = cementerioM.periodoCatastro(tab_tabla1.getValor("IDE_CATASTRO"));
            if (!tabCatastro.isEmpty()) {

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


//                    tab_tabla4.getColumna("PERIODO_ARRENDAMIENTO").setLectura(true);
                tab_tabla4.setValor("PERIODO_ARRENDAMIENTO", tabCatastro.getValor("PERIODO"));
                utilitario.addUpdate("tab_tabla4");
                System.err.println(tab_tabla4.getValor("PERIODO_ARRENDAMIENTO") + "PERIODO ARRENDAMIENTO");
                System.out.println("plazoLugar++++++++++++++++++++++");
                plazoLugar();


            } else {
                utilitario.agregarMensajeInfo("Error al buscar datos pago", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Error al buscar datos", "");
        }

    }

    public void plazoLugar() {

        System.out.println("TIPO_PAGO<<<<" + tab_tabla1.getValor("TIPO_PAGO"));
        if (tab_tabla1.getValor("TIPO_PAGO") != null) {
            TablaGenerica tabInfo = utilitario.consultar("select IDE_TIPO_PAGO,DESCRIPCION from CMT_TIPO_PAGO where IDE_TIPO_PAGO =" + tab_tabla1.getValor("TIPO_PAGO"));
            tab_tabla4.setValor("TIPO_PAGO", tabInfo.getValor("DESCRIPCION"));
            TablaGenerica tabDato = cementerioM.periodoCatastro(tab_tabla1.getValor("IDE_CATASTRO"));
            int valor1 = Integer.parseInt(tabDato.getValor("periodo"));
            System.out.println("valor1  " + valor1);

            int valor2 = Integer.parseInt(tab_tabla4.getValor("PERIODO_ARRENDAMIENTO"));
            System.out.println("valor2 " + valor2);

            if (valor2 <= valor1) {
                TablaGenerica tab_lugar = utilitario.consultar("select ide_lugar,valor,codigofiscal_cuenta,dsc_cuenta from cmt_lugar where ide_lugar in (select ide_lugar from CMT_CATASTRO where ide_catastro=" + tab_tabla1.getValor("IDE_CATASTRO") + ")");
                System.out.println("tab_tabla1.getValor(\"TIPO_FALLECIDO\")  " + tab_tabla1.getValor("TIPO_FALLECIDO"));

                double valor = Double.parseDouble(tab_lugar.getValor("valor"));
                double PERIODO_ARRENDAMIENTO = Double.parseDouble(tab_tabla4.getValor("PERIODO_ARRENDAMIENTO"));

                double resultado = valor * PERIODO_ARRENDAMIENTO;
                if (tab_tabla1.getValor("TIPO_PAGO").equals("1")) {
                    tab_tabla4.setValor("VALOR_LIQUIDACION", String.valueOf(0));
                    utilitario.addUpdate("tab_tabla4");
                } else {

                    tab_tabla4.setValor("VALOR_LIQUIDACION", String.valueOf(resultado));
                    tab_tabla4.setValor("VALOR_LIQUIDACION_CALCULADO", String.valueOf(resultado));
                    utilitario.addUpdate("tab_tabla4");
                }
                fechaDesdeHasta();

            } else if (valor2 > valor1) {
                tab_tabla4.setValor("VALOR_LIQUIDACION", null);
                tab_tabla4.setValor("fecha_hasta", null);

                utilitario.addUpdate("tab_tabla4");
                utilitario.agregarMensajeError("Plazo ", "Excede el periodo");
            }
        } else {
            tab_tabla4.setValor("PERIODO_ARRENDAMIENTO", null);

            utilitario.addUpdate("tab_tabla4");
            utilitario.agregarMensajeError("Ingrese un tipo de pago", "");
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
        cementerioM.setRegistroLiquidacion(dep_codigo, "'" + utilitario.getAnio(utilitario.getFechaActual()) + "'", "'" + dep_abreviatura + "'", null, "'" + tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP") + "'", "'" + tab_tabla2.getValor("NOMBRES_APELLIDOS_CMREP") + "'", null, null, "'" + tab_tabla2.getValor("DIRECCION_CMREP") + "'", null, "'" + tab_tabla4.getValor("ANTECEDENTES") + "'", "'" + tab_tabla4.getValor("OBSERVACION") + "'", null, "'" + tabConsulta.getValor("NICK_USUA") + "'", Double.parseDouble(tab_tabla4.getValor("VALOR_LIQUIDACION")), "'" + cod + "'", "'" + compuestoPlazo + "'", "'" + giro_producto + "'", "'" + tab_tabla1.getValor("CEDULA_FALLECIDO") + "'", "'" + tab_tabla1.getValor("NOMBRES") + "'", "'" + tab_tabla1.getValor("IDE_CATASTRO") + "'", "'" + (Integer.parseInt(maximo_bloq) + 1) + "'", "'" + str_fecha1 + "'", "'" + str_fecha2 + "'", null, "'" + tab_tabla4.getValor("PERIODO_ARRENDAMIENTO") + "'", "'" + tab_tabla4.getValor("CODIGO_LIQUIDACION") + "'", "'" + tab_tabla4.getValor("NUM_LIQUIDACION") + "'");
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

    @Override
    public void guardar() {
        if (!tab_tabla1.getValor("certificado_defun").toString().isEmpty() && !tab_tabla2.getValor("documento_identidad_cmrep").toString().isEmpty() && !tab_tabla2.getValor("telefonos_cmrep").toString().isEmpty()
                && tab_tabla1.getValor("certificado_defun") != null && tab_tabla2.getValor("documento_identidad_cmrep") != null && tab_tabla2.getValor("telefonos_cmrep") != null) {
            if (tab_tabla4.isFilaInsertada()) {
                if (tab_tabla4.getValor("ver").equals("true") || tab_tabla4.getValor("ver").equals("1")) {
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
                            if (tab_tabla1.guardar()) {
                                if (tab_tabla2.guardar()) {
                                    if (tab_tabla4.guardar()) {
                                        guardarPantalla();
                                        if (Double.valueOf(tadLugar.getValor("valor")) > 0.0) {
                                            if (tadLugar.getValor("codigofiscal_cuenta") != null) {
                                                TablaGenerica tabCuentas = cementerioM.getVerificaCuentas(tadLugar.getValor("codigofiscal_cuenta"));
                                                if (!tabCuentas.isEmpty()) {
                                                    System.err.println("entrando sp liquidacion  ->> Renovación");
                                                    //graba encabezado de liquidacion
                                                    ejecutaSP();
                                                    System.err.println("entrando sp detalle liquidacion ->> Renovación");
                                                    //graba detalle de liquidacion
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
                                        cementerioM.setUpdateDetalleMov(Integer.parseInt(tab_tabla2.getValor("IDE_CMREP")), Integer.parseInt(tab_tabla1.getValor("IDE_FALLECIDO")));
                                        cementerioM.set_updateDesHabilita(Integer.parseInt(tab_tabla1.getValor("IDE_CATASTRO")));
                                    }
                                }
                            }
                        } else {
                            utilitario.agregarMensajeInfo("datos de lugar no disponibles", null);
                        }
                    }
                } else {
                    utilitario.agregarMensajeError("Seleccione check", null);
                }
            } else {
                utilitario.agregarMensajeError("Liquidacion se encuentra generada", "No se permite hacer cambios,Anule");
            }
        } else {
            utilitario.agregarMensajeError("Los campos obligatorio deben ser llenados", null);
        }
    }

    public void cemeneterioMovimiento() {
        conSqler.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcer"));
        conSqler.NOMBRE_MARCA_BASE = "sqlserver";
        System.out.println("tab_tabla4.getValor(\"IDE_DET_MOVIMIENTO\")" + tab_tabla4.getValor("IDE_DET_MOVIMIENTO"));
        TablaGenerica tabDato = cementerioM.getMovimiento(Integer.parseInt(tab_tabla4.getValor("IDE_DET_MOVIMIENTO")));
        System.out.println("entreamo a sp ");
        Date fecha1 = utilitario.DeStringADateformato2(tab_tabla4.getValor("fecha_desde"));
        String str_fecha1 = utilitario.DeDateAStringformato2(fecha1);
        Date fecha2 = utilitario.DeStringADateformato2(tab_tabla4.getValor("fecha_hasta"));
        String str_fecha2 = utilitario.DeDateAStringformato2(fecha2);

        String compuestoPlazo = str_fecha1 + " AL " + str_fecha2 + " POR " + tab_tabla4.getValor("PERIODO_ARRENDAMIENTO") + " AÑOS ";
        System.out.println("compuestoPlazo a sp " + compuestoPlazo);

    }

    @Override
    public void actualizar() {
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
        aut_busca.actualizar();
        aut_busca.setSize(70);
        utilitario.addUpdate("aut_busca");
    }

    public void consultaCatastro() {
        int ban = 0, ban1 = 0;
        TablaGenerica tabLugar = cementerioM.getRecuperaLugar(Integer.parseInt(cmbTipo.getValue() + ""));
        if (!tabLugar.isEmpty()) {
            if (tabLugar.getValor("detalle_lugar") == "MAUSOLEO PRIVADO") {
                ban1 = 1;
            } else if (tabLugar.getValor("detalle_lugar") == "OSARIO") {
                ban1 = 1;
            } else if (tabLugar.getValor("detalle_lugar") == "NICHO PERPETUO/COMUN") {
                ban1 = 1;
            } else {
                if (Double.valueOf(tabLugar.getValor("periodo")) > 0.0) {
                    if (Double.valueOf(tabLugar.getValor("valor")) > 0.0) {
                        if (tabLugar.getValor("codigofiscal_cuenta") != null) {
                            TablaGenerica tabCuentas = cementerioM.getVerificaCuentas(tabLugar.getValor("codigofiscal_cuenta"));
                            if (!tabCuentas.isEmpty()) {
                                ban1 = 1;
                            } else {
                                utilitario.agregarMensajeInfo("Codigo de cuenta no es valido", null);
                            }
                            ban1 = 1;
                        } else {
                            ban1 = 1;
                            utilitario.agregarMensajeInfo("Falta codigo de cuenta en lugar", "Codigo fiscal importante para generar liquidación");
                        }
                    } else {
                        ban1 = 1;
                        utilitario.agregarMensajeInfo("Falta Valor de lugar", "Valor es importante para generar la liquidacion");
                    }
                } else {
                    ban1 = 0;
                    utilitario.agregarMensajeInfo("Periodo es un dato obligatorio del lugar", null);
                }
            }

            if (ban1 == 1) {
                TablaGenerica tabDato = cementerioM.consultaCatastro(Integer.parseInt(setRegistros.getValorSeleccionado()));
                if (!tabDato.isEmpty()) {
                    if (tabDato.getValor("DISPONIBLE_OCUPADO").equals("DISPONIBLE")) {
                        ban = 1;
                    } else {
                        String fechActual = utilitario.getFechaActual();
                        Date fecha = utilitario.DeStringADateformato2(fechActual);

                        String fechRenvFallecido = tabDato.getValor("fecha_hasta");
                        Date fecha1 = utilitario.DeStringADateformato2(fechRenvFallecido);
                        int dias = utilitario.getDiferenciasDeFechas(fecha1, fecha);
                        String fechaFallecido = tabDato.getValor("fecha_fallecido");
                        int anio = utilitario.getEdad(fechaFallecido);


                        if (dias <= 0) {
                            if (anio > Integer.parseInt(tabDato.getValor("periodo"))) {
                                ban = 1;
                            } else {
                                ban = 2;
                            }
                        } else {
                            if (tabDato.getValor("catastro_habilita") == null) {
                                if (anio > Integer.parseInt(tabDato.getValor("periodo"))) {
                                    ban = 1;
                                } else {
                                    ban = 2;
                                }
                            } else {
                                if (tabDato.getValor("catastro_habilita").equals("1")) {
                                    if (anio > Integer.parseInt(tabDato.getValor("periodo"))) {
                                        ban = 1;
                                    } else {
                                        ban = 2;
                                    }
                                } else {
                                    ban = 2;
                                }
                            }
                        }
                    }
                } else {
                    utilitario.agregarMensajeError("Informaciòn no Disponible", "No Se Encuentra Registrado");
                }

                if (ban == 1) {
                    tab_tabla1.insertar();
                    tab_tabla2.insertar();
                    tab_tabla4.insertar();
                    tab_tabla1.setValor("IDE_CATASTRO ", tabDato.getValor("IDE_CATASTRO"));
                    tab_tabla4.setValor("fecha_hasta ", tabDato.getValor("fecha_hasta"));
                    tab_tabla4.setValor("IDE_CATASTRO ", tabDato.getValor("IDE_CATASTRO"));
                    setRegistros.cerrar();
                    utilitario.addUpdate("tab_tabla1");
                    utilitario.addUpdate("tab_tabla4");
                } else {
                    utilitario.agregarMensajeError("Periodo de Exhumacion no corresponde", null);
                }
            }
        }
    }

    public void buscaPersona() {
        String cedulaF, usu, pass, cedula, date_fallecido, str_fechad2, str_fecha2;
        Date str_fechad1, str_fecha1;
        cedulaF = cementerioM.cedulaFallecido(tab_tabla1.getValor("CEDULA_FALLECIDO"));
        if (!cedulaF.equals(tab_tabla1.getValor("CEDULA_FALLECIDO"))) {
            if (utilitario.validarCedula(tab_tabla1.getValor("CEDULA_FALLECIDO"))) {
                buscaCedulaFallecido();
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

    public void buscaCedulaFallecido() {
         String usu, pass, cedula, date_fallecido, str_fechad2, str_fecha2;
        Date str_fechad1, str_fecha1;
                        usu = "cementerio";
                pass = "cmtr2016$";
                cedula = tab_tabla1.getValor("CEDULA_FALLECIDO");
                paq_webservice.ConsultaCiudadano service = new paq_webservice.ConsultaCiudadano();
                ClassCiudadania persona = service.getConsultaCiudadanoSoap().busquedaPorCedula(cedula, usu, pass);

                int sexo;
                if (persona.getSexo().equals("FEMENINO")) {
                    sexo = 2;
                } else {
                    sexo = 1;
                }
                
                str_fecha1 = utilitario.DeStringADateformato3(persona.getFechaNacimiento());
                System.out.println("str_fecha1<<<<<" + str_fecha1);
                str_fecha2 = utilitario.DeDateAString(str_fecha1);
                System.out.println("str_fecha2<<<<<" + str_fecha2);

                tab_tabla1.setValor("NOMBRES", persona.getNombre());
                tab_tabla1.setValor("IDE_CMGEN", String.valueOf(sexo));
                tab_tabla1.setValor("FECHA_NACIMIENTO", str_fecha2);

                date_fallecido = persona.getFechaFallecido();
                if (date_fallecido != null && date_fallecido.isEmpty() == false) {
                    str_fechad1 = utilitario.DeStringADateformato3(date_fallecido);
                    str_fechad2 = utilitario.DeDateAString(str_fechad1);

                } else {
                    String fecha_fallecido = busquedaCedulaActual(cedula, usu, pass).getFechaFallecido();
                    if (fecha_fallecido != null && fecha_fallecido.isEmpty() == false) {
                        str_fechad1 = utilitario.DeStringADateformato3(fecha_fallecido);
                        str_fechad2 = utilitario.DeDateAString(str_fechad1);
                    } else {
                        str_fechad1 = null;
                        str_fechad2 = null;
                        tab_tabla1.setValor("NOMBRES", null);
                        tab_tabla1.setValor("IDE_CMGEN", null);
                        tab_tabla1.setValor("FECHA_NACIMIENTO", null);
                        utilitario.agregarMensajeError(null, "Fecha de Defunciòn no registrada");
                    }
                }

                tab_tabla1.setValor("FECHA_DEFUNCION", str_fechad2);
                utilitario.addUpdate("tab_tabla1");
    }
    
    public void buscaPersonaDatos() {
        if (tab_tabla2.getValor("IDE_CMTID").equals("3")) {
            if (utilitario.validarCedula(tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP"))) {
                buscaCedulaRepresentante();
            } else {
                utilitario.agregarMensaje("Número de Cédula Incorrecto", null);
            }
        } else if (tab_tabla2.getValor("IDE_CMTID").equals("1")) {
            if (utilitario.validarRUC(tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP"))) {
                buscaCedulaRepresentante();
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe", "");
            }
        } else if (tab_tabla2.getValor("IDE_CMTID").equals("2")) {
            buscaCedulaRepresentante();
        }
    }

    public void buscaCedulaRepresentante() {
        String usu, pass, cedula;
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
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public void buscaRegistro1() {
        setRegistros1.dibujar();
    }

    public void mostrarDatos() {
        if (cmbTipo.getValue() != null && cmbTipo.getValue().toString().isEmpty() == false) {
            setRegistros.getTab_seleccion().setSql("select  a.IDE_CATASTRO,SECTOR,MODULO,NUMERO,max(case when fecha_hasta is null then '" + utilitario.getFechaActual() + "' else fecha_hasta end)as fecha_hasta , (case when total_ingresa is null then 0 else total_ingresa end)as TOTAL_INGRESA\n"
                    + " from CMT_CATASTRO a left join cmt_lugar b on a.ide_lugar=b.ide_lugar\n"
                    + " left join  (select max(fecha_hasta)as fecha_hasta, ide_catastro from CMT_DETALLE_MOVIMIENTO  group by ide_catastro,ide_fallecido)c  on  c.IDE_CATASTRO=a.IDE_CATASTRO  \n"
                    + "where (case when total_ingresa is null then 0 else  total_ingresa end) <3 \n"
                    + "and   a.IDE_lugar ='" + cmbTipo.getValue() + "' or habilita=1  group by a.IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO,(case when total_ingresa is null then 0 else total_ingresa end),habilita  order by SECTOR,MODULO,NUMERO");
            setRegistros.getTab_seleccion().ejecutarSql();
            setRegistros.getTab_seleccion().imprimirSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void datosTipoFallecido() {
        TablaGenerica tab_dato = cementerioM.getRecuperaCedulaTipoFallecido(tab_tabla1.getValor("TIPO_FALLECIDO"));
        System.out.println("tab_tabla1.getValorzzz<<<<<<<<<<<<" + tab_tabla1.getValor("TIPO_FALLECIDO"));
        if (!tab_dato.isEmpty()) {
            if (tab_tabla1.getValor("TIPO_FALLECIDO").equals("1") || tab_tabla1.getValor("TIPO_FALLECIDO").equals("4") || tab_tabla1.getValor("TIPO_FALLECIDO").equals("5")) {
                tab_tabla1.setValor("CEDULA_FALLECIDO", tab_dato.getValor("CEDULA"));
                tab_tabla1.getColumna("NOMBRES").setLectura(false);
                tab_tabla1.getColumna("FECHA_NACIMIENTO").setLectura(false);
                tab_tabla1.getColumna("FECHA_DEFUNCION").setLectura(false);
                tab_tabla1.getColumna("IDE_CMGEN").setLectura(false);
                utilitario.addUpdate("tab_tabla1");
            } else {
                tab_tabla1.setValor("CEDULA_FALLECIDO", null);
                tab_tabla1.getColumna("NOMBRES").setLectura(true);
                tab_tabla1.getColumna("FECHA_NACIMIENTO").setLectura(true);
                tab_tabla1.getColumna("FECHA_DEFUNCION").setLectura(true);
                tab_tabla1.getColumna("IDE_CMGEN").setLectura(true);
                utilitario.addUpdate("tab_tabla1");
            }
//            datosUsuarioAcc();
        } else {
            utilitario.agregarMensaje("Solicitante Sin Datos", "");
        }
    }

    public void creaConcepto() {
        String rows = "";
        String Texto = "";
        String rows1 = "";
        String tipo = "";

        TablaGenerica tab_lugar = cementerioM.getListaFallecidos(Integer.parseInt(tab_tabla1.getValor("IDE_CATASTRO")));
        if (!tab_lugar.isEmpty()) {
            for (int i = 0; i < tab_lugar.getTotalFilas(); i++) {
                rows += (tab_lugar.getValor(i, "NOMBRES ") + ",");
            }
        } else {
            rows = "";
        }

        System.err.println("" + tab_tabla1.getValor("NOMBRES"));
        rows1 = tab_tabla1.getValor("NOMBRES");

        TablaGenerica tadDato = utilitario.consultar("select (select DETALLE_LUGAR from CMT_LUGAR where IDE_LUGAR = CMT_CATASTRO.IDE_LUGAR) as IDE_LUGAR ,SECTOR,modulo,NUMERO\n"
                + "from CMT_CATASTRO \n"
                + "where IDE_CATASTRO= " + tab_tabla1.getValor("IDE_CATASTRO"));

        Texto = "PAGO POR ARRENDAMIENTO DE UN " + tadDato.getValor("IDE_LUGAR") + " EN EL CEMENTERIO MUNICIPAL SECTOR: " + tadDato.getValor("SECTOR") + " MODULO: " + tadDato.getValor("modulo") + "\n"
                + "NUMERO: " + tadDato.getValor("NUMERO") + ". PARA LOS RESTOS DEL SR(A)(S): " + rows + "" + rows1 + " POR " + tab_tabla4.getValor("PERIODO_ARRENDAMIENTO") + " AÑOS.\n"
                + "DESDE EL " + tab_tabla4.getValor("FECHA_DESDE") + " HASTA EL " + tab_tabla4.getValor("FECHA_HASTA") + "" + tab_tabla1.getValor("catastro_anterior");

        tab_tabla4.setValor("antecedentes", Texto);
        System.err.println("" + Texto);
        utilitario.addUpdate("tab_tabla4");
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTabConsulta() {
        return tabConsulta;
    }

    public void setTabConsulta(Tabla tabConsulta) {
        this.tabConsulta = tabConsulta;
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

    public Conexion getConSqler() {
        return conSqler;
    }

    public void setConSqler(Conexion conSqler) {
        this.conSqler = conSqler;
    }

    public SeleccionTabla getSetRegistros() {
        return setRegistros;
    }

    public void setSetRegistros(SeleccionTabla setRegistros) {
        this.setRegistros = setRegistros;
    }

    public SeleccionTabla getSetRegistros1() {
        return setRegistros1;
    }

    public void setSetRegistros1(SeleccionTabla setRegistros1) {
        this.setRegistros1 = setRegistros1;
    }

    public Tabla getSetSolicitu() {
        return setSolicitu;
    }

    public void setSetSolicitu(Tabla setSolicitu) {
        this.setSolicitu = setSolicitu;
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

//    private static ClassCiudadania busquedaCedula(java.lang.String cedula, java.lang.String usuario, java.lang.String password) {
//        paq_webservice.ConsultaCiudadano service = new paq_webservice.ConsultaCiudadano();
//        paq_webservice.ConsultaCiudadanoSoap port = service.getConsultaCiudadanoSoap();
//        return port.busquedaPorCedula(cedula, usuario, password);
//    }
//    
    private static ClassCiudadania busquedaCedulaActual(java.lang.String cedula, java.lang.String usuario, java.lang.String password) {
        paq_webservice.ConsultaCiudadano service = new paq_webservice.ConsultaCiudadano();
        paq_webservice.ConsultaCiudadanoSoap port = service.getConsultaCiudadanoSoap();
        return port.busquedaCedulaActual(cedula, usuario, password);
    }
}