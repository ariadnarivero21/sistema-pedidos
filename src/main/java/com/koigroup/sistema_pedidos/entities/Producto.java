package com.koigroup.sistema_pedidos.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "PRODUCTO")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Version
    private Long version;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "PRECIO")
    private BigDecimal precio;

    @Column(name = "STOCK")
    private Integer stock;

    @Column(name = "CODIGO")
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "CATEGORIA_ID")
    private Categoria categoria;

    @OneToMany(mappedBy = "producto")
    private List<CarritoItem> items = new ArrayList<>();
}
