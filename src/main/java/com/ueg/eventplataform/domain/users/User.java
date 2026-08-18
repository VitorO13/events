package com.ueg.eventplataform.domain.users;

import java.util.Collection;
import java.util.List;

import javax.management.relation.Role;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    private String email;

    private String password;

    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN)return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_HOST"));
        else if(this.role == UserRole.HOST)return List.of(new SimpleGrantedAuthority("ROLE_HOST"), new SimpleGrantedAuthority("ROLE_USER"));
        else {return List.of(new SimpleGrantedAuthority("ROLE_USER"));
       
            
        }
    }

    @Override
    public @Nullable String getPassword() {
        if(this.password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        return this.password;
    }

    @Override
    public String getUsername() {
        if(this.email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        return this.email;
    }
  
}
