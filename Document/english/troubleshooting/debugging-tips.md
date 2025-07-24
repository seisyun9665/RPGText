# Debugging Tips

Advanced techniques for identifying and resolving RPGText issues.

## 🕵️ Systematic Debugging Approach

### 1. Problem Isolation

**Binary Search Method**

```yaml
# Start with minimal script
minimal_test:
  - Test message

# Gradually add features until error occurs
step1:
  - Test message
  - /color \a
  - Green text

step2:
  - Test message
  - /color \a
  - Green text
  - /score test_var 100
```

**Component Testing**

```yaml
# Test each component separately
colors_only:
  - /color \c
  - Red text
  - /color \r

sounds_only:
  - /singlesound minecraft:entity.experience_orb.pickup
  - Sound test

variables_only:
  - /score money 100
  - Money: \\money\\
```

### 2. Logging and Monitoring

**Debug Output Script**

```yaml
debug_section:
  - === DEBUG START ===
  - Player: %player%
  - Level: %level%
  - Health: %hp%
  - Location: %x%, %y%, %z%
  - /score debug_counter 1
  - Debug counter: \\debug_counter\\
  - === DEBUG END ===
```

**State Tracking**

```yaml
track_state:
  - Current state: \\game_state\\
  - Last action: \\last_action\\
  - Error count: \\error_count\\
  - /add debug_steps
  - Debug step: \\debug_steps\\
```

## 🔍 Error Detection Techniques

### Server Log Analysis

**Key Log Patterns**

```
# Error indicators to look for:
[ERROR] [RPGText] - Critical errors
[WARN] [RPGText] - Warnings that may cause issues
[RPGText] Error in file - File-specific problems
[RPGText] Command failed - Command execution failures
```

**Log Reading Strategy**

1. **Start from the timestamp** when the problem occurred
2. **Look for ERROR and WARN levels** first
3. **Check context** around error messages
4. **Follow the execution flow** through multiple log entries

### Real-time Monitoring

**Performance Tracking**

```yaml
performance_test:
  - Performance test starting...
  - /command time query gametime
  - Start time recorded
  - [Your script content here]
  - /command time query gametime
  - End time recorded
  - Check time difference in logs
```

**Memory Usage Monitoring**

```yaml
memory_test:
  - /score large_var 1000000
  - /score result large_var
  - /score result * large_var
  - Memory test: \\result\\
```

## 🐛 Common Debug Scenarios

### Script Not Running

**Debug Checklist**

```yaml
# 1. Basic connectivity test
connection_test:
  - Connection test successful
  - Time: %timestamp%

# 2. File access test
file_test:
  - File access working
  - Current file loaded

# 3. Permission test
permission_test:
  - /score test_permission 1
  - Permission test: \\test_permission\\
```

### Partial Functionality

**Step-by-Step Validation**

```yaml
validation_suite:
  - === Validation Suite ===
  - /color \a
  - ✓ Colors working
  - /color \r
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - ✓ Sounds working
  - /score validation_var 42
  - ✓ Variables working: \\validation_var\\
  - /jump validation_complete

validation_complete:
  - ✓ Navigation working
  - All systems operational!
```

### Variable Issues

**Variable Debugging**

```yaml
var_debug:
  - === Variable Debug ===
  - Initial money: \\money\\
  - /score money + 10
  - After +10: \\money\\
  - /score money * 2
  - After *2: \\money\\
  - /score money - 5
  - Final: \\money\\
  - Variable operations completed
```

**Boundary Testing**

```yaml
boundary_test:
  - Testing variable boundaries...
  - /score test_var 0
  - Zero value: \\test_var\\
  - /score test_var -1
  - Negative value: \\test_var\\
  - /score test_var 999999
  - Large value: \\test_var\\
```

## 🔧 Advanced Debugging Tools

### Custom Debug Framework

**Debug Mode Toggle**

```yaml
debug_init:
  - /score debug_mode 1
  - Debug mode enabled
  - /jump main_script

debug_log:
  - /if debug_mode = 1 [DEBUG] %player% at %x%,%y%,%z%
  - /if debug_mode = 1 [DEBUG] Current section: \\current_section\\
  - /if debug_mode = 1 [DEBUG] Variables: money=\\money\\ level=\\level\\
```

**Conditional Debugging**

```yaml
conditional_debug:
  - /if error_count > 0 /jump error_debug
  - /if warning_count > 0 /jump warning_debug
  - Normal execution continues...

error_debug:
  - /color \c
  - ERROR DETECTED
  - Error count: \\error_count\\
  - Last error: \\last_error\\
  - /color \r
```

### Performance Profiling

**Execution Time Measurement**

```yaml
profiling_start:
  - /score start_time time
  - /score start_time % 1000
  - Starting measurement...

profiling_end:
  - /score end_time time
  - /score end_time % 1000
  - /score execution_time end_time
  - /score execution_time - start_time
  - Execution time: \\execution_time\\ ticks
```

**Resource Usage Tracking**

```yaml
resource_tracking:
  - Active variables: \\var_count\\
  - Script depth: \\call_depth\\
  - Memory usage level: \\memory_level\\
  - /if memory_level > 80 /color \e
  - /if memory_level > 80 High memory usage warning
  - /color \r
```

