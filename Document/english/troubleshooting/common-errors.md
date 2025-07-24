# Common Errors

Frequently encountered errors in RPGText and their solutions.

## 🚨 Execution Errors

### Error: "File not found"

**Symptoms:**

```
/rpgtext config Player story.yml/intro
Error: File not found: story.yml
```

**Causes:**

- File is not placed in the correct location
- File name is incorrect
- File extension is wrong

**Solutions:**

```yaml
# 1. Check file location
plugins/RPGText/messages/story.yml

# 2. Check file name and extension
story.yml (correct)
story.yaml (incorrect)
story.txt (incorrect)

# 3. Check path
/rpgtext config Player story.yml/intro (correct)
/rpgtext config Player story/intro (incorrect - missing .yml)
```

### Error: "Section not found"

**Symptoms:**

```
/rpgtext config Player story.yml/opening
Error: Section 'opening' not found in story.yml
```

**Causes:**

- Section name is incorrect
- Indentation is wrong
- Section is not defined

**Solutions:**

```yaml
# ✅ Correct section definition
story.yml:
  intro:
    - Welcome to the adventure!
    - Your journey begins here.

  opening:
    - The story starts...

# ❌ Common mistakes
story.yml:
intro:  # Missing proper indentation
- Welcome!

story.yml:
  Into:  # Typo in section name
    - Welcome!
```

### Error: "Invalid YAML format"

**Symptoms:**

```
Error: Could not parse YAML file: story.yml
Line 5: Invalid indentation
```

**Causes:**

- Incorrect indentation (mixing spaces and tabs)
- Missing colons
- Incorrect list formatting

**Solutions:**

```yaml
# ✅ Correct YAML format
section_name:
  - First message
  - Second message
  - /command example

# ❌ Common formatting errors
section_name
  - Missing colon

section_name:
- Missing indentation

section_name:
  -Missing space after dash
```

## 🔧 Command Errors

### Error: "Unknown command"

**Symptoms:**

```
Unknown command: /colours
```

**Causes:**

- Typo in command name
- Using wrong command format
- Command doesn't exist

**Solutions:**

```yaml
# ✅ Correct commands
- /color \a    (not /colour)
- /sound block.note.bell 1 1
- /jump section_name

# ❌ Common typos
- /colour      (should be /color)
- /goto        (should be /jump)
- /playsound   (should be /sound or /singlesound)
```

### Error: "Invalid color code"

**Symptoms:**

```
Invalid color code: \z
```

**Causes:**

- Using non-existent color codes
- Wrong syntax

**Solutions:**

```yaml
# ✅ Valid color codes
- /color \a # Green
- /color \c # Red
- /color \6 # Gold
- /color \r # Reset

# ❌ Invalid codes
- /color \z # 'z' is not a valid color code
- /color a # Missing backslash
- /color \\a # Too many backslashes
```

### Error: "Variable not found"

**Symptoms:**

```
Variable 'money' not defined
```

**Causes:**

- Variable not initialized with /score
- Typo in variable name
- Wrong variable syntax

**Solutions:**

```yaml
# ✅ Correct variable usage
initialization:
  - /score money 100
  - Starting money: \\money\\

# ❌ Common mistakes
display_only:
  - Money: \\money\\ # Error: variable not initialized

wrong_syntax:
  - Money: %money% # Wrong: this is for built-in variables
  - Money: \money\ # Wrong: missing double backslashes
```

## 🎮 Gameplay Errors

### Error: "Choice not working"

**Symptoms:**

- Choices appear but clicking doesn't work
- Wrong choices are executed

**Causes:**

- Incorrect choice syntax
- Missing choice handlers
- Typos in choice names

**Solutions:**

```yaml
# ✅ Correct choice implementation
main_menu:
  - What would you like to do?
  - /? Start Continue Quit
  - /?Start /jump start_game
  - /?Continue /jump continue_game
  - /?Quit Thanks for playing!

# ❌ Common mistakes
broken_choices:
  - /? Start Continue Quit
  - /?Start /jump start_game
  - /?Countinue /jump continue_game # Typo in 'Continue'
  # Missing Quit handler
```

### Error: "Jump target not found"

**Symptoms:**

```
Error: Section 'next_chapter' not found
```

**Causes:**

- Target section doesn't exist
- Typo in section name
- Wrong file path

**Solutions:**

```yaml
# ✅ Correct jump usage
chapter1:
  - End of chapter 1
  - /jump chapter2

chapter2:  # Target exists
  - Chapter 2 begins...

# ❌ Common mistakes
chapter1:
  - /jump chapter2  # Target doesn't exist

# ✅ Cross-file jumping
main_story:
  - /jump chapter2.yml/opening

# File: chapter2.yml
opening:
  - Chapter 2 content...
```

## 🔊 Audio Errors

### Error: "Sound not playing"

**Symptoms:**

- No sound when /sound or /singlesound is used
- Error messages about invalid sounds

**Causes:**

- Invalid sound name
- Client audio settings
- Server version compatibility

**Solutions:**

