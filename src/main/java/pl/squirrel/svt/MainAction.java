/**
 * http://codeoftheday.blogspot.com/2013/07/maven-struts2-annotations-and-tiles.html
 */
package pl.squirrel.svt;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport {
	
	private String serverTime;
	private static int totalVisits;
	
	public String showServerTime ()
	{
		serverTime = SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));
		System.out.println("serverTime = " + serverTime);
		return "success";
	}
	
	public String showTotalVisits ()
	{
		System.out.println("totalVisits = " + ++totalVisits);
		return "success";
	}

	public String getServerTime() {
		return serverTime;
	}
	
	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}
	
	public int getTotalVisits() {
		return totalVisits;
	}
	
	public void setTotalVisits(int totalVisits) {
		MainAction.totalVisits = totalVisits;
	}

}
