package com.koigroup.sistema_pedidos;

import com.koigroup.sistema_pedidos.entities.Categoria;
import com.koigroup.sistema_pedidos.entities.Descuento;
import com.koigroup.sistema_pedidos.repositories.DescuentoRepository;
import com.koigroup.sistema_pedidos.services.DescuentoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DescuentoServiceTest {

    @Mock
    private DescuentoRepository descuentoRepository;

    @InjectMocks
    private DescuentoService descuentoService;

    @Test
    void applyDescuento_categoriaConDescuento_retornaPrecioConDescuento() {
        Categoria categoria = new Categoria();
        categoria.setNombre("TECNOLOGIA");

        Descuento descuento = new Descuento();
        descuento.setPorcentaje(new BigDecimal("10.00"));

        when(descuentoRepository.findByCategoria(categoria)).thenReturn(Optional.of(descuento));

        BigDecimal resultado = descuentoService.applyDescuento(new BigDecimal("1000"), categoria);

        // 1000 - (1000 * 10 / 100) = 900
        assertEquals(0, resultado.compareTo(new BigDecimal("900")));
    }

    @Test
    void applyDescuento_categoriaSinDescuento_retornaPrecioOriginal() {
        Categoria categoria = new Categoria();
        categoria.setNombre("HOGAR");

        when(descuentoRepository.findByCategoria(categoria)).thenReturn(Optional.empty());

        BigDecimal resultado = descuentoService.applyDescuento(new BigDecimal("5000"), categoria);

        assertEquals(0, resultado.compareTo(new BigDecimal("5000")));
    }

    @Test
    void applyDescuento_descuentoParcial_calculaCorrectamente() {
        Categoria categoria = new Categoria();
        categoria.setNombre("ALIMENTOS");

        Descuento descuento = new Descuento();
        descuento.setPorcentaje(new BigDecimal("2.50"));

        when(descuentoRepository.findByCategoria(categoria)).thenReturn(Optional.of(descuento));

        BigDecimal resultado = descuentoService.applyDescuento(new BigDecimal("2500"), categoria);

        // 2500 - (2500 * 2.5 / 100) = 2500 - 62.5 = 2437.5
        assertEquals(0, resultado.compareTo(new BigDecimal("2437.5")));
    }
}