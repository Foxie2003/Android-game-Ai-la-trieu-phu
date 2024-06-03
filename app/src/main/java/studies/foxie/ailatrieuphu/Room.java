package studies.foxie.ailatrieuphu;

import java.util.ArrayList;

public class Room {
    public String roomOwnerId;
    public String player1Id;
    public String player2Id;
    public boolean player1Ready;
    public boolean player2Ready;
    public int currentQuestionIndex;
    public int player1CorrectAnswers;
    public int player2CorrectAnswers;
    public ArrayList<Integer> questionList;
    public String turn;
    public String status;

    public Room() {
        // Constructor mặc định
    }

    public Room(String roomOwnerId, String player1Id, String player2Id, boolean player1Ready, boolean player2Ready, int currentQuestionIndex, int player1CorrectAnswers, int player2CorrectAnswers, ArrayList<Integer> questionList, String turn, String status) {
        this.roomOwnerId = roomOwnerId;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.player1Ready = player1Ready;
        this.player2Ready = player2Ready;
        this.currentQuestionIndex = currentQuestionIndex;
        this.player1CorrectAnswers = player1CorrectAnswers;
        this.player2CorrectAnswers = player2CorrectAnswers;
        this.questionList = questionList;
        this.turn = turn;
        this.status = status;
    }

    public String getRoomOwnerId() {
        return roomOwnerId;
    }

    public void setRoomOwnerId(String roomOwnerId) {
        this.roomOwnerId = roomOwnerId;
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

    public boolean isPlayer1Ready() {
        return player1Ready;
    }

    public void setPlayer1Ready(boolean player1Ready) {
        this.player1Ready = player1Ready;
    }

    public boolean isPlayer2Ready() {
        return player2Ready;
    }

    public void setPlayer2Ready(boolean player2Ready) {
        this.player2Ready = player2Ready;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
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

    public ArrayList<Integer> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(ArrayList<Integer> questionList) {
        this.questionList = questionList;
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