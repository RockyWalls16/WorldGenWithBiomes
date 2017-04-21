# WorldGenWithBiomes
A world generator using simplex noise to generate heights and biomes.
This algorithme approximately recreate a minecraft 1.7.2 world map.

# Frame controls
* [B]: Show biomes
* [H]: Show height map
* [T]: Show temperature map
* [M]: Show moist map
* [C]: Show chunk grid
* [G]: Generate a new world
* [S]: Save world to map.png on your desktop

# Screenshots
## Biomes
![alt tag](https://github.com/valentin4311/WorldGenWithBiomes/blob/master/screenshots/biome-1.png)
![alt tag](https://github.com/valentin4311/WorldGenWithBiomes/blob/master/screenshots/biome-2.png)
![alt tag](https://github.com/valentin4311/WorldGenWithBiomes/blob/master/screenshots/biome-3.png)

## Heightmap

Inside biomes there are height variations you may not see in heightmap since color difference is not visible enough
![alt tag](https://github.com/valentin4311/WorldGenWithBiomes/blob/master/screenshots/heightmap-1.png)

## Temperature map & Moist map
Temperature

![alt tag](https://github.com/valentin4311/WorldGenWithBiomes/blob/master/screenshots/temperaturemap-1.png)

Moist

![alt tag](https://github.com/valentin4311/WorldGenWithBiomes/blob/master/screenshots/moistmap-1.png)

# Disclaimer
This code is written to help people with generating world. It is not optimized (RAM & CPU) at all for I prefered to make it clear as much as posible
This code use simplex noise explained here:
[Explanation](http://webstaff.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf)
And given here : 
[Source](http://weber.itn.liu.se/~stegu/simplexnoise/SimplexNoise.java)

An optimized code will later be used for another project and will be written python. You can found our minecraft "clone" here :
[Repository](https://github.com/DanAurea/Trisdanvalwen)
