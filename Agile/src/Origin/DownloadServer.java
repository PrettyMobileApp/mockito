package Origin;



public interface DownloadServer {
	public boolean startDownload(Movie movie, Account account)
			throws MovieStoreException;

}
