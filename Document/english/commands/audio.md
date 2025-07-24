# Audio Control Commands

Commands for controlling sound effects within the game.

## 🔊 `/singlesound` - Single Sound Playback

Play a sound effect once at a specific timing.

### Basic Syntax

```yaml
/singlesound <sound_name> [volume] [pitch]
```

### Parameters

- **sound_name**: Minecraft sound identifier
- **volume**: 0.0~1.0 (optional, default: 1.0)
- **pitch**: 0.5~2.0 (optional, default: 1.0)

## 🎵 Basic Usage Examples

### Simple Sound Playback

```yaml
sound_examples:
  - /singlesound minecraft:entity.player.levelup
  - Played level up sound
  - /singlesound minecraft:block.note.bell
  - Played bell sound
  - /singlesound minecraft:entity.experience_orb.pickup
  - Played experience gain sound
```

### Volume and Pitch Adjustment

```yaml
volume_examples:
  - /singlesound minecraft:block.note.bell 0.5
  - Quieter volume
  - /singlesound minecraft:block.note.bell 1.0
  - Normal volume
  - /singlesound minecraft:entity.lightning_bolt.thunder 0.3
  - Thunder sound (low volume)

pitch_examples:
  - /singlesound minecraft:block.note.bass 1 0.5
  - Low pitch
  - /singlesound minecraft:block.note.bass 1 1.0
  - Normal pitch
  - /singlesound minecraft:block.note.bass 1 2.0
  - High pitch
```

## 🎼 Commonly Used Sounds

### System Sounds

```yaml
system_sounds:
  - /singlesound minecraft:entity.player.levelup 1 1
  - Success/Achievement
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.2
  - Item acquisition
  - /singlesound minecraft:block.note.bell 1 1.5
  - Notification/Announcement
  - /singlesound minecraft:entity.villager.yes 1 1
  - Affirmation/Approval
  - /singlesound minecraft:entity.villager.no 1 1
  - Denial/Rejection
```

### Instrument Sounds

```yaml
instrument_sounds:
  - /singlesound minecraft:block.note.bass 1 1
  - Bass (low frequency)
  - /singlesound minecraft:block.note.bell 1 1
  - Bell (clear tone)
  - /singlesound minecraft:block.note.flute 1 1
  - Flute (light)
  - /singlesound minecraft:block.note.guitar 1 1
  - Guitar
  - /singlesound minecraft:block.note.xylophone 1 1
  - Xylophone (bright)
  - /singlesound minecraft:block.note.piano 1 1
  - Piano
```

### Environmental & Effect Sounds

```yaml
environment_sounds:
  - /singlesound minecraft:block.chest.open 1 1
  - Opening chest
  - /singlesound minecraft:block.door.wood.open 1 1
  - Opening door
  - /singlesound minecraft:entity.lightning_bolt.thunder 0.5 1
  - Thunder
  - /singlesound minecraft:block.fire.extinguish 1 1
  - Fire extinguishing
```

## 🎯 Practical System Examples

### 1. Dialogue Enhancement

```yaml
enhanced_conversation:
  - /singlesound minecraft:entity.villager.yes 1 1
  - Hello, traveler!
  - /singlesound minecraft:block.note.bell 1 1.2
  - I have important news for you.
  - /singlesound minecraft:entity.experience_orb.pickup 0.8 1.5
  - Here's a reward for your efforts!

emotional_dialogue:
  - /singlesound minecraft:entity.villager.hurt 1 0.8
  - I'm afraid I have bad news...
  - /singlesound minecraft:block.note.bass 1 0.5
  - The village is in danger.
  - /singlesound minecraft:entity.player.levelup 1 1.2
  - But you can save us!
```

### 2. Interactive Shop System

