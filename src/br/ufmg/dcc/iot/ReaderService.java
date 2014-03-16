package br.ufmg.dcc.iot;

public interface ReaderService {
	ReadingResult doSyncReads(int timeout);
	ReadingResult doAsyncReads(int timeout);	
}
