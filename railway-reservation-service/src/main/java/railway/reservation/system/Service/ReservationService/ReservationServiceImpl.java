package railway.reservation.system.Service.ReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import railway.reservation.system.Models.Controller_Body.Payment;
import railway.reservation.system.Models.Ticket.*;
import railway.reservation.system.Repository.ReservedSeatsRepository;
import railway.reservation.system.Repository.ReservedTicketRepository;
import railway.reservation.system.Service.SequenceGenerator.DataSequenceGeneratorService;
import railway.reservation.system.Service.TrainSeatService.TrainSeatService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;


@Service
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
        String status = reserveSeat(ticket.getTrain_no(), ticket.getClass_name(), ticket.getDate_of_journey(), ticket.getPnr());
        ticket.setStatus(Arrays.stream(status.split(":")).collect(Collectors.toList()).get(0));
        return new TicketStatus(ticket.getPnr(),ticket.getStatus(),Arrays.stream(status.split(":")).collect(Collectors.toList()).get(1));
    }

    public ReserveSeats getReserveSeats() {
        return new ReserveSeats();
    }

    @Override
    public String reserveSeat(String train_no, String class_name, LocalDate date, Long pnr) {
        Trains_Seats trains_seats = trainSeatService.getTrainSeats(train_no);
        int seat_no = 0;
        ReserveSeats reserveSeats;
        boolean chk = false;
        try {
            reserveSeats = reservedSeatsRepository.findAll().stream().filter(p -> p.getSeat_id().getTrain_no().equals(train_no) && p.getSeat_id().getReservation_date().equals(date)).collect(Collectors.toList()).get(0);
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
        if (trains_seats.getSeats_per_coach().get(class_name) >= Collections.max(reserveSeats.getSeats().get(class_name).keySet()))
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
        return reservedTicketRepository.findAll().stream().filter(p->p.getPnr().equals(pnr)).collect(Collectors.toList()).get(0);
    }

    @Override
    public boolean ticketExistByPNR(long pnr) {
        return !reservedTicketRepository.findAll().stream().filter(p->p.getPnr().equals(pnr)).collect(Collectors.toList()).isEmpty();
    }

    @Override
    public String ticketCancellation(long pnr) {
        ReservedTicket reservedTicket = reservedTicketRepository.findById(pnr).get();
        reservedTicketRepository.deleteById(pnr);
        return "success";
    }

    @Override
    public String seatCancellation(String seat_no, String class_name,Seat_Id seat_id) {
        ReserveSeats reserveSeats = reservedSeatsRepository.findById(seat_id).get();
        reserveSeats.getSeats().get(class_name).computeIfPresent(Integer.parseInt(seat_no),(key,value)-> Long.valueOf(-1));
        reservedSeatsRepository.save(reserveSeats);
        return "success";
    }
}
