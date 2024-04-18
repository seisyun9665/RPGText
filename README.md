# RPGText

**RPGText is a plugin for Minecraft servers.** It introduces a system that **sequentially displays text, similar to what is seen in typical RPG games**, within the game itself. Additionally, by **generating sound simultaneously with the text display**, it creates an effect as if the game characters themselves are speaking.

Users can customize the message, message color, message display speed, and the sound generated by the message.

Furthermore, by using a dedicated syntax, it is possible to easily implement more advanced features, such as **branching stories and executing Minecraft commands under specific conditions**.

Specifically, this includes **displaying selectable options for players, setting and calculating variables** (even capable of handling calculations like money), and **branching based on those variables**.

With full use of these features, you could create highly creative systems, such as "**a path only accessible to players over level 100**," "**a system where talking to a certain villager three times earns you a key**," or "**a slot game that can only be played once a day**."


### Features:
- Seamlessly craft stories in the style of RPGs.
- Create and display messages in the action bar.
- Assign messages to entities and display them upon interaction.
- Provide choices for players to select from.
- Define and calculate variables specific to each player.
- Utilize variables to influence story paths.
- Develop and manage stories with specialized commands.

### Tutorial

### Commands:

- /rpgtext help - Lists all the commands of this plugin and start the tutorial.
- /rpgtext reload - Reload configs.
- /rpgtext text <player> <text> - Send message in the style an RPG game.
- /rpgtext config <player> <path> - Send message from the yml file.
- /rpgtext character <name> <path> - Set the file path of the message sent to the player when clicking on that entity.
- /rpgtext freeze clear - Allows all frozen players to move.
- /rpgtext freeze toggle <player> -Switch the player's frozen state.
 

### Configuration:

- `default:`
  - `message:`
    - `sound: "block.note.bass"`
    - `volume: 1`
    - `pitch: 2`
    - `speed: 1`
  - `selection:`
    - `move:`
      - `sound: "block.note.hat"`
      - `volume: 1`
      - `pitch: 1`
    - `select:`
      - `sound: "entity.arrow.hit_player"`
      - `volume: 1`
      - `pitch: 1`
  - `freeze:`
    - `horizontal: true`
    - `vertical: false`
    - `jump: true`
    - `invincible: true`
    - `leftclick-cancel: true`
    - `rightclick-cancel: true`
  - `click-type: left`

### Descriptions
- **message** - Default sound settings for sending messages.
- **selection** - Settings default sound when selecting an option.
- **freeze** - Settings for frozen player movement restriction, invincibility, and click invalidation.
- **click-type** - Setting for conversation click type.
 

### Generated file/folder:

- characters.yml - Save the item set with the command "/rpgtext character".
- scoreboard.yml - Save defined variables.
- messages - Save the yml file that wrote the message.
- Tutorial.yml - Save the tutorial to be displayed when the "/rpgtext help" is executed.
 

## 日本語版

**RPGTextは、Minecraftサーバー用プラグインです。** 一般的なRPGゲームでみられるような、**文章を順次表示していくシステムをゲーム内に導入します。** さらに、**文章の表示と同時にサウンドを発生させることで**、まるでゲーム内のキャラクターが発話しているかのような演出を行うことができます。

ユーザーは、メッセージ、メッセージの色、メッセージの表示速度、メッセージで発生させる音声などをカスタマイズできます。

さらに、専用の文法を用いることで、**ストーリーの分岐や、特定の条件下でのみMinecraft内のコマンドを実行するなど、より高度な処理を簡単に実現することができます。**

具体的には、**プレイヤーが選択可能な選択肢の表示、変数の設定と計算**（お金の勘定などを行うことも可能です！）、**それらの変数に基づく分岐**が可能です。

これらの機能をフルに用いることができれば「**１００レベル以上のプレイヤーだけが通れる道**」「**ある村人に３回話しかけると鍵がもらえるシステム**」「**１日１回だけ回せるスロットゲーム**」など、創造性にあふれたシステムを思い通りに作り込むことができるようになるでしょう！

### 特徴:
- RPGのスタイルで物語をシームレスに作成。
- アクションバーにメッセージを作成して表示。
- エンティティにメッセージを割り当て、インタラクション時に表示。
- プレイヤーが選択する選択肢を提供。
- 各プレイヤーに特有の変数を定義して計算。
- 変数を使用してストーリーのパスに影響を与える。
- 特化したコマンドでストーリーを開発および管理。

### チュートリアル:

### コマンド:

- /rpgtext help - このプラグインのすべてのコマンドをリスト表示し、チュートリアルを開始。
- /rpgtext reload - 設定を再読込。
- /rpgtext text <player> <text> - RPGゲームのスタイルでメッセージを送信。
- /rpgtext config <player> <path> - ymlファイルからメッセージを送信。
- /rpgtext character <name> <path> - エンティティをクリックした際にプレイヤーに送信されるメッセージのファイルパスを設定。
- /rpgtext freeze clear - 凍結したすべてのプレイヤーが動けるようにする。
- /rpgtext freeze toggle <player> - プレイヤーの凍結状態を切り替える。

### 設定:

- `default:`
  - `message:`
    - `sound: "block.note.bass"`
    - `volume: 1`
    - `pitch: 2`
    - `speed: 1`
  - `selection:`
    - `move:`
      - `sound: "block.note.hat"`
      - `volume: 1`
      - `pitch: 1`
    - `select:`
      - `sound: "entity.arrow.hit_player"`
      - `volume: 1`
      - `pitch: 1`
  - `freeze:`
    - `horizontal: true`
    - `vertical: false`
    - `jump: true`
    - `invincible: true`
    - `leftclick-cancel: true`
    - `rightclick-cancel: true`
  - `click-type: left`

### 説明
- **message** - メッセージ送信時のデフォルト音設定。
- **selection** - オプション選択時のデフォルト音設定。
- **freeze** - 凍結プレイヤーの動き制限、無敵、クリック無効の設定。
- **click-type** - 会話クリックタイプの設定。

### 生成されるファイル/フォルダー:

- characters.yml - コマンド "/rpgtext character" で設定されたアイテムを保存。
- scoreboard.yml - 定義された変数を保存。
- messages - メッセージを記述したymlファイルを保存。
- Tutorial.yml - コマンド "/rpgtext help" 実行時に表示されるチュートリアルを保存。