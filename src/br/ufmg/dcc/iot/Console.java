package br.ufmg.dcc.iot;

public class Console {

	public static void main(String[] args) {
		ReaderService readerService = new AlienReaderService();
		
		readerService.doSyncReads(60);
		
		readerService.doAsyncReads(10000);
	}

}
