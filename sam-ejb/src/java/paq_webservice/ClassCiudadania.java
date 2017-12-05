
package paq_webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ClassCiudadania complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ClassCiudadania">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Error" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Cedula" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Sexo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Condicion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FechaNacimiento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EstadoCivil" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Direccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DireccionTrabajo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NumeroCasa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FechaFallecido" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Telefono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TelefonoTrabajo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Mail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MailTrabajo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LugarTrabajo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Observaciones" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RazonSocial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NombreComercial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ActividadEconomica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SitioWeb" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Fax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Pais" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FechaIngresoSistema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CondicionCedulado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Nacionalidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Conyuge" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Instruccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Profesion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FechaCedulacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClassCiudadania", propOrder = {
    "codError",
    "error",
    "cedula",
    "nombre",
    "sexo",
    "condicion",
    "fechaNacimiento",
    "estadoCivil",
    "direccion",
    "direccionTrabajo",
    "numeroCasa",
    "fechaFallecido",
    "telefono",
    "telefonoTrabajo",
    "mail",
    "mailTrabajo",
    "lugarTrabajo",
    "observaciones",
    "razonSocial",
    "nombreComercial",
    "actividadEconomica",
    "sitioWeb",
    "fax",
    "pais",
    "fechaIngresoSistema",
    "condicionCedulado",
    "nacionalidad",
    "conyuge",
    "instruccion",
    "profesion",
    "fechaCedulacion"
})
public class ClassCiudadania {

    protected String codError;
    @XmlElement(name = "Error")
    protected String error;
    @XmlElement(name = "Cedula")
    protected String cedula;
    @XmlElement(name = "Nombre")
    protected String nombre;
    @XmlElement(name = "Sexo")
    protected String sexo;
    @XmlElement(name = "Condicion")
    protected String condicion;
    @XmlElement(name = "FechaNacimiento")
    protected String fechaNacimiento;
    @XmlElement(name = "EstadoCivil")
    protected String estadoCivil;
    @XmlElement(name = "Direccion")
    protected String direccion;
    @XmlElement(name = "DireccionTrabajo")
    protected String direccionTrabajo;
    @XmlElement(name = "NumeroCasa")
    protected String numeroCasa;
    @XmlElement(name = "FechaFallecido")
    protected String fechaFallecido;
    @XmlElement(name = "Telefono")
    protected String telefono;
    @XmlElement(name = "TelefonoTrabajo")
    protected String telefonoTrabajo;
    @XmlElement(name = "Mail")
    protected String mail;
    @XmlElement(name = "MailTrabajo")
    protected String mailTrabajo;
    @XmlElement(name = "LugarTrabajo")
    protected String lugarTrabajo;
    @XmlElement(name = "Observaciones")
    protected String observaciones;
    @XmlElement(name = "RazonSocial")
    protected String razonSocial;
    @XmlElement(name = "NombreComercial")
    protected String nombreComercial;
    @XmlElement(name = "ActividadEconomica")
    protected String actividadEconomica;
    @XmlElement(name = "SitioWeb")
    protected String sitioWeb;
    @XmlElement(name = "Fax")
    protected String fax;
    @XmlElement(name = "Pais")
    protected String pais;
    @XmlElement(name = "FechaIngresoSistema")
    protected String fechaIngresoSistema;
    @XmlElement(name = "CondicionCedulado")
    protected String condicionCedulado;
    @XmlElement(name = "Nacionalidad")
    protected String nacionalidad;
    @XmlElement(name = "Conyuge")
    protected String conyuge;
    @XmlElement(name = "Instruccion")
    protected String instruccion;
    @XmlElement(name = "Profesion")
    protected String profesion;
    @XmlElement(name = "FechaCedulacion")
    protected String fechaCedulacion;

