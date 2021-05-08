package railway.application.system.Service;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ApplicationService {
    public String getTrain(String train_info);

    public String trainTimeTable(String station);

    public String trainBetweenStation(String origin,String destination);

    public String trainFares(String origin,String destination);

    public String trainLocation(String train_info,String your_location,String day);
}
