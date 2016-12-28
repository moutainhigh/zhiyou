
package com.shengpay.mcl.bp.api;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.shengpay.mcl.bp.api package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DateRangeQuery_QNAME = new QName("http://api.bp.mcl.shengpay.com/", "DateRangeQuery");
    private final static QName _DateRangeQueryResponse_QNAME = new QName("http://api.bp.mcl.shengpay.com/", "DateRangeQueryResponse");
    private final static QName _Generate_QNAME = new QName("http://api.bp.mcl.shengpay.com/", "Generate");
    private final static QName _GenerateResponse_QNAME = new QName("http://api.bp.mcl.shengpay.com/", "GenerateResponse");
    private final static QName _Query_QNAME = new QName("http://api.bp.mcl.shengpay.com/", "Query");
    private final static QName _QueryResponse_QNAME = new QName("http://api.bp.mcl.shengpay.com/", "QueryResponse");
    private final static QName _Apply_QNAME = new QName("http://api.bp.mcl.shengpay.com/", "apply");
    private final static QName _ApplyResponse_QNAME = new QName("http://api.bp.mcl.shengpay.com/", "applyResponse");
    private final static QName _DateRangePaginationQuery_QNAME = new QName("http://api.bp.mcl.shengpay.com/", "dateRangePaginationQuery");
    private final static QName _DateRangePaginationQueryResponse_QNAME = new QName("http://api.bp.mcl.shengpay.com/", "dateRangePaginationQueryResponse");
    private final static QName _DirectApply_QNAME = new QName("http://api.bp.mcl.shengpay.com/", "directApply");
    private final static QName _DirectApplyResponse_QNAME = new QName("http://api.bp.mcl.shengpay.com/", "directApplyResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.shengpay.mcl.bp.api
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DateRangeQuery }
     * 
     */
    public DateRangeQuery createDateRangeQuery() {
        return new DateRangeQuery();
    }

    /**
     * Create an instance of {@link DateRangeQueryResponse }
     * 
     */
    public DateRangeQueryResponse createDateRangeQueryResponse() {
        return new DateRangeQueryResponse();
    }

    /**
     * Create an instance of {@link Generate }
     * 
     */
    public Generate createGenerate() {
        return new Generate();
    }

    /**
     * Create an instance of {@link GenerateResponse }
     * 
     */
    public GenerateResponse createGenerateResponse() {
        return new GenerateResponse();
    }

    /**
     * Create an instance of {@link Query }
     * 
     */
    public Query createQuery() {
        return new Query();
    }

    /**
     * Create an instance of {@link QueryResponse }
     * 
     */
    public QueryResponse createQueryResponse() {
        return new QueryResponse();
    }

    /**
     * Create an instance of {@link Apply }
     * 
     */
    public Apply createApply() {
        return new Apply();
    }

    /**
     * Create an instance of {@link ApplyResponse }
     * 
     */
    public ApplyResponse createApplyResponse() {
        return new ApplyResponse();
    }

    /**
     * Create an instance of {@link DateRangePaginationQuery }
     * 
     */
    public DateRangePaginationQuery createDateRangePaginationQuery() {
        return new DateRangePaginationQuery();
    }

    /**
     * Create an instance of {@link DateRangePaginationQueryResponse }
     * 
     */
    public DateRangePaginationQueryResponse createDateRangePaginationQueryResponse() {
        return new DateRangePaginationQueryResponse();
    }

    /**
     * Create an instance of {@link DirectApply }
     * 
     */
    public DirectApply createDirectApply() {
        return new DirectApply();
    }

    /**
     * Create an instance of {@link DirectApplyResponse }
     * 
     */
    public DirectApplyResponse createDirectApplyResponse() {
        return new DirectApplyResponse();
    }

    /**
     * Create an instance of {@link BatchPaymentRequest }
     * 
     */
    public BatchPaymentRequest createBatchPaymentRequest() {
        return new BatchPaymentRequest();
    }

    /**
     * Create an instance of {@link DateRangePaginationQueryRequest }
     * 
     */
    public DateRangePaginationQueryRequest createDateRangePaginationQueryRequest() {
        return new DateRangePaginationQueryRequest();
    }

    /**
     * Create an instance of {@link DateRangeQueryRequest }
     * 
     */
    public DateRangeQueryRequest createDateRangeQueryRequest() {
        return new DateRangeQueryRequest();
    }

    /**
     * Create an instance of {@link GenerateResultRequest }
     * 
     */
    public GenerateResultRequest createGenerateResultRequest() {
        return new GenerateResultRequest();
    }

    /**
     * Create an instance of {@link QueryRequest }
     * 
     */
    public QueryRequest createQueryRequest() {
        return new QueryRequest();
    }

    /**
     * Create an instance of {@link DirectApplyRequest }
     * 
     */
    public DirectApplyRequest createDirectApplyRequest() {
        return new DirectApplyRequest();
    }

    /**
     * Create an instance of {@link ApplyInfoDetail }
     * 
     */
    public ApplyInfoDetail createApplyInfoDetail() {
        return new ApplyInfoDetail();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DateRangeQuery }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.bp.mcl.shengpay.com/", name = "DateRangeQuery")
    public JAXBElement<DateRangeQuery> createDateRangeQuery(DateRangeQuery value) {
        return new JAXBElement<DateRangeQuery>(_DateRangeQuery_QNAME, DateRangeQuery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DateRangeQueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.bp.mcl.shengpay.com/", name = "DateRangeQueryResponse")
    public JAXBElement<DateRangeQueryResponse> createDateRangeQueryResponse(DateRangeQueryResponse value) {
        return new JAXBElement<DateRangeQueryResponse>(_DateRangeQueryResponse_QNAME, DateRangeQueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Generate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.bp.mcl.shengpay.com/", name = "Generate")
    public JAXBElement<Generate> createGenerate(Generate value) {
        return new JAXBElement<Generate>(_Generate_QNAME, Generate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.bp.mcl.shengpay.com/", name = "GenerateResponse")
    public JAXBElement<GenerateResponse> createGenerateResponse(GenerateResponse value) {
        return new JAXBElement<GenerateResponse>(_GenerateResponse_QNAME, GenerateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Query }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.bp.mcl.shengpay.com/", name = "Query")
    public JAXBElement<Query> createQuery(Query value) {
        return new JAXBElement<Query>(_Query_QNAME, Query.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.bp.mcl.shengpay.com/", name = "QueryResponse")
    public JAXBElement<QueryResponse> createQueryResponse(QueryResponse value) {
        return new JAXBElement<QueryResponse>(_QueryResponse_QNAME, QueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Apply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.bp.mcl.shengpay.com/", name = "apply")
    public JAXBElement<Apply> createApply(Apply value) {
        return new JAXBElement<Apply>(_Apply_QNAME, Apply.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApplyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.bp.mcl.shengpay.com/", name = "applyResponse")
    public JAXBElement<ApplyResponse> createApplyResponse(ApplyResponse value) {
        return new JAXBElement<ApplyResponse>(_ApplyResponse_QNAME, ApplyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DateRangePaginationQuery }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.bp.mcl.shengpay.com/", name = "dateRangePaginationQuery")
    public JAXBElement<DateRangePaginationQuery> createDateRangePaginationQuery(DateRangePaginationQuery value) {
        return new JAXBElement<DateRangePaginationQuery>(_DateRangePaginationQuery_QNAME, DateRangePaginationQuery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DateRangePaginationQueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.bp.mcl.shengpay.com/", name = "dateRangePaginationQueryResponse")
    public JAXBElement<DateRangePaginationQueryResponse> createDateRangePaginationQueryResponse(DateRangePaginationQueryResponse value) {
        return new JAXBElement<DateRangePaginationQueryResponse>(_DateRangePaginationQueryResponse_QNAME, DateRangePaginationQueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DirectApply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.bp.mcl.shengpay.com/", name = "directApply")
    public JAXBElement<DirectApply> createDirectApply(DirectApply value) {
        return new JAXBElement<DirectApply>(_DirectApply_QNAME, DirectApply.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DirectApplyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.bp.mcl.shengpay.com/", name = "directApplyResponse")
    public JAXBElement<DirectApplyResponse> createDirectApplyResponse(DirectApplyResponse value) {
        return new JAXBElement<DirectApplyResponse>(_DirectApplyResponse_QNAME, DirectApplyResponse.class, null, value);
    }

}
