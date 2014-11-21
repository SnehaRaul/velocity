/**
 * 
 */
package com.scholastic.intl.email;

/**
 * @author nataraj.srikantaiah
 *
 */
public class EmailService {

	private static final String SUBJECT_TEMPLATE_SUFFIX = "_subject.vm";
	private static final String HTML_TEMPLATE_SUFFIX = "_html.vm";
	
	private final static Logger log = Logger.getLogger(EmailService.class.getName());	
	private VelocityEngine mVelocityEngine;
	
	private String getTemplateName(final String templateName, final String templateSuffix) {
	    StringBuilder strBuilder = new StringBuilder(templateName);
	    strBuilder.append(templateSuffix);
	    return strBuilder.toString();
	}
	
	private String mergeTemplate(final String templateName, final Map<String, Object> templateParams) {
	    return VelocityEngineUtils.mergeTemplateIntoString(mVelocityEngine, templateName, templateParams);
	}
		
    private EmailMessage queueEmail(final User user, final String recipientAddress, 
    		final String templateName, final Map<String, Object> globalParams, final boolean isTestEmail) {
    	
    	if (StringUtils.isBlank(recipientAddress))
			throw new IllegalArgumentException("recipientAddress cannot be blank for user " + user);
		
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.setUser(user);
		emailMessage.setRecipientAddress(recipientAddress);
		emailMessage.setTemplateType(templateName);

		String subjectTemplate = getTemplateName(templateName, SUBJECT_TEMPLATE_SUFFIX);
		String subject = mergeTemplate(subjectTemplate, globalParams);

		if(isTestEmail) {
			subject = "TEST EMAIL ~ " + subject;
		}		
		emailMessage.setSubject(subject);

		String htmlText = "";
	    String htmlTemplate = getTemplateName(templateName, HTML_TEMPLATE_SUFFIX);
	    htmlText = mergeTemplate(htmlTemplate, globalParams);

        emailMessage.setHtmlBody(htmlText);        
        emailMessage.setGeneratedDate(new Date());
        emailMessage = save(emailMessage);                
        return emailMessage;
        
	}

	@Required
	public void setVelocityEngine(VelocityEngine pVelocityEngine) {
		mVelocityEngine = pVelocityEngine;
	}

}
