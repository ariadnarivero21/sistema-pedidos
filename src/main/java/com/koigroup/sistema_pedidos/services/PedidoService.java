package com.koigroup.sistema_pedidos.services;

import com.koigroup.sistema_pedidos.entities.Carrito;
import com.koigroup.sistema_pedidos.entities.CarritoItem;
import com.koigroup.sistema_pedidos.entities.Categoria;
import com.koigroup.sistema_pedidos.entities.Pedido;
import com.koigroup.sistema_pedidos.enums.EstadoPedido;
import com.koigroup.sistema_pedidos.exception.CarritoNoEncontradoException;
import com.koigroup.sistema_pedidos.exception.CarritoSinUsuarioException;
import com.koigroup.sistema_pedidos.exception.CarritoVacioException;
import com.koigroup.sistema_pedidos.repositories.PedidoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarritoService carritoService;
    private final DescuentoService descuentoService;

    public PedidoService(PedidoRepository pedidoRepository, CarritoService carritoService,
                         DescuentoService descuentoService) {
        this.pedidoRepository = pedidoRepository;
        this.carritoService = carritoService;
        this.descuentoService = descuentoService;
    }

    public void procesarPedido(String codigoCarrito) {
        Carrito carrito = carritoService.getByCodigo(codigoCarrito)
                .orElseThrow(() -> new CarritoNoEncontradoException(codigoCarrito));

        Pedido pedido = new Pedido();
        validateCarrito(carrito);
        pedido.setUsuario(carrito.getUsuario());
        pedido.setCarrito(carrito);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstadoPedido(EstadoPedido.PROCESANDO);

        pedidoRepository.save(pedido);
        procesarPedidoAsync(pedido.getId());
    }

    private void validateCarrito(Carrito carrito) {
        if (carrito.getUsuario() == null) {
            throw new CarritoSinUsuarioException();
        }

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new CarritoVacioException();
        }
    }

    @Async
    public void procesarPedidoAsync(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(() ->
                new RuntimeException("Pedido con ID" + pedidoId + "no encontrado"));

        try {
            BigDecimal precioFinal = calculatePrecioFinal(pedido.getCarrito().getItems());
            pedido.setPrecioFinal(precioFinal);
            pedido.setEstadoPedido(EstadoPedido.COMPLETADO);
            pedidoRepository.save(pedido);
        } catch (Exception e) {
            pedido.setEstadoPedido(EstadoPedido.ERROR);
            pedido.setMensajeError("Se canceló el pedido por error en el procesamiento: " + e.getMessage());
        }

    }

    private BigDecimal calculatePrecioFinal(List<CarritoItem> carritoItemList) {

        BigDecimal precioFinal = BigDecimal.ZERO;
        for (CarritoItem carritoItem : carritoItemList) {

            Categoria categoria = carritoItem.getProducto().getCategoria();
            BigDecimal precioProducto = carritoItem.getProducto().getPrecio();
            precioProducto = aplicarDescuento(precioProducto, categoria);

            Integer cantidad = carritoItem.getCantidad();

            BigDecimal subtotal = precioProducto.multiply(BigDecimal.valueOf(cantidad));
            precioFinal = precioFinal.add(subtotal);
        }

        return precioFinal;
    }

    private BigDecimal aplicarDescuento(BigDecimal precio, Categoria categoria) {
        return descuentoService.applyDescuento(precio, categoria);
    }

}
