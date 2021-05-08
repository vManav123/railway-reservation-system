package railway.application.system.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApplicationServiceImpl implements ApplicationService{

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getTrain(String train_info) {
        return restTemplate.getForObject("http://localhost:8085/trains/displaytrain/"+train_info,String.class);
    }

    @Override
    public String trainTimeTable(String station) {
        return restTemplate.getForObject("http://localhost:8085/trains/timeTableByYourCity/"+station,String.class);
    }

    @Override
    public String trainBetweenStation(String origin, String destination) {
        return restTemplate.getForObject("http://localhost:8085/trains/trainBetweenStation/"+origin+":"+destination,String.class);
    }

    @Override
    public String trainFares(String origin, String destination) {
        return restTemplate.getForObject("http://localhost:8085/trains/trainFares/"+origin+":"+destination,String.class);
    }

    @Override
    public String trainLocation(String train_info, String your_location, String day) {
        return restTemplate.getForObject("http://localhost:8085/trains/timeTableByYourCity/"+train_info+":"+your_location+":"+day,String.class);
    }
}
