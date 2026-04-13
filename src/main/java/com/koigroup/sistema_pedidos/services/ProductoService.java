package com.koigroup.sistema_pedidos.services;

import com.koigroup.sistema_pedidos.entities.Producto;
import com.koigroup.sistema_pedidos.repositories.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Optional<Producto> findByCodigo(String codigo){
        return productoRepository.findByCodigo(codigo);
    }
}
