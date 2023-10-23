package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {

    // Maximum number of tickets allowed per purchase
    private static final int MAX_TICKETS_PER_PURCHASE = 20;

    // Dependencies injected
    private final TicketPaymentService paymentService;
    private final SeatReservationService seatReservationService;

    public TicketServiceImpl() {
        // Initialize the payment services with the actual implementation
        this.paymentService = new TicketPaymentServiceImpl();
        // Initialize the seat reservation service with actual implementation
        this.seatReservationService = new SeatReservationServiceImpl();
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        // Validate the account ID
        if (accountId <= 0) {
            throw new InvalidPurchaseException("Invalid account ID");
        }

        int totalAmountToPay = 0;
        int totalSeatsToAllocate = 0;

        for (TicketTypeRequest request : ticketTypeRequests) {
            // Validate each ticket request
            validateTicketRequest(request);

            // Calculate the total amount to pay and total seats to allocate
            totalAmountToPay += calculateTicketPrice(request);
            totalSeatsToAllocate += request.getNoOfTickets();
        }

        // Check if the total seats to allocate exceed the maximum limit
        if (totalSeatsToAllocate > MAX_TICKETS_PER_PURCHASE) {
            throw new InvalidPurchaseException("Exceeds maximum number of tickets per purchase");
        }

        // Make the payment using the payment service
        paymentService.makePayment(accountId, totalAmountToPay);
        // Reserve seats using the seat reservation service
        seatReservationService.reserveSeat(accountId, totalSeatsToAllocate);
    }

    private void validateTicketRequest(TicketTypeRequest request) throws InvalidPurchaseException {
        // Check for valid number of tickets and ticket type
        if (request.getNoOfTickets() <= 0 || request.getNoOfTickets() > MAX_TICKETS_PER_PURCHASE) {
            throw new InvalidPurchaseException("Invalid number of tickets");
        }

        // Ensure INFANT tickets are not purchased directly
        if (request.getTicketType() == TicketTypeRequest.Type.INFANT) {
            throw new InvalidPurchaseException("Infant tickets cannot be purchased directly");
        }
    }

    private int calculateTicketPrice(TicketTypeRequest request) {
        int ticketPrice;
        switch (request.getTicketType()) {
            case CHILD:
                ticketPrice = 10;
                break;
            case ADULT:
                ticketPrice = 20;
                break;
            case INFANT:
                // This should not happen, as INFANT tickets should not be included in the purchase
                throw new IllegalArgumentException("Infant tickets should not be included in the purchase");
            default:
                throw new IllegalArgumentException("Invalid ticket type");
        }
        return ticketPrice * request.getNoOfTickets();
    }

}
