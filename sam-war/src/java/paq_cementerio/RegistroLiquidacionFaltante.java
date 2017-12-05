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
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import paq_cementerio.ejb.cementerio;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class RegistroLiquidacionFaltante extends Pantalla {

    private Tabla tabConsulta = new Tabla();
    private Tabla tabFallecido = new Tabla();
    private Tabla tabMovimiento = new Tabla();
    private Tabla tabDetalle = new Tabla();
    private Combo cmbOpcion = new Combo();
    private SeleccionTabla setFallecido = new SeleccionTabla();
    private AutoCompletar autBusca = new AutoCompletar();
    private Texto texBusqueda = new Texto();
    private Panel panOpcion = new Panel();
    @EJB
    private cementerio admin = (cementerio) utilitario.instanciarEJB(cementerio.class);

    public RegistroLiquidacionFaltante() {

        Boton botBusca = new Boton();
        botBusca.setValue("Buscar Fallecido");
        botBusca.setExcluirLectura(true);
        botBusca.setIcon("ui-icon-search");
        botBusca.setMetodo("buscaRegistro");
        bar_botones.agregarBoton(botBusca);

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT IDE_FALLECIDO,FECHA_INGRE,liquidacion,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,CATASTRO,FECHA_DESDE,FECHA_HASTA\n"
                + "FROM CMT_FALLECIDO_RENOVACION");
        autBusca.setMetodoChange("buscarPersona");
        autBusca.setSize(70);

        Grid griBuscar = new Grid();
        griBuscar.setColumns(2);

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

        Boton botRegistro = new Boton();
        botRegistro.setValue("Buscar");
        botRegistro.setIcon("ui-icon-search");
        botRegistro.setMetodo("busquedaInfo");

        griBuscar.getChildren().add(new Etiqueta("Buscar por :"));
        griBuscar.getChildren().add(cmbOpcion);
        texBusqueda.setSize(20);
        griBuscar.getChildren().add(new Etiqueta("ingrese :"));
        griBuscar.getChildren().add(texBusqueda);
        griBuscar.getChildren().add(botRegistro);

        setFallecido.setId("setFallecido");
        setFallecido.setSeleccionTabla("SELECT DISTINCT IDE_FALLECIDO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,CATASTRO,FECHA_DESDE,FECHA_HASTA,\n"
                + "representante,DETALLE_CMACC\n"
                + "FROM CMT_FALLECIDO_RENOVACION WHERE IDE_FALLECIDO =-1", "IDE_FALLECIDO");
        setFallecido.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setFallecido.getTab_seleccion().setRows(10);
        setFallecido.setRadio();
        setFallecido.getGri_cuerpo().setHeader(griBuscar);//consultaFallecido
        setFallecido.getBot_aceptar().setMetodo("consultaFallecido");
        setFallecido.setWidth("90%");
        setFallecido.setHeader("BUSCAR POR PARAMETROS");
        agregarComponente(setFallecido);

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("<CENTER>INGRESO DE MOVIMIENTOS FALTANTES - CEMENTERIO MUNICIPAL </CENTER>");
        agregarComponente(panOpcion);

        dibujarPantalla();
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

    public void buscaRegistro() {
        setFallecido.dibujar();
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
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
        }
    }

    public void buscarSolicitud() {
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("SELECT IDE_FALLECIDO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,CATASTRO,FECHA_DESDE,FECHA_HASTA,\n"
                    + "representante,DETALLE_CMACC\n"
                    + "FROM CMT_FALLECIDO_RENOVACION\n"
                    + "WHERE NOMBRES LIKE '%" + texBusqueda.getValue() + "%' order by NOMBRES");
            setFallecido.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud1() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("SELECT IDE_FALLECIDO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,CATASTRO,FECHA_DESDE,FECHA_HASTA,\n"
                    + "representante,DETALLE_CMACC\n"
                    + "FROM CMT_FALLECIDO_RENOVACION\n"
                    + "WHERE CEDULA_FALLECIDO = '" + texBusqueda.getValue() + "' order by NOMBRES");
            setFallecido.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud2() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("SELECT IDE_FALLECIDO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,CATASTRO,FECHA_DESDE,FECHA_HASTA,\n"
                    + "representante,DETALLE_CMACC\n"
                    + "FROM CMT_FALLECIDO_RENOVACION\n"
                    + "WHERE representante LIKE '%" + texBusqueda.getValue() + "%' order by NOMBRES");
            setFallecido.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud3() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("SELECT IDE_FALLECIDO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,CATASTRO,FECHA_DESDE,FECHA_HASTA,\n"
                    + "representante,DETALLE_CMACC\n"
                    + "FROM CMT_FALLECIDO_RENOVACION\n"
                    + "WHERE documento_identidad_cmrep LIKE '%" + texBusqueda.getValue() + "%' order by NOMBRES");
            setFallecido.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void consultaFallecido() {
        if (setFallecido.getValorSeleccionado() != null) {
            limpiar();
            System.out.println("setSolicitud.getValorSeleccionado()<<" + setFallecido.getValorSeleccionado());
            autBusca.setValor(setFallecido.getValorSeleccionado());
            dibujarPantalla();
            setFallecido.cerrar();
            tabDetalle.insertar();
            actualizaLista();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Solicitud", "");
        }
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
        tabFallecido.setTipoFormulario(true);
        tabFallecido.getGrid().setColumns(6);
        tabFallecido.dibujar();
        tabFallecido.agregarRelacion(tabMovimiento);
        tabFallecido.agregarRelacion(tabDetalle);
        PanelTabla pntFallecido = new PanelTabla();
        pntFallecido.setMensajeWarn("DATOS DE FALLECIDO");
        pntFallecido.setPanelTabla(tabFallecido);

        tabMovimiento.setId("tabMovimiento");
        tabMovimiento.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 2);
        tabMovimiento.getColumna("ide_tipo_movimiento").setCombo("SELECT IDE_TIPO_PAGO, DESCRIPCION FROM dbo.CMT_TIPO_PAGO where IDE_TIPO_PAGO in (3,4)");
        tabMovimiento.setCondicion("IDE_DET_MOVIMIENTO=-1");
        tabMovimiento.setTipoFormulario(true);
        tabMovimiento.getGrid().setColumns(6);
        tabMovimiento.dibujar();
        PanelTabla pntMovimiento = new PanelTabla();
        pntMovimiento.setMensajeWarn("DETALLE ULTIMO MOVIMIENTOS");
        pntMovimiento.setPanelTabla(tabMovimiento);

        tabDetalle.setId("tabDetalle");
        tabDetalle.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 3);
        tabDetalle.setCondicion("IDE_DET_MOVIMIENTO=-1");
        tabDetalle.getColumna("ide_tipo_movimiento").setCombo("SELECT IDE_TIPO_PAGO, DESCRIPCION FROM dbo.CMT_TIPO_PAGO where IDE_TIPO_PAGO in (3,4)");
        tabDetalle.getColumna("num_liquidacion").setMetodoChange("buscaLiquidacion");
        tabDetalle.getColumna("usuario_ingre").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabDetalle.getColumna("fecha_ingre").setValorDefecto(utilitario.getFechaActual());
        tabDetalle.setTipoFormulario(true);
        tabDetalle.getGrid().setColumns(6);
        tabDetalle.dibujar();
        PanelTabla pntDetalle = new PanelTabla();
        pntDetalle.setMensajeWarn("INGRESAR DETALLE MOVIMIENTOS FALTANTE");
        pntDetalle.setPanelTabla(tabDetalle);

        Division div = new Division();
        div.setId("div");
        div.dividir3(pntFallecido, pntMovimiento, pntDetalle, "25%", "45%", "H");

        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);
    }

    public void actualizaLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabMovimiento.setCondicion(getFiltrosAcceso());
            tabMovimiento.ejecutarSql();
            utilitario.addUpdate("tabMovimiento");
        }
    }

    private String getFiltrosAcceso() {
        System.err.println(autBusca.getValor());
        String str_filtros = "";
        if (autBusca.getValor() != null) {
            str_filtros = "IDE_DET_MOVIMIENTO = (SELECT (SELECT top 1 IDE_DET_MOVIMIENTO FROM CMT_DETALLE_MOVIMIENTO where IDE_FALLECIDO = CMT_FALLECIDO.IDE_FALLECIDO order by FECHA_DESDE desc)as ide\n"
                    + "FROM CMT_FALLECIDO\n"
                    + "where IDE_FALLECIDO =" + Integer.parseInt(autBusca.getValor()) + ")";
        }
        return str_filtros;
    }

    public void buscaLiquidacion() {
        Date desde, hasta, ingreso;
        TablaGenerica tabDatos = admin.getLiquidacionTitulo(tabDetalle.getValor("num_liquidacion") + "");
        if (!tabDatos.isEmpty()) {
            TablaGenerica tabDato = admin.getValorLiquidacion(tabDetalle.getValor("num_liquidacion") + "");
            if (!tabDato.isEmpty()) {
                tabDetalle.setValor("antecedentes", tabDatos.getValor("concepto_titulo"));
                tabDetalle.setValor("observacion", tabDatos.getValor("observaciones"));
                tabDetalle.setValor("emitido", tabDatos.getValor("pagado"));
                tabDetalle.setValor("valor_liquidacion", tabDato.getValor("valortotal_liquidacion"));
                tabDetalle.setValor("periodo_arrendamiento", tabDato.getValor("direccion_representante").substring(36, 40).replace(" ", ""));
                tabDetalle.setValor("fecha_desde", utilitario.getFechaActual());
                desde = utilitario.DeStringADateformato3(tabDato.getValor("direccion_representante").substring(0, 10).toString());
                hasta = utilitario.DeStringADateformato3(tabDato.getValor("direccion_representante").substring(15, 28).replace(" ", "").toString());
                tabDetalle.setValor("fecha_desde", utilitario.DeDateAString(desde).toString());
                tabDetalle.setValor("fecha_hasta", utilitario.DeDateAString(hasta).toString());
                tabDetalle.setValor("ide_catastro", tabFallecido.getValor("ide_catastro"));
                utilitario.addUpdate("tabDetalle");
            } else {
                utilitario.agregarMensaje("Liquidación aun no se encuentra pagada", null);
            }
        } else {
            utilitario.agregarMensaje("Liquidación aun no se encuentra pagada", null);
        }
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        TablaGenerica tabDatos = admin.getLiquidacionTitulo(tabDetalle.getValor("num_liquidacion") + "");
        if (!tabDatos.isEmpty()) {
            if (tabDetalle.guardar()) {
                guardarPantalla();
            }
        } else {
            utilitario.agregarMensaje("Liquidación aun no se encuentra pagada", "No se puede agregar");
        }
    }

    @Override
    public void eliminar() {
    }

    public Tabla getTabFallecido() {
        return tabFallecido;
    }

    public void setTabFallecido(Tabla tabFallecido) {
        this.tabFallecido = tabFallecido;
    }

    public SeleccionTabla getSetFallecido() {
        return setFallecido;
    }

    public void setSetFallecido(SeleccionTabla setFallecido) {
        this.setFallecido = setFallecido;
    }

    public Tabla getTabMovimiento() {
        return tabMovimiento;
    }

    public void setTabMovimiento(Tabla tabMovimiento) {
        this.tabMovimiento = tabMovimiento;
    }

    public Tabla getTabDetalle() {
        return tabDetalle;
    }

    public void setTabDetalle(Tabla tabDetalle) {
        this.tabDetalle = tabDetalle;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }
}
