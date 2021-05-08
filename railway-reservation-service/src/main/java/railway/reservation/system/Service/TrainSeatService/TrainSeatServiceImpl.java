package railway.reservation.system.Service.TrainSeatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import railway.reservation.system.Models.Train.Seats;
import railway.reservation.system.Models.Train.Train;
import railway.reservation.system.Models.Train.Trains_Seats;
import railway.reservation.system.Repository.SeatRepository;

import java.util.*;

@Service
public class TrainSeatServiceImpl implements TrainSeatService{

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private RestTemplate restTemplate;

    public static Random randNum = new Random();

    public static List<Integer> divideNumber(int number, int parts) {
        HashSet<Integer> set = new HashSet<Integer>();
        set.add(0);
        set.add(0);
        set.add(0);
        set.add(number);
        int arrSize = parts + 1;
        while (set.size() < arrSize) {
            set.add(1 + randNum.nextInt(number - 1));
        }
        Integer[] dividers = set.toArray(new Integer[arrSize]);
        Arrays.sort(dividers);
        int[] res = new int[parts];
        for(int i = 1, j = 0; i < dividers.length; ++i, ++j) {
            res[j] = dividers[i] - dividers[j];
        }
        List<Integer> list = new ArrayList<>();
        for(int i = 0 ; i < res.length ; i++)
            list.add(res[i]);
        return list;
    }

    @Override
    public String addData() {
        List<Trains_Seats> trains_seats = new ArrayList<>();
        ResponseEntity<Train[]> responseEntity = restTemplate.getForEntity("http://Railway-reservation-Service/trains/displayAllTrains",Train[].class);
        List<Train> trains = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        Map<String, Seats> seat = new Hashtable<>();
        trains.forEach(p->{
            List<String> coaches = p.getCoaches_fair().keySet().stream().toList();
            coaches.forEach(q->{
                Integer value = 0;
                if(q.equals("ACSL"))
                    value = 1;
                else if(q.equals("PC"))
                    value = 1;
                else if(q.equals("ACLZ"))
                    value = 2;
                else if(q.equals("ACL"))
                    value = 4;
                else if(q.equals("ISL"))
                    value = 6;
                else if(q.equals("EC"))
                    value = 12;
                else
                    value = 10;
                seat.put(q,new Seats(value*9,value));
            });
            Map<String,Integer> coaches_no = new Hashtable<>();
            List<Integer> no = divideNumber(p.getTrain_length(),p.getCoaches_fair().size());
            for(int i = 0 ; i < coaches.size() ;i++)
                coaches_no.put(coaches.get(i),no.get(i));


            trains_seats.add(new Trains_Seats(p.getId(),p,seat,coaches_no));
        });
        seatRepository.saveAll(trains_seats);
        return "All Data Added Successfully";
    }



}
