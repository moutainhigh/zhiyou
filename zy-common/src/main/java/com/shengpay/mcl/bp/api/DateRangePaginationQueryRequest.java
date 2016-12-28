
package com.shengpay.mcl.bp.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>dateRangePaginationQueryRequest complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="dateRangePaginationQueryRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://api.bp.mcl.shengpay.com/}dateRangeQueryRequest"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pageNum" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="pageSize" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dateRangePaginationQueryRequest", propOrder = {
    "pageNum",
    "pageSize"
})
public class DateRangePaginationQueryRequest
    extends DateRangeQueryRequest
{

    protected long pageNum;
    protected int pageSize;

    /**
     * 获取pageNum属性的值。
     * 
     */
    public long getPageNum() {
        return pageNum;
    }

    /**
     * 设置pageNum属性的值。
     * 
     */
    public void setPageNum(long value) {
        this.pageNum = value;
    }

    /**
     * 获取pageSize属性的值。
     * 
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置pageSize属性的值。
     * 
     */
    public void setPageSize(int value) {
        this.pageSize = value;
    }

}
