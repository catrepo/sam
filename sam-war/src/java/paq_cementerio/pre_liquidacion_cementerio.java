 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;

import paq_cementerio.ejb.cementerio;
import paq_registros.ejb.ServicioRegistros;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author l-suntaxi
 */
public class pre_liquidacion_cementerio extends Pantalla {

    private Conexion conSqler = new Conexion();
    private Conexion conSql = new Conexion();
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private AutoCompletar autBusca = new AutoCompletar();
    private Panel panOpcion = new Panel();
    private SeleccionTabla setTabla = new SeleccionTabla();
    /*
     * Variable para imprimir reportes
     */
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private cementerio cementerioM = (cementerio) utilitario.instanciarEJB(cementerio.class);
    private ServicioRegistros servicioregistros = (ServicioRegistros) utilitario.instanciarEJB(ServicioRegistros.class);

    public pre_liquidacion_cementerio() {
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_transporte.png");
        agregarComponente(quinde);
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        //cadena de conexión para otra base de datos
        conSqler.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcer"));
        conSqler.NOMBRE_MARCA_BASE = "sqlserver";
        conSql.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        conSql.NOMBRE_MARCA_BASE = "sqlserver";

        autBusca.setId("autBusca");
        autBusca.setConexion(conSqler);
        autBusca.setAutoCompletar("SELECT CODIGO_LIQUIDACION,NOMBRE_RAZONSOCIAL,VALORTOTAL_LIQUIDACION,FECHA_VENCIMIENTO FROM LIQUIDACIONES  where  COD_RUBRO='CMTR' AND DOMICILIO='CEMENTERIO MUNICIPAL' AND PERIODO_LIQUIDACION=2016 AND CODIGO_DIRECCION=140200 ORDER BY CODIGO_LIQUIDACION ");
        autBusca.setSize(70);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("LIQUIDACIONES");
        agregarComponente(panOpcion);
        dibujarPantalla();

        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar Registro");
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("abrirCuadro");
        bar_botones.agregarBoton(botBuscar);


        setTabla.setId("setTabla");
        setTabla.getTab_seleccion().setConexion(conSql);
        setTabla.setSeleccionTabla("SELECT IDE_DET_MOVIMIENTO,\n"
                + "CEDULA_FALLECIDO,\n"
                + "NOMBRES,\n"
                + "FECHA_DEFUNCION,\n"
                + "F.DETALLE_LUGAR,\n"
                + "SECTOR,\n"
                + "NUMERO,\n"
                + "E.MODULO,DETALLE_CMACC,\n"
                + "NOMBRES_APELLIDOS_CMREP,\n"
                + "DOCUMENTO_IDENTIDAD_CMREP,\n"
                + "DIRECCION_CMREP,\n"
                + "TELEFONOS_CMREP,\n"
                + "CELULAR_CMREP,\n"
                + "EMAIL_CMREP\n"
                + "\n"
                + "FROM\n"
                + "CMT_DETALLE_MOVIMIENTO A\n"
                + "INNER JOIN dbo.CMT_REPRESENTANTE B ON A.IDE_CMREP=B.IDE_CMREP\n"
                + "INNER JOIN dbo.CMT_FALLECIDO C ON C.IDE_FALLECIDO=A.IDE_FALLECIDO\n"
                + "INNER JOIN dbo.CMT_CATASTRO E ON E.IDE_CATASTRO =a.IDE_CATASTRO\n"
                + "INNER JOIN dbo.CMT_LUGAR F ON F.IDE_LUGAR = E.IDE_LUGAR\n"
                + "INNER JOIN CMT_ACCION G ON IDE_TIPO_MOVIMIENTO=G.IDE_CMACC where IDE_CMACC!=5  AND ESTADO=1 AND NUM_LIQUIDACION is null  ORDER BY IDE_DET_MOVIMIENTO DESC ", "IDE_DET_MOVIMIENTO");
        setTabla.getTab_seleccion().setEmptyMessage("No Encuentra Datos");
        setTabla.getTab_seleccion().getColumna("NOMBRES").setLongitud(50);
        setTabla.getTab_seleccion().getColumna("NOMBRES_APELLIDOS_CMREP").setLongitud(50);
        setTabla.getTab_seleccion().getColumna("DETALLE_LUGAR").setLongitud(25);
        setTabla.getTab_seleccion().getColumna("DETALLE_CMACC").setLongitud(25);
        setTabla.setWidth("80%");
        setTabla.getBot_aceptar().setMetodo("cemeneterioMovimiento");
        setTabla.getTab_seleccion().setRows(10);
        setTabla.setRadio();
        setTabla.setHeader("MOVIMIENTOS CEMENTERIO - FALLECIDO");
        agregarComponente(setTabla);

        /*
         * Configuración y llamado de objeto reporte
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conSqler);
        agregarComponente(sef_formato);
    }

    public void abrirCuadro() {
        setTabla.dibujar();
    }

    public void dibujarPantalla() {
        limpiarPanel();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(conSqler);
        // tab_tabla.setSql("SELECT CODIGO_LIQUIDACION,PERIODO_LIQUIDACION,PERIODO_TITULO,CODIGO_DIRECCION,NOMBRE_RAZONSOCIAL,DOMICILIO,ANTECEDENTES,OBSERVACIONES,FECHA_LIQUIDACION,LOGIN_LIQUIDADOR,VALORTOTAL_LIQUIDACION,VALORTOTAL_CONTADO,FECHA_VENCIMIENTO,EMITIDO,FECHA_EMISION,DIRECCION_REPRESENTANTE,REPRESENTANTE_LOCALCOMERCIAL,ACTIVIDAD_LOCALCOMERCIAL,COD_RUBRO,cod_catastro,NUM_LIQUIDACION,GIRO_PRODUCTO,CEDULA_RUC FROM LIQUIDACIONES WHERE PERIODO_LIQUIDACION='2016' order by CODIGO_LIQUIDACION ");
        tab_tabla.setTabla("LIQUIDACIONES", "CODIGO_LIQUIDACION", 1);
//        tab_tabla.getColumna("CEDULA_RUC").setMetodoChange("buscaPersona");

        /*
         if (autBusca.getValue() == null) {
         tab_tabla.setCondicion("CODIGO_LIQUIDACION=-1");
         } else {
         tab_tabla.setCondicion("CODIGO_LIQUIDACION=" + autBusca.getValor());
         }*/
        tab_tabla.getColumna("CODIGO_LIQUIDACION ").setLectura(true);
        tab_tabla.getColumna("NOMBRE_RAZONSOCIAL").setLectura(true);
        tab_tabla.getColumna("NUM_LIQUIDACION").setLectura(true);
        tab_tabla.getColumna("CEDULA_RUC").setLectura(true);
        tab_tabla.getColumna("ANTECEDENTES").setLectura(true);
        tab_tabla.getColumna("FECHA_LIQUIDACION").setLectura(true);
        tab_tabla.getColumna("VALORTOTAL_LIQUIDACION").setLectura(true);
        tab_tabla.getColumna("DIRECCION_REPRESENTANTE").setLectura(true);
        tab_tabla.getColumna("REPRESENTANTE_LOCALCOMERCIAL").setLectura(true);
        tab_tabla.getColumna("ACTIVIDAD_LOCALCOMERCIAL").setLectura(true);
        tab_tabla.getColumna("GIRO_PRODUCTO").setLectura(true);
        tab_tabla.getColumna("COD_CATASTRO").setLectura(true);
        tab_tabla.getColumna("DOMICILIO").setLectura(true);
        tab_tabla.getColumna("LOGIN_LIQUIDADOR").setLectura(true);
        tab_tabla.getColumna("fecha_desde").setLectura(true);
        tab_tabla.getColumna("fecha_hasta").setLectura(true);
        tab_tabla.getColumna("nombreDoc").setLectura(true);
        tab_tabla.getColumna("DIRECCION_REPRESENTANTE").setVisible(false);
        tab_tabla.getColumna("COD_RUTA").setVisible(false);
        tab_tabla.getColumna("COD_CATASTRO").setVisible(false);
        tab_tabla.getColumna("COPIA_CIORUC").setVisible(false);
        tab_tabla.getColumna("FECHA_LIQUIDACION").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("PLAZO").setVisible(false);
        tab_tabla.getColumna("BARRIO").setVisible(false);
        tab_tabla.getColumna("TRAMITE_REGYCONT").setVisible(false);
        tab_tabla.getColumna("LOGIN_LIQUIDADOR").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tab_tabla.getColumna("RESP_LIQUIDACION").setVisible(false);
        tab_tabla.getColumna("VALORTOTAL_CONVENIO").setVisible(false);
        tab_tabla.getColumna("FECHA_EMISION").setVisible(false);
        tab_tabla.getColumna("EST_PROPIEDAD").setVisible(false);
        tab_tabla.getColumna("EST_VIA").setVisible(false);
        tab_tabla.getColumna("TELEFONO").setVisible(false);
        tab_tabla.getColumna("VAL_AREMT2").setVisible(false);
        tab_tabla.getColumna("VAL_AREACO").setVisible(false);
        tab_tabla.getColumna("CARTA_IMPREDIAL").setVisible(false);
        tab_tabla.getColumna("AUTORIZ_ROTURASFALTO").setVisible(false);
        tab_tabla.getColumna("ESTADO_LIQUIDACION").setVisible(false);
        tab_tabla.getColumna("NUMERO_TITULO").setVisible(false);
        tab_tabla.getColumna("FECHA_ESTADO_LIQUIDACION").setVisible(false);
        tab_tabla.getColumna("NUMERO_COMPROMISO").setVisible(false);
        tab_tabla.getColumna("NUMERO_SOLICITUD").setVisible(false);
        tab_tabla.getColumna("FECHA_SOLICITUD").setVisible(false);
        tab_tabla.getColumna("NOMBRE_VENDEDOR").setVisible(false);
        tab_tabla.getColumna("CEDULA_VENDEDOR").setVisible(false);
        tab_tabla.getColumna("MES_LIQUIDACION").setVisible(false);
        tab_tabla.getColumna("val_avterr").setVisible(false);
        tab_tabla.getColumna("val_avcons").setVisible(false);
        tab_tabla.getColumna("val_otrmej").setVisible(false);
        tab_tabla.getColumna("val_real").setVisible(false);
        tab_tabla.getColumna("val_exonera").setVisible(false);
        tab_tabla.getColumna("liq_fecrev").setVisible(false);
        tab_tabla.getColumna("liq_usurev").setVisible(false);
        tab_tabla.getColumna("liq_mejper").setVisible(false);
        tab_tabla.getColumna("liq_cem").setVisible(false);
        tab_tabla.getColumna("liq_alicuo").setVisible(false);
        tab_tabla.getColumna("liq_tipo").setVisible(false);
        tab_tabla.getColumna("liq_dya").setVisible(false);
        tab_tabla.getColumna("liq_canton").setVisible(false);
        tab_tabla.getColumna("base_imponible").setVisible(false);
        tab_tabla.getColumna("VALORTOTAL_CONTADO").setVisible(false);
        tab_tabla.getColumna("VALORTOTAL_CONTADO").setMetodoChange("valorLiquidacion");
        tab_tabla.getColumna("CODIGO_DIRECCION").setVisible(false);
        tab_tabla.getColumna("PERIODO_TITULO").setVisible(false);
        tab_tabla.getColumna("tipo").setVisible(false);
        tab_tabla.getColumna("clave_catastral").setVisible(false);
        tab_tabla.getColumna("nombreDoc").setVisible(false);
        tab_tabla.getColumna("login_update").setVisible(false);
        tab_tabla.getColumna("fecha_update").setVisible(false);
        tab_tabla.getColumna("motivo").setVisible(false);
        tab_tabla.getColumna("FECHA_VENCIMIENTO").setVisible(false);
        tab_tabla.getColumna("EMITIDO ").setVisible(false);
        tab_tabla.getColumna("COD_RUBRO ").setVisible(false);
        tab_tabla.getColumna("OBSERVACIONES").setVisible(false);
        tab_tabla.getColumna("COD_CATASTRO").setRequerida(true);
        tab_tabla.setCondicion("where NUM_LIQUIDACION in (select top  10 NUM_LIQUIDACION from LIQUIDACIONES WHERE NUM_LIQUIDACION LIKE '%CMTR%'order by FECHA_LIQUIDACION desc)");
        tab_tabla.setCampoOrden("fechA_LIQUIDACION desc");
        tab_tabla.getColumna("numDocumento").setRequerida(true);
//        tab_tabla.getColumna("clave_catastral").setMetodoChange("valorLiquidacion");
        tab_tabla.getColumna("numDocumento").setMetodoChange("plazoLugar");

        tab_tabla.getColumna("FECHA_DESDE").setMetodoChange("fechaDesdeHasta");
        tab_tabla.getColumna("COD_RUTA").setRequerida(true);
        tab_tabla.getColumna("FECHA_DESDE").setRequerida(true);
        tab_tabla.getColumna("FECHA_HASTA").setRequerida(true);
        tab_tabla.getColumna("fecha_desde").setMetodoChange("fechaVencimiento");
        tab_tabla.agregarRelacion(tab_tabla2);
        tab_tabla.setGenerarPrimaria(false);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setConexion(conSqler);
        tab_tabla2.setGenerarPrimaria(false);
        tab_tabla2.setTabla("DETALLE_LIQUIDACION", "PERIODO_LIQUIDACION,IDE_DET_LIQ,IDE_DET_LIQ", 2);
        tab_tabla2.getColumna("ID_DETALLELIQUIDACION ").setVisible(false);
        tab_tabla2.getColumna("IDE_DET_LIQ ").setVisible(false);

        tab_tabla2.setCondicion("PERIODO_LIQUIDACION= " + tab_tabla.getValor("PERIODO_LIQUIDACION") + " and codigo_liquidacion=" + tab_tabla.getValor("codigo_liquidacion"));
        tab_tabla2.getGrid().setColumns(4);
        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_tabla2);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel, tabp1, "60%", "H");
