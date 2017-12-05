/**
 *
 * @author l-suntaxi
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
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
import framework.componentes.Tabulador;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_cementerio.ejb.cementerio;
import paq_registros.ejb.ServicioRegistros;

public class pre_movimiento_cementerio extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Tabla tab_tabla4 = new Tabla();
    private Tabla tab_tabla5 = new Tabla();
    private SeleccionTabla setRegistros = new SeleccionTabla();
    private Combo cmbTipo = new Combo();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
    private AutoCompletar aut_busca = new AutoCompletar();
    private SeleccionCalendario sec_rango = new SeleccionCalendario();
    //Contiene todos los elementos de la plantilla
    private Panel panOpcion = new Panel();
    @EJB
    private cementerio cementerioM = (cementerio) utilitario.instanciarEJB(cementerio.class);
    private ServicioRegistros servicioregistros = (ServicioRegistros) utilitario.instanciarEJB(ServicioRegistros.class);

    public pre_movimiento_cementerio() {

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

        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("select ide_fallecido,CEDULA_FALLECIDO,NOMBRES  from cmt_fallecido ");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(70);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        bar_botones.agregarComponente(new Etiqueta("Buscador Personas:"));
        bar_botones.agregarComponente(aut_busca);
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);


        sef_reporte.setId("sef_reporte");
        bar_botones.agregarReporte();

        cmbTipo.setId("cmbTipo");
        cmbTipo.setCombo("SELECT distinct IDE_LUGAR  as id,DETALLE_LUGAR  FROM CMT_LUGAR");

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
        setRegistros.setSeleccionTabla("select top 10 IDE_CATASTRO,SECTOR,NUMERO,MODULO,DISPONIBLE_OCUPADO,DETALLE_LUGAR  from CMT_CATASTRO a, cmt_lugar b\n"
                + "where a.ide_lugar=b.ide_lugar ORDER BY DETALLE_LUGAR", "IDE_CATASTRO");
        setRegistros.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setRegistros.getTab_seleccion().getColumna("DETALLE_LUGAR").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("SECTOR").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("NUMERO").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("MODULO").setFiltro(true);

        setRegistros.getTab_seleccion().setRows(12);
        setRegistros.setWidth("40%");
        setRegistros.setRadio();
        setRegistros.getGri_cuerpo().setHeader(gri_busca);
        setRegistros.getBot_aceptar().setMetodo("consultaCatastro");
        setRegistros.setHeader("BUSCAR CATASTRO CEMENTERIO");
        agregarComponente(setRegistros);


        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("INGRESO DE MOVIMIENTOS AL CEMENTERIO MUNICIPAL");
        agregarComponente(panOpcion);
        dibujarPantalla();


    }

    /*
     * Formulario de ingreso
     */
    public void dibujarPantalla() {
        limpiarPanel();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 1);
        if (aut_busca.getValue() == null) {
            tab_tabla1.setCondicion("IDE_DET_MOVIMIENTO=-1");
        } else {
            tab_tabla1.setCondicion("IDE_DET_MOVIMIENTO=" + aut_busca.getValor());
        }
        tab_tabla1.getColumna("IDE_TIPO_MOVIMIENTO").setCombo("SELECT IDE_CMACC,DETALLE_CMACC FROM CMT_ACCION ");
        tab_tabla1.getColumna("IDE_CATASTRO").setCombo("SELECT IDE_CATASTRO,DETALLE_LUGAR FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR=B.IDE_LUGAR  where DETALLE_LUGAR='SITIO' ORDER BY DETALLE_LUGAR+'-'+SECTOR+convert(varchar,MODULO)+'-'+convert(varchar,NUMERO)");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.agregarRelacion(tab_tabla4);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setMensajeWarn("MOVIMIENTOS CEMENTERIO FALLECIDOS");
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("CMT_fallecido", "IDE_fallecido", 2);
        tab_tabla2.getGrid().setColumns(4);
        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setMensajeWarn("DATOS DE FALLECIDO");
        pat_panel2.setPanelTabla(tab_tabla2);


        tab_tabla4.setId("tab_tabla4");
        tab_tabla4.setTabla("CMT_REPRESENTANTE", "IDE_CMREP", 2);
        tab_tabla4.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setMetodoChange("buscaPersonaDatos");
        tab_tabla4.getColumna("IDE_CMTID").setCombo("select IDE_CMTID,DETALLE_CMTID from CMT_TIPO_DOCUMENTO");
        tab_tabla4.getGrid().setColumns(4);
        tab_tabla4.setTipoFormulario(true);
        tab_tabla4.dibujar();
        PanelTabla pat_panel4 = new PanelTabla();
        pat_panel4.setMensajeWarn("DATOS DE REPRESENTANTE");
        pat_panel4.setPanelTabla(tab_tabla4);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(pat_panel1, pat_panel2, pat_panel4, "38%", "20%", "H");
        agregarComponente(div_division);

        Grupo gru = new Grupo();
        gru.getChildren().add(div_division);
        panOpcion.getChildren().add(gru);
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
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        Map p_parametros = new HashMap();
        if (rep_reporte.getReporteSelecionado().equals("Arrendamiento Cementerio")) {
            if (tab_tabla1.getValorSeleccionado() != null) {
                if (tab_tabla2.getTotalFilas() > 0) {
                    p_parametros.put("titulo", "INFORME PARA ARRENDAMIENTO EN EL CEMENTERIO MUNICIPAL");
                    p_parametros.put("par_para", "JEFE DE RENTAS");
                    p_parametros.put("par_de", "Lic. Nelson Loachamin");
                    p_parametros.put("ide_cmare", Integer.parseInt(tab_tabla1.getValorSeleccionado()));
                    rep_reporte.cerrar();
                    sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_reporte.dibujar();
                } else {
                    utilitario.agregarMensaje("No se a puede generar el reporte", "Falta ingresar los datos del representante");
                }

            } else {
                utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Control de Cementerio")) {
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                sec_rango.dibujar();
            } else if (sec_rango.isVisible()) {
                if (sec_rango.isFechasValidas()) {
                    p_parametros.put("fecha_inicio", sec_rango.getFecha1String());
                    p_parametros.put("fecha_fin", sec_rango.getFecha2String());
                    sec_rango.cerrar();
                    p_parametros.put("titulo", "HOJA DE CONTROL DE INHUMACIONES, EXHUMACIONES,RENOVACIONES Y OTROS CONTROLES DEL CEMENTERIO MUNICIPAL DE SANGOLQUI CANTON RUMIÑAHUI");
                    sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_reporte.dibujar();
                } else {
                    utilitario.agregarNotificacionInfo("Rango de fechas no válidas", "");
                }
            }
        }
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
//        if(tab_tabla1){}
//        if (tab_tabla1.isFocus()) {
        tab_tabla1.guardar();
        tab_tabla2.guardar();
