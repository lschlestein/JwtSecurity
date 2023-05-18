package com.lucas.security.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data//@ToString @EqualsAndHashCode @Getter @Setter
@Builder //The @Builder annotation produces complex builder APIs for your classes
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tb_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue//auto, identity entity number, sequence with database, table create table, uuid java
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)//converts a number of a colletction into a string to persist into the database
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;//non expired TRUE
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;// non locked
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
