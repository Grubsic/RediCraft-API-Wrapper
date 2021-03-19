package com.github.grubsic.rcapi.interfaces;

import java.time.LocalDateTime;
import java.util.Locale;

public interface User{

	long getDiscordID();
	long getUserID();
	boolean isOnline();
	long getMoneyCash();
	long getMoneyBank();
	String getRole();
	String getPrefix();
	String getUsername();
	Locale getLanguage();
	String getServer();
	LocalDateTime getDateOfFirstJoin();
	LocalDateTime getDateOfLastJoin();
	long getPlayTime();
	boolean isLoggedIn();
	boolean isStaff();

}
