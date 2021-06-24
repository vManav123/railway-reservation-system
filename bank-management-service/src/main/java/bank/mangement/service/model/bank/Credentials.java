package bank.mangement.service.model.bank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@Setter
@Getter
@ToString
@Document(collection = "Credentials")
public class Credentials implements UserDetails {
    @Id
    private String username;
    private String password;
    private Long user_id;
    private String roles;

    public Credentials(String username, String password, Long user_id, String roles) {
        this.username = username;
        this.password = password;
        this.user_id = user_id;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> role = new ArrayList<>();
        role.add(new SimpleGrantedAuthority(roles));
        return role;
    }


    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
}
