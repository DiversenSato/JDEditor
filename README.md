# JDEditor
A simple library for Java that can alter or create Geometry Dash levels.

# Reference guide
All public classes and methods are listed below:

- [Manager](#class-manager)
- [Level](#class-level)
- [GDObject](#class-gdobject)
- [Color](#class-color)
- [HSB](#class-hsb)
- [Base64Functions](#class-base64functions)
- [Constants](#class-constants)

---

## class Manager
**All changes made to or by the manager won't take effect in-game, unless `save()` is called!**

---

### `new Manager()`:
Automatically loads from CCLocalLevels.dat.

### `Level getLevel(String levelName)`:
Returns a [Level](#class-level) object that represents the level with the name: `levelName`.

### `Level createLevel(String levelName)`:
Returns a new [Level](#class-level) object, created by the `levelName`

### `boolean deleteLevel(String levelName)`:
Attempts to delete the [Level](#class-level) with the name: `levelName`. Returns true if successful, and false if unsuccessful. False will i.e. be returned if no level was found with that name.

### `void save()`:
Writes all data to GD's save file. If this method is not called, no changes can be seen in-game.

### `static byte[] decompress(byte[] zippedBytes)`:
Returns an unzipped byte array (using Java's GZIP methods) from an input byte array. This is mostly used internally to decode GD's save file, but here you go.

### `static byte[] xor(byte[] bytes)`:
Returns a new byte array where all bytes have been xor'ed with 11 from `bytes`.

### `static byte[] sanitize(byte[] bytes)`:
Sometimes GD likes to put a few extra characters at the end of a save file to mess with Base64 decoders or something. This method takes in a byte array and prepares it for Base64 decoding. The returned byte array is thus sometimes a little shorter than the input!

### `static void copy(String theString)`:
Copies a string to the clipboard. Originally meant for debugging, but no harm leaving it in.

---

## class Level
The Level class is a container that stores level settings, stats, colors and objects.
It is meant to represent the levels in-game.

**Note: This class has no public constructor, only the manager can return Level objects!**

---

### `GDObject addObject(int id, int x, int y)`:
Returns a new [GDObject](#class-gdobject) object with the specified object id and world position. This object is also added to the level.

### `void addObject(GDObject o)`:
Adds a [GDObject](#class-gdobject) to this Level.

### `void deleteObjects()`:
Deletes all objects this Level contains.

### `Color getColor(int colorChannel)`:
Returns the HSB object that represents the specified color channel. If no HSB object can be found, null will be returnd instead.

### `String getName()`:
Returns the name of this Level.

### `String getDescription()`:
Returns the description of this Level object.

### `boolean setDescription(String desc)`:
Sets the description of this Level. If successful, this method returns true. Returns false if the input String is longer than 140 characters.

---

## class GDObject
The GDObject class represents an object from GD. It stores object related information such as object id and position.

---

### `new GDObject(int id, int x, int y)`:
The constructor for a GDObject takes in an id, x position and a y position.

### `static GDObject createText(int x, int y, String text)`:
This method returns a new GDObject representing a text object inside GD. It takes a position and the text that should be displayed.

---

## class Color
The Color class represents a color stored by GD. Stored colors contain a lot of different information such as RGB, blending, color channels and HSB.

---

### `new Color(String data)`:
The constructor for a Color takes in a data string in same format that is stored by GD. That is a series of key-value pairs separated by "_".
An example of this can look like: "`1_255_2_0_3_0`" however, the [Level](#class-level) is responsible for reading and loading colors.

### `String storageFormat()`:
Returns the formatted version of this Color.

---

## class HSB
The HSB class represents the HSB data when copy color is toggled on for a Color.

---

### `new HSB()`:
This empty constructor sets the default values of the HSB object.

### `new HSB(String data)`:
Like the [Color](#class-color), HSB can be constructed from a formatted String. This constructor throws a NumberFormatException if there are not 5 values.

### `String storageFormat()`:
The formatted version of this HSB object can be returned by calling this method.

## #`int getHue()`:
Returns the hue of this object.

### `void setHue(int hue)`:
Sets the hue of this object. The minimum hue is -180 and the maximum is 180. This method will make sure this stays true.

### `int getSaturation()`:
Returns the saturation of this object.

### `void setSaturation(int saturation)`:
Sets the saturation of this object. The range is 200, but if the saturation toggle is set, it's from -100-100. Otherwise it's 0-200.

### `int getBrightness()`:
Returns the brightness of this object.

### `void setBrightness(int brightness)`:
Sets the brightness of this object. The range is 200, but if the brightness toggle is set, it's from -100-100. Otherwise it's 0-200.

### `int getSaturationToggle()`:
Returns the state of the saturation toggle for this object. 0 for false/un-set, 1 for true/set.

### `void setSaturationToggle(boolean saturationToggle)`:
Sets the saturation toggle for this object according to `saturationToggle`. Automatically shifts the saturation, so it fits its range.

### `int getBrightnessToggle()`:
Returns the state of the brightness toggle for this object. 0 for false/un-set, 1 for true/set.

### `void setBrightnessToggle(boolean brightnessToggle)`:
Sets the brightness toggle for this object according to `brightnessToggle`. Automatically shifts the brightness, so it fits its range.

---

## class Base64Functions
This class is a container for helper functions for decoding and encoding Base64.

---

### `static byte[] decode(byte[] bytes)`:
Returns a new decoded byte array. The input, `bytes`, is a Base64URL encoded byte array.

### `static String encode(String str)`:
Encodes `str` as a new String and returns the new String using Base64URL.

### `static boolean isBase64Char(byte b)`:
Returns whether `b` is a Base64 character or not.

---

## class Constants
This class is pretty much what it says. It contains a bunch of constants relating to game mode and game speed.
**Note: This class does not contain any methods!**
