package railway.application.system.service.applicationService;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import railway.application.system.exceptionHandling.*;
import railway.application.system.models.Passenger;
import railway.application.system.models.Ticket;
import railway.application.system.models.forms.*;
import railway.application.system.models.payment.AddMoney;
import railway.application.system.models.payment.Payment;
import railway.application.system.models.response.*;
import railway.application.system.models.response.bank.Debit;
import railway.application.system.service.emailService.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {


    // *---------------- Autowiring of Reference Variables -------------*
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EmailService emailService;
    @Autowired
    private Payment payment;
    @Autowired
    private Ticket ticket;
    @Autowired
    private SeatData seatData;
    int j = 0;
    // *-----------------------------------------------------------------*


    // *------------------------ Exception Messages ---------------------*
    String bankNotExistException = " !!! There is no Bank Exist from this name Kindly Check the name that are listed here !!!";
    String userNotExistException = "      !!! There is no User Exist with this ID !!!       ----> Kindly Please Check the User Id";
    String invalidContactNumberException = "!!! Invalid Number !!! , Please take care that the number should be length : 10 and all Digits";
    String accountNoNotExistException = "!!! Account_no Doesn't Exist in our records!!!";
    String noTrainsExistException = " !!! There is no train exist on this train Information !!!";
    String trainNotRunningOnThisDayException = "!!! This train is not running usually on that day !!!";
    String stationNotExistException = "!!! This Station is not Exist in the Train Route !!! , There is a Mistake with Station selection !!! , Take care of these information";
    String reservationDateException = "!!! this date is not allowed to Book Ticket !!!";
    String ticketLimitOutOfBoundException = "!!! Maximum 6 Ticket can be book at a time !!!";
    String trainClassNotExistException = "!!! This Train class not exist in our System !!!";
    String invalidDateException = "!!! Date is Invalid !!!";
    String invalidEmailException = "!!! Invalid Email Address !!! Please Take of this Format email@mail.com";
    String transactionUnSuccessfulException = "!!! Transaction unsuccessful because of some reasons !!! , Please try again later !!!";
    String invalidPNRException = "!!! Invalid PNR !!! , Please write Information correctly.";
    String insufficientBalanceInBankAccount = "!!! Insufficient balance in your bank account !!! , Please add ";
    String seatLimitOutOfBoundException = "!!! Maximum 6 seats can be reserved at a time !!!";
    String invalidQuotaException = "!!! Invalid Quota !!!";
    // *-----------------------------------------------------------------*


    // *--------------------------------------------- Train Functionalities--------------------------------------------*

    @HystrixCommand(fallbackMethod = "getFallBackUserWelcome", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String userWelcome() {
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY/user/public/welcome", String.class);
    }
    public String getFallBackUserWelcome(){return "!!! Service is Down !!! , Please Try Again Later";}

    // *---------------- get Train -------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackTrain", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String getTrain(String train_no) {
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/public/displaytrain/"+train_no,String.class);
    }
    public String getFallBackTrain(String station)
    {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *----------------------------------------*

    // *----------- Train Time Table -----------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackTrainTimeTable" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")
    })
    public String trainTimeTable(String station) {
        log.info("TimeTable process in Service Class");
        return restTemplate.getForObject("http://RAILWAY-RESERVATION-SERVICE:8083/trains/public/trainTimeTable/"+station,String.class);
    }
    public String getFallBackTrainTimeTable(String station)
    {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *----------------------------------------*

    // *--------- Train Between Station --------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackTrainBetweenStation" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String trainBetweenStation(String origin, String destination) {
        String s = restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/public/trainsBetweenStation/"+origin+":"+destination,String.class);
        return s;
    }
    public String getFallBackTrainBetweenStation(String station,String station2)
    {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *----------------------------------------*

    // *------------ Train fares ---------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackTrainFare" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String trainFares(String origin, String destination) {
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/public/trainFare/"+origin+":"+destination,String.class);
    }
    public String getFallBackTrainFare(String station,String station2)
    {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *-----------------------------------------*

    // *------------- Train Location ------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackTrainLocation" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")
    })
    public String trainLocation(LocationBody locationBody) {
        return restTemplate.postForObject("http://RAILWAY-API-GATEWAY/trains/public/trainsLocation",locationBody, String.class);
    }
    public String getFallBackTrainLocation(LocationBody locationBody)
    {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *----------------------------------------------*


    // *------------------------- Get Train Details -------------------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackTrainNo" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")
    })
    public ResponseEntity<?> getTrainByTrainNo(String train_no) {
        return ResponseEntity.of(Optional.ofNullable(restTemplate.getForEntity("http://RAILWAY-API-GATEWAY/trains/public/getTrainByTrainNo/"+train_no,Train.class).getBody()));
    }
    public ResponseEntity<?> getFallBackTrainNo(String train_no){return ResponseEntity.of(Optional.of("!!! Service is Down !!! , Please Try Again Later"));}
    // *-----------------------------------------------------------------------*

    // *--------------------------- Available Seats ---------------------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackAvailableSeats" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000")
    })
    public String availableSeats(AvailableSeats availableSeats) {
        return restTemplate.postForObject("http://RAILWAY-API-GATEWAY/trains/public/availableSeats",availableSeats,String.class);
    }
    public String getFallBackAvailableSeats(AvailableSeats availableSeats){return "!!! Service is Down !!! , Please Try Again Later";}
    // *--------------------------------------------------------------------------*

    // *-------------------------- Available Accommodation -----------------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackAvailableAccommodation" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")
    })
    public String availableAccommodation(AccommodationBody accommodationBody) {
        return restTemplate.postForObject("http://RAILWAY-API-GATEWAY/trains/public/availableAccommodation",accommodationBody,String.class);
    }

    public String getFallBackAvailableAccommodation(AccommodationBody accommodationBody){return "!!! Service is Down !!! , Please Try Again Later";}
    // *-----------------------------------------------------------------------------*


    // *---------------------------- Create User Account --------------------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackCreateUserAccount" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")
    })
    public String createUserAccount(UserForm1 userForm) {
        return restTemplate.postForObject("http://RAILWAY-API-GATEWAY/user/public/createUser",userForm,String.class);
    }

    public String getFallBackCreateUserAccount(UserForm1 userForm){return "!!! Service is Down !!! , Please Try Again Later";}
    // *-----------------------------------------------------------------------------*


    // *---------------------------- Create User Account --------------------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackCreateBankAccount" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")
    })
    public String createBankAccount(BankForm bankForm) {
        return restTemplate.postForObject("http://RAILWAY-API-GATEWAY/bank/public/createAccount",bankForm,String.class);
    }

    public String getFallBackCreateBankAccount(BankForm bankForm) {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *-----------------------------------------------------------------------------*


    // *------------------------------- Add balance --------------------------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackAddBalance" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")
    })
    public String addBalance(AddMoney addMoney) {
        return restTemplate.postForObject("http://RAILWAY-API-GATEWAY/bank/public/addMoney/",addMoney,String.class);
    }

    public String getFallBackAddBalance(AddMoney addMoney) {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *-----------------------------------------------------------------------------*


    // *------------------------------ get Credentials ------------------------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackGetCredential" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")
    })
    public String getCredentials(long user_id) {
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY/user/public/userCredential/"+user_id,String.class);
    }

    public String getFallBackGetCredential(long user_id) {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *-----------------------------------------------------------------------------*

    // *------------------------------ get Credentials ------------------------------*
    @Override
    public User getProfile(long user_id) {
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY/user/public/getUser/"+user_id,User.class);
    }

    // *-----------------------------------------------------------------------------------------------------------------*

    // *------------------------------------------- Reservation Ticket Functionalities ---------------------------------*

    // *----------------------- Get Reserved Ticket -------------------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallbackGetPNR", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String getPNR(Long pnr) {
        try {
            if (!restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/public/ticketExistByPNR/" + pnr, Boolean.class))
                throw new InvalidPNRException(invalidPNRException);
        } catch (InvalidPNRException e) {
            return e.getMessage();
        }
        ReservedTicket ticket = restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/public/getTicket/" + pnr, ReservedTicket.class);
        return getTicket(ticket.getTicket()).toString();
    }


    public String getFallbackGetPNR(Long pnr) {
        return "!!! Service is Down !!! , Please Try Again Later";
    }

    // *-------------------------------------------------------------------------*


    // *------------------------ Ticket Cancellation ----------------------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallbackTicketCancellation", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")
    })
    public String TicketCancellation(long pnr) {
        try {
            if (!restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/public/ticketExistByPNR/" + pnr, Boolean.class))
                throw new InvalidPNRException(invalidPNRException);
            log.info("ticket-Exist");
        } catch (InvalidPNRException e) {
            return e.getMessage();
        }


        ReservedTicket reservedTicket = restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/public/getTicket/" + pnr, ReservedTicket.class);
        log.info("Ticket Received Successfully ✔");
        assert reservedTicket != null;
        Payment payment = restTemplate.getForObject("http://RAILWAY-API-GATEWAY/bank/public/getTransaction/"+reservedTicket.getTransactional_id()+":"+reservedTicket.getAccount_no(),Payment.class);
        log.info("Transactional detail Received Successfully ✔");
        SeatData seat = seatData;
        seat.setSeat_id(new Seat_Id(reservedTicket.getTicket().getTrain_no(), reservedTicket.getTicket().getDate_of_journey()));
        seat.setClass_name(reservedTicket.getTicket().getClass_name());
        seat.setSeat_no(reservedTicket.getTicket().getSeat_no());
        try {
            if (!Objects.equals(restTemplate.postForObject("http://RAILWAY-API-GATEWAY/bank/public/addMoney", new AddMoney(reservedTicket.getAccount_no(), reservedTicket.getAmount()), String.class), "Amount Added Successfully to your Account with account no : " + payment.getAccountNo()))
                throw new TransactionUnSuccessfulException("unable to refund money");
            log.info("Money Refunded Successfully ✔");
            if (!Objects.equals(restTemplate.postForObject("http://RAILWAY-API-GATEWAY/trains/public/cancelSeat", seatData, String.class), "success"))
                throw new TransactionUnSuccessfulException("unable to cancel seat");
            log.info("Seat cancellation happened Successfully ✔");
            if (!Objects.equals(restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/public/cancelTicket/" + pnr, String.class), "success"))
                throw new TransactionUnSuccessfulException("unable to cancel ticket");
            log.info("Seat cancellation happened Successfully ✔");
        } catch (TransactionUnSuccessfulException e) {
            return e.getMessage();
        }

        emailService.sendSimpleEmail(reservedTicket.getTicket().getEmail_address(), "Dear " + reservedTicket.getTicket().getPassenger_name() + ",\n\nYour Ticket has been Cancelled for Trip from : " + reservedTicket.getTicket().getStart() + " ---> to :" + reservedTicket.getTicket().getDestination() + "\nMoney is refunded in your Account Successfully\n\nWith Regards\nRailway Reservation System\nrailway.developer@gmail.com", "Your Ticket has been cancelled Successfully");
        return "Ticket Cancelled Successfully on this PNR No : " + pnr + " and Your Money will be refunded in your account";
    }

    public String getFallbackTicketCancellation(long pnr) {
        return "!!! Service is Down !!! , Please Try Again Later";
    }

    // *------------------------------------------------------------------------------------------------*


    // *------------------------------------- Ticket Reservation ---------------------------------------*

    public String getFallbackReserveTicket(ReservationForm reservationForm) {
        return "!!! Service is Down !!! , Please Try Again Later";
    }

    @Override
    //@EventListener(ApplicationReadyEvent.class)
    @HystrixCommand(fallbackMethod = "getFallbackReserveTicket", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "70000")
    })
    public String reserveTicket(ReservationForm reservationForm) {
        String s = "";
        try {
            if (!restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/public/trainExistByTrainNo/" + reservationForm.getTicketForm().getTrain_no(), Boolean.class))
                throw new NoTrainExistException(noTrainsExistException);
        }
        catch (NoTrainExistException e)
        {
            return e.getMessage();
        }
        log.info("Train No is Validation is Successfully ✔");

        // UserForm Validation
        Train train = restTemplate.getForEntity("http://RAILWAY-API-GATEWAY/trains/public/getTrainByTrainNo/"+reservationForm.getTicketForm().getTrain_no(),Train.class).getBody();

        if(!(s=userFormValidation(reservationForm.getUserForm())).equals("success"))
            return s;
        log.info("UserForm Validation is Successfully ✔");


        // TicketForm Validation
        if(!(s=ticketFormValidation(reservationForm.getTicketForm(),train)).equals("success"))
            return s;
        log.info("TicketForm Validation is Successfully ✔");


        // PassengerForm Validation
        try {
            if(reservationForm.getTicketForm().getPassengers().size()>6)
                throw new TicketLimitOutOfBoundException(ticketLimitOutOfBoundException);
        }
        catch (TicketLimitOutOfBoundException e)
        {
            return e.getMessage();
        }
        if(!(s=passengerFormValidation(reservationForm.getTicketForm().getPassengers())).equals("success"))
            return s;
        log.info("PassengerForm Validation is Successfully ✔");


        // Train Class Validation
        try{
            if(reservationForm.getTicketForm().getPassengers().parallelStream().filter(p -> train.getCoaches_fair().containsKey(p.getClass_name())).count()!=reservationForm.getTicketForm().getPassengers().size())
                throw new TrainClassNotExistException(trainClassNotExistException);
        }
        catch (TrainClassNotExistException e)
        {
            return e.getMessage();
        }
        log.info("Train class is Validation is Successfully ✔");


        // Ticket Price Calculation
        List<Double> ticket_fee = new ArrayList<>();
        List<Double> total_fee = new ArrayList<>();
        total_fee.add(0D);
        boolean chk = false;

        for(Map.Entry<String,Detail> map : train.getRoute().entrySet())
        {
            if(map.getKey().equals(reservationForm.getTicketForm().getDestination()))
            {
                total_fee.set(0,total_fee.get(0)+map.getValue().getPrice());
                break;
            }
            if(map.getKey().equals(reservationForm.getTicketForm().getStart()))
            {
                total_fee.set(0,total_fee.get(0)+map.getValue().getPrice());
                chk=true;
            }
            if(chk)
            {
                total_fee.set(0,total_fee.get(0)+map.getValue().getPrice());
            }
        }

        reservationForm.getTicketForm()
                .getPassengers()
                .forEach(p -> {
                    ticket_fee.add(train.getCoaches_fair().get(p.getClass_name()) * total_fee.get(0));
                });



        // *------------------- payment section ------------------*
        log.info("Payment is Started ✔");
        if (!(payment(reservationForm.getUserForm().getAccount_no(), ticket_fee)).equals("success"))
            return s;
        log.info("Payment is Successful  ✔");
        List<Long> transactional_id = new ArrayList<>();
        int k=1;
        for (Double cost : ticket_fee) {
            payment.setTransactional_id(restTemplate.getForObject("http://RAILWAY-API-GATEWAY/bank/public/generateTransactionSequence",Long.class)+1);
            payment.setAccountNo(reservationForm.getUserForm().getAccount_no());
            payment.setAmount(cost);
            payment.setTransaction_type("Debited");
            payment.setTransaction_time(LocalDateTime.now());
            restTemplate.postForObject("http://RAILWAY-API-GATEWAY/bank/public/saveTransaction",payment,String.class);
            transactional_id.add(payment.getTransactional_id());
        }



        // *-------------------- Ticket Section -------------------*
        List<Ticket> tickets = new ArrayList<>();
        Ticket refer_ticket = ticket;
        refer_ticket.setTrain_no(reservationForm.getTicketForm().getTrain_no());
        refer_ticket.setTrain_name(train.getTrain_name());
        refer_ticket.setStart(reservationForm.getTicketForm().getStart());
        refer_ticket.setDestination(reservationForm.getTicketForm().getDestination());
        refer_ticket.setDate_of_journey(reservationForm.getTicketForm().getReservation_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        log.info("Journey Date : "+refer_ticket.getDate_of_journey().toString());
        chk = false;
        LocalDateTime duration = LocalDateTime.now();
        for (Map.Entry<String, Detail> map : train.getRoute().entrySet()) {
            if (map.getKey().equals(refer_ticket.getDestination())) {
                refer_ticket.setArrival_time(duration.plusMinutes(15));
            } else if (map.getKey().equals(refer_ticket.getStart())) {
                duration = LocalDateTime.of(refer_ticket.getDate_of_journey(), map.getValue().getDeparture_time());
                refer_ticket.setDeparture_time(duration);
                chk = true;
            } else if (chk) {
                duration = duration.plusMinutes(15);
            }

        }
        log.info("TicketReservation is started ✔");
        StringBuilder result = new StringBuilder();
        result.append("Congratulation Reservation has been completed Successfully , here are you Ticket PNR NO , you can check details on your gmail and via PNR status in this Application\n");



        List<Integer> i = new ArrayList<>();
        i.add(0);
        refer_ticket.setJourney_time(Duration.between(refer_ticket.getDeparture_time(), refer_ticket.getArrival_time()).toDaysPart() + " Days " + Duration.between(refer_ticket.getDeparture_time(), refer_ticket.getArrival_time()).toHoursPart() + " h " + ((Duration.between(refer_ticket.getDeparture_time(), refer_ticket.getArrival_time()).toMinutes() - Duration.between(refer_ticket.getDeparture_time(), refer_ticket.getArrival_time()).toMinutes() % 24) / 24 + Duration.between(refer_ticket.getDeparture_time(), refer_ticket.getArrival_time()).toMinutes() % 24) + " m");
        List<ReservedTicket> reservedTickets = new ArrayList<>();
        reservationForm.getTicketForm()
                .getPassengers()
                .forEach(p -> {
                    log.info("TicketReservation is Passenger data Updating ✔");

                    refer_ticket.setAge(p.getAge());
                    refer_ticket.setClass_name(p.getClass_name());
                    refer_ticket.setPassenger_name(p.getPassenger_name());
                    refer_ticket.setContact_no(p.getContact_no());
                    refer_ticket.setQuota(p.getQuota());
                    refer_ticket.setSex(p.getSex());
                    refer_ticket.setPnr(1L);
                    refer_ticket.setTransactional_id(transactional_id.get(i.get(0)));
                    refer_ticket.setEmail_address(p.getEmail_address());
                    TicketStatus ticketStatus = pnrGenerate(refer_ticket);
                    refer_ticket.setPnr(ticketStatus.getPnr());
                    refer_ticket.setStatus(ticketStatus.getStatus());
                    refer_ticket.setSeat_no(ticketStatus.getSeat_no());
                    reservedTickets.add(new ReservedTicket(refer_ticket.getPnr(), refer_ticket, refer_ticket.getTransactional_id(), reservationForm.getUserForm().getAccount_no(), refer_ticket.getEmail_address(), refer_ticket.getStatus(), LocalDateTime.of(refer_ticket.getDate_of_journey(), LocalTime.now()),ticket_fee.get(i.get(0))));
                    log.info("Journey Date : "+refer_ticket.getDate_of_journey().toString());
                    log.info("TicketReservation generating ✔");
                    i.set(0,i.get(0)+1);

                    StringBuilder local = new StringBuilder();
                    local.append("\n\n  *--------------------------------------------------------------------------------------------------*\n");
                    local.append("       Train Passenger Name :xx " + refer_ticket.getPassenger_name() + "         PNR NO. : " + refer_ticket.getPnr());
                    local.append("\n  *--------------------------------------------------------------------------------------------------*\n");
                    result.append(local);



                    local = getTicket(refer_ticket);
                    try {
                        Files.writeString(Paths.get("C:\\Users\\MVERMA\\Railway-Reservation-System\\railway-application-system\\src\\main\\resources\\static\\Ticket.txt"), local.toString());
                    } catch (IOException e) {
                        log.error("FileNotFoundException");
                    }
                    log.info("Ticket has been Reserved ✔");



                    // *------------------ Email Section -----------------*
                    try {
                        log.info(emailService.sendEmailWithAttachment(refer_ticket.getEmail_address(), "Your Train Ticket " + (refer_ticket.getStatus().equals("Confirmed") ? "has been Confirmed for the date : " + refer_ticket.getDate_of_journey() + "\nYou can find your ticket in the Attachment\nThis is PNR NO. : " + refer_ticket.getPnr() : "has been in waiting  \nIf till date of on boarding there is no update in Seat then your money will  be refunded"), "Your ticket reservation is " + (refer_ticket.getStatus().equals("Confirmed") ? "Confirmed" : "in waiting ")));
                    } catch (MessagingException e) {
                        log.error("Unable to send Message");
                    }
                    // *--------------------------------------------------*
                });





        // saving Ticket to database
        reservedTickets.forEach(p -> {

            restTemplate.postForObject("http://RAILWAY-API-GATEWAY/user/public/saveTicket/"+p.getAccount_no()+":"+p.getPnr(),p.getTicket(),String.class);
            restTemplate.postForObject("http://RAILWAY-API-GATEWAY/trains/public/reservedTicket", p,String.class);
        });
        log.info("Ticket Reservation is Successfully ✔");
        j++;
        log.info(" ‼‼ Ticket no "+j+"  ✔💲💱");

        /*
    if(j<75)
         reserveTicket(reservationForm);
        */
        return result.toString();

    }

    private StringBuilder getTicket(Ticket refer_ticket) {
        StringBuilder local;
        local = new StringBuilder();
        local.append("         ----------------------------------  Welcome To Railway Reservation Network ------------------------------------------\n\n");
        local.append("         Train No. / Train Name : ").append(refer_ticket.getTrain_no()).append(" / ").append(refer_ticket.getTrain_name()).append("       Trip : ").append(refer_ticket.getStart()).append(" -----------> ").append(refer_ticket.getDestination()).append("\n");
        local.append("         Class Name : ").append(refer_ticket.getClass_name()).append("        Seat No : ").append(refer_ticket.getSeat_no()).append("       Quota : ").append(refer_ticket.getQuota()).append("\n         Time : ").append(refer_ticket.getDeparture_time()).append(" ----> ").append(refer_ticket.getArrival_time()).append("\n");
        local.append("         Date of Journey : ").append(refer_ticket.getDate_of_journey()).append("            Journey Time : ").append(refer_ticket.getJourney_time()).append("\n         Status : ").append(refer_ticket.getStatus()).append("\n");
        local.append("\n         -----------------------------------------------------------------------------------------------------------------------\n");
        local.append("         Passenger Name : ").append(refer_ticket.getPassenger_name()).append("\n");
        local.append("         Contact No : ").append(refer_ticket.getContact_no()).append("\n");
        local.append("         Gender : ").append(refer_ticket.getSex()).append("\n");
        local.append("         Age : ").append(refer_ticket.getAge()).append("\n");
        local.append("         Email Address : ").append(refer_ticket.getEmail_address()).append("\n");
        return local;
    }

    // *-------------------------------- PNR Generate ----------------------------*
    @HystrixCommand(fallbackMethod = "getFallbackPNRGenerate", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    private TicketStatus pnrGenerate(Ticket refer_ticket) {
        return restTemplate.postForObject("http://RAILWAY-API-GATEWAY/trains/public/reserveTicket", refer_ticket, TicketStatus.class);
    }

    // *------------------------------ Payment Section ----------------------------*
    @HystrixCommand(fallbackMethod = "getFallbackPayment", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String payment(Long account_no, List<Double> ticket_fee) {
        String result = "null";
        Double total_amount = ticket_fee.stream().reduce(Double::sum).get();
        try {
            Double balance = 0D;
            if ((balance = total_amount - restTemplate.getForObject("http://RAILWAY-API-GATEWAY/bank/public/getBalance/" + account_no, Double.class)) > 0)
                throw new InsufficientBalanceInBankAccount(insufficientBalanceInBankAccount + balance);
            log.info("Balance check Successful ✔");
            if (!restTemplate.postForObject("http://RAILWAY-API-GATEWAY/bank/public/balanceDebited", new Debit(account_no, total_amount), String.class).equals("success"))
                throw new TransactionUnSuccessfulException(transactionUnSuccessfulException);
            log.info("Transaction happend Successfully ✔");
        } catch (InsufficientBalanceInBankAccount | TransactionUnSuccessfulException e) {
            result = e.getMessage();
            log.error(e.getMessage());
        }
        return result.equals("null") ? "success" : result;
    }

    public String getFallbackPayment(Long account_no, double total_amount) {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *-------------------------------------------------------------*


    // *----------------- Passenger Form Validation -----------------*

    public boolean isEmailValid(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @HystrixCommand(fallbackMethod = "getFallbackPassengers", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String passengerFormValidation(List<Passenger> passengers) {

        try {
            if (!passengers.parallelStream()
                    .filter(p -> "Female".equalsIgnoreCase(p.getSex()))
                    .allMatch(q -> "ladies".equalsIgnoreCase(q.getQuota())) || !(passengers.parallelStream()
                    .filter(p -> "Female".equalsIgnoreCase(p.getSex()))
                    .allMatch(s -> "ladies".equalsIgnoreCase(s.getQuota()) && passengers.size() == 1))
            )
                throw new InvalidQuotaException(invalidQuotaException + "Ladies Quota Applicable for Female gender ");
            if (!passengers.parallelStream()
                    .filter(p -> "senior citizen".equalsIgnoreCase(p.getQuota()))
                    .allMatch(p -> p.getAge() >= 58))
                throw new InvalidQuotaException(invalidQuotaException + "Senior Citizen Quota is not Applicable for People has age less than 58 years");
            if (passengers.parallelStream().noneMatch(p -> isNumeric(p.getContact_no()))
            )
                throw new InvalidContactNumberException(invalidContactNumberException);

            for (Passenger p : passengers) {
                if (!isEmailValid(p.getEmail_address()))
                    throw new InvalidEmailException(invalidEmailException);
                InternetAddress internetAddress = new InternetAddress(p.getEmail_address());
                internetAddress.validate();
            }

        } catch (InvalidQuotaException | InvalidContactNumberException | AddressException | InvalidEmailException e) {
            return e.getMessage();
        }
        return "success";
    }

    public String getFallbackPassengers(List<Passenger> passengers) {
        return "!!! Service is Down !!! , Please Try Again Later";
    }

    public boolean isNumeric(String train_search_info)
    {
        try {
            Double.parseDouble(train_search_info);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    // *------------------- Ticket Form Validation --------------------*
    @HystrixCommand(fallbackMethod = "getFallbackFormTicketValidation", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    private String ticketFormValidation(TicketForm ticketForm,Train train)
    {
        try{
            if(train.getRoute().keySet().parallelStream().filter(p->p.equals(ticketForm.getStart()) || p.equals(ticketForm.getDestination())).count()!=2)
                throw new StationNotExistException(stationNotExistException);


            LocalDate date = ticketForm.getReservation_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if(!(date.isAfter(LocalDate.now()) && LocalDateTime.of(date,train.getDeparture_time().minusHours(4)).isAfter(LocalDateTime.now())))
                throw new ReservationDateException(reservationDateException + "Your Date Should be before the date train Start");
            if(train.getRun_days().parallelStream()
                                  .filter(p->p.equals(date.getDayOfWeek().toString().toUpperCase().substring(0,3)))
                                  .count()!=1
            )
                throw new TrainNotRunningOnThisDayException(trainNotRunningOnThisDayException);
        }
        catch (NullPointerException | StationNotExistException | ReservationDateException | TrainNotRunningOnThisDayException e)
        {
            return e.getMessage();
        }
        ResponseEntity<Train[]> responseEntity = restTemplate.getForEntity("http://RAILWAY-API-GATEWAY/trains/public/displayAllTrains",Train[].class);
        List<Train> trains = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

        return "success";
    }
    public String getFallbackFormTicketValidation(BankForm bankForm)
    {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *----------------------- ticket Form end -------------------------*

    // *--------------------- User Form Validation ----------------------*
    @HystrixCommand(fallbackMethod = "getFallbackUserFormValidation" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    private String userFormValidation(UserForm userForm) {
        User user = null;
        try{
            if(!restTemplate.getForObject("http://RAILWAY-API-GATEWAY/user/public/userExistById/"+userForm.getUser_id(),Boolean.class))
                throw new UserNotExistException(userNotExistException);
            log.info("User Id has been matched ✔");
            ResponseEntity<User[]> responseEntity = restTemplate.getForEntity("http://RAILWAY-API-GATEWAY/user/public/getAllUsers",User[].class);
            user = Arrays.stream(responseEntity.getBody()).filter(p-> p.getUser_id().equals(userForm.getUser_id())).collect(Collectors.toList()).get(0);
            if(!user.getAccount_no().equals(userForm.getAccount_no()) || !user.getBank_name().equals(userForm.getBank_name()) || !user.getFull_name().equals(userForm.getAccountHolder()) || !user.getCredit_card_no().equals(userForm.getCredit_card_no()) || !user.getCvv().equals(userForm.getCvv()))
                throw new AccountNoNotExistException(accountNoNotExistException);
            log.info("Bank Account has been matched ✔");
            return "success";
        }
        catch (NullPointerException | UserNotExistException | AccountNoNotExistException e)
        {
            return e.getMessage();
        }
    }
    public String getFallbackUserFormValidation(UserForm userForm)
    {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *----------------------------- User form End ------------------------*
    // *----------------------------------------------------------------------------------------------------------------*
}