```yaml
shop_audio:
  - /singlesound minecraft:block.note.bell 1 1.5
  - Welcome to my shop!
  - /? Buy_Item Sell_Item Browse Leave
  - /?Buy_Item /jump purchase_sound
  - /?Sell_Item /jump sell_sound
  - /?Browse /jump browse_sound
  - /?Leave /jump farewell_sound

purchase_sound:
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.3
  - Purchase completed!
  - /singlesound minecraft:block.note.chime 1 1.8
  - Thank you for your business!

sell_sound:
  - /singlesound minecraft:entity.item.pickup 1 1.2
  - Item sold successfully!
  - /singlesound minecraft:block.note.bell 1 1.5
  - Here's your payment!

browse_sound:
  - /singlesound minecraft:ui.button.click 0.7 1.1
  - Take a look around...

farewell_sound:
  - /singlesound minecraft:entity.villager.yes 1 0.9
  - Come back anytime!
```

### 3. Battle System Audio

```yaml
combat_sounds:
  - /singlesound minecraft:entity.player.attack.strong 1 1
  - You prepare for battle!
  - /? Attack Magic Defend Flee
  - /?Attack /jump attack_sound
  - /?Magic /jump magic_sound
  - /?Defend /jump defend_sound
  - /?Flee /jump flee_sound

attack_sound:
  - /singlesound minecraft:entity.player.attack.sweep 1 1.2
  - You swing your weapon!
  - /singlesound minecraft:entity.generic.hurt 0.8 1.5
  - Direct hit!

magic_sound:
  - /singlesound minecraft:entity.evoker.cast_spell 1 1.1
  - You cast a spell!
  - /singlesound minecraft:entity.lightning_bolt.impact 0.6 1.8
  - Magic energy crackles!

defend_sound:
  - /singlesound minecraft:item.shield.block 1 1
  - You raise your guard!

flee_sound:
  - /singlesound minecraft:entity.player.hurt 1 1.5
  - You run away!
```

### 4. Atmospheric Scene Building

```yaml
forest_atmosphere:
  - You enter a peaceful forest...
  - /singlesound minecraft:block.grass.step 0.5 0.8
  - Leaves rustle beneath your feet
  - /wait 40
  - /singlesound minecraft:entity.bat.ambient 0.3 1.2
  - Strange sounds echo in the distance
  - /wait 60
  - /singlesound minecraft:block.note.flute 0.7 1.5
  - Birds sing melodiously

dungeon_atmosphere:
  - The dungeon entrance looms before you...
  - /singlesound minecraft:block.stone.step 0.8 0.6
  - Your footsteps echo in the darkness
  - /wait 30
  - /singlesound minecraft:entity.zombie.ambient 0.4 0.7
  - Something stirs in the shadows...
  - /wait 50
  - /singlesound minecraft:block.iron_door.close 0.6 0.8
  - A door slams shut behind you!
```

### 5. Musical Storytelling

```yaml
musical_sequence:
  - Long ago, in a musical kingdom...
  - /singlesound minecraft:block.note.piano 1 1
  - /wait 30
  - /singlesound minecraft:block.note.piano 1 1.2
  - /wait 30
  - /singlesound minecraft:block.note.piano 1 1.5
  - The royal melody played throughout the land
  - /wait 40
  - /singlesound minecraft:block.note.bell 0.8 1.8
  - But one day, the music stopped...
  - /wait 60
  - /singlesound minecraft:block.note.bass 1 0.5
  - And darkness fell upon the kingdom

celebration_music:
  - The festival begins!
  - /singlesound minecraft:block.note.bell 1 2
  - /wait 10
  - /singlesound minecraft:block.note.chime 1 1.8
  - /wait 10
  - /singlesound minecraft:block.note.xylophone 1 1.5
  - /wait 10
  - /singlesound minecraft:entity.player.levelup 1 1.2
  - Joy fills the air!
```

### 6. Interactive Puzzle Audio

```yaml
puzzle_mechanism:
  - You approach an ancient mechanism...
  - /singlesound minecraft:block.stone_button.click_on 1 1
  - Click! The first gear turns
  - /wait 20
  - /singlesound minecraft:block.piston.extend 0.8 1.2
  - Whirr... machinery activates
  - /wait 30
  - /singlesound minecraft:block.note.chime 1 1.5
  - Success! The puzzle is solved!

riddle_sequence:
  - Listen carefully to this riddle...
  - /singlesound minecraft:block.note.bass 1 0.8
  - /wait 40
  - I speak without a mouth...
  - /singlesound minecraft:block.note.bell 1 1.2
  - /wait 40
  - And hear without ears...
  - /singlesound minecraft:block.note.flute 1 1.5
  - /wait 40
  - What am I?
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.8
```

