package br.com.gooddreams.api.entities;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name="product_variation_id")
    private ProductVariation productVariation;

    private Integer quantity;
    private BigDecimal unitPrice;

    public CartItem() {}

    public CartItem(Long id, Cart cart, ProductVariation productVariation, Integer quantity, BigDecimal unitPrice){
        this.id = id;
        this.cart = cart;
        this.productVariation = productVariation;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Cart getCart() {
        return cart;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }
    public ProductVariation getProductVariation() {
        return productVariation;
    }
    public void setProductVariation(ProductVariation productVariation) {
        this.productVariation = productVariation;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }





}