    /**
     * Obtiene el valor de la propiedad codError.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodError() {
        return codError;
    }

    /**
     * Define el valor de la propiedad codError.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodError(String value) {
        this.codError = value;
    }

    /**
     * Obtiene el valor de la propiedad error.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getError() {
        return error;
    }

    /**
     * Define el valor de la propiedad error.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setError(String value) {
        this.error = value;
    }

    /**
     * Obtiene el valor de la propiedad cedula.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCedula() {
        return cedula;
    }

    /**
     * Define el valor de la propiedad cedula.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCedula(String value) {
        this.cedula = value;
    }

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad sexo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * Define el valor de la propiedad sexo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSexo(String value) {
        this.sexo = value;
    }

    /**
     * Obtiene el valor de la propiedad condicion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCondicion() {
        return condicion;
    }

    /**
     * Define el valor de la propiedad condicion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCondicion(String value) {
        this.condicion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaNacimiento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Define el valor de la propiedad fechaNacimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaNacimiento(String value) {
        this.fechaNacimiento = value;
    }

    /**
     * Obtiene el valor de la propiedad estadoCivil.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoCivil() {
        return estadoCivil;
    }

    /**
     * Define el valor de la propiedad estadoCivil.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoCivil(String value) {
        this.estadoCivil = value;
    }

    /**
     * Obtiene el valor de la propiedad direccion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Define el valor de la propiedad direccion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccion(String value) {
        this.direccion = value;
    }

    /**
     * Obtiene el valor de la propiedad direccionTrabajo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccionTrabajo() {
        return direccionTrabajo;
    }

    /**
     * Define el valor de la propiedad direccionTrabajo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccionTrabajo(String value) {
        this.direccionTrabajo = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroCasa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroCasa() {
        return numeroCasa;
    }

    /**
     * Define el valor de la propiedad numeroCasa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroCasa(String value) {
        this.numeroCasa = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaFallecido.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaFallecido() {
        return fechaFallecido;
    }

    /**
     * Define el valor de la propiedad fechaFallecido.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaFallecido(String value) {
        this.fechaFallecido = value;
    }

    /**
     * Obtiene el valor de la propiedad telefono.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Define el valor de la propiedad telefono.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefono(String value) {
        this.telefono = value;
    }

    /**
     * Obtiene el valor de la propiedad telefonoTrabajo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefonoTrabajo() {
        return telefonoTrabajo;
    }

    /**
     * Define el valor de la propiedad telefonoTrabajo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefonoTrabajo(String value) {
        this.telefonoTrabajo = value;
    }

    /**
     * Obtiene el valor de la propiedad mail.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMail() {
        return mail;
    }

    /**
     * Define el valor de la propiedad mail.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMail(String value) {
        this.mail = value;
    }

    /**
     * Obtiene el valor de la propiedad mailTrabajo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailTrabajo() {
        return mailTrabajo;
    }

    /**
     * Define el valor de la propiedad mailTrabajo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailTrabajo(String value) {
        this.mailTrabajo = value;
    }

    /**
     * Obtiene el valor de la propiedad lugarTrabajo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLugarTrabajo() {
        return lugarTrabajo;
    }

    /**
     * Define el valor de la propiedad lugarTrabajo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLugarTrabajo(String value) {
        this.lugarTrabajo = value;
    }

    /**
     * Obtiene el valor de la propiedad observaciones.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Define el valor de la propiedad observaciones.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservaciones(String value) {
        this.observaciones = value;
    }

    /**
     * Obtiene el valor de la propiedad razonSocial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Define el valor de la propiedad razonSocial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonSocial(String value) {
        this.razonSocial = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreComercial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreComercial() {
        return nombreComercial;
    }

    /**
     * Define el valor de la propiedad nombreComercial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreComercial(String value) {
        this.nombreComercial = value;
    }

    /**
     * Obtiene el valor de la propiedad actividadEconomica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActividadEconomica() {
        return actividadEconomica;
    }

    /**
     * Define el valor de la propiedad actividadEconomica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActividadEconomica(String value) {
        this.actividadEconomica = value;
    }

    /**
     * Obtiene el valor de la propiedad sitioWeb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSitioWeb() {
        return sitioWeb;
    }

    /**
     * Define el valor de la propiedad sitioWeb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSitioWeb(String value) {
        this.sitioWeb = value;
    }

    /**
     * Obtiene el valor de la propiedad fax.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFax() {
        return fax;
    }

    /**
     * Define el valor de la propiedad fax.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFax(String value) {
        this.fax = value;
    }

    /**
     * Obtiene el valor de la propiedad pais.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPais() {
        return pais;
    }

    /**
     * Define el valor de la propiedad pais.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPais(String value) {
        this.pais = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaIngresoSistema.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaIngresoSistema() {
        return fechaIngresoSistema;
    }

    /**
     * Define el valor de la propiedad fechaIngresoSistema.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaIngresoSistema(String value) {
        this.fechaIngresoSistema = value;
    }

    /**
     * Obtiene el valor de la propiedad condicionCedulado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCondicionCedulado() {
        return condicionCedulado;
    }

    /**
     * Define el valor de la propiedad condicionCedulado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCondicionCedulado(String value) {
        this.condicionCedulado = value;
    }

    /**
     * Obtiene el valor de la propiedad nacionalidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNacionalidad() {
        return nacionalidad;
    }

    /**
     * Define el valor de la propiedad nacionalidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNacionalidad(String value) {
        this.nacionalidad = value;
    }

    /**
     * Obtiene el valor de la propiedad conyuge.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConyuge() {
        return conyuge;
    }

    /**
     * Define el valor de la propiedad conyuge.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConyuge(String value) {
        this.conyuge = value;
    }

    /**
     * Obtiene el valor de la propiedad instruccion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstruccion() {
        return instruccion;
    }

    /**
     * Define el valor de la propiedad instruccion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstruccion(String value) {
        this.instruccion = value;
    }

    /**
     * Obtiene el valor de la propiedad profesion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfesion() {
        return profesion;
    }

    /**
     * Define el valor de la propiedad profesion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfesion(String value) {
        this.profesion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaCedulacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaCedulacion() {
        return fechaCedulacion;
    }

    /**
     * Define el valor de la propiedad fechaCedulacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaCedulacion(String value) {
        this.fechaCedulacion = value;
    }

}
