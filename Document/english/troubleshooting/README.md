# Troubleshooting

Common problems with RPGText and their solutions.

## 🆘 Emergency Procedures

### When Scripts Don't Work

1. **Check File Location**

   ```
   plugins/RPGText/messages/your_file.yml
   ```

2. **Verify Execution Command**

   ```
   /rpgtext config <player_name> <filename>/<section_name>
   ```

3. **Check Server Logs**
   - Look for error messages in the console

## 📚 Problem Categories

### 🔧 Common Errors

- **[Common Errors](common-errors.md)** - Frequently occurring problems and solutions

### 🐛 Debugging

- **[Debugging Tips](debugging-tips.md)** - Problem identification and resolution procedures

## 🔍 Problem Identification

### Step 1: Symptom Verification

- [ ] Script doesn't work at all
- [ ] Some commands don't work
- [ ] Display issues
- [ ] Error messages appear

### Step 2: Basic Checks

- [ ] File is in the correct location
- [ ] Command syntax is correct
- [ ] Player name and section name are correct
- [ ] Server supports RPGText

### Step 3: Detailed Investigation

- [ ] Check server logs
- [ ] Test with minimal configuration
- [ ] Verify if same issue occurs with other players

## 🚑 Emergency Fixes

### Emergency Workarounds

```yaml
# Minimal test file for operation verification
test.yml:
  test_section:
    - Test successful!
    - RPGText is working normally
```

Execute:

```
/rpgtext config <player_name> test.yml/test_section
```

### Recovery from Backup

1. **Restore Configuration Files**

   ```
   plugins/RPGText/config.yml
   ```

2. **Restore Message Files**

   ```
   plugins/RPGText/messages/
   ```

3. **Restore Player Data**
   ```
   plugins/RPGText/players/
   ```

## 📞 Help Resources

### Information Gathering Order

1. **[Common Errors](common-errors.md)** - Check here first
2. **[Debugging Tips](debugging-tips.md)** - Detailed investigation procedures
3. **[Limitations](../reference/limitations.md)** - Check what cannot be done
4. **[Best Practices](../reference/best-practices.md)** - Recommended methods

### External Resources

- **Official Documentation** - Latest specification verification
- **Community Forums** - Other users' cases
- **GitHub Issues** - Known issues and fix status

## 🛠️ Self-Help Tools

### Diagnostic Script

```yaml
# diagnostic.yml - Diagnostic file
system_check:
  - === RPGText Diagnostics ===
  - Player: %player%
  - Level: %level%
  - Health: %hp%
  - /score test_var 123
  - Test variable: \\test_var\\
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - Audio test completed
  - /color \a
  - Basic functions are normal
  - /color \r
```

### Log Analysis Procedure

1. **Identify Error Type**

   - Syntax error
   - Runtime error
   - Logic error

2. **Locate Error Position**

   - Check line numbers
   - Check command names
   - Check parameters

3. **Determine Fix Strategy**
   - Syntax correction
   - Logic changes
   - Design review

## 📈 Prevention

### Development Precautions

- **Test in Small Units** - Don't create large files all at once
- **Backup Habits** - Regular saving
- **Syntax Checking** - Verify YML format
- **Incremental Implementation** - Expand gradually from basic functions

### Operational Precautions

- **Regular Maintenance** - File organization
- **Log Monitoring** - Early error detection
- **User Feedback** - Reports from players
- **Update Planning** - Schedule for feature additions and fixes

## 📊 Problem Analysis Template

For use in problem reporting and analysis:

### Basic Information

- **Server Environment**: Java/Bedrock Edition/Version
- **RPGText Version**: Plugin version
- **Occurrence Time**: When did the problem start
- **Impact Scope**: Specific player/All players

### Symptoms

- **Expected Behavior**: What should happen
- **Actual Behavior**: What actually happens
- **Error Messages**: Any displayed errors
- **Reproduction Steps**: How to reproduce the problem

### Environment

- **File Path**: Location of problematic file
- **Execution Command**: Command used
- **Related Files**: Other files used simultaneously
- **Recent Changes**: Recent modifications

### Action History

- **Attempted Solutions**: What you've tried so far
- **Results**: Results of each attempt
- **References**: Documents or forums consulted

## 🎯 Efficient Problem Solving

### Problem Resolution Priority

1. **High Urgency**: Server down, all players affected
2. **Medium Urgency**: Partial function down, specific players affected
3. **Low Urgency**: Display issues, minor bugs

### Resolution Procedure Template

1. **Problem Identification** (Within 5 minutes)

   - Symptom verification
   - Impact scope assessment

2. **Emergency Response** (Within 10 minutes)

   - Minimal response for service continuity
   - Recovery from backup

3. **Root Cause Analysis** (Within 30 minutes)

   - Log analysis
   - Code verification
   - Environment verification

4. **Permanent Solution** (As needed)
   - Code fixes
   - Design changes
   - Prevention implementation

## 🔗 Related Documentation

- **[Limitations](../reference/limitations.md)** - Check what cannot be done
- **[Best Practices](../reference/best-practices.md)** - Recommended implementation methods
- **[Commands Reference](../commands/README.md)** - Command specification verification

## 💡 Final Advice

Problem solving is the accumulation of experience. Gradually solve small problems steadily and become able to handle increasingly complex problems.

**Important Points:**

- Don't rush, approach step by step
- Test small changes frequently
- Utilize community knowledge
- Record solved problems for future reference

When in trouble, first take a deep breath and check this document in order!
