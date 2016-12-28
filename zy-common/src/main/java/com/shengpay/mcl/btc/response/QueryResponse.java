
package com.shengpay.mcl.btc.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.shengpay.mcl.bp.api.BaseResponse;


/**
 * <p>queryResponse complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="queryResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://api.bp.mcl.shengpay.com/}baseResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="batchNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="detailId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="resultInfo" type="{http://response.btc.mcl.shengpay.com}resultInfo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryResponse", propOrder = {
    "batchNo",
    "detailId",
    "resultInfo"
})
public class QueryResponse
    extends BaseResponse
{

    protected String batchNo;
    protected String detailId;
    protected ResultInfo resultInfo;

    /**
     * 获取batchNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * 设置batchNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatchNo(String value) {
        this.batchNo = value;
    }

    /**
     * 获取detailId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetailId() {
        return detailId;
    }

    /**
     * 设置detailId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetailId(String value) {
        this.detailId = value;
    }

    /**
     * 获取resultInfo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ResultInfo }
     *     
     */
    public ResultInfo getResultInfo() {
        return resultInfo;
    }

    /**
     * 设置resultInfo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ResultInfo }
     *     
     */
    public void setResultInfo(ResultInfo value) {
        this.resultInfo = value;
    }

}
