package com.koigroup.sistema_pedidos.services;

import com.koigroup.sistema_pedidos.entities.Carrito;
import com.koigroup.sistema_pedidos.entities.Pedido;
import com.koigroup.sistema_pedidos.enums.EstadoPedido;
import com.koigroup.sistema_pedidos.exception.CarritoNoEncontradoException;
import com.koigroup.sistema_pedidos.exception.CarritoSinUsuarioException;
import com.koigroup.sistema_pedidos.exception.CarritoVacioException;
import com.koigroup.sistema_pedidos.repositories.PedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarritoService carritoService;
    private final PedidoProcessorService pedidoProcessorService;

    public void procesarPedido(String codigoCarrito) {
        Carrito carrito = carritoService.getByCodigo(codigoCarrito)
                .orElseThrow(() -> new CarritoNoEncontradoException(codigoCarrito));

        Pedido pedido = new Pedido();
        validateCarrito(carrito);
        pedido.setUsuario(carrito.getUsuario());
        pedido.setCarrito(carrito);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstadoPedido(EstadoPedido.INICIAL);
        pedido.setEstadoPedido(pedido.getEstadoPedido().transicionarA(EstadoPedido.PROCESANDO));

        pedidoRepository.save(pedido);
        Thread.ofVirtual().start(() -> pedidoProcessorService.procesarPedidoThread(pedido.getId()));
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
