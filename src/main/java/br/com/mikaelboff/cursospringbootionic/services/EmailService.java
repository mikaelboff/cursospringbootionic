package br.com.mikaelboff.cursospringbootionic.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import br.com.mikaelboff.cursospringbootionic.domain.Cliente;
import br.com.mikaelboff.cursospringbootionic.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);

	void sendOrderConfirmationHtmlEmail(Pedido obj);

	void sendMail(SimpleMailMessage msg);

	void sendHtmlEmail(MimeMessage msg);

	void sendNewPasswordEmail(Cliente cliente, String newPass);

}
