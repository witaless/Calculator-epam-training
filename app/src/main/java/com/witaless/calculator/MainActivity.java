package com.witaless.calculator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.constraint.solver.widgets.WidgetContainer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {
    private EditText mEditTextInput;
    private TextView mTextViewOutput;
    private boolean flagInputToClear=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }

    private void setupViews(){
        mEditTextInput = findViewById(R.id.editText_Input); // INPUT VIEW
        mEditTextInput.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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
        mEditTextInput.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.clear();
            }
        });
        mEditTextInput.setLongClickable(false);
        mEditTextInput.setTextIsSelectable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mEditTextInput.setShowSoftInputOnFocus(false);
        }
        else{
            mEditTextInput.setOnTouchListener(new View.OnTouchListener() {
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

        mTextViewOutput = findViewById(R.id.textView_Output);
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
                mEditTextInput.getText().clear();
                tryToCalculateExpression();
                showToast(getString(R.string.message_input_clear));
                return false;
            }
        });

        mTextViewOutput.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                copyOutPutToClipBoard();
                showToast(getString(R.string.message_copy_to_clipboard));
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_num_1:
                insertNum('1');
                break;
            case R.id.button_num_2:
                insertNum('2');
                break;
            case R.id.button_num_3:
                insertNum('3');
                break;
            case R.id.button_num_4:
                insertNum('4');
                break;
            case R.id.button_num_5:
                insertNum('5');
                break;
            case R.id.button_num_6:
                insertNum('6');
                break;
            case R.id.button_num_7:
                insertNum('7');
                break;
            case R.id.button_num_8:
                insertNum('8');
                break;
            case R.id.button_num_9:
                insertNum('9');
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
                insertOperator('÷');
                break;
            case R.id.button_multiply:
                insertOperator('×');
                break;
            case R.id.button_minus:
                insertOperator('−');
                break;
            case R.id.button_plus:
                insertOperator('+');
                break;

                default:
                    break;

        }
    }

    private void insertNum(char num){
        checkFlagInputToClear();
        int position = mEditTextInput.getSelectionStart();
        StringBuilder stringBuilder = new StringBuilder(mEditTextInput.getText().toString());
        stringBuilder.insert(position,num);
        mEditTextInput.setText(stringBuilder.toString());
        mEditTextInput.setSelection(position+1);
        tryToCalculateExpression();
    }

    private void insertZero(){
        checkFlagInputToClear();
        int position = mEditTextInput.getSelectionStart();
        StringBuilder stringBuilder = new StringBuilder(mEditTextInput.getText().toString());
        //handling senseless zero insertion
        if (position==0&&stringBuilder.length()>0&&stringBuilder.charAt(0)=='0'){
            return;
        }
        stringBuilder.insert(position,'0');
        mEditTextInput.setText(stringBuilder.toString());
        mEditTextInput.setSelection(position+1);
        tryToCalculateExpression();
    }

    private void insertDot(){
        checkFlagInputToClear();
        int position = mEditTextInput.getSelectionStart();
        if(position==0){
            return;
        }

        StringBuilder stringBuilder = new StringBuilder(mEditTextInput.getText().toString());
        char prevChar = stringBuilder.charAt(position-1);
        boolean prevCharIsOperator = prevChar == '÷' || prevChar == '×' || prevChar == '−' || prevChar == '+';
        if(prevChar=='.'){
            return;
        }
        if(prevCharIsOperator){
            return;
        }
        stringBuilder.insert(position,'.');
        mEditTextInput.setText(stringBuilder.toString());
        mEditTextInput.setSelection(position+1);
        tryToCalculateExpression();
    }

    private void insertOperator(char operator){
        checkFlagInputToClear();
        int position = mEditTextInput.getSelectionStart();
        StringBuilder stringBuilder = new StringBuilder(mEditTextInput.getText().toString());
        if(operator =='−'){
            stringBuilder.insert(position,operator);
            mEditTextInput.setText(stringBuilder.toString());
            mEditTextInput.setSelection(position+1);
            tryToCalculateExpression();
            return;
        }
        if(position==0){
            return;
        }
        char prevChar = stringBuilder.charAt(position-1);
        boolean prevCharIsOperator = prevChar == '÷' || prevChar == '×' || prevChar == '−' || prevChar == '+';
        if(prevCharIsOperator){//Replace operator,if previous symbol is operator
            stringBuilder.deleteCharAt(position - 1);
            stringBuilder.insert(position-1,operator);
            mEditTextInput.setText(stringBuilder.toString());
            mEditTextInput.setSelection(position);
        }else{
            stringBuilder.insert(position,operator);
            mEditTextInput.setText(stringBuilder.toString());
            mEditTextInput.setSelection(position+1);}

            tryToCalculateExpression();

    }

    private void deleteChar(){
        checkFlagInputToClear();
        int position = mEditTextInput.getSelectionStart();
        if(position>0) {
            StringBuilder stringBuilder = new StringBuilder(mEditTextInput.getText().toString());
            stringBuilder.deleteCharAt(position - 1);
            mEditTextInput.setText(stringBuilder.toString());
            mEditTextInput.setSelection(position - 1);
        }
        tryToCalculateExpression();
    }

    private void tryToCalculateExpression(){
        Calculator.Result result= Calculator.calculate(mEditTextInput.getText().toString());
        mTextViewOutput.setText(String.format(getString(R.string.output_format),result.getResult()));
    }
    private void equals(){
        checkFlagInputToClear();
        if(mEditTextInput.getText().toString().length()==0){
            return;
        }
        Calculator.Result result= Calculator.calculate(mEditTextInput.getText().toString());
        if(!result.isError()){
            mEditTextInput.setText(result.getResult());
            mEditTextInput.setSelection(result.getResult().length());
            mTextViewOutput.setText(String.format(getString(R.string.output_format),result.getResult()));
            flagInputToClear=true;
        }
        else {
            mTextViewOutput.setText(R.string.output_error);
            int defaultColor = mTextViewOutput.getCurrentTextColor();
            Button button_equals = findViewById(R.id.button_equals);
            button_equals.setOnClickListener(null);
            ObjectAnimator anim = ObjectAnimator.ofInt(mTextViewOutput,"textColor",getResources().getColor(R.color.colorError),defaultColor);
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

    }
    private void checkFlagInputToClear(){
        if(flagInputToClear){
            mEditTextInput.getText().clear();
            mTextViewOutput.setText(getString(R.string.default_input));
            flagInputToClear=false;
        }
    }
    private void enableEqualsListener(){
        Button button_equals = findViewById(R.id.button_equals);
        button_equals.setOnClickListener(this);
    }

    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    private void copyOutPutToClipBoard(){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("output", mTextViewOutput.getText().toString());
        clipboard.setPrimaryClip(clip);
    }
}
