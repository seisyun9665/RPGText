# Commands Reference

Detailed documentation for all available RPGText commands, organized by functionality.

## 📋 Command Categories

### 🔧 Basic Settings

- **[Basic Settings](basic-settings.md)** - sound, speed, color

### 🧭 Navigation

- **[Navigation](navigation.md)** - jump

### 🎯 Interaction

- **[Interaction](interaction.md)** - ?, /?

### 📊 Variables & Scoring

- **[Variables & Scoring](variables.md)** - score, add, variable display

### 🔀 Conditional Logic

- **[Conditions](conditions.md)** - if, multiple conditions

### 🎒 Item Management

- **[Items](items.md)** - has, removeItem

### 🎮 Game Control

- **[Game Control](game-control.md)** - command, freeze, wait, auto, skip

### 🔊 Audio Control

- **[Audio](audio.md)** - singlesound

## 🚀 Quick Reference

### Commonly Used Commands

| Command        | Purpose            | Example                             |
| -------------- | ------------------ | ----------------------------------- |
| `/sound`       | Set audio          | `/sound block.note.bell 1 1.5`      |
| `/speed`       | Display speed      | `/speed 30`                         |
| `/color`       | Text color         | `/color \6\l`                       |
| `/jump`        | Section navigation | `/jump section_name`                |
| `/? <choices>` | Display choices    | `/? yes no`                         |
| `/?<choice>`   | Branch processing  | `/?yes Good choice`                 |
| `/score`       | Variable operation | `/score money 100`                  |
| `/if`          | Conditional logic  | `/if money > 50 Purchase available` |
| `/wait`        | Wait               | `/wait 40`                          |
| `/command`     | MC command         | `/command give %player% diamond`    |

### Special Variables

| Variable       | Description     | Example            |
| -------------- | --------------- | ------------------ |
| `%player%`     | Player name     | `Hello %player%`   |
| `%level%`      | Player level    | `Level: %level%`   |
| `%hp%`         | Health          | `Health: %hp%`     |
| `%food%`       | Food level      | `Food: %food%`     |
| `\\variable\\` | Custom variable | `Money: \\money\\` |

### Color Codes

| Code | Color        | Code | Effect        |
| ---- | ------------ | ---- | ------------- |
| `\a` | Dark green   | `\l` | Bold          |
| `\b` | Aqua         | `\r` | Reset         |
| `\c` | Dark red     | `\s` | Strikethrough |
| `\d` | Light purple | `\h` | Underline     |
| `\e` | Yellow       |      |               |

## 📖 Learning Order

For beginners, we recommend learning in this order:

1. **[Basic Settings](basic-settings.md)** - Audio, speed, color settings
2. **[Navigation](navigation.md)** - Moving between sections
3. **[Interaction](interaction.md)** - Player dialogue
4. **[Variables & Scoring](variables.md)** - Game state management
5. **[Conditions](conditions.md)** - Complex processing
6. **[Game Control](game-control.md)** - Advanced control
7. **[Items](items.md)** - Inventory operations
8. **[Audio](audio.md)** - Sound effects

## ⚠️ Important Limitations

1. **Nesting Limitation**: Conditional commands cannot be nested

   ```yaml
   # ❌ This doesn't work
   - /if money > 100 /has DIAMOND 1 none Special processing
   ```

2. **Multiple Conditions**: Use `&` operator or `/jump`
   ```yaml
   # ✅ Correct method
   - /if money > 100 & level > 10 Special processing
   ```

## 💡 Effective Usage

1. **Utilize file-wide settings** - Don't repeat same settings in each section
2. **Keep sections small** - Maintain manageable sizes
3. **Use descriptive variable names** - Make them understandable later
4. **Error handling** - Account for unexpected player actions

## 🔗 Related Documentation

- **[Getting Started](../getting-started/README.md)** - Basic usage
- **[Practical Examples](../examples/README.md)** - Real usage examples
- **[Reference](../reference/README.md)** - Detailed technical information
- **[Troubleshooting](../troubleshooting/README.md)** - Problem solving
