package com.koigroup.sistema_pedidos.enums;

import java.util.Set;

public enum EstadoPedido {
    INICIAL{
      @Override
      public Set<EstadoPedido> transicionesValidas(){
          return Set.of(PROCESANDO);
      }
    },
    PROCESANDO {
        @Override
        public Set<EstadoPedido> transicionesValidas() {
            return Set.of(COMPLETADO, CANCELADO, ERROR);
        }
    },
    COMPLETADO {
        @Override
        public Set<EstadoPedido> transicionesValidas() {
            return Set.of();
        }
    },
    ERROR {
        @Override
        public Set<EstadoPedido> transicionesValidas() {
            return Set.of(PROCESANDO);
        }
    },
    CANCELADO {
        @Override
        public Set<EstadoPedido> transicionesValidas() {
            return Set.of();
        }
    };

    //El Set<>, a diferencia del List<>, No puede tener repetidos.
    public abstract Set<EstadoPedido> transicionesValidas();

    public EstadoPedido transicionarA(EstadoPedido estadoPedido) {
        if (!transicionesValidas().contains(estadoPedido)) {
            throw new IllegalStateException(
                    "Transición inválida para estado " + this + " -> " + estadoPedido
            );
        }
        return estadoPedido;
    }
}
