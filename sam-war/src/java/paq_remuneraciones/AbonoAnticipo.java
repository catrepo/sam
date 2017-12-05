/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_remuneraciones;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_remuneraciones.ejb.BeanRemuneracion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class AbonoAnticipo extends Pantalla {

    private Tabla tabAnticipo = new Tabla();
    private Tabla tabAbono = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla setAnticipo = new Tabla();
    private Tabla setRegistro = new Tabla();
    private Tabla setReimpresion = new Tabla();
    private Panel panOpcion = new Panel();
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaDialogop = new Dialogo();
    private Dialogo diaDialogor = new Dialogo();
    private AutoCompletar autBusca = new AutoCompletar();
    private Grid grid = new Grid();
    private Grid gridp = new Grid();
    private Grid gridr = new Grid();
    private Grid gridD = new Grid();
    private Grid gridP = new Grid();
    private Grid gridR = new Grid();
    @EJB
    private BeanRemuneracion adminRemuneracion = (BeanRemuneracion) utilitario.instanciarEJB(BeanRemuneracion.class);
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    String ban;

    public AbonoAnticipo() {
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonsNavegacion();

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT\n"
                + "d.id_pago,\n"
                + "d.fecha_documento,\n"
                + "d.num_pago,\n"
                + "d.id_solicitud,\n"
                + "n.ced_empleado,\n"
                + "n.nom_empleado\n"
                + "FROM nom_docu_pago d\n"
                + "inner join nom_solicitud n on d.id_solicitud = n.id_solicitud");
        autBusca.setSize(80);

        Boton botPago = new Boton();
        botPago.setValue("Generar Pago");
        botPago.setExcluirLectura(true);
        botPago.setIcon("ui-icon-contact");
        botPago.setMetodo("pagoAnticipo");
        bar_botones.agregarBoton(botPago);

        Boton botComprobante = new Boton();
        botComprobante.setValue("Registrar Pago");
        botComprobante.setExcluirLectura(true);
        botComprobante.setIcon("ui-icon-contact");
        botComprobante.setMetodo("regAnticipo");
        bar_botones.agregarBoton(botComprobante);

        Boton botReimpresion = new Boton();
        botReimpresion.setValue("Reimpresion de Pago");
        botReimpresion.setExcluirLectura(true);
        botReimpresion.setIcon("ui-icon-contact");
        botReimpresion.setMetodo("reImpresion");
        bar_botones.agregarBoton(botReimpresion);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("LISTA PAGO ANTICIPADO, ABONO O LIQUIDACIÒN");
        agregarComponente(panOpcion);

        setAnticipo.setId("setAnticipo");
        setAnticipo.setSql("SELECT\n"
                + "s.id_solicitud,\n"
                + "a.fecha,\n"
                + "s.ced_empleado,\n"
                + "s.nom_empleado,\n"
                + "a.valor,\n"
                + "a.cuotas,\n"
                + "a.valor_saldo,\n"
                + "(select desc_tipo from nom_tipo where id_tipo = a.id_tipo) as tipo\n"
                + "FROM dbo.nom_solicitud s\n"
                + "INNER JOIN dbo.nom_anticipo a ON a.id_solicitud = s.id_solicitud\n"
                + "where a.estado_anticipo in (select id_tipo from nom_tipo where desc_tipo in('Aprobado','Cobrando'))\n"
                + "order by nom_empleado");
        setAnticipo.getColumna("ced_empleado").setFiltro(true);
        setAnticipo.getColumna("nom_empleado").setFiltro(true);
        setAnticipo.getColumna("nom_empleado").setLongitud(50);
        setAnticipo.setLectura(true);
        setAnticipo.setRows(12);

        setRegistro.setId("setRegistro");
        setRegistro.setSql("SELECT\n"
                + "d.id_pago,\n"
                + "d.fecha_documento,\n"
                + "d.num_pago,\n"
                + "d.id_solicitud,\n"
                + "n.ced_empleado,\n"
                + "n.nom_empleado,\n"
                + "d.valor_pagar,\n"
                + "(SELECT desc_tipo FROM nom_tipo where id_tipo =d.cod_pago) as opcion,\n"
                + "(SELECT desc_tipo FROM nom_tipo where id_tipo =d.tipo_pago) as tipo,\n"
                + "d.sub_valor\n"
                + "FROM nom_docu_pago d\n"
                + "inner join nom_solicitud n on d.id_solicitud = n.id_solicitud\n"
                + "where d.num_documento is null and d.obs_pago is null\n"
                + "order by num_pago");
        setRegistro.getColumna("ced_empleado").setFiltro(true);
        setRegistro.getColumna("nom_empleado").setFiltro(true);
        setRegistro.getColumna("num_pago").setFiltro(true);
        setRegistro.setLectura(true);
        setRegistro.setRows(12);

        setReimpresion.setId("setReimpresion");
        setReimpresion.setSql("SELECT \n"
                + "d.id_pago, \n"
                + "d.fecha_documento, \n"
                + "d.num_pago, \n"
                + "d.id_solicitud, \n"
                + "n.ced_empleado, \n"
                + "n.nom_empleado, \n"
                + "d.valor_pagar\n"
                + "FROM nom_docu_pago d \n"
                + "inner join nom_solicitud n on d.id_solicitud = n.id_solicitud \n"
                + "where d.num_pago is not null\n"
                + "order by num_pago desc");
        setReimpresion.getColumna("ced_empleado").setFiltro(true);
        setReimpresion.getColumna("nom_empleado").setFiltro(true);
        setReimpresion.getColumna("num_pago").setFiltro(true);
        setReimpresion.setLectura(true);
        setReimpresion.setRows(12);

        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("SELECCIONE LISTADO"); //titulo
        diaDialogo.setWidth("50%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("60%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("cargarRegistro");
        grid.setColumns(4);
        agregarComponente(diaDialogo);

        diaDialogop.setId("diaDialogop");
        diaDialogop.setTitle("LISTADO PARA REGISTRO VALORES"); //titulo
        diaDialogop.setWidth("50%"); //siempre en porcentajes  ancho
        diaDialogop.setHeight("60%");//siempre porcentaje   alto
        diaDialogop.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogop.getBot_aceptar().setMetodo("cargarPago");
        gridp.setColumns(4);
        agregarComponente(diaDialogop);

        diaDialogor.setId("diaDialogor");
        diaDialogor.setTitle("LISTADO DE REGISTROS PROCESADOS"); //titulo
        diaDialogor.setWidth("50%"); //siempre en porcentajes  ancho
        diaDialogor.setHeight("60%");//siempre porcentaje   alto
        diaDialogor.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogor.getBot_aceptar().setMetodo("cargarAbono");
        gridr.setColumns(4);
        agregarComponente(diaDialogor);

        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);

        dibujarPantalla();
    }

    public void dibujarPantalla() {
        limpiarPanel();

        tabAbono.setId("tabAbono");
        tabAbono.setTabla("nom_docu_pago", "id_pago", 2);
        if (autBusca.getValue() == null) {
            tabAbono.setCondicion("id_pago=-1");
        } else {
            tabAbono.setCondicion("id_pago=" + autBusca.getValor());
        }
        tabAbono.getColumna("id_solicitud").setCombo("SELECT id_solicitud,(ced_empleado+ ' - ' +nom_empleado) as describ,id_solicitud as num FROM nom_solicitud");
        tabAbono.getColumna("id_solicitud").setAutoCompletar();
        tabAbono.getColumna("cod_pago").setCombo("SELECT id_tipo,desc_tipo FROM dbo.nom_tipo where obs_tipo like 'CT' or obs_tipo like 'AB' or obs_tipo like 'SC'");
        tabAbono.getColumna("cod_pago").setMetodoChange("activaDes");
        tabAbono.getColumna("tipo_pago").setCombo("SELECT id_tipo,desc_tipo FROM dbo.nom_tipo where descripcion = 'AB'");
        tabAbono.getColumna("id_pago").setVisible(false);
        tabAbono.getColumna("login_documento").setVisible(false);
        tabAbono.getColumna("login_registro").setVisible(false);
        tabAbono.getColumna("fecha_registro").setVisible(false);
        tabAbono.getColumna("id_detalle").setVisible(false);
        tabAbono.getColumna("adjunto_abono").setUpload("remuneracion");
//        tabAbono.getColumna("adjunto_abono").setImagen("", "");
        tabAbono.setTipoFormulario(true);
        tabAbono.getGrid().setColumns(4);
        tabAbono.dibujar();

        PanelTabla tpa = new PanelTabla();
        tpa.setPanelTabla(tabAbono);

        Division divFormulario = new Division();
        divFormulario.setId("divFormulario");
        divFormulario.dividir1(tpa);
        agregarComponente(divFormulario);

        Grupo gru = new Grupo();
        gru.getChildren().add(divFormulario);
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

    /*
     * Genera pago para cobro total o liquidación
     */
    public void pagoAnticipo() {
        diaDialogo.Limpiar();
        diaDialogo.setDialogo(grid);
        gridD.getChildren().add(setAnticipo);
        diaDialogo.setDialogo(gridD);
        setAnticipo.dibujar();
        diaDialogo.dibujar();
    }

    public void cargarRegistro() {
        tabAnticipo.limpiar();
        limpiar();
        if (setAnticipo.getValorSeleccionado() != null) {
            tabAbono.getColumna("valor").setLectura(true);
            tabAbono.getColumna("fecha_pago").setLectura(true);
            tabAbono.getColumna("num_documento").setLectura(true);
            //tabAbono.getColumna("obs_pago").setLectura(true);
            tabAbono.getColumna("adjunto_abono").setLectura(true);
            tabAbono.getColumna("sub_valor").setLectura(true);
            dibujarPantalla();
            diaDialogo.cerrar();
            utilitario.addUpdate("autBusca,panOpcion");
            tabAbono.insertar();
            tabAbono.setValor("id_solicitud", setAnticipo.getValorSeleccionado());
            tabAbono.setValor("login_documento", utilitario.getVariable("NICK"));
            tabAbono.setValor("fecha_documento", utilitario.getFechaActual());
            utilitario.addUpdate("tabAbono");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Solicitud", "");
        }
    }

    public void activaDes() {
        if (tabAbono.getValor("cod_pago").equals("8")) {
            tabAbono.getColumna("tipo_pago").setLectura(true);
            tabAbono.getColumna("obs_pago").setLectura(true);
            utilitario.addUpdate("tabAbono");
            utilitario.agregarMensaje("Para pago total de anticipo vigente o cobro por liquidación", null);
            totalValor();
        } else if (tabAbono.getValor("cod_pago").equals("9")) {
            tabAbono.setValor("sub_valor", null);
            tabAbono.getColumna("tipo_pago").setLectura(false);
            tabAbono.getColumna("obs_pago").setLectura(true);
            utilitario.addUpdate("tabAbono");
            totalValor();
        } else if (tabAbono.getValor("cod_pago").equals("12")) {
            tabAbono.getColumna("obs_pago").setLectura(false);
            tabAbono.getColumna("tipo_pago").setLectura(true);
            utilitario.addUpdate("tabAbono");
            totalValor();
        }
    }

    /*
     * Calcula el valor que se pagara
     */
    public void totalValor() {
        TablaGenerica tabDatos = adminRemuneracion.getSolicitud(Integer.parseInt(tabAbono.getValor("id_solicitud")));
        if (!tabDatos.isEmpty()) {
            if (tabAbono.getValor("cod_pago").equals("12")) {
                tabAbono.setValor("valor", tabDatos.getValor("saldo_cuota"));
                tabAbono.setValor("id_detalle", tabDatos.getValor("id_detalle"));
            } else {
                tabAbono.setValor("valor", null);
                tabAbono.setValor("id_detalle", null);
            }
            tabAbono.setValor("sub_valor", tabDatos.getValor("valor_saldo"));
            utilitario.addUpdate("tabAbono");
        } else {
            utilitario.agregarMensaje("Valor no encontradro", null);
        }
    }

    /*
     * Registro de anticipo
     */
    public void regAnticipo() {
        diaDialogop.Limpiar();
        diaDialogop.setDialogo(gridp);
        gridP.getChildren().add(setRegistro);
        diaDialogop.setDialogo(gridP);
        setRegistro.dibujar();
        diaDialogop.dibujar();
    }

    public void cargarPago() {
        tabAnticipo.limpiar();
        limpiar();
        if (setRegistro.getValorSeleccionado() != null) {
            tabAbono.getColumna("tipo_pago").setVisible(false);
            tabAbono.getColumna("sub_valor").setVisible(false);
            tabAbono.getColumna("fecha_documento").setVisible(false);
            tabAbono.getColumna("valor_pagar").setVisible(false);
            tabAbono.getColumna("num_pago").setLectura(true);
            tabAbono.getColumna("cod_pago").setLectura(true);
            autBusca.setValor(setRegistro.getValorSeleccionado());
            dibujarPantalla();
            diaDialogop.cerrar();
            utilitario.addUpdate("autBusca,panOpcion");
//            tabAbono.setValor("login_registro", utilitario.getVariable("NICK"));
//            tabAbono.setValor("fecha_registro", utilitario.getFechaActual());
            utilitario.addUpdate("tabAbono");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Solicitud", "");
        }
    }

    public void reImpresion() {
        diaDialogor.Limpiar();
        diaDialogor.setDialogo(gridr);
        gridR.getChildren().add(setReimpresion);
        diaDialogor.setDialogo(gridR);
        setReimpresion.dibujar();
        diaDialogor.dibujar();
    }

    public void cargarAbono() {
        limpiar();
        if (setReimpresion.getValorSeleccionado() != null) {
            tabAbono.getColumna("tipo_pago").setVisible(false);
            tabAbono.getColumna("sub_valor").setVisible(false);
            tabAbono.getColumna("fecha_documento").setVisible(false);
            tabAbono.getColumna("valor_pagar").setVisible(false);
            tabAbono.getColumna("adjunto_abono").setVisible(false);
            tabAbono.getColumna("num_pago").setLectura(true);
            tabAbono.getColumna("cod_pago").setLectura(true);
            tabAbono.getColumna("valor").setLectura(true);
            tabAbono.getColumna("obs_pago").setLectura(true);
            tabAbono.getColumna("fecha_pago").setLectura(true);
            tabAbono.getColumna("num_documento").setLectura(true);
            autBusca.setValor(setReimpresion.getValorSeleccionado());
            dibujarPantalla();
            diaDialogor.cerrar();
            utilitario.addUpdate("autBusca,panOpcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una registro", "");
        }
    }

    /*
     * reimpresion de pago
     */
    public void numDocumento() {
        TablaGenerica tabDatos = adminRemuneracion.getBuscarTipo(Integer.parseInt(tabAbono.getValor("cod_pago")));
        if (!tabDatos.isEmpty()) {
            String numero = adminRemuneracion.getListaCodigo(Integer.parseInt(tabAbono.getValor("cod_pago")), tabDatos.getValor("obs_tipo"));
            String valor, num;
            Integer cantidad = 0;
            valor = numero.substring(3, 7);
            cantidad = Integer.parseInt(valor) + 1;
            if (numero != null) {
                if (cantidad >= 0 && cantidad <= 9) {
                    num = "000" + String.valueOf(cantidad);
                    String cadena = tabDatos.getValor("obs_tipo") + "-" + num;
                    tabAbono.setValor("num_pago", cadena);
                } else if (cantidad >= 10 && cantidad <= 99) {
                    num = "00" + String.valueOf(cantidad);
                    String cadena = tabDatos.getValor("obs_tipo") + "-" + num;
                    tabAbono.setValor("num_pago", cadena);
                } else if (cantidad >= 100 && cantidad <= 999) {
                    num = "0" + String.valueOf(cantidad);
                    String cadena = tabDatos.getValor("obs_tipo") + "-" + num;
                    tabAbono.setValor("num_pago", cadena);
                } else if (cantidad >= 1000 && cantidad <= 9999) {
                    num = String.valueOf(cantidad);
                    String cadena = tabDatos.getValor("obs_tipo") + "-" + num;
                    tabAbono.setValor("num_pago", cadena);
                }
            }
            utilitario.addUpdate("tabAbono");
        } else {
            utilitario.agregarMensaje("Valor no encontradro", null);
        }
    }

    public void veriPago() {
        TablaGenerica tabDatos = adminRemuneracion.getVerificaPago(Integer.parseInt(tabAbono.getValor("id_pago")));
        if (!tabDatos.isEmpty()) {
            if (tabAbono.getValor("cod_pago").endsWith("8")) {
                if (Double.valueOf(tabAbono.getValor("valor")).compareTo(Double.valueOf(tabDatos.getValor("valor_pagar"))) == 0) {
                    aceptoLiquidacion();
                } else {
                    utilitario.agregarMensaje("Valores no Coinciden", null);
                }
            } else {
                if (tabAbono.getValor("tipo_pago").endsWith("10")) {
                    if (Double.valueOf(tabAbono.getValor("valor")).compareTo(Double.valueOf(tabDatos.getValor("valor_pagar"))) == 0) {
                        abonoLiquidacion();
                    } else {
                        utilitario.agregarMensaje("Valores no Coinciden", null);
                    }
                } else {
                    if (Double.valueOf(tabAbono.getValor("valor")) == Double.valueOf(tabDatos.getValor("valor_pagar"))) {
                        utilitario.agregarMensaje("Valores no Coinciden", null);
                    } else {
                        utilitario.agregarMensaje("Valores no Coinciden", null);
                    }
                }
            }
        } else {
            utilitario.agregarMensaje("Datos no encontrados", null);
        }
    }

    public void veriPago1() {
        Double d1 = new Double(Double.valueOf(tabAbono.getValor("valor")));
        Double d2 = new Double(Double.valueOf(tabAbono.getValor("valor_pagar")));
        if (tabAbono.getValor("cod_pago").endsWith("12")) {
            if (d1.compareTo(d2) >= 0) {
                ban = "1";
                aceptoPagoSaldo();
            } else {
                ban = "0";
            }
        }
    }

    /*
     * Registro de pago generado con anticipación
     */
    public void aceptoLiquidacion() {
        TablaGenerica tabDatos = adminRemuneracion.getIdDetalle(Integer.parseInt(tabAbono.getValor("id_solicitud")));
        if (!tabDatos.isEmpty()) {
            adminRemuneracion.negarSolicitud(Integer.parseInt(tabAbono.getValor("id_solicitud")), "'Cancelado'");
//            adminRemuneracion.actualizSolicitud(Integer.parseInt(tabAbono.getValor("id_solicitud")), "'Cancelado'");
            adminRemuneracion.setActuaAnticipo(Integer.parseInt(tabAbono.getValor("id_solicitud")), "'Cancelado'", Double.valueOf(tabAbono.getValor("valor")));
            for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
                adminRemuneracion.actualizDetalle(Integer.parseInt(tabDatos.getValor(i, "id_detalle")), "'Cancelado'", utilitario.getFechaActual());
            }
            adminRemuneracion.setRegPagos(Integer.parseInt(tabAbono.getValor("id_pago")), tabAbono.getValor("fecha_pago"), Double.valueOf(tabAbono.getValor("valor")),
                    tabAbono.getValor("num_documento"), tabAbono.getValor("obs_pago"), tabAbono.getValor("adjunto_abono"), utilitario.getVariable("NICK"), utilitario.getFechaActual());
            utilitario.agregarMensaje("Registro Guardado", null);
        }
    }

    public void abonoLiquidacion() {
        TablaGenerica tabDatos = adminRemuneracion.getVerificaSaldo(Integer.parseInt(tabAbono.getValor("id_solicitud")),
                Double.valueOf(tabAbono.getValor("valor")));
        if (!tabDatos.isEmpty()) {
            if (Double.valueOf(tabDatos.getValor("estado")) > 0) {
                adminRemuneracion.setActuaAnticipo(Integer.parseInt(tabAbono.getValor("id_solicitud")), "'Cobrando'", Double.valueOf(tabAbono.getValor("valor")));
                adminRemuneracion.setRegPagos(Integer.parseInt(tabAbono.getValor("id_pago")), tabAbono.getValor("fecha_pago"), Double.valueOf(tabAbono.getValor("valor")),
                        tabAbono.getValor("num_documento"), tabAbono.getValor("obs_pago"), tabAbono.getValor("adjunto_abono"), utilitario.getVariable("NICK"), utilitario.getFechaActual());
                utilitario.agregarMensaje("Registro Guardado", null);
            } else {
//                TablaGenerica tabDato = adminRemuneracion.getIdDetalle(Integer.parseInt(tabAbono.getValor("id_solicitud")));
//                if (!tabDato.isEmpty()) {
                adminRemuneracion.negarSolicitud(Integer.parseInt(tabAbono.getValor("id_solicitud")), "'Cancelado'");
//                adminRemuneracion.actualizSolicitud(Integer.parseInt(tabAbono.getValor("id_solicitud")), "'Cancelado'");
                adminRemuneracion.setActuaAnticipo(Integer.parseInt(tabAbono.getValor("id_solicitud")), "'Cancelado'", Double.valueOf(tabAbono.getValor("valor")));
//                    for (int i = 0; i < tabDato.getTotalFilas(); i++) {
//                        adminRemuneracion.actualizDetalle(Integer.parseInt(tabDatos.getValor(i, "id_detalle")), "'Cancelado'", utilitario.getFechaActual());
//                    }
                adminRemuneracion.setRegPagos(Integer.parseInt(tabAbono.getValor("id_pago")), tabAbono.getValor("fecha_pago"), Double.valueOf(tabAbono.getValor("valor")),
                        tabAbono.getValor("num_documento"), tabAbono.getValor("obs_pago"), tabAbono.getValor("adjunto_abono"), utilitario.getVariable("NICK"), utilitario.getFechaActual());
                utilitario.agregarMensaje("Registro Guardado", null);
//                }
            }
        } else {
            utilitario.agregarMensaje("No puede ver valores", null);
        }
    }

    public void aceptoPagoSaldo() {
        adminRemuneracion.actualizDetalle1(Integer.parseInt(tabAbono.getValor("id_detalle")), "'Cancelado'", utilitario.getFechaActual());
        adminRemuneracion.registroAbono(Integer.parseInt(tabAbono.getValor("id_solicitud")));
        tabAbono.setValor("login_documento", utilitario.getVariable("NICK"));
        tabAbono.setValor("fecha_documento", utilitario.getFechaActual());
        utilitario.addUpdate("tabAbono");
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        if (tabAbono.getValor("id_pago") != null) {
            veriPago();
        } else {
            
            numDocumento();
            if (tabAbono.getValor("cod_pago").equals("12")) {
                veriPago1();
                if (ban.equals("1")) {
                    tabAbono.guardar();
                    guardarPantalla();
                } else {
                    utilitario.agregarMensaje("Valores no Coinciden", null);
                }
            } else {
                tabAbono.guardar();
                guardarPantalla();
            }
            
        }
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        switch (rep_reporte.getNombre()) {
            case "DOCUMENTO DE PAGO":
                aceptoAprobacion();
                break;
        }
    }

    public void aceptoAprobacion() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "DOCUMENTO DE PAGO":
                p_parametros.put("codigo", tabAbono.getValor("id_pago") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    public Tabla getTabAnticipo() {
        return tabAnticipo;
    }

    public void setTabAnticipo(Tabla tabAnticipo) {
        this.tabAnticipo = tabAnticipo;
    }

    public Tabla getTabAbono() {
        return tabAbono;
    }

    public void setTabAbono(Tabla tabAbono) {
        this.tabAbono = tabAbono;
    }

    public Tabla getSetAnticipo() {
        return setAnticipo;
    }

    public void setSetAnticipo(Tabla setAnticipo) {
        this.setAnticipo = setAnticipo;
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

    public Tabla getSetRegistro() {
        return setRegistro;
    }

    public void setSetRegistro(Tabla setRegistro) {
        this.setRegistro = setRegistro;
    }

    public Tabla getSetReimpresion() {
        return setReimpresion;
    }

    public void setSetReimpresion(Tabla setReimpresion) {
        this.setReimpresion = setReimpresion;
    }
}
