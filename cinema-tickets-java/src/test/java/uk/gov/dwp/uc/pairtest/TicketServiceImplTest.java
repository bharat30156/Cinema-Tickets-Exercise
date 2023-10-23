package uk.gov.dwp.uc.pairtest;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.Assert.fail;

public class TicketServiceImplTest {

    private TicketServiceImpl ticketService;

    @Before
    public void setUp() {
        // Initialize the TicketServiceImpl for testing
        ticketService = new TicketServiceImpl();
    }

    @Test
    public void testPurchaseTicketWithValidData() {
        try{
            // Replace these values with valid test data
            Long accountId = 1L;
            TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
            TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);

            // Perform a test purchase
            ticketService.purchaseTickets(accountId, adultTicket, childTicket);

            // Add assertions for excepted results or behaviour
        } catch (InvalidPurchaseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketWithInvalidAccount() {
        // Attempt to purchase tickets with an invalid account ID
        Long accountId = -1L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);

        ticketService.purchaseTickets(accountId, adultTicket);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsWithZeroTickets(){
        // Attempt to purchase zero tickets
        Long accountId = 1L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0);

        ticketService.purchaseTickets(accountId, adultTicket);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketWithTooManyTickets() {
        // Attempt to purchase more than the maximum allowed tickets
        Long accounId = 1L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 21);

        ticketService.purchaseTickets(accounId, adultTicket);
    }

    @Test
    public void testPurchaseTicketsWithInfant() {
        try {
            // Purchase an infant ticket along with adult tickets
            Long accountId = 1L;
            TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
            TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

            ticketService.purchaseTickets(accountId, adultTicket, infantTicket);

            // Add assertions for expected results or behavior
        } catch (InvalidPurchaseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
}
