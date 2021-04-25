[![Codacy Badge](https://app.codacy.com/project/badge/Grade/defc503b58d644c397d0fd68df039b86)](https://www.codacy.com/gh/Grubsic/RediCraft-API-Wrapper/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Grubsic/RediCraft-API-Wrapper&amp;utm_campaign=Badge_Grade)
[![](https://jitpack.io/v/Grubsic/RediCraft-API-Wrapper.svg)](https://jitpack.io/#Grubsic/RediCraft-API-Wrapper)

# RediCraft API Java Wrapper
This is the java wrapper of the api from **RediCraft** described in: http://api.redicraft.eu/
**Please read the important information at the end of this readme.**

## Usage
To add this to your project, add this repository:

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Then add the dependency:

```
<dependency>
    <groupId>com.github.Grubsic</groupId>
    <artifactId>RediCraft-API-Wrapper</artifactId>
    <version>1.0.0</version>
</dependency>
```

There are four basic interfaces to obtain the data from http://api.redicraft.eu/, this interfaces are:

* `User`
* `Radio`
* `Server`
* `World`

Each one has a series of methods with relevant information.
### To obtain a user information, use

`User user = RediCraft.getUserInfo(userUUID);`

Where `userUUID` is an instance of the Java Class `UUID`.

This method throws an exception if the player is not found.
### To obtain the current radio info, use
`Radio radio = RediCraft.getRadioInfo();`

If the radio information is not available, it will throw an exception.

### To obtain the info of a server, use
`Server server = RediCraft.getServerInfo(RCServerList);`

Where `RCServerList` is an `enum`, you have to select an option, for example: `RCServerList.LOBBY`.

### To check if a player is whitelisted, use
`RediCraft.isUserWhitelisted(userUUID);`

Where `userUUID` is an instance of the Java Class `UUID`.
This method returns a boolean, `true` if the user is whitelisted, `false` if opposite.
### To obtain a list of the worlds and their information from a server, use
`List<World> worlds = RediCraft.getWorldList(RCServerList);`

Where `RCServerList` is an `enum`, you have to select an option, for example: `RCServerList.LOBBY`.
