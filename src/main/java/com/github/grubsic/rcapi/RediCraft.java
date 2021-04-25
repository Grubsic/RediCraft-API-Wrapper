package com.github.grubsic.rcapi;

import com.github.grubsic.rcapi.interfaces.Radio;
import com.github.grubsic.rcapi.interfaces.Server;
import com.github.grubsic.rcapi.interfaces.User;
import com.github.grubsic.rcapi.interfaces.World;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class RediCraft{

	public static User getUserInfo(String uuid) throws RediAPIException, IOException{ return new RCUser(uuid); }
	public static Radio getRadioInfo() throws RediAPIException, IOException{ return new RCRadio(); }
	public static Server getServerInfo(RCServerList server) throws RediAPIException, IOException{ return new RCServer(server); }

	public static boolean isUserWhitelisted(String uuid) throws IOException{
		URL url = new URL("https://api.redicraft.eu/isWhitelist?user=" + uuid);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		JSONObject userJson = new JSONObject(IOUtils.toString(con.getInputStream(), StandardCharsets.UTF_8));
		con.getInputStream().close();
		con.disconnect();

		if(userJson.getInt("status") == 1){ return true; }
		return false;
	}

	public static ArrayList<World> getWorldList(RCServerList server) throws RediAPIException, IOException{
		URL url = new URL("https://api.redicraft.eu/isWorldSettings?server=" + parseWorld(server));
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");

		JSONObject worldJson = new JSONObject(IOUtils.toString(con.getInputStream(), StandardCharsets.UTF_8));
		con.getInputStream().close();
		con.disconnect();

		if(worldJson.getInt("status") == 0){
			throw new RediAPIException("Server not found, server message: " + worldJson.getString("error"));
		}

		ArrayList<World> worlds = new ArrayList<>();
		for(int i = 0; i < worldJson.getJSONArray("info").length(); i++){
			JSONObject worldInfo = worldJson.getJSONArray("info").getJSONObject(i);
			RCWorld world = new RCWorld();

			world.server = worldInfo.getString("server");
			world.worldName = worldInfo.getString("world");
			world.weather = worldInfo.getString("weather");

			world.time = LocalTime.parse(
					worldInfo.getString("time"), DateTimeFormatter.ofPattern("h:m a")
			);
			world.players = worldInfo.getInt("players");

			worlds.add(world);
		}

		return worlds;
	}

	private static String parseWorld(RCServerList server){
		String sv;
		switch(server){
			case LOBBY: sv = "Lobby"; return sv;
			case CREATIVE: sv = "Creative"; return sv;
			case SURVIVAL: sv = "Survival"; return sv;
			case TOWNY: sv = "Towny"; return sv;
			case SKY_BLOCK: sv = "SkyBlock"; return sv;
			case BUNGEE_CORD: sv = "BungeeCord"; return sv;
			default: throw new IllegalStateException("Unexpected value: " + server);
		}
	}

	private static final class RCUser implements User{

		private long discordID;
		private long userID;
		private boolean isOnline;
		private long moneyCash;
		private long moneyBank;
		private String role;
		private String prefix;
		private String username;
		private Locale language;
		private String server;
		private LocalDateTime dateOfFirstJoin;
		private LocalDateTime dateOfLastJoin;
		private long playTime;
		private boolean isLoggedIn;
		private boolean isStaff;

		public RCUser(String uuid) throws RediAPIException, IOException{
			URL url = new URL("https://api.redicraft.eu/isUser?user=" + uuid);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");

			JSONObject userJson = new JSONObject(IOUtils.toString(con.getInputStream(), StandardCharsets.UTF_8));
			con.getInputStream().close();
			con.disconnect();

			if(userJson.getInt("status") == 0){
				throw new RediAPIException("User not found, server message: " + userJson.getString("error"));
			}

			JSONObject userInfo = userJson.getJSONArray("info").getJSONObject(0);
			discordID = userInfo.getLong("discord-id");
			userID = userInfo.getLong("user-id");
			isOnline = userInfo.getString("online").equals("1");
			moneyCash = userInfo.getLong("money-cash");
			moneyBank = userInfo.getLong("money-bank");
			role = userInfo.getString("user-role");
			prefix = userInfo.getString("user-prefix");
			username = userInfo.getString("user-name");
			language = Locale.forLanguageTag(userInfo.getString("user-language"));
			server = userInfo.getString("user-server");

			final String datePatern = "dd/MM/yy - HH:mm:ss";
			dateOfFirstJoin = LocalDateTime.parse(
					userInfo.getString("user-first-join"),
					DateTimeFormatter.ofPattern(datePatern)
			);

			dateOfLastJoin = LocalDateTime.parse(
					userInfo.getString("user-last-join"),
					DateTimeFormatter.ofPattern(datePatern)
			);


			playTime = userInfo.getLong("user-playtime");
			isLoggedIn = userInfo.getString("user-logged-in").equals("1");
			isStaff = userInfo.getString("user-is-staff").equals("1");
		}

		@Override public long getDiscordID(){ return discordID; }
		@Override public long getUserID(){ return userID; }
		@Override public boolean isOnline(){ return isOnline; }
		@Override public long getMoneyCash(){ return moneyCash; }
		@Override public long getMoneyBank(){ return moneyBank; }
		@Override public String getRole(){ return role; }
		@Override public String getPrefix(){ return prefix; }
		@Override public String getUsername(){ return username; }
		@Override public Locale getLanguage(){ return language; }
		@Override public String getServer(){ return server; }
		@Override public LocalDateTime getDateOfFirstJoin(){ return dateOfFirstJoin; }
		@Override public LocalDateTime getDateOfLastJoin(){ return dateOfLastJoin; }
		@Override public long getPlayTime(){ return playTime; }
		@Override public boolean isLoggedIn(){ return isLoggedIn; }
		@Override public boolean isStaff(){ return isStaff; }

	}

	private static final class RCRadio implements Radio{

		private String track;
		private String artist;
		private String album;
		private String playList;
		private int listeners;

		public RCRadio() throws RediAPIException, IOException{
			URL url = new URL("https://api.redicraft.eu/isRadio");
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");

			JSONObject radioJson = new JSONObject(IOUtils.toString(con.getInputStream(), StandardCharsets.UTF_8));
			con.getInputStream().close();
			con.disconnect();

			if(radioJson.getInt("status") == 0){
				throw new RediAPIException("Radio info not found, server message: " + radioJson.getString("error"));
			}

			JSONObject radioInfo = radioJson.getJSONArray("info").getJSONObject(0);
			track = radioInfo.getString("track");
			artist = radioInfo.getString("artist");
			album = radioInfo.getString("album");
			playList = radioInfo.getString("playlist");
			listeners = radioInfo.getInt("listeners");
		}

		@Override public String getTrack(){ return track; }
		@Override public String getArtist(){ return artist; }
		@Override public String getAlbum(){ return album; }
		@Override public String getPlayList(){ return playList; }
		@Override public int getListeners(){ return listeners; }

	}

	private static final class RCServer implements Server{

		private String serverName;
		private int serverID;
		private boolean online;
		private int ramUsage;
		private int availableRAM;
		private double tps;
		private String serverGameVersion;
		private boolean isStaffServer;
		private int currentPlayers;
		private int currentStaffPlayers;
		private int serverSlots;
		private boolean monitored;
		private boolean locked;
		private LocalDateTime onlineSince;

		public RCServer(RCServerList server) throws RediAPIException, IOException{
			URL url = new URL("https://api.redicraft.eu/isServer?server=" + parseWorld(server));
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");

			JSONObject serverJson = new JSONObject(IOUtils.toString(con.getInputStream(), StandardCharsets.UTF_8));
			con.getInputStream().close();
			con.disconnect();

			if(serverJson.getInt("status") == 0){
				throw new RediAPIException("Server not found, server message: " + serverJson.getString("error"));
			}

			JSONObject serverInfo = serverJson.getJSONArray("info").getJSONObject(0);
			serverName = serverInfo.getString("server-name");
			serverID = serverInfo.getInt("server-id");
			online = serverInfo.getString("online").equals("1");
			ramUsage = serverInfo.getInt("ram-usage");
			availableRAM = serverInfo.getInt("ram-available");
			tps = serverInfo.getDouble("tps");
			serverGameVersion = serverInfo.getString("version");
			isStaffServer = serverInfo.getString("staff-server").equals("1");
			currentPlayers = serverInfo.getInt("current-players");
			currentStaffPlayers = serverInfo.getInt("current-staffs");
			serverSlots = serverInfo.getInt("slots");
			monitored = serverInfo.getString("monitoring").equals("1");
			locked = serverInfo.getString("locked").equals("1");

			final String datePattern = "dd/MM/yy - HH:mm:ss";
			onlineSince = LocalDateTime.parse(
					serverInfo.getString("online-since"),
					DateTimeFormatter.ofPattern(datePattern)
			);
		}

		@Override public String getServerName(){ return serverName; }
		@Override public int getId(){ return serverID; }
		@Override public boolean isOnline(){ return online; }
		@Override public int getRAMUsage(){ return ramUsage; }
		@Override public int getAvailableRAM(){ return availableRAM; }
		@Override public double getTps(){ return tps; }
		@Override public String getServerGameVersion(){ return serverGameVersion; }
		@Override public boolean isStaffServer(){ return isStaffServer; }
		@Override public int getCurrentPlayers(){ return currentPlayers; }
		@Override public int getCurrentStaffPlayers(){ return currentStaffPlayers; }
		@Override public int getSlots(){ return serverSlots; }
		@Override public boolean isMonitored(){ return monitored; }
		@Override public boolean isLocked(){ return locked; }
		@Override public LocalDateTime getOnlineSince(){ return onlineSince; }

	}

	private static final class RCWorld implements World{

		private String server;
		private String worldName;
		private String weather;
		private LocalTime time;
		private int players;

		@Override public String getServer(){ return server; }
		@Override public String getWorldName(){ return worldName; }
		@Override public String getWeather(){ return weather; }
		@Override public LocalTime getTime(){ return time; }
		@Override public int getPlayers(){ return players; }

	}
}
