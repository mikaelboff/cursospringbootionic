package br.com.mikaelboff.cursospringbootionic.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mikaelboff.cursospringbootionic.domain.Cliente;
import br.com.mikaelboff.cursospringbootionic.repositories.ClienteRepository;
import br.com.mikaelboff.cursospringbootionic.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ",Tipo: " + Cliente.class.getName()));
	}
}
