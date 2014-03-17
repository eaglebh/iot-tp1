package br.ufmg.dcc.iot;

public class Console {

	public static void main(String[] args) {
		ReaderService readerService = new AlienReaderService();

		
		ReadingResult readingResult = readerService.doSyncReads(60);
		System.out.println(readingResult.toString());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		readingResult = readerService.doAsyncReads(10000);
		System.out.println(readingResult.toString());

	}

}
