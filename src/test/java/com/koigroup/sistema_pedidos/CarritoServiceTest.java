package com.koigroup.sistema_pedidos;

import com.koigroup.sistema_pedidos.DTO.request.NuevoProductoRequest;
import com.koigroup.sistema_pedidos.entities.Carrito;
import com.koigroup.sistema_pedidos.entities.CarritoItem;
import com.koigroup.sistema_pedidos.entities.Producto;
import com.koigroup.sistema_pedidos.entities.Usuario;
import com.koigroup.sistema_pedidos.exception.CarritoItemNoEncontradoException;
import com.koigroup.sistema_pedidos.exception.CarritoNoEncontradoException;
import com.koigroup.sistema_pedidos.exception.ProductoNoEncontradoException;
import com.koigroup.sistema_pedidos.exception.UsuarioNoEncontradoException;
import com.koigroup.sistema_pedidos.repositories.CarritoRepository;
import com.koigroup.sistema_pedidos.services.CarritoItemService;
import com.koigroup.sistema_pedidos.services.CarritoService;
import com.koigroup.sistema_pedidos.services.ProductoService;
import com.koigroup.sistema_pedidos.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private ProductoService productoService;
    @Mock
    private CarritoItemService carritoItemService;

    @InjectMocks
    private CarritoService carritoService;

    // --- createCarritoForUsuario ---

    @Test
    void createCarritoForUsuario_usuarioExiste_guardaCarritoConUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario));

        carritoService.createCarritoForUsuario(1L);

        ArgumentCaptor<Carrito> captor = ArgumentCaptor.forClass(Carrito.class);
        verify(carritoRepository).save(captor.capture());
        assertEquals(usuario, captor.getValue().getUsuario());
    }

    @Test
    void createCarritoForUsuario_usuarioNoExiste_lanzaExcepcion() {
        when(usuarioService.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class,
                () -> carritoService.createCarritoForUsuario(99L));
    }

    // --- addProducto ---

    @Test
    void addProducto_carritoYProductoExisten_guardaItemConCantidad() {
        Carrito carrito = new Carrito();
        carrito.setItems(new ArrayList<>());

        Producto producto = new Producto();
        producto.setCodigo("TEC-001");

        NuevoProductoRequest request = new NuevoProductoRequest();
        request.setCodigoCarrito("CAR-001");
        request.setCodigoProducto("TEC-001");
        request.setCantidadProducto(3);

        when(carritoRepository.findByCodigo("CAR-001")).thenReturn(Optional.of(carrito));
        when(productoService.findByCodigo("TEC-001")).thenReturn(Optional.of(producto));

        carritoService.addProducto(request);

        ArgumentCaptor<CarritoItem> captor = ArgumentCaptor.forClass(CarritoItem.class);
        verify(carritoItemService).saveItem(captor.capture());
        assertEquals(3, captor.getValue().getCantidad());
        verify(carritoRepository).save(carrito);
    }

    @Test
    void addProducto_carritoNoEncontrado_lanzaExcepcion() {
        NuevoProductoRequest request = new NuevoProductoRequest();
        request.setCodigoCarrito("CAR-999");
        request.setCodigoProducto("TEC-001");
        request.setCantidadProducto(1);

        when(carritoRepository.findByCodigo("CAR-999")).thenReturn(Optional.empty());

        assertThrows(CarritoNoEncontradoException.class,
                () -> carritoService.addProducto(request));
    }

    @Test
    void addProducto_productoNoEncontrado_lanzaExcepcion() {
        Carrito carrito = new Carrito();
        carrito.setItems(new ArrayList<>());

        NuevoProductoRequest request = new NuevoProductoRequest();
        request.setCodigoCarrito("CAR-001");
        request.setCodigoProducto("TEC-999");
        request.setCantidadProducto(1);

        when(carritoRepository.findByCodigo("CAR-001")).thenReturn(Optional.of(carrito));
        when(productoService.findByCodigo("TEC-999")).thenReturn(Optional.empty());

        assertThrows(ProductoNoEncontradoException.class,
                () -> carritoService.addProducto(request));
    }

    // --- deleteProducto ---

    @Test
    void deleteProducto_itemExiste_eliminaItem() {
        CarritoItem item = new CarritoItem();

        when(carritoItemService.findByCarritoAndProducto("CAR-001", "TEC-001"))
                .thenReturn(Optional.of(item));

        carritoService.deleteProducto("TEC-001", "CAR-001");

        verify(carritoItemService).deleteItem(item);
    }

    @Test
    void deleteProducto_itemNoExiste_lanzaExcepcion() {
        when(carritoItemService.findByCarritoAndProducto("CAR-001", "TEC-999"))
                .thenReturn(Optional.empty());

        assertThrows(CarritoItemNoEncontradoException.class,
                () -> carritoService.deleteProducto("TEC-999", "CAR-001"));
    }
}