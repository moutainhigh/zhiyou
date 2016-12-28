
package com.shengpay.mcl.btc.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>dateRangePaginationQueryResponse complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="dateRangePaginationQueryResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://response.btc.mcl.shengpay.com}dateRangeQueryResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="totalCount" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="pages" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="pageSize" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="pageNum" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dateRangePaginationQueryResponse", propOrder = {
    "totalCount",
    "pages",
    "pageSize",
    "pageNum"
})
public class DateRangePaginationQueryResponse
    extends DateRangeQueryResponse
{

    protected long totalCount;
    protected long pages;
    protected int pageSize;
    protected long pageNum;

    /**
     * 获取totalCount属性的值。
     * 
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * 设置totalCount属性的值。
     * 
     */
    public void setTotalCount(long value) {
        this.totalCount = value;
    }

    /**
     * 获取pages属性的值。
     * 
     */
    public long getPages() {
        return pages;
    }

    /**
     * 设置pages属性的值。
     * 
     */
    public void setPages(long value) {
        this.pages = value;
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

}
