package railway.reservation.system.service.trainSeatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import railway.reservation.system.model.ticket.Trains_Seats;
import railway.reservation.system.model.train.Train;
import railway.reservation.system.repository.SeatRepository;
import railway.reservation.system.repository.TrainRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrainSeatServiceImpl implements TrainSeatService {

    public static Random randNum = new Random();
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private TrainRepository trainRepository;
    @Autowired
    private RestTemplate restTemplate;

    // *-------------------------- Train Seat Functionality -----------------------------*
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
        for (int i = 1, j = 0; i < dividers.length; ++i, ++j) {
            res[j] = dividers[i] - dividers[j];
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < res.length; i++)
            list.add(res[i]);
        return list;
    }

    @Override
    public String addData() {
        List<Trains_Seats> trains_seats = new ArrayList<>();
        List<Train> trains = trainRepository.findAll();


        trains.forEach(p -> {
            Map<String, Integer> seat = new Hashtable<>();
            List<String> coaches = new ArrayList<>(p.getCoaches_fair().keySet());
            coaches.forEach(q -> {
                int value = switch (q) {
                    case "ACSL" -> 1;
                    case "PC" -> 1;
                    case "ACLZ" -> 2;
                    case "ACL" -> 4;
                    case "ISL" -> 6;
                    case "EC" -> 12;
                    default -> 10;
                };
                seat.put(q, value * 9);
            });


            Map<String, Integer> coaches_no = new Hashtable<>();
            List<Integer> no = divideNumber(p.getTrain_length(), p.getCoaches_fair().size());
            for (int i = 0; i < coaches.size(); i++)
                coaches_no.put(coaches.get(i), no.get(i));

            for (Map.Entry<String, Integer> map : seat.entrySet()) {
                map.setValue(map.getValue() * coaches_no.get(map.getKey()));
            }

            trains_seats.add(new Trains_Seats(p.getTrain_no(), coaches_no, seat));
        });
        seatRepository.saveAll(trains_seats);
        return "All Data Added Successfully";
    }

    @Override
    public Trains_Seats getTrainSeats(String train_no) {
        return seatRepository.findAll().parallelStream().filter(p -> p.getTrain_no().equals(train_no)).collect(Collectors.toList()).get(0);
    }
    // *----------------------------- end --------------------------------*


}
