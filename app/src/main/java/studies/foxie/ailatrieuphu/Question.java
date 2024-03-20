package studies.foxie.ailatrieuphu;

public class Question {
    private int id;
    private int questionNumber;
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int correctAnswer;
    private String audioFileName;
    public Question() {
    }

    public Question(int id, int questionNumber, String question, String answer1, String answer2, String answer3, String answer4, int correctAnswer, String audioFileName) {
        this.id = id;
        this.questionNumber = questionNumber;
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correctAnswer = correctAnswer;
        this.audioFileName = audioFileName;
    }

    public Question(Question question) {
        this.id = question.id;
        this.questionNumber = question.questionNumber;
        this.question = question.question;
        this.answer1 = question.answer1;
        this.answer2 = question.answer2;
        this.answer3 = question.answer3;
        this.answer4 = question.answer4;
        this.correctAnswer = question.correctAnswer;
        this.audioFileName = question.audioFileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public String getCorrectAnswerString() {
        switch (correctAnswer) {
            case (1):
                return "A";
            case (2):
                return "B";
            case (3):
                return "C";
            case (4):
                return "D";
            default:
                return "";
        }
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getAudioFileName() {
        return audioFileName;
    }

    public void setAudioFileName(String audioFileName) {
        this.audioFileName = audioFileName;
    }
}
