package railway.application.system.Service.ApplicationService;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import railway.application.system.ExceptionHandling.AccountNoNotExistException;
import railway.application.system.ExceptionHandling.NoTrainExistException;
import railway.application.system.ExceptionHandling.UserNotExistException;
import railway.application.system.Models.Body.User;
import railway.application.system.Models.Forms.BankForm;
import railway.application.system.Models.Forms.ReservationForm;
import railway.application.system.Models.Forms.TicketForm;
import railway.application.system.Models.Forms.UserForm;
import railway.application.system.Models.Ticket;
import railway.application.system.Service.EmailService.EmailService;

import java.util.Arrays;
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
    String accountNoNotExistException = "!!! Account Doesn't Exist in our records!!!";
    String noTrainsExistException = " !!! There is no train exist on this train Information !!!";
    String trainNotRunningOnThisDayException = "This train is not running usually on that day";
    // *-----------------------------------------------------------------*




    // *--------------------------------------------- Train Functionalities--------------------------------------------*

    // *---------------- get Train -------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackTrain")
    public String getTrain(String train_info) {
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/displaytrain/"+train_info,String.class);
    }
    public String getFallBackTrain(String station)
    {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *----------------------------------------*

    // *----------- Train Time Table -----------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackTrainTimeTable")
    public String trainTimeTable(String station) {
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/timeTableByYourCity/"+station,String.class);
    }
    public String getFallBackTrainTimeTable(String station)
    {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *----------------------------------------*

    // *--------- Train Between Station --------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackTrainBetweenStation")
    public String trainBetweenStation(String origin, String destination) {
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/trainsBetweenStation/"+origin+":"+destination,String.class);
    }
    public String getFallBackTrainBetweenStation(String station)
    {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *----------------------------------------*

    // *------------ Train fares ---------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackTrainFare")
    public String trainFares(String origin, String destination) {
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/trainFare/"+origin+":"+destination,String.class);
    }
    public String getFallBackTrainFare(String station,String station2)
    {
        return "!!! Service is Down !!! , Please Try Again Later";
    }
    // *-----------------------------------------*

    // *------------- Train Location ------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallBackTrainLocation")
    public String trainLocation(String train_info, String your_location, String day) {
        return restTemplate.getForObject("http://RAILWAY-API-GATEWAY/trains/timeTableByYourCity/"+train_info+":"+your_location+":"+day,String.class);
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
        if(!(s=userFormValidation(reservationForm.getUserForm())).equals("success"))
            return s;
        if(!(s=bankFormValidation(reservationForm.getTicketForm())).equals("success"))
            return s;
        return "new ticket";
    }

    @HystrixCommand(fallbackMethod = "getFallbackFormBankValidation")
    private String bankFormValidation(TicketForm ticketForm)
    {
        try{
            if(!restTemplate.getForObject("http://RAILWAY-API-GATEWAY:8085/user/userExistById/"+ticketForm.getTrain_no(),Boolean.class))
            {
                throw new NoTrainExistException(noTrainsExistException);
            }
        }
        catch (Exception e)
        {

        } 

        return "success";
    }
    public String getFallbackFormBankValidation(BankForm bankForm)
    {

        return "!!! Service is Down !!! , Please Try Again Later";
    }

    @HystrixCommand(fallbackMethod = "getFallbackUserFormValidation")
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
    // *----------------------------------------------------------------------------------------------------------------*
}
