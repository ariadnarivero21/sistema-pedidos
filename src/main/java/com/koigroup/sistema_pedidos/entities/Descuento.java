package com.koigroup.sistema_pedidos.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "DESCUENTO")
public class Descuento {

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CATEGORIA_ID", unique = true)
    private Categoria categoria;

    @Column(name = "PORCENTAJE")
    private BigDecimal porcentaje;
}
