/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import paq_cementerio.ejb.cementerio;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class pre_cambio_sitio extends Pantalla {

    private Tabla tabConsulta = new Tabla();
    private Tabla tabFallecido = new Tabla();
    private Tabla tabMovimiento = new Tabla();
    private Tabla tabCatastro = new Tabla();
    private SeleccionTabla setFallecido = new SeleccionTabla();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();
    private Combo cmbOpcion = new Combo();
    private Combo cmbLugar = new Combo();
    private Combo cmbSector = new Combo();
    private Combo cmbModulo = new Combo();
    private Combo cmbNumero = new Combo();
    private Texto txtBusqueda = new Texto();
    private Etiqueta etiLugar = new Etiqueta("Lugar : ");
    private Etiqueta etiSector = new Etiqueta("Sector : ");
    private Etiqueta etiModulo = new Etiqueta("Modulo : ");
    private Etiqueta etiNumero = new Etiqueta("Número : ");
    private Etiqueta etiAdvertencia = new Etiqueta("<center><BR>EL SITIO AL QUE DESEA TRASLADAR LOS RESTOS, SE  ENCUENTRA OCUPADO \n\t </BR></center>"
            + "<center><BR>¿SEGURO QUE DESEA CONTINUAR?</BR></center>");
    private Dialogo diaRegistro = new Dialogo();
    private Grid gridre = new Grid();
    private Grid gridRe = new Grid();
    @EJB
    private cementerio admin = (cementerio) utilitario.instanciarEJB(cementerio.class);

    public pre_cambio_sitio() {

        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT IDE_CATASTRO,IDE_FALLECIDO,FECHA_INGRE,liquidacion,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,CATASTRO,FECHA_DESDE,FECHA_HASTA\n"
                + "FROM CMT_FALLECIDO_RENOVACION");
        autBusca.setMetodoChange("buscarPersona");
        autBusca.setSize(70);

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

        Grid griPri = new Grid();
        griPri.setColumns(1);
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
        setFallecido.setSeleccionTabla("SELECT IDE_CATASTRO,IDE_FALLECIDO,CATASTRO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,FECHA_HASTA\n"
                + "FROM dbo.CMT_FALLECIDO_RENOVACION where IDE_CATASTRO = -1", "IDE_CATASTRO");
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

        Boton botBusca = new Boton();
        botBusca.setValue("Busc. Fallecido");
        botBusca.setExcluirLectura(true);
        botBusca.setIcon("ui-icon-search");
        botBusca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(botBusca);

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("<CENTER>FALLECIDOS - CAMBIO DE LUGAR - CEMENTERIO MUNICIPAL </CENTER>");
        agregarComponente(panOpcion);

        cmbLugar.setId("cmbLugar");
        cmbLugar.setCombo("SELECT IDE_LUGAR, DETALLE_LUGAR  FROM CMT_LUGAR where cod_rubro = 'CMTR'");
        cmbLugar.setMetodo("busSector");

        cmbSector.setId("cmbSector");
        cmbSector.setCombo("SELECT DISTINCT SECTOR as ID, SECTOR FROM dbo.CMT_CATASTRO");
        cmbSector.setMetodo("busModulo");

        cmbModulo.setId("cmbModulo");
        cmbModulo.setCombo("SELECT DISTINCT MODULO as ID,MODULO FROM dbo.CMT_CATASTRO");
        cmbModulo.setMetodo("busNumero");

        cmbNumero.setId("cmbNumero");
        cmbNumero.setCombo("SELECT DISTINCT NUMERO as ID,NUMERO FROM CMT_CATASTRO");

        diaRegistro.setId("diaRegistro");
        diaRegistro.setWidth("20%"); //siempre en porcentajes  ancho
        diaRegistro.setHeight("20%");//siempre porcentaje   alto
        diaRegistro.setResizable(false); //para que no se pueda cambiar el tamaño
//        diaRegistro.getBot_aceptar().setMetodo("ejecutaCambio");
        gridre.setColumns(4);
        agregarComponente(diaRegistro);

        dibujarPantalla();
    }

    public void abrirBusqueda() {
        setFallecido.dibujar();
    }

    public void dibujarPantalla() {
        limpiarPanel();

        tabCatastro.setId("tabCatastro");
        tabCatastro.setTabla("cmt_catastro", "ide_catastro", 1);
        if (autBusca.getValue() == null) {
            tabCatastro.setCondicion("ide_catastro=-1");
        } else {
            tabCatastro.setCondicion("ide_catastro=" + autBusca.getValor());
        }
        tabCatastro.getColumna("IDE_LUGAR").setCombo("SELECT IDE_LUGAR,DETALLE_LUGAR FROM CMT_LUGAR");
        tabCatastro.agregarRelacion(tabFallecido);
        tabCatastro.getGrid().setColumns(4);
        tabCatastro.setTipoFormulario(true);
        tabCatastro.dibujar();
        PanelTabla pntCatas = new PanelTabla();
        pntCatas.setPanelTabla(tabCatastro);

        tabFallecido.setId("tabFallecido");
        tabFallecido.setTabla("cmt_fallecido", "ide_fallecido", 2);
        tabFallecido.getColumna("CEDULA_REPRESENTANTE").setVisible(false);
        tabFallecido.setRows(7);
        tabFallecido.dibujar();
        PanelTabla pntFalle = new PanelTabla();

        Grid griBusca = new Grid();
        griBusca.setColumns(10);

        Boton botCambio = new Boton();
        botCambio.setValue("Realiza Cambio");
        botCambio.setExcluirLectura(true);
        botCambio.setIcon("ui-icon-gear");
        botCambio.setMetodo("procesaCambio");
        griBusca.getChildren().add(etiLugar);
        griBusca.getChildren().add(cmbLugar);
        griBusca.getChildren().add(etiSector);
        griBusca.getChildren().add(cmbSector);
        griBusca.getChildren().add(etiModulo);
        griBusca.getChildren().add(cmbModulo);
        griBusca.getChildren().add(etiNumero);
        griBusca.getChildren().add(cmbNumero);
        griBusca.getChildren().add(botCambio);

        pntFalle.getChildren().add(griBusca);
        pntFalle.setPanelTabla(tabFallecido);

        tabMovimiento.setId("tabMovimiento");
        tabMovimiento.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 3);
        tabMovimiento.setCondicion("IDE_DET_MOVIMIENTO=-1");
        tabMovimiento.getColumna("IDE_TIPO_MOVIMIENTO").setCombo("SELECT IDE_TIPO_PAGO, DESCRIPCION FROM dbo.CMT_TIPO_PAGO");
        tabMovimiento.getGrid().setColumns(4);
        tabMovimiento.setTipoFormulario(true);
        tabMovimiento.dibujar();
        PanelTabla pntMovimiento = new PanelTabla();
        pntMovimiento.setMensajeWarn("DATOS DE ULTIMO MOVIMIENTO");
        pntMovimiento.setPanelTabla(tabMovimiento);

        Division div = new Division();
        div.setId("div");
