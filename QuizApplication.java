import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

class QuizQuestion 
{
    String text;
    ArrayList<String> options;
    char correctAnswer;

    public QuizQuestion(String text, ArrayList<String> options, char correctAnswer) 
    {
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}

public class QuizApplication extends JFrame 
{
    private ArrayList<QuizQuestion> questions;
    private int score;
    private int currentQuestionIndex;
    private Timer questionTimer;

    private JLabel questionLabel;
    private JRadioButton optionA, optionB, optionC, optionD;
    private JButton submitButton;
    private JLabel timerLabel;

    public QuizApplication(ArrayList<QuizQuestion> questions)
     {
        this.questions = questions;
        this.score = 0;
        this.currentQuestionIndex = 0;

        setTitle("Quiz App");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        setLayout(new BorderLayout());
        addComponents();
        displayQuestion();

        setVisible(true);
    }

    private void initializeComponents() 
      {
        questionLabel = new JLabel();
        optionA = new JRadioButton("A");
        optionB = new JRadioButton("B");
        optionC = new JRadioButton("C");
        optionD = new JRadioButton("D");
        submitButton = new JButton("Submit");
        timerLabel = new JLabel();

        ButtonGroup optionsGroup = new ButtonGroup();
        optionsGroup.add(optionA);
        optionsGroup.add(optionB);
        optionsGroup.add(optionC);
        optionsGroup.add(optionD);

        submitButton.addActionListener(new ActionListener() 
     {
            @Override
            public void actionPerformed(ActionEvent e) 
           {
                submitAnswer();
            }
        });

        questionTimer = new Timer();
    }

    private void addComponents() 
   {
        JPanel centerPanel = new JPanel(new GridLayout(6, 1));
        centerPanel.add(questionLabel);
        centerPanel.add(optionA);
        centerPanel.add(optionB);
        centerPanel.add(optionC);
        centerPanel.add(optionD);
        centerPanel.add(timerLabel);

        JPanel southPanel = new JPanel();
        southPanel.add(submitButton);

        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void displayQuestion() 
      {
        clearSelection();
        updateTimerLabel(15);
        submitButton.setEnabled(true);

        QuizQuestion currentQuestion = questions.get(currentQuestionIndex);
        questionLabel.setText(currentQuestion.text);
        optionA.setText(currentQuestion.options.get(0));
        optionB.setText(currentQuestion.options.get(1));
        optionC.setText(currentQuestion.options.get(2));
        optionD.setText(currentQuestion.options.get(3));

        startTimer(15); 
    }

    private void submitAnswer() {
        questionTimer.cancel();
        submitButton.setEnabled(false); 

        QuizQuestion currentQuestion = questions.get(currentQuestionIndex);
        char correctAnswer = currentQuestion.correctAnswer;

        if (optionA.isSelected() && 'A' == correctAnswer ||
                optionB.isSelected() && 'B' == correctAnswer ||
                optionC.isSelected() && 'C' == correctAnswer ||
                optionD.isSelected() && 'D' == correctAnswer) {
            JOptionPane.showMessageDialog(this, "Correct!");
            score++;
        } 
        else {
            JOptionPane.showMessageDialog(this, "Incorrect! The correct answer is " + correctAnswer);
        }

        currentQuestionIndex++;

        if (currentQuestionIndex < questions.size()) {
            displayQuestion();
        } 
         else {
            displayResult();
        }
    }

    private void displayResult() 
       {
        JOptionPane.showMessageDialog(this, "Quiz Completed!\nYour score: " + score + "/" + questions.size());
        System.exit(0);
    }

    private void clearSelection() 
    {
        optionA.setSelected(false);
        optionB.setSelected(false);
        optionC.setSelected(false);
        optionD.setSelected(false);
    }

    private void startTimer(int timeLimit) 
     {
        questionTimer = new Timer();
        questionTimer.scheduleAtFixedRate(new TimerTask() {
            int remainingTime = timeLimit;

            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    updateTimerLabel(remainingTime);
                    remainingTime--;

                    if (remainingTime < 0) {
                        questionTimer.cancel(); 
                        submitAnswer();
                        JOptionPane.showMessageDialog(QuizApplication.this, "Time's up! The correct answer is " +
                                questions.get(currentQuestionIndex).correctAnswer);
                        currentQuestionIndex++;

                        if (currentQuestionIndex < questions.size()) {
                            displayQuestion();
                        } else {
                            displayResult();
                        }
                    }
                });
            }
        }, 0, TimeUnit.SECONDS.toMillis(1));
    }

    private void updateTimerLabel(int remainingTime) 
     {
        timerLabel.setText("Time remaining: " + remainingTime + " seconds");
    }

    public static void main(String[] args) 
    {
        ArrayList<QuizQuestion> quizQuestions = new ArrayList<>();
        quizQuestions.add(new QuizQuestion("What is the capital of Japan?", new ArrayList<>(List.of("A) Beijing", "B) Tokyo", "C) Seoul", "D) Bangkok")), 'B'));
    
        quizQuestions.add(new QuizQuestion("Who wrote the play 'Hamlet'?", new ArrayList<>(List.of("A) William Shakespeare", "B) Charles Dickens", "C) Jane Austen", "D) Leo Tolstoy")), 'A'));
    
        quizQuestions.add(new QuizQuestion("Which planet is known as the Red Planet?", new ArrayList<>(List.of("A) Venus", "B) Mars", "C) Jupiter", "D) Saturn")), 'B'));
    
        quizQuestions.add(new QuizQuestion("Who painted the 'Mona Lisa'?", new ArrayList<>(List.of("A) Leonardo da Vinci", "B) Vincent van Gogh", "C) Pablo Picasso", "D) Michelangelo")), 'A'));
    
        quizQuestions.add(new QuizQuestion("What is the chemical symbol for water?", new ArrayList<>(List.of("A) Wo", "B) Wa", "C) H2O", "D) O2")), 'C'));
    
        SwingUtilities.invokeLater(() -> new QuizApplication(quizQuestions));
    }
    
}