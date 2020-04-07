package com.milosdevelopmetn.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime reservationDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Calendar date;

    @NotNull
    private int numbOfDays;

    @NotEmpty
    private String price;

    @OneToOne(mappedBy = "reservation")
    @JsonManagedReference
    private Room room;

    @ManyToOne
    private User user;

    @NotNull
    private boolean arrived;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Calendar leaving;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReservationDate(LocalDateTime reservationDate){
        this.reservationDate = reservationDate;
    }

    public LocalDateTime getReservationDate(){
        return this.reservationDate;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getNumbOfDays() {
        return numbOfDays;
    }

    public void setNumbOfDays(int numbOfDays) {
        this.numbOfDays = numbOfDays;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean getArrived(){
        return this.arrived;
    }

    public void setArrived(boolean arrived){
        this.arrived = arrived;
    }

    public Calendar getLeaving() {
        return leaving;
    }

    public void setLeaving(Calendar leaving) {
        this.leaving = leaving;
    }
}
