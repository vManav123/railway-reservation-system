package railway.reservation.system.service.realtimeService;

public interface RealTimeService {
    public boolean checkIfAny_Ticket_Is_In_WaitingMode();
    public boolean checkIfAny_Ticket_Is_Cancelled();
    public boolean any_Ticket_Is_OverDue();
    public String delete_Overdue_Ticket();
    public String reEvaluate_Ticket();
}
