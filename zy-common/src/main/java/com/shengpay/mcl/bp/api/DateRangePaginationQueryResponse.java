
package com.shengpay.mcl.bp.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>dateRangePaginationQueryResponse complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="dateRangePaginationQueryResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="return" type="{http://response.btc.mcl.shengpay.com}dateRangePaginationQueryResponse" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dateRangePaginationQueryResponse", propOrder = {
    "_return"
})
public class DateRangePaginationQueryResponse {

    @XmlElement(name = "return")
    protected com.shengpay.mcl.btc.response.DateRangePaginationQueryResponse _return;

    /**
     * 获取return属性的值。
     * 
     * @return
     *     possible object is
     *     {@link com.shengpay.mcl.btc.response.DateRangePaginationQueryResponse }
     *     
     */
    public com.shengpay.mcl.btc.response.DateRangePaginationQueryResponse getReturn() {
        return _return;
    }

    /**
     * 设置return属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link com.shengpay.mcl.btc.response.DateRangePaginationQueryResponse }
     *     
     */
    public void setReturn(com.shengpay.mcl.btc.response.DateRangePaginationQueryResponse value) {
        this._return = value;
    }

}
