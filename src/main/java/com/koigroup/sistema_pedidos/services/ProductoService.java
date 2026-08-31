package com.koigroup.sistema_pedidos.services;

import com.koigroup.sistema_pedidos.entities.Producto;
import com.koigroup.sistema_pedidos.exception.StockInsuficienteException;
import com.koigroup.sistema_pedidos.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public Optional<Producto> findByCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo);
    }

    public void subtractStock(Producto producto, Integer cantidad) {
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException(cantidad, producto.getCodigo(), producto.getStock());
        }

        producto.setStock(producto.getStock() - cantidad);
    }

    public void addStock(Producto producto, Integer cantidad) {
        producto.setStock(producto.getStock() + cantidad);
    }
}
