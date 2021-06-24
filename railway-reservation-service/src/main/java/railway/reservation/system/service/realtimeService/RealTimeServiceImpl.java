package railway.reservation.system.service.realtimeService;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import railway.reservation.system.model.ticket.ReserveSeats;
import railway.reservation.system.model.ticket.ReservedTicket;
import railway.reservation.system.model.ticket.Ticket;
import railway.reservation.system.repository.ReservedSeatsRepository;
import railway.reservation.system.repository.ReservedTicketRepository;
import railway.reservation.system.repository.SeatRepository;
import railway.reservation.system.service.emailService.EmailService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RealTimeServiceImpl implements RealTimeService
{

    @Autowired
    private ReservedSeatsRepository reservedSeatsRepository;

    @Autowired
    private ReservedTicketRepository reservedTicketRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmailService emailService;



    @Override
    public boolean checkIfAny_Ticket_Is_In_WaitingMode() {
        return reservedTicketRepository.findAll().parallelStream().anyMatch(p -> p.getStatus().equals("Waiting"));
    }

    @Override
    public boolean checkIfAny_Ticket_Is_Cancelled() {
        return reservedSeatsRepository.findAll().parallelStream().anyMatch(p->{
            Map<String , Map<Integer, Long>> map = p.getSeats();
            for (Map.Entry<String , Map<Integer, Long>> e : map.entrySet())
            {
                for(Map.Entry<Integer,Long> newMap : e.getValue().entrySet())
                {
                    if(newMap.getValue()==-1L)
                        return true;
                }
            }
            log.info("There is no cancelled seat found");
            return false;
        });
    }

    // ❕------------ Overdue ticket deletion process --------------❕
    @Override
    public boolean any_Ticket_Is_OverDue() {
        return reservedTicketRepository.findAll().parallelStream().anyMatch(p -> p.getTicket().getDate_of_journey().isBefore(LocalDate.now()));
    }

    @Override
    public String delete_Overdue_Ticket() {
        List<ReservedTicket> reservedTickets = reservedTicketRepository.findAll().parallelStream().filter(p->p.getTicket().getDate_of_journey().isBefore(LocalDate.now())).collect(Collectors.toList());
        List<ReserveSeats> reserveSeats = reservedSeatsRepository.findAll().parallelStream().filter(p->p.getSeat_id().getReservation_date().isBefore(LocalDate.now())).collect(Collectors.toList());

        reservedTicketRepository.deleteAll(reservedTickets);
        reservedSeatsRepository.deleteAll(reserveSeats);
        return "success";
    }
    // ❕---------------------------------------------------------------❕

    // ⚠------------- ReEvaluation of Seat and Ticket-------------⚠
    @Override
    public String reEvaluate_Ticket() {

        // waiting tickets
        List<ReservedTicket> reservedTickets =    reservedTicketRepository
                        .findAll()
                        .parallelStream()
                        .filter(p->p.getStatus().equals("Waiting"))
                        .collect(Collectors.toList());


        log.info("Waiting ticket accessed Successfully ✔");
        // cancelled seats
        List<ReserveSeats> reserveSeats =
                reservedSeatsRepository
                        .findAll()
                        .parallelStream()
                        .filter(p->{
                            Map<String , Map<Integer, Long>> map = p.getSeats();
                            for (Map.Entry<String , Map<Integer, Long>> e : map.entrySet())
                            {
                                for(Map.Entry<Integer,Long> newMap : e.getValue().entrySet())
                                {
                                    if(newMap.getValue()==-1L)
                                        return true;
                                }
                            }
                            log.info("There is no cancelled seat found");
                            return false;
                        })
                        .collect(Collectors.toList());


        log.info("Cancelled seat accessed Successfully ✔");
        Map<Long, Ticket> account_no = new HashMap<>();

        // Updating Waiting Reserved Ticket for Total no of waiting seats
        reserveSeats.parallelStream().forEach(p->{
            ConcurrentHashMap<String,Map<Integer,Long>> classMap = new ConcurrentHashMap<>(p.getSeats());

            // store classes per seatMap
            classMap.forEach((classes,seat)->{

                ConcurrentHashMap<Integer,Long> seatMap = new ConcurrentHashMap<>(seat);
                ConcurrentHashMap<Integer,Long> cancelledSeatMap = new ConcurrentHashMap<>();

                // store cancelled seats into a cancelledSeatMap
                seat.forEach((seatNo,pnr)->{
                    if(pnr==-1)
                        cancelledSeatMap.put(seatNo,pnr);
                });

                // select out the tickets which contains the class , date and train no.
                CopyOnWriteArrayList<ReservedTicket> reservedTicketCopyOnWriteArrayList = new CopyOnWriteArrayList<>(reservedTickets
                        .parallelStream()
                        .filter(reservedTicket -> p.getSeat_id().getTrain_no().equals(reservedTicket.getTicket().getTrain_no())
                                &&
                                p.getSeat_id().getReservation_date().equals(reservedTicket.getTicket().getDate_of_journey())
                                &&
                                classes.equals(reservedTicket.getTicket().getClass_name())
                        )
                        .collect(Collectors.toList())
                );


                // updating cancelled seat with waiting pnr
                cancelledSeatMap.forEach((seat_no,pnr)->{

                    for(ReservedTicket reservedTicket : reservedTicketCopyOnWriteArrayList) {
                        if (reservedTicket.getStatus().equals("Waiting")) {
                            cancelledSeatMap.put(seat_no, reservedTicket.getPnr());
                            reservedTicket.getTicket().setStatus("Confirmed");
                            reservedTicket.getTicket().setSeat_no(seat_no + "");
                            reservedTicket.setStatus("Confirmed");
                            seatMap.put(seat_no,reservedTicket.getPnr());
                            account_no.put(reservedTicket.getAccount_no(),reservedTicket.getTicket());
                            break;
                        }
                    }

                });




                List<Integer> keys = new ArrayList<>();
                cancelledSeatMap.forEach((seatNo,pnr)-> {
                            seatMap.forEach((key, value) -> {
                                if (!key.equals(seatNo) && value.equals(pnr)) {
                                    keys.add(key);
                                }
                            });
                        });

                // removing waiting tickets
                keys.forEach(key -> {
                    log.info("waiting tickets removed Successfully ❌");
                    seatMap.remove(key);
                });


                keys.removeAll(keys);

                var number  = new ArrayList<Integer>();
                number.add(0);
                seatMap.forEach((key,value)->{
                    var temp = key-1;
                    if(key==0)
                        temp=key;
                    else if(temp==number.get(0))
                        number.set(0,key);
                    else
                    {
                        seatMap.put(number.get(0)+1,value);
                        number.set(0,number.get(0)+1);
                        seatMap.remove(key);
                    }

                });

                if(TicketConfirmMail(account_no).equals("success"))
                    log.info("Customer Will get Their Update ✔");
                else
                    log.error("Problem Occur is Sending Seat Updates");

                 // update changes of seat per class
                classMap.put(classes,seatMap);
                });


            // update classes
            p.setSeats(classMap);

            });
        reservedTickets.forEach(p->reservedTicketRepository.save(p));
        reserveSeats.forEach(p->reservedSeatsRepository.save(p));

        return "success";
    }



    @HystrixCommand(fallbackMethod = "getFallbackTicketConfirmMail" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")
    })
    private String TicketConfirmMail(Map<Long,Ticket> account) {

        // request for user_id;
        account.forEach((key,value)->{
            restTemplate.postForObject("http://USER-MANAGEMENT-SERVICE:8082/user/public/saveTicket/"+key+":"+value.getPnr(),value,String.class);
            String emailAddress = restTemplate.getForObject("http://USER-MANAGEMENT-SERVICE:8082/user/public/getEmail/"+key,String.class);
            if(!emailAddress.equals("null"))
            {
                emailService.sendSimpleEmail(emailAddress,"Dear Customer,\nYour Ticket has been confirmed for the train : "+value.getTrain_name()+"\nStatus : "+value.getStatus()+"\nSeat No : "+value.getSeat_no()+"\nPNR no : "+value.getPnr()+"\nfor further detail check pnr detail from portal\nRegards\nRailway Developer\nrailway.reservation.service@gmail.com","Congratulation, Your ticket has been Confirmed now ");
            }
            else
            {
                log.error("Email Address not found for this Account no : "+key);
            }

        });
        return "success";
    }
    private String getFallbackTicketConfirmMail(Map<Long,Ticket> account)
    {
        return "failure";
    }


}
