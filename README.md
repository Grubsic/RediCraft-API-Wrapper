# RediCraft API Java Wrapper
This is the java wrapper of the api from **RediCraft** described in: http://api.redicraft.eu/
**Please read the important information at the end of this readme.**

---
## Usage
There are four basic interfaces to obtain the data from http://api.redicraft.eu/, this interfaces are:

* `User`
* `Radio`
* `Server`
* `World`

Each one has a series of methods with relevant information.

### To obtain a user information, use:

`User user = RediCraft.getUserInfo(userUUID);`

Where `userUUID` is an instance of the Java Class `UUID`.

This method throws an exception if the player is not found.

### To obtain the current radio info, use:

`Radio radio = RediCraft.getRadioInfo();`

If the radio information is not available, it will throw an exception.

### To obtain the info of a server, use:

`Server server = RediCraft.getServerInfo(RCServerList);`

Where `RCServerList` is an `enum`, you have to select an option, for example: `RCServerList.LOBBY`.

### To check if a player is whitelisted, use:

`RediCraft.isUserWhitelisted(userUUID);`

Where `userUUID` is an instance of the Java Class `UUID`.

This method returns a boolean, `true` if the user is whitelisted, `false` if opposite.

### To obtain a list of the worlds from a server, use:

`List<World> worlds = RediCraft.getWorldList(RCServerList);`

Where `RCServerList` is an `enum`, you have to select an option, for example: `RCServerList.LOBBY`.

---

# Important Information

Since minecraft UUID's are not formatted in the standard Java UUID with dashes, there's a small class to handle UUID's
named `UUIDUtils`. Includes the methods:

`public static String trimmedToFull(String uuid);`

`public static String trimmedToFull(UUID uuid);`

`public static String fullToTrimmed(String uuid);`

`public static String fullToTrimmed(UUID uuid);`

You can build from a minecraft UUID, for example: `42e325b439f849efa4d09279ba851be5`

You can use: `trimmedToFull("42e325b439f849efa4d09279ba851be5");`, this will return the `String`: `"42e325b4-39f8-49ef-a4d0-9279ba851be5"`

Then you can build an `UUID` object with `UUID.fromString("42e325b4-39f8-49ef-a4d0-9279ba851be5");`.

UUID Class **IS NOT** compatible with this format: `42e325b439f849efa4d09279ba851be5`

That will throw an exception.

---

# Important note:

Due to API small lifetime, the method `getTps();` will return always `20` (default Minecraft TPS). This is going to be fixed soon.
