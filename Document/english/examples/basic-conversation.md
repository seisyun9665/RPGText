# Basic Conversation System

Implementation example of a simple conversation system using basic RPGText features.

## 📋 Overview

This sample implements the following features:

- Basic dialogue with NPCs
- Player choice-based branching
- Basic friendship system
- Color coding for visual clarity
- Sound effects for atmosphere

## 💻 Complete Code

### friendly_npc.yml

```yaml
# Friendly NPC conversation system
# File-wide settings
sound: block.note.bell 1 1.2
speed: 25
color: \r

# Main conversation
greeting:
  - /color \2\l
  - Hello, %player%!
  - /color \r
  - /singlesound minecraft:entity.villager.yes 1 1
  - It's a beautiful day today.
  - Where are you from?
  - /? Nearby village Distant city I'm traveling
  - /?Nearby village /jump nearby_village
  - /?Distant city /jump distant_city
  - /?I'm traveling /jump traveling

# From nearby village
nearby_village:
  - /color \a
  - Oh, a neighbor!
  - /color \r
  - /score friendship + 5
  - The people from that village are all so kind...
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.2
  - They always help us out.
  - /jump ask_about_help

# From distant city
distant_city:
  - /color \6
  - You must be tired from such a long journey!
  - /color \r
  - /score friendship + 3
  - The long trip must have been difficult.
  - Is there anything you need help with around here?
  - /jump ask_about_help

# Traveler
traveling:
  - /color \d
  - A traveler!
  - /color \r
  - /score friendship + 2
  - This land has many unique places
  - and delicious foods to offer.
  - /jump ask_about_help

# Offer to help
ask_about_help:
  - /wait 20
  - Is there anything I can help you with?
  - /? Give directions Need information Want to give thanks Nothing special
  - /?Give directions /jump give_directions
  - /?Need information /jump share_information
  - /?Want to give thanks /jump receive_gift
  - /?Nothing special /jump polite_farewell

# Giving directions
give_directions:
  - /color \b
  - Where are you headed?
  - /color \r
  - /? Town center Inn Weapon shop Castle
  - /?Town center /jump directions_center
  - /?Inn /jump directions_inn
  - /?Weapon shop /jump directions_weapon
  - /?Castle /jump directions_castle

directions_center:
  - /singlesound minecraft:block.note.flute 1 1
  - Go straight down this road,
  - and when you see the big fountain, that's the center.
  - If you get lost, just ask anyone for help.
  - /jump helpful_farewell

directions_inn:
  - /singlesound minecraft:block.note.flute 1 1
  - The inn is that building with the red roof.
  - They serve excellent meals there.
  - Tell them I sent you!
  - /jump helpful_farewell

directions_weapon:
  - /singlesound minecraft:block.note.flute 1 1
  - The weapon shop is just past the market square.
  - Look for the sign with crossed swords.
  - The blacksmith there is very skilled.
  - /jump helpful_farewell

directions_castle:
  - /singlesound minecraft:block.note.flute 1 1
  - The castle is up the hill to the north.
  - Follow the stone path, you can't miss it.
  - /color \c
  - But be careful, guards are quite strict.
  - /color \r
  - /jump helpful_farewell

# Share information
share_information:
  - /color \6
  - What would you like to know?
  - /color \r
  - /? Local rumors Trading tips Dangerous areas Weather
  - /?Local rumors /jump local_rumors
  - /?Trading tips /jump trading_tips
  - /?Dangerous areas /jump danger_warnings
  - /?Weather /jump weather_info

local_rumors:
  - /color \d
  - Well, I've heard strange noises
  - coming from the old mine lately...
  - /color \r
  - Some say treasure hunters have been
  - exploring the abandoned tunnels.
  - /score friendship + 2
  - /jump information_farewell

trading_tips:
  - /color \6
  - The merchant near the fountain
  - gives fair prices for gems.
  - /color \r
  - But avoid the guy in the dark cloak -
  - his deals are too good to be true!
  - /score friendship + 2
  - /jump information_farewell

danger_warnings:
  - /color \c
  - Stay away from the eastern forest at night.
  - Strange creatures have been spotted there.
  - /color \r
  - And the bridge over Miller's Creek
  - has been weakened by recent storms.
  - /score friendship + 3
  - /jump information_farewell

weather_info:
  - /color \b
  - Tomorrow should be sunny,
  - perfect for traveling!
  - /color \r
  - But I heard rain might come
  - later in the week.
  - /score friendship + 1
  - /jump information_farewell

# Receive gift
receive_gift:
  - /color \6
  - Oh, that's very thoughtful of you!
  - /color \r
  - /? Give money Give item Share story Just gratitude
  - /?Give money /jump gift_money
  - /?Give item /jump gift_item
  - /?Share story /jump gift_story
  - /?Just gratitude /jump gift_gratitude

gift_money:
  - /if money >= 20 /jump money_accepted
  - /color \c
  - I appreciate the thought, but you
  - should keep your money for your journey.
  - /color \r
  - /score friendship + 3
  - /jump gracious_farewell

money_accepted:
  - /score money - 20
  - /score friendship + 5
  - /color \a
  - Thank you so much! This will help
  - buy supplies for the village.
  - /color \r
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.5
  - You have a generous heart!
  - /jump gracious_farewell

gift_item:
  - /has BREAD 1 none /jump item_bread
  - /has APPLE 1 none /jump item_apple
  - /color \c
  - I appreciate the gesture, but you
  - should keep your supplies!
  - /color \r
  - /score friendship + 2
  - /jump gracious_farewell

item_bread:
  - /removeItem BREAD 1 none
  - /score friendship + 4
  - /color \a
  - Fresh bread! How wonderful!
  - My family will love this.
  - /color \r
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.2
  - /jump gracious_farewell

item_apple:
  - /removeItem APPLE 1 none
  - /score friendship + 3
  - /color \a
  - A delicious apple! Thank you!
  - /color \r
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.2
  - /jump gracious_farewell

gift_story:
  - /color \d
  - I'd love to hear about your adventures!
  - /color \r
  - Please, tell me about your travels...
  - /wait 60
  - /color \a
  - What an amazing story!
  - Thank you for sharing.
  - /color \r
  - /score friendship + 4
  - /jump gracious_farewell

gift_gratitude:
  - /color \6
  - Your kind words mean more than
  - any material gift.
  - /color \r
  - /score friendship + 3
  - /singlesound minecraft:block.note.bell 1 1.5
  - /jump gracious_farewell

# Farewell variations
polite_farewell:
  - /color \2
  - Well, it was nice meeting you!
  - /color \r
  - Safe travels, %player%!
  - /singlesound minecraft:entity.villager.yes 1 0.8

helpful_farewell:
  - /color \a
  - I hope that helps!
  - /color \r
  - Safe travels, and come back anytime!
  - /singlesound minecraft:entity.villager.yes 1 1.2

information_farewell:
  - /color \6
  - I hope that information is useful!
  - /color \r
  - Travel safely, %player%!
  - /singlesound minecraft:entity.villager.yes 1 1

gracious_farewell:
  - /color \6\l
  - You're truly a kind soul!
  - /color \r
  - May your journey be blessed.
  - Come visit us again!
  - /singlesound minecraft:entity.villager.yes 1 1.5
  - /wait 40
  - /color \a
  - (Friendship with villager increased!)
  - /color \r
```

