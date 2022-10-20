/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameBinding
import com.example.android.navigation.GameFragmentArgs.*

class GameFragment : Fragment() {

    //val args = fromBundle(requireArguments())
    val args = 1

    data class Question(
            val text: String,
            val answers: List<String>)

    private val questions1: MutableList<Question> = mutableListOf(
        Question(text = "3x2= ?",
            answers = listOf("6", "2", "3", "0")),
        Question(text = "4x2=?",
            answers = listOf("8", "0", "1", "4")),
        Question(text = "1x0=?",
            answers = listOf("0", "1", "2", "-1")),
        Question(text = "0x0=?",
            answers = listOf("0", "1", "-1", "2")),
        Question(text = "-1x1=?",
            answers = listOf("-1", "-2", "0", "1")),
        Question(text = "4x4=?",
            answers = listOf("16", "1", "TÄh?", "4")),
        Question(text = "2x3=?",
            answers = listOf("6", "0", "1", "2")),
        Question(text = "4x3=?",
            answers = listOf("12", "0", "-1", "2")),
        Question(text = "8x9?",
            answers = listOf("72", "0", "2", "3")),
        Question(text = "3x3?",
            answers = listOf("9", "1", "-3", "3"))
    )
    private val questions2: MutableList<Question> = mutableListOf(
        Question(text = "3+2= ?",
            answers = listOf("5", "2", "3", "0")),
        Question(text = "4+2=?",
            answers = listOf("6", "0", "1", "4")),
        Question(text = "1+0=?",
            answers = listOf("1", "0", "2", "-1")),
        Question(text = "0+0=?",
            answers = listOf("0", "1", "-1", "2")),
        Question(text = "-1+1=?",
            answers = listOf("0", "-1", "-2", "1")),
        Question(text = "4+4=?",
            answers = listOf("8", "1", "TÄh?", "4")),
        Question(text = "2+3=?",
            answers = listOf("5", "0", "1", "2")),
        Question(text = "4+3=?",
            answers = listOf("7", "0", "-1", "2")),
        Question(text = "8+9?",
            answers = listOf("17", "0", "2", "3")),
        Question(text = "3+3?",
            answers = listOf("6", "1", "-3", "3"))
    )
    private val questions3: MutableList<Question> = mutableListOf(
        Question(text = "3-2= ?",
            answers = listOf("1", "2", "3", "0")),
        Question(text = "4-2=?",
            answers = listOf("2", "0", "1", "4")),
        Question(text = "1-0=?",
            answers = listOf("1", "0", "2", "-1")),
        Question(text = "0-0=?",
            answers = listOf("0", "1", "-1", "2")),
        Question(text = "-1-1=?",
            answers = listOf("-2", "-1", "0", "1")),
        Question(text = "4-4=?",
            answers = listOf("0", "1", "TÄh?", "4")),
        Question(text = "2-3=?",
            answers = listOf("-1", "0", "1", "2")),
        Question(text = "4-3=?",
            answers = listOf("1", "0", "-1", "2")),
        Question(text = "8-9?",
            answers = listOf("-1", "0", "2", "3")),
        Question(text = "3-3?",
            answers = listOf("0", "1", "-3", "3"))
    )
    private val questions4: MutableList<Question> = mutableListOf(
        Question(text = "What is Android Jetpack?",
            answers = listOf("all of these", "tools", "documentation", "libraries")),
        Question(text = "Base class for Layout?",
            answers = listOf("ViewGroup", "ViewSet", "ViewCollection", "ViewRoot")),
        Question(text = "Layout for complex Screens?",
            answers = listOf("ConstraintLayout", "GridLayout", "LinearLayout", "FrameLayout")),
        Question(text = "Pushing structured data into a Layout?",
            answers = listOf("Data Binding", "Data Pushing", "Set Text", "OnClick")),
        Question(text = "Inflate layout in fragments?",
            answers = listOf("onCreateView", "onViewCreated", "onCreateLayout", "onInflateLayout")),
        Question(text = "Build system for Android?",
            answers = listOf("Gradle", "Graddle", "Grodle", "Groyle")),
        Question(text = "Android vector format?",
            answers = listOf("VectorDrawable", "AndroidVectorDrawable", "DrawableVector", "AndroidVector")),
        Question(text = "Android Navigation Component?",
            answers = listOf("NavController", "NavCentral", "NavMaster", "NavSwitcher")),
        Question(text = "Registers app with launcher?",
            answers = listOf("intent-filter", "app-registry", "launcher-registry", "app-launcher")),
        Question(text = "Mark a layout for Data Binding?",
            answers = listOf("<layout>", "<binding>", "<data-binding>", "<dbinding>"))
    )

    private val questionsVal = arrayListOf(questions1, questions2, questions3, questions4)

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = Math.min((questionsVal[args].size + 1) / 2, 3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
                inflater, R.layout.fragment_game, container, false)

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < numQuestions) {
                        currentQuestion = questionsVal[args][questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        // We've won!  Navigate to the gameWonFragment.
                        view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameWonFragment(numQuestions,questionIndex,args))
                    }
                } else {
                    // Game over! A wrong answer sends us to the gameOverFragment.
                    view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment(args))
                }
            }
        }
        return binding.root
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questionsVal[args].shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion =  questionsVal[args][questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }
}
