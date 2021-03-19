package com.github.grubsic.rcapi.interfaces;

import java.time.LocalTime;

public interface World{

	String getServer();
	String getWorldName();
	String getWeather();
	LocalTime getTime();
	int getPlayers();

}
