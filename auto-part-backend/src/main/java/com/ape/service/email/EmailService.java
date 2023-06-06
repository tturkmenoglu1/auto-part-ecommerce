package com.ape.service.email;

import com.ape.model.ImageFile;
import com.ape.model.Order;
import com.ape.utility.ErrorMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EmailService implements EmailSender{

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailAddress;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Override
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage =  mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"UTF-8");
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject("Welcome to the Auto Part");
            helper.setFrom(mailAddress);
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            logger.error(ErrorMessage.FAILED_TO_SEND_EMAIL_MESSAGE,e);
            throw new IllegalStateException(ErrorMessage.FAILED_TO_SEND_EMAIL_MESSAGE);
        }
    }


    public String buildRegisterEmail(String name, String link) {
        return "";
    }

    public String buildOrderMail(Order order) {
        List<String> shippingDetailList = Arrays.asList(order.getShippingDetails().split(":"));
        return "";
    }

    private String orderItemToTable(Order order){
        StringBuilder orderItems = new StringBuilder();
        for (int i = 0; i < order.getOrderItems().size(); i++) {
            String showcaseImage = order.getOrderItems().get(i).getProduct().getImage().stream().filter(ImageFile::isShowcase).findFirst().get().getId();
          orderItems.append("<tr>")
                  .append("<td style=\"text-align: center;\">\n" +
                          "                    <img\n" +
                          "                      src=\"https://ecommerce-backend-v2.herokuapp.com/image/display/"+showcaseImage+"\"\n" +
                          "                      alt=\"3\"\n" +
                          "                      width=\"50vh\"\n" +
                          "                    />\n" +
                          "                  </td>")
                  .append("<td style=\"text-align: left; padding-left: 1rem\">"+ order.getOrderItems().get(i).getProduct().getTitle() +"</td>")
                  .append("<td style=\"text-align: center\">"+order.getOrderItems().get(i).getQuantity()+"</td>")
                  .append("<td style=\"text-align: right\">"+order.getOrderItems().get(i).getSubTotal()+"</td>")
                  .append("</tr>");
        }
        return orderItems.toString();
    }


}
