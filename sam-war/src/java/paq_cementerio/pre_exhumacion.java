/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_cementerio.ejb.cementerio;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class pre_exhumacion extends Pantalla {

    private Tabla tabConsulta = new Tabla();
    private Tabla tabFallecido = new Tabla();
    private Tabla tabRepresentante = new Tabla();
    private Tabla tabMovimiento = new Tabla();
    private Tabla tabExhumacion = new Tabla();
    private AutoCompletar autBusca = new AutoCompletar();
    private Panel panOpcion = new Panel();
    private SeleccionTabla setFallecido = new SeleccionTabla();
    private Combo cmbOpcion = new Combo();
    private Texto txtBusqueda = new Texto();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private cementerio admin = (cementerio) utilitario.instanciarEJB(cementerio.class);

    public pre_exhumacion() {

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT IDE_FALLECIDO,CATASTRO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,FECHA_HASTA\n"
                + "FROM dbo.CMT_FALLECIDO_RENOVACION");
        autBusca.setSize(70);

        Boton botBusca = new Boton();
        botBusca.setValue("Busqueda Para Exhumación");
        botBusca.setExcluirLectura(true);
        botBusca.setIcon("ui-icon-search");
        botBusca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(botBusca);

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
        list.add(fila1);;
        list.add(fila2);;
        list.add(fila3);;
        list.add(fila4);;

        cmbOpcion.setCombo(list);
        cmbOpcion.eliminarVacio();

//        cmbSeleccion.setId("cmbSeleccion");
//        cmbSeleccion.setCombo("SELECT IDE_CMACC, DETALLE_CMACC FROM CMT_ACCION where id_parametro = 'EXHU'");
//        cmbSeleccion.eliminarVacio();

        Grid griPri = new Grid();
        griPri.setColumns(1);

//        Grid griTipo = new Grid();
//        griTipo.setColumns(2);
//        griTipo.getChildren().add(new Etiqueta("Tipo Exhumacion :"));
//        griTipo.getChildren().add(cmbSeleccion);
//        griPri.getChildren().add(griTipo);

        Grid griBusca = new Grid();
        griBusca.setColumns(5);

        griBusca.getChildren().add(new Etiqueta("Buscar por :"));
        griBusca.getChildren().add(cmbOpcion);
        griBusca.getChildren().add(new Etiqueta("Ingrese: "));
        txtBusqueda.setSize(50);
        griBusca.getChildren().add(txtBusqueda);

        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar");
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("busquedaInfo");
        griBusca.getChildren().add(botBuscar);
        griPri.getChildren().add(griBusca);


        setFallecido.setId("setFallecido");
        setFallecido.setSeleccionTabla("SELECT IDE_FALLECIDO,CATASTRO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,FECHA_HASTA\n"
                + "FROM dbo.CMT_FALLECIDO_RENOVACION where IDE_FALLECIDO = -1", "IDE_FALLECIDO");
        setFallecido.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setFallecido.getTab_seleccion().getColumna("CATASTRO").setLongitud(30);
        setFallecido.getTab_seleccion().getColumna("liquidacion").setLongitud(30);
        setFallecido.getTab_seleccion().setRows(10);
        setFallecido.setRadio();
        setFallecido.getGri_cuerpo().setHeader(griPri);//consultaFallecido
        setFallecido.getBot_aceptar().setMetodo("consultaFallecido");
        setFallecido.setWidth("63%");
        setFallecido.setHeader("LISTA DE PERSONAS PARA EXHUMACION");
        agregarComponente(setFallecido);

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("CEMENTERIO MUNICIPAL - EXHUMACIÓN DE FALLECIDO");
        agregarComponente(panOpcion);

        dibujarPantalla();
    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabFallecido.setId("tabFallecido");
        tabFallecido.setTabla("CMT_FALLECIDO", "IDE_FALLECIDO", 1);
        if (autBusca.getValue() == null) {
            tabFallecido.setCondicion("IDE_FALLECIDO=-1");
        } else {
            tabFallecido.setCondicion("IDE_FALLECIDO=" + autBusca.getValor());
        }
        tabFallecido.getColumna("IDE_CMGEN").setCombo("select IDE_CMGEN,DETALLE_CMGEN from CMT_GENERO");
        tabFallecido.getColumna("IDE_CATASTRO").setCombo("SELECT IDE_CATASTRO,DETALLE_LUGAR+'-'+SECTOR+'-'+convert(varchar,MODULO)+'-'+convert(varchar,NUMERO) FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR = B.IDE_LUGAR ");
        tabFallecido.getColumna("IDE_CATASTRO").setLectura(true);
        tabFallecido.getColumna("FECHA_DOCUMENTO_CMARE").setVisible(false);
        tabFallecido.getColumna("CEDULA_FALLECIDO").setRequerida(true);
        tabFallecido.getColumna("CERTIFICADO_DEFUN").setRequerida(true);
        tabFallecido.getColumna("NOMBRES").setLectura(true);
        tabFallecido.getColumna("FECHA_NACIMIENTO").setLectura(true);
        tabFallecido.getColumna("FECHA_DEFUNCION").setLectura(true);
        tabFallecido.getColumna("IDE_CMGEN").setLectura(true);
        tabFallecido.getColumna("CEDULA_REPRESENTANTE").setVisible(false);
        tabFallecido.getColumna("REPRESENTANTE_ACTUAL").setVisible(false);
        tabFallecido.getColumna("TIPO_PAGO").setVisible(false);
        tabFallecido.getColumna("TIPO_FALLECIDO").setVisible(false);
        tabFallecido.setTipoFormulario(true);
        tabFallecido.agregarRelacion(tabRepresentante);
        tabFallecido.getGrid().setColumns(4);
        tabFallecido.setLectura(true);
        tabFallecido.dibujar();
        PanelTabla pntFallecido = new PanelTabla();
        pntFallecido.setMensajeWarn("DATOS DE FALLECIDO");
        pntFallecido.setPanelTabla(tabFallecido);

        tabRepresentante.setId("tabRepresentante");
        tabRepresentante.setTabla("CMT_REPRESENTANTE", "IDE_CMREP", 2);
        tabRepresentante.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setMetodoChange("buscaPersonaDatos");
        tabRepresentante.getColumna("IDE_CMTID").setCombo("select IDE_CMTID,DETALLE_CMTID from CMT_TIPO_DOCUMENTO");
        tabRepresentante.getColumna("EMAIL_CMREP").setMetodoChange("validaemail");
        tabRepresentante.getColumna("ESTADO").setValorDefecto("ACTIVO");
        tabRepresentante.getColumna("ESTADO").setVisible(false);
        tabRepresentante.getColumna("IDE_CMARE").setVisible(false);
        tabRepresentante.getColumna("ORDEN_REPRESENTANTE").setVisible(false);
        tabRepresentante.getGrid().setColumns(4);
        tabRepresentante.setTipoFormulario(true);
        tabRepresentante.setLectura(true);
        tabRepresentante.dibujar();
        PanelTabla pntRepresentante = new PanelTabla();
        pntRepresentante.setMensajeWarn("DATOS DE REPRESENTANTE");
        pntRepresentante.setPanelTabla(tabRepresentante);
        Division diVertical = new Division();
        diVertical.dividir2(pntFallecido, pntRepresentante, "52%", "V");


        tabMovimiento.setId("tabMovimiento");
        tabMovimiento.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 3);
        tabMovimiento.setCondicion("IDE_DET_MOVIMIENTO=-1");
        tabMovimiento.getColumna("IDE_TIPO_MOVIMIENTO").setCombo("SELECT IDE_TIPO_PAGO, DESCRIPCION FROM dbo.CMT_TIPO_PAGO where IDE_TIPO_PAGO in (5,6)");
        tabMovimiento.getGrid().setColumns(4);
        tabMovimiento.setTipoFormulario(true);