//        div.dividir3(pntCatas, pntFalle, pntMovimiento, "30%", "43%", "H");
        div.dividir2(pntCatas, pntFalle, "23%", "H");
        agregarComponente(div);

        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);

    }

    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    public void buscaNombre() {
        if (txtBusqueda.getValue() != null && txtBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("select * \n"
                    + "from (select IDE_CATASTRO,IDE_FALLECIDO,CATASTRO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,FECHA_HASTA\n"
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
                    + "from (select IDE_CATASTRO,IDE_FALLECIDO,CATASTRO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,FECHA_HASTA\n"
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
                    + "from (select IDE_CATASTRO,IDE_FALLECIDO,CATASTRO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,FECHA_HASTA\n"
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
                    + "from (select IDE_CATASTRO,IDE_FALLECIDO,CATASTRO,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,liquidacion,FECHA_HASTA\n"
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
        if (setFallecido.getValorSeleccionado() != null) {
            TablaGenerica tabInfo = admin.consultaFallecido1(Integer.parseInt(setFallecido.getValorSeleccionado() + ""));
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
        datosMovimiento();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
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

    public void busSector() {
        cmbSector.setCombo("SELECT distinct SECTOR as id,SECTOR FROM CMT_CATASTRO where IDE_LUGAR=" + cmbLugar.getValue() + " order by SECTOR");
        utilitario.addUpdate("cmbSector");
    }

    public void busModulo() {
        cmbModulo.setCombo("SELECT distinct MODULO as id,MODULO FROM CMT_CATASTRO where IDE_LUGAR=" + cmbLugar.getValue() + " and SECTOR = " + cmbSector.getValue() + "  order by MODULO");
        utilitario.addUpdate("cmbModulo");
    }

    public void busNumero() {
        cmbNumero.setCombo("SELECT distinct NUMERO as id,NUMERO FROM CMT_CATASTRO where IDE_LUGAR=" + cmbLugar.getValue() + " and SECTOR = " + cmbSector.getValue() + " and MODULO = '" + cmbModulo.getValue() + "'  order by NUMERO");
        utilitario.addUpdate("cmbNumero");
    }

    public void procesaCambio() {
        TablaGenerica tabDato = admin.getDatoCatastro(cmbLugar.getValue() + "", cmbSector.getValue() + "", cmbNumero.getValue() + "", cmbModulo.getValue() + "");
        if (!tabDato.isEmpty()) {
            if (tabDato.getValor("disponible_ocupado").equals("OCUPADO")) {
                diaRegistro.Limpiar();
                diaRegistro.setDialogo(gridre);
                Grid griForma = new Grid();
                griForma.setColumns(1);
                griForma.getChildren().add(etiAdvertencia);
                gridRe.getChildren().add(griForma);
                diaRegistro.setDialogo(gridRe);
                diaRegistro.dibujar();
            } else {
                ejecutaCambio();
            }
        }
    }

    public void ejecutaCambio() {
        diaRegistro.cerrar();
        if (fechas() <= 0) {
            TablaGenerica tabDato = admin.getDatoCatastro(cmbLugar.getValue() + "", cmbSector.getValue() + "", cmbNumero.getValue() + "", cmbModulo.getValue() + "");
            if (!tabDato.isEmpty()) {
                admin.setInserDetalle_SP(cmbSector.getValue() + "", cmbModulo.getValue() + "", cmbNumero.getValue() + "", Integer.parseInt(cmbLugar.getValue() + ""), tabConsulta.getValor("NICK_USUA"), Integer.parseInt(tabMovimiento.getValor("IDE_DET_MOVIMIENTO") + ""));
                for (int i = 0; i < tabFallecido.getTotalFilas(); i++) {
                    admin.setUpdateCambioFallecido(tabConsulta.getValor("NICK_USUA"), Integer.parseInt(tabFallecido.getValor(i, "ide_fallecido")), Integer.parseInt(cmbLugar.getValue() + ""), cmbSector.getValue() + "", cmbModulo.getValue() + "", cmbNumero.getValue() + "", Integer.parseInt(tabCatastro.getValor("ide_catastro")));
                }
                admin.setUpdateLiberaCatastro(Integer.parseInt(tabCatastro.getValor("ide_catastro")), tabConsulta.getValor("NICK_USUA"));
                admin.setUpdatePuestoHabilita(Integer.parseInt(cmbLugar.getValue() + ""),cmbSector.getValue() + "",cmbModulo.getValue() + "",cmbNumero.getValue() + "",tabConsulta.getValor("NICK_USUA"));
                utilitario.agregarMensaje("Registros Cambiados de Ubicacion", null);
            }
        } else {
            utilitario.agregarMensaje("Lugar; se encuentra con retraso", null);
        }
    }

    public int fechas() {
        int valor = 0;
        Date fecha = utilitario.DeStringADateformato2(tabMovimiento.getValor("fecha_hasta"));
        Calendar fecha1 = null;
        try {
            System.err.println("" + fecha);
            fecha1 = Calendar.getInstance();
            fecha1.setTime(fecha);
        } catch (Exception e) {
        }

        Calendar fecha2 = GregorianCalendar.getInstance();
        fecha2.set(Calendar.DAY_OF_MONTH, fecha2.get(Calendar.DAY_OF_MONTH) + 1);
        System.out.println(fecha1.getTime());
        System.out.println(fecha2.getTime());
        switch (fecha2.compareTo(fecha1)) {
            case 1:
                valor = 1;
                System.out.println("La fecha2 es mayor");
                break;
            case 0:
                valor = 0;
                System.out.println("Las fechas son iguales");
                break;
            case -1:
                valor = -1;
                System.out.println("La fecha2 es menor");
                break;
        }
        return valor;
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
                    + "where IDE_FALLECIDO =(select top 1 IDE_FALLECIDO from CMT_FALLECIDO where IDE_CATASTRO = " + Integer.parseInt(setFallecido.getValorSeleccionado()) + " order by FECHA_DEFUNCION desc))";
        }
        return str_filtros;
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

    public Tabla getTabFallecido() {
        return tabFallecido;
    }

    public void setTabFallecido(Tabla tabFallecido) {
        this.tabFallecido = tabFallecido;
    }

    public Tabla getTabMovimiento() {
        return tabMovimiento;
    }

    public void setTabMovimiento(Tabla tabMovimiento) {
        this.tabMovimiento = tabMovimiento;
    }

    public Tabla getTabCatastro() {
        return tabCatastro;
    }

    public void setTabCatastro(Tabla tabCatastro) {
        this.tabCatastro = tabCatastro;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

    public SeleccionTabla getSetFallecido() {
        return setFallecido;
    }

    public void setSetFallecido(SeleccionTabla setFallecido) {
        this.setFallecido = setFallecido;
    }
}
