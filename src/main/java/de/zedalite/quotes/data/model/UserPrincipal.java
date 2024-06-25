package de.zedalite.quotes.data.model;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record UserPrincipal(User user, Collection<? extends GrantedAuthority> authorities) implements UserDetails {
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public Integer getId() {
    return user.id();
  }

  @Override
  public String getUsername() {
    return user.name();
  }

  @Override
  public String getPassword() {
    return null;
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
}