## 📊 Data Collection Strategies

### User Interaction Logging

**Choice Tracking**

```yaml
choice_logger:
  - /score choice_timestamp time
  - /score total_choices + 1
  - What's your choice?
  - /? Option_A Option_B Option_C
  - /?Option_A /score choice_a + 1
  - /?Option_A /jump handle_choice_a
  - /?Option_B /score choice_b + 1
  - /?Option_B /jump handle_choice_b
```

**Path Analysis**

```yaml
path_tracker:
  - /score path_history path_history
  - /score path_history * 10
  - /score path_history + current_location
  - Path: \\path_history\\
```

### Environment Data

**System State Capture**

```yaml
system_snapshot:
  - === System Snapshot ===
  - Server time: %timestamp%
  - Player count: %online_players%
  - Server load: %server_load%
  - Memory free: %memory_free%
  - RPGText version: %rpgtext_version%
```

## 🎯 Targeted Debugging

### Audio Issues

**Sound Testing Suite**

```yaml
audio_debug:
  - === Audio Debug Suite ===
  - Testing basic sound...
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - /wait 20
  - Testing volume levels...
  - /singlesound minecraft:block.note.bell 0.5 1
  - /wait 20
  - /singlesound minecraft:block.note.bell 1.0 1
  - /wait 20
  - Testing pitch variations...
  - /singlesound minecraft:block.note.bass 1 0.5
  - /wait 20
  - /singlesound minecraft:block.note.bass 1 2.0
  - Audio debug complete
```

### Navigation Problems

**Jump Chain Testing**

```yaml
nav_test_start:
  - Navigation test starting...
  - /jump nav_test_1

nav_test_1:
  - ✓ Reached section 1
  - /jump nav_test_2

nav_test_2:
  - ✓ Reached section 2
  - /jump nav_test_complete

nav_test_complete:
  - ✓ Navigation system working
  - All jumps successful
```

### Variable Calculation Issues

**Math Operation Testing**

```yaml
math_debug:
  - === Math Operations Debug ===
  - /score a 10
  - /score b 3
  - Initial: a=\\a\\, b=\\b\\
  - /score result a
  - /score result + b
  - Addition: \\result\\ (should be 13)
  - /score result a
  - /score result - b
  - Subtraction: \\result\\ (should be 7)
  - /score result a
  - /score result * b
  - Multiplication: \\result\\ (should be 30)
  - /score result a
  - /score result / b
  - Division: \\result\\ (should be 3)
```

## 📝 Documentation and Reporting

### Bug Report Template

```yaml
bug_report_generator:
  - === BUG REPORT ===
  - Timestamp: %timestamp%
  - Player: %player%
  - Location: %x%, %y%, %z%
  - Script: \\current_script\\
  - Section: \\current_section\\
  - Variables: money=\\money\\ level=\\level\\
  - Last action: \\last_action\\
  - Error details: \\error_message\\
  - Reproduction steps: \\repro_steps\\
  - === END REPORT ===
```

### Progress Tracking

**Milestone Logging**

```yaml
milestone_tracker:
  - /add milestones_completed
  - Milestone \\milestones_completed\\ reached
  - Player progress: \\progress_percent\\%
  - /if milestones_completed = 10 /jump achievement_unlock
```

## 🚀 Optimization Debugging

### Performance Bottleneck Detection

**Timing Critical Sections**

```yaml
performance_critical:
  - /score perf_start time
  - [Performance critical code here]
  - Heavy calculation section
  - Multiple variable operations
  - [End of critical code]
  - /score perf_end time
  - /score perf_duration perf_end
  - /score perf_duration - perf_start
  - /if perf_duration > 100 /color \e
  - /if perf_duration > 100 Performance warning: \\perf_duration\\ ticks
  - /color \r
```

### Memory Usage Optimization

**Variable Cleanup**

```yaml
cleanup_debug:
  - Cleaning temporary variables...
  - /score temp_var1 0
  - /score temp_var2 0
  - /score temp_var3 0
  - Cleanup completed
  - Memory freed for reuse
```

## 🔗 Related Tools and Resources

### External Debugging Tools

- **YAML Validators** - Online tools to check YAML syntax
- **Text Editors** - IDEs with YAML syntax highlighting
- **Log Analyzers** - Tools for parsing server logs

### Community Resources

- **Debug Script Libraries** - Community-shared debugging scripts
- **Common Issue Databases** - Known problems and solutions
- **Best Practice Guides** - Proven debugging methodologies

## 💡 Pro Tips

### Efficient Debugging Workflow

1. **Reproduce Consistently** - Find reliable steps to recreate the issue
2. **Use Binary Search** - Divide and conquer to isolate problems
3. **Document Everything** - Keep records of what was tried
4. **Test Incrementally** - Make small changes and test frequently
5. **Use Version Control** - Keep backups of working versions

### Common Debugging Mistakes

- Making multiple changes at once
- Not testing after each change
- Ignoring warning messages
- Not checking server logs
- Assuming the problem is complex when it's simple

Remember: Most bugs are simple issues hiding behind complex symptoms!
