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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_cementerio.ejb.cementerio;
import paq_webservice.ClassCiudadania;
import persistencia.Conexion;

public class pre_inhumacion_cementerio extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla4 = new Tabla();
    private Conexion conSqler = new Conexion();
    private SeleccionTabla setRegistros = new SeleccionTabla();
    private SeleccionTabla setRegistros1 = new SeleccionTabla();
    private SeleccionTabla setSolicitud = new SeleccionTabla();
    private Tabla setSolicitu = new Tabla();
    private Combo cmbTipo = new Combo();
    private AutoCompletar aut_busca = new AutoCompletar();
    private SeleccionCalendario sec_rango = new SeleccionCalendario();
    //Contiene todos los elementos de la plantilla
    private Panel panOpcion = new Panel();
    private Dialogo diaDialogoca = new Dialogo();
    private Grid gridCa = new Grid();
    private Combo cmbSeleccion = new Combo();
    private Texto texBusqueda = new Texto();
    private Dialogo diaDialogoso = new Dialogo();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private cementerio cementerioM = (cementerio) utilitario.instanciarEJB(cementerio.class);

    public pre_inhumacion_cementerio() {
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


//        Boton bot_busca1 = new Boton();
//        bot_busca1.setValue("Check List");
//        bot_busca1.setExcluirLectura(true);
//        bot_busca1.setIcon("ui-icon-search");
//        bot_busca1.setMetodo("buscaRegistro1");
//        bar_botones.agregarBoton(bot_busca1);

        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Disponibles");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("buscaRegistro");
        bar_botones.agregarBoton(bot_busca);

        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT TOP 10 a.IDE_fallecido,IDE_DET_MOVIMIENTO,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES  , FECHA_INGRESA,a.ver FROM CMT_DETALLE_MOVIMIENTO A\n"
                + "LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                + "LEFT JOIN  CMT_REPRESENTANTE C  ON A.IDE_FALLECIDO=C.IDE_FALLECIDO\n"
                + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC where IDE_TIPO_MOVIMIENTO=1");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(70);


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
        setRegistros.setSeleccionTabla("select  top 20 a.IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO,fecha_hasta,TOTAL_INGRESA\n"
                + " from CMT_CATASTRO a inner join cmt_lugar b on a.ide_lugar=b.ide_lugar\n"
                + " inner join  (select max(fecha_hasta)as fecha_hasta, ide_catastro from CMT_DETALLE_MOVIMIENTO  group by ide_catastro,ide_fallecido)c  on  c.IDE_CATASTRO=a.IDE_CATASTRO  \n"
                + "where (DETALLE_LUGAR='SITIO' OR DETALLE_LUGAR='NICHO') \n"
                + "and (case when total_ingresa is null then 0 else total_ingresa end) <=5\n"
                + " ORDER BY DETALLE_LUGAR", "IDE_CATASTRO");
        setRegistros.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setRegistros.getTab_seleccion().getColumna("SECTOR").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("NUMERO").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("MODULO").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("SECTOR").setLongitud(30);
        setRegistros.getTab_seleccion().getColumna("NUMERO").setLongitud(30);
        setRegistros.getTab_seleccion().getColumna("MODULO").setLongitud(30);
        setRegistros.getTab_seleccion().getColumna("detalle_lugar").setLongitud(30);
        setRegistros.getTab_seleccion().setRows(20);
        setRegistros.setWidth("60%");
        setRegistros.setRadio();
        setRegistros.setResizable(false);
        setRegistros.getGri_cuerpo().setHeader(gri_busca);
        setRegistros.getBot_aceptar().setMetodo("consultaCatastro");
        setRegistros.setHeader("BUSCAR CATASTRO CEMENTERIO");
        agregarComponente(setRegistros);

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



        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("INGRESO DE MOVIMIENTOS DE FALLECIDOS ANTERIORES AL CEMENTERIO MUNICIPAL ");
        agregarComponente(panOpcion);


        dibujarPantalla();
    }

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
        List lista = new ArrayList();
        Object fila1[] = {"1", "NORMAL"};
        Object fila2[] = {"2", "NEONATO"};
        Object fila3[] = {"3", "INDIGENTE"};
        Object fila4[] = {"4", "EXENTOS"};

        lista.add(fila1);
        lista.add(fila2);
        lista.add(fila3);
        lista.add(fila4);
        tab_tabla1.getColumna("TIPO_FALLECIDO").setCombo(lista);
        tab_tabla1.getColumna("IDE_CMGEN").setCombo("select IDE_CMGEN,DETALLE_CMGEN from CMT_GENERO");
        tab_tabla1.getColumna("IDE_CMGEN").setNombreVisual("GENERO");
        tab_tabla1.getColumna("CEDULA_FALLECIDO").setMetodoChange("buscaPersona");
        tab_tabla1.getColumna("CEDULA_FALLECIDO").setNombreVisual("CEDULA");
        tab_tabla1.getColumna("IDE_CATASTRO").setCombo("SELECT IDE_CATASTRO,DETALLE_LUGAR+'-'+SECTOR+'-'+convert(varchar,NUMERO)+'-'+convert(varchar,MODULO) FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR = B.IDE_LUGAR ");
        tab_tabla1.getColumna("IDE_CATASTRO").setNombreVisual("CATASTRO");
        tab_tabla1.getColumna("IDE_CATASTRO").setLectura(true);
        tab_tabla1.getColumna("FECHA_DOCUMENTO_CMARE").setVisible(false);
        tab_tabla1.getColumna("CERTIFICADO_DEFUN").setNombreVisual("CERTIFICADO DEFUNCION");
        tab_tabla1.getColumna("NOMBRES").setNombreVisual("APELLIDOS Y NOMBRES");
        tab_tabla1.getColumna("NOMBRES").setRequerida(true);
        tab_tabla1.getColumna("FECHA_NACIMIENTO").setNombreVisual("FECHA NACIMIENTO");
        tab_tabla1.getColumna("FECHA_DEFUNCION").setNombreVisual("FECHA DEFUNCION");
        tab_tabla1.getColumna("FECHA_DEFUNCION").setRequerida(true);
        tab_tabla1.getColumna("TIPO_FALLECIDO").setNombreVisual("TIPO");
        tab_tabla1.getColumna("TIPO_FALLECIDO").setRequerida(true);
        tab_tabla1.getColumna("CEDULA_REPRESENTANTE").setVisible(false);
        tab_tabla1.getColumna("REPRESENTANTE_ACTUAL").setVisible(false);
        tab_tabla1.getColumna("TIPO_PAGO").setVisible(false);
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
        tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setMetodoChange("buscaPersonaDatos");
        tab_tabla2.getColumna("IDE_CMTID").setCombo("select IDE_CMTID,DETALLE_CMTID from CMT_TIPO_DOCUMENTO");
        tab_tabla2.getColumna("IDE_CMREP").setNombreVisual("CODIGO");
        tab_tabla2.getColumna("IDE_CMTID").setNombreVisual("TIPO DOCUMENTO");
        tab_tabla2.getColumna("NOMBRES_APELLIDOS_CMREP").setNombreVisual("APELLIDOS Y NOMBRES");
        tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setNombreVisual("CEDULA");
        tab_tabla2.getColumna("TELEFONOS_CMREP").setNombreVisual("TELEFONO");
        tab_tabla2.getColumna("DIRECCION_CMREP").setNombreVisual("DIRECCION");
        tab_tabla2.getColumna("CELULAR_CMREP").setNombreVisual("CELULAR");
        tab_tabla2.getColumna("EMAIL_CMREP").setNombreVisual("EMAIL");
        tab_tabla2.getColumna("EMAIL_CMREP").setMetodoChange("validaemail");
