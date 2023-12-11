package com.example.tictactoe;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tictactoe.api.DataAPI;
import com.example.tictactoe.api.RetrofitData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    // Enum to represent the players' turns
    public enum Turn {
        NOUGHT,
        CROSS
    }

    // Initial turn and current turn variables
    private Turn firstTurn = Turn.CROSS;
    private Turn currentTurn = Turn.CROSS;

    // Scores for each player
    private int crossesScore = 0;
    private int noughtsScore = 0;

    // Buttons representing the Tic-Tac-Toe board
    private Button a1, a2, a3, b1, b2, b3, c1, c2, c3;

    // TextView to display the current turn
    private TextView turnTV;

    private int[] board = new int[9];

    // List to store buttons for easy iteration
    private List<Button> boardList = new ArrayList<>();

    private int nextIndexBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Tic-Tac-Toe board
        initBoard();
    }

    // Initialize the Tic-Tac-Toe board buttons
    private void initBoard() {
        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);

        // Add buttons to the list for easy iteration
        boardList.add(a1);
        boardList.add(a2);
        boardList.add(a3);
        boardList.add(b1);
        boardList.add(b2);
        boardList.add(b3);
        boardList.add(c1);
        boardList.add(c2);
        boardList.add(c3);

        // Get the TextView for displaying the current turn
        turnTV = findViewById(R.id.turnTV);
    }

    // Callback method when a Tic-Tac-Toe board button is tapped
    public void boardTapped(View view) {
        // Check if the tapped view is a Button
        if (!(view instanceof Button)) return;

        // Handle the button tap
        addToBoard((Button) view);

        // Check for victory or a draw
        if (isOver()) return;
        boardToArray();
        predict();
    }

    private boolean isOver(){
        if (checkForVictory(CROSS)) {
            crossesScore++;
            result("Crosses Win!");
            return true;
        }

        if (fullBoard()) {
            result("Draw");
            return true;
        }

        if (checkForVictory(NOUGHT)) {
            noughtsScore++;
            result("Noughts Win!");
            return true;
        }

        return false;
    }

    private int turn_monitor(){
        if(currentTurn == Turn.CROSS)
            return 0;

        else
            return 1;
    }

    private void predict(){
        Retrofit r = RetrofitData.getInstance();
        DataAPI dataAPI = r.create(DataAPI.class);
        boardToArray();
        try {
            Call<ResponseBody> call = dataAPI.getPredict(board[0], board[1], board[2], board[3], board[4], board[5], board[6], board[7], board[8], turn_monitor());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful())
                        return;
                    try {
                        nextIndexBoard = Integer.parseInt(response.body().string());
                        addToBoard(boardList.get(nextIndexBoard));
                        // Check for victory or a draw
                        boolean a = isOver();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "pues esto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(this, "pues no c", Toast.LENGTH_SHORT).show();
        }
    }
    // Check for victory based on the current player's symbol
    private boolean checkForVictory(String s) {
        // Horizontal Victory
        if (match(a1, s) && match(a2, s) && match(a3, s))
            return true;
        if (match(b1, s) && match(b2, s) && match(b3, s))
            return true;
        if (match(c1, s) && match(c2, s) && match(c3, s))
            return true;

        // Vertical Victory
        if (match(a1, s) && match(b1, s) && match(c1, s))
            return true;
        if (match(a2, s) && match(b2, s) && match(c2, s))
            return true;
        if (match(a3, s) && match(b3, s) && match(c3, s))
            return true;

        // Diagonal Victory
        if (match(a1, s) && match(b2, s) && match(c3, s))
            return true;
        return match(a3, s) && match(b2, s) && match(c1, s);
    }

    // Check if a button matches the given symbol
    private boolean match(Button button, String symbol) {
        return button.getText().toString().equals(symbol);
    }

    // Display the result of the game in an AlertDialog
    private void result(String title) {
        String message = "\nNoughts " + noughtsScore + "\n\nCrosses " + crossesScore;
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Reset", (dialog, which) -> resetBoard())
                .setCancelable(false)
                .show();
    }

    // Reset the Tic-Tac-Toe board for a new game
    private void resetBoard() {
        for (Button button : boardList) {
            button.setText("");
        }

        // Toggle the first turn between NOUGHT and CROSS
        if (firstTurn == Turn.NOUGHT)
            firstTurn = Turn.CROSS;
        else if (firstTurn == Turn.CROSS)
            firstTurn = Turn.NOUGHT;

        // Set the current turn to the first turn
        currentTurn = firstTurn;

        // Update the turn label
        setTurnLabel();
    }

    // Check if the Tic-Tac-Toe board is full
    private boolean fullBoard() {
        for (Button button : boardList) {
            if (button.getText().toString().equals("")) {
                return false;
            }
        }
        return true;
    }

    // Handle adding a move to the Tic-Tac-Toe board
    private void addToBoard(Button button) {
        // Check if the button is already filled
        if (!button.getText().toString().equals("")) return;

        // Set the symbol based on the current player's turn
        if (currentTurn == Turn.NOUGHT) {
            button.setText(NOUGHT);
            currentTurn = Turn.CROSS;
        } else if (currentTurn == Turn.CROSS) {
            button.setText(CROSS);
            currentTurn = Turn.NOUGHT;
        }

        // Set text color for better visibility
        button.setTextColor(getResources().getColor(R.color.md_theme_dark_onTertiaryContainer));

        // Update the turn label
        setTurnLabel();
    }

    // Update the turn label based on the current player's turn
    private void setTurnLabel() {
        String turnText = "";
        if (currentTurn == Turn.CROSS)
            turnText = "Turn " + CROSS;
        else if (currentTurn == Turn.NOUGHT)
            turnText = "Turn " + NOUGHT;

        turnTV.setText(turnText);
    }

    // Constants for player symbols
    public static final String NOUGHT = "O";
    public static final String CROSS = "X";

    private void boardToArray() {
        for (int i = 0; i < 9; i++) {
            String buttonText = boardList.get(i).getText().toString();
            if (!buttonText.isEmpty()) {
                if(buttonText.equals(NOUGHT)) board[i] = 1;
                else board[i] = 0;
            } else board[i] = 2;
        }
    }

}
