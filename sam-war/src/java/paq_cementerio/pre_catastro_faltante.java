/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.ejb.EJB;
import paq_cementerio.ejb.cementerio;
import sistema.aplicacion.Pantalla;
import paq_webservice.ClassCiudadania;

/**
 *
 * @author p-chumana
 */
public class pre_catastro_faltante extends Pantalla {

    private Combo cmbLugar = new Combo();
    private Combo cmbSector = new Combo();
    private Combo cmbModulo = new Combo();
    private Combo cmbNumero = new Combo();
    private Tabla tabFallecido = new Tabla();
    private Tabla tabLevantamiento = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Panel panOpcion = new Panel();
    private Dialogo diaRegistro = new Dialogo();
    private Grid gridre = new Grid();
    private Grid gridRe = new Grid();
    private Dialogo diaEliminar = new Dialogo();
    private Grid gridel = new Grid();
    private Grid gridEl = new Grid();
    private Etiqueta etiCedula = new Etiqueta("<BR>Cedula : </BR>");
    private Texto txtCedula = new Texto();
    private Etiqueta etiApellido1 = new Etiqueta("<BR>Primer Apellido : </BR>");
    private Texto txtApellido1 = new Texto();
    private Etiqueta etiApellido2 = new Etiqueta("<BR>Segundo Apellido : </BR>");
    private Texto txtApellido2 = new Texto();
    private Etiqueta etiNombre1 = new Etiqueta("<BR>Primer Nombre : </BR>");
    private Texto txtNombre1 = new Texto();
    private Etiqueta etiNombre2 = new Etiqueta("<BR>Segundo Nombre : </BR>");
    private Texto txtNombre2 = new Texto();
    private Etiqueta etiFNacimiento = new Etiqueta("<BR>Fecha de Nacimiento : </BR>");
    private Calendario calFNacimiento = new Calendario();
    private Etiqueta etiFDefuncion = new Etiqueta("<BR>Fecha de Defunción : </BR>");
    private Etiqueta etiEliminar = new Etiqueta("<center><BR>Se eliminaran todos los registros anexados al sitio </BR></center>");
    private Etiqueta etiConfirma = new Etiqueta("<center><BR>¿Seguro De Continuar?</BR></center>");
    private Calendario calFDefuncion = new Calendario();
    @EJB
    private cementerio adminCementerio = (cementerio) utilitario.instanciarEJB(cementerio.class);

