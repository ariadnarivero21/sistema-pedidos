package com.koigroup.sistema_pedidos.services;

import com.koigroup.sistema_pedidos.entities.CarritoItem;
import com.koigroup.sistema_pedidos.entities.Categoria;
import com.koigroup.sistema_pedidos.entities.Pedido;
import com.koigroup.sistema_pedidos.enums.EstadoPedido;
import com.koigroup.sistema_pedidos.repositories.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class PedidoProcessorService {

    private final PedidoRepository pedidoRepository;
    private final DescuentoService descuentoService;
    private final Logger log = LoggerFactory.getLogger(PedidoProcessorService.class);

    @Transactional
    public void procesarPedidoThread(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(() ->
                new RuntimeException("Pedido con ID" + pedidoId + "no encontrado"));

        try {
            BigDecimal precioFinal = calculatePrecioFinal(pedido.getCarrito().getItems());
            pedido.setPrecioFinal(precioFinal);
            pedido.setEstadoPedido(pedido.getEstadoPedido().transicionarA(EstadoPedido.COMPLETADO));
        } catch (Exception e) {
            log.error("Error al procesar pedido con ID {}: {}", pedidoId, e.getMessage());
            pedido.setEstadoPedido(EstadoPedido.ERROR);
            pedido.setMensajeError("Se canceló el pedido por error en el procesamiento: " + e.getMessage());
        }
        pedidoRepository.save(pedido);
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
