/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.swing.text.Style;
import org.primefaces.event.SelectEvent;
import paq_nomina.ejb.SolicAnticipos;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_aprobacion_anticipos extends Pantalla {

    //PARA ASIGNACION DE MES
    String selec_mes = new String();
    private Tabla tab_anticipo = new Tabla();
    private Tabla tab_listado = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla set_pagos = new Tabla();
    private Tabla set_pagos1 = new Tabla();
    private SeleccionTabla set_lista = new SeleccionTabla();
    //Conexion a base
    private Conexion con_postgres = new Conexion();
    //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();
    private Panel pan_opcion2 = new Panel();
    private Texto txt_num_listado = new Texto();
    private Texto tnum_doc = new Texto();
    private Texto tvalor = new Texto();
    private Texto tcedula = new Texto();
    private Texto tnombre = new Texto();
    private Texto tsaldo = new Texto();
    private Texto etiCedula = new Texto();
    private Texto comentario = new Texto();
    private Etiqueta eti = new Etiqueta("Cédula : ");
    private Calendario cal_fecha = new Calendario();
    private Calendario cal_fechad = new Calendario();
    private Combo combo_tipo = new Combo();
    //Dialogo Busca 
    private Dialogo dia_dialogo = new Dialogo();
    private Dialogo dia_dialogop = new Dialogo();
    private Grid grid_d = new Grid();
    private Grid grid_p = new Grid();
    private Grid grid = new Grid();
    private Grid gridp = new Grid();
    @EJB
    private SolicAnticipos iAnticipos = (SolicAnticipos) utilitario.instanciarEJB(SolicAnticipos.class);
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    public pre_aprobacion_anticipos() {
        //Mostrar el usuario 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();

        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_talento.png");
        agregarComponente(quinde);

        Boton bot_limpiar = new Boton();
        bot_limpiar.setValue("PAGOS ADICIONALES");
        bot_limpiar.setExcluirLectura(true);
        bot_limpiar.setIcon("ui-icon-contact");
        bot_limpiar.setMetodo("pago_anticipado");
        bar_botones.agregarBoton(bot_limpiar);

        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("SOLICITUD DE ANTICIPOS DE SUELDOS INGRESADOS PARA APROBAR");
        agregarComponente(pan_opcion);

        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        combo_tipo.setId("combo_tipo");
        combo_tipo.setConexion(con_postgres);
        combo_tipo.setCombo("select abreviatura,abono from srh_tipo_abono ORDER BY abono");

        tab_anticipo.setId("tab_anticipo");
        tab_anticipo.setConexion(con_postgres);
        tab_anticipo.setSql("SELECT s.ide_solicitud_anticipo, \n"
                + "c.fecha_anticipo, \n"
                + "s.ide_empleado_solicitante, \n"
                + "s.ci_solicitante, \n"
                + "s.solicitante, \n"
                + "c.valor_anticipo, \n"
                + "c.numero_cuotas_anticipo, \n"
                + "c.valor_cuota_mensual, \n"
                + "c.val_cuo_adi, \n"
                + "p.mes AS mes_descuento, \n"
                + "s.aprobado_solicitante, \n"
                + "s.id_distributivo, \n"
                + "c.ide_periodo_anticipo_inicial, \n"
                + "c.ide_periodo_anticipo_final \n"
                + "FROM srh_solicitud_anticipo AS s    \n"
                + "INNER JOIN srh_calculo_anticipo AS c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo     \n"
                + "INNER JOIN srh_periodo_anticipo AS p ON p.ide_periodo_anticipo = c.ide_periodo_anticipo_inicial \n"
                + "WHERE c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'INGRESADO') \n"
                + "and s.ide_tipo_ingreso_anticipo is null\n"
                + "order by s.ide_solicitud_anticipo");
        tab_anticipo.setCampoPrimaria("ide_solicitud_anticipo");
        tab_anticipo.setCampoOrden("ide_solicitud_anticipo");
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "Aprobar"
        };
        Object fila2[] = {
            "0", "Negar"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tab_anticipo.getColumna("aprobado_solicitante").setRadio(lista, "");
        tab_anticipo.getColumna("id_distributivo").setVisible(false);
        tab_anticipo.getColumna("ide_periodo_anticipo_inicial").setVisible(false);
        tab_anticipo.getColumna("ide_periodo_anticipo_final").setVisible(false);
        tab_anticipo.getGrid().setColumns(4);
        tab_anticipo.setRows(8);
        tab_anticipo.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_anticipo);

        tab_listado.setId("tab_listado");
        tab_listado.setConexion(con_postgres);
        tab_listado.setSql("SELECT s.ide_solicitud_anticipo,  \n"
                + "s.ide_empleado_solicitante,  \n"
                + "s.ci_solicitante,  \n"
                + "s.solicitante,  \n"
                + "c.valor_anticipo,  \n"
                + "c.numero_cuotas_anticipo,  \n"
                + "c.valor_cuota_mensual,  \n"
                + "c.val_cuo_adi,  \n"
                + "p.mes AS mes_descuento,  \n"
                + "s.aprobado_solicitante,  \n"
                + "null as quitar_lista,  \n"
                + "s.id_distributivo,  \n"
                + "c.ide_periodo_anticipo_inicial,  \n"
                + "c.ide_periodo_anticipo_final  \n"
                + "FROM srh_solicitud_anticipo AS s    \n"
                + "INNER JOIN srh_calculo_anticipo AS c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo     \n"
                + "INNER JOIN srh_periodo_anticipo AS p ON p.ide_periodo_anticipo = c.ide_periodo_anticipo_inicial  \n"
                + "WHERE c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'APROBADO') and s.ide_listado is null\n"
                + "and s.ide_tipo_ingreso_anticipo is null\n"
                + "order by s.ide_solicitud_anticipo");
        tab_listado.setCampoPrimaria("ide_solicitud_anticipo");
        tab_listado.setCampoOrden("ide_solicitud_anticipo");
        tab_listado.getColumna("id_distributivo").setVisible(false);
        tab_listado.getColumna("ide_periodo_anticipo_inicial").setVisible(false);
        tab_listado.getColumna("ide_periodo_anticipo_final").setVisible(false);
        tab_listado.getColumna("aprobado_solicitante").setVisible(false);
        List list = new ArrayList();
        Object fila[] = {
            "1", "Devolver"
        };
        list.add(fila);;
        tab_listado.getColumna("quitar_lista").setRadio(list, "");
        tab_listado.getColumna("quitar_lista").setLongitud(1);
        tab_listado.getGrid().setColumns(4);
        tab_listado.setRows(7);
        tab_listado.dibujar();

        PanelTabla pat_lista = new PanelTabla();
        pat_lista.setPanelTabla(tab_listado);
        pan_opcion2.getChildren().add(pat_lista);

        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        pan_opcion.getChildren().add(gru);
        txt_num_listado.setId("txt_num_listado");
        Grid gri_busca = new Grid();
        gri_busca.setColumns(6);
        gri_busca.getChildren().add(new Etiqueta("# Listado: "));
        gri_busca.getChildren().add(txt_num_listado);

        Boton bot_save = new Boton();
        bot_save.setValue("Guardar Listado");
        bot_save.setExcluirLectura(true);
        bot_save.setIcon("ui-icon-disk");
        bot_save.setMetodo("actuPerAnio");

        Boton bot_delete = new Boton();
        bot_delete.setValue("Quitar de Listado");
        bot_delete.setExcluirLectura(true);
        bot_delete.setIcon("ui-icon-extlink");
        bot_delete.setMetodo("devolver");

        gri_busca.getChildren().add(bot_save);
        gri_busca.getChildren().add(bot_delete);
        agregarComponente(gri_busca);

        pan_opcion2.setId("pan_opcion2");
        pan_opcion2.setTransient(true);
        pan_opcion2.setHeader("LISTADO DE SOLICITUD DE ANTICIPOS APROBADOS");
        agregarComponente(pan_opcion2);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(pan_opcion, gri_busca, pan_opcion2, "44%", "50%", "H");
        agregarComponente(div_division);

        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        ////Para reportes

        Grupo gru_lis = new Grupo();
        gru_lis.getChildren().add(new Etiqueta("FECHA: "));
        gru_lis.getChildren().add(cal_fecha);
        Boton bot_lista = new Boton();
        bot_lista.setValue("Buscar");
        bot_lista.setIcon("ui-icon-search");
        bot_lista.setMetodo("buscarColumna");
        bar_botones.agregarBoton(bot_lista);
        gru_lis.getChildren().add(bot_lista);

        set_lista.setId("set_lista");
        set_lista.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_lista.setSeleccionTabla("SELECT ide_solicitud_anticipo,ide_listado FROM srh_solicitud_anticipo WHERE ide_solicitud_anticipo=-1", "ide_solicitud_anticipo");
        set_lista.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_lista.getTab_seleccion().setRows(10);
        set_lista.setRadio();
        set_lista.setWidth("20%");
        set_lista.setHeight("40%");
        set_lista.getGri_cuerpo().setHeader(gru_lis);
        set_lista.getBot_aceptar().setMetodo("aceptoAprobacion");
        set_lista.setHeader("SELECCIONE LISTADO");
        agregarComponente(set_lista);

        //para poder busca por apelllido el garante
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("SOLICITUD DE ANTICIPO"); //titulo
        dia_dialogo.setWidth("55%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("50%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getBot_aceptar().setMetodo("datos_anticipo");
        grid_d.setColumns(4);
        agregarComponente(dia_dialogo);

        dia_dialogop.setId("dia_dialogop");
        dia_dialogop.setTitle("PROCESO DE SOLICITUD DE ANTICIPO"); //titulo
        dia_dialogop.setWidth("55%"); //siempre en porcentajes  ancho
        dia_dialogop.setHeight("40%");//siempre porcentaje   alto
        dia_dialogop.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogop.getBot_aceptar().setMetodo("pagoAnti");
        grid_p.setColumns(4);
        agregarComponente(dia_dialogop);

        set_pagos.setId("set_pagos");
        set_pagos.setConexion(con_postgres);
        set_pagos.setHeader("DIRECCIONES");
        set_pagos.setSql("SELECT s.ide_solicitud_anticipo,s.ci_solicitante,s.solicitante,\n"
                + "(case when valor_pagado is null then valor_anticipo when valor_pagado is not null then (valor_anticipo-(valor_pagado+(case when valor_abono is null then 0 else valor_abono end))) end)as saldo\n"
                + "FROM srh_solicitud_anticipo s \n"
                + "INNER JOIN srh_calculo_anticipo c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "left join srh_abono_anticipo a on a.ide_solicitud = s.ide_solicitud_anticipo\n"
                + "where c.ide_estado_anticipo in (2,3)  order by s.ide_solicitud_anticipo");
        set_pagos.getColumna("ci_solicitante").setFiltro(true);
        set_pagos.getColumna("solicitante").setFiltro(true);
        set_pagos.getColumna("solicitante").setLongitud(60);
        set_pagos.setTipoSeleccion(false);
        set_pagos.setRows(13);
        set_pagos.dibujar();

        set_pagos1.setId("set_pagos1");
        set_pagos1.setConexion(con_postgres);
        set_pagos1.setHeader("DIRECCIONES");
        set_pagos1.setSql("SELECT s.ide_solicitud_anticipo,s.ci_solicitante,s.solicitante,(case when valor_pagado is null then valor_anticipo when valor_pagado is not null then (valor_anticipo-valor_pagado) end)as saldo\n"
                + "FROM srh_solicitud_anticipo s INNER JOIN srh_calculo_anticipo c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "where c.ide_estado_anticipo in (2,3)  order by s.ide_solicitud_anticipo");
        set_pagos1.getColumna("ci_solicitante").setFiltro(true);
        set_pagos1.getColumna("solicitante").setFiltro(true);
        set_pagos1.setTipoSeleccion(false);
        set_pagos1.setRows(10);
        set_pagos1.dibujar();
    }

    public void buscarColumna() {
        if (cal_fecha.getValue() != null && cal_fecha.getValue().toString().isEmpty() == false) {
            set_lista.getTab_seleccion().setSql("SELECT DISTINCT on (ide_listado ) ide_solicitud_anticipo,ide_listado FROM srh_solicitud_anticipo where fecha_listado='" + cal_fecha.getFecha() + "' ORDER BY ide_listado ");
            set_lista.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una fecha", "");
        }
    }

    public void devolver() {
        for (int i = 0; i < tab_listado.getTotalFilas(); i++) {
            tab_listado.getValor(i, "quitar_lista");
            tab_listado.getValor(i, "ide_solicitud_anticipo");
            if (tab_listado.getValor(i, "quitar_lista") != null) {
                iAnticipos.deleteDevolver(Integer.parseInt(tab_listado.getValor(i, "ide_solicitud_anticipo")));
                iAnticipos.actuaDevolucion(Integer.parseInt(tab_listado.getValor(i, "ide_solicitud_anticipo")));
                iAnticipos.actuaSoliDevolucion(Integer.parseInt(tab_listado.getValor(i, "ide_solicitud_anticipo")));
            }
        }
        tab_anticipo.actualizar();
        utilitario.agregarMensaje("Formularios Devueltos", "");
        tab_listado.actualizar();
    }

    public void pago_anticipado() {
        dia_dialogo.Limpiar();
        dia_dialogo.setDialogo(grid);
        grid_d.getChildren().add(set_pagos);
        dia_dialogo.setDialogo(grid_d);
        set_pagos.dibujar();
        dia_dialogo.dibujar();
    }

    //PAGO ANTICIPADO, LIQUIDACION O ABONO
    public void datos_anticipo() {
        if (set_pagos.getValorSeleccionado() != null && set_pagos.getValorSeleccionado().toString().isEmpty() == false) {
            TablaGenerica tab_dato = iAnticipos.getSolicitud(Integer.parseInt(set_pagos.getValorSeleccionado()));
            if (!tab_dato.isEmpty()) {
                dia_dialogop.Limpiar();
                dia_dialogop.setDialogo(gridp);
                Grid gru_lis = new Grid();
                gru_lis.setColumns(6);
                tcedula.setSize(10);
//                etiCedula.setSize(5);
                gru_lis.getChildren().add(eti);
                gru_lis.getChildren().add(tcedula);
                tnombre.setSize(38);
                gru_lis.getChildren().add(new Etiqueta("NOMBRES :"));
                gru_lis.getChildren().add(tnombre);
                tsaldo.setSize(8);
                gru_lis.getChildren().add(new Etiqueta("SALDO $ :"));
                gru_lis.getChildren().add(tsaldo);
                gridp.getChildren().add(gru_lis);
                Grid griMostrar = new Grid();
                griMostrar.setColumns(2);
                griMostrar.getChildren().add(new Etiqueta("TIPO :"));
                griMostrar.getChildren().add(combo_tipo);
                griMostrar.getChildren().add(new Etiqueta("FECHA DE DEPOSITO :"));
                griMostrar.getChildren().add(cal_fechad);
                tnum_doc.setSize(10);
                griMostrar.getChildren().add(new Etiqueta("# DE DOCUMENTO :"));
                griMostrar.getChildren().add(tnum_doc);
                tvalor.setSize(5);
                griMostrar.getChildren().add(new Etiqueta("VALOR $ :"));
                griMostrar.getChildren().add(tvalor);
                comentario.setSize(80);
                griMostrar.getChildren().add(new Etiqueta("OBSERVACIÓN :"));
                griMostrar.getChildren().add(comentario);
                gridp.getChildren().add(griMostrar);
                dia_dialogop.setDialogo(grid_p);
//                set_pagos.dibujar();
                dia_dialogop.dibujar();
                tcedula.setValue(tab_dato.getValor("ci_solicitante") + "");
                tnombre.setValue(tab_dato.getValor("solicitante") + "");
                tsaldo.setValue(tab_dato.getValor("saldo") + "");
                tnum_doc.setValue(0);
                utilitario.addUpdate("etiCedula");
                utilitario.addUpdate("tcedula");
                utilitario.addUpdate("tnombre");
                utilitario.addUpdate("tsaldo");
                utilitario.addUpdate("tnum_doc");
            } else {
                utilitario.agregarMensajeInfo("No se encuentra registro", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una registro", "");
        }
    }

    public void pagoAnti() {
        Integer codigo = 0;
        codigo = Integer.parseInt(set_pagos.getValorSeleccionado());
        TablaGenerica tab_dato = iAnticipos.getSoli_Detalle(tcedula.getValue() + "");
        if (!tab_dato.isEmpty()) {
            Double valors = 0.0, valorp = 0.0;
            if (combo_tipo.getValue().equals("AB")) {//abono para anticipo
                TablaGenerica tabCalculo = iAnticipos.getRecalculo(codigo, Double.parseDouble(tvalor.getValue() + ""));
                if (!tabCalculo.isEmpty()) {
                    if (Integer.parseInt(tabCalculo.getValor("cuotas")) > 1) {
                        TablaGenerica tabFilas = iAnticipos.getFilas(codigo);
                        if (!tabFilas.isEmpty()) {
                            for (int i = 0; i < tabFilas.getTotalFilas(); i++) {
                                iAnticipos.setCuotaNueva(Integer.parseInt(tabFilas.getValor(i, "ide_detalle_anticipo")), Double.parseDouble(tabCalculo.getValor("cuota")));
                            }
                            iAnticipos.setAbonoAnticipos(codigo, combo_tipo.getValue() + "", cal_fechad.getFecha(),
                                    Double.parseDouble(tvalor.getValue() + ""), Integer.parseInt(tnum_doc.getValue() + ""), comentario.getValue() + "", utilitario.getVariable("NICK"));
                            utilitario.agregarMensaje("Abono Registrado", null);
                        }
                    } else {
                        utilitario.agregarMensaje("Posee unica cuota pendiente", null);
                    }
                }
            } else if (combo_tipo.getValue().equals("CT")) {//cobro anticipado
                valors = Double.parseDouble(tvalor.getValue() + "");
                valorp = Double.parseDouble(tab_dato.getValor("saldo") + "");
                if (valors.equals(valorp)) {
                    iAnticipos.setActDetallePagoAnti(Integer.parseInt(tab_dato.getValor("ide_solicitud_anticipo")), utilitario.getMes(utilitario.getFechaActual()) + "", utilitario.getAnio(utilitario.getFechaActual()) + "", combo_tipo.getValue() + "");
//                    iAnticipos.set_ActDetalle_PagoAnti(Integer.parseInt(tab_dato.getValor("ide_solicitud_anticipo")), utilitario.getMes(utilitario.getFechaActual()) + "", utilitario.getAnio(utilitario.getFechaActual()) + "", utilitario.getVariable("NICK"), cal_fechad.getFecha());
                    iAnticipos.setActCalculoPagoAnti(Integer.parseInt(tab_dato.getValor("ide_solicitud_anticipo")), Integer.parseInt(tab_dato.getValor("ide_calculo_anticipo")), valors, combo_tipo.getValue() + "");
//                    iAnticipos.set_ActCalculo_PagoAnti(Integer.parseInt(tab_dato.getValor("ide_solicitud_anticipo")), Integer.parseInt(tab_dato.getValor("ide_calculo_anticipo")), utilitario.getVariable("NICK"), cal_fechad.getFecha(), tnum_doc.getValue() + "", valors);
                    iAnticipos.set_ActSolicitud_PagoAnti(Integer.parseInt(tab_dato.getValor("ide_solicitud_anticipo")));
                    iAnticipos.setAbonoAnticipos(codigo, combo_tipo.getValue() + "", cal_fechad.getFecha(),
                            Double.parseDouble(tvalor.getValue() + ""), Integer.parseInt(tnum_doc.getValue() + ""), comentario.getValue() + "",
                            utilitario.getVariable("NICK"));
                    utilitario.agregarMensajeInfo("Pago Completo Realizado con exito", "");
                } else {
                    utilitario.agregarMensajeInfo("Opción para Pago Total", "");
                }
            } else if (combo_tipo.getValue().equals("LQ")) {//cobro por liquidacion
                iAnticipos.setActDetallePagoliq(Integer.parseInt(tab_dato.getValor("ide_solicitud_anticipo")), Integer.parseInt(utilitario.getMes(utilitario.getFechaActual()) + ""), combo_tipo.getValue() + "");
//                iAnticipos.set_ActDetalle_Pagoliq(Integer.parseInt(tab_dato.getValor("ide_solicitud_anticipo")), Integer.parseInt(utilitario.getMes(utilitario.getFechaActual()) + ""), utilitario.getVariable("NICK"), utilitario.getFechaActual());
                iAnticipos.setActCalculoPagoliq(Integer.parseInt(tab_dato.getValor("ide_solicitud_anticipo")), Integer.parseInt(tab_dato.getValor("ide_calculo_anticipo")), combo_tipo.getValue() + "");
//                iAnticipos.set_ActCalculo_Pagoliq(Integer.parseInt(tab_dato.getValor("ide_solicitud_anticipo")), Integer.parseInt(tab_dato.getValor("ide_calculo_anticipo")), utilitario.getVariable("NICK"), utilitario.getFechaActual(), cvalor.getValue() + "");
                iAnticipos.set_ActSolicitud_PagoAnti(Integer.parseInt(tab_dato.getValor("ide_solicitud_anticipo")));

                iAnticipos.setAbonoAnticipos(codigo, combo_tipo.getValue() + "", cal_fechad.getFecha(),
                        Double.parseDouble(tvalor.getValue() + ""), Integer.parseInt(tnum_doc.getValue() + ""), comentario.getValue() + "",
                        utilitario.getVariable("NICK"));
                utilitario.agregarMensajeInfo("Registro realizado con exito", "");
            } else {
                utilitario.agregarMensaje("Selecionar Opción", null);
            }
        } else {
            utilitario.agregarMensajeInfo("No se encuentra registro", "");
        }
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        requisito();
    }

    @Override
    public void eliminar() {
    }

    /* FUNCION QUE PERMITE RECORRER LA TABLA RECUPERANDO EVENTOS ACTUALES     */
    public void seleccionar_tabla1(SelectEvent evt) {
        tab_anticipo.seleccionarFila(evt);
    }

    public void requisito() {
        for (int i = 0; i < tab_anticipo.getTotalFilas(); i++) {
            if (tab_anticipo.getValor(i, "aprobado_solicitante") != null) {
                if (tab_anticipo.getValor(i, "aprobado_solicitante").equals("1")) {
                    if (tab_anticipo.getValor(i, "id_distributivo").equals("1")) {//detalle solicitud de empleados
                        if ((utilitario.getDia(tab_anticipo.getValor(i, "fecha_anticipo"))) > 10) {
                            if ((Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo")) + (utilitario.getMes(tab_anticipo.getValor(i, "fecha_anticipo")))) > 12) {
                                for (int j = 0; j < (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo")) - 1); j++) {
                                    TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_anticipo.getValor(i, "ide_periodo_anticipo_inicial")) + j);
                                    if (!tab_dato.isEmpty()) {
                                        if (tab_dato.getValor("mes").equals("Diciembre")) {
                                            iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), String.valueOf(j + 1), Double.parseDouble(tab_anticipo.getValor(i, "val_cuo_adi")),
                                                    Integer.parseInt(tab_anticipo.getValor(i, "ide_periodo_anticipo_inicial")) + j);

                                        } else {
                                            iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), String.valueOf(j + 1), Double.parseDouble(tab_anticipo.getValor(i, "valor_cuota_mensual")),
                                                    Integer.parseInt(tab_anticipo.getValor(i, "ide_periodo_anticipo_inicial")) + j);
                                        }
                                    } else {
                                        utilitario.agregarMensajeInfo("No se encuentra en roles", "");
                                    }
                                }
                                Double valorp = 0.0, valors = 0.0, totall = 0.0;
                                TablaGenerica tab_dato = iAnticipos.get_cuotadic(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")));
                                if (!tab_dato.isEmpty()) {
                                    if (tab_dato.getValor("val_cuo_adi") != null) {
                                        valorp = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo")) - 2) * Double.parseDouble(tab_anticipo.getValor(i, "valor_cuota_mensual"));
                                        valors = Double.parseDouble(tab_anticipo.getValor(i, "val_cuo_adi")) + valorp;
                                    } else {
                                        valorp = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo")) - 1) * Double.parseDouble(tab_anticipo.getValor(i, "valor_cuota_mensual"));
                                        valors = valorp;
                                    }
                                }
//                                           valorp = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-2)*Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual"));
//                                           valors= Double.parseDouble(tab_anticipo.getValor(i,"val_cuo_adi"))+valorp ;
                                totall = Double.parseDouble(tab_anticipo.getValor(i, "valor_anticipo")) - valors;
                                iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), tab_anticipo.getValor(i, "numero_cuotas_anticipo"), Double.parseDouble(String.valueOf(totall)),
                                        Integer.parseInt(tab_anticipo.getValor(i, "ide_periodo_anticipo_final")));

                            } else {
                                for (int j = 0; j < (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo")) - 1); j++) {
                                    iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), String.valueOf(1 + j), Double.parseDouble(tab_anticipo.getValor(i, "valor_cuota_mensual")),
                                            Integer.parseInt(tab_anticipo.getValor(i, "ide_periodo_anticipo_inicial")) + j);
                                }
                                Double valor1 = 0.0, total = 0.0;
                                valor1 = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo")) - 1) * Double.parseDouble(tab_anticipo.getValor(i, "valor_cuota_mensual"));
                                total = Double.parseDouble(tab_anticipo.getValor(i, "valor_anticipo")) - valor1;
                                iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), tab_anticipo.getValor(i, "numero_cuotas_anticipo"), total,
                                        Integer.parseInt(tab_anticipo.getValor(i, "ide_periodo_anticipo_final")));
                            }//
                        } else {
                            if (Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo")) > 1) {
                                if ((Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo")) + (utilitario.getMes(tab_anticipo.getValor(i, "fecha_anticipo")))) - 1 > 12) {
                                    for (int j = 0; j < (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo")) - 1); j++) {
                                        TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_anticipo.getValor(i, "ide_periodo_anticipo_inicial")) + j);
                                        if (!tab_dato.isEmpty()) {
                                            if (tab_dato.getValor("mes").equals("Diciembre")) {
                                                iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), String.valueOf(j + 1), Double.parseDouble(tab_anticipo.getValor(i, "val_cuo_adi")),
                                                        Integer.parseInt(tab_anticipo.getValor(i, "ide_periodo_anticipo_inicial")) + j);

                                            } else {
                                                iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), String.valueOf(j + 1), Double.parseDouble(tab_anticipo.getValor(i, "valor_cuota_mensual")),
                                                        Integer.parseInt(tab_anticipo.getValor(i, "ide_periodo_anticipo_inicial")) + j);
                                            }
                                        } else {
                                            utilitario.agregarMensajeInfo("No se encuentra en roles", "");
                                        }
                                    }
                                    Double valorp = 0.0, valors = 0.0, totall = 0.0;
                                    valorp = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo")) - 2) * Double.parseDouble(tab_anticipo.getValor(i, "valor_cuota_mensual"));
                                    valors = Double.parseDouble(tab_anticipo.getValor(i, "val_cuo_adi")) + valorp;
                                    totall = Double.parseDouble(tab_anticipo.getValor(i, "valor_anticipo")) - valors;
                                    iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), tab_anticipo.getValor(i, "numero_cuotas_anticipo"), Double.parseDouble(String.valueOf(totall)),
                                            Integer.parseInt(tab_anticipo.getValor(i, "ide_periodo_anticipo_final")));
                                } else {
                                    for (int j = 0; j < (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo")) - 1); j++) {
                                        iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), String.valueOf(1 + j), Double.parseDouble(tab_anticipo.getValor(i, "valor_cuota_mensual")),
                                                Integer.parseInt(tab_anticipo.getValor(i, "ide_periodo_anticipo_inicial")) + j);
                                    }
                                    Double valor1 = 0.0, total = 0.0;
                                    valor1 = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo")) - 1) * Double.parseDouble(tab_anticipo.getValor(i, "valor_cuota_mensual"));
                                    total = Double.parseDouble(tab_anticipo.getValor(i, "valor_anticipo")) - valor1;
                                    iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), tab_anticipo.getValor(i, "numero_cuotas_anticipo"), total,
                                            Integer.parseInt(tab_anticipo.getValor(i, "ide_periodo_anticipo_final")));
                                }
                            } else {
                                iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor("ide_solicitud_anticipo")), String.valueOf(1), Double.parseDouble(tab_anticipo.getValor("valor_anticipo")),
                                        Integer.parseInt(tab_anticipo.getValor("ide_periodo_anticipo_inicial")));
                                iAnticipos.actuaCalculo(Integer.parseInt(tab_anticipo.getValor("ide_solicitud_anticipo")), Double.parseDouble(tab_anticipo.getValor("valor_anticipo")));
                            }
                        }//


                    } else {//detalle para solicitud de trabajadores
                        if (Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo")) > 1) {
                            for (int j = 0; j < (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo")) - 1); j++) {
                                iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), String.valueOf(1 + j), Double.parseDouble(tab_anticipo.getValor(i, "valor_cuota_mensual")),
                                        Integer.parseInt(tab_anticipo.getValor(i, "ide_periodo_anticipo_inicial")) + j);
                            }
                            Double valor1 = 0.0, total = 0.0;
                            valor1 = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo")) - 1) * Double.parseDouble(tab_anticipo.getValor(i, "valor_cuota_mensual"));
                            total = Double.parseDouble(tab_anticipo.getValor(i, "valor_anticipo")) - valor1;
                            iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), tab_anticipo.getValor(i, "numero_cuotas_anticipo"), total,
                                    Integer.parseInt(tab_anticipo.getValor(i, "ide_periodo_anticipo_final")));

                        } else {
                            iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor("ide_solicitud_anticipo")), String.valueOf(1), Double.parseDouble(tab_anticipo.getValor("valor_anticipo")),
                                    Integer.parseInt(tab_anticipo.getValor("ide_periodo_anticipo_inicial")));
                            iAnticipos.actuaCalculo(Integer.parseInt(tab_anticipo.getValor("ide_solicitud_anticipo")), Double.parseDouble(tab_anticipo.getValor("valor_anticipo")));
                        }

                    }
                    iAnticipos.actuaSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "id_solicitud")), tab_anticipo.getValor(i, "ced_empleado"), 4, utilitario.getVariable("NICK"));
                    iAnticipos.actualizSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "id_solicitud")), tab_anticipo.getValor(i, "ced_empleado"));//actualiza cuota de cobro
                } else if (tab_anticipo.getValor(i, "aprobado_solicitante").equals("0")) {//Solicitud Denegada
                    iAnticipos.negarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), tab_anticipo.getValor(i, "ci_solicitante"));
                }
            }
        }
        tab_anticipo.actualizar();
        utilitario.agregarMensaje("Formularios Aprobados", "");
        txt_num_listado.limpiar();
        utilitario.addUpdate("txt_num_listado");
        tab_listado.actualizar();
    }

    public void actuPerAnio() {
        for (int i = 0; i < tab_listado.getTotalFilas(); i++) {
            tab_listado.getValor(i, "ide_solicitud_anticipo");
            iAnticipos.actuaPerAnio15(Integer.parseInt(tab_listado.getValor(i, "ide_solicitud_anticipo")));
        }
        save_lista();
    }

    public void save_lista() {
        txt_num_listado.setDisabled(true); //Desactiva el cuadro de texto
        String numero = iAnticipos.listaMax();
        String valor, anio, num;
        Integer cantidad = 0;
        anio = String.valueOf(utilitario.getAnio(utilitario.getFechaActual()));
        valor = numero.substring(10, 15);
        cantidad = Integer.parseInt(valor) + 1;
        if (numero != null) {
            if (cantidad >= 0 && cantidad <= 9) {
                num = "0000" + String.valueOf(cantidad);
                String cadena = "LIST" + "-" + anio + "-" + num;
                txt_num_listado.setValue(cadena + "");
                utilitario.addUpdate("txt_num_listado");
            } else if (cantidad >= 10 && cantidad <= 99) {
                num = "000" + String.valueOf(cantidad);
                String cadena = "LIST" + "-" + anio + "-" + num;
                txt_num_listado.setValue(cadena + "");
                utilitario.addUpdate("txt_num_listado");
            } else if (cantidad >= 100 && cantidad <= 999) {
                num = "00" + String.valueOf(cantidad);
                String cadena = "LIST" + "-" + anio + "-" + num;
                txt_num_listado.setValue(cadena + "");
                utilitario.addUpdate("txt_num_listado");
            } else if (cantidad >= 1000 && cantidad <= 9999) {
                num = "0" + String.valueOf(cantidad);
                String cadena = "LIST" + "-" + anio + "-" + num;
                txt_num_listado.setValue(cadena + "");
                utilitario.addUpdate("txt_num_listado");
            } else if (cantidad >= 10000 && cantidad <= 99999) {
                num = String.valueOf(cantidad);
                String cadena = "LIST" + "-" + anio + "-" + num;
                txt_num_listado.setValue(cadena + "");
                utilitario.addUpdate("txt_num_listado");
            }
        }
        save_listado();
    }

    public void save_listado() {
        for (int i = 0; i < tab_listado.getTotalFilas(); i++) {
            tab_listado.getValor(i, "ide_solicitud_anticipo");
            tab_listado.getValor(i, "ide_empleado_solicitante");
            tab_listado.getValor(i, "ci_solicitante");

            iAnticipos.llenarListado(Integer.parseInt(tab_listado.getValor(i, "ide_solicitud_anticipo")), Integer.parseInt(tab_listado.getValor(i, "ide_empleado_solicitante")), tab_listado.getValor(i, "ci_solicitante"), txt_num_listado.getValue() + "");
        }
        utilitario.agregarMensaje("Listado Guardado", "");
        utilitario.addUpdate("txt_num_listado");
    }

    /*CREACION DE REPORTES */
    //llamada a reporte
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        cal_fecha.setFechaActual();
        switch (rep_reporte.getNombre()) {
            case "LISTA DE PAGO":
                set_lista.dibujar();
                set_lista.getTab_seleccion().limpiar();
                break;
        }
    }

    public void aceptoAprobacion() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "LISTA DE PAGO":
                TablaGenerica tab_datoo = iAnticipos.ide_listado(Integer.parseInt(set_lista.getValorSeleccionado()));
                if (!tab_datoo.isEmpty()) {
                    TablaGenerica tab_dato1 = iAnticipos.login_solicitud(tab_datoo.getValor("ide_listado"));
                    if (!tab_dato1.isEmpty()) {
                        TablaGenerica tab_dato2 = iAnticipos.director();
                        if (!tab_dato2.isEmpty()) {
                            TablaGenerica tab_dato3 = iAnticipos.getUsuario(tab_dato1.getValor("login_ingre_solicitud"));
                            if (!tab_dato3.isEmpty()) {
                                TablaGenerica tab_dato4 = iAnticipos.getUsuario(tab_dato1.getValor("login_aprob_solicitud"));
                                if (!tab_dato4.isEmpty()) {
                                    TablaGenerica tab_dato5 = iAnticipos.cargoSolic(tab_dato3.getValor("NOM_USUA"));
                                    if (!tab_dato5.isEmpty()) {
                                        TablaGenerica tab_dato6 = iAnticipos.cargoSolic(tab_dato4.getValor("NOM_USUA"));
                                        if (!tab_dato6.isEmpty()) {
                                            p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA") + "");
                                            p_parametros.put("listado", tab_datoo.getValor("ide_listado") + "");
                                            p_parametros.put("quien_elabora", tab_dato3.getValor("NOM_USUA") + "");
                                            if (tab_dato5.getValor("nombre_cargo").equals("TECNICO DE PRESUPUESTO")) {
                                                p_parametros.put("cargoi", "REMUNERACIONES");
                                                p_parametros.put("cargoa", "REMUNERACIONES");
                                            } else {
                                                p_parametros.put("cargoi", tab_dato5.getValor("nombre_cargo") + "");
                                                p_parametros.put("cargoa", tab_dato6.getValor("nombre_cargo") + "");
                                            }
                                            p_parametros.put("quien_aprueba", tab_dato4.getValor("NOM_USUA") + "");
                                            p_parametros.put("director", tab_dato2.getValor("nombres") + "");
//                                            p_parametros.put("cargo", tab_dato2.getValor("nombre_cargo") + "");
                                            p_parametros.put("cargo", "Funcionario Directivo de Talento Humano ");
                                            rep_reporte.cerrar();
                                            sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                                            System.err.println(p_parametros);
                                            System.err.println(rep_reporte.getPath());
                                            sef_formato.dibujar();
                                        } else {//
                                            utilitario.agregarMensaje("No se encuentra cargo ", "");
                                        }
                                    } else {//
                                        utilitario.agregarMensaje("No se a encuentra cargo registro ", "");
                                    }
                                } else {//
                                    utilitario.agregarMensaje("Quien aprueba no existe ", "");
                                }
                            } else {
                                utilitario.agregarMensaje("Quien ingresa no existe ", "");
                            }
                        } else {
                            utilitario.agregarMensaje("No se dispone de director ", "");
                        }
                    } else {
                        utilitario.agregarMensaje("Login de ingreso No disponible ", "");
                    }
                } else {
                    utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                }
                break;
        }
    }
    //BUSQUEDA DE MES PARA LA AISGNACION DEL PERIODO

    public String meses(Integer numero) {
        switch (numero) {
            case 12:
                selec_mes = "Diciembre";
                break;
            case 11:
                selec_mes = "Noviembre";
                break;
            case 10:
                selec_mes = "Octubre";
                break;
            case 9:
                selec_mes = "Septiembre";
                break;
            case 8:
                selec_mes = "Agosto";
                break;
            case 7:
                selec_mes = "Julio";
                break;
            case 6:
                selec_mes = "Junio";
                break;
            case 5:
                selec_mes = "Mayo";
                break;
            case 4:
                selec_mes = "Abril";
                break;
            case 3:
                selec_mes = "Marzo";
                break;
            case 2:
                selec_mes = "Febrero";
                break;
            case 1:
                selec_mes = "Enero";
                break;
        }
        return selec_mes;
    }

    public void abono_anticipo() {
    }

    public Tabla getTab_anticipo() {
        return tab_anticipo;
    }

    public void setTab_anticipo(Tabla tab_anticipo) {
        this.tab_anticipo = tab_anticipo;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_listado() {
        return tab_listado;
    }

    public void setTab_listado(Tabla tab_listado) {
        this.tab_listado = tab_listado;
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

    public SeleccionTabla getSet_lista() {
        return set_lista;
    }

    public void setSet_lista(SeleccionTabla set_lista) {
        this.set_lista = set_lista;
    }

    public Tabla getSet_pagos() {
        return set_pagos;
    }

    public void setSet_pagos(Tabla set_pagos) {
        this.set_pagos = set_pagos;
    }

    public Tabla getSet_pagos1() {
        return set_pagos1;
    }

    public void setSet_pagos1(Tabla set_pagos1) {
        this.set_pagos1 = set_pagos1;
    }
}
