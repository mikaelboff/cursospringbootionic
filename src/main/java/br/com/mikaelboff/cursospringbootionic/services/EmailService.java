package br.com.mikaelboff.cursospringbootionic.services;

import org.springframework.mail.SimpleMailMessage;

import br.com.mikaelboff.cursospringbootionic.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);

	void sendMail(SimpleMailMessage msg);
}