    public pre_catastro_faltante() {
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();


        bar_botones.agregarComponente(new Etiqueta("Lugar"));
        bar_botones.agregarComponente(cmbLugar);
        bar_botones.agregarSeparador();
        bar_botones.agregarComponente(new Etiqueta("Sector"));
        bar_botones.agregarComponente(cmbSector);
        bar_botones.agregarSeparador();
        bar_botones.agregarComponente(new Etiqueta("Modulo"));
        bar_botones.agregarComponente(cmbModulo);
        bar_botones.agregarSeparador();
        bar_botones.agregarComponente(new Etiqueta("Número"));
        bar_botones.agregarComponente(cmbNumero);
        bar_botones.agregarSeparador();
        Boton btnBuscar = new Boton();
        btnBuscar.setValue("Buscar Información");
        btnBuscar.setIcon("ui-icon-search");
        btnBuscar.setMetodo("fitraInformacion");
        bar_botones.agregarBoton(btnBuscar);

        Boton btnQuitar = new Boton();
        btnQuitar.setValue("Eliminar");
        btnQuitar.setIcon("ui-icon-cancel");
        btnQuitar.setMetodo("mensajeLiberacion");
        bar_botones.agregarBoton(btnQuitar);

        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

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

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("<center>INGRESO DE FALLECIDO MULTIPLES - MIGRACIÓN</center>");
        agregarComponente(panOpcion);

        dibujarPantalla();

        diaRegistro.setId("diaRegistro");
        diaRegistro.setTitle("REGISTRO DE CATASTROS FALTANTES"); //titulo
//        diaRegistro.setWidth("50%"); //siempre en porcentajes  ancho
//        diaRegistro.setHeight("50%");//siempre porcentaje   alto
        diaRegistro.setResizable(false); //para que no se pueda cambiar el tamaño
        diaRegistro.getBot_aceptar().setMetodo("guardaRegistro");
        diaRegistro.getBot_cancelar().setMetodo("regresaForma");
        gridre.setColumns(4);
        agregarComponente(diaRegistro);


        diaEliminar.setId("diaEliminar");
        diaEliminar.setTitle("OPCIÓN ELIMINAR REGISTROS"); //titulo
        diaEliminar.setWidth("20%"); //siempre en porcentajes  ancho
        diaEliminar.setHeight("20%");//siempre porcentaje   alto
        diaEliminar.setResizable(false); //para que no se pueda cambiar el tamaño
        diaEliminar.getBot_aceptar().setMetodo("liberarInformacion");
        gridel.setColumns(4);
        agregarComponente(diaEliminar);
    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabFallecido.setId("tabFallecido");
        tabFallecido.setTabla("CMT_FALLECIDO", "IDE_FALLECIDO", 1);
        tabFallecido.setCondicion("IDE_FALLECIDO=-1");
        tabFallecido.getColumna("IDE_CATASTRO").setCombo("SELECT IDE_CATASTRO, (select DETALLE_LUGAR from CMT_LUGAR where IDE_LUGAR=dbo.CMT_CATASTRO.IDE_LUGAR) + ', ' +\n"
                + "+ 'Sector : ' +SECTOR+ ', ' +\n"
                + "+ 'Modulo :  ' +MODULO+ ', ' +\n"
                + "+ 'Lugar :  ' +NUMERO as Lugar\n"
                + "FROM CMT_CATASTRO");
//        tabFallecido.getColumna("IDE_CATASTRO").setLongitud(70);
//        tabFallecido.getColumna("NOMBRES").setLongitud(70);
        tabFallecido.getColumna("IDE_FALLECIDO").setVisible(false);
        tabFallecido.getColumna("IDE_CMGEN").setVisible(false);
        tabFallecido.getColumna("USUARIO_INGRE").setVisible(false);
        tabFallecido.getColumna("FECHA_INGRE").setVisible(false);
        tabFallecido.getColumna("HORA_INGRE").setVisible(false);
        tabFallecido.getColumna("USUARIO_ACTUA").setVisible(false);
        tabFallecido.getColumna("FECHA_ACTUA").setVisible(false);
        tabFallecido.getColumna("HORA_ACTUA").setVisible(false);
        tabFallecido.getColumna("CERTIFICADO_DEFUN").setVisible(false);
        tabFallecido.getColumna("FECHA_DOCUMENTO_CMARE").setVisible(false);
        tabFallecido.getColumna("cedula_representante").setVisible(false);
        tabFallecido.getColumna("representante_actual").setVisible(false);
        tabFallecido.getColumna("tipo_pago").setVisible(false);
        tabFallecido.getColumna("TIPO_FALLECIDO").setVisible(false);
//        tabFallecido.getColumna("IDE_RENTAS_FALLECIDO").setVisible(false);
        tabFallecido.setRows(6);
        tabFallecido.dibujar();
        PanelTabla patFallecido = new PanelTabla();
        patFallecido.setMensajeWarn("DATOS DE FALLECIDO - SISTEMA ACTUAL");
        patFallecido.setPanelTabla(tabFallecido);

        tabLevantamiento.setId("tabLevantamiento");
        tabLevantamiento.setTabla("CMT_MIGRACION_DATOS", "cod_tabla", 2);
        tabLevantamiento.setCondicion("cod_tabla=-1");
        tabLevantamiento.getColumna("cod_tabla").setVisible(false);
        tabLevantamiento.getColumna("ren_ide_fallecido").setVisible(false);
        tabLevantamiento.getColumna("ex_cedula").setVisible(false);
        tabLevantamiento.getColumna("cod_tabla").setVisible(false);
        tabLevantamiento.getColumna("cod_tabla").setVisible(false);
        tabLevantamiento.getColumna("cod_tabla").setVisible(false);
        tabLevantamiento.getColumna("nuevo_ubicacion").setVisible(false);
        tabLevantamiento.getColumna("observacion").setVisible(false);
        tabLevantamiento.getColumna("bloqueo").setVisible(false);
        tabLevantamiento.getColumna("USUARIO_INGRE").setVisible(false);
        tabLevantamiento.getColumna("FECHA_INGRE").setVisible(false);
        tabLevantamiento.getColumna("HORA_INGRE").setVisible(false);
        tabLevantamiento.getColumna("USUARIO_ACTUA").setVisible(false);
        tabLevantamiento.getColumna("FECHA_ACTUA").setVisible(false);
        tabLevantamiento.getColumna("HORA_ACTUA").setVisible(false);
        tabLevantamiento.getColumna("ex_piso").setVisible(false);
        tabLevantamiento.getColumna("verifica").setCheck();
        tabLevantamiento.getColumna("verifica").setMetodoChange("ingresoDatos");
        tabLevantamiento.setRows(5);
        tabLevantamiento.dibujar();
        PanelTabla patExcel = new PanelTabla();
        patExcel.setMensajeWarn("DATOS DE REPRESENTANTE - LEVANTAMIENTO");
        patExcel.setPanelTabla(tabLevantamiento);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(patFallecido, patExcel, "43%", "H");
        agregarComponente(div_division);

        Grupo gru = new Grupo();
        gru.getChildren().add(div_division);
        panOpcion.getChildren().add(gru);
    }

