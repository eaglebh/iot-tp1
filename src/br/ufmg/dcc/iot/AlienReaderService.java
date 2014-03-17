package br.ufmg.dcc.iot;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.alien.enterpriseRFID.notify.Message;
import com.alien.enterpriseRFID.notify.MessageListener;
import com.alien.enterpriseRFID.notify.MessageListenerService;
import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.alien.enterpriseRFID.reader.AlienReaderConnectionException;
import com.alien.enterpriseRFID.reader.AlienReaderConnectionRefusedException;
import com.alien.enterpriseRFID.reader.AlienReaderException;
import com.alien.enterpriseRFID.reader.AlienReaderNotValidException;
import com.alien.enterpriseRFID.reader.AlienReaderTimeoutException;
import com.alien.enterpriseRFID.tags.Tag;

public class AlienReaderService implements ReaderService, MessageListener {

	private AlienClass1Reader reader;
	private long noTagCount;
	private ReadingResult readingResult;

	public AlienReaderService() {
		reader.setConnection("150.164.9.34", 23);
		reader.setUsername("alien");
		reader.setPassword("password");
	}

	public ReadingResult doSyncReads(int tries) {
		readingResult = new ReadingResult();
		// Open a connection to the reader
		try {
			reader.open();

			long startTime = System.nanoTime();
			noTagCount = 0;
			for (int k = 0; k < tries; ++k) {
				Tag tagList[] = reader.getTagList();
				if (tagList == null) {
					++noTagCount;
				} else {
					for (int i = 0; i < tagList.length; i++) {
						Tag tag = tagList[i];
						readingResult.add(tag.getTagID());
					}
				}
			}

			long endTime = System.nanoTime();
			readingResult.setNoTagCount(noTagCount);
			readingResult.setElapsed((endTime - startTime) / 1000000000.0);

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

	public ReadingResult doAsyncReads(int timeoutInMillis) {
		readingResult = new ReadingResult();
		noTagCount = 0;
		// Set up the message listener service
		MessageListenerService service = new MessageListenerService(4000);
		service.setMessageListener(this);
		try {
			service.startService();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			reader.open();
			System.out.println("Configuring Reader");

			reader.setNotifyAddress(
					InetAddress.getLocalHost().getHostAddress(),
					service.getListenerPort());
			reader.setNotifyFormat(AlienClass1Reader.XML_FORMAT);

			reader.setNotifyTrigger("TrueFalse");
			reader.setNotifyMode(AlienClass1Reader.ON);

			// Set up AutoMode
			reader.autoModeReset();
			reader.setAutoStopTimer(1000); // Read for 1 second
			reader.setAutoMode(AlienClass1Reader.ON);

			// Close the connection and spin while messages arrive
			reader.close();
			long startTime = System.currentTimeMillis();
			do {
				Thread.sleep(1000);
			} while (service.isRunning()
					&& (System.currentTimeMillis() - startTime) < timeoutInMillis);
			readingResult
					.setElapsed((System.currentTimeMillis() - startTime) / 1000.0);
			// Reconnect to the reader and turn off AutoMode and TagStreamMode.
			System.out.println("\nResetting Reader");
			reader.open();
			reader.autoModeReset();
			reader.setNotifyMode(AlienClass1Reader.OFF);
		} catch (AlienReaderException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			reader.close();
		}

		return readingResult;
	}

	/**
	 * A single Message has been received from a Reader.
	 * 
	 * @param message
	 *            the notification message received from the reader
	 */
	public void messageReceived(Message message) {
		if (message.getTagCount() == 0) {
			++noTagCount;
		} else {
			for (int i = 0; i < message.getTagCount(); i++) {
				Tag tag = message.getTag(i);
				readingResult.add(tag.getTagID());
			}
		}
	}
}
