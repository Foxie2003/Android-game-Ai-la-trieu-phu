package studies.foxie.ailatrieuphu;

public class Room {
    public String player1Id;
    public String player2Id;
    public int player1CorrectAnswers;
    public int player2CorrectAnswers;
    public int currentQuestionIndex;
    public String turn;
    public String status;

    public Room() {
        // Constructor mặc định
    }

    public Room(String player1Id, String player2Id, int player1CorrectAnswers, int player2CorrectAnswers, int currentQuestionIndex, String turn, String status) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.player1CorrectAnswers = player1CorrectAnswers;
        this.player2CorrectAnswers = player2CorrectAnswers;
        this.currentQuestionIndex = currentQuestionIndex;
        this.turn = turn;
        this.status = status;
    }

    public String getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(String player1Id) {
        this.player1Id = player1Id;
    }

    public String getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(String player2Id) {
        this.player2Id = player2Id;
    }

    public int getPlayer1CorrectAnswers() {
        return player1CorrectAnswers;
    }

    public void setPlayer1CorrectAnswers(int player1CorrectAnswers) {
        this.player1CorrectAnswers = player1CorrectAnswers;
    }

    public int getPlayer2CorrectAnswers() {
        return player2CorrectAnswers;
    }

    public void setPlayer2CorrectAnswers(int player2CorrectAnswers) {
        this.player2CorrectAnswers = player2CorrectAnswers;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}