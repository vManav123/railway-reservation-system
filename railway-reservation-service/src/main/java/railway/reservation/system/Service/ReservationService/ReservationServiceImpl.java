package railway.reservation.system.Service.ReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import railway.reservation.system.ExceptionHandling.*;
import railway.reservation.system.Models.Ticket.Ticket;
import railway.reservation.system.Models.Ticket.TicketForm;
import railway.reservation.system.Models.Train.Train;
import railway.reservation.system.Repository.ReservatedTicketRepository;
import railway.reservation.system.Service.SequenceGenerator.DataSequenceGeneratorService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ReservationServiceImpl implements ReservationService{

    @Autowired
    private ReservatedTicketRepository reservatedTicketRepository;

    @Autowired
    private DataSequenceGeneratorService dataSequenceGeneratorService;

    @Autowired
    private RestTemplate restTemplate;


    // *---------------------------- Exception Messages -------------------------*
    String noTrainsExistException = " !!! There is no train exist on this train Information !!!";
    String trainNotRunningOnThisDayException = "!!! This train is not running usually on that day !!!";
    String trainClassNotExistException = "!!! This Train class not exist in our System !!!";
    String invalidContactNumberException = "!!! Invalid Contact Number !!!";
    String stationNotExistExcception = "!!! These Station doest Exist Please, Select Station Wisely !!!";
    String invalidDateException = "!!! Date is Invalid !!!";
    String seatLimitOutOfBoundException = "!!! Maximum 6 seats can be reserved at a time";
    String invalidQuotaException = "!!! Invalid Quota!!!";
    // *-------------------------------------------------------------------------*



    public boolean isNumeric(String train_search_info)
    {
        try {
            Double.parseDouble(train_search_info);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public String reserveTicket(TicketForm ticketForm) {

        ResponseEntity<Train[]> responseEntity = restTemplate.getForEntity("http://Railway-reservation-Service/trains/displayAllTrains",Train[].class);
        List<Train> trains = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

            try{
                if(ticketForm.getPassengers().stream()
                             .filter(p -> isNumeric(p.getContact_no()))
                             .count()==0
                )
                    throw new InvalidContactNumberException(invalidContactNumberException);

                if(trains.stream()
                         .noneMatch(p -> p.getTrain_no().equals(ticketForm.getTrain_no()) && p.getTrain_name().equalsIgnoreCase(ticketForm.getTrain_name())))
                    throw new NoTrainExistException(noTrainsExistException);

                if(trains.stream()
                        .filter(p -> p.getTrain_name().equals(ticketForm.getTrain_name()))
                        .collect(Collectors.toList())
                        .get(0)
                        .getRoute()
                        .keySet()
                        .stream()
                        .filter(p -> p.equalsIgnoreCase(ticketForm.getStart()) || p.equalsIgnoreCase(ticketForm.getDestination()))
                        .count() != 2
                )
                    throw new StationNotExistExcception(stationNotExistExcception);
                if(!ticketForm.getPassengers().stream()
                                            .allMatch(q -> trains.stream()
                                                                 .filter(p -> p.getTrain_name().equals(ticketForm.getTrain_name()))
                                                                 .collect(Collectors.toList())
                                                                 .get(0).getCoaches_fair()
                                                                 .containsKey(q.getClass_name())
                ))
                    throw new TrainClassNotExistException(trainClassNotExistException);

                if(ticketForm.getReservation_date().isBefore(LocalDate.now()))
                    throw new InvalidDateException(invalidDateException + "You can not book tickets on a date before today");

                if(trains.stream()
                        .filter(p -> p.getTrain_name().equalsIgnoreCase(ticketForm.getTrain_name()))
                        .collect(Collectors.toList())
                        .get(0)
                        .getRun_days()
                        .contains(ticketForm.getReservation_date().getDayOfWeek().toString().substring(3))
                )
                    throw new TrainNotRunningOnThisDayException(trainNotRunningOnThisDayException);

                if(ticketForm.getPassengers().size()>6)
                    throw new SeatLimitOutOfBoundException(seatLimitOutOfBoundException);

                if(!ticketForm.getPassengers().stream()
                                             .filter(p->"Female".equalsIgnoreCase(p.getSex()))
                                             .allMatch(q -> "ladies".equalsIgnoreCase(q.getQuota()))
                  )
                    throw new InvalidQuotaException(invalidDateException+"Ladies Quota Applicable for Female gender ");

                if(!ticketForm.getPassengers().stream()
                                             .filter(p->"senior citizen".equalsIgnoreCase(p.getQuota()))
                                             .allMatch(p->p.getAge()>=58))
                    throw new InvalidQuotaException(invalidDateException+"Senior Citizen Quota is not Applicable for People has age less than 58 years");
            }
            catch (InvalidContactNumberException | InvalidQuotaException | SeatLimitOutOfBoundException | NoTrainExistException | InvalidDateException | TrainNotRunningOnThisDayException | TrainClassNotExistException | StationNotExistExcception e)
            {
                return e.getMessage();
            }

        // problem : if more people is filling ticket form for same info again

        Train train = trains.stream().filter(p->p.getTrain_no().equals(ticketForm.getTrain_no())).collect(Collectors.toList()).get(0);
        List<Ticket> tickets = new ArrayList<>();
        ticketForm.getPassengers().forEach(p -> {
            Ticket ticket = new Ticket();
            ticket.setTrain_no(ticketForm.getTrain_no());
            ticket.setTrain_name(ticketForm.getTrain_name());
            ticket.setStart(ticketForm.getStart());
            ticket.setDestination(ticketForm.getDestination());
            ticket.setClass_name(p.getClass_name());
            ticket.setDeparture_time(LocalDateTime.of(ticketForm.getReservation_date(),train.getRoute().get(ticket.getStart()).getDeparture_time()));
            ticket.setArrival_time(LocalDateTime.of(ticketForm.getReservation_date(),train.getRoute().get(ticket.getStart()).getArrival_time()));
            ticket.setPassenger_name(p.getPassenger_name());
            ticket.setContact_no(p.getContact_no());
            ticket.setAge(p.getAge());
            ticket.setDate_of_journey(ticketForm.getReservation_date());
            ticket.setSex(p.getSex());
            ticket.setQuota(p.getQuota());
        });



        return null;
    }
}
