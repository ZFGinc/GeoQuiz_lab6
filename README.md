<p align = "center">МИНИСТЕРСТВО НАУКИ И ВЫСШЕГО ОБРАЗОВАНИЯ
РОССИЙСКОЙ ФЕДЕРАЦИИ
ФЕДЕРАЛЬНОЕ ГОСУДАРСТВЕННОЕ БЮДЖЕТНОЕ
ОБРАЗОВАТЕЛЬНОЕ УЧРЕЖДЕНИЕ ВЫСШЕГО ОБРАЗОВАНИЯ
«САХАЛИНСКИЙ ГОСУДАРСТВЕННЫЙ УНИВЕРСИТЕТ»</p>
<br><br><br><br><br><br>
<p align = "center">Институт естественных наук и техносферной безопасности<br>Кафедра информатики<br>Хроменков Владимир Александрович</p>
<br><br><br>

<p align = "center">Лабораторная работа №5<br>«Вторая activity»<br>01.03.02 Прикладная математика и информатика</p>

<p align = "center">Лабораторная работа №4<br>«Отладка Android-приложений»<br>01.03.02 Прикладная математика и информатика</p>
<br><br><br><br><br><br><br><br><br><br><br><br>
<p align = "right">Научный руководитель<br>
Соболев Евгений Игоревич</p>
<br><br><br>
<p align = "center">г. Южно-Сахалинск<br>2023 г.</p>

