/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_presupuestaria.ejb.Programas;
import sistema.aplicacion.Pantalla;
import paq_utilitario.ejb.ClaseGenerica;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class OrdenPago extends Pantalla {
    /*
     * Variable que permite conectar a base de datos diferente
     */

    private Conexion conPostgres = new Conexion();
    /*
     * Declaración de Tablas, para el formulario a utilizar
     */
    private Tabla tabTabla = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla setOrdenes = new Tabla();
    /*
     * Cuadro de dialogos, para busqueda o completar información
     */
    private AutoCompletar autBusca = new AutoCompletar();
    private Panel panOpcion = new Panel();
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaDialogot = new Dialogo();
    private Grid grid = new Grid();
    private Grid gridm = new Grid();
    private Grid gridt = new Grid();
    private Grid gridD = new Grid();
    private Grid gridT = new Grid();
    Texto txtMotivo = new Texto();
    /*
     * Variable para imprimir reportes
     */
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    /*
     * Acceso a manipulación de datos, CRUD
     */
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
    private ClaseGenerica generico = (ClaseGenerica) utilitario.instanciarEJB(ClaseGenerica.class);

    public OrdenPago() {
        /*
         * Permite tener acceso a información, de los datos de registro
         */
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        /*
         * Cadena de conexión para otra base de datos
         */
        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        /*
         * Contiene todo el formulario
         */
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("SOLICITUD DE ORDENES DE PAGO");
        agregarComponente(panOpcion);

        /*
         * Elementos dibujados en barra de herramientas
         */

        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar Registro");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("buscaRegistro");
        bar_botones.agregarBoton(botBuscar);

        autBusca.setId("autBusca");
        autBusca.setConexion(conPostgres);
        autBusca.setAutoCompletar("select a.tes_ide_orden_pago,   \n"
                + "a.numero_orden,    \n"
                + "(case when  a.tes_cod_empleado is null then b.titular when a.tes_cod_empleado is not null then c.nombres end)   \n"
                + "as beneficiario,   \n"
                + "a.valor,    \n"
                + "a.acuerdo   \n"
                + "from (SELECT\n"
                + "tes_numero_orden AS numero_orden,     \n"
                + "tes_valor AS valor,    \n"
                + "tes_acuerdo AS acuerdo,   \n"
                + "tes_ide_orden_pago,   \n"
                + "tes_cod_empleado,  \n"
                + "(case when tes_id_proveedor is null then tes_cod_empleado when tes_id_proveedor is not null then tes_id_proveedor end) as cod_benefi   \n"
                + "FROM tes_orden_pago where tes_estado_doc = '1' ) as a   \n"
                + "left join    \n"
                + "(SELECT ide_proveedor,titular FROM tes_proveedores) as b   \n"
                + "on a.cod_benefi = b.ide_proveedor   \n"
                + "left join    \n"
                + "(SELECT cedula_pass,nombres,cod_empleado FROM srh_empleado) as c   \n"
                + "on a.cod_benefi = c.cod_empleado\n"
                + "order by numero_orden desc");
        autBusca.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Registros Encontrado"));
        bar_botones.agregarComponente(autBusca);

        Boton botAnular = new Boton();
        botAnular.setValue("Anular");
        botAnular.setExcluirLectura(true);
        botAnular.setIcon("ui-icon-cancel");
        botAnular.setMetodo("anularOrden");
        bar_botones.agregarBoton(botAnular);

        /*
         * Cuadro de dialogo para anulación y verificacion de datos ingresados
         */
        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("¿ MOTIVO DE ANULACIÓN ?"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("20%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("aceptoAnulacion");
        grid.setColumns(2);
        agregarComponente(diaDialogo);

        diaDialogot.setId("diaDialogot");
        diaDialogot.setTitle("LISTADO DE ORDENES DE PAGO REALIZADAS"); //titulo
        diaDialogot.setWidth("95%"); //siempre en porcentajes  ancho
        diaDialogot.setHeight("85%");//siempre porcentaje   alto
        diaDialogot.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogot.getBot_aceptar().setMetodo("cargarRegistro");
        gridt.setColumns(4);
        agregarComponente(diaDialogot);

        /*
         * Muestra listado de registros almacenados
         */
        setOrdenes.setId("setOrdenes");
        setOrdenes.setConexion(conPostgres);
        setOrdenes.setSql("select   \n"
                + " a.tes_ide_orden_pago,  \n"
                + " a.numero_orden,  \n"
                + " a.numero_comprobante as comprobante,\n"
                + "(case when  a.tes_cod_empleado is null then b.titular when a.tes_cod_empleado is not null then c.nombres end)  \n"
                + "as beneficiario,  \n"
                + " a.asunto,  \n"
                + " a.valor,  \n"
                + " a.concepto,  \n"
                + " a.acuerdo  \n"
                + " from (  \n"
                + " SELECT   \n"
                + " tes_numero_orden AS numero_orden,  \n"
                + " tes_comprobante_egreso AS numero_comprobante,  \n"
                + " tes_asunto AS asunto,  \n"
                + " tes_valor AS valor,  \n"
                + " tes_concepto AS concepto,  \n"
                + " tes_acuerdo AS acuerdo,  \n"
                + " tes_ide_orden_pago,"
                + " tes_anio,  \n"
                + " tes_cod_empleado, \n"
                + " (case when tes_id_proveedor is null then tes_cod_empleado when tes_id_proveedor is not null then tes_id_proveedor end) as cod_benefi  \n"
                + " FROM tes_orden_pago where tes_estado_doc = '1') as a  \n"
                + " left join   \n"
                + " (SELECT ide_proveedor,titular FROM tes_proveedores) as b  \n"
                + " on a.cod_benefi = b.ide_proveedor  \n"
                + " left join   \n"
                + " (SELECT cedula_pass,nombres,cod_empleado FROM srh_empleado) as c  \n"
                + " on a.cod_benefi = c.cod_empleado  \n"
                + " order by numero_orden desc, tes_anio");
        setOrdenes.getColumna("tes_ide_orden_pago").setVisible(false);
        setOrdenes.getColumna("numero_orden").setLongitud(4);
        setOrdenes.getColumna("comprobante").setLongitud(4);
        setOrdenes.getColumna("numero_orden").setFiltroContenido();
        setOrdenes.getColumna("beneficiario").setFiltroContenido();
        setOrdenes.getColumna("acuerdo").setFiltroContenido();
        setOrdenes.getColumna("comprobante").setFiltroContenido();
        setOrdenes.getColumna("acuerdo").setLongitud(50);
        setOrdenes.getColumna("beneficiario").setLongitud(80);
        setOrdenes.setLectura(true);
        setOrdenes.setRows(13);
        setOrdenes.dibujar();

        /*
         * Configuración y llamado de objeto reporte
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);

        dibujarPantalla();
    }

    public void dibujarPantalla() {
        /*
         * Formualrio de ingreso
         */
        limpiarPanel();
        tabTabla.setId("tabTabla");
        tabTabla.setConexion(conPostgres);
        tabTabla.setTabla("tes_orden_pago", "tes_ide_orden_pago", 1);
        if (autBusca.getValue() == null) {
            tabTabla.setCondicion("tes_ide_orden_pago=-1");
        } else {
            tabTabla.setCondicion("tes_ide_orden_pago=" + autBusca.getValor());
        }
        tabTabla.getColumna("tes_id_proveedor").setCombo("select ide_proveedor,titular from tes_proveedores order by titular");
        tabTabla.getColumna("tes_id_proveedor").setFiltroContenido();
        tabTabla.getColumna("tes_cod_empleado").setCombo("select cod_empleado,nombres from srh_empleado order by nombres");
        tabTabla.getColumna("tes_cod_empleado").setFiltroContenido();
        tabTabla.getColumna("tes_id_proveedor").setMetodoChange("proveedor");
        tabTabla.getColumna("tes_cod_empleado").setMetodoChange("empleado");
        tabTabla.getColumna("tes_valor").setMetodoChange("numLetras");
        tabTabla.getColumna("tes_estado_doc").setValorDefecto("1");
        tabTabla.getColumna("tes_anio").setVisible(false);
        tabTabla.getColumna("tes_mes").setVisible(false);
        tabTabla.getColumna("tes_ide_orden_pago").setVisible(false);
        tabTabla.getColumna("tes_ide_orden_pago").setVisible(false);
        tabTabla.getColumna("tes_proveedor").setVisible(false);
        tabTabla.getColumna("tes_estado_doc").setVisible(false);
        tabTabla.getColumna("tes_empleado").setVisible(false);
        tabTabla.getColumna("tipo_solicitantep").setVisible(false);
        tabTabla.getColumna("tes_login_ing").setVisible(false);
        tabTabla.getColumna("tes_login_anu").setVisible(false);
        tabTabla.getColumna("tes_fecha_anu").setVisible(false);
        tabTabla.getColumna("tes_login_actu").setVisible(false);
        tabTabla.getColumna("tes_fecha_actu").setVisible(false);
        tabTabla.getColumna("tes_login_envio").setVisible(false);
        tabTabla.getColumna("tes_comentario_anula").setVisible(false);
        tabTabla.getColumna("tes_loginrecp").setVisible(false);
        tabTabla.getColumna("tes_letrero").setVisible(false);
        tabTabla.getColumna("tes_revision").setVisible(false);
        tabTabla.getColumna("tes_fecha_mod").setVisible(false);
        tabTabla.getColumna("tes_anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tabTabla.getColumna("tes_mes").setValorDefecto(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
        tabTabla.getColumna("tes_fecha_ingreso").setValorDefecto(utilitario.getFechaActual());
        tabTabla.getColumna("tes_login_ing").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabTabla.setTipoFormulario(true);
        tabTabla.getGrid().setColumns(2);
        tabTabla.dibujar();
        PanelTabla pto = new PanelTabla();
        pto.setPanelTabla(tabTabla);
        Grupo gru = new Grupo();
        gru.getChildren().add(pto);
        panOpcion.getChildren().add(gru);
    }

    public void buscaRegistro() {
        diaDialogot.Limpiar();
        diaDialogot.setDialogo(gridt);
        gridT.getChildren().add(setOrdenes);
        diaDialogot.setDialogo(gridT);
        setOrdenes.dibujar();
        diaDialogot.dibujar();
    }

    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    /*
     * Subformulario con listado de ordenes de pago
     */
    public void cargarRegistro() {
        if (setOrdenes.getValorSeleccionado() != null) {
            System.err.println(setOrdenes.getValorSeleccionado());
            autBusca.setValor(setOrdenes.getValorSeleccionado());
            dibujarPantalla();
            diaDialogot.cerrar();
            utilitario.addUpdate("autBusca,panOpcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Solicitud", "");
        }
    }

    /*
     * Ingresa informacion adicional para el formulario y limpia combo de selección
     */
    public void proveedor() {
        if (tabTabla.getValor("tes_id_proveedor") != null) {
            TablaGenerica tab_dato = programas.getProveedor(Integer.parseInt(tabTabla.getValor("tes_id_proveedor")));
            if (!tab_dato.isEmpty()) {
                tabTabla.setValor("tes_proveedor", tab_dato.getValor("titular"));
                tabTabla.setValor("tes_empleado", null);
                tabTabla.setValor("tes_cod_empleado", null);
                tabTabla.setValor("tes_estado", "Pendiente");
                tabTabla.setValor("tipo_solicitantep", "2");
                utilitario.addUpdate("tabTabla");//actualiza solo componente
                System.out.println("Nombre de proveedor>>>>>>\t" + tabTabla.getValor("tes_proveedor"));
                tabTabla.getColumna("tes_cod_empleado").setCombo("select cod_empleado,nombres from srh_empleado where cod_empleado is null");
                utilitario.addUpdateTabla(tabTabla, "tes_cod_empleado", "");//actualiza solo componentes
            }
        } else {
            tabTabla.getColumna("tes_cod_empleado").setLectura(false);
            utilitario.addUpdate("tabTabla");//actualiza solo componentes
        }
    }

    public void empleado() {
        if (tabTabla.getValor("tes_cod_empleado") != null) {
            TablaGenerica tab_dato = programas.getEmpleado(Integer.parseInt(tabTabla.getValor("tes_cod_empleado")));
            if (!tab_dato.isEmpty()) {
                tabTabla.setValor("tes_empleado", tab_dato.getValor("nombres"));
                tabTabla.setValor("tes_proveedor", null);
                tabTabla.setValor("tes_id_proveedor", null);
                tabTabla.setValor("tes_estado", "Pendiente");
                tabTabla.setValor("tipo_solicitantep", "1");
                utilitario.addUpdate("tabTabla");//actualiza solo componentes
                System.out.println("Nombre de empleado>>>>>>\t" + tabTabla.getValor("tes_empleado"));
                tabTabla.getColumna("tes_id_proveedor").setCombo("select ide_proveedor,titular from tes_proveedores order by titular");
                utilitario.addUpdateTabla(tabTabla, "tes_id_proveedor", "");//actualiza solo componentes
            }
        } else {
            tabTabla.getColumna("tes_id_proveedor").setLectura(false);
            utilitario.addUpdate("tabTabla");//actualiza solo componentes
        }
    }

    /*
     * Tranformación de numeros a letras
     */
    public void numLetras() {
        Object objeto = (Object) tabTabla.getValor("tes_valor");
        tabTabla.setValor("tes_valor_letras", utilitario.getLetrasDolarNumero((objeto)));
        utilitario.addUpdate("tabTabla");//actualiza solo componentes
    }

    /*
     * Para anular orden
     */
    public void anularOrden() {
        diaDialogo.Limpiar();
        txtMotivo.limpiar();
        diaDialogo.setDialogo(grid);
        txtMotivo.setSize(50);
        gridm.getChildren().clear();
        gridm.getChildren().add(new Etiqueta("INGRESE MOTIVO DE ANULACIÓN DE ORDEN :"));
        grid.getChildren().clear();
        grid.getChildren().add(new Etiqueta("_____________________________________________________"));
        grid.getChildren().add(txtMotivo);
        diaDialogo.setDialogo(gridD);
        diaDialogo.dibujar();
    }

    public void aceptoAnulacion() {
        programas.actOrden(tabTabla.getValor("tes_numero_orden"), Integer.parseInt(tabTabla.getValor("tes_ide_orden_pago")), tabConsulta.getValor("NICK_USUA"), txtMotivo.getValue() + "");
        utilitario.addUpdate("tabTabla");
        utilitario.agregarMensaje("ORDEN ANULADA", "");
        diaDialogo.cerrar();
    }

    /*
     * Numero secuencial para orden de pago
     */
    public void numerOrden() {
        String numero = programas.maximOrden();
        String num;
        Integer cantidad = 0;
        cantidad = Integer.parseInt(numero) + 1;
        if (numero != null) {
            if (cantidad >= 0 && cantidad <= 9) {
                num = "000000" + String.valueOf(cantidad);
                tabTabla.setValor("tes_numero_orden", num);
            } else if (cantidad >= 10 && cantidad <= 99) {
                num = "00000" + String.valueOf(cantidad);
                tabTabla.setValor("tes_numero_orden", num);
            } else if (cantidad >= 100 && cantidad <= 999) {
                num = "0000" + String.valueOf(cantidad);
                tabTabla.setValor("tes_numero_orden", num);
            } else if (cantidad >= 1000 && cantidad <= 9999) {
                num = "000" + String.valueOf(cantidad);
                tabTabla.setValor("tes_numero_orden", num);
            } else if (cantidad >= 10000 && cantidad <= 99999) {
                num = "00" + String.valueOf(cantidad);
                tabTabla.setValor("tes_numero_orden", num);
            } else if (cantidad >= 100000 && cantidad <= 999999) {
                num = "0" + String.valueOf(cantidad);
                tabTabla.setValor("tes_numero_orden", num);
            } else if (cantidad >= 1000000 && cantidad <= 9999999) {
                num = String.valueOf(cantidad);
                tabTabla.setValor("tes_numero_orden", num);
            }
        }
        utilitario.addUpdate("tabTabla");
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
        numerOrden();
    }

    @Override
    public void guardar() {
        if (tabTabla.getValor("tes_ide_orden_pago") != null) {
            TablaGenerica tabInfo = generico.getCatalogoTabla("*", tabTabla.getTabla(), "tes_ide_orden_pago = " + tabTabla.getValor("tes_ide_orden_pago") + "");
            if (!tabInfo.isEmpty()) {
                TablaGenerica tabDato = generico.getNumeroCampos(tabTabla.getTabla());
                if (!tabDato.isEmpty()) {
                    for (int i = 1; i < Integer.parseInt(tabDato.getValor("NumeroCampos")); i++) {
                        if (i != 1) {
                            TablaGenerica tabInfoColum1 = generico.getEstrucTabla(tabTabla.getTabla(), i);
                            if (!tabInfoColum1.isEmpty()) {
                                try {
                                    if (tabTabla.getValor(tabInfoColum1.getValor("Column_Name")).equals(tabInfo.getValor(tabInfoColum1.getValor("Column_Name")))) {
                                    } else {
                                        generico.setActuaRegistro(Integer.parseInt(tabTabla.getValor("tes_ide_orden_pago")), tabTabla.getTabla(), tabInfoColum1.getValor("Column_Name"), tabTabla.getValor(tabInfoColum1.getValor("Column_Name")), "tes_ide_orden_pago");
                                    }
                                } catch (NullPointerException e) {
                                }
                            }
                        }
                    }
                }
            }
            String cadena = "", cadena1 = "", cadena2 = "";
            if (tabTabla.getValor("tes_id_proveedor") != null) {
                cadena = "tes_cod_empleado";
                cadena1 = "tes_empleado";
                cadena2=null;
            } else {
                cadena = "tes_id_proveedor";
                cadena1 = "tes_proveedor";
                cadena2=null;
            }
            programas.setActuOrdenPago(Integer.parseInt(tabTabla.getValor("tes_ide_orden_pago")), cadena,cadena1,cadena2,cadena2);
            utilitario.agregarMensaje("Registro Actalizado", null);
        } else {
            if (tabTabla.guardar()) {
                conPostgres.guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
    }

    /*
     * Pedido de reporte
     */
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "IMPRIMIR ORDEN":
                aceptoOrden();
                break;
        }
    }

    public void aceptoOrden() {
        switch (rep_reporte.getNombre()) {
            case "IMPRIMIR ORDEN":
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("id_orden", tabTabla.getValor("tes_numero_orden") + "");
                p_parametros.put("id_documento", Integer.parseInt(tabTabla.getValor("tes_ide_orden_pago") + ""));
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }

    public Tabla getSetOrdenes() {
        return setOrdenes;
    }

    public void setSetOrdenes(Tabla setOrdenes) {
        this.setOrdenes = setOrdenes;
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
