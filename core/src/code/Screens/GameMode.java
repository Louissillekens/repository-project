package code.Screens;

public class GameMode {

    public String gameName;

    // Class used to define each particularity of all the different games mode we can have
    public GameMode(String gameName) {

        this.gameName = gameName;
        setRules();
    }

    public void setRules() {

        if (gameName.equals("Single_Player")) {
            Single_Player_Rule();
        }

        if (gameName.equals("Multi_Player")) {
            Multi_Player_Rule();
        }

        if (gameName.equals("Bot")) {
            Bot_Rule();
        }

        if (gameName.equals("Bot_VS_Player")) {
            Bot_VS_Player_Rule();
        }

        if (gameName.equals("Bot_VS_Bot")) {
            Bot_VS_Bot();
        }
    }

    public void Single_Player_Rule() {

        // define mode
    }

    public void Multi_Player_Rule() {

        // define mode
    }

    public void Bot_Rule() {

        // define mode
    }

    public void Bot_VS_Player_Rule() {

        // define mode
    }

    public void Bot_VS_Bot() {

        // define mode
    }
}
