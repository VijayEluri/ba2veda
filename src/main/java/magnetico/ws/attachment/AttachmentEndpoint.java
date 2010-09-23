
package magnetico.ws.attachment;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.5-b03-
 * Generated source version: 2.1
 * 
 */
@WebService(name = "AttachmentEndpoint", targetNamespace = "http://attachments.bigarchive.magnetosoft.ru/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface AttachmentEndpoint {


    /**
     * 
     * @param sessionTicketId
     * @return
     *     returns java.lang.String
     * @throws BigArchiveServerException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getVersion", targetNamespace = "http://attachments.bigarchive.magnetosoft.ru/", className = "magnetico.ws.attachment.GetVersion")
    @ResponseWrapper(localName = "getVersionResponse", targetNamespace = "http://attachments.bigarchive.magnetosoft.ru/", className = "magnetico.ws.attachment.GetVersionResponse")
    public String getVersion(
        @WebParam(name = "sessionTicketId", targetNamespace = "")
        String sessionTicketId)
        throws BigArchiveServerException_Exception
    ;

    /**
     * 
     * @param sessionTicketId
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws BigArchiveServerException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getDependenciesVersions", targetNamespace = "http://attachments.bigarchive.magnetosoft.ru/", className = "magnetico.ws.attachment.GetDependenciesVersions")
    @ResponseWrapper(localName = "getDependenciesVersionsResponse", targetNamespace = "http://attachments.bigarchive.magnetosoft.ru/", className = "magnetico.ws.attachment.GetDependenciesVersionsResponse")
    public List<String> getDependenciesVersions(
        @WebParam(name = "sessionTicketId", targetNamespace = "")
        String sessionTicketId)
        throws BigArchiveServerException_Exception
    ;

    /**
     * 
     * @param obj
     * @param sessionTicketId
     * @return
     *     returns java.lang.String
     * @throws AccessDeniedException_Exception
     * @throws BigArchiveServerException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "createAttachment", targetNamespace = "http://attachments.bigarchive.magnetosoft.ru/", className = "magnetico.ws.attachment.CreateAttachment")
    @ResponseWrapper(localName = "createAttachmentResponse", targetNamespace = "http://attachments.bigarchive.magnetosoft.ru/", className = "magnetico.ws.attachment.CreateAttachmentResponse")
    public String createAttachment(
        @WebParam(name = "sessionTicketId", targetNamespace = "")
        String sessionTicketId,
        @WebParam(name = "obj", targetNamespace = "")
        AttachmentType obj)
        throws AccessDeniedException_Exception, BigArchiveServerException_Exception
    ;

    /**
     * 
     * @param id
     * @param withContent
     * @param contexDocumentId
     * @param sessionTicketId
     * @return
     *     returns magnetico.ws.attachment.AttachmentType
     * @throws AccessDeniedException_Exception
     * @throws BigArchiveServerException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAttachment", targetNamespace = "http://attachments.bigarchive.magnetosoft.ru/", className = "magnetico.ws.attachment.GetAttachment")
    @ResponseWrapper(localName = "getAttachmentResponse", targetNamespace = "http://attachments.bigarchive.magnetosoft.ru/", className = "magnetico.ws.attachment.GetAttachmentResponse")
    public AttachmentType getAttachment(
        @WebParam(name = "sessionTicketId", targetNamespace = "")
        String sessionTicketId,
        @WebParam(name = "contexDocumentId", targetNamespace = "")
        String contexDocumentId,
        @WebParam(name = "id", targetNamespace = "")
        String id,
        @WebParam(name = "withContent", targetNamespace = "")
        boolean withContent)
        throws AccessDeniedException_Exception, BigArchiveServerException_Exception
    ;

    /**
     * 
     * @param withContent
     * @param contexDocumentId
     * @param attachmentIds
     * @param sessionTicketId
     * @return
     *     returns java.util.List<magnetico.ws.attachment.AttachmentType>
     * @throws AccessDeniedException_Exception
     * @throws BigArchiveServerException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "listAttachments", targetNamespace = "http://attachments.bigarchive.magnetosoft.ru/", className = "magnetico.ws.attachment.ListAttachments")
    @ResponseWrapper(localName = "listAttachmentsResponse", targetNamespace = "http://attachments.bigarchive.magnetosoft.ru/", className = "magnetico.ws.attachment.ListAttachmentsResponse")
    public List<AttachmentType> listAttachments(
        @WebParam(name = "sessionTicketId", targetNamespace = "")
        String sessionTicketId,
        @WebParam(name = "contexDocumentId", targetNamespace = "")
        String contexDocumentId,
        @WebParam(name = "attachmentIds", targetNamespace = "")
        List<String> attachmentIds,
        @WebParam(name = "withContent", targetNamespace = "")
        boolean withContent)
        throws AccessDeniedException_Exception, BigArchiveServerException_Exception
    ;

}
