package railway.application.system.Service.ApplicationService;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import railway.application.system.ExceptionHandling.*;
import railway.application.system.Models.Body.Detail;
import railway.application.system.Models.Body.Train;
import railway.application.system.Models.Body.User;
import railway.application.system.Models.Forms.BankForm;
import railway.application.system.Models.Forms.ReservationForm;
import railway.application.system.Models.Forms.TicketForm;
import railway.application.system.Models.Forms.UserForm;
import railway.application.system.Models.Passenger;
import railway.application.system.Service.EmailService.EmailService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService{


    // *---------------- Autowiring of Reference Variables -------------*
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EmailService emailService;
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
    String seatLimitOutOfBoundException = "!!! Maximum 6 seats can be reserved at a time";
    String invalidQuotaException = "!!! Invalid Quota!!!";
    // *-----------------------------------------------------------------*




    // *--------------------------------------------- Train Functionalities--------------------------------------------*


    public String userWelcome(){return restTemplate.getForObject("http://RAILWAY-API-GATEWAY:8085/user/welcome",String.class);}

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
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String trainLocation(String train_info, String your_location, String day) {
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY:8085/trains/trainTimeTable/"+train_info+":"+your_location+":"+day,String.class);
    }
    public String getFallBackTrainLocation(String train_info, String your_location, String day)
    {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *------------------------------------------*

    // *----------------------------------------------------------------------------------------------------------------*


    // *------------------------------------------- Reservation Ticket Functionalities ---------------------------------*
    @Override
    //@EventListener(ApplicationReadyEvent.class)
    public String reserveTicket(ReservationForm reservationForm) {
        String s="";
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
                       .forEach(p->{
                        ticket_fee.add(train.getCoaches_fair().get(p.getClass_name())*total_fee.get(0));
        });
        // Total Amount Calculated
        double Total_amount = ticket_fee.stream().reduce((a,b)->a+b).get();


        // Seat Availability




        return "nwe Ticket";
    }


    // *----------------- Passenger Form Validation -----------------*
    @HystrixCommand(fallbackMethod = "getFallbackFormTicketValidation", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    private String passengerFormValidation(List<Passenger> passengers) {

        try {
                if(!passengers.stream()
                              .filter(p->"Female".equalsIgnoreCase(p.getSex()))
                              .allMatch(q -> "ladies".equalsIgnoreCase(q.getQuota())) || !(passengers.stream()
                                                                                              .filter(p->"Female".equalsIgnoreCase(p.getSex()))
                                                                                              .allMatch(s -> "ladies".equalsIgnoreCase(s.getQuota()) && passengers.size()==1))
                )
                    throw new InvalidQuotaException(invalidQuotaException+"Ladies Quota Applicable for Female gender ");

                if(!passengers.stream()
                        .filter(p->"senior citizen".equalsIgnoreCase(p.getQuota()))
                        .allMatch(p->p.getAge()>=58))
                    throw new InvalidQuotaException(invalidQuotaException+"Senior Citizen Quota is not Applicable for People has age less than 58 years");
                if(passengers.stream().noneMatch(p -> isNumeric(p.getContact_no()))
                )
                    throw new InvalidContactNumberException(invalidContactNumberException);

        } catch (InvalidQuotaException | InvalidContactNumberException e) {
            e.getMessage();
        }
        return "success";
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
