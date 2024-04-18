# RPGText

RPGText introduces a system that mimics the dialogue mechanics of RPG games, displaying text sequentially in the action bar.

Users can customize messages, colors, sounds, speeds, and more.

As a tool for crafting narratives, it allows the display of options, the setting and computation of variables, and branching based on those variables.

### Features:
- Seamlessly craft stories in the style of RPGs.
- Create and display messages in the action bar.
- Assign messages to entities and display them upon interaction.
- Provide choices for players to select from.
- Define and calculate variables specific to each player.
- Utilize variables to influence story paths.
- Develop and manage stories with specialized commands.

### Tutorial: 

### Commands:

- /rpgtext help - Lists all the commands of this plugin and start the tutorial.
- /rpgtext reload - Reload configs.
- /rpgtext text <player> <text> - Send message in the style an RPG game.
- /rpgtext config <player> <path> - Send message from the yml file.
- /rpgtext character <name> <path> - Set the file path of the message sent to the player when clicking on that entity.
- /rpgtext freeze clear - Allows all frozen players to move.
- /rpgtext freeze toggle <player> -Switch the player's frozen state.
 

### Configuration:

- `default:`
  - `message:`
    - `sound: "block.note.bass"`
    - `volume: 1`
    - `pitch: 2`
    - `speed: 1`
  - `selection:`
    - `move:`
      - `sound: "block.note.hat"`
      - `volume: 1`
      - `pitch: 1`
    - `select:`
      - `sound: "entity.arrow.hit_player"`
      - `volume: 1`
      - `pitch: 1`
  - `freeze:`
    - `horizontal: true`
    - `vertical: false`
    - `jump: true`
    - `invincible: true`
    - `leftclick-cancel: true`
    - `rightclick-cancel: true`
  - `click-type: left`

### Descriptions
- **message** - Default sound settings for sending messages.
- **selection** - Settings default sound when selecting an option.
- **freeze** - Settings for frozen player movement restriction, invincibility, and click invalidation.
- **click-type** - Setting for conversation click type.
 

### Generated file/folder:

- characters.yml - Save the item set with the command "/rpgtext character".
- scoreboard.yml - Save defined variables.
- messages - Save the yml file that wrote the message.
- Tutorial.yml - Save the tutorial to be displayed when the "/rpgtext help" is executed.
 
