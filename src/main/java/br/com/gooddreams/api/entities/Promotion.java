package br.com.gooddreams.api.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table (name="promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private float discountPercentage;

    @CreatedDate
    private Instant startDate;

    @LastModifiedBy
    private Instant endDate;

    private boolean isActive;

    public Promotion() {}

    @ManyToOne
    @JoinColumn(name="product_variation_id")
    public ProductVariation productVariation;

    public Promotion(Long id, float discountPercentage, Instant startDate, Instant endDate, boolean isActive, ProductVariation productVariation) {
        this.id = id;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.productVariation = productVariation;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public float getDiscountPercentage() {
        return discountPercentage;
    }
    public void setDiscountPercentage(float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Instant getStartDate() {
        return startDate;
    }
    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }
    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public ProductVariation getProductVariation() {
        return productVariation;
    }
    public void setProductVariation(ProductVariation productVariation) {
        this.productVariation = productVariation;
    }


}
