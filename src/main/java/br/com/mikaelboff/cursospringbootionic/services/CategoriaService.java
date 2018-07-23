package br.com.mikaelboff.cursospringbootionic.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mikaelboff.cursospringbootionic.domain.Categoria;
import br.com.mikaelboff.cursospringbootionic.repositories.CategoriaRepository;
import br.com.mikaelboff.cursospringbootionic.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ",Tipo: " + Categoria.class.getName()));
	}

	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	public Categoria update(Categoria obj) {
		this.find(obj.getId());
		return repo.save(obj);
	}
}