//        tab_tabla2.getColumna("ESTADO").setCombo(lista);
        tab_tabla2.getColumna("ESTADO").setValorDefecto("1");
        tab_tabla2.getColumna("ESTADO").setVisible(false);
        tab_tabla2.getColumna("IDE_CMARE").setVisible(false);
//        tab_tabla2.getColumna("IDE_CMTID").setRequerida(true);
//        tab_tabla2.getColumna("TELEFONOS_CMREP").setRequerida(true);
//        tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setRequerida(true);
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
        tab_tabla4.getColumna("IDE_TIPO_MOVIMIENTO").setCombo("Select IDE_CMACC,DETALLE_CMACC from cmt_accion ");
        tab_tabla4.getColumna("IDE_TIPO_MOVIMIENTO").setRequerida(true);
        tab_tabla4.getColumna("FECHA_INGRESA ").setValorDefecto(utilitario.getFechaActual());
        tab_tabla4.getColumna("FECHA_INGRESA").setLectura(true);
        tab_tabla4.getColumna("PERIODO_ARRENDAMIENTO").setRequerida(true);
        tab_tabla4.getColumna("PERIODO_ARRENDAMIENTO").setMetodoChange("plazoLugar");
//        tab_tabla4.getColumna("FECHA_DESDE").setMetodoChange("fechaDesdeHasta");
        tab_tabla4.getColumna("FECHA_DESDE").setRequerida(true);
        tab_tabla4.getColumna("FECHA_HASTA").setRequerida(true);
