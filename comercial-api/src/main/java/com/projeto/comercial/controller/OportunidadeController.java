package com.projeto.comercial.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projeto.comercial.model.Oportunidade;
import com.projeto.comercial.repository.OportunidadeRepository;

//essa classe é um controlador REST
//CrossOrigin permite acesso de qualquer lugar
@CrossOrigin
@RestController
@RequestMapping("/oportunidades") //mapeia a URI que vai receber requisições
public class OportunidadeController {
	
	@Autowired
	private OportunidadeRepository oportunidades;
	
	//método responsável por tratar requisições GET
	@GetMapping
	public List<Oportunidade> listar() {
		return oportunidades.findAll();		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Oportunidade> buscar(@PathVariable Long id) {
		Optional<Oportunidade> oportunidade = oportunidades.findById(id);
		
		//se a busca não retornar nada
		if (!oportunidade.isEmpty()) {
			//notFound é o status 404
			return ResponseEntity.notFound().build();
		}
		//ok é o status 200
		return ResponseEntity.ok(oportunidade.get());
	}
	//RequestBody pega o JSON que está no corpo da requisição e transforma em um objeto Java do tipo Oportunidade
	//ResponseStatus(HttpStatus.CREATED) diz que o método tem que retornar o status http "created", que é o status 201, ao invés de retornar 200
	//Valid diz para validar primeiro o objeto Oportunidade antes de continuar
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Oportunidade adicionar(@Valid @RequestBody Oportunidade oportunidade) {
		Optional<Oportunidade> oportunidadeExistente = oportunidades
				.findByDescricaoAndNomeProspecto(oportunidade.getDescricao(), 
						oportunidade.getNomeProspecto());
		//se exisitir vai retornar status 404 e uma mensagem
		if (oportunidadeExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Já existe uma oportunidade para este prospecto com a mesma descrição.");
		}
		return oportunidades.save(oportunidade);
	}
}
