package railway.application.system.models.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccommodationForm {
    private UserForm userForm;
    private long pnr;
    private String room_type;
}
