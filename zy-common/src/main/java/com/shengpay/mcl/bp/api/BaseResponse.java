
package com.shengpay.mcl.bp.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.shengpay.mcl.btc.response.BatchPaymentResponse;
import com.shengpay.mcl.btc.response.DateRangeQueryResponse;
import com.shengpay.mcl.btc.response.DirectApplyResponse;
import com.shengpay.mcl.btc.response.GenerateResultResponse;
import com.shengpay.mcl.btc.response.QueryResponse;


/**
 * <p>baseResponse complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="baseResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://api.bp.mcl.shengpay.com/}baseSignedInteraction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="resultCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="resultMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "baseResponse", propOrder = {
    "resultCode",
    "resultMessage"
})
@XmlSeeAlso({
    BatchPaymentResponse.class,
    DateRangeQueryResponse.class,
    GenerateResultResponse.class,
    QueryResponse.class,
    DirectApplyResponse.class
})
public abstract class BaseResponse
    extends BaseSignedInteraction
{

    protected String resultCode;
    protected String resultMessage;

    /**
     * 获取resultCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultCode() {
        return resultCode;
    }

    /**
     * 设置resultCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultCode(String value) {
        this.resultCode = value;
    }

    /**
     * 获取resultMessage属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultMessage() {
        return resultMessage;
    }

    /**
     * 设置resultMessage属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultMessage(String value) {
        this.resultMessage = value;
    }

}