## 📖 Code Explanation

### File Structure

1. **File Settings**: Common sound, speed, and color settings
2. **Main Entry**: `greeting` section starts the conversation
3. **Branching Logic**: Multiple paths based on player responses
4. **State Management**: Friendship system using variables
5. **Farewell System**: Different endings based on interaction

### Key Features Used

#### Color Coding System

```yaml
- /color \2\l # Green + Bold for NPC name
- /color \6 # Gold for important information
- /color \a # Dark green for positive responses
- /color \c # Red for warnings
- /color \d # Purple for mysterious content
```

#### Sound Effects

```yaml
- /singlesound minecraft:entity.villager.yes 1 1 # Greeting
- /singlesound minecraft:entity.experience_orb.pickup # Positive outcome
- /singlesound minecraft:block.note.flute 1 1 # Information sharing
```

#### Variable Management

```yaml
- /score friendship + 5 # High friendship gain
- /score friendship + 3 # Medium friendship gain
- /score friendship + 1 # Small friendship gain
```

#### Conditional Logic

```yaml
- /if money >= 20 /jump money_accepted
- /has BREAD 1 none /jump item_bread
```

## 🎯 Customization Ideas

### Expanding the System

1. **Reputation System**

   ```yaml
   # Add reputation levels
   - /if friendship >= 20 /jump best_friend_dialogue
   - /if friendship >= 10 /jump good_friend_dialogue
   - /if friendship >= 5 /jump acquaintance_dialogue
   ```

2. **Time-based Dialogue**

   ```yaml
   # Different greetings by time of day
   morning_greeting:
     - Good morning, %player%!

   evening_greeting:
     - Good evening! Working late?
   ```

3. **Quest Integration**
   ```yaml
   # Add quest-related dialogue
   - /if active_quest_village_help = 1 /jump quest_progress_check
   - /if completed_quest_monster = 1 /jump hero_recognition
   ```

### Enhanced Features

1. **Mood System**

   ```yaml
   # NPC mood affects dialogue
   - /score npc_mood random 3
   - /if npc_mood = 0 /jump grumpy_dialogue
   - /if npc_mood = 1 /jump normal_dialogue
   - /if npc_mood = 2 /jump cheerful_dialogue
   ```

2. **Memory System**

   ```yaml
   # Remember previous conversations
   - /if met_before = 1 /jump returning_visitor
   - /score met_before 1
   ```

3. **Gift Economy**
   ```yaml
   # More complex gift system
   - /score villager_gifts_received + 1
   - /if villager_gifts_received >= 3 /jump special_reward
   ```

## 💡 Best Practices Demonstrated

1. **Clear Structure**: Logical flow from greeting to farewell
2. **Player Agency**: Multiple meaningful choices at each step
3. **Feedback System**: Visual and audio confirmation of actions
4. **Error Handling**: Graceful handling of insufficient resources
5. **Consistent Theming**: Coherent personality for the NPC
6. **Replayability**: Different outcomes encourage multiple interactions

## 🔗 Related Features

- **[Variables & Scoring](../commands/variables.md)** - Friendship system implementation
- **[Conditional Logic](../commands/conditions.md)** - Branching dialogue
- **[Item Management](../commands/items.md)** - Gift giving mechanics
- **[Audio Control](../commands/audio.md)** - Sound effect usage
- **[Basic Settings](../commands/basic-settings.md)** - Color and speed control

This conversation system provides a solid foundation that can be adapted for any NPC in your RPG world!
