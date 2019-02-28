package com.example.paul.mycalculator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.buttons.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private var numbers: Array<String> = arrayOf("","")
    private var operator: String = ""
    private var expression = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult.text = "0"
    }

    fun buttonClick(view: View){

        when (view){
            tvOne -> addDigitOrDecimalPoint("1")
            tvTwo -> addDigitOrDecimalPoint("2")
            tvThree -> addDigitOrDecimalPoint("3")
            tvFour -> addDigitOrDecimalPoint("4")
            tvFive -> addDigitOrDecimalPoint("5")
            tvSix -> addDigitOrDecimalPoint("6")
            tvSeven -> addDigitOrDecimalPoint("7")
            tvEight -> addDigitOrDecimalPoint("8")
            tvNine -> addDigitOrDecimalPoint("9")
            tvZero -> addDigitOrDecimalPoint("0")
            tvDot -> addDigitOrDecimalPoint(".")
            tvMinus -> {
                calculate()
                operator = "-"
                updateCalculatorText("-")
            }
            tvPlus -> {
                calculate()
                operator = "+"
                updateCalculatorText("+")
            }
            tvDivide -> {
                calculate()
                operator = "÷"
                updateCalculatorText("÷")
            }
            tvMultiply -> {
                calculate()
                operator = "×"
                updateCalculatorText("×")
            }
            tvPercent -> {
                calculate()
                operator = "%"
                updateCalculatorText("%")
            }
            tvEquals -> {
                calculate()
            }
            tvPlusMinus -> {
                rotateMinus("±")
            }
            tvBackSpace -> {
                backSpace()
            }
            tvClear -> {
                clean()
            }
        }
    }

    private fun calculate(){

        if (operator == "") return
        if (numbers[1] != ""){
            var result = 0F
            var first: Float = numbers[0].toFloat()
            var second: Float = numbers[1].toFloat()

            when (operator){
                "÷" -> {
                    if (second == 0.toFloat()){
                        Toast.makeText(this, R.string.divideByZero, Toast.LENGTH_LONG).show()
                        clean()
                    }
                    result = first / second
                }
                "×" -> result = first * second
                "-" -> result = first - second
                "+" -> result = first + second
                "%" -> result = (second / 100) * first
            }

            operator = ""
            numbers[1] = ""

            var temp: Long = result.toLong()

            if (abs(result) > abs(temp)){
                numbers[0] = result.toString()
                tvResult.text = result.toString()
            }
            else if (result == temp.toFloat()){
                numbers[0] = temp.toString()
                tvResult.text = temp.toString()
            }
        }
        else return
    }

    private fun backSpace(){

        // if first number is input but others are not
        if ((numbers[0] != "") && (operator == "") && (numbers[1] == ""))
        {
            expression.delete(0, expression.length)
            numbers[0] = ""; // delete it and display tvProcessing
            updateCalculatorText("");
        }
        else if ((numbers[0] != "") && (operator != "") && (numbers[1] == ""))
        {
            expression.delete(0, expression.length)
            operator = "";
            updateCalculatorText(numbers[0]);
        }
        else if ((numbers[0] != "") && (operator != "") && (numbers[1] != ""))
        {
            expression.delete(0, expression.length)
            numbers[1] = "";
            updateCalculatorText(numbers[0] + operator);
        }
    }

    private fun rotateMinus (value: String){

        var index = if (operator == "") 0 else 1

        if (value == "±" && numbers[index].contentEquals("±")) return

        numbers[index] = "-" + numbers[index]

        updateCalculatorText(numbers[index])
    }

    private fun clean(){

        numbers[0] = ""
        numbers[1] = ""
        operator = ""
        expression.delete(0, expression.length)
        updateCalculatorText("")
        tvResult.text = "0"
    }

    private fun addDigitOrDecimalPoint (value: String){

        // kind of ternary operator
        var index = if (operator == "") 0 else 1

        if (value == "." && numbers[index].contentEquals(".")) return

        numbers[index] += value

        updateCalculatorText(value)
    }

    private fun updateCalculatorText (value: String){

        expression.append(value)
        tvProcessing.text = expression
    }

    override fun onSaveInstanceState(outState: Bundle?) {

        outState?.run {
            putString("numbers_0", numbers[0]) //"Processing", tvProcessing.text.toString()
            putString("numbers_1", numbers[1]) //"Result", tvResult.text.toString()
            putString("operator", operator)
            putString("expression", expression.toString())
            putString("Processing", tvProcessing.text.toString())
            putString("Result", tvResult.text.toString())
        }

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        numbers[0] = savedInstanceState?.getString("numbers_0").toString()
        numbers[1] = savedInstanceState?.getString("numbers_1").toString()
        operator = savedInstanceState?.getString("operator").toString()
        expression = StringBuilder(savedInstanceState?.getString("expression"))
        tvProcessing.text = savedInstanceState?.getString("Processing")
        tvResult.text = savedInstanceState?.getString("Result")
    }
}
