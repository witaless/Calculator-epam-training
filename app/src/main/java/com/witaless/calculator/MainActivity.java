package com.witaless.calculator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final char NUM_ONE = '1';
    public static final char NUM_TWO = '2';
    public static final char NUM_THREE = '3';
    public static final char NUM_FOUR = '4';
    public static final char NUM_FIVE = '5';
    public static final char NUM_SIX = '6';
    public static final char NUM_SEVEN = '7';
    public static final char NUM_EIGHT = '8';
    public static final char NUM_NINE = '9';
    public static final char NUM_ZERO = '0';
    public static final char DOT = '.';

    private EditText editTextInput;
    private TextView textViewOutput;

    private boolean flagInputToClear = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();

    }

    private void setupViews() {
        editTextInput = findViewById(R.id.editText_Input);
        textViewOutput = findViewById(R.id.textView_Output);

        editTextInput.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
                return false;
            }

            public void onDestroyActionMode(ActionMode actionMode) {
            }
        });
        editTextInput.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.clear();
            }
        });
        editTextInput.setLongClickable(false);
        editTextInput.setTextIsSelectable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editTextInput.setShowSoftInputOnFocus(false);
        } else {
            editTextInput.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.onTouchEvent(event);
                    InputMethodManager inputMethod = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethod != null) {
                        inputMethod.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    return true;
                }
            });
        }

        Button buttonNum1 = findViewById(R.id.button_num_1);
        Button buttonNum2 = findViewById(R.id.button_num_2);
        Button buttonNum3 = findViewById(R.id.button_num_3);
        Button buttonNum4 = findViewById(R.id.button_num_4);
        Button buttonNum5 = findViewById(R.id.button_num_5);
        Button buttonNum6 = findViewById(R.id.button_num_6);
        Button buttonNum7 = findViewById(R.id.button_num_7);
        Button buttonNum8 = findViewById(R.id.button_num_8);
        Button buttonNum9 = findViewById(R.id.button_num_9);
        Button buttonNum0 = findViewById(R.id.button_num_0);
        Button buttonDot = findViewById(R.id.button_dot);
        Button buttonEquals = findViewById(R.id.button_equals);
        Button buttonDel = findViewById(R.id.button_del);
        Button buttonDivide = findViewById(R.id.button_divide);
        Button buttonMultiply = findViewById(R.id.button_multiply);
        Button buttonMinus = findViewById(R.id.button_minus);
        Button buttonPlus = findViewById(R.id.button_plus);

        buttonNum1.setOnClickListener(this);
        buttonNum2.setOnClickListener(this);
        buttonNum3.setOnClickListener(this);
        buttonNum4.setOnClickListener(this);
        buttonNum5.setOnClickListener(this);
        buttonNum6.setOnClickListener(this);
        buttonNum7.setOnClickListener(this);
        buttonNum8.setOnClickListener(this);
        buttonNum9.setOnClickListener(this);
        buttonNum0.setOnClickListener(this);
        buttonDot.setOnClickListener(this);
        buttonEquals.setOnClickListener(this);
        buttonDel.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);
        buttonMultiply.setOnClickListener(this);
        buttonMinus.setOnClickListener(this);
        buttonPlus.setOnClickListener(this);

        buttonDel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                editTextInput.getText().clear();
                tryToCalculateExpression();
                showToast(getString(R.string.message_input_clear));
                return false;
            }
        });

        textViewOutput.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                copyOutputToClipBoard();
                showToast(getString(R.string.message_copy_to_clipboard));
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_num_1:
                insertNum(NUM_ONE);
                break;

            case R.id.button_num_2:
                insertNum(NUM_TWO);
                break;

            case R.id.button_num_3:
                insertNum(NUM_THREE);
                break;

            case R.id.button_num_4:
                insertNum(NUM_FOUR);
                break;

            case R.id.button_num_5:
                insertNum(NUM_FIVE);
                break;

            case R.id.button_num_6:
                insertNum(NUM_SIX);
                break;

            case R.id.button_num_7:
                insertNum(NUM_SEVEN);
                break;

            case R.id.button_num_8:
                insertNum(NUM_EIGHT);
                break;

            case R.id.button_num_9:
                insertNum(NUM_NINE);
                break;

            case R.id.button_num_0:
                insertZero();
                break;

            case R.id.button_dot:
                insertDot();
                break;

            case R.id.button_equals:
                equals();
                break;

            case R.id.button_del:
                deleteChar();
                break;

            case R.id.button_divide:
                insertOperator(Const.CHAR_DIVIDE);
                break;

            case R.id.button_multiply:
                insertOperator(Const.CHAR_MULTIPLY);
                break;

            case R.id.button_minus:
                insertOperator(Const.CHAR_MINUS);
                break;

            case R.id.button_plus:
                insertOperator(Const.CHAR_PLUS);
                break;


            default:
                break;

        }
    }

    private void insertNum(char num) {
        checkFlagInputToClear();
        int position = editTextInput.getSelectionStart();
        StringBuilder stringBuilder = new StringBuilder(editTextInput.getText().toString());
        stringBuilder.insert(position, num);
        editTextInput.setText(stringBuilder.toString());
        editTextInput.setSelection(position + 1);
        tryToCalculateExpression();
    }

    private void insertZero() {
        checkFlagInputToClear();
        int position = editTextInput.getSelectionStart();
        StringBuilder stringBuilder = new StringBuilder(editTextInput.getText().toString());

        if (position == 0 && stringBuilder.length() > 0 && stringBuilder.charAt(0) == NUM_ZERO) {
            return;
        }

        stringBuilder.insert(position, NUM_ZERO);
        editTextInput.setText(stringBuilder.toString());
        editTextInput.setSelection(position + 1);
        tryToCalculateExpression();
    }

    private void insertDot() {
        checkFlagInputToClear();
        int position = editTextInput.getSelectionStart();

        if (position == 0) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder(editTextInput.getText().toString());
        char prevChar = stringBuilder.charAt(position - 1);
        boolean prevCharIsOperator = isCharOperator(prevChar);

        if (prevChar == DOT) {
            return;
        }

        if (prevCharIsOperator) {
            return;
        }

        stringBuilder.insert(position, DOT);
        editTextInput.setText(stringBuilder.toString());
        editTextInput.setSelection(position + 1);
        tryToCalculateExpression();
    }

    private void insertOperator(char operator) {
        checkFlagInputToClear();
        int position = editTextInput.getSelectionStart();
        StringBuilder stringBuilder = new StringBuilder(editTextInput.getText().toString());

        if (operator == Const.CHAR_MINUS) {
            stringBuilder.insert(position, operator);
            editTextInput.setText(stringBuilder.toString());
            editTextInput.setSelection(position + 1);
            tryToCalculateExpression();

            return;
        }

        if (position == 0) {
            return;
        }

        char prevChar = stringBuilder.charAt(position - 1);
        boolean prevCharIsOperator = isCharOperator(prevChar);

        if (prevCharIsOperator) {
            stringBuilder.deleteCharAt(position - 1);
            stringBuilder.insert(position - 1, operator);
            editTextInput.setText(stringBuilder.toString());
            editTextInput.setSelection(position);
        } else {
            stringBuilder.insert(position, operator);
            editTextInput.setText(stringBuilder.toString());
            editTextInput.setSelection(position + 1);
        }

        tryToCalculateExpression();

    }

    private void deleteChar() {
        checkFlagInputToClear();
        int position = editTextInput.getSelectionStart();

        if (position > 0) {
            StringBuilder stringBuilder = new StringBuilder(editTextInput.getText().toString());
            stringBuilder.deleteCharAt(position - 1);
            editTextInput.setText(stringBuilder.toString());
            editTextInput.setSelection(position - 1);
        }

        tryToCalculateExpression();
    }

    private void tryToCalculateExpression() {
        ResultModel result = Calculator.calculate(editTextInput.getText().toString());
        textViewOutput.setText(String.format(getString(R.string.output_format), result.getResult()));
    }

    private void equals() {
        checkFlagInputToClear();

        if (editTextInput.getText().toString().length() == 0) {
            return;
        }

        ResultModel result = Calculator.calculate(editTextInput.getText().toString());

        if (!result.isError()) {
            editTextInput.setText(result.getResult());
            editTextInput.setSelection(result.getResult().length());
            textViewOutput.setText(String.format(getString(R.string.output_format), result.getResult()));
            flagInputToClear = true;
        } else {
            showCalculationError();
        }
    }

    private void showCalculationError() {
        textViewOutput.setText(R.string.output_error);
        int defaultColor = textViewOutput.getCurrentTextColor();
        Button button_equals = findViewById(R.id.button_equals);

        button_equals.setOnClickListener(null);
        ObjectAnimator anim = ObjectAnimator.ofInt(textViewOutput, "textColor", getResources().getColor(R.color.colorError), defaultColor);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setDuration(800);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                enableEqualsListener();
            }
        });
        anim.start();
    }

    private void checkFlagInputToClear() {
        if (flagInputToClear) {
            editTextInput.getText().clear();
            textViewOutput.setText(getString(R.string.default_input));
            flagInputToClear = false;
        }
    }

    private void enableEqualsListener() {
        Button button_equals = findViewById(R.id.button_equals);
        button_equals.setOnClickListener(this);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void copyOutputToClipBoard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("output", textViewOutput.getText().toString());
        clipboard.setPrimaryClip(clip);
    }

    private boolean isCharOperator(char symbol){
        return symbol == Const.CHAR_DIVIDE || symbol == Const.CHAR_MULTIPLY || symbol == Const.CHAR_MINUS || symbol == Const.CHAR_PLUS;
    }
}
