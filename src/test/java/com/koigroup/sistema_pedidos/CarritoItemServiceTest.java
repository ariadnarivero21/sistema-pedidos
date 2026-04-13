package com.koigroup.sistema_pedidos;

import com.koigroup.sistema_pedidos.DTO.ProductoCarritoDTO;
import com.koigroup.sistema_pedidos.entities.Carrito;
import com.koigroup.sistema_pedidos.entities.CarritoItem;
import com.koigroup.sistema_pedidos.entities.Categoria;
import com.koigroup.sistema_pedidos.entities.Producto;
import com.koigroup.sistema_pedidos.repositories.CarritoItemRepository;
import com.koigroup.sistema_pedidos.services.CarritoItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarritoItemServiceTest {

    @Mock
    private CarritoItemRepository carritoItemRepository;

    @InjectMocks
    private CarritoItemService carritoItemService;

    @Test
    void getAllProductosByCarrito_conItems_retornaListaDTOsCorrectos() {
        Categoria categoria = new Categoria();
        categoria.setNombre("TECNOLOGIA");

        Producto producto = new Producto();
        producto.setNombre("Mouse Logitech");
        producto.setPrecio(new BigDecimal("1500.00"));
        producto.setCodigo("TEC-001");
        producto.setCategoria(categoria);

        CarritoItem item = new CarritoItem();
        item.setProducto(producto);
        item.setCantidad(2);

        when(carritoItemRepository.findByCarrito("CAR-001")).thenReturn(List.of(item));

        List<ProductoCarritoDTO> resultado = carritoItemService.getAllProductosByCarrito("CAR-001");

        assertEquals(1, resultado.size());
        assertEquals("Mouse Logitech", resultado.get(0).getNombre());
        assertEquals("TEC-001", resultado.get(0).getCodigo());
        assertEquals("TECNOLOGIA", resultado.get(0).getCategoria());
        assertEquals(0, resultado.get(0).getPrecio().compareTo(new BigDecimal("1500.00")));
        assertEquals(2, resultado.get(0).getCantidad());
    }

    @Test
    void getAllProductosByCarrito_multiplesItems_retornaTodosLosDTOs() {
        Categoria cat1 = new Categoria();
        cat1.setNombre("TECNOLOGIA");

        Categoria cat2 = new Categoria();
        cat2.setNombre("ALIMENTOS");

        Producto p1 = new Producto();
        p1.setNombre("Mouse Logitech");
        p1.setPrecio(new BigDecimal("1500.00"));
        p1.setCodigo("TEC-001");
        p1.setCategoria(cat1);

        Producto p2 = new Producto();
        p2.setNombre("Coca Cola 2L");
        p2.setPrecio(new BigDecimal("2500.00"));
        p2.setCodigo("ALI-001");
        p2.setCategoria(cat2);

        CarritoItem item1 = new CarritoItem();
        item1.setProducto(p1);
        item1.setCantidad(1);

        CarritoItem item2 = new CarritoItem();
        item2.setProducto(p2);
        item2.setCantidad(3);

        when(carritoItemRepository.findByCarrito("CAR-001")).thenReturn(List.of(item1, item2));

        List<ProductoCarritoDTO> resultado = carritoItemService.getAllProductosByCarrito("CAR-001");

        assertEquals(2, resultado.size());
        assertEquals("TEC-001", resultado.get(0).getCodigo());
        assertEquals("ALI-001", resultado.get(1).getCodigo());
        assertEquals(3, resultado.get(1).getCantidad());
    }

    @Test
    void getAllProductosByCarrito_sinItems_retornaListaVacia() {
        when(carritoItemRepository.findByCarrito("CAR-001")).thenReturn(List.of());

        List<ProductoCarritoDTO> resultado = carritoItemService.getAllProductosByCarrito("CAR-001");

        assertTrue(resultado.isEmpty());
    }
}