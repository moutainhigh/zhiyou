
package com.shengpay.mcl.btc.response;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.shengpay.mcl.bp.api.BaseResponse;


/**
 * <p>dateRangeQueryResponse complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="dateRangeQueryResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://api.bp.mcl.shengpay.com/}baseResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="details" type="{http://response.btc.mcl.shengpay.com}resultInfoDetail" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dateRangeQueryResponse", propOrder = {
    "details"
})
@XmlSeeAlso({
    DateRangePaginationQueryResponse.class
})
public class DateRangeQueryResponse
    extends BaseResponse
{

    @XmlElement(nillable = true)
    protected List<ResultInfoDetail> details;

    /**
     * Gets the value of the details property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the details property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResultInfoDetail }
     * 
     * 
     */
    public List<ResultInfoDetail> getDetails() {
        if (details == null) {
            details = new ArrayList<ResultInfoDetail>();
        }
        return this.details;
    }

}
