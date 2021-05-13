package railway.application.system.Service.ApplicationService;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import railway.application.system.ExceptionHandling.*;
import railway.application.system.Models.Body.Bank.Debit;
import railway.application.system.Models.Body.Detail;
import railway.application.system.Models.Body.TicketStatus;
import railway.application.system.Models.Body.Train;
import railway.application.system.Models.Body.User;
import railway.application.system.Models.Forms.*;
import railway.application.system.Models.Passenger;
import railway.application.system.Models.Payment.Payment;
import railway.application.system.Models.Ticket;
import railway.application.system.Service.EmailService.EmailService;
import railway.application.system.Service.SequenceGenerator.DataSequenceGeneratorService;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {


    // *---------------- Autowiring of Reference Variables -------------*
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EmailService emailService;
    String insufficientBalanceInBankAccount = "!!! Insufficient balance in your bank account !!! , Please add ";
    String seatLimitOutOfBoundException = "!!! Maximum 6 seats can be reserved at a time !!!";
    String invalidQuotaException = "!!! Invalid Quota !!!";
    // *-----------------------------------------------------------------*


    // *------------------------ Exception Messages ---------------------*
    String bankNotExistException = " !!! There is no Bank Exist from this name Kindly Check the name that are listed here !!!";
    String userNotExistException = "      !!! There is no User Exist with this ID !!!       ----> Kindly Please Check the User Id";
    String invalidContactNumberException = "!!! Invalid Number !!! , Please take care that the number should be length : 10 and all Digits";
    String accountNoNotExistException = "!!! Account_no Doesn't Exist in our records!!!";
    String noTrainsExistException = " !!! There is no train exist on this train Information !!!";
    String trainNotRunningOnThisDayException = "This train is not running usually on that day";
    String stationNotExistException = "!!! This Station is not Exist in the Train Route , There is a Mistake with Station selection !!! , Take care of these informations ";
    String reservationDateException = "!!! this date is not allowed to Book Ticket !!!";
    String ticketLimitOutOfBoundException = "!!! Maximum 6 Ticket can be book at a time !!!";
    String trainClassNotExistException = "!!! This Train class not exist in our System !!!";
    String invalidDateException = "!!! Date is Invalid !!!";
    String invalidEmailException = "!!! Invalid Email Address !!! Please Take of this Format email@mail.com";
    String transactionUnSuccessfulException = "!!! Transaction unsuccessful because of some reasons !!! , Please try again later !!!";
    String invalidPNRException = "!!! Invalid PNR !!! , Please write Information correctly.";
    @Autowired
    private Payment payment;
    @Autowired
    private DataSequenceGeneratorService dataSequenceGeneratorService;
    @Autowired
    private Ticket ticket;
    // *-----------------------------------------------------------------*


    // *--------------------------------------------- Train Functionalities--------------------------------------------*


    public String userWelcome() {
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY:8085/user/welcome", String.class);
    }

    // *---------------- get Train -------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackTrain", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String getTrain(String train_no) {
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY:8085/trains/displaytrain/"+train_no,String.class);
    }
    public String getFallBackTrain(String station)
    {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *----------------------------------------*

    // *----------- Train Time Table -----------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackTrainTimeTable" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String trainTimeTable(String station) {
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY:8085/trains/trainTimeTable/"+station,String.class);
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
        String s = restTemplate.getForObject("http://RAILWAY-API-GATEWAY:8085/trains/trainsBetweenStation/"+origin+":"+destination,String.class);
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
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY:8085/trains/trainFare/"+origin+":"+destination,String.class);
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
    public String trainLocation(String train_info, String your_location, String day) {
        return restTemplate.getForObject("http://RAILWAY-RESERVATION-SERVICE:8083/trains/trainsLocation/" + train_info + ":" + your_location + ":" + day, String.class);
    }
    public String getFallBackTrainLocation(String train_info, String your_location, String day)
    {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *------------------------------------------*

    // *----------------------------------------------------------------------------------------------------------------*


    // *------------------------------------------- Reservation Ticket Functionalities ---------------------------------*

    // *------------------------------------- Get Ticket -----------------------------------------------*

    @Override
    @HystrixCommand(fallbackMethod = "getFallbackGetPNR", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String getPNR(Long pnr) {
        try {
            if (!restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/ticketExistByPNR/" + pnr, Boolean.class))
                throw new InvalidPNRException(invalidPNRException);
        } catch (InvalidPNRException e) {
            return e.getMessage();
        }
        return null;
    }

    public String getFallbackGetPNR(Long pnr) {
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
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String reserveTicket(ReservationForm reservationForm) {
        String s = "";
        try {
            if (!restTemplate.getForObject("http://RAILWAY-API-GATEWAY:8085/trains/trainExistByTrainNo/" + reservationForm.getTicketForm().getTrain_no(), Boolean.class))
                throw new NoTrainExistException(noTrainsExistException);
        }
        catch (NoTrainExistException e)
        {
            return e.getMessage();
        }
        Train train = restTemplate.getForEntity("http://RAILWAY-API-GATEWAY:8085/trains/getTrainByTrainNo/"+reservationForm.getTicketForm().getTrain_no(),Train.class).getBody();
        if(!(s=userFormValidation(reservationForm.getUserForm())).equals("success"))
            return s;
        if(!(s=ticketFormValidation(reservationForm.getTicketForm(),train)).equals("success"))
            return s;
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
        try{
            if(reservationForm.getTicketForm().getPassengers().stream().filter(p -> train.getCoaches_fair().containsKey(p.getClass_name())).count()!=reservationForm.getTicketForm().getPassengers().size())
                throw new TrainClassNotExistException(trainClassNotExistException);
        }
        catch (TrainClassNotExistException e)
        {
            return e.getMessage();
        }

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
        // Total Amount Calculated
        double Total_amount = ticket_fee.stream().reduce((a, b) -> a + b).get();


        // *------------------- payment section ------------------*
        if (!(s = payment(reservationForm.getUserForm().getAccount_no(), Total_amount)).equals("success"))
            return s;

        payment.setTransactional_id(dataSequenceGeneratorService.getUserSequenceNumber("payment_sequence"));
        payment.setAccountNo(reservationForm.getUserForm().getAccount_no());
        payment.setAmountDebited(Total_amount);
        payment.setTransaction_time(LocalDateTime.now());


        // *-------------------- Ticket Section -------------------*
        List<Ticket> tickets = new ArrayList<>();
        Ticket refer_ticket = ticket;
        refer_ticket.setTrain_no(reservationForm.getTicketForm().getTrain_no());
        refer_ticket.setTrain_name(train.getTrain_name());
        refer_ticket.setStart(reservationForm.getTicketForm().getStart());
        refer_ticket.setDestination(reservationForm.getTicketForm().getDestination());
        refer_ticket.setDate_of_journey(LocalDate.now());
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

        StringBuilder result = new StringBuilder();
        result.append("Congratulation Reservation has been completed Successfully , here are you Ticket PNR NO , you can check details on your gmail and via PNR status in this Application\n");


        refer_ticket.setJourney_time(Duration.between(refer_ticket.getDeparture_time(), refer_ticket.getArrival_time()).toDaysPart() + "Days " + Duration.between(refer_ticket.getDeparture_time(), refer_ticket.getArrival_time()).toHoursPart() + " h " + ((Duration.between(refer_ticket.getDeparture_time(), refer_ticket.getArrival_time()).toMinutes() - Duration.between(refer_ticket.getDeparture_time(), refer_ticket.getArrival_time()).toMinutes() % 24) / 24 + Duration.between(refer_ticket.getDeparture_time(), refer_ticket.getArrival_time()).toMinutes() % 24) + " m");
        AtomicReference<Long> pnr = new AtomicReference<>(0L);
        List<ReservedTicket> reservedTickets = new ArrayList<>();
        reservationForm.getTicketForm()
                .getPassengers()
                .forEach(p -> {
                    refer_ticket.setAge(p.getAge());
                    refer_ticket.setClass_name(p.getClass_name());
                    refer_ticket.setPassenger_name(p.getPassenger_name());
                    refer_ticket.setContact_no(p.getContact_no());
                    refer_ticket.setQuota(p.getQuota());
                    refer_ticket.setSex(p.getSex());
                    refer_ticket.setPnr(1L);
                    refer_ticket.setEmail_address(p.getEmail_address());
                    TicketStatus ticketStatus = pnrGenerate(refer_ticket);
                    refer_ticket.setPnr(ticketStatus.getPnr());
                    refer_ticket.setStatus(ticketStatus.getStatus());
                    refer_ticket.setSeat_no(ticketStatus.getSeat_no());
                    reservedTickets.add(new ReservedTicket(refer_ticket.getPnr(), refer_ticket, payment.getTransactional_id(), reservationForm.getUserForm().getAccount_no(), refer_ticket.getEmail_address(), refer_ticket.getStatus(), LocalDateTime.of(refer_ticket.getDate_of_journey(), LocalTime.now())));

                    StringBuilder local = new StringBuilder();
                    local.append("\n\n  *--------------------------------------------------------------------------------------------------*\n");
                    local.append("       Train Passenger Name : " + refer_ticket.getPassenger_name() + "         PNR NO. : " + refer_ticket.getPnr());
                    local.append("\n  *--------------------------------------------------------------------------------------------------*\n");
                    result.append(local);

                    local = getTicket(refer_ticket);
                    try {
                        Files.writeString(Paths.get("C:\\Users\\MVERMA\\Railway-Reservation-System\\railway-application-system\\src\\main\\resources\\Data\\templates\\Ticket.txt"), local.toString());
                    } catch (IOException e) {
                    }
                    // *------------------ Email Section -----------------*
                    try {
                        emailService.sendEmailWithAttachment(refer_ticket.getEmail_address(), "Your Train Ticket " + (refer_ticket.getStatus().equals("Confirmed") ? "has been Confirmed for the date : " + refer_ticket.getDate_of_journey() + "\nYou can find your ticket in the Attachment\nThis is PNR NO. : " + refer_ticket.getPnr() : "has been in waiting  \nIf till date of on boarding there is no update in Seat then your money will  be refunded"), "Your ticket reservation is " + (refer_ticket.getStatus().equals("Confirmed") ? "Confirmed" : "in waiting "));
                    } catch (MessagingException e) {
                        System.out.println("email was not sent");
                    }
                    // *--------------------------------------------------*
                });
        reservedTickets.forEach(p -> {
            restTemplate.postForObject("http://RAILWAY-RESERVATION-SERVICE:8085/trains/reservedTicket", p, String.class);
        });
        return result.toString();
    }

    private StringBuilder getTicket(Ticket refer_ticket) {
        StringBuilder local;
        local = new StringBuilder();
        local.append("         ----------------------------------  Welcome To Railway Reservation Network ------------------------------------------\n\n ");
        local.append("         Train No. / Train Name : " + refer_ticket.getTrain_no() + " / " + refer_ticket.getTrain_name() + "       Trip : " + refer_ticket.getStart() + " -----------> " + refer_ticket.getDestination() + "\n");
        local.append("         Class Name : " + refer_ticket.getClass_name() + "        Seat No : " + refer_ticket.getSeat_no() + "       Quota : " + refer_ticket.getQuota() + "\n         Time : " + refer_ticket.getDeparture_time() + " ----> " + refer_ticket.getArrival_time() + "\n");
        local.append("         Date of Journey : " + refer_ticket.getDate_of_journey() + "            Journey Time : " + refer_ticket.getJourney_time() + "\n         Status : " + refer_ticket.getStatus() + "\n");
        local.append("\n         -----------------------------------------------------------------------------------------------------------------------\n");
        local.append("         Passenger Name : " + refer_ticket.getPassenger_name() + "\n");
        local.append("         Contact No : " + refer_ticket.getContact_no() + "\n");
        local.append("         Gender : " + refer_ticket.getSex() + "\n");
        local.append("         Age : " + refer_ticket.getAge() + "\n");
        local.append("         Email Address : " + refer_ticket.getEmail_address() + "\n");
        return local;
    }

    @HystrixCommand(fallbackMethod = "getFallbackPayment", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    private TicketStatus pnrGenerate(Ticket refer_ticket) {
        return restTemplate.postForObject("http://RAILWAY-RESERVATION-SERVICE:8083/trains/reserveTicket", refer_ticket, TicketStatus.class);
    }

    // *------------------------------ Payment Section ----------------------------*
    @HystrixCommand(fallbackMethod = "getFallbackPayment", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String payment(Long account_no, double total_amount) {
        String result = "null";
        try {
            Double balance = 0D;
            if ((balance = total_amount - restTemplate.getForObject("http://USER-MANAGEMENT-SERVICE:8083/user/getBalance/" + account_no, Double.class)) > 0)
                throw new InsufficientBalanceInBankAccount(insufficientBalanceInBankAccount + balance);
            if (!restTemplate.postForObject("http://USER-MANAGEMENT-SERVICE:8083/bank/balanceDebited", new Debit(account_no, total_amount), String.class).equals("success"))
                throw new TransactionUnSuccessfulException(transactionUnSuccessfulException);
        } catch (InsufficientBalanceInBankAccount | TransactionUnSuccessfulException e) {
            result = e.getMessage();
        }
        return result.equals("null") ? "success" : result;
    }

    public String getFallbackPayment(Long account_no, double total_amount) {
        return "!!! Service is Down !!! , Please Try Again Later";
    }


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
            if (!passengers.stream()
                    .filter(p -> "Female".equalsIgnoreCase(p.getSex()))
                    .allMatch(q -> "ladies".equalsIgnoreCase(q.getQuota())) || !(passengers.stream()
                    .filter(p -> "Female".equalsIgnoreCase(p.getSex()))
                    .allMatch(s -> "ladies".equalsIgnoreCase(s.getQuota()) && passengers.size() == 1))
            )
                throw new InvalidQuotaException(invalidQuotaException + "Ladies Quota Applicable for Female gender ");

            if (!passengers.stream()
                    .filter(p -> "senior citizen".equalsIgnoreCase(p.getQuota()))
                    .allMatch(p -> p.getAge() >= 58))
                throw new InvalidQuotaException(invalidQuotaException + "Senior Citizen Quota is not Applicable for People has age less than 58 years");
            if (passengers.stream().noneMatch(p -> isNumeric(p.getContact_no()))
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
            if(train.getRoute().keySet().stream().filter(p->p.equals(ticketForm.getStart()) || p.equals(ticketForm.getDestination())).count()!=2)
                throw new StationNotExistException(stationNotExistException);


            LocalDate date = ticketForm.getReservation_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if(!(date.isAfter(LocalDate.now()) && LocalDateTime.of(date,train.getDeparture_time().minusHours(4)).isAfter(LocalDateTime.now())))
                throw new ReservationDateException(reservationDateException + "Your Date Should be before the date train Start");
            if(train.getRun_days().stream()
                                  .filter(p->p.equals(date.getDayOfWeek().toString().toUpperCase().substring(0,3)))
                                  .count()!=1
            )
                throw new TrainNotRunningOnThisDayException(trainNotRunningOnThisDayException);
        }
        catch (NullPointerException | StationNotExistException | ReservationDateException | TrainNotRunningOnThisDayException e)
        {
            return e.getMessage();
        }
        ResponseEntity<Train[]> responseEntity = restTemplate.getForEntity("http://Railway-reservation-Service/trains/displayAllTrains",Train[].class);
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
            if(!restTemplate.getForObject("http://RAILWAY-API-GATEWAY:8085/user/userExistById/"+userForm.getUser_id(),Boolean.class))
                throw new UserNotExistException(userNotExistException);
            ResponseEntity<User[]> responseEntity = restTemplate.getForEntity("http://RAILWAY-API-GATEWAY:8085/user/getAllUsers",User[].class);
            user = Arrays.stream(responseEntity.getBody()).filter(p-> p.getUser_id().equals(userForm.getUser_id())).collect(Collectors.toList()).get(0);
            if(!user.getAccount_no().equals(userForm.getAccount_no()) || !user.getBank_name().equals(userForm.getBank_name()) || !user.getFull_name().equals(userForm.getAccountHolder()) || !user.getCredit_card_no().equals(userForm.getCredit_card_no()) || !user.getCvv().equals(userForm.getCvv()))
                throw new AccountNoNotExistException(accountNoNotExistException);
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
