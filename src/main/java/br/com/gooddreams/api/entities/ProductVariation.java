package br.com.gooddreams.api.entities;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="product_variation")
public class ProductVariation {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String size;
    private Integer stock;

    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    public ProductVariation() {}

    public ProductVariation(Long id, String size, Integer stock, Product product) {
        this.id = id;
        this.stock = stock;
        this.product = product;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSize() { return size;}
    public void setSize(String size) { this.size = size;}
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
}
