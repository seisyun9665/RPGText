# Basic Structure

This guide explains the fundamental structure and syntax of RPGText script files.

## File Format

RPGText uses **YML files** to write scripts.

### File Storage Location

```
plugins/RPGText/messages/
├── your_story.yml
├── chapter1/
│   └── dialogue.yml
└── tutorial.yml
```

## Basic Structure

### Understanding Sections

YML files are organized into units called **sections**:

```yaml
section_name:
  - Message1
  - Message2
  - /command example
  - Message3

another_section:
  - Message from another section
```

### Section Naming Rules

- Use alphanumeric characters and underscores (`_`)
- Avoid Japanese characters (may cause errors)
- Use descriptive names

```yaml
# ✅ Good examples
intro:
village_elder:
shop_conversation:

# ❌ Avoid these
セクション1:
"test section":
```

## Writing Messages

### Basic Messages

```yaml
simple_talk:
  - Hello!
  - It's a beautiful day today.
  - See you later.
```

### Inserting Commands

Commands can be inserted between messages:

```yaml
with_commands:
  - Hello!
  - /wait 40
  - (pausing for a moment)
  - /sound block.note.bell 1 1
  - Beautiful weather, isn't it?
```

## File-wide Settings

Settings written at the beginning of the file apply to the entire file:

```yaml
# File-wide settings
sound: block.note.bass 1 2
speed: 20
color: \r

# Section definitions
intro:
  - Starting the game
  - Thank you for playing

dialogue:
  - Hello
  - How are you doing?
```

### Configurable Settings

- `sound: <sound_name> <volume> <pitch>` - Default sound
- `speed: <number>` - Text display speed (characters per second)
- `color: <color_code>` - Default text color

## Practical Example

### hello.yml

```yaml
# Greeting script
sound: block.note.bell 1 1.5
speed: 25
color: \2

greeting:
  - /color \6\l
  - Hello!
  - /color \r
  - Welcome to the world of RPGText.
  - Let's create wonderful stories with this plugin.

farewell:
  - Well then, see you again!
  - /singlesound minecraft:entity.player.levelup
  - Goodbye!
```

## Next Steps

Now that you understand the basic structure, proceed to [First Script](first-script.md) to create your first working script.

## Related Documentation

- **[Commands Reference - Basic Settings](../commands/basic-settings.md)** - Details on sound, speed, color
- **[Commands Reference - Navigation](../commands/navigation.md)** - Moving between sections with jump
- **[Reference - Color Codes](../reference/color-codes.md)** - Available color list
