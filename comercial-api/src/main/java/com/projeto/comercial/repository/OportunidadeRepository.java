package com.projeto.comercial.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.comercial.model.Oportunidade;

public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long>{
	
	//findBy<nome da propriedade>And<nome da propriedade>
	//o Spring Data JPA já faz a implementação
	Optional<Oportunidade> findByDescricaoAndNomeProspecto(String descricao, String nomeProjeto);
}
