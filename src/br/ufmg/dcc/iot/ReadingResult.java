package br.ufmg.dcc.iot;

import java.util.ArrayList;
import java.util.List;

public class ReadingResult {

	@Override
	public String toString() {
		return "ReadingResult [tagIds=" + tagIds.size() + ", noTagCount=" + noTagCount
				+ ", elapsed=" + elapsed + "] " + "read rate = " + tagIds.size() / elapsed;
	}

	private List<String> tagIds = new ArrayList<String>();
	private long noTagCount;
	private double elapsed;

	public List<String> getTagIds() {
		return tagIds;
	}

	public void setTagIds(List<String> tagIds) {
		this.tagIds = tagIds;
	}
	
	public void add(String id) {
		this.tagIds.add(id);
	}

	public void setNoTagCount(long noTagCount) {
		this.noTagCount = noTagCount;
	}

	public void setElapsed(double elapsed) {
		this.elapsed = elapsed;
	}
}
