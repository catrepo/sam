/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_remuneraciones;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.ejb.EJB;
import paq_remuneraciones.ejb.BeanRemuneracion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class IgualarAnticipo extends Pantalla {

    private Tabla tabConsulta = new Tabla();
    private Tabla tabPersonal = new Tabla();
    private Tabla tabDetalle = new Tabla();
    private Dialogo diaDialogo = new Dialogo();
    private Grid grid = new Grid();
    private Grid gridD = new Grid();
    private AreaTexto txaObservacion = new AreaTexto();
    private Texto txtValor = new Texto();
    private Etiqueta etiObservacion = new Etiqueta("Observación : ");
    private Etiqueta etiValor = new Etiqueta("Valor : ");
    @EJB
    private BeanRemuneracion admin = (BeanRemuneracion) utilitario.instanciarEJB(BeanRemuneracion.class);

    public IgualarAnticipo() {

        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonsNavegacion();

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        Boton botBuscar = new Boton();
        botBuscar.setValue("VER DETALLE");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("filtraLista");
        bar_botones.agregarBoton(botBuscar);

        Boton botProceso = new Boton();
        botProceso.setValue("PROCESAR");
        botProceso.setExcluirLectura(true);
        botProceso.setIcon("ui-icon-gear");
        botProceso.setMetodo("cargaMensaje");
        bar_botones.agregarBoton(botProceso);

        tabPersonal.setId("tabPersonal");
        tabPersonal.setSql("select id_solicitud,ced_empleado,nom_empleado from ANTICIPO_PERSONA_VTA order by nom_empleado");
        tabPersonal.getColumna("ced_empleado").setFiltro(true);
        tabPersonal.getColumna("nom_empleado").setFiltro(true);
        tabPersonal.getColumna("id_solicitud").setVisible(false);
        tabPersonal.getColumna("ced_empleado").setLectura(true);
        tabPersonal.getColumna("nom_empleado").setLectura(true);
        tabPersonal.getColumna("nom_empleado").setLongitud(60);
        tabPersonal.setRows(30);
        tabPersonal.setLectura(true);
        tabPersonal.dibujar();
        PanelTabla patPersona = new PanelTabla();
        patPersona.setPanelTabla(tabPersonal);

        tabDetalle.setId("tabDetalle");
        tabDetalle.setTabla("nom_detalle", "id_detalle", 1);
        tabDetalle.setCondicion("id_detalle = -1");
        tabDetalle.getColumna("fecha_registro").setCheck();
        tabDetalle.getColumna("id_tipo").setCombo("SELECT id_tipo,desc_tipo from nom_tipo");
        tabDetalle.getColumna("id_solicitud").setVisible(false);
        tabDetalle.getColumna("numcuota").setVisible(false);
        tabDetalle.dibujar();
        PanelTabla patDetalle = new PanelTabla();
        patDetalle.setPanelTabla(tabDetalle);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(patPersona, patDetalle, "30%", "V");
        agregarComponente(div_division);

        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("MOTIVO DE OPERACIÓN"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("30%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("procesaLista");
        grid.setColumns(4);
        agregarComponente(diaDialogo);

    }

    public void filtraLista() {
        if (!getFiltrosLista().isEmpty()) {
            tabDetalle.setCondicion(getFiltrosLista());
            tabDetalle.ejecutarSql();
            utilitario.addUpdate("tabDetalle");
        }
    }

    private String getFiltrosLista() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "id_solicitud = " + tabPersonal.getValorSeleccionado();
        return str_filtros;
    }

    public void cargaMensaje() {
        if (tabDetalle.getValorSeleccionado() != null) {
            Grid griApel = new Grid();
            griApel.setColumns(2);
            txtValor.setSize(5);
            txaObservacion.setCols(45);
            griApel.getChildren().add(etiValor);
            griApel.getChildren().add(txtValor);
            griApel.getChildren().add(etiObservacion);
            griApel.getChildren().add(txaObservacion);
            diaDialogo.Limpiar();
            diaDialogo.setDialogo(grid);
            gridD.getChildren().add(griApel);
            diaDialogo.setDialogo(gridD);
            diaDialogo.dibujar();
        } else {
            utilitario.agregarMensaje("Debe elegir un detalle", null);
        }
    }

    public void procesaLista() {
        if (txaObservacion.getValue() != null && !txaObservacion.getValue().toString().isEmpty()) {
            if (txtValor.getValue() != null && !txtValor.getValue().toString().isEmpty()) {
                System.err.println("" + tabDetalle.getValorSeleccionado());
                TablaGenerica tabDato = admin.getSolicitudID(Integer.parseInt(tabDetalle.getValorSeleccionado() + ""));
                if (!tabDato.isEmpty()) {
                    if(Integer.parseInt(tabDato.getValor("ide_periodo"))< utilitario.getMes(utilitario.getFechaActual())
                            && Integer.parseInt(tabDato.getValor("anio")) <= utilitario.getAnio(utilitario.getFechaActual())){
                        String codigo = "";
                        codigo = numDocumento();
                        admin.regCuotaAbono(Integer.parseInt(tabDato.getValor("id_solicitud")), codigo.substring(3, 10), Integer.parseInt(codigo.substring(0, 2)), Double.parseDouble(txtValor.getValue() + ""), txaObservacion.getValue() + "", utilitario.getVariable("NICK"), Integer.parseInt(tabDetalle.getValorSeleccionado() + ""));
                        admin.actualizDetalle(Integer.parseInt(tabDetalle.getValorSeleccionado()), "'Cancelado'", utilitario.getFechaActual() + "");
                        actulizaAnticipo(tabDato.getValor("ced_empleado"));
                        actulizaEstado(Integer.parseInt(tabDato.getValor("ced_empleado")));
                        actualizaSolicitud(tabDato.getValor("ced_empleado"));
                        diaDialogo.cerrar();
                        utilitario.agregarMensaje("Operacion ejecutada", null);
                        utilitario.addUpdate("tabDetalle");
                    }else{
                        utilitario.agregarMensaje("Las cuotas solo pueden ser igualadas a un mes anterior de lo mostrado", null);
                    }
                    }else {
                        utilitario.agregarMensaje("Cedula no encontrada", null);
                    }
            } else {
                utilitario.agregarMensaje("Ingresar valor a igualar", null);
            }
        } else {
            utilitario.agregarMensaje("Ingresar observación", null);
        }
    }

    public void actulizaAnticipo(String codigo) {
        admin.descontarAnticipo(codigo);
    }

    public void actulizaEstado(Integer codigo) {
        TablaGenerica tabDatos = admin.getAnticipoAprobado(codigo);
        if (!tabDatos.isEmpty()) {
            admin.estadoAnticipo(Integer.parseInt(tabDatos.getValor("id_anticipo")), "Cobrando");
            admin.estadoSolicitud(Integer.parseInt(tabDatos.getValor("id_solicitud")), "Cobrando");
        }
    }

    public void actualizaSolicitud(String codigo) {
        TablaGenerica tabDatos = admin.getValorAnticipo(codigo);
        if (!tabDatos.isEmpty()) {
            admin.estadoAnticipo(Integer.parseInt(tabDatos.getValor("id_anticipo")), "Cancelado");
            admin.estadoSolicitud(Integer.parseInt(tabDatos.getValor("id_solicitud")), "Cancelado");
        }
    }

    public String numDocumento() {
        String cadena = "", codigo = "";
        TablaGenerica tabDatos = utilitario.consultar("select * from nom_tipo where obs_tipo='SC'");
        String numero = admin.getListaCodigo(Integer.parseInt(tabDatos.getValor("id_tipo")), tabDatos.getValor("obs_tipo"));
        codigo = tabDatos.getValor("id_tipo");
        String valor, num;
        Integer cantidad = 0;
        valor = numero.substring(3, 7);
        cantidad = Integer.parseInt(valor) + 1;
        if (numero != null) {
            if (cantidad >= 0 && cantidad <= 9) {
                num = "000" + String.valueOf(cantidad);
                cadena = tabDatos.getValor("obs_tipo") + "-" + num;
            } else if (cantidad >= 10 && cantidad <= 99) {
                num = "00" + String.valueOf(cantidad);
                cadena = tabDatos.getValor("obs_tipo") + "-" + num;
            } else if (cantidad >= 100 && cantidad <= 999) {
                num = "0" + String.valueOf(cantidad);
                cadena = tabDatos.getValor("obs_tipo") + "-" + num;
            } else if (cantidad >= 1000 && cantidad <= 9999) {
                num = String.valueOf(cantidad);
                cadena = tabDatos.getValor("obs_tipo") + "-" + num;
            }
        }
        return codigo + "/" + cadena;
    }

    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Tabla getTabPersonal() {
        return tabPersonal;
    }

    public void setTabPersonal(Tabla tabPersonal) {
        this.tabPersonal = tabPersonal;
    }

    public Tabla getTabDetalle() {
        return tabDetalle;
    }

    public void setTabDetalle(Tabla tabDetalle) {
        this.tabDetalle = tabDetalle;
    }
}
