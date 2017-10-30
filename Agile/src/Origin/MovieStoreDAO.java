package Origin;

import java.util.List;

public interface MovieStoreDAO {
	public Account findAccountForId(String accountId);
	public Movie getAvailableMovieWithTitle(String title);
	public void createNewTransaction(MovieTransaction movieDownload);
	public List<MovieTransaction> retrieveUnPaidTransactions();
}
