package com.koigroup.sistema_pedidos.services;

import com.koigroup.sistema_pedidos.entities.Carrito;
import com.koigroup.sistema_pedidos.entities.CarritoItem;
import com.koigroup.sistema_pedidos.entities.Pedido;
import com.koigroup.sistema_pedidos.enums.EstadoPedido;
import com.koigroup.sistema_pedidos.exception.CarritoNoEncontradoException;
import com.koigroup.sistema_pedidos.exception.CarritoSinUsuarioException;
import com.koigroup.sistema_pedidos.exception.CarritoVacioException;
import com.koigroup.sistema_pedidos.repositories.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PedidoCreatorService {

    private final CarritoService carritoService;
    private final ProductoService productoService;
    private final PedidoRepository pedidoRepository;

    @Transactional
    public Long createPedidoAndUpdateStock(String codigoCarrito) {
        Carrito carrito = carritoService.getByCodigo(codigoCarrito)
                .orElseThrow(() -> new CarritoNoEncontradoException(codigoCarrito));

        Pedido pedido = new Pedido();
        validateCarrito(carrito);
        pedido.setUsuario(carrito.getUsuario());
        pedido.setCarrito(carrito);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstadoPedido(EstadoPedido.INICIAL);
        pedido.setEstadoPedido(pedido.getEstadoPedido().transicionarA(EstadoPedido.PROCESANDO));

        for (CarritoItem item : pedido.getCarrito().getItems()) {
            productoService.subtractStock(item.getProducto(), item.getCantidad());
        }

        pedidoRepository.save(pedido);
        return pedido.getId();
    }

    private void validateCarrito(Carrito carrito) {
        if (carrito.getUsuario() == null) {
            throw new CarritoSinUsuarioException();
        }

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new CarritoVacioException();
        }
    }
}
