package com.cosmoneural.data.mongottl;


import com.querydsl.core.annotations.QueryEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@QueryEntity
@Document
public class Offer {
    public enum OfferType {
        SIMPLE, COMPLEX
    }

    @Id
    private String id;
    private String name;
    private OfferType offerType;
    private int grade;
    @Indexed(name="OfferCreateDateTime",expireAfterSeconds=10)
    private Date createDate;

    public Offer(String id, String name, OfferType offerType, int grade) {
        this.id = id;
        this.name = name;
        this.offerType = offerType;
        this.grade = grade;
    }

    public Offer(String id, String name, OfferType offerType, int grade, Date createDate) {
        this.id = id;
        this.name = name;
        this.offerType = offerType;
        this.grade = grade;
        this.createDate = createDate;
    }

    public Offer(){}

    public Offer(String id, String name, int grade) {
        this.id = id;
        this.name = name;
        this.offerType = OfferType.COMPLEX;
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OfferType getOfferType() {
        return offerType;
    }

    public void setOfferType(OfferType offerType) {
        this.offerType = offerType;
    }

    public int getGrade() {
        return grade;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
