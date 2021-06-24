package railway.reservation.system.service.reservationService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import railway.reservation.system.exceptions.InvalidTrainNoException;
import railway.reservation.system.model.ticket.*;
import railway.reservation.system.model.train.Train;
import railway.reservation.system.repository.AccommodationRepository;
import railway.reservation.system.repository.ReservedSeatsRepository;
import railway.reservation.system.repository.ReservedTicketRepository;
import railway.reservation.system.repository.TrainRepository;
import railway.reservation.system.service.sequenceGenerator.DataSequenceGeneratorService;
import railway.reservation.system.service.trainSeatService.TrainSeatService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    // *---------------------------- Exception Messages -------------------------*
    String noTrainsExistException = " !!! There is no train exist on this train Information !!!";
    String trainNotRunningOnThisDayException = "!!! This train is not running usually on that day !!!";
    String trainClassNotExistException = "!!! This Train class not exist in our System !!!";
    String invalidContactNumberException = "!!! Invalid Contact Number !!!";
    String stationNotExistException = "!!! These Station doest Exist Please, Select Station Wisely !!!";
    String invalidDateException = "!!! Date is Invalid !!!";
    String seatLimitOutOfBoundException = "!!! Maximum 6 seats can be reserved a t a time";
    String invalidQuotaException = "!!! Invalid Quota!!!";
    String invalidTrainNoException = "!!! Invalid Train No !!!";
    // *--------------------------------------------------------------------------*



    // *-------------------- Autowiring reference Variables ----------------------*
    @Autowired
    private ReservedTicketRepository reservedTicketRepository;
    @Autowired
    private ReservedSeatsRepository reservedSeatsRepository;
    @Autowired
    private TrainSeatService trainSeatService;
    @Autowired
    private DataSequenceGeneratorService dataSequenceGeneratorService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ReservedTicket reservedTicket;
    @Autowired
    private TrainRepository trainRepository;
    @Autowired
    private AccommodationRepository accommodationRepository;
    @Autowired
    private Accommodation accommodation;
    // *-------------------------------------------------------------------------*




    // *------------------------- Reservation Functionality ----------------------*
    public boolean isNumeric(String train_search_info) {
        try {
            Double.parseDouble(train_search_info);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public TicketStatus reserveTicket(Ticket ticket) {
        ticket.setPnr(dataSequenceGeneratorService.getUserSequenceNumber("ticket_sequence"));
        log.info("Journey Date : "+ticket.getDate_of_journey().toString());
        String status = reserveSeat(ticket.getTrain_no(), ticket.getClass_name(), ticket.getDate_of_journey(), ticket.getPnr());
        ticket.setStatus(Arrays.stream(status.split(":")).collect(Collectors.toList()).get(0));
        return new TicketStatus(ticket.getPnr(),ticket.getStatus(),Arrays.stream(status.split(":")).collect(Collectors.toList()).get(1));
    }

    public ReserveSeats getReserveSeats() {
        return new ReserveSeats();
    }

    @Override
    public String reserveSeat(String train_no, String class_name, LocalDate date, Long pnr) {
        log.info("Journey Date : "+date.toString());
        Trains_Seats trains_seats = trainSeatService.getTrainSeats(train_no);
        int seat_no = 0;
        ReserveSeats reserveSeats;
        boolean chk = false;
        try {
            reserveSeats = reservedSeatsRepository.findAll().parallelStream().filter(p -> p.getSeat_id().getTrain_no().equals(train_no) && p.getSeat_id().getReservation_date().equals(date)).collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            reserveSeats = getReserveSeats();
            chk = true;
        }
        if (chk) {
            reserveSeats.setSeat_id(new Seat_Id(train_no, date));
            reserveSeats.setCoaches_per_class(trains_seats.getCoaches_per_class());
            Map<String, Map<Integer, Long>> seats = new Hashtable<>();
            reserveSeats.setSeats(seats);
            for (Map.Entry<String, Integer> map : reserveSeats.getCoaches_per_class().entrySet()) {
                Map<Integer, Long> seat_per_pnr = new Hashtable<>();
                seat_per_pnr.put(0, 0L);
                if (map.getKey().equals(class_name)) {
                    seat_no=1;
                    seat_per_pnr.put(1, pnr);
                    reserveSeats.getSeats().put(map.getKey(), seat_per_pnr);
                    seat_per_pnr.remove(map.getKey());
                    continue;
                }
                reserveSeats.getSeats().put(map.getKey(), seat_per_pnr);
            }
        } else {

            int seat = -1;
            for(Map.Entry<Integer,Long> map : reserveSeats.getSeats().get(class_name).entrySet())
            {
                if(map.getValue()==-1)
                {
                    seat=map.getKey();
                }
            }
            if(seat>0)
            {
                seat_no = seat;
                reserveSeats.getSeats().get(class_name).put(seat, pnr);
            }
            else {
                seat_no = (Collections.max(reserveSeats.getSeats().get(class_name).keySet()) + 1)%(trains_seats.getSeats_per_coach().get(class_name)/trains_seats.getCoaches_per_class().get(class_name));
                reserveSeats.getSeats().get(class_name).put(Collections.max(reserveSeats.getSeats().get(class_name).keySet()) + 1, pnr);
            }
        }
        reservedSeatsRepository.save(reserveSeats);
        if (trains_seats.getSeats_per_coach().get(class_name) > Collections.max(reserveSeats.getSeats().get(class_name).keySet()))
            return "Confirmed"+":"+seat_no;
        else
            return "Waiting"+":"+seat_no;
    }

    @Override
    public String reservedTicket(ReservedTicket reservedTicket) {
        reservedTicketRepository.save(reservedTicket);
        return "success";
    }

    @Override
    public ReservedTicket getTicket(long pnr) {
        return reservedTicketRepository.findAll().parallelStream().filter(p->p.getPnr().equals(pnr)).collect(Collectors.toList()).get(0);
    }

    @Override
    public boolean ticketExistByPNR(long pnr) {
        return !reservedTicketRepository.findAll().parallelStream().filter(p->p.getPnr().equals(pnr)).collect(Collectors.toList()).isEmpty();
    }

    @Override
    public String ticketCancellation(long pnr) {
        ReservedTicket reservedTicket;
        if(reservedTicketRepository.existsById(pnr))
            reservedTicket = reservedTicketRepository.findById(pnr).get();
        else
            return "!!! Invalid PNR !!! , Please write Information correctly.";
        reservedTicketRepository.deleteById(pnr);
        return "success";
    }

    @Override
    public String seatCancellation(String seat_no, String class_name,Seat_Id seat_id) {
        ReserveSeats reserveSeats = reservedSeatsRepository.findById(seat_id).get();
        reserveSeats.getSeats().get(class_name).computeIfPresent(Integer.parseInt(seat_no),(key,value)-> (long) -1);
        reservedSeatsRepository.save(reserveSeats);
        return "success";
    }

    @Override
    public String availableSeats(String start,String destination,LocalDate date) {

        StringBuilder result = new StringBuilder();
        List<Train> trainList = trainRepository.findAll().parallelStream().filter(p-> p.getRoute().keySet().parallelStream().filter(q-> start.equalsIgnoreCase(q) || destination.equals(q)).count()==2).collect(Collectors.toList());
        result.append("-----------------------------------------------------------------------------------------------------------\n");
        result.append("------------------------------- These are the Train Present on the Route ----------------------------------\n");
        result.append("-----------------------------------------------------------------------------------------------------------\n\n");
        trainList.forEach(p->{
            boolean chk = false;
            Trains_Seats trains_seats = trainSeatService.getTrainSeats(p.getTrain_no());
            List<ReserveSeats> reserveSeats = reservedSeatsRepository.findAll().parallelStream().filter(q->q.getSeat_id().toString().equals((new Seat_Id(p.getTrain_no(),date)).toString())).collect(Collectors.toList());
            if(reserveSeats.isEmpty())
                chk=true;

            if(!chk)
            {
                Map<String,Integer> seats = new Hashtable<>();
                ReserveSeats reserveSeats1 = reserveSeats.get(0);
                for (Map.Entry<String,Integer> map : trains_seats.getSeats_per_coach().entrySet())
                {
                    seats.put(map.getKey(),map.getValue()-Collections.max(reserveSeats1.getSeats().get(map.getKey()).keySet()));
                }
                result.append("Train No / Train Name : ").append(p.getTrain_no()).append(" / ").append(p.getTrain_name()).append("   Trip   ").append(p.getStart_from()).append(" -------> ").append(p.getTo_destination()).append("\n");
                result.append("Train Running Days : ").append(p.getRun_days()).append("\nTrain Departure : ").append(p.getDeparture_time()).append("\nTrain Arri;val : ").append(p.getArrival_time()).append("\nTrain Available Seats : ").append(seats).append("\n\n");
                result.append("-----------------------------------------------------------------------------------------------------------\n\n");
            }
            else {
                ResultSeatAvailable(result, p, trains_seats);
            }
        });

        return result.toString();
    }

    private void ResultSeatAvailable(StringBuilder result, Train p, Trains_Seats trains_seats) {
        result.append("Train No / Train Name : ").append(p.getTrain_no()).append(" / ").append(p.getTrain_name()).append("   Trip   ").append(p.getStart_from()).append(" -------> ").append(p.getTo_destination()).append("\n");
        result.append("Train Running Days : ").append(p.getRun_days()).append("\nTrain Departure : ").append(p.getDeparture_time()).append("\nTrain Arrival : ").append(p.getArrival_time()).append("\nTrain Available Seats : ").append(trains_seats.getSeats_per_coach()).append("\n\n");
        result.append("-----------------------------------------------------------------------------------------------------------\n\n");
    }

    @Override
    public String availableAccommodation(String train_no, LocalDate date) {
        boolean chk = trainRepository
                                .findAll()
                                .parallelStream()
                                .filter(p->p.getTrain_no().equals(train_no))
                                .count()==1;
       try
       {
           if(!chk)
               throw new InvalidTrainNoException(invalidTrainNoException);

       }
       catch (InvalidTrainNoException e)
       {
           return e.getMessage();
       }
       StringBuilder result = new StringBuilder();
       result.append("\n\n  ------------------------------ Accommodation for The Route of This Train No  : "+train_no+" ---------------------------------\n");
       accommodationRepository
               .findAll()
               .parallelStream()
               .filter(p->p.getTrain_no().equals(train_no))
               .collect(Collectors.toList())
               .get(0)
               .getAccommodations()
               .forEach(p->{
                   if(p.getTotal_rooms()>0)
                   {
                       result.append("\n------------------------------------------------------------------------------------------------------------------\n\n");

                       result.append("                Station Name : "+p.getStation_name()+ "      Total no of Rooms: "+p.getTotal_rooms()+"  status : "+p.getStatus()+"\n\n        ---------------------------------------- *** -----------------------------------------\n");
                       for (Map.Entry<String,RoomDetail> map : p.getAccommodation().entrySet())
                       {
                           int price = 0;
                           if(map.getKey().equals("Luggage Room"))
                               continue;
                           else if(map.getKey().equals("AC Room"))
                               price=300;
                           else if(map.getKey().equals("Cooler Room"))
                               price=150;
                           result.append("                Room Type : "+map.getKey()+"    =>   Beds : "+map.getValue().getBeds()+"   Bath Room : "+map.getValue().getBath_room() + "  Toilets : " + map.getValue().getToilet() + "\n");
                           result.append("                Room Price : "+map.getValue().getPrice()+price+"\n        ---------------------------------------- *** -----------------------------------------\n");
                       }
                   }
                });
        return result.toString();
    }
}