```yaml
# ✅ Valid sound names
- /sound block.note.bell 1 1
- /singlesound minecraft:entity.player.levelup

# ❌ Invalid sound names
- /sound note.bell          # Missing 'block.' prefix
- /sound invalid_sound_name  # Non-existent sound

# 🔧 Testing audio
audio_test:
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - Did you hear the sound?
  - /? Yes No
  - /?Yes Audio is working!
  - /?No Check your audio settings
```

## 💾 File System Errors

### Error: "Permission denied"

**Symptoms:**

```
Error: Could not write to file
Permission denied: /plugins/RPGText/messages/
```

**Causes:**

- Incorrect file permissions
- Server doesn't have write access
- File is locked by another process

**Solutions:**

```bash
# Fix file permissions (Linux/Mac)
chmod 755 plugins/RPGText/
chmod 644 plugins/RPGText/messages/*.yml

# Check file ownership
chown minecraft:minecraft plugins/RPGText/ -R
```

### Error: "Disk space full"

**Symptoms:**

```
Error: Could not save file - disk space full
```

**Solutions:**

- Clean up old log files
- Remove unused script files
- Increase server storage
- Check for large backup files

## 🔄 Variable Errors

### Error: "Division by zero"

**Symptoms:**

```
Error: Division by zero in score calculation
```

**Causes:**

- Attempting to divide by a variable with value 0
- Not checking variable values before calculations

**Solutions:**

```yaml
# ✅ Safe division
safe_calculation:
  - /if divisor > 0 /score result value
  - /if divisor > 0 /score result / divisor
  - /if divisor = 0 Cannot divide by zero!

# ❌ Unsafe division
unsafe_calculation:
  - /score result value
  - /score result / divisor # Error if divisor is 0
```

### Error: "Variable overflow"

**Symptoms:**

```
Warning: Variable value exceeds maximum limit
```

**Causes:**

- Variable values exceeding integer limits
- Rapid multiplication without bounds checking

**Solutions:**

```yaml
# ✅ Bounds checking
safe_multiplication:
  - /if money < 1000000 /score money * 2
  - /if money >= 1000000 Money at maximum!

# 🔧 Reset if needed
reset_if_overflow:
  - /if money > 2000000000 /score money 1000000
  - /if money > 2000000000 Money reset due to overflow
```

## 🌐 Network Errors

### Error: "Player not found"

**Symptoms:**

```
Error: Player 'PlayerName' not found or not online
```

**Causes:**

- Player is offline
- Player name is misspelled
- Player is in different world/server

**Solutions:**

```yaml
# Use %player% for current player
current_player:
  - Hello, %player%!
# Check if using correct player name in commands
# /rpgtext config Steve story.yml/intro  (Steve must be online)
```

### Error: "Server overload"

**Symptoms:**

- Scripts run very slowly
- Server lag during RPGText execution
- Timeout errors

**Solutions:**

- Reduce script complexity
- Add delays with /wait commands
- Optimize variable usage
- Split large scripts into smaller parts

```yaml
# ✅ Optimized script
optimized:
  - /wait 10 # Small delays prevent overload
  - Message 1
  - /wait 5
  - Message 2

# ❌ Overloading script
heavy:
  - /score var1 random 1000
  - /score var2 random 1000
  - /score var3 var1
  - /score var3 * var2 # Complex calculations without delays
```

## 📱 Client-Side Errors

### Error: "Display corruption"

**Symptoms:**

- Text appears garbled
- Colors don't display correctly
- Special characters show as boxes

**Causes:**

- Client encoding issues
- Unsupported characters
- Resource pack conflicts

**Solutions:**

```yaml
# ✅ Safe character usage
safe_text:
  - Welcome to the adventure!
  - Use standard ASCII characters

# ❌ Problematic characters
problematic:
  - 特殊文字 # May not display on all clients
  - ★▲● # Special symbols may not work
```

## 🔍 Debugging Tips

### General Debugging Strategy

1. **Start Simple**

   ```yaml
   test:
     - Simple test message
   ```

2. **Add Complexity Gradually**

   ```yaml
   test:
     - Simple test message
     - /color \a
     - Green text test
     - /color \r
   ```

3. **Test Each Feature Separately**

   ```yaml
   color_test:
     - /color \c
     - Red text
     - /color \r

   sound_test:
     - /singlesound minecraft:entity.experience_orb.pickup
     - Sound test

   variable_test:
     - /score test_var 123
     - Variable: \\test_var\\
   ```

### Error Log Analysis

```
# Look for these patterns in server logs:
[RPGText] Error in file: script.yml
[RPGText] Section not found: intro
[RPGText] Invalid command: /colours
[RPGText] YAML parsing error at line 5
```

## 🛠️ Prevention Best Practices

### File Management

- Keep backups of working scripts
- Use consistent naming conventions
- Test changes in development environment first

### Code Quality

- Use proper indentation (2 spaces)
- Add comments for complex logic
- Keep sections focused and small

### Testing

- Test with multiple players
- Test all choice paths
- Verify all variable calculations

## 🔗 Related Documentation

- **[Debugging Tips](debugging-tips.md)** - Advanced debugging techniques
- **[Limitations](../reference/limitations.md)** - Known system limitations
- **[Best Practices](../reference/best-practices.md)** - Recommended practices
