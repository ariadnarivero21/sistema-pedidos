package com.koigroup.sistema_pedidos.repositories;

import com.koigroup.sistema_pedidos.entities.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    List<Carrito> findByUsuarioId(Long idUsuario);

    Optional<Carrito> findByCodigo(String codigo);
}
