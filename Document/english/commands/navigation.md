# Navigation Commands

Commands for moving between sections and files.

## 🧭 `/jump` - Section Navigation

Move to a specified section or to a section in another file.

### Syntax

```yaml
/jump <destination>
```

### Destination Specification Methods

#### 1. Section within Same File

```yaml
/jump section_name
```

#### 2. Section in Another File

```yaml
/jump filename.yml/section_name
```

#### 3. Files in Hierarchical Structure

```yaml
/jump folder/filename.yml/section_name
```

## 📁 Basic Usage Examples

### Navigation within Same File

```yaml
# story.yml
intro:
  - Starting the game
  - /jump chapter1

chapter1:
  - Chapter 1: The Beginning of Adventure
  - Are you ready?
  - /? Yes No
  - /?Yes /jump start_adventure
  - /?No /jump preparation

start_adventure:
  - Let's start the adventure!

preparation:
  - Let's prepare a bit more
  - /jump chapter1
```

### Moving to Other Files

```yaml
# main.yml
menu:
  - Main Menu
  - /? Story Shop Exit
  - /?Story /jump story/chapter1.yml/intro
  - /?Shop /jump shop.yml/main_shop
  - /?Exit /jump ending

# story/chapter1.yml
intro:
  - Chapter 1 begins
  - /jump main.yml/menu

# shop.yml
main_shop:
  - Welcome to the shop
  - /jump main.yml/menu
```

## 🏗️ Usage in Hierarchical Structure

### Directory Structure Example

```
messages/
├── main.yml
├── story/
│   ├── chapter1.yml
│   ├── chapter2.yml
│   └── ending.yml
├── characters/
│   ├── npc1.yml
│   └── npc2.yml
└── systems/
    ├── shop.yml
    └── battle.yml
```

### Navigation Examples

```yaml
# main.yml
main_menu:
  - /? Start Story Talk to NPC Shop
  - /?Start Story /jump story/chapter1.yml/intro
  - /?Talk to NPC /jump characters/npc1.yml/greeting
  - /?Shop /jump systems/shop.yml/main

# story/chapter1.yml
intro:
  - Chapter 1: New Departure
  - /jump story/chapter2.yml/intro

boss_fight:
  - Boss Battle!
  - /jump systems/battle.yml/boss_battle

# characters/npc1.yml
greeting:
  - Hello, adventurer
  - /jump story/chapter1.yml/npc_encounter
```

## 🔄 Conditional Navigation

### Branching by Variables

```yaml
check_progress:
  - /if chapter_completed = 1 /jump next_chapter
  - /if level > 10 /jump advanced_path
  - /jump normal_path

next_chapter:
  - Proceeding to next chapter
  - /jump chapter2.yml/intro

advanced_path:
  - Advanced route
  - /jump advanced/special.yml/start

normal_path:
  - Normal route
  - /jump story/normal.yml/start
```

### Multiple Branching by Choices

```yaml
story_branch:
  - Which path will you choose?
  - /? Forest Path Mountain Path Ocean Path
  - /?Forest Path /jump routes/forest.yml/entrance
  - /?Mountain Path /jump routes/mountain.yml/entrance
  - /?Ocean Path /jump routes/ocean.yml/entrance
```

## 💡 Practical Applications

### Dynamic Story Flow

```yaml
story_progress:
  - /if main_quest_completed = 1 /jump epilogue
  - /if side_quest_available = 1 /jump side_quest_intro
  - /jump main_story_continue

checkpoint_system:
  - Save point reached
  - /score checkpoint_id + 1
  - /jump chapter\\checkpoint_id\\/start
```

### Menu Systems

```yaml
main_menu:
  - Welcome to RPG World
  - /? New Game Continue Load Game Settings
  - /?New Game /jump tutorial/intro.yml/start
  - /?Continue /jump save/continue.yml/load_game
  - /?Load Game /jump save/load.yml/menu
  - /?Settings /jump config/settings.yml/main

settings_menu:
  - Game Settings
  - /? Audio Display Controls Back
  - /?Audio /jump config/audio.yml/settings
  - /?Display /jump config/display.yml/settings
  - /?Controls /jump config/controls.yml/settings
  - /?Back /jump main_menu
```

## ⚠️ Important Notes

### File Path Rules

1. **Relative Paths**: All paths are relative to the `messages/` folder
2. **Case Sensitivity**: File and folder names are case-sensitive
3. **Extension**: Always include `.yml` extension for files

### Error Prevention

```yaml
# ✅ Correct examples
- /jump section_name
- /jump folder/file.yml/section
- /jump ../parent_folder/file.yml/section

# ❌ Common mistakes
- /jump Section_Name # Case mismatch
- /jump folder/file/section # Missing .yml
- /jump ./folder/file.yml/section # Unnecessary ./
```

### Performance Considerations

1. **Avoid Circular Loops**: Prevent infinite jumping between sections
2. **Deep Nesting**: Limit deep hierarchical jumps for better performance
3. **File Organization**: Group related sections in the same file when possible

## 🔗 Related Documentation

- **[Conditions](conditions.md)** - Conditional jumping with /if
- **[Variables](variables.md)** - Using variables for dynamic navigation
- **[Basic Structure](../getting-started/basic-structure.md)** - Understanding sections