//        tab_tabla4.getColumna("VALOR_LIQUIDACION").setRequerida(true);
//        tab_tabla4.getColumna("FECHA_HASTA").setLectura(true);
//        tab_tabla4.getColumna("FECHA_DESDE").setMetodoChange("fechaVencimiento");
//        tab_tabla4.getColumna("VALOR_LIQUIDACION").setLectura(true);
//        tab_tabla4.getColumna("NUM_LIQUIDACION").setLectura(true);
        tab_tabla4.getColumna("CODIGO_LIQUIDACION").setVisible(false);
        tab_tabla4.getColumna("PERIODO_LIQUIDACION ").setVisible(false);
        tab_tabla4.getColumna("PERIODO_TITULO").setVisible(false);
        tab_tabla4.getColumna("IDE_CATASTRO2").setVisible(false);
        tab_tabla4.getColumna("DOCUMENTOS").setVisible(false);
        tab_tabla4.getColumna("DOCUMENTOS").setUpload("certificados");
        tab_tabla4.getColumna("ver").setLectura(true);
//        tab_tabla4.getColumna("ver").setRequerida(true);
        tab_tabla4.getColumna("IDE_DET_MOVIMIENTO").setNombreVisual("CODIGO");
        tab_tabla4.getColumna("FECHA_INGRESA").setNombreVisual("FECHA ACTUAL");
        tab_tabla4.getColumna("FECHA_DESDE").setNombreVisual("DESDE");
        tab_tabla4.getColumna("FECHA_HASTA").setNombreVisual("HASTA");
        tab_tabla4.getColumna("NUM_LIQUIDACION").setNombreVisual("N. LIQUIDACION");
        tab_tabla4.getColumna("VALOR_LIQUIDACION").setNombreVisual("VALOR");
        tab_tabla4.getColumna("PERIODO_ARRENDAMIENTO").setNombreVisual("PERIODO ARRENDAMIENTO");
        tab_tabla4.getColumna("ESTADO_PAGO").setNombreVisual("ESTADO PAGO");
        tab_tabla4.getColumna("IDE_TIPO_MOVIMIENTO ").setNombreVisual("TIPO ACCION");
        tab_tabla4.getColumna("TIPO_RENOVACION").setVisible(false);
        tab_tabla4.getColumna("VALOR_LIQUIDACION_CALCULADO").setVisible(false);


        tab_tabla4.getColumna("ver").setCheck();
        tab_tabla4.getColumna("ESTADO_PAGO").setVisible(false);
        tab_tabla4.getColumna("TIPO_PAGO").setVisible(false);
        tab_tabla4.getColumna("ver").setVisible(false);
        tab_tabla4.getGrid().setColumns(8);
        tab_tabla4.setTipoFormulario(true);
        tab_tabla4.dibujar();
        PanelTabla pat_panel4 = new PanelTabla();
        pat_panel4.setMensajeWarn("DATOS DE MOVIMIENTO");
        pat_panel4.setPanelTabla(tab_tabla4);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(pat_panel1, pat_panel2, pat_panel4, "28%", "43%", "H");
        agregarComponente(div_division);

        Grupo gru = new Grupo();
        gru.getChildren().add(div_division);
        panOpcion.getChildren().add(gru);
    }

    public void buscaRegistro() {
        setRegistros.dibujar();
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

    public void fechaVencimiento() {
        Date fecha1 = utilitario.DeStringADateformato2(tab_tabla4.getValor("fecha_desde"));

        System.out.println("tab_tabla4.getValor(\"fecha_desde\")" + tab_tabla4.getValor("fecha_desde"));
        Date fecha2 = utilitario.DeStringADateformato2(tab_tabla1.getValor("FECHA_DEFUNCION"));
        System.out.println("tab_tabla1.getValor(\"FECHA_DEFUNCION\")" + tab_tabla1.getValor("FECHA_DEFUNCION"));

        System.out.println("fecha1  " + fecha1);
        System.out.println("fecha2  " + fecha2);
        if (utilitario.isFechaMayor(fecha1, fecha2)) {
//            System.out.println("fecha  " + tab_tabla4.getValor("PERIODO_ARRENDAMIENTO"));
//            String clave = tab_tabla4.getValor("PERIODO_ARRENDAMIENTO");
//
//
//            int PERIODO_ARRENDAMIENTO = Integer.parseInt(clave);
//            int numero_dias = 365 * PERIODO_ARRENDAMIENTO;
            String fecha = tab_tabla4.getValor("FECHA_DESDE");
//            Date fecha_a_sumar = utilitario.DeStringADate(fecha);
//            Date nueva_fecha = utilitario.sumarDiasFecha(fecha_a_sumar, numero_dias);
//            String str_fecha = utilitario.DeDateAString(nueva_fecha);
            tab_tabla4.setValor("FECHA_DESDE", fecha);
            utilitario.addUpdateTabla(tab_tabla4, "FECHA_DESDE", "");
//            System.out.println("FECHA_HASTA  " + str_fecha);
        } else {
            tab_tabla4.setValor("fecha_desde", null);
            tab_tabla4.setValor("fecha_hasta", null);

            utilitario.addUpdate("tab_tabla4");
            utilitario.agregarMensajeInfo("Fecha invalida", "Fecha de ingreso menor a la fecha defuncion");
        }
    }

    public void fechaDesdeHasta() {
        int PERIODO_ARRENDAMIENTO = Integer.parseInt(tab_tabla4.getValor("PERIODO_ARRENDAMIENTO"));
        int numero_dias = 365 * PERIODO_ARRENDAMIENTO;
        String fecha = tab_tabla4.getValor("FECHA_DESDE");
        Date fecha_a_sumar = utilitario.DeStringADate(fecha);
        Date nueva_fecha = utilitario.sumarDiasFecha(fecha_a_sumar, numero_dias);
        String str_fecha = utilitario.DeDateAString(nueva_fecha);
        tab_tabla4.setValor("DIRECCION_REPRESENTANTE", str_fecha + fecha);
        utilitario.addUpdateTabla(tab_tabla4, "DIRECCION_REPRESENTANTE", "");
        System.out.println("DIRECCION_REPRESENTANTE  " + str_fecha + fecha);
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

    public void plazoLugar() {

        TablaGenerica tab_lugar = utilitario.consultar("select ide_lugar,valor,codigofiscal_cuenta,dsc_cuenta from cmt_lugar where ide_lugar in (select ide_lugar from CMT_CATASTRO where ide_catastro=" + tab_tabla1.getValor("IDE_CATASTRO") + ")");
//            TablaGenerica tab_dato = cementerioM.getMaxdetalleli(tab_tabla4.getValor("PERIODO_LIQUIDACION"), tab_tabla4.getValor("PERIODO_TITULO"));
        System.out.println("tab_lugar.getValor(\"valor\")  " + tab_lugar.getValor("valor"));

        double valor = Double.parseDouble(tab_lugar.getValor("valor"));
        double PERIODO_ARRENDAMIENTO = Double.parseDouble(tab_tabla4.getValor("PERIODO_ARRENDAMIENTO"));
        double resultado = valor * PERIODO_ARRENDAMIENTO;
        tab_tabla4.setValor("VALOR_LIQUIDACION", String.valueOf(resultado));
        tab_tabla4.setValor("VALOR_LIQUIDACION_calculado", String.valueOf(resultado));

        utilitario.addUpdate("tab_tabla4");
        System.out.println("valorLiquidacion  " + resultado);

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
//                utilitario.getTablaisFocus().insertar();

//        if (tab_tabla1.isFocus()) {
//            aut_busca.limpiar();
//            utilitario.addUpdate("aut_busca");
//            tab_tabla1.limpiar();
        tab_tabla1.insertar();
//            tab_tabla2.limpiar();
        tab_tabla2.insertar();
//            tab_tabla4.limpiar();
        tab_tabla4.insertar();
//        }
    }

    @Override
    public void guardar() {
        if (tab_tabla1.guardar()) {
            if (tab_tabla2.guardar()) {
                if (tab_tabla4.guardar()) {
                    guardarPantalla();
                    cementerioM.set_updateDisponible(tab_tabla1.getValor("IDE_CATASTRO"));
                    utilitario.addUpdate("tab_tabla4");
                    String total_ingresa = cementerioM.maxTotalLugar(tab_tabla1.getValor("IDE_CATASTRO"));
                    System.out.println("total_ingresa" + total_ingresa);
                    cementerioM.set_updateTotalLugar(tab_tabla1.getValor("IDE_CATASTRO"), total_ingresa);

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
        System.out.println("consultaCatastro");
        tab_tabla1.insertar();
        tab_tabla2.insertar();
        tab_tabla4.insertar();
        System.out.println("setRegistros.getValorSeleccionado()<<<<<<<" + setRegistros.getValorSeleccionado());
        TablaGenerica tabDato = cementerioM.consultaCatastroAnterior(Integer.parseInt(setRegistros.getValorSeleccionado()));
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

    public void buscaPersona() {
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

    public void buscaPersonaDatos() {
        if (tab_tabla2.getValor("IDE_CMTID").equals("3")) {
            if (utilitario.validarCedula(tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP"))) {
                String usu = "cementerio";
                String pass = "cmtr2016$";
                String cedula = tab_tabla2.getValor("DOCUMENTO_IDENTIDAD_CMREP");
                tab_tabla2.setValor("NOMBRES_APELLIDOS_CMREP", busquedaCedulaActual(cedula, usu, pass).getNombre());
                tab_tabla2.setValor("DIRECCION_CMREP", busquedaCedulaActual(cedula, usu, pass).getDireccion());
                tab_tabla2.setValor("TELEFONOS_CMREP", busquedaCedulaActual(cedula, usu, pass).getTelefono());
                tab_tabla2.setValor("EMAIL_CMREP", busquedaCedulaActual(cedula, usu, pass).getMail());
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

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public void buscaRegistro1() {
        setRegistros1.dibujar();
    }

    public void mostrarDatos() {
        if (cmbTipo.getValue() != null && cmbTipo.getValue().toString().isEmpty() == false) {
            setRegistros.getTab_seleccion().setSql("select  a.IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO,TOTAL_INGRESA\n"
                    + " from CMT_CATASTRO a inner join cmt_lugar b on a.ide_lugar=b.ide_lugar\n"
                    + "where (DETALLE_LUGAR='SITIO' OR DETALLE_LUGAR='NICHO') \n"
                    + "and (case when total_ingresa is null then 0 else total_ingresa end) <=5\n"
                    + "and   a.IDE_lugar ='" + cmbTipo.getValue() + "'");
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

    private static ClassCiudadania busquedaCedulaActual(java.lang.String cedula, java.lang.String usuario, java.lang.String password) {
        paq_webservice.ConsultaCiudadano service = new paq_webservice.ConsultaCiudadano();
        paq_webservice.ConsultaCiudadanoSoap port = service.getConsultaCiudadanoSoap();
        return port.busquedaPorCedula(cedula, usuario, password);
    }
}