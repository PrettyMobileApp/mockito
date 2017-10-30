package MockitoTest;

import static org.junit.Assert.*;
import Origin.*;
import org.junit.Before;
import org.junit.Test;

import org.mockito.*;
import org.mockito.internal.verification.Times;

import static org.mockito.Mockito.*;

public class MovieStoreTest {
	private DownloadServer download;
	private PayPalFacade paypal;
	private MailServer mail;
	private MovieStoreDAO dao;
	private MovieStoreImpl movieStore;
	
	@Before
	public void setUp() throws Exception {
		download = mock(DownloadServer.class);
		paypal = mock(PayPalFacade.class);
		mail = mock(MailServer.class);
		dao = mock(MovieStoreDAO.class);		
		
	}

	@Test
	public void testSuccessfullyDownload() throws Exception {
		Account account1 = new Account( "1", "John", "john@gmail.com");
		Movie movie1 = new Movie( "disney", 5.0);
		MovieTransaction transaction1 = new MovieTransaction(account1, movie1);
		
		when(dao.findAccountForId("1")).thenReturn(account1);
		when(dao.getAvailableMovieWithTitle("disney")).thenReturn(movie1);
			
		
		movieStore = new MovieStoreImpl( dao, download, paypal, mail );
		
		movieStore.downloadMovie("1", "disney");
		
		//assertTrue(download.startDownload(movie1, account1));
		verify(download, new Times(1)).startDownload(movie1, account1);
		
		verify(dao, new Times(1)).createNewTransaction(isA(MovieTransaction.class));
		verify(paypal, new Times(1)).sendAdvice(5.0, "1");
		verify(mail, new Times(1)).send("john@gmail.com", "Download of "+"disney"+" started");
		
/*		when(inventory.getItemsExpireInAMonth()).thenReturn(expiredList);
		when(inventory.itemsUpdated()).thenReturn(1);
		int returnValue=bazar.issueDiscountForItemsExpireIn30Days(0.3);
		verify(inventory, new Times(1)).update(soap,70.0);
		verify(pas).announce(isA(Offer.class));
		assertEquals(1,returnValue);*/
		
	}
	
	@Test(expected = MovieStoreUserException.class)
	public void testAccountNotFound() throws MovieStoreException {
		Movie movie1 = new Movie( "disney", 5.0);
		when(dao.findAccountForId("1")).thenReturn(null);
		
		movieStore = new MovieStoreImpl( dao, download, paypal, mail );
		movieStore.downloadMovie("1", "disney");
	}

	@Test(expected = MovieNotAvailable.class)
	public void testMovieNotAvailable() throws MovieStoreException {
		Account account1 = new Account( "1", "John", "john@gmail.com");
		Movie movie1 = new Movie( "disney", 5.0);
		//MovieTransaction transaction1 = new MovieTransaction(account1, movie1);
		
		when(dao.findAccountForId("1")).thenReturn(account1);
		when(dao.getAvailableMovieWithTitle("disney")).thenReturn(null);
		
		movieStore = new MovieStoreImpl( dao, download, paypal, mail );
		movieStore.downloadMovie("1", "disney");
		
	}
	
	@Test(expected = MovieStoreDownloadException.class)
	public void testDownloadServerException() throws MovieStoreException {
		Account account1 = new Account( "1", "John", "john@gmail.com");
		Movie movie1 = new Movie( "disney", 5.0);
		//MovieTransaction transaction1 = new MovieTransaction(account1, movie1);
		
		when(dao.findAccountForId("1")).thenReturn(account1);
		when(dao.getAvailableMovieWithTitle("disney")).thenReturn(movie1);
		when(download.startDownload(movie1, account1)).thenThrow(MovieStoreDownloadException.class);
		
		movieStore = new MovieStoreImpl( dao, download, paypal, mail );
		movieStore.downloadMovie("1", "disney");
		
		verify(dao, new Times(1)).getAvailableMovieWithTitle("disney");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}

