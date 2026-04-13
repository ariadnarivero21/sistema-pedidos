package com.koigroup.sistema_pedidos.services;

import com.koigroup.sistema_pedidos.entities.Categoria;
import com.koigroup.sistema_pedidos.entities.Descuento;
import com.koigroup.sistema_pedidos.repositories.DescuentoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class DescuentoService {

    private final DescuentoRepository descuentoRepository;

    public DescuentoService(DescuentoRepository descuentoRepository) {
        this.descuentoRepository = descuentoRepository;
    }

    public BigDecimal applyDescuento(BigDecimal precio, Categoria categoria) {

        Optional<Descuento> descuento = descuentoRepository.findByCategoria(categoria);
        if (descuento.isPresent()) {
            BigDecimal porcentaje = descuento.get().getPorcentaje();
            BigDecimal descuentoAplicar = precio.multiply(porcentaje).divide(BigDecimal.valueOf(100));
            return precio.subtract(descuentoAplicar);
        }

        return precio;
    }
}
