package br.com.mikaelboff.cursospringbootionic.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.mikaelboff.cursospringbootionic.domain.Cidade;
import br.com.mikaelboff.cursospringbootionic.domain.Cliente;
import br.com.mikaelboff.cursospringbootionic.domain.Endereco;
import br.com.mikaelboff.cursospringbootionic.domain.enums.Perfil;
import br.com.mikaelboff.cursospringbootionic.domain.enums.TipoCliente;
import br.com.mikaelboff.cursospringbootionic.dto.ClienteDTO;
import br.com.mikaelboff.cursospringbootionic.dto.ClienteNewDTO;
import br.com.mikaelboff.cursospringbootionic.repositories.ClienteRepository;
import br.com.mikaelboff.cursospringbootionic.repositories.EnderecoRepository;
import br.com.mikaelboff.cursospringbootionic.security.UserSS;
import br.com.mikaelboff.cursospringbootionic.services.exceptions.AuthorizationException;
import br.com.mikaelboff.cursospringbootionic.services.exceptions.DataIntegrityException;
import br.com.mikaelboff.cursospringbootionic.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private ImageService imageService;

	@Value("${img.prefix.client.profile}")
	private String prefix;

	public Cliente find(Integer id) {

		UserSS user = UserService.authenticated();

		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}

		Optional<Cliente> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ",Tipo: " + Cliente.class.getName()));
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
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
			throw new DataIntegrityException("Não é possível excluir um cliente que possui pedidos relacionados.");
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
		return new Cliente(dto.getId(), dto.getNome(), dto.getEmail(), null, null, null);
	}

	public Cliente fromDTO(ClienteNewDTO dto) {
		Cliente cli = new Cliente(null, dto.getNome(), dto.getEmail(), dto.getCpfOuCnpj(),
				TipoCliente.toEnum(dto.getTipo()), pe.encode(dto.getSenha()));

		Cidade cid = new Cidade(dto.getCidadeId(), null, null);

		Endereco end = new Endereco(null, dto.getLogradouro(), dto.getNumero(), dto.getComplemento(), dto.getBairro(),
				dto.getCep(), cli, cid);

		cli.getEnderecos().add(end);
		cli.getTelefones().add(dto.getTelefone1());

		if (dto.getTelefone2() != null) {
			cli.getTelefones().add(dto.getTelefone2());
		}

		if (dto.getTelefone3() != null) {
			cli.getTelefones().add(dto.getTelefone3());
		}

		return cli;
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();

		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}

		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		String filename = prefix + user.getId() + ".jpg";

		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), filename, "image");
	}
}
