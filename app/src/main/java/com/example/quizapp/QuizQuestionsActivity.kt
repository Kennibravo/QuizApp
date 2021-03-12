package com.example.quizapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.quizapp.databinding.ActivityQuizQuestionsBinding

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityQuizQuestionsBinding

    private var myCurrentPosition: Int = 0
    private var myQuestionsList: ArrayList<Question>? = null
    private var mySelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizQuestionsBinding.inflate(layoutInflater)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        setContentView(binding.root)

        myQuestionsList = Constants.getQuestions()
        myCurrentPosition = 1

        setQuestion()

        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.tvOptionFour.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

    }

    @SuppressLint("SetTextI18n")
    private fun setQuestion(){
        val question = myQuestionsList!![myCurrentPosition - 1]

        this.defaultOptionView()

        if(myCurrentPosition == myQuestionsList!!.size){
            binding.btnSubmit.text = "FINISH"
        }else{
            binding.btnSubmit.text = "GOTO NEXT QUESTION"
        }
        binding.progressBar.progress = myCurrentPosition
        binding.tvProgress.text = "$myCurrentPosition" + "/" + binding.progressBar.max

        binding.tvQuestion.text = question!!.question
        binding.ivImage.setImageResource(question!!.image)
        binding.tvOptionOne.text = question!!.optionOne
        binding.tvOptionTwo.text = question!!.optionTwo
        binding.tvOptionThree.text = question!!.optionThree
        binding.tvOptionFour.text = question!!.optionFour
    }

    private fun defaultOptionView(){
        val options = ArrayList<TextView>()
        options.add(0, binding.tvOptionOne)
        options.add(1, binding.tvOptionTwo)
        options.add(2, binding.tvOptionThree)
        options.add(3, binding.tvOptionFour)

        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_option_one -> selectedOptionView(v as TextView, 1)
            R.id.tv_option_two -> selectedOptionView(v as TextView, 2)
            R.id.tv_option_three -> selectedOptionView(v as TextView, 3)
            R.id.tv_option_four -> selectedOptionView(v as TextView, 4)
            R.id.btn_submit -> {
                if(mySelectedOptionPosition == 0){
                    myCurrentPosition++

                    when{
                        myCurrentPosition <= myQuestionsList!!.size -> {
                            Log.i("test here", "$myCurrentPosition")
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTION, myQuestionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                }else {
                    val question = myQuestionsList?.get(myCurrentPosition - 1)

                    if(question!!.correctAnswer != mySelectedOptionPosition){
                        answerView(mySelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }else{
                        mCorrectAnswers++
                    }

                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if(myCurrentPosition == myQuestionsList!!.size){
                        binding.btnSubmit.text = "Submit"
                    }else{
                        binding.btnSubmit.text = "GOTO NEXT QUESTION"
                    }

                    mySelectedOptionPosition = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int){
        when(answer){
            1 -> {
                binding.tvOptionOne.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            2 -> {
                binding.tvOptionTwo.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            3 -> {
                binding.tvOptionThree.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            4 -> {
                binding.tvOptionFour.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
        }
    }
    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int){
        this.defaultOptionView()

        mySelectedOptionPosition = selectedOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )

    }
}