    private void limpiarPanel() {
        panOpcion.getChildren().clear();
        utilitario.addUpdate("panOpcion");
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

    public void fitraInformacion() {
        actualizaFallecido();
        actualizaLevantamiento();
    }

    public void actualizaFallecido() {
        if (!getFiltroAcceso().isEmpty()) {
            tabFallecido.setCondicion(getFiltroAcceso());
            tabFallecido.ejecutarSql();
            utilitario.addUpdate("tabFallecido");
        }
    }

    private String getFiltroAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "IDE_CATASTRO = (SELECT IDE_CATASTRO FROM CMT_CATASTRO where IDE_LUGAR=" + cmbLugar.getValue() + "  and sector =" + cmbSector.getValue() + " "
                + "and modulo='" + cmbModulo.getValue() + "' and NUMERO =" + cmbNumero.getValue() + ")";
        return str_filtros;
    }

    public void actualizaLevantamiento() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabLevantamiento.setCondicion(getFiltrosAcceso());
            tabLevantamiento.ejecutarSql();
            utilitario.addUpdate("tabLevantamiento");
        }
    }

    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "nuevo_bloque =" + cmbSector.getValue() + " and nuevo_numero ='" + cmbModulo.getValue() + "' and nuevo_fila= " + cmbNumero.getValue() + " "
                + "and ex_lugar = (select DETALLE_LUGAR from CMT_LUGAR where IDE_LUGAR = " + cmbLugar.getValue() + ") and BLOQUEO is null";
        return str_filtros;
    }

    public void regresaForma() {
        tabLevantamiento.setValor("verifica", null);
        utilitario.addUpdate("tabLevantamiento");
        diaRegistro.cerrar();
    }

    public void ingresoDatos() throws ParseException {
        System.err.println(tabLevantamiento.getValor("cod_tabla"));
        TablaGenerica tabDato = adminCementerio.insertCatastroNuevo(Integer.parseInt(tabLevantamiento.getValor("cod_tabla")));
        if (!tabDato.isEmpty()) {
            diaRegistro.Limpiar();
            diaRegistro.setDialogo(gridre);
            Grid griForma = new Grid();
            griForma.setColumns(1);

            Boton btnCedula = new Boton();
            btnCedula.setValue("Buscar");
            btnCedula.setIcon("ui-icon-search");
            btnCedula.setMetodo("buscarCedula");

            Grid griMostrar = new Grid();
            griMostrar.setColumns(3);
            griMostrar.getChildren().add(etiCedula);
            griMostrar.getChildren().add(txtCedula);
//            griMostrar.getChildren().add(btnCedula);

            Grid griApel = new Grid();
            griApel.setColumns(4);
            txtApellido1.setSize(35);
            txtApellido2.setSize(35);
            txtNombre1.setSize(35);
            txtNombre2.setSize(35);
            griApel.getChildren().add(etiApellido1);
            griApel.getChildren().add(txtApellido1);
            griApel.getChildren().add(etiApellido2);
            griApel.getChildren().add(txtApellido2);
            griApel.getChildren().add(etiNombre1);
            griApel.getChildren().add(txtNombre1);
            griApel.getChildren().add(etiNombre2);
            griApel.getChildren().add(txtNombre2);

            Grid griFecha = new Grid();
            griFecha.setColumns(2);
            griFecha.getChildren().add(etiFNacimiento);
            griFecha.getChildren().add(calFNacimiento);
            griFecha.getChildren().add(etiFDefuncion);
            griFecha.getChildren().add(calFDefuncion);

            griForma.getChildren().add(griMostrar);
            griForma.getChildren().add(griApel);
            griForma.getChildren().add(griFecha);

            gridRe.getChildren().add(griForma);
            diaRegistro.setDialogo(gridRe);
            diaRegistro.dibujar();

            txtNombre1.setValue(null);
            txtApellido1.setValue(null);
            txtNombre2.setValue(null);
            txtApellido2.setValue(null);
            calFNacimiento.setValue(null);
            calFDefuncion.setValue(null);
            txtCedula.setValue(null);

            txtCedula.setValue("0000000000");
            txtNombre1.setValue(tabLevantamiento.getValor("ex_nombres"));
            txtApellido1.setValue(tabLevantamiento.getValor("ex_nombres"));

            utilitario.addUpdate("txtNombre1");
            utilitario.addUpdate("txtApellido1");
            utilitario.addUpdate("txtNombre2");
            utilitario.addUpdate("txtApellido2");
            utilitario.addUpdate("calFNacimiento");
            utilitario.addUpdate("calFDefuncion");
            utilitario.addUpdate("txtCedula");
        } else {
            utilitario.agregarMensajeInfo("Error en información seleccionada", "");
            tabLevantamiento.setValor("verifica", null);
            utilitario.addUpdate("tabLevantamiento");
        }
    }

    public void buscarCedula() {
        String usu, pass, cedula, nombre, direccion, telefono, correo, lugar;
        System.err.println(" " + txtCedula.getValue() + "");
        if (utilitario.validarCedula(txtCedula.getValue() + "")) {
            usu = "cementerio";
            pass = "cmtr2016$";

            cedula = txtCedula.getValue() + "";
            paq_webservice.ConsultaCiudadano service = new paq_webservice.ConsultaCiudadano();
            ClassCiudadania persona = service.getConsultaCiudadanoSoap().busquedaPorCedula(cedula, usu, pass);
            utilitario.agregarMensaje("Nombre : " + persona.getNombre(),
                    "F. Fallecido" + persona.getFechaFallecido() + " <-->   F. Nacimiento" + persona.getFechaNacimiento());

        } else {
            utilitario.agregarMensaje("Número de Cédula Incorrecto", null);
        }
    }

    public void guardaRegistro() {
        String nomb = "", nomb2 = "", apell = "", apell2 = "", nombre = "", str_defuncion = "", str_nacimiento = "", cedula = "";
        TablaGenerica tabDato = adminCementerio.setCatastroID(Integer.parseInt(cmbLugar.getValue() + ""), cmbSector.getValue() + "",
                cmbModulo.getValue() + "", Integer.parseInt(cmbNumero.getValue() + ""));
        if (!tabDato.isEmpty()) {
            if (tabFallecido.getTotalFilas() > 0) {
                if (txtNombre1.getValue() != null && !txtNombre1.getValue().toString().isEmpty()) {
                    nomb = String.valueOf(txtNombre1.getValue()) + " ";
                } else {
                    nomb = "";
                }
                if (txtNombre2.getValue() != null && !txtNombre2.getValue().toString().isEmpty()) {
                    nomb2 = String.valueOf(txtNombre2.getValue()) + " ";
                } else {
                    nomb2 = "";
                }
                if (txtApellido1.getValue() != null && !txtApellido1.getValue().toString().isEmpty()) {
                    apell = String.valueOf(txtApellido1.getValue()) + " ";
                } else {
                    apell = "";
                }
                if (txtApellido2.getValue() != null && !txtApellido2.getValue().toString().isEmpty()) {
                    apell2 = String.valueOf(txtApellido2.getValue()) + " ";
                } else {
                    apell2 = "";
                }

//            if (txtCedula.getValue() != null && !txtCedula.getValue().toString().isEmpty()) {
                cedula = "'" + String.valueOf(txtCedula.getValue()) + "'";
//            } else {
//                cedula = "'0000000000'";
//            }

                nombre = apell + "" + apell2 + nomb + "" + nomb2;
                System.err.println(nombre + "nombre ---");


                if (calFDefuncion.getFecha() != null && !calFDefuncion.getFecha().toString().isEmpty()) {
                    Date fDefuncion = utilitario.DeStringADateformato2(calFDefuncion.getFecha());
                    str_defuncion = "'" + utilitario.DeDateAStringformato2(fDefuncion) + "'";
                } else {
                    str_defuncion = null;
                }

                if (calFNacimiento.getFecha() != null && !calFNacimiento.getFecha().toString().isEmpty()) {
                    Date fNacimiento = utilitario.DeStringADateformato2(calFNacimiento.getFecha());
                    str_nacimiento = "'" + utilitario.DeDateAStringformato2(fNacimiento) + "'";
                } else {
                    str_nacimiento = null;
                }

                System.err.println(str_defuncion);
                adminCementerio.CMT_FALLECIDOS_INSERT_FALTANTE(cedula, nombre.toUpperCase(), str_defuncion + "", str_nacimiento + "", tabConsulta.getValor("NICK_USUA"), Integer.parseInt(tabDato.getValor("IDE_CATASTRO")), tabDato.getValor("catastro_anterior"));
                adminCementerio.set_updateBloqueExcel(Integer.parseInt(tabLevantamiento.getValor("cod_tabla")));
                utilitario.agregarMensajeInfo("Registro Guardado", "");
                diaRegistro.cerrar();
                fitraInformacion();
            } else {
                utilitario.agregarMensajeInfo("No puede ingresar información", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Catastro seleccionado no disponible", "");
        }
    }

    public void mensajeLiberacion() {

        diaEliminar.Limpiar();
        diaEliminar.setDialogo(gridel);
        Grid griForma = new Grid();
        griForma.setColumns(1);
        griForma.getChildren().add(etiEliminar);
        griForma.getChildren().add(etiConfirma);
        gridEl.getChildren().add(griForma);
        diaEliminar.setDialogo(gridEl);
        diaEliminar.dibujar();

    }

    public void liberarInformacion() {
        try {
            TablaGenerica tabDato = adminCementerio.getDatoCatastro(cmbLugar.getValue() + "", cmbSector.getValue() + "", cmbNumero.getValue() + "", cmbModulo.getValue() + "");
            if (!tabDato.isEmpty()) {
                adminCementerio.setQuitarFallecido_SP(Integer.parseInt(cmbLugar.getValue() + ""), cmbSector.getValue() + "", cmbModulo.getValue() + "", cmbNumero.getValue() + "", tabConsulta.getValor("NICK_USUA"));
                utilitario.agregarMensaje("Registros Eliminados Con Exito", "");
                diaEliminar.cerrar();
                fitraInformacion();
            } else {
                utilitario.agregarMensaje("No existen datos", "");
            }
        } catch (Exception e) {
            utilitario.agregarMensaje("Existe un error de datos" + e, "");
        }
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

    public Tabla getTabLevantamiento() {
        return tabLevantamiento;
    }

    public void setTabLevantamiento(Tabla tabLevantamiento) {
        this.tabLevantamiento = tabLevantamiento;
    }

    private static ClassCiudadania busquedaCedulaActual(java.lang.String cedula, java.lang.String usuario, java.lang.String password) {
        paq_webservice.ConsultaCiudadano service = new paq_webservice.ConsultaCiudadano();
        paq_webservice.ConsultaCiudadanoSoap port = service.getConsultaCiudadanoSoap();
        return port.busquedaCedulaActual(cedula, usuario, password);
    }
}
