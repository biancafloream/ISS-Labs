package org.example;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "Borrowings")
public class Borrowing implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "idBook")
    private Book book;
    @Id
    @ManyToOne
    @JoinColumn(name = "idReader")
    private Reader reader;
    @Column(name = "borrowingDate")
    private Timestamp borrowingDate;
    @Column(name = "returnDate")
    private Timestamp returnDate;

    public Borrowing() {
    }

    public Borrowing(Book book, Reader reader, Timestamp borrowingDate, Timestamp returnDate) {
        this.book = book;
        this.reader = reader;
        this.borrowingDate = borrowingDate;
        this.returnDate = returnDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Timestamp getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(Timestamp borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "Borrowing{" +
                "book=" + book +
                ", reader=" + reader +
                ", borrowingDate=" + borrowingDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
