My own changes that the others probably don't want merged

Currently:
* Prevent servers from closing certain screen
* Prevent certain screens from opening
* Drop x amount of packets
* Lag x amount of packets
* Show cursor hotkey
  * Opens a screen that allows you to interact with chat, and close the screen upon pressing a mouse button
* Force close screen hotkey

Downsides:
* 1.21.5 only

Commands:
* /openscreen <screen>
  * Opens the last cached screen of a certain type

Internal:
* ConfigLabel
  * In malilib, will make a label instead of a normal config
* MappingUtils
  * Maps intermediary class names to yarn