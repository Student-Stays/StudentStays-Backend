package com.management.studentstays.App.impl;

import com.management.studentstays.App.entity.EmailTemplate;
import com.management.studentstays.App.exception.EmailTemplateNotFoundException;
import com.management.studentstays.App.repo.EmailTemplateRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

@Slf4j
@Service
public class EmailService {
  @Autowired private JavaMailSender javaMailSender;
  @Autowired private EmailTemplateRepository templateRepository;
  @Autowired private ResourceLoader resourceLoader;

  public HashMap<String, String> renderTemplate(
      String templateName, HashMap<String, String> emailContent) throws IOException {
    HashMap<String, String> templateData = new HashMap<>();

    EmailTemplate template =
        templateRepository
            .findByName(templateName)


            .orElseThrow(() -> new EmailTemplateNotFoundException(templateName));

    templateData.put("recipient", template.getRecipient());


    templateData.put("subject",

            template.getSubject());

    String templateContent = loadTemplateContent(templateName);

    for (HashMap.Entry<String, String> entry : emailContent.entrySet()) {
      String placeholder = "{" + entry.getKey() + "}";
      templateContent = templateContent.replace(placeholder, entry.getValue());
    }
    templateData.put("templateContent", templateContent);

    return templateData;
  }

  private String loadTemplateContent(String templateName) throws IOException {
    Resource resource =
        resourceLoader.getResource("classpath:templates/Email template/" + templateName + ".html");
    try (InputStream inputStream = resource.getInputStream()) {
      return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    }
  }

  public void sendSimpleMessage(String templateName, HashMap<String, String> emailContent) {
    try {
      HashMap<String, String> templateData = renderTemplate(templateName, emailContent);

      String paymentReceived = templateData.get("templateContent");
      String to = templateData.get("recipient");
      String subject = templateData.get("subject");

      if (StringUtils.isNotBlank(paymentReceived)) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(paymentReceived, true);

        javaMailSender.send(mimeMessage);
      } else {
        log.error("Template: {} is not rendered.", templateName);
      }
    } catch (IOException | MessagingException e) {
      log.error("An error occurred while sending the email: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
