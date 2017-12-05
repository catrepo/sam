
package paq_webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BusquedaPorCedulaResult" type="{http://ruminahui.gob.ec/}ClassCiudadania" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "busquedaPorCedulaResult"
})
@XmlRootElement(name = "BusquedaPorCedulaResponse")
public class BusquedaPorCedulaResponse {

    @XmlElement(name = "BusquedaPorCedulaResult")
    protected ClassCiudadania busquedaPorCedulaResult;

    /**
     * Obtiene el valor de la propiedad busquedaPorCedulaResult.
     * 
     * @return
     *     possible object is
     *     {@link ClassCiudadania }
     *     
     */
    public ClassCiudadania getBusquedaPorCedulaResult() {
        return busquedaPorCedulaResult;
    }

    /**
     * Define el valor de la propiedad busquedaPorCedulaResult.
     * 
     * @param value
     *     allowed object is
     *     {@link ClassCiudadania }
     *     
     */
    public void setBusquedaPorCedulaResult(ClassCiudadania value) {
        this.busquedaPorCedulaResult = value;
    }

}