//        cementerioM.ingresaMovimiento(tab_tabla1.getValor("IDE_CATASTRO"), tab_tabla2.getValor("IDE_CMREP"), tab_tabla4.getValor("IDE_TIPO_MOVIMIENTO"), tab_tabla1.getValor("IDE_FALLECIDO"));


//        guardarPantalla();
    }

    @Override
    public void actualizar() {
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
        aut_busca.actualizar();
        aut_busca.setSize(70);
        utilitario.addUpdate("aut_busca");
    }

    public void cambioEstado() {
        tab_tabla4.setValor("FECHA_HORA_ACCION_CMDEA", utilitario.getFechaHoraActual());
    }

    public void consultaCatastro() {
        tab_tabla1.insertar();
        tab_tabla2.insertar();
        tab_tabla4.insertar();

        TablaGenerica tabDato = cementerioM.consultaCatastro(Integer.parseInt(setRegistros.getValorSeleccionado()));
        if (!tabDato.isEmpty()) {
            tab_tabla1.setValor("IDE_CATASTRO ", tabDato.getValor("IDE_CATASTRO"));

            tab_tabla1.setValor("DETALLE_LUGAR ", tabDato.getValor("DETALLE_LUGAR"));
            tab_tabla1.setValor("BLOQUE", tabDato.getValor("SECTOR"));
            tab_tabla1.setValor("NIVEL", tabDato.getValor("NUMERO"));
            tab_tabla1.setValor("MODULO ", tabDato.getValor("MODULO"));
            setRegistros.cerrar();
            utilitario.addUpdate("tab_tabla1");
        } else {
            utilitario.agregarMensajeError("Datos", "No Se Encuentra Registrado");
        }
    }

    //BUSQUEDA DE PERSONAS EN BASE DE DATOS DEL MUNICIPIO
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
            setRegistros.getTab_seleccion().setSql(" select IDE_CATASTRO,SECTOR,NUMERO,MODULO,DISPONIBLE_OCUPADO from CMT_CATASTRO a, cmt_lugar b\n"
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

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public Tabla getTab_tabla4() {
        return tab_tabla4;
    }

    public void setTab_tabla4(Tabla tab_tabla4) {
        this.tab_tabla4 = tab_tabla4;
    }

    public Tabla getTab_tabla5() {
        return tab_tabla5;
    }

    public void setTab_tabla5(Tabla tab_tabla5) {
        this.tab_tabla5 = tab_tabla5;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_reporte() {
        return sef_reporte;
    }

    public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
        this.sef_reporte = sef_reporte;
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
}
