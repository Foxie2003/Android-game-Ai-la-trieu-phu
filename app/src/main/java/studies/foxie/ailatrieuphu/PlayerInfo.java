package studies.foxie.ailatrieuphu;

public class PlayerInfo {
    private String playerName;
    private int avatarId;
    private int frameId;
    private long money;
    private long diamond;
    private int highestQuestionNumber;
    private int answeredQuestion;
    private int correctAnsweredQuestion;

    // Constructor
    public PlayerInfo(String playerName, int avatarId, int frameId, long money, long diamond, int highestQuestionNumber, int answeredQuestion, int correctAnsweredQuestion) {
        this.playerName = playerName;
        this.avatarId = avatarId;
        this.frameId = frameId;
        this.money = money;
        this.diamond = diamond;
        this.highestQuestionNumber = highestQuestionNumber;
        this.answeredQuestion = answeredQuestion;
        this.correctAnsweredQuestion = correctAnsweredQuestion;
    }

    // Getters and setters
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public int getFrameId() {
        return frameId;
    }

    public void setFrameId(int frameId) {
        this.frameId = frameId;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getDiamond() {
        return diamond;
    }

    public void setDiamond(long diamond) {
        this.diamond = diamond;
    }

    public int getHighestQuestionNumber() {
        return highestQuestionNumber;
    }

    public void setHighestQuestionNumber(int highestQuestionNumber) {
        this.highestQuestionNumber = highestQuestionNumber;
    }

    public int getAnsweredQuestion() {
        return answeredQuestion;
    }

    public void setAnsweredQuestion(int answeredQuestion) {
        this.answeredQuestion = answeredQuestion;
    }

    public int getCorrectAnsweredQuestion() {
        return correctAnsweredQuestion;
    }

    public void setCorrectAnsweredQuestion(int correctAnsweredQuestion) {
        this.correctAnsweredQuestion = correctAnsweredQuestion;
    }

    // toString() method
    @Override
    public String toString() {
        return "PlayerInfo{" +
                "playerName='" + playerName + '\'' +
                ", avatarId=" + avatarId +
                ", frameId=" + frameId +
                ", money=" + money +
                ", diamond=" + diamond +
                ", highestQuestionNumber=" + highestQuestionNumber +
                ", answeredQuestion=" + answeredQuestion +
                ", correctAnsweredQuestion=" + correctAnsweredQuestion +
                '}';
    }
}
