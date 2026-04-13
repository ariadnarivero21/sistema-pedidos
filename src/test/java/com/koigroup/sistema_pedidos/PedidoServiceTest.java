package com.koigroup.sistema_pedidos;

import com.koigroup.sistema_pedidos.entities.*;
import com.koigroup.sistema_pedidos.enums.EstadoPedido;
import com.koigroup.sistema_pedidos.exception.CarritoNoEncontradoException;
import com.koigroup.sistema_pedidos.exception.CarritoSinUsuarioException;
import com.koigroup.sistema_pedidos.exception.CarritoVacioException;
import com.koigroup.sistema_pedidos.repositories.PedidoRepository;
import com.koigroup.sistema_pedidos.services.CarritoService;
import com.koigroup.sistema_pedidos.services.DescuentoService;
import com.koigroup.sistema_pedidos.services.PedidoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private CarritoService carritoService;
    @Mock
    private DescuentoService descuentoService;

    @InjectMocks
    private PedidoService pedidoService;

    // --- procesarPedido: casos de error ---

    @Test
    void procesarPedido_carritoNoEncontrado_lanzaExcepcion() {
        when(carritoService.getByCodigo("CAR-999")).thenReturn(Optional.empty());

        assertThrows(CarritoNoEncontradoException.class,
                () -> pedidoService.procesarPedido("CAR-999"));
    }

    @Test
    void procesarPedido_carritoSinUsuario_lanzaExcepcion() {
        Carrito carrito = new Carrito();
        carrito.setUsuario(null);
        carrito.setItems(List.of(new CarritoItem()));

        when(carritoService.getByCodigo("CAR-001")).thenReturn(Optional.of(carrito));

        assertThrows(CarritoSinUsuarioException.class,
                () -> pedidoService.procesarPedido("CAR-001"));
    }

    @Test
    void procesarPedido_carritoVacio_lanzaExcepcion() {
        Carrito carrito = new Carrito();
        carrito.setUsuario(new Usuario());
        carrito.setItems(List.of());

        when(carritoService.getByCodigo("CAR-001")).thenReturn(Optional.of(carrito));

        assertThrows(CarritoVacioException.class,
                () -> pedidoService.procesarPedido("CAR-001"));
    }

    @Test
    void procesarPedido_carritoConItemsNulos_lanzaExcepcion() {
        Carrito carrito = new Carrito();
        carrito.setUsuario(new Usuario());
        carrito.setItems(null);

        when(carritoService.getByCodigo("CAR-001")).thenReturn(Optional.of(carrito));

        assertThrows(CarritoVacioException.class,
                () -> pedidoService.procesarPedido("CAR-001"));
    }

    // --- procesarPedido: camino feliz ---

    @Test
    void procesarPedido_valido_guardaPedidoConEstadoProcesando() {
        Categoria categoria = new Categoria();
        categoria.setNombre("TECNOLOGIA");

        Producto producto = new Producto();
        producto.setPrecio(new BigDecimal("1500.00"));
        producto.setCategoria(categoria);

        CarritoItem item = new CarritoItem();
        item.setProducto(producto);
        item.setCantidad(1);

        Usuario usuario = new Usuario();
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setItems(List.of(item));

        when(carritoService.getByCodigo("CAR-001")).thenReturn(Optional.of(carrito));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> {
            Pedido p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(buildPedido(carrito, usuario)));
        when(descuentoService.applyDescuento(any(), any())).thenAnswer(inv -> inv.getArgument(0));

        pedidoService.procesarPedido("CAR-001");

        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository, atLeastOnce()).save(captor.capture());

        Pedido primerGuardado = captor.getAllValues().get(0);
        assertEquals(EstadoPedido.PROCESANDO, primerGuardado.getEstadoPedido());
        assertEquals(usuario, primerGuardado.getUsuario());
        assertEquals(carrito, primerGuardado.getCarrito());
    }

    // --- procesarPedidoAsync ---

    @Test
    void procesarPedidoAsync_calculaPrecioFinalYActualizaEstadoCompletado() {
        Categoria categoria = new Categoria();

        Producto producto = new Producto();
        producto.setPrecio(new BigDecimal("3000.00"));
        producto.setCategoria(categoria);

        CarritoItem item = new CarritoItem();
        item.setProducto(producto);
        item.setCantidad(2);

        Carrito carrito = new Carrito();
        carrito.setItems(List.of(item));

        Pedido pedido = new Pedido();
        pedido.setCarrito(carrito);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        // sin descuento → retorna el precio original
        when(descuentoService.applyDescuento(any(), any())).thenAnswer(inv -> inv.getArgument(0));

        pedidoService.procesarPedidoAsync(1L);

        assertEquals(EstadoPedido.COMPLETADO, pedido.getEstadoPedido());
        // 3000 * 2 = 6000
        assertEquals(0, pedido.getPrecioFinal().compareTo(new BigDecimal("6000.00")));
    }

    @Test
    void procesarPedidoAsync_errorEnDescuento_actualizaEstadoError() {
        Categoria categoria = new Categoria();

        Producto producto = new Producto();
        producto.setPrecio(new BigDecimal("1000.00"));
        producto.setCategoria(categoria);

        CarritoItem item = new CarritoItem();
        item.setProducto(producto);
        item.setCantidad(1);

        Carrito carrito = new Carrito();
        carrito.setItems(List.of(item));

        Pedido pedido = new Pedido();
        pedido.setCarrito(carrito);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(descuentoService.applyDescuento(any(), any()))
                .thenThrow(new RuntimeException("Error en descuento"));

        pedidoService.procesarPedidoAsync(1L);

        assertEquals(EstadoPedido.ERROR, pedido.getEstadoPedido());
        assertNotNull(pedido.getMensajeError());
        assertTrue(pedido.getMensajeError().contains("Error en descuento"));
    }

    // --- Helper ---

    private Pedido buildPedido(Carrito carrito, Usuario usuario) {
        Pedido p = new Pedido();
        p.setCarrito(carrito);
        p.setUsuario(usuario);
        p.setEstadoPedido(EstadoPedido.PROCESANDO);
        return p;
    }
}