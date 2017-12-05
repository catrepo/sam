package paq_cementerio;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
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
//import paq_webservice.ClassCiudadania;

public class pre_consultar_rentas extends Pantalla {

    private Tabla tabConsulta = new Tabla();
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla4 = new Tabla();
    private SeleccionTabla setRegistros = new SeleccionTabla();
    private SeleccionTabla setSolicitud = new SeleccionTabla();
    private SeleccionTabla setSolicitud1 = new SeleccionTabla();
    private Tabla setSolicitu = new Tabla();
    private Combo cmbOpcion = new Combo();
    private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
    private AutoCompletar aut_busca = new AutoCompletar();
    private SeleccionCalendario sec_rango = new SeleccionCalendario();
    private Calendario fecha1 = new Calendario();
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

    public pre_consultar_rentas() {

//         Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logoactual.png");
        agregarComponente(quinde);
        bar_botones.quitarBotonInsertar();

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
            "2", "Catastro"
        };

        list.add(fila1);;
        list.add(fila2);;
        list.add(fila3);;


        cmbOpcion.setCombo(list);
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Disponibles");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("buscaRegistro");
        bar_botones.agregarBoton(bot_busca);

        setRegistros.setId("setRegistros");
        setRegistros.setSeleccionTabla("select top 10 IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO,DISPONIBLE_OCUPADO,(case when total_ingresa is null then 0 else total_ingresa end) as TOTAL_INGRESA  from CMT_CATASTRO a, cmt_lugar b\n"
                + " where a.ide_lugar=b.ide_lugar   AND DETALLE_LUGAR='NICHO COMUN'  ORDER BY DETALLE_LUGAR", "IDE_CATASTRO");
        setRegistros.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setRegistros.getTab_seleccion().getColumna("SECTOR").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("NUMERO").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("MODULO").setFiltro(true);
        setRegistros.getTab_seleccion().setRows(20);
        setRegistros.setWidth("50%");
        setRegistros.setRadio();
        setRegistros.setResizable(false);
        setRegistros.getBot_aceptar().setMetodo("consultaCatastro");
        setRegistros.setHeader("BUSCAR NICHO COMUN DISPONIBLE");
        agregarComponente(setRegistros);


        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT TOP 10 a.IDE_fallecido,NUM_LIQUIDACION,CEDULA_FALLECIDO,NOMBRES  , DETALLE_CMACC,FECHA_desde FROM CMT_DETALLE_MOVIMIENTO A\n"
                + "   LEFT JOIN CMT_FALLECIDO B  ON A.IDE_FALLECIDO=B.IDE_FALLECIDO\n"
                + "LEFT JOIN  CMT_REPRESENTANTE C  ON A.IDE_FALLECIDO=C.IDE_FALLECIDO\n"
                + "LEFT JOIN CMT_CATASTRO d ON a.ide_catastro=d.ide_catastro\n"
                + "left join CMT_ACCION e on IDE_TIPO_MOVIMIENTO=IDE_CMACC  ");
