package com.zfginc.geoquize

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlin.math.roundToInt

private const val EXTRA_ANSWERS = "com.zfginc.geoquize.answers"

class AllAnswers : AppCompatActivity() {

    private lateinit var mainLinearLayout: LinearLayout;
    private lateinit var procents: TextView;
    private lateinit var restart_button: Button;

    private var answers: BooleanArray? = BooleanArray(8);

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_answers)

        answers = intent.getBooleanArrayExtra(EXTRA_ANSWERS);

        quizViewModel.setAnswers(answers);

        mainLinearLayout = findViewById(R.id.all_answers);
        procents = findViewById(R.id.procent_answers_title);
        restart_button = findViewById(R.id.restart_button);

        restart_button.setOnClickListener(){
            val intent = Intent(this@AllAnswers, MainActivity::class.java);
            startActivity(intent);
        }

        quizViewModel.currentIndex = 0;
        for (i in 0..7){
            addQuestion();
        }
        addStatistic();

        checkAndShowAnswered();
    }

    companion object {
        fun newIntent(packageContext: Context, answers: BooleanArray?): Intent {
            return Intent(packageContext, AllAnswers::class.java).apply {
                putExtra(EXTRA_ANSWERS, answers)
            }
        }
    }

    private fun addQuestion(){
        val layout = LinearLayout(this)
        layout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.orientation = LinearLayout.HORIZONTAL;

        val text_quest = TextView (this);
        text_quest.setText(quizViewModel.currentQuestionText);
        text_quest.setWidth(800);

        val text_answer = TextView (this);
        if(quizViewModel.currentQuestionAnswered == true){

            if(quizViewModel.isCheater) text_answer.setText("Верно (читер)");
            else  text_answer.setText("Верно");

            if(quizViewModel.currentQuestionAnswer){
                text_answer.setTextColor(Color.GREEN);
            }
            else{
                text_answer.setTextColor(Color.RED);
            }
        }
        else{

            if(quizViewModel.isCheater) text_answer.setText("Не верно (читер)");
            else  text_answer.setText("Не верно");

            if(quizViewModel.currentQuestionAnswer){
                text_answer.setTextColor(Color.RED);
            }
            else{
                text_answer.setTextColor(Color.GREEN);
            }
        }

        layout.addView(text_quest);
        layout.addView(text_answer)

        mainLinearLayout.addView(layout);

        var space = Space(this);
        space.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 10);

        mainLinearLayout.addView(space);

        quizViewModel.moveToNext()
    }
    private fun addStatistic(){
        val title = LinearLayout(this)
        title.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        title.orientation = LinearLayout.HORIZONTAL;
        val otstup_quest = TextView (this);
        title.addView(otstup_quest);
        mainLinearLayout.addView(title);

        val layout = LinearLayout(this)
        layout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.orientation = LinearLayout.HORIZONTAL;

        val text_cheat = TextView (this);
        text_cheat.setText("Количество ответов с читом");
        text_cheat.setWidth(800);

        val text_answer = TextView (this);
        text_answer.setText(quizViewModel.countCheatAnswered().toString());

        layout.addView(text_cheat);
        layout.addView(text_answer)

        mainLinearLayout.addView(layout);

        var space = Space(this);
        space.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 10);

        mainLinearLayout.addView(space);

        quizViewModel.moveToNext()
    }

    private fun checkAndShowAnswered(){
        if(quizViewModel.isAllAnswered()) {
            var count : Int = quizViewModel.countTrueAnswered();
            var max : Int = quizViewModel.countQuestions;
            var procent: Double = ((count.toDouble() / max) * 10000).roundToInt() / 100.0;
            var output: String = "Вы ответили верно: " + count + "/" + max + "  -  " + procent + "%";

            procents.setText(output);
        }
    }

}