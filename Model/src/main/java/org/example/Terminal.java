package org.example;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Terminal")
public class Terminal implements Serializable {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name="id", strategy = "increment")
    private Integer id;
    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    public Terminal() {
    }

    public Terminal(Integer id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name + " at: " + address;
    }
}
