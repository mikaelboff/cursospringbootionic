package br.com.mikaelboff.cursospringbootionic.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mikaelboff.cursospringbootionic.domain.Categoria;
import br.com.mikaelboff.cursospringbootionic.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);

		return obj.orElse(null);
	}
}
