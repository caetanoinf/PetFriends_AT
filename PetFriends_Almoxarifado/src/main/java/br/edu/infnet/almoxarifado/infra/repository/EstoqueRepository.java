package br.edu.infnet.almoxarifado.infra.repository;

import br.edu.infnet.almoxarifado.domain.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    Estoque findByProductId(Long produtoId);
}
