package com.koigroup.sistema_pedidos.services;

import com.koigroup.sistema_pedidos.entities.Categoria;
import com.koigroup.sistema_pedidos.entities.Descuento;
import com.koigroup.sistema_pedidos.repositories.DescuentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DescuentoService {

    private final DescuentoRepository descuentoRepository;

    public BigDecimal applyDescuento(BigDecimal precio, Categoria categoria) {

        Optional<Descuento> descuento = descuentoRepository.findByCategoria(categoria);
        if (descuento.isPresent()) {
            BigDecimal porcentaje = descuento.get().getPorcentaje();
            BigDecimal descuentoAplicar = precio.multiply(porcentaje).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            return precio.subtract(descuentoAplicar);
        }

        return precio;
    }
}
