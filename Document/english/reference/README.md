# Reference Materials

Technical details and specifications for RPGText compiled into reference documentation.

## 📚 Reference List

### 🎨 Expression & Design

- **[Special Variables](special-variables.md)** - Player information variable details
- **[Color Codes](color-codes.md)** - Complete color and decoration code list

### ⚠️ Limitations & Notes

- **[Limitations](limitations.md)** - What cannot be done and important notes
- **[Best Practices](best-practices.md)** - Recommended implementation methods

## 🔍 How to Use

Use these references in the following situations:

### During Development

- **Special Variables** - When you want to display player information
- **Color Codes** - When you want to decorate or color-code text
- **Limitations** - When checking errors that occur
- **Best Practices** - When you want to know better implementation methods

### Troubleshooting

1. First check **[Limitations](limitations.md)**
2. Consider improvement ideas in **[Best Practices](best-practices.md)**
3. If still unresolved, go to **[Troubleshooting](../troubleshooting/README.md)**

## 💡 Usage Examples

### Variable Display System

```yaml
player_status:
  - === Player Information ===
  - Name: %player%
  - Level: %level%
  - Health: %hp%/%max_hp%
  - Food: %food%
  - Game Mode: %gamemode%
  - Custom Variable: \\money\\ Gold
```

### Color Coding System

```yaml
colored_messages:
  - /color \c\l
  - 【WARNING】Dangerous area
  - /color \a
  - 【SUCCESS】Quest completed!
  - /color \6\l
  - 【SPECIAL】Rare item discovered!
  - /color \r
```

## 🔗 Related Documentation

- **[Getting Started](../getting-started/README.md)** - Basic usage
- **[Commands Reference](../commands/README.md)** - Detailed command information
- **[Practical Examples](../examples/README.md)** - Implementation samples
- **[Troubleshooting](../troubleshooting/README.md)** - Problem solving

## 📖 How to Read

### For Beginners

1. First check [Special Variables](special-variables.md) and [Color Codes](color-codes.md)
2. Master basic expression methods
3. Check limitations as needed

### For Advanced Users

1. Check [Best Practices](best-practices.md) for efficient implementation methods
2. Understand workarounds in [Limitations](limitations.md)
3. Apply to better system design

## ⚡ Quick Reference

### Commonly Used Special Variables

| Variable       | Content         | Example            |
| -------------- | --------------- | ------------------ |
| `%player%`     | Player name     | `Hello %player%`   |
| `%level%`      | Level           | `Level %level%`    |
| `%hp%`         | Health          | `HP:%hp%`          |
| `\\variable\\` | Custom variable | `Money:\\money\\G` |

### Commonly Used Color Codes

| Code   | Color/Effect | Usage                 |
| ------ | ------------ | --------------------- |
| `\r`   | Reset        | Color reset           |
| `\6\l` | Gold・Bold   | Important information |
| `\a`   | Dark green   | Success messages      |
| `\c`   | Dark red     | Warnings・Errors      |
| `\b`   | Aqua         | System messages       |

Use this reference to create more expressive and functional RPGText scripts!
