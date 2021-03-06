package com.github.zxxz_ru.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;

@Entity
@Table(name = "USER")
@JsonDeserialize(using = UserDeserializer.class)
public class User implements StoreUnit {
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FIRST_NAME")
    private String firstname;

    @Column(name = "LAST_NAME")
    private String lastname;

    @Column(name = "USER_ROLE")
    private String role;

    public User() {
    }

    public User(Integer id, String firstname, String lastname, String role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    @Override
    public <T extends StoreUnit> T from(T u) {
        User n = new User();
        User usr = (User) u;
        if ((usr.getId() != null)) {
            n.setId(usr.getId());
        } else {
            n.setId(this.id);
        }
        if (usr.getFirstName() != null && !usr.getFirstName().equals("")) {
            n.setFirstName(usr.getFirstName());
        } else {
            n.setFirstName(this.firstname);
        }
        if (usr.getLastName() != null && !usr.getLastName().equals("")) {
            n.setLastName(usr.getLastName());
        } else {
            n.setLastName(this.lastname);
        }
        if (usr.getRole() != null && !usr.getRole().equals("")) {
            n.setRole(usr.getRole());
        } else {
            n.setRole(this.role);
        }
        return (T) n;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("\nUser ID: ").append(this.id)
                .append("\nFirst Name: ").append(this.firstname)
                .append("\nLast Name: ").append(this.lastname)
                .append("\nUser Role: ").append(this.role)
                .append("\n\n").substring(0);
    }

    // setters & getters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User u = (User) o;
        return this.id.equals(u.getId()) && this.firstname.equals(u.getFirstName()) &&
                this.lastname.equals(u.getLastName()) && this.role.equals(u.getRole());
    }

    @Override
    public int hashCode() {
        int id = this.id == null ? 1 : this.id;
        return 7 * (this.role.hashCode() + this.lastname.hashCode() + this.firstname.hashCode()) + id;
    }
}
