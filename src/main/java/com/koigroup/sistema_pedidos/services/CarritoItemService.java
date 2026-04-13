package com.koigroup.sistema_pedidos.services;

import com.koigroup.sistema_pedidos.DTO.ProductoCarritoDTO;
import com.koigroup.sistema_pedidos.entities.CarritoItem;
import com.koigroup.sistema_pedidos.repositories.CarritoItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoItemService {

    private final CarritoItemRepository carritoItemRepository;

    public CarritoItemService(CarritoItemRepository carritoItemRepository) {
        this.carritoItemRepository = carritoItemRepository;
    }

    public void saveItem(CarritoItem item){
        carritoItemRepository.save(item);
    }

    public Optional<CarritoItem> findByCarritoAndProducto(String codigoCarrito, String codigoProducto){
        return carritoItemRepository.findByCarritoAndProducto(codigoCarrito, codigoProducto);
    }

    public void deleteItem(CarritoItem carritoItem){
        carritoItemRepository.delete(carritoItem);
    }

    public List<ProductoCarritoDTO> getAllProductosByCarrito(String codigoCarrito){
        List<CarritoItem> items = carritoItemRepository.findByCarrito(codigoCarrito);

        return items.stream()
                .map(item -> new ProductoCarritoDTO(
                        item.getProducto().getNombre(),
                        item.getProducto().getPrecio(),
                        item.getProducto().getCodigo(),
                        item.getProducto().getCategoria().getNombre(),
                        item.getCantidad()
                ))
                .collect(Collectors.toList());
    }
}