//        aut_busca.setMetodoChange("buscarPersona");
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
        gri_busca1.getChildren().add(new Etiqueta("Fecha Exumacion:"));
        fecha1.setSize(11);
        gri_busca1.getChildren().add(fecha1);
        griPri.getChildren().add(gri_busca1);
        bar_botones.agregarComponente(griPri);


        Boton bot_buscar1 = new Boton();
        bot_buscar1.setValue("Buscar");
        bot_buscar1.setIcon("ui-icon-search");
        bot_buscar1.setMetodo("busquedaInfo");
        bar_botones.agregarBoton(bot_buscar1);
        gri_busca1.getChildren().add(bot_buscar1);





        setSolicitud.setId("setSolicitud");
        setSolicitud.setSeleccionTabla("SELECT TOP 20 id_fallecido,cedula_fallecido,nombre_fallecido,fecha_defuncion,lugar_actual,representante_actual from FALLECIDOS ", "id_fallecido");
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
        setSolicitud.getTab_seleccion().setRows(10);
        setSolicitud.setRadio();
        setSolicitud.getGri_cuerpo().setHeader(gri_busca1);//consultaFallecido
        setSolicitud.getBot_aceptar().setMetodo("consultaFallecido");
        setSolicitud.setWidth("90%");
        setSolicitud.setHeader("BUSQUEDA POR PARAMETROS");
        agregarComponente(setSolicitud);
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
        panOpcion.setHeader("INGRESO DE MOVIMIENTOS AL CEMENTERIO MUNICIPAL - TRASLADO FALLECIDO DE OSARIO A NICHO COMUN");
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
        tab_tabla2.getGrid().setColumns(4);
        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setMensajeWarn("DATOS DE REPRESENTANTE");
        pat_panel2.setPanelTabla(tab_tabla2);

        tab_tabla4.setId("tab_tabla4");
        tab_tabla4.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 3);
        tab_tabla4.getGrid().setColumns(4);
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
        if (fecha1.getDate() == null) {
            System.out.println("INGRESO A BUSCAR");
            setSolicitud.getTab_seleccion().setSql("SELECT  B.IDE_FALLECIDO,DETALLE_LUGAR+'-'+SECTOR+'-'+convert(varchar,NUMERO)+'-'+convert(varchar,MODULO) AS CATASTRO,\n"
                    + " CEDULA_FALLECIDO,NOMBRES ,FECHA_DEFUNCION,FECHA_INGRESA,observacion FROM CMT_DETALLE_MOVIMIENTO A, CMT_FALLECIDO B , CMT_REPRESENTANTE C,CMT_CATASTRO d,  CMT_LUGAR E \n"
                    + "  WHERE E.IDE_LUGAR=D.IDE_LUGAR AND A.IDE_FALLECIDO=B.IDE_FALLECIDO AND a.IDE_CMREP=C.IDE_CMREP and a.ide_catastro=d.ide_catastro \n"
                    + "  and DETALLE_LUGAR='OSARIO' AND "
                    + " FECHA_INGRESA >= '" + fecha1.getFecha() + "'");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
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
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla4.isFilaInsertada()) {

            if (tab_tabla4.guardar()) {
                guardarPantalla();
                System.out.println("tab_tabla4.guardar()" + tab_tabla4.guardar());

                System.out.println("tab_tabla1.getValor(\"IDE_fallecido\")<<<<<<<<<<<<<<<<" + tab_tabla1.getValor("IDE_fallecido"));
                cementerioM.ingresaMovimiento(tab_tabla4.getValor("IDE_CATASTRO"), tab_tabla2.getValor("IDE_CMREP"), tab_tabla4.getValor("IDE_DET_MOVIMIENTO"), tab_tabla1.getValor("IDE_fallecido"));
                String totalNichoComun = cementerioM.maxTotalOsario(tab_tabla4.getValor("IDE_CATASTRO"));
                String totalOsario = cementerioM.restaTotalOsario(tab_tabla1.getValor("IDE_CATASTRO"));
                cementerioM.set_updateTotalOsario(tab_tabla1.getValor("IDE_CATASTRO"), totalOsario);
                cementerioM.set_updateTotalLugar(tab_tabla4.getValor("IDE_CATASTRO"), totalNichoComun);
            }
        } else {
            utilitario.agregarMensajeError("Traslado se encuentra generada", "No se permite hacer cambios");
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
        System.out.println("setRegistros.getValorSeleccionado()<<<<<<" + setRegistros.getValorSeleccionado());
        TablaGenerica tabDato = cementerioM.consultaCatastroNichoComun(Integer.parseInt(setRegistros.getValorSeleccionado()));
        if (!tabDato.isEmpty()) {
            tab_tabla4.setValor("IDE_CATASTRO", tabDato.getValor("IDE_CATASTRO"));
            setRegistros.cerrar();
            utilitario.addUpdate("tab_tabla4");
        } else {
            utilitario.agregarMensajeError("Datos", "No Se Encuentra Registrado");
        }
    }

    public void consultaFallecido() {
        limpiar();
        System.out.println("setSolicitud.getValorSeleccionado()<<" + setSolicitud.getValorSeleccionado());
        aut_busca.setValor(setSolicitud.getValorSeleccionado());
        dibujarPantalla();
        setSolicitud.cerrar();
//        datosRepresentante();

    }

    public void datosRepresentante() {
        tab_tabla1.insertar();
        tab_tabla2.insertar();
        tab_tabla4.insertar();
        System.out.println("tab_tabla1.getValor(\"ide_fallecido\")<<<<<<<<<<<" + tab_tabla1.getValor("ide_fallecido"));

        TablaGenerica tabDato = cementerioM.consultaFallecidOsario(Integer.parseInt(setSolicitud.getValorSeleccionado()));

        if (!tabDato.isEmpty()) {
            tab_tabla1.setValor("IDE_FALLECIDO ", tabDato.getValor("IDE_FALLECIDO"));
            tab_tabla1.setValor("CEDULA_FALLECIDO ", tabDato.getValor("CEDULA_FALLECIDO"));
            tab_tabla1.setValor("NOMBRES", tabDato.getValor("NOMBRES"));
            tab_tabla1.setValor("FECHA_NACIMIENTO", tabDato.getValor("FECHA_NACIMIENTO"));
            tab_tabla1.setValor("FECHA_DEFUNCION", tabDato.getValor("FECHA_DEFUNCION"));
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

    public void buscaRegistro() {
        setRegistros.dibujar();
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
//    private static ClassCiudadania busquedaCedulaActual(java.lang.String cedula, java.lang.String usuario, java.lang.String password) {
//        paq_webservice.ConsultaCiudadano service = new paq_webservice.ConsultaCiudadano();
//        paq_webservice.ConsultaCiudadanoSoap port = service.getConsultaCiudadanoSoap();
//        return port.busquedaCedulaActual(cedula, usuario, password);
//    }
}