***
# <p align = "center">Оглавление</p>
- [Цели и задачи](#цели-и-задачи)
- [Решение задач](#решение-задач)
    - [Упражнение. Лазейка для читера](#cheat)
    - [Упражнение. Отслеживание читов по вопросу](#find_cheat)
    - [CodeWars](#codewars)
- [Вывод](#вывод)

***

# <p align = "center">Цели и задачи</p>

1.  Упражнение. Лазейка для читера
Мошенники никогда не выигрывают... Если, конечно, им не удастся обойти вашу защиту от мошенничества. А скорее всего, они так и сделают — именно потому, что они мошенники. У GeoQuiz есть кое-какая лазейка. Пользователи могут вращать CheatActivity после чита, чтобы удалить следы обмана. После возврата к MainActivity их жульничество будет забыто. Исправьте эту ошибку, сохраняя состояние пользовательского интерфейса CheatActivity во время вращения и после уничтожения процесса.
 
2.	Упражнение. Отслеживание читов по вопросу
В настоящее время, когда пользователь читерит на одном вопросе, он считается читером по всем вопросам. Обновите GeoQuiz, чтобы отслеживать, сколько раз пользователь нарушал закон. Когда пользователь использует чит для ответа на заданный вопрос, осуждайте его всякий раз, когда он пытается ответить на этот вопрос. Когда пользователь отвечает на вопрос, с которым он не жульничал, покажите правильный или неправильный ответ.

3. CodeWars
 - [Simple multiplication](https://www.codewars.com/kata/583710ccaa6717322c000105)
 - [Remove String Spaces](https://www.codewars.com/kata/57eae20f5500ad98e50002c5)
 - [String repeat](https://www.codewars.com/kata/57a0e5c372292dd76d000d7e)
 - [Function 1 - hello world](https://www.codewars.com/kata/523b4ff7adca849afe000035)
 - [Square(n) Sum](https://www.codewars.com/kata/515e271a311df0350d00000f)
 - [Century From Year](https://www.codewars.com/kata/5a3fe3dde1ce0e8ed6000097)
 - [Is n divisible by x and y?](https://www.codewars.com/kata/5545f109004975ea66000086)
 - [Even or Odd](https://www.codewars.com/kata/53da3dbb4a5168369a0000fe)


***

# <p align = "center">Решение задач</p>

## <p align = "center">Упражнение. Лазейка для читера</p>

Первоначально я добавил новую кнопку ImageButton в правый верхний угол для использования её в качестве перехода на Activity с подсказкой.

<p align = "center">
    <img src = "images/1-1.png">
    <br>
    <img src = "images/1-2.png">
</p>

Для перехода между Activity я использоваал `Intent` 

Код для перехода к другой Activity:
```kotlin
(MainActivity.kt)
cheat_button.setOnClickListener(){
    val intent = CheatActivity.newIntent(this@MainActivity, quizViewModel.currentQuestionAnswer)
    startActivityForResult(intent, REQUEST_CODE_CHEAT)
}
```

Код самой активити:
```kotlin
(CheatActivity.kt)
package com.zfginc.geoquize

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"

class CheatActivity : AppCompatActivity() {

    private lateinit var back_button: Button
    private lateinit var cheat_button: Button
    private lateinit var question_answer: TextView

    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        back_button = findViewById(R.id.back_button)
        cheat_button = findViewById(R.id.cheat_button)
        question_answer = findViewById(R.id.question_answer)

        back_button.setOnClickListener {
            finish();
        }
        cheat_button.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            question_answer.setText(answerText)
            setAnswerShownResult(true)
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}
```

Работает оно следующим образом:
1. Заморозить главную Activity .
2. Поверх главной Activity отрисовать новую.
3. Дать доступ для работы новой Activity.
4. После завершения работы с новой Activity мы её отключаем и сборщик мусора её забирает в небытие.
5. Разморозить главную Activity.

Таким образом мы не теряем данные с нашей главной Activity и при этом с помощью `startActivityForResult` мы можем получить данные с новой Activity и обработать их в главной.

```kotlin
override fun onActivityResult(requestCode: Int,
                              resultCode: Int,
                              data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode != Activity.RESULT_OK) {
        return
    }
    quizViewModel.setCurrentQuestionIsCheat()
}
```

Для этого я доработал классы `Question.kt` и `QuizViewModel.kt`:
```kotlin
data class Question(@StringRes val textResId: Int, val answer : Boolean) {
    var isAnswered: Boolean? = null;
    var isCheating: Boolean? = false; //Это новое
}
```

```kotlin
class QuizViewModel: ViewModel() {
    ...

    //Это новое
    val isCheater: Boolean
        get() = questionBank[currentIndex].isCheating == true

    //Это новое
    fun setCurrentQuestionIsCheat(){
        questionBank[currentIndex].isCheating = true;
    }

    //Это переделано
    fun getAnswers() : BooleanArray? {
        var answers = BooleanArray(16);

        for(i in 0..7)
            answers[i] = questionBank[i].isAnswered == true;

        for(i in 8..15)
            answers[i] = questionBank[i-8].isCheating == true;

        return answers;
    }

    //Это переделано
    fun setAnswers(answers : BooleanArray?) {
        for(i in 0..7)
            questionBank[i].isAnswered = answers?.get(i);
        for(i in 8..15)
            questionBank[i-8].isCheating = answers?.get(i);
    }

    ...
}
```

В последнем отрывке кода есть проблема в прямой зависимости циклов от количества вопросов. Это я потом переделаю!

Таким образом получается достичь работы псевдо-чита для игры.

<p align = "center">
    <img src = "images/1-3.png">
    <br>
    <img src = "images/1-4.png">
</p>

## <p align = "center">Упражнение. Отслеживание читов по вопросу</p>

Первым делом я сделал оповещение, что данный вопрос был отвечен после просмотра правильного ответа и не в зависимости от того какой был дан ответ, игра будет осуждать игрока за то, что подсматривал. Это можно увидеть на предыдущем скриншоте.

Далее была переработана финальная Activity для просмора статистики.
По большей части, именно для этого я и модифицировал класс `QuizViewModel.kt`.

Были введены изменения для класса `AllAnswers.kt`:
Теперь добавляется пометка к ответам там, где была использована подсказка
```kotlin
if(quizViewModel.currentQuestionAnswered == true){

+++         if(quizViewModel.isCheater) text_answer.setText("Верно (читер)");
+++         else  text_answer.setText("Верно");

            if(quizViewModel.currentQuestionAnswer){
                text_answer.setTextColor(Color.GREEN);
            }
            else{
                text_answer.setTextColor(Color.RED);
            }
        }
        else{

+++         if(quizViewModel.isCheater) text_answer.setText("Не верно (читер)");
+++         else  text_answer.setText("Не верно");

            if(quizViewModel.currentQuestionAnswer){
                text_answer.setTextColor(Color.RED);
            }
            else{
                text_answer.setTextColor(Color.GREEN);
            }
        }
```

<p align = "center">
    <img src = "images/2-2.png">
</p>

Так же добавил новый метод для отображения общего числа использования подсказок:

```kotlin
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

    qsuizViewModel.moveToNext()
}
```

<p align = "center">
    <img src = "images/2-3.png">
</p>

В итоге получилось так:

<p align = "center">
    <img src = "images/2-1.png">
</p>

***


## <p align = "center">CodeWars</p>

### [Simple multiplication](https://www.codewars.com/kata/583710ccaa6717322c000105)
### [Remove String Spaces](https://www.codewars.com/kata/57eae20f5500ad98e50002c5)
### [String repeat](https://www.codewars.com/kata/57a0e5c372292dd76d000d7e)
### [Function 1 - hello world](https://www.codewars.com/kata/523b4ff7adca849afe000035)
### [Square(n) Sum](https://www.codewars.com/kata/515e271a311df0350d00000f)
### [Century From Year](https://www.codewars.com/kata/5a3fe3dde1ce0e8ed6000097)
### [Is n divisible by x and y?](https://www.codewars.com/kata/5545f109004975ea66000086)
### [Even or Odd](https://www.codewars.com/kata/53da3dbb4a5168369a0000fe)

# <p align = "center">Вывод</p>

Выполнив *лабораторную работу №5*, совершенствую навыки работы со средой разработки `Android Studion` и работы с языком `Kotlin`. 