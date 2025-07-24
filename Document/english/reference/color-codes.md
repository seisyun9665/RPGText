# Color Codes

Complete list of colors and decoration effects available in RPGText.

## 🎨 Basic Color Codes

RPGText uses the `\character` format to specify colors and decorations.

### 📊 Color Code List

| Code | Color        | Hex Code | Usage Examples               |
| ---- | ------------ | -------- | ---------------------------- |
| `\0` | Black        | #000000  | Shadows, outlines            |
| `\1` | Dark Blue    | #0000AA  | Night, water, ice            |
| `\2` | Green        | #00AA00  | Nature, success, health      |
| `\3` | Dark Aqua    | #00AAAA  | Ocean, refreshing            |
| `\4` | Dark Red     | #AA0000  | Blood, danger, anger         |
| `\5` | Dark Purple  | #AA00AA  | Magic, mystery, nobility     |
| `\6` | Gold         | #FFAA00  | Treasure, important, special |
| `\7` | Gray         | #AAAAAA  | Neutral, descriptions        |
| `\8` | Dark Gray    | #555555  | Shadow, subdued              |
| `\9` | Blue         | #5555FF  | Sky, water, peace            |
| `\a` | Green        | #55FF55  | Fresh leaves, hope, healing  |
| `\b` | Aqua         | #55FFFF  | Ice, clean, system           |
| `\c` | Red          | #FF5555  | Fire, warning, passion       |
| `\d` | Light Purple | #FF55FF  | Flowers, fantasy             |
| `\e` | Yellow       | #FFFF55  | Sun, caution, brightness     |
| `\f` | White        | #FFFFFF  | Pure, sacred, text           |

### 🎭 Decoration Codes

| Code | Effect            | Description         |
| ---- | ----------------- | ------------------- |
| `\l` | **Bold**          | Makes text bold     |
| `\m` | ~~Strikethrough~~ | Crosses out text    |
| `\n` | <u>Underline</u>  | Underlines text     |
| `\o` | _Italic_          | Makes text italic   |
| `\r` | Reset             | Removes all effects |

## 💡 Basic Usage Examples

### Single Color Usage

```yaml
basic_colors:
  - /color \c
  - This text is red
  - /color \a
  - This text is bright green
  - /color \r
  - Back to normal color
```

### Decoration Usage

```yaml
text_decorations:
  - /color \l
  - This text is bold
  - /color \n
  - This text is underlined
  - /color \r
  - Back to normal text
```

### Combining Colors and Decorations

```yaml
combined_effects:
  - /color \6\l
  - Important gold and bold message
  - /color \c\n
  - Red and underlined warning
  - /color \9\o
  - Blue and italic description
  - /color \r
```

## 🎯 Usage by Scene

### 🚨 Warnings & Error Messages

```yaml
warnings:
  - /color \c\l
  - ⚠️ Warning: Dangerous area
  - /color \4
  - Error: Access denied
  - /color \e\l
  - Caution: Time limit applies
  - /color \r
```

### ✅ Success & Completion Messages

```yaml
success_messages:
  - /color \a\l
  - ✓ Quest completed!
  - /color \2
  - Successfully saved
  - /color \6\l
  - Congratulations!
  - /color \r
```

### 📢 System & Information Messages

```yaml
system_messages:
  - /color \b
  - [System] Server restart in 5 minutes
  - /color \7
  - Note: This feature is experimental
  - /color \9
  - Tip: Use /help for more commands
  - /color \r
```

### 💬 Dialogue & Character Speech

```yaml
character_dialogue:
  - /color \2
  - Hero: "I must save the village!"
  - /color \5
  - Wizard: "Take this magical sword"
  - /color \c
  - Villain: "You cannot stop me!"
  - /color \r
```

## 🎨 Advanced Color Techniques

### 1. Progressive Color Changes

```yaml
story_progression:
  - /color \7
  - Once upon a time...
  - /color \9
  - In a peaceful kingdom...
  - /color \e
  - A great adventure was about to begin...
  - /color \6\l
  - And you are the hero!
  - /color \r
```

### 2. Mood-Based Coloring

