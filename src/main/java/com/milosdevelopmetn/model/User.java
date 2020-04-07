package com.milosdevelopmetn.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import javafx.geometry.HorizontalDirection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
public class User implements UserDetails, Comparable<User>
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	@NotEmpty(message = "Polje je obavezno")
	@Size(min = 8, max = 20 , message = "Mora sadrzati od 8 do 20 karaktera")
	private String username;

	@NotEmpty(message = "Polje je obavezno")
	private String name;

	@NotEmpty(message = "Polje je obavezno")
	private String lastName;

	@Column(length = 100)
	@Size(min= 6, message = "Najmanje 6 karaktera")
	@NotEmpty(message = "Polje je obavezno")
	private String password;

	@OneToOne
	@JoinColumn(name = "role_id")
	private Role role;

    @OneToOne(mappedBy = "manager")
	private Hotel hotel;

	@NotNull
	@OneToMany(mappedBy = "user")
	private List<Reservation> reservations = new ArrayList<>();

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role.getName()));
		return authorities;

	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}


    @Override
    public int compareTo(User u) {
        if (getRole().getName() == null || u.getRole().getName() == null) {
            return 0;
        }
        return getRole().getName().compareTo(u.getRole().getName());
    }
}
