# RPGText

 

RPGText add a system that can talk like RPG game. Display strings one by one in the action bar.

You can set up the messages, color, sound, speed, and more.

As a function useful for story creation, you can display options, set and calculate variables, and branch by variables.

 

 

Features:

Easily create stories like RPG game.
Create messages and show in action bar.
Set messages to entity and show when click that.  
Make option that players can choose.
Set variables for each player.
Calculate and display variables.
Set story branching with variables.
Produce and manage stories with unique commands.
 Tutorial:



 

Commands:

/rpgtext help - Lists all the commands of this plugin and start the tutorial.
/rpgtext reload - Reload configs.
/rpgtext text <player> <text> - Send message in the style an RPG game.
/rpgtext config <player> <path> - Send message from the yml file.
/rpgtext character <name> <path> - Set the file path of the message sent to the player when clicking on that entity.
/rpgtext freeze clear - Allows all frozen players to move.
/rpgtext freeze toggle <player> -Switch the player's frozen state.
 

Configuration:

default:
  message:
    sound: "block.note.bass"
    volume: 1
    pitch: 2
    speed: 1
  selection:
    move:
      sound: "block.note.hat"
      volume: 1
      pitch: 1
    select:
      sound: "entity.arrow.hit_player"
      volume: 1
      pitch: 1
  freeze:
    horizontal: true
    vertical: false
    jump: true
    invincible: true
    leftclick-cancel: true
    rightclick-cancel: true
  click-type: left
message - Default sound settings for sending messages.
selection - Settings default sound when selecting an option.
freeze - Settings for frozen player movement restriction, invincibility, and click invalidation.
click-type - Setting for conversation click type.
 

Generated file/folder:

characters.yml - Save the item set with the command "/rpgtext character".
scoreboard.yml - Save defined variables.
messages - Save the yml file that wrote the message.
Tutorial.yml - Save the tutorial to be displayed when the "/rpgtext help" is executed.
 
