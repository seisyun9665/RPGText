package seisyun.rpgtext;

import org.bukkit.entity.Player;

import java.util.List;

class Selections {
    private Player player;
    private List<String> selections;
    private int selectNumber = 0;
    private String nowTitle = "";
    Selections(Player player,List<String> selections){
        this.player = player;
        this.selections = selections;
    }

    void showSelections(){
        if(selections.size() < 1){
            return;
        }
        StringBuilder left = new StringBuilder();
        StringBuilder right = new StringBuilder();
        String selectedText = "§r <" + selections.get(selectNumber) + "§r> ";
        for(int i = 0; i < selections.size(); i++){
            String selection = selections.get(i);
            if(i < selectNumber){
                selection = "§r " + selection + "§r ";
                left.append(selection);
            }else if(i > selectNumber){
                selection = "§r " + selection + "§r ";
                right.append(selection);
            }
        }
        String mixText = right.toString() + "                                                                                                              " + left.toString();
        this.nowTitle = mixText + selectedText + mixText;
        sendSubtitle(nowTitle,0,72000,0);
    }

    //カーソル移動右
    void moveCursorRight(){
        selectNumber++;
        if(selectNumber >= selections.size()){
            selectNumber = 0;
        }
    }

    //左
    void moveCursorLeft(){
        selectNumber--;
        if(selectNumber < 0){
            selectNumber = selections.size() - 1;
        }
    }

    //選択中の選択肢を返す
    String getSelection(){
        return selections.get(selectNumber);
    }

    void finishSelection(){
        sendSubtitle(nowTitle,0,0,10);
    }

    private void sendSubtitle(String subTitle,int fadein, int stay, int fadeout){
        player.sendTitle("",subTitle,fadein,stay,fadeout);
    }
}
