package com.koigroup.sistema_pedidos.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoCreatorService pedidoCreatorService;
    private final PedidoProcessorService pedidoProcessorService;

    public void procesarPedido(String codigoCarrito) {
        Long pedidoId = pedidoCreatorService.createPedidoAndUpdateStock(codigoCarrito);
        Thread.ofVirtual().start(() -> pedidoProcessorService.procesarPedidoThread(pedidoId));
    }
}