## 💡 Advanced Audio Techniques

### Layered Sound Design

```yaml
complex_scene:
  - The storm intensifies...
  - /singlesound minecraft:entity.lightning_bolt.thunder 0.4 0.8
  - /wait 10
  - /singlesound minecraft:weather.rain 0.6 1
  - /wait 20
  - /singlesound minecraft:entity.lightning_bolt.impact 0.3 1.2
  - /wait 15
  - /singlesound minecraft:block.note.bass 1 0.5
  - Thunder roars overhead!
```

### Dynamic Audio Based on Variables

```yaml
health_based_audio:
  - /if hp > 80 /singlesound minecraft:entity.player.levelup 1 1.2
  - /if hp > 80 You're in excellent health!
  - /if hp < 20 /singlesound minecraft:entity.player.hurt 1 0.8
  - /if hp < 20 You're badly injured...
  - /if hp >= 20 & hp <= 80 /singlesound minecraft:block.note.bell 1 1
  - /if hp >= 20 & hp <= 80 Your health is stable

mood_audio:
  - /if friendship > 50 /singlesound minecraft:entity.villager.yes 1 1.3
  - /if friendship > 50 I'm so happy to see you!
  - /if friendship < 20 /singlesound minecraft:entity.villager.no 1 0.7
  - /if friendship < 20 I don't really want to talk...
```

### Audio-Driven Gameplay

```yaml
memory_game:
  - Remember this sequence:
  - /singlesound minecraft:block.note.bell 1 1
  - /wait 30
  - /singlesound minecraft:block.note.flute 1 1
  - /wait 30
  - /singlesound minecraft:block.note.bass 1 1
  - /wait 30
  - Now repeat it back!
  - /? Bell_Flute_Bass Flute_Bell_Bass Bass_Bell_Flute
  - /?Bell_Flute_Bass /jump correct_sequence
  - Correct!

rhythm_challenge:
  - Follow the rhythm!
  - /singlesound minecraft:block.note.snare 1 1
  - /wait 20
  - /singlesound minecraft:block.note.snare 1 1
  - /wait 40
  - /singlesound minecraft:block.note.snare 1 1
  - /wait 20
  - Your turn!
```

## 🎨 Sound Categories & Selection Guide

### Emotional Atmosphere

| Mood           | Recommended Sounds                                  | Usage                 |
| -------------- | --------------------------------------------------- | --------------------- |
| **Happy**      | `entity.player.levelup`, `block.note.bell`          | Celebrations, success |
| **Mysterious** | `entity.bat.ambient`, `block.note.bass`             | Puzzles, secrets      |
| **Dramatic**   | `entity.lightning_bolt.thunder`, `block.note.piano` | Important moments     |
| **Peaceful**   | `block.note.flute`, `entity.experience_orb.pickup`  | Calm scenes           |

### Technical Considerations

1. **Volume Levels**: 0.3-0.7 for background, 0.8-1.0 for emphasis
2. **Pitch Variation**: 0.5-0.8 for low/serious, 1.2-2.0 for high/cheerful
3. **Timing**: Use `/wait` between sounds to avoid audio overlap

## ⚠️ Important Notes

### Performance & User Experience

1. **Audio Overload**: Avoid too many sounds in quick succession
2. **Volume Consideration**: Respect player audio settings
3. **Accessibility**: Don't rely solely on audio for important information

### Sound Compatibility

- Use standard Minecraft sound identifiers
- Test sounds across different Minecraft versions
- Provide visual cues alongside audio when possible

## 🔗 Related Documentation

- **[Basic Settings](basic-settings.md)** - File-wide sound settings
- **[Game Control](game-control.md)** - Timing and wait commands
- **[Practical Examples](../examples/README.md)** - Complex audio implementations
- **[Reference - Sound List](../reference/sound-list.md)** - Complete sound catalog
