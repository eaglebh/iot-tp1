package br.ufmg.dcc.iot;

import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.alien.enterpriseRFID.reader.AlienReaderConnectionException;
import com.alien.enterpriseRFID.reader.AlienReaderConnectionRefusedException;
import com.alien.enterpriseRFID.reader.AlienReaderException;
import com.alien.enterpriseRFID.reader.AlienReaderNotValidException;
import com.alien.enterpriseRFID.reader.AlienReaderTimeoutException;
import com.alien.enterpriseRFID.tags.Tag;

public class AlienReaderService implements ReaderService {
	
	private AlienClass1Reader reader;
	
	public AlienReaderService() {
		reader.setConnection("150.164.9.34", 23);
		reader.setUsername("alien");
		reader.setPassword("password");
	}

	public ReadingResult doSyncReads(int timeout, int tries) {
		ReadingResult readingResult = new ReadingResult();
		// Open a connection to the reader
		try {
			reader.open();
			
			long startTime = System.nanoTime();
			long readCount = 0;
			long noTagCount = 0;
			for (int k = 0; k < tries; ++k) {
				Tag tagList[] = reader.getTagList();
				if (tagList == null) {
					++noTagCount;
				} else {
					++readCount;
					for (int i = 0; i < tagList.length; i++) {
						Tag tag = tagList[i];
						readingResult.add(tag.getTagID());			
					}
				}
			}
			
			long endTime = System.nanoTime();
			readingResult.setNoTagCount(noTagCount);
			readingResult.setElapsed( (endTime - startTime) / 1000000000.0);
			
			System.out.println(readingResult.toString());
			
		} catch (AlienReaderConnectionRefusedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlienReaderNotValidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlienReaderTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlienReaderConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlienReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Close the connection
			reader.close();			
		}

		return readingResult;

	}

	public ReadingResult doAsyncReads(int timeout) {
		// TODO Auto-generated method stub
		return null;
	}

	public ReadingResult doSyncReads(int timeout) {
		// TODO Auto-generated method stub
		return null;
	}

}
