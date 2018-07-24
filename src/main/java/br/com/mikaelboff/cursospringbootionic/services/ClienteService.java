package br.com.mikaelboff.cursospringbootionic.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.mikaelboff.cursospringbootionic.domain.Cliente;
import br.com.mikaelboff.cursospringbootionic.dto.ClienteDTO;
import br.com.mikaelboff.cursospringbootionic.repositories.ClienteRepository;
import br.com.mikaelboff.cursospringbootionic.services.exceptions.DataIntegrityException;
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

	public Cliente update(Cliente obj) {
		Cliente newObj = this.find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		this.find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente que possui entidades relacionadas.");
		}

	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pagerequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

		return repo.findAll(pagerequest);
	}

	public Cliente fromDTO(ClienteDTO dto) {
		return new Cliente(dto.getId(), dto.getNome(), dto.getEmail(), null, null);
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
