package entities;

import java.util.Calendar;
import java.util.Date;

public class TimeCounter {
	private boolean activate;
	private Date start, end;
	public TimeCounter() {
		this.activate = false;
	}
	
	public Date startCounter() {
		if(!this.activate) {
			this.activate = true;
			start = Calendar.getInstance().getTime();
		}
		return start;
	}
	
	public Date stopCounter() {
		end = Calendar.getInstance().getTime();
		return end;
	}
	
	public void setActive(boolean active) {
		this.activate = active;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

}
