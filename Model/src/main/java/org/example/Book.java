package org.example;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Books")
public class Book implements Serializable {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name="id", strategy = "increment")
    private Integer id;
    @Column(name = "isbn")
    private String isbn;
    @Column(name = "name")
    private String name;
    @Column(name = "author")
    private String author;
    @Column(name = "noOfCopies")
    private Integer noOfCopies;
    @ManyToOne
    @JoinColumn(name = "idTerminal")
    private Terminal terminal;

    public Book() {

    }

    public Book(Integer id, String isbn, String name, String author, Integer noOfCopies, Terminal terminal) {
        this.id = id;
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.noOfCopies = noOfCopies;
        this.terminal = terminal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getNoOfCopies() {
        return noOfCopies;
    }

    public void setNoOfCopies(Integer noOfCopies) {
        this.noOfCopies = noOfCopies;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", noOfCopies=" + noOfCopies +
                ", terminal=" + terminal +
                '}';
    }
}
