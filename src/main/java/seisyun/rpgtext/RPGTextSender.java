package seisyun.rpgtext;
import org.bukkit.entity.Player;

class RPGTextSender {
    private Player player;
    private String text;
    private String sound = RPGText.DEFAULT_MESSAGE_SOUND;
    private int speedManager = 1;
    private int speed = RPGText.DEFAULT_MESSAGE_SPEED;
    private float pitch = RPGText.DEFAULT_MESSAGE_PITCH;
    private float volume = RPGText.DEFAULT_MESSAGE_VOLUME;
    private int length = 0;

    RPGTextSender(Player p,String sendText){
        player = p;
        text = sendText;
        skipColorCode();
    }

    void setSpeed(int speed) {
        if(speed < 1){
            return;
        }
        this.speed = speed;
    }

    Player getPlayer() {
        return player;
    }

    int getLength() {
        return length;
    }

    String getText() {
        return text;
    }

    private void skipColorCode(){
        while(true) {
            if(length > text.length() - 1){
                break;
            }
            if (text.charAt(length) == '§') {
                length += 2;
            }else {
                break;
            }
        }
    }

    void nextLength(){
        length++;
        skipColorCode();
    }

    boolean isFinished(){
        return text.length() - 1 < length;
    }

    void replace(RPGTextSender rpgTextSender){
        player = rpgTextSender.getPlayer();
        text = rpgTextSender.getText();
        length = 0;
    }

    void setFinish(){
        length = text.length() - 1;
    }

    void playSound(){
        if(text.charAt(length) == ' '){
            return;
        }
        if(sound != null) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    void setSound(String sound,float pitch,float volume) {
        this.sound = sound;
        this.pitch = pitch;
        this.volume = volume;
    }

    void setNoSound(){
        this.sound = null;
    }

    //速度調整用
    boolean speedManagerJudge(){
        speedManager++;
        if(speedManager > speed){
            speedManager = 1;
            return true;
        }
        return false;
    }
}