//        agregarComponente(div_division);
        Grupo gru = new Grupo();
        gru.getChildren().add(div_division);
        panOpcion.getChildren().add(gru);
    }

    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    public void cemeneterioMovimiento() {
        tab_tabla.insertar();
        TablaGenerica tabDato = cementerioM.getMovimiento(Integer.parseInt(setTabla.getValorSeleccionado()));
        if (!tabDato.isEmpty()) {
            tab_tabla.setValor("CEDULA_RUC", tabDato.getValor("DOCUMENTO_IDENTIDAD_CMREP"));
            tab_tabla.setValor("NOMBRE_RAZONSOCIAL", tabDato.getValor("NOMBRES_APELLIDOS_CMREP"));
            tab_tabla.setValor("DOMICILIO", tabDato.getValor("DIRECCION_CMREP"));
            tab_tabla.setValor("ANTECEDENTES", tabDato.getValor("ANTECEDENTES"));
            tab_tabla.setValor("VALORTOTAL_LIQUIDACION", tabDato.getValor("valor"));
            tab_tabla.setValor("VALORTOTAL_CONTADO", tabDato.getValor("valor"));
            tab_tabla.setValor("REPRESENTANTE_LOCALCOMERCIAL", tabDato.getValor("NOMBRES"));
            tab_tabla.setValor("ACTIVIDAD_LOCALCOMERCIAL", tabDato.getValor("CEDULA_FALLECIDO"));
            tab_tabla.setValor("GIRO_PRODUCTO", tabDato.getValor("GIRO_PRODUCTO"));
            tab_tabla.setValor("cod_catastro", tabDato.getValor("cod_catastro"));
            tab_tabla.setValor("CODIGO_DIRECCION", tabDato.getValor("CODIGO_DIRECCION"));
            tab_tabla.setValor("PERIODO_TITULO", tabDato.getValor("PERIODO_TITULO"));
            tab_tabla.setValor("PERIODO_LIQUIDACION", tabDato.getValor("PERIODO_LIQUIDACION"));
            tab_tabla.setValor("COD_RUBRO", tabDato.getValor("COD_RUBRO"));
//            tab_tabla.setValor("OBSERVACIONES", tabDato.getValor("OBSERVACIONES"));
            tab_tabla.setValor("COD_RUTA", tabDato.getValor("COD_RUTA"));

            setTabla.cerrar();
            utilitario.addUpdate("tab_tabla");
        } else {
            utilitario.agregarMensajeError("datos", "No Se Encuentra Registrado");
        }

        // tab_tabla2.insertar();
        TablaGenerica tabDato1 = cementerioM.getMovimiento(Integer.parseInt(setTabla.getValorSeleccionado()));
        if (!tabDato.isEmpty()) {
            tab_tabla.setValor("CEDULA_RUC", tabDato.getValor("DOCUMENTO_IDENTIDAD_CMREP"));
            tab_tabla.setValor("NOMBRE_RAZONSOCIAL", tabDato.getValor("NOMBRES_APELLIDOS_CMREP"));
            tab_tabla.setValor("DOMICILIO", tabDato.getValor("DIRECCION_CMREP"));
            tab_tabla.setValor("ANTECEDENTES", tabDato.getValor("ANTECEDENTES"));
            tab_tabla.setValor("VALORTOTAL_LIQUIDACION", tabDato.getValor("valor"));
            tab_tabla.setValor("VALORTOTAL_CONTADO", tabDato.getValor("valor"));
            tab_tabla.setValor("REPRESENTANTE_LOCALCOMERCIAL", tabDato.getValor("NOMBRES"));
            tab_tabla.setValor("ACTIVIDAD_LOCALCOMERCIAL", tabDato.getValor("CEDULA_FALLECIDO"));
            tab_tabla.setValor("GIRO_PRODUCTO", tabDato.getValor("GIRO_PRODUCTO"));
            tab_tabla.setValor("cod_catastro", tabDato.getValor("cod_catastro"));
            tab_tabla.setValor("CODIGO_DIRECCION", tabDato.getValor("CODIGO_DIRECCION"));
            tab_tabla.setValor("PERIODO_TITULO", tabDato.getValor("PERIODO_TITULO"));
            tab_tabla.setValor("PERIODO_LIQUIDACION", tabDato.getValor("PERIODO_LIQUIDACION"));
            tab_tabla.setValor("COD_RUBRO", tabDato.getValor("COD_RUBRO"));
//            tab_tabla.setValor("OBSERVACIONES", tabDato.getValor("OBSERVACIONES"));
            tab_tabla.setValor("COD_RUTA", tabDato.getValor("COD_RUTA"));

            setTabla.cerrar();
            utilitario.addUpdate("tab_tabla");
        } else {
            utilitario.agregarMensajeError("datos", "No Se Encuentra Registrado");
        }
    }

    /* public void ejecutaSp() {
     if (tab_tabla.isFilaInsertada()) {
     System.out.println("entreamo a sp ");
     utilitario.getConexion().ejecutarSql("EXEC ER.dbo.PA_LIQUIDACIONES_INSERT_DATOS_DEP "
     + "	@codigo_direccion ='" + tab_tabla.getValor("codigo_direccion") + "',\n"
     + "	@periodo_titulo ='" + tab_tabla.getValor("periodo_titulo") + "',\n"
     + "	@iniciales ='CMTR',\n"
     + "	@clave_catastral ='" + tab_tabla.getValor("clave_catastral") + "',\n"
     + "	@cedula_ruc ='" + tab_tabla.getValor("CEDULA_RUC") + "',\n"
     + "	@nombre_razonsocial ='" + tab_tabla.getValor("NOMBRE_RAZONSOCIAL") + "',\n"
     + "	@cedula_ruc2 ='',\n"
     + "	@nombre_razonsocial2 ='',\n"
     + "	@domicilio ='CEMENTERIO MUNICIPAL',\n"
     + "	@barrio ='" + tab_tabla.getValor("BARRIO") + "',\n"
     + "	@antecedentes='" + tab_tabla.getValor("ANTECEDENTES") + "',\n"
     + "	@observaciones ='" + tab_tabla.getValor("OBSERVACIONES") + "',\n"
     + "	@tramite_regycont=' ',\n"
     + "	@login_liquidador ='" + tab_tabla.getValor("login_liquidador") + "',\n"
     + "	@valortotal_liquidacion =" + tab_tabla.getValor("VALORTOTAL_LIQUIDACION") + ",\n"
     + "	@cod_rubro =' " + tab_tabla.getValor("COD_RUBRO") + "',\n"
     + "	@direccion_representante =' ',\n"
     + "	@giro_producto ='" + tab_tabla.getValor("giro_producto") + "',\n"
     + "	@actividad_economica ='" + tab_tabla.getValor("ACTIVIDAD_LOCALCOMERCIAL") + "',\n"
     + "	@representante ='" + tab_tabla.getValor("REPRESENTANTE_LOCALCOMERCIAL") + "',\n"
     + "	@cod_catastro ='" + tab_tabla.getValor("cod_catastro") + "',\n"
     + "	@cod_ruta ='" + tab_tabla.getValor("cod_ruta") + " ',\n"
     + "	@fecha_ini =' " + tab_tabla.getValor("FECHA_LIQUIDACION") + "',\n"
     + "	@fecha_fin =' ',\n"
     + "	@tipo ='', -- numerico\n"
     + "	@numDocumento='',\n"
     + "	@id ='" + tab_tabla.getValor("CODIGO_LIQUIDACION") + "',\n"
     + "	@num_liquidacion2 ='" + tab_tabla.getValor("NUM_LIQUIDACION")
     + "'");
     }
     }*/
    public void fechaVencimiento() {
        System.out.println("fecha  " + tab_tabla.getValor("numDocumento"));
        String clave = tab_tabla.getValor("numDocumento");

        int numDocumento = Integer.parseInt(clave);
        int numero_dias = 365 * numDocumento;
        String fecha = tab_tabla.getValor("fecha_desde");
        Date fecha_a_sumar = utilitario.DeStringADate(fecha);
        Date nueva_fecha = utilitario.sumarDiasFecha(fecha_a_sumar, numero_dias);
        String str_fecha = utilitario.DeDateAString(nueva_fecha);
        tab_tabla.setValor("fecha_hasta", str_fecha);
        utilitario.addUpdateTabla(tab_tabla, "fecha_hasta", "");
        System.out.println("fecha_hasta  " + str_fecha);
    }

    public void fechaDesdeHasta() {
        int numDocumento = Integer.parseInt(tab_tabla.getValor("numDocumento"));
        int numero_dias = 365 * numDocumento;
        String fecha = tab_tabla.getValor("fecha_desde");
        Date fecha_a_sumar = utilitario.DeStringADate(fecha);
        Date nueva_fecha = utilitario.sumarDiasFecha(fecha_a_sumar, numero_dias);
        String str_fecha = utilitario.DeDateAString(nueva_fecha);
        tab_tabla.setValor("DIRECCION_REPRESENTANTE", str_fecha + fecha);
        utilitario.addUpdateTabla(tab_tabla, "DIRECCION_REPRESENTANTE", "");
        System.out.println("DIRECCION_REPRESENTANTE  " + str_fecha + fecha);
    }

    public void valorLiquidacion() {
        TablaGenerica tab_lugar = utilitario.consultar("select ide_lugar,valor,codigofiscal_cuenta,dsc_cuenta from cmt_lugar where ide_lugar in (select ide_lugar from CMT_CATASTRO where ide_catastro=" + tab_tabla.getValor("COD_CATASTRO") + ")");
        TablaGenerica tab_dato = cementerioM.getMaxdetalleli(tab_tabla.getValor("PERIODO_LIQUIDACION"), tab_tabla.getValor("PERIODO_TITULO"));

        double valor = Double.parseDouble(tab_lugar.getValor("valor"));
        double numDocumento = Double.parseDouble(tab_tabla.getValor("numDocumento"));
        double resultado = valor * numDocumento;
        tab_tabla.setValor("VALORTOTAL_LIQUIDACION", String.valueOf(resultado));
        utilitario.addUpdate("tab_tabla");
        System.out.println("valorLiquidacion  " + resultado);
    }

    public void plazoLugar() {
        System.out.println("tab_tabla.getValor(\"COD_CATASTRO\")" + tab_tabla.getValor("COD_CATASTRO"));
        TablaGenerica tabDato = cementerioM.periodoCatastro(tab_tabla.getValor("COD_CATASTRO"));

        System.out.println("tab_tabla.getValor(\"periodo\")" + tabDato.getValor("periodo"));
        System.out.println("tab_tabla.getValor(\"numDocumento\")" + tab_tabla.getValor("numDocumento"));

        int valor1 = Integer.parseInt(tabDato.getValor("periodo"));
        int valor2 = Integer.parseInt(tab_tabla.getValor("numDocumento"));
        if (valor2 <= valor1) {
            TablaGenerica tab_lugar = utilitario.consultar("select ide_lugar,valor,codigofiscal_cuenta,dsc_cuenta from cmt_lugar where ide_lugar in (select ide_lugar from CMT_CATASTRO where ide_catastro=" + tab_tabla.getValor("COD_CATASTRO") + ")");
            TablaGenerica tab_dato = cementerioM.getMaxdetalleli(tab_tabla.getValor("PERIODO_LIQUIDACION"), tab_tabla.getValor("PERIODO_TITULO"));

            double valor = Double.parseDouble(tab_lugar.getValor("valor"));
            double numDocumento = Double.parseDouble(tab_tabla.getValor("numDocumento"));
            double resultado = valor * numDocumento;
            tab_tabla.setValor("VALORTOTAL_LIQUIDACION", String.valueOf(resultado));
            utilitario.addUpdate("tab_tabla");
            System.out.println("valorLiquidacion  " + resultado);
        } else if (valor2 > valor1) {
            utilitario.agregarMensajeError("Plazo ", "Excede el periodo");
//              utilitario.addUpdate("tab_tabla");           
        }
    }

    @Override
    public void insertar() {
        if (tab_tabla2.isFocus()) {
            tab_tabla2.insertar();
            tab_tabla2.setValor("PERIODO_LIQUIDACION", tab_tabla.getValor("PERIODO_LIQUIDACION"));
            tab_tabla2.setValor("PERIODO_TITULO", tab_tabla.getValor("PERIODO_TITULO"));
            TablaGenerica tab_dato = cementerioM.getMaxdetalleli(tab_tabla.getValor("PERIODO_LIQUIDACION"), tab_tabla.getValor("PERIODO_TITULO"));

            tab_tabla2.setValor("IDE_DET_LIQ", tab_dato.getValor("maximo"));
            utilitario.addUpdateTabla(tab_tabla2, "PERIODO_LIQUIDACION,PERIODO_TITULO,IDE_DET_LIQ", "");
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla.isFilaInsertada()) {
            System.out.println("entreamo a sp ");
            Date fecha1 = utilitario.DeStringADateformato2(tab_tabla.getValor("fecha_desde"));
            String str_fecha1 = utilitario.DeDateAStringformato2(fecha1);

            Date fecha2 = utilitario.DeStringADateformato2(tab_tabla.getValor("fecha_hasta"));
            String str_fecha2 = utilitario.DeDateAStringformato2(fecha2);

            String compuestoPlazo = str_fecha1 + " AL " + str_fecha2 + " POR " + tab_tabla.getValor("numDocumento") + " AÑOS ";
            System.out.println("compuestoPlazo a sp " + compuestoPlazo);

            String query = "EXEC ER.dbo.PA_LIQUIDACIONES_INSERT_DATOS_DEP "
                    + "	@codigo_direccion ='" + tab_tabla.getValor("codigo_direccion") + "',\n"
                    + "	@periodo_titulo ='" + tab_tabla.getValor("periodo_titulo") + "',\n"
                    + "	@iniciales ='CMTR',\n"
                    + "	@clave_catastral ='',\n"
                    + "	@cedula_ruc ='" + tab_tabla.getValor("CEDULA_RUC") + "',\n"
                    + "	@nombre_razonsocial ='" + tab_tabla.getValor("NOMBRE_RAZONSOCIAL") + "',\n"
                    + "	@cedula_ruc2 ='',\n"
                    + "	@nombre_razonsocial2 ='',\n"
                    + "	@domicilio ='" + tab_tabla.getValor("domicilio") + "',\n"
                    + "	@barrio ='" + tab_tabla.getValor("BARRIO") + "',\n"
                    + "	@antecedentes='" + tab_tabla.getValor("ANTECEDENTES") + "',\n"
                    + "	@observaciones ='',\n"
                    + "	@tramite_regycont='',\n"
                    + "	@login_liquidador ='" + tab_tabla.getValor("login_liquidador") + "',\n"
                    //                    + "	@valortotal_liquidacion =" + tab_tabla.getValor("VALORTOTAL_LIQUIDACION") + ",\n"
                    + "	@valortotal_liquidacion =" + tab_tabla.getValor("VALORTOTAL_LIQUIDACION") + ",\n"
                    + "	@cod_rubro =' " + tab_tabla.getValor("COD_RUBRO") + "',\n"
                    + "	@direccion_representante ='" + compuestoPlazo + "',\n"
                    + "	@giro_producto ='" + tab_tabla.getValor("giro_producto") + "',\n"
                    + "	@actividad_economica ='" + tab_tabla.getValor("ACTIVIDAD_LOCALCOMERCIAL") + "',\n"
                    + "	@representante ='" + tab_tabla.getValor("REPRESENTANTE_LOCALCOMERCIAL") + "',\n"
                    + "	@cod_catastro ='" + tab_tabla.getValor("COD_CATASTRO") + "',\n"
                    + "	@cod_ruta ='" + tab_tabla.getValor("cod_ruta") + "',\n"
                    + "	@fecha_ini ='" + str_fecha1 + "',\n"
                    + "	@fecha_fin ='" + str_fecha2 + "',\n"
                    + "	@tipo ='', \n"
                    + "	@numDocumento='" + tab_tabla.getValor("numDocumento") + "',\n"
                    + "	@archivo=null,\n"
                    + "	@dirArch=null,\n"
                    + "	@nomArc=null,\n"
                    + "	@id ='" + tab_tabla.getValor("CODIGO_LIQUIDACION") + "',\n"
                    + "	@num_liquidacion2 ='" + tab_tabla.getValor("NUM_LIQUIDACION") + "'";
            System.out.println("guarda sqrda sp " + query);

            utilitario.getConexion().ejecutarSql(query);
            //}
            tab_tabla.ejecutarSql();
            System.out.println("tab_tabla.getValor xx " + tab_tabla.getValor("numDocumento"));

            TablaGenerica tab_lugar = utilitario.consultar("select ide_lugar,valor,codigofiscal_cuenta,dsc_cuenta from cmt_lugar where ide_lugar in (select ide_lugar from CMT_CATASTRO where ide_catastro=" + tab_tabla.getValor("COD_CATASTRO") + ")");
            TablaGenerica tab_dato = cementerioM.getMaxdetalleli(tab_tabla.getValor("PERIODO_LIQUIDACION"), tab_tabla.getValor("PERIODO_TITULO"));
            cementerioM.set_updateAntecedentes(tab_tabla.getValor("numDocumento"), compuestoPlazo, tab_tabla.getValor("NUM_LIQUIDACION"));
            cementerioM.set_updateAntecedentes1(tab_tabla.getValor("NUM_LIQUIDACION"));

            double valor = Double.parseDouble(tab_lugar.getValor("valor"));
            String clave = tab_tabla.getValor("numDocumento");

            double numDocumento = Double.parseDouble(clave);
            double resultado = valor * numDocumento;

            System.out.println("resultado" + tab_tabla.getValor("COD_CATASTRO"));

            // inserto en el detalle
             /*
             String sql ="insert into DETALLE_LIQUIDACION (CODIGO_LIQUIDACION,PERIODO_LIQUIDACION,ID_DETALLELIQUIDACION,PERIODO_TITULO,CODIGOFISCAL_CUENTA,DSC_CUENTA,VALOR_CUENTA,CONTADO) values ("
             + tab_tabla.getValor("CODIGO_LIQUIDACION")+",'"+tab_tabla.getValor("PERIODO_LIQUIDACION")+"',"+tab_dato.getValor("maximo")+",'"+tab_tabla.getValor("PERIODO_TITULO")+"','"+tab_lugar.getValor("codigofiscal_cuenta")+"','"+tab_lugar.getValor("dsc_cuenta")+"',"+resultado+",1)";
             System.out.println("imiro el detalle "+sql);
             utilitario.getConexion().ejecutarSql(sql);
             */
            cementerioM.setGuardaDetalle(tab_tabla.getValor("CODIGO_LIQUIDACION"), tab_tabla.getValor("PERIODO_LIQUIDACION"), tab_dato.getValor("maximo"), tab_tabla.getValor("PERIODO_TITULO"), tab_lugar.getValor("codigofiscal_cuenta"), tab_lugar.getValor("dsc_cuenta"), resultado + "", "1");

            System.out.println("entreamo a fecha_desde " + tab_tabla.getValor("fecha_desde"));
            System.out.println("entreamo a cod_ruta " + tab_tabla.getValor("cod_ruta"));
            System.out.println("entreamo a fecha_hasta " + tab_tabla.getValor("fecha_hasta"));

            cementerioM.set_updateValor(str_fecha1, tab_tabla.getValor("COD_RUTA"), str_fecha2, tab_tabla.getValor("NUM_LIQUIDACION"));

            tab_tabla2.setCondicion("PERIODO_LIQUIDACION= " + tab_tabla.getValor("PERIODO_LIQUIDACION") + " and codigo_liquidacion=" + tab_tabla.getValor("codigo_liquidacion"));
            tab_tabla2.ejecutarSql();
            utilitario.agregarMensaje("Se guardo Correctamente", "El registro se guardo con exito");
        }

        if (tab_tabla.isFilaModificada()) {
            System.out.println("entre a guardar1");

            tab_tabla.setConexion(conSqler);
            guardarPantalla();
            System.out.println("entre a guardar 2");

        }
        if (tab_tabla2.isFocus()) {
            System.out.println("entre a guardar");
            tab_tabla2.guardar();
            guardarPantalla();
        }

    }

    @Override
    public void inicio() {
        super.inicio(); //To change body of generated methods, choose Tools | Templates.
        if (tab_tabla2.isFocus()) {
            tab_tabla2.setCondicion("PERIODO_LIQUIDACION= " + tab_tabla.getValor("PERIODO_LIQUIDACION") + " and codigo_liquidacion=" + tab_tabla.getValor("codigo_liquidacion"));
            tab_tabla2.ejecutarSql();
        }

    }

    @Override
    public void fin() {
        super.fin(); //To change body of generated methods, choose Tools | Templates.
        if (tab_tabla2.isFocus()) {
            tab_tabla2.setCondicion("PERIODO_LIQUIDACION= " + tab_tabla.getValor("PERIODO_LIQUIDACION") + " and codigo_liquidacion=" + tab_tabla.getValor("codigo_liquidacion"));
            tab_tabla2.ejecutarSql();
        }
    }

    @Override
    public void siguiente() {
        super.siguiente(); //To change body of generated methods, choose Tools | Templates.
        if (tab_tabla2.isFocus()) {
            tab_tabla2.setCondicion("PERIODO_LIQUIDACION= " + tab_tabla.getValor("PERIODO_LIQUIDACION") + " and codigo_liquidacion=" + tab_tabla.getValor("codigo_liquidacion"));
            tab_tabla2.ejecutarSql();
        }
    }

    @Override
    public void atras() {
        super.atras(); //To change body of generated methods, choose Tools | Templates.
        if (tab_tabla2.isFocus()) {
            tab_tabla2.setCondicion("PERIODO_LIQUIDACION= " + tab_tabla.getValor("PERIODO_LIQUIDACION") + " and codigo_liquidacion=" + tab_tabla.getValor("codigo_liquidacion"));
            tab_tabla2.ejecutarSql();
        }
    }

    @Override
    public void eliminar() {
//        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        System.out.println("sali akkk");
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "MOVIMIENTO FALLECIDO":
                aceptoOrden();
                break;
        }
    }

    public void aceptoOrden() {
        System.out.println("voy por  akkk" + tab_tabla.getValor("NUM_LIQUIDACION"));
        switch (rep_reporte.getNombre()) {
            case "MOVIMIENTO FALLECIDO":

//              p_parametros.put("nom_resp", tab_tabla2.getValor("NICK_USUA") + "");
                p_parametros.put("NUM_LIQUIDACION", tab_tabla.getValor("NUM_LIQUIDACION") + "");
                System.out.println(p_parametros);
                System.out.println(rep_reporte.getPath());
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Conexion getConSqler() {
        return conSqler;
    }

    public void setConSqler(Conexion conSqler) {
        this.conSqler = conSqler;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

    public SeleccionTabla getSetTabla() {
        return setTabla;
    }

    public void setSetTabla(SeleccionTabla setTabla) {
        this.setTabla = setTabla;
    }

    public Conexion getConSql() {
        return conSql;
    }

    public void setConSql(Conexion conSql) {
        this.conSql = conSql;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
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
}
