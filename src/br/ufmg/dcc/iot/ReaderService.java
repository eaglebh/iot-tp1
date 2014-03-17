package br.ufmg.dcc.iot;

public interface ReaderService {
	ReadingResult doSyncReads(int tries);
	ReadingResult doAsyncReads(int timeoutInMillis);	
}
