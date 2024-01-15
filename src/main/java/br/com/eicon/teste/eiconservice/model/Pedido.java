package br.com.eicon.teste.eiconservice.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "Pedidos")
@Table(name = "pedidos")
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer numeroControle;
    @Column(nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();
    private String nome;    
    private Double valor;
    private Integer quantidade;
    private Integer codigoCliente;
    private Double valorTotal;

    public Double calcularValorTotal() {
        this.valorTotal = this.valor * this.quantidade;
        if (this.quantidade > 5 && this.quantidade < 10) {
            this.valorTotal *= 0.95; // Aplica desconto de 5%
        } else if (this.quantidade >= 10) {
            this.valorTotal *= 0.9; // Aplica desconto de 10%
        }
        return this.valorTotal;
    }
}
