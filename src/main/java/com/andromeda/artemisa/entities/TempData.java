package com.andromeda.artemisa.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "temp_data")
public class TempData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "key_name", nullable = false, length = 200)
    private String key;
    @Lob
    private String payload;
    @Column(name = "create_at", nullable = false, updatable = false)
    private Instant createAt;
    @Column(name = "update_at", nullable = false)
    private Instant updateAt;

    public TempData(Instant createAt, Instant updateAt, Long id, String key, String payload) {
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.id = id;
        this.key = key;
        this.payload = payload;
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getPayload() {
        return payload;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public static class Builder {

        private Long id;
        private String key;
        private String payload;
        private Instant createAt;
        private Instant updateAt;

        public Builder() {

        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder payload(String payload) {
            this.payload = payload;
            return this;
        }

        public Builder createAt() {
            this.createAt = Instant.now();
            return this;
        }

        public Builder updateAt(Instant updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public TempData build() {
            TempData tempData = new TempData(this.createAt, this.updateAt, this.id, this.key, this.payload);
            return tempData;
        }

    }

}
