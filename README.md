# nova's Music Script
nova's Music Script is a mod that exposes the machinations of the Minecraft music system to the end user, to be controlled via python scripts via Jython, and even allows you to play your own songs!

# How it Works
There are several events that will trigger a script to be run. These events are, respectively
- play-music.py, which will play music at an interval determined by the delay (by default 50 seconds) after a song has finished
- change-dimension.py, which triggers every time a player changes what dimension they are in
- player-respawn.py, which triggers when a player respawns

These scripts should be placed in the mods config folder.

```
if data["dimension"] == "minecraft:the_end":
    output = "play minecraft:music.end; delay 10000"
elif data["dimension"] == "minecraft:overworld":
    output = "play minecraft:music.game"
else:
    output = "play minecraft:music.nether.nether_wastes minecraft:music.nether.crimson_forest; delay 2000 4000"
```

Here is a script. Note the data and output variables. data is set by the mod, do not set it in your own script, or else it will not work. This will make python whine about data not being set, but that is okay.

data is a dictionary. It tells the script about the state of the player and of the world. The information it shares is as follows

- `data["dimension"]` is the dimension the player is in. For vanilla Minecraft, these will be "minecraft:overworld", "minecraft:the_nether", and "minecraft:the_end". This is a string.
- `data["biome"]` is the biome the player is in. This will be the same as what you see describing the biome you're in in the F3 menu. This is a string.
- `data["biome_temperature"]` is the temperature of the biome you're in. This is a float.
- `data["time"]` is the current time of day. This is an integer.
- `data["x"]` is the player's X coordinate. This is a float.
- `data["y"]` is the player's Y coordinate. This is a float.
- `data["z"]` is the player's Z coordinate. This is a float.

output is a variable the user sets in the script. This variable is read by the mod and tells it what to do.
The output string is essentially a list of commands, seperated by semicolons. There are three kinds of commands.
- `stop` will stop all songs playing.
- `play` will randomly play any of the songs listed after it.
- `delay` will set the delay after a song finishes before `play-song.py` will trigger. If it has 1 integer following it, it will set the delay to that number. If it has 2, it will randomly pick a number in that range.

The way a song is identified either uses the Minecraft resource location or the filepath of a file within the songs directory of the config folder.
For example, `play song.wav` will play the file `song.wav` in the `songs` directory, but `play Music/song.wav` will play the file `song.wav` in the `Music` directory in the `songs` directory.
Only `.wav` and `.aiff` files are supported right now. I'll work on expanding support for other filetypes later.

# A word
This mod is largely untested. I can not guarantee that it will work, but if it does not, please let me know so that I can fix it.

Available only on Fabric and possibly Quilt for now. A Forge port and later Neoforged for newer versions of Minecraft will be in the works.
Requires Architectury and Fabric Language Kotlin.