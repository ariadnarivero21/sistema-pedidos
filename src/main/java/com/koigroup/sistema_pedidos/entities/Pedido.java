package com.koigroup.sistema_pedidos.entities;


import com.koigroup.sistema_pedidos.enums.EstadoPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PEDIDO")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "CARRITO_ID")
    private Carrito carrito;

    @Column(name = "PRECIO_FINAL")
    private BigDecimal precioFinal;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO")
    private EstadoPedido estadoPedido;

    @Column(name = "FECHA")
    private LocalDateTime fecha;

    @Column(name = "MENSAJE_ERROR")
    private String mensajeError;
}
