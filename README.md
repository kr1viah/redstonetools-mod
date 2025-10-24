My own changes that the others probably don't want merged

Currently:
* Prevent servers from closing certain screen
* Prevent certain screens from opening
* Show cursor hotkey
  * Opens a screen that allows you to interact with chat, and close the screen upon pressing a mouse button
* Force close screen hotkey
* (Re)move chat messages that match a regex
* Chat selecting and copying
* Display currently pressed keys/mouse buttons
* Force toggle creative flight
* Prevent creative flight state change
* Main menu servers
* No main menu fade in

Downsides:
* 1.21.5 only
* No server sided functionality

Commands:
* /openscreen <screen>
  * Opens the last cached screen of a certain type

Internal:
* ConfigLabel
  * In malilib, will make a label instead of a normal config
* MappingUtils
  * Maps intermediary class names to yarn