//        tabMovimiento.setLectura(true);
        tabMovimiento.dibujar();
        PanelTabla pntMovimiento = new PanelTabla();
        pntMovimiento.setMensajeWarn("DATOS DE ULTIMO MOVIMIENTO");
        pntMovimiento.setPanelTabla(tabMovimiento);

        tabExhumacion.setId("tabExhumacion");
        tabExhumacion.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 4);
        tabExhumacion.setCondicion("IDE_DET_MOVIMIENTO=-1");
        tabExhumacion.getColumna("IDE_TIPO_MOVIMIENTO").setCombo("SELECT IDE_CMACC, DETALLE_CMACC FROM CMT_ACCION where id_parametro = 'EXHU'");
        tabExhumacion.getColumna("IDE_TIPO_MOVIMIENTO").setMetodoChange("validaTipo");
//        tabExhumacion.getColumna("NUM_LIQUIDACION").setMetodoChange("validaLiquidacion");
        tabExhumacion.getColumna("FECHA_INGRESA").setValorDefecto(utilitario.getFechaActual());
        tabExhumacion.getGrid().setColumns(4);
        tabExhumacion.setTipoFormulario(true);
        tabExhumacion.dibujar();
        PanelTabla pntExhumacion = new PanelTabla();
        pntExhumacion.setMensajeWarn("DATOS PARA EXHUMACIÓN");
        pntExhumacion.setPanelTabla(tabExhumacion);

        Division divDivision = new Division();
        divDivision.setId("divDivision");
        divDivision.dividir3(diVertical, pntMovimiento, pntExhumacion, "30%", "40%", "H");

        Grupo gru = new Grupo();
        gru.getChildren().add(divDivision);
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
        setFallecido.dibujar();
    }

    public void busquedaInfo() {
        if (cmbOpcion.getValue().equals("0")) {
            buscaNombre();
        } else if (cmbOpcion.getValue().equals("1")) {
            buscaCedula();
        } else if (cmbOpcion.getValue().equals("2")) {
            buscaRepNombre();
        } else if (cmbOpcion.getValue().equals("3")) {
            buscaRepCedula();
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
        }
    }

    public void buscaNombre() {
        if (txtBusqueda.getValue() != null && txtBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("select * \n"
                    + "from (select IDE_FALLECIDO,CATASTRO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,FECHA_HASTA\n"
                    + "from CMT_FALLECIDO_RENOVACION \n"
                    + "where tipo_pago <> 4 or tipo_pago is null) a \n"
                    + "where nombres like '%" + txtBusqueda.getValue() + "%'");
            setFallecido.getTab_seleccion().setEmptyMessage("No se encontraron resultados, fuera de rango de exhumacion");
            setFallecido.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscaCedula() {
        if (txtBusqueda.getValue() != null && txtBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("select * \n"
                    + "from (select IDE_FALLECIDO,CATASTRO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,FECHA_HASTA\n"
                    + "from CMT_FALLECIDO_RENOVACION \n"
                    + "where tipo_pago <> 4 or tipo_pago is null) a \n"
                    + "where CEDULA_FALLECIDO like '%" + txtBusqueda.getValue() + "%'");
            setFallecido.getTab_seleccion().setEmptyMessage("No se encontraron resultados, fuera de rango de exhumacion");
            setFallecido.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscaRepNombre() {
        if (txtBusqueda.getValue() != null && txtBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("select * \n"
                    + "from (select IDE_FALLECIDO,CATASTRO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,FECHA_HASTA\n"
                    + "from CMT_FALLECIDO_RENOVACION \n"
                    + "where tipo_pago <> 4 or tipo_pago is null) a \n"
                    + "where representante = like '%" + txtBusqueda.getValue() + "%'");
            setFallecido.getTab_seleccion().setEmptyMessage("No se encontraron resultados, fuera de rango de exhumacion");
            setFallecido.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscaRepCedula() {
        if (txtBusqueda.getValue() != null && txtBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("select * \n"
                    + "from (select IDE_FALLECIDO,CATASTRO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,FECHA_HASTA\n"
                    + "from CMT_FALLECIDO_RENOVACION \n"
                    + "where tipo_pago <> 4 or tipo_pago is null) a \n"
                    + "where DOCUMENTO_IDENTIDAD_CMREP = like '%" + txtBusqueda.getValue() + "%'");
            setFallecido.getTab_seleccion().setEmptyMessage("No se encontraron resultados, fuera de rango de exhumacion");
            setFallecido.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void consultaFallecido() {
        Integer ban;
        if (setFallecido.getValorSeleccionado() != null) {
            TablaGenerica tabInfo = admin.consultaFallecido(Integer.parseInt(setFallecido.getValorSeleccionado() + ""));
            if (!tabInfo.isEmpty()) {
                ejecutar();
            } else {
                utilitario.agregarMensajeInfo("Información no disponible", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una registro", "");
        }
    }

    public void ejecutar() {
        limpiar();
        System.out.println("Fallecido()<<" + setFallecido.getValorSeleccionado());
        autBusca.setValor(setFallecido.getValorSeleccionado());
        dibujarPantalla();
        setFallecido.cerrar();
        tabExhumacion.insertar();
        datosRepresentante();
        datosMovimiento();
//        tabExhumacion.setValor("ide_tipo_movimiento", cmbSeleccion.getValue()+"");
        tabExhumacion.setValor("ide_catastro", tabFallecido.getValor("ide_catastro"));
        tabExhumacion.setValor("IDE_FALLECIDO", tabFallecido.getValor("IDE_FALLECIDO"));
        tabExhumacion.setValor("ide_cmrep", tabRepresentante.getValor("IDE_CMREP"));
        utilitario.addUpdate("tabExhumacion");

    }

    public void datosRepresentante() {
        if (!getFiltroRepresentante().isEmpty()) {
            tabRepresentante.setCondicion(getFiltroRepresentante());
            tabRepresentante.ejecutarSql();
            utilitario.addUpdate("tabRepresentante");
        }
    }

    private String getFiltroRepresentante() {
        String str_filtros = "";
        if (setFallecido.getValorSeleccionado() != null) {
            str_filtros = "IDE_CMREP = (SELECT DISTINCT (SELECT top 1 IDE_CMREP FROM CMT_REPRESENTANTE where IDE_FALLECIDO = " + Integer.parseInt(setFallecido.getValorSeleccionado()) + " order by IDE_CMREP desc)as codigo\n"
                    + "FROM CMT_FALLECIDO)";
        }
        return str_filtros;
    }

    public void datosMovimiento() {
        if (!getFiltroMovimiento().isEmpty()) {
            tabMovimiento.setCondicion(getFiltroMovimiento());
            tabMovimiento.ejecutarSql();
            utilitario.addUpdate("tabMovimiento");
        }
    }

    private String getFiltroMovimiento() {
        String str_filtros = "";
        if (setFallecido.getValorSeleccionado() != null) {
            str_filtros = "IDE_DET_MOVIMIENTO = (SELECT (SELECT top 1 IDE_DET_MOVIMIENTO FROM CMT_DETALLE_MOVIMIENTO where IDE_FALLECIDO = CMT_FALLECIDO.IDE_FALLECIDO order by FECHA_DESDE desc)as ide\n"
                    + "FROM CMT_FALLECIDO\n"
                    + "where IDE_FALLECIDO =" + Integer.parseInt(setFallecido.getValorSeleccionado()) + ")";
        }
        return str_filtros;
    }

    public Date validaFecha() {
        GregorianCalendar now = new GregorianCalendar();
        now.roll(Calendar.DAY_OF_MONTH, -15); //Incrementa 15 dias
        Date cuando = now.getTime(); //Obtenemos otro Date c1on la nueva fecha
        return cuando;
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        Date fechas= new Date();
        Date fechass= new Date();
        int dia = utilitario.getDia(utilitario.getFechaActual());
        int dias = dia -15;      
        Date fecha = utilitario.DeStringADateformato2(utilitario.getFechaActual());
         if(dias < 0){
            fechass = utilitario.sumarRestarMesFecha(fecha, -15);
             System.err.println(""+utilitario.DeDateAString(fechass));
            fechas = utilitario.sumarRestarDiasFecha(fechass, dias);
        }else{
            fechas = utilitario.sumarRestarDiasFecha(fecha, -15);
        }
        
        String fecha1 = utilitario.DeDateAString(fechas);
        String fecha2 = tabExhumacion.getValor("FECHA_INGRESA");
        Date fec1 = utilitario.DeStringADate(fecha1);
        Date fec2 = utilitario.DeStringADate(fecha2);
//        if (fec2.before(fec1)) {
//            utilitario.agregarMensaje("tiempo maximo de fecha a escoger no puede ser menor a 15 dias", "");
//        } else {
            if (tabExhumacion.getValor("observacion") != null && !tabExhumacion.getValor("observacion").isEmpty()) {
                if (tabExhumacion.guardar()) {
                    guardarPantalla();
                    procesoAdicional();
                }
            } else {
                utilitario.agregarMensaje("debe poner una observacion", "");
            }
//        }
    }

    public void validaTipo() {
        if (tabExhumacion.getValor("IDE_TIPO_MOVIMIENTO") != null) {
            TablaGenerica tabInfo = admin.consultaExhumacion(Integer.parseInt(tabExhumacion.getValor("IDE_TIPO_MOVIMIENTO") + ""));
            if (!tabInfo.isEmpty()) {
                if (tabInfo.getValor("parametro2").equals("A")) {
                    if (calculateAge(utilitario.DeStringADate(tabFallecido.getValor("fecha_defuncion"))) >= Integer.parseInt(tabInfo.getValor("parametro"))) {
                        tabExhumacion.getColumna("observacion").setLectura(false);
                        tabExhumacion.setValor("observacion", null);
                        utilitario.addUpdate("tabExhumacion");
                    } else {
                        tabExhumacion.setValor("observacion", null);
                        tabExhumacion.getColumna("observacion").setLectura(true);
                        utilitario.addUpdate("tabExhumacion");
                        utilitario.agregarMensaje("Tiempo para exhumación aun no concluye", "minimo deben ser " + tabInfo.getValor("parametro") + " años");
                    }
                } else if (tabInfo.getValor("parametro2").equals("M")) {
                    if (calcularMeses(utilitario.DeStringADate(tabFallecido.getValor("fecha_defuncion")), utilitario.DeStringADate(utilitario.getFechaActual())) >= Integer.parseInt(tabInfo.getValor("parametro"))) {
                        tabExhumacion.getColumna("observacion").setLectura(false);
                        tabExhumacion.setValor("observacion", null);
                        utilitario.addUpdate("tabExhumacion");
                    } else {
                        tabExhumacion.setValor("observacion", null);
                        tabExhumacion.getColumna("observacion").setLectura(true);
                        utilitario.addUpdate("tabExhumacion");
                        utilitario.agregarMensaje("Tiempo para exhumación aun no concluye", "minimo deben ser " + tabInfo.getValor("parametro") + " meses");
                    }
                } else {
                    tabExhumacion.getColumna("observacion").setLectura(false);
                    utilitario.addUpdate("tabExhumacion");
                }
                if (tabInfo.getValor("id_tipo").equals("SL")) {
                    validaLiquidacion();
                } else {
                }
            } else {
                utilitario.agregarMensaje("elegir tipo de exhumación", "");
            }
        } else {
            tabExhumacion.getColumna("observacion").setLectura(true);
            tabExhumacion.setValor("observacion", null);
            utilitario.addUpdate("tabExhumacion");
        }
    }

    public void procesoAdicional() {
        String totalLugar = admin.restaTotalOsario(tabFallecido.getValor("IDE_CATASTRO"));
        admin.set_updateDisponibleExhumado(tabFallecido.getValor("IDE_CATASTRO"));
        admin.set_updateTotalOsario(tabFallecido.getValor("IDE_CATASTRO"), totalLugar);
        admin.setUpdateFallecido(Integer.parseInt(tabFallecido.getValor("IDE_FALLECIDO")));

    }

    public void validaLiquidacion() {
        TablaGenerica tabInfo = admin.getLiquidacionTitulo(tabMovimiento.getValor("NUM_LIQUIDACION"));
        if (!tabInfo.isEmpty()) {
            tabExhumacion.getColumna("observacion").setLectura(false);
            tabExhumacion.getColumna("NUM_LIQUIDACION").setLectura(false);
            utilitario.addUpdate("tabExhumacion");
        } else {
            utilitario.agregarMensaje("liquidacion, Pago pendiente", null);
            tabExhumacion.setValor("observacion", null);
            tabExhumacion.setValor("NUM_LIQUIDACION", null);
            tabExhumacion.getColumna("observacion").setLectura(true);
            tabExhumacion.getColumna("NUM_LIQUIDACION").setLectura(true);
            utilitario.addUpdate("tabExhumacion");
        }
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int calculateAge(Date Date) {
        Calendar cal = Calendar.getInstance(); // current date
        int currYear = cal.get(Calendar.YEAR);
        int currMonth = cal.get(Calendar.MONTH);
        int currDay = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(Date); // now born date
        int years = currYear - cal.get(Calendar.YEAR);
        int bornMonth = cal.get(Calendar.MONTH);
        if (bornMonth == currMonth) { // same month
            return cal.get(Calendar.DAY_OF_MONTH) <= currDay ? years
                    : years - 1;
        } else {
            return bornMonth < currMonth ? years - 1 : years;
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
            return diffMonth;
        } catch (Exception e) {
            return 0;
        }
    }

    public Tabla getTabFallecido() {
        return tabFallecido;
    }

    public void setTabFallecido(Tabla tabFallecido) {
        this.tabFallecido = tabFallecido;
    }

    public Tabla getTabRepresentante() {
        return tabRepresentante;
    }

    public void setTabRepresentante(Tabla tabRepresentante) {
        this.tabRepresentante = tabRepresentante;
    }

    public Tabla getTabMovimiento() {
        return tabMovimiento;
    }

    public void setTabMovimiento(Tabla tabMovimiento) {
        this.tabMovimiento = tabMovimiento;
    }

    public Tabla getTabExhumacion() {
        return tabExhumacion;
    }

    public void setTabExhumacion(Tabla tabExhumacion) {
        this.tabExhumacion = tabExhumacion;
    }

    public SeleccionTabla getSetFallecido() {
        return setFallecido;
    }

    public void setSetFallecido(SeleccionTabla setFallecido) {
        this.setFallecido = setFallecido;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
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
