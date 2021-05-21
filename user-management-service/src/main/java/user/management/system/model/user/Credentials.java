package user.management.system.model.user;

import lombok.*;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Document(collection = "Credentials")
public class Credentials {
    @Id
    private String username;
    private String password;
    private Long user_id;
    private String roles;
}
