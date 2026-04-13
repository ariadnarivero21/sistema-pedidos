package com.koigroup.sistema_pedidos.repositories;

import com.koigroup.sistema_pedidos.entities.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    @Query("select ci from CarritoItem ci where ci.carrito.codigo = :codigoCarrito and ci.producto.codigo = :codigoProducto")
    Optional<CarritoItem> findByCarritoAndProducto(@Param("codigoCarrito") String codigoCarrito,
                                                   @Param("codigoProducto") String codigoProducto);

    @Query("select ci from CarritoItem ci where ci.carrito.codigo = :codigoCarrito")
    List<CarritoItem> findByCarrito(String codigoCarrito);
}
