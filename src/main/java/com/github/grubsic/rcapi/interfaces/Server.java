package com.github.grubsic.rcapi.interfaces;

import java.time.LocalDateTime;

public interface Server{

	String getServerName();
	int getId();
	boolean isOnline();
	int getRAMUsage();
	int getAvailableRAM();
	double getTps();
	String getServerGameVersion();
	boolean isStaffServer();
	int getCurrentPlayers();
	int getCurrentStaffPlayers();
	int getSlots();
	boolean isMonitored();
	boolean isLocked();
	LocalDateTime getOnlineSince();

}
