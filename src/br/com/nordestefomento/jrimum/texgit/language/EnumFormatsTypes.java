//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.0 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.08.03 at 12:27:56 AM GMT-03:00 
//

package br.com.nordestefomento.jrimum.texgit.language;

import javax.xml.bind.annotation.XmlEnum;

/**
 * 
 * 
 * <p>
 * Tipo de formatadores suportados. Usados junto a defini��o de novos
 * formatadores.
 * </p>
 * 
 * 
 * 
 * <p>
 * Java class for enumFormatsTypes.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name=&quot;enumFormatsTypes&quot;&gt;
 *   &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *     &lt;enumeration value=&quot;DATE&quot;/&gt;
 *     &lt;enumeration value=&quot;DECIMAL&quot;/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlEnum
public enum EnumFormatsTypes {

	DATE, DECIMAL;

	public String value() {
		return name();
	}

	public static EnumFormatsTypes fromValue(String v) {
		return valueOf(v);
	}

}
