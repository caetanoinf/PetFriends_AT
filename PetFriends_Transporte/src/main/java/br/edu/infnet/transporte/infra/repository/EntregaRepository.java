package br.edu.infnet.transporte.infra.repository;

import br.edu.infnet.transporte.domain.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntregaRepository extends JpaRepository<Entrega, Long> {
}
