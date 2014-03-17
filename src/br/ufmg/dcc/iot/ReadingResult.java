package br.ufmg.dcc.iot;

import java.util.HashSet;
import java.util.Set;

public class ReadingResult {

	private Set<String> tagIds = new HashSet<String>();
	private long noTagCount;
	private long readCount;
	private double elapsed;

	@Override
	public String toString() {
		return "ReadingResult [tagIds=" + readCount + ", noTagCount=" + noTagCount
				+ ", elapsed=" + elapsed + "] " + "read rate = " + readCount / elapsed;
	}

	public void addTagId(String id) {
		this.tagIds.add(id);
		++readCount;
	}

	public void addReads(int count) {
		readCount += count;
	}
	
	public void incReads() {
		++readCount;
	}
	
	public void incNoTagCount() {
		++noTagCount;
	}

	public void setElapsed(double elapsed) {
		this.elapsed = elapsed;
	}
}