```yaml
mood_system:
  - /if mood = happy /color \e\l
  - /if mood = happy Feeling great today!
  - /if mood = sad /color \1
  - /if mood = sad Feeling a bit down...
  - /if mood = angry /color \c\l
  - /if mood = angry Very frustrated!
  - /color \r
```

### 3. Health Status Colors

```yaml
health_colors:
  - Health Status:
  - /if hp > 80 /color \a
  - /if hp > 80 Excellent (%hp%/100)
  - /if hp >= 40 & hp <= 80 /color \e
  - /if hp >= 40 & hp <= 80 Good (%hp%/100)
  - /if hp < 40 /color \c
  - /if hp < 40 Critical (%hp%/100)
  - /color \r
```

### 4. Rarity-Based Item Colors

```yaml
item_rarity:
  - Item discovered:
  - /if rarity = common /color \f
  - /if rarity = common Common Item
  - /if rarity = uncommon /color \a
  - /if rarity = uncommon Uncommon Item
  - /if rarity = rare /color \9
  - /if rarity = rare Rare Item
  - /if rarity = epic /color \5
  - /if rarity = epic Epic Item
  - /if rarity = legendary /color \6\l
  - /if rarity = legendary LEGENDARY ITEM!
  - /color \r
```

## 📋 Color Selection Guide

### Emotional Associations

| Emotion/Theme         | Recommended Colors  | Codes            |
| --------------------- | ------------------- | ---------------- |
| **Success/Victory**   | Green, Gold         | `\a`, `\2`, `\6` |
| **Danger/Warning**    | Red, Dark Red       | `\c`, `\4`       |
| **Magic/Mystery**     | Purple, Dark Purple | `\d`, `\5`       |
| **Information**       | Blue, Aqua          | `\9`, `\b`       |
| **Neutral/System**    | Gray, White         | `\7`, `\f`       |
| **Special/Legendary** | Gold + Bold         | `\6\l`           |

### Readability Considerations

| Background | Good Colors            | Avoid            |
| ---------- | ---------------------- | ---------------- |
| **Dark**   | `\f`, `\e`, `\a`, `\b` | `\0`, `\8`, `\1` |
| **Light**  | `\0`, `\1`, `\4`, `\8` | `\f`, `\e`       |
| **Any**    | `\c`, `\a`, `\9`, `\6` | N/A              |

## 🎭 Creative Formatting Patterns

### 1. Header Formatting

```yaml
section_headers:
  - /color \6\l\n
  - === CHAPTER ONE ===
  - /color \r
  - /color \7
  - The story begins...
  - /color \r
```

### 2. Status Bars

```yaml
progress_bar:
  - Quest Progress:
  - /color \a
  - ████████████████████
  - /color \7
  - ░░░░░░░░░░░░░░░░░░░░
  - /color \r
  - 80% Complete
```

### 3. Dialogue Formatting

```yaml
formatted_dialogue:
  - /color \n
  - Speaker Name
  - /color \r
  - "This is what they said."
  - /color \7
  - *gestures dramatically*
  - /color \r
```

### 4. Menu Systems

```yaml
menu_formatting:
  - /color \6\l
  - ═══ MAIN MENU ═══
  - /color \r
  - /color \a
  - ▶ Start Game
  - /color \9
  - ▶ Options
  - /color \c
  - ▶ Exit
  - /color \r
```

## ⚠️ Important Notes

### Performance Considerations

1. **Color Changes**: Frequent color switching may impact readability
2. **Decoration Overuse**: Too many effects can make text hard to read
3. **Consistency**: Maintain consistent color schemes throughout your script

### Accessibility Guidelines

1. **Contrast**: Ensure sufficient contrast between text and background
2. **Color Blindness**: Don't rely solely on color to convey important information
3. **Alternative Indicators**: Use symbols and text formatting alongside colors

### Best Practices

1. **Reset Regularly**: Always use `\r` to reset colors after special formatting
2. **Meaningful Colors**: Use colors that match the content's meaning
3. **Moderation**: Less is often more - avoid overwhelming players with colors

## 🔗 Related Documentation

- **[Basic Settings](../commands/basic-settings.md)** - File-wide color settings
- **[Special Variables](special-variables.md)** - Displaying colored variables
- **[Best Practices](best-practices.md)** - Optimal color usage patterns
- **[Examples](../examples/README.md)** - Real-world color implementations
